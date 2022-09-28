package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.util.MessageFormatter;

public class ASCIIReader extends Reader {
   public static final int DEFAULT_BUFFER_SIZE = 2048;
   protected InputStream fInputStream;
   protected byte[] fBuffer;
   private MessageFormatter fFormatter;
   private Locale fLocale;

   public ASCIIReader(InputStream var1, MessageFormatter var2, Locale var3) {
      this(var1, 2048, var2, var3);
   }

   public ASCIIReader(InputStream var1, int var2, MessageFormatter var3, Locale var4) {
      this.fFormatter = null;
      this.fLocale = null;
      this.fInputStream = var1;
      this.fBuffer = new byte[var2];
      this.fFormatter = var3;
      this.fLocale = var4;
   }

   public int read() throws IOException {
      int var1 = this.fInputStream.read();
      if (var1 >= 128) {
         throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(var1)});
      } else {
         return var1;
      }
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      if (var3 > this.fBuffer.length) {
         var3 = this.fBuffer.length;
      }

      int var4 = this.fInputStream.read(this.fBuffer, 0, var3);

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = this.fBuffer[var5];
         if (var6 < 0) {
            throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(var6 & 255)});
         }

         var1[var2 + var5] = (char)var6;
      }

      return var4;
   }

   public long skip(long var1) throws IOException {
      return this.fInputStream.skip(var1);
   }

   public boolean ready() throws IOException {
      return false;
   }

   public boolean markSupported() {
      return this.fInputStream.markSupported();
   }

   public void mark(int var1) throws IOException {
      this.fInputStream.mark(var1);
   }

   public void reset() throws IOException {
      this.fInputStream.reset();
   }

   public void close() throws IOException {
      this.fInputStream.close();
   }
}
