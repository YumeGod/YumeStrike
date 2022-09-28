package org.apache.html.dom;

import org.w3c.dom.html.HTMLHRElement;

public class HTMLHRElementImpl extends HTMLElementImpl implements HTMLHRElement {
   private static final long serialVersionUID = 3257283617338963251L;

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public boolean getNoShade() {
      return this.getBinary("noshade");
   }

   public void setNoShade(boolean var1) {
      this.setAttribute("noshade", var1);
   }

   public String getSize() {
      return this.getAttribute("size");
   }

   public void setSize(String var1) {
      this.setAttribute("size", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLHRElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
