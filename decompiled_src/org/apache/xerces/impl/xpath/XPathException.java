package org.apache.xerces.impl.xpath;

public class XPathException extends Exception {
   static final long serialVersionUID = -948482312169512085L;
   private String fKey;

   public XPathException() {
      this.fKey = "c-general-xpath";
   }

   public XPathException(String var1) {
      this.fKey = var1;
   }

   public String getKey() {
      return this.fKey;
   }
}
