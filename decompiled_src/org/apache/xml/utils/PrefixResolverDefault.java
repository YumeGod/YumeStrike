package org.apache.xml.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PrefixResolverDefault implements PrefixResolver {
   Node m_context;

   public PrefixResolverDefault(Node xpathExpressionContext) {
      this.m_context = xpathExpressionContext;
   }

   public String getNamespaceForPrefix(String prefix) {
      return this.getNamespaceForPrefix(prefix, this.m_context);
   }

   public String getNamespaceForPrefix(String prefix, Node namespaceContext) {
      Node parent = namespaceContext;
      String namespace = null;
      short type;
      if (prefix.equals("xml")) {
         namespace = "http://www.w3.org/XML/1998/namespace";
      } else {
         for(; null != parent && null == namespace && ((type = parent.getNodeType()) == 1 || type == 5); parent = parent.getParentNode()) {
            if (type == 1) {
               if (parent.getNodeName().indexOf(prefix + ":") == 0) {
                  return parent.getNamespaceURI();
               }

               NamedNodeMap nnm = parent.getAttributes();

               for(int i = 0; i < nnm.getLength(); ++i) {
                  Node attr = nnm.item(i);
                  String aname = attr.getNodeName();
                  boolean isPrefix = aname.startsWith("xmlns:");
                  if (isPrefix || aname.equals("xmlns")) {
                     int index = aname.indexOf(58);
                     String p = isPrefix ? aname.substring(index + 1) : "";
                     if (p.equals(prefix)) {
                        namespace = attr.getNodeValue();
                        break;
                     }
                  }
               }
            }
         }
      }

      return namespace;
   }

   public String getBaseIdentifier() {
      return null;
   }

   public boolean handlesNullPrefixes() {
      return false;
   }
}
