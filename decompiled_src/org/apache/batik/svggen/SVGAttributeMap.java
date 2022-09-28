package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.Map;

public class SVGAttributeMap {
   private static Map attrMap = new HashMap();

   public static SVGAttribute get(String var0) {
      return (SVGAttribute)attrMap.get(var0);
   }
}
