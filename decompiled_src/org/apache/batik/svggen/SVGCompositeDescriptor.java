package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class SVGCompositeDescriptor implements SVGDescriptor, SVGSyntax {
   private Element def;
   private String opacityValue;
   private String filterValue;

   public SVGCompositeDescriptor(String var1, String var2) {
      this.opacityValue = var1;
      this.filterValue = var2;
   }

   public SVGCompositeDescriptor(String var1, String var2, Element var3) {
      this(var1, var2);
      this.def = var3;
   }

   public String getOpacityValue() {
      return this.opacityValue;
   }

   public String getFilterValue() {
      return this.filterValue;
   }

   public Element getDef() {
      return this.def;
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("opacity", this.opacityValue);
      ((Map)var1).put("filter", this.filterValue);
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
