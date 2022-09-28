package net.jsign.bouncycastle.asn1.x509;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.DERSequence;

public class PolicyInformation extends ASN1Object {
   private ASN1ObjectIdentifier policyIdentifier;
   private ASN1Sequence policyQualifiers;

   private PolicyInformation(ASN1Sequence var1) {
      if (var1.size() >= 1 && var1.size() <= 2) {
         this.policyIdentifier = ASN1ObjectIdentifier.getInstance(var1.getObjectAt(0));
         if (var1.size() > 1) {
            this.policyQualifiers = ASN1Sequence.getInstance(var1.getObjectAt(1));
         }

      } else {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      }
   }

   public PolicyInformation(ASN1ObjectIdentifier var1) {
      this.policyIdentifier = var1;
   }

   public PolicyInformation(ASN1ObjectIdentifier var1, ASN1Sequence var2) {
      this.policyIdentifier = var1;
      this.policyQualifiers = var2;
   }

   public static PolicyInformation getInstance(Object var0) {
      return var0 != null && !(var0 instanceof PolicyInformation) ? new PolicyInformation(ASN1Sequence.getInstance(var0)) : (PolicyInformation)var0;
   }

   public ASN1ObjectIdentifier getPolicyIdentifier() {
      return this.policyIdentifier;
   }

   public ASN1Sequence getPolicyQualifiers() {
      return this.policyQualifiers;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.policyIdentifier);
      if (this.policyQualifiers != null) {
         var1.add(this.policyQualifiers);
      }

      return new DERSequence(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Policy information: ");
      var1.append(this.policyIdentifier);
      if (this.policyQualifiers != null) {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < this.policyQualifiers.size(); ++var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(PolicyQualifierInfo.getInstance(this.policyQualifiers.getObjectAt(var3)));
         }

         var1.append("[");
         var1.append(var2);
         var1.append("]");
      }

      return var1.toString();
   }
}
