package org.apache.batik.bridge;

import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.text.AttributedCharacterIterator;
import java.util.List;
import org.apache.batik.dom.events.DOMKeyEvent;
import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.event.GraphicsNodeKeyEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyListener;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseListener;
import org.apache.batik.gvt.renderer.StrokingTextPainter;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;
import org.apache.batik.gvt.text.TextHit;
import org.apache.batik.gvt.text.TextSpanLayout;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

public abstract class BridgeEventSupport implements SVGConstants {
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_ID;

   protected BridgeEventSupport() {
   }

   public static void addGVTListener(BridgeContext var0, Document var1) {
      UserAgent var2 = var0.getUserAgent();
      if (var2 != null) {
         EventDispatcher var3 = var2.getEventDispatcher();
         if (var3 != null) {
            Listener var4 = new Listener(var0, var2);
            var3.addGraphicsNodeMouseListener(var4);
            var3.addGraphicsNodeKeyListener(var4);
            GVTUnloadListener var5 = new GVTUnloadListener(var3, var4);
            NodeEventTarget var6 = (NodeEventTarget)var1;
            var6.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGUnload", var5, false, (Object)null);
            storeEventListenerNS(var0, var6, "http://www.w3.org/2001/xml-events", "SVGUnload", var5, false);
         }
      }

   }

   protected static void storeEventListener(BridgeContext var0, EventTarget var1, String var2, EventListener var3, boolean var4) {
      var0.storeEventListener(var1, var2, var3, var4);
   }

   protected static void storeEventListenerNS(BridgeContext var0, EventTarget var1, String var2, String var3, EventListener var4, boolean var5) {
      var0.storeEventListenerNS(var1, var2, var3, var4, var5);
   }

   static {
      TEXT_COMPOUND_ID = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_ID;
   }

   protected static class Listener implements GraphicsNodeMouseListener, GraphicsNodeKeyListener {
      protected BridgeContext context;
      protected UserAgent ua;
      protected Element lastTargetElement;
      protected boolean isDown;

      public Listener(BridgeContext var1, UserAgent var2) {
         this.context = var1;
         this.ua = var2;
      }

      public void keyPressed(GraphicsNodeKeyEvent var1) {
         if (!this.isDown) {
            this.isDown = true;
            this.dispatchKeyEvent("keydown", var1);
         }

         if (var1.getKeyChar() == '\uffff') {
            this.dispatchKeyEvent("keypress", var1);
         }

      }

      public void keyReleased(GraphicsNodeKeyEvent var1) {
         this.dispatchKeyEvent("keyup", var1);
         this.isDown = false;
      }

      public void keyTyped(GraphicsNodeKeyEvent var1) {
         this.dispatchKeyEvent("keypress", var1);
      }

      protected void dispatchKeyEvent(String var1, GraphicsNodeKeyEvent var2) {
         FocusManager var3 = this.context.getFocusManager();
         if (var3 != null) {
            Element var4 = (Element)var3.getCurrentEventTarget();
            if (var4 == null) {
               var4 = this.context.getDocument().getDocumentElement();
            }

            DocumentEvent var5 = (DocumentEvent)var4.getOwnerDocument();
            DOMKeyEvent var6 = (DOMKeyEvent)var5.createEvent("KeyEvents");
            var6.initKeyEvent(var1, true, true, var2.isControlDown(), var2.isAltDown(), var2.isShiftDown(), var2.isMetaDown(), this.mapKeyCode(var2.getKeyCode()), var2.getKeyChar(), (AbstractView)null);

            try {
               ((EventTarget)var4).dispatchEvent(var6);
            } catch (RuntimeException var8) {
               this.ua.displayError(var8);
            }

         }
      }

      protected final int mapKeyCode(int var1) {
         switch (var1) {
            case 10:
               return 13;
            case 262:
               return 0;
            case 263:
               return 0;
            default:
               return var1;
         }
      }

      public void mouseClicked(GraphicsNodeMouseEvent var1) {
         this.dispatchMouseEvent("click", var1, true);
      }

      public void mousePressed(GraphicsNodeMouseEvent var1) {
         this.dispatchMouseEvent("mousedown", var1, true);
      }

      public void mouseReleased(GraphicsNodeMouseEvent var1) {
         this.dispatchMouseEvent("mouseup", var1, true);
      }

      public void mouseEntered(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getGraphicsNode();
         Element var4 = this.getEventTarget(var3, new Point2D.Float(var1.getX(), var1.getY()));
         Element var5 = this.getRelatedElement(var1);
         this.dispatchMouseEvent("mouseover", var4, var5, var2, var1, true);
      }

