package org.apache.fop.render.pdf;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.extensions.xmp.XMPMetadata;
import org.apache.fop.pdf.PDFAMode;
import org.apache.fop.pdf.PDFConformanceException;
import org.apache.fop.pdf.PDFDeviceColorSpace;
import org.apache.fop.pdf.PDFDictionary;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFEncryptionManager;
import org.apache.fop.pdf.PDFEncryptionParams;
import org.apache.fop.pdf.PDFICCBasedColorSpace;
import org.apache.fop.pdf.PDFICCStream;
import org.apache.fop.pdf.PDFInfo;
import org.apache.fop.pdf.PDFMetadata;
import org.apache.fop.pdf.PDFNumsArray;
import org.apache.fop.pdf.PDFOutputIntent;
import org.apache.fop.pdf.PDFPageLabels;
import org.apache.fop.pdf.PDFXMode;
import org.apache.fop.util.ColorProfileUtil;
import org.apache.xmlgraphics.xmp.Metadata;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicAdapter;
import org.apache.xmlgraphics.xmp.schemas.XMPBasicSchema;

class PDFRenderingUtil implements PDFConfigurationConstants {
   private static Log log;
   private FOUserAgent userAgent;
   protected PDFDocument pdfDoc;
   protected PDFAMode pdfAMode;
   protected PDFXMode pdfXMode;
   protected PDFEncryptionParams encryptionParams;
   protected Map filterMap;
   protected PDFICCStream outputProfile;
   protected PDFICCBasedColorSpace sRGBColorSpace;
   protected boolean disableSRGBColorSpace;
   protected String outputProfileURI;

   PDFRenderingUtil(FOUserAgent userAgent) {
      this.pdfAMode = PDFAMode.DISABLED;
      this.pdfXMode = PDFXMode.DISABLED;
      this.disableSRGBColorSpace = false;
      this.userAgent = userAgent;
      this.initialize();
   }

   private static boolean booleanValueOf(Object obj) {
      if (obj instanceof Boolean) {
         return (Boolean)obj;
      } else if (obj instanceof String) {
         return Boolean.valueOf((String)obj);
      } else {
         throw new IllegalArgumentException("Boolean or \"true\" or \"false\" expected.");
      }
   }

