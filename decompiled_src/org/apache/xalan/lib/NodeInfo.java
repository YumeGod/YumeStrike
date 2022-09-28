package org.apache.xalan.lib;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeInfo {
   public static String systemId(ExpressionContext context) {
      Node contextNode = context.getContextNode();
      int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
      SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
      return locator != null ? locator.getSystemId() : null;
   }

   public static String systemId(NodeList nodeList) {
      if (nodeList != null && nodeList.getLength() != 0) {
         Node node = nodeList.item(0);
         int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
         SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
         return locator != null ? locator.getSystemId() : null;
      } else {
         return null;
      }
   }

   public static String publicId(ExpressionContext context) {
      Node contextNode = context.getContextNode();
      int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
      SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
      return locator != null ? locator.getPublicId() : null;
   }

   public static String publicId(NodeList nodeList) {
      if (nodeList != null && nodeList.getLength() != 0) {
         Node node = nodeList.item(0);
         int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
         SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
         return locator != null ? locator.getPublicId() : null;
      } else {
         return null;
      }
   }

   public static int lineNumber(ExpressionContext context) {
      Node contextNode = context.getContextNode();
      int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
      SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
      return locator != null ? locator.getLineNumber() : -1;
   }

   public static int lineNumber(NodeList nodeList) {
      if (nodeList != null && nodeList.getLength() != 0) {
         Node node = nodeList.item(0);
         int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
         SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
         return locator != null ? locator.getLineNumber() : -1;
      } else {
         return -1;
      }
   }

   public static int columnNumber(ExpressionContext context) {
      Node contextNode = context.getContextNode();
      int nodeHandler = ((DTMNodeProxy)contextNode).getDTMNodeNumber();
      SourceLocator locator = ((DTMNodeProxy)contextNode).getDTM().getSourceLocatorFor(nodeHandler);
      return locator != null ? locator.getColumnNumber() : -1;
   }

   public static int columnNumber(NodeList nodeList) {
      if (nodeList != null && nodeList.getLength() != 0) {
         Node node = nodeList.item(0);
         int nodeHandler = ((DTMNodeProxy)node).getDTMNodeNumber();
         SourceLocator locator = ((DTMNodeProxy)node).getDTM().getSourceLocatorFor(nodeHandler);
         return locator != null ? locator.getColumnNumber() : -1;
      } else {
         return -1;
      }
   }
}