      public void mouseExited(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getRelatedNode();
         Element var4 = this.getEventTarget(var3, var2);
         if (this.lastTargetElement != null) {
            this.dispatchMouseEvent("mouseout", this.lastTargetElement, var4, var2, var1, true);
            this.lastTargetElement = null;
         }

      }

      public void mouseDragged(GraphicsNodeMouseEvent var1) {
         this.dispatchMouseEvent("mousemove", var1, false);
      }

      public void mouseMoved(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getGraphicsNode();
         Element var4 = this.getEventTarget(var3, var2);
         Element var5 = this.lastTargetElement;
         if (var5 != var4) {
            if (var5 != null) {
               this.dispatchMouseEvent("mouseout", var5, var4, var2, var1, true);
            }

            if (var4 != null) {
               this.dispatchMouseEvent("mouseover", var4, var5, var2, var1, true);
            }
         }

         this.dispatchMouseEvent("mousemove", var4, (Element)null, var2, var1, false);
      }

      protected void dispatchMouseEvent(String var1, GraphicsNodeMouseEvent var2, boolean var3) {
         Point var4 = var2.getClientPoint();
         GraphicsNode var5 = var2.getGraphicsNode();
         Element var6 = this.getEventTarget(var5, new Point2D.Float(var2.getX(), var2.getY()));
         Element var7 = this.getRelatedElement(var2);
         this.dispatchMouseEvent(var1, var6, var7, var4, var2, var3);
      }

      protected void dispatchMouseEvent(String var1, Element var2, Element var3, Point var4, GraphicsNodeMouseEvent var5, boolean var6) {
         if (var2 != null) {
            Point var7 = var5.getScreenPoint();
            DocumentEvent var8 = (DocumentEvent)var2.getOwnerDocument();
            DOMMouseEvent var9 = (DOMMouseEvent)var8.createEvent("MouseEvents");
            String var10 = DOMUtilities.getModifiersList(var5.getLockState(), var5.getModifiers());
            var9.initMouseEventNS("http://www.w3.org/2001/xml-events", var1, true, var6, (AbstractView)null, var5.getClickCount(), var7.x, var7.y, var4.x, var4.y, (short)(var5.getButton() - 1), (EventTarget)var3, var10);

            try {
               ((EventTarget)var2).dispatchEvent(var9);
            } catch (RuntimeException var15) {
               this.ua.displayError(var15);
            } finally {
               this.lastTargetElement = var2;
            }

         }
      }

      protected Element getRelatedElement(GraphicsNodeMouseEvent var1) {
         GraphicsNode var2 = var1.getRelatedNode();
         Element var3 = null;
         if (var2 != null) {
            var3 = this.context.getElement(var2);
         }

         return var3;
      }

      protected Element getEventTarget(GraphicsNode var1, Point2D var2) {
         Element var3 = this.context.getElement(var1);
         if (var3 != null && var1 instanceof TextNode) {
            TextNode var4 = (TextNode)var1;
            List var5 = var4.getTextRuns();
            Point2D var6 = (Point2D)var2.clone();

            try {
               var1.getGlobalTransform().createInverse().transform(var6, var6);
            } catch (NoninvertibleTransformException var17) {
            }

            if (var5 != null) {
               for(int var7 = 0; var7 < var5.size(); ++var7) {
                  StrokingTextPainter.TextRun var8 = (StrokingTextPainter.TextRun)var5.get(var7);
                  AttributedCharacterIterator var9 = var8.getACI();
                  TextSpanLayout var10 = var8.getLayout();
                  float var11 = (float)var6.getX();
                  float var12 = (float)var6.getY();
                  TextHit var13 = var10.hitTestChar(var11, var12);
                  Rectangle2D var14 = var10.getBounds2D();
                  if (var13 != null && var14 != null && var14.contains((double)var11, (double)var12)) {
                     SoftReference var15 = (SoftReference)var9.getAttribute(BridgeEventSupport.TEXT_COMPOUND_ID);
                     Object var16 = var15.get();
                     if (var16 instanceof Element) {
                        return (Element)var16;
                     }
                  }
               }
            }
         }

         return var3;
      }
   }

   protected static class GVTUnloadListener implements EventListener {
      protected EventDispatcher dispatcher;
      protected Listener listener;

      public GVTUnloadListener(EventDispatcher var1, Listener var2) {
         this.dispatcher = var1;
         this.listener = var2;
      }

      public void handleEvent(Event var1) {
         this.dispatcher.removeGraphicsNodeMouseListener(this.listener);
         this.dispatcher.removeGraphicsNodeKeyListener(this.listener);
         NodeEventTarget var2 = (NodeEventTarget)var1.getTarget();
         var2.removeEventListenerNS("http://www.w3.org/2001/xml-events", "SVGUnload", this, false);
      }
   }
}
