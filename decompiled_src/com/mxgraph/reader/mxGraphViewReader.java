package com.mxgraph.reader;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraphView;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class mxGraphViewReader extends DefaultHandler {
   protected mxICanvas canvas;
   protected double scale = 1.0;
   protected boolean htmlLabels = false;

   public void setHtmlLabels(boolean var1) {
      this.htmlLabels = var1;
   }

   public boolean isHtmlLabels() {
      return this.htmlLabels;
   }

   public abstract mxICanvas createCanvas(Map var1);

   public mxICanvas getCanvas() {
      return this.canvas;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      String var5 = var3.toUpperCase();
      Hashtable var6 = new Hashtable();

      for(int var7 = 0; var7 < var4.getLength(); ++var7) {
         String var8 = var4.getQName(var7);
         if (var8 == null || var8.length() == 0) {
            var8 = var4.getLocalName(var7);
         }

         var6.put(var8, var4.getValue(var7));
      }

      this.parseElement(var5, var6);
   }

   public void parseElement(String var1, Map var2) {
      if (this.canvas == null && var1.equalsIgnoreCase("graph")) {
         this.scale = mxUtils.getDouble(var2, "scale", 1.0);
         this.canvas = this.createCanvas(var2);
         if (this.canvas != null) {
            this.canvas.setScale(this.scale);
         }
      } else if (this.canvas != null) {
         boolean var3 = var1.equalsIgnoreCase("edge");
         boolean var4 = var1.equalsIgnoreCase("group");
         boolean var5 = var1.equalsIgnoreCase("vertex");
         if (var3 && var2.containsKey("points") || (var5 || var4) && var2.containsKey("x") && var2.containsKey("y") && var2.containsKey("width") && var2.containsKey("height")) {
            mxCellState var6 = new mxCellState((mxGraphView)null, (Object)null, var2);
            String var7 = this.parseState(var6, var3);
            this.canvas.drawCell(var6);
            this.canvas.drawLabel(var7, var6, this.isHtmlLabels());
         }
      }

   }

   public String parseState(mxCellState var1, boolean var2) {
      Map var3 = var1.getStyle();
      var1.setX(mxUtils.getDouble(var3, "x"));
      var1.setY(mxUtils.getDouble(var3, "y"));
      var1.setWidth(mxUtils.getDouble(var3, "width"));
      var1.setHeight(mxUtils.getDouble(var3, "height"));
      List var4 = parsePoints(mxUtils.getString(var3, "points"));
      if (var4.size() > 0) {
         var1.setAbsolutePoints(var4);
      }

      String var5 = mxUtils.getString(var3, "label");
      if (var5 != null && var5.length() > 0) {
         mxPoint var6 = new mxPoint(mxUtils.getDouble(var3, "dx"), mxUtils.getDouble(var3, "dy"));
         mxCellState var7 = !var2 ? var1 : null;
         var1.setLabelBounds(mxUtils.getLabelPaintBounds(var5, var1.getStyle(), mxUtils.isTrue(var3, "html", false), var6, var7, this.scale));
      }

      return var5;
   }

   public static List parsePoints(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         int var2 = var0.length();
         String var3 = "";
         String var4 = null;

         for(int var5 = 0; var5 < var2; ++var5) {
            char var6 = var0.charAt(var5);
            if (var6 != ',' && var6 != ' ') {
               var3 = var3 + var6;
            } else {
               if (var4 == null) {
                  var4 = var3;
               } else {
                  var1.add(new mxPoint(Double.parseDouble(var4), Double.parseDouble(var3)));
                  var4 = null;
               }

               var3 = "";
            }
         }

         var1.add(new mxPoint(Double.parseDouble(var4), Double.parseDouble(var3)));
      }

      return var1;
   }
}
