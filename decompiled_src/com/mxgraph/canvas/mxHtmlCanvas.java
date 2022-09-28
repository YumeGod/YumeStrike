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
import org.w3c.dom.Node;

public class mxHtmlCanvas extends mxBasicCanvas {
   protected Document document;

   public mxHtmlCanvas() {
      this((Document)null);
   }

   public mxHtmlCanvas(Document var1) {
      this.setDocument(var1);
   }

   public void appendHtmlElement(Element var1) {
      if (this.document != null) {
         Node var2 = this.document.getDocumentElement().getFirstChild().getNextSibling();
         if (var2 != null) {
            var2.appendChild(var1);
         }
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
      if (var1.getAbsolutePointCount() > 1) {
         List var3 = var1.getAbsolutePoints();
         var3 = mxUtils.translatePoints(var3, (double)this.translate.x, (double)this.translate.y);
         this.drawLine(var3, var2);
      } else {
         int var9 = (int)var1.getX() + this.translate.x;
         int var4 = (int)var1.getY() + this.translate.y;
         int var5 = (int)var1.getWidth();
         int var6 = (int)var1.getHeight();
         if (!mxUtils.getString(var2, mxConstants.STYLE_SHAPE, "").equals("swimlane")) {
            this.drawShape(var9, var4, var5, var6, var2);
         } else {
            int var7 = (int)Math.round((double)mxUtils.getInt(var2, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE) * this.scale);
            Hashtable var8 = new Hashtable(var2);
            var8.remove(mxConstants.STYLE_FILLCOLOR);
            var8.remove(mxConstants.STYLE_ROUNDED);
            if (mxUtils.isTrue(var2, mxConstants.STYLE_HORIZONTAL, true)) {
               this.drawShape(var9, var4, var5, var7, var2);
               this.drawShape(var9, var4 + var7, var5, var6 - var7, var8);
            } else {
               this.drawShape(var9, var4, var7, var6, var2);
               this.drawShape(var9 + var7, var4, var5 - var7, var6, var8);
            }
         }
      }

      return null;
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
      Element var10 = this.document.createElement("div");
      String var11;
      if (var9.equals("line")) {
         var11 = mxUtils.getString(var5, mxConstants.STYLE_DIRECTION, "east");
         if (!var11.equals("east") && !var11.equals("west")) {
            var1 = Math.round((float)(var2 + var3 / 2));
            var3 = 1;
         } else {
            var2 = Math.round((float)(var2 + var4 / 2));
            var4 = 1;
         }
      }

      if (mxUtils.isTrue(var5, mxConstants.STYLE_SHADOW, false) && var6 != null) {
         Element var13 = (Element)var10.cloneNode(true);
         String var12 = "overflow:hidden;position:absolute;left:" + String.valueOf(var1 + mxConstants.SHADOW_OFFSETX) + "px;" + "top:" + (var2 + mxConstants.SHADOW_OFFSETY) + "px;" + "width:" + var3 + "px;" + "height:" + var4 + "px;background:" + mxConstants.W3C_SHADOWCOLOR + ";border-style:solid;border-color:" + mxConstants.W3C_SHADOWCOLOR + ";border-width:" + Math.round(var8) + ";";
         var13.setAttribute("style", var12);
         this.appendHtmlElement(var13);
      }

      if (var9.equals("image")) {
         var11 = this.getImageForStyle(var5);
         if (var11 != null) {
            var10 = this.document.createElement("img");
            var10.setAttribute("border", "0");
            var10.setAttribute("src", var11);
         }
      }

      var11 = "overflow:hidden;position:absolute;left:" + String.valueOf(var1) + "px;" + "top:" + var2 + "px;" + "width:" + var3 + "px;" + "height:" + var4 + "px;background:" + var6 + ";" + ";border-style:solid;border-color:" + var7 + ";border-width:" + Math.round(var8) + ";";
      var10.setAttribute("style", var11);
      this.appendHtmlElement(var10);
      return var10;
   }

   public void drawLine(List var1, Map var2) {
      String var3 = mxUtils.getString(var2, mxConstants.STYLE_STROKECOLOR);
      int var4 = (int)((double)mxUtils.getInt(var2, mxConstants.STYLE_STROKEWIDTH, 1) * this.scale);
      if (var3 != null && var4 > 0) {
         mxPoint var5 = (mxPoint)var1.get(0);

         for(int var6 = 1; var6 < var1.size(); ++var6) {
            mxPoint var7 = (mxPoint)var1.get(var6);
            this.drawSegment((int)var5.getX(), (int)var5.getY(), (int)var7.getX(), (int)var7.getY(), var3, var4);
            var5 = var7;
         }
      }

   }

   protected void drawSegment(int var1, int var2, int var3, int var4, String var5, int var6) {
      int var7 = Math.min(var1, var3);
      int var8 = Math.min(var2, var4);
      int var9 = Math.max(var1, var3) - var7;
      int var10 = Math.max(var2, var4) - var8;
      if (var9 != 0 && var10 != 0) {
         int var13 = var7 + (var3 - var7) / 2;
         this.drawSegment(var7, var8, var13, var8, var5, var6);
         this.drawSegment(var13, var8, var13, var4, var5, var6);
         this.drawSegment(var13, var4, var3, var4, var5, var6);
      } else {
         String var11 = "overflow:hidden;position:absolute;left:" + String.valueOf(var7) + "px;" + "top:" + var8 + "px;" + "width:" + var9 + "px;" + "height:" + var10 + "px;" + "border-color:" + var5 + ";" + "border-style:solid;" + "border-width:1 1 0 0px;";
         Element var12 = this.document.createElement("div");
         var12.setAttribute("style", var11);
         this.appendHtmlElement(var12);
      }

   }

   public Element drawText(String var1, int var2, int var3, int var4, int var5, Map var6) {
      Element var7 = mxUtils.createTable(this.document, var1, var2, var3, var4, var5, this.scale, var6);
      this.appendHtmlElement(var7);
      return var7;
   }
}
