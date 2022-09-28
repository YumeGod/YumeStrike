package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SVGStrokeDescriptor implements SVGDescriptor, SVGSyntax {
   private String strokeWidth;
   private String capStyle;
   private String joinStyle;
   private String miterLimit;
   private String dashArray;
   private String dashOffset;

   public SVGStrokeDescriptor(String var1, String var2, String var3, String var4, String var5, String var6) {
      if (var1 != null && var2 != null && var3 != null && var4 != null && var5 != null && var6 != null) {
         this.strokeWidth = var1;
         this.capStyle = var2;
         this.joinStyle = var3;
         this.miterLimit = var4;
         this.dashArray = var5;
         this.dashOffset = var6;
      } else {
         throw new SVGGraphics2DRuntimeException("none of the stroke description parameters should be null");
      }
   }

   String getStrokeWidth() {
      return this.strokeWidth;
   }

   String getCapStyle() {
      return this.capStyle;
   }

   String getJoinStyle() {
      return this.joinStyle;
   }

   String getMiterLimit() {
      return this.miterLimit;
   }

   String getDashArray() {
      return this.dashArray;
   }

   String getDashOffset() {
      return this.dashOffset;
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("stroke-width", this.strokeWidth);
      ((Map)var1).put("stroke-linecap", this.capStyle);
      ((Map)var1).put("stroke-linejoin", this.joinStyle);
      ((Map)var1).put("stroke-miterlimit", this.miterLimit);
      ((Map)var1).put("stroke-dasharray", this.dashArray);
      ((Map)var1).put("stroke-dashoffset", this.dashOffset);
      return (Map)var1;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      return (List)var1;
   }
}
