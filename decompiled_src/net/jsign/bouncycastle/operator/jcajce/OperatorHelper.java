package net.jsign.bouncycastle.operator.jcajce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.bsi.BSIObjectIdentifiers;
import net.jsign.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import net.jsign.bouncycastle.asn1.eac.EACObjectIdentifiers;
import net.jsign.bouncycastle.asn1.kisa.KISAObjectIdentifiers;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.ntt.NTTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import net.jsign.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import net.jsign.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.jcajce.util.AlgorithmParametersUtils;
import net.jsign.bouncycastle.jcajce.util.JcaJceHelper;
import net.jsign.bouncycastle.jcajce.util.MessageDigestUtils;
import net.jsign.bouncycastle.operator.OperatorCreationException;
import net.jsign.bouncycastle.util.Integers;

class OperatorHelper {
   private static final Map oids = new HashMap();
   private static final Map asymmetricWrapperAlgNames = new HashMap();
   private static final Map symmetricWrapperAlgNames = new HashMap();
   private static final Map symmetricKeyAlgNames = new HashMap();
   private static final Map symmetricWrapperKeySizes = new HashMap();
   private JcaJceHelper helper;

   OperatorHelper(JcaJceHelper var1) {
      this.helper = var1;
   }

   String getWrappingAlgorithmName(ASN1ObjectIdentifier var1) {
      return (String)symmetricWrapperAlgNames.get(var1);
   }

   int getKeySizeInBits(ASN1ObjectIdentifier var1) {
      return (Integer)symmetricWrapperKeySizes.get(var1);
   }

   Cipher createAsymmetricWrapper(ASN1ObjectIdentifier var1, Map var2) throws OperatorCreationException {
      try {
         String var3 = null;
         if (!var2.isEmpty()) {
            var3 = (String)var2.get(var1);
         }

         if (var3 == null) {
            var3 = (String)asymmetricWrapperAlgNames.get(var1);
         }

         if (var3 != null) {
            try {
               return this.helper.createCipher(var3);
            } catch (NoSuchAlgorithmException var7) {
               if (var3.equals("RSA/ECB/PKCS1Padding")) {
                  try {
                     return this.helper.createCipher("RSA/NONE/PKCS1Padding");
                  } catch (NoSuchAlgorithmException var6) {
                  }
               }
            }
         }

         return this.helper.createCipher(var1.getId());
      } catch (GeneralSecurityException var8) {
         throw new OperatorCreationException("cannot create cipher: " + var8.getMessage(), var8);
      }
   }

   Cipher createSymmetricWrapper(ASN1ObjectIdentifier var1) throws OperatorCreationException {
      try {
         String var2 = (String)symmetricWrapperAlgNames.get(var1);
         if (var2 != null) {
            try {
               return this.helper.createCipher(var2);
            } catch (NoSuchAlgorithmException var4) {
            }
         }

         return this.helper.createCipher(var1.getId());
      } catch (GeneralSecurityException var5) {
         throw new OperatorCreationException("cannot create cipher: " + var5.getMessage(), var5);
      }
   }

   AlgorithmParameters createAlgorithmParameters(AlgorithmIdentifier var1) throws OperatorCreationException {
      if (var1.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
         return null;
      } else {
         AlgorithmParameters var2;
         try {
            var2 = this.helper.createAlgorithmParameters(var1.getAlgorithm().getId());
         } catch (NoSuchAlgorithmException var5) {
            return null;
         } catch (NoSuchProviderException var6) {
            throw new OperatorCreationException("cannot create algorithm parameters: " + var6.getMessage(), var6);
         }

         try {
            var2.init(var1.getParameters().toASN1Primitive().getEncoded());
            return var2;
         } catch (IOException var4) {
            throw new OperatorCreationException("cannot initialise algorithm parameters: " + var4.getMessage(), var4);
         }
      }
   }

   MessageDigest createDigest(AlgorithmIdentifier var1) throws GeneralSecurityException {
      MessageDigest var2;
      try {
         var2 = this.helper.createDigest(MessageDigestUtils.getDigestName(var1.getAlgorithm()));
      } catch (NoSuchAlgorithmException var5) {
         if (oids.get(var1.getAlgorithm()) == null) {
            throw var5;
         }

         String var4 = (String)oids.get(var1.getAlgorithm());
         var2 = this.helper.createDigest(var4);
      }

      return var2;
   }

   Signature createSignature(AlgorithmIdentifier var1) throws GeneralSecurityException {
      Signature var2;
      try {
         var2 = this.helper.createSignature(getSignatureName(var1));
      } catch (NoSuchAlgorithmException var5) {
         if (oids.get(var1.getAlgorithm()) == null) {
            throw var5;
         }

         String var4 = (String)oids.get(var1.getAlgorithm());
         var2 = this.helper.createSignature(var4);
      }

      return var2;
   }

