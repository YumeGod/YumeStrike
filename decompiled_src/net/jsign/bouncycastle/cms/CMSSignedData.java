package net.jsign.bouncycastle.cms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1InputStream;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1OctetString;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.SignedData;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.util.Encodable;
import net.jsign.bouncycastle.util.Store;

public class CMSSignedData implements Encodable {
   private static final CMSSignedHelper HELPER;
   SignedData signedData;
   ContentInfo contentInfo;
   CMSTypedData signedContent;
   SignerInformationStore signerInfoStore;
   private Map hashes;

   private CMSSignedData(CMSSignedData var1) {
      this.signedData = var1.signedData;
      this.contentInfo = var1.contentInfo;
      this.signedContent = var1.signedContent;
      this.signerInfoStore = var1.signerInfoStore;
   }

   public CMSSignedData(byte[] var1) throws CMSException {
      this(CMSUtils.readContentInfo(var1));
   }

   public CMSSignedData(CMSProcessable var1, byte[] var2) throws CMSException {
      this(var1, CMSUtils.readContentInfo(var2));
   }

   public CMSSignedData(Map var1, byte[] var2) throws CMSException {
      this(var1, CMSUtils.readContentInfo(var2));
   }

   public CMSSignedData(CMSProcessable var1, InputStream var2) throws CMSException {
      this(var1, CMSUtils.readContentInfo((InputStream)(new ASN1InputStream(var2))));
   }

   public CMSSignedData(InputStream var1) throws CMSException {
      this(CMSUtils.readContentInfo(var1));
   }

   public CMSSignedData(final CMSProcessable var1, ContentInfo var2) throws CMSException {
      if (var1 instanceof CMSTypedData) {
         this.signedContent = (CMSTypedData)var1;
      } else {
         this.signedContent = new CMSTypedData() {
            public ASN1ObjectIdentifier getContentType() {
               return CMSSignedData.this.signedData.getEncapContentInfo().getContentType();
            }

            public void write(OutputStream var1x) throws IOException, CMSException {
               var1.write(var1x);
            }

            public Object getContent() {
               return var1.getContent();
            }
         };
      }

      this.contentInfo = var2;
      this.signedData = this.getSignedData();
   }

   public CMSSignedData(Map var1, ContentInfo var2) throws CMSException {
      this.hashes = var1;
      this.contentInfo = var2;
      this.signedData = this.getSignedData();
   }

   public CMSSignedData(ContentInfo var1) throws CMSException {
      this.contentInfo = var1;
      this.signedData = this.getSignedData();
      if (this.signedData.getEncapContentInfo().getContent() != null) {
         this.signedContent = new CMSProcessableByteArray(this.signedData.getEncapContentInfo().getContentType(), ((ASN1OctetString)((ASN1OctetString)this.signedData.getEncapContentInfo().getContent())).getOctets());
      } else {
         this.signedContent = null;
      }

   }

   private SignedData getSignedData() throws CMSException {
      try {
         return SignedData.getInstance(this.contentInfo.getContent());
      } catch (ClassCastException var2) {
         throw new CMSException("Malformed content.", var2);
      } catch (IllegalArgumentException var3) {
         throw new CMSException("Malformed content.", var3);
      }
   }

   public int getVersion() {
      return this.signedData.getVersion().getValue().intValue();
   }

   public SignerInformationStore getSignerInfos() {
      if (this.signerInfoStore == null) {
         ASN1Set var1 = this.signedData.getSignerInfos();
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 != var1.size(); ++var3) {
            SignerInfo var4 = SignerInfo.getInstance(var1.getObjectAt(var3));
            ASN1ObjectIdentifier var5 = this.signedData.getEncapContentInfo().getContentType();
            if (this.hashes == null) {
               var2.add(new SignerInformation(var4, var5, this.signedContent, (byte[])null));
            } else {
               Object var6 = this.hashes.keySet().iterator().next();
               byte[] var7 = var6 instanceof String ? (byte[])((byte[])this.hashes.get(var4.getDigestAlgorithm().getAlgorithm().getId())) : (byte[])((byte[])this.hashes.get(var4.getDigestAlgorithm().getAlgorithm()));
               var2.add(new SignerInformation(var4, var5, (CMSProcessable)null, var7));
            }
         }

         this.signerInfoStore = new SignerInformationStore(var2);
      }

