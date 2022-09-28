package dns;

import javax.crypto.SecretKey;

public class QuickSecurity extends BaseSecurity {
   public static short getCryptoScheme() {
      return 0;
   }

   protected byte[] do_encrypt(SecretKey var1, byte[] var2) throws Exception {
      this.in.init(1, var1, this.ivspec);
      return this.in.doFinal(var2);
   }

   protected byte[] do_decrypt(SecretKey var1, byte[] var2) throws Exception {
      this.out.init(2, var1, this.ivspec);
      return this.out.doFinal(var2, 0, var2.length);
   }
}
