package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Borders {
   private static final int BORDER_THIN = 20;
   private static final int BORDER_MEDIUM = 40;
   private static final int BORDER_THICK = 60;
   private static final int BORDER_MIN_WIDTH = 5;
   private static final int BORDER_MAX_WIDTH = 240;
   public Border top;
   public Border bottom;
   public Border left;
   public Border right;

   public Borders() {
      this.top = new Border("top");
      this.bottom = new Border("bottom");
      this.left = new Border("left");
      this.right = new Border("right");
   }

   public Borders(Value[] var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Value[] var1) {
      int var2 = var1[66].keyword();
      int var4 = space(var1[208]);
      int var3;
      Color var5;
      if (var2 != 125) {
         var3 = width(var1[67]);
         var5 = var1[65].color();
         this.top.set(var2, var3, var5, var4);
      } else {
         this.top.space = var4;
      }

      var2 = var1[35].keyword();
      var4 = space(var1[199]);
      if (var2 != 125) {
         var3 = width(var1[36]);
         var5 = var1[34].color();
         this.bottom.set(var2, var3, var5, var4);
      } else {
         this.bottom.space = var4;
      }

      var2 = var1[47].keyword();
      var4 = space(var1[203]);
      if (var2 != 125) {
         var3 = width(var1[48]);
         var5 = var1[46].color();
         this.left.set(var2, var3, var5, var4);
      } else {
         this.left.space = var4;
      }

      var2 = var1[51].keyword();
      var4 = space(var1[204]);
      if (var2 != 125) {
         var3 = width(var1[52]);
         var5 = var1[50].color();
         this.right.set(var2, var3, var5, var4);
      } else {
         this.right.space = var4;
      }

   }

   private static int width(Value var0) {
      int var1;
      if (var0.type == 1) {
         switch (var0.keyword()) {
            case 118:
            default:
               var1 = 40;
               break;
            case 202:
               var1 = 20;
               break;
            case 203:
               var1 = 60;
         }
      } else {
         var1 = Wml.toTwips(var0.length());
         if (var1 > 0) {
            if (var1 < 5) {
               var1 = 5;
            } else if (var1 > 240) {
               var1 = 240;
            }
         } else if (var1 < 0) {
            var1 = 0;
         }
      }

      return var1;
   }

   private static int space(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Wml.toTwips(var0.length());
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
