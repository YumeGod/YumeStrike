package org.apache.html.dom;

import org.w3c.dom.html.HTMLInputElement;

public class HTMLInputElementImpl extends HTMLElementImpl implements HTMLInputElement, HTMLFormControl {
   private static final long serialVersionUID = 3905799764707980082L;

   public String getDefaultValue() {
      return this.getAttribute("defaultValue");
   }

   public void setDefaultValue(String var1) {
      this.setAttribute("defaultValue", var1);
   }

   public boolean getDefaultChecked() {
      return this.getBinary("defaultChecked");
   }

   public void setDefaultChecked(boolean var1) {
      this.setAttribute("defaultChecked", var1);
   }

   public String getAccept() {
      return this.getAttribute("accept");
   }

   public void setAccept(String var1) {
      this.setAttribute("accept", var1);
   }

   public String getAccessKey() {
      String var1 = this.getAttribute("accesskey");
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      return var1;
   }

   public void setAccessKey(String var1) {
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      this.setAttribute("accesskey", var1);
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

   public boolean getChecked() {
      return this.getBinary("checked");
   }

   public void setChecked(boolean var1) {
      this.setAttribute("checked", var1);
   }

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public int getMaxLength() {
      return this.getInteger(this.getAttribute("maxlength"));
   }

   public void setMaxLength(int var1) {
      this.setAttribute("maxlength", String.valueOf(var1));
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public boolean getReadOnly() {
      return this.getBinary("readonly");
   }

   public void setReadOnly(boolean var1) {
      this.setAttribute("readonly", var1);
   }

   public String getSize() {
      return this.getAttribute("size");
   }

   public void setSize(String var1) {
      this.setAttribute("size", var1);
   }

   public String getSrc() {
      return this.getAttribute("src");
   }

   public void setSrc(String var1) {
      this.setAttribute("src", var1);
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

   public String getUseMap() {
      return this.getAttribute("useMap");
   }

   public void setUseMap(String var1) {
      this.setAttribute("useMap", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public void blur() {
   }

   public void focus() {
   }

   public void select() {
   }

   public void click() {
   }

   public HTMLInputElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
