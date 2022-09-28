package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.GraphicLayout;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.graphic.GraphicUtil;
import com.xmlmind.fo.objects.ExternalGraphic;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import com.xmlmind.fo.util.URLUtil;
import java.io.PrintWriter;

public final class Image {
   private int id;
   private Graphic graphic;
   private OdtGraphicEnv graphicEnv;
   private boolean prescaled;
   private int imageResolution;
   private int imageRendererResolution;
   private String[] graphicFormats;
   private Encoder encoder;
   private GraphicStyle style;
   private double xScale = 1.0;
   private double yScale = 1.0;
   private Graphic image;
   private String imageName;
   private GraphicLayout layout;
   private static final String[] graphicFormats1 = new String[]{"image/x-wmf", "image/x-emf", "image/jpeg", "image/png"};
   private static final String[] graphicFormats2 = new String[]{"image/x-wmf", "image/x-emf", "image/png"};

   public Image(int var1, Graphic var2, OdtGraphicEnv var3, Context var4, boolean var5, int var6, int var7, boolean var8, Encoder var9) {
      this.id = var1;
      this.graphic = var2;
      this.graphicEnv = var3;
      this.prescaled = var5;
      this.imageResolution = var6;
      this.imageRendererResolution = var7;
      this.graphicFormats = var8 ? graphicFormats2 : graphicFormats1;
      this.encoder = var9;
      this.style = style(var4);
   }

   private static GraphicStyle style(Context var0) {
      GraphicStyle var1 = new GraphicStyle();
      Value[] var2 = var0.properties.values;
      var1.anchorType = 4;
      switch (var2[3].keyword()) {
         case 5:
         case 198:
            var1.verticalPosition = 2;
            var1.verticalRelation = 9;
            break;
         case 17:
         case 199:
            var1.verticalPosition = 0;
            var1.verticalRelation = 9;
            break;
         case 121:
            var1.verticalPosition = 1;
            var1.verticalRelation = 9;
            break;
         default:
            var1.verticalPosition = 0;
            var1.verticalRelation = 8;
      }

      var1.borders = new Borders(var2);
      var1.paddingTop = length(var2[208]);
      var1.paddingBottom = length(var2[199]);
      var1.paddingLeft = length(var2[203]);
      var1.paddingRight = length(var2[204]);
      if (var0.background != null) {
         var1.background = Odt.rgb(var0.background);
      }

      return var1;
   }

   private static double length(Value var0) {
      double var1 = 0.0;
      if (var0.type == 4) {
         var1 = var0.length();
      }

      return var1;
   }

   public double width() {
      double var1 = GraphicLayout.width(this.graphic, this.imageResolution);
      if (var1 == 0.0) {
         var1 = GraphicLayout.intrinsicWidth(this.graphic, this.imageResolution);
      }

      var1 += this.style.borders.left.width + this.style.paddingLeft;
      var1 += this.style.borders.right.width + this.style.paddingRight;
      return var1;
   }

   public void layout(double var1) throws Exception {
      if (this.image == null) {
         var1 -= this.style.borders.left.width + this.style.paddingLeft;
         var1 -= this.style.borders.right.width + this.style.paddingRight;
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

         try {
            if (this.prescaled) {
               this.image = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, this.xScale, this.yScale, (Object)null, this.graphicEnv);
            } else {
               this.image = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, 1.0, 1.0, (Object)null, this.graphicEnv);
            }
         } catch (Exception var20) {
            this.graphicEnv.reportWarning("failed to convert image \"" + this.graphic.getLocation() + "\": " + var20.getMessage());
            return;
         }

         this.imageName = Odt.imageName(this.id, GraphicUtil.formatToSuffix(this.image.getFormat()));
         OdtEntry var7 = new OdtEntry(this.imageName, this.image.getFormat(), URLUtil.locationToFilename(this.image.getLocation()));
         this.graphicEnv.fileEntries.addElement(var7);
         if (this.layout.contentWidth != this.layout.viewportWidth || this.layout.contentHeight != this.layout.viewportHeight) {
            double var8 = 0.0;
            double var10 = 0.0;
            double var12 = 0.0;
            double var14 = 0.0;
            double var16;
            if (this.layout.contentWidth < this.layout.viewportWidth) {
               var8 = -this.layout.contentX;
               var16 = this.layout.contentX + this.layout.contentWidth;
               var12 = -(this.layout.viewportWidth - var16);
            } else if (this.layout.contentWidth > this.layout.viewportWidth) {
               var8 = this.layout.cropX;
               var16 = this.layout.cropX + this.layout.viewportWidth;
               var12 = this.layout.contentWidth - var16;
            }

            double var18;
            if (this.layout.contentHeight < this.layout.viewportHeight) {
               var10 = -this.layout.contentY;
               var18 = this.layout.contentY + this.layout.contentHeight;
               var14 = -(this.layout.viewportHeight - var18);
            } else if (this.layout.contentHeight > this.layout.viewportHeight) {
               var10 = this.layout.cropY;
               var18 = this.layout.cropY + this.layout.viewportHeight;
               var14 = this.layout.contentHeight - var18;
            }

            if (!this.prescaled) {
               var8 /= this.xScale;
               var12 /= this.xScale;
               var10 /= this.yScale;
               var14 /= this.yScale;
            }

            this.style.clipTop = var10;
            this.style.clipBottom = var14;
            this.style.clipLeft = var8;
            this.style.clipRight = var12;
         }

         this.style = this.graphicEnv.styleTable.add(this.style);
      }
   }

   public void print(PrintWriter var1) {
      if (this.image != null) {
         double var2 = this.layout.viewportWidth;
         double var4 = this.layout.viewportHeight;
         var2 += this.style.paddingLeft + this.style.paddingRight;
         var4 += this.style.paddingTop + this.style.paddingBottom;
         var1.print("<draw:frame");
         var1.println(" draw:name=\"image" + this.id + "\"");
         var1.println(" draw:style-name=\"" + this.style.name + "\"");
         var1.println(" svg:width=\"" + Odt.length(var2, 1) + "\"");
         var1.println(" svg:height=\"" + Odt.length(var4, 1) + "\"");
         var1.print(">");
         var1.println("<draw:image");
         var1.println(" xlink:href=\"" + this.imageName + "\"");
         var1.print("/>");
         String var6 = null;
         ExternalGraphic var7 = GraphicLayout.properties(this.graphic);
         if (var7 != null) {
            var6 = var7.role;
         }

         if (var6 != null && var6.length() > 0) {
            var1.print("<svg:title>");
            var1.print(Odt.escape(var6, this.encoder));
            var1.print("</svg:title>");
         }

         var1.println("</draw:frame");
         var1.print(">");
      }
   }
}
