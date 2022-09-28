package org.apache.fop.pdf;

public class PDFResourceContext extends PDFDictionary {
   public PDFResourceContext(PDFResources resources) {
      this.put("Resources", resources);
   }

   public PDFResources getPDFResources() {
      return (PDFResources)this.get("Resources");
   }

   public void addAnnotation(PDFObject annot) {
      PDFAnnotList annotList = this.getAnnotations();
      if (annotList == null) {
         annotList = this.getDocument().getFactory().makeAnnotList();
         this.put("Annots", annotList);
      }

      annotList.addAnnot(annot);
   }

   public PDFAnnotList getAnnotations() {
      return (PDFAnnotList)this.get("Annots");
   }

   public void addGState(PDFGState gstate) {
      this.getPDFResources().addGState(gstate);
   }

   public void addShading(PDFShading shading) {
      this.getPDFResources().addShading(shading);
   }
}
