package net.jsign.bouncycastle.util;

import java.io.IOException;

public interface Encodable {
   byte[] getEncoded() throws IOException;
}
