package org.apache.html.dom;

import org.w3c.dom.html.HTMLParamElement;

public class HTMLParamElementImpl extends HTMLElementImpl implements HTMLParamElement {
   private static final long serialVersionUID = 3258412815831020848L;

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public String getValueType() {
      return this.capitalize(this.getAttribute("valuetype"));
   }

   public void setValueType(String var1) {
      this.setAttribute("valuetype", var1);
   }

   public HTMLParamElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
