package net.jsign.bouncycastle.asn1.cmp;

import java.math.BigInteger;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.DERBitString;
import net.jsign.bouncycastle.asn1.DERSequence;

public class PKIStatusInfo extends ASN1Object {
   ASN1Integer status;
   PKIFreeText statusString;
   DERBitString failInfo;

   public static PKIStatusInfo getInstance(ASN1TaggedObject var0, boolean var1) {
      return getInstance(ASN1Sequence.getInstance(var0, var1));
   }

   public static PKIStatusInfo getInstance(Object var0) {
      if (var0 instanceof PKIStatusInfo) {
         return (PKIStatusInfo)var0;
      } else {
         return var0 != null ? new PKIStatusInfo(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   private PKIStatusInfo(ASN1Sequence var1) {
      this.status = ASN1Integer.getInstance(var1.getObjectAt(0));
      this.statusString = null;
      this.failInfo = null;
      if (var1.size() > 2) {
         this.statusString = PKIFreeText.getInstance(var1.getObjectAt(1));
         this.failInfo = DERBitString.getInstance(var1.getObjectAt(2));
      } else if (var1.size() > 1) {
         ASN1Encodable var2 = var1.getObjectAt(1);
         if (var2 instanceof DERBitString) {
            this.failInfo = DERBitString.getInstance(var2);
         } else {
            this.statusString = PKIFreeText.getInstance(var2);
         }
      }

   }

   public PKIStatusInfo(PKIStatus var1) {
      this.status = ASN1Integer.getInstance(var1.toASN1Primitive());
   }

   public PKIStatusInfo(PKIStatus var1, PKIFreeText var2) {
      this.status = ASN1Integer.getInstance(var1.toASN1Primitive());
      this.statusString = var2;
   }

   public PKIStatusInfo(PKIStatus var1, PKIFreeText var2, PKIFailureInfo var3) {
      this.status = ASN1Integer.getInstance(var1.toASN1Primitive());
      this.statusString = var2;
      this.failInfo = var3;
   }

   public BigInteger getStatus() {
      return this.status.getValue();
   }

   public PKIFreeText getStatusString() {
      return this.statusString;
   }

   public DERBitString getFailInfo() {
      return this.failInfo;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.status);
      if (this.statusString != null) {
         var1.add(this.statusString);
      }

      if (this.failInfo != null) {
         var1.add(this.failInfo);
      }

      return new DERSequence(var1);
   }
}
