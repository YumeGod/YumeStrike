package org.apache.batik.apps.svgbrowser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.event.MouseInputAdapter;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.JGVTComponent;
import org.apache.batik.swing.gvt.Overlay;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.resources.ResourceManager;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class ThumbnailDialog extends JDialog {
   protected static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.ThumbnailDialog";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.ThumbnailDialog", Locale.getDefault());
   protected static ResourceManager resources;
   protected JSVGCanvas svgCanvas;
   protected JGVTComponent svgThumbnailCanvas;
   protected boolean documentChanged;
   protected AreaOfInterestOverlay overlay;
   protected AreaOfInterestListener aoiListener;
   protected boolean interactionEnabled = true;

   public ThumbnailDialog(Frame var1, JSVGCanvas var2) {
      super(var1, resources.getString("Dialog.title"));
      this.addWindowListener(new ThumbnailListener());
      this.svgCanvas = var2;
      var2.addGVTTreeRendererListener(new ThumbnailGVTListener());
      var2.addSVGDocumentLoaderListener(new ThumbnailDocumentListener());
      var2.addComponentListener(new ThumbnailCanvasComponentListener());
      this.svgThumbnailCanvas = new JGVTComponent();
      this.overlay = new AreaOfInterestOverlay();
      this.svgThumbnailCanvas.getOverlays().add(this.overlay);
      this.svgThumbnailCanvas.setPreferredSize(new Dimension(150, 150));
      this.svgThumbnailCanvas.addComponentListener(new ThumbnailComponentListener());
      this.aoiListener = new AreaOfInterestListener();
      this.svgThumbnailCanvas.addMouseListener(this.aoiListener);
      this.svgThumbnailCanvas.addMouseMotionListener(this.aoiListener);
      this.getContentPane().add(this.svgThumbnailCanvas, "Center");
   }

   public void setInteractionEnabled(boolean var1) {
      if (var1 != this.interactionEnabled) {
         this.interactionEnabled = var1;
         if (var1) {
            this.svgThumbnailCanvas.addMouseListener(this.aoiListener);
            this.svgThumbnailCanvas.addMouseMotionListener(this.aoiListener);
         } else {
            this.svgThumbnailCanvas.removeMouseListener(this.aoiListener);
            this.svgThumbnailCanvas.removeMouseMotionListener(this.aoiListener);
         }

      }
   }

   public boolean getInteractionEnabled() {
      return this.interactionEnabled;
   }

   protected void updateThumbnailGraphicsNode() {
      this.svgThumbnailCanvas.setGraphicsNode(this.svgCanvas.getGraphicsNode());
      this.updateThumbnailRenderingTransform();
   }

   protected CanvasGraphicsNode getCanvasGraphicsNode(GraphicsNode var1) {
      if (!(var1 instanceof CompositeGraphicsNode)) {
         return null;
      } else {
         CompositeGraphicsNode var2 = (CompositeGraphicsNode)var1;
         List var3 = var2.getChildren();
         if (var3.size() == 0) {
            return null;
         } else {
            var1 = (GraphicsNode)var2.getChildren().get(0);
            return !(var1 instanceof CanvasGraphicsNode) ? null : (CanvasGraphicsNode)var1;
         }
      }
   }

   protected void updateThumbnailRenderingTransform() {
      SVGDocument var1 = this.svgCanvas.getSVGDocument();
      if (var1 != null) {
         SVGSVGElement var2 = var1.getRootElement();
         Dimension var3 = this.svgThumbnailCanvas.getSize();
         String var4 = var2.getAttributeNS((String)null, "viewBox");
         AffineTransform var5;
         if (var4.length() != 0) {
            String var6 = var2.getAttributeNS((String)null, "preserveAspectRatio");
            var5 = ViewBox.getPreserveAspectRatioTransform(var2, (String)var4, (String)var6, (float)var3.width, (float)var3.height, (BridgeContext)null);
         } else {
            Dimension2D var14 = this.svgCanvas.getSVGDocumentSize();
            double var7 = (double)var3.width / var14.getWidth();
            double var9 = (double)var3.height / var14.getHeight();
            double var11 = Math.min(var7, var9);
            var5 = AffineTransform.getScaleInstance(var11, var11);
         }

         GraphicsNode var15 = this.svgCanvas.getGraphicsNode();
         CanvasGraphicsNode var16 = this.getCanvasGraphicsNode(var15);
         if (var16 != null) {
            AffineTransform var8 = var16.getViewingTransform();
            if (var8 != null && !var8.isIdentity()) {
               try {
                  AffineTransform var17 = var8.createInverse();
                  var5.concatenate(var17);
               } catch (NoninvertibleTransformException var13) {
               }
            }
         }

         this.svgThumbnailCanvas.setRenderingTransform(var5);
         this.overlay.synchronizeAreaOfInterest();
      }

   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected class AreaOfInterestOverlay implements Overlay {
      protected Shape s;
      protected AffineTransform at;
      protected AffineTransform paintingTransform = new AffineTransform();

      public boolean contains(int var1, int var2) {
         return this.s != null ? this.s.contains((double)var1, (double)var2) : false;
      }

      public AffineTransform getOverlayTransform() {
         return this.at;
      }

      public void setPaintingTransform(AffineTransform var1) {
         this.paintingTransform = var1;
      }

      public AffineTransform getPaintingTransform() {
         return this.paintingTransform;
      }

      public void synchronizeAreaOfInterest() {
         this.paintingTransform = new AffineTransform();
         Dimension var1 = ThumbnailDialog.this.svgCanvas.getSize();
         this.s = new Rectangle2D.Float(0.0F, 0.0F, (float)var1.width, (float)var1.height);

         try {
            this.at = ThumbnailDialog.this.svgCanvas.getRenderingTransform().createInverse();
            this.at.preConcatenate(ThumbnailDialog.this.svgThumbnailCanvas.getRenderingTransform());
            this.s = this.at.createTransformedShape(this.s);
         } catch (NoninvertibleTransformException var3) {
            var1 = ThumbnailDialog.this.svgThumbnailCanvas.getSize();
            this.s = new Rectangle2D.Float(0.0F, 0.0F, (float)var1.width, (float)var1.height);
         }

      }

      public void paint(Graphics var1) {
         if (this.s != null) {
            Graphics2D var2 = (Graphics2D)var1;
            var2.transform(this.paintingTransform);
            var2.setColor(new Color(255, 255, 255, 128));
            var2.fill(this.s);
            var2.setColor(Color.black);
            var2.setStroke(new BasicStroke());
            var2.draw(this.s);
         }

      }
   }

   protected class ThumbnailCanvasComponentListener extends ComponentAdapter {
      public void componentResized(ComponentEvent var1) {
         ThumbnailDialog.this.updateThumbnailRenderingTransform();
      }
   }

   protected class ThumbnailComponentListener extends ComponentAdapter {
      public void componentResized(ComponentEvent var1) {
         ThumbnailDialog.this.updateThumbnailRenderingTransform();
      }
   }

   protected class ThumbnailListener extends WindowAdapter {
      public void windowOpened(WindowEvent var1) {
         ThumbnailDialog.this.updateThumbnailGraphicsNode();
      }
   }

   protected class ThumbnailGVTListener extends GVTTreeRendererAdapter {
      public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
         if (ThumbnailDialog.this.documentChanged) {
            ThumbnailDialog.this.updateThumbnailGraphicsNode();
            ThumbnailDialog.this.documentChanged = false;
         } else {
            ThumbnailDialog.this.overlay.synchronizeAreaOfInterest();
            ThumbnailDialog.this.svgThumbnailCanvas.repaint();
         }

      }

      public void gvtRenderingCancelled(GVTTreeRendererEvent var1) {
         if (ThumbnailDialog.this.documentChanged) {
            ThumbnailDialog.this.svgThumbnailCanvas.setGraphicsNode((GraphicsNode)null);
            ThumbnailDialog.this.svgThumbnailCanvas.setRenderingTransform(new AffineTransform());
         }

      }

      public void gvtRenderingFailed(GVTTreeRendererEvent var1) {
         if (ThumbnailDialog.this.documentChanged) {
            ThumbnailDialog.this.svgThumbnailCanvas.setGraphicsNode((GraphicsNode)null);
            ThumbnailDialog.this.svgThumbnailCanvas.setRenderingTransform(new AffineTransform());
         }

      }
   }

   protected class AreaOfInterestListener extends MouseInputAdapter {
      protected int sx;
      protected int sy;
      protected boolean in;

      public void mousePressed(MouseEvent var1) {
         this.sx = var1.getX();
         this.sy = var1.getY();
         this.in = ThumbnailDialog.this.overlay.contains(this.sx, this.sy);
         ThumbnailDialog.this.overlay.setPaintingTransform(new AffineTransform());
      }

      public void mouseDragged(MouseEvent var1) {
         if (this.in) {
            int var2 = var1.getX() - this.sx;
            int var3 = var1.getY() - this.sy;
            ThumbnailDialog.this.overlay.setPaintingTransform(AffineTransform.getTranslateInstance((double)var2, (double)var3));
            ThumbnailDialog.this.svgThumbnailCanvas.repaint();
         }

      }

      public void mouseReleased(MouseEvent var1) {
         if (this.in) {
            this.in = false;
            int var2 = var1.getX() - this.sx;
            int var3 = var1.getY() - this.sy;
            AffineTransform var4 = ThumbnailDialog.this.overlay.getOverlayTransform();
            Point2D.Float var5 = new Point2D.Float(0.0F, 0.0F);
            Point2D.Float var6 = new Point2D.Float((float)var2, (float)var3);

            try {
               var4.inverseTransform(var5, var5);
               var4.inverseTransform(var6, var6);
               double var7 = var5.getX() - var6.getX();
               double var9 = var5.getY() - var6.getY();
               var4 = ThumbnailDialog.this.svgCanvas.getRenderingTransform();
               var4.preConcatenate(AffineTransform.getTranslateInstance(var7, var9));
               ThumbnailDialog.this.svgCanvas.setRenderingTransform(var4);
            } catch (NoninvertibleTransformException var11) {
            }
         }

      }
   }

   protected class ThumbnailDocumentListener extends SVGDocumentLoaderAdapter {
      public void documentLoadingStarted(SVGDocumentLoaderEvent var1) {
         ThumbnailDialog.this.documentChanged = true;
      }
   }
}
