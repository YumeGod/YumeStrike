package com.mxgraph.layout.hierarchical.model;

import java.util.List;

public abstract class mxGraphAbstractHierarchyCell {
   public int maxRank = -1;
   public int minRank = -1;
   public double[] x = new double[1];
   public double[] y = new double[1];
   public double width = 0.0;
   public double height = 0.0;
   protected List[] nextLayerConnectedCells = null;
   protected List[] previousLayerConnectedCells = null;
   public int[] temp = new int[1];

   public abstract List getNextLayerConnectedCells(int var1);

   public abstract List getPreviousLayerConnectedCells(int var1);

   public abstract boolean isEdge();

   public abstract boolean isVertex();

   public abstract int getGeneralPurposeVariable(int var1);

   public abstract void setGeneralPurposeVariable(int var1, int var2);

   public void setX(int var1, double var2) {
      if (this.isVertex()) {
         this.x[0] = var2;
      } else if (this.isEdge()) {
         this.x[var1 - this.minRank - 1] = var2;
      }

   }

   public double getX(int var1) {
      if (this.isVertex()) {
         return this.x[0];
      } else {
         return this.isEdge() ? this.x[var1 - this.minRank - 1] : 0.0;
      }
   }

   public void setY(int var1, double var2) {
      if (this.isVertex()) {
         this.y[0] = var2;
      } else if (this.isEdge()) {
         this.y[var1 - this.minRank - 1] = var2;
      }

   }
}
