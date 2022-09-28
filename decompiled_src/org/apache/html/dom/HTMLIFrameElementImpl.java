package org.apache.html.dom;

import org.w3c.dom.html.HTMLIFrameElement;

public class HTMLIFrameElementImpl extends HTMLElementImpl implements HTMLIFrameElement {
   private static final long serialVersionUID = 3617297817824014903L;

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getFrameBorder() {
      return this.getAttribute("frameborder");
   }

   public void setFrameBorder(String var1) {
      this.setAttribute("frameborder", var1);
   }

   public String getHeight() {
      return this.getAttribute("height");
   }

   public void setHeight(String var1) {
      this.setAttribute("height", var1);
   }

   public String getLongDesc() {
      return this.getAttribute("longdesc");
   }

   public void setLongDesc(String var1) {
      this.setAttribute("longdesc", var1);
   }

   public String getMarginHeight() {
      return this.getAttribute("marginheight");
   }

   public void setMarginHeight(String var1) {
      this.setAttribute("marginheight", var1);
   }

   public String getMarginWidth() {
      return this.getAttribute("marginwidth");
   }

   public void setMarginWidth(String var1) {
      this.setAttribute("marginwidth", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getScrolling() {
      return this.capitalize(this.getAttribute("scrolling"));
   }

   public void setScrolling(String var1) {
      this.setAttribute("scrolling", var1);
   }

   public String getSrc() {
      return this.getAttribute("src");
   }

   public void setSrc(String var1) {
      this.setAttribute("src", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLIFrameElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
