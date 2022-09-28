package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.output.CountingOutputStream;

public class PDFArray extends PDFObject {
   protected List values;

   public PDFArray(PDFObject parent) {
      super(parent);
      this.values = new ArrayList();
   }

   public PDFArray() {
      this((PDFObject)null);
   }

   public PDFArray(PDFObject parent, int[] values) {
      super(parent);
      this.values = new ArrayList();
      int i = 0;

      for(int c = values.length; i < c; ++i) {
         this.values.add(new Integer(values[i]));
      }

   }

   public PDFArray(PDFObject parent, double[] values) {
      super(parent);
      this.values = new ArrayList();
      int i = 0;

      for(int c = values.length; i < c; ++i) {
         this.values.add(new Double(values[i]));
      }

   }

   public PDFArray(PDFObject parent, Collection values) {
      super(parent);
      this.values = new ArrayList();
      this.values.addAll(values);
   }

   public PDFArray(PDFObject parent, Object[] values) {
      super(parent);
      this.values = new ArrayList();
      int i = 0;

      for(int c = values.length; i < c; ++i) {
         this.values.add(values[i]);
      }

   }

   public boolean contains(Object obj) {
      return this.values.contains(obj);
   }

   public int length() {
      return this.values.size();
   }

   public void set(int index, Object obj) {
      this.values.set(index, obj);
   }

   public void set(int index, double value) {
      this.values.set(index, new Double(value));
   }

   public Object get(int index) {
      return this.values.get(index);
   }

   public void add(Object obj) {
      if (obj instanceof PDFObject) {
         PDFObject pdfObj = (PDFObject)obj;
         if (!pdfObj.hasObjectNumber()) {
            pdfObj.setParent(this);
         }
      }

      this.values.add(obj);
   }

   public void add(double value) {
      this.values.add(new Double(value));
   }

   protected int output(OutputStream stream) throws IOException {
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      if (this.hasObjectNumber()) {
         writer.write(this.getObjectID());
      }

      writer.write(91);

      for(int i = 0; i < this.values.size(); ++i) {
         if (i > 0) {
            writer.write(32);
         }

         Object obj = this.values.get(i);
         this.formatObject(obj, cout, writer);
      }

      writer.write(93);
      if (this.hasObjectNumber()) {
         writer.write("\nendobj\n");
      }

      writer.flush();
      return cout.getCount();
   }
}
