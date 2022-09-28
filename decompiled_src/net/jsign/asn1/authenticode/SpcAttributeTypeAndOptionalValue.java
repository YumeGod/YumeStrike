package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.BERSequence;

public class SpcAttributeTypeAndOptionalValue extends ASN1Object {
   private ASN1ObjectIdentifier type;
   private ASN1Object value;

   public SpcAttributeTypeAndOptionalValue(ASN1ObjectIdentifier type, ASN1Object value) {
      this.type = type;
      this.value = value;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.type);
      if (this.value != null) {
         v.add(this.value);
      }

      return new BERSequence(v);
   }
}
