package net.jsign.bouncycastle.cms;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CMSSignatureAlgorithmNameGenerator {
   String getSignatureName(AlgorithmIdentifier var1, AlgorithmIdentifier var2);
}
