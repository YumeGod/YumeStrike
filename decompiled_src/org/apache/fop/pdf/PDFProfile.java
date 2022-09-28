package org.apache.fop.pdf;

import java.text.MessageFormat;

public class PDFProfile {
   protected PDFAMode pdfAMode;
   protected PDFXMode pdfXMode;
   private PDFDocument doc;

   public PDFProfile(PDFDocument doc) {
      this.pdfAMode = PDFAMode.DISABLED;
      this.pdfXMode = PDFXMode.DISABLED;
      this.doc = doc;
   }

   protected void validateProfileCombination() {
      if (this.pdfAMode != PDFAMode.DISABLED && this.pdfAMode == PDFAMode.PDFA_1B && this.pdfXMode != PDFXMode.DISABLED && this.pdfXMode != PDFXMode.PDFX_3_2003) {
         throw new PDFConformanceException(this.pdfAMode + " and " + this.pdfXMode + " are not compatible!");
      }
   }

   public PDFDocument getDocument() {
      return this.doc;
   }

   public PDFAMode getPDFAMode() {
      return this.pdfAMode;
   }

   public boolean isPDFAActive() {
      return this.getPDFAMode() != PDFAMode.DISABLED;
   }

   public void setPDFAMode(PDFAMode mode) {
      if (mode == null) {
         mode = PDFAMode.DISABLED;
      }

      this.pdfAMode = mode;
      this.validateProfileCombination();
   }

   public PDFXMode getPDFXMode() {
      return this.pdfXMode;
   }

   public boolean isPDFXActive() {
      return this.getPDFXMode() != PDFXMode.DISABLED;
   }

   public void setPDFXMode(PDFXMode mode) {
      if (mode == null) {
         mode = PDFXMode.DISABLED;
      }

      this.pdfXMode = mode;
      this.validateProfileCombination();
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      if (this.isPDFAActive() && this.isPDFXActive()) {
         sb.append("[").append(this.getPDFAMode()).append(",").append(this.getPDFXMode()).append("]");
      } else if (this.isPDFAActive()) {
         sb.append(this.getPDFAMode());
      } else if (this.isPDFXActive()) {
         sb.append(this.getPDFXMode());
      } else {
         sb.append(super.toString());
      }

      return sb.toString();
   }

   private String format(String pattern, Object arg) {
      return MessageFormat.format(pattern, arg);
   }

   public void verifyEncryptionAllowed() {
      String err = "{0} doesn't allow encrypted PDFs";
      if (this.isPDFAActive()) {
         throw new PDFConformanceException(this.format("{0} doesn't allow encrypted PDFs", this.getPDFAMode()));
      } else if (this.isPDFXActive()) {
         throw new PDFConformanceException(this.format("{0} doesn't allow encrypted PDFs", this.getPDFXMode()));
      }
   }

   public void verifyPSXObjectsAllowed() {
      String err = "PostScript XObjects are prohibited when {0} is active. Convert EPS graphics to another format.";
      if (this.isPDFAActive()) {
         throw new PDFConformanceException(this.format("PostScript XObjects are prohibited when {0} is active. Convert EPS graphics to another format.", this.getPDFAMode()));
      } else if (this.isPDFXActive()) {
         throw new PDFConformanceException(this.format("PostScript XObjects are prohibited when {0} is active. Convert EPS graphics to another format.", this.getPDFXMode()));
      }
   }

   public void verifyTransparencyAllowed(String context) {
      String err = "{0} does not allow the use of transparency. ({1})";
      if (this.isPDFAActive()) {
         throw new PDFConformanceException(MessageFormat.format("{0} does not allow the use of transparency. ({1})", this.getPDFAMode(), context));
      } else if (this.isPDFXActive()) {
         throw new PDFConformanceException(MessageFormat.format("{0} does not allow the use of transparency. ({1})", this.getPDFXMode(), context));
      }
   }

   public void verifyPDFVersion() {
      String err = "PDF version must be 1.4 for {0}";
      if (this.getPDFAMode().isPDFA1LevelB() && this.getDocument().getPDFVersion() != 4) {
         throw new PDFConformanceException(this.format("PDF version must be 1.4 for {0}", this.getPDFAMode()));
      } else if (this.getPDFXMode() == PDFXMode.PDFX_3_2003 && this.getDocument().getPDFVersion() != 4) {
         throw new PDFConformanceException(this.format("PDF version must be 1.4 for {0}", this.getPDFXMode()));
      }
   }

   public void verifyTaggedPDF() {
      if (this.getPDFAMode().isPDFA1LevelA()) {
         String err = "{0} requires the {1} dictionary entry to be set";
         PDFDictionary markInfo = this.getDocument().getRoot().getMarkInfo();
         if (markInfo == null) {
            throw new PDFConformanceException(this.format("{0} requires the MarkInfo dictionary to be present", this.getPDFAMode()));
         }

         if (!Boolean.TRUE.equals(markInfo.get("Marked"))) {
            throw new PDFConformanceException(this.format("{0} requires the {1} dictionary entry to be set", new Object[]{this.getPDFAMode(), "Marked"}));
         }

         if (this.getDocument().getRoot().getStructTreeRoot() == null) {
            throw new PDFConformanceException(this.format("{0} requires the {1} dictionary entry to be set", new Object[]{this.getPDFAMode(), "StructTreeRoot"}));
         }

         if (this.getDocument().getRoot().getLanguage() == null) {
            throw new PDFConformanceException(this.format("{0} requires the {1} dictionary entry to be set", new Object[]{this.getPDFAMode(), "Lang"}));
         }
      }

   }

   public boolean isIDEntryRequired() {
      return this.isPDFAActive() || this.isPDFXActive();
   }

   public boolean isFontEmbeddingRequired() {
      return this.isPDFAActive() || this.isPDFXActive();
   }

   public void verifyTitleAbsent() {
      if (this.isPDFXActive()) {
         String err = "{0} requires the title to be set.";
         throw new PDFConformanceException(this.format("{0} requires the title to be set.", this.getPDFXMode()));
      }
   }

   public boolean isModDateRequired() {
      return this.getPDFXMode() == PDFXMode.PDFX_3_2003;
   }

   public boolean isTrappedEntryRequired() {
      return this.getPDFXMode() == PDFXMode.PDFX_3_2003;
   }

   public boolean isAnnotationAllowed() {
      return !this.isPDFXActive();
   }

   public void verifyAnnotAllowed() {
      if (!this.isAnnotationAllowed()) {
         String err = "{0} does not allow annotations inside the printable area.";
         throw new PDFConformanceException(this.format("{0} does not allow annotations inside the printable area.", this.getPDFXMode()));
      }
   }

   public void verifyActionAllowed() {
      if (this.isPDFXActive()) {
         String err = "{0} does not allow Actions.";
         throw new PDFConformanceException(this.format("{0} does not allow Actions.", this.getPDFXMode()));
      }
   }
}
