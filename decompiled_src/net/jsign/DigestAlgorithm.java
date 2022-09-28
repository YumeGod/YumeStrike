package net.jsign;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.tsp.TSPAlgorithms;

public enum DigestAlgorithm {
   MD5("MD5", TSPAlgorithms.MD5),
   SHA1("SHA-1", TSPAlgorithms.SHA1),
   SHA256("SHA-256", TSPAlgorithms.SHA256),
   SHA384("SHA-384", TSPAlgorithms.SHA384),
   SHA512("SHA-512", TSPAlgorithms.SHA512);

   public final String id;
   public final ASN1ObjectIdentifier oid;

   private DigestAlgorithm(String id, ASN1ObjectIdentifier oid) {
      this.id = id;
      this.oid = oid;
   }

   public static DigestAlgorithm of(String s) {
      if (s == null) {
         return null;
      } else {
         s = s.toUpperCase().replaceAll("-", "");
         DigestAlgorithm[] arr$ = values();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            DigestAlgorithm algorithm = arr$[i$];
            if (algorithm.name().equals(s)) {
               return algorithm;
            }
         }

         if ("SHA2".equals(s)) {
            return SHA256;
         } else {
            return null;
         }
      }
   }

   public static DigestAlgorithm of(ASN1ObjectIdentifier oid) {
      DigestAlgorithm[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DigestAlgorithm algorithm = arr$[i$];
         if (algorithm.oid.equals(oid)) {
            return algorithm;
         }
      }

      return null;
   }

   public MessageDigest getMessageDigest() {
      try {
         return MessageDigest.getInstance(this.id);
      } catch (NoSuchAlgorithmException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static DigestAlgorithm getDefault() {
      return SHA256;
   }
}
