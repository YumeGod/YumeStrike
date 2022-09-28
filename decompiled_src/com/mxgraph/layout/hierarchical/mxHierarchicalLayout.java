package com.mxgraph.layout.hierarchical;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.stage.mxCoordinateAssignment;
import com.mxgraph.layout.hierarchical.stage.mxMedianHybridCrossingReduction;
import com.mxgraph.layout.hierarchical.stage.mxMinimumCycleRemover;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mxHierarchicalLayout extends mxGraphLayout {
   protected Object[] roots;
   protected boolean resizeParent;
   protected boolean moveParent;
   protected int parentBorder;
   protected double intraCellSpacing;
   protected double interRankCellSpacing;
   protected double interHierarchySpacing;
   protected double parallelEdgeSpacing;
   protected int orientation;
   protected boolean disableEdgeStyle;
   protected boolean fineTuning;
   protected boolean deterministic;
   protected boolean fixRoots;
   protected boolean layoutFromSinks;
   protected mxGraphHierarchyModel model;
   private static Logger logger = Logger.getLogger("com.jgraph.layout.hierarchical.JGraphHierarchicalLayout");

   public mxHierarchicalLayout(mxGraph var1) {
      this(var1, 1);
   }

   public mxHierarchicalLayout(mxGraph var1, int var2) {
      super(var1);
      this.roots = null;
      this.resizeParent = false;
      this.moveParent = false;
      this.parentBorder = 0;
      this.intraCellSpacing = 30.0;
      this.interRankCellSpacing = 50.0;
      this.interHierarchySpacing = 60.0;
      this.parallelEdgeSpacing = 10.0;
      this.orientation = 1;
      this.disableEdgeStyle = true;
      this.fineTuning = true;
      this.fixRoots = false;
      this.layoutFromSinks = true;
      this.model = null;
      this.orientation = var2;
   }

   public mxGraphHierarchyModel getModel() {
      return this.model;
   }

   public void execute(Object var1) {
      this.execute(var1, (Object[])null);
   }

   public void execute(Object var1, Object[] var2) {
      if (var2 == null) {
         var2 = this.graph.findTreeRoots(var1);
      }

      this.roots = var2;
      mxIGraphModel var3 = this.graph.getModel();
      var3.beginUpdate();

      try {
         this.run(var1);
         if (this.isResizeParent() && !this.graph.isCellCollapsed(var1)) {
            this.graph.updateGroupBounds(new Object[]{var1}, this.getParentBorder(), this.isMoveParent());
         }
      } finally {
         var3.endUpdate();
      }

   }

   public void run(Object var1) {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = null;
      ArrayList var4 = null;
      ArrayList var5 = null;
      if (this.fixRoots) {
         var3 = new ArrayList();
         var4 = new ArrayList();
         var5 = new ArrayList();
      }

      Iterator var8;
      label78:
      for(int var6 = 0; var6 < this.roots.length; ++var6) {
         boolean var7 = true;
         var8 = var2.iterator();

         while(var7 && var8.hasNext()) {
            if (((Set)var8.next()).contains(this.roots[var6])) {
               var7 = false;
            }
         }

         if (var7) {
            Stack var9 = new Stack();
            var9.push(this.roots[var6]);
            HashSet var10 = null;
            if (this.fixRoots) {
               var3.add(this.roots[var6]);
               Point var11 = this.getVertexBounds(this.roots[var6]).getPoint();
               var4.add(var11);
               var10 = new HashSet();
            }

            HashSet var22 = new HashSet();

            while(true) {
               Object var12;
               do {
                  if (var9.isEmpty()) {
                     var2.add(var22);
                     if (this.fixRoots) {
                        var5.add(var10);
                     }
                     continue label78;
                  }

                  var12 = var9.pop();
               } while(var22.contains(var12));

               var22.add(var12);
               if (this.fixRoots) {
                  var10.addAll(Arrays.asList(this.graph.getIncomingEdges(var12, var1)));
               }

               Object[] var13 = this.graph.getConnections(var12, var1);
               Object[] var14 = this.graph.getOpposites(var13, var12);

               for(int var15 = 0; var15 < var14.length; ++var15) {
                  if (!var22.contains(var14[var15])) {
                     var9.push(var14[var15]);
                  }
               }
            }
         }
      }

      double var19 = 0.0;
      var8 = var2.iterator();
      int var20 = 0;

      while(var8.hasNext()) {
         Set var21 = (Set)var8.next();
         this.model = new mxGraphHierarchyModel(this, var21.toArray(), Arrays.asList(this.roots), var1, false, this.deterministic, this.layoutFromSinks);
         this.cycleStage(var1);
         this.layeringStage();
         this.crossingStage(var1);
         var19 = this.placementStage(var19, var1);
         if (this.fixRoots) {
            Object var23 = var3.get(var20);
            Point2D var24 = (Point2D)var4.get(var20);
            Point var25 = this.graph.getModel().getGeometry(var23).getPoint();
            double var26 = var24.getX() - var25.getX();
            double var16 = var24.getY() - var25.getY();
            this.graph.moveCells(var21.toArray(), var26, var16);
            Set var18 = (Set)var5.get(var20++);
            this.graph.moveCells(var18.toArray(), var26, var16);
         }
      }

   }

   public void cycleStage(Object var1) {
      mxMinimumCycleRemover var2 = new mxMinimumCycleRemover(this);
      var2.execute(var1);
   }

   public void layeringStage() {
      this.model.initialRank();
      this.model.fixRanks();
   }

   public void crossingStage(Object var1) {
      mxMedianHybridCrossingReduction var2 = new mxMedianHybridCrossingReduction(this);
      var2.execute(var1);
   }

   public double placementStage(double var1, Object var3) {
      mxCoordinateAssignment var4 = new mxCoordinateAssignment(this, this.intraCellSpacing, this.interRankCellSpacing, this.orientation, var1, this.parallelEdgeSpacing);
      var4.setFineTuning(this.fineTuning);
      var4.execute(var3);
      return var4.getLimitX() + this.interHierarchySpacing;
   }

   public boolean isResizeParent() {
      return this.resizeParent;
   }

   public void setResizeParent(boolean var1) {
      this.resizeParent = var1;
   }

   public boolean isMoveParent() {
      return this.moveParent;
   }

   public void setMoveParent(boolean var1) {
      this.moveParent = var1;
   }

   public int getParentBorder() {
      return this.parentBorder;
   }

   public void setParentBorder(int var1) {
      this.parentBorder = var1;
   }

   public double getIntraCellSpacing() {
      return this.intraCellSpacing;
   }

   public void setIntraCellSpacing(double var1) {
      this.intraCellSpacing = var1;
   }

   public double getInterRankCellSpacing() {
      return this.interRankCellSpacing;
   }

   public void setInterRankCellSpacing(double var1) {
      this.interRankCellSpacing = var1;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public void setOrientation(int var1) {
      this.orientation = var1;
   }

   public double getInterHierarchySpacing() {
      return this.interHierarchySpacing;
   }

   public void setInterHierarchySpacing(double var1) {
      this.interHierarchySpacing = var1;
   }

   public double getParallelEdgeSpacing() {
      return this.parallelEdgeSpacing;
   }

   public void setParallelEdgeSpacing(double var1) {
      this.parallelEdgeSpacing = var1;
   }

   public boolean isFineTuning() {
      return this.fineTuning;
   }

   public void setFineTuning(boolean var1) {
      this.fineTuning = var1;
   }

   public boolean isDisableEdgeStyle() {
      return this.disableEdgeStyle;
   }

   public void setDisableEdgeStyle(boolean var1) {
      this.disableEdgeStyle = var1;
   }

   public boolean isDeterministic() {
      return this.deterministic;
   }

   public void setDeterministic(boolean var1) {
      this.deterministic = var1;
   }

   public boolean isFixRoots() {
      return this.fixRoots;
   }

   public void setFixRoots(boolean var1) {
      this.fixRoots = var1;
   }

   public boolean isLayoutFromSinks() {
      return this.layoutFromSinks;
   }

   public void setLayoutFromSinks(boolean var1) {
      this.layoutFromSinks = var1;
   }

   public void setLoggerLevel(Level var1) {
      try {
         logger.setLevel(var1);
      } catch (SecurityException var3) {
      }

   }

   public String toString() {
      return "Hierarchical";
   }
}
