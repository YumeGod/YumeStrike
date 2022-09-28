package org.apache.batik.dom.xbl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface XBLManager {
   void startProcessing();

   void stopProcessing();

   boolean isProcessing();

   Node getXblParentNode(Node var1);

   NodeList getXblChildNodes(Node var1);

   NodeList getXblScopedChildNodes(Node var1);

   Node getXblFirstChild(Node var1);

   Node getXblLastChild(Node var1);

   Node getXblPreviousSibling(Node var1);

   Node getXblNextSibling(Node var1);

   Element getXblFirstElementChild(Node var1);

   Element getXblLastElementChild(Node var1);

   Element getXblPreviousElementSibling(Node var1);

   Element getXblNextElementSibling(Node var1);

   Element getXblBoundElement(Node var1);

   Element getXblShadowTree(Node var1);

   NodeList getXblDefinitions(Node var1);
}
