package org.apache.xerces.dom3.as;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** @deprecated */
public interface ElementEditAS extends NodeEditAS {
   NodeList getDefinedElementTypes();

   short contentType();

   boolean canSetAttribute(String var1, String var2);

   boolean canSetAttributeNode(Attr var1);

   boolean canSetAttributeNS(String var1, String var2, String var3);

   boolean canRemoveAttribute(String var1);

   boolean canRemoveAttributeNS(String var1, String var2);

   boolean canRemoveAttributeNode(Node var1);

   NodeList getChildElements();

   NodeList getParentElements();

   NodeList getAttributeList();

   boolean isElementDefined(String var1);

   boolean isElementDefinedNS(String var1, String var2, String var3);
}
