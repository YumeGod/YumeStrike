package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class ParProperties {
   public static final String ALIGNMENT_LEFT = "left";
   public static final String ALIGNMENT_CENTER = "center";
   public static final String ALIGNMENT_RIGHT = "right";
   public static final String ALIGNMENT_JUSTIFIED = "both";
   public int breakBefore;
   public int spaceBefore;
   public int lineHeight;
   public boolean keepTogether;
   public boolean keepWithNext;
   public String alignment;
   public int startIndent;
   public int endIndent;
   public int firstLineIndent;
   public int outlineLevel;
   public Borders borders;
   public Color background;
   public int listId;
   public Vector tabs;
   public RunProperties markProperties;

   public ParProperties() {
      this.alignment = "left";
   }

   public ParProperties(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Context var2 = var1.block();
      Value[] var3 = var2.properties.values;
      this.breakBefore = var2.breakBefore;
      this.spaceBefore = Wml.toTwips(var2.spaceBefore());
      this.lineHeight = length(var2.lineHeight);
      this.keepTogether = keep(var3[137]);
      this.keepWithNext = keep(var3[141]);
      switch (var3[289].keyword()) {
         case 31:
            this.alignment = "center";
            break;
         case 52:
         case 144:
         case 165:
            this.alignment = "right";
         case 90:
         case 100:
         case 190:
         default:
            break;
         case 93:
            this.alignment = "both";
      }

      this.startIndent = length(var3[277]);
      this.endIndent = length(var3[97]);
      this.firstLineIndent = length(var3[294]);
      this.outlineLevel = integer(var3[322], 0);
      this.borders = new Borders(var3);
      this.background = var2.background;
   }

   public void addTab(Tab var1) {
      if (this.tabs == null) {
         this.tabs = new Vector();
      }

      this.tabs.addElement(var1);
   }

   public boolean hasTabs() {
      return this.tabs != null && this.tabs.size() > 0;
   }

   public Tab[] tabs() {
      Tab[] var1 = null;
      if (this.hasTabs()) {
         var1 = new Tab[this.tabs.size()];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (Tab)this.tabs.elementAt(var2);
         }
      }

      return var1;
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

      if (this.listId > 0) {
         var1.print("<w:listPr>");
         var1.print("<w:ilfo w:val=\"" + this.listId + "\" />");
         var1.print("</w:listPr>");
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

      if (this.tabs != null) {
         var1.print("<w:tabs>");
         int var2 = 0;

         for(int var3 = this.tabs.size(); var2 < var3; ++var2) {
            Tab var4 = (Tab)this.tabs.elementAt(var2);
            var4.print(var1);
         }

         var1.print("</w:tabs>");
      }

      if (this.spaceBefore != 0 || this.lineHeight != 0) {
         var1.print("<w:spacing");
         var1.print(" w:before=\"" + this.spaceBefore + "\"");
         var1.print(" w:after=\"0\"");
         if (this.lineHeight != 0) {
            var1.print(" w:line=\"" + this.lineHeight + "\"");
            var1.print(" w:line-rule=\"at-least\"");
         }

         var1.print(" />");
      }

      if (this.startIndent != 0 || this.endIndent != 0 || this.firstLineIndent != 0) {
         var1.print("<w:ind");
         var1.print(" w:left=\"" + this.startIndent + "\"");
         var1.print(" w:right=\"" + this.endIndent + "\"");
         var1.print(" w:first-line=\"" + this.firstLineIndent + "\"");
         var1.print(" />");
      }

      if (this.alignment != "left") {
         var1.print("<w:jc");
         var1.print(" w:val=\"" + this.alignment + "\"");
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

   private static int length(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Wml.toTwips(var0.length());
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

   public static class Tab {
      public static final String ALIGNMENT_LEFT = "left";
      public static final String ALIGNMENT_CENTER = "center";
      public static final String ALIGNMENT_RIGHT = "right";
      public static final String ALIGNMENT_DECIMAL = "decimal";
      public static final String ALIGNMENT_LIST = "list";
      public static final String LEADER_DOTS = "dot";
      public static final String LEADER_LINE = "underscore";
      public int position;
      public String alignment;
      public String leader;

      public Tab(int var1) {
         this(var1, "left");
      }

      public Tab(int var1, String var2) {
         this(var1, var2, (String)null);
      }

      public Tab(int var1, String var2, String var3) {
         this.position = var1;
         this.alignment = var2;
         this.leader = var3;
      }

      public void print(PrintWriter var1) {
         var1.print("<w:tab");
         var1.print(" w:val=\"" + this.alignment + "\"");
         var1.print(" w:pos=\"" + this.position + "\"");
         if (this.leader != null) {
            var1.print(" w:leader=\"" + this.leader + "\"");
         }

         var1.print(" />");
      }
   }
}
