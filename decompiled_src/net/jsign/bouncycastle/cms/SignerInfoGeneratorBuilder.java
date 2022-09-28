package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import net.jsign.bouncycastle.asn1.cms.SignerIdentifier;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.operator.ContentSigner;
import net.jsign.bouncycastle.operator.DigestCalculatorProvider;
import net.jsign.bouncycastle.operator.OperatorCreationException;

public class SignerInfoGeneratorBuilder {
   private DigestCalculatorProvider digestProvider;
   private boolean directSignature;
   private CMSAttributeTableGenerator signedGen;
   private CMSAttributeTableGenerator unsignedGen;
   private CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder;

   public SignerInfoGeneratorBuilder(DigestCalculatorProvider var1) {
      this(var1, new DefaultCMSSignatureEncryptionAlgorithmFinder());
   }

   public SignerInfoGeneratorBuilder(DigestCalculatorProvider var1, CMSSignatureEncryptionAlgorithmFinder var2) {
      this.digestProvider = var1;
      this.sigEncAlgFinder = var2;
   }

   public SignerInfoGeneratorBuilder setDirectSignature(boolean var1) {
      this.directSignature = var1;
      return this;
   }

   public SignerInfoGeneratorBuilder setSignedAttributeGenerator(CMSAttributeTableGenerator var1) {
      this.signedGen = var1;
      return this;
   }

   public SignerInfoGeneratorBuilder setUnsignedAttributeGenerator(CMSAttributeTableGenerator var1) {
      this.unsignedGen = var1;
      return this;
   }

   public SignerInfoGenerator build(ContentSigner var1, X509CertificateHolder var2) throws OperatorCreationException {
      SignerIdentifier var3 = new SignerIdentifier(new IssuerAndSerialNumber(var2.toASN1Structure()));
      SignerInfoGenerator var4 = this.createGenerator(var1, var3);
      var4.setAssociatedCertificate(var2);
      return var4;
   }

   public SignerInfoGenerator build(ContentSigner var1, byte[] var2) throws OperatorCreationException {
      SignerIdentifier var3 = new SignerIdentifier(new DEROctetString(var2));
      return this.createGenerator(var1, var3);
   }

   private SignerInfoGenerator createGenerator(ContentSigner var1, SignerIdentifier var2) throws OperatorCreationException {
      if (this.directSignature) {
         return new SignerInfoGenerator(var2, var1, this.digestProvider, this.sigEncAlgFinder, true);
      } else if (this.signedGen == null && this.unsignedGen == null) {
         return new SignerInfoGenerator(var2, var1, this.digestProvider, this.sigEncAlgFinder);
      } else {
         if (this.signedGen == null) {
            this.signedGen = new DefaultSignedAttributeTableGenerator();
         }

         return new SignerInfoGenerator(var2, var1, this.digestProvider, this.sigEncAlgFinder, this.signedGen, this.unsignedGen);
      }
   }
}
