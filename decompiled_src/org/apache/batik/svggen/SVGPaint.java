package org.apache.batik.svggen;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.ext.awt.g2d.GraphicContext;

public class SVGPaint implements SVGConverter {
   private SVGLinearGradient svgLinearGradient;
   private SVGTexturePaint svgTexturePaint;
   private SVGColor svgColor;
   private SVGCustomPaint svgCustomPaint;
   private SVGGeneratorContext generatorContext;

   public SVGPaint(SVGGeneratorContext var1) {
      this.svgLinearGradient = new SVGLinearGradient(var1);
      this.svgTexturePaint = new SVGTexturePaint(var1);
      this.svgCustomPaint = new SVGCustomPaint(var1);
      this.svgColor = new SVGColor(var1);
      this.generatorContext = var1;
   }

   public List getDefinitionSet() {
      LinkedList var1 = new LinkedList(this.svgLinearGradient.getDefinitionSet());
      var1.addAll(this.svgTexturePaint.getDefinitionSet());
      var1.addAll(this.svgCustomPaint.getDefinitionSet());
      var1.addAll(this.svgColor.getDefinitionSet());
      return var1;
   }

   public SVGTexturePaint getTexturePaintConverter() {
      return this.svgTexturePaint;
   }

   public SVGLinearGradient getGradientPaintConverter() {
      return this.svgLinearGradient;
   }

   public SVGCustomPaint getCustomPaintConverter() {
      return this.svgCustomPaint;
   }

   public SVGColor getColorConverter() {
      return this.svgColor;
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG(var1.getPaint());
   }

   public SVGPaintDescriptor toSVG(Paint var1) {
      SVGPaintDescriptor var2 = this.svgCustomPaint.toSVG(var1);
      if (var2 == null) {
         if (var1 instanceof Color) {
            var2 = SVGColor.toSVG((Color)var1, this.generatorContext);
         } else if (var1 instanceof GradientPaint) {
            var2 = this.svgLinearGradient.toSVG((GradientPaint)var1);
         } else if (var1 instanceof TexturePaint) {
            var2 = this.svgTexturePaint.toSVG((TexturePaint)var1);
         }
      }

      return var2;
   }
}
