package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamCache {
   OutputStream getOutputStream() throws IOException;

   void write(byte[] var1) throws IOException;

   int outputContents(OutputStream var1) throws IOException;

   int getSize() throws IOException;

   void clear() throws IOException;
}
