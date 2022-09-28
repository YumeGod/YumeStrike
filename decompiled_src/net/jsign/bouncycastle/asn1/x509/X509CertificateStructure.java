package net.jsign.bouncycastle.asn1.x509;

import net.jsign.bouncycastle.asn1.ASN1Integer;
import net.jsign.bouncycastle.asn1.ASN1Object;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.DERBitString;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x500.X500Name;

/** @deprecated */
public class X509CertificateStructure extends ASN1Object implements X509ObjectIdentifiers, PKCSObjectIdentifiers {
   ASN1Sequence seq;
   TBSCertificateStructure tbsCert;
   AlgorithmIdentifier sigAlgId;
   DERBitString sig;

   public static X509CertificateStructure getInstance(ASN1TaggedObject var0, boolean var1) {
      return getInstance(ASN1Sequence.getInstance(var0, var1));
   }

   public static X509CertificateStructure getInstance(Object var0) {
      if (var0 instanceof X509CertificateStructure) {
         return (X509CertificateStructure)var0;
      } else {
         return var0 != null ? new X509CertificateStructure(ASN1Sequence.getInstance(var0)) : null;
      }
   }

   public X509CertificateStructure(ASN1Sequence var1) {
      this.seq = var1;
      if (var1.size() == 3) {
         this.tbsCert = TBSCertificateStructure.getInstance(var1.getObjectAt(0));
         this.sigAlgId = AlgorithmIdentifier.getInstance(var1.getObjectAt(1));
         this.sig = DERBitString.getInstance(var1.getObjectAt(2));
      } else {
         throw new IllegalArgumentException("sequence wrong size for a certificate");
      }
   }

   public TBSCertificateStructure getTBSCertificate() {
      return this.tbsCert;
   }

   public int getVersion() {
      return this.tbsCert.getVersion();
   }

   public ASN1Integer getSerialNumber() {
      return this.tbsCert.getSerialNumber();
   }

   public X500Name getIssuer() {
      return this.tbsCert.getIssuer();
   }

   public Time getStartDate() {
      return this.tbsCert.getStartDate();
   }

   public Time getEndDate() {
      return this.tbsCert.getEndDate();
   }

   public X500Name getSubject() {
      return this.tbsCert.getSubject();
   }

   public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
      return this.tbsCert.getSubjectPublicKeyInfo();
   }

   public AlgorithmIdentifier getSignatureAlgorithm() {
      return this.sigAlgId;
   }

   public DERBitString getSignature() {
      return this.sig;
   }

   public ASN1Primitive toASN1Primitive() {
      return this.seq;
   }
}
