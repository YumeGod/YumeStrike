package org.apache.xerces.xni.parser;

import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.XMLResourceIdentifier;

public class XMLInputSource {
   protected String fPublicId;
   protected String fSystemId;
   protected String fBaseSystemId;
   protected InputStream fByteStream;
   protected Reader fCharStream;
   protected String fEncoding;

   public XMLInputSource(String var1, String var2, String var3) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
   }

   public XMLInputSource(XMLResourceIdentifier var1) {
      this.fPublicId = var1.getPublicId();
      this.fSystemId = var1.getLiteralSystemId();
      this.fBaseSystemId = var1.getBaseSystemId();
   }

   public XMLInputSource(String var1, String var2, String var3, InputStream var4, String var5) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
      this.fByteStream = var4;
      this.fEncoding = var5;
   }

   public XMLInputSource(String var1, String var2, String var3, Reader var4, String var5) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
      this.fCharStream = var4;
      this.fEncoding = var5;
   }

   public void setPublicId(String var1) {
      this.fPublicId = var1;
   }

   public String getPublicId() {
      return this.fPublicId;
   }

   public void setSystemId(String var1) {
      this.fSystemId = var1;
   }

   public String getSystemId() {
      return this.fSystemId;
   }

   public void setBaseSystemId(String var1) {
      this.fBaseSystemId = var1;
   }

   public String getBaseSystemId() {
      return this.fBaseSystemId;
   }

   public void setByteStream(InputStream var1) {
      this.fByteStream = var1;
   }

   public InputStream getByteStream() {
      return this.fByteStream;
   }

   public void setCharacterStream(Reader var1) {
      this.fCharStream = var1;
   }

   public Reader getCharacterStream() {
      return this.fCharStream;
   }

   public void setEncoding(String var1) {
      this.fEncoding = var1;
   }

   public String getEncoding() {
      return this.fEncoding;
   }
}
