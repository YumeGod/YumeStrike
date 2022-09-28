package org.apache.xerces.dom;

import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeepNodeListImpl implements NodeList {
   protected NodeImpl rootNode;
   protected String tagName;
   protected int changes;
   protected Vector nodes;
   protected String nsName;
   protected boolean enableNS;

   public DeepNodeListImpl(NodeImpl var1, String var2) {
      this.changes = 0;
      this.enableNS = false;
      this.rootNode = var1;
      this.tagName = var2;
      this.nodes = new Vector();
   }

   public DeepNodeListImpl(NodeImpl var1, String var2, String var3) {
      this(var1, var3);
      this.nsName = var2 != null && !var2.equals("") ? var2 : null;
      this.enableNS = true;
   }

   public int getLength() {
      this.item(Integer.MAX_VALUE);
      return this.nodes.size();
   }

   public Node item(int var1) {
      if (this.rootNode.changes() != this.changes) {
         this.nodes = new Vector();
         this.changes = this.rootNode.changes();
      }

      if (var1 < this.nodes.size()) {
         return (Node)this.nodes.elementAt(var1);
      } else {
         Object var2;
         if (this.nodes.size() == 0) {
            var2 = this.rootNode;
         } else {
            var2 = (NodeImpl)this.nodes.lastElement();
         }

         while(var2 != null && var1 >= this.nodes.size()) {
            var2 = this.nextMatchingElementAfter((Node)var2);
            if (var2 != null) {
               this.nodes.addElement(var2);
            }
         }

         return (Node)var2;
      }
   }

   protected Node nextMatchingElementAfter(Node var1) {
      while(var1 != null) {
         if (var1.hasChildNodes()) {
            var1 = var1.getFirstChild();
         } else {
            Node var2;
            if (var1 != this.rootNode && null != (var2 = var1.getNextSibling())) {
               var1 = var2;
            } else {
               for(var2 = null; var1 != this.rootNode; var1 = var1.getParentNode()) {
                  var2 = var1.getNextSibling();
                  if (var2 != null) {
                     break;
                  }
               }

               var1 = var2;
            }
         }

         if (var1 != this.rootNode && var1 != null && var1.getNodeType() == 1) {
            if (!this.enableNS) {
               if (this.tagName.equals("*") || ((ElementImpl)var1).getTagName().equals(this.tagName)) {
                  return var1;
               }
            } else {
               ElementImpl var3;
               if (this.tagName.equals("*")) {
                  if (this.nsName != null && this.nsName.equals("*")) {
                     return var1;
                  }

                  var3 = (ElementImpl)var1;
                  if (this.nsName == null && var3.getNamespaceURI() == null || this.nsName != null && this.nsName.equals(var3.getNamespaceURI())) {
                     return var1;
                  }
               } else {
                  var3 = (ElementImpl)var1;
                  if (var3.getLocalName() != null && var3.getLocalName().equals(this.tagName)) {
                     if (this.nsName != null && this.nsName.equals("*")) {
                        return var1;
                     }

                     if (this.nsName == null && var3.getNamespaceURI() == null || this.nsName != null && this.nsName.equals(var3.getNamespaceURI())) {
                        return var1;
                     }
                  }
               }
            }
         }
      }

      return null;
   }
}
