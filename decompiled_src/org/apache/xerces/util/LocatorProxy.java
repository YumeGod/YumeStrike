package org.apache.xerces.util;

import org.apache.xerces.xni.XMLLocator;
import org.xml.sax.ext.Locator2;

public class LocatorProxy implements Locator2 {
   private final XMLLocator fLocator;

   public LocatorProxy(XMLLocator var1) {
      this.fLocator = var1;
   }

   public String getPublicId() {
      return this.fLocator.getPublicId();
   }

   public String getSystemId() {
      return this.fLocator.getExpandedSystemId();
   }

   public int getLineNumber() {
      return this.fLocator.getLineNumber();
   }

   public int getColumnNumber() {
      return this.fLocator.getColumnNumber();
   }

   public String getXMLVersion() {
      return this.fLocator.getXMLVersion();
   }

   public String getEncoding() {
      return this.fLocator.getEncoding();
   }
}
