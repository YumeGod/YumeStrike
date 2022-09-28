package org.apache.xerces.dom;

import java.io.Serializable;
import java.util.Vector;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl implements NamedNodeMap, Serializable {
   static final long serialVersionUID = -7039242451046758020L;
   protected short flags;
   protected static final short READONLY = 1;
   protected static final short CHANGED = 2;
   protected static final short HASDEFAULTS = 4;
   protected Vector nodes;
   protected NodeImpl ownerNode;

   protected NamedNodeMapImpl(NodeImpl var1) {
      this.ownerNode = var1;
   }

   public int getLength() {
      return this.nodes != null ? this.nodes.size() : 0;
   }

   public Node item(int var1) {
      return this.nodes != null && var1 < this.nodes.size() ? (Node)this.nodes.elementAt(var1) : null;
   }

   public Node getNamedItem(String var1) {
      int var2 = this.findNamePoint(var1, 0);
      return var2 < 0 ? null : (Node)this.nodes.elementAt(var2);
   }

   public Node getNamedItemNS(String var1, String var2) {
      int var3 = this.findNamePoint(var1, var2);
      return var3 < 0 ? null : (Node)this.nodes.elementAt(var3);
   }

   public Node setNamedItem(Node var1) throws DOMException {
      CoreDocumentImpl var2 = this.ownerNode.ownerDocument();
      if (var2.errorChecking) {
         String var5;
         if (this.isReadOnly()) {
            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var5);
         }

         if (var1.getOwnerDocument() != var2) {
            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
            throw new DOMException((short)4, var5);
         }
      }

      int var3 = this.findNamePoint(var1.getNodeName(), 0);
      NodeImpl var4 = null;
      if (var3 >= 0) {
         var4 = (NodeImpl)this.nodes.elementAt(var3);
         this.nodes.setElementAt(var1, var3);
      } else {
         var3 = -1 - var3;
         if (null == this.nodes) {
            this.nodes = new Vector(5, 10);
         }

         this.nodes.insertElementAt(var1, var3);
      }

      return var4;
   }

   public Node setNamedItemNS(Node var1) throws DOMException {
      CoreDocumentImpl var2 = this.ownerNode.ownerDocument();
      if (var2.errorChecking) {
         String var5;
         if (this.isReadOnly()) {
            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
            throw new DOMException((short)7, var5);
         }

         if (var1.getOwnerDocument() != var2) {
            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
            throw new DOMException((short)4, var5);
         }
      }

      int var3 = this.findNamePoint(var1.getNamespaceURI(), var1.getLocalName());
      NodeImpl var4 = null;
      if (var3 >= 0) {
         var4 = (NodeImpl)this.nodes.elementAt(var3);
         this.nodes.setElementAt(var1, var3);
      } else {
         var3 = this.findNamePoint(var1.getNodeName(), 0);
         if (var3 >= 0) {
            var4 = (NodeImpl)this.nodes.elementAt(var3);
            this.nodes.insertElementAt(var1, var3);
         } else {
            var3 = -1 - var3;
            if (null == this.nodes) {
               this.nodes = new Vector(5, 10);
            }

            this.nodes.insertElementAt(var1, var3);
         }
      }

      return var4;
   }

   public Node removeNamedItem(String var1) throws DOMException {
      if (this.isReadOnly()) {
         String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var4);
      } else {
         int var2 = this.findNamePoint(var1, 0);
         if (var2 < 0) {
            String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
            throw new DOMException((short)8, var5);
         } else {
            NodeImpl var3 = (NodeImpl)this.nodes.elementAt(var2);
            this.nodes.removeElementAt(var2);
            return var3;
         }
      }
   }

   public Node removeNamedItemNS(String var1, String var2) throws DOMException {
      if (this.isReadOnly()) {
         String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null);
         throw new DOMException((short)7, var5);
      } else {
         int var3 = this.findNamePoint(var1, var2);
         if (var3 < 0) {
            String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null);
            throw new DOMException((short)8, var6);
         } else {
            NodeImpl var4 = (NodeImpl)this.nodes.elementAt(var3);
            this.nodes.removeElementAt(var3);
            return var4;
         }
      }
   }

   public NamedNodeMapImpl cloneMap(NodeImpl var1) {
      NamedNodeMapImpl var2 = new NamedNodeMapImpl(var1);
      var2.cloneContent(this);
      return var2;
   }

   protected void cloneContent(NamedNodeMapImpl var1) {
      Vector var2 = var1.nodes;
      if (var2 != null) {
         int var3 = var2.size();
         if (var3 != 0) {
            if (this.nodes == null) {
               this.nodes = new Vector(var3);
            }

            this.nodes.setSize(var3);

            for(int var4 = 0; var4 < var3; ++var4) {
               NodeImpl var5 = (NodeImpl)var1.nodes.elementAt(var4);
               NodeImpl var6 = (NodeImpl)var5.cloneNode(true);
               var6.isSpecified(var5.isSpecified());
               this.nodes.setElementAt(var6, var4);
            }
         }
      }

   }

   void setReadOnly(boolean var1, boolean var2) {
      this.isReadOnly(var1);
      if (var2 && this.nodes != null) {
         for(int var3 = this.nodes.size() - 1; var3 >= 0; --var3) {
            ((NodeImpl)this.nodes.elementAt(var3)).setReadOnly(var1, var2);
         }
      }

   }

   boolean getReadOnly() {
      return this.isReadOnly();
   }

   void setOwnerDocument(CoreDocumentImpl var1) {
      if (this.nodes != null) {
         for(int var2 = 0; var2 < this.nodes.size(); ++var2) {
            ((NodeImpl)this.item(var2)).setOwnerDocument(var1);
         }
      }

   }

   final boolean isReadOnly() {
      return (this.flags & 1) != 0;
   }

   final void isReadOnly(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 1) : (short)(this.flags & -2);
   }

   final boolean changed() {
      return (this.flags & 2) != 0;
   }

   final void changed(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 2) : (short)(this.flags & -3);
   }

   final boolean hasDefaults() {
      return (this.flags & 4) != 0;
   }

   final void hasDefaults(boolean var1) {
      this.flags = var1 ? (short)(this.flags | 4) : (short)(this.flags & -5);
   }

   protected int findNamePoint(String var1, int var2) {
      int var3 = 0;
      if (this.nodes != null) {
         int var4 = var2;
         int var5 = this.nodes.size() - 1;

         while(var4 <= var5) {
            var3 = (var4 + var5) / 2;
            int var6 = var1.compareTo(((Node)this.nodes.elementAt(var3)).getNodeName());
            if (var6 == 0) {
               return var3;
            }

            if (var6 < 0) {
               var5 = var3 - 1;
            } else {
               var4 = var3 + 1;
            }
         }

         if (var4 > var3) {
            var3 = var4;
         }
      }

      return -1 - var3;
   }

   protected int findNamePoint(String var1, String var2) {
      if (this.nodes == null) {
         return -1;
      } else if (var2 == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < this.nodes.size(); ++var3) {
            NodeImpl var4 = (NodeImpl)this.nodes.elementAt(var3);
            String var5 = var4.getNamespaceURI();
            String var6 = var4.getLocalName();
            if (var1 == null) {
               if (var5 == null && (var2.equals(var6) || var6 == null && var2.equals(var4.getNodeName()))) {
                  return var3;
               }
            } else if (var1.equals(var5) && var2.equals(var6)) {
               return var3;
            }
         }

         return -1;
      }
   }

   protected boolean precedes(Node var1, Node var2) {
      if (this.nodes != null) {
         for(int var3 = 0; var3 < this.nodes.size(); ++var3) {
            Node var4 = (Node)this.nodes.elementAt(var3);
            if (var4 == var1) {
               return true;
            }

            if (var4 == var2) {
               return false;
            }
         }
      }

      return false;
   }

   protected void removeItem(int var1) {
      if (this.nodes != null && var1 < this.nodes.size()) {
         this.nodes.removeElementAt(var1);
      }

   }

   protected Object getItem(int var1) {
      return this.nodes != null ? this.nodes.elementAt(var1) : null;
   }

   protected int addItem(Node var1) {
      int var2 = this.findNamePoint(var1.getNamespaceURI(), var1.getLocalName());
      if (var2 >= 0) {
         this.nodes.setElementAt(var1, var2);
      } else {
         var2 = this.findNamePoint(var1.getNodeName(), 0);
         if (var2 >= 0) {
            this.nodes.insertElementAt(var1, var2);
         } else {
            var2 = -1 - var2;
            if (null == this.nodes) {
               this.nodes = new Vector(5, 10);
            }

            this.nodes.insertElementAt(var1, var2);
         }
      }

      return var2;
   }

   protected Vector cloneMap(Vector var1) {
      if (var1 == null) {
         var1 = new Vector(5, 10);
      }

      var1.setSize(0);
      if (this.nodes != null) {
         for(int var2 = 0; var2 < this.nodes.size(); ++var2) {
            var1.insertElementAt(this.nodes.elementAt(var2), var2);
         }
      }

      return var1;
   }

   protected int getNamedItemIndex(String var1, String var2) {
      return this.findNamePoint(var1, var2);
   }

   public void removeAll() {
      if (this.nodes != null) {
         this.nodes.removeAllElements();
      }

   }
}
