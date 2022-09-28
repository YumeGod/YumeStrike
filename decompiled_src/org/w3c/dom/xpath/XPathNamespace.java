package org.w3c.dom.xpath;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface XPathNamespace extends Node {
   short XPATH_NAMESPACE_NODE = 13;

   Element getOwnerElement();
}
