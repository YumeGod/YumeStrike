package net.jsign.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class DEROutputStream extends ASN1OutputStream {
   public DEROutputStream(OutputStream var1) {
      super(var1);
   }

   public void writeObject(ASN1Encodable var1) throws IOException {
      if (var1 != null) {
         var1.toASN1Primitive().toDERObject().encode(this);
      } else {
         throw new IOException("null object detected");
      }
   }

   ASN1OutputStream getDERSubStream() {
      return this;
   }

   ASN1OutputStream getDLSubStream() {
      return this;
   }
}
