package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMouseAdapter;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class mxInsertHandler extends mxMouseAdapter {
   protected mxGraphComponent graphComponent;
   protected boolean enabled = true;
   protected String style;
   protected Point first;
   protected float lineWidth = 1.0F;
   protected Color lineColor;
   protected boolean rounded;
   protected mxRectangle current;
   protected mxEventSource eventSource;

   public mxInsertHandler(mxGraphComponent var1, String var2) {
      this.lineColor = Color.black;
      this.rounded = false;
      this.eventSource = new mxEventSource(this);
      this.graphComponent = var1;
      this.style = var2;
      var1.addListener("afterPaint", new mxEventSource.mxIEventListener() {
         public void invoke(Object var1, mxEventObject var2) {
            Graphics var3 = (Graphics)var2.getProperty("g");
            mxInsertHandler.this.paint(var3);
         }
      });
      var1.getGraphControl().addMouseListener(this);
      var1.getGraphControl().addMouseMotionListener(this);
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

   public boolean isStartEvent(MouseEvent var1) {
      return true;
   }

   public void start(MouseEvent var1) {
      this.first = var1.getPoint();
   }

   public void mousePressed(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && this.isStartEvent(var1)) {
         this.start(var1);
         var1.consume();
      }

   }

   public void mouseDragged(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && this.first != null) {
         mxRectangle var2 = this.current;
         this.current = new mxRectangle((double)this.first.x, (double)this.first.y, 0.0, 0.0);
         this.current.add(new mxRectangle((double)var1.getX(), (double)var1.getY(), 0.0, 0.0));
         if (var2 != null) {
            var2.add(this.current);
         } else {
            var2 = this.current;
         }

         Rectangle var3 = var2.getRectangle();
         int var4 = (int)Math.ceil((double)this.lineWidth);
         this.graphComponent.getGraphControl().repaint(var3.x - var4, var3.y - var4, var3.width + 2 * var4, var3.height + 2 * var4);
         var1.consume();
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (this.graphComponent.isEnabled() && this.isEnabled() && !var1.isConsumed() && this.current != null) {
         mxGraph var2 = this.graphComponent.getGraph();
         double var3 = var2.getView().getScale();
         mxPoint var5 = var2.getView().getTranslate();
         this.current.setX(this.current.getX() / var3 - var5.getX());
         this.current.setY(this.current.getY() / var3 - var5.getY());
         this.current.setWidth(this.current.getWidth() / var3);
         this.current.setHeight(this.current.getHeight() / var3);
         Object var6 = this.insertCell(this.current);
         this.eventSource.fireEvent(new mxEventObject("insert", new Object[]{"cell", var6}));
         var1.consume();
      }

      this.reset();
   }

   public Object insertCell(mxRectangle var1) {
      return this.graphComponent.getGraph().insertVertex((Object)null, (String)null, "", var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight(), this.style);
   }

   public void reset() {
      Rectangle var1 = null;
      if (this.current != null) {
         var1 = this.current.getRectangle();
      }

      this.current = null;
      this.first = null;
      if (var1 != null) {
         int var2 = (int)Math.ceil((double)this.lineWidth);
         this.graphComponent.getGraphControl().repaint(var1.x - var2, var1.y - var2, var1.width + 2 * var2, var1.height + 2 * var2);
      }

   }

   public void paint(Graphics var1) {
      if (this.first != null && this.current != null) {
         ((Graphics2D)var1).setStroke(new BasicStroke(this.lineWidth));
         var1.setColor(this.lineColor);
         Rectangle var2 = this.current.getRectangle();
         if (this.rounded) {
            var1.drawRoundRect(var2.x, var2.y, var2.width, var2.height, 8, 8);
         } else {
            var1.drawRect(var2.x, var2.y, var2.width, var2.height);
         }
      }

   }

   public void addListener(String var1, mxEventSource.mxIEventListener var2) {
      this.eventSource.addListener(var1, var2);
   }

   public void removeListener(mxEventSource.mxIEventListener var1) {
      this.removeListener(var1, (String)null);
   }

   public void removeListener(mxEventSource.mxIEventListener var1, String var2) {
      this.eventSource.removeListener(var1, var2);
   }
}
