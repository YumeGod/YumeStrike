package org.apache.wml.dom;

import org.apache.wml.WMLElement;
import org.apache.xerces.dom.ElementImpl;

public class WMLElementImpl extends ElementImpl implements WMLElement {
   private static final long serialVersionUID = 3689631376446338103L;

   public WMLElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
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

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   void setAttribute(String var1, boolean var2) {
      this.setAttribute(var1, var2 ? "true" : "false");
   }

   boolean getAttribute(String var1, boolean var2) {
      boolean var3 = var2;
      String var4;
      if ((var4 = this.getAttribute("emptyok")) != null && var4.equals("true")) {
         var3 = true;
      }

      return var3;
   }

   void setAttribute(String var1, int var2) {
      this.setAttribute(var1, var2 + "");
   }

   int getAttribute(String var1, int var2) {
      int var3 = var2;
      String var4;
      if ((var4 = this.getAttribute("emptyok")) != null) {
         var3 = Integer.parseInt(var4);
      }

      return var3;
   }
}
