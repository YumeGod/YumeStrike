package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLScriptElement;

public class HTMLScriptElementImpl extends HTMLElementImpl implements HTMLScriptElement {
   private static final long serialVersionUID = 3832626162072498224L;

   public String getText() {
      StringBuffer var2 = new StringBuffer();

      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof Text) {
            var2.append(((Text)var1).getData());
         }
      }

      return var2.toString();
   }

   public void setText(String var1) {
      Node var3;
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var3) {
         var3 = var2.getNextSibling();
         this.removeChild(var2);
      }

      this.insertBefore(this.getOwnerDocument().createTextNode(var1), this.getFirstChild());
   }

   public String getHtmlFor() {
      return this.getAttribute("for");
   }

   public void setHtmlFor(String var1) {
      this.setAttribute("for", var1);
   }

   public String getEvent() {
      return this.getAttribute("event");
   }

   public void setEvent(String var1) {
      this.setAttribute("event", var1);
   }

   public String getCharset() {
      return this.getAttribute("charset");
   }

   public void setCharset(String var1) {
      this.setAttribute("charset", var1);
   }

   public boolean getDefer() {
      return this.getBinary("defer");
   }

   public void setDefer(boolean var1) {
      this.setAttribute("defer", var1);
   }

   public String getSrc() {
      return this.getAttribute("src");
   }

   public void setSrc(String var1) {
      this.setAttribute("src", var1);
   }

   public String getType() {
      return this.getAttribute("type");
   }

   public void setType(String var1) {
      this.setAttribute("type", var1);
   }

   public HTMLScriptElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
