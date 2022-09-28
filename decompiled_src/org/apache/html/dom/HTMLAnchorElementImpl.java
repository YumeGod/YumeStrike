package org.apache.html.dom;

import org.w3c.dom.html.HTMLAnchorElement;

public class HTMLAnchorElementImpl extends HTMLElementImpl implements HTMLAnchorElement {
   private static final long serialVersionUID = 3256441387221334069L;

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

   public String getCharset() {
      return this.getAttribute("charset");
   }

   public void setCharset(String var1) {
      this.setAttribute("charset", var1);
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

   public String getHreflang() {
      return this.getAttribute("hreflang");
   }

   public void setHreflang(String var1) {
      this.setAttribute("hreflang", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getRel() {
      return this.getAttribute("rel");
   }

   public void setRel(String var1) {
      this.setAttribute("rel", var1);
   }

   public String getRev() {
      return this.getAttribute("rev");
   }

   public void setRev(String var1) {
      this.setAttribute("rev", var1);
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

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public void blur() {
   }

   public void focus() {
   }

   public HTMLAnchorElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
