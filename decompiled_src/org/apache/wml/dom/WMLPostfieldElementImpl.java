package org.apache.wml.dom;

import org.apache.wml.WMLPostfieldElement;

public class WMLPostfieldElementImpl extends WMLElementImpl implements WMLPostfieldElement {
   private static final long serialVersionUID = 3256438114523296562L;

   public WMLPostfieldElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
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

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }
}
