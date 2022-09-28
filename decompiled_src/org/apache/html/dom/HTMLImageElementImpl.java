package org.apache.html.dom;

import org.w3c.dom.html.HTMLImageElement;

public class HTMLImageElementImpl extends HTMLElementImpl implements HTMLImageElement {
   private static final long serialVersionUID = 3545514006426300471L;

   public String getLowSrc() {
      return this.getAttribute("lowsrc");
   }

   public void setLowSrc(String var1) {
      this.setAttribute("lowsrc", var1);
   }

   public String getSrc() {
      return this.getAttribute("src");
   }

   public void setSrc(String var1) {
      this.setAttribute("src", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getAlt() {
      return this.getAttribute("alt");
   }

   public void setAlt(String var1) {
      this.setAttribute("alt", var1);
   }

   public String getBorder() {
      return this.getAttribute("border");
   }

   public void setBorder(String var1) {
      this.setAttribute("border", var1);
   }

   public String getHeight() {
      return this.getAttribute("height");
   }

   public void setHeight(String var1) {
      this.setAttribute("height", var1);
   }

   public String getHspace() {
      return this.getAttribute("hspace");
   }

   public void setHspace(String var1) {
      this.setAttribute("hspace", var1);
   }

   public boolean getIsMap() {
      return this.getBinary("ismap");
   }

   public void setIsMap(boolean var1) {
      this.setAttribute("ismap", var1);
   }

   public String getLongDesc() {
      return this.getAttribute("longdesc");
   }

   public void setLongDesc(String var1) {
      this.setAttribute("longdesc", var1);
   }

   public String getUseMap() {
      return this.getAttribute("useMap");
   }

   public void setUseMap(String var1) {
      this.setAttribute("useMap", var1);
   }

   public String getVspace() {
      return this.getAttribute("vspace");
   }

   public void setVspace(String var1) {
      this.setAttribute("vspace", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLImageElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
