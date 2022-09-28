package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.GraphicLayout;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.objects.ExternalGraphic;
import com.xmlmind.fo.util.URLUtil;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

public class Picture {
   private Graphic graphic;
   private DocxGraphicEnv graphicEnv;
   private RunProperties properties;
   private boolean prescaled;
   private int imageResolution;
   private int imageRendererResolution;
   private String[] graphicFormats;
   private boolean useVML;
   private double xScale = 1.0;
   private double yScale = 1.0;
   private Graphic picture;
   private GraphicLayout layout;
   private int index;
   private String id;
   private static final String[] graphicFormats1 = new String[]{"image/x-wmf", "image/x-emf", "image/jpeg", "image/png"};
   private static final String[] graphicFormats2 = new String[]{"image/x-wmf", "image/x-emf", "image/png"};
   private NumberFormat numberFormat;

   public Picture(Graphic var1, DocxGraphicEnv var2, RunProperties var3, boolean var4, int var5, int var6, boolean var7, boolean var8) {
      this.graphic = var1;
      this.graphicEnv = var2;
      this.properties = var3;
      this.prescaled = var4;
      this.imageResolution = var5;
      this.imageRendererResolution = var6;
      this.graphicFormats = var7 ? graphicFormats2 : graphicFormats1;
      this.useVML = var8;
      this.numberFormat = NumberFormat.getInstance(Locale.US);
      this.numberFormat.setGroupingUsed(false);
      this.numberFormat.setMinimumIntegerDigits(1);
   }

   public double width() {
      double var1 = GraphicLayout.width(this.graphic, this.imageResolution);
      if (var1 == 0.0) {
         var1 = GraphicLayout.intrinsicWidth(this.graphic, this.imageResolution);
      }

      if (this.properties != null && this.properties.border != null) {
         var1 += (this.properties.border.width + this.properties.border.space) * 2.0;
      }

      return var1;
   }

   public void layout(double var1) throws Exception {
      if (this.id == null) {
         if (this.properties != null && this.properties.border != null) {
            var1 -= (this.properties.border.width + this.properties.border.space) * 2.0;
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

         try {
            if (this.prescaled) {
               this.picture = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, this.xScale, this.yScale, (Object)null, this.graphicEnv);
            } else {
               this.picture = GraphicFactories.convertGraphic(this.graphic, (String[])this.graphicFormats, 1.0, 1.0, (Object)null, this.graphicEnv);
            }
         } catch (Exception var14) {
            this.graphicEnv.reportWarning("failed to convert image \"" + this.graphic.getLocation() + "\": " + var14.getMessage());
            return;
         }

         this.index = this.graphicEnv.images.count();
         String var7 = this.graphicEnv.images.add(URLUtil.locationToFilename(this.picture.getLocation()), this.picture.getFormat());
         this.id = this.graphicEnv.relationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", var7);
         if (this.properties != null) {
            double var8 = this.properties.fontSize;
            double var10 = this.properties.fontSize / 4.0;
            double var12 = (var8 + var10) / 2.0 - var10;
            switch (this.properties.alignmentBaseline) {
               case 5:
               case 198:
                  this.properties.baselineShift = -var10;
                  break;
               case 17:
               case 199:
                  this.properties.baselineShift = -this.layout.viewportHeight + var8;
                  break;
               case 121:
                  this.properties.baselineShift = -(this.layout.viewportHeight / 2.0) + var12;
            }
         }

      }
   }

   public void print(PrintWriter var1) {
      if (this.id != null) {
         var1.println("<w:r>");
         if (this.properties != null) {
            this.properties.print(var1);
         }

         if (this.useVML) {
            this.printVMLPicture(var1);
         } else {
            this.printPicture(var1);
         }

         var1.println("</w:r>");
      }
   }

