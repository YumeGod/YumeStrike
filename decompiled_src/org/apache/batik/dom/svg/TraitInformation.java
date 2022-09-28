package org.apache.batik.dom.svg;

public class TraitInformation {
   public static final short PERCENTAGE_FONT_SIZE = 0;
   public static final short PERCENTAGE_VIEWPORT_WIDTH = 1;
   public static final short PERCENTAGE_VIEWPORT_HEIGHT = 2;
   public static final short PERCENTAGE_VIEWPORT_SIZE = 3;
   protected boolean isAnimatable;
   protected int type;
   protected short percentageInterpretation;

   public TraitInformation(boolean var1, int var2, short var3) {
      this.isAnimatable = var1;
      this.type = var2;
      this.percentageInterpretation = var3;
   }

   public TraitInformation(boolean var1, int var2) {
      this.isAnimatable = var1;
      this.type = var2;
      this.percentageInterpretation = -1;
   }

   public boolean isAnimatable() {
      return this.isAnimatable;
   }

   public int getType() {
      return this.type;
   }

   public short getPercentageInterpretation() {
      return this.percentageInterpretation;
   }
}
