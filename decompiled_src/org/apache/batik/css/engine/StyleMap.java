package org.apache.batik.css.engine;

import org.apache.batik.css.engine.value.Value;

public class StyleMap {
   public static final short IMPORTANT_MASK = 1;
   public static final short COMPUTED_MASK = 2;
   public static final short NULL_CASCADED_MASK = 4;
   public static final short INHERITED_MASK = 8;
   public static final short LINE_HEIGHT_RELATIVE_MASK = 16;
   public static final short FONT_SIZE_RELATIVE_MASK = 32;
   public static final short COLOR_RELATIVE_MASK = 64;
   public static final short PARENT_RELATIVE_MASK = 128;
   public static final short BLOCK_WIDTH_RELATIVE_MASK = 256;
   public static final short BLOCK_HEIGHT_RELATIVE_MASK = 512;
   public static final short BOX_RELATIVE_MASK = 1024;
   public static final short ORIGIN_MASK = -8192;
   public static final short USER_AGENT_ORIGIN = 0;
   public static final short USER_ORIGIN = 8192;
   public static final short NON_CSS_ORIGIN = 16384;
   public static final short AUTHOR_ORIGIN = 24576;
   public static final short INLINE_AUTHOR_ORIGIN = Short.MIN_VALUE;
   public static final short OVERRIDE_ORIGIN = -24576;
   protected Value[] values;
   protected short[] masks;
   protected boolean fixedCascadedValues;

   public StyleMap(int var1) {
      this.values = new Value[var1];
      this.masks = new short[var1];
   }

   public boolean hasFixedCascadedValues() {
      return this.fixedCascadedValues;
   }

   public void setFixedCascadedStyle(boolean var1) {
      this.fixedCascadedValues = var1;
   }

   public Value getValue(int var1) {
      return this.values[var1];
   }

   public short getMask(int var1) {
      return this.masks[var1];
   }

   public boolean isImportant(int var1) {
      return (this.masks[var1] & 1) != 0;
   }

   public boolean isComputed(int var1) {
      return (this.masks[var1] & 2) != 0;
   }

   public boolean isNullCascaded(int var1) {
      return (this.masks[var1] & 4) != 0;
   }

   public boolean isInherited(int var1) {
      return (this.masks[var1] & 8) != 0;
   }

   public short getOrigin(int var1) {
      return (short)(this.masks[var1] & -8192);
   }

   public boolean isColorRelative(int var1) {
      return (this.masks[var1] & 64) != 0;
   }

   public boolean isParentRelative(int var1) {
      return (this.masks[var1] & 128) != 0;
   }

   public boolean isLineHeightRelative(int var1) {
      return (this.masks[var1] & 16) != 0;
   }

   public boolean isFontSizeRelative(int var1) {
      return (this.masks[var1] & 32) != 0;
   }

   public boolean isBlockWidthRelative(int var1) {
      return (this.masks[var1] & 256) != 0;
   }

   public boolean isBlockHeightRelative(int var1) {
      return (this.masks[var1] & 512) != 0;
   }

   public void putValue(int var1, Value var2) {
      this.values[var1] = var2;
   }

   public void putMask(int var1, short var2) {
      this.masks[var1] = var2;
   }

   public void putImportant(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 1);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -2);
      }

   }

   public void putOrigin(int var1, short var2) {
      short[] var10000 = this.masks;
      var10000[var1] = (short)(var10000[var1] & 8191);
      var10000 = this.masks;
      var10000[var1] |= (short)(var2 & -8192);
   }

   public void putComputed(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 2);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -3);
      }

   }

   public void putNullCascaded(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 4);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -5);
      }

   }

   public void putInherited(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 8);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -9);
      }

   }

   public void putColorRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 64);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -65);
      }

   }

   public void putParentRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 128);
      } else {
         var10000 = this.masks;
         var10000[var1] &= -129;
      }

   }

   public void putLineHeightRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 16);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -17);
      }

   }

   public void putFontSizeRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 32);
      } else {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] & -33);
      }

   }

   public void putBlockWidthRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 256);
      } else {
         var10000 = this.masks;
         var10000[var1] &= -257;
      }

   }

   public void putBlockHeightRelative(int var1, boolean var2) {
      short[] var10000;
      if (var2) {
         var10000 = this.masks;
         var10000[var1] = (short)(var10000[var1] | 512);
      } else {
         var10000 = this.masks;
         var10000[var1] &= -513;
      }

   }

   public String toString(CSSEngine var1) {
      int var2 = this.values.length;
      StringBuffer var3 = new StringBuffer(var2 * 8);

      for(int var4 = 0; var4 < var2; ++var4) {
         Value var5 = this.values[var4];
         if (var5 != null) {
            var3.append(var1.getPropertyName(var4));
            var3.append(": ");
            var3.append(var5);
            if (this.isImportant(var4)) {
               var3.append(" !important");
            }

            var3.append(";\n");
         }
      }

      return var3.toString();
   }
}
