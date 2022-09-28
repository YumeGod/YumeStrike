package net.jsign.bouncycastle.asn1.cms;

import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.BERTaggedObject;

public class ContentInfo extends ASN1Object implements CMSObjectIdentifiers {
   private ASN1ObjectIdentifier contentType;
   private ASN1Encodable content;

   public static ContentInfo getInstance(Object var0) {
      if (var0 instanceof ContentInfo) {
         return (ContentInfo)var0;
      } else {
         return var0 != null ? new ContentInfo(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   public static ContentInfo getInstance(ASN1TaggedObject var0, boolean var1) {
      return getInstance(ASN1Sequence.getInstance(var0, var1));
   }

   /** @deprecated */
   public ContentInfo(ASN1Sequence var1) {
      if (var1.size() >= 1 && var1.size() <= 2) {
         this.contentType = (ASN1ObjectIdentifier)var1.getObjectAt(0);
         if (var1.size() > 1) {
            ASN1TaggedObject var2 = (ASN1TaggedObject)var1.getObjectAt(1);
            if (!var2.isExplicit() || var2.getTagNo() != 0) {
               throw new IllegalArgumentException("Bad tag for 'content'");
            }

            this.content = var2.getObject();
         }

      } else {
         throw new IllegalArgumentException("Bad sequence size: " + var1.size());
      }
   }

   public ContentInfo(ASN1ObjectIdentifier var1, ASN1Encodable var2) {
      this.contentType = var1;
      this.content = var2;
   }

   public ASN1ObjectIdentifier getContentType() {
      return this.contentType;
   }

   public ASN1Encodable getContent() {
      return this.content;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.contentType);
      if (this.content != null) {
         var1.add(new BERTaggedObject(0, this.content));
      }

      return new BERSequence(var1);
   }
}
