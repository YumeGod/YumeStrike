package org.w3c.dom.ls;

import java.io.OutputStream;
import java.io.Writer;

public interface LSOutput {
   Writer getCharacterStream();

   void setCharacterStream(Writer var1);

   OutputStream getByteStream();

   void setByteStream(OutputStream var1);

   String getSystemId();

   void setSystemId(String var1);

   String getEncoding();

   void setEncoding(String var1);
}
