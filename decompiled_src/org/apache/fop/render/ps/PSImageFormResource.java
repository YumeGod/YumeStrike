package org.apache.fop.render.ps;

import org.apache.xmlgraphics.ps.PSResource;

public class PSImageFormResource extends PSResource {
   private String uri;

   public PSImageFormResource(int id, String uri) {
      this("FOPForm:" + Integer.toString(id), uri);
   }

   public PSImageFormResource(String name, String uri) {
      super("form", name);
      this.uri = uri;
   }

   public String getImageURI() {
      return this.uri;
   }
}
