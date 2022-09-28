package org.apache.batik.swing.svg;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.BridgeExtension;
import org.apache.batik.bridge.DefaultScriptSecurity;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.ExternalResourceSecurity;
import org.apache.batik.bridge.RelaxedExternalResourceSecurity;
import org.apache.batik.bridge.ScriptSecurity;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.swing.gvt.AbstractJGVTComponent;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.JGVTComponent;
import org.apache.batik.swing.gvt.JGVTComponentListener;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.RunnableQueue;
import org.apache.batik.util.SVGFeatureStrings;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class AbstractJSVGComponent extends JGVTComponent {
   public static final int AUTODETECT = 0;
   public static final int ALWAYS_DYNAMIC = 1;
   public static final int ALWAYS_STATIC = 2;
   public static final int ALWAYS_INTERACTIVE = 3;
   public static final String SCRIPT_ALERT = "script.alert";
   public static final String SCRIPT_PROMPT = "script.prompt";
   public static final String SCRIPT_CONFIRM = "script.confirm";
   public static final String BROKEN_LINK_TITLE = "broken.link.title";
   protected SVGDocumentLoader documentLoader;
   protected SVGDocumentLoader nextDocumentLoader;
   protected DocumentLoader loader;
   protected GVTTreeBuilder gvtTreeBuilder;
   protected GVTTreeBuilder nextGVTTreeBuilder;
   protected SVGLoadEventDispatcher svgLoadEventDispatcher;
   protected UpdateManager updateManager;
   protected UpdateManager nextUpdateManager;
   protected SVGDocument svgDocument;
   protected List svgDocumentLoaderListeners;
   protected List gvtTreeBuilderListeners;
   protected List svgLoadEventDispatcherListeners;
   protected List linkActivationListeners;
   protected List updateManagerListeners;
   protected UserAgent userAgent;
   protected SVGUserAgent svgUserAgent;
   protected BridgeContext bridgeContext;
   protected String fragmentIdentifier;
   protected boolean isDynamicDocument;
   protected boolean isInteractiveDocument;
   protected boolean selfCallingDisableInteractions;
   protected boolean userSetDisableInteractions;
   protected int documentState;
   protected Dimension prevComponentSize;
   protected Runnable afterStopRunnable;
   protected SVGUpdateOverlay updateOverlay;
   protected boolean recenterOnResize;
   protected AffineTransform viewingTransform;
   protected int animationLimitingMode;
   protected float animationLimitingAmount;
   protected JSVGComponentListener jsvgComponentListener;
   protected static final Set FEATURES = new HashSet();
   // $FF: synthetic field
   static Class class$org$apache$batik$swing$svg$AbstractJSVGComponent;

   public AbstractJSVGComponent() {
      this((SVGUserAgent)null, false, false);
   }

   public AbstractJSVGComponent(SVGUserAgent var1, boolean var2, boolean var3) {
      super(var2, var3);
      this.svgDocumentLoaderListeners = new LinkedList();
      this.gvtTreeBuilderListeners = new LinkedList();
      this.svgLoadEventDispatcherListeners = new LinkedList();
      this.linkActivationListeners = new LinkedList();
      this.updateManagerListeners = new LinkedList();
      this.selfCallingDisableInteractions = false;
      this.userSetDisableInteractions = false;
      this.afterStopRunnable = null;
      this.recenterOnResize = true;
      this.viewingTransform = null;
      this.jsvgComponentListener = new JSVGComponentListener();
      this.svgUserAgent = var1;
      this.userAgent = new BridgeUserAgentWrapper(this.createUserAgent());
      this.addSVGDocumentLoaderListener((SVGListener)this.listener);
      this.addGVTTreeBuilderListener((SVGListener)this.listener);
      this.addSVGLoadEventDispatcherListener((SVGListener)this.listener);
      if (this.updateOverlay != null) {
         this.getOverlays().add(this.updateOverlay);
      }

   }

   public void dispose() {
      this.setSVGDocument((SVGDocument)null);
   }

   public void setDisableInteractions(boolean var1) {
      super.setDisableInteractions(var1);
      if (!this.selfCallingDisableInteractions) {
         this.userSetDisableInteractions = true;
      }

   }

   public void clearUserSetDisableInteractions() {
      this.userSetDisableInteractions = false;
      this.updateZoomAndPanEnable(this.svgDocument);
   }

   public void updateZoomAndPanEnable(Document var1) {
      if (!this.userSetDisableInteractions) {
         if (var1 != null) {
            try {
               Element var2 = var1.getDocumentElement();
               String var3 = var2.getAttributeNS((String)null, "zoomAndPan");
               boolean var4 = "magnify".equals(var3);
               this.selfCallingDisableInteractions = true;
               this.setDisableInteractions(!var4);
            } finally {
               this.selfCallingDisableInteractions = false;
            }

         }
      }
   }

   public boolean getRecenterOnResize() {
      return this.recenterOnResize;
   }

   public void setRecenterOnResize(boolean var1) {
      this.recenterOnResize = var1;
   }

   public boolean isDynamic() {
      return this.isDynamicDocument;
   }

   public boolean isInteractive() {
      return this.isInteractiveDocument;
   }

   public void setDocumentState(int var1) {
      this.documentState = var1;
   }

   public UpdateManager getUpdateManager() {
      if (this.svgLoadEventDispatcher != null) {
         return this.svgLoadEventDispatcher.getUpdateManager();
      } else {
         return this.nextUpdateManager != null ? this.nextUpdateManager : this.updateManager;
      }
   }

   public void resumeProcessing() {
      if (this.updateManager != null) {
         this.updateManager.resume();
      }

   }

   public void suspendProcessing() {
      if (this.updateManager != null) {
         this.updateManager.suspend();
      }

   }

   public void stopProcessing() {
      this.nextDocumentLoader = null;
      this.nextGVTTreeBuilder = null;
      if (this.documentLoader != null) {
         this.documentLoader.halt();
      }

      if (this.gvtTreeBuilder != null) {
         this.gvtTreeBuilder.halt();
      }

      if (this.svgLoadEventDispatcher != null) {
         this.svgLoadEventDispatcher.halt();
      }

      if (this.nextUpdateManager != null) {
         this.nextUpdateManager.interrupt();
         this.nextUpdateManager = null;
      }

      if (this.updateManager != null) {
         this.updateManager.interrupt();
      }

      super.stopProcessing();
   }

   public void loadSVGDocument(String var1) {
      String var2 = null;
      if (this.svgDocument != null) {
         var2 = this.svgDocument.getURL();
      }

      final ParsedURL var3 = new ParsedURL(var2, var1);
      this.stopThenRun(new Runnable() {
         public void run() {
            String var1 = var3.toString();
            AbstractJSVGComponent.this.fragmentIdentifier = var3.getRef();
            AbstractJSVGComponent.this.loader = new DocumentLoader(AbstractJSVGComponent.this.userAgent);
            AbstractJSVGComponent.this.nextDocumentLoader = new SVGDocumentLoader(var1, AbstractJSVGComponent.this.loader);
            AbstractJSVGComponent.this.nextDocumentLoader.setPriority(1);
            Iterator var2 = AbstractJSVGComponent.this.svgDocumentLoaderListeners.iterator();

            while(var2.hasNext()) {
               AbstractJSVGComponent.this.nextDocumentLoader.addSVGDocumentLoaderListener((SVGDocumentLoaderListener)var2.next());
            }

            AbstractJSVGComponent.this.startDocumentLoader();
         }
      });
   }

   private void startDocumentLoader() {
      this.documentLoader = this.nextDocumentLoader;
      this.nextDocumentLoader = null;
      this.documentLoader.start();
   }

   public void setDocument(Document var1) {
      if (var1 != null && !(var1.getImplementation() instanceof SVGDOMImplementation)) {
         DOMImplementation var2 = SVGDOMImplementation.getDOMImplementation();
         Document var3 = DOMUtilities.deepCloneDocument(var1, var2);
         var1 = var3;
      }

      this.setSVGDocument((SVGDocument)var1);
   }

   public void setSVGDocument(final SVGDocument var1) {
      if (var1 != null && !(var1.getImplementation() instanceof SVGDOMImplementation)) {
         DOMImplementation var2 = SVGDOMImplementation.getDOMImplementation();
         Document var3 = DOMUtilities.deepCloneDocument(var1, var2);
         var1 = (SVGDocument)var3;
      }

      this.stopThenRun(new Runnable() {
         public void run() {
            AbstractJSVGComponent.this.installSVGDocument(var1);
         }
      });
   }

   protected void stopThenRun(Runnable var1) {
      if (this.afterStopRunnable != null) {
         this.afterStopRunnable = var1;
      } else {
         this.afterStopRunnable = var1;
         this.stopProcessing();
         if (this.documentLoader == null && this.gvtTreeBuilder == null && this.gvtTreeRenderer == null && this.svgLoadEventDispatcher == null && this.nextUpdateManager == null && this.updateManager == null) {
            Runnable var2 = this.afterStopRunnable;
            this.afterStopRunnable = null;
            var2.run();
         }

      }
   }

   protected void installSVGDocument(SVGDocument var1) {
      this.svgDocument = var1;
      if (this.bridgeContext != null) {
         this.bridgeContext.dispose();
         this.bridgeContext = null;
      }

      this.releaseRenderingReferences();
      if (var1 == null) {
         this.isDynamicDocument = false;
         this.isInteractiveDocument = false;
         this.disableInteractions = true;
         this.initialTransform = new AffineTransform();
         this.setRenderingTransform(this.initialTransform, false);
         Rectangle var3 = this.getRenderRect();
         this.repaint(var3.x, var3.y, var3.width, var3.height);
      } else {
         this.bridgeContext = this.createBridgeContext((SVGOMDocument)var1);
         switch (this.documentState) {
            case 0:
               this.isDynamicDocument = this.bridgeContext.isDynamicDocument(var1);
               this.isInteractiveDocument = this.isDynamicDocument || this.bridgeContext.isInteractiveDocument(var1);
               break;
            case 1:
               this.isDynamicDocument = true;
               this.isInteractiveDocument = true;
               break;
            case 2:
               this.isDynamicDocument = false;
               this.isInteractiveDocument = false;
               break;
            case 3:
               this.isDynamicDocument = false;
               this.isInteractiveDocument = true;
         }

         if (this.isInteractiveDocument) {
            if (this.isDynamicDocument) {
               this.bridgeContext.setDynamicState(2);
            } else {
               this.bridgeContext.setDynamicState(1);
            }
         }

         this.setBridgeContextAnimationLimitingMode();
         this.updateZoomAndPanEnable(var1);
         this.nextGVTTreeBuilder = new GVTTreeBuilder(var1, this.bridgeContext);
         this.nextGVTTreeBuilder.setPriority(1);
         Iterator var2 = this.gvtTreeBuilderListeners.iterator();

         while(var2.hasNext()) {
            this.nextGVTTreeBuilder.addGVTTreeBuilderListener((GVTTreeBuilderListener)var2.next());
         }

         this.initializeEventHandling();
         if (this.gvtTreeBuilder == null && this.documentLoader == null && this.gvtTreeRenderer == null && this.svgLoadEventDispatcher == null && this.updateManager == null) {
            this.startGVTTreeBuilder();
         }

      }
   }

   protected void startGVTTreeBuilder() {
      this.gvtTreeBuilder = this.nextGVTTreeBuilder;
      this.nextGVTTreeBuilder = null;
      this.gvtTreeBuilder.start();
   }

   public SVGDocument getSVGDocument() {
      return this.svgDocument;
   }

   public Dimension2D getSVGDocumentSize() {
      return this.bridgeContext.getDocumentSize();
   }

   public String getFragmentIdentifier() {
      return this.fragmentIdentifier;
   }

   public void setFragmentIdentifier(String var1) {
      this.fragmentIdentifier = var1;
      if (this.computeRenderingTransform()) {
         this.scheduleGVTRendering();
      }

   }

   public void flushImageCache() {
      ImageTagRegistry var1 = ImageTagRegistry.getRegistry();
      var1.flushCache();
   }

   public void setGraphicsNode(GraphicsNode var1, boolean var2) {
      Dimension2D var3 = this.bridgeContext.getDocumentSize();
      Dimension var4 = new Dimension((int)var3.getWidth(), (int)var3.getHeight());
      this.setMySize(var4);
      SVGSVGElement var5 = this.svgDocument.getRootElement();
      this.prevComponentSize = this.getSize();
      AffineTransform var6 = this.calculateViewingTransform(this.fragmentIdentifier, var5);
      CanvasGraphicsNode var7 = this.getCanvasGraphicsNode(var1);
      var7.setViewingTransform(var6);
      this.viewingTransform = null;
      this.initialTransform = new AffineTransform();
      this.setRenderingTransform(this.initialTransform, false);
      this.jsvgComponentListener.updateMatrix(this.initialTransform);
      this.addJGVTComponentListener(this.jsvgComponentListener);
      this.addComponentListener(this.jsvgComponentListener);
      super.setGraphicsNode(var1, var2);
   }

   protected BridgeContext createBridgeContext(SVGOMDocument var1) {
      if (this.loader == null) {
         this.loader = new DocumentLoader(this.userAgent);
      }

      Object var2;
      if (var1.isSVG12()) {
         var2 = new SVG12BridgeContext(this.userAgent, this.loader);
      } else {
         var2 = new BridgeContext(this.userAgent, this.loader);
      }

      return (BridgeContext)var2;
   }

   protected void startSVGLoadEventDispatcher(GraphicsNode var1) {
      UpdateManager var2 = new UpdateManager(this.bridgeContext, var1, this.svgDocument);
      this.svgLoadEventDispatcher = new SVGLoadEventDispatcher(var1, this.svgDocument, this.bridgeContext, var2);
      Iterator var3 = this.svgLoadEventDispatcherListeners.iterator();

      while(var3.hasNext()) {
         this.svgLoadEventDispatcher.addSVGLoadEventDispatcherListener((SVGLoadEventDispatcherListener)var3.next());
      }

      this.svgLoadEventDispatcher.start();
   }

   protected ImageRenderer createImageRenderer() {
      return this.isDynamicDocument ? this.rendererFactory.createDynamicImageRenderer() : this.rendererFactory.createStaticImageRenderer();
   }

   public CanvasGraphicsNode getCanvasGraphicsNode() {
      return this.getCanvasGraphicsNode(this.gvtRoot);
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
            var1 = (GraphicsNode)var3.get(0);
            return !(var1 instanceof CanvasGraphicsNode) ? null : (CanvasGraphicsNode)var1;
         }
      }
   }

   public AffineTransform getViewingTransform() {
      synchronized(this) {
         AffineTransform var1 = this.viewingTransform;
         if (var1 == null) {
            CanvasGraphicsNode var3 = this.getCanvasGraphicsNode();
            if (var3 != null) {
               var1 = var3.getViewingTransform();
            }
         }

         return var1;
      }
   }

   public AffineTransform getViewBoxTransform() {
      AffineTransform var1 = this.getRenderingTransform();
      if (var1 == null) {
         var1 = new AffineTransform();
      } else {
         var1 = new AffineTransform(var1);
      }

      AffineTransform var2 = this.getViewingTransform();
      if (var2 != null) {
         var1.concatenate(var2);
      }

      return var1;
   }

   protected boolean computeRenderingTransform() {
      if (this.svgDocument != null && this.gvtRoot != null) {
         boolean var1 = this.updateRenderingTransform();
         this.initialTransform = new AffineTransform();
         if (!this.initialTransform.equals(this.getRenderingTransform())) {
            this.setRenderingTransform(this.initialTransform, false);
            var1 = true;
         }

         return var1;
      } else {
         return false;
      }
   }

   protected AffineTransform calculateViewingTransform(String var1, SVGSVGElement var2) {
      Dimension var3 = this.getSize();
      if (var3.width < 1) {
         var3.width = 1;
      }

      if (var3.height < 1) {
         var3.height = 1;
      }

      return ViewBox.getViewTransform(var1, var2, (float)var3.width, (float)var3.height, this.bridgeContext);
   }

   protected boolean updateRenderingTransform() {
      if (this.svgDocument != null && this.gvtRoot != null) {
         try {
            SVGSVGElement var1 = this.svgDocument.getRootElement();
            Dimension var2 = this.getSize();
            Dimension var3 = this.prevComponentSize;
            if (var3 == null) {
               var3 = var2;
            }

            this.prevComponentSize = var2;
            if (var2.width < 1) {
               var2.width = 1;
            }

            if (var2.height < 1) {
               var2.height = 1;
            }

            final AffineTransform var4 = this.calculateViewingTransform(this.fragmentIdentifier, var1);
            AffineTransform var5 = this.getViewingTransform();
            if (var4.equals(var5)) {
               return var3.width != var2.width || var3.height != var2.height;
            }

            if (!this.recenterOnResize) {
               return true;
            }

            Object var6 = new Point2D.Float((float)var3.width / 2.0F, (float)var3.height / 2.0F);
            AffineTransform var7 = this.getRenderingTransform();
            AffineTransform var8;
            if (var7 != null) {
               try {
                  var8 = var7.createInverse();
                  var6 = var8.transform((Point2D)var6, (Point2D)null);
               } catch (NoninvertibleTransformException var14) {
               }
            }

            if (var5 != null) {
               try {
                  var8 = var5.createInverse();
                  var6 = var8.transform((Point2D)var6, (Point2D)null);
               } catch (NoninvertibleTransformException var13) {
               }
            }

            if (var4 != null) {
               var6 = var4.transform((Point2D)var6, (Point2D)null);
            }

            if (var7 != null) {
               var6 = var7.transform((Point2D)var6, (Point2D)null);
            }

            float var16 = (float)((double)((float)var2.width / 2.0F) - ((Point2D)var6).getX());
            float var9 = (float)((double)((float)var2.height / 2.0F) - ((Point2D)var6).getY());
            var16 = (float)((int)(var16 < 0.0F ? (double)var16 - 0.5 : (double)var16 + 0.5));
            var9 = (float)((int)(var9 < 0.0F ? (double)var9 - 0.5 : (double)var9 + 0.5));
            if (var16 != 0.0F || var9 != 0.0F) {
               var7.preConcatenate(AffineTransform.getTranslateInstance((double)var16, (double)var9));
               this.setRenderingTransform(var7, false);
            }

            synchronized(this) {
               this.viewingTransform = var4;
            }

            Runnable var10 = new Runnable() {
               AffineTransform myAT = var4;
               CanvasGraphicsNode myCGN = AbstractJSVGComponent.this.getCanvasGraphicsNode();

               public void run() {
                  synchronized(AbstractJSVGComponent.this) {
                     this.myCGN.setViewingTransform(this.myAT);
                     if (AbstractJSVGComponent.this.viewingTransform == this.myAT) {
                        AbstractJSVGComponent.this.viewingTransform = null;
                     }

                  }
               }
            };
            UpdateManager var11 = this.getUpdateManager();
            if (var11 != null) {
               var11.getUpdateRunnableQueue().invokeLater(var10);
            } else {
               var10.run();
            }
         } catch (BridgeException var15) {
            this.userAgent.displayError(var15);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void renderGVTTree() {
      if (this.isInteractiveDocument && this.updateManager != null && this.updateManager.isRunning()) {
         Rectangle var1 = this.getRenderRect();
         if (this.gvtRoot != null && var1.width > 0 && var1.height > 0) {
            AffineTransform var2 = null;

            try {
               var2 = this.renderingTransform.createInverse();
            } catch (NoninvertibleTransformException var9) {
            }

            Object var3;
            if (var2 == null) {
               var3 = var1;
            } else {
               var3 = var2.createTransformedShape(var1);
            }

            RunnableQueue var4 = this.updateManager.getUpdateRunnableQueue();

            class UpdateRenderingRunnable implements Runnable {
               AffineTransform at;
               boolean doubleBuf;
               boolean clearPaintTrans;
               Shape aoi;
               int width;
               int height;
               boolean active;

               public UpdateRenderingRunnable(AffineTransform var2, boolean var3, boolean var4, Shape var5, int var6, int var7) {
                  this.updateInfo(var2, var3, var4, var5, var6, var7);
                  this.active = true;
               }

               public void updateInfo(AffineTransform var1, boolean var2, boolean var3, Shape var4, int var5, int var6) {
                  this.at = var1;
                  this.doubleBuf = var2;
                  this.clearPaintTrans = var3;
                  this.aoi = var4;
                  this.width = var5;
                  this.height = var6;
                  this.active = true;
               }

               public void deactivate() {
                  this.active = false;
               }

               public void run() {
                  if (this.active) {
                     AbstractJSVGComponent.this.updateManager.updateRendering(this.at, this.doubleBuf, this.clearPaintTrans, this.aoi, this.width, this.height);
                  }
               }
            }

            synchronized(var4.getIteratorLock()) {
               Iterator var6 = var4.iterator();

               while(true) {
                  if (!var6.hasNext()) {
                     break;
                  }

                  Object var7 = var6.next();
                  if (var7 instanceof UpdateRenderingRunnable) {
                     ((UpdateRenderingRunnable)var7).deactivate();
                  }
               }
            }

            var4.invokeLater(new UpdateRenderingRunnable(this.renderingTransform, this.doubleBufferedRendering, true, (Shape)var3, var1.width, var1.height));
         }
      } else {
         super.renderGVTTree();
      }
   }

   protected void handleException(Exception var1) {
      this.userAgent.displayError(var1);
   }

   public void addSVGDocumentLoaderListener(SVGDocumentLoaderListener var1) {
      this.svgDocumentLoaderListeners.add(var1);
   }

   public void removeSVGDocumentLoaderListener(SVGDocumentLoaderListener var1) {
      this.svgDocumentLoaderListeners.remove(var1);
   }

   public void addGVTTreeBuilderListener(GVTTreeBuilderListener var1) {
      this.gvtTreeBuilderListeners.add(var1);
   }

   public void removeGVTTreeBuilderListener(GVTTreeBuilderListener var1) {
      this.gvtTreeBuilderListeners.remove(var1);
   }

   public void addSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener var1) {
      this.svgLoadEventDispatcherListeners.add(var1);
   }

   public void removeSVGLoadEventDispatcherListener(SVGLoadEventDispatcherListener var1) {
      this.svgLoadEventDispatcherListeners.remove(var1);
   }

   public void addLinkActivationListener(LinkActivationListener var1) {
      this.linkActivationListeners.add(var1);
   }

   public void removeLinkActivationListener(LinkActivationListener var1) {
      this.linkActivationListeners.remove(var1);
   }

   public void addUpdateManagerListener(UpdateManagerListener var1) {
      this.updateManagerListeners.add(var1);
   }

   public void removeUpdateManagerListener(UpdateManagerListener var1) {
      this.updateManagerListeners.remove(var1);
   }

   public void showAlert(String var1) {
      JOptionPane.showMessageDialog(this, Messages.formatMessage("script.alert", new Object[]{var1}));
   }

   public String showPrompt(String var1) {
      return JOptionPane.showInputDialog(this, Messages.formatMessage("script.prompt", new Object[]{var1}));
   }

   public String showPrompt(String var1, String var2) {
      return (String)JOptionPane.showInputDialog(this, Messages.formatMessage("script.prompt", new Object[]{var1}), (String)null, -1, (Icon)null, (Object[])null, var2);
   }

   public boolean showConfirm(String var1) {
      return JOptionPane.showConfirmDialog(this, Messages.formatMessage("script.confirm", new Object[]{var1}), "Confirm", 0) == 0;
   }

   public void setMySize(Dimension var1) {
      this.setPreferredSize(var1);
      this.invalidate();
   }

   public void setAnimationLimitingNone() {
      this.animationLimitingMode = 0;
      if (this.bridgeContext != null) {
         this.setBridgeContextAnimationLimitingMode();
      }

   }

   public void setAnimationLimitingCPU(float var1) {
      this.animationLimitingMode = 1;
      this.animationLimitingAmount = var1;
      if (this.bridgeContext != null) {
         this.setBridgeContextAnimationLimitingMode();
      }

   }

   public void setAnimationLimitingFPS(float var1) {
      this.animationLimitingMode = 2;
      this.animationLimitingAmount = var1;
      if (this.bridgeContext != null) {
         this.setBridgeContextAnimationLimitingMode();
      }

   }

   protected void setBridgeContextAnimationLimitingMode() {
      switch (this.animationLimitingMode) {
         case 0:
            this.bridgeContext.setAnimationLimitingNone();
            break;
         case 1:
            this.bridgeContext.setAnimationLimitingCPU(this.animationLimitingAmount);
            break;
         case 2:
            this.bridgeContext.setAnimationLimitingFPS(this.animationLimitingAmount);
      }

   }

   protected AbstractJGVTComponent.Listener createListener() {
      return new SVGListener();
   }

   protected UserAgent createUserAgent() {
      return new BridgeUserAgent();
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      SVGFeatureStrings.addSupportedFeatureStrings(FEATURES);
   }

   protected class BridgeUserAgent implements UserAgent {
      protected Map extensions = new HashMap();

      public Dimension2D getViewportSize() {
         return AbstractJSVGComponent.this.getSize();
      }

      public EventDispatcher getEventDispatcher() {
         return AbstractJSVGComponent.this.eventDispatcher;
      }

      public void displayError(String var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.displayError(var1);
         }

      }

      public void displayError(Exception var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.displayError(var1);
         }

      }

      public void displayMessage(String var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.displayMessage(var1);
         }

      }

      public void showAlert(String var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.showAlert(var1);
         } else {
            AbstractJSVGComponent.this.showAlert(var1);
         }
      }

      public String showPrompt(String var1) {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.showPrompt(var1) : AbstractJSVGComponent.this.showPrompt(var1);
      }

      public String showPrompt(String var1, String var2) {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.showPrompt(var1, var2) : AbstractJSVGComponent.this.showPrompt(var1, var2);
      }

      public boolean showConfirm(String var1) {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.showConfirm(var1) : AbstractJSVGComponent.this.showConfirm(var1);
      }

      public float getPixelUnitToMillimeter() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getPixelUnitToMillimeter() : 0.26458332F;
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public String getDefaultFontFamily() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getDefaultFontFamily() : "Arial, Helvetica, sans-serif";
      }

      public float getMediumFontSize() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getMediumFontSize() : 228.59999F / (72.0F * this.getPixelUnitToMillimeter());
      }

      public float getLighterFontWeight(float var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            return AbstractJSVGComponent.this.svgUserAgent.getLighterFontWeight(var1);
         } else {
            int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
            switch (var2) {
               case 100:
                  return 100.0F;
               case 200:
                  return 100.0F;
               case 300:
                  return 200.0F;
               case 400:
                  return 300.0F;
               case 500:
                  return 400.0F;
               case 600:
                  return 400.0F;
               case 700:
                  return 400.0F;
               case 800:
                  return 400.0F;
               case 900:
                  return 400.0F;
               default:
                  throw new IllegalArgumentException("Bad Font Weight: " + var1);
            }
         }
      }

      public float getBolderFontWeight(float var1) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            return AbstractJSVGComponent.this.svgUserAgent.getBolderFontWeight(var1);
         } else {
            int var2 = (int)((var1 + 50.0F) / 100.0F) * 100;
            switch (var2) {
               case 100:
                  return 600.0F;
               case 200:
                  return 600.0F;
               case 300:
                  return 600.0F;
               case 400:
                  return 600.0F;
               case 500:
                  return 600.0F;
               case 600:
                  return 700.0F;
               case 700:
                  return 800.0F;
               case 800:
                  return 900.0F;
               case 900:
                  return 900.0F;
               default:
                  throw new IllegalArgumentException("Bad Font Weight: " + var1);
            }
         }
      }

      public String getLanguages() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getLanguages() : "en";
      }

      public String getUserStyleSheetURI() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getUserStyleSheetURI() : null;
      }

      public void openLink(SVGAElement var1) {
         String var2 = XLinkSupport.getXLinkShow(var1);
         String var3 = var1.getHref().getAnimVal();
         ParsedURL var5;
         if (var2.equals("new")) {
            this.fireLinkActivatedEvent(var1, var3);
            if (AbstractJSVGComponent.this.svgUserAgent != null) {
               String var7 = AbstractJSVGComponent.this.svgDocument.getURL();
               var5 = null;
               if (var1.getOwnerDocument() != AbstractJSVGComponent.this.svgDocument) {
                  SVGDocument var8 = (SVGDocument)var1.getOwnerDocument();
                  var3 = (new ParsedURL(var8.getURL(), var3)).toString();
               }

               var5 = new ParsedURL(var7, var3);
               var3 = var5.toString();
               AbstractJSVGComponent.this.svgUserAgent.openLink(var3, true);
            } else {
               AbstractJSVGComponent.this.loadSVGDocument(var3);
            }

         } else {
            ParsedURL var4 = new ParsedURL(((SVGDocument)var1.getOwnerDocument()).getURL(), var3);
            var3 = var4.toString();
            if (AbstractJSVGComponent.this.svgDocument != null) {
               var5 = new ParsedURL(AbstractJSVGComponent.this.svgDocument.getURL());
               if (var4.sameFile(var5)) {
                  String var6 = var4.getRef();
                  if (AbstractJSVGComponent.this.fragmentIdentifier != var6 && (var6 == null || !var6.equals(AbstractJSVGComponent.this.fragmentIdentifier))) {
                     AbstractJSVGComponent.this.fragmentIdentifier = var6;
                     if (AbstractJSVGComponent.this.computeRenderingTransform()) {
                        AbstractJSVGComponent.this.scheduleGVTRendering();
                     }
                  }

                  this.fireLinkActivatedEvent(var1, var3);
                  return;
               }
            }

            this.fireLinkActivatedEvent(var1, var3);
            if (AbstractJSVGComponent.this.svgUserAgent != null) {
               AbstractJSVGComponent.this.svgUserAgent.openLink(var3, false);
            } else {
               AbstractJSVGComponent.this.loadSVGDocument(var3);
            }

         }
      }

      protected void fireLinkActivatedEvent(SVGAElement var1, String var2) {
         Object[] var3 = AbstractJSVGComponent.this.linkActivationListeners.toArray();
         if (var3.length > 0) {
            LinkActivationEvent var4 = new LinkActivationEvent(AbstractJSVGComponent.this, var1, var2);

            for(int var5 = 0; var5 < var3.length; ++var5) {
               LinkActivationListener var6 = (LinkActivationListener)var3[var5];
               var6.linkActivated(var4);
            }
         }

      }

      public void setSVGCursor(Cursor var1) {
         if (var1 != AbstractJSVGComponent.this.getCursor()) {
            AbstractJSVGComponent.this.setCursor(var1);
         }

      }

      public void setTextSelection(Mark var1, Mark var2) {
         AbstractJSVGComponent.this.select(var1, var2);
      }

      public void deselectAll() {
         AbstractJSVGComponent.this.deselectAll();
      }

      public String getXMLParserClassName() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getXMLParserClassName() : XMLResourceDescriptor.getXMLParserClassName();
      }

      public boolean isXMLParserValidating() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.isXMLParserValidating() : false;
      }

      public AffineTransform getTransform() {
         return AbstractJSVGComponent.this.renderingTransform;
      }

      public void setTransform(AffineTransform var1) {
         AbstractJSVGComponent.this.setRenderingTransform(var1);
      }

      public String getMedia() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getMedia() : "screen";
      }

      public String getAlternateStyleSheet() {
         return AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getAlternateStyleSheet() : null;
      }

      public Point getClientAreaLocationOnScreen() {
         return AbstractJSVGComponent.this.getLocationOnScreen();
      }

      public boolean hasFeature(String var1) {
         return AbstractJSVGComponent.FEATURES.contains(var1);
      }

      public boolean supportExtension(String var1) {
         return AbstractJSVGComponent.this.svgUserAgent != null && AbstractJSVGComponent.this.svgUserAgent.supportExtension(var1) ? true : this.extensions.containsKey(var1);
      }

      public void registerExtension(BridgeExtension var1) {
         Iterator var2 = var1.getImplementedExtensions();

         while(var2.hasNext()) {
            this.extensions.put(var2.next(), var1);
         }

      }

      public void handleElement(Element var1, Object var2) {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.handleElement(var1, var2);
         }

      }

      public ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
         return (ScriptSecurity)(AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getScriptSecurity(var1, var2, var3) : new DefaultScriptSecurity(var1, var2, var3));
      }

      public void checkLoadScript(String var1, ParsedURL var2, ParsedURL var3) throws SecurityException {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.checkLoadScript(var1, var2, var3);
         } else {
            ScriptSecurity var4 = this.getScriptSecurity(var1, var2, var3);
            if (var4 != null) {
               var4.checkLoadScript();
            }
         }

      }

      public ExternalResourceSecurity getExternalResourceSecurity(ParsedURL var1, ParsedURL var2) {
         return (ExternalResourceSecurity)(AbstractJSVGComponent.this.svgUserAgent != null ? AbstractJSVGComponent.this.svgUserAgent.getExternalResourceSecurity(var1, var2) : new RelaxedExternalResourceSecurity(var1, var2));
      }

      public void checkLoadExternalResource(ParsedURL var1, ParsedURL var2) throws SecurityException {
         if (AbstractJSVGComponent.this.svgUserAgent != null) {
            AbstractJSVGComponent.this.svgUserAgent.checkLoadExternalResource(var1, var2);
         } else {
            ExternalResourceSecurity var3 = this.getExternalResourceSecurity(var1, var2);
            if (var3 != null) {
               var3.checkLoadExternalResource();
            }
         }

      }

      public SVGDocument getBrokenLinkDocument(Element var1, String var2, String var3) {
         Class var4 = AbstractJSVGComponent.class$org$apache$batik$swing$svg$AbstractJSVGComponent == null ? (AbstractJSVGComponent.class$org$apache$batik$swing$svg$AbstractJSVGComponent = AbstractJSVGComponent.class$("org.apache.batik.swing.svg.AbstractJSVGComponent")) : AbstractJSVGComponent.class$org$apache$batik$swing$svg$AbstractJSVGComponent;
         URL var5 = var4.getResource("resources/BrokenLink.svg");
         if (var5 == null) {
            throw new BridgeException(AbstractJSVGComponent.this.bridgeContext, var1, "uri.image.broken", new Object[]{var2, var3});
         } else {
            DocumentLoader var6 = AbstractJSVGComponent.this.bridgeContext.getDocumentLoader();
            SVGDocument var7 = null;

            try {
               var7 = (SVGDocument)var6.loadDocument(var5.toString());
               if (var7 == null) {
                  return var7;
               } else {
                  DOMImplementation var8 = SVGDOMImplementation.getDOMImplementation();
                  var7 = (SVGDocument)DOMUtilities.deepCloneDocument(var7, var8);
                  Element var10 = var7.getElementById("__More_About");
                  if (var10 == null) {
                     return var7;
                  } else {
                     Element var11 = var7.createElementNS("http://www.w3.org/2000/svg", "title");
                     String var9 = Messages.formatMessage("broken.link.title", (Object[])null);
                     var11.appendChild(var7.createTextNode(var9));
                     Element var12 = var7.createElementNS("http://www.w3.org/2000/svg", "desc");
                     var12.appendChild(var7.createTextNode(var3));
                     var10.insertBefore(var12, var10.getFirstChild());
                     var10.insertBefore(var11, var12);
                     return var7;
                  }
               }
            } catch (Exception var13) {
               throw new BridgeException(AbstractJSVGComponent.this.bridgeContext, var1, var13, "uri.image.broken", new Object[]{var2, var3});
            }
         }
      }
   }

   protected static class BridgeUserAgentWrapper implements UserAgent {
      protected UserAgent userAgent;

      public BridgeUserAgentWrapper(UserAgent var1) {
         this.userAgent = var1;
      }

      public EventDispatcher getEventDispatcher() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getEventDispatcher();
         } else {
            class Query implements Runnable {
               EventDispatcher result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getEventDispatcher();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public Dimension2D getViewportSize() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getViewportSize();
         } else {
            class Query implements Runnable {
               Dimension2D result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getViewportSize();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public void displayError(final Exception var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.displayError(var1);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.displayError(var1);
               }
            });
         }

      }

      public void displayMessage(final String var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.displayMessage(var1);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.displayMessage(var1);
               }
            });
         }

      }

      public void showAlert(final String var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.showAlert(var1);
         } else {
            this.invokeAndWait(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.showAlert(var1);
               }
            });
         }

      }

      public String showPrompt(final String var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.showPrompt(var1);
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.showPrompt(var1);
               }
            }

            Query var2 = new Query();
            this.invokeAndWait(var2);
            return var2.result;
         }
      }

      public String showPrompt(final String var1, final String var2) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.showPrompt(var1, var2);
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.showPrompt(var1, var2);
               }
            }

            Query var3 = new Query();
            this.invokeAndWait(var3);
            return var3.result;
         }
      }

      public boolean showConfirm(final String var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.showConfirm(var1);
         } else {
            class Query implements Runnable {
               boolean result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.showConfirm(var1);
               }
            }

            Query var2 = new Query();
            this.invokeAndWait(var2);
            return var2.result;
         }
      }

      public float getPixelUnitToMillimeter() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getPixelUnitToMillimeter();
         } else {
            class Query implements Runnable {
               float result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getPixelUnitToMillimeter();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public String getDefaultFontFamily() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getDefaultFontFamily();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getDefaultFontFamily();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public float getMediumFontSize() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getMediumFontSize();
         } else {
            class Query implements Runnable {
               float result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getMediumFontSize();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public float getLighterFontWeight(final float var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getLighterFontWeight(var1);
         } else {
            class Query implements Runnable {
               float result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getLighterFontWeight(var1);
               }
            }

            Query var3 = new Query();
            this.invokeAndWait(var3);
            return var3.result;
         }
      }

      public float getBolderFontWeight(final float var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getBolderFontWeight(var1);
         } else {
            class Query implements Runnable {
               float result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getBolderFontWeight(var1);
               }
            }

            Query var3 = new Query();
            this.invokeAndWait(var3);
            return var3.result;
         }
      }

      public String getLanguages() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getLanguages();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getLanguages();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public String getUserStyleSheetURI() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getUserStyleSheetURI();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getUserStyleSheetURI();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public void openLink(final SVGAElement var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.openLink(var1);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.openLink(var1);
               }
            });
         }

      }

      public void setSVGCursor(final Cursor var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.setSVGCursor(var1);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.setSVGCursor(var1);
               }
            });
         }

      }

      public void setTextSelection(final Mark var1, final Mark var2) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.setTextSelection(var1, var2);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.setTextSelection(var1, var2);
               }
            });
         }

      }

      public void deselectAll() {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.deselectAll();
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.deselectAll();
               }
            });
         }

      }

      public String getXMLParserClassName() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getXMLParserClassName();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getXMLParserClassName();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public boolean isXMLParserValidating() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.isXMLParserValidating();
         } else {
            class Query implements Runnable {
               boolean result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.isXMLParserValidating();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public AffineTransform getTransform() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getTransform();
         } else {
            class Query implements Runnable {
               AffineTransform result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getTransform();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public void setTransform(final AffineTransform var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.setTransform(var1);
         } else {
            class Query implements Runnable {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.setTransform(var1);
               }
            }

            Query var3 = new Query();
            this.invokeAndWait(var3);
         }

      }

      public String getMedia() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getMedia();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getMedia();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public String getAlternateStyleSheet() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getAlternateStyleSheet();
         } else {
            class Query implements Runnable {
               String result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getAlternateStyleSheet();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public Point getClientAreaLocationOnScreen() {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getClientAreaLocationOnScreen();
         } else {
            class Query implements Runnable {
               Point result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getClientAreaLocationOnScreen();
               }
            }

            Query var1 = new Query();
            this.invokeAndWait(var1);
            return var1.result;
         }
      }

      public boolean hasFeature(final String var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.hasFeature(var1);
         } else {
            class Query implements Runnable {
               boolean result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.hasFeature(var1);
               }
            }

            Query var2 = new Query();
            this.invokeAndWait(var2);
            return var2.result;
         }
      }

      public boolean supportExtension(final String var1) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.supportExtension(var1);
         } else {
            class Query implements Runnable {
               boolean result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.supportExtension(var1);
               }
            }

            Query var2 = new Query();
            this.invokeAndWait(var2);
            return var2.result;
         }
      }

      public void registerExtension(final BridgeExtension var1) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.registerExtension(var1);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.registerExtension(var1);
               }
            });
         }

      }

      public void handleElement(final Element var1, final Object var2) {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.handleElement(var1, var2);
         } else {
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  BridgeUserAgentWrapper.this.userAgent.handleElement(var1, var2);
               }
            });
         }

      }

      public ScriptSecurity getScriptSecurity(final String var1, final ParsedURL var2, final ParsedURL var3) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getScriptSecurity(var1, var2, var3);
         } else {
            class Query implements Runnable {
               ScriptSecurity result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getScriptSecurity(var1, var2, var3);
               }
            }

            Query var7 = new Query();
            this.invokeAndWait(var7);
            return var7.result;
         }
      }

      public void checkLoadScript(final String var1, final ParsedURL var2, final ParsedURL var3) throws SecurityException {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.checkLoadScript(var1, var2, var3);
         } else {
            class Query implements Runnable {
               SecurityException se = null;

               public void run() {
                  try {
                     BridgeUserAgentWrapper.this.userAgent.checkLoadScript(var1, var2, var3);
                  } catch (SecurityException var2x) {
                     this.se = var2x;
                  }

               }
            }

            Query var7 = new Query();
            this.invokeAndWait(var7);
            if (var7.se != null) {
               var7.se.fillInStackTrace();
               throw var7.se;
            }
         }

      }

      public ExternalResourceSecurity getExternalResourceSecurity(final ParsedURL var1, final ParsedURL var2) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getExternalResourceSecurity(var1, var2);
         } else {
            class Query implements Runnable {
               ExternalResourceSecurity result;

               public void run() {
                  this.result = BridgeUserAgentWrapper.this.userAgent.getExternalResourceSecurity(var1, var2);
               }
            }

            Query var5 = new Query();
            this.invokeAndWait(var5);
            return var5.result;
         }
      }

      public void checkLoadExternalResource(final ParsedURL var1, final ParsedURL var2) throws SecurityException {
         if (EventQueue.isDispatchThread()) {
            this.userAgent.checkLoadExternalResource(var1, var2);
         } else {
            class Query implements Runnable {
               SecurityException se;

               public void run() {
                  try {
                     BridgeUserAgentWrapper.this.userAgent.checkLoadExternalResource(var1, var2);
                  } catch (SecurityException var2x) {
                     this.se = var2x;
                  }

               }
            }

            Query var5 = new Query();
            this.invokeAndWait(var5);
            if (var5.se != null) {
               var5.se.fillInStackTrace();
               throw var5.se;
            }
         }

      }

      public SVGDocument getBrokenLinkDocument(final Element var1, final String var2, final String var3) {
         if (EventQueue.isDispatchThread()) {
            return this.userAgent.getBrokenLinkDocument(var1, var2, var3);
         } else {
            class Query implements Runnable {
               SVGDocument doc;
               RuntimeException rex = null;

               public void run() {
                  try {
                     this.doc = BridgeUserAgentWrapper.this.userAgent.getBrokenLinkDocument(var1, var2, var3);
                  } catch (RuntimeException var2x) {
                     this.rex = var2x;
                  }

               }
            }

            Query var4 = new Query();
            this.invokeAndWait(var4);
            if (var4.rex != null) {
               throw var4.rex;
            } else {
               return var4.doc;
            }
         }
      }

      protected void invokeAndWait(Runnable var1) {
         try {
            EventQueue.invokeAndWait(var1);
         } catch (Exception var3) {
         }

      }
   }

   protected class SVGListener extends JGVTComponent.ExtendedListener implements SVGDocumentLoaderListener, GVTTreeBuilderListener, SVGLoadEventDispatcherListener, UpdateManagerListener {
      protected SVGListener() {
         super();
      }

      public void documentLoadingStarted(SVGDocumentLoaderEvent var1) {
      }

      public void documentLoadingCompleted(SVGDocumentLoaderEvent var1) {
         if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            AbstractJSVGComponent.this.documentLoader = null;
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else {
               AbstractJSVGComponent.this.setSVGDocument(var1.getSVGDocument());
            }
         }
      }

      public void documentLoadingCancelled(SVGDocumentLoaderEvent var1) {
         if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            AbstractJSVGComponent.this.documentLoader = null;
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
               AbstractJSVGComponent.this.startGVTTreeBuilder();
            }
         }
      }

      public void documentLoadingFailed(SVGDocumentLoaderEvent var1) {
         if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            AbstractJSVGComponent.this.documentLoader = null;
            AbstractJSVGComponent.this.userAgent.displayError(((SVGDocumentLoader)var1.getSource()).getException());
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
               AbstractJSVGComponent.this.startGVTTreeBuilder();
            }
         }
      }

      public void gvtBuildStarted(GVTTreeBuilderEvent var1) {
         AbstractJSVGComponent.this.removeJGVTComponentListener(AbstractJSVGComponent.this.jsvgComponentListener);
         AbstractJSVGComponent.this.removeComponentListener(AbstractJSVGComponent.this.jsvgComponentListener);
      }

      public void gvtBuildCompleted(GVTTreeBuilderEvent var1) {
         if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else {
            AbstractJSVGComponent.this.loader = null;
            AbstractJSVGComponent.this.gvtTreeBuilder = null;
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
               AbstractJSVGComponent.this.startDocumentLoader();
            } else {
               AbstractJSVGComponent.this.gvtRoot = null;
               if (AbstractJSVGComponent.this.isDynamicDocument && AbstractJSVGComponent.this.eventsEnabled) {
                  AbstractJSVGComponent.this.startSVGLoadEventDispatcher(var1.getGVTRoot());
               } else {
                  if (AbstractJSVGComponent.this.isInteractiveDocument) {
                     AbstractJSVGComponent.this.nextUpdateManager = new UpdateManager(AbstractJSVGComponent.this.bridgeContext, var1.getGVTRoot(), AbstractJSVGComponent.this.svgDocument);
                  }

                  AbstractJSVGComponent.this.setGraphicsNode(var1.getGVTRoot(), false);
                  AbstractJSVGComponent.this.scheduleGVTRendering();
               }

            }
         }
      }

      public void gvtBuildCancelled(GVTTreeBuilderEvent var1) {
         if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else {
            AbstractJSVGComponent.this.loader = null;
            AbstractJSVGComponent.this.gvtTreeBuilder = null;
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
               AbstractJSVGComponent.this.startDocumentLoader();
            } else {
               AbstractJSVGComponent.this.image = null;
               AbstractJSVGComponent.this.repaint();
            }
         }
      }

      public void gvtBuildFailed(GVTTreeBuilderEvent var1) {
         if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else {
            AbstractJSVGComponent.this.loader = null;
            AbstractJSVGComponent.this.gvtTreeBuilder = null;
            if (AbstractJSVGComponent.this.afterStopRunnable != null) {
               EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
               AbstractJSVGComponent.this.afterStopRunnable = null;
            } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
               AbstractJSVGComponent.this.startDocumentLoader();
            } else {
               GraphicsNode var2 = var1.getGVTRoot();
               if (var2 == null) {
                  AbstractJSVGComponent.this.image = null;
                  AbstractJSVGComponent.this.repaint();
               } else {
                  AbstractJSVGComponent.this.setGraphicsNode(var2, false);
                  AbstractJSVGComponent.this.computeRenderingTransform();
               }

               AbstractJSVGComponent.this.userAgent.displayError(((GVTTreeBuilder)var1.getSource()).getException());
            }
         }
      }

      public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent var1) {
      }

      public void svgLoadEventDispatchCompleted(SVGLoadEventDispatcherEvent var1) {
         AbstractJSVGComponent.this.nextUpdateManager = AbstractJSVGComponent.this.svgLoadEventDispatcher.getUpdateManager();
         AbstractJSVGComponent.this.svgLoadEventDispatcher = null;
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            AbstractJSVGComponent.this.nextUpdateManager.interrupt();
            AbstractJSVGComponent.this.nextUpdateManager = null;
            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.nextUpdateManager.interrupt();
            AbstractJSVGComponent.this.nextUpdateManager = null;
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.nextUpdateManager.interrupt();
            AbstractJSVGComponent.this.nextUpdateManager = null;
            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            AbstractJSVGComponent.this.setGraphicsNode(var1.getGVTRoot(), false);
            AbstractJSVGComponent.this.scheduleGVTRendering();
         }
      }

      public void svgLoadEventDispatchCancelled(SVGLoadEventDispatcherEvent var1) {
         AbstractJSVGComponent.this.nextUpdateManager = AbstractJSVGComponent.this.svgLoadEventDispatcher.getUpdateManager();
         AbstractJSVGComponent.this.svgLoadEventDispatcher = null;
         AbstractJSVGComponent.this.nextUpdateManager.interrupt();
         AbstractJSVGComponent.this.nextUpdateManager = null;
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.startDocumentLoader();
         }
      }

      public void svgLoadEventDispatchFailed(SVGLoadEventDispatcherEvent var1) {
         AbstractJSVGComponent.this.nextUpdateManager = AbstractJSVGComponent.this.svgLoadEventDispatcher.getUpdateManager();
         AbstractJSVGComponent.this.svgLoadEventDispatcher = null;
         AbstractJSVGComponent.this.nextUpdateManager.interrupt();
         AbstractJSVGComponent.this.nextUpdateManager = null;
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            GraphicsNode var2 = var1.getGVTRoot();
            if (var2 == null) {
               AbstractJSVGComponent.this.image = null;
               AbstractJSVGComponent.this.repaint();
            } else {
               AbstractJSVGComponent.this.setGraphicsNode(var2, false);
               AbstractJSVGComponent.this.computeRenderingTransform();
            }

            AbstractJSVGComponent.this.userAgent.displayError(((SVGLoadEventDispatcher)var1.getSource()).getException());
         }
      }

      public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
         super.gvtRenderingCompleted(var1);
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startDocumentLoader();
         } else {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.updateManager = AbstractJSVGComponent.this.nextUpdateManager;
               AbstractJSVGComponent.this.nextUpdateManager = null;
               AbstractJSVGComponent.this.updateManager.addUpdateManagerListener(this);
               AbstractJSVGComponent.this.updateManager.manageUpdates(AbstractJSVGComponent.this.renderer);
            }

         }
      }

      public void gvtRenderingCancelled(GVTTreeRendererEvent var1) {
         super.gvtRenderingCancelled(var1);
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startDocumentLoader();
         }
      }

      public void gvtRenderingFailed(GVTTreeRendererEvent var1) {
         super.gvtRenderingFailed(var1);
         if (AbstractJSVGComponent.this.afterStopRunnable != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
            AbstractJSVGComponent.this.afterStopRunnable = null;
         } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startGVTTreeBuilder();
         } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
            if (AbstractJSVGComponent.this.nextUpdateManager != null) {
               AbstractJSVGComponent.this.nextUpdateManager.interrupt();
               AbstractJSVGComponent.this.nextUpdateManager = null;
            }

            AbstractJSVGComponent.this.startDocumentLoader();
         }
      }

      public void managerStarted(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               AbstractJSVGComponent.this.suspendInteractions = false;
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).managerStarted(var1);
                  }
               }

            }
         });
      }

      public void managerSuspended(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).managerSuspended(var1);
                  }
               }

            }
         });
      }

      public void managerResumed(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).managerResumed(var1);
                  }
               }

            }
         });
      }

      public void managerStopped(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               AbstractJSVGComponent.this.updateManager = null;
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).managerStopped(var1);
                  }
               }

               if (AbstractJSVGComponent.this.afterStopRunnable != null) {
                  EventQueue.invokeLater(AbstractJSVGComponent.this.afterStopRunnable);
                  AbstractJSVGComponent.this.afterStopRunnable = null;
               } else if (AbstractJSVGComponent.this.nextGVTTreeBuilder != null) {
                  AbstractJSVGComponent.this.startGVTTreeBuilder();
               } else if (AbstractJSVGComponent.this.nextDocumentLoader != null) {
                  AbstractJSVGComponent.this.startDocumentLoader();
               }
            }
         });
      }

      public void updateStarted(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               if (!AbstractJSVGComponent.this.doubleBufferedRendering) {
                  AbstractJSVGComponent.this.image = var1.getImage();
               }

               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).updateStarted(var1);
                  }
               }

            }
         });
      }

      public void updateCompleted(final UpdateManagerEvent var1) {
         try {
            EventQueue.invokeAndWait(new Runnable() {
               public void run() {
                  AbstractJSVGComponent.this.image = var1.getImage();
                  if (var1.getClearPaintingTransform()) {
                     AbstractJSVGComponent.this.paintingTransform = null;
                  }

                  List var1x = var1.getDirtyAreas();
                  if (var1x != null) {
                     Iterator var2 = var1x.iterator();

                     while(var2.hasNext()) {
                        Rectangle var3 = (Rectangle)var2.next();
                        if (AbstractJSVGComponent.this.updateOverlay != null) {
                           AbstractJSVGComponent.this.updateOverlay.addRect(var3);
                           var3 = AbstractJSVGComponent.this.getRenderRect();
                        }

                        if (AbstractJSVGComponent.this.doubleBufferedRendering) {
                           AbstractJSVGComponent.this.repaint(var3);
                        } else {
                           AbstractJSVGComponent.this.paintImmediately(var3);
                        }
                     }

                     if (AbstractJSVGComponent.this.updateOverlay != null) {
                        AbstractJSVGComponent.this.updateOverlay.endUpdate();
                     }
                  }

                  AbstractJSVGComponent.this.suspendInteractions = false;
               }
            });
         } catch (Exception var3) {
         }

         EventQueue.invokeLater(new Runnable() {
            public void run() {
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).updateCompleted(var1);
                  }
               }

            }
         });
      }

      public void updateFailed(final UpdateManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               Object[] var1x = AbstractJSVGComponent.this.updateManagerListeners.toArray();
               if (var1x.length > 0) {
                  for(int var2 = 0; var2 < var1x.length; ++var2) {
                     ((UpdateManagerListener)var1x[var2]).updateFailed(var1);
                  }
               }

            }
         });
      }

      protected void dispatchKeyTyped(final KeyEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchKeyTyped(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.keyTyped(var1);
                  }
               });
            }

         }
      }

      protected void dispatchKeyPressed(final KeyEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchKeyPressed(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.keyPressed(var1);
                  }
               });
            }

         }
      }

      protected void dispatchKeyReleased(final KeyEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchKeyReleased(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.keyReleased(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMouseClicked(final MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isInteractiveDocument) {
            super.dispatchMouseClicked(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseClicked(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMousePressed(final MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchMousePressed(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mousePressed(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMouseReleased(final MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchMouseReleased(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseReleased(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMouseEntered(final MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isInteractiveDocument) {
            super.dispatchMouseEntered(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseEntered(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMouseExited(final MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isInteractiveDocument) {
            super.dispatchMouseExited(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseExited(var1);
                  }
               });
            }

         }
      }

      protected void dispatchMouseDragged(MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isDynamicDocument) {
            super.dispatchMouseDragged(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               RunnableQueue var2 = AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue();

               class MouseDraggedRunnable implements Runnable {
                  MouseEvent event;

                  MouseDraggedRunnable(MouseEvent var2) {
                     this.event = var2;
                  }

                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseDragged(this.event);
                  }
               }

               synchronized(var2.getIteratorLock()) {
                  Iterator var4 = var2.iterator();

                  while(true) {
                     if (!var4.hasNext()) {
                        break;
                     }

                     Object var5 = var4.next();
                     if (var5 instanceof MouseDraggedRunnable) {
                        MouseDraggedRunnable var6 = (MouseDraggedRunnable)var5;
                        MouseEvent var7 = var6.event;
                        if (var7.getModifiers() == var1.getModifiers()) {
                           var6.event = var1;
                        }

                        return;
                     }
                  }
               }

               var2.invokeLater(new MouseDraggedRunnable(var1));
            }

         }
      }

      protected void dispatchMouseMoved(MouseEvent var1) {
         if (!AbstractJSVGComponent.this.isInteractiveDocument) {
            super.dispatchMouseMoved(var1);
         } else {
            if (AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
               RunnableQueue var2 = AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue();
               int var3 = 0;

               class MouseMovedRunnable implements Runnable {
                  MouseEvent event;

                  MouseMovedRunnable(MouseEvent var2) {
                     this.event = var2;
                  }

                  public void run() {
                     AbstractJSVGComponent.this.eventDispatcher.mouseMoved(this.event);
                  }
               }

               synchronized(var2.getIteratorLock()) {
                  Iterator var5 = var2.iterator();

                  while(true) {
                     if (!var5.hasNext()) {
                        break;
                     }

                     Object var6 = var5.next();
                     if (var6 instanceof MouseMovedRunnable) {
                        MouseMovedRunnable var7 = (MouseMovedRunnable)var6;
                        MouseEvent var8 = var7.event;
                        if (var8.getModifiers() == var1.getModifiers()) {
                           var7.event = var1;
                        }

                        return;
                     }

                     ++var3;
                  }
               }

               var2.invokeLater(new MouseMovedRunnable(var1));
            }

         }
      }
   }

   protected class JSVGComponentListener extends ComponentAdapter implements JGVTComponentListener {
      float prevScale = 0.0F;
      float prevTransX = 0.0F;
      float prevTransY = 0.0F;

      public void componentResized(ComponentEvent var1) {
         if (AbstractJSVGComponent.this.isDynamicDocument && AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
            AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
               public void run() {
                  try {
                     AbstractJSVGComponent.this.updateManager.dispatchSVGResizeEvent();
                  } catch (InterruptedException var2) {
                  }

               }
            });
         }

      }

      public void componentTransformChanged(ComponentEvent var1) {
         AffineTransform var2 = AbstractJSVGComponent.this.getRenderingTransform();
         float var3 = (float)Math.sqrt(var2.getDeterminant());
         float var4 = (float)var2.getTranslateX();
         float var5 = (float)var2.getTranslateY();
         final boolean var6 = var3 != this.prevScale;
         final boolean var7 = var4 != this.prevTransX || var5 != this.prevTransY;
         if (AbstractJSVGComponent.this.isDynamicDocument && AbstractJSVGComponent.this.updateManager != null && AbstractJSVGComponent.this.updateManager.isRunning()) {
            AbstractJSVGComponent.this.updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
               public void run() {
                  try {
                     if (var6) {
                        AbstractJSVGComponent.this.updateManager.dispatchSVGZoomEvent();
                     }

                     if (var7) {
                        AbstractJSVGComponent.this.updateManager.dispatchSVGScrollEvent();
                     }
                  } catch (InterruptedException var2) {
                  }

               }
            });
         }

         this.prevScale = var3;
         this.prevTransX = var4;
         this.prevTransY = var5;
      }

      public void updateMatrix(AffineTransform var1) {
         this.prevScale = (float)Math.sqrt(var1.getDeterminant());
         this.prevTransX = (float)var1.getTranslateX();
         this.prevTransY = (float)var1.getTranslateY();
      }
   }
}
