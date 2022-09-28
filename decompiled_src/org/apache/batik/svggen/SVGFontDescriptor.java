package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class SVGFontDescriptor implements SVGDescriptor, SVGSyntax {
   private Element def;
   private String fontSize;
   private String fontWeight;
   private String fontStyle;
   private String fontFamily;

   public SVGFontDescriptor(String var1, String var2, String var3, String var4, Element var5) {
      if (var1 != null && var2 != null && var3 != null && var4 != null) {
         this.fontSize = var1;
         this.fontWeight = var2;
         this.fontStyle = var3;
         this.fontFamily = var4;
         this.def = var5;
      } else {
         throw new SVGGraphics2DRuntimeException("none of the font description parameters should be null");
      }
   }

   public Map getAttributeMap(Map var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      ((Map)var1).put("font-size", this.fontSize);
      ((Map)var1).put("font-weight", this.fontWeight);
      ((Map)var1).put("font-style", this.fontStyle);
      ((Map)var1).put("font-family", this.fontFamily);
      return (Map)var1;
   }

   public Element getDef() {
      return this.def;
   }

   public List getDefinitionSet(List var1) {
      if (var1 == null) {
         var1 = new LinkedList();
      }

      if (this.def != null && !((List)var1).contains(this.def)) {
         ((List)var1).add(this.def);
      }

      return (List)var1;
   }
}
