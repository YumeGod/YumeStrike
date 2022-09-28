package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Borders {
   private static final double BORDER_THIN = 1.0;
   private static final double BORDER_MEDIUM = 2.0;
   private static final double BORDER_THICK = 3.0;
   public Border top;
   public Border bottom;
   public Border left;
   public Border right;

   public Borders() {
      this.top = new Border();
      this.bottom = new Border();
      this.left = new Border();
      this.right = new Border();
   }

   private Borders(Border var1, Border var2, Border var3, Border var4) {
      this.top = var1;
      this.bottom = var2;
      this.left = var3;
      this.right = var4;
   }

   public Borders(Value[] var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Value[] var1) {
      int var2 = var1[66].keyword();
      int var3;
      double var4;
      if (var2 != 125) {
         var4 = width(var1[67]);
         var3 = Odt.rgb(var1[65].color());
         this.top.set(var2, var3, var4);
      }

      var2 = var1[35].keyword();
      if (var2 != 125) {
         var4 = width(var1[36]);
         var3 = Odt.rgb(var1[34].color());
         this.bottom.set(var2, var3, var4);
      }

      var2 = var1[47].keyword();
      if (var2 != 125) {
         var4 = width(var1[48]);
         var3 = Odt.rgb(var1[46].color());
         this.left.set(var2, var3, var4);
      }

      var2 = var1[51].keyword();
      if (var2 != 125) {
         var4 = width(var1[52]);
         var3 = Odt.rgb(var1[50].color());
         this.right.set(var2, var3, var4);
      }

   }

   private static double width(Value var0) {
      double var1 = 0.0;
      if (var0.type == 1) {
         switch (var0.keyword()) {
            case 118:
               var1 = 2.0;
               break;
            case 202:
               var1 = 1.0;
               break;
            case 203:
               var1 = 3.0;
         }
      } else {
         var1 = var0.length();
         if (var1 < 0.0) {
            var1 = 0.0;
         }
      }

      return var1;
   }

   public boolean materialized() {
      return this.top.materialized() || this.bottom.materialized() || this.left.materialized() || this.right.materialized();
   }

   public void print(PrintWriter var1) {
      if (this.top.materialized()) {
         var1.println(" fo:border-top=\"" + this.top.toString() + "\"");
      }

      if (this.bottom.materialized()) {
         var1.println(" fo:border-bottom=\"" + this.bottom.toString() + "\"");
      }

      if (this.left.materialized()) {
         var1.println(" fo:border-left=\"" + this.left.toString() + "\"");
      }

      if (this.right.materialized()) {
         var1.println(" fo:border-right=\"" + this.right.toString() + "\"");
      }

   }

   public int hashCode() {
      int var1 = 0;
      var1 = shift(var1) ^ this.top.hashCode();
      var1 = shift(var1) ^ this.bottom.hashCode();
      var1 = shift(var1) ^ this.left.hashCode();
      var1 = shift(var1) ^ this.right.hashCode();
      return var1;
   }

   private static int shift(int var0) {
      return var0 << 1 | var0 >>> 31;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Borders)) {
         return false;
      } else {
         Borders var2 = (Borders)var1;
         return this.top.equals(var2.top) && this.bottom.equals(var2.bottom) && this.left.equals(var2.left) && this.right.equals(var2.right);
      }
   }

   public Borders copy() {
      return new Borders(this.top.copy(), this.bottom.copy(), this.left.copy(), this.right.copy());
   }
}
