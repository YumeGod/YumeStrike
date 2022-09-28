package org.apache.batik.extension;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMElement;

public abstract class ExtensionElement extends SVGOMElement {
   protected ExtensionElement() {
   }

   protected ExtensionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public boolean isReadonly() {
      return false;
   }

   public void setReadonly(boolean var1) {
   }
}
