package net.jsign.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Primitive extends ASN1Object {
   ASN1Primitive() {
   }

   public static ASN1Primitive fromByteArray(byte[] var0) throws IOException {
      ASN1InputStream var1 = new ASN1InputStream(var0);

      try {
         return var1.readObject();
      } catch (ClassCastException var3) {
         throw new IOException("cannot recognise object in stream");
      }
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof ASN1Encodable && this.asn1Equals(((ASN1Encodable)var1).toASN1Primitive());
      }
   }

   public ASN1Primitive toASN1Primitive() {
      return this;
   }

   ASN1Primitive toDERObject() {
      return this;
   }

   ASN1Primitive toDLObject() {
      return this;
   }

   public abstract int hashCode();

   abstract boolean isConstructed();

   abstract int encodedLength() throws IOException;

   abstract void encode(ASN1OutputStream var1) throws IOException;

   abstract boolean asn1Equals(ASN1Primitive var1);
}
