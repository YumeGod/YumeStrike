package org.apache.fop.pdf;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class PDFICCBasedColorSpace extends PDFObject implements PDFColorSpace {
   private PDFICCStream iccStream;
   private String explicitName;

   public PDFICCBasedColorSpace(String explicitName, PDFICCStream iccStream) {
      this.explicitName = explicitName;
      this.iccStream = iccStream;
   }

   public PDFICCBasedColorSpace(PDFICCStream iccStream) {
      this((String)null, iccStream);
   }

   public PDFICCStream getICCStream() {
      return this.iccStream;
   }

   public int getNumComponents() {
      return this.iccStream.getICCProfile().getNumComponents();
   }

   public String getName() {
      return this.explicitName != null ? this.explicitName : "ICC" + this.iccStream.getObjectNumber();
   }

   public boolean isDeviceColorSpace() {
      return false;
   }

   public boolean isRGBColorSpace() {
      return this.getNumComponents() == 3;
   }

   public boolean isCMYKColorSpace() {
      return this.getNumComponents() == 4;
   }

   public boolean isGrayColorSpace() {
      return this.getNumComponents() == 1;
   }

   protected String toPDFString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append(this.getObjectID());
      sb.append("[/ICCBased ").append(this.getICCStream().referencePDF()).append("]");
      sb.append("\nendobj\n");
      return sb.toString();
   }

   public static PDFICCBasedColorSpace setupsRGBAsDefaultRGBColorSpace(PDFDocument pdfDoc) {
      PDFICCStream sRGBProfile = setupsRGBColorProfile(pdfDoc);
      return pdfDoc.getFactory().makeICCBasedColorSpace((PDFResourceContext)null, "DefaultRGB", sRGBProfile);
   }

   public static PDFICCBasedColorSpace setupsRGBColorSpace(PDFDocument pdfDoc) {
      PDFICCStream sRGBProfile = setupsRGBColorProfile(pdfDoc);
      return pdfDoc.getFactory().makeICCBasedColorSpace((PDFResourceContext)null, (String)null, sRGBProfile);
   }

   public static PDFICCStream setupsRGBColorProfile(PDFDocument pdfDoc) {
      PDFICCStream sRGBProfile = pdfDoc.getFactory().makePDFICCStream();
      ICC_Profile profile;
      synchronized(PDFICCBasedColorSpace.class) {
         InputStream in = PDFDocument.class.getResourceAsStream("sRGB Color Space Profile.icm");
         if (in != null) {
            try {
               profile = ICC_Profile.getInstance(in);
            } catch (IOException var11) {
               throw new RuntimeException("Unexpected IOException loading the sRGB profile: " + var11.getMessage());
            } finally {
               IOUtils.closeQuietly(in);
            }
         } else {
            profile = ICC_Profile.getInstance(1000);
         }
      }

      sRGBProfile.setColorSpace(profile, (PDFDeviceColorSpace)null);
      return sRGBProfile;
   }
}
