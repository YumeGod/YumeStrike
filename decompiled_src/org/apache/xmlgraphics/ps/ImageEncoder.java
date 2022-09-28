package org.apache.xmlgraphics.ps;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageEncoder {
   void writeTo(OutputStream var1) throws IOException;

   String getImplicitFilter();
}
