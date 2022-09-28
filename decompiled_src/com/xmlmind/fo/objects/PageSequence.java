package com.xmlmind.fo.objects;

import com.xmlmind.fo.properties.Value;

public class PageSequence {
   public static final int AUTO = 0;
   public static final int AUTO_ODD = -1;
   public static final int AUTO_EVEN = -2;
   public SimplePageMaster pageMaster;
   public PageSequenceMaster pageSequenceMaster;
   public String masterReference;
   public String id;
   public String country;
   public String language;
   public int initialPageNumber;
   public int forcePageCount;
   public String format;
   public int letterValue;
   public char groupingSeparator;
   public int groupingSize;

   public PageSequence(Value[] var1) {
      Value var2 = var1[179];
      if (var2 != null) {
         this.masterReference = var2.name();
      }

      var2 = var1[125];
      if (var2 != null) {
         this.id = var2.id();
      }

      var2 = var1[87];
      if (var2.type == 19) {
         this.country = var2.country();
      }

      var2 = var1[146];
      if (var2.type == 20) {
         this.language = var2.language();
      }

      var2 = var1[127];
      if (var2.type == 1) {
         switch (var2.keyword()) {
            case 10:
            default:
               this.initialPageNumber = 0;
               break;
            case 11:
               this.initialPageNumber = -2;
               break;
            case 12:
               this.initialPageNumber = -1;
         }
      } else {
         this.initialPageNumber = (int)Math.round(var2.number());
         if (this.initialPageNumber < 1) {
            this.initialPageNumber = 1;
         }
      }

      this.forcePageCount = var1[112].keyword();
      this.format = var1[113].string();
      this.letterValue = var1[162].keyword();
      var2 = var1[116];
      if (var2 != null) {
         this.groupingSeparator = var2.character();
      }

      var2 = var1[117];
      if (var2 != null) {
         this.groupingSize = (int)Math.round(var2.number());
         if (this.groupingSize < 1) {
            this.groupingSize = 1;
         }
      }

   }
}
