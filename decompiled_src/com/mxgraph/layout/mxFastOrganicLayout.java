package com.mxgraph.layout;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Hashtable;

public class mxFastOrganicLayout extends mxGraphLayout {
   protected boolean useInputOrigin = true;
   protected boolean resetEdges = true;
   protected boolean disableEdgeStyle = true;
   protected double forceConstant = 50.0;
   protected double forceConstantSquared = 0.0;
   protected double minDistanceLimit = 2.0;
   protected double minDistanceLimitSquared = 0.0;
   protected double initialTemp = 200.0;
   protected double temperature = 0.0;
   protected int maxIterations = 0;
   protected int iteration = 0;
   protected Object[] vertexArray;
   protected double[] dispX;
   protected double[] dispY;
   protected double[][] cellLocation;
   protected double[] radius;
   protected double[] radiusSquared;
   protected boolean[] isMoveable;
   protected int[][] neighbours;
   protected boolean allowedToRun = true;
   protected Hashtable indices = new Hashtable();

   public mxFastOrganicLayout(mxGraph var1) {
      super(var1);
   }

   public boolean isVertexIgnored(Object var1) {
      return super.isVertexIgnored(var1) || this.graph.getConnections(var1).length == 0;
   }

   public boolean isUseInputOrigin() {
      return this.useInputOrigin;
   }

   public void setUseInputOrigin(boolean var1) {
      this.useInputOrigin = var1;
   }

   public boolean isResetEdges() {
      return this.resetEdges;
   }

   public void setResetEdges(boolean var1) {
      this.resetEdges = var1;
   }

   public boolean isDisableEdgeStyle() {
      return this.disableEdgeStyle;
   }

   public void setDisableEdgeStyle(boolean var1) {
      this.disableEdgeStyle = var1;
   }

   public int getMaxIterations() {
      return this.maxIterations;
   }

   public void setMaxIterations(int var1) {
      this.maxIterations = var1;
   }

   public double getForceConstant() {
      return this.forceConstant;
   }

   public void setForceConstant(double var1) {
      this.forceConstant = var1;
   }

   public double getMinDistanceLimit() {
      return this.minDistanceLimit;
   }

   public void setMinDistanceLimit(double var1) {
      this.minDistanceLimit = var1;
   }

   public double getInitialTemp() {
      return this.initialTemp;
   }

   public void setInitialTemp(double var1) {
      this.initialTemp = var1;
   }

   protected void reduceTemperature() {
      this.temperature = this.initialTemp * (1.0 - (double)(this.iteration / this.maxIterations));
   }

   public void moveCell(Object var1, double var2, double var4) {
   }

   public void execute(Object var1) {
      mxIGraphModel var2 = this.graph.getModel();
      Object[] var3 = this.graph.getChildVertices(var1);
      ArrayList var4 = new ArrayList(var3.length);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (!this.isVertexIgnored(var3[var5])) {
            var4.add(var3[var5]);
         }
      }

      this.vertexArray = var4.toArray();
      mxRectangle var21 = this.useInputOrigin ? this.graph.getBoundsForCells(this.vertexArray, false, false, true) : null;
      int var6 = this.vertexArray.length;
      this.dispX = new double[var6];
      this.dispY = new double[var6];
      this.cellLocation = new double[var6][];
      this.isMoveable = new boolean[var6];
      this.neighbours = new int[var6][];
      this.radius = new double[var6];
      this.radiusSquared = new double[var6];
      this.minDistanceLimitSquared = this.minDistanceLimit * this.minDistanceLimit;
      if (this.forceConstant < 0.001) {
         this.forceConstant = 0.001;
      }

      this.forceConstantSquared = this.forceConstant * this.forceConstant;

      int var7;
      double var12;
      double var14;
      for(var7 = 0; var7 < this.vertexArray.length; ++var7) {
         Object var8 = this.vertexArray[var7];
         this.cellLocation[var7] = new double[2];
         this.indices.put(var8, new Integer(var7));
         mxRectangle var9 = this.getVertexBounds(var8);
         double var10 = var9.getWidth();
         var12 = var9.getHeight();
         var14 = var9.getX();
         double var16 = var9.getY();
         this.cellLocation[var7][0] = var14 + var10 / 2.0;
         this.cellLocation[var7][1] = var16 + var12 / 2.0;
         this.radius[var7] = Math.min(var10, var12);
         this.radiusSquared[var7] = this.radius[var7] * this.radius[var7];
      }

      var2.beginUpdate();

