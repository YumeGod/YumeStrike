package org.apache.xml.serializer;

import java.io.IOException;
import org.w3c.dom.Node;

public interface DOMSerializer {
   void serialize(Node var1) throws IOException;
}
