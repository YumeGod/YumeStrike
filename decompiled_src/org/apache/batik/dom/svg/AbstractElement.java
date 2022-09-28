package org.apache.batik.dom.svg;

import org.apache.batik.css.engine.CSSNavigableNode;
import org.apache.batik.dom.AbstractAttr;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class AbstractElement extends org.apache.batik.dom.AbstractElement implements NodeEventTarget, CSSNavigableNode, SVGConstants {
   protected transient DoublyIndexedTable liveAttributeValues = new DoublyIndexedTable();

   protected AbstractElement() {
   }

   protected AbstractElement(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      this.setPrefix(var1);
      this.initializeAttributes();
   }

   public Node getCSSParentNode() {
      return this.getXblParentNode();
   }

   public Node getCSSPreviousSibling() {
      return this.getXblPreviousSibling();
   }

   public Node getCSSNextSibling() {
      return this.getXblNextSibling();
   }

   public Node getCSSFirstChild() {
      return this.getXblFirstChild();
   }

   public Node getCSSLastChild() {
      return this.getXblLastChild();
   }

   public boolean isHiddenFromSelectors() {
      return false;
   }

   public void fireDOMAttrModifiedEvent(String var1, Attr var2, String var3, String var4, short var5) {
      super.fireDOMAttrModifiedEvent(var1, var2, var3, var4, var5);
      if (((SVGOMDocument)this.ownerDocument).isSVG12 && (var5 == 2 || var5 == 1)) {
         Attr var6;
         if (var2.getNamespaceURI() == null && var2.getNodeName().equals("id")) {
            var6 = this.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "id");
            if (var6 == null) {
               this.setAttributeNS("http://www.w3.org/XML/1998/namespace", "id", var4);
            } else if (!var6.getNodeValue().equals(var4)) {
               var6.setNodeValue(var4);
            }
         } else if (var2.getNodeName().equals("xml:id")) {
            var6 = this.getAttributeNodeNS((String)null, "id");
            if (var6 == null) {
               this.setAttributeNS((String)null, "id", var4);
            } else if (!var6.getNodeValue().equals(var4)) {
               var6.setNodeValue(var4);
            }
         }
      }

   }

   public LiveAttributeValue getLiveAttributeValue(String var1, String var2) {
      return (LiveAttributeValue)this.liveAttributeValues.get(var1, var2);
   }

   public void putLiveAttributeValue(String var1, String var2, LiveAttributeValue var3) {
      this.liveAttributeValues.put(var1, var2, var3);
   }

   protected AttributeInitializer getAttributeInitializer() {
      return null;
   }

   protected void initializeAttributes() {
      AttributeInitializer var1 = this.getAttributeInitializer();
      if (var1 != null) {
         var1.initializeAttributes(this);
      }

   }

   protected boolean resetAttribute(String var1, String var2, String var3) {
      AttributeInitializer var4 = this.getAttributeInitializer();
      return var4 == null ? false : var4.resetAttribute(this, var1, var2, var3);
   }

   protected NamedNodeMap createAttributes() {
      return new ExtendedNamedNodeHashMap();
   }

   public void setUnspecifiedAttribute(String var1, String var2, String var3) {
      if (this.attributes == null) {
         this.attributes = this.createAttributes();
      }

      ((ExtendedNamedNodeHashMap)this.attributes).setUnspecifiedAttribute(var1, var2, var3);
   }

   protected void attrAdded(Attr var1, String var2) {
      LiveAttributeValue var3 = this.getLiveAttributeValue(var1);
      if (var3 != null) {
         var3.attrAdded(var1, var2);
      }

   }

   protected void attrModified(Attr var1, String var2, String var3) {
      LiveAttributeValue var4 = this.getLiveAttributeValue(var1);
      if (var4 != null) {
         var4.attrModified(var1, var2, var3);
      }

   }

   protected void attrRemoved(Attr var1, String var2) {
      LiveAttributeValue var3 = this.getLiveAttributeValue(var1);
      if (var3 != null) {
         var3.attrRemoved(var1, var2);
      }

   }

   private LiveAttributeValue getLiveAttributeValue(Attr var1) {
      String var2 = var1.getNamespaceURI();
      return this.getLiveAttributeValue(var2, var2 == null ? var1.getNodeName() : var1.getLocalName());
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      ((AbstractElement)var1).initializeAttributes();
      super.export(var1, var2);
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      ((AbstractElement)var1).initializeAttributes();
      super.deepExport(var1, var2);
      return var1;
   }

   protected class ExtendedNamedNodeHashMap extends org.apache.batik.dom.AbstractElement.NamedNodeHashMap {
      public ExtendedNamedNodeHashMap() {
         super();
      }

      public void setUnspecifiedAttribute(String var1, String var2, String var3) {
         Attr var4 = AbstractElement.this.getOwnerDocument().createAttributeNS(var1, var2);
         var4.setValue(var3);
         ((AbstractAttr)var4).setSpecified(false);
         this.setNamedItemNS(var4);
      }

      public Node removeNamedItemNS(String var1, String var2) throws DOMException {
         if (AbstractElement.this.isReadonly()) {
            throw AbstractElement.this.createDOMException((short)7, "readonly.node.map", new Object[0]);
         } else if (var2 == null) {
            throw AbstractElement.this.createDOMException((short)8, "attribute.missing", new Object[]{""});
         } else {
            AbstractAttr var3 = (AbstractAttr)this.remove(var1, var2);
            if (var3 == null) {
               throw AbstractElement.this.createDOMException((short)8, "attribute.missing", new Object[]{var2});
            } else {
               var3.setOwnerElement((org.apache.batik.dom.AbstractElement)null);
               String var4 = var3.getPrefix();
               if (!AbstractElement.this.resetAttribute(var1, var4, var2)) {
                  AbstractElement.this.fireDOMAttrModifiedEvent(var3.getNodeName(), var3, var3.getNodeValue(), "", (short)3);
               }

               return var3;
            }
         }
      }
   }
}
