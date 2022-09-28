package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.properties.Color;
import java.io.PrintWriter;

public final class Border implements Cloneable {
   public static final int SIDE_ALL = 0;
   public static final int SIDE_TOP = 1;
   public static final int SIDE_BOTTOM = 2;
   public static final int SIDE_LEFT = 3;
   public static final int SIDE_RIGHT = 4;
   public int side;
   public int style;
   public double width;
   public Color color;
   public double space;

   public Border(int var1) {
      this(var1, 125, 0.0, (Color)null, 0.0);
   }

   public Border(int var1, int var2, double var3, Color var5, double var6) {
      this.side = var1;
      this.set(var2, var3, var5, var6);
   }

   public void set(int var1, double var2, Color var4, double var5) {
      this.style = var1;
      this.width = var2;
      this.color = var4;
      this.space = var5;
   }

   public boolean materialized() {
      if (this.style != 125 && this.style != 75 && this.width > 0.0) {
         return true;
      } else {
         return this.space > 0.0;
      }
   }

   public void print(PrintWriter var1) {
      var1.print("<w:" + this.side());
      var1.print(" w:val=\"" + this.style() + "\"");
      if (this.width > 0.0) {
         var1.print(" w:sz=\"" + Math.round(8.0 * this.width()) + "\"");
      }

      if (this.color != null) {
         var1.print(" w:color=\"" + Wml.hexColorType(this.color) + "\"");
      }

      if (this.space > 0.0) {
         var1.print(" w:space=\"" + Math.round(this.space) + "\"");
      }

      var1.print(" />");
   }

   private String side() {
      String var1;
      switch (this.side) {
         case 0:
         default:
            var1 = "bdr";
            break;
         case 1:
            var1 = "top";
            break;
         case 2:
            var1 = "bottom";
            break;
         case 3:
            var1 = "left";
            break;
         case 4:
            var1 = "right";
      }

      return var1;
   }

   private String style() {
      String var1;
      switch (this.style) {
         case 42:
            var1 = "dashed";
            break;
         case 49:
            var1 = "dotted";
            break;
         case 50:
            var1 = "double";
            break;
         case 73:
            var1 = "threeDEngrave";
            break;
         case 75:
         case 125:
         default:
            var1 = "none";
            break;
         case 89:
            var1 = "inset";
            break;
         case 143:
            var1 = "outset";
            break;
         case 164:
            var1 = "threeDEmboss";
            break;
         case 187:
            var1 = "single";
      }

      return var1;
   }

   private double width() {
      if (this.style == 50) {
         double var1 = this.width / 3.0;
         if (var1 <= 0.0) {
            var1 = 1.0;
         }

         return var1;
      } else {
         return this.width;
      }
   }

   public Border copy() {
      Border var1 = null;

      try {
         var1 = (Border)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
