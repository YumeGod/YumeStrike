package org.apache.xml.serialize;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public interface DOMSerializer {
   void serialize(Element var1) throws IOException;

   void serialize(Document var1) throws IOException;

   void serialize(DocumentFragment var1) throws IOException;
}
