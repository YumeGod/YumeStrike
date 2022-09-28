package javax.xml.xpath;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.xml.sax.InputSource;

public interface XPath {
   void reset();

   void setXPathVariableResolver(XPathVariableResolver var1);

   XPathVariableResolver getXPathVariableResolver();

   void setXPathFunctionResolver(XPathFunctionResolver var1);

   XPathFunctionResolver getXPathFunctionResolver();

   void setNamespaceContext(NamespaceContext var1);

   NamespaceContext getNamespaceContext();

   XPathExpression compile(String var1) throws XPathExpressionException;

   Object evaluate(String var1, Object var2, QName var3) throws XPathExpressionException;

   String evaluate(String var1, Object var2) throws XPathExpressionException;

   Object evaluate(String var1, InputSource var2, QName var3) throws XPathExpressionException;

   String evaluate(String var1, InputSource var2) throws XPathExpressionException;
}
