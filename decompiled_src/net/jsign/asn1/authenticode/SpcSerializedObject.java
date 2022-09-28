package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DEROctetString;
import net.jsign.bouncycastle.asn1.DERSequence;

public class SpcSerializedObject extends ASN1Object {
   private SpcUuid classId = new SpcUuid();
   private DEROctetString serializedData;

   public SpcSerializedObject(byte[] serializedData) {
      this.serializedData = new DEROctetString(serializedData);
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.classId);
      v.add(this.serializedData);
      return new DERSequence(v);
   }
}
