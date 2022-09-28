package org.apache.html.dom;

import org.w3c.dom.html.HTMLTableColElement;

public class HTMLTableColElementImpl extends HTMLElementImpl implements HTMLTableColElement {
   private static final long serialVersionUID = 3257845497996915254L;

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getCh() {
      String var1 = this.getAttribute("char");
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      return var1;
   }

   public void setCh(String var1) {
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      this.setAttribute("char", var1);
   }

   public String getChOff() {
      return this.getAttribute("charoff");
   }

   public void setChOff(String var1) {
      this.setAttribute("charoff", var1);
   }

   public int getSpan() {
      return this.getInteger(this.getAttribute("span"));
   }

   public void setSpan(int var1) {
      this.setAttribute("span", String.valueOf(var1));
   }

   public String getVAlign() {
      return this.capitalize(this.getAttribute("valign"));
   }

   public void setVAlign(String var1) {
      this.setAttribute("valign", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLTableColElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
