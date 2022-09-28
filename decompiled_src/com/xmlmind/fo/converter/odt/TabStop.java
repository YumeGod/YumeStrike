package com.xmlmind.fo.converter.odt;

import java.io.PrintWriter;

public final class TabStop {
   public static final double POSITION_UNSPECIFIED = Double.MAX_VALUE;
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   public static final int ALIGNMENT_CHAR = 3;
   public static final int LEADER_TYPE_NONE = 0;
   public static final int LEADER_TYPE_SINGLE = 1;
   public static final int LEADER_TYPE_DOUBLE = 2;
   public static final int LEADER_STYLE_NONE = 0;
   public static final int LEADER_STYLE_SOLID = 1;
   public static final int LEADER_STYLE_DOTTED = 2;
   public static final int LEADER_STYLE_DASHED = 3;
   public double position;
   public int alignment;
   public int leaderType;
   public int leaderStyle;
   public double leaderWidth;

   public TabStop() {
      this(Double.MAX_VALUE);
   }

   public TabStop(double var1) {
      this.position = var1;
   }

   public void print(PrintWriter var1) {
      var1.println("<style:tab-stop");
      var1.println(" style:position=\"" + Odt.length(this.position, 1) + "\"");
      var1.println(" style:type=\"" + this.type() + "\"");
      if (this.alignment == 3) {
         var1.println(" style:char=\".\"");
      }

      if (this.leaderType != 0 && this.leaderStyle != 0) {
         var1.println(" style:leader-type=\"" + this.leaderType() + "\"");
         var1.println(" style:leader-style=\"" + this.leaderStyle() + "\"");
         if (this.leaderWidth > 0.0) {
            var1.println(" style:leader-width=\"" + Odt.length(this.leaderWidth, 1) + "\"");
         }
      }

      var1.println("/>");
   }

   private String type() {
      String var1 = "left";
      switch (this.alignment) {
         case 1:
            var1 = "center";
            break;
         case 2:
            var1 = "right";
            break;
         case 3:
            var1 = "char";
      }

      return var1;
   }

   private String leaderType() {
      String var1 = "none";
      switch (this.leaderType) {
         case 1:
            var1 = "single";
            break;
         case 2:
            var1 = "double";
      }

      return var1;
   }

   private String leaderStyle() {
      String var1 = "none";
      switch (this.leaderStyle) {
         case 1:
            var1 = "solid";
            break;
         case 2:
            var1 = "dotted";
            break;
         case 3:
            var1 = "dash";
      }

      return var1;
   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ hash(this.position);
      var1 = shift(var1) ^ this.alignment;
      var1 = shift(var1) ^ this.leaderType;
      var1 = shift(var1) ^ this.leaderStyle;
      var1 = shift(var1) ^ hash(this.leaderWidth);
      return var1;
   }

   private static int hash(double var0) {
      long var2 = Double.doubleToLongBits(var0);
      return (int)(var2 ^ var2 >>> 32);
   }

   private static int shift(int var0) {
      return var0 << 1 | var0 >>> 31;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TabStop)) {
         return false;
      } else {
         TabStop var2 = (TabStop)var1;
         return this.position == var2.position && this.alignment == var2.alignment && this.leaderType == var2.leaderType && this.leaderStyle == var2.leaderStyle && this.leaderWidth == var2.leaderWidth;
      }
   }
}
