package org.apache.batik.css.parser;

import org.w3c.css.sac.AttributeCondition;

public abstract class AbstractAttributeCondition implements AttributeCondition {
   protected String value;

   protected AbstractAttributeCondition(String var1) {
      this.value = var1;
   }

   public String getValue() {
      return this.value;
   }
}
