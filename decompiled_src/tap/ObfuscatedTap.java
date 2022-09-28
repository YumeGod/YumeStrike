package tap;

public class ObfuscatedTap extends TapProtocol {
   protected byte[] xor_key = new byte[1024];

   public ObfuscatedTap(String var1, byte[] var2) {
      super(var1);

      for(int var3 = 0; var3 < this.xor_key.length; ++var3) {
         this.xor_key[var3] = var2[var3 % var2.length];
      }

   }

   public int readFrame(byte[] var1) {
      int var2 = super.readFrame(var1);

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3] ^= this.xor_key[var3 % 1024];
      }

      return var2;
   }

   public void writeFrame(byte[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3] ^= this.xor_key[var3 % 1024];
      }

      this.writeFrame(this.fd, var1, var2);
   }

   public byte[] protocol(int var1, byte[] var2) {
      byte[] var3 = super.protocol(var1, var2);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] ^= this.xor_key[var4 % 1024];
      }

      return var3;
   }
}
