package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.x509.DigestInfo;

public class SpcIndirectDataContent extends ASN1Object {
   private SpcAttributeTypeAndOptionalValue data;
   private DigestInfo messageDigest;

   public SpcIndirectDataContent(SpcAttributeTypeAndOptionalValue data, DigestInfo messageDigest) {
      this.data = data;
      this.messageDigest = messageDigest;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.data);
      v.add(this.messageDigest);
      return new BERSequence(v);
   }
}
