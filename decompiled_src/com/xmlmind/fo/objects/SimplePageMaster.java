package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;

public class SimplePageMaster {
   public String masterName;
   public Value pageHeight;
   public Value pageWidth;
   public Value marginTop;
   public Value marginBottom;
   public Value marginLeft;
   public Value marginRight;
   public int referenceOrientation;
   public int writingMode;
   public Region[] regions = new Region[5];

   public SimplePageMaster(Value[] var1) {
      Value var2 = var1[178];
      if (var2 != null) {
         this.masterName = var2.name();
      }

      this.pageHeight = var1[212];
      this.pageWidth = var1[214];
      this.marginTop = var1[176];
      this.marginBottom = var1[173];
      this.marginLeft = var1[174];
      this.marginRight = var1[175];
      this.referenceOrientation = var1[225].integer();
      this.writingMode = var1[316].keyword();
   }
}
