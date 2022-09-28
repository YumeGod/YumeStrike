package org.apache.xerces.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementDefinitionImpl extends ParentNode {
   static final long serialVersionUID = -8373890672670022714L;
   protected String name;
   protected NamedNodeMapImpl attributes;

   public ElementDefinitionImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.name = var2;
      this.attributes = new NamedNodeMapImpl(var1);
   }

   public short getNodeType() {
      return 21;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public Node cloneNode(boolean var1) {
      ElementDefinitionImpl var2 = (ElementDefinitionImpl)super.cloneNode(var1);
      var2.attributes = this.attributes.cloneMap((NodeImpl)var2);
      return var2;
   }

   public NamedNodeMap getAttributes() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.attributes;
   }
}
