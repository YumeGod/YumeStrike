package org.apache.avalon.framework.configuration;

import java.util.ArrayList;
import java.util.BitSet;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXConfigurationHandler extends DefaultHandler implements ErrorHandler {
   private static final int EXPECTED_DEPTH = 5;
   private final ArrayList m_elements = new ArrayList(5);
   private final ArrayList m_values = new ArrayList(5);
   private final BitSet m_preserveSpace = new BitSet();
   private Configuration m_configuration;
   private Locator m_locator;

   public Configuration getConfiguration() {
      return this.m_configuration;
   }

   public void clear() {
      this.m_elements.clear();
      this.m_values.clear();
      this.m_locator = null;
   }

   public void setDocumentLocator(Locator locator) {
      this.m_locator = locator;
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

   }

   protected DefaultConfiguration createConfiguration(String localName, String location) {
      return new DefaultConfiguration(localName, location);
   }

   public void startElement(String namespaceURI, String localName, String rawName, Attributes attributes) throws SAXException {
      DefaultConfiguration configuration = this.createConfiguration(rawName, this.getLocationString());
      int depth = this.m_elements.size();
      boolean preserveSpace = false;
      if (depth > 0) {
         DefaultConfiguration parent = (DefaultConfiguration)this.m_elements.get(depth - 1);
         parent.addChild(configuration);
         preserveSpace = this.m_preserveSpace.get(depth - 1);
      }

      this.m_elements.add(configuration);
      this.m_values.add(new StringBuffer());
      int attributesSize = attributes.getLength();

      for(int i = 0; i < attributesSize; ++i) {
         String name = attributes.getQName(i);
         String value = attributes.getValue(i);
         if (!name.equals("xml:space")) {
            configuration.setAttribute(name, value);
         } else {
            preserveSpace = value.equals("preserve");
         }
      }

      if (preserveSpace) {
         this.m_preserveSpace.set(depth);
      } else {
         this.m_preserveSpace.clear(depth);
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
}
