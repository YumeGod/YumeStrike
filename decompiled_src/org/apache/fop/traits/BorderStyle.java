package org.apache.fop.traits;

import java.io.ObjectStreamException;

public final class BorderStyle extends TraitEnum {
   private static final long serialVersionUID = 1L;
   private static final String[] BORDER_STYLE_NAMES = new String[]{"none", "hidden", "dotted", "dashed", "solid", "double", "groove", "ridge", "inset", "outset"};
   private static final int[] BORDER_STYLE_VALUES = new int[]{95, 57, 36, 31, 133, 37, 55, 119, 67, 101};
   public static final BorderStyle NONE = new BorderStyle(0);
   public static final BorderStyle HIDDEN = new BorderStyle(1);
   public static final BorderStyle DOTTED = new BorderStyle(2);
   public static final BorderStyle DASHED = new BorderStyle(3);
   public static final BorderStyle SOLID = new BorderStyle(4);
   public static final BorderStyle DOUBLE = new BorderStyle(5);
   public static final BorderStyle GROOVE = new BorderStyle(6);
   public static final BorderStyle RIDGE = new BorderStyle(7);
   public static final BorderStyle INSET = new BorderStyle(8);
   public static final BorderStyle OUTSET = new BorderStyle(9);
   private static final BorderStyle[] STYLES;

   private BorderStyle(int index) {
      super(BORDER_STYLE_NAMES[index], BORDER_STYLE_VALUES[index]);
   }

   public static BorderStyle valueOf(String name) {
      for(int i = 0; i < STYLES.length; ++i) {
         if (STYLES[i].getName().equalsIgnoreCase(name)) {
            return STYLES[i];
         }
      }

      throw new IllegalArgumentException("Illegal border style: " + name);
   }

   public static BorderStyle valueOf(int enumValue) {
      for(int i = 0; i < STYLES.length; ++i) {
         if (STYLES[i].getEnumValue() == enumValue) {
            return STYLES[i];
         }
      }

      throw new IllegalArgumentException("Illegal border style: " + enumValue);
   }

   private Object readResolve() throws ObjectStreamException {
      return valueOf(this.getName());
   }

   public String toString() {
      return "BorderStyle:" + this.getName();
   }

   static {
      STYLES = new BorderStyle[]{NONE, HIDDEN, DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE, INSET, OUTSET};
   }
}
