package com.xmlmind.fo.converter;

import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.objects.ExternalGraphic;
import com.xmlmind.fo.properties.Value;
import java.util.Properties;

public final class GraphicLayout {
   public double viewportWidth;
   public double viewportHeight;
   public double contentX;
   public double contentY;
   public double contentWidth;
   public double contentHeight;
   public double cropX;
   public double cropY;

   public GraphicLayout(Graphic var1, int var2, double var3) {
      ExternalGraphic var5 = properties(var1);
      int var6 = var5.scaling;
      double var7 = intrinsicWidth(var1, var2);
      double var9 = intrinsicHeight(var1, var2);
      Value var11 = var5.width;
      Value var12 = var5.height;
      switch (var11.type) {
         case 4:
            this.viewportWidth = var11.length();
            if (this.viewportWidth < 0.0) {
               this.viewportWidth = 0.0;
            }
            break;
         case 13:
            this.viewportWidth = var3 * var11.percentage() / 100.0;
      }

      if (var12.type == 4) {
         this.viewportHeight = var12.length();
         if (this.viewportHeight < 0.0) {
            this.viewportHeight = 0.0;
         }
      }

      var11 = var5.contentWidth;
      var12 = var5.contentHeight;
      label141:
      switch (var11.type) {
         case 1:
            switch (var11.keyword()) {
               case 173:
                  this.contentWidth = this.viewportWidth;
                  break label141;
               case 246:
                  if (this.viewportWidth > 0.0 && var7 > this.viewportWidth) {
                     this.contentWidth = this.viewportWidth;
                  }
                  break label141;
               case 247:
                  if (this.viewportWidth > 0.0 && var7 < this.viewportWidth) {
                     this.contentWidth = this.viewportWidth;
                  }
               default:
                  break label141;
            }
         case 4:
            this.contentWidth = var11.length();
            if (this.contentWidth < 0.0) {
               this.contentWidth = 0.0;
            }
            break;
         case 13:
            this.contentWidth = var7 * var11.percentage() / 100.0;
      }

      label129:
      switch (var12.type) {
         case 1:
            switch (var12.keyword()) {
               case 173:
                  this.contentHeight = this.viewportHeight;
                  break label129;
               case 246:
                  if (this.viewportHeight > 0.0 && var9 > this.viewportHeight) {
                     this.contentHeight = this.viewportHeight;
                  }
                  break label129;
               case 247:
                  if (this.viewportHeight > 0.0 && var9 < this.viewportHeight) {
                     this.contentHeight = this.viewportHeight;
                  }
               default:
                  break label129;
            }
         case 4:
            this.contentHeight = var12.length();
            if (this.contentHeight < 0.0) {
               this.contentHeight = 0.0;
            }
            break;
         case 13:
            this.contentHeight = var9 * var12.percentage() / 100.0;
      }

      boolean var13 = var6 == 214;
      double var14;
      if (var13) {
         var14 = -1.0;
         if (this.contentWidth != 0.0) {
            var14 = this.contentWidth / var7;
         }

         double var16 = -1.0;
         if (this.contentHeight != 0.0) {
            var16 = this.contentHeight / var9;
         }

         if (var14 > 0.0 && var16 > 0.0) {
            double var18 = Math.min(var14, var16);
            this.contentWidth = var7 * var18;
            this.contentHeight = var9 * var18;
         }
      }

      if (this.contentWidth == 0.0 && this.contentHeight != 0.0 && var13) {
         var14 = this.contentHeight / var9;
         this.contentWidth = var7 * var14;
      }

      if (this.contentHeight == 0.0 && this.contentWidth != 0.0 && var13) {
         var14 = this.contentWidth / var7;
         this.contentHeight = var9 * var14;
      }

      if (this.contentWidth == 0.0) {
         this.contentWidth = var7;
      }

      if (this.contentHeight == 0.0) {
         this.contentHeight = var9;
      }

      if (this.viewportWidth == 0.0) {
         this.viewportWidth = this.contentWidth;
      }

      if (this.viewportHeight == 0.0) {
         this.viewportHeight = this.contentHeight;
      }

      if (this.contentWidth != this.viewportWidth) {
         switch (var5.textAlign) {
            case 31:
               if (this.contentWidth < this.viewportWidth) {
                  this.contentX = (this.viewportWidth - this.contentWidth) / 2.0;
               } else {
                  this.cropX = (this.contentWidth - this.viewportWidth) / 2.0;
               }
               break;
            case 52:
            case 165:
               if (this.contentWidth < this.viewportWidth) {
                  this.contentX = this.viewportWidth - this.contentWidth;
               } else {
                  this.cropX = this.contentWidth - this.viewportWidth;
               }
         }
      }

      if (this.contentHeight != this.viewportHeight) {
         switch (var5.displayAlign) {
            case 4:
               if (this.contentHeight < this.viewportHeight) {
                  this.contentY = this.viewportHeight - this.contentHeight;
               } else {
                  this.cropY = this.contentHeight - this.viewportHeight;
               }
               break;
            case 31:
               if (this.contentHeight < this.viewportHeight) {
                  this.contentY = (this.viewportHeight - this.contentHeight) / 2.0;
               } else {
                  this.cropY = (this.contentHeight - this.viewportHeight) / 2.0;
               }
         }
      }

   }

