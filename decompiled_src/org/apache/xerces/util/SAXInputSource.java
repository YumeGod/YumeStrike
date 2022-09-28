package org.apache.xerces.util;

import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public final class SAXInputSource extends XMLInputSource {
   private XMLReader fXMLReader;
   private InputSource fInputSource;

   public SAXInputSource() {
      this((InputSource)null);
   }

   public SAXInputSource(InputSource var1) {
      this((XMLReader)null, var1);
   }

   public SAXInputSource(XMLReader var1, InputSource var2) {
      super(var2 != null ? var2.getPublicId() : null, var2 != null ? var2.getSystemId() : null, (String)null);
      if (var2 != null) {
         this.setByteStream(var2.getByteStream());
         this.setCharacterStream(var2.getCharacterStream());
         this.setEncoding(var2.getEncoding());
      }

      this.fInputSource = var2;
      this.fXMLReader = var1;
   }

   public void setXMLReader(XMLReader var1) {
      this.fXMLReader = var1;
   }

   public XMLReader getXMLReader() {
      return this.fXMLReader;
   }

   public void setInputSource(InputSource var1) {
      if (var1 != null) {
         this.setPublicId(var1.getPublicId());
         this.setSystemId(var1.getSystemId());
         this.setByteStream(var1.getByteStream());
         this.setCharacterStream(var1.getCharacterStream());
         this.setEncoding(var1.getEncoding());
      } else {
         this.setPublicId((String)null);
         this.setSystemId((String)null);
         this.setByteStream((InputStream)null);
         this.setCharacterStream((Reader)null);
         this.setEncoding((String)null);
      }

      this.fInputSource = var1;
   }

   public InputSource getInputSource() {
      return this.fInputSource;
   }

   public void setPublicId(String var1) {
      super.setPublicId(var1);
      if (this.fInputSource == null) {
         this.fInputSource = new InputSource();
      }

      this.fInputSource.setPublicId(var1);
   }

   public void setSystemId(String var1) {
      super.setSystemId(var1);
      if (this.fInputSource == null) {
         this.fInputSource = new InputSource();
      }

      this.fInputSource.setSystemId(var1);
   }

   public void setByteStream(InputStream var1) {
      super.setByteStream(var1);
      if (this.fInputSource == null) {
         this.fInputSource = new InputSource();
      }

      this.fInputSource.setByteStream(var1);
   }

   public void setCharacterStream(Reader var1) {
      super.setCharacterStream(var1);
      if (this.fInputSource == null) {
         this.fInputSource = new InputSource();
      }

      this.fInputSource.setCharacterStream(var1);
   }

   public void setEncoding(String var1) {
      super.setEncoding(var1);
      if (this.fInputSource == null) {
         this.fInputSource = new InputSource();
      }

      this.fInputSource.setEncoding(var1);
   }
}
