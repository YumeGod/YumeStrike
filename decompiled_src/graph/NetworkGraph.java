package graph;

import aggressor.Prefs;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import common.CommonUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

public class NetworkGraph extends JComponent implements ActionListener {
   protected mxGraph graph;
   protected mxGraphComponent component;
   protected Object parent;
   protected boolean isAlive = true;
   protected String layout = null;
   protected Map nodeImages = new HashMap();
   protected GraphPopup popup = null;
   protected double zoom = 1.0;
   protected TouchMap nodes = new TouchMap();
   protected LinkedList edges = new LinkedList();
   public static final int DIRECTION_NONE = 0;
   public static final int DIRECTION_INBOUND = 1;
   public static final int DIRECTION_OUTBOUND = 2;
   protected Map tooltips = new HashMap();

   public void actionPerformed(ActionEvent var1) {
      this.isAlive = false;
   }

   public boolean isAlive() {
      return this.isAlive;
   }

   public GraphPopup getGraphPopup() {
      return this.popup;
   }

   public void setGraphPopup(GraphPopup var1) {
      this.popup = var1;
   }

   public Image getScreenshot() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.nodes.values().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.addAll(Arrays.asList(this.graph.getEdges(var3)));
      }

      var1.addAll(this.nodes.values());
      return mxCellRenderer.createBufferedImage(this.graph, var1.toArray(), this.zoom, (Color)null, true, (mxRectangle)null, new NetworkGraphCanvas());
   }

   public void setTransferHandler(TransferHandler var1) {
      this.component.setTransferHandler(var1);
   }

   public void clearSelection() {
      this.graph.clearSelection();
   }

   public void selectAll() {
      this.graph.selectAll();
   }

   public NetworkGraph() {
      mxConstants.VERTEX_SELECTION_COLOR = Prefs.getPreferences().getColor("graph.selection.color", "#00ff00");
      mxConstants.EDGE_SELECTION_COLOR = Prefs.getPreferences().getColor("graph.edge.color", "#3c6318");
      this.graph = new mxGraph() {
         public String getToolTipForCell(Object var1) {
            return NetworkGraph.this.tooltips.get(var1) == null ? "" : NetworkGraph.this.tooltips.get(var1) + "";
         }
      };
      this.graph.setAutoOrigin(true);
      this.graph.setCellsEditable(false);
      this.graph.setCellsResizable(false);
      this.graph.setCellsBendable(false);
      this.graph.setAllowDanglingEdges(false);
      this.graph.setSplitEnabled(false);
      this.graph.setKeepEdgesInForeground(false);
      this.graph.setKeepEdgesInBackground(true);
      this.parent = this.graph.getDefaultParent();
      this.component = new NetworkGraphComponent(this.graph);
      this.component.setFoldingEnabled(true);
      this.component.setConnectable(false);
      this.component.setCenterPage(true);
      this.component.setToolTips(true);
      this.graph.setDropEnabled(true);
      new mxRubberband(this.component);
      this.addPopupListener();
      this.layout = Prefs.getPreferences().getString("graph.default_layout.layout", "none");
      this.component.getViewport().setOpaque(false);
      this.component.setOpaque(true);
      this.component.setBackground(Prefs.getPreferences().getColor("graph.background.color", "#111111"));
      this.setLayout(new BorderLayout());
      this.add(this.component, "Center");
      this.setupShortcuts();
   }

   public void addActionForKeyStroke(KeyStroke var1, Action var2) {
      this.component.getActionMap().put(var1.toString(), var2);
      this.component.getInputMap().put(var1, var1.toString());
   }

   public void addActionForKey(String var1, Action var2) {
      this.addActionForKeyStroke(KeyStroke.getKeyStroke(var1), var2);
   }

   public void addActionForKeySetting(String var1, String var2, Action var3) {
      KeyStroke var4 = KeyStroke.getKeyStroke(var2);
      if (var4 != null) {
         this.addActionForKeyStroke(var4, var3);
      }

   }

   public void doStackLayout() {
      if (this.layout != null) {
         this.layout = "stack";
      }

      mxStackLayout var1 = new mxStackLayout(this.graph, true, 25);
      var1.execute(this.parent);
   }

   public void doTreeLeftLayout() {
      if (this.layout != null) {
         this.layout = "tree-left";
      }

      mxHierarchicalLayout var1 = new mxHierarchicalLayout(this.graph, 7);
      var1.execute(this.parent);
   }

   public void doTreeRightLayout() {
      if (this.layout != null) {
         this.layout = "tree-right";
      }

      mxHierarchicalLayout var1 = new mxHierarchicalLayout(this.graph, 3);
      var1.execute(this.parent);
   }

   public void doTreeTopLayout() {
      if (this.layout != null) {
         this.layout = "tree-top";
      }

      mxHierarchicalLayout var1 = new mxHierarchicalLayout(this.graph, 1);
      var1.execute(this.parent);
   }

   public void doTreeBottomLayout() {
      if (this.layout != null) {
         this.layout = "tree-bottom";
      }

      mxHierarchicalLayout var1 = new mxHierarchicalLayout(this.graph, 5);
      var1.execute(this.parent);
   }

   public void doCircleLayout() {
      if (this.layout != null) {
         this.layout = "circle";
      }

      CircleLayout var1 = new CircleLayout(this.graph, 1.0);
      var1.execute(this.parent);
   }

   public void doTreeLayout() {
      mxFastOrganicLayout var1 = new mxFastOrganicLayout(this.graph);
      var1.execute(this.parent);
   }

   private void setupShortcuts() {
      this.addActionForKeySetting("graph.clear_selection.shortcut", "pressed ESCAPE", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.clearSelection();
         }
      });
      this.addActionForKeySetting("graph.select_all.shortcut", "ctrl pressed A", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.selectAll();
         }
      });
      this.addActionForKeySetting("graph.zoom_in.shortcut", "ctrl pressed EQUALS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.zoom(0.1);
         }
      });
      this.addActionForKeySetting("graph.zoom_out.shortcut", "ctrl pressed MINUS", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.zoom(-0.1);
         }
      });
      this.addActionForKeySetting("graph.zoom_reset.shortcut", "ctrl pressed 0", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.resetZoom();
         }
      });
      this.addActionForKeySetting("graph.arrange_icons_stack.shortcut", "ctrl pressed S", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.doStackLayout();
         }
      });
      this.addActionForKeySetting("graph.arrange_icons_circle.shortcut", "ctrl pressed C", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.doCircleLayout();
         }
      });
      this.addActionForKeySetting("graph.arrange_icons_hierarchical.shortcut", "ctrl pressed H", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            NetworkGraph.this.doTreeLeftLayout();
         }
      });
   }

   public String getCellAt(Point var1) {
      Point var2 = this.component.getViewport().getViewPosition();
      Point var3 = new Point((int)(var1.getX() + var2.getX()), (int)(var1.getY() + var2.getY()));
      mxCell var4 = (mxCell)this.component.getCellAt((int)var3.getX(), (int)var3.getY());
      return var4 != null ? var4.getId() : null;
   }

   public String[] getSelectedHosts() {
      LinkedList var2 = new LinkedList();
      Object[] var3 = this.graph.getSelectionCells();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         mxCell var1 = (mxCell)var3[var4];
         if (this.nodes.containsKey(var1.getId()) && !"".equals(var1.getId())) {
            var2.add(var1.getId());
         }
      }

      String[] var7 = new String[var2.size()];
      Iterator var5 = var2.iterator();

      for(int var6 = 0; var5.hasNext(); ++var6) {
         var7[var6] = var5.next() + "";
      }

      return var7;
   }

   private void addPopupListener() {
      this.component.getGraphControl().addMouseListener(new MouseAdapter() {
         public void handleEvent(MouseEvent var1) {
            if (var1.isPopupTrigger() && NetworkGraph.this.getGraphPopup() != null) {
               NetworkGraph.this.getGraphPopup().showGraphPopup(NetworkGraph.this.getSelectedHosts(), var1);
               var1.consume();
            } else if (var1.getClickCount() >= 2 && !var1.isConsumed()) {
            }

         }

         public void mousePressed(MouseEvent var1) {
            this.handleEvent(var1);
         }

         public void mouseReleased(MouseEvent var1) {
            this.handleEvent(var1);
         }

         public void mouseClicked(MouseEvent var1) {
            this.handleEvent(var1);
         }
      });
   }

   public void resetZoom() {
      this.zoom = 1.0;
      this.zoom(0.0);
   }

   public void zoom(double var1) {
      this.zoom += var1;
      this.component.zoomTo(this.zoom, true);
   }

   public void start() {
      this.graph.getModel().beginUpdate();
      this.nodes.startUpdates();
      Iterator var1 = this.edges.iterator();

      while(var1.hasNext()) {
         mxCell var2 = (mxCell)var1.next();
         this.graph.getModel().remove(var2);
      }

      this.edges = new LinkedList();
   }

   public void setAutoLayout(String var1) {
      this.layout = var1;
      this.autoLayout();
   }

   public void autoLayout() {
      if (this.layout != null) {
         if (this.layout.equals("circle")) {
            this.doCircleLayout();
         }

         if (this.layout.equals("stack")) {
            this.doStackLayout();
         }

         if (this.layout.equals("tree-left")) {
            this.doTreeLeftLayout();
         }

         if (this.layout.equals("tree-top")) {
            this.doTreeTopLayout();
         }

         if (this.layout.equals("tree-right")) {
            this.doTreeRightLayout();
         }

         if (this.layout.equals("tree-bottom")) {
            this.doTreeBottomLayout();
         }

      }
   }

   public void end() {
      this.graph.getModel().endUpdate();
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            NetworkGraph.this.autoLayout();
            NetworkGraph.this.graph.refresh();
         }
      });
   }

   public void deleteNodes(String[] var1) {
      Object[] var2 = new Object[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.nodes.remove(var1[var3]);
      }

      this.graph.removeCells(var2, true);
   }

   public void deleteNodes() {
      List var1 = this.nodes.clearUntouched();
      Object[] var2 = new Object[var1.size()];
      Iterator var3 = var1.iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         Map.Entry var5 = (Map.Entry)var3.next();
         var2[var4] = var5.getValue();
      }

      this.graph.removeCells(var2, true);
   }

   public void addEdge(String var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      mxCell var8 = (mxCell)this.nodes.get(var1);
      mxCell var9 = (mxCell)this.nodes.get(var2);
      mxCell var10 = (mxCell)this.graph.insertEdge(this.parent, (String)null, var6, var8, var9);
      StringBuffer var11 = new StringBuffer();
      var11.append("fontColor=" + Prefs.getPreferences().getString("graph.foreground.color", "#cccccc") + ";");
      Font var12 = Prefs.getPreferences().getFont("graph.font.font", "Monospaced BOLD 14");
      var11.append("fontSize=" + var12.getSize() + ";");
      var11.append("fontFamily=" + var12.getFamily() + ";");
      var11.append("fontStyle=" + var12.getStyle() + ";");
      var11.append("strokeColor=" + var3 + ";strokeWidth=" + var4 + ";dashed=" + var5);
      if (var7 == 2) {
         var11.append(";startArrow=classic;endArrow=none");
      } else if (var7 == 1) {
         var11.append(";startArrow=none;endArrow=classic");
      } else if (var7 == 0) {
         var11.append(";startArrow=none;endArrow=none");
      }

      var10.setStyle(var11.toString());
      this.edges.add(var10);
   }

   public Object addNode(String var1, String var2, String var3, Image var4, String var5, String var6) {
      this.nodeImages.put(var1, var4);
      if (var2.length() > 0) {
         if (var3.length() > 0) {
            var3 = var3 + "\n" + var2;
         } else {
            var3 = var2;
         }
      }

      mxCell var7;
      if (!this.nodes.containsKey(var1)) {
         var7 = (mxCell)this.graph.insertVertex(this.parent, var1, var3, 0.0, 0.0, 125.0, 97.0);
         this.nodes.put(var1, var7);
      } else {
         var7 = (mxCell)this.nodes.get(var1);
         var7.setValue(var3);
      }

      this.nodes.touch(var1);
      this.tooltips.put(var7, var5);
      StringBuffer var8 = new StringBuffer();
      var8.append("shape=image;image=" + var1 + ";");
      if ("good".equals(var6)) {
         var8.append("fontColor=#c6efce;");
      } else if ("bad".equals(var6)) {
         var8.append("fontColor=#ffc7ce;");
      } else if ("neutral".equals(var6)) {
         var8.append("fontColor=#ffeb9c;");
      } else if ("ignore".equals(var6)) {
         var8.append("fontColor=#a5a5a5;");
      } else if ("cancel".equals(var6)) {
         var8.append("fontColor=#3d579e;");
      } else {
         var8.append("fontColor=" + Prefs.getPreferences().getString("graph.foreground.color", "#cccccc") + ";");
      }

      Font var9 = Prefs.getPreferences().getFont("graph.font.font", "Monospaced BOLD 14");
      var8.append("fontSize=" + var9.getSize() + ";");
      var8.append("fontFamily=" + var9.getFamily() + ";");
      var8.append("fontStyle=" + var9.getStyle() + ";");
      var8.append("verticalLabelPosition=bottom;verticalAlign=top");
      var7.setStyle(var8.toString());
      return var7;
   }

   public boolean requestFocusInWindow() {
      return this.component.requestFocusInWindow();
   }

   private class NetworkGraphComponent extends mxGraphComponent {
      public NetworkGraphComponent(mxGraph var2) {
         super(var2);
         this.setBorder(BorderFactory.createEmptyBorder());
         this.getHorizontalScrollBar().setUnitIncrement(15);
         this.getHorizontalScrollBar().setBlockIncrement(60);
         this.getVerticalScrollBar().setUnitIncrement(15);
         this.getVerticalScrollBar().setBlockIncrement(60);
      }

      public mxInteractiveCanvas createCanvas() {
         return NetworkGraph.this.new NetworkGraphCanvas();
      }
   }

   private class NetworkGraphCanvas extends mxInteractiveCanvas {
      private NetworkGraphCanvas() {
      }

      public Image loadImage(String var1) {
         return NetworkGraph.this.nodeImages.containsKey(var1) ? (Image)NetworkGraph.this.nodeImages.get(var1) : super.loadImage(var1);
      }

      // $FF: synthetic method
      NetworkGraphCanvas(Object var2) {
         this();
      }
   }
}
