package org.apache.xerces.impl.xs.util;

import org.apache.xerces.xni.XMLLocator;

public class SimpleLocator implements XMLLocator {
   String lsid;
   String esid;
   int line;
   int column;
   int charOffset;

   public SimpleLocator() {
   }

   public SimpleLocator(String var1, String var2, int var3, int var4) {
      this(var1, var2, var3, var4, -1);
   }

   public void setValues(String var1, String var2, int var3, int var4) {
      this.setValues(var1, var2, var3, var4, -1);
   }

   public SimpleLocator(String var1, String var2, int var3, int var4, int var5) {
      this.line = var3;
      this.column = var4;
      this.lsid = var1;
      this.esid = var2;
      this.charOffset = var5;
   }

   public void setValues(String var1, String var2, int var3, int var4, int var5) {
      this.line = var3;
      this.column = var4;
      this.lsid = var1;
      this.esid = var2;
      this.charOffset = var5;
   }

   public int getLineNumber() {
      return this.line;
   }

   public int getColumnNumber() {
      return this.column;
   }

   public int getCharacterOffset() {
      return this.charOffset;
   }

   public String getPublicId() {
      return null;
   }

   public String getExpandedSystemId() {
      return this.esid;
   }

   public String getLiteralSystemId() {
      return this.lsid;
   }

   public String getBaseSystemId() {
      return null;
   }

   public void setColumnNumber(int var1) {
      this.column = var1;
   }

   public void setLineNumber(int var1) {
      this.line = var1;
   }

   public void setCharacterOffset(int var1) {
      this.charOffset = var1;
   }

   public void setBaseSystemId(String var1) {
   }

   public void setExpandedSystemId(String var1) {
      this.esid = var1;
   }

   public void setLiteralSystemId(String var1) {
      this.lsid = var1;
   }

   public void setPublicId(String var1) {
   }

   public String getEncoding() {
      return null;
   }

   public String getXMLVersion() {
      return null;
   }
}
