package com.mxgraph.layout.hierarchical.model;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class mxGraphHierarchyModel {
   protected boolean scanRanksFromSinks = true;
   public int maxRank;
   protected Map vertexMapper = null;
   protected Map edgeMapper = null;
   public Map ranks = null;
   public List roots;
   public Object parent = null;
   protected int dfsCount = 0;
   protected boolean deterministic = false;
   private final int SOURCESCANSTARTRANK = 100000000;

   public mxGraphHierarchyModel(mxHierarchicalLayout var1, Object[] var2, List var3, Object var4, boolean var5, boolean var6, boolean var7) {
      mxGraph var8 = var1.getGraph();
      this.deterministic = var6;
      this.scanRanksFromSinks = var7;
      this.roots = var3;
      this.parent = var4;
      if (var2 == null) {
         var2 = var8.getChildVertices(var4);
      }

      if (var5) {
         this.formOrderedHierarchy(var1, var2, var4);
      } else {
         this.vertexMapper = new Hashtable(var2.length);
         this.edgeMapper = new Hashtable(var2.length);
         if (var7) {
            this.maxRank = 0;
         } else {
            this.maxRank = 100000000;
         }

         mxGraphHierarchyNode[] var9 = new mxGraphHierarchyNode[var2.length];
         this.createInternalCells(var1, var2, var9);

         for(int var10 = 0; var10 < var2.length; ++var10) {
            Collection var11 = var9[var10].connectsAsSource;
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
               mxGraphHierarchyEdge var13 = (mxGraphHierarchyEdge)var12.next();
               List var14 = var13.edges;
               Iterator var15 = var14.iterator();
               if (var15.hasNext()) {
                  Object var16 = var15.next();
                  Object var17 = var8.getView().getVisibleTerminal(var16, false);
                  mxGraphHierarchyNode var18 = (mxGraphHierarchyNode)this.vertexMapper.get(var17);
                  if (var18 != null && var9[var10] != var18) {
                     var13.target = var18;
                     if (var18.connectsAsTarget.size() == 0) {
                        var18.connectsAsTarget = new LinkedHashSet(4);
                     }

                     var18.connectsAsTarget.add(var13);
                  }
               }
            }

            var9[var10].temp[0] = 1;
         }
      }

   }

   public void formOrderedHierarchy(mxHierarchicalLayout var1, Object[] var2, Object var3) {
      mxGraph var4 = var1.getGraph();
      this.vertexMapper = new Hashtable(var2.length * 2);
      this.edgeMapper = new Hashtable(var2.length);
      this.maxRank = 0;
      mxGraphHierarchyNode[] var5 = new mxGraphHierarchyNode[var2.length];
      this.createInternalCells(var1, var2, var5);
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var2.length; ++var7) {
         Collection var8 = var5[var7].connectsAsSource;
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            mxGraphHierarchyEdge var10 = (mxGraphHierarchyEdge)var9.next();
            List var11 = var10.edges;
            Iterator var12 = var11.iterator();
            if (var12.hasNext()) {
               Object var13 = var12.next();
               Object var14 = var4.getView().getVisibleTerminal(var13, false);
               mxGraphHierarchyNode var15 = (mxGraphHierarchyNode)this.vertexMapper.get(var14);
               if (var15 != null && var5[var7] != var15) {
                  var10.target = var15;
                  if (var15.connectsAsTarget.size() == 0) {
                     var15.connectsAsTarget = new ArrayList(4);
                  }

                  if (var15.temp[0] == 1) {
                     var10.invert();
                     var15.connectsAsSource.add(var10);
                     var6.add(var10);
                     var5[var7].connectsAsTarget.add(var10);
                  } else {
                     var15.connectsAsTarget.add(var10);
                  }
               }
            }
         }

         Iterator var16 = var6.iterator();

         while(var16.hasNext()) {
            var5[var7].connectsAsSource.remove(var16.next());
         }

         var6.clear();
         var5[var7].temp[0] = 1;
      }

   }

   protected void createInternalCells(mxHierarchicalLayout var1, Object[] var2, mxGraphHierarchyNode[] var3) {
      mxGraph var4 = var1.getGraph();

      label66:
      for(int var5 = 0; var5 < var2.length; ++var5) {
         var3[var5] = new mxGraphHierarchyNode(var2[var5]);
         this.vertexMapper.put(var2[var5], var3[var5]);
         Object[] var6 = var4.getConnections(var2[var5], this.parent);
         List var7 = Arrays.asList(var4.getOpposites(var6, var2[var5]));
         var3[var5].connectsAsSource = new LinkedHashSet(var7.size());
         Iterator var8 = var7.iterator();

         while(true) {
            Object[] var10;
            do {
               do {
                  Object var9;
                  do {
                     do {
                        do {
                           if (!var8.hasNext()) {
                              var3[var5].temp[0] = 0;
                              continue label66;
                           }

                           var9 = var8.next();
                        } while(var9 == var2[var5]);
                     } while(!var4.getModel().isVertex(var9));
                  } while(var1.isVertexIgnored(var9));

                  var10 = var4.getEdgesBetween(var2[var5], var9, true);
               } while(var10 == null);
            } while(var10.length <= 0);

            ArrayList var11 = new ArrayList(var10.length);

            for(int var12 = 0; var12 < var10.length; ++var12) {
               var11.add(var10[var12]);
            }

            mxGraphHierarchyEdge var15 = new mxGraphHierarchyEdge(var11);
            Iterator var13 = var11.iterator();

            while(var13.hasNext()) {
               Object var14 = var13.next();
               this.edgeMapper.put(var14, var15);
               var4.resetEdge(var14);
               if (var1.isDisableEdgeStyle()) {
                  var1.setEdgeStyleEnabled(var14, false);
               }
            }

            var15.source = var3[var5];
            var3[var5].connectsAsSource.add(var15);
         }
      }

   }

   public void initialRank() {
      Collection var1 = this.vertexMapper.values();
      LinkedList var2 = new LinkedList();
      Iterator var3;
      mxGraphHierarchyNode var4;
      if (!this.scanRanksFromSinks && this.roots != null) {
         var3 = this.roots.iterator();

         while(var3.hasNext()) {
            var4 = (mxGraphHierarchyNode)this.vertexMapper.get(var3.next());
            if (var4 != null) {
               var2.add(var4);
            }
         }
      }

      if (this.scanRanksFromSinks) {
         var3 = var1.iterator();

         label164:
         while(true) {
            do {
               if (!var3.hasNext()) {
                  break label164;
               }

               var4 = (mxGraphHierarchyNode)var3.next();
            } while(var4.connectsAsSource != null && !var4.connectsAsSource.isEmpty());

            var2.add(var4);
         }
      }

      if (var2.isEmpty()) {
         var3 = var1.iterator();

         label151:
         while(true) {
            do {
               if (!var3.hasNext()) {
                  break label151;
               }

               var4 = (mxGraphHierarchyNode)var3.next();
            } while(var4.connectsAsTarget != null && !var4.connectsAsTarget.isEmpty());

            var2.add(var4);
         }
      }

      for(var3 = var1.iterator(); var3.hasNext(); var4.temp[0] = -1) {
         var4 = (mxGraphHierarchyNode)var3.next();
      }

      ArrayList var14 = new ArrayList(var2);

      mxGraphHierarchyNode var5;
      while(!var2.isEmpty()) {
         var5 = (mxGraphHierarchyNode)var2.getFirst();
         Collection var6;
         Collection var7;
         if (this.scanRanksFromSinks) {
            var6 = var5.connectsAsSource;
            var7 = var5.connectsAsTarget;
         } else {
            var6 = var5.connectsAsTarget;
            var7 = var5.connectsAsSource;
         }

         boolean var8 = true;
         Iterator var9 = var6.iterator();
         int var10 = 0;
         if (!this.scanRanksFromSinks) {
            var10 = 100000000;
         }

         while(var8 && var9.hasNext()) {
            mxGraphHierarchyEdge var11 = (mxGraphHierarchyEdge)var9.next();
            if (var11.temp[0] == 5270620) {
               mxGraphHierarchyNode var12;
               if (this.scanRanksFromSinks) {
                  var12 = var11.target;
               } else {
                  var12 = var11.source;
               }

               if (this.scanRanksFromSinks) {
                  var10 = Math.max(var10, var12.temp[0] + 1);
               } else {
                  var10 = Math.min(var10, var12.temp[0] - 1);
               }
            } else {
               var8 = false;
            }
         }

         if (var8) {
            var5.temp[0] = var10;
            if (this.scanRanksFromSinks) {
               this.maxRank = Math.max(this.maxRank, var10);
            } else {
               this.maxRank = Math.min(this.maxRank, var10);
            }

            if (var7 != null) {
               Iterator var21 = var7.iterator();

               while(var21.hasNext()) {
                  mxGraphHierarchyEdge var23 = (mxGraphHierarchyEdge)var21.next();
                  var23.temp[0] = 5270620;
                  mxGraphHierarchyNode var13;
                  if (this.scanRanksFromSinks) {
                     var13 = var23.source;
                  } else {
                     var13 = var23.target;
                  }

                  if (var13.temp[0] == -1) {
                     var2.addLast(var13);
                     var13.temp[0] = -2;
                  }
               }
            }

            var2.removeFirst();
         } else {
            Object var20 = var2.removeFirst();
            var2.addLast(var5);
            if (var20 == var5 && var2.size() == 1) {
               break;
            }
         }
      }

      if (this.scanRanksFromSinks) {
         for(int var15 = 0; var15 < var14.size(); ++var15) {
            mxGraphHierarchyNode var16 = (mxGraphHierarchyNode)var14.get(var15);
            int var17 = 1000000;

            for(Iterator var18 = var16.connectsAsTarget.iterator(); var18.hasNext(); var17 = var16.temp[0]) {
               mxGraphHierarchyEdge var19 = (mxGraphHierarchyEdge)var18.next();
               mxGraphHierarchyNode var22 = var19.source;
               var16.temp[0] = Math.min(var17, var22.temp[0] - 1);
            }
         }
      } else {
         int[] var10000;
         for(var3 = var1.iterator(); var3.hasNext(); var10000[0] -= this.maxRank) {
            var5 = (mxGraphHierarchyNode)var3.next();
            var10000 = var5.temp;
         }

         this.maxRank = 100000000 - this.maxRank;
      }

   }

   public void fixRanks() {
      final mxGraphHierarchyRank[] var1 = new mxGraphHierarchyRank[this.maxRank + 1];
      this.ranks = new LinkedHashMap(this.maxRank + 1);

      for(int var2 = 0; var2 < this.maxRank + 1; ++var2) {
         var1[var2] = new mxGraphHierarchyRank();
         this.ranks.put(new Integer(var2), var1[var2]);
      }

      mxGraphHierarchyNode[] var7 = null;
      if (this.roots != null) {
         Object[] var3 = this.roots.toArray();
         var7 = new mxGraphHierarchyNode[var3.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            Object var5 = var3[var4];
            mxGraphHierarchyNode var6 = (mxGraphHierarchyNode)this.vertexMapper.get(var5);
            var7[var4] = var6;
         }
      }

      this.visit(new CellVisitor() {
         public void visit(mxGraphHierarchyNode var1x, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3, int var4, int var5) {
            if (var5 == 0 && var2.maxRank < 0 && var2.minRank < 0) {
               var1[var2.temp[0]].add(var2);
               var2.maxRank = var2.temp[0];
               var2.minRank = var2.temp[0];
               var2.temp[0] = var1[var2.maxRank].size() - 1;
            }

            if (var1x != null && var3 != null) {
               int var7 = var1x.maxRank - var2.maxRank;
               if (var7 > 1) {
                  mxGraphHierarchyEdge var8 = var3;
                  var3.maxRank = var1x.maxRank;
                  var3.minRank = var2.maxRank;
                  var3.temp = new int[var7 - 1];
                  var3.x = new double[var7 - 1];
                  var3.y = new double[var7 - 1];

                  for(int var9 = var3.minRank + 1; var9 < var8.maxRank; ++var9) {
                     var1[var9].add(var8);
                     var8.setGeneralPurposeVariable(var9, var1[var9].size() - 1);
                  }
               }
            }

         }
      }, var7, false, (Set)null);
   }

   public void visit(CellVisitor var1, mxGraphHierarchyNode[] var2, boolean var3, Set var4) {
      if (var2 != null) {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            mxGraphHierarchyNode var6 = var2[var5];
            if (var6 != null) {
               if (var4 == null) {
                  var4 = new HashSet();
               }

               if (var3) {
                  var6.hashCode = new int[2];
                  var6.hashCode[0] = this.dfsCount;
                  var6.hashCode[1] = var5;
                  this.dfs((mxGraphHierarchyNode)null, var6, (mxGraphHierarchyEdge)null, var1, (Set)var4, var6.hashCode, var5, 0);
               } else {
                  this.dfs((mxGraphHierarchyNode)null, var6, (mxGraphHierarchyEdge)null, var1, (Set)var4, 0);
               }
            }
         }

         ++this.dfsCount;
      }

   }

   public void dfs(mxGraphHierarchyNode var1, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3, CellVisitor var4, Set var5, int var6) {
      if (var2 != null) {
         if (!var5.contains(var2)) {
            var4.visit(var1, var2, var3, var6, 0);
            var5.add(var2);
            Object[] var7 = var2.connectsAsSource.toArray();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               mxGraphHierarchyEdge var9 = (mxGraphHierarchyEdge)var7[var8];
               mxGraphHierarchyNode var10 = var9.target;
               this.dfs(var2, var10, var9, var4, var5, var6 + 1);
            }
         } else {
            var4.visit(var1, var2, var3, var6, 1);
         }
      }

   }

   public void dfs(mxGraphHierarchyNode var1, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3, CellVisitor var4, Set var5, int[] var6, int var7, int var8) {
      if (var2 != null) {
         if (var1 != null && (var2.hashCode == null || var2.hashCode[0] != var1.hashCode[0])) {
            int var9 = var1.hashCode.length + 1;
            var2.hashCode = new int[var9];
            System.arraycopy(var1.hashCode, 0, var2.hashCode, 0, var1.hashCode.length);
            var2.hashCode[var9 - 1] = var7;
         }

         if (!var5.contains(var2)) {
            var4.visit(var1, var2, var3, var8, 0);
            var5.add(var2);
            Object[] var13 = var2.connectsAsSource.toArray();

            for(int var10 = 0; var10 < var13.length; ++var10) {
               mxGraphHierarchyEdge var11 = (mxGraphHierarchyEdge)var13[var10];
               mxGraphHierarchyNode var12 = var11.target;
               this.dfs(var2, var12, var11, var4, var5, var2.hashCode, var10, var8 + 1);
            }
         } else {
            var4.visit(var1, var2, var3, var8, 1);
         }
      }

   }

   public Map getVertexMapping() {
      if (this.vertexMapper == null) {
         this.vertexMapper = new Hashtable();
      }

      return this.vertexMapper;
   }

   public void setVertexMapping(Map var1) {
      this.vertexMapper = var1;
   }

   public Map getEdgeMapper() {
      return this.edgeMapper;
   }

   public void setEdgeMapper(Map var1) {
      this.edgeMapper = var1;
   }

   public int getDfsCount() {
      return this.dfsCount;
   }

   public void setDfsCount(int var1) {
      this.dfsCount = var1;
   }

   public boolean isDeterministic() {
      return this.deterministic;
   }

   public void setDeterministic(boolean var1) {
      this.deterministic = var1;
   }

   public interface CellVisitor {
      void visit(mxGraphHierarchyNode var1, mxGraphHierarchyNode var2, mxGraphHierarchyEdge var3, int var4, int var5);
   }
}
