package org.apache.fop.pdf;

import java.io.IOException;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class PDFOutputIntent extends PDFObject {
   public static final String GTS_PDFX = "GTS_PDFX";
   public static final String GTS_PDFA1 = "GTS_PDFA1";
   private String subtype;
   private String outputCondition;
   private String outputConditionIdentifier;
   private String registryName;
   private String info;
   private PDFICCStream destOutputProfile;

   public String getSubtype() {
      return this.subtype;
   }

   public void setSubtype(String subtype) {
      this.subtype = subtype;
   }

   public String getOutputCondition() {
      return this.outputCondition;
   }

   public void setOutputCondition(String outputCondition) {
      this.outputCondition = outputCondition;
   }

   public String getOutputConditionIdentifier() {
      return this.outputConditionIdentifier;
   }

   public void setOutputConditionIdentifier(String outputConditionIdentifier) {
      this.outputConditionIdentifier = outputConditionIdentifier;
   }

   public String getRegistryName() {
      return this.registryName;
   }

   public void setRegistryName(String registryName) {
      this.registryName = registryName;
   }

   public String getInfo() {
      return this.info;
   }

   public void setInfo(String info) {
      this.info = info;
   }

   public PDFICCStream getDestOutputProfile() {
      return this.destOutputProfile;
   }

   public void setDestOutputProfile(PDFICCStream destOutputProfile) {
      this.destOutputProfile = destOutputProfile;
   }

   public byte[] toPDF() {
      ByteArrayOutputStream bout = new ByteArrayOutputStream(128);

      try {
         bout.write(encode(this.getObjectID()));
         bout.write(encode("<<\n"));
         bout.write(encode("/Type /OutputIntent\n"));
         bout.write(encode("/S /"));
         bout.write(encode(this.subtype));
         bout.write(encode("\n"));
         if (this.outputCondition != null) {
            bout.write(encode("/OutputCondition "));
            bout.write(this.encodeText(this.outputCondition));
            bout.write(encode("\n"));
         }

         bout.write(encode("/OutputConditionIdentifier "));
         bout.write(this.encodeText(this.outputConditionIdentifier));
         bout.write(encode("\n"));
         if (this.registryName != null) {
            bout.write(encode("/RegistryName "));
            bout.write(this.encodeText(this.registryName));
            bout.write(encode("\n"));
         }

         if (this.info != null) {
            bout.write(encode("/Info "));
            bout.write(this.encodeText(this.info));
            bout.write(encode("\n"));
         }

         if (this.destOutputProfile != null) {
            bout.write(encode("/DestOutputProfile " + this.destOutputProfile.referencePDF() + "\n"));
         }

         bout.write(encode(">>\nendobj\n"));
      } catch (IOException var3) {
         log.error("Ignored I/O exception", var3);
      }

      return bout.toByteArray();
   }
}
