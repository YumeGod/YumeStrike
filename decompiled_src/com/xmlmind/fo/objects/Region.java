package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Value;

public class Region {
   public static final int NONE = -1;
   public static final int BODY = 0;
   public static final int BEFORE = 1;
   public static final int AFTER = 2;
   public static final int START = 3;
   public static final int END = 4;
   public Value[] properties;
   public String regionName;
   public Value marginTop;
   public Value marginBottom;
   public Value marginLeft;
   public Value marginRight;
   public Value paddingTop;
   public Value paddingBottom;
   public Value paddingLeft;
   public Value paddingRight;
   public int columnCount;
   public Value columnGap;
   public int referenceOrientation;
   public int writingMode;

   public Region(Value[] var1) {
      this.properties = var1;
      Value var2 = var1[227];
      if (var2.type == 1) {
         int var3 = var2.keyword();
         this.regionName = Keyword.list[var3].keyword;
      } else {
         this.regionName = var2.name();
      }

      this.marginTop = var1[176];
      this.marginBottom = var1[173];
      this.marginLeft = var1[174];
      this.marginRight = var1[175];
      this.paddingTop = var1[208];
      this.paddingBottom = var1[199];
      this.paddingLeft = var1[203];
      this.paddingRight = var1[204];
      this.columnCount = (int)Math.round(var1[80].number());
      if (this.columnCount < 1) {
         this.columnCount = 1;
      }

      this.columnGap = var1[81];
      this.referenceOrientation = var1[225].integer();
      this.writingMode = var1[316].keyword();
   }
}
