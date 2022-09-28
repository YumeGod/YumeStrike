package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.GraphicLayout;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.util.StringUtil;
import com.xmlmind.fo.util.URLUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Picture {
   private Graphic graphic;
   private GraphicEnv graphicEnv;
   private RunProperties properties;
   private ColorTable colors;
   private boolean prescaled;
   private int imageResolution;
   private int imageRendererResolution;
   private String[] graphicFormats;
   private double xScale = 1.0;
   private double yScale = 1.0;
   private Graphic picture;
   private GraphicLayout layout;
   private static final String[] graphicFormats1 = new String[]{"image/x-wmf", "image/x-emf", "image/jpeg", "image/png"};
   private static final String[] graphicFormats2 = new String[]{"image/x-wmf", "image/x-emf", "image/png"};

   public Picture(Graphic var1, GraphicEnv var2, RunProperties var3, ColorTable var4, boolean var5, int var6, int var7, boolean var8) {
      this.graphic = var1;
      this.graphicEnv = var2;
      this.properties = var3;
      this.colors = var4;
      this.prescaled = var5;
      this.imageResolution = var6;
      this.imageRendererResolution = var7;
      this.graphicFormats = var8 ? graphicFormats2 : graphicFormats1;
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

            if (var7 != 0) {
               if (var7 < 0) {
                  this.properties.subscript = true;
               } else {
                  this.properties.superscript = true;
               }

               this.properties.baselineShift = Math.abs(var7);
            }
         }

      }
   }

   public void print(PrintWriter var1) throws Exception {
      if (this.picture != null) {
         var1.print("{");
         if (this.properties != null) {
            this.properties.print(var1);
         }

         var1.println();
         this.printPicture(var1);
         var1.println("}");
      }
   }

   private void printPicture(PrintWriter var1) throws Exception {
      int var2;
      int var3;
      if (this.prescaled) {
         var2 = 100;
         var3 = 100;
      } else {
         var2 = (int)Math.round(this.xScale * 100.0);
         var3 = (int)Math.round(this.yScale * 100.0);
      }

      var1.print("{\\pict");
      String var4 = this.picture.getFormat();
      byte var5 = 0;
      if ("image/x-wmf".equals(var4)) {
         var1.print("\\wmetafile8");
         int var6 = (int)Math.rint((double)this.picture.getWidth() / this.picture.getXResolution() * 2540.0);
         int var7 = (int)Math.rint((double)this.picture.getHeight() / this.picture.getYResolution() * 2540.0);
         var1.print("\\picw" + var6);
         var1.print("\\pich" + var7);
         int var8 = (int)Math.rint(this.picture.getXResolution());
         var1.print("\\blipupi" + var8);
         var5 = 22;
      } else if ("image/x-emf".equals(var4)) {
         var1.print("\\emfblip");
      } else {
         var1.print("\\pngblip");
      }

      var1.print("\\picscalex" + var2);
      var1.println("\\picscaley" + var3);
      if (this.layout.contentWidth != this.layout.viewportWidth || this.layout.contentHeight != this.layout.viewportHeight) {
         double var23 = 0.0;
         double var24 = 0.0;
         double var10 = 0.0;
         double var12 = 0.0;
         double var18;
         if (this.layout.contentWidth < this.layout.viewportWidth) {
            var23 = -this.layout.contentX;
            var18 = this.layout.contentX + this.layout.contentWidth;
            var10 = -(this.layout.viewportWidth - var18);
         } else if (this.layout.contentWidth > this.layout.viewportWidth) {
            var23 = this.layout.cropX;
            var18 = this.layout.cropX + this.layout.viewportWidth;
            var10 = this.layout.contentWidth - var18;
         }

         double var20;
         if (this.layout.contentHeight < this.layout.viewportHeight) {
            var24 = -this.layout.contentY;
            var20 = this.layout.contentY + this.layout.contentHeight;
            var12 = -(this.layout.viewportHeight - var20);
         } else if (this.layout.contentHeight > this.layout.viewportHeight) {
            var24 = this.layout.cropY;
            var20 = this.layout.cropY + this.layout.viewportHeight;
            var12 = this.layout.contentHeight - var20;
         }

         double var14;
         double var16;
         if (this.prescaled) {
            var14 = this.layout.contentWidth;
            var16 = this.layout.contentHeight;
         } else {
            var14 = GraphicLayout.intrinsicWidth(this.graphic, this.imageRendererResolution);
            var16 = GraphicLayout.intrinsicHeight(this.graphic, this.imageRendererResolution);
            var23 /= this.xScale;
            var10 /= this.xScale;
            var24 /= this.yScale;
            var12 /= this.yScale;
         }

         var1.print("\\picwgoal" + toTwips(var14));
         var1.print("\\pichgoal" + toTwips(var16));
         var1.print("\\piccropl" + toTwips(var23));
         var1.print("\\piccropt" + toTwips(var24));
         var1.print("\\piccropr" + toTwips(var10));
         var1.println("\\piccropb" + toTwips(var12));
      }

      try {
         printImageData(this.picture, var5, var1);
      } catch (Exception var22) {
         this.graphicEnv.reportWarning("failed to read image \"" + this.picture.getLocation() + "\": " + var22.getMessage());
      }

      var1.println("}");
   }

   private static int toTwips(double var0) {
      return (int)Math.round(var0 * 20.0);
   }

   private static void printImageData(Graphic var0, int var1, PrintWriter var2) throws IOException {
      BufferedInputStream var3 = new BufferedInputStream(URLUtil.openStream(var0.getLocation()));

      try {
         if (var1 > 0) {
            var3.skip((long)var1);
         }

         byte[] var4 = new byte[256];
         StringBuffer var5 = new StringBuffer(2 * var4.length);

         while(true) {
            int var6 = var3.read(var4);
            if (var6 < 0) {
               return;
            }

            var5.setLength(0);

            for(int var7 = 0; var7 < var6; ++var7) {
               byte var8 = var4[var7];
               var5.append(Integer.toHexString(var8 >> 4 & 15));
               var5.append(Integer.toHexString(var8 & 15));
            }

            var2.println(var5.toString());
         }
      } finally {
         var3.close();
      }
   }
}
