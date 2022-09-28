package org.apache.xalan.lib;

import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ExsltBase {
   protected static String toString(Node n) {
      if (n instanceof DTMNodeProxy) {
         return ((DTMNodeProxy)n).getStringValue();
      } else {
         String value = n.getNodeValue();
         if (value != null) {
            return value;
         } else {
            NodeList nodelist = n.getChildNodes();
            StringBuffer buf = new StringBuffer();

            for(int i = 0; i < nodelist.getLength(); ++i) {
               Node childNode = nodelist.item(i);
               buf.append(toString(childNode));
            }

            return buf.toString();
         }
      }
   }

   protected static double toNumber(Node n) {
      double d = 0.0;
      String str = toString(n);

      try {
         d = Double.valueOf(str);
      } catch (NumberFormatException var5) {
         d = Double.NaN;
      }

      return d;
   }
}
