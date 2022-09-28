package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.DERTaggedObject;

public class SpcSpOpusInfo extends ASN1Object {
   private SpcString programName;
   private SpcLink moreInfo;

   public SpcSpOpusInfo(String programName, String url) {
      if (programName != null) {
         this.programName = new SpcString(programName);
      }

      if (url != null) {
         this.moreInfo = new SpcLink(url);
      }

   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      if (this.programName != null) {
         v.add(new DERTaggedObject(true, 0, this.programName));
      }

      if (this.moreInfo != null) {
         v.add(new DERTaggedObject(true, 1, this.moreInfo));
      }

      return new BERSequence(v);
   }
}
