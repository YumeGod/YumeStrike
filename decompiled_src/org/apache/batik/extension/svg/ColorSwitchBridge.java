package org.apache.batik.extension.svg;

import java.awt.Color;
import java.awt.Paint;
import org.apache.batik.bridge.AbstractSVGBridge;
import org.apache.batik.bridge.Bridge;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.PaintBridge;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ColorSwitchBridge extends AbstractSVGBridge implements PaintBridge, BatikExtConstants {
   public String getNamespaceURI() {
      return "http://xml.apache.org/batik/ext";
   }

   public String getLocalName() {
      return "colorSwitch";
   }

   public Paint createPaint(BridgeContext var1, Element var2, Element var3, GraphicsNode var4, float var5) {
      Element var6 = null;

      for(Node var7 = var2.getFirstChild(); var7 != null; var7 = var7.getNextSibling()) {
         if (var7.getNodeType() == 1) {
            Element var8 = (Element)var7;
            if (SVGUtilities.matchUserAgent(var8, var1.getUserAgent())) {
               var6 = var8;
               break;
            }
         }
      }

      if (var6 == null) {
         return Color.black;
      } else {
         Bridge var9 = var1.getBridge(var6);
         return (Paint)(var9 != null && var9 instanceof PaintBridge ? ((PaintBridge)var9).createPaint(var1, var6, var3, var4, var5) : Color.black);
      }
   }
}
