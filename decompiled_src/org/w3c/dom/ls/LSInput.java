package org.w3c.dom.ls;

import java.io.InputStream;
import java.io.Reader;

public interface LSInput {
   Reader getCharacterStream();

   void setCharacterStream(Reader var1);

   InputStream getByteStream();

   void setByteStream(InputStream var1);

   String getStringData();

   void setStringData(String var1);

   String getSystemId();

   void setSystemId(String var1);

   String getPublicId();

   void setPublicId(String var1);

   String getBaseURI();

   void setBaseURI(String var1);

   String getEncoding();

   void setEncoding(String var1);

   boolean getCertifiedText();

   void setCertifiedText(boolean var1);
}
