package org.apache.fop.fonts;

public class FontType {
   public static final FontType OTHER = new FontType("Other", 0);
   public static final FontType TYPE0 = new FontType("Type0", 1);
   public static final FontType TYPE1 = new FontType("Type1", 2);
   public static final FontType MMTYPE1 = new FontType("MMType1", 3);
   public static final FontType TYPE3 = new FontType("Type3", 4);
   public static final FontType TRUETYPE = new FontType("TrueType", 5);
   private final String name;
   private final int value;

   protected FontType(String name, int value) {
      this.name = name;
      this.value = value;
   }

   public static FontType byName(String name) {
      if (name.equalsIgnoreCase(OTHER.getName())) {
         return OTHER;
      } else if (name.equalsIgnoreCase(TYPE0.getName())) {
         return TYPE0;
      } else if (name.equalsIgnoreCase(TYPE1.getName())) {
         return TYPE1;
      } else if (name.equalsIgnoreCase(MMTYPE1.getName())) {
         return MMTYPE1;
      } else if (name.equalsIgnoreCase(TYPE3.getName())) {
         return TYPE3;
      } else if (name.equalsIgnoreCase(TRUETYPE.getName())) {
         return TRUETYPE;
      } else {
         throw new IllegalArgumentException("Invalid font type: " + name);
      }
   }

   public static FontType byValue(int value) {
      if (value == OTHER.getValue()) {
         return OTHER;
      } else if (value == TYPE0.getValue()) {
         return TYPE0;
      } else if (value == TYPE1.getValue()) {
         return TYPE1;
      } else if (value == MMTYPE1.getValue()) {
         return MMTYPE1;
      } else if (value == TYPE3.getValue()) {
         return TYPE3;
      } else if (value == TRUETYPE.getValue()) {
         return TRUETYPE;
      } else {
         throw new IllegalArgumentException("Invalid font type: " + value);
      }
   }

   public String getName() {
      return this.name;
   }

   public int getValue() {
      return this.value;
   }
}
