package org.apache.wml.dom;

import org.apache.wml.WMLFieldsetElement;

public class WMLFieldsetElementImpl extends WMLElementImpl implements WMLFieldsetElement {
   private static final long serialVersionUID = 3907211537721405752L;

   public WMLFieldsetElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
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
}