   private void printPicture(PrintWriter var1) {
      var1.println("<w:drawing>");
      var1.println("<wp:inline>");
      var1.print("<wp:extent cx=\"");
      var1.print(ptToEMU(this.layout.viewportWidth));
      var1.print("\" cy=\"");
      var1.print(ptToEMU(this.layout.viewportHeight));
      var1.println("\"/>");
      String var2 = this.name();
      String var3 = this.role();
      var1.print("<wp:docPr id=\"");
      var1.print(this.graphicEnv.images.getNextId());
      var1.print("\" name=\"");
      var1.print(Wml.escape(var2));
      if (var3 != null) {
         var1.print("\" descr=\"");
         var1.print(Wml.escape(var3));
      }

      var1.println("\"/>");
      var1.println("<a:graphic>");
      var1.println("<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">");
      var1.println("<p:pic>");
      var1.println("<p:nvPicPr>");
      var1.print("<p:cNvPr id=\"");
      var1.print(this.graphicEnv.images.getNextId());
      var1.print("\" name=\"");
      var1.print(Wml.escape(var2));
      if (var3 != null) {
         var1.print("\" descr=\"");
         var1.print(Wml.escape(var3));
      }

      var1.println("\"/>");
      var1.println("<p:cNvPicPr/>");
      var1.println("</p:nvPicPr>");
      var1.println("<p:blipFill>");
      var1.print("<a:blip r:embed=\"");
      var1.print(this.id);
      var1.println("\"/>");
      if (this.layout.contentWidth == this.layout.viewportWidth && this.layout.contentHeight == this.layout.viewportHeight) {
         var1.println("<a:srcRect/>");
      } else {
         double var4 = 0.0;
         double var6 = 0.0;
         double var8 = 0.0;
         double var10 = 0.0;
         double var12;
         if (this.layout.contentWidth < this.layout.viewportWidth) {
            var4 = -this.layout.contentX;
            var12 = this.layout.contentX + this.layout.contentWidth;
            var8 = -(this.layout.viewportWidth - var12);
         } else if (this.layout.contentWidth > this.layout.viewportWidth) {
            var4 = this.layout.cropX;
            var12 = this.layout.cropX + this.layout.viewportWidth;
            var8 = this.layout.contentWidth - var12;
         }

         var4 = var4 * 100000.0 / this.layout.contentWidth;
         var8 = var8 * 100000.0 / this.layout.contentWidth;
         double var14;
         if (this.layout.contentHeight < this.layout.viewportHeight) {
            var6 = -this.layout.contentY;
            var14 = this.layout.contentY + this.layout.contentHeight;
            var10 = -(this.layout.viewportHeight - var14);
         } else if (this.layout.contentHeight > this.layout.viewportHeight) {
            var6 = this.layout.cropY;
            var14 = this.layout.cropY + this.layout.viewportHeight;
            var10 = this.layout.contentHeight - var14;
         }

         var6 = var6 * 100000.0 / this.layout.contentHeight;
         var10 = var10 * 100000.0 / this.layout.contentHeight;
         var1.println("<a:srcRect");
         var1.print(" l=\"" + (int)Math.rint(var4) + "\"");
         var1.print(" t=\"" + (int)Math.rint(var6) + "\"");
         var1.print(" r=\"" + (int)Math.rint(var8) + "\"");
         var1.print(" b=\"" + (int)Math.rint(var10) + "\"");
         var1.println(" />");
      }

      var1.println("<a:stretch>");
      var1.println("<a:fillRect/>");
      var1.println("</a:stretch>");
      var1.println("</p:blipFill>");
      var1.println("<p:spPr>");
      var1.println("<a:xfrm>");
      var1.println("<a:off x=\"0\" y=\"0\"/>");
      var1.print("<a:ext cx=\"");
      var1.print(ptToEMU(this.layout.viewportWidth));
      var1.print("\" cy=\"");
      var1.print(ptToEMU(this.layout.viewportHeight));
      var1.println("\"/>");
      var1.println("</a:xfrm>");
      var1.println("<a:prstGeom prst=\"rect\"/>");
      var1.println("</p:spPr>");
      var1.println("</p:pic>");
      var1.println("</a:graphicData>");
      var1.println("</a:graphic>");
      var1.println("</wp:inline>");
      var1.println("</w:drawing>");
   }

   private String name() {
      String var1 = null;
      Object var2 = this.graphic.getClientData();
      if (var2 != null && var2 instanceof ExternalGraphic && this.graphic.getType() != 2) {
         var1 = this.graphic.getLocation();
         if (var1 != null) {
            var1 = URLUtil.locationToFilename(var1);
         }
      }

      if (var1 == null) {
         var1 = "Picture " + this.index;
      }

      return var1;
   }

   private String role() {
      String var1 = null;
      ExternalGraphic var2 = GraphicLayout.properties(this.graphic);
      if (var2 != null) {
         var1 = var2.role;
         if (var1 != null && (var1 = var1.trim()).length() == 0) {
            var1 = null;
         }
      }

      return var1;
   }

   private static final long ptToEMU(double var0) {
      return (long)Math.rint(var0 * 914400.0 / 72.0);
   }

   private void printVMLPicture(PrintWriter var1) {
      var1.println("<w:pict>");
      var1.print("<v:shape filled=\"f\"");
      String var2 = null;
      ExternalGraphic var3 = GraphicLayout.properties(this.graphic);
      if (var3 != null) {
         var2 = var3.role;
      }

      if (var2 != null && var2.length() > 0) {
         var1.print(" alt=\"");
         var1.print(Wml.escape(var2));
         var1.print("\"");
      }

      var1.print(" style=\"");
      var1.print("width:" + this.format(this.layout.viewportWidth, 2) + "pt;");
      var1.print("height:" + this.format(this.layout.viewportHeight, 2) + "pt");
      var1.println("\">");
      var1.print("<v:imagedata r:id=\"" + this.id + "\"");
      if (this.layout.contentWidth != this.layout.viewportWidth || this.layout.contentHeight != this.layout.viewportHeight) {
         double var4 = 0.0;
         double var6 = 0.0;
         double var8 = 0.0;
         double var10 = 0.0;
         double var12;
         if (this.layout.contentWidth < this.layout.viewportWidth) {
            var4 = -this.layout.contentX;
            var12 = this.layout.contentX + this.layout.contentWidth;
            var8 = -(this.layout.viewportWidth - var12);
         } else if (this.layout.contentWidth > this.layout.viewportWidth) {
            var4 = this.layout.cropX;
            var12 = this.layout.cropX + this.layout.viewportWidth;
            var8 = this.layout.contentWidth - var12;
         }

         var4 /= this.layout.contentWidth;
         var8 /= this.layout.contentWidth;
         double var14;
         if (this.layout.contentHeight < this.layout.viewportHeight) {
            var6 = -this.layout.contentY;
            var14 = this.layout.contentY + this.layout.contentHeight;
            var10 = -(this.layout.viewportHeight - var14);
         } else if (this.layout.contentHeight > this.layout.viewportHeight) {
            var6 = this.layout.cropY;
            var14 = this.layout.cropY + this.layout.viewportHeight;
            var10 = this.layout.contentHeight - var14;
         }

         var6 /= this.layout.contentHeight;
         var10 /= this.layout.contentHeight;
         var1.print(" cropleft=\"" + this.format(var4, 2) + "\"");
         var1.print(" croptop=\"" + this.format(var6, 2) + "\"");
         var1.print(" cropright=\"" + this.format(var8, 2) + "\"");
         var1.print(" cropbottom=\"" + this.format(var10, 2) + "\"");
      }

      var1.println(" />");
      var1.println("</v:shape>");
      var1.println("</w:pict>");
   }

   private String format(double var1, int var3) {
      this.numberFormat.setMaximumFractionDigits(var3);
      this.numberFormat.setMinimumFractionDigits(var3);
      return this.numberFormat.format(var1);
   }
}
