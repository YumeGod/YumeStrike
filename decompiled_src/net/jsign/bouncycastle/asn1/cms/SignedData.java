package net.jsign.bouncycastle.asn1.cms;

import java.util.Enumeration;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.BERSet;
import net.jsign.bouncycastle.asn1.BERTaggedObject;
import net.jsign.bouncycastle.asn1.DERTaggedObject;

public class SignedData extends ASN1Object {
   private static final ASN1Integer VERSION_1 = new ASN1Integer(1L);
   private static final ASN1Integer VERSION_3 = new ASN1Integer(3L);
   private static final ASN1Integer VERSION_4 = new ASN1Integer(4L);
   private static final ASN1Integer VERSION_5 = new ASN1Integer(5L);
   private ASN1Integer version;
   private ASN1Set digestAlgorithms;
   private ContentInfo contentInfo;
   private ASN1Set certificates;
   private ASN1Set crls;
   private ASN1Set signerInfos;
   private boolean certsBer;
   private boolean crlsBer;

   public static SignedData getInstance(Object var0) {
      if (var0 instanceof SignedData) {
         return (SignedData)var0;
      } else {
         return var0 != null ? new SignedData(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   public SignedData(ASN1Set var1, ContentInfo var2, ASN1Set var3, ASN1Set var4, ASN1Set var5) {
      this.version = this.calculateVersion(var2.getContentType(), var3, var4, var5);
      this.digestAlgorithms = var1;
      this.contentInfo = var2;
      this.certificates = var3;
      this.crls = var4;
      this.signerInfos = var5;
      this.crlsBer = var4 instanceof BERSet;
      this.certsBer = var3 instanceof BERSet;
   }

   private ASN1Integer calculateVersion(ASN1ObjectIdentifier var1, ASN1Set var2, ASN1Set var3, ASN1Set var4) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      Enumeration var9;
      Object var10;
      if (var2 != null) {
         var9 = var2.getObjects();

         while(var9.hasMoreElements()) {
            var10 = var9.nextElement();
            if (var10 instanceof ASN1TaggedObject) {
               ASN1TaggedObject var11 = ASN1TaggedObject.getInstance(var10);
               if (var11.getTagNo() == 1) {
                  var7 = true;
               } else if (var11.getTagNo() == 2) {
                  var8 = true;
               } else if (var11.getTagNo() == 3) {
                  var5 = true;
               }
            }
         }
      }

      if (var5) {
         return new ASN1Integer(5L);
      } else {
         if (var3 != null) {
            var9 = var3.getObjects();

            while(var9.hasMoreElements()) {
               var10 = var9.nextElement();
               if (var10 instanceof ASN1TaggedObject) {
                  var6 = true;
               }
            }
         }

         if (var6) {
            return VERSION_5;
         } else if (var8) {
            return VERSION_4;
         } else if (var7) {
            return VERSION_3;
         } else if (this.checkForVersion3(var4)) {
            return VERSION_3;
         } else {
            return !CMSObjectIdentifiers.data.equals(var1) ? VERSION_3 : VERSION_1;
         }
      }
   }

   private boolean checkForVersion3(ASN1Set var1) {
      Enumeration var2 = var1.getObjects();

      SignerInfo var3;
      do {
         if (!var2.hasMoreElements()) {
            return false;
         }

         var3 = SignerInfo.getInstance(var2.nextElement());
      } while(var3.getVersion().getValue().intValue() != 3);

      return true;
   }

   private SignedData(ASN1Sequence var1) {
      Enumeration var2 = var1.getObjects();
      this.version = ASN1Integer.getInstance(var2.nextElement());
      this.digestAlgorithms = (ASN1Set)var2.nextElement();
      this.contentInfo = ContentInfo.getInstance(var2.nextElement());

      while(var2.hasMoreElements()) {
         ASN1Primitive var3 = (ASN1Primitive)var2.nextElement();
         if (var3 instanceof ASN1TaggedObject) {
            ASN1TaggedObject var4 = (ASN1TaggedObject)var3;
            switch (var4.getTagNo()) {
               case 0:
                  this.certsBer = var4 instanceof BERTaggedObject;
                  this.certificates = ASN1Set.getInstance(var4, false);
                  break;
               case 1:
                  this.crlsBer = var4 instanceof BERTaggedObject;
                  this.crls = ASN1Set.getInstance(var4, false);
                  break;
               default:
                  throw new IllegalArgumentException("unknown tag value " + var4.getTagNo());
            }
         } else {
            this.signerInfos = (ASN1Set)var3;
         }
      }

   }

   public ASN1Integer getVersion() {
      return this.version;
   }

   public ASN1Set getDigestAlgorithms() {
      return this.digestAlgorithms;
   }

   public ContentInfo getEncapContentInfo() {
      return this.contentInfo;
   }

   public ASN1Set getCertificates() {
      return this.certificates;
   }

   public ASN1Set getCRLs() {
      return this.crls;
   }

   public ASN1Set getSignerInfos() {
      return this.signerInfos;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      var1.add(this.version);
      var1.add(this.digestAlgorithms);
      var1.add(this.contentInfo);
      if (this.certificates != null) {
         if (this.certsBer) {
            var1.add(new BERTaggedObject(false, 0, this.certificates));
         } else {
            var1.add(new DERTaggedObject(false, 0, this.certificates));
         }
      }

      if (this.crls != null) {
         if (this.crlsBer) {
            var1.add(new BERTaggedObject(false, 1, this.crls));
         } else {
            var1.add(new DERTaggedObject(false, 1, this.crls));
         }
      }

      var1.add(this.signerInfos);
      return new BERSequence(var1);
   }
}
