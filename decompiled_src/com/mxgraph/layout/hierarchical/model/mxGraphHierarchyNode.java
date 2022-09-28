package com.mxgraph.layout.hierarchical.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class mxGraphHierarchyNode extends mxGraphAbstractHierarchyCell {
   public static Collection emptyConnectionMap = new ArrayList(0);
   public Object cell = null;
   public Collection connectsAsTarget;
   public Collection connectsAsSource;
   public int[] hashCode;

   public mxGraphHierarchyNode(Object var1) {
      this.connectsAsTarget = emptyConnectionMap;
      this.connectsAsSource = emptyConnectionMap;
      this.cell = var1;
   }

   public int getRankValue() {
      return this.maxRank;
   }

   public List getNextLayerConnectedCells(int var1) {
      if (this.nextLayerConnectedCells == null) {
         this.nextLayerConnectedCells = new ArrayList[1];
         this.nextLayerConnectedCells[0] = new ArrayList(this.connectsAsTarget.size());
         Iterator var2 = this.connectsAsTarget.iterator();

         while(true) {
            while(var2.hasNext()) {
               mxGraphHierarchyEdge var3 = (mxGraphHierarchyEdge)var2.next();
               if (var3.maxRank != -1 && var3.maxRank != var1 + 1) {
                  this.nextLayerConnectedCells[0].add(var3);
               } else {
                  this.nextLayerConnectedCells[0].add(var3.source);
               }
            }

            return this.nextLayerConnectedCells[0];
         }
      } else {
         return this.nextLayerConnectedCells[0];
      }
   }

   public List getPreviousLayerConnectedCells(int var1) {
      if (this.previousLayerConnectedCells == null) {
         this.previousLayerConnectedCells = new ArrayList[1];
         this.previousLayerConnectedCells[0] = new ArrayList(this.connectsAsSource.size());
         Iterator var2 = this.connectsAsSource.iterator();

         while(true) {
            while(var2.hasNext()) {
               mxGraphHierarchyEdge var3 = (mxGraphHierarchyEdge)var2.next();
               if (var3.minRank != -1 && var3.minRank != var1 - 1) {
                  this.previousLayerConnectedCells[0].add(var3);
               } else {
                  this.previousLayerConnectedCells[0].add(var3.target);
               }
            }

            return this.previousLayerConnectedCells[0];
         }
      } else {
         return this.previousLayerConnectedCells[0];
      }
   }

   public boolean isEdge() {
      return false;
   }

   public boolean isVertex() {
      return true;
   }

   public int getGeneralPurposeVariable(int var1) {
      return this.temp[0];
   }

   public void setGeneralPurposeVariable(int var1, int var2) {
      this.temp[0] = var2;
   }

   public boolean isAncestor(mxGraphHierarchyNode var1) {
      if (var1 != null && this.hashCode != null && var1.hashCode != null && this.hashCode.length < var1.hashCode.length) {
         if (this.hashCode == var1.hashCode) {
            return true;
         } else if (this.hashCode != null && this.hashCode != null) {
            for(int var2 = 0; var2 < this.hashCode.length; ++var2) {
               if (this.hashCode[var2] != var1.hashCode[var2]) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
