package org.apache.wml.dom;

import org.apache.wml.WMLInputElement;

public class WMLInputElementImpl extends WMLElementImpl implements WMLInputElement {
   private static final long serialVersionUID = 3618705209837041975L;

   public WMLInputElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setSize(int var1) {
      this.setAttribute("size", var1);
   }

   public int getSize() {
      return this.getAttribute("size", 0);
   }

   public void setFormat(String var1) {
      this.setAttribute("format", var1);
   }

   public String getFormat() {
      return this.getAttribute("format");
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
   }

   public void setMaxLength(int var1) {
      this.setAttribute("maxlength", var1);
   }

   public int getMaxLength() {
      return this.getAttribute("maxlength", 0);
   }

   public void setTabIndex(int var1) {
      this.setAttribute("tabindex", var1);
   }

   public int getTabIndex() {
      return this.getAttribute("tabindex", 0);
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

   public void setEmptyOk(boolean var1) {
      this.setAttribute("emptyok", var1);
   }

   public boolean getEmptyOk() {
      return this.getAttribute("emptyok", false);
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

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }
}
