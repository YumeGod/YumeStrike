package org.apache.batik.transcoder;

import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;

public class TranscoderInput {
   protected XMLReader xmlReader;
   protected InputStream istream;
   protected Reader reader;
   protected Document document;
   protected String uri;

   public TranscoderInput() {
   }

   public TranscoderInput(XMLReader var1) {
      this.xmlReader = var1;
   }

   public TranscoderInput(InputStream var1) {
      this.istream = var1;
   }

   public TranscoderInput(Reader var1) {
      this.reader = var1;
   }

   public TranscoderInput(Document var1) {
      this.document = var1;
   }

   public TranscoderInput(String var1) {
      this.uri = var1;
   }

   public void setXMLReader(XMLReader var1) {
      this.xmlReader = var1;
   }

   public XMLReader getXMLReader() {
      return this.xmlReader;
   }

   public void setInputStream(InputStream var1) {
      this.istream = var1;
   }

   public InputStream getInputStream() {
      return this.istream;
   }

   public void setReader(Reader var1) {
      this.reader = var1;
   }

   public Reader getReader() {
      return this.reader;
   }

   public void setDocument(Document var1) {
      this.document = var1;
   }

   public Document getDocument() {
      return this.document;
   }

   public void setURI(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }
}
