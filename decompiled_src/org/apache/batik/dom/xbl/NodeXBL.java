package org.apache.batik.dom.xbl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface NodeXBL {
   Node getXblParentNode();

   NodeList getXblChildNodes();

   NodeList getXblScopedChildNodes();

   Node getXblFirstChild();

   Node getXblLastChild();

   Node getXblPreviousSibling();

   Node getXblNextSibling();

   Element getXblFirstElementChild();

   Element getXblLastElementChild();

   Element getXblPreviousElementSibling();

   Element getXblNextElementSibling();

   Element getXblBoundElement();

   Element getXblShadowTree();

   NodeList getXblDefinitions();
}
