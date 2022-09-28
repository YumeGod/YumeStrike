package org.apache.batik.css.parser;

public class DefaultBeginHyphenAttributeCondition extends DefaultAttributeCondition {
   public DefaultBeginHyphenAttributeCondition(String var1, String var2, boolean var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public short getConditionType() {
      return 8;
   }

   public String toString() {
      return "[" + this.getLocalName() + "|=\"" + this.getValue() + "\"]";
   }
}
