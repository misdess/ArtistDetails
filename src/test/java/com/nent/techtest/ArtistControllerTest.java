package com.nent.techtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nent.techtest.model.MessageCodes;
import com.nent.techtest.services.ArtistService;
import com.nent.techtest.services.RequestValidator;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    final private static String WRONG_ID = "5b11f4ce-a62d-471e-81fc-a69a8278c7dt38";
    final private static String CORRECT_ID = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";

    final private static String INVALID_REQUEST = "Request not valid";

    final private String ARTIST_URL = "http://musicbrainz.org/ws/2/artist/";
    final private String COVER_ART_URL = "http://coverartarchive.org/release-group/";


    final private static String ARTIST_DETAIL = "{\n" +
            "  \"isnis\": [\n" +
            "    \"0000000123486830\",\n" +
            "    \"0000000123487390\"\n" +
            "  ],\n" +
            "  \"end_area\": null,\n" +
            "  \"begin_area\": {\n" +
            "    \"sort-name\": \"Aberdeen\",\n" +
            "    \"id\": \"a640b45c-c173-49b1-8030-973603e895b5\",\n" +
            "    \"type-id\": null,\n" +
            "    \"disambiguation\": \"\",\n" +
            "    \"name\": \"Aberdeen\",\n" +
            "    \"type\": null\n" +
            "  },\n" +
            "  \"life-span\": {\n" +
            "    \"ended\": true,\n" +
            "    \"end\": \"1994-04-05\",\n" +
            "    \"begin\": \"1988-01\"\n" +
            "  },\n" +
            "  \"release-groups\": [\n" +
            "    {\n" +
            "      \"primary-type\": \"Album\",\n" +
            "      \"primary-type-id\": \"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\n" +
            "      \"secondary-types\": [],\n" +
            "      \"id\": \"2a0981fb-9593-3019-864b-ce934d97a16e\",\n" +
            "      \"secondary-type-ids\": [],\n" +
            "      \"first-release-date\": \"1993-09-21\",\n" +
            "      \"disambiguation\": \"\",\n" +
            "      \"title\": \"In Utero\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"country\": \"US\",\n" +
            "  \"sort-name\": \"Nirvana\",\n" +
            "  \"type\": \"Group\",\n" +
            "  \"ipis\": [],\n" +
            "  \"area\": {\n" +
            "    \"name\": \"United States\",\n" +
            "    \"type\": null,\n" +
            "    \"disambiguation\": \"\",\n" +
            "    \"sort-name\": \"United States\",\n" +
            "    \"iso-3166-1-codes\": [\n" +
            "      \"US\"\n" +
            "    ],\n" +
            "    \"id\": \"489ce91b-6658-3307-9877-795b68554c98\",\n" +
            "    \"type-id\": null\n" +
            "  },\n" +
            "  \"end-area\": null,\n" +
            "  \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
            "  \"gender\": null,\n" +
            "  \"begin-area\": {\n" +
            "    \"sort-name\": \"Aberdeen\",\n" +
            "    \"id\": \"a640b45c-c173-49b1-8030-973603e895b5\",\n" +
            "    \"type-id\": null,\n" +
            "    \"disambiguation\": \"\",\n" +
            "    \"name\": \"Aberdeen\",\n" +
            "    \"type\": null\n" +
            "  },\n" +
            "  \"gender-id\": null,\n" +
            "  \"relations\": [\n" +
            "    {\n" +
            "      \"end\": null,\n" +
            "      \"ended\": false,\n" +
            "      \"url\": {\n" +
            "        \"id\": \"4a425cd3-641d-409c-a282-2334935bf1bd\",\n" +
            "        \"resource\": \"https://www.allmusic.com/artist/mn0000357406\"\n" +
            "      },\n" +
            "      \"attribute-ids\": {},\n" +
            "      \"target-credit\": \"\",\n" +
            "      \"source-credit\": \"\",\n" +
            "      \"type-id\": \"6b3e3c85-0002-4f34-aca6-80ace0d7e846\",\n" +
            "      \"type\": \"allmusic\",\n" +
            "      \"attributes\": [],\n" +
            "      \"begin\": null,\n" +
            "      \"attribute-values\": {},\n" +
            "      \"direction\": \"forward\",\n" +
            "      \"target-type\": \"url\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"attributes\": [],\n" +
            "      \"type\": \"discogs\",\n" +
            "      \"attribute-values\": {},\n" +
            "      \"target-type\": \"url\",\n" +
            "      \"direction\": \"forward\",\n" +
            "      \"begin\": null,\n" +
            "      \"url\": {\n" +
            "        \"id\": \"81846eca-af41-43d0-bcae-b62dbf5cfa2f\",\n" +
            "        \"resource\": \"https://www.discogs.com/artist/125246\"\n" +
            "      },\n" +
            "      \"end\": null,\n" +
            "      \"ended\": false,\n" +
            "      \"source-credit\": \"\",\n" +
            "      \"target-credit\": \"\",\n" +
            "      \"type-id\": \"04a5b104-a4c2-4bac-99a1-7b837c37d9e4\",\n" +
            "      \"attribute-ids\": {}\n" +
            "    }\n" +
            "  ],\n" +
            "  \"id\": \"5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\n" +
            "  \"name\": \"Nirvana\",\n" +
            "  \"disambiguation\": \"90s US grunge band\"\n" +
            "}";

    final private static String COVER_ART = "{\n" +
            "  \"images\": [\n" +
            "    {\n" +
            "      \"approved\": true,\n" +
            "      \"back\": false,\n" +
            "      \"comment\": \"\",\n" +
            "      \"edit\": 18021941,\n" +
            "      \"front\": true,\n" +
            "      \"id\": 1247101964,\n" +
            "      \"image\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964.jpg\",\n" +
            "      \"thumbnails\": {\n" +
            "        \"250\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964-250.jpg\",\n" +
            "        \"500\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964-500.jpg\",\n" +
            "        \"1200\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964-1200.jpg\",\n" +
            "        \"large\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964-500.jpg\",\n" +
            "        \"small\": \"http://coverartarchive.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6/1247101964-250.jpg\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"release\": \"https://musicbrainz.org/release/7d166a44-cfb5-4b08-aacb-6863bbe677d6\"\n" +
            "}";

    final private static String DESCRIPTION = "{\n" +
            "  \"name\": \"Nirvana\",\n" +
            "  \"id\": 125246,\n" +
            "  \"resource_url\": \"https://api.discogs.com/artists/125246\",\n" +
            "  \"uri\": \"https://www.discogs.com/artist/125246-Nirvana\",\n" +
            "  \"releases_url\": \"https://api.discogs.com/artists/125246/releases\",\n" +
            "  \"images\": [\n" +
            "    {\n" +
            "      \"type\": \"primary\",\n" +
            "      \"uri\": \"\",\n" +
            "      \"resource_url\": \"\",\n" +
            "      \"uri150\": \"\",\n" +
            "      \"width\": 600,\n" +
            "      \"height\": 450\n" +
            "    }\n" +
            "  ],\n" +
            "  \"profile\": \"Rock band from Aberdeen, Washington, USA.\",\n" +
            "  \n" +
            "  \"data_quality\": \"Needs Vote\"\n" +
            "}";

    ArtistController artistController;
    @Mock
    protected RequestValidator validator;
    @Mock
    RestTemplate restTemplate;
    @Mock
    ResponseEntity<String> artistResponse;
    @Mock
    ResponseEntity<String> coverArtResponse;
    @Mock
    ResponseEntity<String> descriptionResponse;

    @BeforeEach
    public void setUp(){
        ArtistService service = new ArtistService(restTemplate);
        artistController = new ArtistController(service, validator);

    }

    @Test
    void testArtistDetailsNotRetrievedWhenInvalidMbid() throws JsonProcessingException, JSONException, ExecutionException, InterruptedException {

        when(validator.validateMdIdLength(anyString())).thenReturn(false);

        String json = artistController.artist(WRONG_ID);

        JSONObject object = new JSONObject(json);
        assertEquals(INVALID_REQUEST, object.getString("message"));
        assertEquals(MessageCodes.INVALID.name(), object.getString("code"));
    }

    @Test
    void testArtistNotFound() throws JsonProcessingException, JSONException, ExecutionException, InterruptedException {

        when(validator.validateMdIdLength(anyString())).thenReturn(true);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(artistResponse);
        when(artistResponse.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        String response = artistController.artist(WRONG_ID);
        JSONObject profile = new JSONObject(response);

        assertEquals(MessageCodes.NOT_FOUND.name(), profile.getString("code"));
        assertEquals("Cannot find artist in our system", profile.getString("message"));
    }


    @Test
    void testArtistDetailsRetrievedWhenValidMbid() throws JsonProcessingException, JSONException, ExecutionException, InterruptedException {

        when(validator.validateMdIdLength(anyString())).thenReturn(true);
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocation) {
                String arg = invocation.getArgument(0);

                if (arg.startsWith(ARTIST_URL)) return artistResponse;
                if (arg.startsWith(COVER_ART_URL)) return coverArtResponse;

                return descriptionResponse;
            }

        }).when(restTemplate).getForEntity(anyString(), eq(String.class));

        doReturn(ARTIST_DETAIL).when(artistResponse).getBody();
        doReturn(HttpStatus.OK).when(artistResponse).getStatusCode();

        //configure release-group details
        doReturn(COVER_ART).when(coverArtResponse).getBody();
        doReturn(HttpStatus.OK).when(coverArtResponse).getStatusCode();

        //configure release-group details
        doReturn(DESCRIPTION).when(descriptionResponse).getBody();
        doReturn(HttpStatus.OK).when(descriptionResponse).getStatusCode();

        String response = artistController.artist(CORRECT_ID);
        JSONObject profile = new JSONObject(response);

        assertEquals(1, profile.getJSONArray("albums").length());
        assertEquals(CORRECT_ID, profile.getString("mbid"));
        assertEquals("Rock band from Aberdeen, Washington, USA.", profile.getString("description"));
    }




}