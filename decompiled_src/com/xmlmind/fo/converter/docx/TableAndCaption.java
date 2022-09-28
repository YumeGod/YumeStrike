package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import java.io.PrintWriter;

public final class TableAndCaption {
   public Table table;
   public Caption caption;

   public TableAndCaption(Context var1) {
      this.initialize(var1);
   }

   private void initialize(Context var1) {
   }

   public void layout(double var1, NumberingDefinitions var3) throws Exception {
      if (this.table != null) {
         this.table.layout(var1, var3);
      }

   }

   public void print(PrintWriter var1) {
      if (this.caption != null && this.caption.side == 0) {
         this.caption.print(var1);
      }

      if (this.table != null) {
         this.table.print(var1);
      }

      if (this.caption != null && this.caption.side == 1) {
         this.caption.print(var1);
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
