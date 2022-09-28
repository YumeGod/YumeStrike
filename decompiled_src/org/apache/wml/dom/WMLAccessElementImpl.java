package org.apache.wml.dom;

import org.apache.wml.WMLAccessElement;

public class WMLAccessElementImpl extends WMLElementImpl implements WMLAccessElement {
   private static final long serialVersionUID = 3256721784177833777L;

   public WMLAccessElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setClassName(String var1) {
      this.setAttribute("class", var1);
   }

   public String getClassName() {
      return this.getAttribute("class");
   }

   public void setDomain(String var1) {
      this.setAttribute("domain", var1);
   }

   public String getDomain() {
      return this.getAttribute("domain");
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setPath(String var1) {
      this.setAttribute("path", var1);
   }

   public String getPath() {
      return this.getAttribute("path");
   }
}
