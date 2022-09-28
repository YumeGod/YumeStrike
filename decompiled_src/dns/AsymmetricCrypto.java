package dns;

import common.MudgeSanity;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricCrypto {
   public Cipher cipher;
   public PrivateKey privatekey;
   public PublicKey publickey;

   public AsymmetricCrypto(KeyPair var1) throws NoSuchAlgorithmException, NoSuchPaddingException {
      this.privatekey = var1.getPrivate();
      this.publickey = var1.getPublic();
      this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
   }

   public byte[] exportPublicKey() {
      return this.publickey.getEncoded();
   }

   public byte[] decrypt(byte[] var1) {
      byte[] var2 = new byte[0];

      try {
         synchronized(this.cipher) {
            this.cipher.init(2, this.privatekey);
            var2 = this.cipher.doFinal(var1);
         }

         DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var2));
         int var4 = var3.readInt();
         if (var4 != 48879) {
            System.err.println("Magic number failed :( [RSA decrypt]");
            return new byte[0];
         } else {
            int var5 = var3.readInt();
            if (var5 > 117) {
               System.err.println("Length field check failed :( [RSA decrypt]");
               return new byte[0];
            } else {
               byte[] var6 = new byte[var5];
               var3.readFully(var6, 0, var5);
               return var6;
            }
         }
      } catch (Exception var8) {
         MudgeSanity.logException("RSA decrypt", var8, false);
         return new byte[0];
      }
   }

   public static KeyPair generateKeys() {
      try {
         KeyPairGenerator var0 = KeyPairGenerator.getInstance("RSA");
         var0.initialize(1024);
         return var0.generateKeyPair();
      } catch (Exception var1) {
         var1.printStackTrace();
         return null;
      }
   }
}
