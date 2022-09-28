package org.apache.batik.bridge.svg12;

import java.awt.Point;
import java.awt.geom.Point2D;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeEventSupport;
import org.apache.batik.bridge.FocusManager;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.dom.events.DOMKeyboardEvent;
import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.events.DOMTextEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.svg12.SVGOMWheelEvent;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.event.EventDispatcher;
import org.apache.batik.gvt.event.GraphicsNodeKeyEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseWheelEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseWheelListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

public abstract class SVG12BridgeEventSupport extends BridgeEventSupport {
   protected SVG12BridgeEventSupport() {
   }

   public static void addGVTListener(BridgeContext var0, Document var1) {
      UserAgent var2 = var0.getUserAgent();
      if (var2 != null) {
         EventDispatcher var3 = var2.getEventDispatcher();
         if (var3 != null) {
            Listener var4 = new Listener(var0, var2);
            var3.addGraphicsNodeMouseListener(var4);
            var3.addGraphicsNodeMouseWheelListener(var4);
            var3.addGraphicsNodeKeyListener(var4);
            BridgeEventSupport.GVTUnloadListener var5 = new BridgeEventSupport.GVTUnloadListener(var3, var4);
            NodeEventTarget var6 = (NodeEventTarget)var1;
            var6.addEventListenerNS("http://www.w3.org/2001/xml-events", "SVGUnload", var5, false, (Object)null);
            storeEventListenerNS(var0, var6, "http://www.w3.org/2001/xml-events", "SVGUnload", var5, false);
         }
      }

   }

   protected static class Listener extends BridgeEventSupport.Listener implements GraphicsNodeMouseWheelListener {
      protected SVG12BridgeContext ctx12;
      protected static String[][] IDENTIFIER_KEY_CODES = new String[256][];

      public Listener(BridgeContext var1, UserAgent var2) {
         super(var1, var2);
         this.ctx12 = (SVG12BridgeContext)var1;
      }

      public void keyPressed(GraphicsNodeKeyEvent var1) {
         if (!this.isDown) {
            this.isDown = true;
            this.dispatchKeyboardEvent("keydown", var1);
         }

         if (var1.getKeyChar() == '\uffff') {
            this.dispatchTextEvent(var1);
         }

      }

      public void keyReleased(GraphicsNodeKeyEvent var1) {
         this.dispatchKeyboardEvent("keyup", var1);
         this.isDown = false;
      }

      public void keyTyped(GraphicsNodeKeyEvent var1) {
         this.dispatchTextEvent(var1);
      }

      protected void dispatchKeyboardEvent(String var1, GraphicsNodeKeyEvent var2) {
         FocusManager var3 = this.context.getFocusManager();
         if (var3 != null) {
            Element var4 = (Element)var3.getCurrentEventTarget();
            if (var4 == null) {
               var4 = this.context.getDocument().getDocumentElement();
            }

            DocumentEvent var5 = (DocumentEvent)var4.getOwnerDocument();
            DOMKeyboardEvent var6 = (DOMKeyboardEvent)var5.createEvent("KeyboardEvent");
            String var7 = DOMUtilities.getModifiersList(var2.getLockState(), var2.getModifiers());
            var6.initKeyboardEventNS("http://www.w3.org/2001/xml-events", var1, true, true, (AbstractView)null, this.mapKeyCodeToIdentifier(var2.getKeyCode()), this.mapKeyLocation(var2.getKeyLocation()), var7);

            try {
               ((EventTarget)var4).dispatchEvent(var6);
            } catch (RuntimeException var9) {
               this.ua.displayError(var9);
            }

         }
      }

