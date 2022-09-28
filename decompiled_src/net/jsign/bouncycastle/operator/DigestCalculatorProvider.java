package net.jsign.bouncycastle.operator;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestCalculatorProvider {
   DigestCalculator get(AlgorithmIdentifier var1) throws OperatorCreationException;
}
