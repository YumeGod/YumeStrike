package org.apache.batik.ext.awt.g2d;

public class TransformType {
   public static final int TRANSFORM_TRANSLATE = 0;
   public static final int TRANSFORM_ROTATE = 1;
   public static final int TRANSFORM_SCALE = 2;
   public static final int TRANSFORM_SHEAR = 3;
   public static final int TRANSFORM_GENERAL = 4;
   public static final String TRANSLATE_STRING = "translate";
   public static final String ROTATE_STRING = "rotate";
   public static final String SCALE_STRING = "scale";
   public static final String SHEAR_STRING = "shear";
   public static final String GENERAL_STRING = "general";
   public static final TransformType TRANSLATE = new TransformType(0, "translate");
   public static final TransformType ROTATE = new TransformType(1, "rotate");
   public static final TransformType SCALE = new TransformType(2, "scale");
   public static final TransformType SHEAR = new TransformType(3, "shear");
   public static final TransformType GENERAL = new TransformType(4, "general");
   private String desc;
   private int val;

   private TransformType(int var1, String var2) {
      this.desc = var2;
      this.val = var1;
   }

   public String toString() {
      return this.desc;
   }

   public int toInt() {
      return this.val;
   }

   public Object readResolve() {
      switch (this.val) {
         case 0:
            return TRANSLATE;
         case 1:
            return ROTATE;
         case 2:
            return SCALE;
         case 3:
            return SHEAR;
         case 4:
            return GENERAL;
         default:
            throw new Error("Unknown TransformType value:" + this.val);
      }
   }
}
