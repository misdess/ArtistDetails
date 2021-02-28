#Description

A [Spring Boot](https://spring.io/projects/spring-boot) REST API for providing clients with information about a specific music
artist, collected from 3 different sources: MusicBrainz, Cover ArtArchive and Discogs.

#Installation and Running


* From within project root run: *mvn spring-boot:run*. This starts the application at port 8080.

* From browser, connect to the application *http://localhost:8080/artist?id=mbid*  where \<mbid> is [MusicBrainsz Identifier](https://musicbrainz.org/doc/MusicBrainz_Identifier).

#Response

* The application validates the request (only length of *mbid* for now), and returns *INVALID* for <mbid> length lower or greater than 36.
* For valid *mbid* the application sends requests to external API's and 
 returns a successful json message containing the artist details if an artist with the identifier exists or <NOT_FOUND> otherwise.
 
#Sample working *mbid*

* 5b11f4ce-a62d-471e-81fc-a69a8278c7da
* f27ec8db-af05-4f36-916e-3d57f91ecf5e


#Implementation Details

* The application is built by using [Spring Boot](https://spring.io/projects/spring-boot) and runs with [Jetty server](https://www.eclipse.org/jetty/)
* It uses [RestTemplate](https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html) to request external libraries.
* As the external libraries are very slow, the application keeps its own local caches of *artist*, *covert art* and *description* while it is running. 
For any request, it checks if an artist identified by \<mbid> exists in the cache otherwise sends a requests to external API's. 
                                                                                        

#Test
* One unit test class *ArtitstService.java* that test the getDescripion() method
* One Integration test class *ArtistControllerTest.java* that test the entire application covering 3 different scenarios.

