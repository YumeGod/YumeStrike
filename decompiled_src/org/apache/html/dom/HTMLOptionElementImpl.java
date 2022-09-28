package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement {
   private static final long serialVersionUID = 3257285846528112436L;

   public boolean getDefaultSelected() {
      return this.getBinary("default-selected");
   }

   public void setDefaultSelected(boolean var1) {
      this.setAttribute("default-selected", var1);
   }

   public String getText() {
      StringBuffer var2 = new StringBuffer();

      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof Text) {
            var2.append(((Text)var1).getData());
         }
      }

      return var2.toString();
   }

   public void setText(String var1) {
      Node var3;
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var3) {
         var3 = var2.getNextSibling();
         this.removeChild(var2);
      }

      this.insertBefore(this.getOwnerDocument().createTextNode(var1), this.getFirstChild());
   }

   public int getIndex() {
      Node var1;
      for(var1 = this.getParentNode(); var1 != null && !(var1 instanceof HTMLSelectElement); var1 = var1.getParentNode()) {
      }

      if (var1 != null) {
         NodeList var2 = ((HTMLElement)var1).getElementsByTagName("OPTION");

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            if (var2.item(var3) == this) {
               return var3;
            }
         }
      }

      return -1;
   }

   public void setIndex(int var1) {
      Node var2;
      for(var2 = this.getParentNode(); var2 != null && !(var2 instanceof HTMLSelectElement); var2 = var2.getParentNode()) {
      }

      if (var2 != null) {
         NodeList var3 = ((HTMLElement)var2).getElementsByTagName("OPTION");
         if (var3.item(var1) != this) {
            this.getParentNode().removeChild(this);
            Node var4 = var3.item(var1);
            var4.getParentNode().insertBefore(this, var4);
         }
      }

   }

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public String getLabel() {
      return this.capitalize(this.getAttribute("label"));
   }

   public void setLabel(String var1) {
      this.setAttribute("label", var1);
   }

   public boolean getSelected() {
      return this.getBinary("selected");
   }

   public void setSelected(boolean var1) {
      this.setAttribute("selected", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public HTMLOptionElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
