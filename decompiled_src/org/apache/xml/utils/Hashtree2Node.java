package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class Hashtree2Node {
   public static void appendHashToNode(Hashtable hash, String name, Node container, Document factory) {
      if (null != container && null != factory && null != hash) {
         String elemName = null;
         if (null != name && !"".equals(name)) {
            elemName = name;
         } else {
            elemName = "appendHashToNode";
         }

         try {
            Element hashNode = factory.createElement(elemName);
            container.appendChild(hashNode);
            Enumeration keys = hash.keys();
            Vector v = new Vector();

            while(keys.hasMoreElements()) {
               Object key = keys.nextElement();
               String keyStr = key.toString();
               Object item = hash.get(key);
               if (item instanceof Hashtable) {
                  v.addElement(keyStr);
                  v.addElement((Hashtable)item);
               } else {
                  try {
                     Element node = factory.createElement("item");
                     node.setAttribute("key", keyStr);
                     node.appendChild(factory.createTextNode((String)item));
                     hashNode.appendChild(node);
                  } catch (Exception var13) {
                     Element node = factory.createElement("item");
                     node.setAttribute("key", keyStr);
                     node.appendChild(factory.createTextNode("ERROR: Reading " + key + " threw: " + var13.toString()));
                     hashNode.appendChild(node);
                  }
               }
            }

            keys = v.elements();

            while(keys.hasMoreElements()) {
               String n = (String)keys.nextElement();
               Hashtable h = (Hashtable)keys.nextElement();
               appendHashToNode(h, n, hashNode, factory);
            }
         } catch (Exception var14) {
            var14.printStackTrace();
         }

      }
   }
}
