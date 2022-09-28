package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.CountingOutputStream;

public class PDFDictionary extends PDFObject {
   protected Map entries = new HashMap();
   protected List order = new ArrayList();

   public PDFDictionary() {
   }

   public PDFDictionary(PDFObject parent) {
      super(parent);
   }

   public void put(String name, Object value) {
      if (value instanceof PDFObject) {
         PDFObject pdfObj = (PDFObject)value;
         if (!pdfObj.hasObjectNumber()) {
            pdfObj.setParent(this);
         }
      }

      if (!this.entries.containsKey(name)) {
         this.order.add(name);
      }

      this.entries.put(name, value);
   }

   public void put(String name, int value) {
      if (!this.entries.containsKey(name)) {
         this.order.add(name);
      }

      this.entries.put(name, new Integer(value));
   }

   public Object get(String name) {
      return this.entries.get(name);
   }

   protected int output(OutputStream stream) throws IOException {
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      if (this.hasObjectNumber()) {
         writer.write(this.getObjectID());
      }

      this.writeDictionary(cout, writer);
      if (this.hasObjectNumber()) {
         writer.write("\nendobj\n");
      }

      writer.flush();
      return cout.getCount();
   }

   protected void writeDictionary(OutputStream out, Writer writer) throws IOException {
      writer.write("<<");
      boolean compact = this.order.size() <= 2;
      Iterator iter = this.order.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         if (compact) {
            writer.write(32);
         } else {
            writer.write("\n  ");
         }

         writer.write(PDFName.escapeName(key));
         writer.write(32);
         Object obj = this.entries.get(key);
         this.formatObject(obj, out, writer);
      }

      if (compact) {
         writer.write(32);
      } else {
         writer.write(10);
      }

      writer.write(">>\n");
   }
}
