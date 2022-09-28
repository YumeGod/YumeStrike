package org.apache.xpath.jaxp;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

public class XPathExpressionImpl implements XPathExpression {
   private XPathFunctionResolver functionResolver;
   private XPathVariableResolver variableResolver;
   private JAXPPrefixResolver prefixResolver;
   private XPath xpath;
   private boolean featureSecureProcessing = false;
   static DocumentBuilderFactory dbf = null;
   static DocumentBuilder db = null;
   static Document d = null;

   protected XPathExpressionImpl() {
   }

   protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver) {
      this.xpath = xpath;
      this.prefixResolver = prefixResolver;
      this.functionResolver = functionResolver;
      this.variableResolver = variableResolver;
      this.featureSecureProcessing = false;
   }

   protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver, boolean featureSecureProcessing) {
      this.xpath = xpath;
      this.prefixResolver = prefixResolver;
      this.functionResolver = functionResolver;
      this.variableResolver = variableResolver;
      this.featureSecureProcessing = featureSecureProcessing;
   }

   public void setXPath(XPath xpath) {
      this.xpath = xpath;
   }

   public Object eval(Object item, QName returnType) throws TransformerException {
      XObject resultObject = this.eval(item);
      return this.getResultAsType(resultObject, returnType);
   }

   private XObject eval(Object contextItem) throws TransformerException {
      XPathContext xpathSupport = null;
      JAXPExtensionsProvider xobj;
      if (this.functionResolver != null) {
         xobj = new JAXPExtensionsProvider(this.functionResolver, this.featureSecureProcessing);
         xpathSupport = new XPathContext(xobj);
      } else {
         xpathSupport = new XPathContext();
      }

      xpathSupport.setVarStack(new JAXPVariableStack(this.variableResolver));
      xobj = null;
      Node contextNode = (Node)contextItem;
      if (contextNode == null) {
         contextNode = getDummyDocument();
      }

      XObject xobj = this.xpath.execute(xpathSupport, (Node)contextNode, this.prefixResolver);
      return xobj;
   }

   public Object evaluate(Object item, QName returnType) throws XPathExpressionException {
      String fmsg;
      if (returnType == null) {
         fmsg = XPATHMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"returnType"});
         throw new NullPointerException(fmsg);
      } else if (!this.isSupported(returnType)) {
         fmsg = XPATHMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
         throw new IllegalArgumentException(fmsg);
      } else {
         try {
            return this.eval(item, returnType);
         } catch (NullPointerException var6) {
            throw new XPathExpressionException(var6);
         } catch (TransformerException var7) {
            Throwable nestedException = var7.getException();
            if (nestedException instanceof XPathFunctionException) {
               throw (XPathFunctionException)nestedException;
            } else {
               throw new XPathExpressionException(var7);
            }
         }
      }
   }

   public String evaluate(Object item) throws XPathExpressionException {
      return (String)this.evaluate(item, XPathConstants.STRING);
   }

   public Object evaluate(InputSource source, QName returnType) throws XPathExpressionException {
      String fmsg;
      if (source != null && returnType != null) {
         if (!this.isSupported(returnType)) {
            fmsg = XPATHMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
            throw new IllegalArgumentException(fmsg);
         } else {
            try {
               if (dbf == null) {
                  dbf = DocumentBuilderFactory.newInstance();
                  dbf.setNamespaceAware(true);
                  dbf.setValidating(false);
               }

               db = dbf.newDocumentBuilder();
               Document document = db.parse(source);
               return this.eval(document, returnType);
            } catch (Exception var4) {
               throw new XPathExpressionException(var4);
            }
         }
      } else {
         fmsg = XPATHMessages.createXPATHMessage("ER_SOURCE_RETURN_TYPE_CANNOT_BE_NULL", (Object[])null);
         throw new NullPointerException(fmsg);
      }
   }

   public String evaluate(InputSource source) throws XPathExpressionException {
      return (String)this.evaluate(source, XPathConstants.STRING);
   }

   private boolean isSupported(QName returnType) {
      return returnType.equals(XPathConstants.STRING) || returnType.equals(XPathConstants.NUMBER) || returnType.equals(XPathConstants.BOOLEAN) || returnType.equals(XPathConstants.NODE) || returnType.equals(XPathConstants.NODESET);
   }

   private Object getResultAsType(XObject resultObject, QName returnType) throws TransformerException {
      if (returnType.equals(XPathConstants.STRING)) {
         return resultObject.str();
      } else if (returnType.equals(XPathConstants.NUMBER)) {
         return new Double(resultObject.num());
      } else if (returnType.equals(XPathConstants.BOOLEAN)) {
         return new Boolean(resultObject.bool());
      } else if (returnType.equals(XPathConstants.NODESET)) {
         return resultObject.nodelist();
      } else if (returnType.equals(XPathConstants.NODE)) {
         NodeIterator ni = resultObject.nodeset();
         return ni.nextNode();
      } else {
         String fmsg = XPATHMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[]{returnType.toString()});
         throw new IllegalArgumentException(fmsg);
      }
   }

   private static Document getDummyDocument() {
      try {
         if (dbf == null) {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
         }

         db = dbf.newDocumentBuilder();
         DOMImplementation dim = db.getDOMImplementation();
         d = dim.createDocument("http://java.sun.com/jaxp/xpath", "dummyroot", (DocumentType)null);
         return d;
      } catch (Exception var1) {
         var1.printStackTrace();
         return null;
      }
   }
}