      protected void dispatchTextEvent(GraphicsNodeKeyEvent var1) {
         FocusManager var2 = this.context.getFocusManager();
         if (var2 != null) {
            Element var3 = (Element)var2.getCurrentEventTarget();
            if (var3 == null) {
               var3 = this.context.getDocument().getDocumentElement();
            }

            DocumentEvent var4 = (DocumentEvent)var3.getOwnerDocument();
            DOMTextEvent var5 = (DOMTextEvent)var4.createEvent("TextEvent");
            var5.initTextEventNS("http://www.w3.org/2001/xml-events", "textInput", true, true, (AbstractView)null, String.valueOf(var1.getKeyChar()));

            try {
               ((EventTarget)var3).dispatchEvent(var5);
            } catch (RuntimeException var7) {
               this.ua.displayError(var7);
            }

         }
      }

      protected int mapKeyLocation(int var1) {
         return var1 - 1;
      }

      protected static void putIdentifierKeyCode(String var0, int var1) {
         if (IDENTIFIER_KEY_CODES[var1 / 256] == null) {
            IDENTIFIER_KEY_CODES[var1 / 256] = new String[256];
         }

         IDENTIFIER_KEY_CODES[var1 / 256][var1 % 256] = var0;
      }

      protected String mapKeyCodeToIdentifier(int var1) {
         String[] var2 = IDENTIFIER_KEY_CODES[var1 / 256];
         return var2 == null ? "Unidentified" : var2[var1 % 256];
      }

      public void mouseWheelMoved(GraphicsNodeMouseWheelEvent var1) {
         Document var2 = this.context.getPrimaryBridgeContext().getDocument();
         Element var3 = var2.getDocumentElement();
         DocumentEvent var4 = (DocumentEvent)var2;
         SVGOMWheelEvent var5 = (SVGOMWheelEvent)var4.createEvent("WheelEvent");
         var5.initWheelEventNS("http://www.w3.org/2001/xml-events", "wheel", true, true, (AbstractView)null, var1.getWheelDelta());

         try {
            ((EventTarget)var3).dispatchEvent(var5);
         } catch (RuntimeException var7) {
            this.ua.displayError(var7);
         }

      }

      public void mouseEntered(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getGraphicsNode();
         Element var4 = this.getEventTarget(var3, new Point2D.Float(var1.getX(), var1.getY()));
         Element var5 = this.getRelatedElement(var1);
         int var6 = 0;
         if (var5 != null && var4 != null) {
            var6 = DefaultXBLManager.computeBubbleLimit(var4, var5);
         }

         this.dispatchMouseEvent("mouseover", var4, var5, var2, var1, true, var6);
      }

      public void mouseExited(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getRelatedNode();
         Element var4 = this.getEventTarget(var3, var2);
         if (this.lastTargetElement != null) {
            int var5 = 0;
            if (var4 != null) {
               var5 = DefaultXBLManager.computeBubbleLimit(this.lastTargetElement, var4);
            }

            this.dispatchMouseEvent("mouseout", this.lastTargetElement, var4, var2, var1, true, var5);
            this.lastTargetElement = null;
         }

      }

      public void mouseMoved(GraphicsNodeMouseEvent var1) {
         Point var2 = var1.getClientPoint();
         GraphicsNode var3 = var1.getGraphicsNode();
         Element var4 = this.getEventTarget(var3, var2);
         Element var5 = this.lastTargetElement;
         if (var5 != var4) {
            int var6;
            if (var5 != null) {
               var6 = 0;
               if (var4 != null) {
                  var6 = DefaultXBLManager.computeBubbleLimit(var5, var4);
               }

               this.dispatchMouseEvent("mouseout", var5, var4, var2, var1, true, var6);
            }

            if (var4 != null) {
               var6 = 0;
               if (var5 != null) {
                  var6 = DefaultXBLManager.computeBubbleLimit(var4, var5);
               }

               this.dispatchMouseEvent("mouseover", var4, var5, var2, var1, true, var6);
            }
         }

         this.dispatchMouseEvent("mousemove", var4, (Element)null, var2, var1, false, 0);
      }

      protected void dispatchMouseEvent(String var1, Element var2, Element var3, Point var4, GraphicsNodeMouseEvent var5, boolean var6) {
         this.dispatchMouseEvent(var1, var2, var3, var4, var5, var6, 0);
      }

