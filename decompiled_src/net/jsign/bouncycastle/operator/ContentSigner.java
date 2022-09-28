package net.jsign.bouncycastle.operator;

import java.io.OutputStream;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface ContentSigner {
   AlgorithmIdentifier getAlgorithmIdentifier();

   OutputStream getOutputStream();

   byte[] getSignature();
}
