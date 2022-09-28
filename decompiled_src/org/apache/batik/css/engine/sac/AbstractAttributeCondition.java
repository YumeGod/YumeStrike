package org.apache.batik.css.engine.sac;

import org.w3c.css.sac.AttributeCondition;

public abstract class AbstractAttributeCondition implements AttributeCondition, ExtendedCondition {
   protected String value;

   protected AbstractAttributeCondition(String var1) {
      this.value = var1;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         AbstractAttributeCondition var2 = (AbstractAttributeCondition)var1;
         return var2.value.equals(this.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value == null ? -1 : this.value.hashCode();
   }

   public int getSpecificity() {
      return 256;
   }

   public String getValue() {
      return this.value;
   }
}
