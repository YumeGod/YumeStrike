package com.mxgraph.layout.hierarchical.stage;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyEdge;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyModel;
import com.mxgraph.layout.hierarchical.model.mxGraphHierarchyNode;
import com.mxgraph.view.mxGraph;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class mxMinimumCycleRemover implements mxHierarchicalLayoutStage {
   protected mxHierarchicalLayout layout;

   public mxMinimumCycleRemover(mxHierarchicalLayout var1) {
      this.layout = var1;
   }

   public void execute(Object var1) {
      mxGraphHierarchyModel var2 = this.layout.getModel();
      final HashSet var3 = new HashSet();
      final HashSet var4 = new HashSet(var2.getVertexMapping().values());
      mxGraphHierarchyNode[] var5 = null;
      if (var2.roots != null) {
         Object[] var6 = var2.roots.toArray();
         var5 = new mxGraphHierarchyNode[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            Object var8 = var6[var7];
            mxGraphHierarchyNode var9 = (mxGraphHierarchyNode)var2.getVertexMapping().get(var8);
            var5[var7] = var9;
         }
      }

      var2.visit(new mxGraphHierarchyModel.CellVisitor() {
         public void visit(mxGraphHierarchyNode var1, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3x, int var4x, int var5) {
            if (var2.isAncestor(var1)) {
               var3x.invert();
               var1.connectsAsSource.remove(var3x);
               var1.connectsAsTarget.add(var3x);
               var2.connectsAsTarget.remove(var3x);
               var2.connectsAsSource.add(var3x);
            }

            var3.add(var2);
            var4.remove(var2);
         }
      }, var5, true, (Set)null);
      HashSet var15 = null;
      if (var4.size() > 0) {
         var15 = new HashSet(var4);
      }

      HashSet var16 = new HashSet(var3);
      mxGraphHierarchyNode[] var17 = new mxGraphHierarchyNode[1];
      var4.toArray(var17);
      var2.visit(new mxGraphHierarchyModel.CellVisitor() {
         public void visit(mxGraphHierarchyNode var1, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3x, int var4x, int var5) {
            if (var2.isAncestor(var1)) {
               var3x.invert();
               var1.connectsAsSource.remove(var3x);
               var1.connectsAsTarget.add(var3x);
               var2.connectsAsTarget.remove(var3x);
               var2.connectsAsSource.add(var3x);
            }

            var3.add(var2);
            var4.remove(var2);
         }
      }, var17, true, var16);
      mxGraph var18 = this.layout.getGraph();
      if (var15 != null && var15.size() > 0) {
         Iterator var10 = var15.iterator();
         List var11 = var2.roots;

         while(var10.hasNext()) {
            mxGraphHierarchyNode var12 = (mxGraphHierarchyNode)var10.next();
            Object var13 = var12.cell;
            int var14 = var18.getIncomingEdges(var13).length;
            if (var14 == 0) {
               var11.add(var13);
            }
         }
      }

   }
}
