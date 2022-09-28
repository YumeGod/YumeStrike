package com.mxgraph.swing.util;

import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import java.awt.Cursor;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class mxCellOverlay extends JComponent implements mxICellOverlay {
   private static final long serialVersionUID = 921991820491141221L;
   protected ImageIcon imageIcon;
   protected Object align = "right";
   protected Object verticalAlign = "bottom";
   protected double defaultOverlap = 0.5;

   public mxCellOverlay(ImageIcon var1, String var2) {
      this.imageIcon = var1;
      this.setToolTipText(var2);
      this.setCursor(new Cursor(0));
   }

   public void paint(Graphics var1) {
      var1.drawImage(this.imageIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
   }

   public mxRectangle getBounds(mxCellState var1) {
      boolean var2 = var1.getView().getGraph().getModel().isEdge(var1.getCell());
      double var3 = var1.getView().getScale();
      mxPoint var5 = null;
      int var6 = this.imageIcon.getIconWidth();
      int var7 = this.imageIcon.getIconHeight();
      if (var2) {
         int var8 = var1.getAbsolutePointCount();
         if (var8 % 2 == 1) {
            var5 = var1.getAbsolutePoint(var8 / 2 + 1);
         } else {
            int var9 = var8 / 2;
            mxPoint var10 = var1.getAbsolutePoint(var9 - 1);
            mxPoint var11 = var1.getAbsolutePoint(var9);
            var5 = new mxPoint(var10.getX() + (var11.getX() - var10.getX()) / 2.0, var10.getY() + (var11.getY() - var10.getY()) / 2.0);
         }
      } else {
         var5 = new mxPoint();
         if (this.align.equals("left")) {
            var5.setX(var1.getX());
         } else if (this.align.equals("center")) {
            var5.setX(var1.getX() + var1.getWidth() / 2.0);
         } else {
            var5.setX(var1.getX() + var1.getWidth());
         }

         if (this.verticalAlign.equals("top")) {
            var5.setY(var1.getY());
         } else if (this.verticalAlign.equals("middle")) {
            var5.setY(var1.getY() + var1.getHeight() / 2.0);
         } else {
            var5.setY(var1.getY() + var1.getHeight());
         }
      }

      return new mxRectangle(var5.getX() - (double)var6 * this.defaultOverlap * var3, var5.getY() - (double)var7 * this.defaultOverlap * var3, (double)var6 * var3, (double)var7 * var3);
   }
}
