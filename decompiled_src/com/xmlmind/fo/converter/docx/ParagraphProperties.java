package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class ParagraphProperties {
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   public static final int ALIGNMENT_JUSTIFIED = 3;
   public int breakBefore;
   public double spaceBefore;
   public double lineHeight;
   public boolean keepTogether;
   public boolean keepWithNext;
   public int alignment;
   public double startIndent;
   public double endIndent;
   public double firstLineIndent;
   public int outlineLevel;
   public Borders borders;
   public Color background;
   public int numberingId;
   public TabStops tabStops;
   public RunProperties markProperties;

   public ParagraphProperties() {
      this.tabStops = new TabStops();
   }

   public ParagraphProperties(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Context var2 = var1.block();
      Value[] var3 = var2.properties.values;
      this.breakBefore = var2.breakBefore;
      this.spaceBefore = var2.spaceBefore();
      this.lineHeight = length(var2.lineHeight);
      this.keepTogether = keep(var3[137]);
      this.keepWithNext = keep(var3[141]);
      switch (var3[289].keyword()) {
         case 31:
            this.alignment = 1;
            break;
         case 52:
         case 144:
         case 165:
            this.alignment = 2;
         case 90:
         case 100:
         case 190:
         default:
            break;
         case 93:
            this.alignment = 3;
      }

      this.startIndent = length(var3[277]);
      this.endIndent = length(var3[97]);
      this.firstLineIndent = length(var3[294]);
      this.outlineLevel = integer(var3[322], 0);
      this.borders = new Borders(var3);
      this.background = var2.background;
   }

   public void print(PrintWriter var1) {
      var1.print("<w:pPr>");
      if (this.keepWithNext) {
         var1.print("<w:keepNext />");
      }

      if (this.keepTogether) {
         var1.print("<w:keepLines />");
      }

      if (this.breakBefore == 2) {
         var1.print("<w:pageBreakBefore />");
      }

      if (this.numberingId != 0) {
         var1.print("<w:numPr>");
         var1.print("<w:ilvl w:val=\"0\" />");
         var1.print("<w:numId w:val=\"" + this.numberingId + "\" />");
         var1.print("</w:numPr>");
      }

      if (this.borders != null && this.borders.materialized()) {
         var1.print("<w:pBdr>");
         this.borders.print(var1);
         var1.print("</w:pBdr>");
      }

      if (this.background != null) {
         var1.print("<w:shd");
         var1.print(" w:val=\"clear\"");
         var1.print(" w:fill=\"" + Wml.hexColorType(this.background) + "\"");
         var1.print(" />");
      }

      if (this.tabStops.count() > 0) {
         this.tabStops.print(var1);
      }

      var1.print("<w:spacing");
      var1.print(" w:before=\"" + Math.round(20.0 * this.spaceBefore) + "\"");
      var1.print(" w:after=\"0\"");
      if (this.lineHeight > 0.0) {
         var1.print(" w:line=\"" + Math.round(20.0 * this.lineHeight) + "\"");
         var1.print(" w:lineRule=\"atLeast\"");
      } else {
         var1.print(" w:line=\"240\" w:lineRule=\"auto\"");
      }

      var1.print(" />");
      if (this.startIndent > 0.0 || this.endIndent > 0.0 || this.firstLineIndent != 0.0) {
         var1.print("<w:ind");
         var1.print(" w:left=\"" + Math.round(20.0 * this.startIndent) + "\"");
         var1.print(" w:right=\"" + Math.round(20.0 * this.endIndent) + "\"");
         int var2 = (int)Math.round(20.0 * this.firstLineIndent);
         if (var2 < 0) {
            var1.print(" w:hanging=\"" + -var2 + "\"");
         } else {
            var1.print(" w:firstLine=\"" + var2 + "\"");
         }

         var1.print(" />");
      }

      if (this.alignment != 0) {
         var1.print("<w:jc");
         var1.print(" w:val=\"" + this.alignment() + "\"");
         var1.print(" />");
      }

      if (this.outlineLevel >= 1 && this.outlineLevel <= 9) {
         var1.print("<w:outlineLvl w:val=\"" + Integer.toString(this.outlineLevel - 1) + "\" />");
      }

      if (this.markProperties != null) {
         this.markProperties.print(var1);
      }

      var1.println("</w:pPr>");
   }

   private static int integer(Value var0, int var1) {
      return var0 != null && var0.type == 2 ? var0.integer() : var1;
   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   private static boolean keep(Value var0) {
      boolean var1 = false;
      switch (var0.type) {
         case 1:
            if (var0.keyword() == 8) {
               var1 = true;
            }
            break;
         case 2:
            if (var0.integer() > 0) {
               var1 = true;
            }
      }

      return var1;
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
            var1 = "both";
      }

      return var1;
   }
}
