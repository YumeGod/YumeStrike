package org.apache.batik.util.io;

import java.io.IOException;

public interface CharDecoder {
   int END_OF_STREAM = -1;

   int readChar() throws IOException;

   void dispose() throws IOException;
}
