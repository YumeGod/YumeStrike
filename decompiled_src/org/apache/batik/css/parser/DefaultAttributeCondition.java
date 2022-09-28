package org.apache.batik.css.parser;

public class DefaultAttributeCondition extends AbstractAttributeCondition {
   protected String localName;
   protected String namespaceURI;
   protected boolean specified;

   public DefaultAttributeCondition(String var1, String var2, boolean var3, String var4) {
      super(var4);
      this.localName = var1;
      this.namespaceURI = var2;
      this.specified = var3;
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

   public String toString() {
      return this.value == null ? "[" + this.localName + "]" : "[" + this.localName + "=\"" + this.value + "\"]";
   }
}
