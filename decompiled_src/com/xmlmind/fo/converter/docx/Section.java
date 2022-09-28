package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import java.io.PrintWriter;

public final class Section {
   public double pageWidth;
   public double pageHeight;
   public double marginTop;
   public double marginBottom;
   public double marginLeft;
   public double marginRight;
   public double headerOffset;
   public double footerOffset;
   public int columnCount;
   public double columnGap;
   public double columnWidth;
   public double contentWidth;
   public Borders borders;
   public boolean titlePage;
   public boolean mirrorMargins;
   public boolean isLast;
   public char numberFormat;
   public int startPageNumber;
   public String oddHeaderId;
   public String evenHeaderId;
   public String firstHeaderId;
   public String oddFooterId;
   public String evenFooterId;
   public String firstFooterId;

   public Section() {
      this.headerOffset = 36.0;
      this.footerOffset = 36.0;
      this.numberFormat = '1';
   }

   public Section(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4) {
      this();
      this.initialize(var1, var2, var3, var4);
   }

   public void initialize(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4) {
      Region var5 = var1.regions[0];
      if (var1.pageWidth.type == 4) {
         this.pageWidth = var1.pageWidth.length();
      }

      if (var1.pageHeight.type == 4) {
         this.pageHeight = var1.pageHeight.length();
      }

      this.marginTop = length(var1.marginTop);
      this.marginBottom = length(var1.marginBottom);
      this.marginLeft = length(var1.marginLeft);
      this.marginRight = length(var1.marginRight);
      if (this.marginTop > 0.0) {
         this.headerOffset = this.marginTop;
      }

      if (this.marginBottom > 0.0) {
         this.footerOffset = this.marginBottom;
      }

      this.marginTop += length(var5.marginTop);
      this.marginBottom += length(var5.marginBottom);
      this.marginLeft += length(var5.marginLeft);
      this.marginRight += length(var5.marginRight);
      this.marginTop += length(var5.paddingTop);
      this.marginBottom += length(var5.paddingBottom);
      this.marginLeft += length(var5.paddingLeft);
      this.marginRight += length(var5.paddingRight);
      this.contentWidth = this.pageWidth - (this.marginLeft + this.marginRight);
      this.columnCount = var5.columnCount;
      this.columnGap = length(var5.columnGap);
      this.columnWidth = this.contentWidth;
      if (this.columnCount > 1) {
         this.columnWidth -= (double)(this.columnCount - 1) * this.columnGap;
         this.columnWidth /= (double)this.columnCount;
      }

      if (var2 != null) {
         double var6 = length(var2.marginLeft);
         double var8 = length(var2.marginRight);
         var5 = var2.regions[0];
         var6 += length(var5.marginLeft);
         var8 += length(var5.marginRight);
         var6 += length(var5.paddingLeft);
         var8 += length(var5.paddingRight);
         if (var6 == this.marginRight && var8 == this.marginLeft) {
            this.mirrorMargins = true;
         }
      }

      this.borders = new Borders(var5.properties);
      if (var3 != null) {
         this.titlePage = true;
      }

      if (var4.initialPageNumber > 0) {
         this.startPageNumber = var4.initialPageNumber;
      }

      int var10 = 0;

      for(int var7 = var4.format.length(); var10 < var7; ++var10) {
         char var11 = var4.format.charAt(var10);
         if (Character.isLetterOrDigit(var11)) {
            this.numberFormat = var11;
            break;
         }
      }

   }

   public void start(PrintWriter var1) {
   }

   public void end(PrintWriter var1) {
      if (!this.isLast) {
         var1.println("<w:p>");
         var1.println("<w:pPr>");
      }

      var1.println("<w:sectPr>");
      if (this.oddHeaderId != null) {
         var1.println("<w:headerReference w:type=\"default\" r:id=\"" + this.oddHeaderId + "\" />");
      }

      if (this.evenHeaderId != null) {
         var1.println("<w:headerReference w:type=\"even\" r:id=\"" + this.evenHeaderId + "\" />");
      }

      if (this.firstHeaderId != null) {
         var1.println("<w:headerReference w:type=\"first\" r:id=\"" + this.firstHeaderId + "\" />");
      }

      if (this.oddFooterId != null) {
         var1.println("<w:footerReference w:type=\"default\" r:id=\"" + this.oddFooterId + "\" />");
      }

      if (this.evenFooterId != null) {
         var1.println("<w:footerReference w:type=\"even\" r:id=\"" + this.evenFooterId + "\" />");
      }

      if (this.firstFooterId != null) {
         var1.println("<w:footerReference w:type=\"first\" r:id=\"" + this.firstFooterId + "\" />");
      }

      var1.print("<w:pgSz");
      var1.print(" w:w=\"" + Math.round(20.0 * this.pageWidth) + "\"");
      var1.print(" w:h=\"" + Math.round(20.0 * this.pageHeight) + "\"");
      if (this.pageWidth > this.pageHeight) {
         var1.print(" w:orient=\"landscape\"");
      }

      var1.println(" />");
      var1.print("<w:pgMar");
      var1.print(" w:top=\"" + Math.round(20.0 * this.marginTop) + "\"");
      var1.print(" w:bottom=\"" + Math.round(20.0 * this.marginBottom) + "\"");
      var1.print(" w:left=\"" + Math.round(20.0 * this.marginLeft) + "\"");
      var1.print(" w:right=\"" + Math.round(20.0 * this.marginRight) + "\"");
      var1.print(" w:header=\"" + Math.round(20.0 * this.headerOffset) + "\"");
      var1.print(" w:footer=\"" + Math.round(20.0 * this.footerOffset) + "\"");
      var1.print(" w:gutter=\"0\"");
      var1.println(" />");
      if (this.borders.materialized()) {
         var1.print("<w:pgBorders>");
         this.borders.print(var1);
         var1.println("</w:pgBorders>");
      }

      var1.print("<w:pgNumType");
      var1.print(" w:fmt=\"" + this.numberFormat() + "\"");
      if (this.startPageNumber > 0) {
         var1.print(" w:start=\"" + this.startPageNumber + "\"");
      }

      var1.println(" />");
      if (this.columnCount > 1) {
         var1.print("<w:cols");
         var1.print(" w:num=\"" + this.columnCount + "\"");
         var1.print(" w:space=\"" + Math.round(20.0 * this.columnGap) + "\"");
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

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   private String numberFormat() {
      String var1;
      switch (this.numberFormat) {
         case '0':
         case '1':
         default:
            var1 = "decimal";
            break;
         case 'A':
            var1 = "upperLetter";
            break;
         case 'I':
            var1 = "upperRoman";
            break;
         case 'a':
            var1 = "lowerLetter";
            break;
         case 'i':
            var1 = "lowerRoman";
      }

      return var1;
   }
}
