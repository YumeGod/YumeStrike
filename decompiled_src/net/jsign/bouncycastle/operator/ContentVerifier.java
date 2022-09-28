package net.jsign.bouncycastle.operator;

import java.io.OutputStream;
import net.jsign.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface ContentVerifier {
   AlgorithmIdentifier getAlgorithmIdentifier();

   OutputStream getOutputStream();

   boolean verify(byte[] var1);
}
