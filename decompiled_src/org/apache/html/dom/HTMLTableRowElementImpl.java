package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableRowElementImpl extends HTMLElementImpl implements HTMLTableRowElement {
   private static final long serialVersionUID = 3545231444772468278L;
   HTMLCollection _cells;

   public int getRowIndex() {
      Node var1 = this.getParentNode();
      if (var1 instanceof HTMLTableSectionElement) {
         var1 = var1.getParentNode();
      }

      return var1 instanceof HTMLTableElement ? this.getRowIndex(var1) : -1;
   }

   public void setRowIndex(int var1) {
      Node var2 = this.getParentNode();
      if (var2 instanceof HTMLTableSectionElement) {
         var2 = var2.getParentNode();
      }

      if (var2 instanceof HTMLTableElement) {
         ((HTMLTableElementImpl)var2).insertRowX(var1, this);
      }

   }

   public int getSectionRowIndex() {
      Node var1 = this.getParentNode();
      return var1 instanceof HTMLTableSectionElement ? this.getRowIndex(var1) : -1;
   }

   public void setSectionRowIndex(int var1) {
      Node var2 = this.getParentNode();
      if (var2 instanceof HTMLTableSectionElement) {
         ((HTMLTableSectionElementImpl)var2).insertRowX(var1, this);
      }

   }

   int getRowIndex(Node var1) {
      NodeList var2 = ((HTMLElement)var1).getElementsByTagName("TR");

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         if (var2.item(var3) == this) {
            return var3;
         }
      }

      return -1;
   }

   public HTMLCollection getCells() {
      if (this._cells == null) {
         this._cells = new HTMLCollectionImpl(this, (short)-3);
      }

      return this._cells;
   }

   public void setCells(HTMLCollection var1) {
      Node var2;
      for(var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         this.removeChild(var2);
      }

      int var3 = 0;

      for(var2 = var1.item(var3); var2 != null; var2 = var1.item(var3)) {
         this.appendChild(var2);
         ++var3;
      }

   }

   public HTMLElement insertCell(int var1) {
      HTMLTableCellElementImpl var3 = new HTMLTableCellElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "TD");

      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof HTMLTableCellElement) {
            if (var1 == 0) {
               this.insertBefore(var3, var2);
               return var3;
            }

            --var1;
         }
      }

      this.appendChild(var3);
      return var3;
   }

   public void deleteCell(int var1) {
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof HTMLTableCellElement) {
            if (var1 == 0) {
               this.removeChild(var2);
               return;
            }

            --var1;
         }
      }

   }

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getBgColor() {
      return this.getAttribute("bgcolor");
   }

   public void setBgColor(String var1) {
      this.setAttribute("bgcolor", var1);
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

   public Node cloneNode(boolean var1) {
      HTMLTableRowElementImpl var2 = (HTMLTableRowElementImpl)super.cloneNode(var1);
      var2._cells = null;
      return var2;
   }

   public HTMLTableRowElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
