package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface XPathEvaluator {
   XPathExpression createExpression(String var1, XPathNSResolver var2) throws XPathException, DOMException;

   XPathNSResolver createNSResolver(Node var1);

   Object evaluate(String var1, Node var2, XPathNSResolver var3, short var4, Object var5) throws XPathException, DOMException;
}
