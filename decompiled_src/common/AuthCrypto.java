package common;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public final class AuthCrypto {
   public Cipher cipher;
   public Key pubkey = null;
   protected String error = null;

   public AuthCrypto() {
      try {
         this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         this.load();
      } catch (Exception var2) {
         this.error = "Could not initialize crypto";
         MudgeSanity.logException("AuthCrypto init", var2, false);
      }

   }

   public void load() {
      try {
         byte[] var1 = CommonUtils.readAll(CommonUtils.class.getClassLoader().getResourceAsStream("resources/authkey.pub"));
         if (!"7ef5ed238ec450fd1ae2e935d65ddbcb".equals(CommonUtils.toHex(CommonUtils.MD5(var1)))) {
            CommonUtils.print_error("Invalid authorization file");
            System.exit(0);
         }

         this.pubkey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(var1));
      } catch (Exception var2) {
         this.error = "Could not deserialize authpub.key";
         MudgeSanity.logException("authpub.key deserialization", var2, false);
      }

   }

   public String error() {
      return this.error;
   }

   public byte[] decrypt(byte[] var1) {
      byte[] var2 = this._decrypt(var1);

      try {
         if (var2.length == 0) {
            return var2;
         } else {
            DataParser var3 = new DataParser(var2);
            var3.big();
            int var4 = var3.readInt();
            if (var4 == -889274181) {
               this.error = "pre-4.0 authorization file. Run update to get new file";
               return new byte[0];
            } else if (var4 != -889274157) {
               this.error = "bad header";
               return new byte[0];
            } else {
               return var3.readBytes(var3.readShort());
            }
         }
      } catch (Exception var5) {
         this.error = var5.getMessage();
         return new byte[0];
      }
   }

   protected byte[] _decrypt(byte[] var1) {
      byte[] var2 = new byte[0];

      try {
         if (this.pubkey == null) {
            return new byte[0];
         } else {
            synchronized(this.cipher) {
               this.cipher.init(2, this.pubkey);
               var2 = this.cipher.doFinal(var1);
            }

            return var2;
         }
      } catch (Exception var6) {
         this.error = var6.getMessage();
         return new byte[0];
      }
   }
}
