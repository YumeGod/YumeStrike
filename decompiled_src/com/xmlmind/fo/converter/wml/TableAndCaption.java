package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class TableAndCaption {
   public int alignment;
   public Table table;
   public Caption caption;

   public TableAndCaption() {
   }

   public TableAndCaption(Context var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Context var1) {
      Value[] var2 = var1.properties.values;
      switch (var2[289].keyword()) {
         case 31:
         case 93:
            this.alignment = 1;
            break;
         case 52:
         case 144:
         case 165:
            this.alignment = 2;
            break;
         case 90:
         case 100:
         case 190:
         default:
            this.alignment = 0;
      }

   }

   public void layout(int var1) throws Exception {
      if (this.table != null) {
         this.table.layout(var1, this.alignment);
      }

   }

   public void print(PrintWriter var1, Encoder var2) throws Exception {
      if (this.caption != null && this.caption.side == 1) {
         this.caption.print(var1, var2);
      }

      if (this.table != null) {
         this.table.print(var1, var2);
      }

      if (this.caption != null && this.caption.side == 2) {
         this.caption.print(var1, var2);
      }

   }

   public int maxWidth() {
      int var1 = 0;
      if (this.table != null) {
         var1 = this.table.maxWidth();
      }

      if (this.caption != null) {
         int var2 = this.caption.maxWidth();
         if (var2 > var1) {
            var1 = var2;
         }
      }

      return var1;
   }

   public int minWidth() {
      int var1 = 0;
      if (this.table != null) {
         var1 = this.table.minWidth();
      }

      if (this.caption != null) {
         int var2 = this.caption.minWidth();
         if (var2 > var1) {
            var1 = var2;
         }
      }

      return var1;
   }
}
