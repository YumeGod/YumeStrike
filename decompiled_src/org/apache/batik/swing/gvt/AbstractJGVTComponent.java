package org.apache.batik.swing.gvt;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.text.CharacterIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.event.AWTEventDispatcher;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.event.SelectionAdapter;
import org.apache.batik.gvt.event.SelectionEvent;
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.gvt.renderer.ImageRendererFactory;
import org.apache.batik.gvt.text.Mark;
import org.apache.batik.util.HaltingThread;
import org.apache.batik.util.Platform;

public abstract class AbstractJGVTComponent extends JComponent {
   protected Listener listener;
   protected GVTTreeRenderer gvtTreeRenderer;
   protected GraphicsNode gvtRoot;
   protected ImageRendererFactory rendererFactory;
   protected ImageRenderer renderer;
   protected List gvtTreeRendererListeners;
   protected boolean needRender;
   protected boolean progressivePaint;
   protected HaltingThread progressivePaintThread;
   protected BufferedImage image;
   protected AffineTransform initialTransform;
   protected AffineTransform renderingTransform;
   protected AffineTransform paintingTransform;
   protected List interactors;
   protected Interactor interactor;
   protected List overlays;
   protected List jgvtListeners;
   protected AWTEventDispatcher eventDispatcher;
   protected TextSelectionManager textSelectionManager;
   protected boolean doubleBufferedRendering;
   protected boolean eventsEnabled;
   protected boolean selectableText;
   protected boolean useUnixTextSelection;
   protected boolean suspendInteractions;
   protected boolean disableInteractions;

   public AbstractJGVTComponent() {
      this(false, false);
   }

