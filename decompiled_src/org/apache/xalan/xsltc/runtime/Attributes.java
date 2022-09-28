package org.apache.xalan.xsltc.runtime;

import org.apache.xalan.xsltc.DOM;

public final class Attributes implements org.xml.sax.AttributeList {
   private int _element;
   private DOM _document;

   public Attributes(DOM document, int element) {
      this._element = element;
      this._document = document;
   }

   public int getLength() {
      return 0;
   }

   public String getName(int i) {
      return null;
   }

   public String getType(int i) {
      return null;
   }

   public String getType(String name) {
      return null;
   }

   public String getValue(int i) {
      return null;
   }

   public String getValue(String name) {
      return null;
   }
}
