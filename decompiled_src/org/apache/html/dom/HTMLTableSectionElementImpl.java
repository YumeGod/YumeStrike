package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableSectionElementImpl extends HTMLElementImpl implements HTMLTableSectionElement {
   private static final long serialVersionUID = 3257001042984973618L;
   private HTMLCollectionImpl _rows;

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getCh() {
      String var1 = this.getAttribute("char");
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      return var1;
   }

   public void setCh(String var1) {
      if (var1 != null && var1.length() > 1) {
         var1 = var1.substring(0, 1);
      }

      this.setAttribute("char", var1);
   }

   public String getChOff() {
      return this.getAttribute("charoff");
   }

   public void setChOff(String var1) {
      this.setAttribute("charoff", var1);
   }

   public String getVAlign() {
      return this.capitalize(this.getAttribute("valign"));
   }

   public void setVAlign(String var1) {
      this.setAttribute("valign", var1);
   }

   public HTMLCollection getRows() {
      if (this._rows == null) {
         this._rows = new HTMLCollectionImpl(this, (short)7);
      }

      return this._rows;
   }

   public HTMLElement insertRow(int var1) {
      HTMLTableRowElementImpl var2 = new HTMLTableRowElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "TR");
      var2.insertCell(0);
      if (this.insertRowX(var1, var2) >= 0) {
         this.appendChild(var2);
      }

      return var2;
   }

   int insertRowX(int var1, HTMLTableRowElementImpl var2) {
      for(Node var3 = this.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof HTMLTableRowElement) {
            if (var1 == 0) {
               this.insertBefore(var2, var3);
               return -1;
            }

            --var1;
         }
      }

      return var1;
   }

   public void deleteRow(int var1) {
      this.deleteRowX(var1);
   }

   int deleteRowX(int var1) {
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof HTMLTableRowElement) {
            if (var1 == 0) {
               this.removeChild(var2);
               return -1;
            }

            --var1;
         }
      }

      return var1;
   }

   public Node cloneNode(boolean var1) {
      HTMLTableSectionElementImpl var2 = (HTMLTableSectionElementImpl)super.cloneNode(var1);
      var2._rows = null;
      return var2;
   }

   public HTMLTableSectionElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
