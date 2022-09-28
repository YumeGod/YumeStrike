package org.apache.html.dom;

import org.w3c.dom.html.HTMLAreaElement;

public class HTMLAreaElementImpl extends HTMLElementImpl implements HTMLAreaElement {
   private static final long serialVersionUID = 3977019543719523380L;

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

   public String getAlt() {
      return this.getAttribute("alt");
   }

   public void setAlt(String var1) {
      this.setAttribute("alt", var1);
   }

   public String getCoords() {
      return this.getAttribute("coords");
   }

   public void setCoords(String var1) {
      this.setAttribute("coords", var1);
   }

   public String getHref() {
      return this.getAttribute("href");
   }

   public void setHref(String var1) {
      this.setAttribute("href", var1);
   }

   public boolean getNoHref() {
      return this.getBinary("href");
   }

   public void setNoHref(boolean var1) {
      this.setAttribute("nohref", var1);
   }

   public String getShape() {
      return this.capitalize(this.getAttribute("shape"));
   }

   public void setShape(String var1) {
      this.setAttribute("shape", var1);
   }

   public int getTabIndex() {
      return this.getInteger(this.getAttribute("tabindex"));
   }

   public void setTabIndex(int var1) {
      this.setAttribute("tabindex", String.valueOf(var1));
   }

   public String getTarget() {
      return this.getAttribute("target");
   }

   public void setTarget(String var1) {
      this.setAttribute("target", var1);
   }

   public HTMLAreaElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
