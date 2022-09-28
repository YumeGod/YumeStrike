package org.apache.fop.afp;

import java.io.IOException;
import java.io.OutputStream;

public interface Streamable {
   void writeToStream(OutputStream var1) throws IOException;
}
