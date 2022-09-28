package org.xml.sax.ext;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

public class Locator2Impl extends LocatorImpl implements Locator2 {
   private String encoding;
   private String version;

   public Locator2Impl() {
   }

   public Locator2Impl(Locator var1) {
      super(var1);
      if (var1 instanceof Locator2) {
         Locator2 var2 = (Locator2)var1;
         this.version = var2.getXMLVersion();
         this.encoding = var2.getEncoding();
      }

   }

   public String getXMLVersion() {
      return this.version;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setXMLVersion(String var1) {
      this.version = var1;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }
}
