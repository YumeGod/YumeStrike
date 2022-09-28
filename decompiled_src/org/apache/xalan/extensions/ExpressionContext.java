package org.apache.xalan.extensions;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public interface ExpressionContext {
   Node getContextNode();

   NodeIterator getContextNodes();

   ErrorListener getErrorListener();

   double toNumber(Node var1);

   String toString(Node var1);

   XObject getVariableOrParam(QName var1) throws TransformerException;

   XPathContext getXPathContext() throws TransformerException;
}
