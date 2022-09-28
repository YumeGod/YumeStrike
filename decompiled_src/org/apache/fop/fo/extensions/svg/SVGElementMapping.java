package org.apache.fop.fo.extensions.svg;

import java.util.HashMap;
import javax.xml.parsers.SAXParserFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

public class SVGElementMapping extends ElementMapping {
   public static final String URI = "http://www.w3.org/2000/svg";
   protected Log log;
   private boolean batikAvailable;

   public SVGElementMapping() {
      this.log = LogFactory.getLog(SVGElementMapping.class);
      this.batikAvailable = true;
      this.namespaceURI = "http://www.w3.org/2000/svg";
   }

   public DOMImplementation getDOMImplementation() {
      return SVGDOMImplementation.getDOMImplementation();
   }

   private String getAParserClassName() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         return factory.newSAXParser().getXMLReader().getClass().getName();
      } catch (Exception var2) {
         return null;
      }
   }

   protected void initialize() {
      if (this.foObjs == null && this.batikAvailable) {
         try {
            XMLResourceDescriptor.setXMLParserClassName(this.getAParserClassName());
            this.foObjs = new HashMap();
            this.foObjs.put("svg", new SE());
            this.foObjs.put("<default>", new SVGMaker());
         } catch (Throwable var2) {
            this.log.error("Error while initializing the Batik SVG extensions", var2);
            this.batikAvailable = false;
         }
      }

   }

   public String getStandardPrefix() {
      return "svg";
   }

   static class SE extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new SVGElement(parent);
      }
   }

   static class SVGMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new SVGObj(parent);
      }
   }
}
