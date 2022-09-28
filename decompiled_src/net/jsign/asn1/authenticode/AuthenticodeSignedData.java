package net.jsign.asn1.authenticode;

import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.BERSequence;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.DERTaggedObject;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.SignerInfo;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class AuthenticodeSignedData extends ASN1Object {
   private AlgorithmIdentifier digestAlgorithm;
   private ContentInfo contentInfo;
   private ASN1Set certificates;
   private SignerInfo signerInformation;

   public AuthenticodeSignedData(AlgorithmIdentifier digestAlgorithm, ContentInfo contentInfo, ASN1Set certificates, SignerInfo signerInformation) {
      this.digestAlgorithm = digestAlgorithm;
      this.contentInfo = contentInfo;
      this.certificates = certificates;
      this.signerInformation = signerInformation;
   }

   public ASN1Primitive toASN1Primitive() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(new ASN1Integer(1L));
      v.add(new DERSet(this.digestAlgorithm));
      v.add(this.contentInfo);
      if (this.certificates != null) {
         v.add(new DERTaggedObject(false, 0, this.certificates));
      }

      v.add(new DERSet(this.signerInformation));
      return new BERSequence(v);
   }
}