      protected void dispatchMouseEvent(String var1, Element var2, Element var3, Point var4, GraphicsNodeMouseEvent var5, boolean var6, int var7) {
         if (this.ctx12.mouseCaptureTarget != null) {
            NodeEventTarget var8 = null;
            if (var2 != null) {
               for(var8 = (NodeEventTarget)var2; var8 != null && var8 != this.ctx12.mouseCaptureTarget; var8 = var8.getParentNodeEventTarget()) {
               }
            }

            if (var8 == null) {
               if (this.ctx12.mouseCaptureSendAll) {
                  var2 = (Element)this.ctx12.mouseCaptureTarget;
               } else {
                  var2 = null;
               }
            }
         }

         if (var2 != null) {
            Point var18 = var5.getScreenPoint();
            DocumentEvent var9 = (DocumentEvent)var2.getOwnerDocument();
            DOMMouseEvent var10 = (DOMMouseEvent)var9.createEvent("MouseEvents");
            String var11 = DOMUtilities.getModifiersList(var5.getLockState(), var5.getModifiers());
            var10.initMouseEventNS("http://www.w3.org/2001/xml-events", var1, true, var6, (AbstractView)null, var5.getClickCount(), var18.x, var18.y, var4.x, var4.y, (short)(var5.getButton() - 1), (EventTarget)var3, var11);
            var10.setBubbleLimit(var7);

            try {
               ((EventTarget)var2).dispatchEvent(var10);
            } catch (RuntimeException var16) {
               this.ua.displayError(var16);
            } finally {
               this.lastTargetElement = var2;
            }
         }

         if (this.ctx12.mouseCaptureTarget != null && this.ctx12.mouseCaptureAutoRelease && "mouseup".equals(var1)) {
            this.ctx12.stopMouseCapture();
         }

      }

