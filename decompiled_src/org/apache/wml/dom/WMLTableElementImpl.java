package org.apache.wml.dom;

import org.apache.wml.WMLTableElement;

public class WMLTableElementImpl extends WMLElementImpl implements WMLTableElement {
   private static final long serialVersionUID = 3978428026738194488L;

   public WMLTableElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setColumns(int var1) {
      this.setAttribute("columns", var1);
   }

   public int getColumns() {
      return this.getAttribute("columns", 0);
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setXmlLang(String var1) {
      this.setAttribute("xml:lang", var1);
   }

   public String getXmlLang() {
      return this.getAttribute("xml:lang");
   }

   public void setAlign(String var1) {
      this.setAttribute("align", var1);
   }

   public String getAlign() {
      return this.getAttribute("align");
   }

   public void setTitle(String var1) {
      this.setAttribute("title", var1);
   }

   public String getTitle() {
      return this.getAttribute("title");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }
}
