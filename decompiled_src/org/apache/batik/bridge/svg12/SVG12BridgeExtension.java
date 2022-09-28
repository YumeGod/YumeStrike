package org.apache.batik.bridge.svg12;

import java.util.Collections;
import java.util.Iterator;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.SVGBridgeExtension;
import org.w3c.dom.Element;

public class SVG12BridgeExtension extends SVGBridgeExtension {
   public float getPriority() {
      return 0.0F;
   }

   public Iterator getImplementedExtensions() {
      return Collections.EMPTY_LIST.iterator();
   }

   public String getAuthor() {
      return "The Apache Batik Team.";
   }

   public String getContactAddress() {
      return "batik-dev@xmlgraphics.apache.org";
   }

   public String getURL() {
      return "http://xml.apache.org/batik";
   }

   public String getDescription() {
      return "The required SVG 1.2 tags";
   }

   public void registerTags(BridgeContext var1) {
      super.registerTags(var1);
      var1.putBridge(new SVGFlowRootElementBridge());
      var1.putBridge(new SVGMultiImageElementBridge());
      var1.putBridge(new SVGSolidColorElementBridge());
      var1.putBridge(new SVG12TextElementBridge());
      var1.putBridge(new XBLShadowTreeElementBridge());
      var1.putBridge(new XBLContentElementBridge());
      var1.setDefaultBridge(new BindableElementBridge());
      var1.putReservedNamespaceURI((String)null);
      var1.putReservedNamespaceURI("http://www.w3.org/2000/svg");
      var1.putReservedNamespaceURI("http://www.w3.org/2004/xbl");
   }

   public boolean isDynamicElement(Element var1) {
      String var2 = var1.getNamespaceURI();
      if ("http://www.w3.org/2004/xbl".equals(var2)) {
         return true;
      } else if (!"http://www.w3.org/2000/svg".equals(var2)) {
         return false;
      } else {
         String var3 = var1.getLocalName();
         return var3.equals("script") || var3.equals("handler") || var3.startsWith("animate") || var3.equals("set");
      }
   }
}
