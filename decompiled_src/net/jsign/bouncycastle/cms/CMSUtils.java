package net.jsign.bouncycastle.cms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.jsign.bouncycastle.asn1.ASN1Encodable;
import net.jsign.bouncycastle.asn1.ASN1EncodableVector;
import net.jsign.bouncycastle.asn1.ASN1InputStream;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.ASN1Set;
import net.jsign.bouncycastle.asn1.ASN1TaggedObject;
import net.jsign.bouncycastle.asn1.BEROctetStringGenerator;
import net.jsign.bouncycastle.asn1.BERSet;
import net.jsign.bouncycastle.asn1.DERNull;
import net.jsign.bouncycastle.asn1.DERSet;
import net.jsign.bouncycastle.asn1.DERTaggedObject;
import net.jsign.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.cms.ContentInfo;
import net.jsign.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import net.jsign.bouncycastle.asn1.ocsp.OCSPResponse;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.cert.X509AttributeCertificateHolder;
import net.jsign.bouncycastle.cert.X509CRLHolder;
import net.jsign.bouncycastle.cert.X509CertificateHolder;
import net.jsign.bouncycastle.operator.DigestCalculator;
import net.jsign.bouncycastle.util.Selector;
import net.jsign.bouncycastle.util.Store;
import net.jsign.bouncycastle.util.Strings;
import net.jsign.bouncycastle.util.io.Streams;
import net.jsign.bouncycastle.util.io.TeeInputStream;
import net.jsign.bouncycastle.util.io.TeeOutputStream;

class CMSUtils {
   private static final Set des = new HashSet();

   static boolean isDES(String var0) {
      String var1 = Strings.toUpperCase(var0);
      return des.contains(var1);
   }

   static boolean isEquivalent(AlgorithmIdentifier var0, AlgorithmIdentifier var1) {
      if (var0 != null && var1 != null) {
         if (!var0.getAlgorithm().equals(var1.getAlgorithm())) {
            return false;
         } else {
            ASN1Encodable var2 = var0.getParameters();
            ASN1Encodable var3 = var1.getParameters();
            if (var2 != null) {
               return var2.equals(var3) || var2.equals(DERNull.INSTANCE) && var3 == null;
            } else {
               return var3 == null || var3.equals(DERNull.INSTANCE);
            }
         }
      } else {
         return false;
      }
   }

   static ContentInfo readContentInfo(byte[] var0) throws CMSException {
      return readContentInfo(new ASN1InputStream(var0));
   }

   static ContentInfo readContentInfo(InputStream var0) throws CMSException {
      return readContentInfo(new ASN1InputStream(var0));
   }

   static List getCertificatesFromStore(Store var0) throws CMSException {
      ArrayList var1 = new ArrayList();

      try {
         Iterator var2 = var0.getMatches((Selector)null).iterator();

         while(var2.hasNext()) {
            X509CertificateHolder var3 = (X509CertificateHolder)var2.next();
            var1.add(var3.toASN1Structure());
         }

         return var1;
      } catch (ClassCastException var4) {
         throw new CMSException("error processing certs", var4);
      }
   }

   static List getAttributeCertificatesFromStore(Store var0) throws CMSException {
      ArrayList var1 = new ArrayList();

      try {
         Iterator var2 = var0.getMatches((Selector)null).iterator();

         while(var2.hasNext()) {
            X509AttributeCertificateHolder var3 = (X509AttributeCertificateHolder)var2.next();
            var1.add(new DERTaggedObject(false, 2, var3.toASN1Structure()));
         }

         return var1;
      } catch (ClassCastException var4) {
         throw new CMSException("error processing certs", var4);
      }
   }

   static List getCRLsFromStore(Store var0) throws CMSException {
      ArrayList var1 = new ArrayList();

      try {
         Iterator var2 = var0.getMatches((Selector)null).iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof X509CRLHolder) {
               X509CRLHolder var4 = (X509CRLHolder)var3;
               var1.add(var4.toASN1Structure());
            } else if (var3 instanceof OtherRevocationInfoFormat) {
               OtherRevocationInfoFormat var6 = OtherRevocationInfoFormat.getInstance(var3);
               validateInfoFormat(var6);
               var1.add(new DERTaggedObject(false, 1, var6));
            } else if (var3 instanceof ASN1TaggedObject) {
               var1.add(var3);
            }
         }

