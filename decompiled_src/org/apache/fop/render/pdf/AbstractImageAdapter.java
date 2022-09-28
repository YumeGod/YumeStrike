package org.apache.fop.render.pdf;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFConformanceException;
import org.apache.fop.pdf.PDFDeviceColorSpace;
import org.apache.fop.pdf.PDFDictionary;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFICCBasedColorSpace;
import org.apache.fop.pdf.PDFICCStream;
import org.apache.fop.pdf.PDFImage;
import org.apache.fop.pdf.PDFReference;
import org.apache.fop.pdf.PDFResourceContext;
import org.apache.fop.util.ColorProfileUtil;
import org.apache.xmlgraphics.image.loader.Image;

public abstract class AbstractImageAdapter implements PDFImage {
   private static Log log;
   private String key;
   protected Image image;
   private PDFICCStream pdfICCStream = null;

   public AbstractImageAdapter(Image image, String key) {
      this.image = image;
      this.key = key;
      if (log.isDebugEnabled()) {
         log.debug("New ImageAdapter created for key: " + key);
      }

   }

   public String getKey() {
      return this.key;
   }

   protected ColorSpace getImageColorSpace() {
      return this.image.getColorSpace();
   }

   public void setup(PDFDocument doc) {
      ICC_Profile prof = this.getEffectiveICCProfile();
      PDFDeviceColorSpace pdfCS = toPDFColorSpace(this.getImageColorSpace());
      if (prof != null) {
         this.pdfICCStream = setupColorProfile(doc, prof, pdfCS);
      }

      if (doc.getProfile().getPDFAMode().isPDFA1LevelB() && pdfCS != null && pdfCS.getColorSpace() != 2 && pdfCS.getColorSpace() != 1 && prof == null) {
         throw new PDFConformanceException("PDF/A-1 does not allow mixing DeviceRGB and DeviceCMYK: " + this.image.getInfo());
      }
   }

   protected ICC_Profile getEffectiveICCProfile() {
      return this.image.getICCProfile();
   }

   private static PDFICCStream setupColorProfile(PDFDocument doc, ICC_Profile prof, PDFDeviceColorSpace pdfCS) {
      boolean defaultsRGB = ColorProfileUtil.isDefaultsRGB(prof);
      String desc = ColorProfileUtil.getICCProfileDescription(prof);
      if (log.isDebugEnabled()) {
         log.debug("Image returns ICC profile: " + desc + ", default sRGB=" + defaultsRGB);
      }

      PDFICCBasedColorSpace cs = doc.getResources().getICCColorSpaceByProfileName(desc);
      PDFICCStream pdfICCStream;
      if (!defaultsRGB) {
         if (cs == null) {
            pdfICCStream = doc.getFactory().makePDFICCStream();
            pdfICCStream.setColorSpace(prof, pdfCS);
            cs = doc.getFactory().makeICCBasedColorSpace((PDFResourceContext)null, (String)null, pdfICCStream);
         } else {
            pdfICCStream = cs.getICCStream();
         }
      } else {
         if (cs == null && desc.startsWith("sRGB")) {
            cs = doc.getResources().getColorSpace("DefaultRGB");
         }

         if (cs == null) {
            cs = PDFICCBasedColorSpace.setupsRGBColorSpace(doc);
         }

         pdfICCStream = cs.getICCStream();
      }

      return pdfICCStream;
   }

   public int getWidth() {
      return this.image.getSize().getWidthPx();
   }

   public int getHeight() {
      return this.image.getSize().getHeightPx();
   }

   public boolean isTransparent() {
      return false;
   }

   public PDFColor getTransparentColor() {
      return null;
   }

   public String getMask() {
      return null;
   }

   public String getSoftMask() {
      return null;
   }

   public PDFReference getSoftMaskReference() {
      return null;
   }

   public boolean isInverted() {
      return false;
   }

   public boolean isPS() {
      return false;
   }

   public PDFICCStream getICCStream() {
      return this.pdfICCStream;
   }

   public void populateXObjectDictionary(PDFDictionary dict) {
   }

   public static PDFDeviceColorSpace toPDFColorSpace(ColorSpace cs) {
      if (cs == null) {
         return null;
      } else {
         PDFDeviceColorSpace pdfCS = new PDFDeviceColorSpace(0);
         switch (cs.getType()) {
            case 6:
               pdfCS.setColorSpace(1);
               break;
            case 9:
               pdfCS.setColorSpace(3);
               break;
            default:
               pdfCS.setColorSpace(2);
         }

         return pdfCS;
      }
   }

   static {
      log = LogFactory.getLog(AbstractImageAdapter.class);
   }
}
