package org.apache.fop.pdf;

public class PDFLaunch extends PDFAction {
   private PDFFileSpec externalFileSpec;

   public PDFLaunch(PDFFileSpec fileSpec) {
      this.externalFileSpec = fileSpec;
   }

   public String getAction() {
      return this.referencePDF();
   }

   public String toPDFString() {
      StringBuffer sb = new StringBuffer(64);
      sb.append(this.getObjectID());
      sb.append("<<\n/S /Launch\n/F ");
      sb.append(this.externalFileSpec.referencePDF());
      sb.append(" \n>>\nendobj\n");
      return sb.toString();
   }

   protected boolean contentEquals(PDFObject obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof PDFLaunch) {
         PDFLaunch launch = (PDFLaunch)obj;
         return launch.externalFileSpec.referencePDF().equals(this.externalFileSpec.referencePDF());
      } else {
         return false;
      }
   }
}