   public AbstractJGVTComponent(boolean var1, boolean var2) {
      this.rendererFactory = new ConcreteImageRendererFactory();
      this.gvtTreeRendererListeners = Collections.synchronizedList(new LinkedList());
      this.initialTransform = new AffineTransform();
      this.renderingTransform = new AffineTransform();
      this.interactors = new LinkedList();
      this.overlays = new LinkedList();
      this.jgvtListeners = null;
      this.useUnixTextSelection = true;
      this.setBackground(Color.white);
      this.eventsEnabled = var1;
      this.selectableText = var2;
      this.listener = this.createListener();
      this.addAWTListeners();
      this.addGVTTreeRendererListener(this.listener);
      this.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent var1) {
            if (AbstractJGVTComponent.this.updateRenderingTransform()) {
               AbstractJGVTComponent.this.scheduleGVTRendering();
            }

         }
      });
   }

   protected void addAWTListeners() {
      this.addKeyListener(this.listener);
      this.addMouseListener(this.listener);
      this.addMouseMotionListener(this.listener);
   }

   public void setDisableInteractions(boolean var1) {
      this.disableInteractions = var1;
   }

   public boolean getDisableInteractions() {
      return this.disableInteractions;
   }

   public void setUseUnixTextSelection(boolean var1) {
      this.useUnixTextSelection = var1;
   }

   public void getUseUnixTextSelection(boolean var1) {
      this.useUnixTextSelection = var1;
   }

   public List getInteractors() {
      return this.interactors;
   }

   public List getOverlays() {
      return this.overlays;
   }

   public BufferedImage getOffScreen() {
      return this.image;
   }

   public void addJGVTComponentListener(JGVTComponentListener var1) {
      if (this.jgvtListeners == null) {
         this.jgvtListeners = new LinkedList();
      }

      this.jgvtListeners.add(var1);
   }

   public void removeJGVTComponentListener(JGVTComponentListener var1) {
      if (this.jgvtListeners != null) {
         this.jgvtListeners.remove(var1);
      }
   }

   public void resetRenderingTransform() {
      this.setRenderingTransform(this.initialTransform);
   }

   public void stopProcessing() {
      if (this.gvtTreeRenderer != null) {
         this.needRender = false;
         this.gvtTreeRenderer.halt();
         this.haltProgressivePaintThread();
      }

   }

   public GraphicsNode getGraphicsNode() {
      return this.gvtRoot;
   }

   public void setGraphicsNode(GraphicsNode var1) {
      this.setGraphicsNode(var1, true);
      this.initialTransform = new AffineTransform();
      this.updateRenderingTransform();
      this.setRenderingTransform(this.initialTransform, true);
   }

   protected void setGraphicsNode(GraphicsNode var1, boolean var2) {
      this.gvtRoot = var1;
      if (var1 != null && var2) {
         this.initializeEventHandling();
      }

      if (this.eventDispatcher != null) {
         this.eventDispatcher.setRootNode(var1);
      }

   }

   protected void initializeEventHandling() {
      if (this.eventsEnabled) {
         this.eventDispatcher = new AWTEventDispatcher();
         if (this.selectableText) {
            this.textSelectionManager = this.createTextSelectionManager(this.eventDispatcher);
            this.textSelectionManager.addSelectionListener(new UnixTextSelectionListener());
         }
      }

   }

   protected TextSelectionManager createTextSelectionManager(EventDispatcher var1) {
      return new TextSelectionManager(this, var1);
   }

   public TextSelectionManager getTextSelectionManager() {
      return this.textSelectionManager;
   }

   public void setSelectionOverlayColor(Color var1) {
      if (this.textSelectionManager != null) {
         this.textSelectionManager.setSelectionOverlayColor(var1);
      }

   }

   public Color getSelectionOverlayColor() {
      return this.textSelectionManager != null ? this.textSelectionManager.getSelectionOverlayColor() : null;
   }

   public void setSelectionOverlayStrokeColor(Color var1) {
      if (this.textSelectionManager != null) {
         this.textSelectionManager.setSelectionOverlayStrokeColor(var1);
      }

   }

   public Color getSelectionOverlayStrokeColor() {
      return this.textSelectionManager != null ? this.textSelectionManager.getSelectionOverlayStrokeColor() : null;
   }

   public void setSelectionOverlayXORMode(boolean var1) {
      if (this.textSelectionManager != null) {
         this.textSelectionManager.setSelectionOverlayXORMode(var1);
      }

   }

   public boolean isSelectionOverlayXORMode() {
      return this.textSelectionManager != null ? this.textSelectionManager.isSelectionOverlayXORMode() : false;
   }

   public void select(Mark var1, Mark var2) {
      if (this.textSelectionManager != null) {
         this.textSelectionManager.setSelection(var1, var2);
      }

   }

   public void deselectAll() {
      if (this.textSelectionManager != null) {
         this.textSelectionManager.clearSelection();
      }

   }

   public void setProgressivePaint(boolean var1) {
      if (this.progressivePaint != var1) {
         this.progressivePaint = var1;
         this.haltProgressivePaintThread();
      }

   }

   public boolean getProgressivePaint() {
      return this.progressivePaint;
   }

   public Rectangle getRenderRect() {
      Dimension var1 = this.getSize();
      return new Rectangle(0, 0, var1.width, var1.height);
   }

   public void immediateRepaint() {
      if (EventQueue.isDispatchThread()) {
         Rectangle var1 = this.getRenderRect();
         if (this.doubleBufferedRendering) {
            this.repaint(var1.x, var1.y, var1.width, var1.height);
         } else {
            this.paintImmediately(var1.x, var1.y, var1.width, var1.height);
         }
      } else {
         try {
            EventQueue.invokeAndWait(new Runnable() {
               public void run() {
                  Rectangle var1 = AbstractJGVTComponent.this.getRenderRect();
                  if (AbstractJGVTComponent.this.doubleBufferedRendering) {
                     AbstractJGVTComponent.this.repaint(var1.x, var1.y, var1.width, var1.height);
                  } else {
                     AbstractJGVTComponent.this.paintImmediately(var1.x, var1.y, var1.width, var1.height);
                  }

               }
            });
         } catch (Exception var2) {
         }
      }

   }

   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      Graphics2D var2 = (Graphics2D)var1;
      Rectangle var3 = this.getRenderRect();
      var2.setComposite(AlphaComposite.SrcOver);
      var2.setPaint(this.getBackground());
      var2.fillRect(var3.x, var3.y, var3.width, var3.height);
      if (this.image != null) {
         if (this.paintingTransform != null) {
            var2.transform(this.paintingTransform);
         }

         var2.drawRenderedImage(this.image, (AffineTransform)null);
         var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         Iterator var4 = this.overlays.iterator();

         while(var4.hasNext()) {
            ((Overlay)var4.next()).paint(var1);
         }
      }

   }

   public void setPaintingTransform(AffineTransform var1) {
      this.paintingTransform = var1;
      this.immediateRepaint();
   }

   public AffineTransform getPaintingTransform() {
      return this.paintingTransform;
   }

   public void setRenderingTransform(AffineTransform var1) {
      this.setRenderingTransform(var1, true);
   }

   public void setRenderingTransform(AffineTransform var1, boolean var2) {
      this.renderingTransform = new AffineTransform(var1);
      this.suspendInteractions = true;
      if (this.eventDispatcher != null) {
         try {
            this.eventDispatcher.setBaseTransform(this.renderingTransform.createInverse());
         } catch (NoninvertibleTransformException var6) {
            this.handleException(var6);
         }
      }

      if (this.jgvtListeners != null) {
         Iterator var3 = this.jgvtListeners.iterator();
         ComponentEvent var4 = new ComponentEvent(this, 1337);

         while(var3.hasNext()) {
            JGVTComponentListener var5 = (JGVTComponentListener)var3.next();
            var5.componentTransformChanged(var4);
         }
      }

      if (var2) {
         this.scheduleGVTRendering();
      }

   }

   public AffineTransform getInitialTransform() {
      return new AffineTransform(this.initialTransform);
   }

   public AffineTransform getRenderingTransform() {
      return new AffineTransform(this.renderingTransform);
   }

   public void setDoubleBufferedRendering(boolean var1) {
      this.doubleBufferedRendering = var1;
   }

   public boolean getDoubleBufferedRendering() {
      return this.doubleBufferedRendering;
   }

   public void addGVTTreeRendererListener(GVTTreeRendererListener var1) {
      this.gvtTreeRendererListeners.add(var1);
   }

   public void removeGVTTreeRendererListener(GVTTreeRendererListener var1) {
      this.gvtTreeRendererListeners.remove(var1);
   }

   public void flush() {
      this.renderer.flush();
   }

   public void flush(Rectangle var1) {
      this.renderer.flush(var1);
   }

   protected ImageRenderer createImageRenderer() {
      return this.rendererFactory.createStaticImageRenderer();
   }

   protected void renderGVTTree() {
      Rectangle var1 = this.getRenderRect();
      if (this.gvtRoot != null && var1.width > 0 && var1.height > 0) {
         if (this.renderer == null || this.renderer.getTree() != this.gvtRoot) {
            this.renderer = this.createImageRenderer();
            this.renderer.setTree(this.gvtRoot);
         }

         AffineTransform var2;
         try {
            var2 = this.renderingTransform.createInverse();
         } catch (NoninvertibleTransformException var5) {
            throw new IllegalStateException("NoninvertibleTransformEx:" + var5.getMessage());
         }

         Shape var3 = var2.createTransformedShape(var1);
         this.gvtTreeRenderer = new GVTTreeRenderer(this.renderer, this.renderingTransform, this.doubleBufferedRendering, var3, var1.width, var1.height);
         this.gvtTreeRenderer.setPriority(1);
         Iterator var4 = this.gvtTreeRendererListeners.iterator();

         while(var4.hasNext()) {
            this.gvtTreeRenderer.addGVTTreeRendererListener((GVTTreeRendererListener)var4.next());
         }

         if (this.eventDispatcher != null) {
            this.eventDispatcher.setEventDispatchEnabled(false);
         }

         this.gvtTreeRenderer.start();
      }
   }

   protected boolean computeRenderingTransform() {
      this.initialTransform = new AffineTransform();
      if (!this.initialTransform.equals(this.renderingTransform)) {
         this.setRenderingTransform(this.initialTransform, false);
         return true;
      } else {
         return false;
      }
   }

   protected boolean updateRenderingTransform() {
      return false;
   }

   protected void handleException(Exception var1) {
   }

   protected void releaseRenderingReferences() {
      this.eventDispatcher = null;
      if (this.textSelectionManager != null) {
         this.overlays.remove(this.textSelectionManager.getSelectionOverlay());
         this.textSelectionManager = null;
      }

      this.renderer = null;
      this.image = null;
      this.gvtRoot = null;
   }

   protected void scheduleGVTRendering() {
      if (this.gvtTreeRenderer != null) {
         this.needRender = true;
         this.gvtTreeRenderer.halt();
      } else {
         this.renderGVTTree();
      }

   }

   private void haltProgressivePaintThread() {
      if (this.progressivePaintThread != null) {
         this.progressivePaintThread.halt();
         this.progressivePaintThread = null;
      }

   }

   protected Listener createListener() {
      return new Listener();
   }

   protected class UnixTextSelectionListener extends SelectionAdapter {
      public void selectionDone(SelectionEvent var1) {
         if (AbstractJGVTComponent.this.useUnixTextSelection) {
            Object var2 = var1.getSelection();
            if (var2 instanceof CharacterIterator) {
               CharacterIterator var3 = (CharacterIterator)var2;
               SecurityManager var4 = System.getSecurityManager();
               if (var4 != null) {
                  try {
                     var4.checkSystemClipboardAccess();
                  } catch (SecurityException var8) {
                     return;
                  }
               }

               int var5 = var3.getEndIndex() - var3.getBeginIndex();
               if (var5 != 0) {
                  char[] var6 = new char[var5];
                  var6[0] = var3.first();

                  for(int var7 = 1; var7 < var6.length; ++var7) {
                     var6[var7] = var3.next();
                  }

                  final String var9 = new String(var6);
                  (new Thread() {
                     public void run() {
                        Clipboard var1 = Toolkit.getDefaultToolkit().getSystemClipboard();
                        StringSelection var2 = new StringSelection(var9);
                        var1.setContents(var2, var2);
                     }
                  }).start();
               }
            }
         }
      }
   }

   protected class Listener implements GVTTreeRendererListener, KeyListener, MouseListener, MouseMotionListener {
      boolean checkClick = false;
      boolean hadDrag = false;
      int startX;
      int startY;
      long startTime;
      long fakeClickTime;
      int MAX_DISP = 16;
      long CLICK_TIME = 200L;

      public void gvtRenderingPrepare(GVTTreeRendererEvent var1) {
         AbstractJGVTComponent.this.suspendInteractions = true;
         if (!AbstractJGVTComponent.this.progressivePaint && !AbstractJGVTComponent.this.doubleBufferedRendering) {
            AbstractJGVTComponent.this.image = null;
         }

      }

      public void gvtRenderingStarted(GVTTreeRendererEvent var1) {
         if (AbstractJGVTComponent.this.progressivePaint && !AbstractJGVTComponent.this.doubleBufferedRendering) {
            AbstractJGVTComponent.this.image = var1.getImage();
            AbstractJGVTComponent.this.progressivePaintThread = new HaltingThread() {
               public void run() {
                  final <undefinedtype> var1 = this;

                  try {
                     while(!hasBeenHalted()) {
                        EventQueue.invokeLater(new Runnable() {
                           public void run() {
                              if (AbstractJGVTComponent.this.progressivePaintThread == var1) {
                                 Rectangle var1x = AbstractJGVTComponent.this.getRenderRect();
                                 AbstractJGVTComponent.this.repaint(var1x.x, var1x.y, var1x.width, var1x.height);
                              }

                           }
                        });
                        sleep(200L);
                     }
                  } catch (InterruptedException var3) {
                  } catch (ThreadDeath var4) {
                     throw var4;
                  } catch (Throwable var5) {
                     var5.printStackTrace();
                  }

               }
            };
            AbstractJGVTComponent.this.progressivePaintThread.setPriority(2);
            AbstractJGVTComponent.this.progressivePaintThread.start();
         }

         if (!AbstractJGVTComponent.this.doubleBufferedRendering) {
            AbstractJGVTComponent.this.paintingTransform = null;
            AbstractJGVTComponent.this.suspendInteractions = false;
         }

      }

      public void gvtRenderingCompleted(GVTTreeRendererEvent var1) {
         AbstractJGVTComponent.this.haltProgressivePaintThread();
         if (AbstractJGVTComponent.this.doubleBufferedRendering) {
            AbstractJGVTComponent.this.paintingTransform = null;
            AbstractJGVTComponent.this.suspendInteractions = false;
         }

         AbstractJGVTComponent.this.gvtTreeRenderer = null;
         if (AbstractJGVTComponent.this.needRender) {
            AbstractJGVTComponent.this.renderGVTTree();
            AbstractJGVTComponent.this.needRender = false;
         } else {
            AbstractJGVTComponent.this.image = var1.getImage();
            AbstractJGVTComponent.this.immediateRepaint();
         }

         if (AbstractJGVTComponent.this.eventDispatcher != null) {
            AbstractJGVTComponent.this.eventDispatcher.setEventDispatchEnabled(true);
         }

      }

      public void gvtRenderingCancelled(GVTTreeRendererEvent var1) {
         this.renderingStopped();
      }

      public void gvtRenderingFailed(GVTTreeRendererEvent var1) {
         this.renderingStopped();
      }

      private void renderingStopped() {
         AbstractJGVTComponent.this.haltProgressivePaintThread();
         if (AbstractJGVTComponent.this.doubleBufferedRendering) {
            AbstractJGVTComponent.this.suspendInteractions = false;
         }

         AbstractJGVTComponent.this.gvtTreeRenderer = null;
         if (AbstractJGVTComponent.this.needRender) {
            AbstractJGVTComponent.this.renderGVTTree();
            AbstractJGVTComponent.this.needRender = false;
         } else {
            AbstractJGVTComponent.this.immediateRepaint();
         }

         if (AbstractJGVTComponent.this.eventDispatcher != null) {
            AbstractJGVTComponent.this.eventDispatcher.setEventDispatchEnabled(true);
         }

      }

      public void keyTyped(KeyEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.keyTyped(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchKeyTyped(var1);
         }

      }

      protected void dispatchKeyTyped(KeyEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.keyTyped(var1);
      }

      public void keyPressed(KeyEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.keyPressed(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchKeyPressed(var1);
         }

      }

      protected void dispatchKeyPressed(KeyEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.keyPressed(var1);
      }

      public void keyReleased(KeyEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.keyReleased(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchKeyReleased(var1);
         }

      }

      protected void dispatchKeyReleased(KeyEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.keyReleased(var1);
      }

      public void mouseClicked(MouseEvent var1) {
         if (this.fakeClickTime != var1.getWhen()) {
            this.handleMouseClicked(var1);
         }

      }

      public void handleMouseClicked(MouseEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mouseClicked(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseClicked(var1);
         }

      }

      protected void dispatchMouseClicked(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseClicked(var1);
      }

      public void mousePressed(MouseEvent var1) {
         this.startX = var1.getX();
         this.startY = var1.getY();
         this.startTime = var1.getWhen();
         this.checkClick = true;
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mousePressed(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMousePressed(var1);
         }

      }

      protected void dispatchMousePressed(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mousePressed(var1);
      }

      public void mouseReleased(MouseEvent var1) {
         if (this.checkClick && this.hadDrag) {
            int var2 = this.startX - var1.getX();
            int var3 = this.startY - var1.getY();
            long var4 = var1.getWhen();
            if (var2 * var2 + var3 * var3 < this.MAX_DISP && var4 - this.startTime < this.CLICK_TIME) {
               MouseEvent var6 = new MouseEvent(var1.getComponent(), 500, var1.getWhen(), var1.getModifiers(), var1.getX(), var1.getY(), var1.getClickCount(), var1.isPopupTrigger());
               this.fakeClickTime = var6.getWhen();
               this.handleMouseClicked(var6);
            }
         }

         this.checkClick = false;
         this.hadDrag = false;
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mouseReleased(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseReleased(var1);
         }

      }

      protected void dispatchMouseReleased(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseReleased(var1);
      }

      public void mouseEntered(MouseEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mouseEntered(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseEntered(var1);
         }

      }

      protected void dispatchMouseEntered(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseEntered(var1);
      }

      public void mouseExited(MouseEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mouseExited(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseExited(var1);
         }

      }

      protected void dispatchMouseExited(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseExited(var1);
      }

      public void mouseDragged(MouseEvent var1) {
         this.hadDrag = true;
         int var2 = this.startX - var1.getX();
         int var3 = this.startY - var1.getY();
         if (var2 * var2 + var3 * var3 > this.MAX_DISP) {
            this.checkClick = false;
         }

         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            AbstractJGVTComponent.this.interactor.mouseDragged(var1);
            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseDragged(var1);
         }

      }

      protected void dispatchMouseDragged(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseDragged(var1);
      }

      public void mouseMoved(MouseEvent var1) {
         this.selectInteractor(var1);
         if (AbstractJGVTComponent.this.interactor != null) {
            if (Platform.isOSX && AbstractJGVTComponent.this.interactor instanceof AbstractZoomInteractor) {
               this.mouseDragged(var1);
            } else {
               AbstractJGVTComponent.this.interactor.mouseMoved(var1);
            }

            this.deselectInteractor();
         } else if (AbstractJGVTComponent.this.eventDispatcher != null) {
            this.dispatchMouseMoved(var1);
         }

      }

      protected void dispatchMouseMoved(MouseEvent var1) {
         AbstractJGVTComponent.this.eventDispatcher.mouseMoved(var1);
      }

      protected void selectInteractor(InputEvent var1) {
         if (!AbstractJGVTComponent.this.disableInteractions && !AbstractJGVTComponent.this.suspendInteractions && AbstractJGVTComponent.this.interactor == null && AbstractJGVTComponent.this.gvtRoot != null) {
            Iterator var2 = AbstractJGVTComponent.this.interactors.iterator();

            while(var2.hasNext()) {
               Interactor var3 = (Interactor)var2.next();
               if (var3.startInteraction(var1)) {
                  AbstractJGVTComponent.this.interactor = var3;
                  break;
               }
            }
         }

      }

      protected void deselectInteractor() {
         if (AbstractJGVTComponent.this.interactor.endInteraction()) {
            AbstractJGVTComponent.this.interactor = null;
         }

      }
   }
}
