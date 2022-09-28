package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class SVGPaintDescriptor implements SVGDescriptor, SVGSyntax {
   private Element def;
   private String paintValue;
   private String opacityValue;

   public SVGPaintDescriptor(String var1, String var2) {
      this.paintValue = var1;
      this.opacityValue = var2;
   }

   public SVGPaintDescriptor(String var1, String var2, Element var3) {
      this(var1, var2);
      this.def = var3;
   }

   public String getPaintValue() {
      return this.paintValue;
   }

   public String getOpacityValue() {
      return this.opacityValue;
   }

   public Element getDef() {
      return this.def;
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("fill", this.paintValue);
      ((Map)var1).put("stroke", this.paintValue);
      ((Map)var1).put("fill-opacity", this.opacityValue);
      ((Map)var1).put("stroke-opacity", this.opacityValue);
      return (Map)var1;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      if (this.def != null) {
         ((List)var1).add(this.def);
      }

      return (List)var1;
   }
}
