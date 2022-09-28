package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class PageLayout {
   public static final int ORIENTATION_PORTRAIT = 0;
   public static final int ORIENTATION_LANDSCAPE = 1;
   public static final int COLOR_TRANSPARENT = -1;
   public static final int FORMAT_DECIMAL = 0;
   public static final int FORMAT_LOWERCASE_LETTER = 1;
   public static final int FORMAT_UPPERCASE_LETTER = 2;
   public static final int FORMAT_LOWERCASE_ROMAN = 3;
   public static final int FORMAT_UPPERCASE_ROMAN = 4;
   public static final int USAGE_ALL = 0;
   public static final int USAGE_LEFT = 1;
   public static final int USAGE_RIGHT = 2;
   public static final int USAGE_MIRRORED = 3;
   public static final int PAGE_NUMBER_CONTINUE = 0;
   public String name;
   public double width;
   public double height;
   public int orientation;
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
   public int columnCount;
   public double columnGap;
   public int firstPageNumber;
   public int numberFormat;
   public int pageUsage;
   public HeaderStyle header;
   public FooterStyle footer;

   public PageLayout() {
      this.width = 595.275590551181;
      this.height = 841.8897637795275;
      this.background = -1;
      this.columnCount = 1;
      this.firstPageNumber = 1;
   }

   public PageLayout(SimplePageMaster var1) {
      this();
      this.initialize(var1);
   }

   public void initialize(SimplePageMaster var1) {
      Region var2 = var1.regions[0];
      if (var1.pageWidth.type == 4) {
         this.width = var1.pageWidth.length();
      }

      if (var1.pageHeight.type == 4) {
         this.height = var1.pageHeight.length();
      }

      if (this.width > this.height) {
         this.orientation = 1;
      }

      this.marginTop = length(var1.marginTop);
      this.marginBottom = length(var1.marginBottom);
      this.marginLeft = length(var1.marginLeft);
      this.marginRight = length(var1.marginRight);
      if (var1.regions[1] == null) {
         this.marginTop += length(var2.marginTop);
      }

      if (var1.regions[2] == null) {
         this.marginBottom += length(var2.marginBottom);
      }

      this.marginLeft += length(var2.marginLeft);
      this.marginRight += length(var2.marginRight);
      this.paddingTop = length(var2.paddingTop);
      this.paddingBottom = length(var2.paddingBottom);
      this.paddingLeft = length(var2.paddingLeft);
      this.paddingRight = length(var2.paddingRight);
      Value var3 = var2.properties[8];
      if (var3.type == 24) {
         this.background = Odt.rgb(var3.color());
      }

      this.borders = new Borders(var2.properties);
      this.columnCount = var2.columnCount;
      this.columnGap = length(var2.columnGap);
      if (var1.regions[1] != null) {
         this.header = new HeaderStyle(var1.regions[1]);
         this.header.properties.marginBottom = this.paddingTop;
         this.paddingTop = length(var2.marginTop) - this.header.properties.height;
         if (this.paddingTop < 0.0) {
            this.paddingTop = 0.0;
         }
      }

      if (var1.regions[2] != null) {
         this.footer = new FooterStyle(var1.regions[2]);
         this.footer.properties.marginTop = this.paddingBottom;
         this.paddingBottom = length(var2.marginBottom) - this.footer.properties.height;
         if (this.paddingBottom < 0.0) {
            this.paddingBottom = 0.0;
         }
      }

   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   public void setPageNumbering(PageSequence var1) {
      char var2 = '1';
      if (var1.initialPageNumber > 0) {
         this.firstPageNumber = var1.initialPageNumber;
      } else {
         this.firstPageNumber = 0;
      }

      int var3 = 0;

      for(int var4 = var1.format.length(); var3 < var4; ++var3) {
         var2 = var1.format.charAt(var3);
         if (Character.isLetterOrDigit(var2)) {
            break;
         }
      }

      switch (var2) {
         case '0':
         case '1':
         default:
            this.numberFormat = 0;
            break;
         case 'A':
            this.numberFormat = 2;
            break;
         case 'I':
            this.numberFormat = 4;
            break;
         case 'a':
            this.numberFormat = 1;
            break;
         case 'i':
            this.numberFormat = 3;
      }

   }

   public double contentWidth() {
      double var1 = this.width;
      var1 -= this.marginLeft + this.marginRight;
      var1 -= this.paddingLeft + this.paddingRight;
      if (this.columnCount > 1) {
         var1 -= (double)(this.columnCount - 1) * this.columnGap;
         var1 /= (double)this.columnCount;
      }

      return var1;
   }

   public void print(PrintWriter var1) {
      var1.print("<style:page-layout style:name=\"" + this.name + "\"");
      var1.println(" style:page-usage=\"" + this.pageUsage() + "\">");
      var1.println("<style:page-layout-properties");
      var1.println(" fo:page-width=\"" + Odt.length(this.width, 1) + "\"");
      var1.println(" fo:page-height=\"" + Odt.length(this.height, 1) + "\"");
      var1.println(" style:print-orientation=\"" + this.orientation() + "\"");
      if (this.marginTop >= 0.0) {
         var1.println(" fo:margin-top=\"" + Odt.length(this.marginTop, 1) + "\"");
      }

      if (this.marginBottom >= 0.0) {
         var1.println(" fo:margin-bottom=\"" + Odt.length(this.marginBottom, 1) + "\"");
      }

      if (this.marginLeft >= 0.0) {
         var1.println(" fo:margin-left=\"" + Odt.length(this.marginLeft, 1) + "\"");
      }

      if (this.marginRight >= 0.0) {
         var1.println(" fo:margin-right=\"" + Odt.length(this.marginRight, 1) + "\"");
      }

      if (this.paddingTop > 0.0) {
         var1.println(" fo:padding-top=\"" + Odt.length(this.paddingTop, 1) + "\"");
      }

      if (this.paddingBottom > 0.0) {
         var1.println(" fo:padding-bottom=\"" + Odt.length(this.paddingBottom, 1) + "\"");
      }

      if (this.paddingLeft > 0.0) {
         var1.println(" fo:padding-left=\"" + Odt.length(this.paddingLeft, 1) + "\"");
      }

      if (this.paddingRight > 0.0) {
         var1.println(" fo:padding-right=\"" + Odt.length(this.paddingRight, 1) + "\"");
      }

      if (this.background != -1) {
         var1.println(" fo:background-color=\"" + Odt.color(this.background) + "\"");
      }

      if (this.borders != null) {
         this.borders.print(var1);
      }

      var1.println(" style:first-page-number=\"" + this.firstPageNumber() + "\"");
      var1.println(" style:num-format=\"" + this.numberFormat() + "\"");
      var1.println(">");
      if (this.columnCount > 1) {
         var1.println("<style:columns");
         var1.println(" fo:column-count=\"" + this.columnCount + "\"");
         var1.println(" fo:column-gap=\"" + Odt.length(this.columnGap, 1) + "\"");
         var1.println("/>");
      }

      var1.println("</style:page-layout-properties>");
      if (this.header != null) {
         this.header.print(var1);
      }

      if (this.footer != null) {
         this.footer.print(var1);
      }

      var1.println("</style:page-layout>");
   }

   private String orientation() {
      return this.orientation == 1 ? "landscape" : "portrait";
   }

   private String firstPageNumber() {
      return this.firstPageNumber == 0 ? "continue" : Integer.toString(this.firstPageNumber);
   }

   private char numberFormat() {
      char var1 = '1';
      switch (this.numberFormat) {
         case 1:
            var1 = 'a';
            break;
         case 2:
            var1 = 'A';
            break;
         case 3:
            var1 = 'i';
            break;
         case 4:
            var1 = 'I';
      }

      return var1;
   }

   private String pageUsage() {
      String var1 = "all";
      switch (this.pageUsage) {
         case 1:
            var1 = "left";
            break;
         case 2:
            var1 = "right";
            break;
         case 3:
            var1 = "mirrored";
      }

      return var1;
   }
}
