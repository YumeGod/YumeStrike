package org.apache.fop.layoutmgr.table;

import org.apache.fop.fo.flow.table.BorderSpecification;

public abstract class CollapsingBorderModel {
   protected static final int BEFORE = 0;
   protected static final int AFTER = 1;
   protected static final int START = 2;
   protected static final int END = 3;
   private static CollapsingBorderModel collapse = null;
   private static CollapsingBorderModel collapseWithPrecedence = null;

   public static CollapsingBorderModel getBorderModelFor(int borderCollapse) {
      switch (borderCollapse) {
         case 26:
            if (collapse == null) {
               collapse = new CollapsingBorderModelEyeCatching();
            }

            return collapse;
         case 27:
            if (collapseWithPrecedence == null) {
            }

            return collapseWithPrecedence;
         default:
            throw new IllegalArgumentException("Illegal border-collapse mode.");
      }
   }

   public static int getOtherSide(int side) {
      switch (side) {
         case 0:
            return 1;
         case 1:
            return 0;
         case 2:
            return 3;
         case 3:
            return 2;
         default:
            throw new IllegalArgumentException("Illegal parameter: side");
      }
   }

   protected boolean isVerticalRelation(int side) {
      return side == 0 || side == 1;
   }

   private static int compareInt(int value1, int value2) {
      if (value1 < value2) {
         return -1;
      } else {
         return value1 == value2 ? 0 : 1;
      }
   }

   private static int getStylePreferenceValue(int style) {
      switch (style) {
         case 31:
            return -2;
         case 36:
            return -3;
         case 37:
            return 0;
         case 55:
            return -6;
         case 67:
            return -7;
         case 101:
            return -5;
         case 119:
            return -4;
         case 133:
            return -1;
         default:
            throw new IllegalStateException("Illegal border style: " + style);
      }
   }

   static int compareStyles(int style1, int style2) {
      int value1 = getStylePreferenceValue(style1);
      int value2 = getStylePreferenceValue(style2);
      return compareInt(value1, value2);
   }

   private static int getHolderPreferenceValue(int id) {
      switch (id) {
         case 71:
            return -4;
         case 72:
         case 74:
         default:
            throw new IllegalStateException();
         case 73:
         case 77:
         case 78:
            return -2;
         case 75:
            return 0;
         case 76:
            return -3;
         case 79:
            return -1;
      }
   }

   static int compareFOs(int id1, int id2) {
      int p1 = getHolderPreferenceValue(id1);
      int p2 = getHolderPreferenceValue(id2);
      return compareInt(p1, p2);
   }

   public abstract BorderSpecification determineWinner(BorderSpecification var1, BorderSpecification var2, boolean var3);

   public abstract BorderSpecification determineWinner(BorderSpecification var1, BorderSpecification var2);
}
