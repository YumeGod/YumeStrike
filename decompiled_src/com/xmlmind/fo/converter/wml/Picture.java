package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.converter.GraphicLayout;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.graphic.GraphicUtil;
import com.xmlmind.fo.objects.ExternalGraphic;
import com.xmlmind.fo.util.Base64;
import com.xmlmind.fo.util.Encoder;
import com.xmlmind.fo.util.StringUtil;
import com.xmlmind.fo.util.URLUtil;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

public class Picture {
   private int id;
   private Graphic graphic;
   private GraphicEnv graphicEnv;
   private RunProperties properties;
   private boolean prescaled;
   private int imageResolution;
   private int imageRendererResolution;
   private String[] graphicFormats;
   private Encoder encoder;
   private NumberFormat numberFormat;
   private double xScale = 1.0;
   private double yScale = 1.0;
   private Graphic picture;
   private GraphicLayout layout;
   private static final String[] graphicFormats1 = new String[]{"image/x-wmf", "image/x-emf", "image/jpeg", "image/png"};
   private static final String[] graphicFormats2 = new String[]{"image/x-wmf", "image/x-emf", "image/png"};

   public Picture(int var1, Graphic var2, GraphicEnv var3, RunProperties var4, boolean var5, int var6, int var7, boolean var8, Encoder var9) {
      this.id = var1;
      this.graphic = var2;
      this.graphicEnv = var3;
      this.properties = var4;
      this.prescaled = var5;
      this.imageResolution = var6;
      this.imageRendererResolution = var7;
      this.graphicFormats = var8 ? graphicFormats2 : graphicFormats1;
      this.encoder = var9;
      this.numberFormat = NumberFormat.getInstance(Locale.US);
      this.numberFormat.setGroupingUsed(false);
      this.numberFormat.setMinimumIntegerDigits(1);
   }

   public int width() {
      double var1 = GraphicLayout.width(this.graphic, this.imageResolution);
      if (var1 == 0.0) {
         var1 = GraphicLayout.intrinsicWidth(this.graphic, this.imageResolution);
      }

      if (this.properties != null && this.properties.border != null) {
         var1 += (double)(this.properties.border.width + this.properties.border.space) / 10.0;
      }

      return toTwips(var1);
   }

   public void layout(double var1) throws Exception {
      if (this.picture == null) {
         if (this.properties != null && this.properties.border != null) {
            var1 -= (double)(this.properties.border.width + this.properties.border.space) / 10.0;
         }

         this.layout = new GraphicLayout(this.graphic, this.imageResolution, var1);
         double var3 = GraphicLayout.intrinsicWidth(this.graphic, this.imageRendererResolution);
         double var5 = GraphicLayout.intrinsicHeight(this.graphic, this.imageRendererResolution);
         if (this.layout.contentWidth != var3) {
            this.xScale = this.layout.contentWidth / var3;
         }

         if (this.layout.contentHeight != var5) {
            this.yScale = this.layout.contentHeight / var5;
         }

         switch (this.graphic.getType()) {
            case 0:
               this.prescaled = false;
               break;
            case 2:
               this.prescaled = true;
               break;
            default:
               if (!(this.xScale < 1.0) || !(this.yScale < 1.0)) {
                  this.prescaled = false;
               }
         }

         if (!this.prescaled && StringUtil.contains(this.graphicFormats, this.graphic.getFormat())) {
            this.picture = this.graphic;
         } else {
            try {
               if (this.prescaled) {
                  this.picture = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, this.xScale, this.yScale, (Object)null, this.graphicEnv);
               } else {
                  this.picture = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, 1.0, 1.0, (Object)null, this.graphicEnv);
               }
            } catch (Exception var12) {
               this.graphicEnv.reportWarning("failed to convert image \"" + this.graphic.getLocation() + "\": " + var12.getMessage());
               this.picture = null;
               return;
            }
         }

