package org.apache.fop.pdf;

public class PDFUri extends PDFAction {
   private String uri;

   public PDFUri(String uri) {
      this.uri = uri;
   }

   public String getAction() {
      return this.hasObjectNumber() ? this.referencePDF() : this.getDictString();
   }

   private String getDictString() {
      return "<< /URI (" + this.uri + ")\n/S /URI >>";
   }

   public String toPDFString() {
      return this.getObjectID() + this.getDictString() + "\nendobj\n";
   }
}
