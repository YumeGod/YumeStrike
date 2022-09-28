package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.xml.transform.TransformerConfigurationException;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.XMPSerializer;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreAdapter;
import org.apache.xmlgraphics.xmp.schemas.DublinCoreSchema;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicAdapter;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicSchema;
import org.apache.xmlgraphics.xmp.schemas.pdf.AdobePDFAdapter;
import org.apache.xmlgraphics.xmp.schemas.pdf.AdobePDFSchema;
import org.apache.xmlgraphics.xmp.schemas.pdf.PDFAAdapter;
import org.apache.xmlgraphics.xmp.schemas.pdf.PDFAXMPSchema;
import org.xml.sax.SAXException;

public class PDFMetadata extends PDFStream {
   private Metadata xmpMetadata;
   private boolean readOnly = true;

   public PDFMetadata(Metadata xmp, boolean readOnly) {
      if (xmp == null) {
         throw new NullPointerException("The parameter for the XMP Document must not be null");
      } else {
         this.xmpMetadata = xmp;
         this.readOnly = readOnly;
      }
   }

   protected String getDefaultFilterName() {
      return "metadata";
   }

   public Metadata getMetadata() {
      return this.xmpMetadata;
   }

   protected int output(OutputStream stream) throws IOException {
      int length = super.output(stream);
      this.xmpMetadata = null;
      return length;
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      try {
         XMPSerializer.writeXMPPacket(this.xmpMetadata, out, this.readOnly);
      } catch (TransformerConfigurationException var3) {
         throw new IOException("Error setting up Transformer for XMP stream serialization: " + var3.getMessage());
      } catch (SAXException var4) {
         throw new IOException("Error while serializing XMP stream: " + var4.getMessage());
      }
   }

   protected void populateStreamDict(Object lengthEntry) {
      String filterEntry = this.getFilterList().buildFilterDictEntries();
      if (this.getDocumentSafely().getProfile().getPDFAMode().isPDFA1LevelB() && filterEntry != null && filterEntry.length() > 0) {
         throw new PDFConformanceException("The Filter key is prohibited when PDF/A-1 is active");
      } else {
         this.put("Type", new PDFName("Metadata"));
         this.put("Subtype", new PDFName("XML"));
         super.populateStreamDict(lengthEntry);
      }
   }

   public static Metadata createXMPFromPDFDocument(PDFDocument pdfDoc) {
      Metadata meta = new Metadata();
      PDFInfo info = pdfDoc.getInfo();
      PDFRoot root = pdfDoc.getRoot();
      if (info.getCreationDate() == null) {
         Date d = new Date();
         info.setCreationDate(d);
      }

      DublinCoreAdapter dc = DublinCoreSchema.getAdapter(meta);
      if (info.getAuthor() != null) {
         dc.addCreator(info.getAuthor());
      }

      if (info.getTitle() != null) {
         dc.setTitle(info.getTitle());
      }

      if (info.getSubject() != null) {
         dc.setDescription((String)null, info.getSubject());
      }

      if (root.getLanguage() != null) {
         dc.addLanguage(root.getLanguage());
      }

      dc.addDate(info.getCreationDate());
      PDFAMode pdfaMode = pdfDoc.getProfile().getPDFAMode();
      if (pdfaMode.isPDFA1LevelB()) {
         PDFAAdapter pdfa = PDFAXMPSchema.getAdapter(meta);
         pdfa.setPart(1);
         if (pdfaMode == PDFAMode.PDFA_1A) {
            pdfa.setConformance("A");
         } else {
            pdfa.setConformance("B");
         }
      }

      XMPBasicAdapter xmpBasic = XMPBasicSchema.getAdapter(meta);
      xmpBasic.setCreateDate(info.getCreationDate());
      PDFProfile profile = pdfDoc.getProfile();
      if (info.getModDate() != null) {
         xmpBasic.setModifyDate(info.getModDate());
      } else if (profile.isModDateRequired()) {
         xmpBasic.setModifyDate(info.getCreationDate());
      }

      if (info.getCreator() != null) {
         xmpBasic.setCreatorTool(info.getCreator());
      }

      AdobePDFAdapter adobePDF = AdobePDFSchema.getAdapter(meta);
      if (info.getKeywords() != null) {
         adobePDF.setKeywords(info.getKeywords());
      }

      if (info.getProducer() != null) {
         adobePDF.setProducer(info.getProducer());
      }

      adobePDF.setPDFVersion(pdfDoc.getPDFVersionString());
      return meta;
   }

   public static void updateInfoFromMetadata(Metadata meta, PDFInfo info) {
      DublinCoreAdapter dc = DublinCoreSchema.getAdapter(meta);
      info.setTitle(dc.getTitle());
      String[] creators = dc.getCreators();
      if (creators != null && creators.length > 0) {
         info.setAuthor(creators[0]);
      } else {
         info.setAuthor((String)null);
      }

      info.setSubject(dc.getDescription());
      AdobePDFAdapter pdf = AdobePDFSchema.getAdapter(meta);
      info.setKeywords(pdf.getKeywords());
      info.setProducer(pdf.getProducer());
      XMPBasicAdapter xmpBasic = XMPBasicSchema.getAdapter(meta);
      info.setCreator(xmpBasic.getCreatorTool());
      Date d = xmpBasic.getCreateDate();
      xmpBasic.setCreateDate(d);
      info.setCreationDate(d);
      d = xmpBasic.getModifyDate();
      if (d != null) {
         xmpBasic.setModifyDate(d);
         info.setModDate(d);
      }

   }
}
