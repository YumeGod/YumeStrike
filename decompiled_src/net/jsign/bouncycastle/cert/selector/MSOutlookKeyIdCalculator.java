package net.jsign.bouncycastle.cert.selector;

import java.io.IOException;
import net.jsign.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import net.jsign.bouncycastle.util.Pack;

class MSOutlookKeyIdCalculator {
   static byte[] calculateKeyId(SubjectPublicKeyInfo var0) {
      SHA1Digest var1 = new SHA1Digest();
      byte[] var2 = new byte[var1.getDigestSize()];
      byte[] var3 = new byte[0];

      try {
         var3 = var0.getEncoded("DER");
      } catch (IOException var5) {
         return new byte[0];
      }

      var1.update(var3, 0, var3.length);
      var1.doFinal(var2, 0);
      return var2;
   }

   private abstract static class GeneralDigest {
      private static final int BYTE_LENGTH = 64;
      private byte[] xBuf;
      private int xBufOff;
      private long byteCount;

      protected GeneralDigest() {
         this.xBuf = new byte[4];
         this.xBufOff = 0;
      }

      protected GeneralDigest(GeneralDigest var1) {
         this.xBuf = new byte[var1.xBuf.length];
         this.copyIn(var1);
      }

      protected void copyIn(GeneralDigest var1) {
         System.arraycopy(var1.xBuf, 0, this.xBuf, 0, var1.xBuf.length);
         this.xBufOff = var1.xBufOff;
         this.byteCount = var1.byteCount;
      }

      public void update(byte var1) {
         this.xBuf[this.xBufOff++] = var1;
         if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
            this.xBufOff = 0;
         }

         ++this.byteCount;
      }

      public void update(byte[] var1, int var2, int var3) {
         while(this.xBufOff != 0 && var3 > 0) {
            this.update(var1[var2]);
            ++var2;
            --var3;
         }

         while(var3 > this.xBuf.length) {
            this.processWord(var1, var2);
            var2 += this.xBuf.length;
            var3 -= this.xBuf.length;
            this.byteCount += (long)this.xBuf.length;
         }

         while(var3 > 0) {
            this.update(var1[var2]);
            ++var2;
            --var3;
         }

      }

      public void finish() {
         long var1 = this.byteCount << 3;
         this.update((byte)-128);

         while(this.xBufOff != 0) {
            this.update((byte)0);
         }

         this.processLength(var1);
         this.processBlock();
      }

      public void reset() {
         this.byteCount = 0L;
         this.xBufOff = 0;

         for(int var1 = 0; var1 < this.xBuf.length; ++var1) {
            this.xBuf[var1] = 0;
         }

      }

      protected abstract void processWord(byte[] var1, int var2);

      protected abstract void processLength(long var1);

