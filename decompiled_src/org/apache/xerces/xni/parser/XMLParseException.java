package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;

public class XMLParseException extends XNIException {
   static final long serialVersionUID = 1732959359448549967L;
   protected String fPublicId;
   protected String fLiteralSystemId;
   protected String fExpandedSystemId;
   protected String fBaseSystemId;
   protected int fLineNumber = -1;
   protected int fColumnNumber = -1;
   protected int fCharacterOffset = -1;

   public XMLParseException(XMLLocator var1, String var2) {
      super(var2);
      if (var1 != null) {
         this.fPublicId = var1.getPublicId();
         this.fLiteralSystemId = var1.getLiteralSystemId();
         this.fExpandedSystemId = var1.getExpandedSystemId();
         this.fBaseSystemId = var1.getBaseSystemId();
         this.fLineNumber = var1.getLineNumber();
         this.fColumnNumber = var1.getColumnNumber();
         this.fCharacterOffset = var1.getCharacterOffset();
      }

   }

   public XMLParseException(XMLLocator var1, String var2, Exception var3) {
      super(var2, var3);
      if (var1 != null) {
         this.fPublicId = var1.getPublicId();
         this.fLiteralSystemId = var1.getLiteralSystemId();
         this.fExpandedSystemId = var1.getExpandedSystemId();
         this.fBaseSystemId = var1.getBaseSystemId();
         this.fLineNumber = var1.getLineNumber();
         this.fColumnNumber = var1.getColumnNumber();
         this.fCharacterOffset = var1.getCharacterOffset();
      }

   }

   public String getPublicId() {
      return this.fPublicId;
   }

   public String getExpandedSystemId() {
      return this.fExpandedSystemId;
   }

   public String getLiteralSystemId() {
      return this.fLiteralSystemId;
   }

   public String getBaseSystemId() {
      return this.fBaseSystemId;
   }

   public int getLineNumber() {
      return this.fLineNumber;
   }

   public int getColumnNumber() {
      return this.fColumnNumber;
   }

   public int getCharacterOffset() {
      return this.fCharacterOffset;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.fPublicId != null) {
         var1.append(this.fPublicId);
      }

      var1.append(':');
      if (this.fLiteralSystemId != null) {
         var1.append(this.fLiteralSystemId);
      }

      var1.append(':');
      if (this.fExpandedSystemId != null) {
         var1.append(this.fExpandedSystemId);
      }

      var1.append(':');
      if (this.fBaseSystemId != null) {
         var1.append(this.fBaseSystemId);
      }

      var1.append(':');
      var1.append(this.fLineNumber);
      var1.append(':');
      var1.append(this.fColumnNumber);
      var1.append(':');
      var1.append(this.fCharacterOffset);
      var1.append(':');
      String var2 = this.getMessage();
      if (var2 == null) {
         Exception var3 = this.getException();
         if (var3 != null) {
            var2 = var3.getMessage();
         }
      }

      if (var2 != null) {
         var1.append(var2);
      }

      return var1.toString();
   }
}
