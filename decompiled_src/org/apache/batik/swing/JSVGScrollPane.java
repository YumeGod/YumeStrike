package org.apache.batik.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.gvt.JGVTComponentListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class JSVGScrollPane extends JPanel {
   protected JSVGCanvas canvas;
   protected JPanel horizontalPanel;
   protected JScrollBar vertical;
   protected JScrollBar horizontal;
   protected Component cornerBox;
   protected boolean scrollbarsAlwaysVisible = false;
   protected SBListener hsbListener;
   protected SBListener vsbListener;
   protected Rectangle2D viewBox = null;
   protected boolean ignoreScrollChange = false;

   public JSVGScrollPane(JSVGCanvas var1) {
      this.canvas = var1;
      var1.setRecenterOnResize(false);
      this.vertical = new JScrollBar(1, 0, 0, 0, 0);
      this.horizontal = new JScrollBar(0, 0, 0, 0, 0);
      this.horizontalPanel = new JPanel(new BorderLayout());
      this.horizontalPanel.add(this.horizontal, "Center");
      this.cornerBox = Box.createRigidArea(new Dimension(this.vertical.getPreferredSize().width, this.horizontal.getPreferredSize().height));
      this.horizontalPanel.add(this.cornerBox, "East");
      this.hsbListener = this.createScrollBarListener(false);
      this.horizontal.getModel().addChangeListener(this.hsbListener);
      this.vsbListener = this.createScrollBarListener(true);
      this.vertical.getModel().addChangeListener(this.vsbListener);
      this.updateScrollbarState(false, false);
      this.setLayout(new BorderLayout());
      this.add(var1, "Center");
      this.add(this.vertical, "East");
      this.add(this.horizontalPanel, "South");
      var1.addSVGDocumentLoaderListener(this.createLoadListener());
      ScrollListener var2 = this.createScrollListener();
      this.addComponentListener(var2);
      var1.addGVTTreeRendererListener(var2);
      var1.addJGVTComponentListener(var2);
      var1.addGVTTreeBuilderListener(var2);
      var1.addUpdateManagerListener(var2);
   }

   public boolean getScrollbarsAlwaysVisible() {
      return this.scrollbarsAlwaysVisible;
   }

   public void setScrollbarsAlwaysVisible(boolean var1) {
      this.scrollbarsAlwaysVisible = var1;
      this.resizeScrollBars();
   }

   protected SBListener createScrollBarListener(boolean var1) {
      return new SBListener(var1);
   }

   protected ScrollListener createScrollListener() {
      return new ScrollListener();
   }

   protected SVGDocumentLoaderListener createLoadListener() {
      return new SVGScrollDocumentLoaderListener();
   }

   public JSVGCanvas getCanvas() {
      return this.canvas;
   }

   public void reset() {
      this.viewBox = null;
      this.updateScrollbarState(false, false);
      this.revalidate();
   }

   protected void setScrollPosition() {
      this.checkAndSetViewBoxRect();
      if (this.viewBox != null) {
         AffineTransform var1 = this.canvas.getRenderingTransform();
         AffineTransform var2 = this.canvas.getViewBoxTransform();
         if (var1 == null) {
            var1 = new AffineTransform();
         }

         if (var2 == null) {
            var2 = new AffineTransform();
         }

         Rectangle var3 = var2.createTransformedShape(this.viewBox).getBounds();
         int var4 = 0;
         int var5 = 0;
         if (var3.x < 0) {
            var4 -= var3.x;
         }

         if (var3.y < 0) {
            var5 -= var3.y;
         }

         int var6 = this.horizontal.getValue() - var4;
         int var7 = this.vertical.getValue() - var5;
         var1.preConcatenate(AffineTransform.getTranslateInstance((double)(-var6), (double)(-var7)));
         this.canvas.setRenderingTransform(var1);
      }
   }

   protected void resizeScrollBars() {
      this.ignoreScrollChange = true;
      this.checkAndSetViewBoxRect();
      if (this.viewBox != null) {
         AffineTransform var1 = this.canvas.getViewBoxTransform();
         if (var1 == null) {
            var1 = new AffineTransform();
         }

         Rectangle var2 = var1.createTransformedShape(this.viewBox).getBounds();
         int var3 = var2.width;
         int var4 = var2.height;
         int var5 = 0;
         int var6 = 0;
         if (var2.x > 0) {
            var3 += var2.x;
         } else {
            var5 -= var2.x;
         }

         if (var2.y > 0) {
            var4 += var2.y;
         } else {
            var6 -= var2.y;
         }

         Dimension var7 = this.updateScrollbarVisibility(var5, var6, var3, var4);
         this.vertical.setValues(var6, var7.height, 0, var4);
         this.horizontal.setValues(var5, var7.width, 0, var3);
         this.vertical.setBlockIncrement((int)(0.9F * (float)var7.height));
         this.horizontal.setBlockIncrement((int)(0.9F * (float)var7.width));
         this.vertical.setUnitIncrement((int)(0.2F * (float)var7.height));
         this.horizontal.setUnitIncrement((int)(0.2F * (float)var7.width));
         this.doLayout();
         this.horizontalPanel.doLayout();
         this.horizontal.doLayout();
         this.vertical.doLayout();
         this.ignoreScrollChange = false;
      }
   }

   protected Dimension updateScrollbarVisibility(int var1, int var2, int var3, int var4) {
      Dimension var5 = this.canvas.getSize();
      int var6 = var5.width;
      int var7 = var5.width;
      int var8 = var5.height;
      int var9 = var5.height;
      if (this.vertical.isVisible()) {
         var6 += this.vertical.getPreferredSize().width;
      } else {
         var7 -= this.vertical.getPreferredSize().width;
      }

      if (this.horizontalPanel.isVisible()) {
         var8 += this.horizontal.getPreferredSize().height;
      } else {
         var9 -= this.horizontal.getPreferredSize().height;
      }

      Dimension var12 = new Dimension();
      boolean var10;
      boolean var11;
      if (this.scrollbarsAlwaysVisible) {
         var10 = var3 > var7;
         var11 = var4 > var9;
         var12.width = var7;
         var12.height = var9;
      } else {
         var10 = var3 > var6 || var1 != 0;
         var11 = var4 > var8 || var2 != 0;
         if (var11 && !var10) {
            var10 = var3 > var7;
         } else if (var10 && !var11) {
            var11 = var4 > var9;
         }

         var12.width = var10 ? var7 : var6;
         var12.height = var11 ? var9 : var8;
      }

      this.updateScrollbarState(var10, var11);
      return var12;
   }

   protected void updateScrollbarState(boolean var1, boolean var2) {
      this.horizontal.setEnabled(var1);
      this.vertical.setEnabled(var2);
      if (this.scrollbarsAlwaysVisible) {
         this.horizontalPanel.setVisible(true);
         this.vertical.setVisible(true);
         this.cornerBox.setVisible(true);
      } else {
         this.horizontalPanel.setVisible(var1);
         this.vertical.setVisible(var2);
         this.cornerBox.setVisible(var1 && var2);
      }

   }

   protected void checkAndSetViewBoxRect() {
      if (this.viewBox == null) {
         this.viewBox = this.getViewBoxRect();
      }
   }

   protected Rectangle2D getViewBoxRect() {
      SVGDocument var1 = this.canvas.getSVGDocument();
      if (var1 == null) {
         return null;
      } else {
         SVGSVGElement var2 = var1.getRootElement();
         if (var2 == null) {
            return null;
         } else {
            String var3 = var2.getAttributeNS((String)null, "viewBox");
            if (var3.length() != 0) {
               float[] var6 = ViewBox.parseViewBoxAttribute(var2, var3, (BridgeContext)null);
               return new Rectangle2D.Float(var6[0], var6[1], var6[2], var6[3]);
            } else {
               GraphicsNode var4 = this.canvas.getGraphicsNode();
               if (var4 == null) {
                  return null;
               } else {
                  Rectangle2D var5 = var4.getBounds();
                  return var5 == null ? null : (Rectangle2D)var5.clone();
               }
            }
         }
      }
   }

   public void scaleChange(float var1) {
   }

   protected class ScrollListener extends ComponentAdapter implements JGVTComponentListener, GVTTreeBuilderListener, GVTTreeRendererListener, UpdateManagerListener {
      protected boolean isReady = false;

      public void componentTransformChanged(ComponentEvent var1) {
         if (this.isReady) {
            JSVGScrollPane.this.resizeScrollBars();
         }

      }

      public void componentResized(ComponentEvent var1) {
         if (this.isReady) {
            JSVGScrollPane.this.resizeScrollBars();
         }

      }

      public void gvtBuildStarted(GVTTreeBuilderEvent var1) {
         this.isReady = false;
         JSVGScrollPane.this.updateScrollbarState(false, false);
      }

      public void gvtBuildCompleted(GVTTreeBuilderEvent var1) {
         this.isReady = true;
         JSVGScrollPane.this.viewBox = null;
      }

      public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
         if (JSVGScrollPane.this.viewBox == null) {
            JSVGScrollPane.this.resizeScrollBars();
         } else {
            Rectangle2D var2 = JSVGScrollPane.this.getViewBoxRect();
            if (var2.getX() != JSVGScrollPane.this.viewBox.getX() || var2.getY() != JSVGScrollPane.this.viewBox.getY() || var2.getWidth() != JSVGScrollPane.this.viewBox.getWidth() || var2.getHeight() != JSVGScrollPane.this.viewBox.getHeight()) {
               JSVGScrollPane.this.viewBox = var2;
               JSVGScrollPane.this.resizeScrollBars();
            }

         }
      }

      public void updateCompleted(UpdateManagerEvent var1) {
         if (JSVGScrollPane.this.viewBox == null) {
            JSVGScrollPane.this.resizeScrollBars();
         } else {
            Rectangle2D var2 = JSVGScrollPane.this.getViewBoxRect();
            if (var2.getX() != JSVGScrollPane.this.viewBox.getX() || var2.getY() != JSVGScrollPane.this.viewBox.getY() || var2.getWidth() != JSVGScrollPane.this.viewBox.getWidth() || var2.getHeight() != JSVGScrollPane.this.viewBox.getHeight()) {
               JSVGScrollPane.this.viewBox = var2;
               JSVGScrollPane.this.resizeScrollBars();
            }

         }
      }

      public void gvtBuildCancelled(GVTTreeBuilderEvent var1) {
      }

      public void gvtBuildFailed(GVTTreeBuilderEvent var1) {
      }

      public void gvtRenderingPrepare(GVTTreeRendererEvent var1) {
      }

      public void gvtRenderingStarted(GVTTreeRendererEvent var1) {
      }

      public void gvtRenderingCancelled(GVTTreeRendererEvent var1) {
      }

      public void gvtRenderingFailed(GVTTreeRendererEvent var1) {
      }

      public void managerStarted(UpdateManagerEvent var1) {
      }

      public void managerSuspended(UpdateManagerEvent var1) {
      }

      public void managerResumed(UpdateManagerEvent var1) {
      }

      public void managerStopped(UpdateManagerEvent var1) {
      }

      public void updateStarted(UpdateManagerEvent var1) {
      }

      public void updateFailed(UpdateManagerEvent var1) {
      }
   }

   protected class SBListener implements ChangeListener {
      protected boolean inDrag = false;
      protected int startValue;
      protected boolean isVertical;

      public SBListener(boolean var2) {
         this.isVertical = var2;
      }

      public synchronized void stateChanged(ChangeEvent var1) {
         if (!JSVGScrollPane.this.ignoreScrollChange) {
            Object var2 = var1.getSource();
            if (var2 instanceof BoundedRangeModel) {
               int var3 = this.isVertical ? JSVGScrollPane.this.vertical.getValue() : JSVGScrollPane.this.horizontal.getValue();
               BoundedRangeModel var4 = (BoundedRangeModel)var2;
               if (var4.getValueIsAdjusting()) {
                  if (!this.inDrag) {
                     this.inDrag = true;
                     this.startValue = var3;
                  } else {
                     AffineTransform var5;
                     if (this.isVertical) {
                        var5 = AffineTransform.getTranslateInstance(0.0, (double)(this.startValue - var3));
                     } else {
                        var5 = AffineTransform.getTranslateInstance((double)(this.startValue - var3), 0.0);
                     }

                     JSVGScrollPane.this.canvas.setPaintingTransform(var5);
                  }
               } else {
                  if (this.inDrag) {
                     this.inDrag = false;
                     if (var3 == this.startValue) {
                        JSVGScrollPane.this.canvas.setPaintingTransform(new AffineTransform());
                        return;
                     }
                  }

                  JSVGScrollPane.this.setScrollPosition();
               }

            }
         }
      }
   }

   class SVGScrollDocumentLoaderListener extends SVGDocumentLoaderAdapter {
      public void documentLoadingCompleted(SVGDocumentLoaderEvent var1) {
         NodeEventTarget var2 = (NodeEventTarget)var1.getSVGDocument().getRootElement();
         var2.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGZoom", new EventListener() {
            public void handleEvent(Event var1) {
               if (var1.getTarget() instanceof SVGSVGElement) {
                  SVGSVGElement var2 = (SVGSVGElement)var1.getTarget();
                  JSVGScrollPane.this.scaleChange(var2.getCurrentScale());
               }
            }
         }, false, (Object)null);
      }
   }
}
