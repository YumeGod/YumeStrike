package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public final class TableAndCaption {
   public Table table;
   public Caption caption;

   public TableAndCaption(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
   }

   public void layout(double var1, StyleTable var3) throws Exception {
      if (this.table != null) {
         this.table.layout(var1, var3);
      }

   }

   public void print(PrintWriter var1, Encoder var2) {
      if (this.caption != null && this.caption.side == 0) {
         this.caption.print(var1, var2);
      }

      if (this.table != null) {
         this.table.print(var1, var2);
      }

      if (this.caption != null && this.caption.side == 1) {
         this.caption.print(var1, var2);
      }

   }

   public double minWidth() {
      double var1 = 0.0;
      if (this.table != null) {
         var1 = this.table.minWidth();
      }

      if (this.caption != null) {
         double var3 = this.caption.minWidth();
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   public double maxWidth() {
      double var1 = 0.0;
      if (this.table != null) {
         var1 = this.table.maxWidth();
      }

      if (this.caption != null) {
         double var3 = this.caption.maxWidth();
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }
}