      try {
         int var25;
         for(var7 = 0; var7 < var6; ++var7) {
            this.dispX[var7] = 0.0;
            this.dispY[var7] = 0.0;
            this.isMoveable[var7] = this.isVertexMovable(this.vertexArray[var7]);
            Object[] var22 = this.graph.getConnections(this.vertexArray[var7], var1);

            for(var25 = 0; var25 < var22.length; ++var25) {
               if (this.isResetEdges()) {
                  this.graph.resetEdge(var22[var25]);
               }

               if (this.isDisableEdgeStyle()) {
                  this.setEdgeStyleEnabled(var22[var25], false);
               }
            }

            Object[] var26 = this.graph.getOpposites(var22, this.vertexArray[var7]);
            this.neighbours[var7] = new int[var26.length];

            for(int var27 = 0; var27 < var26.length; ++var27) {
               Integer var11 = (Integer)this.indices.get(var26[var27]);
               if (var11 != null) {
                  this.neighbours[var7][var27] = var11;
               } else {
                  this.neighbours[var7][var27] = var7;
               }
            }
         }

         this.temperature = this.initialTemp;
         if (this.maxIterations == 0) {
            this.maxIterations = (int)(20.0 * Math.sqrt((double)var6));
         }

         for(this.iteration = 0; this.iteration < this.maxIterations; ++this.iteration) {
            if (!this.allowedToRun) {
               return;
            }

            this.calcRepulsion();
            this.calcAttraction();
            this.calcPositions();
            this.reduceTemperature();
         }

         Double var23 = null;
         Double var24 = null;

         for(var25 = 0; var25 < this.vertexArray.length; ++var25) {
            Object var29 = this.vertexArray[var25];
            mxGeometry var30 = var2.getGeometry(var29);
            if (var30 != null) {
               double[] var10000 = this.cellLocation[var25];
               var10000[0] -= var30.getWidth() / 2.0;
               var10000 = this.cellLocation[var25];
               var10000[1] -= var30.getHeight() / 2.0;
               var12 = this.graph.snap(this.cellLocation[var25][0]);
               var14 = this.graph.snap(this.cellLocation[var25][1]);
               this.setVertexLocation(var29, var12, var14);
               if (var23 == null) {
                  var23 = new Double(var12);
               } else {
                  var23 = new Double(Math.min(var23, var12));
               }

               if (var24 == null) {
                  var24 = new Double(var14);
               } else {
                  var24 = new Double(Math.min(var24, var14));
               }
            }
         }

         double var28 = var23 != null ? -var23 - 1.0 : 0.0;
         double var31 = var24 != null ? -var24 - 1.0 : 0.0;
         if (var21 != null) {
            var28 += var21.getX();
            var31 += var21.getY();
         }

         this.graph.moveCells(this.vertexArray, var28, var31);
      } finally {
         var2.endUpdate();
      }
   }

   protected void calcPositions() {
      for(int var1 = 0; var1 < this.vertexArray.length; ++var1) {
         if (this.isMoveable[var1]) {
            double var2 = Math.sqrt(this.dispX[var1] * this.dispX[var1] + this.dispY[var1] * this.dispY[var1]);
            if (var2 < 0.001) {
               var2 = 0.001;
            }

            double var4 = this.dispX[var1] / var2 * Math.min(var2, this.temperature);
            double var6 = this.dispY[var1] / var2 * Math.min(var2, this.temperature);
            this.dispX[var1] = 0.0;
            this.dispY[var1] = 0.0;
            double[] var10000 = this.cellLocation[var1];
            var10000[0] += var4;
            var10000 = this.cellLocation[var1];
            var10000[1] += var6;
         }
      }

   }

   protected void calcAttraction() {
      for(int var1 = 0; var1 < this.vertexArray.length; ++var1) {
         for(int var2 = 0; var2 < this.neighbours[var1].length; ++var2) {
            int var3 = this.neighbours[var1][var2];
            if (var1 != var3) {
               double var4 = this.cellLocation[var1][0] - this.cellLocation[var3][0];
               double var6 = this.cellLocation[var1][1] - this.cellLocation[var3][1];
               double var8 = var4 * var4 + var6 * var6 - this.radiusSquared[var1] - this.radiusSquared[var3];
               if (var8 < this.minDistanceLimitSquared) {
                  var8 = this.minDistanceLimitSquared;
               }

               double var10 = Math.sqrt(var8);
               double var12 = var8 / this.forceConstant;
               double var14 = var4 / var10 * var12;
               double var16 = var6 / var10 * var12;
               double[] var10000;
               if (this.isMoveable[var1]) {
                  var10000 = this.dispX;
                  var10000[var1] -= var14;
                  var10000 = this.dispY;
                  var10000[var1] -= var16;
               }

               if (this.isMoveable[var3]) {
                  var10000 = this.dispX;
                  var10000[var3] += var14;
                  var10000 = this.dispY;
                  var10000[var3] += var16;
               }
            }
         }
      }

   }

   protected void calcRepulsion() {
      int var1 = this.vertexArray.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         for(int var3 = var2; var3 < var1; ++var3) {
            if (!this.allowedToRun) {
               return;
            }

            if (var3 != var2) {
               double var4 = this.cellLocation[var2][0] - this.cellLocation[var3][0];
               double var6 = this.cellLocation[var2][1] - this.cellLocation[var3][1];
               if (var4 == 0.0) {
                  var4 = 0.01 + Math.random();
               }

               if (var6 == 0.0) {
                  var6 = 0.01 + Math.random();
               }

               double var8 = Math.sqrt(var4 * var4 + var6 * var6);
               double var10 = var8 - this.radius[var2] - this.radius[var3];
               if (var10 < this.minDistanceLimit) {
                  var10 = this.minDistanceLimit;
               }

               double var12 = this.forceConstantSquared / var10;
               double var14 = var4 / var8 * var12;
               double var16 = var6 / var8 * var12;
               double[] var10000;
               if (this.isMoveable[var2]) {
                  var10000 = this.dispX;
                  var10000[var2] += var14;
                  var10000 = this.dispY;
                  var10000[var2] += var16;
               }

               if (this.isMoveable[var3]) {
                  var10000 = this.dispX;
                  var10000[var3] -= var14;
                  var10000 = this.dispY;
                  var10000[var3] -= var16;
               }
            }
         }
      }

   }
}
