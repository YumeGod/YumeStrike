package org.apache.batik.dom;

import java.io.Serializable;
import org.apache.batik.dom.events.DOMMutationEvent;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.ElementTraversal;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

public abstract class AbstractElement extends AbstractParentChildNode implements Element, ElementTraversal {
   protected NamedNodeMap attributes;
   protected TypeInfo typeInfo;

   protected AbstractElement() {
   }

   protected AbstractElement(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      if (var2.getStrictErrorChecking() && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "xml.name", new Object[]{var1});
      }
   }

   public short getNodeType() {
      return 1;
   }

   public boolean hasAttributes() {
      return this.attributes != null && this.attributes.getLength() != 0;
   }

   public NamedNodeMap getAttributes() {
      return this.attributes == null ? (this.attributes = this.createAttributes()) : this.attributes;
   }

   public String getTagName() {
      return this.getNodeName();
   }

   public boolean hasAttribute(String var1) {
      return this.attributes != null && this.attributes.getNamedItem(var1) != null;
   }

   public String getAttribute(String var1) {
      if (this.attributes == null) {
         return "";
      } else {
         Attr var2 = (Attr)this.attributes.getNamedItem(var1);
         return var2 == null ? "" : var2.getValue();
      }
   }

   public void setAttribute(String var1, String var2) throws DOMException {
      if (this.attributes == null) {
         this.attributes = this.createAttributes();
      }

      Attr var3 = this.getAttributeNode(var1);
      if (var3 == null) {
         var3 = this.getOwnerDocument().createAttribute(var1);
         var3.setValue(var2);
         this.attributes.setNamedItem(var3);
      } else {
         var3.setValue(var2);
      }

   }

   public void removeAttribute(String var1) throws DOMException {
      if (this.hasAttribute(var1)) {
         this.attributes.removeNamedItem(var1);
      }
   }

   public Attr getAttributeNode(String var1) {
      return this.attributes == null ? null : (Attr)this.attributes.getNamedItem(var1);
   }

   public Attr setAttributeNode(Attr var1) throws DOMException {
      if (var1 == null) {
         return null;
      } else {
         if (this.attributes == null) {
            this.attributes = this.createAttributes();
         }

         return (Attr)this.attributes.setNamedItemNS(var1);
      }
   }

   public Attr removeAttributeNode(Attr var1) throws DOMException {
      if (var1 == null) {
         return null;
      } else if (this.attributes == null) {
         throw this.createDOMException((short)8, "attribute.missing", new Object[]{var1.getName()});
      } else {
         String var2 = var1.getNamespaceURI();
         return (Attr)this.attributes.removeNamedItemNS(var2, var2 == null ? var1.getNodeName() : var1.getLocalName());
      }
   }

   public void normalize() {
      super.normalize();
      if (this.attributes != null) {
         NamedNodeMap var1 = this.getAttributes();

         for(int var2 = var1.getLength() - 1; var2 >= 0; --var2) {
            var1.item(var2).normalize();
         }
      }

   }

   public boolean hasAttributeNS(String var1, String var2) {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return this.attributes != null && this.attributes.getNamedItemNS(var1, var2) != null;
   }

   public String getAttributeNS(String var1, String var2) {
      if (this.attributes == null) {
         return "";
      } else {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         Attr var3 = (Attr)this.attributes.getNamedItemNS(var1, var2);
         return var3 == null ? "" : var3.getValue();
      }
   }

   public void setAttributeNS(String var1, String var2, String var3) throws DOMException {
      if (this.attributes == null) {
         this.attributes = this.createAttributes();
      }

      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      Attr var4 = this.getAttributeNodeNS(var1, var2);
      if (var4 == null) {
         var4 = this.getOwnerDocument().createAttributeNS(var1, var2);
         var4.setValue(var3);
         this.attributes.setNamedItemNS(var4);
      } else {
         var4.setValue(var3);
      }

   }

   public void removeAttributeNS(String var1, String var2) throws DOMException {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      if (this.hasAttributeNS(var1, var2)) {
         this.attributes.removeNamedItemNS(var1, var2);
      }
   }

   public Attr getAttributeNodeNS(String var1, String var2) {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return this.attributes == null ? null : (Attr)this.attributes.getNamedItemNS(var1, var2);
   }

   public Attr setAttributeNodeNS(Attr var1) throws DOMException {
      if (var1 == null) {
         return null;
      } else {
         if (this.attributes == null) {
            this.attributes = this.createAttributes();
         }

         return (Attr)this.attributes.setNamedItemNS(var1);
      }
   }

   public TypeInfo getSchemaTypeInfo() {
      if (this.typeInfo == null) {
         this.typeInfo = new ElementTypeInfo();
      }

      return this.typeInfo;
   }

   public void setIdAttribute(String var1, boolean var2) throws DOMException {
      AbstractAttr var3 = (AbstractAttr)this.getAttributeNode(var1);
      if (var3 == null) {
         throw this.createDOMException((short)8, "attribute.missing", new Object[]{var1});
      } else if (var3.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{var1});
      } else {
         var3.isIdAttr = var2;
      }
   }

   public void setIdAttributeNS(String var1, String var2, boolean var3) throws DOMException {
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      AbstractAttr var4 = (AbstractAttr)this.getAttributeNodeNS(var1, var2);
      if (var4 == null) {
         throw this.createDOMException((short)8, "attribute.missing", new Object[]{var1, var2});
      } else if (var4.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{var4.getNodeName()});
      } else {
         var4.isIdAttr = var3;
      }
   }

   public void setIdAttributeNode(Attr var1, boolean var2) throws DOMException {
      AbstractAttr var3 = (AbstractAttr)var1;
      if (var3.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{var3.getNodeName()});
      } else {
         var3.isIdAttr = var2;
      }
   }

   protected Attr getIdAttribute() {
      NamedNodeMap var1 = this.getAttributes();
      if (var1 == null) {
         return null;
      } else {
         int var2 = var1.getLength();

         for(int var3 = 0; var3 < var2; ++var3) {
            AbstractAttr var4 = (AbstractAttr)var1.item(var3);
            if (var4.isId()) {
               return var4;
            }
         }

         return null;
      }
   }

   protected String getId() {
      Attr var1 = this.getIdAttribute();
      if (var1 != null) {
         String var2 = var1.getNodeValue();
         if (var2.length() > 0) {
            return var2;
         }
      }

      return null;
   }

   protected void nodeAdded(Node var1) {
      this.invalidateElementsByTagName(var1);
   }

   protected void nodeToBeRemoved(Node var1) {
      this.invalidateElementsByTagName(var1);
   }

   private void invalidateElementsByTagName(Node var1) {
      if (var1.getNodeType() == 1) {
         AbstractDocument var2 = this.getCurrentDocument();
         String var3 = var1.getNamespaceURI();
         String var4 = var1.getNodeName();
         String var5 = var3 == null ? var1.getNodeName() : var1.getLocalName();
         Object var6 = this;

         while(var6 != null) {
            switch (((Node)var6).getNodeType()) {
               case 1:
               case 9:
                  AbstractParentNode.ElementsByTagName var7 = var2.getElementsByTagName((Node)var6, var4);
                  if (var7 != null) {
                     var7.invalidate();
                  }

                  var7 = var2.getElementsByTagName((Node)var6, "*");
                  if (var7 != null) {
                     var7.invalidate();
                  }

                  AbstractParentNode.ElementsByTagNameNS var8 = var2.getElementsByTagNameNS((Node)var6, var3, var5);
                  if (var8 != null) {
                     var8.invalidate();
                  }

                  var8 = var2.getElementsByTagNameNS((Node)var6, "*", var5);
                  if (var8 != null) {
                     var8.invalidate();
                  }

                  var8 = var2.getElementsByTagNameNS((Node)var6, var3, "*");
                  if (var8 != null) {
                     var8.invalidate();
                  }

                  var8 = var2.getElementsByTagNameNS((Node)var6, "*", "*");
                  if (var8 != null) {
                     var8.invalidate();
                  }
               default:
                  var6 = ((Node)var6).getParentNode();
            }
         }

         for(Node var9 = var1.getFirstChild(); var9 != null; var9 = var9.getNextSibling()) {
            this.invalidateElementsByTagName(var9);
         }

      }
   }

   protected NamedNodeMap createAttributes() {
      return new NamedNodeHashMap();
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractElement var3 = (AbstractElement)var1;
      if (this.attributes != null) {
         NamedNodeMap var4 = this.attributes;

         for(int var5 = var4.getLength() - 1; var5 >= 0; --var5) {
            AbstractAttr var6 = (AbstractAttr)var4.item(var5);
            if (var6.getSpecified()) {
               Attr var7 = (Attr)var6.deepExport(var6.cloneNode(false), var2);
               if (var6 instanceof AbstractAttrNS) {
                  var3.setAttributeNodeNS(var7);
               } else {
                  var3.setAttributeNode(var7);
               }
            }
         }
      }

      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractElement var3 = (AbstractElement)var1;
      if (this.attributes != null) {
         NamedNodeMap var4 = this.attributes;

         for(int var5 = var4.getLength() - 1; var5 >= 0; --var5) {
            AbstractAttr var6 = (AbstractAttr)var4.item(var5);
            if (var6.getSpecified()) {
               Attr var7 = (Attr)var6.deepExport(var6.cloneNode(false), var2);
               if (var6 instanceof AbstractAttrNS) {
                  var3.setAttributeNodeNS(var7);
               } else {
                  var3.setAttributeNode(var7);
               }
            }
         }
      }

      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractElement var2 = (AbstractElement)var1;
      if (this.attributes != null) {
         NamedNodeMap var3 = this.attributes;

         for(int var4 = var3.getLength() - 1; var4 >= 0; --var4) {
            AbstractAttr var5 = (AbstractAttr)var3.item(var4).cloneNode(true);
            if (var5 instanceof AbstractAttrNS) {
               var2.setAttributeNodeNS(var5);
            } else {
               var2.setAttributeNode(var5);
            }
         }
      }

      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractElement var2 = (AbstractElement)var1;
      if (this.attributes != null) {
         NamedNodeMap var3 = this.attributes;

         for(int var4 = var3.getLength() - 1; var4 >= 0; --var4) {
            AbstractAttr var5 = (AbstractAttr)var3.item(var4).cloneNode(true);
            if (var5 instanceof AbstractAttrNS) {
               var2.setAttributeNodeNS(var5);
            } else {
               var2.setAttributeNode(var5);
            }
         }
      }

      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      switch (var1.getNodeType()) {
         case 1:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 11:
            return;
         case 2:
         case 6:
         case 9:
         case 10:
         default:
            throw this.createDOMException((short)3, "child.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), new Integer(var1.getNodeType()), var1.getNodeName()});
      }
   }

   public void fireDOMAttrModifiedEvent(String var1, Attr var2, String var3, String var4, short var5) {
      switch (var5) {
         case 1:
            if (((AbstractAttr)var2).isId()) {
               this.ownerDocument.updateIdEntry(this, var3, var4);
            }

            this.attrModified(var2, var3, var4);
            break;
         case 2:
            if (((AbstractAttr)var2).isId()) {
               this.ownerDocument.addIdEntry(this, var4);
            }

            this.attrAdded(var2, var4);
            break;
         default:
            if (((AbstractAttr)var2).isId()) {
               this.ownerDocument.removeIdEntry(this, var3);
            }

            this.attrRemoved(var2, var3);
      }

      AbstractDocument var6 = this.getCurrentDocument();
      if (var6.getEventsEnabled() && !var3.equals(var4)) {
         DOMMutationEvent var7 = (DOMMutationEvent)var6.createEvent("MutationEvents");
         var7.initMutationEventNS("http://www.w3.org/2001/xml-events", "DOMAttrModified", true, false, var2, var3, var4, var1, var5);
         this.dispatchEvent(var7);
      }

   }

   protected void attrAdded(Attr var1, String var2) {
   }

   protected void attrModified(Attr var1, String var2, String var3) {
   }

   protected void attrRemoved(Attr var1, String var2) {
   }

   public Element getFirstElementChild() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public Element getLastElementChild() {
      for(Node var1 = this.getLastChild(); var1 != null; var1 = var1.getPreviousSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public Element getNextElementSibling() {
      for(Node var1 = this.getNextSibling(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return null;
   }

   public Element getPreviousElementSibling() {
      Node var1;
      for(var1 = this.getPreviousSibling(); var1 != null; var1 = var1.getPreviousSibling()) {
         if (var1.getNodeType() == 1) {
            return (Element)var1;
         }
      }

      return (Element)var1;
   }

   public int getChildElementCount() {
      this.getChildNodes();
      return this.childNodes.elementChildren;
   }

   public class ElementTypeInfo implements TypeInfo {
      public String getTypeNamespace() {
         return null;
      }

      public String getTypeName() {
         return null;
      }

      public boolean isDerivedFrom(String var1, String var2, int var3) {
         return false;
      }
   }

   protected static class Entry implements Serializable {
      public int hash;
      public String namespaceURI;
      public String name;
      public Node value;
      public Entry next;

      public Entry(int var1, String var2, String var3, Node var4, Entry var5) {
         this.hash = var1;
         this.namespaceURI = var2;
         this.name = var3;
         this.value = var4;
         this.next = var5;
      }

      public boolean match(String var1, String var2) {
         if (this.namespaceURI != null) {
            if (!this.namespaceURI.equals(var1)) {
               return false;
            }
         } else if (var1 != null) {
            return false;
         }

         return this.name.equals(var2);
      }
   }

   public class NamedNodeHashMap implements NamedNodeMap, Serializable {
      protected static final int INITIAL_CAPACITY = 3;
      protected Entry[] table = new Entry[3];
      protected int count;

      public Node getNamedItem(String var1) {
         return var1 == null ? null : this.get((String)null, var1);
      }

      public Node setNamedItem(Node var1) throws DOMException {
         if (var1 == null) {
            return null;
         } else {
            this.checkNode(var1);
            return this.setNamedItem((String)null, var1.getNodeName(), var1);
         }
      }

      public Node removeNamedItem(String var1) throws DOMException {
         return this.removeNamedItemNS((String)null, var1);
      }

      public Node item(int var1) {
         if (var1 >= 0 && var1 < this.count) {
            int var2 = 0;

            for(int var3 = 0; var3 < this.table.length; ++var3) {
               Entry var4 = this.table[var3];
               if (var4 != null) {
                  do {
                     if (var2++ == var1) {
                        return var4.value;
                     }

                     var4 = var4.next;
                  } while(var4 != null);
               }
            }

            return null;
         } else {
            return null;
         }
      }

      public int getLength() {
         return this.count;
      }

      public Node getNamedItemNS(String var1, String var2) {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         return this.get(var1, var2);
      }

      public Node setNamedItemNS(Node var1) throws DOMException {
         if (var1 == null) {
            return null;
         } else {
            String var2 = var1.getNamespaceURI();
            return this.setNamedItem(var2, var2 == null ? var1.getNodeName() : var1.getLocalName(), var1);
         }
      }

      public Node removeNamedItemNS(String var1, String var2) throws DOMException {
         if (AbstractElement.this.isReadonly()) {
            throw AbstractElement.this.createDOMException((short)7, "readonly.node.map", new Object[0]);
         } else if (var2 == null) {
            throw AbstractElement.this.createDOMException((short)8, "attribute.missing", new Object[]{""});
         } else {
            if (var1 != null && var1.length() == 0) {
               var1 = null;
            }

            AbstractAttr var3 = (AbstractAttr)this.remove(var1, var2);
            if (var3 == null) {
               throw AbstractElement.this.createDOMException((short)8, "attribute.missing", new Object[]{var2});
            } else {
               var3.setOwnerElement((AbstractElement)null);
               AbstractElement.this.fireDOMAttrModifiedEvent(var3.getNodeName(), var3, var3.getNodeValue(), "", (short)3);
               return var3;
            }
         }
      }

      public Node setNamedItem(String var1, String var2, Node var3) throws DOMException {
         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         ((AbstractAttr)var3).setOwnerElement(AbstractElement.this);
         AbstractAttr var4 = (AbstractAttr)this.put(var1, var2, var3);
         if (var4 != null) {
            var4.setOwnerElement((AbstractElement)null);
            AbstractElement.this.fireDOMAttrModifiedEvent(var2, var4, var4.getNodeValue(), "", (short)3);
         }

         AbstractElement.this.fireDOMAttrModifiedEvent(var2, (Attr)var3, "", var3.getNodeValue(), (short)2);
         return var4;
      }

      protected void checkNode(Node var1) {
         if (AbstractElement.this.isReadonly()) {
            throw AbstractElement.this.createDOMException((short)7, "readonly.node.map", new Object[0]);
         } else if (AbstractElement.this.getOwnerDocument() != var1.getOwnerDocument()) {
            throw AbstractElement.this.createDOMException((short)4, "node.from.wrong.document", new Object[]{new Integer(var1.getNodeType()), var1.getNodeName()});
         } else if (var1.getNodeType() == 2 && ((Attr)var1).getOwnerElement() != null) {
            throw AbstractElement.this.createDOMException((short)4, "inuse.attribute", new Object[]{var1.getNodeName()});
         }
      }

      protected Node get(String var1, String var2) {
         int var3 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
         int var4 = var3 % this.table.length;

         for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && var5.match(var1, var2)) {
               return var5.value;
            }
         }

         return null;
      }

      protected Node put(String var1, String var2, Node var3) {
         int var4 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
         int var5 = var4 % this.table.length;

         for(Entry var6 = this.table[var5]; var6 != null; var6 = var6.next) {
            if (var6.hash == var4 && var6.match(var1, var2)) {
               Node var7 = var6.value;
               var6.value = var3;
               return var7;
            }
         }

         int var8 = this.table.length;
         if (this.count++ >= var8 - (var8 >> 2)) {
            this.rehash();
            var5 = var4 % this.table.length;
         }

         Entry var9 = new Entry(var4, var1, var2, var3, this.table[var5]);
         this.table[var5] = var9;
         return null;
      }

      protected Node remove(String var1, String var2) {
         int var3 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
         int var4 = var3 % this.table.length;
         Entry var5 = null;

         for(Entry var6 = this.table[var4]; var6 != null; var6 = var6.next) {
            if (var6.hash == var3 && var6.match(var1, var2)) {
               Node var7 = var6.value;
               if (var5 == null) {
                  this.table[var4] = var6.next;
               } else {
                  var5.next = var6.next;
               }

               --this.count;
               return var7;
            }

            var5 = var6;
         }

         return null;
      }

      protected void rehash() {
         Entry[] var1 = this.table;
         this.table = new Entry[var1.length * 2 + 1];

         Entry var4;
         int var5;
         for(int var2 = var1.length - 1; var2 >= 0; --var2) {
            for(Entry var3 = var1[var2]; var3 != null; this.table[var5] = var4) {
               var4 = var3;
               var3 = var3.next;
               var5 = var4.hash % this.table.length;
               var4.next = this.table[var5];
            }
         }

      }

      protected int hashCode(String var1, String var2) {
         int var3 = var1 == null ? 0 : var1.hashCode();
         return var3 ^ var2.hashCode();
      }
   }
}
