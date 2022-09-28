package org.apache.html.dom;

import org.w3c.dom.html.HTMLBodyElement;

public class HTMLBodyElementImpl extends HTMLElementImpl implements HTMLBodyElement {
   private static final long serialVersionUID = 4120852174621454900L;

   public String getALink() {
      return this.getAttribute("alink");
   }

   public void setALink(String var1) {
      this.setAttribute("alink", var1);
   }

   public String getBackground() {
      return this.getAttribute("background");
   }

   public void setBackground(String var1) {
      this.setAttribute("background", var1);
   }

   public String getBgColor() {
      return this.getAttribute("bgcolor");
   }

   public void setBgColor(String var1) {
      this.setAttribute("bgcolor", var1);
   }

   public String getLink() {
      return this.getAttribute("link");
   }

   public void setLink(String var1) {
      this.setAttribute("link", var1);
   }

   public String getText() {
      return this.getAttribute("text");
   }

   public void setText(String var1) {
      this.setAttribute("text", var1);
   }

   public String getVLink() {
      return this.getAttribute("vlink");
   }

   public void setVLink(String var1) {
      this.setAttribute("vlink", var1);
   }

   public HTMLBodyElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
