package org.apache.batik.swing;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.swing.gvt.AbstractImageZoomInteractor;
import org.apache.batik.swing.gvt.AbstractJGVTComponent;
import org.apache.batik.swing.gvt.AbstractPanInteractor;
import org.apache.batik.swing.gvt.AbstractResetTransformInteractor;
import org.apache.batik.swing.gvt.AbstractRotateInteractor;
import org.apache.batik.swing.gvt.AbstractZoomInteractor;
import org.apache.batik.swing.gvt.Interactor;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGUserAgent;
import org.apache.batik.util.XMLConstants;
import org.apache.batik.util.gui.JErrorPane;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;

public class JSVGCanvas extends JSVGComponent {
   public static final String SCROLL_RIGHT_ACTION = "ScrollRight";
   public static final String SCROLL_LEFT_ACTION = "ScrollLeft";
   public static final String SCROLL_UP_ACTION = "ScrollUp";
   public static final String SCROLL_DOWN_ACTION = "ScrollDown";
   public static final String FAST_SCROLL_RIGHT_ACTION = "FastScrollRight";
   public static final String FAST_SCROLL_LEFT_ACTION = "FastScrollLeft";
   public static final String FAST_SCROLL_UP_ACTION = "FastScrollUp";
   public static final String FAST_SCROLL_DOWN_ACTION = "FastScrollDown";
   public static final String ZOOM_IN_ACTION = "ZoomIn";
   public static final String ZOOM_OUT_ACTION = "ZoomOut";
   public static final String RESET_TRANSFORM_ACTION = "ResetTransform";
   private boolean isZoomInteractorEnabled;
   private boolean isImageZoomInteractorEnabled;
   private boolean isPanInteractorEnabled;
   private boolean isRotateInteractorEnabled;
   private boolean isResetTransformInteractorEnabled;
   protected PropertyChangeSupport pcs;
   protected String uri;
   protected LocationListener locationListener;
   protected Map toolTipMap;
   protected EventListener toolTipListener;
   protected EventTarget lastTarget;
   protected Map toolTipDocs;
   protected static final Object MAP_TOKEN = new Object();
   protected long lastToolTipEventTimeStamp;
   protected EventTarget lastToolTipEventTarget;
   protected Interactor zoomInteractor;
   protected Interactor imageZoomInteractor;
   protected Interactor panInteractor;
   protected Interactor rotateInteractor;
   protected Interactor resetTransformInteractor;

   public JSVGCanvas() {
      this((SVGUserAgent)null, true, true);
      this.addMouseMotionListener(this.locationListener);
   }

