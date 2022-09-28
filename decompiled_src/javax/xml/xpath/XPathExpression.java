package javax.xml.xpath;

import javax.xml.namespace.QName;
import org.xml.sax.InputSource;

public interface XPathExpression {
   Object evaluate(Object var1, QName var2) throws XPathExpressionException;

   String evaluate(Object var1) throws XPathExpressionException;

   Object evaluate(InputSource var1, QName var2) throws XPathExpressionException;

   String evaluate(InputSource var1) throws XPathExpressionException;
}
