package org.apache.wml.dom;

import org.apache.wml.WMLSmallElement;

public class WMLSmallElementImpl extends WMLElementImpl implements WMLSmallElement {
   private static final long serialVersionUID = 3618137848969506873L;

   public WMLSmallElementImpl(WMLDocumentImpl var1, String var2) {
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

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }
}
