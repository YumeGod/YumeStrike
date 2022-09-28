package com.xmlmind.fo.converter.rtf;

import java.io.PrintWriter;

public class Border implements Cloneable {
   public static final int SIDE_TOP = 1;
   public static final int SIDE_BOTTOM = 2;
   public static final int SIDE_LEFT = 3;
   public static final int SIDE_RIGHT = 4;
   public static final int SIDE_ALL = 5;
   public int side;
   public int style;
   public int width;
   public int color;
   public int space;

   public Border(int var1) {
      this(var1, 125, 0, 0, 0);
   }

   public Border(int var1, int var2, int var3, int var4, int var5) {
      this.side = var1;
      this.set(var2, var3, var4, var5);
   }

   public void set(int var1, int var2, int var3, int var4) {
      this.style = var1;
      this.width = var2;
      this.color = var3;
      this.space = var4;
   }

   public void set(Border var1) {
      this.set(var1.style, var1.width, var1.color, var1.space);
   }

   public boolean materialized() {
      return this.style != 125 && this.style != 75 && this.width > 0;
   }

   public void print(PrintWriter var1) {
      switch (this.side) {
         case 1:
            var1.print("\\brdrt");
            break;
         case 2:
            var1.print("\\brdrb");
            break;
         case 3:
            var1.print("\\brdrl");
            break;
         case 4:
            var1.print("\\brdrr");
            break;
         case 5:
            var1.print("\\chbrdr");
            break;
         default:
            return;
      }

      var1.print("\\brdr" + this.style());
      var1.print("\\brdrw" + this.width());
      var1.print("\\brsp" + this.space);
      if (this.color > 0) {
         var1.print("\\brdrcf" + this.color);
      }

   }

   protected String style() {
      String var1;
      switch (this.style) {
         case 42:
            var1 = "dash";
            break;
         case 49:
            var1 = "dot";
            break;
         case 50:
            var1 = "db";
            break;
         case 73:
            var1 = "engrave";
            break;
         case 75:
         case 125:
         default:
            var1 = "s";
            break;
         case 89:
            var1 = "inset";
            break;
         case 143:
            var1 = "outset";
            break;
         case 164:
            var1 = "emboss";
            break;
         case 187:
            var1 = "s";
      }

      return var1;
   }

   protected int width() {
      if (this.style == 50) {
         int var1 = this.width / 3;
         if (var1 <= 0) {
            var1 = 1;
         }

         return var1;
      } else {
         return this.width;
      }
   }

   public Border copy() {
      try {
         return (Border)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
