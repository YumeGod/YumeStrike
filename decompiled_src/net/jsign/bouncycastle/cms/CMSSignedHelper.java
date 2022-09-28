package net.jsign.bouncycastle.cms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Primitive;
import net.jsign.bouncycastle.asn1.ASN1Sequence;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import net.jsign.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import net.jsign.bouncycastle.asn1.eac.EACObjectIdentifiers;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.AttributeCertificate;
import net.jsign.bouncycastle.asn1.x509.Certificate;
import net.jsign.bouncycastle.asn1.x509.CertificateList;
import net.jsign.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import net.jsign.bouncycastle.cert.X509AttributeCertificateHolder;
import net.jsign.bouncycastle.cert.X509CRLHolder;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.util.CollectionStore;
import net.jsign.bouncycastle.util.Store;

class CMSSignedHelper {
   static final CMSSignedHelper INSTANCE = new CMSSignedHelper();
   private static final Map encryptionAlgs = new HashMap();
   private static final Map digestAlgs = new HashMap();
   private static final Map digestAliases = new HashMap();

   private static void addEntries(ASN1ObjectIdentifier var0, String var1, String var2) {
      digestAlgs.put(var0.getId(), var1);
      encryptionAlgs.put(var0.getId(), var2);
   }

   String getEncryptionAlgName(String var1) {
      String var2 = (String)encryptionAlgs.get(var1);
      return var2 != null ? var2 : var1;
   }

   AlgorithmIdentifier fixAlgID(AlgorithmIdentifier var1) {
      return var1.getParameters() == null ? new AlgorithmIdentifier(var1.getAlgorithm(), DERNull.INSTANCE) : var1;
   }

   void setSigningEncryptionAlgorithmMapping(ASN1ObjectIdentifier var1, String var2) {
      encryptionAlgs.put(var1.getId(), var2);
   }

   void setSigningDigestAlgorithmMapping(ASN1ObjectIdentifier var1, String var2) {
      digestAlgs.put(var1.getId(), var2);
   }

