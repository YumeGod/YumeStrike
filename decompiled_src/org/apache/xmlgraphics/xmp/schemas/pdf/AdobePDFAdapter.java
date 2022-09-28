package org.apache.xmlgraphics.xmp.schemas.pdf;

import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSchemaAdapter;
import org.apache.xmlgraphics.xmp.XMPSchemaRegistry;

public class AdobePDFAdapter extends XMPSchemaAdapter {
   private static final String KEYWORDS = "Keywords";
   private static final String PDFVERSION = "PDFVersion";
   private static final String PRODUCER = "Producer";

   public AdobePDFAdapter(Metadata meta, String namespace) {
      super(meta, XMPSchemaRegistry.getInstance().getSchema(namespace));
   }

   public String getKeywords() {
      return this.getValue("Keywords");
   }

   public void setKeywords(String value) {
      this.setValue("Keywords", value);
   }

   public String getPDFVersion() {
      return this.getValue("PDFVersion");
   }

   public void setPDFVersion(String value) {
      this.setValue("PDFVersion", value);
   }

   public String getProducer() {
      return this.getValue("Producer");
   }

   public void setProducer(String value) {
      this.setValue("Producer", value);
   }
}
