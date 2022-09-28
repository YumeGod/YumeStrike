package org.apache.batik.bridge;

import java.awt.Cursor;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg.SVGOMAElement;
import org.apache.batik.dom.svg.SVGOMAnimationElement;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.SVGAElement;

public class SVGAElementBridge extends SVGGElementBridge {
   protected AnchorListener al;
   protected CursorMouseOverListener bl;
   protected CursorMouseOutListener cl;

   public String getLocalName() {
      return "a";
   }

   public Bridge getInstance() {
      return new SVGAElementBridge();
   }

   public void buildGraphicsNode(BridgeContext var1, Element var2, GraphicsNode var3) {
      super.buildGraphicsNode(var1, var2, var3);
      if (var1.isInteractive()) {
         NodeEventTarget var4 = (NodeEventTarget)var2;
         CursorHolder var5 = new CursorHolder(CursorManager.DEFAULT_CURSOR);
         this.al = new AnchorListener(var1.getUserAgent(), var5);
         var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.al, false, (Object)null);
         var1.storeEventListenerNS(var4, "http://www.w3.org/2001/xml-events", "click", this.al, false);
         this.bl = new CursorMouseOverListener(var1.getUserAgent(), var5);
         var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.bl, false, (Object)null);
         var1.storeEventListenerNS(var4, "http://www.w3.org/2001/xml-events", "mouseover", this.bl, false);
         this.cl = new CursorMouseOutListener(var1.getUserAgent(), var5);
         var4.addEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.cl, false, (Object)null);
         var1.storeEventListenerNS(var4, "http://www.w3.org/2001/xml-events", "mouseout", this.cl, false);
      }

   }

   public void dispose() {
      NodeEventTarget var1 = (NodeEventTarget)this.e;
      if (this.al != null) {
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "click", this.al, false);
         this.al = null;
      }

      if (this.bl != null) {
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseover", this.bl, false);
         this.bl = null;
      }

      if (this.cl != null) {
         var1.removeEventListenerNS("http://www.w3.org/2001/xml-events", "mouseout", this.cl, false);
         this.cl = null;
      }

      super.dispose();
   }

   public boolean isComposite() {
      return true;
   }

   public static class MouseOutDefaultActionable implements Runnable {
      protected SVGAElement elt;
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public MouseOutDefaultActionable(SVGAElement var1, UserAgent var2, CursorHolder var3) {
         this.elt = var1;
         this.userAgent = var2;
         this.holder = var3;
      }

      public void run() {
         if (this.elt != null) {
            this.userAgent.displayMessage("");
         }

      }
   }

   public static class CursorMouseOutListener implements EventListener {
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public CursorMouseOutListener(UserAgent var1, CursorHolder var2) {
         this.userAgent = var1;
         this.holder = var2;
      }

      public void handleEvent(Event var1) {
         if (var1 instanceof AbstractEvent) {
            AbstractEvent var2 = (AbstractEvent)var1;
            List var3 = var2.getDefaultActions();
            if (var3 != null) {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Object var5 = var4.next();
                  if (var5 instanceof MouseOutDefaultActionable) {
                     return;
                  }
               }
            }

            SVGAElement var6 = (SVGAElement)var1.getCurrentTarget();
            var2.addDefaultAction(new MouseOutDefaultActionable(var6, this.userAgent, this.holder));
         }
      }
   }

   public static class MouseOverDefaultActionable implements Runnable {
      protected Element target;
      protected SVGAElement elt;
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public MouseOverDefaultActionable(Element var1, SVGAElement var2, UserAgent var3, CursorHolder var4) {
         this.target = var1;
         this.elt = var2;
         this.userAgent = var3;
         this.holder = var4;
      }

      public void run() {
         if (CSSUtilities.isAutoCursor(this.target)) {
            this.holder.holdCursor(CursorManager.DEFAULT_CURSOR);
            this.userAgent.setSVGCursor(CursorManager.ANCHOR_CURSOR);
         }

         if (this.elt != null) {
            String var1 = this.elt.getHref().getAnimVal();
            this.userAgent.displayMessage(var1);
         }

      }
   }

   public static class CursorMouseOverListener implements EventListener {
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public CursorMouseOverListener(UserAgent var1, CursorHolder var2) {
         this.userAgent = var1;
         this.holder = var2;
      }

      public void handleEvent(Event var1) {
         if (var1 instanceof AbstractEvent) {
            AbstractEvent var2 = (AbstractEvent)var1;
            List var3 = var2.getDefaultActions();
            if (var3 != null) {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Object var5 = var4.next();
                  if (var5 instanceof MouseOverDefaultActionable) {
                     return;
                  }
               }
            }

            Element var7 = (Element)var2.getTarget();
            SVGAElement var6 = (SVGAElement)var2.getCurrentTarget();
            var2.addDefaultAction(new MouseOverDefaultActionable(var7, var6, this.userAgent, this.holder));
         }
      }
   }

   public static class AnchorDefaultActionable implements Runnable {
      protected SVGOMAElement elt;
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public AnchorDefaultActionable(SVGAElement var1, UserAgent var2, CursorHolder var3) {
         this.elt = (SVGOMAElement)var1;
         this.userAgent = var2;
         this.holder = var3;
      }

      public void run() {
         this.userAgent.setSVGCursor(this.holder.getCursor());
         String var1 = this.elt.getHref().getAnimVal();
         ParsedURL var2 = new ParsedURL(this.elt.getBaseURI(), var1);
         SVGOMDocument var3 = (SVGOMDocument)this.elt.getOwnerDocument();
         ParsedURL var4 = var3.getParsedURL();
         if (var2.sameFile(var4)) {
            String var5 = var2.getRef();
            if (var5 != null && var5.length() != 0) {
               Element var6 = var3.getElementById(var5);
               if (var6 instanceof SVGOMAnimationElement) {
                  SVGOMAnimationElement var7 = (SVGOMAnimationElement)var6;
                  float var8 = var7.getHyperlinkBeginTime();
                  if (Float.isNaN(var8)) {
                     var7.beginElement();
                  } else {
                     var3.getRootElement().setCurrentTime(var8);
                  }

                  return;
               }
            }
         }

         this.userAgent.openLink(this.elt);
      }
   }

   public static class AnchorListener implements EventListener {
      protected UserAgent userAgent;
      protected CursorHolder holder;

      public AnchorListener(UserAgent var1, CursorHolder var2) {
         this.userAgent = var1;
         this.holder = var2;
      }

      public void handleEvent(Event var1) {
         if (var1 instanceof AbstractEvent) {
            AbstractEvent var2 = (AbstractEvent)var1;
            List var3 = var2.getDefaultActions();
            if (var3 != null) {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Object var5 = var4.next();
                  if (var5 instanceof AnchorDefaultActionable) {
                     return;
                  }
               }
            }

            SVGAElement var6 = (SVGAElement)var1.getCurrentTarget();
            var2.addDefaultAction(new AnchorDefaultActionable(var6, this.userAgent, this.holder));
         }
      }
   }

   public static class CursorHolder {
      Cursor cursor = null;

      public CursorHolder(Cursor var1) {
         this.cursor = var1;
      }

      public void holdCursor(Cursor var1) {
         this.cursor = var1;
      }

      public Cursor getCursor() {
         return this.cursor;
      }
   }
}
