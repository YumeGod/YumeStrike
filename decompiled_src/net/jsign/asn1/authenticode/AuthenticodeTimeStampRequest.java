package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.BEROctetString;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public class AuthenticodeTimeStampRequest extends ASN1Object {
   private ContentInfo contenInfo;

   public AuthenticodeTimeStampRequest(byte[] digest) {
      this.contenInfo = new ContentInfo(PKCSObjectIdentifiers.data, new BEROctetString(digest));
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(AuthenticodeObjectIdentifiers.SPC_TIME_STAMP_REQUEST_OBJID);
      v.add(this.contenInfo);
      return new DERSequence(v);
   }
}
