package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.LabelFormat;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;
import java.util.Vector;

public final class List {
   public static final int TYPE_LIST_BLOCK = 0;
   public static final int TYPE_PLAIN_PARAGRAPHS = 1;
   private int level;
   private LabelFormat labelFormat;
   private int type;
   private ListStyle style;
   private double marginLeft;
   private Vector items = new Vector();

   public List(int var1, Context var2) {
      this.level = var1;
      this.initialize(var2);
   }

   private void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      Value var3 = var2[321];
      if (var3 != null && var3.type == 30) {
         this.labelFormat = var3.labelFormat();
      }

   }

   public void add(ListItem var1) {
      this.items.addElement(var1);
   }

   public int type() {
      return this.type;
   }

   public void layout(int var1, double var2, StyleTable var4) throws Exception {
      this.type = var1;
      this.layout(var2, var4);
   }

   public void layout(double var1, StyleTable var3) throws Exception {
      if (this.items.size() != 0) {
         int var4 = 0;

         int var5;
         ListItem var6;
         for(var5 = this.items.size(); var4 < var5; ++var4) {
            var6 = (ListItem)this.items.elementAt(var4);
            var6.layout(var1, var3);
            if (this.marginLeft == 0.0) {
               this.marginLeft = var6.labelStart;
            }
         }

         if (this.type == 0) {
            var6 = (ListItem)this.items.elementAt(0);
            this.style = var6.listStyle(this.labelFormat);
            if (this.style != null) {
               this.style.listLevel = this.level;
               this.style = var3.add(this.style);
            } else {
               this.type = 1;
            }
         }

         var4 = 0;

         for(var5 = this.items.size(); var4 < var5; ++var4) {
            var6 = (ListItem)this.items.elementAt(var4);
            var6.setup(this.type, this.style, var3);
         }

      }
   }

   public void print(PrintWriter var1, Encoder var2) {
      int var3 = this.items.size();
      if (var3 != 0) {
         int var5;
         if (this.type == 0) {
            boolean var4 = false;
            var1.print("<text:list");
            var1.print(" text:style-name=\"" + this.style.name + "\"");
            var1.println(">");
            var5 = 0;

            for(int var6 = var3; var5 < var6; ++var5) {
               if (var4) {
                  var1.print("<text:list");
                  var1.print(" text:style-name=\"" + this.style.name + "\"");
                  var1.print(" text:continue-numbering=\"true\"");
                  var1.println(">");
                  var4 = false;
               }

               ListItem var7 = (ListItem)this.items.elementAt(var5);
               var4 = var7.print(var1, var2);
            }

            if (!var4) {
               var1.println("</text:list>");
            }
         } else {
            int var8 = 0;

            for(var5 = var3; var8 < var5; ++var8) {
               ListItem var9 = (ListItem)this.items.elementAt(var8);
               var9.print(var1, var2);
            }
         }

      }
   }

   public double minWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.items.size(); var3 < var4; ++var3) {
         ListItem var5 = (ListItem)this.items.elementAt(var3);
         double var6 = var5.minWidth();
         if (var6 > var1) {
            var1 = var6;
         }
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      int var3 = 0;

      for(int var4 = this.items.size(); var3 < var4; ++var3) {
         ListItem var5 = (ListItem)this.items.elementAt(var3);
         double var6 = var5.maxWidth();
         if (var6 > var1) {
            var1 = var6;
         }
      }

      return var1;
   }

   public double marginLeft() {
      return this.marginLeft;
   }
}
