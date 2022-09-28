package ssl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class ArmitageTrustManager implements X509TrustManager {
   protected ArmitageTrustListener checker;

   public ArmitageTrustManager(ArmitageTrustListener var1) {
      this.checker = var1;
   }

   public void checkClientTrusted(X509Certificate[] var1, String var2) {
   }

   public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      try {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            byte[] var4 = var1[var3].getEncoded();
            MessageDigest var5 = MessageDigest.getInstance("SHA-256");
            byte[] var6 = var5.digest(var4);
            BigInteger var7 = new BigInteger(1, var6);
            String var8 = var7.toString(16);
            if (this.checker != null && !this.checker.trust(var8)) {
               throw new CertificateException("Certificate Rejected. Press Cancel.");
            }
         }

      } catch (CertificateException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new CertificateException(var10.getMessage());
      }
   }

   public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
   }
}
