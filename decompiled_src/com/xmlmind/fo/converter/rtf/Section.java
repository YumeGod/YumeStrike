package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import java.io.PrintWriter;

public class Section {
   public static final String FORMAT_DECIMAL = "dec";
   public static final String FORMAT_LOWERCASE_LETTER = "lcltr";
   public static final String FORMAT_UPPERCASE_LETTER = "ucltr";
   public static final String FORMAT_LOWERCASE_ROMAN = "lcrm";
   public static final String FORMAT_UPPERCASE_ROMAN = "ucrm";
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
   public int startPageNumber;
   public String numberFormat;
   public Header headerOdd;
   public Header headerEven;
   public Header headerFirst;
   public Footer footerOdd;
   public Footer footerEven;
   public Footer footerFirst;

   public Section() {
   }

   public Section(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4, ColorTable var5) {
      this();
      this.initialize(var1, var2, var3, var4, var5);
   }

   public void initialize(SimplePageMaster var1, SimplePageMaster var2, SimplePageMaster var3, PageSequence var4, ColorTable var5) {
      char var6 = '1';
      Region var7 = var1.regions[0];
      if (var1.pageWidth.type == 4) {
         this.pageWidth = Rtf.toTwips(var1.pageWidth.length());
      }

      if (var1.pageHeight.type == 4) {
         this.pageHeight = Rtf.toTwips(var1.pageHeight.length());
      }

      this.marginTop = length(var1.marginTop);
      this.marginBottom = length(var1.marginBottom);
      this.marginLeft = length(var1.marginLeft);
      this.marginRight = length(var1.marginRight);
      this.headerOffset = this.marginTop;
      this.footerOffset = this.marginBottom;
      this.marginTop += length(var7.marginTop);
      this.marginBottom += length(var7.marginBottom);
      this.marginLeft += length(var7.marginLeft);
      this.marginRight += length(var7.marginRight);
      this.marginTop += length(var7.paddingTop);
      this.marginBottom += length(var7.paddingBottom);
      this.marginLeft += length(var7.paddingLeft);
      this.marginRight += length(var7.paddingRight);
      this.contentWidth = this.pageWidth - (this.marginLeft + this.marginRight);
      this.columnCount = var7.columnCount;
      this.columnGap = length(var7.columnGap);
      this.columnWidth = this.contentWidth;
      if (this.columnCount > 1) {
         this.columnWidth -= (this.columnCount - 1) * this.columnGap;
         this.columnWidth /= this.columnCount;
      }

      this.borders = new Borders(3);
      this.borders.initialize(var7.properties, var5);
      int var8;
      int var9;
      if (var2 != null) {
         var8 = length(var2.marginLeft);
         var9 = length(var2.marginRight);
         var7 = var2.regions[0];
         var8 += length(var7.marginLeft);
         var9 += length(var7.marginRight);
         var8 += length(var7.paddingLeft);
         var9 += length(var7.paddingRight);
         if (var8 == this.marginRight && var9 == this.marginLeft) {
            this.mirrorMargins = true;
         }
      }

      if (var3 != null) {
         this.titlePage = true;
      }

      if (var4.initialPageNumber > 0) {
         this.startPageNumber = var4.initialPageNumber;
      }

      var8 = 0;

      for(var9 = var4.format.length(); var8 < var9; ++var8) {
         var6 = var4.format.charAt(var8);
         if (Character.isLetterOrDigit(var6)) {
            break;
         }
      }

      switch (var6) {
         case '0':
         case '1':
         default:
            this.numberFormat = "dec";
            break;
         case 'A':
            this.numberFormat = "ucltr";
            break;
         case 'I':
            this.numberFormat = "ucrm";
            break;
         case 'a':
            this.numberFormat = "lcltr";
            break;
         case 'i':
            this.numberFormat = "lcrm";
      }

   }

   public void start(PrintWriter var1, Encoder var2, int var3) throws Exception {
      var1.println("\\sectd");
      var1.println("\\pghsxn" + this.pageHeight + "\\pgwsxn" + this.pageWidth);
      if (this.pageWidth > this.pageHeight) {
         var1.println("\\lndscpsxn");
      }

      var1.print("\\margtsxn" + this.marginTop + "\\margbsxn" + this.marginBottom);
      var1.println("\\marglsxn" + this.marginLeft + "\\margrsxn" + this.marginRight);
      if (this.mirrorMargins) {
         var1.println("\\margmirsxn");
      }

      if (this.headerOffset > 0) {
         var1.println("\\headery" + this.headerOffset);
      }

      if (this.footerOffset > 0) {
         var1.println("\\footery" + this.footerOffset);
      }

      if (this.titlePage) {
         var1.println("\\titlepg");
      }

      if (this.columnCount > 1) {
         var1.println("\\cols" + this.columnCount + "\\colsx" + this.columnGap);
      }

      if (this.borders.materialized()) {
         this.borders.print(var1);
         var1.println();
      }

      if (this.startPageNumber > 0) {
         var1.print("\\pgnrestart\\pgnstarts" + this.startPageNumber);
      } else {
         var1.print("\\pgncont");
      }

      var1.println("\\pgn" + this.numberFormat);
      if (this.headerOdd != null) {
         this.headerOdd.print(var1, var2, var3);
      }

      if (this.headerEven != null) {
         this.headerEven.print(var1, var2, var3);
      }

      if (this.headerFirst != null) {
         this.headerFirst.print(var1, var2, var3);
      }

      if (this.footerOdd != null) {
         this.footerOdd.print(var1, var2, var3);
      }

      if (this.footerEven != null) {
         this.footerEven.print(var1, var2, var3);
      }

      if (this.footerFirst != null) {
         this.footerFirst.print(var1, var2, var3);
      }

   }

   public void end(PrintWriter var1) {
      var1.println("\\pard\\sect");
   }

   private static int length(Value var0) {
      int var1 = 0;
      if (var0.type == 4) {
         var1 = Rtf.toTwips(var0.length());
      }

      return var1;
   }
}
