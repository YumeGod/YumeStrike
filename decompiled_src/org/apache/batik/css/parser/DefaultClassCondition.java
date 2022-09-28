package org.apache.batik.css.parser;

public class DefaultClassCondition extends DefaultAttributeCondition {
   public DefaultClassCondition(String var1, String var2) {
      super("class", var1, true, var2);
   }

   public short getConditionType() {
      return 9;
   }

   public String toString() {
      return "." + this.getValue();
   }
}
