package org.apache.batik.apps.svgbrowser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Node;

public class DOMDocumentTree extends JTree implements Autoscroll {
   protected EventListenerList eventListeners = new EventListenerList();
   protected Insets autoscrollInsets = new Insets(20, 20, 20, 20);
   protected Insets scrollUnits = new Insets(25, 25, 25, 25);
   protected DOMDocumentTreeController controller;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$TransferData;
   // $FF: synthetic field
   static Class class$javax$swing$JViewport;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener;

   public DOMDocumentTree(TreeNode var1, DOMDocumentTreeController var2) {
      super(var1);
      this.controller = var2;
      new TreeDragSource(this, 3);
      new DropTarget(this, new TreeDropTargetListener(this));
   }

   public void autoscroll(Point var1) {
      JViewport var2 = (JViewport)SwingUtilities.getAncestorOfClass(class$javax$swing$JViewport == null ? (class$javax$swing$JViewport = class$("javax.swing.JViewport")) : class$javax$swing$JViewport, this);
      if (var2 != null) {
         Point var3 = var2.getViewPosition();
         int var4 = var2.getExtentSize().height;
         int var5 = var2.getExtentSize().width;
         if (var1.y - var3.y < this.autoscrollInsets.top) {
            var2.setViewPosition(new Point(var3.x, Math.max(var3.y - this.scrollUnits.top, 0)));
            this.fireOnAutoscroll(new DOMDocumentTreeEvent(this));
         } else if (var3.y + var4 - var1.y < this.autoscrollInsets.bottom) {
            var2.setViewPosition(new Point(var3.x, Math.min(var3.y + this.scrollUnits.bottom, this.getHeight() - var4)));
            this.fireOnAutoscroll(new DOMDocumentTreeEvent(this));
         } else if (var1.x - var3.x < this.autoscrollInsets.left) {
            var2.setViewPosition(new Point(Math.max(var3.x - this.scrollUnits.left, 0), var3.y));
            this.fireOnAutoscroll(new DOMDocumentTreeEvent(this));
         } else if (var3.x + var5 - var1.x < this.autoscrollInsets.right) {
            var2.setViewPosition(new Point(Math.min(var3.x + this.scrollUnits.right, this.getWidth() - var5), var3.y));
            this.fireOnAutoscroll(new DOMDocumentTreeEvent(this));
         }

      }
   }

   public Insets getAutoscrollInsets() {
      int var1 = this.getHeight();
      int var2 = this.getWidth();
      return new Insets(var1, var2, var1, var2);
   }

   public void addListener(DOMDocumentTreeListener var1) {
      this.eventListeners.add(class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener == null ? (class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener = class$("org.apache.batik.apps.svgbrowser.DOMDocumentTree$DOMDocumentTreeListener")) : class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener, var1);
   }

