package org.apache.html.dom;

import org.w3c.dom.html.HTMLHeadElement;

public class HTMLHeadElementImpl extends HTMLElementImpl implements HTMLHeadElement {
   private static final long serialVersionUID = 3905803093357770804L;

   public String getProfile() {
      return this.getAttribute("profile");
   }

   public void setProfile(String var1) {
      this.setAttribute("profile", var1);
   }

   public HTMLHeadElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
