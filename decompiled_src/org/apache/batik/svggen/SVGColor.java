package org.apache.batik.svggen;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.GraphicContext;

public class SVGColor extends AbstractSVGConverter {
   public static final Color aqua;
   public static final Color black;
   public static final Color blue;
   public static final Color fuchsia;
   public static final Color gray;
   public static final Color green;
   public static final Color lime;
   public static final Color maroon;
   public static final Color navy;
   public static final Color olive;
   public static final Color purple;
   public static final Color red;
   public static final Color silver;
   public static final Color teal;
   public static final Color white;
   public static final Color yellow;
   private static Map colorMap;

   public SVGColor(SVGGeneratorContext var1) {
      super(var1);
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      Paint var2 = var1.getPaint();
      return toSVG((Color)var2, this.generatorContext);
   }

   public static SVGPaintDescriptor toSVG(Color var0, SVGGeneratorContext var1) {
      String var2 = (String)colorMap.get(var0);
      if (var2 == null) {
         StringBuffer var3 = new StringBuffer("rgb(");
         var3.append(var0.getRed());
         var3.append(",");
         var3.append(var0.getGreen());
         var3.append(",");
         var3.append(var0.getBlue());
         var3.append(")");
         var2 = var3.toString();
      }

      float var5 = (float)var0.getAlpha() / 255.0F;
      String var4 = var1.doubleString((double)var5);
      return new SVGPaintDescriptor(var2, var4);
   }

   static {
      aqua = Color.cyan;
      black = Color.black;
      blue = Color.blue;
      fuchsia = Color.magenta;
      gray = Color.gray;
      green = new Color(0, 128, 0);
      lime = Color.green;
      maroon = new Color(128, 0, 0);
      navy = new Color(0, 0, 128);
      olive = new Color(128, 128, 0);
      purple = new Color(128, 0, 128);
      red = Color.red;
      silver = new Color(192, 192, 192);
      teal = new Color(0, 128, 128);
      white = Color.white;
      yellow = Color.yellow;
      colorMap = new HashMap();
      colorMap.put(black, "black");
      colorMap.put(silver, "silver");
      colorMap.put(gray, "gray");
      colorMap.put(white, "white");
      colorMap.put(maroon, "maroon");
      colorMap.put(red, "red");
      colorMap.put(purple, "purple");
      colorMap.put(fuchsia, "fuchsia");
      colorMap.put(green, "green");
      colorMap.put(lime, "lime");
      colorMap.put(olive, "olive");
      colorMap.put(yellow, "yellow");
      colorMap.put(navy, "navy");
      colorMap.put(blue, "blue");
      colorMap.put(teal, "teal");
      colorMap.put(aqua, "aqua");
   }
}
