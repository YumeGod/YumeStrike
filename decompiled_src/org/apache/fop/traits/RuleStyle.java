package org.apache.fop.traits;

import java.io.ObjectStreamException;

public final class RuleStyle extends TraitEnum {
   private static final long serialVersionUID = 1L;
   private static final String[] RULE_STYLE_NAMES = new String[]{"none", "dotted", "dashed", "solid", "double", "groove", "ridge"};
   private static final int[] RULE_STYLE_VALUES = new int[]{95, 36, 31, 133, 37, 55, 119};
   public static final RuleStyle NONE = new RuleStyle(0);
   public static final RuleStyle DOTTED = new RuleStyle(1);
   public static final RuleStyle DASHED = new RuleStyle(2);
   public static final RuleStyle SOLID = new RuleStyle(3);
   public static final RuleStyle DOUBLE = new RuleStyle(4);
   public static final RuleStyle GROOVE = new RuleStyle(5);
   public static final RuleStyle RIDGE = new RuleStyle(6);
   private static final RuleStyle[] STYLES;

   private RuleStyle(int index) {
      super(RULE_STYLE_NAMES[index], RULE_STYLE_VALUES[index]);
   }

   public static RuleStyle valueOf(String name) {
      for(int i = 0; i < STYLES.length; ++i) {
         if (STYLES[i].getName().equalsIgnoreCase(name)) {
            return STYLES[i];
         }
      }

      throw new IllegalArgumentException("Illegal rule style: " + name);
   }

   public static RuleStyle valueOf(int enumValue) {
      for(int i = 0; i < STYLES.length; ++i) {
         if (STYLES[i].getEnumValue() == enumValue) {
            return STYLES[i];
         }
      }

      throw new IllegalArgumentException("Illegal rule style: " + enumValue);
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return "RuleStyle:" + this.getName();
   }

   static {
      STYLES = new RuleStyle[]{NONE, DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE};
   }
}