   public Signature createRawSignature(AlgorithmIdentifier var1) {
      try {
         String var2 = getSignatureName(var1);
         var2 = "NONE" + var2.substring(var2.indexOf("WITH"));
         Signature var3 = this.helper.createSignature(var2);
         if (var1.getAlgorithm().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
            AlgorithmParameters var4 = this.helper.createAlgorithmParameters(var2);
            AlgorithmParametersUtils.loadParameters(var4, var1.getParameters());
            PSSParameterSpec var5 = (PSSParameterSpec)var4.getParameterSpec(PSSParameterSpec.class);
            var3.setParameter(var5);
         }

         return var3;
      } catch (Exception var6) {
         return null;
      }
   }

   private static String getSignatureName(AlgorithmIdentifier var0) {
      ASN1Encodable var1 = var0.getParameters();
      if (var1 != null && !DERNull.INSTANCE.equals(var1) && var0.getAlgorithm().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
         RSASSAPSSparams var2 = RSASSAPSSparams.getInstance(var1);
         return getDigestName(var2.getHashAlgorithm().getAlgorithm()) + "WITHRSAANDMGF1";
      } else {
         return oids.containsKey(var0.getAlgorithm()) ? (String)oids.get(var0.getAlgorithm()) : var0.getAlgorithm().getId();
      }
   }

   private static String getDigestName(ASN1ObjectIdentifier var0) {
      String var1 = MessageDigestUtils.getDigestName(var0);
      int var2 = var1.indexOf(45);
      return var2 > 0 ? var1.substring(0, var2) + var1.substring(var2 + 1) : MessageDigestUtils.getDigestName(var0);
   }

   public X509Certificate convertCertificate(X509CertificateHolder var1) throws CertificateException {
      try {
         CertificateFactory var2 = this.helper.createCertificateFactory("X.509");
         return (X509Certificate)var2.generateCertificate(new ByteArrayInputStream(var1.getEncoded()));
      } catch (IOException var3) {
         throw new OpCertificateException("cannot get encoded form of certificate: " + var3.getMessage(), var3);
      } catch (NoSuchProviderException var4) {
         throw new OpCertificateException("cannot find factory provider: " + var4.getMessage(), var4);
      }
   }

