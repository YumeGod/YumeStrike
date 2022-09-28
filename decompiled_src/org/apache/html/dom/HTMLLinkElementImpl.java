package org.apache.html.dom;

import org.w3c.dom.html.HTMLLinkElement;

public class HTMLLinkElementImpl extends HTMLElementImpl implements HTMLLinkElement {
   private static final long serialVersionUID = 4050763784366929202L;

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public String getCharset() {
      return this.getAttribute("charset");
   }

   public void setCharset(String var1) {
      this.setAttribute("charset", var1);
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

   public String getMedia() {
      return this.getAttribute("media");
   }

   public void setMedia(String var1) {
      this.setAttribute("media", var1);
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

   public HTMLLinkElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
