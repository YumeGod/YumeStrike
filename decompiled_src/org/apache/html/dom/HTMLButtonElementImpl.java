package org.apache.html.dom;

import org.w3c.dom.html.HTMLButtonElement;

public class HTMLButtonElementImpl extends HTMLElementImpl implements HTMLButtonElement, HTMLFormControl {
   private static final long serialVersionUID = 3258131349495100728L;

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
      return this.capitalize(this.getAttribute("type"));
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public HTMLButtonElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
