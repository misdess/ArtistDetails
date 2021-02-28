package com.nent.techtest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nent.techtest.model.Album;
import com.nent.techtest.model.Artist;
import com.nent.techtest.model.ErrorResponse;
import com.nent.techtest.model.MessageCodes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableAsync
public class ArtistService {

    final private String EMPTY_STRING = "";
    final private String ARTIST_URL = "http://musicbrainz.org/ws/2/artist/";
    final private String ARTIST_URL_OPTIONS = "?&fmt=json&inc=url-rels+release-groups";
    final private String COVER_ART_URL = "http://coverartarchive.org/release-group/";
    final private String DISCONGS_URL = "https://api.discogs.com/artists/";

    private Map<String, ResponseEntity<String>> artistCache = new HashMap<>();
    private Map<String, ResponseEntity<String>> coverArtCache = new HashMap<>();
    private Map<String, ResponseEntity<String>> discogsCache = new HashMap<>();


    private RestTemplate restTemplate;

    public ArtistService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getArtistDetails(String mdid) throws JsonProcessingException {

        ResponseEntity<String> coverArtResponse = findArtistDetails(mdid);

        if (coverArtResponse != null && coverArtResponse.getStatusCode() != HttpStatus.OK) {
            return new JSONObject(new ErrorResponse(MessageCodes.NOT_FOUND, "Cannot find artist in our system")).toString();
        }

        JSONObject object = new JSONObject(coverArtResponse.getBody());
        JSONArray releaseGroupsArray = object.getJSONArray("release-groups");
        JSONArray relationsArray = object.getJSONArray("relations");

        List<Album> albums = getAlbums(releaseGroupsArray);

        String description;
        try {
            description = getDescription(relationsArray);
        } catch (JSONException ex) {
            description = "";
        }

        Artist artist = new Artist(mdid, description, albums);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(artist);
    }

    private synchronized List<Album> getAlbums(JSONArray releaseGroupsArray){
        List<Album> albumList = new ArrayList<>();

        for (int i = 0; i < releaseGroupsArray.length(); i++) {
            String id = releaseGroupsArray.getJSONObject(i).getString("id");

            ResponseEntity<String> coverArtResponse = findCoverArtDetails(id);

            String image = "";

            if (coverArtResponse != null && HttpStatus.OK.equals(coverArtResponse.getStatusCode())) {
                image = getImage(coverArtResponse);
            }

            Album album = new Album(releaseGroupsArray.getJSONObject(i).getString("title"), id, image);
            albumList.add(album);
        }

        return albumList;
    }

    private String getImage(ResponseEntity<String> coverArtResponse) {

        JSONObject coverArtObject = new JSONObject(coverArtResponse.getBody());

        JSONArray coverArtObjectArray = coverArtObject.getJSONArray("images");

        for (int j = 0; j < coverArtObjectArray.length(); j++) {
            if (coverArtObjectArray.getJSONObject(j).getBoolean("front")) {
                return coverArtObjectArray.getJSONObject(j).getString("image");
            }
        }

        return EMPTY_STRING;
    }

    String getDescription(JSONArray relationsArray) {
        try {
            return findDescription(relationsArray);
        } catch (JSONException ex) {
            return EMPTY_STRING;
        }
    }

    String findDescription(JSONArray relationsArray) throws JSONException {

        for (int i = 0; i < relationsArray.length(); i++) {
            JSONObject subObject = relationsArray.getJSONObject(i);

            if ("discogs".equals(subObject.getString("type"))) {
                String[] values = subObject.getJSONObject("url").getString("resource").split("/");

                ResponseEntity<String> coverArtResponse = findDiscogsDetails(values[4]);

                if (coverArtResponse != null && HttpStatus.OK.equals(coverArtResponse.getStatusCode())) {

                    return new JSONObject(coverArtResponse.getBody()).getString("profile");
                }

                break;
            }
        }
        return EMPTY_STRING;
    }

    ResponseEntity<String> findDiscogsDetails(String id) {
        return accessResource(discogsCache, DISCONGS_URL.concat(id), id);
    }

    ResponseEntity<String> findCoverArtDetails(String id) {
        return accessResource(coverArtCache, COVER_ART_URL.concat(id), id);
    }

    ResponseEntity<String> findArtistDetails(String mdid) {
        return accessResource(artistCache, ARTIST_URL.concat(mdid).concat(ARTIST_URL_OPTIONS), mdid);
    }

    ResponseEntity<String> accessResource(Map<String, ResponseEntity<String>> cache, String url, String mdid) {

        ResponseEntity<String> coverArtResponse;
        if (cache.containsKey(mdid)) {
            coverArtResponse = cache.get(mdid);
        } else {
            coverArtResponse = restTemplate.getForEntity(url, String.class);
            cache.put(mdid, coverArtResponse);
        }
        return coverArtResponse;
    }

}
