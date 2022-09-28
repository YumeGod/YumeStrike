package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Borders {
   public static final int TYPE_PARAGRAPH = 0;
   public static final int TYPE_TABLE_ROW = 1;
   public static final int TYPE_TABLE_CELL = 2;
   public static final int TYPE_PAGE = 3;
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
      this(0);
   }

   public Borders(int var1) {
      switch (var1) {
         case 0:
         default:
            this.top = new Border(1);
            this.bottom = new Border(2);
            this.left = new Border(3);
            this.right = new Border(4);
            break;
         case 1:
            this.top = new RowBorder(1);
            this.bottom = new RowBorder(2);
            this.left = new RowBorder(3);
            this.right = new RowBorder(4);
            break;
         case 2:
            this.top = new CellBorder(1);
            this.bottom = new CellBorder(2);
            this.left = new CellBorder(3);
            this.right = new CellBorder(4);
            break;
         case 3:
            this.top = new PageBorder(1);
            this.bottom = new PageBorder(2);
            this.left = new PageBorder(3);
            this.right = new PageBorder(4);
      }

   }

   public Borders(Value[] var1, ColorTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public Borders(int var1, Value[] var2, ColorTable var3) {
      this(var1);
      this.initialize(var2, var3);
   }

   public void initialize(Value[] var1, ColorTable var2) {
      int var3 = var1[66].keyword();
      int var5 = space(var1[208]);
      int var4;
      Color var6;
      if (var3 != 125) {
         var4 = width(var1[67]);
         var6 = var1[65].color();
         this.top.set(var3, var4, Rtf.colorIndex(var6, var2), var5);
      } else {
         this.top.space = var5;
      }

      var3 = var1[35].keyword();
      var5 = space(var1[199]);
      if (var3 != 125) {
         var4 = width(var1[36]);
         var6 = var1[34].color();
         this.bottom.set(var3, var4, Rtf.colorIndex(var6, var2), var5);
      } else {
         this.bottom.space = var5;
      }

      var3 = var1[47].keyword();
      var5 = space(var1[203]);
      if (var3 != 125) {
         var4 = width(var1[48]);
         var6 = var1[46].color();
         this.left.set(var3, var4, Rtf.colorIndex(var6, var2), var5);
      } else {
         this.left.space = var5;
      }

      var3 = var1[51].keyword();
      var5 = space(var1[204]);
      if (var3 != 125) {
         var4 = width(var1[52]);
         var6 = var1[50].color();
         this.right.set(var3, var4, Rtf.colorIndex(var6, var2), var5);
      } else {
         this.right.space = var5;
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
         var1 = Rtf.toTwips(var0.length());
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
         var1 = Rtf.toTwips(var0.length());
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

      if (this.bottom.materialized()) {
         this.bottom.print(var1);
      }

      if (this.left.materialized()) {
         this.left.print(var1);
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
