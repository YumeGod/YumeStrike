package org.apache.html.dom;

import org.w3c.dom.html.HTMLFrameSetElement;

public class HTMLFrameSetElementImpl extends HTMLElementImpl implements HTMLFrameSetElement {
   private static final long serialVersionUID = 4049914961339888440L;

   public String getCols() {
      return this.getAttribute("cols");
   }

   public void setCols(String var1) {
      this.setAttribute("cols", var1);
   }

   public String getRows() {
      return this.getAttribute("rows");
   }

   public void setRows(String var1) {
      this.setAttribute("rows", var1);
   }

   public HTMLFrameSetElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
