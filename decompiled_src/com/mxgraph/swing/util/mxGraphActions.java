package com.mxgraph.swing.util;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class mxGraphActions {
   static final Action deleteAction = new DeleteAction("delete");
   static final Action editAction = new EditAction("edit");
   static final Action groupAction = new GroupAction("group");
   static final Action ungroupAction = new UngroupAction("ungroup");
   static final Action removeFromParentAction = new RemoveFromParentAction("removeFromParent");
   static final Action updateGroupBoundsAction = new UpdateGroupBoundsAction("updateGroupBounds");
   static final Action selectAllAction = new SelectAction("selectAll");
   static final Action selectVerticesAction = new SelectAction("vertices");
   static final Action selectEdgesAction = new SelectAction("edges");
   static final Action selectNoneAction = new SelectAction("selectNone");
   static final Action selectNextAction = new SelectAction("selectNext");
   static final Action selectPreviousAction = new SelectAction("selectPrevious");
   static final Action selectParentAction = new SelectAction("selectParent");
   static final Action selectChildAction = new SelectAction("selectChild");
   static final Action collapseAction = new FoldAction("collapse");
   static final Action expandAction = new FoldAction("expand");
   static final Action enterGroupAction = new DrillAction("enterGroup");
   static final Action exitGroupAction = new DrillAction("exitGroup");
   static final Action homeAction = new DrillAction("home");
   static final Action zoomActualAction = new ZoomAction("actual");
   static final Action zoomInAction = new ZoomAction("zoomIn");
   static final Action zoomOutAction = new ZoomAction("zoomOut");
   static final Action toBackAction = new LayerAction("toBack");
   static final Action toFrontAction = new LayerAction("toFront");

   public static Action getDeleteAction() {
      return deleteAction;
   }

   public static Action getEditAction() {
      return editAction;
   }

   public static Action getGroupAction() {
      return groupAction;
   }

   public static Action getUngroupAction() {
      return ungroupAction;
   }

   public static Action getRemoveFromParentAction() {
      return removeFromParentAction;
   }

   public static Action getUpdateGroupBoundsAction() {
      return updateGroupBoundsAction;
   }

   public static Action getSelectAllAction() {
      return selectAllAction;
   }

   public static Action getSelectVerticesAction() {
      return selectVerticesAction;
   }

   public static Action getSelectEdgesAction() {
      return selectEdgesAction;
   }

   public static Action getSelectNoneAction() {
      return selectNoneAction;
   }

   public static Action getSelectNextAction() {
      return selectNextAction;
   }

   public static Action getSelectPreviousAction() {
      return selectPreviousAction;
   }

   public static Action getSelectParentAction() {
      return selectParentAction;
   }

   public static Action getSelectChildAction() {
      return selectChildAction;
   }

   public static Action getEnterGroupAction() {
      return enterGroupAction;
   }

   public static Action getExitGroupAction() {
      return exitGroupAction;
   }

   public static Action getHomeAction() {
      return homeAction;
   }

   public static Action getCollapseAction() {
      return collapseAction;
   }

   public static Action getExpandAction() {
      return expandAction;
   }

   public static Action getZoomActualAction() {
      return zoomActualAction;
   }

   public static Action getZoomInAction() {
      return zoomInAction;
   }

   public static Action getZoomOutAction() {
      return zoomOutAction;
   }

   public static Action getToBackAction() {
      return toBackAction;
   }

   public static Action getToFrontAction() {
      return toFrontAction;
   }

   public static final mxGraph getGraph(ActionEvent var0) {
      Object var1 = var0.getSource();
      return var1 instanceof mxGraphComponent ? ((mxGraphComponent)var1).getGraph() : null;
   }

   public static class SelectAction extends AbstractAction {
      private static final long serialVersionUID = 6501585024845668187L;

      public SelectAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            String var3 = this.getValue("Name").toString();
            if (var3.equalsIgnoreCase("selectAll")) {
               var2.selectAll();
            } else if (var3.equalsIgnoreCase("selectNone")) {
               var2.clearSelection();
            } else if (var3.equalsIgnoreCase("selectNext")) {
               var2.selectNextCell();
            } else if (var3.equalsIgnoreCase("selectPrevious")) {
               var2.selectPreviousCell();
            } else if (var3.equalsIgnoreCase("selectParent")) {
               var2.selectParentCell();
            } else if (var3.equalsIgnoreCase("vertices")) {
               var2.selectVertices();
            } else if (var3.equalsIgnoreCase("edges")) {
               var2.selectEdges();
            } else {
               var2.selectChildCell();
            }
         }

      }
   }

   public static class ZoomAction extends AbstractAction {
      private static final long serialVersionUID = -7500195051313272384L;

      public ZoomAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         Object var2 = var1.getSource();
         if (var2 instanceof mxGraphComponent) {
            String var3 = this.getValue("Name").toString();
            mxGraphComponent var4 = (mxGraphComponent)var2;
            if (var3.equalsIgnoreCase("zoomIn")) {
               var4.zoomIn();
            } else if (var3.equalsIgnoreCase("zoomOut")) {
               var4.zoomOut();
            } else {
               var4.zoomActual();
            }
         }

      }
   }

   public static class DrillAction extends AbstractAction {
      private static final long serialVersionUID = 5464382323663870291L;

      public DrillAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            String var3 = this.getValue("Name").toString();
            if (var3.equalsIgnoreCase("enterGroup")) {
               var2.enterGroup();
            } else if (var3.equalsIgnoreCase("exitGroup")) {
               var2.exitGroup();
            } else {
               var2.home();
            }
         }

      }
   }

   public static class FoldAction extends AbstractAction {
      private static final long serialVersionUID = 4078517503905239901L;

      public FoldAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            boolean var3 = this.getValue("Name").toString().equalsIgnoreCase("collapse");
            var2.foldCells(var3);
         }

      }
   }

   public static class LayerAction extends AbstractAction {
      private static final long serialVersionUID = 562519299806253741L;

      public LayerAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            boolean var3 = this.getValue("Name").toString().equalsIgnoreCase("toBack");
            var2.orderCells(var3);
         }

      }
   }

   public static class UpdateGroupBoundsAction extends AbstractAction {
      private static final long serialVersionUID = -4718086600089409092L;

      public UpdateGroupBoundsAction(String var1) {
         super(var1);
      }

      protected int getGroupBorder(mxGraph var1) {
         return 2 * var1.getGridSize();
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            var2.updateGroupBounds((Object[])null, this.getGroupBorder(var2));
         }

      }
   }

   public static class RemoveFromParentAction extends AbstractAction {
      private static final long serialVersionUID = 7169443038859140811L;

      public RemoveFromParentAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            var2.removeCellsFromParent();
         }

      }
   }

   public static class UngroupAction extends AbstractAction {
      private static final long serialVersionUID = 2247770767961318251L;

      public UngroupAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            var2.setSelectionCells(var2.ungroupCells());
         }

      }
   }

   public static class GroupAction extends AbstractAction {
      private static final long serialVersionUID = -4718086600089409092L;

      public GroupAction(String var1) {
         super(var1);
      }

      protected int getGroupBorder(mxGraph var1) {
         return 2 * var1.getGridSize();
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            var2.setSelectionCell(var2.groupCells((Object)null, (double)this.getGroupBorder(var2)));
         }

      }
   }

   public static class DeleteAction extends AbstractAction {
      private static final long serialVersionUID = -8212339796803275529L;

      public DeleteAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         mxGraph var2 = mxGraphActions.getGraph(var1);
         if (var2 != null) {
            var2.removeCells();
         }

      }
   }

   public static class EditAction extends AbstractAction {
      private static final long serialVersionUID = 4610112721356742702L;

      public EditAction(String var1) {
         super(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() instanceof mxGraphComponent) {
            ((mxGraphComponent)var1.getSource()).startEditing();
         }

      }
   }
}
