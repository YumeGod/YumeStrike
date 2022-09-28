package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraphView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class mxCellMarker extends JComponent {
   private static final long serialVersionUID = 614473367053597572L;
   public static boolean KEEP_ON_TOP = false;
   public static Stroke DEFAULT_STROKE = new BasicStroke(3.0F);
   protected mxEventSource eventSource;
   protected mxGraphComponent graphComponent;
   protected boolean enabled;
   protected double hotspot;
   protected boolean hotspotEnabled;
   protected boolean swimlaneContentEnabled;
   protected Color validColor;
   protected Color invalidColor;
   protected transient Color currentColor;
   protected transient mxCellState validState;
   protected transient mxCellState markedState;

   public mxCellMarker(mxGraphComponent var1) {
      this(var1, mxConstants.DEFAULT_VALID_COLOR);
   }

   public mxCellMarker(mxGraphComponent var1, Color var2) {
      this(var1, var2, mxConstants.DEFAULT_INVALID_COLOR);
   }

   public mxCellMarker(mxGraphComponent var1, Color var2, Color var3) {
      this(var1, var2, var3, mxConstants.DEFAULT_HOTSPOT);
   }

   public mxCellMarker(mxGraphComponent var1, Color var2, Color var3, double var4) {
      this.eventSource = new mxEventSource(this);
      this.enabled = true;
      this.hotspotEnabled = false;
      this.swimlaneContentEnabled = false;
      this.graphComponent = var1;
      this.validColor = var2;
      this.invalidColor = var3;
      this.hotspot = var4;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setHotspot(double var1) {
      this.hotspot = var1;
   }

   public double getHotspot() {
      return this.hotspot;
   }

   public void setHotspotEnabled(boolean var1) {
      this.hotspotEnabled = var1;
   }

   public boolean isHotspotEnabled() {
      return this.hotspotEnabled;
   }

   public void setSwimlaneContentEnabled(boolean var1) {
      this.swimlaneContentEnabled = var1;
   }

   public boolean isSwimlaneContentEnabled() {
      return this.swimlaneContentEnabled;
   }

   public void setValidColor(Color var1) {
      this.validColor = var1;
   }

   public Color getValidColor() {
      return this.validColor;
   }

   public void setInvalidColor(Color var1) {
      this.invalidColor = var1;
   }

   public Color getInvalidColor() {
      return this.invalidColor;
   }

   public boolean hasValidState() {
      return this.validState != null;
   }

   public mxCellState getValidState() {
      return this.validState;
   }

   public void setCurrentColor(Color var1) {
      this.currentColor = var1;
   }

   public Color getCurrentColor() {
      return this.currentColor;
   }

   public void setMarkedState(mxCellState var1) {
      this.markedState = var1;
   }

   public mxCellState getMarkedState() {
      return this.markedState;
   }

   public void reset() {
      this.validState = null;
      if (this.markedState != null) {
         this.markedState = null;
         this.unmark();
      }

   }

   public mxCellState process(MouseEvent var1) {
      mxCellState var2 = null;
      if (this.isEnabled()) {
         var2 = this.getState(var1);
         boolean var3 = var2 != null ? this.isValidState(var2) : false;
         Color var4 = this.getMarkerColor(var1, var2, var3);
         if (var3) {
            this.validState = var2;
         } else {
            this.validState = null;
         }

         if (var2 != this.markedState || var4 != this.currentColor) {
            this.currentColor = var4;
            if (var2 != null && this.currentColor != null) {
               this.markedState = var2;
               this.mark();
            } else if (this.markedState != null) {
               this.markedState = null;
               this.unmark();
            }
         }
      }

      return var2;
   }

   public void mark() {
      if (this.markedState != null) {
         Rectangle var1 = this.markedState.getRectangle();
         var1.grow(3, 3);
         ++var1.width;
         ++var1.height;
         this.setBounds(var1);
         if (this.getParent() == null) {
            this.setVisible(true);
            if (KEEP_ON_TOP) {
               this.graphComponent.getGraphControl().add(this, 0);
            } else {
               this.graphComponent.getGraphControl().add(this);
            }
         }

         this.repaint();
         this.eventSource.fireEvent(new mxEventObject(mxEvent.MARK, new Object[]{"state", this.markedState}));
      }

   }

   public void unmark() {
      if (this.getParent() != null) {
         this.setVisible(false);
         this.getParent().remove(this);
         this.eventSource.fireEvent(new mxEventObject(mxEvent.MARK));
      }

   }

   protected boolean isValidState(mxCellState var1) {
      return true;
   }

   protected Color getMarkerColor(MouseEvent var1, mxCellState var2, boolean var3) {
      return var3 ? this.validColor : this.invalidColor;
   }

   protected mxCellState getState(MouseEvent var1) {
      Object var2 = this.getCell(var1);
      mxGraphView var3 = this.graphComponent.getGraph().getView();
      mxCellState var4 = this.getStateToMark(var3.getState(var2));
      return var4 != null && this.intersects(var4, var1) ? var4 : null;
   }

   protected Object getCell(MouseEvent var1) {
      return this.graphComponent.getCellAt(var1.getX(), var1.getY(), this.swimlaneContentEnabled);
   }

   protected mxCellState getStateToMark(mxCellState var1) {
      return var1;
   }

   protected boolean intersects(mxCellState var1, MouseEvent var2) {
      return this.isHotspotEnabled() ? mxUtils.intersectsHotspot(var1, var2.getX(), var2.getY(), this.hotspot, mxConstants.MIN_HOTSPOT_SIZE, mxConstants.MAX_HOTSPOT_SIZE) : true;
   }

   public void addListener(String var1, mxEventSource.mxIEventListener var2) {
      this.eventSource.addListener(var1, var2);
   }

   public void removeListener(mxEventSource.mxIEventListener var1) {
      this.eventSource.removeListener(var1);
   }

   public void removeListener(mxEventSource.mxIEventListener var1, String var2) {
      this.eventSource.removeListener(var1, var2);
   }

   public void paint(Graphics var1) {
      if (this.markedState != null && this.currentColor != null) {
         ((Graphics2D)var1).setStroke(DEFAULT_STROKE);
         var1.setColor(this.currentColor);
         if (this.markedState.getAbsolutePointCount() > 0) {
            Point var2 = this.markedState.getAbsolutePoint(0).getPoint();

            for(int var3 = 1; var3 < this.markedState.getAbsolutePointCount(); ++var3) {
               Point var4 = this.markedState.getAbsolutePoint(var3).getPoint();
               var1.drawLine(var2.x - this.getX(), var2.y - this.getY(), var4.x - this.getX(), var4.y - this.getY());
               var2 = var4;
            }
         } else {
            var1.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
         }
      }

   }
}
