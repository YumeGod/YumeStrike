package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

public class mxSelectionCellsHandler implements MouseListener, MouseMotionListener {
   private static final long serialVersionUID = -882368002120921842L;
   public static int DEFAULT_MAX_HANDLERS = 100;
   protected mxGraphComponent graphComponent;
   protected boolean enabled = true;
   protected boolean visible = true;
   protected Rectangle bounds = null;
   protected int maxHandlers;
   protected transient Map handlers;
   protected transient mxEventSource.mxIEventListener refreshHandler;

   public mxSelectionCellsHandler(mxGraphComponent var1) {
      this.maxHandlers = DEFAULT_MAX_HANDLERS;
      this.handlers = new LinkedHashMap();
      this.refreshHandler = new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            mxSelectionCellsHandler.this.refresh();
         }
      };
      this.graphComponent = var1;
      var1.getGraphControl().addMouseListener(this);
      var1.getGraphControl().addMouseMotionListener(this);
      mxGraph var2 = var1.getGraph();
      var2.getSelectionModel().addListener("change", this.refreshHandler);
      var2.getModel().addListener("change", this.refreshHandler);
      var2.getView().addListener("scale", this.refreshHandler);
      var2.getView().addListener("translate", this.refreshHandler);
      var2.getView().addListener("scaleAndTranslate", this.refreshHandler);
      var2.getView().addListener("down", this.refreshHandler);
      var2.getView().addListener("up", this.refreshHandler);
      var2.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            if (var1.getPropertyName().equals("vertexLabelsMovable") || var1.getPropertyName().equals("edgeLabelsMovable")) {
               mxSelectionCellsHandler.this.refresh();
            }

         }
      });
      var1.addListener("paint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxSelectionCellsHandler.this.paintHandles(var3);
         }
      });
   }

   public mxGraphComponent getGraphComponent() {
      return this.graphComponent;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   public int getMaxHandlers() {
      return this.maxHandlers;
   }

   public void setMaxHandlers(int var1) {
      this.maxHandlers = var1;
   }

   public mxCellHandler getHandler(Object var1) {
      return (mxCellHandler)this.handlers.get(var1);
   }

   public void mousePressed(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && !this.graphComponent.isForceMarqueeEvent(var1) && this.isEnabled()) {
         Iterator var2 = this.handlers.values().iterator();

         while(var2.hasNext() && !var1.isConsumed()) {
            ((mxCellHandler)var2.next()).mousePressed(var1);
         }
      }

   }

   public void mouseMoved(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled()) {
         Iterator var2 = this.handlers.values().iterator();

         while(var2.hasNext() && !var1.isConsumed()) {
            ((mxCellHandler)var2.next()).mouseMoved(var1);
         }
      }

   }

   public void mouseDragged(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled()) {
         Iterator var2 = this.handlers.values().iterator();

         while(var2.hasNext() && !var1.isConsumed()) {
            ((mxCellHandler)var2.next()).mouseDragged(var1);
         }
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled()) {
         Iterator var2 = this.handlers.values().iterator();

         while(var2.hasNext() && !var1.isConsumed()) {
            ((mxCellHandler)var2.next()).mouseReleased(var1);
         }
      }

      this.reset();
   }

   public String getToolTipText(MouseEvent var1) {
      MouseEvent var2 = SwingUtilities.convertMouseEvent(var1.getComponent(), var1, this.graphComponent.getGraphControl());
      Iterator var3 = this.handlers.values().iterator();

      String var4;
      for(var4 = null; var3.hasNext() && var4 == null; var4 = ((mxCellHandler)var3.next()).getToolTipText(var2)) {
      }

      return var4;
   }

   public void reset() {
      Iterator var1 = this.handlers.values().iterator();

      while(var1.hasNext()) {
         ((mxCellHandler)var1.next()).reset();
      }

   }

   public void refresh() {
      mxGraph var1 = this.graphComponent.getGraph();
      Map var2 = this.handlers;
      this.handlers = new LinkedHashMap();
      Object[] var3 = var1.getSelectionCells();
      boolean var4 = var3.length <= this.getMaxHandlers();
      Rectangle var5 = null;

      for(int var6 = 0; var6 < var3.length; ++var6) {
         mxCellState var7 = var1.getView().getState(var3[var6]);
         if (var7 != null) {
            mxCellHandler var8 = (mxCellHandler)var2.get(var3[var6]);
            if (var8 != null) {
               var8.refresh(var7);
            } else {
               var8 = this.graphComponent.createHandler(var7);
            }

            if (var8 != null) {
               var8.setHandlesVisible(var4);
               this.handlers.put(var3[var6], var8);
               if (var5 == null) {
                  var5 = var8.getBounds();
               } else {
                  var5.add(var8.getBounds());
               }
            }
         }
      }

      Rectangle var9 = this.bounds;
      if (var5 != null) {
         if (var9 != null) {
            var9.add(var5);
         } else {
            var9 = var5;
         }
      }

      if (var9 != null) {
         this.graphComponent.getGraphControl().repaint(var9);
      }

      this.bounds = var5;
   }

   public void paintHandles(Graphics var1) {
      Iterator var2 = this.handlers.values().iterator();

      while(var2.hasNext()) {
         ((mxCellHandler)var2.next()).paint(var1);
      }

   }

   public void mouseClicked(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }
}