   private void initialize() {
      PDFEncryptionParams params = (PDFEncryptionParams)this.userAgent.getRendererOptions().get("encryption-params");
      if (params != null) {
         this.encryptionParams = params;
      }

      String pwd = (String)this.userAgent.getRendererOptions().get("user-password");
      if (pwd != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setUserPassword(pwd);
      }

      pwd = (String)this.userAgent.getRendererOptions().get("owner-password");
      if (pwd != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setOwnerPassword(pwd);
      }

      Object setting = this.userAgent.getRendererOptions().get("noprint");
      if (setting != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setAllowPrint(!booleanValueOf(setting));
      }

      setting = this.userAgent.getRendererOptions().get("nocopy");
      if (setting != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setAllowCopyContent(!booleanValueOf(setting));
      }

      setting = this.userAgent.getRendererOptions().get("noedit");
      if (setting != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setAllowEditContent(!booleanValueOf(setting));
      }

      setting = this.userAgent.getRendererOptions().get("noannotations");
      if (setting != null) {
         if (this.encryptionParams == null) {
            this.encryptionParams = new PDFEncryptionParams();
         }

         this.encryptionParams.setAllowEditAnnotations(!booleanValueOf(setting));
      }

      String s = (String)this.userAgent.getRendererOptions().get("pdf-a-mode");
      if (s != null) {
         this.pdfAMode = PDFAMode.valueOf(s);
      }

      if (this.pdfAMode.isPDFA1LevelA()) {
         this.userAgent.getRendererOptions().put("accessibility", Boolean.TRUE);
      }

      s = (String)this.userAgent.getRendererOptions().get("pdf-x-mode");
      if (s != null) {
         this.pdfXMode = PDFXMode.valueOf(s);
      }

      s = (String)this.userAgent.getRendererOptions().get("output-profile");
      if (s != null) {
         this.outputProfileURI = s;
      }

      setting = this.userAgent.getRendererOptions().get("disable-srgb-colorspace");
      if (setting != null) {
         this.disableSRGBColorSpace = booleanValueOf(setting);
      }

   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public void setAMode(PDFAMode mode) {
      this.pdfAMode = mode;
   }

   public void setXMode(PDFXMode mode) {
      this.pdfXMode = mode;
   }

   public void setOutputProfileURI(String outputProfileURI) {
      this.outputProfileURI = outputProfileURI;
   }

   public void setDisableSRGBColorSpace(boolean disable) {
      this.disableSRGBColorSpace = disable;
   }

   public void setFilterMap(Map filterMap) {
      this.filterMap = filterMap;
   }

   public void setEncryptionParams(PDFEncryptionParams encryptionParams) {
      this.encryptionParams = encryptionParams;
   }

   private void updateInfo() {
      PDFInfo info = this.pdfDoc.getInfo();
      info.setCreator(this.userAgent.getCreator());
      info.setCreationDate(this.userAgent.getCreationDate());
      info.setAuthor(this.userAgent.getAuthor());
      info.setTitle(this.userAgent.getTitle());
      info.setSubject(this.userAgent.getSubject());
      info.setKeywords(this.userAgent.getKeywords());
   }

   private void updatePDFProfiles() {
      this.pdfDoc.getProfile().setPDFAMode(this.pdfAMode);
      this.pdfDoc.getProfile().setPDFXMode(this.pdfXMode);
   }

   private void addsRGBColorSpace() throws IOException {
      if (this.disableSRGBColorSpace) {
         if (this.pdfAMode != PDFAMode.DISABLED || this.pdfXMode != PDFXMode.DISABLED || this.outputProfileURI != null) {
            throw new IllegalStateException("It is not possible to disable the sRGB color space if PDF/A or PDF/X functionality is enabled or an output profile is set!");
         }
      } else {
         if (this.sRGBColorSpace != null) {
            return;
         }

         this.sRGBColorSpace = PDFICCBasedColorSpace.setupsRGBAsDefaultRGBColorSpace(this.pdfDoc);
      }

   }

   private void addDefaultOutputProfile() throws IOException {
      if (this.outputProfile == null) {
         InputStream in = null;
         if (this.outputProfileURI != null) {
            this.outputProfile = this.pdfDoc.getFactory().makePDFICCStream();
            Source src = this.getUserAgent().resolveURI(this.outputProfileURI);
            if (src == null) {
               throw new IOException("Output profile not found: " + this.outputProfileURI);
            }

            if (src instanceof StreamSource) {
               in = ((StreamSource)src).getInputStream();
            } else {
               in = (new URL(src.getSystemId())).openStream();
            }

            ICC_Profile profile;
            try {
               profile = ICC_Profile.getInstance(in);
            } finally {
               IOUtils.closeQuietly(in);
            }

            this.outputProfile.setColorSpace(profile, (PDFDeviceColorSpace)null);
         } else {
            this.outputProfile = this.sRGBColorSpace.getICCStream();
         }

      }
   }

   private void addPDFA1OutputIntent() throws IOException {
      this.addDefaultOutputProfile();
      String desc = ColorProfileUtil.getICCProfileDescription(this.outputProfile.getICCProfile());
      PDFOutputIntent outputIntent = this.pdfDoc.getFactory().makeOutputIntent();
      outputIntent.setSubtype("GTS_PDFA1");
      outputIntent.setDestOutputProfile(this.outputProfile);
      outputIntent.setOutputConditionIdentifier(desc);
      outputIntent.setInfo(outputIntent.getOutputConditionIdentifier());
      this.pdfDoc.getRoot().addOutputIntent(outputIntent);
   }

   private void addPDFXOutputIntent() throws IOException {
      this.addDefaultOutputProfile();
      String desc = ColorProfileUtil.getICCProfileDescription(this.outputProfile.getICCProfile());
      int deviceClass = this.outputProfile.getICCProfile().getProfileClass();
      if (deviceClass != 2) {
         throw new PDFConformanceException(this.pdfDoc.getProfile().getPDFXMode() + " requires that" + " the DestOutputProfile be an Output Device Profile. " + desc + " does not match that requirement.");
      } else {
         PDFOutputIntent outputIntent = this.pdfDoc.getFactory().makeOutputIntent();
         outputIntent.setSubtype("GTS_PDFX");
         outputIntent.setDestOutputProfile(this.outputProfile);
         outputIntent.setOutputConditionIdentifier(desc);
         outputIntent.setInfo(outputIntent.getOutputConditionIdentifier());
         this.pdfDoc.getRoot().addOutputIntent(outputIntent);
      }
   }

   public void renderXMPMetadata(XMPMetadata metadata) {
      Metadata docXMP = metadata.getMetadata();
      Metadata fopXMP = PDFMetadata.createXMPFromPDFDocument(this.pdfDoc);
      fopXMP.mergeInto(docXMP);
      XMPBasicAdapter xmpBasic = XMPBasicSchema.getAdapter(docXMP);
      xmpBasic.setMetadataDate(new Date());
      PDFMetadata.updateInfoFromMetadata(docXMP, this.pdfDoc.getInfo());
      PDFMetadata pdfMetadata = this.pdfDoc.getFactory().makeMetadata(docXMP, metadata.isReadOnly());
      this.pdfDoc.getRoot().setMetadata(pdfMetadata);
   }

   public void generateDefaultXMPMetadata() {
      if (this.pdfDoc.getRoot().getMetadata() == null) {
         Metadata xmp = PDFMetadata.createXMPFromPDFDocument(this.pdfDoc);
         PDFMetadata pdfMetadata = this.pdfDoc.getFactory().makeMetadata(xmp, true);
         this.pdfDoc.getRoot().setMetadata(pdfMetadata);
      }

   }

   public PDFDocument setupPDFDocument(OutputStream out) throws IOException {
      if (this.pdfDoc != null) {
         throw new IllegalStateException("PDFDocument already set up");
      } else {
         this.pdfDoc = new PDFDocument(this.userAgent.getProducer() != null ? this.userAgent.getProducer() : "");
         this.updateInfo();
         this.updatePDFProfiles();
         this.pdfDoc.setFilterMap(this.filterMap);
         this.pdfDoc.outputHeader(out);
         PDFEncryptionManager.setupPDFEncryption(this.encryptionParams, this.pdfDoc);
         this.addsRGBColorSpace();
         if (this.outputProfileURI != null) {
            this.addDefaultOutputProfile();
         }

         if (this.pdfXMode != PDFXMode.DISABLED) {
            log.debug(this.pdfXMode + " is active.");
            log.warn("Note: " + this.pdfXMode + " support is work-in-progress and not fully implemented, yet!");
            this.addPDFXOutputIntent();
         }

         if (this.pdfAMode.isPDFA1LevelB()) {
            log.debug("PDF/A is active. Conformance Level: " + this.pdfAMode);
            this.addPDFA1OutputIntent();
         }

         return this.pdfDoc;
      }
   }

   public void generatePageLabel(int pageIndex, String pageNumber) {
      PDFPageLabels pageLabels = this.pdfDoc.getRoot().getPageLabels();
      if (pageLabels == null) {
         pageLabels = this.pdfDoc.getFactory().makePageLabels();
         this.pdfDoc.getRoot().setPageLabels(pageLabels);
      }

      PDFNumsArray nums = pageLabels.getNums();
      PDFDictionary dict = new PDFDictionary(nums);
      dict.put("P", pageNumber);
      nums.put(pageIndex, dict);
   }

   static {
      log = LogFactory.getLog(PDFRenderingUtil.class);
   }
}
