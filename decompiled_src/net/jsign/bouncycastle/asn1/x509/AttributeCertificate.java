package net.jsign.bouncycastle.asn1.x509;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DERBitString;
import net.jsign.bouncycastle.asn1.DERSequence;

public class AttributeCertificate extends ASN1Object {
   AttributeCertificateInfo acinfo;
   AlgorithmIdentifier signatureAlgorithm;
   DERBitString signatureValue;

   public static AttributeCertificate getInstance(Object var0) {
      if (var0 instanceof AttributeCertificate) {
         return (AttributeCertificate)var0;
      } else {
         return var0 != null ? new AttributeCertificate(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   public AttributeCertificate(AttributeCertificateInfo var1, AlgorithmIdentifier var2, DERBitString var3) {
      this.acinfo = var1;
      this.signatureAlgorithm = var2;
      this.signatureValue = var3;
   }

   /** @deprecated */
   public AttributeCertificate(ASN1Sequence var1) {
      if (var1.size() != 3) {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      } else {
         this.acinfo = AttributeCertificateInfo.getInstance(var1.getObjectAt(0));
         this.signatureAlgorithm = AlgorithmIdentifier.getInstance(var1.getObjectAt(1));
         this.signatureValue = DERBitString.getInstance(var1.getObjectAt(2));
      }
   }

   public AttributeCertificateInfo getAcinfo() {
      return this.acinfo;
   }

   public AlgorithmIdentifier getSignatureAlgorithm() {
      return this.signatureAlgorithm;
   }

   public DERBitString getSignatureValue() {
      return this.signatureValue;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.acinfo);
      var1.add(this.signatureAlgorithm);
      var1.add(this.signatureValue);
      return new DERSequence(var1);
   }
}
