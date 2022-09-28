package org.apache.html.dom;

import org.w3c.dom.html.HTMLTextAreaElement;

public class HTMLTextAreaElementImpl extends HTMLElementImpl implements HTMLTextAreaElement, HTMLFormControl {
   private static final long serialVersionUID = 3257852073558357816L;

   public String getDefaultValue() {
      return this.getAttribute("default-value");
   }

   public void setDefaultValue(String var1) {
      this.setAttribute("default-value", var1);
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

   public int getCols() {
      return this.getInteger(this.getAttribute("cols"));
   }

   public void setCols(int var1) {
      this.setAttribute("cols", String.valueOf(var1));
   }

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
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

   public int getRows() {
      return this.getInteger(this.getAttribute("rows"));
   }

   public void setRows(int var1) {
      this.setAttribute("rows", String.valueOf(var1));
   }

   public int getTabIndex() {
      return this.getInteger(this.getAttribute("tabindex"));
   }

   public void setTabIndex(int var1) {
      this.setAttribute("tabindex", String.valueOf(var1));
   }

   public String getType() {
      return this.getAttribute("type");
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

   public HTMLTextAreaElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
