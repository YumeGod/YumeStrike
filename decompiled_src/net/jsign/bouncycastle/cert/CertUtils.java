package net.jsign.bouncycastle.cert;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1GeneralizedTime;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.DERBitString;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.DEROutputStream;
import net.jsign.bouncycastle.asn1.DERSequence;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.asn1.x509.AttributeCertificate;
import net.jsign.bouncycastle.asn1.x509.AttributeCertificateInfo;
import net.jsign.bouncycastle.asn1.x509.Certificate;
import net.jsign.bouncycastle.asn1.x509.CertificateList;
import net.jsign.bouncycastle.asn1.x509.Extensions;
import net.jsign.bouncycastle.asn1.x509.ExtensionsGenerator;
import net.jsign.bouncycastle.asn1.x509.TBSCertList;
import net.jsign.bouncycastle.asn1.x509.TBSCertificate;
import net.jsign.bouncycastle.operator.ContentSigner;

class CertUtils {
   private static Set EMPTY_SET = Collections.unmodifiableSet(new HashSet());
   private static List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());

   static X509CertificateHolder generateFullCert(ContentSigner var0, TBSCertificate var1) {
      try {
         return new X509CertificateHolder(generateStructure(var1, var0.getAlgorithmIdentifier(), generateSig(var0, var1)));
      } catch (IOException var3) {
         throw new IllegalStateException("cannot produce certificate signature");
      }
   }

   static X509AttributeCertificateHolder generateFullAttrCert(ContentSigner var0, AttributeCertificateInfo var1) {
      try {
         return new X509AttributeCertificateHolder(generateAttrStructure(var1, var0.getAlgorithmIdentifier(), generateSig(var0, var1)));
      } catch (IOException var3) {
         throw new IllegalStateException("cannot produce attribute certificate signature");
      }
   }

   static X509CRLHolder generateFullCRL(ContentSigner var0, TBSCertList var1) {
      try {
         return new X509CRLHolder(generateCRLStructure(var1, var0.getAlgorithmIdentifier(), generateSig(var0, var1)));
      } catch (IOException var3) {
         throw new IllegalStateException("cannot produce certificate signature");
      }
   }

   private static byte[] generateSig(ContentSigner var0, ASN1Encodable var1) throws IOException {
      OutputStream var2 = var0.getOutputStream();
      DEROutputStream var3 = new DEROutputStream(var2);
      var3.writeObject(var1);
      var2.close();
      return var0.getSignature();
   }

   private static Certificate generateStructure(TBSCertificate var0, AlgorithmIdentifier var1, byte[] var2) {
      ASN1EncodableVector var3 = new ASN1EncodableVector();
      var3.add(var0);
      var3.add(var1);
      var3.add(new DERBitString(var2));
      return Certificate.getInstance(new DERSequence(var3));
   }

   private static AttributeCertificate generateAttrStructure(AttributeCertificateInfo var0, AlgorithmIdentifier var1, byte[] var2) {
      ASN1EncodableVector var3 = new ASN1EncodableVector();
      var3.add(var0);
      var3.add(var1);
      var3.add(new DERBitString(var2));
      return AttributeCertificate.getInstance(new DERSequence(var3));
   }

   private static CertificateList generateCRLStructure(TBSCertList var0, AlgorithmIdentifier var1, byte[] var2) {
      ASN1EncodableVector var3 = new ASN1EncodableVector();
      var3.add(var0);
      var3.add(var1);
      var3.add(new DERBitString(var2));
      return CertificateList.getInstance(new DERSequence(var3));
   }

   static Set getCriticalExtensionOIDs(Extensions var0) {
      return var0 == null ? EMPTY_SET : Collections.unmodifiableSet(new HashSet(Arrays.asList(var0.getCriticalExtensionOIDs())));
   }

   static Set getNonCriticalExtensionOIDs(Extensions var0) {
      return var0 == null ? EMPTY_SET : Collections.unmodifiableSet(new HashSet(Arrays.asList(var0.getNonCriticalExtensionOIDs())));
   }

   static List getExtensionOIDs(Extensions var0) {
      return var0 == null ? EMPTY_LIST : Collections.unmodifiableList(Arrays.asList(var0.getExtensionOIDs()));
   }

   static void addExtension(ExtensionsGenerator var0, ASN1ObjectIdentifier var1, boolean var2, ASN1Encodable var3) throws CertIOException {
      try {
         var0.addExtension(var1, var2, var3);
      } catch (IOException var5) {
         throw new CertIOException("cannot encode extension: " + var5.getMessage(), var5);
      }
   }

   static DERBitString booleanToBitString(boolean[] var0) {
      byte[] var1 = new byte[(var0.length + 7) / 8];

      int var2;
      for(var2 = 0; var2 != var0.length; ++var2) {
         var1[var2 / 8] = (byte)(var1[var2 / 8] | (var0[var2] ? 1 << 7 - var2 % 8 : 0));
      }

      var2 = var0.length % 8;
      if (var2 == 0) {
         return new DERBitString(var1);
      } else {
         return new DERBitString(var1, 8 - var2);
      }
   }

   static boolean[] bitStringToBoolean(DERBitString var0) {
      if (var0 != null) {
         byte[] var1 = var0.getBytes();
         boolean[] var2 = new boolean[var1.length * 8 - var0.getPadBits()];

         for(int var3 = 0; var3 != var2.length; ++var3) {
            var2[var3] = (var1[var3 / 8] & 128 >>> var3 % 8) != 0;
         }

         return var2;
      } else {
         return null;
      }
   }

   static Date recoverDate(ASN1GeneralizedTime var0) {
      try {
         return var0.getDate();
      } catch (ParseException var2) {
         throw new IllegalStateException("unable to recover date: " + var2.getMessage());
      }
   }

   static boolean isAlgIdEqual(AlgorithmIdentifier var0, AlgorithmIdentifier var1) {
      if (!var0.getAlgorithm().equals(var1.getAlgorithm())) {
         return false;
      } else if (var0.getParameters() == null) {
         return var1.getParameters() == null || var1.getParameters().equals(DERNull.INSTANCE);
      } else if (var1.getParameters() == null) {
         return var0.getParameters() == null || var0.getParameters().equals(DERNull.INSTANCE);
      } else {
         return var0.getParameters().equals(var1.getParameters());
      }
   }
}
