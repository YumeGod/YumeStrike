package org.apache.xpath.domapi;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;

class XPathExpressionImpl implements XPathExpression {
   private final XPath m_xpath;
   private final Document m_doc;

   XPathExpressionImpl(XPath xpath, Document doc) {
      this.m_xpath = xpath;
      this.m_doc = doc;
   }

   public Object evaluate(Node contextNode, short type, Object result) throws XPathException, DOMException {
      String fmsg;
      if (this.m_doc != null) {
         if (contextNode != this.m_doc && !contextNode.getOwnerDocument().equals(this.m_doc)) {
            fmsg = XPATHMessages.createXPATHMessage("ER_WRONG_DOCUMENT", (Object[])null);
            throw new DOMException((short)4, fmsg);
         }

         short nodeType = contextNode.getNodeType();
         if (nodeType != 9 && nodeType != 1 && nodeType != 2 && nodeType != 3 && nodeType != 4 && nodeType != 8 && nodeType != 7 && nodeType != 13) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_WRONG_NODETYPE", (Object[])null);
            throw new DOMException((short)9, fmsg);
         }
      }

      if (!XPathResultImpl.isValidType(type)) {
         fmsg = XPATHMessages.createXPATHMessage("ER_INVALID_XPATH_TYPE", new Object[]{new Integer(type)});
         throw new XPathException((short)52, fmsg);
      } else {
         XPathContext xpathSupport = new XPathContext();
         if (null != this.m_doc) {
            xpathSupport.getDTMHandleFromNode(this.m_doc);
         }

         XObject xobj = null;

         try {
            xobj = this.m_xpath.execute(xpathSupport, contextNode, (PrefixResolver)null);
         } catch (TransformerException var7) {
            throw new XPathException((short)51, var7.getMessageAndLocation());
         }

         return new XPathResultImpl(type, xobj, contextNode, this.m_xpath);
      }
   }
}
