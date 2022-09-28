package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.commons.io.output.CountingOutputStream;

public class PDFName extends PDFObject {
   private String name;
   private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public PDFName(String name) {
      this.name = escapeName(name);
   }

   static String escapeName(String name) {
      StringBuffer sb = new StringBuffer(Math.min(16, name.length() + 4));
      if (!name.startsWith("/")) {
         sb.append('/');
      }

      int i = 0;

      for(int c = name.length(); i < c; ++i) {
         char ch = name.charAt(i);
         if (ch >= '!' && ch <= '~') {
            sb.append(ch);
         } else {
            sb.append('#');
            toHex(ch, sb);
         }
      }

      return sb.toString();
   }

   private static void toHex(char ch, StringBuffer sb) {
      if (ch >= 256) {
         throw new IllegalArgumentException("Only 8-bit characters allowed by this implementation");
      } else {
         sb.append(DIGITS[ch >>> 4 & 15]);
         sb.append(DIGITS[ch & 15]);
      }
   }

   public String toString() {
      return this.name;
   }

   protected int output(OutputStream stream) throws IOException {
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      if (this.hasObjectNumber()) {
         writer.write(this.getObjectID());
      }

      writer.write(this.toString());
      if (this.hasObjectNumber()) {
         writer.write("\nendobj\n");
      }

      writer.flush();
      return cout.getCount();
   }

   public void outputInline(OutputStream out, Writer writer) throws IOException {
      if (this.hasObjectNumber()) {
         writer.write(this.referencePDF());
      } else {
         writer.write(this.toString());
      }

   }
}
