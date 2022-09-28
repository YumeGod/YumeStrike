package org.apache.fop.pdf;

import java.util.ArrayList;
import java.util.List;

public class PDFWArray {
   private List entries = new ArrayList();

   public PDFWArray() {
   }

   public PDFWArray(int[] metrics) {
      this.addEntry(0, metrics);
   }

   public void addEntry(int start, int[] metrics) {
      this.entries.add(new Entry(start, metrics));
   }

   public void addEntry(int first, int last, int width) {
      this.entries.add(new int[]{first, last, width});
   }

   public void addEntry(int first, int last, int width, int posX, int posY) {
      this.entries.add(new int[]{first, last, width, posX, posY});
   }

   public byte[] toPDF() {
      return PDFDocument.encode(this.toPDFString());
   }

   public String toPDFString() {
      StringBuffer p = new StringBuffer();
      p.append("[ ");
      int len = this.entries.size();

      for(int i = 0; i < len; ++i) {
         Object entry = this.entries.get(i);
         if (entry instanceof int[]) {
            int[] line = (int[])entry;

            for(int j = 0; j < line.length; ++j) {
               p.append(line[j]);
               p.append(" ");
            }
         } else {
            ((Entry)entry).fillInPDF(p);
         }
      }

      p.append("]");
      return p.toString();
   }

   private static class Entry {
      private int start;
      private int[] metrics;

      public Entry(int s, int[] m) {
         this.start = s;
         this.metrics = m;
      }

      public void fillInPDF(StringBuffer p) {
         p.append(this.start);
         p.append(" [");

         for(int i = 0; i < this.metrics.length; ++i) {
            p.append(this.metrics[i]);
            p.append(" ");
         }

         p.append("] ");
      }
   }
}
