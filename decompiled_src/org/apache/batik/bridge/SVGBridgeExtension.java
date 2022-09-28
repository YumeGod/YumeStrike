package org.apache.batik.bridge;

import java.util.Collections;
import java.util.Iterator;
import org.w3c.dom.Element;

public class SVGBridgeExtension implements BridgeExtension {
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
      return "The required SVG 1.0 tags";
   }

   public void registerTags(BridgeContext var1) {
      var1.putBridge(new SVGAElementBridge());
      var1.putBridge(new SVGAltGlyphElementBridge());
      var1.putBridge(new SVGCircleElementBridge());
      var1.putBridge(new SVGClipPathElementBridge());
      var1.putBridge(new SVGColorProfileElementBridge());
      var1.putBridge(new SVGDescElementBridge());
      var1.putBridge(new SVGEllipseElementBridge());
      var1.putBridge(new SVGFeBlendElementBridge());
      var1.putBridge(new SVGFeColorMatrixElementBridge());
      var1.putBridge(new SVGFeComponentTransferElementBridge());
      var1.putBridge(new SVGFeCompositeElementBridge());
      var1.putBridge(new SVGFeComponentTransferElementBridge.SVGFeFuncAElementBridge());
      var1.putBridge(new SVGFeComponentTransferElementBridge.SVGFeFuncRElementBridge());
      var1.putBridge(new SVGFeComponentTransferElementBridge.SVGFeFuncGElementBridge());
      var1.putBridge(new SVGFeComponentTransferElementBridge.SVGFeFuncBElementBridge());
      var1.putBridge(new SVGFeConvolveMatrixElementBridge());
      var1.putBridge(new SVGFeDiffuseLightingElementBridge());
      var1.putBridge(new SVGFeDisplacementMapElementBridge());
      var1.putBridge(new AbstractSVGLightingElementBridge.SVGFeDistantLightElementBridge());
      var1.putBridge(new SVGFeFloodElementBridge());
      var1.putBridge(new SVGFeGaussianBlurElementBridge());
      var1.putBridge(new SVGFeImageElementBridge());
      var1.putBridge(new SVGFeMergeElementBridge());
      var1.putBridge(new SVGFeMergeElementBridge.SVGFeMergeNodeElementBridge());
      var1.putBridge(new SVGFeMorphologyElementBridge());
      var1.putBridge(new SVGFeOffsetElementBridge());
      var1.putBridge(new AbstractSVGLightingElementBridge.SVGFePointLightElementBridge());
      var1.putBridge(new SVGFeSpecularLightingElementBridge());
      var1.putBridge(new AbstractSVGLightingElementBridge.SVGFeSpotLightElementBridge());
      var1.putBridge(new SVGFeTileElementBridge());
      var1.putBridge(new SVGFeTurbulenceElementBridge());
      var1.putBridge(new SVGFontElementBridge());
      var1.putBridge(new SVGFontFaceElementBridge());
      var1.putBridge(new SVGFilterElementBridge());
      var1.putBridge(new SVGGElementBridge());
      var1.putBridge(new SVGGlyphElementBridge());
      var1.putBridge(new SVGHKernElementBridge());
      var1.putBridge(new SVGImageElementBridge());
      var1.putBridge(new SVGLineElementBridge());
      var1.putBridge(new SVGLinearGradientElementBridge());
      var1.putBridge(new SVGMarkerElementBridge());
      var1.putBridge(new SVGMaskElementBridge());
      var1.putBridge(new SVGMissingGlyphElementBridge());
      var1.putBridge(new SVGPathElementBridge());
      var1.putBridge(new SVGPatternElementBridge());
      var1.putBridge(new SVGPolylineElementBridge());
      var1.putBridge(new SVGPolygonElementBridge());
      var1.putBridge(new SVGRadialGradientElementBridge());
      var1.putBridge(new SVGRectElementBridge());
      var1.putBridge(new AbstractSVGGradientElementBridge.SVGStopElementBridge());
      var1.putBridge(new SVGSVGElementBridge());
      var1.putBridge(new SVGSwitchElementBridge());
      var1.putBridge(new SVGTextElementBridge());
      var1.putBridge(new SVGTextPathElementBridge());
      var1.putBridge(new SVGTitleElementBridge());
      var1.putBridge(new SVGUseElementBridge());
      var1.putBridge(new SVGVKernElementBridge());
      var1.putBridge(new SVGSetElementBridge());
      var1.putBridge(new SVGAnimateElementBridge());
      var1.putBridge(new SVGAnimateColorElementBridge());
      var1.putBridge(new SVGAnimateTransformElementBridge());
      var1.putBridge(new SVGAnimateMotionElementBridge());
   }

   public boolean isDynamicElement(Element var1) {
      String var2 = var1.getNamespaceURI();
      if (!"http://www.w3.org/2000/svg".equals(var2)) {
         return false;
      } else {
         String var3 = var1.getLocalName();
         return var3.equals("script") || var3.startsWith("animate") || var3.equals("set");
      }
   }
}
