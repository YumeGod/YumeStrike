package net.jsign.bouncycastle.operator;

import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;
import net.jsign.bouncycastle.cert.X509CertificateHolder;

public interface ContentVerifierProvider {
   boolean hasAssociatedCertificate();

   X509CertificateHolder getAssociatedCertificate();

   ContentVerifier get(AlgorithmIdentifier var1) throws OperatorCreationException;
}
