package com.mxgraph.canvas;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class mxVmlCanvas extends mxBasicCanvas {
   protected Document document;

   public mxVmlCanvas() {
      this((Document)null);
   }

   public mxVmlCanvas(Document var1) {
      this.setDocument(var1);
   }

   public void setDocument(Document var1) {
      this.document = var1;
   }

   public Document getDocument() {
      return this.document;
   }

   public void appendVmlElement(Element var1) {
      if (this.document != null) {
         Node var2 = this.document.getDocumentElement().getFirstChild().getNextSibling();
         if (var2 != null) {
            var2.appendChild(var1);
         }
      }

   }

   public Object drawCell(mxCellState var1) {
      Map var2 = var1.getStyle();
      Element var3 = null;
      if (var1.getAbsolutePointCount() > 1) {
         List var4 = var1.getAbsolutePoints();
         var4 = mxUtils.translatePoints(var4, (double)this.translate.x, (double)this.translate.y);
         var3 = this.drawLine(var4, var2);
         Element var5 = this.document.createElement("v:stroke");
         String var6 = mxUtils.getString(var2, mxConstants.STYLE_STARTARROW);
         String var7 = mxUtils.getString(var2, mxConstants.STYLE_ENDARROW);
         if (var6 != null || var7 != null) {
            String var8;
            String var9;
            double var10;
            if (var6 != null) {
               var5.setAttribute("startarrow", var6);
               var8 = "medium";
               var9 = "medium";
               var10 = (double)mxUtils.getFloat(var2, mxConstants.STYLE_STARTSIZE, (float)mxConstants.DEFAULT_MARKERSIZE) * this.scale;
               if (var10 < 6.0) {
                  var8 = "narrow";
                  var9 = "short";
               } else if (var10 > 10.0) {
                  var8 = "wide";
                  var9 = "long";
               }

               var5.setAttribute("startarrowwidth", var8);
               var5.setAttribute("startarrowlength", var9);
            }

            if (var7 != null) {
               var5.setAttribute("endarrow", var7);
               var8 = "medium";
               var9 = "medium";
               var10 = (double)mxUtils.getFloat(var2, mxConstants.STYLE_ENDSIZE, (float)mxConstants.DEFAULT_MARKERSIZE) * this.scale;
               if (var10 < 6.0) {
                  var8 = "narrow";
                  var9 = "short";
               } else if (var10 > 10.0) {
                  var8 = "wide";
                  var9 = "long";
               }

               var5.setAttribute("endarrowwidth", var8);
               var5.setAttribute("endarrowlength", var9);
            }
         }

         if (mxUtils.isTrue(var2, mxConstants.STYLE_DASHED)) {
            var5.setAttribute("dashstyle", "2 2");
         }

         var3.appendChild(var5);
      } else {
         int var12 = (int)var1.getX() + this.translate.x;
         int var13 = (int)var1.getY() + this.translate.y;
         int var14 = (int)var1.getWidth();
         int var15 = (int)var1.getHeight();
         if (!mxUtils.getString(var2, mxConstants.STYLE_SHAPE, "").equals("swimlane")) {
            var3 = this.drawShape(var12, var13, var14, var15, var2);
            if (mxUtils.isTrue(var2, mxConstants.STYLE_DASHED)) {
               Element var16 = this.document.createElement("v:stroke");
               var16.setAttribute("dashstyle", "2 2");
               var3.appendChild(var16);
            }
         } else {
            int var17 = (int)Math.round((double)mxUtils.getInt(var2, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE) * this.scale);
            Hashtable var18 = new Hashtable(var2);
            var18.remove(mxConstants.STYLE_FILLCOLOR);
            var18.remove(mxConstants.STYLE_ROUNDED);
            if (mxUtils.isTrue(var2, mxConstants.STYLE_HORIZONTAL, true)) {
               var3 = this.drawShape(var12, var13, var14, var17, var2);
               this.drawShape(var12, var13 + var17, var14, var15 - var17, var18);
            } else {
               var3 = this.drawShape(var12, var13, var17, var15, var2);
               this.drawShape(var12 + var17, var13, var14 - var17, var15, var18);
            }
         }
      }

      return var3;
   }

   public Object drawLabel(String var1, mxCellState var2, boolean var3) {
      mxRectangle var4 = var2.getLabelBounds();
      if (this.drawLabels && var4 != null) {
         int var5 = (int)var4.getX() + this.translate.x;
         int var6 = (int)var4.getY() + this.translate.y;
         int var7 = (int)var4.getWidth();
         int var8 = (int)var4.getHeight();
         Map var9 = var2.getStyle();
         return this.drawText(var1, var5, var6, var7, var8, var9);
      } else {
         return null;
      }
   }

   public Element drawShape(int var1, int var2, int var3, int var4, Map var5) {
      String var6 = mxUtils.getString(var5, mxConstants.STYLE_FILLCOLOR);
      String var7 = mxUtils.getString(var5, mxConstants.STYLE_STROKECOLOR);
      float var8 = (float)((double)mxUtils.getFloat(var5, mxConstants.STYLE_STROKEWIDTH, 1.0F) * this.scale);
      String var9 = mxUtils.getString(var5, mxConstants.STYLE_SHAPE);
      Element var10 = null;
      String var11;
      if (var9.equals("image")) {
         var11 = this.getImageForStyle(var5);
         if (var11 != null) {
            var10 = this.document.createElement("v:img");
            var10.setAttribute("src", var11);
         }
      } else {
         String var12;
         if (var9.equals("line")) {
            var11 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "east");
            var12 = null;
            int var13;
            if (!var11.equals("east") && !var11.equals("west")) {
               var13 = Math.round((float)(var3 / 2));
               var12 = "m " + var13 + " 0 L " + var13 + " " + var4;
            } else {
               var13 = Math.round((float)(var4 / 2));
               var12 = "m 0 " + var13 + " l " + var3 + " " + var13;
            }

            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            var10.setAttribute("path", var12 + " x e");
         } else if (var9.equals("ellipse")) {
            var10 = this.document.createElement("v:oval");
         } else if (var9.equals("doubleEllipse")) {
            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            int var16 = (int)((double)(3.0F + var8) * this.scale);
            var12 = "ar 0 0 " + var3 + " " + var4 + " 0 " + var4 / 2 + " " + var3 / 2 + " " + var4 / 2 + " e ar " + var16 + " " + var16 + " " + (var3 - var16) + " " + (var4 - var16) + " 0 " + var4 / 2 + " " + var3 / 2 + " " + var4 / 2;
            var10.setAttribute("path", var12 + " x e");
         } else if (var9.equals("rhombus")) {
            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            var11 = "m " + var3 / 2 + " 0 l " + var3 + " " + var4 / 2 + " l " + var3 / 2 + " " + var4 + " l 0 " + var4 / 2;
            var10.setAttribute("path", var11 + " x e");
         } else if (var9.equals("triangle")) {
            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            var11 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "");
            var12 = null;
            if (var11.equals("north")) {
               var12 = "m 0 " + var4 + " l " + var3 / 2 + " 0 " + " l " + var3 + " " + var4;
            } else if (var11.equals("south")) {
               var12 = "m 0 0 l " + var3 / 2 + " " + var4 + " l " + var3 + " 0";
            } else if (var11.equals("west")) {
               var12 = "m " + var3 + " 0 l " + var3 + " " + var4 / 2 + " l " + var3 + " " + var4;
            } else {
               var12 = "m 0 0 l " + var3 + " " + var4 / 2 + " l 0 " + var4;
            }

            var10.setAttribute("path", var12 + " x e");
         } else if (var9.equals("hexagon")) {
            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            var11 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "");
            var12 = null;
            if (!var11.equals("north") && !var11.equals("south")) {
               var12 = "m " + (int)(0.25 * (double)var3) + " 0 l " + (int)(0.75 * (double)var3) + " 0 l " + var3 + " " + (int)(0.5 * (double)var4) + " l " + (int)(0.75 * (double)var3) + " " + var4 + " l " + (int)(0.25 * (double)var3) + " " + var4 + " l 0 " + (int)(0.5 * (double)var4);
            } else {
               var12 = "m " + (int)(0.5 * (double)var3) + " 0 l " + var3 + " " + (int)(0.25 * (double)var4) + " l " + var3 + " " + (int)(0.75 * (double)var4) + " l " + (int)(0.5 * (double)var3) + " " + var4 + " l 0 " + (int)(0.75 * (double)var4) + " l 0 " + (int)(0.25 * (double)var4);
            }

            var10.setAttribute("path", var12 + " x e");
         } else if (var9.equals("cloud")) {
            var10 = this.document.createElement("v:shape");
            var10.setAttribute("coordsize", var3 + " " + var4);
            var11 = "m " + (int)(0.25 * (double)var3) + " " + (int)(0.25 * (double)var4) + " c " + (int)(0.05 * (double)var3) + " " + (int)(0.25 * (double)var4) + " 0 " + (int)(0.5 * (double)var4) + " " + (int)(0.16 * (double)var3) + " " + (int)(0.55 * (double)var4) + " c 0 " + (int)(0.66 * (double)var4) + " " + (int)(0.18 * (double)var3) + " " + (int)(0.9 * (double)var4) + " " + (int)(0.31 * (double)var3) + " " + (int)(0.8 * (double)var4) + " c " + (int)(0.4 * (double)var3) + " " + var4 + " " + (int)(0.7 * (double)var3) + " " + var4 + " " + (int)(0.8 * (double)var3) + " " + (int)(0.8 * (double)var4) + " c " + var3 + " " + (int)(0.8 * (double)var4) + " " + var3 + " " + (int)(0.6 * (double)var4) + " " + (int)(0.875 * (double)var3) + " " + (int)(0.5 * (double)var4) + " c " + var3 + " " + (int)(0.3 * (double)var4) + " " + (int)(0.8 * (double)var3) + " " + (int)(0.1 * (double)var4) + " " + (int)(0.625 * (double)var3) + " " + (int)(0.2 * (double)var4) + " c " + (int)(0.5 * (double)var3) + " " + (int)(0.05 * (double)var4) + " " + (int)(0.3 * (double)var3) + " " + (int)(0.05 * (double)var4) + " " + (int)(0.25 * (double)var3) + " " + (int)(0.25 * (double)var4);
            var10.setAttribute("path", var11 + " x e");
         } else {
            double var17;
            String var18;
            if (var9.equals("actor")) {
               var10 = this.document.createElement("v:shape");
               var10.setAttribute("coordsize", var3 + " " + var4);
               var17 = (double)(var3 / 3);
               var18 = "m 0 " + var4 + " C 0 " + 3 * var4 / 5 + " 0 " + 2 * var4 / 5 + " " + var3 / 2 + " " + 2 * var4 / 5 + " c " + (int)((double)(var3 / 2) - var17) + " " + 2 * var4 / 5 + " " + (int)((double)(var3 / 2) - var17) + " 0 " + var3 / 2 + " 0 c " + (int)((double)(var3 / 2) + var17) + " 0 " + (int)((double)(var3 / 2) + var17) + " " + 2 * var4 / 5 + " " + var3 / 2 + " " + 2 * var4 / 5 + " c " + var3 + " " + 2 * var4 / 5 + " " + var3 + " " + 3 * var4 / 5 + " " + var3 + " " + var4;
               var10.setAttribute("path", var18 + " x e");
            } else if (var9.equals("cylinder")) {
               var10 = this.document.createElement("v:shape");
               var10.setAttribute("coordsize", var3 + " " + var4);
               var17 = Math.min(40.0, Math.floor((double)(var4 / 5)));
               var18 = "m 0 " + (int)var17 + " C 0 " + (int)(var17 / 3.0) + " " + var3 + " " + (int)(var17 / 3.0) + " " + var3 + " " + (int)var17 + " L " + var3 + " " + (int)((double)var4 - var17) + " C " + var3 + " " + (int)((double)var4 + var17 / 3.0) + " 0 " + (int)((double)var4 + var17 / 3.0) + " 0 " + (int)((double)var4 - var17) + " x e" + " m 0 " + (int)var17 + " C 0 " + (int)(2.0 * var17) + " " + var3 + " " + (int)(2.0 * var17) + " " + var3 + " " + (int)var17;
               var10.setAttribute("path", var18 + " e");
            } else if (mxUtils.isTrue(var5, mxConstants.STYLE_ROUNDED, false)) {
               var10 = this.document.createElement("v:roundrect");
               var10.setAttribute("arcsize", mxConstants.RECTANGLE_ROUNDING_FACTOR * 100.0 + "%");
            } else {
               var10 = this.document.createElement("v:rect");
            }
         }
      }

      var11 = "position:absolute;left:" + String.valueOf(var1) + "px;top:" + var2 + "px;width:" + var3 + "px;height:" + var4 + "px;";
      double var20 = mxUtils.getDouble(var5, mxConstants.STYLE_ROTATION);
      if (var20 != 0.0) {
         var11 = var11 + "rotation:" + var20 + ";";
      }

      var10.setAttribute("style", var11);
      if (mxUtils.isTrue(var5, mxConstants.STYLE_SHADOW, false) && var6 != null) {
         Element var14 = this.document.createElement("v:shadow");
         var14.setAttribute("on", "true");
         var14.setAttribute("color", mxConstants.W3C_SHADOWCOLOR);
         var10.appendChild(var14);
      }

      float var19 = mxUtils.getFloat(var5, mxConstants.STYLE_OPACITY, 100.0F);
      Element var15;
      if (var6 != null) {
         var15 = this.document.createElement("v:fill");
         var15.setAttribute("color", var6);
         if (var19 != 100.0F) {
            var15.setAttribute("opacity", String.valueOf(var19 / 100.0F));
         }

         var10.appendChild(var15);
      } else {
         var10.setAttribute("filled", "false");
      }

      if (var7 != null) {
         var10.setAttribute("strokecolor", var7);
         var15 = this.document.createElement("v:stroke");
         if (var19 != 100.0F) {
            var15.setAttribute("opacity", String.valueOf(var19 / 100.0F));
         }

         var10.appendChild(var15);
      } else {
         var10.setAttribute("stroked", "false");
      }

      var10.setAttribute("strokeweight", var8 + "pt");
      this.appendVmlElement(var10);
      return var10;
   }

   public Element drawLine(List var1, Map var2) {
      String var3 = mxUtils.getString(var2, mxConstants.STYLE_STROKECOLOR);
      float var4 = (float)((double)mxUtils.getFloat(var2, mxConstants.STYLE_STROKEWIDTH, 1.0F) * this.scale);
      Element var5 = this.document.createElement("v:shape");
      if (var3 != null && var4 > 0.0F) {
         mxPoint var6 = (mxPoint)var1.get(0);
         Rectangle var7 = new Rectangle(var6.getPoint());
         String var8 = "m " + Math.round(var6.getX()) + " " + Math.round(var6.getY());

         for(int var9 = 1; var9 < var1.size(); ++var9) {
            var6 = (mxPoint)var1.get(var9);
            var8 = var8 + " l " + Math.round(var6.getX()) + " " + Math.round(var6.getY());
            var7 = var7.union(new Rectangle(var6.getPoint()));
         }

         var5.setAttribute("path", var8);
         var5.setAttribute("filled", "false");
         var5.setAttribute("strokecolor", var3);
         var5.setAttribute("strokeweight", var4 + "pt");
         String var10 = "position:absolute;left:" + String.valueOf(var7.x) + "px;" + "top:" + var7.y + "px;" + "width:" + var7.width + "px;" + "height:" + var7.height + "px;";
         var5.setAttribute("style", var10);
         var5.setAttribute("coordorigin", var7.x + " " + var7.y);
         var5.setAttribute("coordsize", var7.width + " " + var7.height);
      }

      this.appendVmlElement(var5);
      return var5;
   }

   public Element drawText(String var1, int var2, int var3, int var4, int var5, Map var6) {
      Element var7 = mxUtils.createTable(this.document, var1, var2, var3, var4, var5, this.scale, var6);
      this.appendVmlElement(var7);
      return var7;
   }
}
