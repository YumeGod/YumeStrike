package org.apache.batik.extension.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.DomExtension;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BatikDomExtension implements DomExtension, BatikExtConstants {
   public float getPriority() {
      return 1.0F;
   }

   public String getAuthor() {
      return "Thomas DeWeese";
   }

   public String getContactAddress() {
      return "deweese@apache.org";
   }

   public String getURL() {
      return "http://xml.apache.org/batik";
   }

   public String getDescription() {
      return "Example extension to standard SVG shape tags";
   }

   public void registerTags(ExtensibleDOMImplementation var1) {
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "regularPolygon", new BatikRegularPolygonElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "star", new BatikStarElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "histogramNormalization", new BatikHistogramNormalizationElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "colorSwitch", new ColorSwitchElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowText", new FlowTextElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowDiv", new FlowDivElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowPara", new FlowParaElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowRegionBreak", new FlowRegionBreakElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowRegion", new FlowRegionElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowLine", new FlowLineElementFactory());
      var1.registerCustomElementFactory("http://xml.apache.org/batik/ext", "flowSpan", new FlowSpanElementFactory());
   }

   protected static class FlowSpanElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowSpanElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowSpanElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowLineElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowLineElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowLineElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRegionElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRegionElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowRegionElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRegionBreakElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRegionBreakElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowRegionBreakElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowParaElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowParaElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowParaElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowDivElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowDivElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowDivElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowTextElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowTextElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new FlowTextElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ColorSwitchElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ColorSwitchElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new ColorSwitchElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class BatikHistogramNormalizationElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public BatikHistogramNormalizationElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new BatikHistogramNormalizationElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class BatikStarElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public BatikStarElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new BatikStarElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class BatikRegularPolygonElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public BatikRegularPolygonElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new BatikRegularPolygonElement(var1, (AbstractDocument)var2);
      }
   }
}
