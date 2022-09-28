package org.apache.xpath.domapi;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;

public final class XPathEvaluatorImpl implements XPathEvaluator {
   private final Document m_doc;

   public XPathEvaluatorImpl(Document doc) {
      this.m_doc = doc;
   }

   public XPathEvaluatorImpl() {
      this.m_doc = null;
   }

   public XPathExpression createExpression(String expression, XPathNSResolver resolver) throws XPathException, DOMException {
      try {
         XPath xpath = new XPath(expression, (SourceLocator)null, (PrefixResolver)(null == resolver ? new DummyPrefixResolver() : (PrefixResolver)resolver), 0);
         return new XPathExpressionImpl(xpath, this.m_doc);
      } catch (TransformerException var4) {
         if (var4 instanceof XPathStylesheetDOM3Exception) {
            throw new DOMException((short)14, var4.getMessageAndLocation());
         } else {
            throw new XPathException((short)51, var4.getMessageAndLocation());
         }
      }
   }

   public XPathNSResolver createNSResolver(Node nodeResolver) {
      return new XPathNSResolverImpl((Node)(nodeResolver.getNodeType() == 9 ? ((Document)nodeResolver).getDocumentElement() : nodeResolver));
   }

   public Object evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, Object result) throws XPathException, DOMException {
      XPathExpression xpathExpression = this.createExpression(expression, resolver);
      return xpathExpression.evaluate(contextNode, type, result);
   }

   private class DummyPrefixResolver implements PrefixResolver {
      DummyPrefixResolver() {
      }

      public String getNamespaceForPrefix(String prefix, Node context) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_NULL_RESOLVER", (Object[])null);
         throw new DOMException((short)14, fmsg);
      }

      public String getNamespaceForPrefix(String prefix) {
         return this.getNamespaceForPrefix(prefix, (Node)null);
      }

      public boolean handlesNullPrefixes() {
         return false;
      }

      public String getBaseIdentifier() {
         return null;
      }
   }
}
