package net.jsign.bouncycastle.asn1;

import java.io.IOException;

public class BERSetParser implements ASN1SetParser {
   private ASN1StreamParser _parser;

   BERSetParser(ASN1StreamParser var1) {
      this._parser = var1;
   }

   public ASN1Encodable readObject() throws IOException {
      return this._parser.readObject();
   }

   public ASN1Primitive getLoadedObject() throws IOException {
      return new BERSet(this._parser.readVector());
   }

   public ASN1Primitive toASN1Primitive() {
      try {
         return this.getLoadedObject();
      } catch (IOException var2) {
         throw new ASN1ParsingException(var2.getMessage(), var2);
      }
   }
}
