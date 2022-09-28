package org.apache.fop.pdf;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

public class PDFFormXObject extends PDFXObject {
   private PDFStream contents;

   public PDFFormXObject(int xnumber, PDFStream contents, PDFReference resources) {
      this.put("Name", new PDFName("Form" + xnumber));
      this.contents = contents;
      this.put("Type", new PDFName("XObject"));
      this.put("Subtype", new PDFName("Form"));
      this.put("FormType", new Integer(1));
      this.setMatrix(new AffineTransform());
      if (resources != null) {
         this.put("Resources", resources);
      }

   }

   public void setBBox(Rectangle2D bbox) {
      PDFArray array = (PDFArray)this.get("BBox");
      if (array == null) {
         array = new PDFArray(this);
         array.add(bbox.getX());
         array.add(bbox.getY());
         array.add(bbox.getWidth());
         array.add(bbox.getHeight());
         this.put("BBox", array);
      } else {
         array.set(0, bbox.getX());
         array.set(1, bbox.getY());
         array.set(2, bbox.getWidth());
         array.set(3, bbox.getHeight());
      }

   }

   public Rectangle2D getBBox() {
      PDFArray array = (PDFArray)this.get("BBox");
      if (array != null) {
         Rectangle2D rect = new Rectangle2D.Double();
         double x = ((Number)array.get(0)).doubleValue();
         double y = ((Number)array.get(1)).doubleValue();
         double w = ((Number)array.get(2)).doubleValue();
         double h = ((Number)array.get(3)).doubleValue();
         rect.setFrame(x, y, w, h);
         return rect;
      } else {
         return null;
      }
   }

   public void setMatrix(AffineTransform at) {
      PDFArray array = (PDFArray)this.get("Matrix");
      double[] m = new double[6];
      at.getMatrix(m);
      if (array == null) {
         array = new PDFArray(this);
         array.add(m[0]);
         array.add(m[1]);
         array.add(m[2]);
         array.add(m[3]);
         array.add(m[4]);
         array.add(m[5]);
         this.put("Matrix", array);
      } else {
         array.set(0, m[0]);
         array.set(1, m[1]);
         array.set(2, m[2]);
         array.set(3, m[3]);
         array.set(4, m[4]);
         array.set(5, m[5]);
      }

   }

   public AffineTransform getMatrix() {
      PDFArray array = (PDFArray)this.get("Matrix");
      if (array != null) {
         AffineTransform at = new AffineTransform();
         double m00 = ((Number)array.get(0)).doubleValue();
         double m10 = ((Number)array.get(1)).doubleValue();
         double m01 = ((Number)array.get(2)).doubleValue();
         double m11 = ((Number)array.get(3)).doubleValue();
         double m02 = ((Number)array.get(4)).doubleValue();
         double m12 = ((Number)array.get(5)).doubleValue();
         at.setTransform(m00, m10, m01, m11, m02, m12);
         return at;
      } else {
         return null;
      }
   }

   public void setData(byte[] data) throws IOException {
      this.contents.setData(data);
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      this.contents.outputRawStreamData(out);
   }

   protected int output(OutputStream stream) throws IOException {
      int len = super.output(stream);
      this.contents = null;
      return len;
   }

   protected void populateStreamDict(Object lengthEntry) {
      if (this.get("Matrix") == null) {
         this.put("Matrix", new PDFArray(this, new int[]{1, 0, 0, 1, 0, 0}));
      }

      super.populateStreamDict(lengthEntry);
   }
}
