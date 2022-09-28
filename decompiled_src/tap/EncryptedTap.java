package tap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedTap extends TapProtocol {
   protected SecretKey key;
   protected IvParameterSpec ivspec;
   protected Cipher in;
   protected Cipher out;
   protected byte[] out_buffer = new byte[65536];
   protected byte[] in_buffer = new byte[65536];
   protected ByteArrayOutputStream out_bytes;
   protected DataOutputStream out_handle;

   public EncryptedTap(String var1, byte[] var2) {
      super(var1);
      byte[] var3 = new byte[16];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = var2[var4 % var2.length];
      }

      try {
         this.key = new SecretKeySpec(var3, "AES");
         byte[] var6 = "abcdefghijklmnop".getBytes();
         this.ivspec = new IvParameterSpec(var6);
         this.in = Cipher.getInstance("AES/CBC/NoPadding");
         this.out = Cipher.getInstance("AES/CBC/NoPadding");
         this.out_bytes = new ByteArrayOutputStream(65536);
         this.out_handle = new DataOutputStream(this.out_bytes);
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   protected void pad(ByteArrayOutputStream var1) {
      for(int var2 = var1.size() % 16; var2 < 16; ++var2) {
         var1.write(65);
      }

   }

   public byte[] protocol(int var1, byte[] var2) {
      byte[] var3 = super.protocol(var1, var2);

      try {
         this.out_bytes.reset();
         this.out_handle.write(var3, 0, var3.length);
         this.pad(this.out_bytes);
         this.in.init(1, this.key, this.ivspec);
         byte[] var4 = this.in.doFinal(this.out_bytes.toByteArray());
         return var4;
      } catch (Exception var5) {
         var5.printStackTrace();
         return new byte[0];
      }
   }

   public int readFrame(byte[] var1) {
      int var2 = super.readFrame(this.out_buffer);

      try {
         this.out_bytes.reset();
         this.out_handle.writeShort(var2);
         this.out_handle.write(this.out_buffer, 0, var2);
         this.pad(this.out_bytes);
         this.in.init(1, this.key, this.ivspec);
         byte[] var3 = this.in.doFinal(this.out_bytes.toByteArray());
         System.arraycopy(var3, 0, var1, 0, var3.length);
         return var3.length;
      } catch (Exception var4) {
         var4.printStackTrace();
         return 0;
      }
   }

   public void writeFrame(byte[] var1, int var2) {
      try {
         this.out.init(2, this.key, this.ivspec);
         byte[] var3 = this.out.doFinal(var1, 0, var2);
         DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var3));
         int var5 = var4.readUnsignedShort();
         var4.readFully(this.in_buffer, 0, var5);
         this.writeFrame(this.fd, this.in_buffer, var5);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
