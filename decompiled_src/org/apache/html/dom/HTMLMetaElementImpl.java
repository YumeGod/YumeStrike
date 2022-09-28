package org.apache.html.dom;

import org.w3c.dom.html.HTMLMetaElement;

public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {
   private static final long serialVersionUID = 3256722862131197489L;

   public String getContent() {
      return this.getAttribute("content");
   }

   public void setContent(String var1) {
      this.setAttribute("content", var1);
   }

   public String getHttpEquiv() {
      return this.getAttribute("http-equiv");
   }

   public void setHttpEquiv(String var1) {
      this.setAttribute("http-equiv", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getScheme() {
      return this.getAttribute("scheme");
   }

   public void setScheme(String var1) {
      this.setAttribute("scheme", var1);
   }

   public HTMLMetaElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
