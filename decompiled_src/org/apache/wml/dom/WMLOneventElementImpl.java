package org.apache.wml.dom;

import org.apache.wml.WMLOneventElement;

public class WMLOneventElementImpl extends WMLElementImpl implements WMLOneventElement {
   private static final long serialVersionUID = 3257283643259892021L;

   public WMLOneventElementImpl(WMLDocumentImpl var1, String var2) {
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

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }
}