         if (this.properties != null) {
            int var7 = 0;
            int var8 = (int)Math.round(2.0 * this.layout.viewportHeight);
            int var9 = this.properties.fontSize;
            int var10 = this.properties.fontSize / 4;
            int var11 = (var9 + var10) / 2 - var10;
            switch (this.properties.alignmentBaseline) {
               case 5:
               case 198:
                  var7 = -var10;
                  break;
               case 17:
               case 199:
                  var7 = -var8 + var9;
                  break;
               case 121:
                  var7 = -(var8 / 2) + var11;
            }

            this.properties.baselineShift = var7;
         }

      }
   }

   public void print(PrintWriter var1) throws Exception {
      if (this.picture != null) {
         var1.println("<w:r>");
         if (this.properties != null) {
            this.properties.print(var1);
         }

         this.printPicture(var1);
         var1.println("</w:r>");
      }
   }

   private void printPicture(PrintWriter var1) throws Exception {
      String var2 = "wordml://" + this.id + GraphicUtil.formatToSuffix(this.picture.getFormat());
      var1.println("<w:pict>");

      try {
         this.printImageData(var2, var1);
      } catch (Exception var17) {
         this.graphicEnv.reportWarning("failed to read image \"" + this.graphic.getLocation() + "\": " + var17.getMessage());
         var2 = null;
      }

      var1.print("<v:shape filled=\"f\"");
      String var3 = null;
      ExternalGraphic var4 = GraphicLayout.properties(this.graphic);
      if (var4 != null) {
         var3 = var4.role;
      }

      if (var3 != null && var3.length() > 0) {
         var1.print(" alt=\"");
         var1.print(Wml.escape(var3, this.encoder));
         var1.print("\"");
      }

      var1.print(" style=\"");
      var1.print("width:" + this.format(this.layout.viewportWidth, 2) + "pt;");
      var1.print("height:" + this.format(this.layout.viewportHeight, 2) + "pt");
      var1.println("\">");
      if (var2 != null) {
         var1.print("<v:imagedata src=\"" + var2 + "\"");
         if (this.layout.contentWidth != this.layout.viewportWidth || this.layout.contentHeight != this.layout.viewportHeight) {
            double var5 = 0.0;
            double var7 = 0.0;
            double var9 = 0.0;
            double var11 = 0.0;
            double var13;
            if (this.layout.contentWidth < this.layout.viewportWidth) {
               var5 = -this.layout.contentX;
               var13 = this.layout.contentX + this.layout.contentWidth;
               var9 = -(this.layout.viewportWidth - var13);
            } else if (this.layout.contentWidth > this.layout.viewportWidth) {
               var5 = this.layout.cropX;
               var13 = this.layout.cropX + this.layout.viewportWidth;
               var9 = this.layout.contentWidth - var13;
            }

            var5 /= this.layout.contentWidth;
            var9 /= this.layout.contentWidth;
            double var15;
            if (this.layout.contentHeight < this.layout.viewportHeight) {
               var7 = -this.layout.contentY;
               var15 = this.layout.contentY + this.layout.contentHeight;
               var11 = -(this.layout.viewportHeight - var15);
            } else if (this.layout.contentHeight > this.layout.viewportHeight) {
               var7 = this.layout.cropY;
               var15 = this.layout.cropY + this.layout.viewportHeight;
               var11 = this.layout.contentHeight - var15;
            }

            var7 /= this.layout.contentHeight;
            var11 /= this.layout.contentHeight;
            var1.print(" cropleft=\"" + this.format(var5, 2) + "\"");
            var1.print(" croptop=\"" + this.format(var7, 2) + "\"");
            var1.print(" cropright=\"" + this.format(var9, 2) + "\"");
            var1.print(" cropbottom=\"" + this.format(var11, 2) + "\"");
         }

         var1.println(" />");
      }

      var1.println("</v:shape>");
      var1.println("</w:pict>");
   }

   private void printImageData(String var1, PrintWriter var2) throws Exception {
      var2.print("<w:binData w:name=\"" + var1 + "\">");
      var2.print("\r\n");
      InputStream var3 = URLUtil.openStream(this.picture.getLocation());

      try {
         var2.flush();
         Base64.OutputStream var4 = new Base64.OutputStream(var2);
         byte[] var5 = new byte[8192];

         while(true) {
            int var6 = var3.read(var5);
            if (var6 < 0) {
               var4.finish();
               break;
            }

            if (var6 > 0) {
               var4.write(var5, 0, var6);
            }
         }
      } finally {
         var3.close();
      }

      var2.println("</w:binData>");
   }

   private String format(double var1, int var3) {
      this.numberFormat.setMaximumFractionDigits(var3);
      this.numberFormat.setMinimumFractionDigits(var3);
      return this.numberFormat.format(var1);
   }

   private static int toTwips(double var0) {
      return (int)Math.round(var0 * 20.0);
   }
}