   public JSVGCanvas(SVGUserAgent var1, boolean var2, boolean var3) {
      super(var1, var2, var3);
      this.isZoomInteractorEnabled = true;
      this.isImageZoomInteractorEnabled = true;
      this.isPanInteractorEnabled = true;
      this.isRotateInteractorEnabled = true;
      this.isResetTransformInteractorEnabled = true;
      this.pcs = new PropertyChangeSupport(this);
      this.locationListener = new LocationListener();
      this.toolTipMap = null;
      this.toolTipListener = new ToolTipModifier();
      this.lastTarget = null;
      this.toolTipDocs = null;
      this.zoomInteractor = new AbstractZoomInteractor() {
         public boolean startInteraction(InputEvent var1) {
            int var2 = var1.getModifiers();
            return var1.getID() == 501 && (var2 & 16) != 0 && (var2 & 2) != 0;
         }
      };
      this.imageZoomInteractor = new AbstractImageZoomInteractor() {
         public boolean startInteraction(InputEvent var1) {
            int var2 = var1.getModifiers();
            return var1.getID() == 501 && (var2 & 4) != 0 && (var2 & 1) != 0;
         }
      };
      this.panInteractor = new AbstractPanInteractor() {
         public boolean startInteraction(InputEvent var1) {
            int var2 = var1.getModifiers();
            return var1.getID() == 501 && (var2 & 16) != 0 && (var2 & 1) != 0;
         }
      };
      this.rotateInteractor = new AbstractRotateInteractor() {
         public boolean startInteraction(InputEvent var1) {
            int var2 = var1.getModifiers();
            return var1.getID() == 501 && (var2 & 4) != 0 && (var2 & 2) != 0;
         }
      };
      this.resetTransformInteractor = new AbstractResetTransformInteractor() {
         public boolean startInteraction(InputEvent var1) {
            int var2 = var1.getModifiers();
            return var1.getID() == 500 && (var2 & 4) != 0 && (var2 & 1) != 0 && (var2 & 2) != 0;
         }
      };
      this.setPreferredSize(new Dimension(200, 200));
      this.setMinimumSize(new Dimension(100, 100));
      List var4 = this.getInteractors();
      var4.add(this.zoomInteractor);
      var4.add(this.imageZoomInteractor);
      var4.add(this.panInteractor);
      var4.add(this.rotateInteractor);
      var4.add(this.resetTransformInteractor);
      this.installActions();
      if (var2) {
         this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent var1) {
               JSVGCanvas.this.requestFocus();
            }
         });
         this.installKeyboardActions();
      }

      this.addMouseMotionListener(this.locationListener);
   }

   protected void installActions() {
      ActionMap var1 = this.getActionMap();
      var1.put("ScrollRight", new ScrollRightAction(10));
      var1.put("ScrollLeft", new ScrollLeftAction(10));
      var1.put("ScrollUp", new ScrollUpAction(10));
      var1.put("ScrollDown", new ScrollDownAction(10));
      var1.put("FastScrollRight", new ScrollRightAction(30));
      var1.put("FastScrollLeft", new ScrollLeftAction(30));
      var1.put("FastScrollUp", new ScrollUpAction(30));
      var1.put("FastScrollDown", new ScrollDownAction(30));
      var1.put("ZoomIn", new ZoomInAction());
      var1.put("ZoomOut", new ZoomOutAction());
      var1.put("ResetTransform", new ResetTransformAction());
   }

   public void setDisableInteractions(boolean var1) {
      super.setDisableInteractions(var1);
      ActionMap var2 = this.getActionMap();
      var2.get("ScrollRight").setEnabled(!var1);
      var2.get("ScrollLeft").setEnabled(!var1);
      var2.get("ScrollUp").setEnabled(!var1);
      var2.get("ScrollDown").setEnabled(!var1);
      var2.get("FastScrollRight").setEnabled(!var1);
      var2.get("FastScrollLeft").setEnabled(!var1);
      var2.get("FastScrollUp").setEnabled(!var1);
      var2.get("FastScrollDown").setEnabled(!var1);
      var2.get("ZoomIn").setEnabled(!var1);
      var2.get("ZoomOut").setEnabled(!var1);
      var2.get("ResetTransform").setEnabled(!var1);
   }

   protected void installKeyboardActions() {
      InputMap var1 = this.getInputMap(0);
      KeyStroke var2 = KeyStroke.getKeyStroke(39, 0);
      var1.put(var2, "ScrollRight");
      var2 = KeyStroke.getKeyStroke(37, 0);
      var1.put(var2, "ScrollLeft");
      var2 = KeyStroke.getKeyStroke(38, 0);
      var1.put(var2, "ScrollUp");
      var2 = KeyStroke.getKeyStroke(40, 0);
      var1.put(var2, "ScrollDown");
      var2 = KeyStroke.getKeyStroke(39, 1);
      var1.put(var2, "FastScrollRight");
      var2 = KeyStroke.getKeyStroke(37, 1);
      var1.put(var2, "FastScrollLeft");
      var2 = KeyStroke.getKeyStroke(38, 1);
      var1.put(var2, "FastScrollUp");
      var2 = KeyStroke.getKeyStroke(40, 1);
      var1.put(var2, "FastScrollDown");
      var2 = KeyStroke.getKeyStroke(73, 2);
      var1.put(var2, "ZoomIn");
      var2 = KeyStroke.getKeyStroke(79, 2);
      var1.put(var2, "ZoomOut");
      var2 = KeyStroke.getKeyStroke(84, 2);
      var1.put(var2, "ResetTransform");
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.pcs.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.pcs.removePropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.pcs.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.pcs.removePropertyChangeListener(var1, var2);
   }

   public void setEnableZoomInteractor(boolean var1) {
      if (this.isZoomInteractorEnabled != var1) {
         boolean var2 = this.isZoomInteractorEnabled;
         this.isZoomInteractorEnabled = var1;
         if (this.isZoomInteractorEnabled) {
            this.getInteractors().add(this.zoomInteractor);
         } else {
            this.getInteractors().remove(this.zoomInteractor);
         }

         this.pcs.firePropertyChange("enableZoomInteractor", var2, var1);
      }

   }

   public boolean getEnableZoomInteractor() {
      return this.isZoomInteractorEnabled;
   }

   public void setEnableImageZoomInteractor(boolean var1) {
      if (this.isImageZoomInteractorEnabled != var1) {
         boolean var2 = this.isImageZoomInteractorEnabled;
         this.isImageZoomInteractorEnabled = var1;
         if (this.isImageZoomInteractorEnabled) {
            this.getInteractors().add(this.imageZoomInteractor);
         } else {
            this.getInteractors().remove(this.imageZoomInteractor);
         }

         this.pcs.firePropertyChange("enableImageZoomInteractor", var2, var1);
      }

   }

   public boolean getEnableImageZoomInteractor() {
      return this.isImageZoomInteractorEnabled;
   }

   public void setEnablePanInteractor(boolean var1) {
      if (this.isPanInteractorEnabled != var1) {
         boolean var2 = this.isPanInteractorEnabled;
         this.isPanInteractorEnabled = var1;
         if (this.isPanInteractorEnabled) {
            this.getInteractors().add(this.panInteractor);
         } else {
            this.getInteractors().remove(this.panInteractor);
         }

         this.pcs.firePropertyChange("enablePanInteractor", var2, var1);
      }

   }

   public boolean getEnablePanInteractor() {
      return this.isPanInteractorEnabled;
   }

   public void setEnableRotateInteractor(boolean var1) {
      if (this.isRotateInteractorEnabled != var1) {
         boolean var2 = this.isRotateInteractorEnabled;
         this.isRotateInteractorEnabled = var1;
         if (this.isRotateInteractorEnabled) {
            this.getInteractors().add(this.rotateInteractor);
         } else {
            this.getInteractors().remove(this.rotateInteractor);
         }

         this.pcs.firePropertyChange("enableRotateInteractor", var2, var1);
      }

   }

   public boolean getEnableRotateInteractor() {
      return this.isRotateInteractorEnabled;
   }

   public void setEnableResetTransformInteractor(boolean var1) {
      if (this.isResetTransformInteractorEnabled != var1) {
         boolean var2 = this.isResetTransformInteractorEnabled;
         this.isResetTransformInteractorEnabled = var1;
         if (this.isResetTransformInteractorEnabled) {
            this.getInteractors().add(this.resetTransformInteractor);
         } else {
            this.getInteractors().remove(this.resetTransformInteractor);
         }

         this.pcs.firePropertyChange("enableResetTransformInteractor", var2, var1);
      }

   }

   public boolean getEnableResetTransformInteractor() {
      return this.isResetTransformInteractorEnabled;
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      String var2 = this.uri;
      this.uri = var1;
      if (this.uri != null) {
         this.loadSVGDocument(this.uri);
      } else {
         this.setSVGDocument((SVGDocument)null);
      }

      this.pcs.firePropertyChange("URI", var2, this.uri);
   }

   protected UserAgent createUserAgent() {
      return new CanvasUserAgent();
   }

   protected AbstractJGVTComponent.Listener createListener() {
      return new CanvasSVGListener();
   }

   protected void installSVGDocument(SVGDocument var1) {
      if (this.toolTipDocs != null) {
         Iterator var2 = this.toolTipDocs.keySet().iterator();

         while(var2.hasNext()) {
            SVGDocument var3 = (SVGDocument)var2.next();
            if (var3 != null) {
               NodeEventTarget var4 = (NodeEventTarget)var3.getRootElement();
               if (var4 != null) {
                  var4.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.toolTipListener, false);
                  var4.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.toolTipListener, false);
               }
            }
         }

         this.toolTipDocs = null;
      }

      this.lastTarget = null;
      if (this.toolTipMap != null) {
         this.toolTipMap.clear();
      }

      super.installSVGDocument(var1);
   }

   public void setLastToolTipEvent(long var1, EventTarget var3) {
      this.lastToolTipEventTimeStamp = var1;
      this.lastToolTipEventTarget = var3;
   }

   public boolean matchLastToolTipEvent(long var1, EventTarget var3) {
      return this.lastToolTipEventTimeStamp == var1 && this.lastToolTipEventTarget == var3;
   }

   protected class ToolTipRunnable implements Runnable {
      String theToolTip;

      public ToolTipRunnable(String var2) {
         this.theToolTip = var2;
      }

      public void run() {
         JSVGCanvas.this.setToolTipText(this.theToolTip);
         MouseEvent var1;
         if (this.theToolTip != null) {
            var1 = new MouseEvent(JSVGCanvas.this, 504, System.currentTimeMillis(), 0, JSVGCanvas.this.locationListener.getLastX(), JSVGCanvas.this.locationListener.getLastY(), 0, false);
            ToolTipManager.sharedInstance().mouseEntered(var1);
            var1 = new MouseEvent(JSVGCanvas.this, 503, System.currentTimeMillis(), 0, JSVGCanvas.this.locationListener.getLastX(), JSVGCanvas.this.locationListener.getLastY(), 0, false);
            ToolTipManager.sharedInstance().mouseMoved(var1);
         } else {
            var1 = new MouseEvent(JSVGCanvas.this, 503, System.currentTimeMillis(), 0, JSVGCanvas.this.locationListener.getLastX(), JSVGCanvas.this.locationListener.getLastY(), 0, false);
            ToolTipManager.sharedInstance().mouseMoved(var1);
         }

      }
   }

   protected class ToolTipModifier implements EventListener {
      protected CanvasUserAgent canvasUserAgent;

      public ToolTipModifier() {
      }

      public void handleEvent(Event var1) {
         if (!JSVGCanvas.this.matchLastToolTipEvent(var1.getTimeStamp(), var1.getTarget())) {
            JSVGCanvas.this.setLastToolTipEvent(var1.getTimeStamp(), var1.getTarget());
            EventTarget var2 = JSVGCanvas.this.lastTarget;
            if ("mouseover".equals(var1.getType())) {
               JSVGCanvas.this.lastTarget = var1.getTarget();
            } else if ("mouseout".equals(var1.getType())) {
               org.w3c.dom.events.MouseEvent var3 = (org.w3c.dom.events.MouseEvent)var1;
               JSVGCanvas.this.lastTarget = var3.getRelatedTarget();
            }

            if (JSVGCanvas.this.toolTipMap != null) {
               Object var6 = (Element)JSVGCanvas.this.lastTarget;

               Object var4;
               for(var4 = null; var6 != null; var6 = CSSEngine.getParentCSSStylableElement((Element)var6)) {
                  var4 = JSVGCanvas.this.toolTipMap.get(var6);
                  if (var4 != null) {
                     break;
                  }
               }

               String var5 = (String)var4;
               if (var2 != JSVGCanvas.this.lastTarget) {
                  EventQueue.invokeLater(JSVGCanvas.this.new ToolTipRunnable(var5));
               }
            }

         }
      }
   }

   protected class LocationListener extends MouseMotionAdapter {
      protected int lastX = 0;
      protected int lastY = 0;

      public LocationListener() {
      }

      public void mouseMoved(MouseEvent var1) {
         this.lastX = var1.getX();
         this.lastY = var1.getY();
      }

      public int getLastX() {
         return this.lastX;
      }

      public int getLastY() {
         return this.lastY;
      }
   }

   protected class CanvasUserAgent extends AbstractJSVGComponent.BridgeUserAgent implements XMLConstants {
      final String TOOLTIP_TITLE_ONLY = "JSVGCanvas.CanvasUserAgent.ToolTip.titleOnly";
      final String TOOLTIP_DESC_ONLY = "JSVGCanvas.CanvasUserAgent.ToolTip.descOnly";
      final String TOOLTIP_TITLE_AND_TEXT = "JSVGCanvas.CanvasUserAgent.ToolTip.titleAndDesc";

      protected CanvasUserAgent() {
         super();
      }

      public void handleElement(Element var1, Object var2) {
         super.handleElement(var1, var2);
         if (JSVGCanvas.this.isInteractive()) {
            if ("http://www.w3.org/2000/svg".equals(var1.getNamespaceURI())) {
               if (var1.getParentNode() != var1.getOwnerDocument().getDocumentElement()) {
                  Element var3;
                  if (var2 instanceof Element) {
                     var3 = (Element)var2;
                  } else {
                     var3 = (Element)var1.getParentNode();
                  }

                  Element var4 = null;
                  Element var5 = null;
                  if (var1.getLocalName().equals("title")) {
                     if (var2 == Boolean.TRUE) {
                        var5 = var1;
                     }

                     var4 = this.getPeerWithTag(var3, "http://www.w3.org/2000/svg", "desc");
                  } else if (var1.getLocalName().equals("desc")) {
                     if (var2 == Boolean.TRUE) {
                        var4 = var1;
                     }

                     var5 = this.getPeerWithTag(var3, "http://www.w3.org/2000/svg", "title");
                  }

                  String var6 = null;
                  if (var5 != null) {
                     var5.normalize();
                     if (var5.getFirstChild() != null) {
                        var6 = var5.getFirstChild().getNodeValue();
                     }
                  }

                  String var7 = null;
                  if (var4 != null) {
                     var4.normalize();
                     if (var4.getFirstChild() != null) {
                        var7 = var4.getFirstChild().getNodeValue();
                     }
                  }

                  final String var8;
                  if (var6 != null && var6.length() != 0) {
                     if (var7 != null && var7.length() != 0) {
                        var8 = Messages.formatMessage("JSVGCanvas.CanvasUserAgent.ToolTip.titleAndDesc", new Object[]{this.toFormattedHTML(var6), this.toFormattedHTML(var7)});
                     } else {
                        var8 = Messages.formatMessage("JSVGCanvas.CanvasUserAgent.ToolTip.titleOnly", new Object[]{this.toFormattedHTML(var6)});
                     }
                  } else if (var7 != null && var7.length() != 0) {
                     var8 = Messages.formatMessage("JSVGCanvas.CanvasUserAgent.ToolTip.descOnly", new Object[]{this.toFormattedHTML(var7)});
                  } else {
                     var8 = null;
                  }

                  if (var8 == null) {
                     this.removeToolTip(var3);
                  } else {
                     if (JSVGCanvas.this.lastTarget != var3) {
                        this.setToolTip(var3, var8);
                     } else {
                        Object var9 = null;
                        if (JSVGCanvas.this.toolTipMap != null) {
                           var9 = JSVGCanvas.this.toolTipMap.get(var3);
                           JSVGCanvas.this.toolTipMap.put(var3, var8);
                        }

                        if (var9 != null) {
                           EventQueue.invokeLater(new Runnable() {
                              public void run() {
                                 JSVGCanvas.this.setToolTipText(var8);
                                 MouseEvent var1 = new MouseEvent(JSVGCanvas.this, 503, System.currentTimeMillis(), 0, JSVGCanvas.this.locationListener.getLastX(), JSVGCanvas.this.locationListener.getLastY(), 0, false);
                                 ToolTipManager.sharedInstance().mouseMoved(var1);
                              }
                           });
                        } else {
                           EventQueue.invokeLater(JSVGCanvas.this.new ToolTipRunnable(var8));
                        }
                     }

                  }
               }
            }
         }
      }

      public String toFormattedHTML(String var1) {
         StringBuffer var2 = new StringBuffer(var1);
         this.replace(var2, '&', "&amp;");
         this.replace(var2, '<', "&lt;");
         this.replace(var2, '>', "&gt;");
         this.replace(var2, '"', "&quot;");
         this.replace(var2, '\n', "<br>");
         return var2.toString();
      }

      protected void replace(StringBuffer var1, char var2, String var3) {
         String var4 = var1.toString();
         int var5 = var4.length();

         while((var5 = var4.lastIndexOf(var2, var5 - 1)) != -1) {
            var1.deleteCharAt(var5);
            var1.insert(var5, var3);
         }

      }

      public Element getPeerWithTag(Element var1, String var2, String var3) {
         if (var1 == null) {
            return null;
         } else {
            for(Node var5 = var1.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
               if (var2.equals(var5.getNamespaceURI()) && var3.equals(var5.getLocalName()) && var5.getNodeType() == 1) {
                  return (Element)var5;
               }
            }

            return null;
         }
      }

      public boolean hasPeerWithTag(Element var1, String var2, String var3) {
         return this.getPeerWithTag(var1, var2, var3) != null;
      }

      public void setToolTip(Element var1, String var2) {
         if (JSVGCanvas.this.toolTipMap == null) {
            JSVGCanvas.this.toolTipMap = new WeakHashMap();
         }

         if (JSVGCanvas.this.toolTipDocs == null) {
            JSVGCanvas.this.toolTipDocs = new WeakHashMap();
         }

         SVGDocument var3 = (SVGDocument)var1.getOwnerDocument();
         if (JSVGCanvas.this.toolTipDocs.put(var3, JSVGCanvas.MAP_TOKEN) == null) {
            NodeEventTarget var4 = (NodeEventTarget)var3.getRootElement();
            var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", JSVGCanvas.this.toolTipListener, false, (Object)null);
            var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", JSVGCanvas.this.toolTipListener, false, (Object)null);
         }

         JSVGCanvas.this.toolTipMap.put(var1, var2);
         if (var1 == JSVGCanvas.this.lastTarget) {
            EventQueue.invokeLater(JSVGCanvas.this.new ToolTipRunnable(var2));
         }

      }

      public void removeToolTip(Element var1) {
         if (JSVGCanvas.this.toolTipMap != null) {
            JSVGCanvas.this.toolTipMap.remove(var1);
         }

         if (JSVGCanvas.this.lastTarget == var1) {
            EventQueue.invokeLater(JSVGCanvas.this.new ToolTipRunnable((String)null));
         }

      }

      public void displayError(String var1) {
         if (JSVGCanvas.this.svgUserAgent != null) {
            super.displayError(var1);
         } else {
            JOptionPane var2 = new JOptionPane(var1, 0);
            JDialog var3 = var2.createDialog(JSVGCanvas.this, "ERROR");
            var3.setModal(false);
            var3.setVisible(true);
         }

      }

      public void displayError(Exception var1) {
         if (JSVGCanvas.this.svgUserAgent != null) {
            super.displayError(var1);
         } else {
            JErrorPane var2 = new JErrorPane(var1, 0);
            JDialog var3 = var2.createDialog(JSVGCanvas.this, "ERROR");
            var3.setModal(false);
            var3.setVisible(true);
         }

      }
   }

   public class ScrollDownAction extends ScrollAction {
      public ScrollDownAction(int var2) {
         super(0.0, (double)(-var2));
      }
   }

   public class ScrollUpAction extends ScrollAction {
      public ScrollUpAction(int var2) {
         super(0.0, (double)var2);
      }
   }

   public class ScrollLeftAction extends ScrollAction {
      public ScrollLeftAction(int var2) {
         super((double)var2, 0.0);
      }
   }

   public class ScrollRightAction extends ScrollAction {
      public ScrollRightAction(int var2) {
         super((double)(-var2), 0.0);
      }
   }

   public class ScrollAction extends AffineAction {
      public ScrollAction(double var2, double var4) {
         super(AffineTransform.getTranslateInstance(var2, var4));
      }
   }

   public class RotateAction extends AffineAction {
      public RotateAction(double var2) {
         super(AffineTransform.getRotateInstance(var2));
      }
   }

   public class ZoomOutAction extends ZoomAction {
      ZoomOutAction() {
         super(0.5);
      }
   }

   public class ZoomInAction extends ZoomAction {
      ZoomInAction() {
         super(2.0);
      }
   }

   public class ZoomAction extends AffineAction {
      public ZoomAction(double var2) {
         super(AffineTransform.getScaleInstance(var2, var2));
      }

      public ZoomAction(double var2, double var4) {
         super(AffineTransform.getScaleInstance(var2, var4));
      }
   }

   public class AffineAction extends AbstractAction {
      AffineTransform at;

      public AffineAction(AffineTransform var2) {
         this.at = var2;
      }

      public void actionPerformed(ActionEvent var1) {
         if (JSVGCanvas.this.gvtRoot != null) {
            AffineTransform var2 = JSVGCanvas.this.getRenderingTransform();
            if (this.at != null) {
               Dimension var3 = JSVGCanvas.this.getSize();
               int var4 = var3.width / 2;
               int var5 = var3.height / 2;
               AffineTransform var6 = AffineTransform.getTranslateInstance((double)var4, (double)var5);
               var6.concatenate(this.at);
               var6.translate((double)(-var4), (double)(-var5));
               var6.concatenate(var2);
               JSVGCanvas.this.setRenderingTransform(var6);
            }

         }
      }
   }

   public class ResetTransformAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JSVGCanvas.this.fragmentIdentifier = null;
         JSVGCanvas.this.resetRenderingTransform();
      }
   }

   protected class CanvasSVGListener extends JSVGComponent.ExtendedSVGListener {
      protected CanvasSVGListener() {
         super();
      }

      public void documentLoadingStarted(SVGDocumentLoaderEvent var1) {
         super.documentLoadingStarted(var1);
         JSVGCanvas.this.setToolTipText((String)null);
      }
   }
}
