package net.jsign.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROctetStringGenerator extends BERGenerator {
   public BEROctetStringGenerator(OutputStream var1) throws IOException {
      super(var1);
      this.writeBERHeader(36);
   }

   public BEROctetStringGenerator(OutputStream var1, int var2, boolean var3) throws IOException {
      super(var1, var2, var3);
      this.writeBERHeader(36);
   }

   public OutputStream getOctetOutputStream() {
      return this.getOctetOutputStream(new byte[1000]);
   }

   public OutputStream getOctetOutputStream(byte[] var1) {
      return new BufferedBEROctetStream(var1);
   }

   private class BufferedBEROctetStream extends OutputStream {
      private byte[] _buf;
      private int _off;
      private DEROutputStream _derOut;

      BufferedBEROctetStream(byte[] var2) {
         this._buf = var2;
         this._off = 0;
         this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
      }

      public void write(int var1) throws IOException {
         this._buf[this._off++] = (byte)var1;
         if (this._off == this._buf.length) {
            DEROctetString.encode(this._derOut, this._buf);
            this._off = 0;
         }

      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         while(true) {
            if (var3 > 0) {
               int var4 = Math.min(var3, this._buf.length - this._off);
               System.arraycopy(var1, var2, this._buf, this._off, var4);
               this._off += var4;
               if (this._off >= this._buf.length) {
                  DEROctetString.encode(this._derOut, this._buf);
                  this._off = 0;
                  var2 += var4;
                  var3 -= var4;
                  continue;
               }
            }

            return;
         }
      }

      public void close() throws IOException {
         if (this._off != 0) {
            byte[] var1 = new byte[this._off];
            System.arraycopy(this._buf, 0, var1, 0, this._off);
            DEROctetString.encode(this._derOut, var1);
         }

         BEROctetStringGenerator.this.writeBEREnd();
      }
   }
}
