package net.jsign.bouncycastle.operator;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestAlgorithmIdentifierFinder {
   AlgorithmIdentifier find(AlgorithmIdentifier var1);

   AlgorithmIdentifier find(String var1);
}
