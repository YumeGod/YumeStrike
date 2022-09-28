package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.properties.Color;
import java.io.PrintWriter;

public final class Border implements Cloneable {
   public static final String SIDE_TOP = "top";
   public static final String SIDE_BOTTOM = "bottom";
   public static final String SIDE_LEFT = "left";
   public static final String SIDE_RIGHT = "right";
   public static final String SIDE_ALL = "bdr";
   public String side;
   public int style;
   public int width;
   public Color color;
   public int space;

   public Border(String var1) {
      this(var1, 125, 0, (Color)null, 0);
   }

   public Border(String var1, int var2, int var3, Color var4, int var5) {
      this.side = var1;
      this.set(var2, var3, var4, var5);
   }

   public void set(int var1, int var2, Color var3, int var4) {
      this.style = var1;
      this.width = var2;
      this.color = var3;
      this.space = var4;
   }

   public boolean materialized() {
      if (this.style != 125 && this.style != 75 && this.width > 0) {
         return true;
      } else {
         return this.space > 0;
      }
   }

   public void print(PrintWriter var1) {
      var1.print("<w:" + this.side);
      var1.print(" w:val=\"" + this.style() + "\"");
      if (this.width > 0) {
         var1.print(" w:sz=\"" + Wml.toEighths((double)this.width() / 20.0) + "\"");
      }

      if (this.color != null) {
         var1.print(" w:color=\"" + Wml.hexColorType(this.color) + "\"");
      }

      if (this.space > 0) {
         var1.print(" w:space=\"" + this.space / 20 + "\"");
      }

      var1.print(" />");
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
            var1 = "three-d-engrave";
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
            var1 = "three-d-emboss";
            break;
         case 187:
            var1 = "single";
      }

      return var1;
   }

   private int width() {
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
      Border var1 = null;

      try {
         var1 = (Border)this.clone();
      } catch (CloneNotSupportedException var3) {
      }

      return var1;
   }
}
