package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class SVGClipDescriptor implements SVGDescriptor, SVGSyntax {
   private String clipPathValue;
   private Element clipPathDef;

   public SVGClipDescriptor(String var1, Element var2) {
      if (var1 == null) {
         throw new SVGGraphics2DRuntimeException("clipPathValue should not be null");
      } else {
         this.clipPathValue = var1;
         this.clipPathDef = var2;
      }
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("clip-path", this.clipPathValue);
      return (Map)var1;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      if (this.clipPathDef != null) {
         ((List)var1).add(this.clipPathDef);
      }

      return (List)var1;
   }
}
