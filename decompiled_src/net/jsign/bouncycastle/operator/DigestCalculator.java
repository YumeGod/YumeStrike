package net.jsign.bouncycastle.operator;

import java.io.OutputStream;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestCalculator {
   AlgorithmIdentifier getAlgorithmIdentifier();

   OutputStream getOutputStream();

   byte[] getDigest();
}
