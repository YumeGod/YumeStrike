package com.mxgraph.swing.handler;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class mxVertexHandler extends mxCellHandler {
   public static Cursor[] CURSORS = new Cursor[]{new Cursor(6), new Cursor(8), new Cursor(7), new Cursor(10), new Cursor(11), new Cursor(4), new Cursor(9), new Cursor(5), new Cursor(13)};
   protected transient boolean gridEnabledEvent = false;
   protected transient boolean constrainedEvent = false;

   public mxVertexHandler(mxGraphComponent var1, mxCellState var2) {
      super(var1, var2);
   }

   protected Rectangle[] createHandles() {
      Rectangle[] var1 = null;
      if (this.graphComponent.getGraph().isCellResizable(this.getState().getCell())) {
         Rectangle var2 = this.getState().getRectangle();
         int var3 = mxConstants.HANDLE_SIZE / 2;
         int var4 = var2.x - var3;
         int var5 = var2.y - var3;
         int var6 = var2.x + var2.width / 2 - var3;
         int var7 = var2.y + var2.height / 2 - var3;
         int var8 = var2.x + var2.width - var3;
         int var9 = var2.y + var2.height - var3;
         var1 = new Rectangle[9];
         int var10 = mxConstants.HANDLE_SIZE;
         var1[0] = new Rectangle(var4, var5, var10, var10);
         var1[1] = new Rectangle(var6, var5, var10, var10);
         var1[2] = new Rectangle(var8, var5, var10, var10);
         var1[3] = new Rectangle(var4, var7, var10, var10);
         var1[4] = new Rectangle(var8, var7, var10, var10);
         var1[5] = new Rectangle(var4, var9, var10, var10);
         var1[6] = new Rectangle(var6, var9, var10, var10);
         var1[7] = new Rectangle(var8, var9, var10, var10);
      } else {
         var1 = new Rectangle[1];
      }

      int var11 = mxConstants.LABEL_HANDLE_SIZE;
      mxRectangle var12 = this.state.getLabelBounds();
      var1[var1.length - 1] = new Rectangle((int)(var12.getX() + var12.getWidth() / 2.0 - (double)var11), (int)(var12.getY() + var12.getHeight() / 2.0 - (double)var11), 2 * var11, 2 * var11);
      return var1;
   }

   protected JComponent createPreview() {
      JPanel var1 = new JPanel();
      var1.setBorder(mxConstants.PREVIEW_BORDER);
      var1.setOpaque(false);
      var1.setVisible(false);
      return var1;
   }

   public void mouseDragged(MouseEvent var1) {
      if (!var1.isConsumed() && this.first != null) {
         this.gridEnabledEvent = this.graphComponent.isGridEnabledEvent(var1);
         this.constrainedEvent = this.graphComponent.isConstrainedEvent(var1);
         double var2 = (double)(var1.getX() - this.first.x);
         double var4 = (double)(var1.getY() - this.first.y);
         if (this.isLabel(this.index)) {
            mxPoint var6 = new mxPoint(var1.getPoint());
            if (this.gridEnabledEvent) {
               var6 = this.graphComponent.snapScaledPoint(var6);
            }

            int var7 = (int)Math.round(var6.getX() - (double)this.first.x);
            int var8 = (int)Math.round(var6.getY() - (double)this.first.y);
            if (this.constrainedEvent) {
               if (Math.abs(var7) > Math.abs(var8)) {
                  var8 = 0;
               } else {
                  var7 = 0;
               }
            }

            Rectangle var9 = this.state.getLabelBounds().getRectangle();
            var9.translate(var7, var8);
            this.preview.setBounds(var9);
         } else {
            mxGraph var10 = this.graphComponent.getGraph();
            double var11 = var10.getView().getScale();
            if (this.gridEnabledEvent) {
               var2 = var10.snap(var2 / var11) * var11;
               var4 = var10.snap(var4 / var11) * var11;
            }

            mxRectangle var12 = this.union(this.getState(), var2, var4, this.index);
            var12.setWidth(var12.getWidth() + 1.0);
            var12.setHeight(var12.getHeight() + 1.0);
            this.preview.setBounds(var12.getRectangle());
         }

         if (!this.preview.isVisible() && this.graphComponent.isSignificant(var2, var4)) {
            this.preview.setVisible(true);
         }

         var1.consume();
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (!var1.isConsumed() && this.first != null) {
         if (this.preview != null && this.preview.isVisible()) {
            if (this.isLabel(this.index)) {
               this.moveLabel(var1);
            } else {
               this.resizeCell(var1);
            }
         }

         var1.consume();
      }

      super.mouseReleased(var1);
   }

   protected void moveLabel(MouseEvent var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      mxGeometry var3 = var2.getModel().getGeometry(this.state.getCell());
      if (var3 != null) {
         double var4 = var2.getView().getScale();
         mxPoint var6 = new mxPoint(var1.getPoint());
         if (this.gridEnabledEvent) {
            var6 = this.graphComponent.snapScaledPoint(var6);
         }

         double var7 = (var6.getX() - (double)this.first.x) / var4;
         double var9 = (var6.getY() - (double)this.first.y) / var4;
         if (this.constrainedEvent) {
            if (Math.abs(var7) > Math.abs(var9)) {
               var9 = 0.0;
            } else {
               var7 = 0.0;
            }
         }

         mxPoint var11 = var3.getOffset();
         if (var11 == null) {
            var11 = new mxPoint();
         }

         var7 += var11.getX();
         var9 += var11.getY();
         var3 = (mxGeometry)var3.clone();
         var3.setOffset(new mxPoint((double)Math.round(var7), (double)Math.round(var9)));
         var2.getModel().setGeometry(this.state.getCell(), var3);
      }

   }

   protected void resizeCell(MouseEvent var1) {
      mxGraph var2 = this.graphComponent.getGraph();
      double var3 = var2.getView().getScale();
      Object var5 = this.state.getCell();
      mxGeometry var6 = var2.getModel().getGeometry(var5);
      if (var6 != null) {
         double var7 = (double)(var1.getX() - this.first.x) / var3;
         double var9 = (double)(var1.getY() - this.first.y) / var3;
         if (this.isLabel(this.index)) {
            var6 = (mxGeometry)var6.clone();
            if (var6.getOffset() != null) {
               var7 += var6.getOffset().getX();
               var9 += var6.getOffset().getY();
            }

            if (this.gridEnabledEvent) {
               var7 = var2.snap(var7);
               var9 = var2.snap(var9);
            }

            var6.setOffset(new mxPoint(var7, var9));
            var2.getModel().setGeometry(var5, var6);
         } else {
            mxRectangle var11 = this.union(var6, var7, var9, this.index);
            Rectangle var12 = var11.getRectangle();
            if (this.gridEnabledEvent) {
               int var13 = (int)var2.snap((double)var12.x);
               int var14 = (int)var2.snap((double)var12.y);
               var12.width = (int)var2.snap((double)(var12.width - var13 + var12.x));
               var12.height = (int)var2.snap((double)(var12.height - var14 + var12.y));
               var12.x = var13;
               var12.y = var14;
            }

            var2.resizeCell(var5, new mxRectangle(var12));
         }
      }

   }

   protected Cursor getCursor(MouseEvent var1, int var2) {
      return var2 >= 0 && var2 <= CURSORS.length ? CURSORS[var2] : null;
   }

   protected mxRectangle union(mxRectangle var1, double var2, double var4, int var6) {
      double var7 = var1.getX();
      double var9 = var7 + var1.getWidth();
      double var11 = var1.getY();
      double var13 = var11 + var1.getHeight();
      if (var6 > 4) {
         var13 += var4;
      } else if (var6 < 3) {
         var11 += var4;
      }

      if (var6 != 0 && var6 != 3 && var6 != 5) {
         if (var6 == 2 || var6 == 4 || var6 == 7) {
            var9 += var2;
         }
      } else {
         var7 += var2;
      }

      double var15 = var9 - var7;
      double var17 = var13 - var11;
      if (var15 < 0.0) {
         var7 += var15;
         var15 = Math.abs(var15);
      }

      if (var17 < 0.0) {
         var11 += var17;
         var17 = Math.abs(var17);
      }

      return new mxRectangle(var7, var11, var15, var17);
   }

   protected Color getSelectionColor() {
      return mxConstants.VERTEX_SELECTION_COLOR;
   }

   protected Stroke getSelectionStroke() {
      return mxConstants.VERTEX_SELECTION_STROKE;
   }

   public void paint(Graphics var1) {
      Rectangle var2 = this.getState().getRectangle();
      if (var1.hitClip(var2.x, var2.y, var2.width, var2.height)) {
         Graphics2D var3 = (Graphics2D)var1;
         Stroke var4 = var3.getStroke();
         var3.setStroke(this.getSelectionStroke());
         var1.setColor(this.getSelectionColor());
         var1.drawRect(var2.x, var2.y, var2.width, var2.height);
         var3.setStroke(var4);
      }

      super.paint(var1);
   }
}
