package org.apache.html.dom;

import org.w3c.dom.html.HTMLAppletElement;

public class HTMLAppletElementImpl extends HTMLElementImpl implements HTMLAppletElement {
   private static final long serialVersionUID = 4049641191635498032L;

   public String getAlign() {
      return this.getAttribute("align");
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

   public String getArchive() {
      return this.getAttribute("archive");
   }

   public void setArchive(String var1) {
      this.setAttribute("archive", var1);
   }

   public String getCode() {
      return this.getAttribute("code");
   }

   public void setCode(String var1) {
      this.setAttribute("code", var1);
   }

   public String getCodeBase() {
      return this.getAttribute("codebase");
   }

   public void setCodeBase(String var1) {
      this.setAttribute("codebase", var1);
   }

   public String getHeight() {
      return this.getAttribute("height");
   }

   public void setHeight(String var1) {
      this.setAttribute("height", var1);
   }

   public String getHspace() {
      return this.getAttribute("height");
   }

   public void setHspace(String var1) {
      this.setAttribute("height", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getObject() {
      return this.getAttribute("object");
   }

   public void setObject(String var1) {
      this.setAttribute("object", var1);
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

   public HTMLAppletElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
