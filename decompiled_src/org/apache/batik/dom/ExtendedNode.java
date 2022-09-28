package org.apache.batik.dom;

import org.apache.batik.dom.events.NodeEventTarget;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ExtendedNode extends Node, NodeEventTarget {
   void setNodeName(String var1);

   boolean isReadonly();

   void setReadonly(boolean var1);

   void setOwnerDocument(Document var1);

   void setParentNode(Node var1);

   void setPreviousSibling(Node var1);

   void setNextSibling(Node var1);

   void setSpecified(boolean var1);
}
