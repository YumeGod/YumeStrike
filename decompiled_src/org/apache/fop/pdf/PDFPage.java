package org.apache.fop.pdf;

import java.awt.geom.Rectangle2D;

public class PDFPage extends PDFResourceContext {
   protected int pageIndex;

   public PDFPage(PDFResources resources, int pageIndex, Rectangle2D mediaBox, Rectangle2D cropBox, Rectangle2D bleedBox, Rectangle2D trimBox) {
      super(resources);
      this.put("Type", new PDFName("Page"));
      this.setSimplePageSize(mediaBox, cropBox, bleedBox, trimBox);
      this.pageIndex = pageIndex;
   }

   private void setSimplePageSize(Rectangle2D mediaBox, Rectangle2D cropBox, Rectangle2D bleedBox, Rectangle2D trimBox) {
      this.setMediaBox(mediaBox);
      if (cropBox == null) {
         cropBox = mediaBox;
      }

      this.setCropBox(cropBox);
      if (bleedBox == null) {
         bleedBox = cropBox;
      }

      this.setBleedBox(bleedBox);
      if (trimBox == null) {
         trimBox = bleedBox;
      }

      this.setTrimBox(trimBox);
   }

   private PDFArray toPDFArray(Rectangle2D box) {
      return new PDFArray(this, new double[]{box.getX(), box.getY(), box.getMaxX(), box.getMaxY()});
   }

   public void setMediaBox(Rectangle2D box) {
      this.put("MediaBox", this.toPDFArray(box));
   }

   public void setCropBox(Rectangle2D box) {
      this.put("CropBox", this.toPDFArray(box));
   }

   public void setBleedBox(Rectangle2D box) {
      this.put("BleedBox", this.toPDFArray(box));
   }

   public void setTrimBox(Rectangle2D box) {
      this.put("TrimBox", this.toPDFArray(box));
   }

   public void setContents(PDFStream contents) {
      if (contents != null) {
         this.put("Contents", new PDFReference(contents));
      }

   }

   public void setParent(PDFPages parent) {
      this.put("Parent", new PDFReference(parent));
   }

   public void setTransition(int dur, TransitionDictionary tr) {
      this.put("Dur", new Integer(dur));
      this.put("Trans", tr);
   }

   public int getPageIndex() {
      return this.pageIndex;
   }

   public void setStructParents(int structParents) {
      this.put("StructParents", structParents);
      this.setTabs(new PDFName("S"));
   }

   public Integer getStructParents() {
      return (Integer)this.get("StructParents");
   }

   public void setTabs(PDFName value) {
      this.put("Tabs", value);
   }
}
