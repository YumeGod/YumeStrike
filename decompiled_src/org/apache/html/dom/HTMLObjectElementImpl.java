package org.apache.html.dom;

import org.w3c.dom.html.HTMLObjectElement;

public class HTMLObjectElementImpl extends HTMLElementImpl implements HTMLObjectElement, HTMLFormControl {
   private static final long serialVersionUID = 3617014156759479090L;

   public String getCode() {
      return this.getAttribute("code");
   }

   public void setCode(String var1) {
      this.setAttribute("code", var1);
   }

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getArchive() {
      return this.getAttribute("archive");
   }

   public void setArchive(String var1) {
      this.setAttribute("archive", var1);
   }

   public String getBorder() {
      return this.getAttribute("border");
   }

   public void setBorder(String var1) {
      this.setAttribute("border", var1);
   }

   public String getCodeBase() {
      return this.getAttribute("codebase");
   }

   public void setCodeBase(String var1) {
      this.setAttribute("codebase", var1);
   }

   public String getCodeType() {
      return this.getAttribute("codetype");
   }

   public void setCodeType(String var1) {
      this.setAttribute("codetype", var1);
   }

   public String getData() {
      return this.getAttribute("data");
   }

   public void setData(String var1) {
      this.setAttribute("data", var1);
   }

   public boolean getDeclare() {
      return this.getBinary("declare");
   }

   public void setDeclare(boolean var1) {
      this.setAttribute("declare", var1);
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

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getStandby() {
      return this.getAttribute("standby");
   }

   public void setStandby(String var1) {
      this.setAttribute("standby", var1);
   }

   public int getTabIndex() {
      try {
         return Integer.parseInt(this.getAttribute("tabindex"));
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   public void setTabIndex(int var1) {
      this.setAttribute("tabindex", String.valueOf(var1));
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
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

   public HTMLObjectElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
