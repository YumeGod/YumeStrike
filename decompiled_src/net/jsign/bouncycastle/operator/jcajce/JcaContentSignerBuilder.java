package net.jsign.bouncycastle.operator.jcajce;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.jcajce.util.DefaultJcaJceHelper;
import net.jsign.bouncycastle.jcajce.util.NamedJcaJceHelper;
import net.jsign.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import net.jsign.bouncycastle.operator.ContentSigner;
import net.jsign.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.operator.OperatorStreamException;
import net.jsign.bouncycastle.operator.RuntimeOperatorException;

public class JcaContentSignerBuilder {
   private OperatorHelper helper = new OperatorHelper(new DefaultJcaJceHelper());
   private SecureRandom random;
   private String signatureAlgorithm;
   private AlgorithmIdentifier sigAlgId;

   public JcaContentSignerBuilder(String var1) {
      this.signatureAlgorithm = var1;
      this.sigAlgId = (new DefaultSignatureAlgorithmIdentifierFinder()).find(var1);
   }

   public JcaContentSignerBuilder setProvider(Provider var1) {
      this.helper = new OperatorHelper(new ProviderJcaJceHelper(var1));
      return this;
   }

   public JcaContentSignerBuilder setProvider(String var1) {
      this.helper = new OperatorHelper(new NamedJcaJceHelper(var1));
      return this;
   }

   public JcaContentSignerBuilder setSecureRandom(SecureRandom var1) {
      this.random = var1;
      return this;
   }

   public ContentSigner build(PrivateKey var1) throws OperatorCreationException {
      try {
         final Signature var2 = this.helper.createSignature(this.sigAlgId);
         if (this.random != null) {
            var2.initSign(var1, this.random);
         } else {
            var2.initSign(var1);
         }

         return new ContentSigner() {
            private SignatureOutputStream stream = JcaContentSignerBuilder.this.new SignatureOutputStream(var2);

            public AlgorithmIdentifier getAlgorithmIdentifier() {
               return JcaContentSignerBuilder.this.sigAlgId;
            }

            public OutputStream getOutputStream() {
               return this.stream;
            }

            public byte[] getSignature() {
               try {
                  return this.stream.getSignature();
               } catch (SignatureException var2x) {
                  throw new RuntimeOperatorException("exception obtaining signature: " + var2x.getMessage(), var2x);
               }
            }
         };
      } catch (GeneralSecurityException var3) {
         throw new OperatorCreationException("cannot create signer: " + var3.getMessage(), var3);
      }
   }

   private class SignatureOutputStream extends OutputStream {
      private Signature sig;

      SignatureOutputStream(Signature var2) {
         this.sig = var2;
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         try {
            this.sig.update(var1, var2, var3);
         } catch (SignatureException var5) {
            throw new OperatorStreamException("exception in content signer: " + var5.getMessage(), var5);
         }
      }

      public void write(byte[] var1) throws IOException {
         try {
            this.sig.update(var1);
         } catch (SignatureException var3) {
            throw new OperatorStreamException("exception in content signer: " + var3.getMessage(), var3);
         }
      }

      public void write(int var1) throws IOException {
         try {
            this.sig.update((byte)var1);
         } catch (SignatureException var3) {
            throw new OperatorStreamException("exception in content signer: " + var3.getMessage(), var3);
         }
      }

      byte[] getSignature() throws SignatureException {
         return this.sig.sign();
      }
   }
}
