package com.nent.techtest.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {


    final private String DISCOGS = "https://api.discogs.com/artists/334495";
    final private String DESCRIPTION = "Rock band from Aberdeen, Washington, USA Nirvana formed in 1987. Considered by many...";
    final private String COVER_ART = "{\n" +
            "  \"name\": \"Nirvana\",\n" +
            "  \"id\": 125246,\n" +
            "  \"resource_url\": \"https://api.discogs.com/artists/125246\",\n" +
            "  \"uri\": \"https://www.discogs.com/artist/125246-Nirvana\",\n" +
            "  \"releases_url\": \"https://api.discogs.com/artists/125246/releases\",\n" +
            "  \"images\": [\n" +
            "    {\n" +
            "      \"type\": \"secondary\",\n" +
            "      \"uri\": \"\",\n" +
            "      \"resource_url\": \"\",\n" +
            "      \"uri150\": \"\",\n" +
            "      \"width\": 600,\n" +
            "      \"height\": 378\n" +
            "    }\n" +
            "  ],\n" +
            "  \"profile\": \"Rock band from Aberdeen, Washington, USA Nirvana formed in 1987. Considered by many...\",\n" +
            "  \"data_quality\": \"Needs Vote\"\n" +
            "}";

    final private String COVER_ART_2 = "{\n" +
            "  \"name\": \"Nirvana\",\n" +
            "  \"id\": 125246,\n" +
            "  \"images\": [\n" +
            "    {\n" +
            "      \"type\": \"secondary\",\n" +
            "      \"height\": 378\n" +
            "    }\n" +
            "  ],\n" +
            "  \"data_quality\": \"Needs Vote\"\n" +
            "}";

    @InjectMocks
    ArtistService service;
    @Mock
    JSONArray jsonArray;
    @Mock
    JSONObject subObject;
    @Mock
    JSONObject object;
    @Mock
    ResponseEntity<String> response;
    @Mock
    RestTemplate restTemplate;


     @Test
    void artistDescriptionAvailable() throws JSONException {

        when(jsonArray.length()).thenReturn(1);
        when(jsonArray.getJSONObject(0)).thenReturn(subObject);
        when(subObject.getString("type")).thenReturn("discogs");
        when(subObject.getJSONObject("url")).thenReturn(object);
        when(object.getString("resource")).thenReturn(DISCOGS);

        when(restTemplate.getForEntity(DISCOGS, String.class)).thenReturn(response);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getBody()).thenReturn(COVER_ART);

        String description = service.getDescription(jsonArray);

        assertEquals(DESCRIPTION, description);
    }

    @Test
    void artistDescriptionMissing() throws JSONException {

        when(jsonArray.length()).thenReturn(1);
        when(jsonArray.getJSONObject(0)).thenReturn(subObject);
        when(subObject.getString("type")).thenReturn("discogs");
        when(subObject.getString("type")).thenReturn("discogs");
        when(subObject.getJSONObject("url")).thenReturn(object);
        when(object.getString("resource")).thenReturn(DISCOGS);

        when(restTemplate.getForEntity(DISCOGS, String.class)).thenReturn(response);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getBody()).thenReturn(COVER_ART_2);

        String description = service.getDescription(jsonArray);

        assertEquals("", description);
    }

}