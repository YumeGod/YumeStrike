package com.mxgraph.view;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class mxStyleRegistry {
   protected static Map values = new Hashtable();

   public static void putValue(String var0, Object var1) {
      values.put(var0, var1);
   }

   public static Object getValue(String var0) {
      return values.get(var0);
   }

   public static String getName(Object var0) {
      Iterator var1 = values.entrySet().iterator();

      Map.Entry var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (Map.Entry)var1.next();
      } while(var2.getValue() != var0);

      return (String)var2.getKey();
   }

   static {
      putValue("elbowEdgeStyle", mxEdgeStyle.ElbowConnector);
      putValue("entityRelationEdgeStyle", mxEdgeStyle.EntityRelation);
      putValue("loopEdgeStyle", mxEdgeStyle.Loop);
      putValue("sideToSideEdgeStyle", mxEdgeStyle.SideToSide);
      putValue("topToBottomEdgeStyle", mxEdgeStyle.TopToBottom);
      putValue("ellipsePerimeter", mxPerimeter.EllipsePerimeter);
      putValue("rectanglePerimeter", mxPerimeter.RectanglePerimeter);
      putValue("rhombusPerimeter", mxPerimeter.RhombusPerimeter);
      putValue("trianglePerimeter", mxPerimeter.TrianglePerimeter);
   }
}
