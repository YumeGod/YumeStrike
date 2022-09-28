package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.Element;

public class CSSPseudoClassCondition extends AbstractAttributeCondition {
   protected String namespaceURI;

   public CSSPseudoClassCondition(String var1, String var2) {
      super(var2);
      this.namespaceURI = var1;
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         CSSPseudoClassCondition var2 = (CSSPseudoClassCondition)var1;
         return var2.namespaceURI.equals(this.namespaceURI);
      }
   }

   public int hashCode() {
      return this.namespaceURI.hashCode();
   }

   public short getConditionType() {
      return 10;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return null;
   }

   public boolean getSpecified() {
      return false;
   }

   public boolean match(Element var1, String var2) {
      return var1 instanceof CSSStylableElement ? ((CSSStylableElement)var1).isPseudoInstanceOf(this.getValue()) : false;
   }

   public void fillAttributeSet(Set var1) {
   }

   public String toString() {
      return ":" + this.getValue();
   }
}
