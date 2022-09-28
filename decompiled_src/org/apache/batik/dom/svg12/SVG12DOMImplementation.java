package org.apache.batik.dom.svg12;

import java.net.URL;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.SVG12CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.GenericElement;
import org.apache.batik.dom.events.DocumentEventSupport;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

public class SVG12DOMImplementation extends SVGDOMImplementation {
   protected static HashTable svg12Factories;
   protected static HashTable xblFactories;
   protected static final DOMImplementation DOM_IMPLEMENTATION;

   public SVG12DOMImplementation() {
      this.factories = svg12Factories;
      this.registerFeature("CSS", "2.0");
      this.registerFeature("StyleSheets", "2.0");
      this.registerFeature("SVG", new String[]{"1.0", "1.1", "1.2"});
      this.registerFeature("SVGEvents", new String[]{"1.0", "1.1", "1.2"});
   }

   public CSSEngine createCSSEngine(AbstractStylableDocument var1, CSSContext var2, ExtendedParser var3, ValueManager[] var4, ShorthandManager[] var5) {
      ParsedURL var6 = ((SVGOMDocument)var1).getParsedURL();
      SVG12CSSEngine var7 = new SVG12CSSEngine(var1, var6, var3, var4, var5, var2);
      URL var8 = this.getClass().getResource("resources/UserAgentStyleSheet.css");
      if (var8 != null) {
         ParsedURL var9 = new ParsedURL(var8);
         InputSource var10 = new InputSource(var9.toString());
         var7.setUserAgentStyleSheet(var7.parseStyleSheet(var10, var9, "all"));
      }

      return var7;
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      SVG12OMDocument var4 = new SVG12OMDocument(var3, this);
      var4.setIsSVG12(true);
      if (var2 != null) {
         var4.appendChild(var4.createElementNS(var1, var2));
      }

      return var4;
   }

   public Element createElementNS(AbstractDocument var1, String var2, String var3) {
      if (var2 == null) {
         return new GenericElement(var3.intern(), var1);
      } else {
         String var4 = DOMUtilities.getLocalName(var3);
         String var5 = DOMUtilities.getPrefix(var3);
         ExtensibleDOMImplementation.ElementFactory var6;
         if ("http://www.w3.org/2000/svg".equals(var2)) {
            var6 = (ExtensibleDOMImplementation.ElementFactory)this.factories.get(var4);
            if (var6 != null) {
               return var6.create(var5, var1);
            }
         } else if ("http://www.w3.org/2004/xbl".equals(var2)) {
            var6 = (ExtensibleDOMImplementation.ElementFactory)xblFactories.get(var4);
            if (var6 != null) {
               return var6.create(var5, var1);
            }
         }

         if (this.customFactories != null) {
            var6 = (ExtensibleDOMImplementation.ElementFactory)this.customFactories.get(var2, var4);
            if (var6 != null) {
               return var6.create(var5, var1);
            }
         }

         return new BindableElement(var5, var1, var2, var4);
      }
   }

   public DocumentEventSupport createDocumentEventSupport() {
      DocumentEventSupport var1 = super.createDocumentEventSupport();
      var1.registerEventFactory("WheelEvent", new DocumentEventSupport.EventFactory() {
         public Event createEvent() {
            return new SVGOMWheelEvent();
         }
      });
      var1.registerEventFactory("ShadowTreeEvent", new DocumentEventSupport.EventFactory() {
         public Event createEvent() {
            return new XBLOMShadowTreeEvent();
         }
      });
      return var1;
   }

   public EventSupport createEventSupport(AbstractNode var1) {
      return new XBLEventSupport(var1);
   }

   public static DOMImplementation getDOMImplementation() {
      return DOM_IMPLEMENTATION;
   }

   static {
      svg12Factories = new HashTable(svg11Factories);
      svg12Factories.put("flowDiv", new FlowDivElementFactory());
      svg12Factories.put("flowLine", new FlowLineElementFactory());
      svg12Factories.put("flowPara", new FlowParaElementFactory());
      svg12Factories.put("flowRegionBreak", new FlowRegionBreakElementFactory());
      svg12Factories.put("flowRegion", new FlowRegionElementFactory());
      svg12Factories.put("flowRegionExclude", new FlowRegionExcludeElementFactory());
      svg12Factories.put("flowRoot", new FlowRootElementFactory());
      svg12Factories.put("flowSpan", new FlowSpanElementFactory());
      svg12Factories.put("handler", new HandlerElementFactory());
      svg12Factories.put("multiImage", new MultiImageElementFactory());
      svg12Factories.put("solidColor", new SolidColorElementFactory());
      svg12Factories.put("subImage", new SubImageElementFactory());
      svg12Factories.put("subImageRef", new SubImageRefElementFactory());
      xblFactories = new HashTable();
      xblFactories.put("xbl", new XBLXBLElementFactory());
      xblFactories.put("definition", new XBLDefinitionElementFactory());
      xblFactories.put("template", new XBLTemplateElementFactory());
      xblFactories.put("content", new XBLContentElementFactory());
      xblFactories.put("handlerGroup", new XBLHandlerGroupElementFactory());
      xblFactories.put("import", new XBLImportElementFactory());
      xblFactories.put("shadowTree", new XBLShadowTreeElementFactory());
      DOM_IMPLEMENTATION = new SVG12DOMImplementation();
   }

   protected static class XBLShadowTreeElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLShadowTreeElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMShadowTreeElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLImportElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLImportElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMImportElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLHandlerGroupElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLHandlerGroupElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMHandlerGroupElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLContentElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLContentElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMContentElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLTemplateElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLTemplateElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMTemplateElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLDefinitionElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLDefinitionElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMDefinitionElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class XBLXBLElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public XBLXBLElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new XBLOMXBLElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SubImageRefElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SubImageRefElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSubImageRefElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SubImageElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SubImageElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSubImageElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SolidColorElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SolidColorElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSolidColorElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MultiImageElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MultiImageElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMultiImageElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class HandlerElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public HandlerElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMHandlerElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowSpanElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowSpanElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowSpanElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRootElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRootElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowRootElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRegionExcludeElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRegionExcludeElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowRegionExcludeElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRegionElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRegionElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowRegionElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowRegionBreakElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowRegionBreakElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowRegionBreakElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowParaElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowParaElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowParaElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowLineElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowLineElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowLineElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FlowDivElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FlowDivElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFlowDivElement(var1, (AbstractDocument)var2);
      }
   }
}