   public PublicKey convertPublicKey(SubjectPublicKeyInfo var1) throws OperatorCreationException {
      try {
         KeyFactory var2 = this.helper.createKeyFactory(var1.getAlgorithm().getAlgorithm().getId());
         return var2.generatePublic(new X509EncodedKeySpec(var1.getEncoded()));
      } catch (IOException var3) {
         throw new OperatorCreationException("cannot get encoded form of key: " + var3.getMessage(), var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new OperatorCreationException("cannot create key factory: " + var4.getMessage(), var4);
      } catch (NoSuchProviderException var5) {
         throw new OperatorCreationException("cannot find factory provider: " + var5.getMessage(), var5);
      } catch (InvalidKeySpecException var6) {
         throw new OperatorCreationException("cannot create key factory: " + var6.getMessage(), var6);
      }
   }

   String getKeyAlgorithmName(ASN1ObjectIdentifier var1) {
      String var2 = (String)symmetricKeyAlgNames.get(var1);
      return var2 != null ? var2 : var1.getId();
   }

   static {
      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"), "SHA1WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512WITHRSA");
      oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3411WITHGOST3410");
      oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, "GOST3411WITHECGOST3410");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_SHA1, "SHA1WITHPLAIN-ECDSA");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_SHA224, "SHA224WITHPLAIN-ECDSA");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_SHA256, "SHA256WITHPLAIN-ECDSA");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_SHA384, "SHA384WITHPLAIN-ECDSA");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_SHA512, "SHA512WITHPLAIN-ECDSA");
      oids.put(BSIObjectIdentifiers.ecdsa_plain_RIPEMD160, "RIPEMD160WITHPLAIN-ECDSA");
      oids.put(EACObjectIdentifiers.id_TA_ECDSA_SHA_1, "SHA1WITHCVC-ECDSA");
      oids.put(EACObjectIdentifiers.id_TA_ECDSA_SHA_224, "SHA224WITHCVC-ECDSA");
      oids.put(EACObjectIdentifiers.id_TA_ECDSA_SHA_256, "SHA256WITHCVC-ECDSA");
      oids.put(EACObjectIdentifiers.id_TA_ECDSA_SHA_384, "SHA384WITHCVC-ECDSA");
      oids.put(EACObjectIdentifiers.id_TA_ECDSA_SHA_512, "SHA512WITHCVC-ECDSA");
      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"), "MD5WITHRSA");
      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"), "MD2WITHRSA");
      oids.put(new ASN1ObjectIdentifier("1.2.840.10040.4.3"), "SHA1WITHDSA");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1WITHECDSA");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224WITHECDSA");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256WITHECDSA");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384WITHECDSA");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512WITHECDSA");
      oids.put(OIWObjectIdentifiers.sha1WithRSA, "SHA1WITHRSA");
      oids.put(OIWObjectIdentifiers.dsaWithSHA1, "SHA1WITHDSA");
      oids.put(NISTObjectIdentifiers.dsa_with_sha224, "SHA224WITHDSA");
      oids.put(NISTObjectIdentifiers.dsa_with_sha256, "SHA256WITHDSA");
      oids.put(OIWObjectIdentifiers.idSHA1, "SHA-1");
      oids.put(NISTObjectIdentifiers.id_sha224, "SHA-224");
      oids.put(NISTObjectIdentifiers.id_sha256, "SHA-256");
      oids.put(NISTObjectIdentifiers.id_sha384, "SHA-384");
      oids.put(NISTObjectIdentifiers.id_sha512, "SHA-512");
      oids.put(TeleTrusTObjectIdentifiers.ripemd128, "RIPEMD128");
      oids.put(TeleTrusTObjectIdentifiers.ripemd160, "RIPEMD160");
      oids.put(TeleTrusTObjectIdentifiers.ripemd256, "RIPEMD256");
      asymmetricWrapperAlgNames.put(PKCSObjectIdentifiers.rsaEncryption, "RSA/ECB/PKCS1Padding");
      symmetricWrapperAlgNames.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap, "DESEDEWrap");
      symmetricWrapperAlgNames.put(PKCSObjectIdentifiers.id_alg_CMSRC2wrap, "RC2Wrap");
      symmetricWrapperAlgNames.put(NISTObjectIdentifiers.id_aes128_wrap, "AESWrap");
      symmetricWrapperAlgNames.put(NISTObjectIdentifiers.id_aes192_wrap, "AESWrap");
      symmetricWrapperAlgNames.put(NISTObjectIdentifiers.id_aes256_wrap, "AESWrap");
      symmetricWrapperAlgNames.put(NTTObjectIdentifiers.id_camellia128_wrap, "CamelliaWrap");
      symmetricWrapperAlgNames.put(NTTObjectIdentifiers.id_camellia192_wrap, "CamelliaWrap");
      symmetricWrapperAlgNames.put(NTTObjectIdentifiers.id_camellia256_wrap, "CamelliaWrap");
      symmetricWrapperAlgNames.put(KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "SEEDWrap");
      symmetricWrapperAlgNames.put(PKCSObjectIdentifiers.des_EDE3_CBC, "DESede");
      symmetricWrapperKeySizes.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap, Integers.valueOf(192));
      symmetricWrapperKeySizes.put(NISTObjectIdentifiers.id_aes128_wrap, Integers.valueOf(128));
      symmetricWrapperKeySizes.put(NISTObjectIdentifiers.id_aes192_wrap, Integers.valueOf(192));
      symmetricWrapperKeySizes.put(NISTObjectIdentifiers.id_aes256_wrap, Integers.valueOf(256));
      symmetricWrapperKeySizes.put(NTTObjectIdentifiers.id_camellia128_wrap, Integers.valueOf(128));
      symmetricWrapperKeySizes.put(NTTObjectIdentifiers.id_camellia192_wrap, Integers.valueOf(192));
      symmetricWrapperKeySizes.put(NTTObjectIdentifiers.id_camellia256_wrap, Integers.valueOf(256));
      symmetricWrapperKeySizes.put(KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, Integers.valueOf(128));
      symmetricWrapperKeySizes.put(PKCSObjectIdentifiers.des_EDE3_CBC, Integers.valueOf(192));
      symmetricKeyAlgNames.put(NISTObjectIdentifiers.aes, "AES");
      symmetricKeyAlgNames.put(NISTObjectIdentifiers.id_aes128_CBC, "AES");
      symmetricKeyAlgNames.put(NISTObjectIdentifiers.id_aes192_CBC, "AES");
      symmetricKeyAlgNames.put(NISTObjectIdentifiers.id_aes256_CBC, "AES");
      symmetricKeyAlgNames.put(PKCSObjectIdentifiers.des_EDE3_CBC, "DESede");
      symmetricKeyAlgNames.put(PKCSObjectIdentifiers.RC2_CBC, "RC2");
   }

   private static class OpCertificateException extends CertificateException {
      private Throwable cause;

      public OpCertificateException(String var1, Throwable var2) {
         super(var1);
         this.cause = var2;
      }

      public Throwable getCause() {
         return this.cause;
      }
   }
}