   public void fireDropCompleted(DOMDocumentTreeEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener == null ? (class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener = class$("org.apache.batik.apps.svgbrowser.DOMDocumentTree$DOMDocumentTreeListener")) : class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener)) {
            ((DOMDocumentTreeListener)var2[var4 + 1]).dropCompleted(var1);
         }
      }

   }

   public void fireOnAutoscroll(DOMDocumentTreeEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener == null ? (class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener = class$("org.apache.batik.apps.svgbrowser.DOMDocumentTree$DOMDocumentTreeListener")) : class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$DOMDocumentTreeListener)) {
            ((DOMDocumentTreeListener)var2[var4 + 1]).onAutoscroll(var1);
         }
      }

   }

   protected Node getDomNodeFromTreeNode(DefaultMutableTreeNode var1) {
      if (var1 == null) {
         return null;
      } else {
         return var1.getUserObject() instanceof DOMViewer.NodeInfo ? ((DOMViewer.NodeInfo)var1.getUserObject()).getNode() : null;
      }
   }

   protected ArrayList getNodeListForParent(ArrayList var1, Node var2) {
      ArrayList var3 = new ArrayList();
      int var4 = var1.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         Node var6 = (Node)var1.get(var5);
         if (DOMUtilities.canAppend(var6, var2)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class DropCompletedInfo {
      protected Node parent;
      protected ArrayList children;
      protected Node sibling;

      public DropCompletedInfo(Node var1, Node var2, ArrayList var3) {
         this.parent = var1;
         this.sibling = var2;
         this.children = var3;
      }

      public ArrayList getChildren() {
         return this.children;
      }

      public Node getParent() {
         return this.parent;
      }

      public Node getSibling() {
         return this.sibling;
      }
   }

   public static class DOMDocumentTreeAdapter implements DOMDocumentTreeListener {
      public void dropCompleted(DOMDocumentTreeEvent var1) {
      }

      public void onAutoscroll(DOMDocumentTreeEvent var1) {
      }
   }

   public interface DOMDocumentTreeListener extends EventListener {
      void dropCompleted(DOMDocumentTreeEvent var1);

      void onAutoscroll(DOMDocumentTreeEvent var1);
   }

   public static class DOMDocumentTreeEvent extends EventObject {
      public DOMDocumentTreeEvent(Object var1) {
         super(var1);
      }
   }

   public static class TransferData {
      protected ArrayList nodeList;

      public TransferData(ArrayList var1) {
         this.nodeList = var1;
      }

      public ArrayList getNodeList() {
         return this.nodeList;
      }

      public String getNodesAsXML() {
         String var1 = "";

         Node var3;
         for(Iterator var2 = this.nodeList.iterator(); var2.hasNext(); var1 = var1 + DOMUtilities.getXML(var3)) {
            var3 = (Node)var2.next();
         }

         return var1;
      }
   }

   public static class TransferableTreeNode implements Transferable {
      protected static final DataFlavor NODE_FLAVOR;
      protected static final DataFlavor[] FLAVORS;
      protected TransferData data;

      public TransferableTreeNode(TransferData var1) {
         this.data = var1;
      }

      public synchronized DataFlavor[] getTransferDataFlavors() {
         return FLAVORS;
      }

      public boolean isDataFlavorSupported(DataFlavor var1) {
         for(int var2 = 0; var2 < FLAVORS.length; ++var2) {
            if (var1.equals(FLAVORS[var2])) {
               return true;
            }
         }

         return false;
      }

      public synchronized Object getTransferData(DataFlavor var1) {
         if (!this.isDataFlavorSupported(var1)) {
            return null;
         } else if (var1.equals(NODE_FLAVOR)) {
            return this.data;
         } else {
            return var1.equals(DataFlavor.stringFlavor) ? this.data.getNodesAsXML() : null;
         }
      }

      static {
         NODE_FLAVOR = new DataFlavor(DOMDocumentTree.class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$TransferData == null ? (DOMDocumentTree.class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$TransferData = DOMDocumentTree.class$("org.apache.batik.apps.svgbrowser.DOMDocumentTree$TransferData")) : DOMDocumentTree.class$org$apache$batik$apps$svgbrowser$DOMDocumentTree$TransferData, "TransferData");
         FLAVORS = new DataFlavor[]{NODE_FLAVOR, DataFlavor.stringFlavor};
      }
   }

   public class TreeDropTargetListener implements DropTargetListener {
      private static final int BEFORE = 1;
      private static final int AFTER = 2;
      private static final int CURRENT = 3;
      private TransferData transferData;
      private Component originalGlassPane;
      private int visualTipOffset = 5;
      private int visualTipThickness = 2;
      private int positionIndicator;
      private Point startPoint;
      private Point endPoint;
      protected JPanel visualTipGlassPane = new JPanel() {
         public void paint(Graphics var1) {
            var1.setColor(UIManager.getColor("Tree.selectionBackground"));
            if (TreeDropTargetListener.this.startPoint != null && TreeDropTargetListener.this.endPoint != null) {
               int var2 = TreeDropTargetListener.this.startPoint.x;
               int var3 = TreeDropTargetListener.this.endPoint.x;
               int var4 = TreeDropTargetListener.this.startPoint.y;
               int var5 = -TreeDropTargetListener.this.visualTipThickness / 2;
               var5 += TreeDropTargetListener.this.visualTipThickness % 2 == 0 ? 1 : 0;

               for(int var6 = var5; var6 <= TreeDropTargetListener.this.visualTipThickness / 2; ++var6) {
                  var1.drawLine(var2 + 2, var4 + var6, var3 - 2, var4 + var6);
               }

            }
         }
      };
      private Timer expandControlTimer;
      private int expandTimeout = 1500;
      private TreePath dragOverTreePath;
      private TreePath treePathToExpand;

      public TreeDropTargetListener(DOMDocumentTree var2) {
         this.addOnAutoscrollListener(var2);
      }

      public void dragEnter(DropTargetDragEvent var1) {
         JTree var2 = (JTree)var1.getDropTargetContext().getComponent();
         JRootPane var3 = var2.getRootPane();
         this.originalGlassPane = var3.getGlassPane();
         var3.setGlassPane(this.visualTipGlassPane);
         this.visualTipGlassPane.setOpaque(false);
         this.visualTipGlassPane.setVisible(true);
         this.updateVisualTipLine(var2, (TreePath)null);

         try {
            Transferable var4 = (new DropTargetDropEvent(var1.getDropTargetContext(), var1.getLocation(), 0, 0)).getTransferable();
            DataFlavor[] var5 = var4.getTransferDataFlavors();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var4.isDataFlavorSupported(var5[var6])) {
                  this.transferData = (TransferData)var4.getTransferData(var5[var6]);
                  return;
               }
            }
         } catch (UnsupportedFlavorException var7) {
            var7.printStackTrace();
         } catch (IOException var8) {
            var8.printStackTrace();
         }

      }

      public void dragOver(DropTargetDragEvent var1) {
         JTree var2 = (JTree)var1.getDropTargetContext().getComponent();
         TreeNode var3 = this.getNode(var1);
         if (var3 != null) {
            this.updatePositionIndicator(var1);
            Point var4 = var1.getLocation();
            TreePath var5 = var2.getPathForLocation(var4.x, var4.y);
            TreePath var6 = this.getParentPathForPosition(var5);
            TreeNode var7 = this.getNodeForPath(var6);
            TreePath var8 = this.getSiblingPathForPosition(var5);
            TreeNode var9 = this.getNodeForPath(var8);
            Node var10 = DOMDocumentTree.this.getDomNodeFromTreeNode((DefaultMutableTreeNode)var7);
            Node var11 = DOMDocumentTree.this.getDomNodeFromTreeNode((DefaultMutableTreeNode)var9);
            if (DOMUtilities.canAppendAny(this.transferData.getNodeList(), var10) && !this.transferData.getNodeList().contains(var11)) {
               var1.acceptDrag(var1.getDropAction());
               this.updateVisualTipLine(var2, var5);
               this.dragOverTreePath = var5;
               if (!var2.isExpanded(var5)) {
                  this.scheduleExpand(var5, var2);
               }
            } else {
               var1.rejectDrag();
            }
         } else {
            var1.rejectDrag();
         }

      }

      public void dropActionChanged(DropTargetDragEvent var1) {
      }

      public void drop(DropTargetDropEvent var1) {
         Point var2 = var1.getLocation();
         DropTargetContext var3 = var1.getDropTargetContext();
         JTree var4 = (JTree)var3.getComponent();
         this.setOriginalGlassPane(var4);
         this.dragOverTreePath = null;
         TreePath var5 = var4.getPathForLocation(var2.x, var2.y);
         DefaultMutableTreeNode var6 = (DefaultMutableTreeNode)this.getNodeForPath(this.getParentPathForPosition(var5));
         Node var7 = DOMDocumentTree.this.getDomNodeFromTreeNode(var6);
         DefaultMutableTreeNode var8 = (DefaultMutableTreeNode)this.getNodeForPath(this.getSiblingPathForPosition(var5));
         Node var9 = DOMDocumentTree.this.getDomNodeFromTreeNode(var8);
         if (this.transferData != null) {
            ArrayList var10 = DOMDocumentTree.this.getNodeListForParent(this.transferData.getNodeList(), var7);
            DOMDocumentTree.this.fireDropCompleted(new DOMDocumentTreeEvent(new DropCompletedInfo(var7, var9, var10)));
            var1.dropComplete(true);
         } else {
            var1.rejectDrop();
         }
      }

      public void dragExit(DropTargetEvent var1) {
         this.setOriginalGlassPane((JTree)var1.getDropTargetContext().getComponent());
         this.dragOverTreePath = null;
      }

      private void updatePositionIndicator(DropTargetDragEvent var1) {
         Point var2 = var1.getLocation();
         DropTargetContext var3 = var1.getDropTargetContext();
         JTree var4 = (JTree)var3.getComponent();
         TreePath var5 = var4.getPathForLocation(var2.x, var2.y);
         Rectangle var6 = var4.getPathBounds(var5);
         if (var2.y <= var6.y + this.visualTipOffset) {
            this.positionIndicator = 1;
         } else if (var2.y >= var6.y + var6.height - this.visualTipOffset) {
            this.positionIndicator = 2;
         } else {
            this.positionIndicator = 3;
         }

      }

      private TreePath getParentPathForPosition(TreePath var1) {
         if (var1 == null) {
            return null;
         } else {
            TreePath var2 = null;
            if (this.positionIndicator == 2) {
               var2 = var1.getParentPath();
            } else if (this.positionIndicator == 1) {
               var2 = var1.getParentPath();
            } else if (this.positionIndicator == 3) {
               var2 = var1;
            }

            return var2;
         }
      }

      private TreePath getSiblingPathForPosition(TreePath var1) {
         TreePath var2 = this.getParentPathForPosition(var1);
         TreePath var3 = null;
         if (this.positionIndicator == 2) {
            TreeNode var4 = this.getNodeForPath(var2);
            TreeNode var5 = this.getNodeForPath(var1);
            if (var2 != null && var4 != null && var5 != null) {
               int var6 = var4.getIndex(var5) + 1;
               if (var4.getChildCount() > var6) {
                  var3 = var2.pathByAddingChild(var4.getChildAt(var6));
               }
            }
         } else if (this.positionIndicator == 1) {
            var3 = var1;
         } else if (this.positionIndicator == 3) {
            var3 = null;
         }

         return var3;
      }

      private TreeNode getNodeForPath(TreePath var1) {
         return var1 != null && var1.getLastPathComponent() != null ? (TreeNode)var1.getLastPathComponent() : null;
      }

      private TreeNode getNode(DropTargetDragEvent var1) {
         Point var2 = var1.getLocation();
         DropTargetContext var3 = var1.getDropTargetContext();
         JTree var4 = (JTree)var3.getComponent();
         TreePath var5 = var4.getPathForLocation(var2.x, var2.y);
         return var5 != null && var5.getLastPathComponent() != null ? (TreeNode)var5.getLastPathComponent() : null;
      }

      private void updateVisualTipLine(JTree var1, TreePath var2) {
         if (var2 == null) {
            this.startPoint = null;
            this.endPoint = null;
         } else {
            Rectangle var3 = var1.getPathBounds(var2);
            if (this.positionIndicator == 1) {
               this.startPoint = var3.getLocation();
               this.endPoint = new Point(this.startPoint.x + var3.width, this.startPoint.y);
            } else if (this.positionIndicator == 2) {
               this.startPoint = new Point(var3.x, var3.y + var3.height);
               this.endPoint = new Point(this.startPoint.x + var3.width, this.startPoint.y);
               this.positionIndicator = 2;
            } else if (this.positionIndicator == 3) {
               this.startPoint = null;
               this.endPoint = null;
            }

            if (this.startPoint != null && this.endPoint != null) {
               this.startPoint = SwingUtilities.convertPoint(var1, this.startPoint, this.visualTipGlassPane);
               this.endPoint = SwingUtilities.convertPoint(var1, this.endPoint, this.visualTipGlassPane);
            }
         }

         this.visualTipGlassPane.getRootPane().repaint();
      }

      private void addOnAutoscrollListener(DOMDocumentTree var1) {
         var1.addListener(new DOMDocumentTreeAdapter() {
            public void onAutoscroll(DOMDocumentTreeEvent var1) {
               TreeDropTargetListener.this.startPoint = null;
               TreeDropTargetListener.this.endPoint = null;
            }
         });
      }

      private void setOriginalGlassPane(JTree var1) {
         JRootPane var2 = var1.getRootPane();
         var2.setGlassPane(this.originalGlassPane);
         this.originalGlassPane.setVisible(false);
         var2.repaint();
      }

      private void scheduleExpand(TreePath var1, JTree var2) {
         if (var1 != this.treePathToExpand) {
            this.getExpandTreeTimer(var2).stop();
            this.treePathToExpand = var1;
            this.getExpandTreeTimer(var2).start();
         }

      }

      private Timer getExpandTreeTimer(final JTree var1) {
         if (this.expandControlTimer == null) {
            this.expandControlTimer = new Timer(this.expandTimeout, new ActionListener() {
               public void actionPerformed(ActionEvent var1x) {
                  if (TreeDropTargetListener.this.treePathToExpand != null && TreeDropTargetListener.this.treePathToExpand == TreeDropTargetListener.this.dragOverTreePath) {
                     var1.expandPath(TreeDropTargetListener.this.treePathToExpand);
                  }

                  TreeDropTargetListener.this.getExpandTreeTimer(var1).stop();
               }
            });
         }

         return this.expandControlTimer;
      }
   }

   public class TreeDragSource implements DragSourceListener, DragGestureListener {
      protected DragSource source;
      protected DragGestureRecognizer recognizer;
      protected TransferableTreeNode transferable;
      protected DOMDocumentTree sourceTree;

      public TreeDragSource(DOMDocumentTree var2, int var3) {
         this.sourceTree = var2;
         this.source = new DragSource();
         this.recognizer = this.source.createDefaultDragGestureRecognizer(this.sourceTree, var3, this);
      }

      public void dragGestureRecognized(DragGestureEvent var1) {
         if (DOMDocumentTree.this.controller.isDNDSupported()) {
            TreePath[] var2 = this.sourceTree.getSelectionPaths();
            if (var2 != null) {
               ArrayList var3 = new ArrayList();

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  TreePath var5 = var2[var4];
                  if (var5.getPathCount() > 1) {
                     DefaultMutableTreeNode var6 = (DefaultMutableTreeNode)var5.getLastPathComponent();
                     Node var7 = DOMDocumentTree.this.getDomNodeFromTreeNode(var6);
                     if (var7 != null) {
                        var3.add(var7);
                     }
                  }
               }

               if (!var3.isEmpty()) {
                  this.transferable = new TransferableTreeNode(new TransferData(var3));
                  this.source.startDrag(var1, (Cursor)null, this.transferable, this);
               }
            }
         }
      }

      public void dragEnter(DragSourceDragEvent var1) {
      }

      public void dragExit(DragSourceEvent var1) {
      }

      public void dragOver(DragSourceDragEvent var1) {
      }

      public void dropActionChanged(DragSourceDragEvent var1) {
      }

      public void dragDropEnd(DragSourceDropEvent var1) {
      }
   }
}
