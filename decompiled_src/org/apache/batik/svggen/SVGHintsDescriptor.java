package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SVGHintsDescriptor implements SVGDescriptor, SVGSyntax {
   private String colorInterpolation;
   private String colorRendering;
   private String textRendering;
   private String shapeRendering;
   private String imageRendering;

   public SVGHintsDescriptor(String var1, String var2, String var3, String var4, String var5) {
      if (var1 != null && var2 != null && var3 != null && var4 != null && var5 != null) {
         this.colorInterpolation = var1;
         this.colorRendering = var2;
         this.textRendering = var3;
         this.shapeRendering = var4;
         this.imageRendering = var5;
      } else {
         throw new SVGGraphics2DRuntimeException("none of the hints description parameters should be null");
      }
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("color-interpolation", this.colorInterpolation);
      ((Map)var1).put("color-rendering", this.colorRendering);
      ((Map)var1).put("text-rendering", this.textRendering);
      ((Map)var1).put("shape-rendering", this.shapeRendering);
      ((Map)var1).put("image-rendering", this.imageRendering);
      return (Map)var1;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      return (List)var1;
   }
}
