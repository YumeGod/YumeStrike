package org.apache.batik.transcoder;

import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.Document;
import org.xml.sax.XMLFilter;

public class TranscoderOutput {
   protected XMLFilter xmlFilter;
   protected OutputStream ostream;
   protected Writer writer;
   protected Document document;
   protected String uri;

   public TranscoderOutput() {
   }

   public TranscoderOutput(XMLFilter var1) {
      this.xmlFilter = var1;
   }

   public TranscoderOutput(OutputStream var1) {
      this.ostream = var1;
   }

   public TranscoderOutput(Writer var1) {
      this.writer = var1;
   }

   public TranscoderOutput(Document var1) {
      this.document = var1;
   }

   public TranscoderOutput(String var1) {
      this.uri = var1;
   }

   public void setXMLFilter(XMLFilter var1) {
      this.xmlFilter = var1;
   }

   public XMLFilter getXMLFilter() {
      return this.xmlFilter;
   }

   public void setOutputStream(OutputStream var1) {
      this.ostream = var1;
   }

   public OutputStream getOutputStream() {
      return this.ostream;
   }

   public void setWriter(Writer var1) {
      this.writer = var1;
   }

   public Writer getWriter() {
      return this.writer;
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
