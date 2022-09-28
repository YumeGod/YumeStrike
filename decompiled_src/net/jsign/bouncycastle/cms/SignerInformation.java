package net.jsign.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.Attribute;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.asn1.cms.CMSAlgorithmProtection;
import net.jsign.bouncycastle.asn1.cms.CMSAttributes;
import net.jsign.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import net.jsign.bouncycastle.asn1.cms.SignerIdentifier;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;
import net.jsign.bouncycastle.asn1.cms.Time;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.DigestInfo;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.operator.ContentVerifier;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.operator.RawContentVerifier;
import net.jsign.bouncycastle.util.Arrays;
import net.jsign.bouncycastle.util.io.TeeOutputStream;

public class SignerInformation {
   private final SignerId sid;
   private final CMSProcessable content;
   private final byte[] signature;
   private final ASN1ObjectIdentifier contentType;
   private final boolean isCounterSignature;
   private AttributeTable signedAttributeValues;
   private AttributeTable unsignedAttributeValues;
   private byte[] resultDigest;
   protected final SignerInfo info;
   protected final AlgorithmIdentifier digestAlgorithm;
   protected final AlgorithmIdentifier encryptionAlgorithm;
   protected final ASN1Set signedAttributeSet;
   protected final ASN1Set unsignedAttributeSet;

   SignerInformation(SignerInfo var1, ASN1ObjectIdentifier var2, CMSProcessable var3, byte[] var4) {
      this.info = var1;
      this.contentType = var2;
      this.isCounterSignature = var2 == null;
      SignerIdentifier var5 = var1.getSID();
      if (var5.isTagged()) {
         ASN1OctetString var6 = ASN1OctetString.getInstance(var5.getId());
         this.sid = new SignerId(var6.getOctets());
      } else {
         IssuerAndSerialNumber var7 = IssuerAndSerialNumber.getInstance(var5.getId());
         this.sid = new SignerId(var7.getName(), var7.getSerialNumber().getValue());
      }

      this.digestAlgorithm = var1.getDigestAlgorithm();
      this.signedAttributeSet = var1.getAuthenticatedAttributes();
      this.unsignedAttributeSet = var1.getUnauthenticatedAttributes();
      this.encryptionAlgorithm = var1.getDigestEncryptionAlgorithm();
      this.signature = var1.getEncryptedDigest().getOctets();
      this.content = var3;
      this.resultDigest = var4;
   }

   protected SignerInformation(SignerInformation var1) {
      this.info = var1.info;
      this.contentType = var1.contentType;
      this.isCounterSignature = var1.isCounterSignature();
      this.sid = var1.getSID();
      this.digestAlgorithm = this.info.getDigestAlgorithm();
      this.signedAttributeSet = this.info.getAuthenticatedAttributes();
      this.unsignedAttributeSet = this.info.getUnauthenticatedAttributes();
      this.encryptionAlgorithm = this.info.getDigestEncryptionAlgorithm();
      this.signature = this.info.getEncryptedDigest().getOctets();
      this.content = var1.content;
      this.resultDigest = var1.resultDigest;
   }

   public boolean isCounterSignature() {
      return this.isCounterSignature;
   }

   public ASN1ObjectIdentifier getContentType() {
      return this.contentType;
   }

   private byte[] encodeObj(ASN1Encodable var1) throws IOException {
      return var1 != null ? var1.toASN1Primitive().getEncoded() : null;
   }

   public SignerId getSID() {
      return this.sid;
   }

   public int getVersion() {
      return this.info.getVersion().getValue().intValue();
   }

   public AlgorithmIdentifier getDigestAlgorithmID() {
      return this.digestAlgorithm;
   }

   public String getDigestAlgOID() {
      return this.digestAlgorithm.getAlgorithm().getId();
   }

   public byte[] getDigestAlgParams() {
      try {
         return this.encodeObj(this.digestAlgorithm.getParameters());
      } catch (Exception var2) {
         throw new RuntimeException("exception getting digest parameters " + var2);
      }
   }

