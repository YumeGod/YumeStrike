package net.jsign.bouncycastle.asn1.tsp;

import net.jsign.bouncycastle.asn1.ASN1Boolean;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.DERTaggedObject;
import net.jsign.bouncycastle.asn1.x509.Extensions;

public class TimeStampReq extends ASN1Object {
   ASN1Integer version;
   MessageImprint messageImprint;
   ASN1ObjectIdentifier tsaPolicy;
   ASN1Integer nonce;
   ASN1Boolean certReq;
   Extensions extensions;

   public static TimeStampReq getInstance(Object var0) {
      if (var0 instanceof TimeStampReq) {
         return (TimeStampReq)var0;
      } else {
         return var0 != null ? new TimeStampReq(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   private TimeStampReq(ASN1Sequence var1) {
      int var2 = var1.size();
      int var3 = 0;
      this.version = ASN1Integer.getInstance(var1.getObjectAt(var3));
      ++var3;
      this.messageImprint = MessageImprint.getInstance(var1.getObjectAt(var3));
      ++var3;

      for(int var4 = var3; var4 < var2; ++var4) {
         if (var1.getObjectAt(var4) instanceof ASN1ObjectIdentifier) {
            this.tsaPolicy = ASN1ObjectIdentifier.getInstance(var1.getObjectAt(var4));
         } else if (var1.getObjectAt(var4) instanceof ASN1Integer) {
            this.nonce = ASN1Integer.getInstance(var1.getObjectAt(var4));
         } else if (var1.getObjectAt(var4) instanceof ASN1Boolean) {
            this.certReq = ASN1Boolean.getInstance(var1.getObjectAt(var4));
         } else if (var1.getObjectAt(var4) instanceof ASN1TaggedObject) {
            ASN1TaggedObject var5 = (ASN1TaggedObject)var1.getObjectAt(var4);
            if (var5.getTagNo() == 0) {
               this.extensions = Extensions.getInstance(var5, false);
            }
         }
      }

   }

   public TimeStampReq(MessageImprint var1, ASN1ObjectIdentifier var2, ASN1Integer var3, ASN1Boolean var4, Extensions var5) {
      this.version = new ASN1Integer(1L);
      this.messageImprint = var1;
      this.tsaPolicy = var2;
      this.nonce = var3;
      this.certReq = var4;
      this.extensions = var5;
   }

   public ASN1Integer getVersion() {
      return this.version;
   }

   public MessageImprint getMessageImprint() {
      return this.messageImprint;
   }

   public ASN1ObjectIdentifier getReqPolicy() {
      return this.tsaPolicy;
   }

   public ASN1Integer getNonce() {
      return this.nonce;
   }

   public ASN1Boolean getCertReq() {
      return this.certReq;
   }

   public Extensions getExtensions() {
      return this.extensions;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.version);
      var1.add(this.messageImprint);
      if (this.tsaPolicy != null) {
         var1.add(this.tsaPolicy);
      }

      if (this.nonce != null) {
         var1.add(this.nonce);
      }

      if (this.certReq != null && this.certReq.isTrue()) {
         var1.add(this.certReq);
      }

      if (this.extensions != null) {
         var1.add(new DERTaggedObject(false, 0, this.extensions));
      }

      return new DERSequence(var1);
   }
}
