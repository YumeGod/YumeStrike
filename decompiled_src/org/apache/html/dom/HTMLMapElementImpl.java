package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;

public class HTMLMapElementImpl extends HTMLElementImpl implements HTMLMapElement {
   private static final long serialVersionUID = 3257847692725270834L;
   private HTMLCollection _areas;

   public HTMLCollection getAreas() {
      if (this._areas == null) {
         this._areas = new HTMLCollectionImpl(this, (short)-1);
      }

      return this._areas;
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public Node cloneNode(boolean var1) {
      HTMLMapElementImpl var2 = (HTMLMapElementImpl)super.cloneNode(var1);
      var2._areas = null;
      return var2;
   }

   public HTMLMapElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
