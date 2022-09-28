package org.w3c.css.sac;

import java.io.InputStream;
import java.io.Reader;

public class InputSource {
   private String uri;
   private InputStream byteStream;
   private String encoding;
   private Reader characterStream;
   private String title;
   private String media;

   public InputSource() {
   }

   public InputSource(String var1) {
      this.setURI(var1);
   }

   public InputSource(Reader var1) {
      this.setCharacterStream(var1);
   }

   public void setURI(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public void setByteStream(InputStream var1) {
      this.byteStream = var1;
   }

   public InputStream getByteStream() {
      return this.byteStream;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setCharacterStream(Reader var1) {
      this.characterStream = var1;
   }

   public Reader getCharacterStream() {
      return this.characterStream;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setMedia(String var1) {
      this.media = var1;
   }

   public String getMedia() {
      return this.media == null ? "all" : this.media;
   }
}
