package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;
import java.util.Vector;

public class PageMasterReference {
   public static final int TYPE_SINGLE = 1;
   public static final int TYPE_REPEATABLE = 2;
   public static final int TYPE_ALTERNATIVES = 3;
   public static final int TYPE_CONDITIONAL = 4;
   public int type;
   public SimplePageMaster pageMaster;
   public Vector alternatives;
   public String masterReference;
   public int maximumRepeats = 1;
   public int pagePosition;
   public int oddOrEven;
   public int blankOrNotBlank;

   public PageMasterReference(int var1, Value[] var2) {
      this.type = var1;
      if (var1 == 3) {
         this.alternatives = new Vector();
      }

      Value var3 = var2[179];
      if (var3 != null) {
         this.masterReference = var3.name();
      }

      if (var1 == 2 || var1 == 3) {
         var3 = var2[181];
         if (var3.type == 1) {
            this.maximumRepeats = -1;
         } else {
            this.maximumRepeats = (int)Math.round(var3.number());
            if (this.maximumRepeats < 0) {
               this.maximumRepeats = 0;
            }
         }
      }

      this.pagePosition = var2[213].keyword();
      this.oddOrEven = var2[189].keyword();
      this.blankOrNotBlank = var2[15].keyword();
   }

   public void setPageMaster(Vector var1) {
      int var2;
      int var3;
      if (this.type == 3) {
         var2 = 0;

         for(var3 = this.alternatives.size(); var2 < var3; ++var2) {
            PageMasterReference var4 = this.alternative(var2);
            var4.setPageMaster(var1);
         }
      } else {
         var2 = 0;

         for(var3 = var1.size(); var2 < var3; ++var2) {
            SimplePageMaster var5 = (SimplePageMaster)var1.elementAt(var2);
            if (this.masterReference.equals(var5.masterName)) {
               this.pageMaster = var5;
               break;
            }
         }
      }

   }

   public PageMasterReference alternative(int var1) {
      return (PageMasterReference)this.alternatives.elementAt(var1);
   }
}
