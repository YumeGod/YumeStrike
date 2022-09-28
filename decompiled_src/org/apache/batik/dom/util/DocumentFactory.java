package org.apache.batik.dom.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;

public interface DocumentFactory {
   void setValidating(boolean var1);

   boolean isValidating();

   Document createDocument(String var1, String var2, String var3) throws IOException;

   Document createDocument(String var1, String var2, String var3, InputStream var4) throws IOException;

   Document createDocument(String var1, String var2, String var3, XMLReader var4) throws IOException;

   Document createDocument(String var1, String var2, String var3, Reader var4) throws IOException;

   DocumentDescriptor getDocumentDescriptor();
}
