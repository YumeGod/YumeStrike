package org.apache.fop.render.awt.viewer;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.PageViewport;
import org.apache.fop.render.awt.AWTRenderer;

public class PreviewPanel extends JPanel {
   public static final int SINGLE = 1;
   public static final int CONTINUOUS = 2;
   public static final int CONT_FACING = 3;
   private static final int BORDER_SPACING = 10;
   private JScrollPane previewArea;
   private AWTRenderer renderer;
   protected FOUserAgent foUserAgent;
   protected Renderable renderable;
   private int currentPage = 0;
   private int firstPage = 0;
   private int pageRange = 1;
   private int displayMode = 1;
   private ImageProxyPanel[] pagePanels = null;
   private JPanel gridPanel = null;
   private Reloader reloader;
   private ViewportScroller scroller;

   public PreviewPanel(FOUserAgent foUserAgent, Renderable renderable, AWTRenderer renderer) {
      super(new GridLayout(1, 1));
      this.renderable = renderable;
      this.renderer = renderer;
      this.foUserAgent = foUserAgent;
      this.foUserAgent.setTargetResolution(Toolkit.getDefaultToolkit().getScreenResolution());
      this.gridPanel = new JPanel();
      this.gridPanel.setLayout(new GridLayout(0, 1));
      this.previewArea = new JScrollPane(this.gridPanel);
      this.previewArea.getViewport().setBackground(Color.gray);
      this.previewArea.getVerticalScrollBar().addAdjustmentListener(new PageNumberListener());
      this.scroller = new ViewportScroller(this.previewArea.getViewport());
      this.previewArea.addMouseListener(this.scroller);
      this.previewArea.addMouseMotionListener(this.scroller);
      this.previewArea.setMinimumSize(new Dimension(50, 50));
      this.add(this.previewArea);
   }

   public int getPage() {
      return this.currentPage;
   }

   public void setPage(int number) {
      int oldPage = this.currentPage;
      if (this.displayMode != 2 && this.displayMode != 3) {
         this.currentPage = number;
         this.firstPage = this.currentPage;
      } else {
         this.currentPage = number;
         this.gridPanel.scrollRectToVisible(this.pagePanels[this.currentPage].getBounds());
      }

      this.showPage();
      this.firePageChange(oldPage, this.currentPage);
   }

   public void setDisplayMode(int mode) {
      if (mode != this.displayMode) {
         this.displayMode = mode;
         this.gridPanel.setLayout(new GridLayout(0, this.displayMode == 3 ? 2 : 1));
         this.reload();
      }

   }

   public int getDisplayMode() {
      return this.displayMode;
   }

   public synchronized void reload() {
      if (this.reloader == null || !this.reloader.isAlive()) {
         this.reloader = new Reloader();
         this.reloader.start();
      }

   }

   void debug() {
      this.renderer.debug = !this.renderer.debug;
      this.reload();
   }

   public void addPageChangeListener(PageChangeListener l) {
      this.listenerList.add(PageChangeListener.class, l);
   }

   public void removePageChangeListener(PageChangeListener l) {
      this.listenerList.remove(PageChangeListener.class, l);
   }

