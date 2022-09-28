package org.apache.xmlgraphics.util.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ASCIIHexOutputStream extends FilterOutputStream implements Finalizable {
   private static final int EOL = 10;
   private static final int EOD = 62;
   private static final int ZERO = 48;
   private static final int NINE = 57;
   private static final int A = 65;
   private static final int ADIFF = 7;
   private int posinline = 0;

   public ASCIIHexOutputStream(OutputStream out) {
      super(out);
   }

   public void write(int b) throws IOException {
      b &= 255;
      int digit1 = ((b & 240) >> 4) + 48;
      if (digit1 > 57) {
         digit1 += 7;
      }

      this.out.write(digit1);
      int digit2 = (b & 15) + 48;
      if (digit2 > 57) {
         digit2 += 7;
      }

      this.out.write(digit2);
      ++this.posinline;
      this.checkLineWrap();
   }

   private void checkLineWrap() throws IOException {
      if (this.posinline >= 40) {
         this.out.write(10);
         this.posinline = 0;
      }

   }

   public void finalizeStream() throws IOException {
      this.checkLineWrap();
      super.write(62);
      this.flush();
      if (this.out instanceof Finalizable) {
         ((Finalizable)this.out).finalizeStream();
      }

   }

   public void close() throws IOException {
      this.finalizeStream();
      super.close();
   }
}
