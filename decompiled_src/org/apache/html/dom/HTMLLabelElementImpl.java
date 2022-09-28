package org.apache.html.dom;

import org.w3c.dom.html.HTMLLabelElement;

public class HTMLLabelElementImpl extends HTMLElementImpl implements HTMLLabelElement, HTMLFormControl {
   private static final long serialVersionUID = 3834594305066416178L;

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

   public String getHtmlFor() {
      return this.getAttribute("for");
   }

   public void setHtmlFor(String var1) {
      this.setAttribute("for", var1);
   }

   public HTMLLabelElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
