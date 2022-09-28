package org.apache.wml.dom;

import org.apache.wml.WMLAElement;

public class WMLAElementImpl extends WMLElementImpl implements WMLAElement {
   private static final long serialVersionUID = 3618134567563966776L;

   public WMLAElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setHref(String var1) {
      this.setAttribute("href", var1);
   }

   public String getHref() {
      return this.getAttribute("href");
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
