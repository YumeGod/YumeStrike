package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class HTMLTableCellElementImpl extends HTMLElementImpl implements HTMLTableCellElement {
   private static final long serialVersionUID = 3256722862214820152L;

   public int getCellIndex() {
      Node var1 = this.getParentNode();
      int var3 = 0;
      if (var1 instanceof HTMLTableRowElement) {
         for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            if (var2 instanceof HTMLTableCellElement) {
               if (var2 == this) {
                  return var3;
               }

               ++var3;
            }
         }
      }

      return -1;
   }

   public void setCellIndex(int var1) {
      Node var2 = this.getParentNode();
      if (var2 instanceof HTMLTableRowElement) {
         for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
            if (var3 instanceof HTMLTableCellElement) {
               if (var1 == 0) {
                  if (this != var3) {
                     var2.insertBefore(this, var3);
                  }

                  return;
               }

               --var1;
            }
         }
      }

      var2.appendChild(this);
   }

   public String getAbbr() {
      return this.getAttribute("abbr");
   }

   public void setAbbr(String var1) {
      this.setAttribute("abbr", var1);
   }

   public String getAlign() {
      return this.capitalize(this.getAttribute("align"));
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getAxis() {
      return this.getAttribute("axis");
   }

   public void setAxis(String var1) {
      this.setAttribute("axis", var1);
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

   public int getColSpan() {
      return this.getInteger(this.getAttribute("colspan"));
   }

   public void setColSpan(int var1) {
      this.setAttribute("colspan", String.valueOf(var1));
   }

   public String getHeaders() {
      return this.getAttribute("headers");
   }

   public void setHeaders(String var1) {
      this.setAttribute("headers", var1);
   }

   public String getHeight() {
      return this.getAttribute("height");
   }

   public void setHeight(String var1) {
      this.setAttribute("height", var1);
   }

   public boolean getNoWrap() {
      return this.getBinary("nowrap");
   }

   public void setNoWrap(boolean var1) {
      this.setAttribute("nowrap", var1);
   }

   public int getRowSpan() {
      return this.getInteger(this.getAttribute("rowspan"));
   }

   public void setRowSpan(int var1) {
      this.setAttribute("rowspan", String.valueOf(var1));
   }

   public String getScope() {
      return this.getAttribute("scope");
   }

   public void setScope(String var1) {
      this.setAttribute("scope", var1);
   }

   public String getVAlign() {
      return this.capitalize(this.getAttribute("valign"));
   }

   public void setVAlign(String var1) {
      this.setAttribute("valign", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLTableCellElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
