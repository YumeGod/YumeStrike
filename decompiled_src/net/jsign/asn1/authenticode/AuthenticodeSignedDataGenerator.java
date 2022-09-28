package net.jsign.asn1.authenticode;

import java.io.IOException;
import java.io.OutputStream;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;
import net.jsign.bouncycastle.cms.CMSException;
import net.jsign.bouncycastle.cms.CMSProcessableByteArray;
import net.jsign.bouncycastle.cms.CMSSignedData;
import net.jsign.bouncycastle.cms.CMSSignedDataGenerator;
import net.jsign.bouncycastle.cms.SignerInfoGenerator;
import net.jsign.bouncycastle.cms.SignerInformation;

public class AuthenticodeSignedDataGenerator extends CMSSignedDataGenerator {
   public CMSSignedData generate(ASN1ObjectIdentifier contentTypeOID, ASN1Encodable content) throws CMSException, IOException {
      this.digests.clear();
      SignerInfo signerInfo;
      if (!this._signers.isEmpty()) {
         signerInfo = ((SignerInformation)this._signers.get(0)).toASN1Structure();
      } else {
         SignerInfoGenerator signerInfoGenerator = (SignerInfoGenerator)this.signerGens.get(0);
         byte[] signedContent = content.toASN1Primitive().getEncoded("DER");
         OutputStream out = signerInfoGenerator.getCalculatingOutputStream();
         out.write(signedContent, 2, signedContent.length - 2);
         out.flush();
         out.close();
         signerInfo = signerInfoGenerator.generate(contentTypeOID);
         byte[] calculatedDigest = signerInfoGenerator.getCalculatedDigest();
         this.digests.put(signerInfoGenerator.getDigestAlgorithm().getAlgorithm().getId(), calculatedDigest);
      }

      ContentInfo encInfo = new ContentInfo(contentTypeOID, content);
      ASN1Set certificates = new DERSet((ASN1Encodable[])((ASN1Encodable[])this.certs.toArray(new ASN1Encodable[0])));
      ASN1Encodable signedData = new AuthenticodeSignedData(signerInfo.getDigestAlgorithm(), encInfo, certificates, signerInfo);
      ContentInfo contentInfo = new ContentInfo(CMSObjectIdentifiers.signedData, signedData);
      return new CMSSignedData(new CMSProcessableByteArray(contentTypeOID, content.toASN1Primitive().getEncoded("DER")), contentInfo);
   }
}
