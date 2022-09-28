package org.apache.avalon.framework.configuration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

public class NamespacedSAXConfigurationHandler extends SAXConfigurationHandler {
   private static final int EXPECTED_DEPTH = 5;
   private final ArrayList m_elements = new ArrayList(5);
   private final ArrayList m_prefixes = new ArrayList(5);
   private final ArrayList m_values = new ArrayList(5);
   private final BitSet m_preserveSpace = new BitSet();
   private Configuration m_configuration;
   private Locator m_locator;
   private NamespaceSupport m_namespaceSupport = new NamespaceSupport();

   public Configuration getConfiguration() {
      return this.m_configuration;
   }

   public void clear() {
      this.m_elements.clear();
      Iterator i = this.m_prefixes.iterator();

      while(i.hasNext()) {
         ((ArrayList)i.next()).clear();
      }

      this.m_prefixes.clear();
      this.m_values.clear();
      this.m_locator = null;
   }

   public void setDocumentLocator(Locator locator) {
      this.m_locator = locator;
   }

   public void startDocument() throws SAXException {
      this.m_namespaceSupport.reset();
      super.startDocument();
   }

   public void endDocument() throws SAXException {
      super.endDocument();
      this.m_namespaceSupport.reset();
   }

   public void characters(char[] ch, int start, int end) throws SAXException {
      int depth = this.m_values.size() - 1;
      StringBuffer valueBuffer = (StringBuffer)this.m_values.get(depth);
      valueBuffer.append(ch, start, end);
   }

   public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
      int depth = this.m_elements.size() - 1;
      DefaultConfiguration finishedConfiguration = (DefaultConfiguration)this.m_elements.remove(depth);
      String accumulatedValue = ((StringBuffer)this.m_values.remove(depth)).toString();
      ArrayList prefixes = (ArrayList)this.m_prefixes.remove(depth);
      Iterator i = prefixes.iterator();

      while(i.hasNext()) {
         this.endPrefixMapping((String)i.next());
      }

      prefixes.clear();
      String finishedValue;
      if (finishedConfiguration.getChildren().length == 0) {
         if (this.m_preserveSpace.get(depth)) {
            finishedValue = accumulatedValue;
         } else if (0 == accumulatedValue.length()) {
            finishedValue = null;
         } else {
            finishedValue = accumulatedValue.trim();
         }

         finishedConfiguration.setValue(finishedValue);
      } else {
         finishedValue = accumulatedValue.trim();
         if (finishedValue.length() > 0) {
            throw new SAXException("Not allowed to define mixed content in the element " + finishedConfiguration.getName() + " at " + finishedConfiguration.getLocation());
         }
      }

      if (0 == depth) {
         this.m_configuration = finishedConfiguration;
      }

      this.m_namespaceSupport.popContext();
   }

   protected DefaultConfiguration createConfiguration(String localName, String namespaceURI, String location) {
      String prefix = this.m_namespaceSupport.getPrefix(namespaceURI);
      if (prefix == null) {
         prefix = "";
      }

      return new DefaultConfiguration(localName, location, namespaceURI, prefix);
   }

   public void startElement(String namespaceURI, String localName, String rawName, Attributes attributes) throws SAXException {
      this.m_namespaceSupport.pushContext();
      DefaultConfiguration configuration = this.createConfiguration(localName, namespaceURI, this.getLocationString());
      int depth = this.m_elements.size();
      boolean preserveSpace = false;
      if (depth > 0) {
         DefaultConfiguration parent = (DefaultConfiguration)this.m_elements.get(depth - 1);
         parent.addChild(configuration);
         preserveSpace = this.m_preserveSpace.get(depth - 1);
      }

      this.m_elements.add(configuration);
      this.m_values.add(new StringBuffer());
      ArrayList prefixes = new ArrayList();
      AttributesImpl componentAttr = new AttributesImpl();

      int attributesSize;
      for(attributesSize = 0; attributesSize < attributes.getLength(); ++attributesSize) {
         if (attributes.getQName(attributesSize).startsWith("xmlns")) {
            prefixes.add(attributes.getLocalName(attributesSize));
            this.startPrefixMapping(attributes.getLocalName(attributesSize), attributes.getValue(attributesSize));
         } else if (attributes.getQName(attributesSize).equals("xml:space")) {
            preserveSpace = attributes.getValue(attributesSize).equals("preserve");
         } else {
            componentAttr.addAttribute(attributes.getURI(attributesSize), attributes.getLocalName(attributesSize), attributes.getQName(attributesSize), attributes.getType(attributesSize), attributes.getValue(attributesSize));
         }
      }

      if (preserveSpace) {
         this.m_preserveSpace.set(depth);
      } else {
         this.m_preserveSpace.clear(depth);
      }

      this.m_prefixes.add(prefixes);
      attributesSize = componentAttr.getLength();

      for(int i = 0; i < attributesSize; ++i) {
         String name = componentAttr.getQName(i);
         String value = componentAttr.getValue(i);
         configuration.setAttribute(name, value);
      }

   }

   public void error(SAXParseException exception) throws SAXException {
      throw exception;
   }

   public void warning(SAXParseException exception) throws SAXException {
      throw exception;
   }

   public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
   }

   protected String getLocationString() {
      if (null == this.m_locator) {
         return "Unknown";
      } else {
         int columnNumber = this.m_locator.getColumnNumber();
         return this.m_locator.getSystemId() + ":" + this.m_locator.getLineNumber() + (columnNumber >= 0 ? ":" + columnNumber : "");
      }
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.m_namespaceSupport.declarePrefix(prefix, uri);
      super.startPrefixMapping(prefix, uri);
   }
}
