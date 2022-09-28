package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.dom.Element;

public class CSSAttributeCondition extends AbstractAttributeCondition {
   protected String localName;
   protected String namespaceURI;
   protected boolean specified;

   public CSSAttributeCondition(String var1, String var2, boolean var3, String var4) {
      super(var4);
      this.localName = var1;
      this.namespaceURI = var2;
      this.specified = var3;
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         CSSAttributeCondition var2 = (CSSAttributeCondition)var1;
         return var2.namespaceURI.equals(this.namespaceURI) && var2.localName.equals(this.localName) && var2.specified == this.specified;
      }
   }

   public int hashCode() {
      return this.namespaceURI.hashCode() ^ this.localName.hashCode() ^ (this.specified ? -1 : 0);
   }

   public short getConditionType() {
      return 4;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }

   public boolean getSpecified() {
      return this.specified;
   }

   public boolean match(Element var1, String var2) {
      String var3 = this.getValue();
      if (var3 == null) {
         return !var1.getAttribute(this.getLocalName()).equals("");
      } else {
         return var1.getAttribute(this.getLocalName()).equals(var3);
      }
   }

   public void fillAttributeSet(Set var1) {
      var1.add(this.localName);
   }

   public String toString() {
      return this.value == null ? '[' + this.localName + ']' : '[' + this.localName + "=\"" + this.value + "\"]";
   }
}
