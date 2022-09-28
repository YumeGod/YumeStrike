package org.apache.xerces.dom3.as;

import org.w3c.dom.Node;

/** @deprecated */
public interface NodeEditAS {
   short WF_CHECK = 1;
   short NS_WF_CHECK = 2;
   short PARTIAL_VALIDITY_CHECK = 3;
   short STRICT_VALIDITY_CHECK = 4;

   boolean canInsertBefore(Node var1, Node var2);

   boolean canRemoveChild(Node var1);

   boolean canReplaceChild(Node var1, Node var2);

   boolean canAppendChild(Node var1);

   boolean isNodeValid(boolean var1, short var2) throws DOMASException;
}
