package com.mxgraph.view;

import com.mxgraph.util.mxConstants;
import java.util.Hashtable;
import java.util.Map;

public class mxStylesheet {
   public static final Map EMPTY_STYLE = new Hashtable();
   protected Map styles = new Hashtable();

   public mxStylesheet() {
      this.setDefaultVertexStyle(this.createDefaultVertexStyle());
      this.setDefaultEdgeStyle(this.createDefaultEdgeStyle());
   }

   public Map getStyles() {
      return this.styles;
   }

   public void setStyles(Map var1) {
      this.styles = var1;
   }

   protected Map createDefaultVertexStyle() {
      Hashtable var1 = new Hashtable();
      var1.put(mxConstants.STYLE_SHAPE, "rectangle");
      var1.put(mxConstants.STYLE_PERIMETER, mxPerimeter.RectanglePerimeter);
      var1.put(mxConstants.STYLE_VERTICAL_ALIGN, "middle");
      var1.put(mxConstants.STYLE_ALIGN, "center");
      var1.put(mxConstants.STYLE_FILLCOLOR, "#C3D9FF");
      var1.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
      var1.put(mxConstants.STYLE_FONTCOLOR, "#774400");
      return var1;
   }

   protected Map createDefaultEdgeStyle() {
      Hashtable var1 = new Hashtable();
      var1.put(mxConstants.STYLE_SHAPE, "connector");
      var1.put(mxConstants.STYLE_ENDARROW, "classic");
      var1.put(mxConstants.STYLE_VERTICAL_ALIGN, "middle");
      var1.put(mxConstants.STYLE_ALIGN, "center");
      var1.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
      var1.put(mxConstants.STYLE_FONTCOLOR, "#446299");
      return var1;
   }

   public Map getDefaultVertexStyle() {
      return (Map)this.styles.get("defaultVertex");
   }

   public void setDefaultVertexStyle(Map var1) {
      this.putCellStyle("defaultVertex", var1);
   }

   public Map getDefaultEdgeStyle() {
      return (Map)this.styles.get("defaultEdge");
   }

   public void setDefaultEdgeStyle(Map var1) {
      this.putCellStyle("defaultEdge", var1);
   }

   public void putCellStyle(String var1, Map var2) {
      this.styles.put(var1, var2);
   }

   public Map getCellStyle(String var1, Map var2) {
      Object var3 = var2;
      if (var1 != null && var1.length() > 0) {
         String[] var4 = var1.split(";");
         if (var2 != null && !var1.startsWith(";")) {
            var3 = new Hashtable(var2);
         } else {
            var3 = new Hashtable();
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            int var7 = var6.indexOf(61);
            if (var7 >= 0) {
               String var8 = var6.substring(0, var7);
               String var9 = var6.substring(var7 + 1);
               if (var9.equals(mxConstants.NONE)) {
                  ((Map)var3).remove(var8);
               } else {
                  ((Map)var3).put(var8, var9);
               }
            } else {
               Map var10 = (Map)this.styles.get(var6);
               if (var10 != null) {
                  ((Map)var3).putAll(var10);
               }
            }
         }
      }

      return (Map)var3;
   }
}
