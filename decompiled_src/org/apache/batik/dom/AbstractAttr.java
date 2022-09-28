package org.apache.batik.dom;

import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

public abstract class AbstractAttr extends AbstractParentNode implements Attr {
   protected String nodeName;
   protected boolean unspecified;
   protected boolean isIdAttr;
   protected AbstractElement ownerElement;
   protected TypeInfo typeInfo;

   protected AbstractAttr() {
   }

   protected AbstractAttr(String var1, AbstractDocument var2) throws DOMException {
      this.ownerDocument = var2;
      if (var2.getStrictErrorChecking() && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "xml.name", new Object[]{var1});
      }
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
      this.isIdAttr = this.ownerDocument.isId(this);
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public short getNodeType() {
      return 2;
   }

   public String getNodeValue() throws DOMException {
      Node var1 = this.getFirstChild();
      if (var1 == null) {
         return "";
      } else {
         Node var2 = var1.getNextSibling();
         if (var2 == null) {
            return var1.getNodeValue();
         } else {
            StringBuffer var3 = new StringBuffer(var1.getNodeValue());

            do {
               var3.append(var2.getNodeValue());
               var2 = var2.getNextSibling();
            } while(var2 != null);

            return var3.toString();
         }
      }
   }

   public void setNodeValue(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else {
         String var2 = this.getNodeValue();

         Node var3;
         while((var3 = this.getFirstChild()) != null) {
            this.removeChild(var3);
         }

         String var4 = var1 == null ? "" : var1;
         Text var5 = this.getOwnerDocument().createTextNode(var4);
         this.appendChild(var5);
         if (this.ownerElement != null) {
            this.ownerElement.fireDOMAttrModifiedEvent(this.nodeName, this, var2, var4, (short)1);
         }

      }
   }

   public String getName() {
      return this.getNodeName();
   }

   public boolean getSpecified() {
      return !this.unspecified;
   }

   public void setSpecified(boolean var1) {
      this.unspecified = !var1;
   }

   public String getValue() {
      return this.getNodeValue();
   }

   public void setValue(String var1) throws DOMException {
      this.setNodeValue(var1);
   }

   public void setOwnerElement(AbstractElement var1) {
      this.ownerElement = var1;
   }

   public Element getOwnerElement() {
      return this.ownerElement;
   }

   public TypeInfo getSchemaTypeInfo() {
      if (this.typeInfo == null) {
         this.typeInfo = new AttrTypeInfo();
      }

      return this.typeInfo;
   }

   public boolean isId() {
      return this.isIdAttr;
   }

   public void setIsId(boolean var1) {
      this.isIdAttr = var1;
   }

   protected void nodeAdded(Node var1) {
      this.setSpecified(true);
   }

   protected void nodeToBeRemoved(Node var1) {
      this.setSpecified(true);
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractAttr var3 = (AbstractAttr)var1;
      var3.nodeName = this.nodeName;
      var3.unspecified = false;
      var3.isIdAttr = var2.isId(var3);
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractAttr var3 = (AbstractAttr)var1;
      var3.nodeName = this.nodeName;
      var3.unspecified = false;
      var3.isIdAttr = var2.isId(var3);
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractAttr var2 = (AbstractAttr)var1;
      var2.nodeName = this.nodeName;
      var2.unspecified = this.unspecified;
      var2.isIdAttr = this.isIdAttr;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractAttr var2 = (AbstractAttr)var1;
      var2.nodeName = this.nodeName;
      var2.unspecified = this.unspecified;
      var2.isIdAttr = this.isIdAttr;
      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      switch (var1.getNodeType()) {
         case 3:
         case 5:
         case 11:
            return;
         default:
            throw this.createDOMException((short)3, "child.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), new Integer(var1.getNodeType()), var1.getNodeName()});
      }
   }

   protected void fireDOMSubtreeModifiedEvent() {
      AbstractDocument var1 = this.getCurrentDocument();
      if (var1.getEventsEnabled()) {
         super.fireDOMSubtreeModifiedEvent();
         if (this.getOwnerElement() != null) {
            ((AbstractElement)this.getOwnerElement()).fireDOMSubtreeModifiedEvent();
         }
      }

   }

   public class AttrTypeInfo implements TypeInfo {
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
}
