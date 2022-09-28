package org.apache.avalon.framework.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class DefaultConfigurationBuilder {
   private SAXConfigurationHandler m_handler;
   private XMLReader m_parser;

   public DefaultConfigurationBuilder() {
      this(false);
   }

   public DefaultConfigurationBuilder(boolean enableNamespaces) {
      try {
         SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
         if (enableNamespaces) {
            saxParserFactory.setNamespaceAware(true);
         }

         SAXParser saxParser = saxParserFactory.newSAXParser();
         this.setParser(saxParser.getXMLReader());
      } catch (Exception var4) {
         throw new Error("Unable to setup SAX parser" + var4);
      }
   }

   public DefaultConfigurationBuilder(XMLReader parser) {
      this.setParser(parser);
   }

   private void setParser(XMLReader parser) {
      this.m_parser = parser;
      this.m_handler = this.getHandler();
      this.m_parser.setContentHandler(this.m_handler);
      this.m_parser.setErrorHandler(this.m_handler);
   }

   protected SAXConfigurationHandler getHandler() {
      try {
         if (this.m_parser.getFeature("http://xml.org/sax/features/namespaces")) {
            return new NamespacedSAXConfigurationHandler();
         }
      } catch (Exception var2) {
      }

      return new SAXConfigurationHandler();
   }

   public Configuration buildFromFile(String filename) throws SAXException, IOException, ConfigurationException {
      return this.buildFromFile(new File(filename));
   }

   public Configuration buildFromFile(File file) throws SAXException, IOException, ConfigurationException {
      synchronized(this) {
         this.m_handler.clear();
         this.m_parser.parse(file.toURL().toString());
         return this.m_handler.getConfiguration();
      }
   }

   public Configuration build(InputStream inputStream) throws SAXException, IOException, ConfigurationException {
      return this.build(new InputSource(inputStream));
   }

   public Configuration build(InputStream inputStream, String systemId) throws SAXException, IOException, ConfigurationException {
      InputSource inputSource = new InputSource(inputStream);
      inputSource.setSystemId(systemId);
      return this.build(inputSource);
   }

   public Configuration build(String uri) throws SAXException, IOException, ConfigurationException {
      return this.build(new InputSource(uri));
   }

   public Configuration build(InputSource input) throws SAXException, IOException, ConfigurationException {
      synchronized(this) {
         this.m_handler.clear();
         this.m_parser.parse(input);
         return this.m_handler.getConfiguration();
      }
   }

   public void setEntityResolver(EntityResolver resolver) {
      synchronized(this) {
         this.m_parser.setEntityResolver(resolver);
      }
   }
}
