package org.apache.fop.area.inline;

import org.apache.fop.area.Area;
import org.w3c.dom.Document;

public class ForeignObject extends Area {
   private static final long serialVersionUID = -214947698798577885L;
   private Document doc;
   private String namespace;

   public ForeignObject(Document d, String ns) {
      this.doc = d;
      this.namespace = ns;
   }

   public ForeignObject(String ns) {
      this.namespace = ns;
   }

   public void setDocument(Document document) {
      this.doc = document;
   }

   public Document getDocument() {
      return this.doc;
   }

   public String getNameSpace() {
      return this.namespace;
   }
}
