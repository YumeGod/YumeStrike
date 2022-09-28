package org.apache.wml.dom;

import org.apache.wml.WMLOptionElement;

public class WMLOptionElementImpl extends WMLElementImpl implements WMLOptionElement {
   private static final long serialVersionUID = 3257004350009129273L;

   public WMLOptionElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setValue(String var1) {
      this.setAttribute("value", var1);
   }

   public String getValue() {
      return this.getAttribute("value");
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

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setOnPick(String var1) {
      this.setAttribute("onpick", var1);
   }

   public String getOnPick() {
      return this.getAttribute("onpick");
   }
}