   protected void firePageChange(int oldPage, int newPage) {
      Object[] listeners = this.listenerList.getListenerList();
      PageChangeEvent e = null;

      for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == PageChangeListener.class) {
            if (e == null) {
               e = new PageChangeEvent(this, newPage, oldPage);
            }

            ((PageChangeListener)listeners[i + 1]).pageChanged(e);
         }
      }

   }

   public void setScaleFactor(double scale) {
      this.renderer.setScaleFactor(scale);
      this.reload();
   }

   public double getScaleToFitWindow() throws FOPException {
      Dimension extents = this.previewArea.getViewport().getExtentSize();
      return this.getScaleToFit(extents.getWidth() - 20.0, extents.getHeight() - 20.0);
   }

   public double getScaleToFitWidth() throws FOPException {
      Dimension extents = this.previewArea.getViewport().getExtentSize();
      return this.getScaleToFit(extents.getWidth() - 20.0, Double.MAX_VALUE);
   }

   public double getScaleToFit(double viewWidth, double viewHeight) throws FOPException {
      PageViewport pageViewport = this.renderer.getPageViewport(this.currentPage);
      Rectangle2D pageSize = pageViewport.getViewArea();
      float screenResolution = (float)Toolkit.getDefaultToolkit().getScreenResolution();
      float screenFactor = screenResolution / 72.0F;
      double widthScale = viewWidth / (pageSize.getWidth() / 1000.0) / (double)screenFactor;
      double heightScale = viewHeight / (pageSize.getHeight() / 1000.0) / (double)screenFactor;
      return Math.min(this.displayMode == 3 ? widthScale / 2.0 : widthScale, heightScale);
   }

   public synchronized void showPage() {
      ShowPageImage viewer = new ShowPageImage();
      if (SwingUtilities.isEventDispatchThread()) {
         viewer.run();
      } else {
         SwingUtilities.invokeLater(viewer);
      }

   }

   // $FF: synthetic method
   static JScrollPane access$300(PreviewPanel x0) {
      return x0.previewArea;
   }

   // $FF: synthetic method
   static ImageProxyPanel[] access$402(PreviewPanel x0, ImageProxyPanel[] x1) {
      return x0.pagePanels = x1;
   }

   // $FF: synthetic method
   static JPanel access$600(PreviewPanel x0) {
      return x0.gridPanel;
   }

   // $FF: synthetic method
   static int access$802(PreviewPanel x0, int x1) {
      return x0.firstPage = x1;
   }

   // $FF: synthetic method
   static int access$902(PreviewPanel x0, int x1) {
      return x0.pageRange = x1;
   }

   private class ShowPageImage implements Runnable {
      private ShowPageImage() {
      }

      public void run() {
         for(int pg = PreviewPanel.this.firstPage; pg < PreviewPanel.this.firstPage + PreviewPanel.this.pageRange; ++pg) {
            PreviewPanel.this.pagePanels[pg - PreviewPanel.this.firstPage].setPage(pg);
         }

         PreviewPanel.this.revalidate();
      }

      // $FF: synthetic method
      ShowPageImage(Object x1) {
         this();
      }
   }

   private class PageNumberListener implements AdjustmentListener {
      private PageNumberListener() {
      }

      public void adjustmentValueChanged(AdjustmentEvent e) {
         if (PreviewPanel.this.displayMode == 2 || PreviewPanel.this.displayMode == 3) {
            Adjustable a = e.getAdjustable();
            int value = e.getValue();
            int min = a.getMinimum();
            int max = a.getMaximum();
            int page = PreviewPanel.this.renderer.getNumberOfPages() * value / (max - min);
            if (page != PreviewPanel.this.currentPage) {
               int oldPage = PreviewPanel.this.currentPage;
               PreviewPanel.this.currentPage = page;
               PreviewPanel.this.firePageChange(oldPage, PreviewPanel.this.currentPage);
            }
         }

      }

      // $FF: synthetic method
      PageNumberListener(Object x1) {
         this();
      }
   }

   private class Reloader extends Thread {
      private Reloader() {
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      Reloader(Object x1) {
         this();
      }
   }

   private class ViewportScroller implements MouseListener, MouseMotionListener {
      private final JViewport viewport;
      private int startPosX = 0;
      private int startPosY = 0;

      ViewportScroller(JViewport vp) {
         this.viewport = vp;
      }

      public synchronized void mouseDragged(MouseEvent e) {
         if (this.viewport != null) {
            int x = e.getX();
            int y = e.getY();
            int xmove = x - this.startPosX;
            int ymove = y - this.startPosY;
            int viewWidth = this.viewport.getExtentSize().width;
            int viewHeight = this.viewport.getExtentSize().height;
            int imageWidth = this.viewport.getViewSize().width;
            int imageHeight = this.viewport.getViewSize().height;
            Point viewPoint = this.viewport.getViewPosition();
            int viewX = Math.max(0, Math.min(imageWidth - viewWidth, viewPoint.x - xmove));
            int viewY = Math.max(0, Math.min(imageHeight - viewHeight, viewPoint.y - ymove));
            this.viewport.setViewPosition(new Point(viewX, viewY));
            this.startPosX = x;
            this.startPosY = y;
         }
      }

      public void mouseMoved(MouseEvent e) {
      }

      public void mousePressed(MouseEvent e) {
         this.startPosX = e.getX();
         this.startPosY = e.getY();
      }

      public void mouseExited(MouseEvent e) {
      }

      public void mouseEntered(MouseEvent e) {
      }

      public void mouseClicked(MouseEvent e) {
      }

      public void mouseReleased(MouseEvent e) {
      }
   }
}
