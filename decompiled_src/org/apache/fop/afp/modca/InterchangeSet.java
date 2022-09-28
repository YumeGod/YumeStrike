package org.apache.fop.afp.modca;

public class InterchangeSet {
   public static final String MODCA_PRESENTATION_INTERCHANGE_SET_1 = "MO:DCA-P IS/1";
   public static final String MODCA_PRESENTATION_INTERCHANGE_SET_2 = "MO:DCA-P IS/2";
   public static final String MODCA_RESOURCE_INTERCHANGE_SET = "MO:DCA-L";
   private static final String[] NAMES = new String[]{"MO:DCA-P IS/1", "MO:DCA-P IS/2", "MO:DCA-L"};
   private static final int SET_1 = 0;
   private static final int SET_2 = 1;
   private static final int RESOURCE_SET = 2;
   private int value;

   public static InterchangeSet valueOf(String str) {
      if ("MO:DCA-P IS/1".equals(str)) {
         return new InterchangeSet(0);
      } else if ("MO:DCA-P IS/2".equals(str)) {
         return new InterchangeSet(1);
      } else if ("MO:DCA-L".equals(str)) {
         return new InterchangeSet(2);
      } else {
         throw new IllegalArgumentException("Invalid MO:DCA interchange set :" + str);
      }
   }

   public InterchangeSet(int value) {
      this.value = value;
   }

   protected boolean is1() {
      return this.value == 0;
   }

   public boolean is2() {
      return this.value == 1;
   }

   public boolean isResource() {
      return this.value == 2;
   }

   public String toString() {
      return NAMES[this.value];
   }

   public boolean supportsLevel2() {
      return this.is2() || this.isResource();
   }
}
