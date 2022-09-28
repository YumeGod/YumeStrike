package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Borders {
   private static final double BORDER_THIN = 1.0;
   private static final double BORDER_MEDIUM = 2.0;
   private static final double BORDER_THICK = 3.0;
   private static final double BORDER_MIN_WIDTH = 0.25;
   private static final double BORDER_MAX_WIDTH = 12.0;
   public Border top;
   public Border bottom;
   public Border left;
   public Border right;

   public Borders() {
      this.top = new Border(1);
      this.bottom = new Border(2);
      this.left = new Border(3);
      this.right = new Border(4);
   }

   public Borders(Value[] var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Value[] var1) {
      int var2 = var1[66].keyword();
      double var5 = space(var1[208]);
      double var3;
      Color var7;
      if (var2 != 125) {
         var3 = width(var1[67]);
         var7 = var1[65].color();
         this.top.set(var2, var3, var7, var5);
      } else {
         this.top.space = var5;
      }

      var2 = var1[35].keyword();
      var5 = space(var1[199]);
      if (var2 != 125) {
         var3 = width(var1[36]);
         var7 = var1[34].color();
         this.bottom.set(var2, var3, var7, var5);
      } else {
         this.bottom.space = var5;
      }

      var2 = var1[47].keyword();
      var5 = space(var1[203]);
      if (var2 != 125) {
         var3 = width(var1[48]);
         var7 = var1[46].color();
         this.left.set(var2, var3, var7, var5);
      } else {
         this.left.space = var5;
      }

      var2 = var1[51].keyword();
      var5 = space(var1[204]);
      if (var2 != 125) {
         var3 = width(var1[52]);
         var7 = var1[50].color();
         this.right.set(var2, var3, var7, var5);
      } else {
         this.right.space = var5;
      }

   }

   private static double width(Value var0) {
      double var1 = 0.0;
      if (var0.type == 1) {
         switch (var0.keyword()) {
            case 118:
               var1 = 2.0;
               break;
            case 202:
               var1 = 1.0;
               break;
            case 203:
               var1 = 3.0;
         }
      } else if (var0.type == 4) {
         var1 = var0.length();
         if (var1 > 0.0) {
            if (var1 < 0.25) {
               var1 = 0.25;
            } else if (var1 > 12.0) {
               var1 = 12.0;
            }
         } else if (var1 < 0.0) {
            var1 = 0.0;
         }
      }

      return var1;
   }

   private static double space(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
         if (var1 < 0.0) {
            var1 = 0.0;
         }
      }

      return var1;
   }

   public boolean materialized() {
      return this.top.materialized() || this.bottom.materialized() || this.left.materialized() || this.right.materialized();
   }

   public void print(PrintWriter var1) {
      if (this.top.materialized()) {
         this.top.print(var1);
      }

      if (this.left.materialized()) {
         this.left.print(var1);
      }

      if (this.bottom.materialized()) {
         this.bottom.print(var1);
      }

      if (this.right.materialized()) {
         this.right.print(var1);
      }

   }

   public Borders copy() {
      Borders var1 = new Borders();
      var1.top = this.top.copy();
      var1.bottom = this.bottom.copy();
      var1.left = this.left.copy();
      var1.right = this.right.copy();
      return var1;
   }
}
