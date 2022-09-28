package org.apache.wml.dom;

import org.apache.wml.WMLSelectElement;

public class WMLSelectElementImpl extends WMLElementImpl implements WMLSelectElement {
   private static final long serialVersionUID = 3905808595126661684L;

   public WMLSelectElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setMultiple(boolean var1) {
      this.setAttribute("multiple", var1);
   }

   public boolean getMultiple() {
      return this.getAttribute("multiple", false);
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
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

   public void setTitle(String var1) {
      this.setAttribute("title", var1);
   }

   public String getTitle() {
      return this.getAttribute("title");
   }

   public void setIValue(String var1) {
      this.setAttribute("ivalue", var1);
   }

   public String getIValue() {
      return this.getAttribute("ivalue");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setIName(String var1) {
      this.setAttribute("iname", var1);
   }

   public String getIName() {
      return this.getAttribute("iname");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }
}
