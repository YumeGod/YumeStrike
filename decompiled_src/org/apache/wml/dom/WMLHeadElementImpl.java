package org.apache.wml.dom;

import org.apache.wml.WMLHeadElement;

public class WMLHeadElementImpl extends WMLElementImpl implements WMLHeadElement {
   private static final long serialVersionUID = 3689346607128524595L;

   public WMLHeadElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
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
}
