package org.apache.wml.dom;

import org.apache.wml.WMLCardElement;

public class WMLCardElementImpl extends WMLElementImpl implements WMLCardElement {
   private static final long serialVersionUID = 3257005466683781686L;

   public WMLCardElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setOnTimer(String var1) {
      this.setAttribute("ontimer", var1);
   }

   public String getOnTimer() {
      return this.getAttribute("ontimer");
   }

   public void setOrdered(boolean var1) {
      this.setAttribute("ordered", var1);
   }

   public boolean getOrdered() {
      return this.getAttribute("ordered", true);
   }

   public void setOnEnterBackward(String var1) {
      this.setAttribute("onenterbackward", var1);
   }

   public String getOnEnterBackward() {
      return this.getAttribute("onenterbackward");
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

   public void setNewContext(boolean var1) {
      this.setAttribute("newcontext", var1);
   }

   public boolean getNewContext() {
      return this.getAttribute("newcontext", false);
   }

   public void setId(String var1) {
      this.setAttribute("id", var1);
   }

   public String getId() {
      return this.getAttribute("id");
   }

   public void setOnEnterForward(String var1) {
      this.setAttribute("onenterforward", var1);
   }

   public String getOnEnterForward() {
      return this.getAttribute("onenterforward");
   }
}
