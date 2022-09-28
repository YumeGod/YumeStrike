package com.mxgraph.layout;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class mxOrganicLayout extends mxGraphLayout {
   protected boolean isOptimizeEdgeDistance = true;
   protected boolean isOptimizeEdgeCrossing = true;
   protected boolean isOptimizeEdgeLength = true;
   protected boolean isOptimizeBorderLine = true;
   protected boolean isOptimizeNodeDistribution = true;
   protected double minMoveRadius = 2.0;
   protected double moveRadius;
   protected double initialMoveRadius = 0.0;
   protected double radiusScaleFactor = 0.75;
   protected double averageNodeArea = 10000.0;
   protected double fineTuningRadius = 40.0;
   protected int maxIterations = 100;
   protected double edgeDistanceCostFactor = 4000.0;
   protected double edgeCrossingCostFactor = 2000.0;
   protected double nodeDistributionCostFactor = 300000.0;
   protected double borderLineCostFactor = 5.0;
   protected double edgeLengthCostFactor = 0.02;
   protected double boundsX = 0.0;
   protected double boundsY = 0.0;
   protected double boundsWidth = 0.0;
   protected double boundsHeight = 0.0;
   protected int iteration;
   protected int triesPerCell = 8;
   protected double minDistanceLimit = 2.0;
   protected double minDistanceLimitSquared;
   protected double maxDistanceLimit = 100.0;
   protected double maxDistanceLimitSquared;
   protected int unchangedEnergyRoundCount;
   protected int unchangedEnergyRoundTermination = 5;
   protected boolean approxNodeDimensions = true;
   protected CellWrapper[] v;
   protected CellWrapper[] e;
   protected double[] xNormTry;
   protected double[] yNormTry;
   protected boolean isFineTuning = true;

   public mxOrganicLayout(mxGraph var1) {
      super(var1);
   }

   public mxOrganicLayout(mxGraph var1, Rectangle2D var2) {
      super(var1);
      this.boundsX = var2.getX();
      this.boundsY = var2.getY();
      this.boundsWidth = var2.getWidth();
      this.boundsHeight = var2.getHeight();
   }

   public boolean isVertexIgnored(Object var1) {
      return false;
   }

   public void execute(Object var1) {
      Object[] var2 = this.graph.getChildVertices(var1);
      Object[] var3 = this.graph.getChildEdges(var1);
      mxRectangle var4 = this.graph.getBoundsForCells(var2, false, false, true);
      double var7;
      double var9;
      if (this.averageNodeArea == 0.0) {
         if (this.boundsWidth == 0.0 && var4 != null) {
            this.boundsX = var4.getX();
            this.boundsY = var4.getY();
            this.boundsWidth = var4.getWidth();
            this.boundsHeight = var4.getHeight();
         }
      } else {
         double var5 = this.averageNodeArea * (double)var2.length;
         var7 = Math.sqrt(var5);
         double var11;
         if (var4 != null) {
            var9 = var4.getX() + var4.getWidth() / 2.0;
            var11 = var4.getY() + var4.getHeight() / 2.0;
            this.boundsX = var9 - var7 / 2.0;
            this.boundsY = var11 - var7 / 2.0;
         } else {
            this.boundsX = 0.0;
            this.boundsY = 0.0;
         }

         this.boundsWidth = var7;
         this.boundsHeight = var7;
         if (this.boundsX < 0.0 || this.boundsY < 0.0) {
            var9 = Math.min(this.boundsX, this.boundsY);
            var11 = -var9;
            this.boundsX += var11;
            this.boundsY += var11;
         }
      }

      if (this.initialMoveRadius == 0.0) {
         this.initialMoveRadius = Math.max(this.boundsWidth, this.boundsHeight) / 2.0;
      }

      this.moveRadius = this.initialMoveRadius;
      this.minDistanceLimitSquared = this.minDistanceLimit * this.minDistanceLimit;
      this.maxDistanceLimitSquared = this.maxDistanceLimit * this.maxDistanceLimit;
      this.unchangedEnergyRoundCount = 0;
      Hashtable var16 = new Hashtable();
      this.v = new CellWrapper[var2.length];

      for(int var6 = 0; var6 < var2.length; ++var6) {
         this.v[var6] = new CellWrapper(var2[var6]);
         var16.put(var2[var6], new Integer(var6));
         var4 = this.graph.getCellBounds(var2[var6]);
         var7 = var4.getWidth();
         var9 = var4.getHeight();
         this.v[var6].x = var4.getX() + var7 / 2.0;
         this.v[var6].y = var4.getY() + var9 / 2.0;
         if (this.approxNodeDimensions) {
            this.v[var6].radiusSquared = Math.min(var7, var9);
            CellWrapper var10000 = this.v[var6];
            var10000.radiusSquared *= this.v[var6].radiusSquared;
         } else {
            this.v[var6].radiusSquared = var7 * var7;
            this.v[var6].heightSquared = var9 * var9;
         }
      }

      this.e = new CellWrapper[var3.length];
      mxIGraphModel var17 = this.graph.getModel();

      int var18;
      for(var18 = 0; var18 < this.e.length; ++var18) {
         this.e[var18] = new CellWrapper(var3[var18]);
         Object var8 = var17.getTerminal(var3[var18], true);
         Object var22 = var17.getTerminal(var3[var18], false);
         Integer var10 = null;
         Integer var23 = null;
         if (var8 != null) {
            var10 = (Integer)var16.get(var8);
         }

         if (var22 != null) {
            var23 = (Integer)var16.get(var22);
         }

         if (var10 != null) {
            this.e[var18].source = var10;
         } else {
            this.e[var18].source = -1;
         }

         if (var23 != null) {
            this.e[var18].target = var23;
         } else {
            this.e[var18].target = -1;
         }
      }

      for(var18 = 0; var18 < this.v.length; ++var18) {
         this.v[var18].relevantEdges = this.getRelevantEdges(var18);
         this.v[var18].connectedEdges = this.getConnectedEdges(var18);
      }

      this.xNormTry = new double[this.triesPerCell];
      this.yNormTry = new double[this.triesPerCell];

      for(var18 = 0; var18 < this.triesPerCell; ++var18) {
         double var19 = (double)var18 * (6.283185307179586 / (double)this.triesPerCell);
         this.xNormTry[var18] = Math.cos(var19);
         this.yNormTry[var18] = Math.sin(var19);
      }

      for(this.iteration = 0; this.iteration < this.maxIterations; ++this.iteration) {
         this.performRound();
      }

      double[][] var21 = new double[this.v.length][2];

      int var20;
      for(var20 = 0; var20 < this.v.length; ++var20) {
         var2[var20] = this.v[var20].cell;
         var4 = this.graph.getCellBounds(var2[var20]);
         var21[var20][0] = this.v[var20].x - var4.getWidth() / 2.0;
         var21[var20][1] = this.v[var20].y - var4.getHeight() / 2.0;
      }

      var17.beginUpdate();

      try {
         for(var20 = 0; var20 < var2.length; ++var20) {
            this.setVertexLocation(var2[var20], var21[var20][0], var21[var20][1]);
         }
      } finally {
         var17.endUpdate();
      }

   }

   protected void performRound() {
      boolean var1 = false;

      for(int var2 = 0; var2 < this.v.length; ++var2) {
         int var3 = var2;
         double var4 = this.getNodeDistribution(var2);
         double var6 = this.getEdgeDistanceFromNode(var2);
         var6 += this.getEdgeDistanceAffectedNodes(var2);
         double var8 = this.getEdgeCrossingAffectedEdges(var2);
         double var10 = this.getBorderline(var2);
         double var12 = this.getEdgeLengthAffectedEdges(var2);
         double var14 = this.getAdditionFactorsEnergy(var2);

         for(int var16 = 0; var16 < this.triesPerCell; ++var16) {
            double var17 = this.moveRadius * this.xNormTry[var16];
            double var19 = this.moveRadius * this.yNormTry[var16];
            double var21 = this.v[var3].x;
            double var23 = this.v[var3].y;
            this.v[var3].x += var17;
            this.v[var3].y += var19;
            double var25 = this.calcEnergyDelta(var3, var4, var6, var8, var10, var12, var14);
            if (var25 < 0.0) {
               var1 = true;
               break;
            }

            this.v[var3].x = var21;
            this.v[var3].y = var23;
         }
      }

      if (var1) {
         this.unchangedEnergyRoundCount = 0;
      } else {
         ++this.unchangedEnergyRoundCount;
         this.moveRadius /= 2.0;
      }

      if (this.unchangedEnergyRoundCount >= this.unchangedEnergyRoundTermination) {
         this.iteration = this.maxIterations;
      }

      double var27 = this.moveRadius * this.radiusScaleFactor;
      if (this.moveRadius - var27 < this.minMoveRadius) {
         var27 = this.moveRadius - this.minMoveRadius;
      }

      if (var27 <= this.minMoveRadius) {
         this.iteration = this.maxIterations;
      }

      if (var27 < this.fineTuningRadius) {
         this.isFineTuning = true;
      }

      this.moveRadius = var27;
   }

   protected double calcEnergyDelta(int var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      double var14 = 0.0;
      var14 += this.getNodeDistribution(var1) * 2.0;
      var14 -= var2 * 2.0;
      var14 += this.getBorderline(var1);
      var14 -= var8;
      var14 += this.getEdgeDistanceFromNode(var1);
      var14 += this.getEdgeDistanceAffectedNodes(var1);
      var14 -= var4;
      var14 -= var10;
      var14 += this.getEdgeLengthAffectedEdges(var1);
      var14 -= var6;
      var14 += this.getEdgeCrossingAffectedEdges(var1);
      var14 -= var12;
      var14 += this.getAdditionFactorsEnergy(var1);
      return var14;
   }

   protected double getNodeDistribution(int var1) {
      double var2 = 0.0;
      if (this.isOptimizeNodeDistribution) {
         int var4;
         double var5;
         double var7;
         double var9;
         if (this.approxNodeDimensions) {
            for(var4 = 0; var4 < this.v.length; ++var4) {
               if (var1 != var4) {
                  var5 = this.v[var1].x - this.v[var4].x;
                  var7 = this.v[var1].y - this.v[var4].y;
                  var9 = var5 * var5 + var7 * var7;
                  var9 -= this.v[var1].radiusSquared;
                  var9 -= this.v[var4].radiusSquared;
                  if (var9 < this.minDistanceLimitSquared) {
                     var9 = this.minDistanceLimitSquared;
                  }

                  var2 += this.nodeDistributionCostFactor / var9;
               }
            }
         } else {
            for(var4 = 0; var4 < this.v.length; ++var4) {
               if (var1 != var4) {
                  var5 = this.v[var1].x - this.v[var4].x;
                  var7 = this.v[var1].y - this.v[var4].y;
                  var9 = var5 * var5 + var7 * var7;
                  var9 -= this.v[var1].radiusSquared;
                  var9 -= this.v[var4].radiusSquared;
                  if (var9 < this.minDistanceLimitSquared) {
                     var9 = this.minDistanceLimitSquared;
                  }

                  var2 += this.nodeDistributionCostFactor / var9;
               }
            }
         }
      }

      return var2;
   }

   protected double getBorderline(int var1) {
      double var2 = 0.0;
      if (this.isOptimizeBorderLine) {
         double var4 = this.v[var1].x - this.boundsX;
         if (var4 < this.minDistanceLimit) {
            var4 = this.minDistanceLimit;
         }

         double var6 = this.v[var1].y - this.boundsY;
         if (var6 < this.minDistanceLimit) {
            var6 = this.minDistanceLimit;
         }

         double var8 = this.boundsX + this.boundsWidth - this.v[var1].x;
         if (var8 < this.minDistanceLimit) {
            var8 = this.minDistanceLimit;
         }

         double var10 = this.boundsY + this.boundsHeight - this.v[var1].y;
         if (var10 < this.minDistanceLimit) {
            var10 = this.minDistanceLimit;
         }

         var2 += this.borderLineCostFactor * (1000000.0 / (var6 * var6) + 1000000.0 / (var4 * var4) + 1000000.0 / (var10 * var10) + 1000000.0 / (var8 * var8));
      }

      return var2;
   }

   protected double getEdgeLengthAffectedEdges(int var1) {
      double var2 = 0.0;

      for(int var4 = 0; var4 < this.v[var1].connectedEdges.length; ++var4) {
         var2 += this.getEdgeLength(this.v[var1].connectedEdges[var4]);
      }

      return var2;
   }

   protected double getEdgeLength(int var1) {
      if (this.isOptimizeEdgeLength) {
         double var2 = Point2D.distance(this.v[this.e[var1].source].x, this.v[this.e[var1].source].y, this.v[this.e[var1].target].x, this.v[this.e[var1].target].y);
         return this.edgeLengthCostFactor * var2 * var2;
      } else {
         return 0.0;
      }
   }

   protected double getEdgeCrossingAffectedEdges(int var1) {
      double var2 = 0.0;

      for(int var4 = 0; var4 < this.v[var1].connectedEdges.length; ++var4) {
         var2 += this.getEdgeCrossing(this.v[var1].connectedEdges[var4]);
      }

      return var2;
   }

   protected double getEdgeCrossing(int var1) {
      int var2 = 0;
      if (this.isOptimizeEdgeCrossing) {
         double var19 = this.v[this.e[var1].source].x;
         double var21 = this.v[this.e[var1].source].y;
         double var23 = this.v[this.e[var1].target].x;
         double var25 = this.v[this.e[var1].target].y;

         for(int var27 = 0; var27 < this.e.length; ++var27) {
            double var28 = this.v[this.e[var27].source].x;
            double var30 = this.v[this.e[var27].source].y;
            double var32 = this.v[this.e[var27].target].x;
            double var34 = this.v[this.e[var27].target].y;
            if (var27 != var1) {
               double var7;
               double var15;
               if (var19 < var23) {
                  var7 = var19;
                  var15 = var23;
               } else {
                  var7 = var23;
                  var15 = var19;
               }

               double var3;
               double var11;
               if (var28 < var32) {
                  var3 = var28;
                  var11 = var32;
               } else {
                  var3 = var32;
                  var11 = var28;
               }

               if (!(var15 < var3) && !(var7 > var11)) {
                  double var9;
                  double var17;
                  if (var21 < var25) {
                     var9 = var21;
                     var17 = var25;
                  } else {
                     var9 = var25;
                     var17 = var21;
                  }

                  double var5;
                  double var13;
                  if (var30 < var34) {
                     var5 = var30;
                     var13 = var34;
                  } else {
                     var5 = var34;
                     var13 = var30;
                  }

                  if (!(var17 < var5) && !(var9 > var13) && var19 != var28 && var21 != var30 && var19 != var32 && var21 != var34 && var23 != var28 && var25 != var30 && var23 != var32 && var25 != var34) {
                     boolean var36 = Line2D.relativeCCW(var19, var21, var23, var25, var28, var30) != Line2D.relativeCCW(var19, var21, var23, var25, var32, var34) && Line2D.relativeCCW(var28, var30, var32, var34, var19, var21) != Line2D.relativeCCW(var28, var30, var32, var34, var23, var25);
                     if (var36) {
                        ++var2;
                     }
                  }
               }
            }
         }
      }

      return this.edgeCrossingCostFactor * (double)var2;
   }

   protected double getEdgeDistanceFromNode(int var1) {
      double var2 = 0.0;
      if (this.isOptimizeEdgeDistance && this.isFineTuning) {
         int[] var4 = this.v[var1].relevantEdges;

         for(int var5 = 0; var5 < var4.length; ++var5) {
            double var6 = Line2D.ptSegDistSq(this.v[this.e[var4[var5]].source].x, this.v[this.e[var4[var5]].source].y, this.v[this.e[var4[var5]].target].x, this.v[this.e[var4[var5]].target].y, this.v[var1].x, this.v[var1].y);
            var6 -= this.v[var1].radiusSquared;
            if (var6 < this.minDistanceLimitSquared) {
               var6 = this.minDistanceLimitSquared;
            }

            if (var6 < this.maxDistanceLimitSquared) {
               var2 += this.edgeDistanceCostFactor / var6;
            }
         }
      }

      return var2;
   }

   protected double getEdgeDistanceAffectedNodes(int var1) {
      double var2 = 0.0;

      for(int var4 = 0; var4 < this.v[var1].connectedEdges.length; ++var4) {
         var2 += this.getEdgeDistanceFromEdge(this.v[var1].connectedEdges[var4]);
      }

      return var2;
   }

   protected double getEdgeDistanceFromEdge(int var1) {
      double var2 = 0.0;
      if (this.isOptimizeEdgeDistance && this.isFineTuning) {
         for(int var4 = 0; var4 < this.v.length; ++var4) {
            if (this.e[var1].source != var4 && this.e[var1].target != var4) {
               double var5 = Line2D.ptSegDistSq(this.v[this.e[var1].source].x, this.v[this.e[var1].source].y, this.v[this.e[var1].target].x, this.v[this.e[var1].target].y, this.v[var4].x, this.v[var4].y);
               var5 -= this.v[var4].radiusSquared;
               if (var5 < this.minDistanceLimitSquared) {
                  var5 = this.minDistanceLimitSquared;
               }

               if (var5 < this.maxDistanceLimitSquared) {
                  var2 += this.edgeDistanceCostFactor / var5;
               }
            }
         }
      }

      return var2;
   }

   protected double getAdditionFactorsEnergy(int var1) {
      return 0.0;
   }

   protected int[] getRelevantEdges(int var1) {
      ArrayList var2 = new ArrayList(this.e.length);

      for(int var3 = 0; var3 < this.e.length; ++var3) {
         if (this.e[var3].source != var1 && this.e[var3].target != var1) {
            var2.add(new Integer(var3));
         }
      }

      int[] var6 = new int[var2.size()];
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var5 < var6.length; ++var5) {
         if (var4.hasNext()) {
            var6[var5] = (Integer)var4.next();
         }
      }

      return var6;
   }

   protected int[] getConnectedEdges(int var1) {
      ArrayList var2 = new ArrayList(this.e.length);

      for(int var3 = 0; var3 < this.e.length; ++var3) {
         if (this.e[var3].source == var1 || this.e[var3].target == var1) {
            var2.add(new Integer(var3));
         }
      }

      int[] var6 = new int[var2.size()];
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var5 < var6.length; ++var5) {
         if (var4.hasNext()) {
            var6[var5] = (Integer)var4.next();
         }
      }

      return var6;
   }

   public String toString() {
      return "Organic";
   }

   public double getAverageNodeArea() {
      return this.averageNodeArea;
   }

   public void setAverageNodeArea(double var1) {
      this.averageNodeArea = var1;
   }

   public double getBorderLineCostFactor() {
      return this.borderLineCostFactor;
   }

   public void setBorderLineCostFactor(double var1) {
      this.borderLineCostFactor = var1;
   }

   public double getEdgeCrossingCostFactor() {
      return this.edgeCrossingCostFactor;
   }

   public void setEdgeCrossingCostFactor(double var1) {
      this.edgeCrossingCostFactor = var1;
   }

   public double getEdgeDistanceCostFactor() {
      return this.edgeDistanceCostFactor;
   }

   public void setEdgeDistanceCostFactor(double var1) {
      this.edgeDistanceCostFactor = var1;
   }

   public double getEdgeLengthCostFactor() {
      return this.edgeLengthCostFactor;
   }

   public void setEdgeLengthCostFactor(double var1) {
      this.edgeLengthCostFactor = var1;
   }

   public double getFineTuningRadius() {
      return this.fineTuningRadius;
   }

   public void setFineTuningRadius(double var1) {
      this.fineTuningRadius = var1;
   }

   public double getInitialMoveRadius() {
      return this.initialMoveRadius;
   }

   public void setInitialMoveRadius(double var1) {
      this.initialMoveRadius = var1;
   }

   public boolean isFineTuning() {
      return this.isFineTuning;
   }

   public void setFineTuning(boolean var1) {
      this.isFineTuning = var1;
   }

   public boolean isOptimizeBorderLine() {
      return this.isOptimizeBorderLine;
   }

   public void setOptimizeBorderLine(boolean var1) {
      this.isOptimizeBorderLine = var1;
   }

   public boolean isOptimizeEdgeCrossing() {
      return this.isOptimizeEdgeCrossing;
   }

   public void setOptimizeEdgeCrossing(boolean var1) {
      this.isOptimizeEdgeCrossing = var1;
   }

   public boolean isOptimizeEdgeDistance() {
      return this.isOptimizeEdgeDistance;
   }

   public void setOptimizeEdgeDistance(boolean var1) {
      this.isOptimizeEdgeDistance = var1;
   }

   public boolean isOptimizeEdgeLength() {
      return this.isOptimizeEdgeLength;
   }

   public void setOptimizeEdgeLength(boolean var1) {
      this.isOptimizeEdgeLength = var1;
   }

   public boolean isOptimizeNodeDistribution() {
      return this.isOptimizeNodeDistribution;
   }

   public void setOptimizeNodeDistribution(boolean var1) {
      this.isOptimizeNodeDistribution = var1;
   }

   public int getMaxIterations() {
      return this.maxIterations;
   }

   public void setMaxIterations(int var1) {
      this.maxIterations = var1;
   }

   public double getMinDistanceLimit() {
      return this.minDistanceLimit;
   }

   public void setMinDistanceLimit(double var1) {
      this.minDistanceLimit = var1;
   }

   public double getMinMoveRadius() {
      return this.minMoveRadius;
   }

   public void setMinMoveRadius(double var1) {
      this.minMoveRadius = var1;
   }

   public double getNodeDistributionCostFactor() {
      return this.nodeDistributionCostFactor;
   }

   public void setNodeDistributionCostFactor(double var1) {
      this.nodeDistributionCostFactor = var1;
   }

   public double getRadiusScaleFactor() {
      return this.radiusScaleFactor;
   }

   public void setRadiusScaleFactor(double var1) {
      this.radiusScaleFactor = var1;
   }

   public int getTriesPerCell() {
      return this.triesPerCell;
   }

   public void setTriesPerCell(int var1) {
      this.triesPerCell = var1;
   }

   public int getUnchangedEnergyRoundTermination() {
      return this.unchangedEnergyRoundTermination;
   }

   public void setUnchangedEnergyRoundTermination(int var1) {
      this.unchangedEnergyRoundTermination = var1;
   }

   public double getMaxDistanceLimit() {
      return this.maxDistanceLimit;
   }

   public void setMaxDistanceLimit(double var1) {
      this.maxDistanceLimit = var1;
   }

   public boolean isApproxNodeDimensions() {
      return this.approxNodeDimensions;
   }

   public void setApproxNodeDimensions(boolean var1) {
      this.approxNodeDimensions = var1;
   }

   public class CellWrapper {
      protected Object cell;
      protected int[] relevantEdges = null;
      protected int[] connectedEdges = null;
      protected double x;
      protected double y;
      protected double radiusSquared;
      protected double heightSquared;
      protected int source;
      protected int target;

      public CellWrapper(Object var2) {
         this.cell = var2;
      }

      public int[] getRelevantEdges() {
         return this.relevantEdges;
      }

      public void setRelevantEdges(int[] var1) {
         this.relevantEdges = var1;
      }

      public int[] getConnectedEdges() {
         return this.connectedEdges;
      }

      public void setConnectedEdges(int[] var1) {
         this.connectedEdges = var1;
      }

      public double getX() {
         return this.x;
      }

      public void setX(double var1) {
         this.x = var1;
      }

      public double getY() {
         return this.y;
      }

      public void setY(double var1) {
         this.y = var1;
      }

      public double getRadiusSquared() {
         return this.radiusSquared;
      }

      public void setRadiusSquared(double var1) {
         this.radiusSquared = var1;
      }

      public double getHeightSquared() {
         return this.heightSquared;
      }

      public void setHeightSquared(double var1) {
         this.heightSquared = var1;
      }

      public int getSource() {
         return this.source;
      }

      public void setSource(int var1) {
         this.source = var1;
      }

      public int getTarget() {
         return this.target;
      }

      public void setTarget(int var1) {
         this.target = var1;
      }

      public Object getCell() {
         return this.cell;
      }
   }
}
