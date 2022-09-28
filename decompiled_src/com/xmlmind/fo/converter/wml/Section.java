package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class Section {
   public static final String FORMAT_DECIMAL = "decimal";
   public static final String FORMAT_LOWERCASE_LETTER = "lower-letter";
   public static final String FORMAT_UPPERCASE_LETTER = "upper-letter";
   public static final String FORMAT_LOWERCASE_ROMAN = "lower-roman";
   public static final String FORMAT_UPPERCASE_ROMAN = "upper-roman";
   public int pageWidth;
   public int pageHeight;
   public int marginTop;
   public int marginBottom;
   public int marginLeft;
   public int marginRight;
   public int headerOffset;
   public int footerOffset;
   public int columnCount;
   public int columnGap;
   public int columnWidth;
   public int contentWidth;
   public Borders borders;
   public boolean titlePage;
   public boolean mirrorMargins;
   public boolean isLast;
   public int startPageNumber;
   public String numberFormat;
   public Header headerOdd;
   public Header headerEven;
   public Header headerFirst;
   public Footer footerOdd;
   public Footer footerEven;
   public Footer footerFirst;

   public Section() {
      this.headerOffset = 720;
      this.footerOffset = 720;
   }

   public Section(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4) {
      this();
      this.initialize(var1, var2, var3, var4);
   }

   public void initialize(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4) {
      char var5 = '1';
      Region var6 = var1.regions[0];
      if (var1.pageWidth.type == 4) {
         this.pageWidth = Wml.toTwips(var1.pageWidth.length());
      }

      if (var1.pageHeight.type == 4) {
         this.pageHeight = Wml.toTwips(var1.pageHeight.length());
      }

      this.marginTop = length(var1.marginTop);
      this.marginBottom = length(var1.marginBottom);
      this.marginLeft = length(var1.marginLeft);
      this.marginRight = length(var1.marginRight);
      if (this.marginTop > 0) {
         this.headerOffset = this.marginTop;
      }

      if (this.marginBottom > 0) {
         this.footerOffset = this.marginBottom;
      }

      this.marginTop += length(var6.marginTop);
      this.marginBottom += length(var6.marginBottom);
      this.marginLeft += length(var6.marginLeft);
      this.marginRight += length(var6.marginRight);
      this.marginTop += length(var6.paddingTop);
      this.marginBottom += length(var6.paddingBottom);
      this.marginLeft += length(var6.paddingLeft);
      this.marginRight += length(var6.paddingRight);
      this.contentWidth = this.pageWidth - (this.marginLeft + this.marginRight);
      this.columnCount = var6.columnCount;
      this.columnGap = length(var6.columnGap);
      this.columnWidth = this.contentWidth;
      if (this.columnCount > 1) {
         this.columnWidth -= (this.columnCount - 1) * this.columnGap;
         this.columnWidth /= this.columnCount;
      }

      int var7;
      int var8;
      if (var2 != null) {
         var7 = length(var2.marginLeft);
         var8 = length(var2.marginRight);
         var6 = var2.regions[0];
         var7 += length(var6.marginLeft);
         var8 += length(var6.marginRight);
         var7 += length(var6.paddingLeft);
         var8 += length(var6.paddingRight);
         if (var7 == this.marginRight && var8 == this.marginLeft) {
            this.mirrorMargins = true;
         }
      }

      this.borders = new Borders(var6.properties);
      if (var3 != null) {
         this.titlePage = true;
      }

      if (var4.initialPageNumber > 0) {
         this.startPageNumber = var4.initialPageNumber;
      }

      var7 = 0;

      for(var8 = var4.format.length(); var7 < var8; ++var7) {
         var5 = var4.format.charAt(var7);
         if (Character.isLetterOrDigit(var5)) {
            break;
         }
      }

      switch (var5) {
         case '0':
         case '1':
         default:
            this.numberFormat = "decimal";
            break;
         case 'A':
            this.numberFormat = "upper-letter";
            break;
         case 'I':
            this.numberFormat = "upper-roman";
            break;
         case 'a':
            this.numberFormat = "lower-letter";
            break;
         case 'i':
            this.numberFormat = "lower-roman";
      }

   }

   public void start(PrintWriter var1, Encoder var2) {
   }

   public void end(PrintWriter var1, Encoder var2) throws Exception {
      if (!this.isLast) {
         var1.println("<w:p>");
         var1.println("<w:pPr>");
      }

      var1.println("<w:sectPr>");
      if (this.headerOdd != null) {
         this.headerOdd.print(var1, var2);
      }

      if (this.headerEven != null) {
         this.headerEven.print(var1, var2);
      }

      if (this.headerFirst != null) {
         this.headerFirst.print(var1, var2);
      }

      if (this.footerOdd != null) {
         this.footerOdd.print(var1, var2);
      }

      if (this.footerEven != null) {
         this.footerEven.print(var1, var2);
      }

      if (this.footerFirst != null) {
         this.footerFirst.print(var1, var2);
      }

      var1.print("<w:pgSz");
      var1.print(" w:w=\"" + this.pageWidth + "\"");
      var1.print(" w:h=\"" + this.pageHeight + "\"");
      if (this.pageWidth > this.pageHeight) {
         var1.print(" w:orient=\"landscape\"");
      }

      var1.println(" />");
      var1.print("<w:pgMar");
      var1.print(" w:top=\"" + this.marginTop + "\"");
      var1.print(" w:bottom=\"" + this.marginBottom + "\"");
      var1.print(" w:left=\"" + this.marginLeft + "\"");
      var1.print(" w:right=\"" + this.marginRight + "\"");
      var1.print(" w:header=\"" + this.headerOffset + "\"");
      var1.print(" w:footer=\"" + this.footerOffset + "\"");
      var1.print(" w:gutter=\"0\"");
      var1.println(" />");
      if (this.borders.materialized()) {
         var1.print("<w:pgBorders>");
         this.borders.print(var1);
         var1.println("</w:pgBorders>");
      }

      var1.print("<w:pgNumType");
      var1.print(" w:fmt=\"" + this.numberFormat + "\"");
      if (this.startPageNumber > 0) {
         var1.print(" w:start=\"" + this.startPageNumber + "\"");
      }

      var1.println(" />");
      if (this.columnCount > 1) {
         var1.print("<w:cols");
         var1.print(" w:num=\"" + this.columnCount + "\"");
         var1.print(" w:space=\"" + this.columnGap + "\"");
         var1.println(" />");
      }

      if (this.titlePage) {
         var1.println("<w:titlePg />");
      }

      var1.println("</w:sectPr>");
      if (!this.isLast) {
         var1.println("</w:pPr>");
         var1.println("</w:p>");
      }

   }

   private static int length(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Wml.toTwips(var0.length());
      }

      return var1;
   }
}
