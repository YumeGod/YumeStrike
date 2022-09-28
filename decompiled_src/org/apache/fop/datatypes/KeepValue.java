package org.apache.fop.datatypes;

public class KeepValue {
   public static final String KEEP_WITH_ALWAYS = "KEEP_WITH_ALWAYS";
   public static final String KEEP_WITH_AUTO = "KEEP_WITH_AUTO";
   public static final String KEEP_WITH_VALUE = "KEEP_WITH_VALUE";
   private String type = "KEEP_WITH_AUTO";
   private int value = 0;

   public KeepValue(String type, int val) {
      this.type = type;
      this.value = val;
   }

   public int getValue() {
      return this.value;
   }

   public String getType() {
      return this.type;
   }

   public String toString() {
      return this.type;
   }
}
