package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLTitleElement;

public class HTMLTitleElementImpl extends HTMLElementImpl implements HTMLTitleElement {
   private static final long serialVersionUID = 4050769294810034992L;

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

   public HTMLTitleElementImpl(HTMLDocumentImpl var1, String var2) {
      super(var1, var2);
   }
}