   public static double intrinsicWidth(Graphic var0, int var1) {
      double var2 = var0.getXResolution();
      if (var2 <= 0.0) {
         var2 = (double)var1;
      }

      return (double)var0.getWidth() / var2 * 72.0;
   }

   public static double intrinsicHeight(Graphic var0, int var1) {
      double var2 = var0.getYResolution();
      if (var2 <= 0.0) {
         var2 = (double)var1;
      }

      return (double)var0.getHeight() / var2 * 72.0;
   }

   public static ExternalGraphic properties(Graphic var0) {
      Object var1 = var0.getClientData();
      return var1 != null && var1 instanceof ExternalGraphic ? (ExternalGraphic)var1 : new ExternalGraphic(var0.getLocation(), var0.getFormat());
   }

   public static int getResolutionProperty(Properties var0, String var1, int var2) {
      int var3 = 0;
      String var4 = var0.getProperty(var1);
      if (var4 != null) {
         try {
            var3 = Integer.parseInt(var4);
         } catch (Exception var6) {
         }
      }

      if (var3 <= 0) {
         var3 = var2;
      }

      return var3;
   }

   public static double width(Graphic var0, int var1) {
      double var2 = 0.0;
      ExternalGraphic var6 = properties(var0);
      switch (var6.width.type) {
         case 1:
            double var4;
            switch (var6.contentWidth.type) {
               case 1:
                  if (var6.contentWidth.keyword() == 10 && var6.scaling == 214) {
                     switch (var6.contentHeight.type) {
                        case 4:
                           double var7 = var6.contentHeight.length();
                           if (var7 > 0.0) {
                              var4 = var7 / intrinsicHeight(var0, var1);
                              var2 = intrinsicWidth(var0, var1) * var4;
                           }

                           return var2;
                        case 13:
                           var4 = var6.contentHeight.percentage() / 100.0;
                           var2 = intrinsicWidth(var0, var1) * var4;
                     }
                  }

                  return var2;
               case 4:
                  var2 = var6.contentWidth.length();
                  if (var2 < 0.0) {
                     var2 = 0.0;
                  }

                  return var2;
               case 13:
                  var4 = var6.contentWidth.percentage() / 100.0;
                  var2 = intrinsicWidth(var0, var1) * var4;
                  return var2;
               default:
                  return var2;
            }
         case 4:
            var2 = var6.width.length();
            if (var2 < 0.0) {
               var2 = 0.0;
            }
      }

      return var2;
   }
}
