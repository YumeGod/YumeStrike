package org.xml.sax.helpers;

import org.xml.sax.Locator;

public class LocatorImpl implements Locator {
   private String publicId;
   private String systemId;
   private int lineNumber;
   private int columnNumber;

   public LocatorImpl() {
   }

   public LocatorImpl(Locator var1) {
      this.setPublicId(var1.getPublicId());
      this.setSystemId(var1.getSystemId());
      this.setLineNumber(var1.getLineNumber());
      this.setColumnNumber(var1.getColumnNumber());
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   public void setPublicId(String var1) {
      this.publicId = var1;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public void setLineNumber(int var1) {
      this.lineNumber = var1;
   }

   public void setColumnNumber(int var1) {
      this.columnNumber = var1;
   }
}
