package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CMSSignatureEncryptionAlgorithmFinder {
   AlgorithmIdentifier findEncryptionAlgorithm(AlgorithmIdentifier var1);
}
