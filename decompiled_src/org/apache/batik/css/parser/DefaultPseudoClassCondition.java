package org.apache.batik.css.parser;

public class DefaultPseudoClassCondition extends AbstractAttributeCondition {
   protected String namespaceURI;

   public DefaultPseudoClassCondition(String var1, String var2) {
      super(var2);
      this.namespaceURI = var1;
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

   public String toString() {
      return ":" + this.getValue();
   }
}
