package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class HeaderFooterProperties {
   public static final int COLOR_TRANSPARENT = -1;
   public double height;
   public boolean minHeight;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public double paddingTop;
   public double paddingBottom;
   public double paddingLeft;
   public double paddingRight;
   public int background;
   public Borders borders;

   public HeaderFooterProperties() {
      this.height = 28.346456692913385;
      this.minHeight = true;
      this.background = -1;
   }

   public HeaderFooterProperties(Region var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(Region var1) {
      this.height = length(var1.properties[99]);
      this.paddingTop = length(var1.paddingTop);
      this.paddingBottom = length(var1.paddingBottom);
      this.paddingLeft = length(var1.paddingLeft);
      this.paddingRight = length(var1.paddingRight);
      Value var2 = var1.properties[8];
      if (var2.type == 24) {
         this.background = Odt.rgb(var2.color());
      }

      this.borders = new Borders(var1.properties);
   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.println("<style:header-footer-properties");
      if (this.minHeight) {
         var1.println(" fo:min-height=\"" + Odt.length(this.height, 1) + "\"");
      } else {
         var1.println(" svg:height=\"" + Odt.length(this.height, 1) + "\"");
      }

      if (this.marginTop != 0.0) {
         var1.println(" fo:margin-top=\"" + Odt.length(this.marginTop, 1) + "\"");
      }

      if (this.marginBottom != 0.0) {
         var1.println(" fo:margin-bottom=\"" + Odt.length(this.marginBottom, 1) + "\"");
      }

      if (this.marginLeft != 0.0) {
         var1.println(" fo:margin-left=\"" + Odt.length(this.marginLeft, 1) + "\"");
      }

      if (this.marginRight != 0.0) {
         var1.println(" fo:margin-right=\"" + Odt.length(this.marginRight, 1) + "\"");
      }

      if (this.paddingTop != 0.0) {
         var1.println(" fo:padding-top=\"" + Odt.length(this.paddingTop, 1) + "\"");
      }

      if (this.paddingBottom != 0.0) {
         var1.println(" fo:padding-bottom=\"" + Odt.length(this.paddingBottom, 1) + "\"");
      }

      if (this.paddingLeft != 0.0) {
         var1.println(" fo:padding-left=\"" + Odt.length(this.paddingLeft, 1) + "\"");
      }

      if (this.paddingRight != 0.0) {
         var1.println(" fo:padding-right=\"" + Odt.length(this.paddingRight, 1) + "\"");
      }

      if (this.background != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.background) + "\"");
      }

      if (this.borders != null) {
         this.borders.print(var1);
      }

      var1.println("/>");
   }
}
