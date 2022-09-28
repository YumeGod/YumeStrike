package net.jsign.bouncycastle.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString {
   public DEROctetString(byte[] var1) {
      super(var1);
   }

   public DEROctetString(ASN1Encodable var1) throws IOException {
      super(var1.toASN1Primitive().getEncoded("DER"));
   }

   boolean isConstructed() {
      return false;
   }

   int encodedLength() {
      return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
   }

   void encode(ASN1OutputStream var1) throws IOException {
      var1.writeEncoded(4, this.string);
   }

   static void encode(DEROutputStream var0, byte[] var1) throws IOException {
      var0.writeEncoded(4, var1);
   }
}
