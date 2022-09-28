package net.jsign.bouncycastle.jcajce.util;

import java.util.HashMap;
import java.util.Map;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import net.jsign.bouncycastle.asn1.gnu.GNUObjectIdentifiers;
import net.jsign.bouncycastle.asn1.iso.ISOIECObjectIdentifiers;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;

public class MessageDigestUtils {
   private static Map digestOidMap = new HashMap();

   public static String getDigestName(ASN1ObjectIdentifier var0) {
      String var1 = (String)digestOidMap.get(var0);
      return var1 != null ? var1 : var0.getId();
   }

   static {
      digestOidMap.put(PKCSObjectIdentifiers.md2, "MD2");
      digestOidMap.put(PKCSObjectIdentifiers.md4, "MD4");
      digestOidMap.put(PKCSObjectIdentifiers.md5, "MD5");
      digestOidMap.put(OIWObjectIdentifiers.idSHA1, "SHA-1");
      digestOidMap.put(NISTObjectIdentifiers.id_sha224, "SHA-224");
      digestOidMap.put(NISTObjectIdentifiers.id_sha256, "SHA-256");
      digestOidMap.put(NISTObjectIdentifiers.id_sha384, "SHA-384");
      digestOidMap.put(NISTObjectIdentifiers.id_sha512, "SHA-512");
      digestOidMap.put(TeleTrusTObjectIdentifiers.ripemd128, "RIPEMD-128");
      digestOidMap.put(TeleTrusTObjectIdentifiers.ripemd160, "RIPEMD-160");
      digestOidMap.put(TeleTrusTObjectIdentifiers.ripemd256, "RIPEMD-128");
      digestOidMap.put(ISOIECObjectIdentifiers.ripemd128, "RIPEMD-128");
      digestOidMap.put(ISOIECObjectIdentifiers.ripemd160, "RIPEMD-160");
      digestOidMap.put(CryptoProObjectIdentifiers.gostR3411, "GOST3411");
      digestOidMap.put(GNUObjectIdentifiers.Tiger_192, "Tiger");
      digestOidMap.put(ISOIECObjectIdentifiers.whirlpool, "Whirlpool");
   }
}
