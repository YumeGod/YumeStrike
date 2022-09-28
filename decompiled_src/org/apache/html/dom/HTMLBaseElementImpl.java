package org.apache.html.dom;

import org.w3c.dom.html.HTMLBaseElement;

public class HTMLBaseElementImpl extends HTMLElementImpl implements HTMLBaseElement {
   private static final long serialVersionUID = 3257009860519409717L;

   public String getHref() {
      return this.getAttribute("href");
   }

   public void setHref(String var1) {
      this.setAttribute("href", var1);
   }

   public String getTarget() {
      return this.getAttribute("target");
   }

   public void setTarget(String var1) {
      this.setAttribute("target", var1);
   }

   public HTMLBaseElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
