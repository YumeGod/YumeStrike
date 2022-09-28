package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1Choice;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.DERIA5String;
import net.jsign.bouncycastle.asn1.DERTaggedObject;

public class SpcLink extends ASN1Object implements ASN1Choice {
   private DERIA5String url;
   private SpcSerializedObject moniker;
   private SpcString file = new SpcString("<<<Obsolete>>>");

   public SpcLink() {
   }

   public SpcLink(String url) {
      this.url = new DERIA5String(url);
   }

   public ASN1Primitive toASN1Primitive() {
      if (this.url != null) {
         return new DERTaggedObject(false, 0, this.url);
      } else {
         return this.moniker != null ? new DERTaggedObject(false, 1, this.moniker) : new DERTaggedObject(false, 2, this.file);
      }
   }
}
