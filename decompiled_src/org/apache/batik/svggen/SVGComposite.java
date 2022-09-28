package org.apache.batik.svggen;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.util.LinkedList;
import java.util.List;
import org.apache.batik.ext.awt.g2d.GraphicContext;

public class SVGComposite implements SVGConverter {
   private SVGAlphaComposite svgAlphaComposite;
   private SVGCustomComposite svgCustomComposite;

   public SVGComposite(SVGGeneratorContext var1) {
      this.svgAlphaComposite = new SVGAlphaComposite(var1);
      this.svgCustomComposite = new SVGCustomComposite(var1);
   }

   public List getDefinitionSet() {
      LinkedList var1 = new LinkedList(this.svgAlphaComposite.getDefinitionSet());
      var1.addAll(this.svgCustomComposite.getDefinitionSet());
      return var1;
   }

   public SVGAlphaComposite getAlphaCompositeConverter() {
      return this.svgAlphaComposite;
   }

   public SVGCustomComposite getCustomCompositeConverter() {
      return this.svgCustomComposite;
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG(var1.getComposite());
   }

   public SVGCompositeDescriptor toSVG(Composite var1) {
      return var1 instanceof AlphaComposite ? this.svgAlphaComposite.toSVG((AlphaComposite)var1) : this.svgCustomComposite.toSVG(var1);
   }
}
