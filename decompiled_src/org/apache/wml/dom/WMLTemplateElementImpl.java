package org.apache.wml.dom;

import org.apache.wml.WMLTemplateElement;

public class WMLTemplateElementImpl extends WMLElementImpl implements WMLTemplateElement {
   private static final long serialVersionUID = 3761124925280301624L;

   public WMLTemplateElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setOnTimer(String var1) {
      this.setAttribute("ontimer", var1);
   }

   public String getOnTimer() {
      return this.getAttribute("ontimer");
   }

   public void setOnEnterBackward(String var1) {
      this.setAttribute("onenterbackward", var1);
   }

   public String getOnEnterBackward() {
      return this.getAttribute("onenterbackward");
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setOnEnterForward(String var1) {
      this.setAttribute("onenterforward", var1);
   }

   public String getOnEnterForward() {
      return this.getAttribute("onenterforward");
   }
}
