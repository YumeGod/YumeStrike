package net.jsign.bouncycastle.asn1.ess;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.x509.PolicyInformation;

public class SigningCertificateV2 extends ASN1Object {
   ASN1Sequence certs;
   ASN1Sequence policies;

   public static SigningCertificateV2 getInstance(Object var0) {
      if (var0 != null && !(var0 instanceof SigningCertificateV2)) {
         return var0 instanceof ASN1Sequence ? new SigningCertificateV2((ASN1Sequence)var0) : null;
      } else {
         return (SigningCertificateV2)var0;
      }
   }

   private SigningCertificateV2(ASN1Sequence var1) {
      if (var1.size() >= 1 && var1.size() <= 2) {
         this.certs = ASN1Sequence.getInstance(var1.getObjectAt(0));
         if (var1.size() > 1) {
            this.policies = ASN1Sequence.getInstance(var1.getObjectAt(1));
         }

      } else {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      }
   }

   public SigningCertificateV2(ESSCertIDv2 var1) {
      this.certs = new DERSequence(var1);
   }

   public SigningCertificateV2(ESSCertIDv2[] var1) {
      ASN1EncodableVector var2 = new ASN1EncodableVector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3]);
      }

      this.certs = new DERSequence(var2);
   }

   public SigningCertificateV2(ESSCertIDv2[] var1, PolicyInformation[] var2) {
      ASN1EncodableVector var3 = new ASN1EncodableVector();

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3.add(var1[var4]);
      }

      this.certs = new DERSequence(var3);
      if (var2 != null) {
         var3 = new ASN1EncodableVector();

         for(var4 = 0; var4 < var2.length; ++var4) {
            var3.add(var2[var4]);
         }

         this.policies = new DERSequence(var3);
      }

   }

   public ESSCertIDv2[] getCerts() {
      ESSCertIDv2[] var1 = new ESSCertIDv2[this.certs.size()];

      for(int var2 = 0; var2 != this.certs.size(); ++var2) {
         var1[var2] = ESSCertIDv2.getInstance(this.certs.getObjectAt(var2));
      }

      return var1;
   }

   public PolicyInformation[] getPolicies() {
      if (this.policies == null) {
         return null;
      } else {
         PolicyInformation[] var1 = new PolicyInformation[this.policies.size()];

         for(int var2 = 0; var2 != this.policies.size(); ++var2) {
            var1[var2] = PolicyInformation.getInstance(this.policies.getObjectAt(var2));
         }

         return var1;
      }
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.certs);
      if (this.policies != null) {
         var1.add(this.policies);
      }

      return new DERSequence(var1);
   }
}
