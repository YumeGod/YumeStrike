package com.mxgraph.canvas;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class mxSvgCanvas extends mxBasicCanvas {
   protected Document document;

   public mxSvgCanvas() {
      this((Document)null);
   }

   public mxSvgCanvas(Document var1) {
      this.setDocument(var1);
   }

   public void appendSvgElement(Element var1) {
      if (this.document != null) {
         this.document.getDocumentElement().appendChild(var1);
      }

   }

   public void setDocument(Document var1) {
      this.document = var1;
   }

   public Document getDocument() {
      return this.document;
   }

   public Object drawCell(mxCellState var1) {
      Map var2 = var1.getStyle();
      Element var3 = null;
      if (var1.getAbsolutePointCount() > 1) {
         List var4 = var1.getAbsolutePoints();
         var4 = mxUtils.translatePoints(var4, (double)this.translate.x, (double)this.translate.y);
         var3 = this.drawLine(var4, var2);
         float var5 = mxUtils.getFloat(var2, mxConstants.STYLE_OPACITY, 100.0F);
         if (var5 != 100.0F) {
            String var6 = String.valueOf(var5 / 100.0F);
            var3.setAttribute("fill-opacity", var6);
            var3.setAttribute("stroke-opacity", var6);
         }
      } else {
         int var10 = (int)var1.getX() + this.translate.x;
         int var11 = (int)var1.getY() + this.translate.y;
         int var12 = (int)var1.getWidth();
         int var7 = (int)var1.getHeight();
         if (!mxUtils.getString(var2, mxConstants.STYLE_SHAPE, "").equals("swimlane")) {
            var3 = this.drawShape(var10, var11, var12, var7, var2);
         } else {
            int var8 = (int)Math.round((double)mxUtils.getInt(var2, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE) * this.scale);
            Hashtable var9 = new Hashtable(var2);
            var9.remove(mxConstants.STYLE_FILLCOLOR);
            var9.remove(mxConstants.STYLE_ROUNDED);
            if (mxUtils.isTrue(var2, mxConstants.STYLE_HORIZONTAL, true)) {
               var3 = this.drawShape(var10, var11, var12, var8, var2);
               this.drawShape(var10, var11 + var8, var12, var7 - var8, var9);
            } else {
               var3 = this.drawShape(var10, var11, var8, var7, var2);
               this.drawShape(var10 + var8, var11, var12 - var8, var7, var9);
            }
         }
      }

      return var3;
   }

   public Object drawLabel(String var1, mxCellState var2, boolean var3) {
      mxRectangle var4 = var2.getLabelBounds();
      if (this.drawLabels) {
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
      String var6 = mxUtils.getString(var5, mxConstants.STYLE_FILLCOLOR, "none");
      String var7 = mxUtils.getString(var5, mxConstants.STYLE_STROKECOLOR);
      float var8 = (float)((double)mxUtils.getFloat(var5, mxConstants.STYLE_STROKEWIDTH, 1.0F) * this.scale);
      String var9 = mxUtils.getString(var5, mxConstants.STYLE_SHAPE);
      Element var10 = null;
      Element var11 = null;
      String var12;
      Element var19;
      if (var9.equals("image")) {
         var12 = this.getImageForStyle(var5);
         if (var12 != null) {
            var10 = this.document.createElement("image");
            var10.setAttribute("x", String.valueOf(var1));
            var10.setAttribute("y", String.valueOf(var2));
            var10.setAttribute("width", String.valueOf(var3));
            var10.setAttribute("height", String.valueOf(var4));
            var10.setAttributeNS(mxConstants.NS_XLINK, "xlink:href", var12);
         }
      } else {
         String var13;
         if (var9.equals("line")) {
            var12 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "east");
            var13 = null;
            int var14;
            if (!var12.equals("east") && !var12.equals("west")) {
               var14 = var1 + var3 / 2;
               var13 = "M " + var14 + " " + var2 + " L " + var14 + " " + (var2 + var4);
            } else {
               var14 = var2 + var4 / 2;
               var13 = "M " + var1 + " " + var14 + " L " + (var1 + var3) + " " + var14;
            }

            var10 = this.document.createElement("path");
            var10.setAttribute("d", var13 + " Z");
         } else if (var9.equals("ellipse")) {
            var10 = this.document.createElement("ellipse");
            var10.setAttribute("cx", String.valueOf(var1 + var3 / 2));
            var10.setAttribute("cy", String.valueOf(var2 + var4 / 2));
            var10.setAttribute("rx", String.valueOf(var3 / 2));
            var10.setAttribute("ry", String.valueOf(var4 / 2));
         } else if (var9.equals("doubleEllipse")) {
            var10 = this.document.createElement("g");
            var11 = this.document.createElement("ellipse");
            var11.setAttribute("cx", String.valueOf(var1 + var3 / 2));
            var11.setAttribute("cy", String.valueOf(var2 + var4 / 2));
            var11.setAttribute("rx", String.valueOf(var3 / 2));
            var11.setAttribute("ry", String.valueOf(var4 / 2));
            var10.appendChild(var11);
            int var18 = (int)((double)(3.0F + var8) * this.scale);
            var19 = this.document.createElement("ellipse");
            var19.setAttribute("fill", "none");
            var19.setAttribute("stroke", var7);
            var19.setAttribute("stroke-width", String.valueOf(var8));
            var19.setAttribute("cx", String.valueOf(var1 + var3 / 2));
            var19.setAttribute("cy", String.valueOf(var2 + var4 / 2));
            var19.setAttribute("rx", String.valueOf(var3 / 2 - var18));
            var19.setAttribute("ry", String.valueOf(var4 / 2 - var18));
            var10.appendChild(var19);
         } else if (var9.equals("rhombus")) {
            var10 = this.document.createElement("path");
            var12 = "M " + (var1 + var3 / 2) + " " + var2 + " L " + (var1 + var3) + " " + (var2 + var4 / 2) + " L " + (var1 + var3 / 2) + " " + (var2 + var4) + " L " + var1 + " " + (var2 + var4 / 2);
            var10.setAttribute("d", var12 + " Z");
         } else if (var9.equals("triangle")) {
            var10 = this.document.createElement("path");
            var12 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "");
            var13 = null;
            if (var12.equals("north")) {
               var13 = "M " + var1 + " " + (var2 + var4) + " L " + (var1 + var3 / 2) + " " + var2 + " L " + (var1 + var3) + " " + (var2 + var4);
            } else if (var12.equals("south")) {
               var13 = "M " + var1 + " " + var2 + " L " + (var1 + var3 / 2) + " " + (var2 + var4) + " L " + (var1 + var3) + " " + var2;
            } else if (var12.equals("west")) {
               var13 = "M " + (var1 + var3) + " " + var2 + " L " + var1 + " " + (var2 + var4 / 2) + " L " + (var1 + var3) + " " + (var2 + var4);
            } else {
               var13 = "M " + var1 + " " + var2 + " L " + (var1 + var3) + " " + (var2 + var4 / 2) + " L " + var1 + " " + (var2 + var4);
            }

            var10.setAttribute("d", var13 + " Z");
         } else if (var9.equals("hexagon")) {
            var10 = this.document.createElement("path");
            var12 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "");
            var13 = null;
            if (!var12.equals("north") && !var12.equals("south")) {
               var13 = "M " + ((double)var1 + 0.25 * (double)var3) + " " + var2 + " L " + ((double)var1 + 0.75 * (double)var3) + " " + var2 + " L " + (var1 + var3) + " " + ((double)var2 + 0.5 * (double)var4) + " L " + ((double)var1 + 0.75 * (double)var3) + " " + (var2 + var4) + " L " + ((double)var1 + 0.25 * (double)var3) + " " + (var2 + var4) + " L " + var1 + " " + ((double)var2 + 0.5 * (double)var4);
            } else {
               var13 = "M " + ((double)var1 + 0.5 * (double)var3) + " " + var2 + " L " + (var1 + var3) + " " + ((double)var2 + 0.25 * (double)var4) + " L " + (var1 + var3) + " " + ((double)var2 + 0.75 * (double)var4) + " L " + ((double)var1 + 0.5 * (double)var3) + " " + (var2 + var4) + " L " + var1 + " " + ((double)var2 + 0.75 * (double)var4) + " L " + var1 + " " + ((double)var2 + 0.25 * (double)var4);
            }

            var10.setAttribute("d", var13 + " Z");
         } else if (var9.equals("cloud")) {
            var10 = this.document.createElement("path");
            var12 = "M " + ((double)var1 + 0.25 * (double)var3) + " " + ((double)var2 + 0.25 * (double)var4) + " C " + ((double)var1 + 0.05 * (double)var3) + " " + ((double)var2 + 0.25 * (double)var4) + " " + var1 + " " + ((double)var2 + 0.5 * (double)var4) + " " + ((double)var1 + 0.16 * (double)var3) + " " + ((double)var2 + 0.55 * (double)var4) + " C " + var1 + " " + ((double)var2 + 0.66 * (double)var4) + " " + ((double)var1 + 0.18 * (double)var3) + " " + ((double)var2 + 0.9 * (double)var4) + " " + ((double)var1 + 0.31 * (double)var3) + " " + ((double)var2 + 0.8 * (double)var4) + " C " + ((double)var1 + 0.4 * (double)var3) + " " + (var2 + var4) + " " + ((double)var1 + 0.7 * (double)var3) + " " + (var2 + var4) + " " + ((double)var1 + 0.8 * (double)var3) + " " + ((double)var2 + 0.8 * (double)var4) + " C " + (var1 + var3) + " " + ((double)var2 + 0.8 * (double)var4) + " " + (var1 + var3) + " " + ((double)var2 + 0.6 * (double)var4) + " " + ((double)var1 + 0.875 * (double)var3) + " " + ((double)var2 + 0.5 * (double)var4) + " C " + (var1 + var3) + " " + ((double)var2 + 0.3 * (double)var4) + " " + ((double)var1 + 0.8 * (double)var3) + " " + ((double)var2 + 0.1 * (double)var4) + " " + ((double)var1 + 0.625 * (double)var3) + " " + ((double)var2 + 0.2 * (double)var4) + " C " + ((double)var1 + 0.5 * (double)var3) + " " + ((double)var2 + 0.05 * (double)var4) + " " + ((double)var1 + 0.3 * (double)var3) + " " + ((double)var2 + 0.05 * (double)var4) + " " + ((double)var1 + 0.25 * (double)var3) + " " + ((double)var2 + 0.25 * (double)var4);
            var10.setAttribute("d", var12 + " Z");
         } else {
            double var20;
            String var21;
            if (var9.equals("actor")) {
               var10 = this.document.createElement("path");
               var20 = (double)(var3 / 3);
               var21 = " M " + var1 + " " + (var2 + var4) + " C " + var1 + " " + (var2 + 3 * var4 / 5) + " " + var1 + " " + (var2 + 2 * var4 / 5) + " " + (var1 + var3 / 2) + " " + (var2 + 2 * var4 / 5) + " C " + ((double)(var1 + var3 / 2) - var20) + " " + (var2 + 2 * var4 / 5) + " " + ((double)(var1 + var3 / 2) - var20) + " " + var2 + " " + (var1 + var3 / 2) + " " + var2 + " C " + ((double)(var1 + var3 / 2) + var20) + " " + var2 + " " + ((double)(var1 + var3 / 2) + var20) + " " + (var2 + 2 * var4 / 5) + " " + (var1 + var3 / 2) + " " + (var2 + 2 * var4 / 5) + " C " + (var1 + var3) + " " + (var2 + 2 * var4 / 5) + " " + (var1 + var3) + " " + (var2 + 3 * var4 / 5) + " " + (var1 + var3) + " " + (var2 + var4);
               var10.setAttribute("d", var21 + " Z");
            } else if (var9.equals("cylinder")) {
               var10 = this.document.createElement("g");
               var11 = this.document.createElement("path");
               var20 = Math.min(40.0, Math.floor((double)(var4 / 5)));
               var21 = " M " + var1 + " " + ((double)var2 + var20) + " C " + var1 + " " + ((double)var2 - var20 / 3.0) + " " + (var1 + var3) + " " + ((double)var2 - var20 / 3.0) + " " + (var1 + var3) + " " + ((double)var2 + var20) + " L " + (var1 + var3) + " " + ((double)(var2 + var4) - var20) + " C " + (var1 + var3) + " " + ((double)(var2 + var4) + var20 / 3.0) + " " + var1 + " " + ((double)(var2 + var4) + var20 / 3.0) + " " + var1 + " " + ((double)(var2 + var4) - var20);
               var11.setAttribute("d", var21 + " Z");
               var10.appendChild(var11);
               Element var15 = this.document.createElement("path");
               var21 = "M " + var1 + " " + ((double)var2 + var20) + " C " + var1 + " " + ((double)var2 + 2.0 * var20) + " " + (var1 + var3) + " " + ((double)var2 + 2.0 * var20) + " " + (var1 + var3) + " " + ((double)var2 + var20);
               var15.setAttribute("d", var21);
               var15.setAttribute("fill", "none");
               var15.setAttribute("stroke", var7);
               var15.setAttribute("stroke-width", String.valueOf(var8));
               var10.appendChild(var15);
            } else {
               var10 = this.document.createElement("rect");
               var10.setAttribute("x", String.valueOf(var1));
               var10.setAttribute("y", String.valueOf(var2));
               var10.setAttribute("width", String.valueOf(var3));
               var10.setAttribute("height", String.valueOf(var4));
               if (mxUtils.isTrue(var5, mxConstants.STYLE_ROUNDED, false)) {
                  var10.setAttribute("rx", String.valueOf((double)var3 * mxConstants.RECTANGLE_ROUNDING_FACTOR));
                  var10.setAttribute("ry", String.valueOf((double)var4 * mxConstants.RECTANGLE_ROUNDING_FACTOR));
               }
            }
         }
      }

      Element var22 = var11;
      if (var11 == null) {
         var22 = var10;
      }

      var22.setAttribute("fill", var6);
      var22.setAttribute("stroke", var7);
      var22.setAttribute("stroke-width", String.valueOf(var8));
      var19 = null;
      if (mxUtils.isTrue(var5, mxConstants.STYLE_SHADOW, false) && !var6.equals("none")) {
         var19 = (Element)var22.cloneNode(true);
         var19.setAttribute("transform", mxConstants.SVG_SHADOWTRANSFORM);
         var19.setAttribute("fill", mxConstants.W3C_SHADOWCOLOR);
         var19.setAttribute("stroke", mxConstants.W3C_SHADOWCOLOR);
         var19.setAttribute("stroke-width", String.valueOf(var8));
         this.appendSvgElement(var19);
      }

      double var23 = mxUtils.getDouble(var5, mxConstants.STYLE_ROTATION);
      if (var23 != 0.0) {
         int var16 = var1 + var3 / 2;
         int var17 = var2 + var4 / 2;
         var10.setAttribute("transform", "rotate(" + var23 + "," + var16 + "," + var17 + ")");
         if (var19 != null) {
            var19.setAttribute("transform", "rotate(" + var23 + "," + var16 + "," + var17 + ") " + mxConstants.SVG_SHADOWTRANSFORM);
         }
      }

      float var24 = mxUtils.getFloat(var5, mxConstants.STYLE_OPACITY, 100.0F);
      if (var24 != 100.0F) {
         String var25 = String.valueOf(var24 / 100.0F);
         var10.setAttribute("fill-opacity", var25);
         var10.setAttribute("stroke-opacity", var25);
         if (var19 != null) {
            var19.setAttribute("fill-opacity", var25);
            var19.setAttribute("stroke-opacity", var25);
         }
      }

      if (mxUtils.isTrue(var5, mxConstants.STYLE_DASHED)) {
         var10.setAttribute("stroke-dasharray", "3, 3");
      }

      this.appendSvgElement(var10);
      return var10;
   }

   public Element drawLine(List var1, Map var2) {
      Element var3 = this.document.createElement("g");
      Element var4 = this.document.createElement("path");
      String var5 = mxUtils.getString(var2, mxConstants.STYLE_STROKECOLOR);
      float var6 = mxUtils.getFloat(var2, mxConstants.STYLE_STROKEWIDTH, 1.0F);
      float var7 = (float)((double)var6 * this.scale);
      if (var5 != null && var7 > 0.0F) {
         Object var8 = var2.get(mxConstants.STYLE_STARTARROW);
         mxPoint var9 = (mxPoint)var1.get(1);
         mxPoint var10 = (mxPoint)var1.get(0);
         mxPoint var11 = null;
         if (var8 != null) {
            float var12 = mxUtils.getFloat(var2, mxConstants.STYLE_STARTSIZE, (float)mxConstants.DEFAULT_MARKERSIZE);
            var11 = this.drawMarker(var3, var8, var9, var10, var12, var6, var5);
         } else {
            double var23 = var9.getX() - var10.getX();
            double var14 = var9.getY() - var10.getY();
            double var16 = Math.max(1.0, Math.sqrt(var23 * var23 + var14 * var14));
            double var18 = var23 * (double)var7 / var16;
            double var20 = var14 * (double)var7 / var16;
            var11 = new mxPoint(var18 / 2.0, var20 / 2.0);
         }

         if (var11 != null) {
            var10 = (mxPoint)var10.clone();
            var10.setX(var10.getX() + var11.getX());
            var10.setY(var10.getY() + var11.getY());
            var11 = null;
         }

         var8 = var2.get(mxConstants.STYLE_ENDARROW);
         var9 = (mxPoint)var1.get(var1.size() - 2);
         mxPoint var24 = (mxPoint)var1.get(var1.size() - 1);
         if (var8 != null) {
            float var13 = mxUtils.getFloat(var2, mxConstants.STYLE_ENDSIZE, (float)mxConstants.DEFAULT_MARKERSIZE);
            var11 = this.drawMarker(var3, var8, var9, var24, var13, var6, var5);
         } else {
            double var25 = var9.getX() - var10.getX();
            double var15 = var9.getY() - var10.getY();
            double var17 = Math.max(1.0, Math.sqrt(var25 * var25 + var15 * var15));
            double var19 = var25 * (double)var7 / var17;
            double var21 = var15 * (double)var7 / var17;
            var11 = new mxPoint(var19 / 2.0, var21 / 2.0);
         }

         if (var11 != null) {
            var24 = (mxPoint)var24.clone();
            var24.setX(var24.getX() + var11.getX());
            var24.setY(var24.getY() + var11.getY());
            var11 = null;
         }

         String var26 = "M " + var10.getX() + " " + var10.getY();

         for(int var27 = 1; var27 < var1.size() - 1; ++var27) {
            var9 = (mxPoint)var1.get(var27);
            var26 = var26 + " L " + var9.getX() + " " + var9.getY();
         }

         var26 = var26 + " L " + var24.getX() + " " + var24.getY();
         var4.setAttribute("d", var26);
         var4.setAttribute("stroke", var5);
         var4.setAttribute("fill", "none");
         var4.setAttribute("stroke-width", String.valueOf(var7));
         if (mxUtils.isTrue(var2, mxConstants.STYLE_DASHED)) {
            var4.setAttribute("stroke-dasharray", "3, 3");
         }

         var3.appendChild(var4);
         this.appendSvgElement(var3);
      }

      return var3;
   }

   public mxPoint drawMarker(Element var1, Object var2, mxPoint var3, mxPoint var4, float var5, float var6, String var7) {
      Object var8 = null;
      double var9 = var4.getX() - var3.getX();
      double var11 = var4.getY() - var3.getY();
      double var13 = Math.max(1.0, Math.sqrt(var9 * var9 + var11 * var11));
      double var15 = (double)var5 * this.scale;
      double var17 = var9 * var15 / var13;
      double var19 = var11 * var15 / var13;
      var4 = (mxPoint)var4.clone();
      var4.setX(var4.getX() - var17 * (double)var6 / (double)(2.0F * var5));
      var4.setY(var4.getY() - var19 * (double)var6 / (double)(2.0F * var5));
      var17 *= 0.5 + (double)(var6 / 2.0F);
      var19 *= 0.5 + (double)(var6 / 2.0F);
      Element var21 = this.document.createElement("path");
      var21.setAttribute("stroke-width", String.valueOf((double)var6 * this.scale));
      var21.setAttribute("stroke", var7);
      var21.setAttribute("fill", var7);
      String var22 = null;
      if (!var2.equals("classic") && !var2.equals("block")) {
         if (var2.equals("open")) {
            var17 *= 1.2;
            var19 *= 1.2;
            var22 = "M " + (var4.getX() - var17 - var19 / 2.0) + " " + (var4.getY() - var19 + var17 / 2.0) + " L " + (var4.getX() - var17 / 6.0) + " " + (var4.getY() - var19 / 6.0) + " L " + (var4.getX() + var19 / 2.0 - var17) + " " + (var4.getY() - var19 - var17 / 2.0) + " M " + var4.getX() + " " + var4.getY();
            var21.setAttribute("fill", "none");
         } else if (var2.equals("oval")) {
            var17 *= 1.2;
            var19 *= 1.2;
            var15 *= 1.2;
            var22 = "M " + (var4.getX() - var19 / 2.0) + " " + (var4.getY() + var17 / 2.0) + " a " + var15 / 2.0 + " " + var15 / 2.0 + " 0  1,1 " + var17 / 8.0 + " " + var19 / 8.0 + " z";
         } else if (var2.equals("diamond")) {
            var22 = "M " + (var4.getX() + var17 / 2.0) + " " + (var4.getY() + var19 / 2.0) + " L " + (var4.getX() - var19 / 2.0) + " " + (var4.getY() + var17 / 2.0) + " L " + (var4.getX() - var17 / 2.0) + " " + (var4.getY() - var19 / 2.0) + " L " + (var4.getX() + var19 / 2.0) + " " + (var4.getY() - var17 / 2.0) + " z";
         }
      } else {
         var22 = "M " + var4.getX() + " " + var4.getY() + " L " + (var4.getX() - var17 - var19 / 2.0) + " " + (var4.getY() - var19 + var17 / 2.0) + (!var2.equals("classic") ? "" : " L " + (var4.getX() - var17 * 3.0 / 4.0) + " " + (var4.getY() - var19 * 3.0 / 4.0)) + " L " + (var4.getX() + var19 / 2.0 - var17) + " " + (var4.getY() - var19 - var17 / 2.0) + " z";
      }

      if (var22 != null) {
         var21.setAttribute("d", var22);
         var1.appendChild(var21);
      }

      return (mxPoint)var8;
   }

   public Object drawText(String var1, int var2, int var3, int var4, int var5, Map var6) {
      Element var7 = null;
      String var8 = mxUtils.getString(var6, mxConstants.STYLE_FONTCOLOR, "black");
      String var9 = mxUtils.getString(var6, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILIES);
      int var10 = (int)((double)mxUtils.getInt(var6, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE) * this.scale);
      if (var1 != null && var1.length() > 0) {
         var7 = this.document.createElement("text");
         float var11 = mxUtils.getFloat(var6, mxConstants.STYLE_TEXT_OPACITY, 100.0F);
         if (var11 != 100.0F) {
            String var12 = String.valueOf(var11 / 100.0F);
            var7.setAttribute("fill-opacity", var12);
            var7.setAttribute("stroke-opacity", var12);
         }

         var7.setAttribute("text-anchor", "middle");
         var7.setAttribute("font-weight", "normal");
         var7.setAttribute("font-decoration", "none");
         var7.setAttribute("font-size", String.valueOf(var10));
         var7.setAttribute("font-family", var9);
         var7.setAttribute("fill", var8);
         String[] var15 = var1.split("\n");
         var3 += var10 + (var5 - var15.length * (var10 + mxConstants.LINESPACING)) / 2 - 2;

         for(int var13 = 0; var13 < var15.length; ++var13) {
            Element var14 = this.document.createElement("tspan");
            var14.setAttribute("x", String.valueOf(var2 + var4 / 2));
            var14.setAttribute("y", String.valueOf(var3));
            var14.appendChild(this.document.createTextNode(var15[var13]));
            var7.appendChild(var14);
            var3 += var10 + mxConstants.LINESPACING;
         }

         this.appendSvgElement(var7);
      }

      return var7;
   }
}
