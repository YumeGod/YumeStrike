package com.mxgraph.layout.hierarchical.stage;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphAbstractHierarchyCell;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyEdge;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyNode;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyRank;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mxCoordinateAssignment implements mxHierarchicalLayoutStage {
   protected mxHierarchicalLayout layout;
   protected double intraCellSpacing = 30.0;
   protected double interRankCellSpacing = 30.0;
   protected double parallelEdgeSpacing = 10.0;
   protected int maxIterations = 8;
   protected int orientation = 1;
   protected double initialX;
   protected double limitX;
   protected double currentXDelta;
   protected int widestRank;
   protected double widestRankValue;
   protected double[] rankWidths;
   protected double[] rankY;
   protected boolean fineTuning = true;
   protected boolean disableEdgeStyle = true;
   protected mxGraphAbstractHierarchyCell[][] nextLayerConnectedCache;
   protected mxGraphAbstractHierarchyCell[][] previousLayerConnectedCache;
   private static Logger logger = Logger.getLogger("com.jgraph.layout.hierarchical.JGraphCoordinateAssignment");

   public mxCoordinateAssignment(mxHierarchicalLayout var1, double var2, double var4, int var6, double var7, double var9) {
      this.layout = var1;
      this.intraCellSpacing = var2;
      this.interRankCellSpacing = var4;
      this.orientation = var6;
      this.initialX = var7;
      this.parallelEdgeSpacing = var9;
      this.setLoggerLevel(Level.OFF);
   }

   public void execute(Object var1) {
      mxGraphHierarchyModel var2 = this.layout.getModel();
      this.currentXDelta = 0.0;
      this.initialCoords(this.layout.getGraph(), var2);
      if (this.fineTuning) {
         this.minNode(var2);
      }

      double var3 = 1.0E8;
      if (this.fineTuning) {
         for(int var5 = 0; var5 < this.maxIterations; ++var5) {
            if (var5 != 0) {
               this.medianPos(var5, var2);
               this.minNode(var2);
            }

            int var6;
            mxGraphHierarchyRank var7;
            Iterator var8;
            mxGraphAbstractHierarchyCell var9;
            if (this.currentXDelta < var3) {
               var6 = 0;

               while(true) {
                  if (var6 >= var2.ranks.size()) {
                     var3 = this.currentXDelta;
                     break;
                  }

                  var7 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var6));
                  var8 = var7.iterator();

                  while(var8.hasNext()) {
                     var9 = (mxGraphAbstractHierarchyCell)var8.next();
                     var9.setX(var6, (double)var9.getGeneralPurposeVariable(var6));
                  }

                  ++var6;
               }
            } else {
               for(var6 = 0; var6 < var2.ranks.size(); ++var6) {
                  var7 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var6));
                  var8 = var7.iterator();

                  while(var8.hasNext()) {
                     var9 = (mxGraphAbstractHierarchyCell)var8.next();
                     var9.setGeneralPurposeVariable(var6, (int)var9.getX(var6));
                  }
               }
            }

            this.currentXDelta = 0.0;
         }
      }

      this.setCellLocations(this.layout.getGraph(), var2);
   }

   private void minNode(mxGraphHierarchyModel var1) {
      LinkedList var2 = new LinkedList();
      Hashtable var3 = new Hashtable();
      Object[][] var4 = new Object[var1.maxRank + 1][];

      int var5;
      for(var5 = 0; var5 <= var1.maxRank; ++var5) {
         mxGraphHierarchyRank var6 = (mxGraphHierarchyRank)var1.ranks.get(new Integer(var5));
         var4[var5] = var6.toArray();

         for(int var7 = 0; var7 < var4[var5].length; ++var7) {
            mxGraphAbstractHierarchyCell var8 = (mxGraphAbstractHierarchyCell)var4[var5][var7];
            WeightedCellSorter var9 = new WeightedCellSorter(var8, var5);
            var9.rankIndex = var7;
            var9.visited = true;
            var2.add(var9);
            var3.put(var8, var9);
         }
      }

      var5 = var2.size() * 10;
      int var26 = 0;

      for(byte var27 = 1; !var2.isEmpty() && var26 <= var5; ++var26) {
         WeightedCellSorter var28 = (WeightedCellSorter)var2.getFirst();
         mxGraphAbstractHierarchyCell var29 = var28.cell;
         int var10 = var28.weightedValue;
         int var11 = var28.rankIndex;
         Object[] var12 = var29.getNextLayerConnectedCells(var10).toArray();
         Object[] var13 = var29.getPreviousLayerConnectedCells(var10).toArray();
         int var14 = var12.length;
         int var15 = var13.length;
         int var16 = this.medianXValue(var12, var10 + 1);
         int var17 = this.medianXValue(var13, var10 - 1);
         int var18 = var14 + var15;
         int var19 = var29.getGeneralPurposeVariable(var10);
         double var20 = (double)var19;
         if (var18 > 0) {
            var20 = (double)((var16 * var14 + var17 * var15) / var18);
         }

         boolean var22 = false;
         int var30;
         mxGraphAbstractHierarchyCell var31;
         if (var20 < (double)(var19 - var27)) {
            if (var11 == 0) {
               var29.setGeneralPurposeVariable(var10, (int)var20);
               var22 = true;
            } else {
               mxGraphAbstractHierarchyCell var23 = (mxGraphAbstractHierarchyCell)var4[var10][var11 - 1];
               int var24 = var23.getGeneralPurposeVariable(var10);
               var24 = var24 + (int)var23.width / 2 + (int)this.intraCellSpacing + (int)var29.width / 2;
               if ((double)var24 < var20) {
                  var29.setGeneralPurposeVariable(var10, (int)var20);
                  var22 = true;
               } else if (var24 < var29.getGeneralPurposeVariable(var10) - var27) {
                  var29.setGeneralPurposeVariable(var10, var24);
                  var22 = true;
               }
            }
         } else if (var20 > (double)(var19 + var27)) {
            var30 = var4[var10].length;
            if (var11 == var30 - 1) {
               var29.setGeneralPurposeVariable(var10, (int)var20);
               var22 = true;
            } else {
               var31 = (mxGraphAbstractHierarchyCell)var4[var10][var11 + 1];
               int var25 = var31.getGeneralPurposeVariable(var10);
               var25 = var25 - (int)var31.width / 2 - (int)this.intraCellSpacing - (int)var29.width / 2;
               if ((double)var25 > var20) {
                  var29.setGeneralPurposeVariable(var10, (int)var20);
                  var22 = true;
               } else if (var25 > var29.getGeneralPurposeVariable(var10) + var27) {
                  var29.setGeneralPurposeVariable(var10, var25);
                  var22 = true;
               }
            }
         }

         if (var22) {
            WeightedCellSorter var32;
            for(var30 = 0; var30 < var12.length; ++var30) {
               var31 = (mxGraphAbstractHierarchyCell)var12[var30];
               var32 = (WeightedCellSorter)var3.get(var31);
               if (var32 != null && !var32.visited) {
                  var32.visited = true;
                  var2.add(var32);
               }
            }

            for(var30 = 0; var30 < var13.length; ++var30) {
               var31 = (mxGraphAbstractHierarchyCell)var13[var30];
               var32 = (WeightedCellSorter)var3.get(var31);
               if (var32 != null && !var32.visited) {
                  var32.visited = true;
                  var2.add(var32);
               }
            }
         }

         var2.removeFirst();
         var28.visited = false;
      }

   }

   private void medianPos(int var1, mxGraphHierarchyModel var2) {
      boolean var3 = var1 % 2 == 0;
      int var4;
      if (var3) {
         for(var4 = var2.maxRank; var4 > 0; --var4) {
            this.rankMedianPosition(var4 - 1, var2, var4);
         }
      } else {
         for(var4 = 0; var4 < var2.maxRank - 1; ++var4) {
            this.rankMedianPosition(var4 + 1, var2, var4);
         }
      }

   }

   protected void rankMedianPosition(int var1, mxGraphHierarchyModel var2, int var3) {
      mxGraphHierarchyRank var4 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var1));
      Object[] var5 = var4.toArray();
      WeightedCellSorter[] var6 = new WeightedCellSorter[var5.length];
      Hashtable var7 = new Hashtable(var5.length);

      int var8;
      for(var8 = 0; var8 < var5.length; ++var8) {
         mxGraphAbstractHierarchyCell var9 = (mxGraphAbstractHierarchyCell)var5[var8];
         var6[var8] = new WeightedCellSorter();
         var6[var8].cell = var9;
         var6[var8].rankIndex = var8;
         var7.put(var9, var6[var8]);
         List var10 = null;
         if (var3 < var1) {
            var10 = var9.getPreviousLayerConnectedCells(var1);
         } else {
            var10 = var9.getNextLayerConnectedCells(var1);
         }

         var6[var8].weightedValue = this.calculatedWeightedValue(var9, var10);
      }

      Arrays.sort(var6);

      for(var8 = 0; var8 < var6.length; ++var8) {
         boolean var24 = false;
         mxGraphAbstractHierarchyCell var26 = var6[var8].cell;
         Object[] var11 = null;
         int var12 = 0;
         if (var3 < var1) {
            var11 = var26.getPreviousLayerConnectedCells(var1).toArray();
         } else {
            var11 = var26.getNextLayerConnectedCells(var1).toArray();
         }

         if (var11 != null) {
            int var25 = var11.length;
            if (var25 > 0) {
               var12 = this.medianXValue(var11, var3);
            } else {
               var12 = var26.getGeneralPurposeVariable(var1);
            }
         }

         double var13 = 0.0;
         double var15 = -1.0E8;
         int var17 = var6[var8].rankIndex - 1;

         while(var17 >= 0) {
            WeightedCellSorter var18 = (WeightedCellSorter)var7.get(var5[var17]);
            if (var18 != null) {
               mxGraphAbstractHierarchyCell var19 = var18.cell;
               if (var18.visited) {
                  var15 = (double)var19.getGeneralPurposeVariable(var1) + var19.width / 2.0 + this.intraCellSpacing + var13 + var26.width / 2.0;
                  var17 = -1;
               } else {
                  var13 += var19.width + this.intraCellSpacing;
                  --var17;
               }
            }
         }

         double var27 = 0.0;
         double var28 = 1.0E8;
         int var21 = var6[var8].rankIndex + 1;

         while(var21 < var6.length) {
            WeightedCellSorter var22 = (WeightedCellSorter)var7.get(var5[var21]);
            if (var22 != null) {
               mxGraphAbstractHierarchyCell var23 = var22.cell;
               if (var22.visited) {
                  var28 = (double)var23.getGeneralPurposeVariable(var1) - var23.width / 2.0 - this.intraCellSpacing - var27 - var26.width / 2.0;
                  var21 = var6.length;
               } else {
                  var27 += var23.width + this.intraCellSpacing;
                  ++var21;
               }
            }
         }

         if ((double)var12 >= var15 && (double)var12 <= var28) {
            var26.setGeneralPurposeVariable(var1, var12);
         } else if ((double)var12 < var15) {
            var26.setGeneralPurposeVariable(var1, (int)var15);
            this.currentXDelta += var15 - (double)var12;
         } else if ((double)var12 > var28) {
            var26.setGeneralPurposeVariable(var1, (int)var28);
            this.currentXDelta += (double)var12 - var28;
         }

         var6[var8].visited = true;
      }

   }

   private int calculatedWeightedValue(mxGraphAbstractHierarchyCell var1, Collection var2) {
      int var3 = 0;
      Iterator var4 = var2.iterator();

      while(true) {
         while(var4.hasNext()) {
            mxGraphAbstractHierarchyCell var5 = (mxGraphAbstractHierarchyCell)var4.next();
            if (var1.isVertex() && var5.isVertex()) {
               ++var3;
            } else if (var1.isEdge() && var5.isEdge()) {
               var3 += 8;
            } else {
               var3 += 2;
            }
         }

         return var3;
      }
   }

   private int medianXValue(Object[] var1, int var2) {
      if (var1.length == 0) {
         return 0;
      } else {
         int[] var3 = new int[var1.length];

         int var4;
         for(var4 = 0; var4 < var1.length; ++var4) {
            var3[var4] = ((mxGraphAbstractHierarchyCell)var1[var4]).getGeneralPurposeVariable(var2);
         }

         Arrays.sort(var3);
         if (var1.length % 2 == 1) {
            return var3[var1.length / 2];
         } else {
            var4 = var1.length / 2;
            int var5 = var3[var4 - 1];
            int var6 = var3[var4];
            return (var5 + var6) / 2;
         }
      }
   }

   private void initialCoords(mxGraph var1, mxGraphHierarchyModel var2) {
      this.calculateWidestRank(var1, var2);

      int var3;
      for(var3 = this.widestRank; var3 >= 0; --var3) {
         if (var3 < var2.maxRank) {
            this.rankCoordinates(var3, var1, var2);
         }
      }

      for(var3 = this.widestRank + 1; var3 <= var2.maxRank; ++var3) {
         if (var3 > 0) {
            this.rankCoordinates(var3, var1, var2);
         }
      }

   }

   protected void rankCoordinates(int var1, mxGraph var2, mxGraphHierarchyModel var3) {
      mxGraphHierarchyRank var4 = (mxGraphHierarchyRank)var3.ranks.get(new Integer(var1));
      double var5 = 0.0;
      double var7 = this.initialX + (this.widestRankValue - this.rankWidths[var1]) / 2.0;
      boolean var9 = false;

      for(Iterator var10 = var4.iterator(); var10.hasNext(); var7 += this.intraCellSpacing) {
         mxGraphAbstractHierarchyCell var11 = (mxGraphAbstractHierarchyCell)var10.next();
         if (!var11.isVertex()) {
            if (var11.isEdge()) {
               mxGraphHierarchyEdge var14 = (mxGraphHierarchyEdge)var11;
               int var15 = 1;
               if (var14.edges != null) {
                  var15 = var14.edges.size();
               } else {
                  logger.info("edge.edges is null");
               }

               var11.width = (double)(var15 - 1) * this.parallelEdgeSpacing;
            }
         } else {
            mxGraphHierarchyNode var12 = (mxGraphHierarchyNode)var11;
            mxRectangle var13 = this.layout.getVertexBounds(var12.cell);
            if (var13 != null) {
               if (this.orientation != 1 && this.orientation != 5) {
                  var11.width = var13.getHeight();
                  var11.height = var13.getWidth();
               } else {
                  var11.width = var13.getWidth();
                  var11.height = var13.getHeight();
               }
            } else {
               var9 = true;
            }

            var5 = Math.max(var5, var11.height);
         }

         var7 += var11.width / 2.0;
         var11.setX(var1, var7);
         var11.setGeneralPurposeVariable(var1, (int)var7);
         var7 += var11.width / 2.0;
      }

      if (var9) {
         logger.info("At least one cell has no bounds");
      }

   }

   protected void calculateWidestRank(mxGraph var1, mxGraphHierarchyModel var2) {
      double var3 = -this.interRankCellSpacing;
      double var5 = 0.0;
      this.rankWidths = new double[var2.maxRank + 1];
      this.rankY = new double[var2.maxRank + 1];

      for(int var7 = var2.maxRank; var7 >= 0; --var7) {
         double var8 = 0.0;
         mxGraphHierarchyRank var10 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var7));
         double var11 = this.initialX;
         boolean var13 = false;

         Iterator var14;
         for(var14 = var10.iterator(); var14.hasNext(); this.rankWidths[var7] = var11) {
            mxGraphAbstractHierarchyCell var15 = (mxGraphAbstractHierarchyCell)var14.next();
            if (!var15.isVertex()) {
               if (var15.isEdge()) {
                  mxGraphHierarchyEdge var19 = (mxGraphHierarchyEdge)var15;
                  int var20 = 1;
                  if (var19.edges != null) {
                     var20 = var19.edges.size();
                  } else {
                     logger.info("edge.edges is null");
                  }

                  var15.width = (double)(var20 - 1) * this.parallelEdgeSpacing;
               }
            } else {
               mxGraphHierarchyNode var16 = (mxGraphHierarchyNode)var15;
               mxRectangle var17 = this.layout.getVertexBounds(var16.cell);
               if (var17 != null) {
                  if (this.orientation != 1 && this.orientation != 5) {
                     var15.width = var17.getHeight();
                     var15.height = var17.getWidth();
                  } else {
                     var15.width = var17.getWidth();
                     var15.height = var17.getHeight();
                  }
               } else {
                  var13 = true;
               }

               var8 = Math.max(var8, var15.height);
            }

            var11 += var15.width / 2.0;
            var15.setX(var7, var11);
            var15.setGeneralPurposeVariable(var7, (int)var11);
            var11 += var15.width / 2.0;
            var11 += this.intraCellSpacing;
            if (var11 > this.widestRankValue) {
               this.widestRankValue = var11;
               this.widestRank = var7;
            }
         }

         if (var13) {
            logger.info("At least one cell has no bounds");
         }

         this.rankY[var7] = var3;
         double var18 = var8 / 2.0 + var5 / 2.0 + this.interRankCellSpacing;
         var5 = var8;
         if (this.orientation != 1 && this.orientation != 7) {
            var3 -= var18;
         } else {
            var3 += var18;
         }

         var14 = var10.iterator();

         while(var14.hasNext()) {
            mxGraphAbstractHierarchyCell var21 = (mxGraphAbstractHierarchyCell)var14.next();
            var21.setY(var7, var3);
         }
      }

   }

   private void setCellLocations(mxGraph var1, mxGraphHierarchyModel var2) {
      label94:
      for(int var3 = 0; var3 < var2.ranks.size(); ++var3) {
         mxGraphHierarchyRank var4 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var3));
         Iterator var5 = var4.iterator();

         while(true) {
            while(true) {
               if (!var5.hasNext()) {
                  continue label94;
               }

               mxGraphAbstractHierarchyCell var6 = (mxGraphAbstractHierarchyCell)var5.next();
               if (var6.isVertex()) {
                  mxGraphHierarchyNode var16 = (mxGraphHierarchyNode)var6;
                  Object var17 = var16.cell;
                  double var9 = var16.x[0] - var16.width / 2.0;
                  double var18 = var16.y[0] - var16.height / 2.0;
                  if (this.orientation != 1 && this.orientation != 5) {
                     this.layout.setVertexLocation(var17, var18, var9);
                  } else {
                     this.layout.setVertexLocation(var17, var9, var18);
                  }

                  this.limitX = Math.max(this.limitX, var9 + var16.width);
               } else if (var6.isEdge()) {
                  mxGraphHierarchyEdge var7 = (mxGraphHierarchyEdge)var6;
                  double var8 = 0.0;
                  if (var7.temp[0] != 101207) {
                     Iterator var10 = var7.edges.iterator();

                     while(var10.hasNext()) {
                        Object var11 = var10.next();
                        ArrayList var12 = new ArrayList(var7.x.length);
                        int var13;
                        double var14;
                        if (var7.isReversed()) {
                           var13 = 0;

                           while(true) {
                              if (var13 >= var7.x.length) {
                                 this.processReversedEdge(var7, var11);
                                 break;
                              }

                              var14 = var7.x[var13] + var8;
                              if (this.orientation != 1 && this.orientation != 5) {
                                 var12.add(new mxPoint(var7.y[var13], var14));
                              } else {
                                 var12.add(new mxPoint(var14, var7.y[var13]));
                              }

                              this.limitX = Math.max(this.limitX, var14);
                              ++var13;
                           }
                        } else {
                           for(var13 = var7.x.length - 1; var13 >= 0; --var13) {
                              var14 = var7.x[var13] + var8;
                              if (this.orientation != 1 && this.orientation != 5) {
                                 var12.add(new mxPoint(var7.y[var13], var14));
                              } else {
                                 var12.add(new mxPoint(var14, var7.y[var13]));
                              }

                              this.limitX = Math.max(this.limitX, var14);
                           }
                        }

                        this.layout.setEdgePoints(var11, var12);
                        if (var8 == 0.0) {
                           var8 = this.parallelEdgeSpacing;
                        } else if (var8 > 0.0) {
                           var8 = -var8;
                        } else {
                           var8 = -var8 + this.parallelEdgeSpacing;
                        }
                     }

                     var7.temp[0] = 101207;
                  }
               }
            }
         }
      }

   }

   private void processReversedEdge(mxGraphHierarchyEdge var1, Object var2) {
   }

   public double getInterRankCellSpacing() {
      return this.interRankCellSpacing;
   }

   public void setInterRankCellSpacing(double var1) {
      this.interRankCellSpacing = var1;
   }

   public double getIntraCellSpacing() {
      return this.intraCellSpacing;
   }

   public void setIntraCellSpacing(double var1) {
      this.intraCellSpacing = var1;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public void setOrientation(int var1) {
      this.orientation = var1;
   }

   public double getLimitX() {
      return this.limitX;
   }

   public void setLimitX(double var1) {
      this.limitX = var1;
   }

   public boolean isFineTuning() {
      return this.fineTuning;
   }

   public void setFineTuning(boolean var1) {
      this.fineTuning = var1;
   }

   public void setLoggerLevel(Level var1) {
      try {
         logger.setLevel(var1);
      } catch (SecurityException var3) {
      }

   }

   protected class AreaSpatialCache extends Rectangle2D.Double {
      public Set cells = new HashSet();
   }

   protected class WeightedCellSorter implements Comparable {
      public int weightedValue;
      public boolean nudge;
      public boolean visited;
      public int rankIndex;
      public mxGraphAbstractHierarchyCell cell;

      public WeightedCellSorter() {
         this((mxGraphAbstractHierarchyCell)null, 0);
      }

      public WeightedCellSorter(mxGraphAbstractHierarchyCell var2, int var3) {
         this.weightedValue = 0;
         this.nudge = false;
         this.visited = false;
         this.cell = null;
         this.cell = var2;
         this.weightedValue = var3;
      }

      public int compareTo(Object var1) {
         if (var1 instanceof WeightedCellSorter) {
            if (this.weightedValue > ((WeightedCellSorter)var1).weightedValue) {
               return -1;
            } else if (this.weightedValue < ((WeightedCellSorter)var1).weightedValue) {
               return 1;
            } else {
               return this.nudge ? -1 : 1;
            }
         } else {
            return 0;
         }
      }
   }
}
