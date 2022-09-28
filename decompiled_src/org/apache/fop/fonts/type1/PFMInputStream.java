package org.apache.fop.fonts.type1;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PFMInputStream extends FilterInputStream {
   private final DataInputStream datain;

   public PFMInputStream(InputStream in) {
      super(in);
      this.datain = new DataInputStream(in);
   }

   public short readByte() throws IOException {
      short s = (short)this.datain.readByte();
      int s1 = ((s & 240) >>> 4 << 4) + (s & 15);
      return (short)s1;
   }

   public int readShort() throws IOException {
      int i = this.datain.readShort();
      int high = (i & '\uff00') >>> 8;
      int low = (i & 255) << 8;
      return low + high;
   }

   public long readInt() throws IOException {
      int i = this.datain.readInt();
      int i1 = (i & -16777216) >>> 24;
      int i2 = (i & 16711680) >>> 8;
      int i3 = (i & '\uff00') << 8;
      int i4 = (i & 255) << 24;
      return (long)(i1 + i2 + i3 + i4);
   }

   public String readString() throws IOException {
      InputStreamReader reader = new InputStreamReader(this.in, "ISO-8859-1");
      StringBuffer buf = new StringBuffer();

      int ch;
      for(ch = reader.read(); ch > 0; ch = reader.read()) {
         buf.append((char)ch);
      }

      if (ch == -1) {
         throw new EOFException("Unexpected end of stream reached");
      } else {
         return buf.toString();
      }
   }
}
