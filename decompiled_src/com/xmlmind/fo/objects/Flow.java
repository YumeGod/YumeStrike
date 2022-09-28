package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;

public class Flow {
   public static final int TYPE_FLOW = 1;
   public static final int TYPE_STATIC_CONTENT = 2;
   public int type;
   public String flowName;

   public Flow(int var1, Value[] var2) {
      this.type = var1;
      Value var3 = var2[102];
      if (var3 != null) {
         this.flowName = var3.name();
      }

   }

   public int region(SimplePageMaster var1) {
      int var2 = -1;
      if (this.flowName != null) {
         for(int var3 = 0; var3 < var1.regions.length; ++var3) {
            if (var1.regions[var3] != null && this.flowName.equals(var1.regions[var3].regionName)) {
               var2 = var3;
               break;
            }
         }
      }

      return var2;
   }
}
