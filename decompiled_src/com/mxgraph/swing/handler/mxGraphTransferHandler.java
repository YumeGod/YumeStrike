package com.mxgraph.swing.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class mxGraphTransferHandler extends TransferHandler {
   private static final long serialVersionUID = -6443287704811197675L;
   public static boolean DEFAULT_TRANSFER_IMAGE_ENABLED = true;
   public static Color DEFAULT_BACKGROUNDCOLOR;
   protected Object[] originalCells;
   protected Object[] lastImported;
   protected int importCount = 0;
   protected boolean transferImageEnabled;
   protected Color transferImageBackground;
   protected Point location;
   protected Point offset;

   public mxGraphTransferHandler() {
      this.transferImageEnabled = DEFAULT_TRANSFER_IMAGE_ENABLED;
      this.transferImageBackground = DEFAULT_BACKGROUNDCOLOR;
   }

   public void setTransferImageEnabled(boolean var1) {
      this.transferImageEnabled = var1;
   }

   public boolean isTransferImageEnabled() {
      return this.transferImageEnabled;
   }

   public void setTransferImageBackground(Color var1) {
      this.transferImageBackground = var1;
   }

   public Color getTransferImageBackground() {
      return this.transferImageBackground;
   }

   public boolean isLocalDrag() {
      return this.originalCells != null;
   }

   public void setLocation(Point var1) {
      this.location = var1;
   }

   public void setOffset(Point var1) {
      this.offset = var1;
   }

   public boolean canImport(JComponent var1, DataFlavor[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] != null && var2[var3].equals(mxGraphTransferable.dataFlavor)) {
            return true;
         }
      }

      return false;
   }

   public Transferable createTransferable(JComponent var1) {
      if (var1 instanceof mxGraphComponent) {
         mxGraphComponent var2 = (mxGraphComponent)var1;
         mxGraph var3 = var2.getGraph();
         if (!var3.isSelectionEmpty()) {
            this.originalCells = var2.getExportableCells(var3.getSelectionCells());
            if (this.originalCells.length > 0) {
               ImageIcon var4 = this.transferImageEnabled ? this.createTransferableImage(var2, this.originalCells) : null;
               return this.createGraphTransferable(var2, this.originalCells, var4);
            }
         }
      }

      return null;
   }

   public mxGraphTransferable createGraphTransferable(mxGraphComponent var1, Object[] var2, ImageIcon var3) {
      mxGraph var4 = var1.getGraph();
      mxPoint var5 = var4.getView().getTranslate();
      double var6 = var4.getView().getScale();
      mxRectangle var8 = var4.getPaintBounds(var2);
      var8.setX(var8.getX() / var6 - var5.getX());
      var8.setY(var8.getY() / var6 - var5.getY());
      var8.setWidth(var8.getWidth() / var6);
      var8.setHeight(var8.getHeight() / var6);
      return this.createGraphTransferable(var1, var4.cloneCells(var2), var8, var3);
   }

   public mxGraphTransferable createGraphTransferable(mxGraphComponent var1, Object[] var2, mxRectangle var3, ImageIcon var4) {
      return new mxGraphTransferable(var2, var3, var4);
   }

   public ImageIcon createTransferableImage(mxGraphComponent var1, Object[] var2) {
      ImageIcon var3 = null;
      Color var4 = this.transferImageBackground != null ? this.transferImageBackground : var1.getBackground();
      BufferedImage var5 = mxCellRenderer.createBufferedImage(var1.getGraph(), var2, 1.0, var4, var1.isAntiAlias(), (mxRectangle)null, var1.getCanvas());
      if (var5 != null) {
         var3 = new ImageIcon(var5);
      }

      return var3;
   }

   public void exportDone(JComponent var1, Transferable var2, int var3) {
      if (var1 instanceof mxGraphComponent && var2 instanceof mxGraphTransferable) {
         boolean var4 = this.location != null;
         if (var3 == 2 && !var4) {
            this.removeCells((mxGraphComponent)var1, this.originalCells);
         }
      }

      this.originalCells = null;
      this.location = null;
      this.offset = null;
   }

   protected void removeCells(mxGraphComponent var1, Object[] var2) {
      var1.getGraph().removeCells(var2);
   }

   public int getSourceActions(JComponent var1) {
      return 3;
   }

   public boolean importData(JComponent var1, Transferable var2) {
      boolean var3 = false;
      if (this.isLocalDrag()) {
         var3 = true;
      } else {
         try {
            if (var1 instanceof mxGraphComponent) {
               mxGraphComponent var4 = (mxGraphComponent)var1;
               if (var4.isEnabled() && var2.isDataFlavorSupported(mxGraphTransferable.dataFlavor)) {
                  mxGraphTransferable var5 = (mxGraphTransferable)var2.getTransferData(mxGraphTransferable.dataFlavor);
                  if (var5.getCells() != null) {
                     var3 = this.importCells(var4, var5.getCells(), var5.getBounds());
                  }
               }
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      return var3;
   }

   protected boolean importCells(mxGraphComponent var1, Object[] var2, mxRectangle var3) {
      boolean var4 = false;

      try {
         mxGraph var5 = var1.getGraph();
         double var6 = var5.getView().getScale();
         double var8 = 0.0;
         double var10 = 0.0;
         Object var12 = null;
         if (this.location != null) {
            var12 = var5.getDropTarget(var2, this.location, var1.getCellAt(this.location.x, this.location.y));
            if (var2.length > 0 && var5.getModel().getParent(var2[0]) == var12) {
               var12 = null;
            }
         }

         if (this.location != null && var3 != null) {
            mxPoint var15 = var5.getView().getTranslate();
            var8 = this.location.getX() - (var3.getX() + var15.getX()) * var6;
            var10 = this.location.getY() - (var3.getY() + var15.getY()) * var6;
            var8 = var5.snap(var8 / var6);
            var10 = var5.snap(var10 / var6);
         } else {
            if (this.lastImported != var2) {
               this.importCount = 1;
            } else {
               ++this.importCount;
            }

            int var13 = var5.getGridSize();
            var8 = (double)(this.importCount * var13);
            var10 = (double)(this.importCount * var13);
         }

         if (this.offset != null) {
            var8 += (double)this.offset.x;
            var10 += (double)this.offset.y;
         }

         this.lastImported = var2;
         var2 = var1.getImportableCells(var2);
         if (var5.isSplitEnabled() && var5.isSplitTarget(var12, var2)) {
            var5.splitEdge(var12, var2, var8, var10);
         } else {
            var2 = this.importCells(var1, var2, var8, var10, var12, this.location);
            var5.setSelectionCells(var2);
         }

         this.location = null;
         this.offset = null;
         var4 = true;
         var1.requestFocus();
      } catch (Exception var14) {
         var14.printStackTrace();
      }

      return var4;
   }

   protected Object[] importCells(mxGraphComponent var1, Object[] var2, double var3, double var5, Object var7, Point var8) {
      return var1.importCells(var2, var3, var5, var7, var8);
   }

   static {
      DEFAULT_BACKGROUNDCOLOR = Color.WHITE;
   }
}
