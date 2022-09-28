package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class ParProperties {
   public static final int ALIGNMENT_LEFT = 0;
   public static final int ALIGNMENT_CENTER = 1;
   public static final int ALIGNMENT_RIGHT = 2;
   public static final int ALIGNMENT_JUSTIFIED = 3;
   public int breakBefore;
   public int spaceBefore;
   public int lineHeight;
   public boolean keepTogether;
   public boolean keepWithNext;
   public int alignment;
   public int startIndent;
   public int endIndent;
   public int firstLineIndent;
   public int outlineLevel;
   public Borders borders;
   public int background;
   public int listId;
   public Text listText;
   public Vector tabs;
   public boolean isInTable;
   public int nestingLevel;
   public RunProperties markProperties;

   public ParProperties() {
      this.alignment = 0;
   }

   public ParProperties(Context var1, ColorTable var2) {
      this();
      this.initialize(var1, var2);
   }

   public void initialize(Context var1, ColorTable var2) {
      Context var3 = var1.block();
      Value[] var4 = var3.properties.values;
      this.breakBefore = var3.breakBefore;
      this.spaceBefore = Rtf.toTwips(var3.spaceBefore());
      this.lineHeight = length(var3.lineHeight);
      this.keepTogether = keep(var4[137]);
      this.keepWithNext = keep(var4[141]);
      switch (var4[289].keyword()) {
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

      this.startIndent = length(var4[277]);
      this.endIndent = length(var4[97]);
      this.firstLineIndent = length(var4[294]);
      this.outlineLevel = integer(var4[322], 0);
      this.borders = new Borders(var4, var2);
      if (var3.background != null) {
         this.background = Rtf.colorIndex(var3.background, var2);
      }

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

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      var1.print("\\pard");
      if (this.borders != null && this.borders.materialized()) {
         this.borders.print(var1);
      }

      if (this.isInTable) {
         var1.print("\\intbl");
         if (this.nestingLevel > 1) {
            var1.print("\\itap" + this.nestingLevel);
         }
      }

      if (this.keepTogether) {
         var1.print("\\keep");
      }

      if (this.keepWithNext) {
         var1.print("\\keepn");
      }

      if (this.outlineLevel >= 1 && this.outlineLevel <= 9) {
         var1.print("\\outlinelevel" + Integer.toString(this.outlineLevel - 1));
      }

      if (this.breakBefore == 2) {
         var1.print("\\pagebb");
      }

      if (this.spaceBefore > 0) {
         var1.print("\\sb" + this.spaceBefore);
      }

      switch (this.alignment) {
         case 1:
            var1.print("\\qc");
            break;
         case 2:
            var1.print("\\qr");
            break;
         case 3:
            var1.print("\\qj");
      }

      if (this.startIndent != 0) {
         var1.print("\\li" + this.startIndent);
      }

      if (this.endIndent != 0) {
         var1.print("\\ri" + this.endIndent);
      }

      if (this.firstLineIndent != 0) {
         var1.print("\\fi" + this.firstLineIndent);
      }

      if (this.lineHeight > 0) {
         var1.print("\\sl" + this.lineHeight);
      }

      if (this.listId > 0) {
         var1.print("\\ls" + this.listId);
      }

      if (this.tabs != null) {
         int var3 = 0;

         for(int var4 = this.tabs.size(); var3 < var4; ++var3) {
            Tab var5 = (Tab)this.tabs.elementAt(var3);
            var5.print(var1);
         }
      }

      if (this.background > 0) {
         var1.print("\\cbpat" + this.background);
      }

      if (this.markProperties != null) {
         this.markProperties.print(var1);
      }

   }

   private static int integer(Value var0, int var1) {
      return var0 != null && var0.type == 2 ? var0.integer() : var1;
   }

   private static int length(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Rtf.toTwips(var0.length());
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
      public static final int LEADER_NONE = 0;
      public static final int LEADER_DOTS = 1;
      public static final int LEADER_LINE = 2;
      public static final int ALIGN_LEFT = 0;
      public static final int ALIGN_RIGHT = 1;
      public static final int ALIGN_CENTER = 2;
      public static final int ALIGN_DECIMAL = 3;
      public int position;
      public int leader;
      public int align;

      public Tab(int var1) {
         this(var1, 0, 0);
      }

      public Tab(int var1, int var2, int var3) {
         this.position = var1;
         this.leader = var2;
         this.align = var3;
      }

      public void print(PrintWriter var1) {
         switch (this.align) {
            case 1:
               var1.print("\\tqr");
               break;
            case 2:
               var1.print("\\tqc");
               break;
            case 3:
               var1.print("\\tqdec");
         }

         switch (this.leader) {
            case 1:
               var1.print("\\tldot");
               break;
            case 2:
               var1.print("\\tlth");
         }

         var1.print("\\tx" + this.position);
      }
   }
}
