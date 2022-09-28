package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.Element;

public class CSSIdCondition extends AbstractAttributeCondition {
   protected String namespaceURI;
   protected String localName;

   public CSSIdCondition(String var1, String var2, String var3) {
      super(var3);
      this.namespaceURI = var1;
      this.localName = var2;
   }

   public short getConditionType() {
      return 5;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }

   public boolean getSpecified() {
      return true;
   }

   public boolean match(Element var1, String var2) {
      return var1 instanceof CSSStylableElement ? ((CSSStylableElement)var1).getXMLId().equals(this.getValue()) : false;
   }

   public void fillAttributeSet(Set var1) {
      var1.add(this.localName);
   }

   public int getSpecificity() {
      return 65536;
   }

   public String toString() {
      return '#' + this.getValue();
   }
}
