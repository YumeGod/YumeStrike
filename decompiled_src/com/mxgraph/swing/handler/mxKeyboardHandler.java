package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

public class mxKeyboardHandler {
   public mxKeyboardHandler(mxGraphComponent var1) {
      this.installKeyboardActions(var1);
   }

   protected void installKeyboardActions(mxGraphComponent var1) {
      InputMap var2 = this.getInputMap(1);
      SwingUtilities.replaceUIInputMap(var1, 1, var2);
      var2 = this.getInputMap(0);
      SwingUtilities.replaceUIInputMap(var1, 0, var2);
      SwingUtilities.replaceUIActionMap(var1, this.createActionMap());
   }

   protected InputMap getInputMap(int var1) {
      InputMap var2 = null;
      if (var1 == 1) {
         var2 = (InputMap)UIManager.get("ScrollPane.ancestorInputMap");
      } else if (var1 == 0) {
         var2 = new InputMap();
         var2.put(KeyStroke.getKeyStroke("F2"), "edit");
         var2.put(KeyStroke.getKeyStroke("DELETE"), "delete");
         var2.put(KeyStroke.getKeyStroke("UP"), "selectParent");
         var2.put(KeyStroke.getKeyStroke("DOWN"), "selectChild");
         var2.put(KeyStroke.getKeyStroke("RIGHT"), "selectNext");
         var2.put(KeyStroke.getKeyStroke("LEFT"), "selectPrevious");
         var2.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "enterGroup");
         var2.put(KeyStroke.getKeyStroke("PAGE_UP"), "exitGroup");
         var2.put(KeyStroke.getKeyStroke("HOME"), "home");
         var2.put(KeyStroke.getKeyStroke("ENTER"), "expand");
         var2.put(KeyStroke.getKeyStroke("BACK_SPACE"), "collapse");
         var2.put(KeyStroke.getKeyStroke("control A"), "selectAll");
         var2.put(KeyStroke.getKeyStroke("control D"), "selectNone");
         var2.put(KeyStroke.getKeyStroke("control X"), "cut");
         var2.put(KeyStroke.getKeyStroke("CUT"), "cut");
         var2.put(KeyStroke.getKeyStroke("control C"), "copy");
         var2.put(KeyStroke.getKeyStroke("COPY"), "copy");
         var2.put(KeyStroke.getKeyStroke("control V"), "paste");
         var2.put(KeyStroke.getKeyStroke("PASTE"), "paste");
         var2.put(KeyStroke.getKeyStroke("control G"), "group");
         var2.put(KeyStroke.getKeyStroke("control U"), "ungroup");
         var2.put(KeyStroke.getKeyStroke("control ADD"), "zoomIn");
         var2.put(KeyStroke.getKeyStroke("control SUBTRACT"), "zoomOut");
      }

      return var2;
   }

   protected ActionMap createActionMap() {
      ActionMap var1 = (ActionMap)UIManager.get("ScrollPane.actionMap");
      var1.put("edit", mxGraphActions.getEditAction());
      var1.put("delete", mxGraphActions.getDeleteAction());
      var1.put("home", mxGraphActions.getHomeAction());
      var1.put("enterGroup", mxGraphActions.getEnterGroupAction());
      var1.put("exitGroup", mxGraphActions.getExitGroupAction());
      var1.put("collapse", mxGraphActions.getCollapseAction());
      var1.put("expand", mxGraphActions.getExpandAction());
      var1.put("toBack", mxGraphActions.getToBackAction());
      var1.put("toFront", mxGraphActions.getToFrontAction());
      var1.put("selectNone", mxGraphActions.getSelectNoneAction());
      var1.put("selectAll", mxGraphActions.getSelectAllAction());
      var1.put("selectNext", mxGraphActions.getSelectNextAction());
      var1.put("selectPrevious", mxGraphActions.getSelectPreviousAction());
      var1.put("selectParent", mxGraphActions.getSelectParentAction());
      var1.put("selectChild", mxGraphActions.getSelectChildAction());
      var1.put("cut", TransferHandler.getCutAction());
      var1.put("copy", TransferHandler.getCopyAction());
      var1.put("paste", TransferHandler.getPasteAction());
      var1.put("group", mxGraphActions.getGroupAction());
      var1.put("ungroup", mxGraphActions.getUngroupAction());
      var1.put("zoomIn", mxGraphActions.getZoomInAction());
      var1.put("zoomOut", mxGraphActions.getZoomOutAction());
      return var1;
   }
}
