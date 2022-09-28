package org.apache.xmlgraphics.xmp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMPStructure extends XMPComplexValue implements PropertyAccess {
   private Map properties = new HashMap();

   public Object getSimpleValue() {
      return null;
   }

   public void setProperty(XMPProperty prop) {
      this.properties.put(prop.getName(), prop);
   }

   public XMPProperty getProperty(String uri, String localName) {
      return this.getProperty(new QName(uri, localName));
   }

   public XMPProperty getValueProperty() {
      return this.getProperty(XMPConstants.RDF_VALUE);
   }

   public XMPProperty getProperty(QName name) {
      XMPProperty prop = (XMPProperty)this.properties.get(name);
      return prop;
   }

   public XMPProperty removeProperty(QName name) {
      return (XMPProperty)this.properties.remove(name);
   }

   public int getPropertyCount() {
      return this.properties.size();
   }

   public Iterator iterator() {
      return this.properties.keySet().iterator();
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      atts.clear();
      handler.startElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:Description", atts);
      Iterator props = this.properties.values().iterator();

      while(props.hasNext()) {
         XMPProperty prop = (XMPProperty)props.next();
         prop.toSAX(handler);
      }

      handler.endElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:Description");
   }

   public String toString() {
      return "XMP structure: " + this.getPropertyCount();
   }
}
