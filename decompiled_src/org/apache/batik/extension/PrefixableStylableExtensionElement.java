package org.apache.batik.extension;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.DOMException;

public abstract class PrefixableStylableExtensionElement extends StylableExtensionElement {
   protected String prefix = null;

   protected PrefixableStylableExtensionElement() {
   }

   public PrefixableStylableExtensionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
      this.setPrefix(var1);
   }

   public String getNodeName() {
      return this.prefix != null && !this.prefix.equals("") ? this.prefix + ':' + this.getLocalName() : this.getLocalName();
   }

   public void setPrefix(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (var1 != null && !var1.equals("") && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
      } else {
         this.prefix = var1;
      }
   }
}
