package org.apache.xmlgraphics.util.io;

import java.io.IOException;

public interface Finalizable {
   void finalizeStream() throws IOException;
}
