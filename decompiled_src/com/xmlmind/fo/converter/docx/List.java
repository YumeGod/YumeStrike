package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.LabelFormat;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;
import java.util.Vector;

public final class List {
   public static final int TYPE_LIST = 0;
   public static final int TYPE_PLAIN = 1;
   private int type;
   private LabelFormat labelFormat;
   private Numbering numbering;
   private double startIndent;
   private Vector items;

   public List(Context var1) {
      this(0, var1);
   }

   public List(int var1, Context var2) {
      this.items = new Vector();
      this.type = var1;
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

   public void layout(double var1, NumberingDefinitions var3) throws Exception {
      if (this.items.size() != 0) {
         int var4 = 0;

         int var5;
         ListItem var6;
         for(var5 = this.items.size(); var4 < var5; ++var4) {
            var6 = (ListItem)this.items.elementAt(var4);
            var6.layout(var1, var3);
            if (this.startIndent == 0.0) {
               this.startIndent = var6.labelStart;
            }
         }

         if (this.type == 0) {
            var6 = (ListItem)this.items.elementAt(0);
            this.numbering = var6.numbering(this.labelFormat);
            if (this.numbering != null) {
               var3.add(this.numbering);
            } else {
               this.type = 1;
            }
         }

         var4 = 0;

         for(var5 = this.items.size(); var4 < var5; ++var4) {
            var6 = (ListItem)this.items.elementAt(var4);
            var6.setup(this.numbering);
         }

      }
   }

   public void print(PrintWriter var1) {
      int var2 = 0;

      for(int var3 = this.items.size(); var2 < var3; ++var2) {
         ListItem var4 = (ListItem)this.items.elementAt(var2);
         var4.print(var1);
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

   public double startIndent() {
      return this.startIndent;
   }
}
