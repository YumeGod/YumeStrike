package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface XPathExpression {
   Object evaluate(Node var1, short var2, Object var3) throws XPathException, DOMException;
}
