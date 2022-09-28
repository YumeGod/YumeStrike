package com.mxgraph.layout.hierarchical.stage;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphAbstractHierarchyCell;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyRank;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class mxMedianHybridCrossingReduction implements mxHierarchicalLayoutStage {
   protected mxHierarchicalLayout layout;
   protected int maxIterations = 24;
   protected mxGraphAbstractHierarchyCell[][] nestedBestRanks = (mxGraphAbstractHierarchyCell[][])null;
   protected int currentBestCrossings = 0;
   protected int iterationsWithoutImprovement = 0;
   protected int maxNoImprovementIterations = 2;

   public mxMedianHybridCrossingReduction(mxHierarchicalLayout var1) {
      this.layout = var1;
   }

   public void execute(Object var1) {
      mxGraphHierarchyModel var2 = this.layout.getModel();
      this.nestedBestRanks = new mxGraphAbstractHierarchyCell[var2.ranks.size()][];

      int var3;
      for(var3 = 0; var3 < this.nestedBestRanks.length; ++var3) {
         mxGraphHierarchyRank var4 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var3));
         this.nestedBestRanks[var3] = new mxGraphAbstractHierarchyCell[var4.size()];
         var4.toArray(this.nestedBestRanks[var3]);
      }

      this.iterationsWithoutImprovement = 0;
      this.currentBestCrossings = this.calculateCrossings(var2);

      int var5;
      for(var3 = 0; var3 < this.maxIterations && this.iterationsWithoutImprovement < this.maxNoImprovementIterations; ++var3) {
         this.weightedMedian(var3, var2);
         this.transpose(var3, var2);
         int var11 = this.calculateCrossings(var2);
         mxGraphHierarchyRank var6;
         Iterator var7;
         int var8;
         mxGraphAbstractHierarchyCell var9;
         if (var11 < this.currentBestCrossings) {
            this.currentBestCrossings = var11;
            this.iterationsWithoutImprovement = 0;

            for(var5 = 0; var5 < this.nestedBestRanks.length; ++var5) {
               var6 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var5));
               var7 = var6.iterator();

               for(var8 = 0; var8 < var6.size(); ++var8) {
                  var9 = (mxGraphAbstractHierarchyCell)var7.next();
                  this.nestedBestRanks[var5][var9.getGeneralPurposeVariable(var5)] = var9;
               }
            }
         } else {
            ++this.iterationsWithoutImprovement;

            for(var5 = 0; var5 < this.nestedBestRanks.length; ++var5) {
               var6 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var5));
               var7 = var6.iterator();

               for(var8 = 0; var8 < var6.size(); ++var8) {
                  var9 = (mxGraphAbstractHierarchyCell)var7.next();
                  var9.setGeneralPurposeVariable(var5, var8);
               }
            }
         }

         if (this.currentBestCrossings == 0) {
            break;
         }
      }

      LinkedHashMap var10 = new LinkedHashMap(var2.maxRank + 1);
      mxGraphHierarchyRank[] var12 = new mxGraphHierarchyRank[var2.maxRank + 1];

      for(var5 = 0; var5 < var2.maxRank + 1; ++var5) {
         var12[var5] = new mxGraphHierarchyRank();
         var10.put(new Integer(var5), var12[var5]);
      }

      for(var5 = 0; var5 < this.nestedBestRanks.length; ++var5) {
         for(int var13 = 0; var13 < this.nestedBestRanks[var5].length; ++var13) {
            var12[var5].add(this.nestedBestRanks[var5][var13]);
         }
      }

      var2.ranks = var10;
   }

   private int calculateCrossings(mxGraphHierarchyModel var1) {
      int var2 = var1.ranks.size();
      int var3 = 0;

      for(int var4 = 1; var4 < var2; ++var4) {
         var3 += this.calculateRankCrossing(var4, var1);
      }

      return var3;
   }

   protected int calculateRankCrossing(int var1, mxGraphHierarchyModel var2) {
      int var3 = 0;
      mxGraphHierarchyRank var4 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var1));
      mxGraphHierarchyRank var5 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var1 - 1));
      int var6 = var4.size();
      int var7 = var5.size();
      int[][] var8 = new int[var6][var7];
      Iterator var9 = var4.iterator();

      int var11;
      while(var9.hasNext()) {
         mxGraphAbstractHierarchyCell var10 = (mxGraphAbstractHierarchyCell)var9.next();
         var11 = var10.getGeneralPurposeVariable(var1);
         List var12 = var10.getPreviousLayerConnectedCells(var1);

         int var15;
         for(Iterator var13 = var12.iterator(); var13.hasNext(); var8[var11][var15] = 201207) {
            mxGraphAbstractHierarchyCell var14 = (mxGraphAbstractHierarchyCell)var13.next();
            var15 = var14.getGeneralPurposeVariable(var1 - 1);
         }
      }

      for(int var16 = 0; var16 < var6; ++var16) {
         for(var11 = 0; var11 < var7; ++var11) {
            if (var8[var16][var11] == 201207) {
               int var17;
               int var18;
               for(var17 = var16 + 1; var17 < var6; ++var17) {
                  for(var18 = 0; var18 < var11; ++var18) {
                     if (var8[var17][var18] == 201207) {
                        ++var3;
                     }
                  }
               }

               for(var17 = 0; var17 < var16; ++var17) {
                  for(var18 = var11 + 1; var18 < var7; ++var18) {
                     if (var8[var17][var18] == 201207) {
                        ++var3;
                     }
                  }
               }
            }
         }
      }

      return var3 / 2;
   }

   private void transpose(int var1, mxGraphHierarchyModel var2) {
      boolean var3 = true;
      int var4 = 0;
      byte var5 = 10;

      while(var3 && var4++ < var5) {
         boolean var6 = var1 % 2 == 1 && var4 % 2 == 1;
         var3 = false;

         for(int var7 = 0; var7 < var2.ranks.size(); ++var7) {
            mxGraphHierarchyRank var8 = (mxGraphHierarchyRank)var2.ranks.get(new Integer(var7));
            mxGraphAbstractHierarchyCell[] var9 = new mxGraphAbstractHierarchyCell[var8.size()];
            Iterator var10 = var8.iterator();

            mxGraphAbstractHierarchyCell var12;
            for(int var11 = 0; var11 < var9.length; ++var11) {
               var12 = (mxGraphAbstractHierarchyCell)var10.next();
               var9[var12.getGeneralPurposeVariable(var7)] = var12;
            }

            List var26 = null;
            var12 = null;
            List var13 = null;
            List var14 = null;
            Object var15 = null;
            Object var16 = null;
            int[] var17 = null;
            int[] var18 = null;
            mxGraphAbstractHierarchyCell var19 = null;
            mxGraphAbstractHierarchyCell var20 = null;

            for(int var21 = 0; var21 < var8.size() - 1; ++var21) {
               int var22;
               List var27;
               int[] var28;
               int[] var29;
               if (var21 != 0) {
                  var26 = var13;
                  var27 = var14;
                  var28 = var17;
                  var29 = var18;
                  var19 = var20;
               } else {
                  var19 = var9[var21];
                  var26 = var19.getNextLayerConnectedCells(var7);
                  var27 = var19.getPreviousLayerConnectedCells(var7);
                  var28 = new int[var26.size()];
                  var29 = new int[var27.size()];

                  for(var22 = 0; var22 < var28.length; ++var22) {
                     var28[var22] = ((mxGraphAbstractHierarchyCell)var26.get(var22)).getGeneralPurposeVariable(var7 + 1);
                  }

                  for(var22 = 0; var22 < var29.length; ++var22) {
                     var29[var22] = ((mxGraphAbstractHierarchyCell)var27.get(var22)).getGeneralPurposeVariable(var7 - 1);
                  }
               }

               var20 = var9[var21 + 1];
               var13 = var20.getNextLayerConnectedCells(var7);
               var14 = var20.getPreviousLayerConnectedCells(var7);
               var17 = new int[var13.size()];
               var18 = new int[var14.size()];

               for(var22 = 0; var22 < var17.length; ++var22) {
                  var17[var22] = ((mxGraphAbstractHierarchyCell)var13.get(var22)).getGeneralPurposeVariable(var7 + 1);
               }

               for(var22 = 0; var22 < var18.length; ++var22) {
                  var18[var22] = ((mxGraphAbstractHierarchyCell)var14.get(var22)).getGeneralPurposeVariable(var7 - 1);
               }

               var22 = 0;
               int var23 = 0;

               int var24;
               int var25;
               for(var24 = 0; var24 < var28.length; ++var24) {
                  for(var25 = 0; var25 < var17.length; ++var25) {
                     if (var28[var24] > var17[var25]) {
                        ++var22;
                     }

                     if (var28[var24] < var17[var25]) {
                        ++var23;
                     }
                  }
               }

               for(var24 = 0; var24 < var29.length; ++var24) {
                  for(var25 = 0; var25 < var18.length; ++var25) {
                     if (var29[var24] > var18[var25]) {
                        ++var22;
                     }

                     if (var29[var24] < var18[var25]) {
                        ++var23;
                     }
                  }
               }

               if (var23 < var22 || var23 == var22 && var6) {
                  var24 = var19.getGeneralPurposeVariable(var7);
                  var19.setGeneralPurposeVariable(var7, var20.getGeneralPurposeVariable(var7));
                  var20.setGeneralPurposeVariable(var7, var24);
                  var13 = var26;
                  var14 = var27;
                  var17 = var28;
                  var18 = var29;
                  var20 = var19;
                  if (!var6) {
                     var3 = true;
                  }
               }
            }
         }
      }

   }

   private void weightedMedian(int var1, mxGraphHierarchyModel var2) {
      boolean var3 = var1 % 2 == 0;
      int var4;
      if (var3) {
         for(var4 = var2.maxRank - 1; var4 >= 0; --var4) {
            this.medianRank(var4, var3);
         }
      } else {
         for(var4 = 1; var4 < var2.maxRank; ++var4) {
            this.medianRank(var4, var3);
         }
      }

   }

   private void medianRank(int var1, boolean var2) {
      int var3 = this.nestedBestRanks[var1].length;
      MedianCellSorter[] var4 = new MedianCellSorter[var3];

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         mxGraphAbstractHierarchyCell var6 = this.nestedBestRanks[var1][var5];
         var4[var5] = new MedianCellSorter();
         var4[var5].cell = var6;
         var4[var5].nudge = !var2;
         List var7;
         if (var2) {
            var7 = var6.getNextLayerConnectedCells(var1);
         } else {
            var7 = var6.getPreviousLayerConnectedCells(var1);
         }

         int var8;
         if (var2) {
            var8 = var1 + 1;
         } else {
            var8 = var1 - 1;
         }

         if (var7 != null && var7.size() != 0) {
            var4[var5].medianValue = this.medianValue(var7, var8);
         } else {
            var4[var5].medianValue = -1.0;
         }
      }

      Arrays.sort(var4);

      for(var5 = 0; var5 < var3; ++var5) {
         var4[var5].cell.setGeneralPurposeVariable(var1, var5);
      }

   }

   private double medianValue(Collection var1, int var2) {
      double[] var3 = new double[var1.size()];
      int var4 = 0;

      for(Iterator var5 = var1.iterator(); var5.hasNext(); var3[var4++] = (double)((mxGraphAbstractHierarchyCell)var5.next()).getGeneralPurposeVariable(var2)) {
      }

      Arrays.sort(var3);
      if (var4 % 2 == 1) {
         return var3[var4 / 2];
      } else if (var4 == 2) {
         return (var3[0] + var3[1]) / 2.0;
      } else {
         int var6 = var4 / 2;
         double var7 = var3[var6 - 1] - var3[0];
         double var9 = var3[var4 - 1] - var3[var6];
         return (var3[var6 - 1] * var9 + var3[var6] * var7) / (var7 + var9);
      }
   }

   protected class MedianCellSorter implements Comparable {
      public double medianValue = 0.0;
      public boolean nudge = false;
      mxGraphAbstractHierarchyCell cell = null;

      public int compareTo(Object var1) {
         if (var1 instanceof MedianCellSorter) {
            if (this.medianValue < ((MedianCellSorter)var1).medianValue) {
               return -1;
            } else if (this.medianValue > ((MedianCellSorter)var1).medianValue) {
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