      protected abstract void processBlock();
   }

   private static class SHA1Digest extends GeneralDigest {
      private static final int DIGEST_LENGTH = 20;
      private int H1;
      private int H2;
      private int H3;
      private int H4;
      private int H5;
      private int[] X = new int[80];
      private int xOff;
      private static final int Y1 = 1518500249;
      private static final int Y2 = 1859775393;
      private static final int Y3 = -1894007588;
      private static final int Y4 = -899497514;

      public SHA1Digest() {
         this.reset();
      }

      public String getAlgorithmName() {
         return "SHA-1";
      }

      public int getDigestSize() {
         return 20;
      }

      protected void processWord(byte[] var1, int var2) {
         int var3 = var1[var2] << 24;
         ++var2;
         var3 |= (var1[var2] & 255) << 16;
         ++var2;
         var3 |= (var1[var2] & 255) << 8;
         ++var2;
         var3 |= var1[var2] & 255;
         this.X[this.xOff] = var3;
         if (++this.xOff == 16) {
            this.processBlock();
         }

      }

      protected void processLength(long var1) {
         if (this.xOff > 14) {
            this.processBlock();
         }

         this.X[14] = (int)(var1 >>> 32);
         this.X[15] = (int)(var1 & -1L);
      }

      public int doFinal(byte[] var1, int var2) {
         this.finish();
         Pack.intToBigEndian(this.H1, var1, var2);
         Pack.intToBigEndian(this.H2, var1, var2 + 4);
         Pack.intToBigEndian(this.H3, var1, var2 + 8);
         Pack.intToBigEndian(this.H4, var1, var2 + 12);
         Pack.intToBigEndian(this.H5, var1, var2 + 16);
         this.reset();
         return 20;
      }

      public void reset() {
         super.reset();
         this.H1 = 1732584193;
         this.H2 = -271733879;
         this.H3 = -1732584194;
         this.H4 = 271733878;
         this.H5 = -1009589776;
         this.xOff = 0;

         for(int var1 = 0; var1 != this.X.length; ++var1) {
            this.X[var1] = 0;
         }

      }

      private int f(int var1, int var2, int var3) {
         return var1 & var2 | ~var1 & var3;
      }

      private int h(int var1, int var2, int var3) {
         return var1 ^ var2 ^ var3;
      }

      private int g(int var1, int var2, int var3) {
         return var1 & var2 | var1 & var3 | var2 & var3;
      }

      protected void processBlock() {
         int var1;
         int var2;
         for(var1 = 16; var1 < 80; ++var1) {
            var2 = this.X[var1 - 3] ^ this.X[var1 - 8] ^ this.X[var1 - 14] ^ this.X[var1 - 16];
            this.X[var1] = var2 << 1 | var2 >>> 31;
         }

         var1 = this.H1;
         var2 = this.H2;
         int var3 = this.H3;
         int var4 = this.H4;
         int var5 = this.H5;
         int var6 = 0;

         int var7;
         for(var7 = 0; var7 < 4; ++var7) {
            var5 += (var1 << 5 | var1 >>> 27) + this.f(var2, var3, var4) + this.X[var6++] + 1518500249;
            var2 = var2 << 30 | var2 >>> 2;
            var4 += (var5 << 5 | var5 >>> 27) + this.f(var1, var2, var3) + this.X[var6++] + 1518500249;
            var1 = var1 << 30 | var1 >>> 2;
            var3 += (var4 << 5 | var4 >>> 27) + this.f(var5, var1, var2) + this.X[var6++] + 1518500249;
            var5 = var5 << 30 | var5 >>> 2;
            var2 += (var3 << 5 | var3 >>> 27) + this.f(var4, var5, var1) + this.X[var6++] + 1518500249;
            var4 = var4 << 30 | var4 >>> 2;
            var1 += (var2 << 5 | var2 >>> 27) + this.f(var3, var4, var5) + this.X[var6++] + 1518500249;
            var3 = var3 << 30 | var3 >>> 2;
         }

         for(var7 = 0; var7 < 4; ++var7) {
            var5 += (var1 << 5 | var1 >>> 27) + this.h(var2, var3, var4) + this.X[var6++] + 1859775393;
            var2 = var2 << 30 | var2 >>> 2;
            var4 += (var5 << 5 | var5 >>> 27) + this.h(var1, var2, var3) + this.X[var6++] + 1859775393;
            var1 = var1 << 30 | var1 >>> 2;
            var3 += (var4 << 5 | var4 >>> 27) + this.h(var5, var1, var2) + this.X[var6++] + 1859775393;
            var5 = var5 << 30 | var5 >>> 2;
            var2 += (var3 << 5 | var3 >>> 27) + this.h(var4, var5, var1) + this.X[var6++] + 1859775393;
            var4 = var4 << 30 | var4 >>> 2;
            var1 += (var2 << 5 | var2 >>> 27) + this.h(var3, var4, var5) + this.X[var6++] + 1859775393;
            var3 = var3 << 30 | var3 >>> 2;
         }

         for(var7 = 0; var7 < 4; ++var7) {
            var5 += (var1 << 5 | var1 >>> 27) + this.g(var2, var3, var4) + this.X[var6++] + -1894007588;
            var2 = var2 << 30 | var2 >>> 2;
            var4 += (var5 << 5 | var5 >>> 27) + this.g(var1, var2, var3) + this.X[var6++] + -1894007588;
            var1 = var1 << 30 | var1 >>> 2;
            var3 += (var4 << 5 | var4 >>> 27) + this.g(var5, var1, var2) + this.X[var6++] + -1894007588;
            var5 = var5 << 30 | var5 >>> 2;
            var2 += (var3 << 5 | var3 >>> 27) + this.g(var4, var5, var1) + this.X[var6++] + -1894007588;
            var4 = var4 << 30 | var4 >>> 2;
            var1 += (var2 << 5 | var2 >>> 27) + this.g(var3, var4, var5) + this.X[var6++] + -1894007588;
            var3 = var3 << 30 | var3 >>> 2;
         }

         for(var7 = 0; var7 <= 3; ++var7) {
            var5 += (var1 << 5 | var1 >>> 27) + this.h(var2, var3, var4) + this.X[var6++] + -899497514;
            var2 = var2 << 30 | var2 >>> 2;
            var4 += (var5 << 5 | var5 >>> 27) + this.h(var1, var2, var3) + this.X[var6++] + -899497514;
            var1 = var1 << 30 | var1 >>> 2;
            var3 += (var4 << 5 | var4 >>> 27) + this.h(var5, var1, var2) + this.X[var6++] + -899497514;
            var5 = var5 << 30 | var5 >>> 2;
            var2 += (var3 << 5 | var3 >>> 27) + this.h(var4, var5, var1) + this.X[var6++] + -899497514;
            var4 = var4 << 30 | var4 >>> 2;
            var1 += (var2 << 5 | var2 >>> 27) + this.h(var3, var4, var5) + this.X[var6++] + -899497514;
            var3 = var3 << 30 | var3 >>> 2;
         }

         this.H1 += var1;
         this.H2 += var2;
         this.H3 += var3;
         this.H4 += var4;
         this.H5 += var5;
         this.xOff = 0;

         for(var7 = 0; var7 < 16; ++var7) {
            this.X[var7] = 0;
         }

      }
   }
}
