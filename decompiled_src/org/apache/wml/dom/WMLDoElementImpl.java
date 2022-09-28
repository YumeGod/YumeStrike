package org.apache.wml.dom;

import org.apache.wml.WMLDoElement;

public class WMLDoElementImpl extends WMLElementImpl implements WMLDoElement {
   private static final long serialVersionUID = 3978707298497737012L;

   public WMLDoElementImpl(WMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setOptional(String var1) {
      this.setAttribute("optional", var1);
   }

   public String getOptional() {
      return this.getAttribute("optional");
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

   public void setLabel(String var1) {
      this.setAttribute("label", var1);
   }

   public String getLabel() {
      return this.getAttribute("label");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setName(String var1) {
      this.setAttribute("name", var1);
   }

   public String getName() {
      return this.getAttribute("name");
   }
}
