package org.apache.batik.bridge.svg12;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import org.apache.batik.bridge.AnimatableGenericSVGBridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.CSSUtilities;
import org.apache.batik.bridge.PaintBridge;
import org.apache.batik.bridge.PaintServer;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.ICCColor;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

public class SVGSolidColorElementBridge extends AnimatableGenericSVGBridge implements PaintBridge {
   public String getNamespaceURI() {
      return "http://www.w3.org/2000/svg";
   }

   public String getLocalName() {
      return "solidColor";
   }

   public Paint createPaint(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, float var5) {
      var5 = extractOpacity(var2, var5, var1);
      return extractColor(var2, var5, var1);
   }

   protected static float extractOpacity(Element var0, float var1, BridgeContext var2) {
      HashMap var3 = new HashMap();
      CSSEngine var4 = CSSUtilities.getCSSEngine(var0);
      int var5 = var4.getPropertyIndex("solid-opacity");

      while(true) {
         Value var6 = CSSUtilities.getComputedStyle(var0, var5);
         StyleMap var7 = ((CSSStylableElement)var0).getComputedStyleMap((String)null);
         if (!var7.isNullCascaded(var5)) {
            float var11 = PaintServer.convertOpacity(var6);
            return var1 * var11;
         }

         String var8 = XLinkSupport.getXLinkHref(var0);
         if (var8.length() == 0) {
            return var1;
         }

         SVGOMDocument var9 = (SVGOMDocument)var0.getOwnerDocument();
         ParsedURL var10 = new ParsedURL(var9.getURL(), var8);
         if (var3.containsKey(var10)) {
            throw new BridgeException(var2, var0, "xlink.href.circularDependencies", new Object[]{var8});
         }

         var3.put(var10, var10);
         var0 = var2.getReferencedElement(var0, var8);
      }
   }

   protected static Color extractColor(Element var0, float var1, BridgeContext var2) {
      HashMap var3 = new HashMap();
      CSSEngine var4 = CSSUtilities.getCSSEngine(var0);
      int var5 = var4.getPropertyIndex("solid-color");

      while(true) {
         Value var6 = CSSUtilities.getComputedStyle(var0, var5);
         StyleMap var7 = ((CSSStylableElement)var0).getComputedStyleMap((String)null);
         if (!var7.isNullCascaded(var5)) {
            if (var6.getCssValueType() == 1) {
               return PaintServer.convertColor(var6, var1);
            }

            return PaintServer.convertRGBICCColor(var0, var6.item(0), (ICCColor)var6.item(1), var1, var2);
         }

         String var8 = XLinkSupport.getXLinkHref(var0);
         if (var8.length() == 0) {
            return new Color(0.0F, 0.0F, 0.0F, var1);
         }

         SVGOMDocument var9 = (SVGOMDocument)var0.getOwnerDocument();
         ParsedURL var10 = new ParsedURL(var9.getURL(), var8);
         if (var3.containsKey(var10)) {
            throw new BridgeException(var2, var0, "xlink.href.circularDependencies", new Object[]{var8});
         }

         var3.put(var10, var10);
         var0 = var2.getReferencedElement(var0, var8);
      }
   }
}
