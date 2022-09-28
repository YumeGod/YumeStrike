package net.jsign.bouncycastle.tsp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.jsign.bouncycastle.asn1.ASN1ObjectIdentifier;
import net.jsign.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import net.jsign.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import net.jsign.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import net.jsign.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import net.jsign.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;

public interface TSPAlgorithms {
   ASN1ObjectIdentifier MD5 = PKCSObjectIdentifiers.md5;
   ASN1ObjectIdentifier SHA1 = OIWObjectIdentifiers.idSHA1;
   ASN1ObjectIdentifier SHA224 = NISTObjectIdentifiers.id_sha224;
   ASN1ObjectIdentifier SHA256 = NISTObjectIdentifiers.id_sha256;
   ASN1ObjectIdentifier SHA384 = NISTObjectIdentifiers.id_sha384;
   ASN1ObjectIdentifier SHA512 = NISTObjectIdentifiers.id_sha512;
   ASN1ObjectIdentifier RIPEMD128 = TeleTrusTObjectIdentifiers.ripemd128;
   ASN1ObjectIdentifier RIPEMD160 = TeleTrusTObjectIdentifiers.ripemd160;
   ASN1ObjectIdentifier RIPEMD256 = TeleTrusTObjectIdentifiers.ripemd256;
   ASN1ObjectIdentifier GOST3411 = CryptoProObjectIdentifiers.gostR3411;
   Set ALLOWED = new HashSet(Arrays.asList(GOST3411, MD5, SHA1, SHA224, SHA256, SHA384, SHA512, RIPEMD128, RIPEMD160, RIPEMD256));
}
