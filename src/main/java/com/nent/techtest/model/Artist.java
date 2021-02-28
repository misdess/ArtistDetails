package com.nent.techtest.model;

import java.util.Arrays;
import java.util.List;

public class Artist {

   String mbid;
   String description;
   List<Album> albums= Arrays.asList();

   public String getMbid() {
      return mbid;
   }

   public String getDescription() {
      return description;
   }

   public List<Album> getAlbums() {
      return albums;
   }

   public void setMbid(String mbid) {
      this.mbid = mbid;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setAlbums(List<Album> albums) {
      this.albums = albums;
   }


   public Artist() {
   }
   public Artist(String mbid, String description, List<Album> albums) {
      this.mbid = mbid;
      this.description = description;
      this.albums = albums;
   }

   @Override
   public String toString() {
      return "Artist{" +
              "mbid='" + mbid + '\'' +
              ", description='" + description + '\'' +
              ", albums=" + albums +
              '}';
   }
}
