package org.apache.fop.fo.extensions.svg;

import java.util.HashMap;
import javax.xml.parsers.SAXParserFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.w3c.dom.DOMImplementation;

public class BatikExtensionElementMapping extends ElementMapping {
   public static final String URI = "http://xml.apache.org/batik/ext";
   private boolean batikAvail = true;

   public BatikExtensionElementMapping() {
      this.namespaceURI = "http://xml.apache.org/batik/ext";
   }

   public DOMImplementation getDOMImplementation() {
      return null;
   }

   private final String getAParserClassName() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         return factory.newSAXParser().getXMLReader().getClass().getName();
      } catch (Exception var2) {
         return null;
      }
   }

   protected void initialize() {
      if (this.foObjs == null && this.batikAvail) {
         try {
            XMLResourceDescriptor.setXMLParserClassName(this.getAParserClassName());
            this.foObjs = new HashMap();
            this.foObjs.put("batik", new SE());
            this.foObjs.put("<default>", new SVGMaker());
         } catch (Throwable var2) {
            this.batikAvail = false;
         }
      }

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
