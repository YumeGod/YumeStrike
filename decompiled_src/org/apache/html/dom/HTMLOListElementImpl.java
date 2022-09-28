package org.apache.html.dom;

import org.w3c.dom.html.HTMLOListElement;

public class HTMLOListElementImpl extends HTMLElementImpl implements HTMLOListElement {
   private static final long serialVersionUID = 3544958748826349621L;

   public boolean getCompact() {
      return this.getBinary("compact");
   }

   public void setCompact(boolean var1) {
      this.setAttribute("compact", var1);
   }

   public int getStart() {
      return this.getInteger(this.getAttribute("start"));
   }

   public void setStart(int var1) {
      this.setAttribute("start", String.valueOf(var1));
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public HTMLOListElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
