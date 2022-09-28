package org.apache.xmlgraphics.util.io;

public interface ASCII85Constants {
   int ZERO = 122;
   byte[] ZERO_ARRAY = new byte[]{122};
   int START = 33;
   int END = 117;
   int EOL = 10;
   byte[] EOD = new byte[]{126, 62};
   long[] POW85 = new long[]{52200625L, 614125L, 7225L, 85L, 1L};
}
