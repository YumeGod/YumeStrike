package org.apache.xml.utils;

import java.io.Serializable;
import org.xml.sax.Locator;

public class SerializableLocatorImpl implements Locator, Serializable {
   static final long serialVersionUID = -2660312888446371460L;
   private String publicId;
   private String systemId;
   private int lineNumber;
   private int columnNumber;

   public SerializableLocatorImpl() {
   }

   public SerializableLocatorImpl(Locator locator) {
      this.setPublicId(locator.getPublicId());
      this.setSystemId(locator.getSystemId());
      this.setLineNumber(locator.getLineNumber());
      this.setColumnNumber(locator.getColumnNumber());
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

   public void setPublicId(String publicId) {
      this.publicId = publicId;
   }

   public void setSystemId(String systemId) {
      this.systemId = systemId;
   }

   public void setLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   public void setColumnNumber(int columnNumber) {
      this.columnNumber = columnNumber;
   }
}
