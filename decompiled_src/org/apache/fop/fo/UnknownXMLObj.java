package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.xml.sax.Locator;

public class UnknownXMLObj extends XMLObj {
   private String namespace;

   protected UnknownXMLObj(FONode parent, String space) {
      super(parent);
      this.namespace = space;
   }

   public String getNamespaceURI() {
      return this.namespace;
   }

   public String getNormalNamespacePrefix() {
      return null;
   }

   protected void addChildNode(FONode child) {
      if (this.doc == null) {
         this.createBasicDocument();
      }

      super.addChildNode(child);
   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) throws FOPException {
      if (this.doc == null) {
         this.createBasicDocument();
      }

      super.characters(data, start, length, pList, locator);
   }

   public static class Maker extends ElementMapping.Maker {
      private String space;

      public Maker(String ns) {
         this.space = ns;
      }

      public FONode make(FONode parent) {
         return new UnknownXMLObj(parent, this.space);
      }
   }
}
