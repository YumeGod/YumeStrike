package org.apache.wml.dom;

import org.apache.wml.WMLMetaElement;

public class WMLMetaElementImpl extends WMLElementImpl implements WMLMetaElement {
   private static final long serialVersionUID = 3256726199320589875L;

   public WMLMetaElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setForua(boolean var1) {
      this.setAttribute("forua", var1);
   }

   public boolean getForua() {
      return this.getAttribute("forua", false);
   }

   public void setScheme(String var1) {
      this.setAttribute("scheme", var1);
   }

   public String getScheme() {
      return this.getAttribute("scheme");
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setHttpEquiv(String var1) {
      this.setAttribute("http-equiv", var1);
   }

   public String getHttpEquiv() {
      return this.getAttribute("http-equiv");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setContent(String var1) {
      this.setAttribute("content", var1);
   }

   public String getContent() {
      return this.getAttribute("content");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }
}
