package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.io.output.CountingOutputStream;

public class PDFNumsArray extends PDFObject {
   protected SortedMap map = new TreeMap();

   public PDFNumsArray(PDFObject parent) {
      super(parent);
   }

   public int length() {
      return this.map.size();
   }

   public void put(Integer key, Object obj) {
      this.map.put(key, obj);
   }

   public void put(int key, Object obj) {
      this.put(new Integer(key), obj);
   }

   public Object get(Integer key) {
      return this.map.get(key);
   }

   public Object get(int key) {
      return this.get(new Integer(key));
   }

   protected int output(OutputStream stream) throws IOException {
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      if (this.hasObjectNumber()) {
         writer.write(this.getObjectID());
      }

      writer.write(91);
      boolean first = true;
      Iterator iter = this.map.entrySet().iterator();

      while(iter.hasNext()) {
         Map.Entry entry = (Map.Entry)iter.next();
         if (!first) {
            writer.write(" ");
         }

         first = false;
         this.formatObject(entry.getKey(), cout, writer);
         writer.write(" ");
         this.formatObject(entry.getValue(), cout, writer);
      }

      writer.write(93);
      if (this.hasObjectNumber()) {
         writer.write("\nendobj\n");
      }

      writer.flush();
      return cout.getCount();
   }
}