   Store getCertificates(ASN1Set var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList(var1.size());
         Enumeration var3 = var1.getObjects();

         while(var3.hasMoreElements()) {
            ASN1Primitive var4 = ((ASN1Encodable)var3.nextElement()).toASN1Primitive();
            if (var4 instanceof ASN1Sequence) {
               var2.add(new X509CertificateHolder(Certificate.getInstance(var4)));
            }
         }

         return new CollectionStore(var2);
      } else {
         return new CollectionStore(new ArrayList());
      }
   }

   Store getAttributeCertificates(ASN1Set var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList(var1.size());
         Enumeration var3 = var1.getObjects();

         while(var3.hasMoreElements()) {
            ASN1Primitive var4 = ((ASN1Encodable)var3.nextElement()).toASN1Primitive();
            if (var4 instanceof ASN1TaggedObject) {
               var2.add(new X509AttributeCertificateHolder(AttributeCertificate.getInstance(((ASN1TaggedObject)var4).getObject())));
            }
         }

         return new CollectionStore(var2);
      } else {
         return new CollectionStore(new ArrayList());
      }
   }

   Store getCRLs(ASN1Set var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList(var1.size());
         Enumeration var3 = var1.getObjects();

         while(var3.hasMoreElements()) {
            ASN1Primitive var4 = ((ASN1Encodable)var3.nextElement()).toASN1Primitive();
            if (var4 instanceof ASN1Sequence) {
               var2.add(new X509CRLHolder(CertificateList.getInstance(var4)));
            }
         }

         return new CollectionStore(var2);
      } else {
         return new CollectionStore(new ArrayList());
      }
   }

   Store getOtherRevocationInfo(ASN1ObjectIdentifier var1, ASN1Set var2) {
      if (var2 != null) {
         ArrayList var3 = new ArrayList(var2.size());
         Enumeration var4 = var2.getObjects();

         while(var4.hasMoreElements()) {
            ASN1Primitive var5 = ((ASN1Encodable)var4.nextElement()).toASN1Primitive();
            if (var5 instanceof ASN1TaggedObject) {
               ASN1TaggedObject var6 = ASN1TaggedObject.getInstance(var5);
               if (var6.getTagNo() == 1) {
                  OtherRevocationInfoFormat var7 = OtherRevocationInfoFormat.getInstance(var6, false);
                  if (var1.equals(var7.getInfoFormat())) {
                     var3.add(var7.getInfo());
                  }
               }
            }
         }

         return new CollectionStore(var3);
      } else {
         return new CollectionStore(new ArrayList());
      }
   }

   static {
      addEntries(NISTObjectIdentifiers.dsa_with_sha224, "SHA224", "DSA");
      addEntries(NISTObjectIdentifiers.dsa_with_sha256, "SHA256", "DSA");
      addEntries(NISTObjectIdentifiers.dsa_with_sha384, "SHA384", "DSA");
      addEntries(NISTObjectIdentifiers.dsa_with_sha512, "SHA512", "DSA");
      addEntries(OIWObjectIdentifiers.dsaWithSHA1, "SHA1", "DSA");
      addEntries(OIWObjectIdentifiers.md4WithRSA, "MD4", "RSA");
      addEntries(OIWObjectIdentifiers.md4WithRSAEncryption, "MD4", "RSA");
      addEntries(OIWObjectIdentifiers.md5WithRSA, "MD5", "RSA");
      addEntries(OIWObjectIdentifiers.sha1WithRSA, "SHA1", "RSA");
      addEntries(PKCSObjectIdentifiers.md2WithRSAEncryption, "MD2", "RSA");
      addEntries(PKCSObjectIdentifiers.md4WithRSAEncryption, "MD4", "RSA");
      addEntries(PKCSObjectIdentifiers.md5WithRSAEncryption, "MD5", "RSA");
      addEntries(PKCSObjectIdentifiers.sha1WithRSAEncryption, "SHA1", "RSA");
      addEntries(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224", "RSA");
      addEntries(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256", "RSA");
      addEntries(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384", "RSA");
      addEntries(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512", "RSA");
      addEntries(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1", "ECDSA");
      addEntries(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224", "ECDSA");
      addEntries(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256", "ECDSA");
      addEntries(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384", "ECDSA");
      addEntries(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512", "ECDSA");
      addEntries(X9ObjectIdentifiers.id_dsa_with_sha1, "SHA1", "DSA");
      addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_1, "SHA1", "ECDSA");
      addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_224, "SHA224", "ECDSA");
      addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_256, "SHA256", "ECDSA");
      addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_384, "SHA384", "ECDSA");
      addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_512, "SHA512", "ECDSA");
      addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_1, "SHA1", "RSA");
      addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_256, "SHA256", "RSA");
      addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_1, "SHA1", "RSAandMGF1");
      addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_256, "SHA256", "RSAandMGF1");
      encryptionAlgs.put(X9ObjectIdentifiers.id_dsa.getId(), "DSA");
      encryptionAlgs.put(PKCSObjectIdentifiers.rsaEncryption.getId(), "RSA");
      encryptionAlgs.put(TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm, "RSA");
      encryptionAlgs.put(X509ObjectIdentifiers.id_ea_rsa.getId(), "RSA");
      encryptionAlgs.put(CMSSignedDataGenerator.ENCRYPTION_RSA_PSS, "RSAandMGF1");
      encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_94.getId(), "GOST3410");
      encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_2001.getId(), "ECGOST3410");
      encryptionAlgs.put("1.3.6.1.4.1.5849.1.6.2", "ECGOST3410");
      encryptionAlgs.put("1.3.6.1.4.1.5849.1.1.5", "GOST3410");
      encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001.getId(), "ECGOST3410");
      encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94.getId(), "GOST3410");
      digestAlgs.put(PKCSObjectIdentifiers.md2.getId(), "MD2");
      digestAlgs.put(PKCSObjectIdentifiers.md4.getId(), "MD4");
      digestAlgs.put(PKCSObjectIdentifiers.md5.getId(), "MD5");
      digestAlgs.put(OIWObjectIdentifiers.idSHA1.getId(), "SHA1");
      digestAlgs.put(NISTObjectIdentifiers.id_sha224.getId(), "SHA224");
      digestAlgs.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA256");
      digestAlgs.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA384");
      digestAlgs.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA512");
      digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd128.getId(), "RIPEMD128");
      digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd160.getId(), "RIPEMD160");
      digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd256.getId(), "RIPEMD256");
      digestAlgs.put(CryptoProObjectIdentifiers.gostR3411.getId(), "GOST3411");
      digestAlgs.put("1.3.6.1.4.1.5849.1.2.1", "GOST3411");
      digestAliases.put("SHA1", new String[]{"SHA-1"});
      digestAliases.put("SHA224", new String[]{"SHA-224"});
      digestAliases.put("SHA256", new String[]{"SHA-256"});
      digestAliases.put("SHA384", new String[]{"SHA-384"});
      digestAliases.put("SHA512", new String[]{"SHA-512"});
   }
}
