package org.apache.fop.render.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.IOException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.Area;
import org.apache.fop.area.PageViewport;
import org.apache.fop.render.awt.viewer.PreviewDialog;
import org.apache.fop.render.awt.viewer.Renderable;
import org.apache.fop.render.awt.viewer.StatusListener;
import org.apache.fop.render.extensions.prepress.PageScale;
import org.apache.fop.render.java2d.Java2DRenderer;

public class AWTRenderer extends Java2DRenderer implements Pageable {
   public static final String MIME_TYPE = "application/X-fop-awt-preview";
   public boolean debug;
   public boolean dialogDisplay;
   private boolean previewAsMainWindow;
   protected Renderable renderable;
   protected StatusListener statusListener;

   public AWTRenderer() {
      this(false);
   }

   public AWTRenderer(boolean previewAsMainWindow) {
      this.dialogDisplay = true;
      this.statusListener = null;
      this.previewAsMainWindow = previewAsMainWindow;
   }

   public void setUserAgent(FOUserAgent foUserAgent) {
      super.setUserAgent(foUserAgent);
      if (this.dialogDisplay) {
         this.setStatusListener(PreviewDialog.createPreviewDialog(this.userAgent, this.renderable, this.previewAsMainWindow));
      }

   }

   public void setRenderable(Renderable renderable) {
      this.renderable = renderable;
   }

   public void setPreviewDialogDisplayed(boolean show) {
      this.dialogDisplay = show;
   }

   public void renderPage(PageViewport pageViewport) throws IOException {
      super.renderPage(pageViewport);
      if (this.statusListener != null) {
         this.statusListener.notifyPageRendered();
      }

   }

   public void stopRenderer() throws IOException {
      super.stopRenderer();
      if (this.statusListener != null) {
         this.statusListener.notifyRendererStopped();
      }

   }

   public Dimension getPageImageSize(int pageNum) throws FOPException {
      Rectangle2D bounds = this.getPageViewport(pageNum).getViewArea();
      this.pageWidth = (int)Math.round(bounds.getWidth() / 1000.0);
      this.pageHeight = (int)Math.round(bounds.getHeight() / 1000.0);
      double scaleX = this.scaleFactor * 0.35277777777777775 / (double)this.userAgent.getTargetPixelUnitToMillimeter();
      double scaleY = this.scaleFactor * 0.35277777777777775 / (double)this.userAgent.getTargetPixelUnitToMillimeter();
      if (this.getPageViewport(pageNum).getForeignAttributes() != null) {
         String scale = (String)this.getPageViewport(pageNum).getForeignAttributes().get(PageScale.EXT_PAGE_SCALE);
         Point2D scales = PageScale.getScale(scale);
         if (scales != null) {
            scaleX *= scales.getX();
            scaleY *= scales.getY();
         }
      }

      int bitmapWidth = (int)((double)this.pageWidth * scaleX + 0.5);
      int bitmapHeight = (int)((double)this.pageHeight * scaleY + 0.5);
      return new Dimension(bitmapWidth, bitmapHeight);
   }

   public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      try {
         if (pageIndex >= this.getNumberOfPages()) {
            return null;
         } else {
            PageFormat pageFormat = new PageFormat();
            Paper paper = new Paper();
            Rectangle2D dim = this.getPageViewport(pageIndex).getViewArea();
            double width = dim.getWidth();
            double height = dim.getHeight();
            if (width > height) {
               paper.setImageableArea(0.0, 0.0, height / 1000.0, width / 1000.0);
               paper.setSize(height / 1000.0, width / 1000.0);
               pageFormat.setOrientation(0);
            } else {
               paper.setImageableArea(0.0, 0.0, width / 1000.0, height / 1000.0);
               paper.setSize(width / 1000.0, height / 1000.0);
               pageFormat.setOrientation(1);
            }

            pageFormat.setPaper(paper);
            return pageFormat;
         }
      } catch (FOPException var9) {
         throw new IndexOutOfBoundsException(var9.getMessage());
      }
   }

   public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
      return this;
   }

   public boolean supportsOutOfOrder() {
      return false;
   }

   public String getMimeType() {
      return "application/X-fop-awt-preview";
   }

   protected void drawBackAndBorders(Area area, float startx, float starty, float width, float height) {
      if (this.debug) {
         this.debugBackAndBorders(area, startx, starty, width, height);
      }

      super.drawBackAndBorders(area, startx, starty, width, height);
   }

   private void debugBackAndBorders(Area area, float startx, float starty, float width, float height) {
      this.saveGraphicsState();
      Color col = new Color(0.7F, 0.7F, 0.7F);
      this.state.updateColor(col);
      this.state.updateStroke(0.4F, 133);
      this.state.getGraph().draw(new Rectangle2D.Float(startx, starty, width, height));
      this.restoreGraphicsState();
   }

   public StatusListener getStatusListener() {
      return this.statusListener;
   }

   public void setStatusListener(StatusListener statusListener) {
      this.statusListener = statusListener;
   }
}
