package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.DERBitString;
import net.jsign.bouncycastle.asn1.DERTaggedObject;

public class SpcPeImageData extends ASN1Object {
   private DERBitString flags = new DERBitString(new byte[0]);
   private SpcLink file = new SpcLink();

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.flags);
      v.add(new DERTaggedObject(0, this.file));
      return new BERSequence(v);
   }
}
