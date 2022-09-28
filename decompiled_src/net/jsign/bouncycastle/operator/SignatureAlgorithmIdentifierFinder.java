package net.jsign.bouncycastle.operator;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface SignatureAlgorithmIdentifierFinder {
   AlgorithmIdentifier find(String var1);
}
