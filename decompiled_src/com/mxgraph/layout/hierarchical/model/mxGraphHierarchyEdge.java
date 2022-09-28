package com.mxgraph.layout.hierarchical.model;

import java.util.ArrayList;
import java.util.List;

public class mxGraphHierarchyEdge extends mxGraphAbstractHierarchyCell {
   public List edges;
   public mxGraphHierarchyNode source;
   public mxGraphHierarchyNode target;
   protected boolean isReversed = false;

   public mxGraphHierarchyEdge(List var1) {
      this.edges = var1;
   }

   public void invert() {
      mxGraphHierarchyNode var1 = this.source;
      this.source = this.target;
      this.target = var1;
      this.isReversed = !this.isReversed;
   }

   public boolean isReversed() {
      return this.isReversed;
   }

   public void setReversed(boolean var1) {
      this.isReversed = var1;
   }

   public List getNextLayerConnectedCells(int var1) {
      if (this.nextLayerConnectedCells == null) {
         this.nextLayerConnectedCells = new ArrayList[this.temp.length];

         for(int var2 = 0; var2 < this.nextLayerConnectedCells.length; ++var2) {
            this.nextLayerConnectedCells[var2] = new ArrayList(2);
            if (var2 == this.nextLayerConnectedCells.length - 1) {
               this.nextLayerConnectedCells[var2].add(this.source);
            } else {
               this.nextLayerConnectedCells[var2].add(this);
            }
         }
      }

      return this.nextLayerConnectedCells[var1 - this.minRank - 1];
   }

   public List getPreviousLayerConnectedCells(int var1) {
      if (this.previousLayerConnectedCells == null) {
         this.previousLayerConnectedCells = new ArrayList[this.temp.length];

         for(int var2 = 0; var2 < this.previousLayerConnectedCells.length; ++var2) {
            this.previousLayerConnectedCells[var2] = new ArrayList(2);
            if (var2 == 0) {
               this.previousLayerConnectedCells[var2].add(this.target);
            } else {
               this.previousLayerConnectedCells[var2].add(this);
            }
         }
      }

      return this.previousLayerConnectedCells[var1 - this.minRank - 1];
   }

   public boolean isEdge() {
      return true;
   }

   public boolean isVertex() {
      return false;
   }

   public int getGeneralPurposeVariable(int var1) {
      return this.temp[var1 - this.minRank - 1];
   }

   public void setGeneralPurposeVariable(int var1, int var2) {
      this.temp[var1 - this.minRank - 1] = var2;
   }
}
