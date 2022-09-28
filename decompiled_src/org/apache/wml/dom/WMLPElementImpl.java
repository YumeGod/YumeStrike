package org.apache.wml.dom;

import org.apache.wml.WMLPElement;

public class WMLPElementImpl extends WMLElementImpl implements WMLPElement {
   private static final long serialVersionUID = 3761128232321365815L;

   public WMLPElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setMode(String var1) {
      this.setAttribute("mode", var1);
   }

   public String getMode() {
      return this.getAttribute("mode");
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

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }
}