         return var1;
      } catch (ClassCastException var5) {
         throw new CMSException("error processing certs", var5);
      }
   }

   private static void validateInfoFormat(OtherRevocationInfoFormat var0) {
      if (CMSObjectIdentifiers.id_ri_ocsp_response.equals(var0.getInfoFormat())) {
         OCSPResponse var1 = OCSPResponse.getInstance(var0.getInfo());
         if (var1.getResponseStatus().getValue().intValue() != 0) {
            throw new IllegalArgumentException("cannot add unsuccessful OCSP response to CMS SignedData");
         }
      }

   }

   static Collection getOthersFromStore(ASN1ObjectIdentifier var0, Store var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getMatches((Selector)null).iterator();

      while(var3.hasNext()) {
         ASN1Encodable var4 = (ASN1Encodable)var3.next();
         OtherRevocationInfoFormat var5 = new OtherRevocationInfoFormat(var0, var4);
         validateInfoFormat(var5);
         var2.add(new DERTaggedObject(false, 1, var5));
      }

      return var2;
   }

   static ASN1Set createBerSetFromList(List var0) {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add((ASN1Encodable)var2.next());
      }

      return new BERSet(var1);
   }

   static ASN1Set createDerSetFromList(List var0) {
      ASN1EncodableVector var1 = new ASN1EncodableVector();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add((ASN1Encodable)var2.next());
      }

      return new DERSet(var1);
   }

   static OutputStream createBEROctetOutputStream(OutputStream var0, int var1, boolean var2, int var3) throws IOException {
      BEROctetStringGenerator var4 = new BEROctetStringGenerator(var0, var1, var2);
      return var3 != 0 ? var4.getOctetOutputStream(new byte[var3]) : var4.getOctetOutputStream();
   }

   private static ContentInfo readContentInfo(ASN1InputStream var0) throws CMSException {
      try {
         return ContentInfo.getInstance(var0.readObject());
      } catch (IOException var2) {
         throw new CMSException("IOException reading content.", var2);
      } catch (ClassCastException var3) {
         throw new CMSException("Malformed content.", var3);
      } catch (IllegalArgumentException var4) {
         throw new CMSException("Malformed content.", var4);
      }
   }

   public static byte[] streamToByteArray(InputStream var0) throws IOException {
      return Streams.readAll(var0);
   }

   public static byte[] streamToByteArray(InputStream var0, int var1) throws IOException {
      return Streams.readAllLimited(var0, var1);
   }

   static InputStream attachDigestsToInputStream(Collection var0, InputStream var1) {
      Object var2 = var1;

      DigestCalculator var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var2 = new TeeInputStream((InputStream)var2, var4.getOutputStream())) {
         var4 = (DigestCalculator)var3.next();
      }

      return (InputStream)var2;
   }

   static OutputStream attachSignersToOutputStream(Collection var0, OutputStream var1) {
      OutputStream var2 = var1;

      SignerInfoGenerator var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var2 = getSafeTeeOutputStream(var2, var4.getCalculatingOutputStream())) {
         var4 = (SignerInfoGenerator)var3.next();
      }

      return var2;
   }

   static OutputStream getSafeOutputStream(OutputStream var0) {
      return (OutputStream)(var0 == null ? new NullOutputStream() : var0);
   }

   static OutputStream getSafeTeeOutputStream(OutputStream var0, OutputStream var1) {
      return (OutputStream)(var0 == null ? getSafeOutputStream(var1) : (var1 == null ? getSafeOutputStream(var0) : new TeeOutputStream(var0, var1)));
   }

   static {
      des.add("DES");
      des.add("DESEDE");
      des.add(OIWObjectIdentifiers.desCBC.getId());
      des.add(PKCSObjectIdentifiers.des_EDE3_CBC.getId());
      des.add(PKCSObjectIdentifiers.des_EDE3_CBC.getId());
      des.add(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId());
   }
}
