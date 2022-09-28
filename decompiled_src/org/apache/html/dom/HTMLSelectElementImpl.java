package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

public class HTMLSelectElementImpl extends HTMLElementImpl implements HTMLSelectElement, HTMLFormControl {
   private static final long serialVersionUID = 3256722883588665912L;
   private HTMLCollection _options;

   public String getType() {
      return this.getAttribute("type");
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public int getSelectedIndex() {
      NodeList var1 = this.getElementsByTagName("OPTION");

      for(int var2 = 0; var2 < var1.getLength(); ++var2) {
         if (((HTMLOptionElement)var1.item(var2)).getSelected()) {
            return var2;
         }
      }

      return -1;
   }

   public void setSelectedIndex(int var1) {
      NodeList var2 = this.getElementsByTagName("OPTION");

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         ((HTMLOptionElementImpl)var2.item(var3)).setSelected(var3 == var1);
      }

   }

   public HTMLCollection getOptions() {
      if (this._options == null) {
         this._options = new HTMLCollectionImpl(this, (short)6);
      }

      return this._options;
   }

   public int getLength() {
      return this.getOptions().getLength();
   }

   public boolean getDisabled() {
      return this.getBinary("disabled");
   }

   public void setDisabled(boolean var1) {
      this.setAttribute("disabled", var1);
   }

   public boolean getMultiple() {
      return this.getBinary("multiple");
   }

   public void setMultiple(boolean var1) {
      this.setAttribute("multiple", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public int getSize() {
      return this.getInteger(this.getAttribute("size"));
   }

   public void setSize(int var1) {
      this.setAttribute("size", String.valueOf(var1));
   }

   public int getTabIndex() {
      return this.getInteger(this.getAttribute("tabindex"));
   }

   public void setTabIndex(int var1) {
      this.setAttribute("tabindex", String.valueOf(var1));
   }

   public void add(HTMLElement var1, HTMLElement var2) {
      this.insertBefore(var1, var2);
   }

   public void remove(int var1) {
      NodeList var2 = this.getElementsByTagName("OPTION");
      Node var3 = var2.item(var1);
      if (var3 != null) {
         var3.getParentNode().removeChild(var3);
      }

   }

   public void blur() {
   }

   public void focus() {
   }

   public NodeList getChildNodes() {
      return this.getChildNodesUnoptimized();
   }

   public Node cloneNode(boolean var1) {
      HTMLSelectElementImpl var2 = (HTMLSelectElementImpl)super.cloneNode(var1);
      var2._options = null;
      return var2;
   }

   public HTMLSelectElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
