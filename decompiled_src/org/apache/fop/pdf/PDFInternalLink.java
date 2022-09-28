package org.apache.fop.pdf;

public class PDFInternalLink extends PDFAction {
   private String goToReference;

   public PDFInternalLink(String goToReference) {
      this.goToReference = goToReference;
   }

   public String getAction() {
      return this.goToReference;
   }

   protected String toPDFString() {
      throw new UnsupportedOperationException("This method should not be called");
   }
}
