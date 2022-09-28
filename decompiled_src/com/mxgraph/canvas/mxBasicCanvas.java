package com.mxgraph.canvas;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.awt.Point;
import java.util.Map;

public abstract class mxBasicCanvas implements mxICanvas {
   public static String DEFAULT_IMAGEBASEPATH = "";
   protected String imageBasePath;
   protected Point translate;
   protected double scale;
   protected boolean drawLabels;

   public mxBasicCanvas() {
      this.imageBasePath = DEFAULT_IMAGEBASEPATH;
      this.translate = new Point();
      this.scale = 1.0;
      this.drawLabels = true;
   }

   public void setTranslate(int var1, int var2) {
      this.translate.setLocation(var1, var2);
   }

   public Point getTranslate() {
      return this.translate;
   }

   public void setScale(double var1) {
      this.scale = var1;
   }

   public double getScale() {
      return this.scale;
   }

   public void setDrawLabels(boolean var1) {
      this.drawLabels = var1;
   }

   public String getImageBasePath() {
      return this.imageBasePath;
   }

   public void setImageBasePath(String var1) {
      this.imageBasePath = var1;
   }

   public boolean isDrawLabels() {
      return this.drawLabels;
   }

   public String getImageForStyle(Map var1) {
      String var2 = mxUtils.getString(var1, mxConstants.STYLE_IMAGE);
      if (var2 != null && !var2.startsWith("/")) {
         var2 = this.imageBasePath + var2;
      }

      return var2;
   }
}
