package com.mxgraph.model;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUndoableEdit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class mxGraphModel extends mxEventSource implements mxIGraphModel {
   protected mxICell root;
   protected Map cells;
   protected boolean maintainEdgeParent;
   protected boolean createIds;
   protected int nextId;
   protected transient mxUndoableEdit currentEdit;
   protected transient int updateLevel;
   protected transient boolean endingUpdate;

   public mxGraphModel() {
      this((Object)null);
   }

   public mxGraphModel(Object var1) {
      this.maintainEdgeParent = true;
      this.createIds = true;
      this.nextId = 0;
      this.updateLevel = 0;
      this.endingUpdate = false;
      this.currentEdit = this.createUndoableEdit();
      if (var1 != null) {
         this.setRoot(var1);
      } else {
         this.clear();
      }

   }

   public void clear() {
      this.setRoot(this.createRoot());
   }

   public int getUpdateLevel() {
      return this.updateLevel;
   }

   public Object createRoot() {
      mxCell var1 = new mxCell();
      var1.insert(new mxCell());
      return var1;
   }

   public Map getCells() {
      return this.cells;
   }

   public Object getCell(String var1) {
      Object var2 = null;
      if (this.cells != null) {
         var2 = this.cells.get(var1);
      }

      return var2;
   }

   public boolean isMaintainEdgeParent() {
      return this.maintainEdgeParent;
   }

   public void setMaintainEdgeParent(boolean var1) {
      this.maintainEdgeParent = var1;
   }

   public boolean isCreateIds() {
      return this.createIds;
   }

   public void setCreateIds(boolean var1) {
      this.createIds = var1;
   }

   public Object getRoot() {
      return this.root;
   }

   public Object setRoot(Object var1) {
      this.execute(new mxRootChange(this, var1));
      return var1;
   }

   protected Object rootChanged(Object var1) {
      mxICell var2 = this.root;
      this.root = (mxICell)var1;
      this.nextId = 0;
      this.cells = null;
      this.cellAdded(var1);
      return var2;
   }

   protected mxUndoableEdit createUndoableEdit() {
      return new mxUndoableEdit(this) {
         public void dispatch() {
            ((mxGraphModel)this.source).fireEvent(new mxEventObject("change", new Object[]{"changes", this.changes}));
         }
      };
   }

   public Object[] cloneCells(Object[] var1, boolean var2) {
      Hashtable var3 = new Hashtable();
      Object[] var4 = new Object[var1.length];

      int var5;
      for(var5 = 0; var5 < var1.length; ++var5) {
         try {
            var4[var5] = this.cloneCell(var1[var5], var3, var2);
         } catch (CloneNotSupportedException var7) {
         }
      }

      for(var5 = 0; var5 < var1.length; ++var5) {
         this.restoreClone(var4[var5], var1[var5], var3);
      }

      return var4;
   }

   protected Object cloneCell(Object var1, Map var2, boolean var3) throws CloneNotSupportedException {
      if (!(var1 instanceof mxICell)) {
         return null;
      } else {
         mxICell var4 = (mxICell)((mxICell)var1).clone();
         var2.put(var1, var4);
         if (var3) {
            int var5 = this.getChildCount(var1);

            for(int var6 = 0; var6 < var5; ++var6) {
               Object var7 = this.cloneCell(this.getChildAt(var1, var6), var2, true);
               var4.insert((mxICell)var7);
            }
         }

         return var4;
      }
   }

   protected void restoreClone(Object var1, Object var2, Map var3) {
      if (var1 instanceof mxICell) {
         mxICell var4 = (mxICell)var1;
         Object var5 = this.getTerminal(var2, true);
         if (var5 instanceof mxICell) {
            mxICell var6 = (mxICell)var3.get(var5);
            if (var6 != null) {
               var6.insertEdge(var4, true);
            }
         }

         Object var10 = this.getTerminal(var2, false);
         if (var10 instanceof mxICell) {
            mxICell var7 = (mxICell)var3.get(var10);
            if (var7 != null) {
               var7.insertEdge(var4, false);
            }
         }
      }

      int var8 = this.getChildCount(var1);

      for(int var9 = 0; var9 < var8; ++var9) {
         this.restoreClone(this.getChildAt(var1, var9), this.getChildAt(var2, var9), var3);
      }

   }

   public boolean isAncestor(Object var1, Object var2) {
      while(var2 != null && var2 != var1) {
         var2 = this.getParent(var2);
      }

      return var2 == var1;
   }

   public boolean contains(Object var1) {
      return this.isAncestor(this.getRoot(), var1);
   }

   public Object getParent(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getParent() : null;
   }

   public Object add(Object var1, Object var2, int var3) {
      if (var2 != var1 && var1 != null && var2 != null) {
         boolean var4 = var1 != this.getParent(var2);
         this.execute(new mxChildChange(this, var1, var2, var3));
         if (this.maintainEdgeParent && var4) {
            this.updateEdgeParents(var2);
         }
      }

      return var2;
   }

   protected void cellAdded(Object var1) {
      if (var1 instanceof mxICell) {
         mxICell var2 = (mxICell)var1;
         if (var2.getId() == null && this.isCreateIds()) {
            var2.setId(this.createId(var1));
         }

         if (var2.getId() != null) {
            Object var3 = this.getCell(var2.getId());
            if (var3 != var1) {
               while(var3 != null) {
                  var2.setId(this.createId(var1));
                  var3 = this.getCell(var2.getId());
               }

               if (this.cells == null) {
                  this.cells = new Hashtable();
               }

               this.cells.put(var2.getId(), var1);
            }
         }

         int var6;
         try {
            var6 = Integer.parseInt(var2.getId());
            this.nextId = Math.max(this.nextId, var6 + 1);
         } catch (NumberFormatException var5) {
         }

         var6 = var2.getChildCount();

         for(int var4 = 0; var4 < var6; ++var4) {
            this.cellAdded(var2.getChildAt(var4));
         }
      }

   }

   public String createId(Object var1) {
      String var2 = String.valueOf(this.nextId);
      ++this.nextId;
      return var2;
   }

   public Object remove(Object var1) {
      if (var1 == this.root) {
         this.setRoot((Object)null);
      } else if (this.getParent(var1) != null) {
         this.execute(new mxChildChange(this, (Object)null, var1));
      }

      return var1;
   }

   protected void cellRemoved(Object var1) {
      if (var1 instanceof mxICell) {
         mxICell var2 = (mxICell)var1;
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            this.cellRemoved(var2.getChildAt(var4));
         }

         if (this.cells != null && var2.getId() != null) {
            this.cells.remove(var2.getId());
         }
      }

   }

   protected Object parentForCellChanged(Object var1, Object var2, int var3) {
      mxICell var4 = (mxICell)var1;
      mxICell var5 = (mxICell)this.getParent(var1);
      if (var2 != null) {
         if (var2 != var5 || var5.getIndex(var4) != var3) {
            ((mxICell)var2).insert(var4, var3);
         }
      } else if (var5 != null) {
         int var6 = var5.getIndex(var4);
         var5.remove(var6);
      }

      if (!this.contains(var5) && var2 != null) {
         this.cellAdded(var1);
      } else if (var2 == null) {
         this.cellRemoved(var1);
      }

      return var5;
   }

   public int getChildCount(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getChildCount() : 0;
   }

   public Object getChildAt(Object var1, int var2) {
      return var1 instanceof mxICell ? ((mxICell)var1).getChildAt(var2) : null;
   }

   public Object getTerminal(Object var1, boolean var2) {
      return var1 instanceof mxICell ? ((mxICell)var1).getTerminal(var2) : null;
   }

   public Object setTerminal(Object var1, Object var2, boolean var3) {
      boolean var4 = var2 != this.getTerminal(var1, var3);
      this.execute(new mxTerminalChange(this, var1, var2, var3));
      if (this.maintainEdgeParent && var4) {
         this.updateEdgeParent(var1, this.getRoot());
      }

      return var2;
   }

   protected Object terminalForCellChanged(Object var1, Object var2, boolean var3) {
      mxICell var4 = (mxICell)this.getTerminal(var1, var3);
      if (var2 != null) {
         ((mxICell)var2).insertEdge((mxICell)var1, var3);
      } else if (var4 != null) {
         var4.removeEdge((mxICell)var1, var3);
      }

      return var4;
   }

   public void updateEdgeParents(Object var1) {
      this.updateEdgeParents(var1, this.getRoot());
   }

   public void updateEdgeParents(Object var1, Object var2) {
      int var3 = this.getChildCount(var1);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Object var5 = this.getChildAt(var1, var4);
         this.updateEdgeParents(var5, var2);
      }

      var4 = this.getEdgeCount(var1);
      ArrayList var8 = new ArrayList(var4);

      for(int var6 = 0; var6 < var4; ++var6) {
         var8.add(this.getEdgeAt(var1, var6));
      }

      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         Object var7 = var9.next();
         if (this.isAncestor(var2, var7)) {
            this.updateEdgeParent(var7, var2);
         }
      }

   }

   public void updateEdgeParent(Object var1, Object var2) {
      Object var3 = this.getTerminal(var1, true);
      Object var4 = this.getTerminal(var1, false);
      Object var5 = null;
      if (this.isAncestor(var2, var3) && this.isAncestor(var2, var4)) {
         if (var3 == var4) {
            var5 = this.getParent(var3);
         } else {
            var5 = this.getNearestCommonAncestor(var3, var4);
         }

         if (var5 != null && (this.getParent(var5) != var2 || this.isAncestor(var5, var1)) && this.getParent(var1) != var5) {
            mxGeometry var6 = this.getGeometry(var1);
            if (var6 != null) {
               mxPoint var7 = this.getOrigin(this.getParent(var1));
               mxPoint var8 = this.getOrigin(var5);
               double var9 = var8.getX() - var7.getX();
               double var11 = var8.getY() - var7.getY();
               var6 = (mxGeometry)var6.clone();
               var6.translate(-var9, -var11);
               this.setGeometry(var1, var6);
            }

            this.add(var5, var1, this.getChildCount(var5));
         }
      }

   }

   public mxPoint getOrigin(Object var1) {
      mxPoint var2 = null;
      if (var1 != null) {
         var2 = this.getOrigin(this.getParent(var1));
         if (!this.isEdge(var1)) {
            mxGeometry var3 = this.getGeometry(var1);
            if (var3 != null) {
               var2.setX(var2.getX() + var3.getX());
               var2.setY(var2.getY() + var3.getY());
            }
         }
      } else {
         var2 = new mxPoint();
      }

      return var2;
   }

   public Object getNearestCommonAncestor(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         String var3 = mxCellPath.create((mxICell)var2);
         if (var3 != null && var3.length() > 0) {
            Object var4 = var1;

            Object var6;
            for(String var5 = mxCellPath.create((mxICell)var1); var4 != null; var4 = var6) {
               var6 = this.getParent(var4);
               if (var3.indexOf(var5 + mxCellPath.PATH_SEPARATOR) == 0 && var6 != null) {
                  return var4;
               }

               var5 = mxCellPath.getParentPath(var5);
            }
         }
      }

      return null;
   }

   public int getEdgeCount(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getEdgeCount() : 0;
   }

   public Object getEdgeAt(Object var1, int var2) {
      return var1 instanceof mxICell ? ((mxICell)var1).getEdgeAt(var2) : null;
   }

   public boolean isVertex(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).isVertex() : false;
   }

   public boolean isEdge(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).isEdge() : false;
   }

   public boolean isConnectable(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).isConnectable() : true;
   }

   public Object getValue(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getValue() : null;
   }

   public Object setValue(Object var1, Object var2) {
      this.execute(new mxValueChange(this, var1, var2));
      return var2;
   }

   protected Object valueForCellChanged(Object var1, Object var2) {
      Object var3 = ((mxICell)var1).getValue();
      ((mxICell)var1).setValue(var2);
      return var3;
   }

   public mxGeometry getGeometry(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getGeometry() : null;
   }

   public mxGeometry setGeometry(Object var1, mxGeometry var2) {
      if (var2 != this.getGeometry(var1)) {
         this.execute(new mxGeometryChange(this, var1, var2));
      }

      return var2;
   }

   protected mxGeometry geometryForCellChanged(Object var1, mxGeometry var2) {
      mxGeometry var3 = this.getGeometry(var1);
      ((mxICell)var1).setGeometry(var2);
      return var3;
   }

   public String getStyle(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).getStyle() : null;
   }

   public String setStyle(Object var1, String var2) {
      if (var2 != this.getStyle(var1)) {
         this.execute(new mxStyleChange(this, var1, var2));
      }

      return var2;
   }

   protected String styleForCellChanged(Object var1, String var2) {
      String var3 = this.getStyle(var1);
      ((mxICell)var1).setStyle(var2);
      return var3;
   }

   public boolean isCollapsed(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).isCollapsed() : false;
   }

   public boolean setCollapsed(Object var1, boolean var2) {
      if (var2 != this.isCollapsed(var1)) {
         this.execute(new mxCollapseChange(this, var1, var2));
      }

      return var2;
   }

   protected boolean collapsedStateForCellChanged(Object var1, boolean var2) {
      boolean var3 = this.isCollapsed(var1);
      ((mxICell)var1).setCollapsed(var2);
      return var3;
   }

   public boolean isVisible(Object var1) {
      return var1 instanceof mxICell ? ((mxICell)var1).isVisible() : false;
   }

   public boolean setVisible(Object var1, boolean var2) {
      if (var2 != this.isVisible(var1)) {
         this.execute(new mxVisibleChange(this, var1, var2));
      }

      return var2;
   }

   protected boolean visibleStateForCellChanged(Object var1, boolean var2) {
      boolean var3 = this.isVisible(var1);
      ((mxICell)var1).setVisible(var2);
      return var3;
   }

   public void execute(mxIGraphModel.mxAtomicGraphModelChange var1) {
      var1.execute();
      this.beginUpdate();
      this.currentEdit.add(var1);
      this.fireEvent(new mxEventObject("execute", new Object[]{"change", var1}));
      this.endUpdate();
   }

   public void beginUpdate() {
      ++this.updateLevel;
      this.fireEvent(new mxEventObject("beginUpdate"));
   }

   public void endUpdate() {
      --this.updateLevel;
      if (!this.endingUpdate) {
         this.endingUpdate = this.updateLevel == 0;
         this.fireEvent(new mxEventObject("endUpdate", new Object[]{"edit", this.currentEdit}));

         try {
            if (this.endingUpdate && !this.currentEdit.isEmpty()) {
               this.fireEvent(new mxEventObject("beforeUndo", new Object[]{"edit", this.currentEdit}));
               mxUndoableEdit var1 = this.currentEdit;
               this.currentEdit = this.createUndoableEdit();
               var1.dispatch();
               this.fireEvent(new mxEventObject("undo", new Object[]{"edit", var1}));
            }
         } finally {
            this.endingUpdate = false;
         }
      }

   }

   public void mergeChildren(mxICell var1, mxICell var2, boolean var3) throws CloneNotSupportedException {
      this.beginUpdate();

      try {
         Hashtable var4 = new Hashtable();
         this.mergeChildrenImpl(var1, var2, var3, var4);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            Object var7 = var4.get(var6);
            Object var8 = this.getTerminal(var6, true);
            if (var8 != null) {
               var8 = var4.get(var8);
               this.setTerminal(var7, var8, true);
            }

            var8 = this.getTerminal(var6, false);
            if (var8 != null) {
               var8 = var4.get(var8);
               this.setTerminal(var7, var8, false);
            }
         }
      } finally {
         this.endUpdate();
      }

   }

   protected void mergeChildrenImpl(mxICell var1, mxICell var2, boolean var3, Hashtable var4) throws CloneNotSupportedException {
      this.beginUpdate();

      try {
         int var5 = var1.getChildCount();

         for(int var6 = 0; var6 < var5; ++var6) {
            mxICell var7 = var1.getChildAt(var6);
            if (var7 instanceof mxICell) {
               mxICell var8 = (mxICell)var7;
               String var9 = var8.getId();
               mxICell var10 = (mxICell)(var9 == null || this.isEdge(var8) && var3 ? null : this.getCell(var9));
               if (var10 == null) {
                  mxCell var11 = (mxCell)var8.clone();
                  var11.setId(var9);
                  var10 = var2.insert(var11);
                  this.cellAdded(var10);
               }

               var4.put(var8, var10);
               this.mergeChildrenImpl(var8, var10, var3, var4);
            }
         }
      } finally {
         this.endUpdate();
      }

   }

   public static int getDirectedEdgeCount(mxIGraphModel var0, Object var1, boolean var2) {
      return getDirectedEdgeCount(var0, var1, var2, (Object)null);
   }

   public static int getDirectedEdgeCount(mxIGraphModel var0, Object var1, boolean var2, Object var3) {
      int var4 = 0;
      int var5 = var0.getEdgeCount(var1);

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var0.getEdgeAt(var1, var6);
         if (var7 != var3 && var0.getTerminal(var7, var2) == var1) {
            ++var4;
         }
      }

      return var4;
   }

   public static Object[] getEdges(mxIGraphModel var0, Object var1) {
      return getEdges(var0, var1, true, true, true);
   }

   public static Object[] getConnections(mxIGraphModel var0, Object var1) {
      return getEdges(var0, var1, true, true, false);
   }

   public static Object[] getIncomingEdges(mxIGraphModel var0, Object var1) {
      return getEdges(var0, var1, true, false, false);
   }

   public static Object[] getOutgoingEdges(mxIGraphModel var0, Object var1) {
      return getEdges(var0, var1, false, true, false);
   }

   public static Object[] getEdges(mxIGraphModel var0, Object var1, boolean var2, boolean var3, boolean var4) {
      int var5 = var0.getEdgeCount(var1);
      ArrayList var6 = new ArrayList(var5);

      for(int var7 = 0; var7 < var5; ++var7) {
         Object var8 = var0.getEdgeAt(var1, var7);
         Object var9 = var0.getTerminal(var8, true);
         Object var10 = var0.getTerminal(var8, false);
         if (var4 || var9 != var10 && (var2 && var10 == var1 || var3 && var9 == var1)) {
            var6.add(var8);
         }
      }

      return var6.toArray();
   }

   public static Object[] getEdgesBetween(mxIGraphModel var0, Object var1, Object var2) {
      return getEdgesBetween(var0, var1, var2, false);
   }

   public static Object[] getEdgesBetween(mxIGraphModel var0, Object var1, Object var2, boolean var3) {
      int var4 = var0.getEdgeCount(var1);
      int var5 = var0.getEdgeCount(var2);
      Object var6 = var1;
      int var7 = var4;
      if (var5 < var4) {
         var7 = var5;
         var6 = var2;
      }

      ArrayList var8 = new ArrayList(var7);

      for(int var9 = 0; var9 < var7; ++var9) {
         Object var10 = var0.getEdgeAt(var6, var9);
         Object var11 = var0.getTerminal(var10, true);
         Object var12 = var0.getTerminal(var10, false);
         boolean var13 = var11 == var1;
         if (var13 && var12 == var2 || !var3 && var0.getTerminal(var10, !var13) == var2) {
            var8.add(var10);
         }
      }

      return var8.toArray();
   }

   public static Object[] getOpposites(mxIGraphModel var0, Object[] var1, Object var2) {
      return getOpposites(var0, var1, var2, true, true);
   }

   public static Object[] getOpposites(mxIGraphModel var0, Object[] var1, Object var2, boolean var3, boolean var4) {
      ArrayList var5 = new ArrayList();
      if (var1 != null) {
         for(int var6 = 0; var6 < var1.length; ++var6) {
            Object var7 = var0.getTerminal(var1[var6], true);
            Object var8 = var0.getTerminal(var1[var6], false);
            if (var4 && var7 == var2 && var8 != null && var8 != var2) {
               var5.add(var8);
            } else if (var3 && var8 == var2 && var7 != null && var7 != var2) {
               var5.add(var7);
            }
         }
      }

      return var5.toArray();
   }

   public static void setTerminals(mxIGraphModel var0, Object var1, Object var2, Object var3) {
      var0.beginUpdate();

      try {
         var0.setTerminal(var1, var2, true);
         var0.setTerminal(var1, var3, false);
      } finally {
         var0.endUpdate();
      }

   }

   public static Object[] getChildren(mxIGraphModel var0, Object var1) {
      return getChildCells(var0, var1, false, false);
   }

   public static Object[] getChildVertices(mxIGraphModel var0, Object var1) {
      return getChildCells(var0, var1, true, false);
   }

   public static Object[] getChildEdges(mxIGraphModel var0, Object var1) {
      return getChildCells(var0, var1, false, true);
   }

   public static Object[] getChildCells(mxIGraphModel var0, Object var1, boolean var2, boolean var3) {
      int var4 = var0.getChildCount(var1);
      ArrayList var5 = new ArrayList(var4);

      for(int var6 = 0; var6 < var4; ++var6) {
         Object var7 = var0.getChildAt(var1, var6);
         if (!var3 && !var2 || var3 && var0.isEdge(var7) || var2 && var0.isVertex(var7)) {
            var5.add(var7);
         }
      }

      return var5.toArray();
   }

   public static Object[] getParents(mxIGraphModel var0, Object[] var1) {
      HashSet var2 = new HashSet();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            Object var4 = var0.getParent(var1[var3]);
            if (var4 != null) {
               var2.add(var4);
            }
         }
      }

      return var2.toArray();
   }

   public static Object[] filterCells(Object[] var0, Filter var1) {
      ArrayList var2 = null;
      if (var0 != null) {
         var2 = new ArrayList(var0.length);

         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (var1.filter(var0[var3])) {
               var2.add(var0[var3]);
            }
         }
      }

      return var2 != null ? var2.toArray() : null;
   }

   public static Collection getDescendants(mxIGraphModel var0, Object var1) {
      return filterDescendants(var0, (Filter)null, var1);
   }

   public static Collection filterDescendants(mxIGraphModel var0, Filter var1) {
      return filterDescendants(var0, var1, var0.getRoot());
   }

   public static Collection filterDescendants(mxIGraphModel var0, Filter var1, Object var2) {
      ArrayList var3 = new ArrayList();
      if (var1 == null || var1.filter(var2)) {
         var3.add(var2);
      }

      int var4 = var0.getChildCount(var2);

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var0.getChildAt(var2, var5);
         var3.addAll(filterDescendants(var0, var1, var6));
      }

      return var3;
   }

   public static Object[] getTopmostCells(mxIGraphModel var0, Object[] var1) {
      HashSet var2 = new HashSet();
      var2.addAll(Arrays.asList(var1));
      ArrayList var3 = new ArrayList(var1.length);

      for(int var4 = 0; var4 < var1.length; ++var4) {
         Object var5 = var1[var4];
         boolean var6 = true;

         for(Object var7 = var0.getParent(var5); var7 != null; var7 = var0.getParent(var7)) {
            if (var2.contains(var7)) {
               var6 = false;
               break;
            }
         }

         if (var6) {
            var3.add(var5);
         }
      }

      return var3.toArray();
   }

   public static class mxVisibleChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected boolean visible;
      protected boolean previous;

      public mxVisibleChange(mxGraphModel var1, Object var2, boolean var3) {
         super(var1);
         this.cell = var2;
         this.visible = var3;
         this.previous = this.visible;
      }

      public Object getCell() {
         return this.cell;
      }

      public boolean isVisible() {
         return this.visible;
      }

      public boolean getPrevious() {
         return this.visible;
      }

      public void execute() {
         this.visible = this.previous;
         this.previous = ((mxGraphModel)this.model).visibleStateForCellChanged(this.cell, this.previous);
      }
   }

   public static class mxCollapseChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected boolean collapsed;
      protected boolean previous;

      public mxCollapseChange(mxGraphModel var1, Object var2, boolean var3) {
         super(var1);
         this.cell = var2;
         this.collapsed = var3;
         this.previous = this.collapsed;
      }

      public Object getCell() {
         return this.cell;
      }

      public boolean isCollapsed() {
         return this.collapsed;
      }

      public boolean getPrevious() {
         return this.previous;
      }

      public void execute() {
         this.collapsed = this.previous;
         this.previous = ((mxGraphModel)this.model).collapsedStateForCellChanged(this.cell, this.previous);
      }
   }

   public static class mxGeometryChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected mxGeometry geometry;
      protected mxGeometry previous;

      public mxGeometryChange(mxGraphModel var1, Object var2, mxGeometry var3) {
         super(var1);
         this.cell = var2;
         this.geometry = var3;
         this.previous = this.geometry;
      }

      public Object getCell() {
         return this.cell;
      }

      public mxGeometry getGeometry() {
         return this.geometry;
      }

      public mxGeometry getPrevious() {
         return this.previous;
      }

      public void execute() {
         this.geometry = this.previous;
         this.previous = ((mxGraphModel)this.model).geometryForCellChanged(this.cell, this.previous);
      }
   }

   public static class mxStyleChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected String style;
      protected String previous;

      public mxStyleChange(mxGraphModel var1, Object var2, String var3) {
         super(var1);
         this.cell = var2;
         this.style = var3;
         this.previous = this.style;
      }

      public Object getCell() {
         return this.cell;
      }

      public String getStyle() {
         return this.style;
      }

      public String getPrevious() {
         return this.previous;
      }

      public void execute() {
         this.style = this.previous;
         this.previous = ((mxGraphModel)this.model).styleForCellChanged(this.cell, this.previous);
      }
   }

   public static class mxValueChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected Object value;
      protected Object previous;

      public mxValueChange(mxGraphModel var1, Object var2, Object var3) {
         super(var1);
         this.cell = var2;
         this.value = var3;
         this.previous = this.value;
      }

      public Object getCell() {
         return this.cell;
      }

      public Object getValue() {
         return this.value;
      }

      public Object getPrevious() {
         return this.previous;
      }

      public void execute() {
         this.value = this.previous;
         this.previous = ((mxGraphModel)this.model).valueForCellChanged(this.cell, this.previous);
      }
   }

   public static class mxTerminalChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object cell;
      protected Object terminal;
      protected Object previous;
      protected boolean isSource;

      public mxTerminalChange(mxGraphModel var1, Object var2, Object var3, boolean var4) {
         super(var1);
         this.cell = var2;
         this.terminal = var3;
         this.previous = this.terminal;
         this.isSource = var4;
      }

      public Object getCell() {
         return this.cell;
      }

      public Object getTerminal() {
         return this.terminal;
      }

      public Object getPrevious() {
         return this.previous;
      }

      public boolean isSource() {
         return this.isSource;
      }

      public void execute() {
         this.terminal = this.previous;
         this.previous = ((mxGraphModel)this.model).terminalForCellChanged(this.cell, this.previous, this.isSource);
      }
   }

   public static class mxChildChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object parent;
      protected Object previous;
      protected Object child;
      protected int index;
      protected int previousIndex;

      public mxChildChange(mxGraphModel var1, Object var2, Object var3) {
         this(var1, var2, var3, 0);
      }

      public mxChildChange(mxGraphModel var1, Object var2, Object var3, int var4) {
         super(var1);
         this.parent = var2;
         this.previous = this.parent;
         this.child = var3;
         this.index = var4;
         this.previousIndex = var4;
      }

      public Object getParent() {
         return this.parent;
      }

      public Object getPrevious() {
         return this.previous;
      }

      public Object getChild() {
         return this.child;
      }

      public int getIndex() {
         return this.index;
      }

      public int getPreviousIndex() {
         return this.previousIndex;
      }

      protected Object getTerminal(Object var1, boolean var2) {
         return this.model.getTerminal(var1, var2);
      }

      protected void setTerminal(Object var1, Object var2, boolean var3) {
         ((mxICell)var1).setTerminal((mxICell)var2, var3);
      }

      protected void connect(Object var1, boolean var2) {
         Object var3 = this.getTerminal(var1, true);
         Object var4 = this.getTerminal(var1, false);
         if (var3 != null) {
            if (var2) {
               ((mxGraphModel)this.model).terminalForCellChanged(var1, var3, true);
            } else {
               ((mxGraphModel)this.model).terminalForCellChanged(var1, (Object)null, true);
            }
         }

         if (var4 != null) {
            if (var2) {
               ((mxGraphModel)this.model).terminalForCellChanged(var1, var4, false);
            } else {
               ((mxGraphModel)this.model).terminalForCellChanged(var1, (Object)null, false);
            }
         }

         this.setTerminal(var1, var3, true);
         this.setTerminal(var1, var4, false);
         int var5 = this.model.getChildCount(var1);

         for(int var6 = 0; var6 < var5; ++var6) {
            this.connect((mxICell)this.model.getChildAt(var1, var6), var2);
         }

      }

      protected int getChildIndex(Object var1, Object var2) {
         return var1 instanceof mxICell && var2 instanceof mxICell ? ((mxICell)var1).getIndex((mxICell)var2) : 0;
      }

      public void execute() {
         Object var1 = this.model.getParent(this.child);
         int var2 = this.getChildIndex(var1, this.child);
         if (this.previous == null) {
            this.connect(this.child, false);
         }

         var1 = ((mxGraphModel)this.model).parentForCellChanged(this.child, this.previous, this.previousIndex);
         if (this.previous != null) {
            this.connect(this.child, true);
         }

         this.parent = this.previous;
         this.previous = var1;
         this.index = this.previousIndex;
         this.previousIndex = var2;
      }
   }

   public static class mxRootChange extends mxIGraphModel.mxAtomicGraphModelChange {
      protected Object root;
      protected Object previous;

      public mxRootChange(mxGraphModel var1, Object var2) {
         super(var1);
         this.root = var2;
         this.previous = var2;
      }

      public Object getRoot() {
         return this.root;
      }

      public Object getPrevious() {
         return this.previous;
      }

      public void execute() {
         this.root = this.previous;
         this.previous = ((mxGraphModel)this.model).rootChanged(this.previous);
      }
   }

   public interface Filter {
      boolean filter(Object var1);
   }
}
