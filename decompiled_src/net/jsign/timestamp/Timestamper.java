package net.jsign.timestamp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import net.jsign.DigestAlgorithm;
import net.jsign.asn1.authenticode.AuthenticodeSignedDataGenerator;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.cms.AttributeTable;
import net.jsign.bouncycastle.cms.CMSException;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.cms.SignerInformation;
import net.jsign.bouncycastle.cms.SignerInformationStore;
import net.jsign.bouncycastle.util.CollectionStore;
import net.jsign.bouncycastle.util.Selector;
import net.jsign.bouncycastle.util.Store;

public abstract class Timestamper {
   protected URL tsaurl;

   public void setURL(String tsaurl) {
      try {
         this.tsaurl = new URL(tsaurl);
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("Invalid timestamping URL: " + tsaurl, var3);
      }
   }

   public CMSSignedData timestamp(DigestAlgorithm algo, CMSSignedData sigData) throws IOException, CMSException {
      CMSSignedData token = this.timestamp(algo, this.getEncryptedDigest(sigData));
      return this.modifySignedData(sigData, this.getUnsignedAttributes(token), this.getExtraCertificates(token));
   }

   private byte[] getEncryptedDigest(CMSSignedData sigData) {
      SignerInformation signerInformation = (SignerInformation)sigData.getSignerInfos().getSigners().iterator().next();
      return signerInformation.toASN1Structure().getEncryptedDigest().getOctets();
   }

   protected Collection getExtraCertificates(CMSSignedData token) {
      return null;
   }

   protected abstract AttributeTable getUnsignedAttributes(CMSSignedData var1);

   protected CMSSignedData modifySignedData(CMSSignedData sigData, AttributeTable unsignedAttributes, Collection extraCertificates) throws IOException, CMSException {
      SignerInformation signerInformation = (SignerInformation)sigData.getSignerInfos().getSigners().iterator().next();
      signerInformation = SignerInformation.replaceUnsignedAttributes(signerInformation, unsignedAttributes);
      Collection certificates = new ArrayList();
      certificates.addAll(sigData.getCertificates().getMatches((Selector)null));
      if (extraCertificates != null) {
         certificates.addAll(extraCertificates);
      }

      Store certificateStore = new CollectionStore(certificates);
      AuthenticodeSignedDataGenerator generator = new AuthenticodeSignedDataGenerator();
      generator.addCertificates(certificateStore);
      generator.addSigners(new SignerInformationStore(signerInformation));
      ASN1ObjectIdentifier contentType = new ASN1ObjectIdentifier(sigData.getSignedContentTypeOID());
      ASN1Encodable content = ASN1Sequence.getInstance(sigData.getSignedContent().getContent());
      return generator.generate(contentType, content);
   }

   protected abstract CMSSignedData timestamp(DigestAlgorithm var1, byte[] var2) throws IOException, CMSException;

   public static Timestamper create(TimestampingMode mode) {
      switch (mode) {
         case AUTHENTICODE:
            return new AuthenticodeTimestamper();
         case RFC3161:
            return new RFC3161Timestamper();
         default:
            throw new IllegalArgumentException("Unsupported timestamping mode: " + mode);
      }
   }
}
