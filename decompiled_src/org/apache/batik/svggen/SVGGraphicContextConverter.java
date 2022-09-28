package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.ext.awt.g2d.TransformStackElement;

public class SVGGraphicContextConverter {
   private static final int GRAPHIC_CONTEXT_CONVERTER_COUNT = 6;
   private SVGTransform transformConverter;
   private SVGPaint paintConverter;
   private SVGBasicStroke strokeConverter;
   private SVGComposite compositeConverter;
   private SVGClip clipConverter;
   private SVGRenderingHints hintsConverter;
   private SVGFont fontConverter;
   private SVGConverter[] converters = new SVGConverter[6];

   public SVGTransform getTransformConverter() {
      return this.transformConverter;
   }

   public SVGPaint getPaintConverter() {
      return this.paintConverter;
   }

   public SVGBasicStroke getStrokeConverter() {
      return this.strokeConverter;
   }

   public SVGComposite getCompositeConverter() {
      return this.compositeConverter;
   }

   public SVGClip getClipConverter() {
      return this.clipConverter;
   }

   public SVGRenderingHints getHintsConverter() {
      return this.hintsConverter;
   }

   public SVGFont getFontConverter() {
      return this.fontConverter;
   }

   public SVGGraphicContextConverter(SVGGeneratorContext var1) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("generatorContext should not be null");
      } else {
         this.transformConverter = new SVGTransform(var1);
         this.paintConverter = new SVGPaint(var1);
         this.strokeConverter = new SVGBasicStroke(var1);
         this.compositeConverter = new SVGComposite(var1);
         this.clipConverter = new SVGClip(var1);
         this.hintsConverter = new SVGRenderingHints(var1);
         this.fontConverter = new SVGFont(var1);
         int var2 = 0;
         this.converters[var2++] = this.paintConverter;
         this.converters[var2++] = this.strokeConverter;
         this.converters[var2++] = this.compositeConverter;
         this.converters[var2++] = this.clipConverter;
         this.converters[var2++] = this.hintsConverter;
         this.converters[var2++] = this.fontConverter;
      }
   }

   public String toSVG(TransformStackElement[] var1) {
      return this.transformConverter.toSVGTransform(var1);
   }

   public SVGGraphicContext toSVG(GraphicContext var1) {
      HashMap var2 = new HashMap();

      for(int var3 = 0; var3 < this.converters.length; ++var3) {
         SVGDescriptor var4 = this.converters[var3].toSVG(var1);
         if (var4 != null) {
            var4.getAttributeMap(var2);
         }
      }

      return new SVGGraphicContext(var2, var1.getTransformStack());
   }

   public List getDefinitionSet() {
      LinkedList var1 = new LinkedList();

      for(int var2 = 0; var2 < this.converters.length; ++var2) {
         var1.addAll(this.converters[var2].getDefinitionSet());
      }

      return var1;
   }
}
