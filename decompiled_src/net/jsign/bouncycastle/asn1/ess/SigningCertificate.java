package net.jsign.bouncycastle.asn1.ess;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.x509.PolicyInformation;

public class SigningCertificate extends ASN1Object {
   ASN1Sequence certs;
   ASN1Sequence policies;

   public static SigningCertificate getInstance(Object var0) {
      if (var0 instanceof SigningCertificate) {
         return (SigningCertificate)var0;
      } else {
         return var0 != null ? new SigningCertificate(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   private SigningCertificate(ASN1Sequence var1) {
      if (var1.size() >= 1 && var1.size() <= 2) {
         this.certs = ASN1Sequence.getInstance(var1.getObjectAt(0));
         if (var1.size() > 1) {
            this.policies = ASN1Sequence.getInstance(var1.getObjectAt(1));
         }

      } else {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      }
   }

   public SigningCertificate(ESSCertID var1) {
      this.certs = new DERSequence(var1);
   }

   public ESSCertID[] getCerts() {
      ESSCertID[] var1 = new ESSCertID[this.certs.size()];

      for(int var2 = 0; var2 != this.certs.size(); ++var2) {
         var1[var2] = ESSCertID.getInstance(this.certs.getObjectAt(var2));
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
