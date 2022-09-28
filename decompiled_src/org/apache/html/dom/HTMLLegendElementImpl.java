package org.apache.html.dom;

import org.w3c.dom.html.HTMLLegendElement;

public class HTMLLegendElementImpl extends HTMLElementImpl implements HTMLLegendElement {
   private static final long serialVersionUID = 3257846567426865457L;

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
      return this.getAttribute("align");
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public HTMLLegendElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
