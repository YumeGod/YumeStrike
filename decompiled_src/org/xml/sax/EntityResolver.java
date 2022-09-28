package org.xml.sax;

import java.io.IOException;

public interface EntityResolver {
   InputSource resolveEntity(String var1, String var2) throws SAXException, IOException;
}
