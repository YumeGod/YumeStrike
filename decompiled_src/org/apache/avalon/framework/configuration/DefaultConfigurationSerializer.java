package org.apache.avalon.framework.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

public class DefaultConfigurationSerializer {
   private SAXTransformerFactory m_tfactory;
   private Properties m_format = new Properties();

   public void setIndent(boolean indent) {
      if (indent) {
         this.m_format.put("indent", "yes");
      } else {
         this.m_format.put("indent", "no");
      }

   }

   protected ContentHandler createContentHandler(Result result) {
      try {
         TransformerHandler handler = this.getTransformerFactory().newTransformerHandler();
         this.m_format.put("method", "xml");
         handler.setResult(result);
         handler.getTransformer().setOutputProperties(this.m_format);
         return handler;
      } catch (Exception var3) {
         throw new RuntimeException(var3.toString());
      }
   }

   protected SAXTransformerFactory getTransformerFactory() {
      if (this.m_tfactory == null) {
         this.m_tfactory = (SAXTransformerFactory)TransformerFactory.newInstance();
      }

      return this.m_tfactory;
   }

   public void serialize(ContentHandler handler, Configuration source) throws SAXException, ConfigurationException {
      handler.startDocument();
      this.serializeElement(handler, new NamespaceSupport(), source);
      handler.endDocument();
   }

   protected void serializeElement(ContentHandler handler, NamespaceSupport namespaceSupport, Configuration element) throws SAXException, ConfigurationException {
      namespaceSupport.pushContext();
      AttributesImpl attr = new AttributesImpl();
      String[] attrNames = element.getAttributeNames();
      if (null != attrNames) {
         for(int i = 0; i < attrNames.length; ++i) {
            attr.addAttribute("", attrNames[i], attrNames[i], "CDATA", element.getAttribute(attrNames[i], ""));
         }
      }

      String nsURI = element.getNamespace();
      String nsPrefix = "";
      if (element instanceof AbstractConfiguration) {
         nsPrefix = ((AbstractConfiguration)element).getPrefix();
      }

      boolean nsWasDeclared = false;
      String existingURI = namespaceSupport.getURI(nsPrefix);
      if (existingURI == null || !existingURI.equals(nsURI)) {
         nsWasDeclared = true;
         if (!nsPrefix.equals("") || !nsURI.equals("")) {
            if (nsPrefix.equals("")) {
               attr.addAttribute("", "xmlns", "xmlns", "CDATA", nsURI);
            } else {
               attr.addAttribute("", "xmlns:" + nsPrefix, "xmlns:" + nsPrefix, "CDATA", nsURI);
            }
         }

         handler.startPrefixMapping(nsPrefix, nsURI);
         namespaceSupport.declarePrefix(nsPrefix, nsURI);
      }

      String localName = element.getName();
      String qName = element.getName();
      if (nsPrefix != null && nsPrefix.length() != 0) {
         qName = nsPrefix + ":" + localName;
      } else {
         qName = localName;
      }

      handler.startElement(nsURI, localName, qName, attr);
      String value = element.getValue((String)null);
      if (null == value) {
         Configuration[] children = element.getChildren();

         for(int i = 0; i < children.length; ++i) {
            this.serializeElement(handler, namespaceSupport, children[i]);
         }
      } else {
         handler.characters(value.toCharArray(), 0, value.length());
      }

      handler.endElement(nsURI, localName, qName);
      if (nsWasDeclared) {
         handler.endPrefixMapping(nsPrefix);
      }

      namespaceSupport.popContext();
   }

   public void serializeToFile(String filename, Configuration source) throws SAXException, IOException, ConfigurationException {
      this.serializeToFile(new File(filename), source);
   }

   public void serializeToFile(File file, Configuration source) throws SAXException, IOException, ConfigurationException {
      OutputStream outputStream = null;

      try {
         outputStream = new FileOutputStream(file);
         this.serialize((OutputStream)outputStream, source);
      } finally {
         if (outputStream != null) {
            outputStream.close();
         }

      }

   }

   public void serialize(OutputStream outputStream, Configuration source) throws SAXException, IOException, ConfigurationException {
      this.serialize(this.createContentHandler(new StreamResult(outputStream)), source);
   }

   public void serialize(String uri, Configuration source) throws SAXException, IOException, ConfigurationException {
      OutputStream outputStream = null;

      try {
         outputStream = (new URL(uri)).openConnection().getOutputStream();
         this.serialize(outputStream, source);
      } finally {
         if (outputStream != null) {
            outputStream.close();
         }

      }

   }

   public String serialize(Configuration source) throws SAXException, ConfigurationException {
      StringWriter writer = new StringWriter();
      this.serialize(this.createContentHandler(new StreamResult(writer)), source);
      return writer.toString();
   }
}
