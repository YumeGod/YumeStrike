package net.jsign.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.cms.SignerIdentifier;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.operator.ContentSigner;
import net.jsign.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import net.jsign.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.operator.DigestCalculatorProvider;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.util.Arrays;
import net.jsign.bouncycastle.util.io.TeeOutputStream;

public class SignerInfoGenerator {
   private final SignerIdentifier signerIdentifier;
   private final CMSAttributeTableGenerator sAttrGen;
   private final CMSAttributeTableGenerator unsAttrGen;
   private final ContentSigner signer;
   private final DigestCalculator digester;
   private final DigestAlgorithmIdentifierFinder digAlgFinder;
   private final CMSSignatureEncryptionAlgorithmFinder sigEncAlgFinder;
   private byte[] calculatedDigest;
   private X509CertificateHolder certHolder;

   SignerInfoGenerator(SignerIdentifier var1, ContentSigner var2, DigestCalculatorProvider var3, CMSSignatureEncryptionAlgorithmFinder var4) throws OperatorCreationException {
      this(var1, var2, var3, var4, false);
   }

   SignerInfoGenerator(SignerIdentifier var1, ContentSigner var2, DigestCalculatorProvider var3, CMSSignatureEncryptionAlgorithmFinder var4, boolean var5) throws OperatorCreationException {
      this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
      this.calculatedDigest = null;
      this.signerIdentifier = var1;
      this.signer = var2;
      if (var3 != null) {
         this.digester = var3.get(this.digAlgFinder.find(var2.getAlgorithmIdentifier()));
      } else {
         this.digester = null;
      }

      if (var5) {
         this.sAttrGen = null;
         this.unsAttrGen = null;
      } else {
         this.sAttrGen = new DefaultSignedAttributeTableGenerator();
         this.unsAttrGen = null;
      }

      this.sigEncAlgFinder = var4;
   }

   public SignerInfoGenerator(SignerInfoGenerator var1, CMSAttributeTableGenerator var2, CMSAttributeTableGenerator var3) {
      this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
      this.calculatedDigest = null;
      this.signerIdentifier = var1.signerIdentifier;
      this.signer = var1.signer;
      this.digester = var1.digester;
      this.sigEncAlgFinder = var1.sigEncAlgFinder;
      this.sAttrGen = var2;
      this.unsAttrGen = var3;
   }

   SignerInfoGenerator(SignerIdentifier var1, ContentSigner var2, DigestCalculatorProvider var3, CMSSignatureEncryptionAlgorithmFinder var4, CMSAttributeTableGenerator var5, CMSAttributeTableGenerator var6) throws OperatorCreationException {
      this.digAlgFinder = new DefaultDigestAlgorithmIdentifierFinder();
      this.calculatedDigest = null;
      this.signerIdentifier = var1;
      this.signer = var2;
      if (var3 != null) {
         this.digester = var3.get(this.digAlgFinder.find(var2.getAlgorithmIdentifier()));
      } else {
         this.digester = null;
      }

      this.sAttrGen = var5;
      this.unsAttrGen = var6;
      this.sigEncAlgFinder = var4;
   }

   public SignerIdentifier getSID() {
      return this.signerIdentifier;
   }

   public int getGeneratedVersion() {
      return this.signerIdentifier.isTagged() ? 3 : 1;
   }

   public boolean hasAssociatedCertificate() {
      return this.certHolder != null;
   }

   public X509CertificateHolder getAssociatedCertificate() {
      return this.certHolder;
   }

   public AlgorithmIdentifier getDigestAlgorithm() {
      return this.digester != null ? this.digester.getAlgorithmIdentifier() : this.digAlgFinder.find(this.signer.getAlgorithmIdentifier());
   }

   public OutputStream getCalculatingOutputStream() {
      if (this.digester != null) {
         return (OutputStream)(this.sAttrGen == null ? new TeeOutputStream(this.digester.getOutputStream(), this.signer.getOutputStream()) : this.digester.getOutputStream());
      } else {
         return this.signer.getOutputStream();
      }
   }

   public SignerInfo generate(ASN1ObjectIdentifier var1) throws CMSException {
      try {
         ASN1Set var2 = null;
         AlgorithmIdentifier var3 = this.sigEncAlgFinder.findEncryptionAlgorithm(this.signer.getAlgorithmIdentifier());
         AlgorithmIdentifier var4 = null;
         if (this.sAttrGen != null) {
            var4 = this.digester.getAlgorithmIdentifier();
            this.calculatedDigest = this.digester.getDigest();
            Map var5 = this.getBaseParameters(var1, this.digester.getAlgorithmIdentifier(), var3, this.calculatedDigest);
            AttributeTable var6 = this.sAttrGen.getAttributes(Collections.unmodifiableMap(var5));
            var2 = this.getAttributeSet(var6);
            OutputStream var7 = this.signer.getOutputStream();
            var7.write(var2.getEncoded("DER"));
            var7.close();
         } else if (this.digester != null) {
            var4 = this.digester.getAlgorithmIdentifier();
            this.calculatedDigest = this.digester.getDigest();
         } else {
            var4 = this.digAlgFinder.find(this.signer.getAlgorithmIdentifier());
            this.calculatedDigest = null;
         }

         byte[] var10 = this.signer.getSignature();
         ASN1Set var11 = null;
         if (this.unsAttrGen != null) {
            Map var12 = this.getBaseParameters(var1, var4, var3, this.calculatedDigest);
            var12.put("encryptedDigest", Arrays.clone(var10));
            AttributeTable var8 = this.unsAttrGen.getAttributes(Collections.unmodifiableMap(var12));
            var11 = this.getAttributeSet(var8);
         }

         return new SignerInfo(this.signerIdentifier, var4, var2, var3, new DEROctetString(var10), var11);
      } catch (IOException var9) {
         throw new CMSException("encoding error.", var9);
      }
   }

   void setAssociatedCertificate(X509CertificateHolder var1) {
      this.certHolder = var1;
   }

   private ASN1Set getAttributeSet(AttributeTable var1) {
      return var1 != null ? new DERSet(var1.toASN1EncodableVector()) : null;
   }

   private Map getBaseParameters(ASN1ObjectIdentifier var1, AlgorithmIdentifier var2, AlgorithmIdentifier var3, byte[] var4) {
      HashMap var5 = new HashMap();
      if (var1 != null) {
         var5.put("contentType", var1);
      }

      var5.put("digestAlgID", var2);
      var5.put("signatureAlgID", var3);
      var5.put("digest", Arrays.clone(var4));
      return var5;
   }

   public byte[] getCalculatedDigest() {
      return this.calculatedDigest != null ? Arrays.clone(this.calculatedDigest) : null;
   }

   public CMSAttributeTableGenerator getSignedAttributeTableGenerator() {
      return this.sAttrGen;
   }

   public CMSAttributeTableGenerator getUnsignedAttributeTableGenerator() {
      return this.unsAttrGen;
   }
}