      return this.signerInfoStore;
   }

   public boolean isDetachedSignature() {
      return this.signedData.getEncapContentInfo().getContent() == null && this.signedData.getSignerInfos().size() > 0;
   }

   public boolean isCertificateManagementMessage() {
      return this.signedData.getEncapContentInfo().getContent() == null && this.signedData.getSignerInfos().size() == 0;
   }

   public Store getCertificates() {
      return HELPER.getCertificates(this.signedData.getCertificates());
   }

   public Store getCRLs() {
      return HELPER.getCRLs(this.signedData.getCRLs());
   }

   public Store getAttributeCertificates() {
      return HELPER.getAttributeCertificates(this.signedData.getCertificates());
   }

   public Store getOtherRevocationInfo(ASN1ObjectIdentifier var1) {
      return HELPER.getOtherRevocationInfo(var1, this.signedData.getCRLs());
   }

   public Set getDigestAlgorithmIDs() {
      HashSet var1 = new HashSet(this.signedData.getDigestAlgorithms().size());
      Enumeration var2 = this.signedData.getDigestAlgorithms().getObjects();

      while(var2.hasMoreElements()) {
         var1.add(AlgorithmIdentifier.getInstance(var2.nextElement()));
      }

      return Collections.unmodifiableSet(var1);
   }

   public String getSignedContentTypeOID() {
      return this.signedData.getEncapContentInfo().getContentType().getId();
   }

   public CMSTypedData getSignedContent() {
      return this.signedContent;
   }

   public ContentInfo toASN1Structure() {
      return this.contentInfo;
   }

   public byte[] getEncoded() throws IOException {
      return this.contentInfo.getEncoded();
   }

   public boolean verifySignatures(SignerInformationVerifierProvider var1) throws CMSException {
      return this.verifySignatures(var1, false);
   }

   public boolean verifySignatures(SignerInformationVerifierProvider var1, boolean var2) throws CMSException {
      Collection var3 = this.getSignerInfos().getSigners();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         SignerInformation var5 = (SignerInformation)var4.next();

         try {
            SignerInformationVerifier var6 = var1.get(var5.getSID());
            if (!var5.verify(var6)) {
               return false;
            }

            if (!var2) {
               Collection var7 = var5.getCounterSignatures().getSigners();
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  if (!this.verifyCounterSignature((SignerInformation)var8.next(), var1)) {
                     return false;
                  }
               }
            }
         } catch (OperatorCreationException var9) {
            throw new CMSException("failure in verifier provider: " + var9.getMessage(), var9);
         }
      }

      return true;
   }

   private boolean verifyCounterSignature(SignerInformation var1, SignerInformationVerifierProvider var2) throws OperatorCreationException, CMSException {
      SignerInformationVerifier var3 = var2.get(var1.getSID());
      if (!var1.verify(var3)) {
         return false;
      } else {
         Collection var4 = var1.getCounterSignatures().getSigners();
         Iterator var5 = var4.iterator();

         do {
            if (!var5.hasNext()) {
               return true;
            }
         } while(this.verifyCounterSignature((SignerInformation)var5.next(), var2));

         return false;
      }
   }

   public static CMSSignedData replaceSigners(CMSSignedData var0, SignerInformationStore var1) {
      CMSSignedData var2 = new CMSSignedData(var0);
      var2.signerInfoStore = var1;
      ASN1EncodableVector var3 = new ASN1EncodableVector();
      ASN1EncodableVector var4 = new ASN1EncodableVector();
      Iterator var5 = var1.getSigners().iterator();

      while(var5.hasNext()) {
         SignerInformation var6 = (SignerInformation)var5.next();
         var3.add(CMSSignedHelper.INSTANCE.fixAlgID(var6.getDigestAlgorithmID()));
         var4.add(var6.toASN1Structure());
      }

      DERSet var10 = new DERSet(var3);
      DERSet var7 = new DERSet(var4);
      ASN1Sequence var8 = (ASN1Sequence)var0.signedData.toASN1Primitive();
      var4 = new ASN1EncodableVector();
      var4.add(var8.getObjectAt(0));
      var4.add(var10);

      for(int var9 = 2; var9 != var8.size() - 1; ++var9) {
         var4.add(var8.getObjectAt(var9));
      }

      var4.add(var7);
      var2.signedData = SignedData.getInstance(new BERSequence(var4));
      var2.contentInfo = new ContentInfo(var2.contentInfo.getContentType(), var2.signedData);
      return var2;
   }

   public static CMSSignedData replaceCertificatesAndCRLs(CMSSignedData var0, Store var1, Store var2, Store var3) throws CMSException {
      CMSSignedData var4 = new CMSSignedData(var0);
      ASN1Set var5 = null;
      ASN1Set var6 = null;
      if (var1 != null || var2 != null) {
         ArrayList var7 = new ArrayList();
         if (var1 != null) {
            var7.addAll(CMSUtils.getCertificatesFromStore(var1));
         }

         if (var2 != null) {
            var7.addAll(CMSUtils.getAttributeCertificatesFromStore(var2));
         }

         ASN1Set var8 = CMSUtils.createBerSetFromList(var7);
         if (var8.size() != 0) {
            var5 = var8;
         }
      }

      if (var3 != null) {
         ASN1Set var9 = CMSUtils.createBerSetFromList(CMSUtils.getCRLsFromStore(var3));
         if (var9.size() != 0) {
            var6 = var9;
         }
      }

      var4.signedData = new SignedData(var0.signedData.getDigestAlgorithms(), var0.signedData.getEncapContentInfo(), var5, var6, var0.signedData.getSignerInfos());
      var4.contentInfo = new ContentInfo(var4.contentInfo.getContentType(), var4.signedData);
      return var4;
   }

   static {
      HELPER = CMSSignedHelper.INSTANCE;
   }
}
