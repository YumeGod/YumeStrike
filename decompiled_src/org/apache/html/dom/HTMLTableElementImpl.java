package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCaptionElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableElementImpl extends HTMLElementImpl implements HTMLTableElement {
   private static final long serialVersionUID = 3977585787963651891L;
   private HTMLCollectionImpl _rows;
   private HTMLCollectionImpl _bodies;

   public synchronized HTMLTableCaptionElement getCaption() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof HTMLTableCaptionElement && var1.getNodeName().equals("CAPTION")) {
            return (HTMLTableCaptionElement)var1;
         }
      }

      return null;
   }

   public synchronized void setCaption(HTMLTableCaptionElement var1) {
      if (var1 != null && !var1.getTagName().equals("CAPTION")) {
         throw new IllegalArgumentException("HTM016 Argument 'caption' is not an element of type <CAPTION>.");
      } else {
         this.deleteCaption();
         if (var1 != null) {
            this.appendChild(var1);
         }

      }
   }

   public synchronized HTMLElement createCaption() {
      HTMLTableCaptionElement var1 = this.getCaption();
      if (var1 != null) {
         return var1;
      } else {
         HTMLTableCaptionElementImpl var2 = new HTMLTableCaptionElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "CAPTION");
         this.appendChild(var2);
         return var2;
      }
   }

   public synchronized void deleteCaption() {
      HTMLTableCaptionElement var1 = this.getCaption();
      if (var1 != null) {
         this.removeChild(var1);
      }

   }

   public synchronized HTMLTableSectionElement getTHead() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof HTMLTableSectionElement && var1.getNodeName().equals("THEAD")) {
            return (HTMLTableSectionElement)var1;
         }
      }

      return null;
   }

   public synchronized void setTHead(HTMLTableSectionElement var1) {
      if (var1 != null && !var1.getTagName().equals("THEAD")) {
         throw new IllegalArgumentException("HTM017 Argument 'tHead' is not an element of type <THEAD>.");
      } else {
         this.deleteTHead();
         if (var1 != null) {
            this.appendChild(var1);
         }

      }
   }

   public synchronized HTMLElement createTHead() {
      HTMLTableSectionElement var1 = this.getTHead();
      if (var1 != null) {
         return var1;
      } else {
         HTMLTableSectionElementImpl var2 = new HTMLTableSectionElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "THEAD");
         this.appendChild(var2);
         return var2;
      }
   }

   public synchronized void deleteTHead() {
      HTMLTableSectionElement var1 = this.getTHead();
      if (var1 != null) {
         this.removeChild(var1);
      }

   }

   public synchronized HTMLTableSectionElement getTFoot() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof HTMLTableSectionElement && var1.getNodeName().equals("TFOOT")) {
            return (HTMLTableSectionElement)var1;
         }
      }

      return null;
   }

   public synchronized void setTFoot(HTMLTableSectionElement var1) {
      if (var1 != null && !var1.getTagName().equals("TFOOT")) {
         throw new IllegalArgumentException("HTM018 Argument 'tFoot' is not an element of type <TFOOT>.");
      } else {
         this.deleteTFoot();
         if (var1 != null) {
            this.appendChild(var1);
         }

      }
   }

   public synchronized HTMLElement createTFoot() {
      HTMLTableSectionElement var1 = this.getTFoot();
      if (var1 != null) {
         return var1;
      } else {
         HTMLTableSectionElementImpl var2 = new HTMLTableSectionElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "TFOOT");
         this.appendChild(var2);
         return var2;
      }
   }

   public synchronized void deleteTFoot() {
      HTMLTableSectionElement var1 = this.getTFoot();
      if (var1 != null) {
         this.removeChild(var1);
      }

   }

   public HTMLCollection getRows() {
      if (this._rows == null) {
         this._rows = new HTMLCollectionImpl(this, (short)7);
      }

      return this._rows;
   }

   public HTMLCollection getTBodies() {
      if (this._bodies == null) {
         this._bodies = new HTMLCollectionImpl(this, (short)-2);
      }

      return this._bodies;
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

   public String getBorder() {
      return this.getAttribute("border");
   }

   public void setBorder(String var1) {
      this.setAttribute("border", var1);
   }

   public String getCellPadding() {
      return this.getAttribute("cellpadding");
   }

   public void setCellPadding(String var1) {
      this.setAttribute("cellpadding", var1);
   }

   public String getCellSpacing() {
      return this.getAttribute("cellspacing");
   }

   public void setCellSpacing(String var1) {
      this.setAttribute("cellspacing", var1);
   }

   public String getFrame() {
      return this.capitalize(this.getAttribute("frame"));
   }

   public void setFrame(String var1) {
      this.setAttribute("frame", var1);
   }

   public String getRules() {
      return this.capitalize(this.getAttribute("rules"));
   }

   public void setRules(String var1) {
      this.setAttribute("rules", var1);
   }

   public String getSummary() {
      return this.getAttribute("summary");
   }

   public void setSummary(String var1) {
      this.setAttribute("summary", var1);
   }

   public String getWidth() {
      return this.getAttribute("width");
   }

   public void setWidth(String var1) {
      this.setAttribute("width", var1);
   }

   public HTMLElement insertRow(int var1) {
      HTMLTableRowElementImpl var2 = new HTMLTableRowElementImpl((HTMLDocumentImpl)this.getOwnerDocument(), "TR");
      this.insertRowX(var1, var2);
      return var2;
   }

   void insertRowX(int var1, HTMLTableRowElementImpl var2) {
      Node var4 = null;

      for(Node var3 = this.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof HTMLTableRowElement) {
            if (var1 == 0) {
               this.insertBefore(var2, var3);
               return;
            }
         } else if (var3 instanceof HTMLTableSectionElementImpl) {
            var4 = var3;
            var1 = ((HTMLTableSectionElementImpl)var3).insertRowX(var1, var2);
            if (var1 < 0) {
               return;
            }
         }
      }

      if (var4 != null) {
         var4.appendChild(var2);
      } else {
         this.appendChild(var2);
      }

   }

   public synchronized void deleteRow(int var1) {
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2 instanceof HTMLTableRowElement) {
            if (var1 == 0) {
               this.removeChild(var2);
               return;
            }

            --var1;
         } else if (var2 instanceof HTMLTableSectionElementImpl) {
            var1 = ((HTMLTableSectionElementImpl)var2).deleteRowX(var1);
            if (var1 < 0) {
               return;
            }
         }
      }

   }

   public Node cloneNode(boolean var1) {
      HTMLTableElementImpl var2 = (HTMLTableElementImpl)super.cloneNode(var1);
      var2._rows = null;
      var2._bodies = null;
      return var2;
   }

   public HTMLTableElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
