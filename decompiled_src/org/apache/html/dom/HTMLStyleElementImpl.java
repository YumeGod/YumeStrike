package org.apache.html.dom;

import org.w3c.dom.html.HTMLStyleElement;

public class HTMLStyleElementImpl extends HTMLElementImpl implements HTMLStyleElement {
   private static final long serialVersionUID = 3258688788921594165L;

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public String getMedia() {
      return this.getAttribute("media");
   }

   public void setMedia(String var1) {
      this.setAttribute("media", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public HTMLStyleElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
