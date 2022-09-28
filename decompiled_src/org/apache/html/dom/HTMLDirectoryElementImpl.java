package org.apache.html.dom;

import org.w3c.dom.html.HTMLDirectoryElement;

public class HTMLDirectoryElementImpl extends HTMLElementImpl implements HTMLDirectoryElement {
   private static final long serialVersionUID = 3256436993385772854L;

   public boolean getCompact() {
      return this.getBinary("compact");
   }

   public void setCompact(boolean var1) {
      this.setAttribute("compact", var1);
   }

   public HTMLDirectoryElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
