package org.apache.xml.serializer.utils;

import org.w3c.dom.Node;

public final class DOM2Helper {
   public String getLocalNameOfNode(Node n) {
      String name = n.getLocalName();
      return null == name ? this.getLocalNameOfNodeFallback(n) : name;
   }

   private String getLocalNameOfNodeFallback(Node n) {
      String qname = n.getNodeName();
      int index = qname.indexOf(58);
      return index < 0 ? qname : qname.substring(index + 1);
   }

   public String getNamespaceOfNode(Node n) {
      return n.getNamespaceURI();
   }
}
