package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1Choice;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DERBMPString;
import net.jsign.bouncycastle.asn1.DERIA5String;
import net.jsign.bouncycastle.asn1.DERTaggedObject;

public class SpcString extends ASN1Object implements ASN1Choice {
   private DERBMPString unicode;
   private DERIA5String ascii;

   public SpcString(String string) {
      this.unicode = new DERBMPString(string);
   }

   public ASN1Primitive toASN1Primitive() {
      return this.unicode != null ? new DERTaggedObject(false, 0, this.unicode) : new DERTaggedObject(false, 1, this.ascii);
   }
}
