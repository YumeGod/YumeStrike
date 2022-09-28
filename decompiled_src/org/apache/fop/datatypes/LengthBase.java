package org.apache.fop.datatypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class LengthBase implements PercentBase {
   public static final int CUSTOM_BASE = 0;
   public static final int FONTSIZE = 1;
   public static final int INH_FONTSIZE = 2;
   public static final int PARENT_AREA_WIDTH = 3;
   public static final int CONTAINING_REFAREA_WIDTH = 4;
   public static final int CONTAINING_BLOCK_WIDTH = 5;
   public static final int CONTAINING_BLOCK_HEIGHT = 6;
   public static final int IMAGE_INTRINSIC_WIDTH = 7;
   public static final int IMAGE_INTRINSIC_HEIGHT = 8;
   public static final int IMAGE_BACKGROUND_POSITION_HORIZONTAL = 9;
   public static final int IMAGE_BACKGROUND_POSITION_VERTICAL = 10;
   public static final int TABLE_UNITS = 11;
   public static final int ALIGNMENT_ADJUST = 12;
   protected static Log log;
   protected FObj fobj;
   private int baseType;
   private Length baseLength;

   public LengthBase(PropertyList plist, int baseType) throws PropertyException {
      this.fobj = plist.getFObj();
      this.baseType = baseType;
      switch (baseType) {
         case 1:
            this.baseLength = plist.get(103).getLength();
            break;
         case 2:
            this.baseLength = plist.getInherited(103).getLength();
      }

   }

   public int getDimension() {
      return 1;
   }

   public double getBaseValue() {
      return 1.0;
   }

   public int getBaseLength(PercentBaseContext context) throws PropertyException {
      int baseLen = 0;
      if (context != null) {
         if (this.baseType == 1 || this.baseType == 2) {
            return this.baseLength.getValue(context);
         }

         baseLen = context.getBaseLength(this.baseType, this.fobj);
      } else {
         log.error("getBaseLength called without context");
      }

      return baseLen;
   }

   public String toString() {
      return super.toString() + "[fo=" + this.fobj + "," + "baseType=" + this.baseType + "," + "baseLength=" + this.baseLength + "]";
   }

   public Length getBaseLength() {
      return this.baseLength;
   }

   static {
      log = LogFactory.getLog(LengthBase.class);
   }
}
