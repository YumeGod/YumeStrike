package net.jsign.asn1.authenticode;

import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DEROctetString;

public class SpcUuid extends ASN1Object {
   private static final DEROctetString UUID = new DEROctetString((new BigInteger("a6b586d5b4a12466ae05a217da8e60d6", 16)).toByteArray());

   public ASN1Primitive toASN1Primitive() {
      return UUID;
   }
}
