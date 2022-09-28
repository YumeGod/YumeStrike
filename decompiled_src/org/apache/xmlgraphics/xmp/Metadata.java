package org.apache.xmlgraphics.xmp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.util.XMLizable;
import org.apache.xmlgraphics.xmp.merge.MergeRuleSet;
import org.apache.xmlgraphics.xmp.merge.PropertyMerger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class Metadata implements XMLizable, PropertyAccess {
   private Map properties = new HashMap();

   public void setProperty(XMPProperty prop) {
      this.properties.put(prop.getName(), prop);
   }

   public XMPProperty getProperty(String uri, String localName) {
      return this.getProperty(new QName(uri, localName));
   }

   public XMPProperty getProperty(QName name) {
      XMPProperty prop = (XMPProperty)this.properties.get(name);
      return prop;
   }

   public XMPProperty removeProperty(QName name) {
      return (XMPProperty)this.properties.remove(name);
   }

   public XMPProperty getValueProperty() {
      return this.getProperty(XMPConstants.RDF_VALUE);
   }

   public int getPropertyCount() {
      return this.properties.size();
   }

   public Iterator iterator() {
      return this.properties.keySet().iterator();
   }

   public void mergeInto(Metadata target) {
      XMPSchemaRegistry registry = XMPSchemaRegistry.getInstance();
      Iterator iter = this.properties.values().iterator();

      while(iter.hasNext()) {
         XMPProperty prop = (XMPProperty)iter.next();
         XMPSchema schema = registry.getSchema(prop.getNamespace());
         MergeRuleSet rules = schema.getDefaultMergeRuleSet();
         PropertyMerger merger = rules.getPropertyMergerFor(prop);
         merger.merge(prop, target);
      }

   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      handler.startPrefixMapping("x", "adobe:ns:meta/");
      handler.startElement("adobe:ns:meta/", "xmpmeta", "x:xmpmeta", atts);
      handler.startPrefixMapping("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
      handler.startElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:RDF", atts);
      Set namespaces = new HashSet();
      Iterator iter = this.properties.keySet().iterator();

      while(iter.hasNext()) {
         QName n = (QName)iter.next();
         namespaces.add(n.getNamespaceURI());
      }

      iter = namespaces.iterator();

      while(iter.hasNext()) {
         String ns = (String)iter.next();
         XMPSchema schema = XMPSchemaRegistry.getInstance().getSchema(ns);
         String prefix = schema != null ? schema.getPreferredPrefix() : null;
         boolean first = true;
         boolean empty = true;
         Iterator props = this.properties.values().iterator();

         while(props.hasNext()) {
            XMPProperty prop = (XMPProperty)props.next();
            if (prop.getName().getNamespaceURI().equals(ns)) {
               if (first) {
                  if (prefix == null) {
                     prefix = prop.getName().getPrefix();
                  }

                  atts.clear();
                  atts.addAttribute("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "about", "rdf:about", "CDATA", "");
                  if (prefix != null) {
                     handler.startPrefixMapping(prefix, ns);
                  }

                  handler.startElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:Description", atts);
                  empty = false;
                  first = false;
               }

               prop.toSAX(handler);
            }
         }

         if (!empty) {
            handler.endElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:Description");
            if (prefix != null) {
               handler.endPrefixMapping(prefix);
            }
         }
      }

      handler.endElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF", "rdf:RDF");
      handler.endPrefixMapping("rdf");
      handler.endElement("adobe:ns:meta/", "xmpmeta", "x:xmpmeta");
      handler.endPrefixMapping("x");
   }
}
