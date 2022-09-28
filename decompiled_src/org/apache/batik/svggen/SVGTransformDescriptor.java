package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SVGTransformDescriptor implements SVGDescriptor, SVGSyntax {
   private String transform;

   public SVGTransformDescriptor(String var1) {
      this.transform = var1;
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("transform", this.transform);
      return (Map)var1;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      return (List)var1;
   }
}
