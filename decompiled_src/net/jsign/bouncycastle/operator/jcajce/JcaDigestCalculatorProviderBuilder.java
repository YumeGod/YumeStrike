package net.jsign.bouncycastle.operator.jcajce;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Provider;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.jcajce.util.DefaultJcaJceHelper;
import net.jsign.bouncycastle.jcajce.util.NamedJcaJceHelper;
import net.jsign.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.operator.DigestCalculatorProvider;
import net.jsign.bouncycastle.operator.OperatorCreationException;

public class JcaDigestCalculatorProviderBuilder {
   private OperatorHelper helper = new OperatorHelper(new DefaultJcaJceHelper());

   public JcaDigestCalculatorProviderBuilder setProvider(Provider var1) {
      this.helper = new OperatorHelper(new ProviderJcaJceHelper(var1));
      return this;
   }

   public JcaDigestCalculatorProviderBuilder setProvider(String var1) {
      this.helper = new OperatorHelper(new NamedJcaJceHelper(var1));
      return this;
   }

   public DigestCalculatorProvider build() throws OperatorCreationException {
      return new DigestCalculatorProvider() {
         public DigestCalculator get(final AlgorithmIdentifier var1) throws OperatorCreationException {
            final DigestOutputStream var3;
            try {
               MessageDigest var2 = JcaDigestCalculatorProviderBuilder.this.helper.createDigest(var1);
               var3 = JcaDigestCalculatorProviderBuilder.this.new DigestOutputStream(var2);
            } catch (GeneralSecurityException var4) {
               throw new OperatorCreationException("exception on setup: " + var4, var4);
            }

            return new DigestCalculator() {
               public AlgorithmIdentifier getAlgorithmIdentifier() {
                  return var1;
               }

               public OutputStream getOutputStream() {
                  return var3;
               }

               public byte[] getDigest() {
                  return var3.getDigest();
               }
            };
         }
      };
   }

   private class DigestOutputStream extends OutputStream {
      private MessageDigest dig;

      DigestOutputStream(MessageDigest var2) {
         this.dig = var2;
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.dig.update(var1, var2, var3);
      }

      public void write(byte[] var1) throws IOException {
         this.dig.update(var1);
      }

      public void write(int var1) throws IOException {
         this.dig.update((byte)var1);
      }

      byte[] getDigest() {
         return this.dig.digest();
      }
   }
}
