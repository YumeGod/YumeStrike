package org.apache.xerces.dom;

import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.ls.LSInput;

public class DOMInputImpl implements LSInput {
   protected String fPublicId = null;
   protected String fSystemId = null;
   protected String fBaseSystemId = null;
   protected InputStream fByteStream = null;
   protected Reader fCharStream = null;
   protected String fData = null;
   protected String fEncoding = null;
   protected boolean fCertifiedText = false;

   public DOMInputImpl() {
   }

   public DOMInputImpl(String var1, String var2, String var3) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
   }

   public DOMInputImpl(String var1, String var2, String var3, InputStream var4, String var5) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
      this.fByteStream = var4;
      this.fEncoding = var5;
   }

   public DOMInputImpl(String var1, String var2, String var3, Reader var4, String var5) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
      this.fCharStream = var4;
      this.fEncoding = var5;
   }

   public DOMInputImpl(String var1, String var2, String var3, String var4, String var5) {
      this.fPublicId = var1;
      this.fSystemId = var2;
      this.fBaseSystemId = var3;
      this.fData = var4;
      this.fEncoding = var5;
   }

   public InputStream getByteStream() {
      return this.fByteStream;
   }

   public void setByteStream(InputStream var1) {
      this.fByteStream = var1;
   }

   public Reader getCharacterStream() {
      return this.fCharStream;
   }

   public void setCharacterStream(Reader var1) {
      this.fCharStream = var1;
   }

   public String getStringData() {
      return this.fData;
   }

   public void setStringData(String var1) {
      this.fData = var1;
   }

   public String getEncoding() {
      return this.fEncoding;
   }

   public void setEncoding(String var1) {
      this.fEncoding = var1;
   }

   public String getPublicId() {
      return this.fPublicId;
   }

   public void setPublicId(String var1) {
      this.fPublicId = var1;
   }

   public String getSystemId() {
      return this.fSystemId;
   }

   public void setSystemId(String var1) {
      this.fSystemId = var1;
   }

   public String getBaseURI() {
      return this.fBaseSystemId;
   }

   public void setBaseURI(String var1) {
      this.fBaseSystemId = var1;
   }

   public boolean getCertifiedText() {
      return this.fCertifiedText;
   }

   public void setCertifiedText(boolean var1) {
      this.fCertifiedText = var1;
   }
}
