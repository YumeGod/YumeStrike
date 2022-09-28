package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractNode;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class SVGZoomAndPanSupport implements SVGConstants {
   protected SVGZoomAndPanSupport() {
   }

   public static void setZoomAndPan(Element var0, short var1) throws DOMException {
      switch (var1) {
         case 1:
            var0.setAttributeNS((String)null, "zoomAndPan", "disable");
            break;
         case 2:
            var0.setAttributeNS((String)null, "zoomAndPan", "magnify");
            break;
         default:
            throw ((AbstractNode)var0).createDOMException((short)13, "zoom.and.pan", new Object[]{new Integer(var1)});
      }

   }

   public static short getZoomAndPan(Element var0) {
      String var1 = var0.getAttributeNS((String)null, "zoomAndPan");
      return (short)(var1.equals("magnify") ? 2 : 1);
   }
}