   public byte[] getContentDigest() {
      if (this.resultDigest == null) {
         throw new IllegalStateException("method can only be called after verify.");
      } else {
         return Arrays.clone(this.resultDigest);
      }
   }

   public String getEncryptionAlgOID() {
      return this.encryptionAlgorithm.getAlgorithm().getId();
   }

   public byte[] getEncryptionAlgParams() {
      try {
         return this.encodeObj(this.encryptionAlgorithm.getParameters());
      } catch (Exception var2) {
         throw new RuntimeException("exception getting encryption parameters " + var2);
      }
   }

   public AttributeTable getSignedAttributes() {
      if (this.signedAttributeSet != null && this.signedAttributeValues == null) {
         this.signedAttributeValues = new AttributeTable(this.signedAttributeSet);
      }

      return this.signedAttributeValues;
   }

   public AttributeTable getUnsignedAttributes() {
      if (this.unsignedAttributeSet != null && this.unsignedAttributeValues == null) {
         this.unsignedAttributeValues = new AttributeTable(this.unsignedAttributeSet);
      }

      return this.unsignedAttributeValues;
   }

   public byte[] getSignature() {
      return Arrays.clone(this.signature);
   }

   public SignerInformationStore getCounterSignatures() {
      AttributeTable var1 = this.getUnsignedAttributes();
      if (var1 == null) {
         return new SignerInformationStore(new ArrayList(0));
      } else {
         ArrayList var2 = new ArrayList();
         ASN1EncodableVector var3 = var1.getAll(CMSAttributes.counterSignature);

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            Attribute var5 = (Attribute)var3.get(var4);
            ASN1Set var6 = var5.getAttrValues();
            if (var6.size() < 1) {
            }

            Enumeration var7 = var6.getObjects();

            while(var7.hasMoreElements()) {
               SignerInfo var8 = SignerInfo.getInstance(var7.nextElement());
               var2.add(new SignerInformation(var8, (ASN1ObjectIdentifier)null, new CMSProcessableByteArray(this.getSignature()), (byte[])null));
            }
         }

         return new SignerInformationStore(var2);
      }
   }

   public byte[] getEncodedSignedAttributes() throws IOException {
      return this.signedAttributeSet != null ? this.signedAttributeSet.getEncoded("DER") : null;
   }

   private boolean doVerify(SignerInformationVerifier var1) throws CMSException {
      String var2 = CMSSignedHelper.INSTANCE.getEncryptionAlgName(this.getEncryptionAlgOID());

      ContentVerifier var3;
      try {
         var3 = var1.getContentVerifier(this.encryptionAlgorithm, this.info.getDigestAlgorithm());
      } catch (OperatorCreationException var12) {
         throw new CMSException("can't create content verifier: " + var12.getMessage(), var12);
      }

      try {
         OutputStream var4 = var3.getOutputStream();
         if (this.resultDigest == null) {
            DigestCalculator var5 = var1.getDigestCalculator(this.getDigestAlgorithmID());
            if (this.content != null) {
               OutputStream var6 = var5.getOutputStream();
               if (this.signedAttributeSet == null) {
                  if (var3 instanceof RawContentVerifier) {
                     this.content.write(var6);
                  } else {
                     TeeOutputStream var7 = new TeeOutputStream(var6, var4);
                     this.content.write(var7);
                     var7.close();
                  }
               } else {
                  this.content.write(var6);
                  var4.write(this.getEncodedSignedAttributes());
               }

               var6.close();
            } else {
               if (this.signedAttributeSet == null) {
                  throw new CMSException("data not encapsulated in signature - use detached constructor.");
               }

               var4.write(this.getEncodedSignedAttributes());
            }

            this.resultDigest = var5.getDigest();
         } else if (this.signedAttributeSet == null) {
            if (this.content != null) {
               this.content.write(var4);
            }
         } else {
            var4.write(this.getEncodedSignedAttributes());
         }

         var4.close();
      } catch (IOException var10) {
         throw new CMSException("can't process mime object to create signature.", var10);
      } catch (OperatorCreationException var11) {
         throw new CMSException("can't create digest calculator: " + var11.getMessage(), var11);
      }

      ASN1Primitive var13 = this.getSingleValuedSignedAttribute(CMSAttributes.contentType, "content-type");
      if (var13 == null) {
         if (!this.isCounterSignature && this.signedAttributeSet != null) {
            throw new CMSException("The content-type attribute type MUST be present whenever signed attributes are present in signed-data");
         }
      } else {
         if (this.isCounterSignature) {
            throw new CMSException("[For counter signatures,] the signedAttributes field MUST NOT contain a content-type attribute");
         }

         if (!(var13 instanceof ASN1ObjectIdentifier)) {
            throw new CMSException("content-type attribute value not of ASN.1 type 'OBJECT IDENTIFIER'");
         }

         ASN1ObjectIdentifier var15 = (ASN1ObjectIdentifier)var13;
         if (!var15.equals(this.contentType)) {
            throw new CMSException("content-type attribute value does not match eContentType");
         }
      }

      AttributeTable var14 = this.getSignedAttributes();
      AttributeTable var16 = this.getUnsignedAttributes();
      if (var16 != null && var16.getAll(CMSAttributes.cmsAlgorithmProtect).size() > 0) {
         throw new CMSException("A cmsAlgorithmProtect attribute MUST be a signed attribute");
      } else {
         ASN1EncodableVector var18;
         if (var14 != null) {
            var18 = var14.getAll(CMSAttributes.cmsAlgorithmProtect);
            if (var18.size() > 1) {
               throw new CMSException("Only one instance of a cmsAlgorithmProtect attribute can be present");
            }

            if (var18.size() > 0) {
               Attribute var21 = Attribute.getInstance(var18.get(0));
               if (var21.getAttrValues().size() != 1) {
                  throw new CMSException("A cmsAlgorithmProtect attribute MUST contain exactly one value");
               }

               CMSAlgorithmProtection var8 = CMSAlgorithmProtection.getInstance(var21.getAttributeValues()[0]);
               if (!CMSUtils.isEquivalent(var8.getDigestAlgorithm(), this.info.getDigestAlgorithm())) {
                  throw new CMSException("CMS Algorithm Identifier Protection check failed for digestAlgorithm");
               }

               if (!CMSUtils.isEquivalent(var8.getSignatureAlgorithm(), this.info.getDigestEncryptionAlgorithm())) {
                  throw new CMSException("CMS Algorithm Identifier Protection check failed for signatureAlgorithm");
               }
            }
         }

         ASN1Primitive var17 = this.getSingleValuedSignedAttribute(CMSAttributes.messageDigest, "message-digest");
         if (var17 == null) {
            if (this.signedAttributeSet != null) {
               throw new CMSException("the message-digest signed attribute type MUST be present when there are any signed attributes present");
            }
         } else {
            if (!(var17 instanceof ASN1OctetString)) {
               throw new CMSException("message-digest attribute value not of ASN.1 type 'OCTET STRING'");
            }

            ASN1OctetString var19 = (ASN1OctetString)var17;
            if (!Arrays.constantTimeAreEqual(this.resultDigest, var19.getOctets())) {
               throw new CMSSignerDigestMismatchException("message-digest attribute value does not match calculated value");
            }
         }

         if (var14 != null && var14.getAll(CMSAttributes.counterSignature).size() > 0) {
            throw new CMSException("A countersignature attribute MUST NOT be a signed attribute");
         } else {
            var16 = this.getUnsignedAttributes();
            if (var16 != null) {
               var18 = var16.getAll(CMSAttributes.counterSignature);

               for(int var23 = 0; var23 < var18.size(); ++var23) {
                  Attribute var24 = Attribute.getInstance(var18.get(var23));
                  if (var24.getAttrValues().size() < 1) {
                     throw new CMSException("A countersignature attribute MUST contain at least one AttributeValue");
                  }
               }
            }

            try {
               if (this.signedAttributeSet == null && this.resultDigest != null && var3 instanceof RawContentVerifier) {
                  RawContentVerifier var20 = (RawContentVerifier)var3;
                  if (var2.equals("RSA")) {
                     DigestInfo var22 = new DigestInfo(new AlgorithmIdentifier(this.digestAlgorithm.getAlgorithm(), DERNull.INSTANCE), this.resultDigest);
                     return var20.verify(var22.getEncoded("DER"), this.getSignature());
                  } else {
                     return var20.verify(this.resultDigest, this.getSignature());
                  }
               } else {
                  return var3.verify(this.getSignature());
               }
            } catch (IOException var9) {
               throw new CMSException("can't process mime object to create signature.", var9);
            }
         }
      }
   }

   public boolean verify(SignerInformationVerifier var1) throws CMSException {
      Time var2 = this.getSigningTime();
      if (var1.hasAssociatedCertificate() && var2 != null) {
         X509CertificateHolder var3 = var1.getAssociatedCertificate();
         if (!var3.isValidOn(var2.getDate())) {
            throw new CMSVerifierCertificateNotValidException("verifier not valid at signingTime");
         }
      }

      return this.doVerify(var1);
   }

   public SignerInfo toASN1Structure() {
      return this.info;
   }

   private ASN1Primitive getSingleValuedSignedAttribute(ASN1ObjectIdentifier var1, String var2) throws CMSException {
      AttributeTable var3 = this.getUnsignedAttributes();
      if (var3 != null && var3.getAll(var1).size() > 0) {
         throw new CMSException("The " + var2 + " attribute MUST NOT be an unsigned attribute");
      } else {
         AttributeTable var4 = this.getSignedAttributes();
         if (var4 == null) {
            return null;
         } else {
            ASN1EncodableVector var5 = var4.getAll(var1);
            switch (var5.size()) {
               case 0:
                  return null;
               case 1:
                  Attribute var6 = (Attribute)var5.get(0);
                  ASN1Set var7 = var6.getAttrValues();
                  if (var7.size() != 1) {
                     throw new CMSException("A " + var2 + " attribute MUST have a single attribute value");
                  }

                  return var7.getObjectAt(0).toASN1Primitive();
               default:
                  throw new CMSException("The SignedAttributes in a signerInfo MUST NOT include multiple instances of the " + var2 + " attribute");
            }
         }
      }
   }

   private Time getSigningTime() throws CMSException {
      ASN1Primitive var1 = this.getSingleValuedSignedAttribute(CMSAttributes.signingTime, "signing-time");
      if (var1 == null) {
         return null;
      } else {
         try {
            return Time.getInstance(var1);
         } catch (IllegalArgumentException var3) {
            throw new CMSException("signing-time attribute value not a valid 'Time' structure");
         }
      }
   }

   public static SignerInformation replaceUnsignedAttributes(SignerInformation var0, AttributeTable var1) {
      SignerInfo var2 = var0.info;
      DERSet var3 = null;
      if (var1 != null) {
         var3 = new DERSet(var1.toASN1EncodableVector());
      }

      return new SignerInformation(new SignerInfo(var2.getSID(), var2.getDigestAlgorithm(), var2.getAuthenticatedAttributes(), var2.getDigestEncryptionAlgorithm(), var2.getEncryptedDigest(), var3), var0.contentType, var0.content, (byte[])null);
   }

   public static SignerInformation addCounterSigners(SignerInformation var0, SignerInformationStore var1) {
      SignerInfo var2 = var0.info;
      AttributeTable var3 = var0.getUnsignedAttributes();
      ASN1EncodableVector var4;
      if (var3 != null) {
         var4 = var3.toASN1EncodableVector();
      } else {
         var4 = new ASN1EncodableVector();
      }

      ASN1EncodableVector var5 = new ASN1EncodableVector();
      Iterator var6 = var1.getSigners().iterator();

      while(var6.hasNext()) {
         var5.add(((SignerInformation)var6.next()).toASN1Structure());
      }

      var4.add(new Attribute(CMSAttributes.counterSignature, new DERSet(var5)));
      return new SignerInformation(new SignerInfo(var2.getSID(), var2.getDigestAlgorithm(), var2.getAuthenticatedAttributes(), var2.getDigestEncryptionAlgorithm(), var2.getEncryptedDigest(), new DERSet(var4)), var0.contentType, var0.content, (byte[])null);
   }
}
