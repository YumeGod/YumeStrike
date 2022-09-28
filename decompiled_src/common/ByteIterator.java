package common;

import java.util.Arrays;

public class ByteIterator {
   protected byte[] buffer;
   protected int index = 0;

   public ByteIterator(byte[] var1) {
      this.buffer = var1;
   }

   public boolean hasNext() {
      return this.index < this.buffer.length;
   }

   public byte[] next(long var1) {
      int var3 = (int)var1;
      if (this.index >= this.buffer.length) {
         return new byte[0];
      } else {
         byte[] var4;
         if (this.index + var3 < this.buffer.length) {
            var4 = Arrays.copyOfRange(this.buffer, this.index, this.index + var3);
            this.index += var3;
            return var4;
         } else {
            var4 = Arrays.copyOfRange(this.buffer, this.index, this.buffer.length);
            this.index = this.buffer.length;
            return var4;
         }
      }
   }

   public void reset() {
      this.index = 0;
   }

   public static void test1() {
      byte[] var0 = CommonUtils.randomData(CommonUtils.rand(10485760));
      CommonUtils.print_warn("Garbage is: " + var0.length);
      String var1 = CommonUtils.toHex(CommonUtils.MD5(var0));
      ByteIterator var2 = new ByteIterator(var0);
      byte[] var3 = new byte[0];

      for(int var4 = 0; var2.hasNext(); ++var4) {
         byte[] var5 = var2.next(1048576L);
         CommonUtils.print_warn("Chunk " + var4 + ": " + var5.length);
         var3 = CommonUtils.join(var3, var5);
      }

      String var6 = CommonUtils.toHex(CommonUtils.MD5(var3));
      CommonUtils.print_info("MD5 (before): " + var1);
      CommonUtils.print_info("MD5  (after): " + var6);
      if (!var1.equals(var6)) {
         CommonUtils.print_error("FAILED!");
         System.exit(0);
      }

   }

   public static void main(String[] var0) {
      for(int var1 = 0; var1 < 8192; ++var1) {
         test1();
      }

      CommonUtils.print_good("PASSED!");
   }
}
