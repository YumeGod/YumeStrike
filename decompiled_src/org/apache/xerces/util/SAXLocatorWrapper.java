package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;

public final class SAXLocatorWrapper implements XMLLocator {
   private Locator fLocator = null;
   private Locator2 fLocator2 = null;

   public void setLocator(Locator var1) {
      this.fLocator = var1;
      if (var1 instanceof Locator2 || var1 == null) {
         this.fLocator2 = (Locator2)var1;
      }

   }

   public Locator getLocator() {
      return this.fLocator;
   }

   public String getPublicId() {
      return this.fLocator != null ? this.fLocator.getPublicId() : null;
   }

   public String getLiteralSystemId() {
      return this.fLocator != null ? this.fLocator.getSystemId() : null;
   }

   public String getBaseSystemId() {
      return null;
   }

   public String getExpandedSystemId() {
      return this.getLiteralSystemId();
   }

   public int getLineNumber() {
      return this.fLocator != null ? this.fLocator.getLineNumber() : -1;
   }

   public int getColumnNumber() {
      return this.fLocator != null ? this.fLocator.getColumnNumber() : -1;
   }

   public int getCharacterOffset() {
      return -1;
   }

   public String getEncoding() {
      return this.fLocator2 != null ? this.fLocator2.getEncoding() : null;
   }

   public String getXMLVersion() {
      return this.fLocator2 != null ? this.fLocator2.getXMLVersion() : null;
   }
}
