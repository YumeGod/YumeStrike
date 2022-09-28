package org.apache.wml.dom;

import org.apache.wml.WMLImgElement;

public class WMLImgElementImpl extends WMLElementImpl implements WMLImgElement {
   private static final long serialVersionUID = 3257562888998040112L;

   public WMLImgElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setXmlLang(String var1) {
      this.setAttribute("xml:lang", var1);
   }

   public String getXmlLang() {
      return this.getAttribute("xml:lang");
   }

   public void setLocalSrc(String var1) {
      this.setAttribute("localsrc", var1);
   }

   public String getLocalSrc() {
      return this.getAttribute("localsrc");
   }

   public void setHeight(String var1) {
      this.setAttribute("height", var1);
   }

   public String getHeight() {
      return this.getAttribute("height");
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getAlign() {
      return this.getAttribute("align");
   }

   public void setVspace(String var1) {
      this.setAttribute("vspace", var1);
   }

   public String getVspace() {
      return this.getAttribute("vspace");
   }

   public void setAlt(String var1) {
      this.setAttribute("alt", var1);
   }

   public String getAlt() {
      return this.getAttribute("alt");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setHspace(String var1) {
      this.setAttribute("hspace", var1);
   }

   public String getHspace() {
      return this.getAttribute("hspace");
   }

   public void setSrc(String var1) {
      this.setAttribute("src", var1);
   }

   public String getSrc() {
      return this.getAttribute("src");
   }
}