      static {
         putIdentifierKeyCode("U+0030", 48);
         putIdentifierKeyCode("U+0031", 49);
         putIdentifierKeyCode("U+0032", 50);
         putIdentifierKeyCode("U+0033", 51);
         putIdentifierKeyCode("U+0034", 52);
         putIdentifierKeyCode("U+0035", 53);
         putIdentifierKeyCode("U+0036", 54);
         putIdentifierKeyCode("U+0037", 55);
         putIdentifierKeyCode("U+0038", 56);
         putIdentifierKeyCode("U+0039", 57);
         putIdentifierKeyCode("Accept", 30);
         putIdentifierKeyCode("Again", 65481);
         putIdentifierKeyCode("U+0041", 65);
         putIdentifierKeyCode("AllCandidates", 256);
         putIdentifierKeyCode("Alphanumeric", 240);
         putIdentifierKeyCode("AltGraph", 65406);
         putIdentifierKeyCode("Alt", 18);
         putIdentifierKeyCode("U+0026", 150);
         putIdentifierKeyCode("U+0027", 222);
         putIdentifierKeyCode("U+002A", 151);
         putIdentifierKeyCode("U+0040", 512);
         putIdentifierKeyCode("U+005C", 92);
         putIdentifierKeyCode("U+0008", 8);
         putIdentifierKeyCode("U+0042", 66);
         putIdentifierKeyCode("U+0018", 3);
         putIdentifierKeyCode("CapsLock", 20);
         putIdentifierKeyCode("U+005E", 514);
         putIdentifierKeyCode("U+0043", 67);
         putIdentifierKeyCode("Clear", 12);
         putIdentifierKeyCode("CodeInput", 258);
         putIdentifierKeyCode("U+003A", 513);
         putIdentifierKeyCode("U+0301", 129);
         putIdentifierKeyCode("U+0306", 133);
         putIdentifierKeyCode("U+030C", 138);
         putIdentifierKeyCode("U+0327", 139);
         putIdentifierKeyCode("U+0302", 130);
         putIdentifierKeyCode("U+0308", 135);
         putIdentifierKeyCode("U+0307", 134);
         putIdentifierKeyCode("U+030B", 137);
         putIdentifierKeyCode("U+0300", 128);
         putIdentifierKeyCode("U+0345", 141);
         putIdentifierKeyCode("U+0304", 132);
         putIdentifierKeyCode("U+0328", 140);
         putIdentifierKeyCode("U+030A", 136);
         putIdentifierKeyCode("U+0303", 131);
         putIdentifierKeyCode("U+002C", 44);
         putIdentifierKeyCode("Compose", 65312);
         putIdentifierKeyCode("Control", 17);
         putIdentifierKeyCode("Convert", 28);
         putIdentifierKeyCode("Copy", 65485);
         putIdentifierKeyCode("Cut", 65489);
         putIdentifierKeyCode("U+007F", 127);
         putIdentifierKeyCode("U+0044", 68);
         putIdentifierKeyCode("U+0024", 515);
         putIdentifierKeyCode("Down", 40);
         putIdentifierKeyCode("U+0045", 69);
         putIdentifierKeyCode("End", 35);
         putIdentifierKeyCode("Enter", 10);
         putIdentifierKeyCode("U+003D", 61);
         putIdentifierKeyCode("U+001B", 27);
         putIdentifierKeyCode("U+20AC", 516);
         putIdentifierKeyCode("U+0021", 517);
         putIdentifierKeyCode("F10", 121);
         putIdentifierKeyCode("F11", 122);
         putIdentifierKeyCode("F12", 123);
         putIdentifierKeyCode("F13", 61440);
         putIdentifierKeyCode("F14", 61441);
         putIdentifierKeyCode("F15", 61442);
         putIdentifierKeyCode("F16", 61443);
         putIdentifierKeyCode("F17", 61444);
         putIdentifierKeyCode("F18", 61445);
         putIdentifierKeyCode("F19", 61446);
         putIdentifierKeyCode("F1", 112);
         putIdentifierKeyCode("F20", 61447);
         putIdentifierKeyCode("F21", 61448);
         putIdentifierKeyCode("F22", 61449);
         putIdentifierKeyCode("F23", 61450);
         putIdentifierKeyCode("F24", 61451);
         putIdentifierKeyCode("F2", 113);
         putIdentifierKeyCode("F3", 114);
         putIdentifierKeyCode("F4", 115);
         putIdentifierKeyCode("F5", 116);
         putIdentifierKeyCode("F6", 117);
         putIdentifierKeyCode("F7", 118);
         putIdentifierKeyCode("F8", 119);
         putIdentifierKeyCode("F9", 120);
         putIdentifierKeyCode("FinalMode", 24);
         putIdentifierKeyCode("Find", 65488);
         putIdentifierKeyCode("U+0046", 70);
         putIdentifierKeyCode("U+002E", 46);
         putIdentifierKeyCode("FullWidth", 243);
         putIdentifierKeyCode("U+0047", 71);
         putIdentifierKeyCode("U+0060", 192);
         putIdentifierKeyCode("U+003E", 160);
         putIdentifierKeyCode("HalfWidth", 244);
         putIdentifierKeyCode("U+0023", 520);
         putIdentifierKeyCode("Help", 156);
         putIdentifierKeyCode("Hiragana", 242);
         putIdentifierKeyCode("U+0048", 72);
         putIdentifierKeyCode("Home", 36);
         putIdentifierKeyCode("U+0049", 73);
         putIdentifierKeyCode("Insert", 155);
         putIdentifierKeyCode("U+00A1", 518);
         putIdentifierKeyCode("JapaneseHiragana", 260);
         putIdentifierKeyCode("JapaneseKatakana", 259);
         putIdentifierKeyCode("JapaneseRomaji", 261);
         putIdentifierKeyCode("U+004A", 74);
         putIdentifierKeyCode("KanaMode", 262);
         putIdentifierKeyCode("KanjiMode", 25);
         putIdentifierKeyCode("Katakana", 241);
         putIdentifierKeyCode("U+004B", 75);
         putIdentifierKeyCode("U+007B", 161);
         putIdentifierKeyCode("Left", 37);
         putIdentifierKeyCode("U+0028", 519);
         putIdentifierKeyCode("U+005B", 91);
         putIdentifierKeyCode("U+003C", 153);
         putIdentifierKeyCode("U+004C", 76);
         putIdentifierKeyCode("Meta", 157);
         putIdentifierKeyCode("Meta", 157);
         putIdentifierKeyCode("U+002D", 45);
         putIdentifierKeyCode("U+004D", 77);
         putIdentifierKeyCode("ModeChange", 31);
         putIdentifierKeyCode("U+004E", 78);
         putIdentifierKeyCode("Nonconvert", 29);
         putIdentifierKeyCode("NumLock", 144);
         putIdentifierKeyCode("NumLock", 144);
         putIdentifierKeyCode("U+004F", 79);
         putIdentifierKeyCode("PageDown", 34);
         putIdentifierKeyCode("PageUp", 33);
         putIdentifierKeyCode("Paste", 65487);
         putIdentifierKeyCode("Pause", 19);
         putIdentifierKeyCode("U+0050", 80);
         putIdentifierKeyCode("U+002B", 521);
         putIdentifierKeyCode("PreviousCandidate", 257);
         putIdentifierKeyCode("PrintScreen", 154);
         putIdentifierKeyCode("Props", 65482);
         putIdentifierKeyCode("U+0051", 81);
         putIdentifierKeyCode("U+0022", 152);
         putIdentifierKeyCode("U+007D", 162);
         putIdentifierKeyCode("Right", 39);
         putIdentifierKeyCode("U+0029", 522);
         putIdentifierKeyCode("U+005D", 93);
         putIdentifierKeyCode("U+0052", 82);
         putIdentifierKeyCode("RomanCharacters", 245);
         putIdentifierKeyCode("Scroll", 145);
         putIdentifierKeyCode("Scroll", 145);
         putIdentifierKeyCode("U+003B", 59);
         putIdentifierKeyCode("U+309A", 143);
         putIdentifierKeyCode("Shift", 16);
         putIdentifierKeyCode("Shift", 16);
         putIdentifierKeyCode("U+0053", 83);
         putIdentifierKeyCode("U+002F", 47);
         putIdentifierKeyCode("U+0020", 32);
         putIdentifierKeyCode("Stop", 65480);
         putIdentifierKeyCode("U+0009", 9);
         putIdentifierKeyCode("U+0054", 84);
         putIdentifierKeyCode("U+0055", 85);
         putIdentifierKeyCode("U+005F", 523);
         putIdentifierKeyCode("Undo", 65483);
         putIdentifierKeyCode("Unidentified", 0);
         putIdentifierKeyCode("Up", 38);
         putIdentifierKeyCode("U+0056", 86);
         putIdentifierKeyCode("U+3099", 142);
         putIdentifierKeyCode("U+0057", 87);
         putIdentifierKeyCode("U+0058", 88);
         putIdentifierKeyCode("U+0059", 89);
         putIdentifierKeyCode("U+005A", 90);
         putIdentifierKeyCode("U+0030", 96);
         putIdentifierKeyCode("U+0031", 97);
         putIdentifierKeyCode("U+0032", 98);
         putIdentifierKeyCode("U+0033", 99);
         putIdentifierKeyCode("U+0034", 100);
         putIdentifierKeyCode("U+0035", 101);
         putIdentifierKeyCode("U+0036", 102);
         putIdentifierKeyCode("U+0037", 103);
         putIdentifierKeyCode("U+0038", 104);
         putIdentifierKeyCode("U+0039", 105);
         putIdentifierKeyCode("U+002A", 106);
         putIdentifierKeyCode("Down", 225);
         putIdentifierKeyCode("U+002E", 110);
         putIdentifierKeyCode("Left", 226);
         putIdentifierKeyCode("U+002D", 109);
         putIdentifierKeyCode("U+002B", 107);
         putIdentifierKeyCode("Right", 227);
         putIdentifierKeyCode("U+002F", 111);
         putIdentifierKeyCode("Up", 224);
      }
   }
}
