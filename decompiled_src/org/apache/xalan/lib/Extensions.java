package org.apache.xalan.lib;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.xslt.EnvironmentCheck;
import org.apache.xml.utils.Hashtree2Node;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.NodeSet;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXNotSupportedException;

public class Extensions {
   // $FF: synthetic field
   static Class class$java$util$Hashtable;
   // $FF: synthetic field
   static Class class$java$lang$String;

   private Extensions() {
   }

   public static NodeSet nodeset(ExpressionContext myProcessor, Object rtf) {
      if (rtf instanceof NodeIterator) {
         return new NodeSet((NodeIterator)rtf);
      } else {
         String textNodeValue;
         if (rtf instanceof String) {
            textNodeValue = (String)rtf;
         } else if (rtf instanceof Boolean) {
            textNodeValue = (new XBoolean((Boolean)rtf)).str();
         } else if (rtf instanceof Double) {
            textNodeValue = (new XNumber((Double)rtf)).str();
         } else {
            textNodeValue = rtf.toString();
         }

         try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document myDoc = db.newDocument();
            Text textNode = myDoc.createTextNode(textNodeValue);
            DocumentFragment docFrag = myDoc.createDocumentFragment();
            docFrag.appendChild(textNode);
            return new NodeSet(docFrag);
         } catch (ParserConfigurationException var8) {
            throw new WrappedRuntimeException(var8);
         }
      }
   }

   public static NodeList intersection(NodeList nl1, NodeList nl2) {
      return ExsltSets.intersection(nl1, nl2);
   }

   public static NodeList difference(NodeList nl1, NodeList nl2) {
      return ExsltSets.difference(nl1, nl2);
   }

   public static NodeList distinct(NodeList nl) {
      return ExsltSets.distinct(nl);
   }

   public static boolean hasSameNodes(NodeList nl1, NodeList nl2) {
      NodeSet ns1 = new NodeSet(nl1);
      NodeSet ns2 = new NodeSet(nl2);
      if (ns1.getLength() != ns2.getLength()) {
         return false;
      } else {
         for(int i = 0; i < ns1.getLength(); ++i) {
            Node n = ns1.elementAt(i);
            if (!ns2.contains(n)) {
               return false;
            }
         }

         return true;
      }
   }

   public static XObject evaluate(ExpressionContext myContext, String xpathExpr) throws SAXNotSupportedException {
      return ExsltDynamic.evaluate(myContext, xpathExpr);
   }

   public static NodeList tokenize(String toTokenize, String delims) {
      Document doc = Extensions.DocumentHolder.m_doc;
      StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
      NodeSet resultSet = new NodeSet();
      synchronized(doc) {
         while(lTokenizer.hasMoreTokens()) {
            resultSet.addNode(doc.createTextNode(lTokenizer.nextToken()));
         }

         return resultSet;
      }
   }

   public static NodeList tokenize(String toTokenize) {
      return tokenize(toTokenize, " \t\n\r");
   }

   public static Node checkEnvironment(ExpressionContext myContext) {
      Document factoryDocument;
      DocumentBuilderFactory resultNode;
      DocumentBuilder db;
      try {
         resultNode = DocumentBuilderFactory.newInstance();
         db = resultNode.newDocumentBuilder();
         factoryDocument = db.newDocument();
      } catch (ParserConfigurationException var6) {
         throw new WrappedRuntimeException(var6);
      }

      resultNode = null;

      try {
         Node resultNode = checkEnvironmentUsingWhich(myContext, factoryDocument);
         if (null != resultNode) {
            return resultNode;
         } else {
            EnvironmentCheck envChecker = new EnvironmentCheck();
            Hashtable h = envChecker.getEnvironmentHash();
            Node resultNode = factoryDocument.createElement("checkEnvironmentExtension");
            envChecker.appendEnvironmentReport(resultNode, factoryDocument, h);
            db = null;
            return resultNode;
         }
      } catch (Exception var5) {
         throw new WrappedRuntimeException(var5);
      }
   }

   private static Node checkEnvironmentUsingWhich(ExpressionContext myContext, Document factoryDocument) {
      String WHICH_CLASSNAME = "org.apache.env.Which";
      String WHICH_METHODNAME = "which";
      Class[] WHICH_METHOD_ARGS = new Class[]{class$java$util$Hashtable == null ? (class$java$util$Hashtable = class$("java.util.Hashtable")) : class$java$util$Hashtable, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};

      try {
         Class clazz = ObjectFactory.findProviderClass("org.apache.env.Which", ObjectFactory.findClassLoader(), true);
         if (null == clazz) {
            return null;
         } else {
            Method method = clazz.getMethod("which", WHICH_METHOD_ARGS);
            Hashtable report = new Hashtable();
            Object[] methodArgs = new Object[]{report, "XmlCommons;Xalan;Xerces;Crimson;Ant", ""};
            method.invoke((Object)null, methodArgs);
            Node resultNode = factoryDocument.createElement("checkEnvironmentExtension");
            Hashtree2Node.appendHashToNode(report, "whichReport", resultNode, factoryDocument);
            return resultNode;
         }
      } catch (Throwable var11) {
         return null;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static class DocumentHolder {
      private static final Document m_doc;

      static {
         try {
            m_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
         } catch (ParserConfigurationException var1) {
            throw new WrappedRuntimeException(var1);
         }
      }
   }
}
