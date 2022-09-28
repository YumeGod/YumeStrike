package org.apache.xml.serializer;

import javax.xml.transform.Transformer;
import org.w3c.dom.Node;

public interface TransformStateSetter {
   void setCurrentNode(Node var1);

   void resetState(Transformer var1);
}
