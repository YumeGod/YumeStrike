package net.jsign.bouncycastle.cms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.BEROctetString;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.SignedData;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;

public class CMSSignedDataGenerator extends CMSSignedGenerator {
   private List signerInfs = new ArrayList();

   public CMSSignedData generate(CMSTypedData var1) throws CMSException {
      return this.generate(var1, false);
   }

   public CMSSignedData generate(CMSTypedData var1, boolean var2) throws CMSException {
      if (!this.signerInfs.isEmpty()) {
         throw new IllegalStateException("this method can only be used with SignerInfoGenerator");
      } else {
         ASN1EncodableVector var3 = new ASN1EncodableVector();
         ASN1EncodableVector var4 = new ASN1EncodableVector();
         this.digests.clear();
         Iterator var5 = this._signers.iterator();

         while(var5.hasNext()) {
            SignerInformation var6 = (SignerInformation)var5.next();
            var3.add(CMSSignedHelper.INSTANCE.fixAlgID(var6.getDigestAlgorithmID()));
            var4.add(var6.toASN1Structure());
         }

         ASN1ObjectIdentifier var13 = var1.getContentType();
         BEROctetString var14 = null;
         if (var1.getContent() != null) {
            ByteArrayOutputStream var7 = null;
            if (var2) {
               var7 = new ByteArrayOutputStream();
            }

            OutputStream var8 = CMSUtils.attachSignersToOutputStream(this.signerGens, var7);
            var8 = CMSUtils.getSafeOutputStream(var8);

            try {
               var1.write(var8);
               var8.close();
            } catch (IOException var12) {
               throw new CMSException("data processing exception: " + var12.getMessage(), var12);
            }

            if (var2) {
               var14 = new BEROctetString(var7.toByteArray());
            }
         }

         Iterator var15 = this.signerGens.iterator();

         while(var15.hasNext()) {
            SignerInfoGenerator var17 = (SignerInfoGenerator)var15.next();
            SignerInfo var9 = var17.generate(var13);
            var3.add(var9.getDigestAlgorithm());
            var4.add(var9);
            byte[] var10 = var17.getCalculatedDigest();
            if (var10 != null) {
               this.digests.put(var9.getDigestAlgorithm().getAlgorithm().getId(), var10);
            }
         }

         ASN1Set var16 = null;
         if (this.certs.size() != 0) {
            var16 = CMSUtils.createBerSetFromList(this.certs);
         }

         ASN1Set var18 = null;
         if (this.crls.size() != 0) {
            var18 = CMSUtils.createBerSetFromList(this.crls);
         }

         ContentInfo var19 = new ContentInfo(var13, var14);
         SignedData var20 = new SignedData(new DERSet(var3), var19, var16, var18, new DERSet(var4));
         ContentInfo var11 = new ContentInfo(CMSObjectIdentifiers.signedData, var20);
         return new CMSSignedData(var1, var11);
      }
   }

   public SignerInformationStore generateCounterSigners(SignerInformation var1) throws CMSException {
      return this.generate(new CMSProcessableByteArray((ASN1ObjectIdentifier)null, var1.getSignature()), false).getSignerInfos();
   }
}
