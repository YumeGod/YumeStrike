package com.xmlmind.fo.converter.docx;

import java.io.PrintWriter;

public final class TabStop {
   public static final double POSITION_UNSPECIFIED = Double.MAX_VALUE;
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   public static final int ALIGNMENT_DECIMAL = 3;
   public static final int LEADER_NONE = 0;
   public static final int LEADER_SOLID = 1;
   public static final int LEADER_DOTTED = 2;
   public double position;
   public int alignment;
   public int leader;

   public TabStop() {
      this(Double.MAX_VALUE);
   }

   public TabStop(double var1) {
      this(var1, 0);
   }

   public TabStop(double var1, int var3) {
      this(var1, var3, 0);
   }

   public TabStop(double var1, int var3, int var4) {
      this.position = var1;
      this.alignment = var3;
      this.leader = var4;
   }

   public void print(PrintWriter var1) {
      var1.print("<w:tab");
      var1.print(" w:val=\"" + this.alignment() + "\"");
      var1.print(" w:pos=\"" + Math.round(20.0 * this.position) + "\"");
      if (this.leader != 0) {
         var1.print(" w:leader=\"" + this.leader() + "\"");
      }

      var1.print(" />");
   }

   private String alignment() {
      String var1;
      switch (this.alignment) {
         case 0:
         default:
            var1 = "left";
            break;
         case 1:
            var1 = "center";
            break;
         case 2:
            var1 = "right";
            break;
         case 3:
            var1 = "decimal";
      }

      return var1;
   }

   private String leader() {
      String var1;
      switch (this.leader) {
         case 0:
         default:
            var1 = "none";
            break;
         case 1:
            var1 = "underscore";
            break;
         case 2:
            var1 = "dot";
      }

      return var1;
   }
}
