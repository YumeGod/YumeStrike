package org.apache.batik.svggen;

import java.awt.AlphaComposite;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.w3c.dom.Element;

public class SVGAlphaComposite extends AbstractSVGConverter {
   private Map compositeDefsMap = new HashMap();
   private boolean backgroundAccessRequired = false;

   public SVGAlphaComposite(SVGGeneratorContext var1) {
      super(var1);
      this.compositeDefsMap.put(AlphaComposite.Src, this.compositeToSVG(AlphaComposite.Src));
      this.compositeDefsMap.put(AlphaComposite.SrcIn, this.compositeToSVG(AlphaComposite.SrcIn));
      this.compositeDefsMap.put(AlphaComposite.SrcOut, this.compositeToSVG(AlphaComposite.SrcOut));
      this.compositeDefsMap.put(AlphaComposite.DstIn, this.compositeToSVG(AlphaComposite.DstIn));
      this.compositeDefsMap.put(AlphaComposite.DstOut, this.compositeToSVG(AlphaComposite.DstOut));
      this.compositeDefsMap.put(AlphaComposite.DstOver, this.compositeToSVG(AlphaComposite.DstOver));
      this.compositeDefsMap.put(AlphaComposite.Clear, this.compositeToSVG(AlphaComposite.Clear));
   }

   public List getAlphaCompositeFilterSet() {
      return new LinkedList(this.compositeDefsMap.values());
   }

   public boolean requiresBackgroundAccess() {
      return this.backgroundAccessRequired;
   }

   public SVGDescriptor toSVG(GraphicContext var1) {
      return this.toSVG((AlphaComposite)var1.getComposite());
   }

   public SVGCompositeDescriptor toSVG(AlphaComposite var1) {
      SVGCompositeDescriptor var2 = (SVGCompositeDescriptor)this.descMap.get(var1);
      if (var2 == null) {
         String var3 = this.doubleString((double)var1.getAlpha());
         String var4 = null;
         Element var5 = null;
         if (var1.getRule() != 3) {
            AlphaComposite var6 = AlphaComposite.getInstance(var1.getRule());
            var5 = (Element)this.compositeDefsMap.get(var6);
            this.defSet.add(var5);
            StringBuffer var7 = new StringBuffer("url(");
            var7.append("#");
            var7.append(var5.getAttributeNS((String)null, "id"));
            var7.append(")");
            var4 = var7.toString();
         } else {
            var4 = "none";
         }

         var2 = new SVGCompositeDescriptor(var3, var4, var5);
         this.descMap.put(var1, var2);
      }

      if (var1.getRule() != 3) {
         this.backgroundAccessRequired = true;
      }

      return var2;
   }

   private Element compositeToSVG(AlphaComposite var1) {
      String var2 = null;
      String var3 = null;
      String var4 = null;
      String var5 = "0";
      String var6 = null;
      switch (var1.getRule()) {
         case 1:
            var2 = "arithmetic";
            var3 = "SourceGraphic";
            var4 = "BackgroundImage";
            var6 = "alphaCompositeClear";
            break;
         case 2:
            var2 = "arithmetic";
            var3 = "SourceGraphic";
            var4 = "BackgroundImage";
            var6 = "alphaCompositeSrc";
            var5 = "1";
            break;
         case 3:
         default:
            throw new Error("invalid rule:" + var1.getRule());
         case 4:
            var2 = "over";
            var4 = "SourceGraphic";
            var3 = "BackgroundImage";
            var6 = "alphaCompositeDstOver";
            break;
         case 5:
            var2 = "in";
            var3 = "SourceGraphic";
            var4 = "BackgroundImage";
            var6 = "alphaCompositeSrcIn";
            break;
         case 6:
            var2 = "in";
            var4 = "SourceGraphic";
            var3 = "BackgroundImage";
            var6 = "alphaCompositeDstIn";
            break;
         case 7:
            var2 = "out";
            var3 = "SourceGraphic";
            var4 = "BackgroundImage";
            var6 = "alphaCompositeSrcOut";
            break;
         case 8:
            var2 = "out";
            var4 = "SourceGraphic";
            var3 = "BackgroundImage";
            var6 = "alphaCompositeDstOut";
      }

      Element var7 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "filter");
      var7.setAttributeNS((String)null, "id", var6);
      var7.setAttributeNS((String)null, "filterUnits", "objectBoundingBox");
      var7.setAttributeNS((String)null, "x", "0%");
      var7.setAttributeNS((String)null, "y", "0%");
      var7.setAttributeNS((String)null, "width", "100%");
      var7.setAttributeNS((String)null, "height", "100%");
      Element var8 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "feComposite");
      var8.setAttributeNS((String)null, "operator", var2);
      var8.setAttributeNS((String)null, "in", var3);
      var8.setAttributeNS((String)null, "in2", var4);
      var8.setAttributeNS((String)null, "k2", var5);
      var8.setAttributeNS((String)null, "result", "composite");
      Element var9 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "feFlood");
      var9.setAttributeNS((String)null, "flood-color", "white");
      var9.setAttributeNS((String)null, "flood-opacity", "1");
      var9.setAttributeNS((String)null, "result", "flood");
      Element var10 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "feMerge");
      Element var11 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "feMergeNode");
      var11.setAttributeNS((String)null, "in", "flood");
      Element var12 = this.generatorContext.domFactory.createElementNS("http://www.w3.org/2000/svg", "feMergeNode");
      var12.setAttributeNS((String)null, "in", "composite");
      var10.appendChild(var11);
      var10.appendChild(var12);
      var7.appendChild(var9);
      var7.appendChild(var8);
      var7.appendChild(var10);
      return var7;
   }
}
