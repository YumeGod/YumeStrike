package org.apache.wml.dom;

import org.apache.wml.WMLGoElement;

public class WMLGoElementImpl extends WMLElementImpl implements WMLGoElement {
   private static final long serialVersionUID = 3256718485575841072L;

   public WMLGoElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setSendreferer(String var1) {
      this.setAttribute("sendreferer", var1);
   }

   public String getSendreferer() {
      return this.getAttribute("sendreferer");
   }

   public void setAcceptCharset(String var1) {
      this.setAttribute("accept-charset", var1);
   }

   public String getAcceptCharset() {
      return this.getAttribute("accept-charset");
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

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setMethod(String var1) {
      this.setAttribute("method", var1);
   }

   public String getMethod() {
      return this.getAttribute("method");
   }
}
