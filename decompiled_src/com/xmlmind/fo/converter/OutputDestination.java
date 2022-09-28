package com.xmlmind.fo.converter;

import java.io.OutputStream;
import java.io.Writer;

public class OutputDestination implements Cloneable {
   private String fileName;
   private String encoding;
   private OutputStream byteStream;
   private Writer characterStream;

   public OutputDestination() {
   }

   public OutputDestination(String var1) {
      this.setFileName(var1);
   }

   public OutputDestination(OutputStream var1) {
      this.setByteStream(var1);
   }

   public OutputDestination(Writer var1) {
      this.setCharacterStream(var1);
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public OutputStream getByteStream() {
      return this.byteStream;
   }

   public Writer getCharacterStream() {
      return this.characterStream;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public void setByteStream(OutputStream var1) {
      this.byteStream = var1;
   }

   public void setCharacterStream(Writer var1) {
      this.characterStream = var1;
   }

   public OutputDestination copy() {
      try {
         return (OutputDestination)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
