package org.apache.xerces.jaxp;

import java.util.Hashtable;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.util.SAXMessageFormatter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
   private Hashtable attributes;
   private Hashtable features;
   private Schema grammar;
   private boolean isXIncludeAware;
   private boolean fSecureProcess = false;

   public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      if (this.grammar != null && this.attributes != null) {
         if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
            throw new ParserConfigurationException(SAXMessageFormatter.formatMessage((Locale)null, "schema-already-specified", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaLanguage"}));
         }

         if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
            throw new ParserConfigurationException(SAXMessageFormatter.formatMessage((Locale)null, "schema-already-specified", new Object[]{"http://java.sun.com/xml/jaxp/properties/schemaSource"}));
         }
      }

      try {
         return new DocumentBuilderImpl(this, this.attributes, this.features, this.fSecureProcess);
      } catch (SAXException var2) {
         throw new ParserConfigurationException(var2.getMessage());
      }
   }

   public void setAttribute(String var1, Object var2) throws IllegalArgumentException {
      if (var2 == null) {
         if (this.attributes != null) {
            this.attributes.remove(var1);
         }

      } else {
         if (this.attributes == null) {
            this.attributes = new Hashtable();
         }

         this.attributes.put(var1, var2);

         try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
         } catch (Exception var4) {
            this.attributes.remove(var1);
            throw new IllegalArgumentException(var4.getMessage());
         }
      }
   }

   public Object getAttribute(String var1) throws IllegalArgumentException {
      Object var2;
      if (this.attributes != null) {
         var2 = this.attributes.get(var1);
         if (var2 != null) {
            return var2;
         }
      }

      var2 = null;

      try {
         DOMParser var7 = (new DocumentBuilderImpl(this, this.attributes, this.features)).getDOMParser();
         return var7.getProperty(var1);
      } catch (SAXException var6) {
         try {
            boolean var4 = ((DOMParser)var2).getFeature(var1);
            return var4 ? Boolean.TRUE : Boolean.FALSE;
         } catch (SAXException var5) {
            throw new IllegalArgumentException(var6.getMessage());
         }
      }
   }

   public Schema getSchema() {
      return this.grammar;
   }

   public void setSchema(Schema var1) {
      this.grammar = var1;
   }

   public boolean isXIncludeAware() {
      return this.isXIncludeAware;
   }

   public void setXIncludeAware(boolean var1) {
      this.isXIncludeAware = var1;
   }

   public boolean getFeature(String var1) throws ParserConfigurationException {
      if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         return this.fSecureProcess;
      } else {
         if (this.features != null) {
            Object var2 = this.features.get(var1);
            if (var2 != null) {
               return (Boolean)var2;
            }
         }

         try {
            DOMParser var4 = (new DocumentBuilderImpl(this, this.attributes, this.features)).getDOMParser();
            return var4.getFeature(var1);
         } catch (SAXException var3) {
            throw new ParserConfigurationException(var3.getMessage());
         }
      }
   }

   public void setFeature(String var1, boolean var2) throws ParserConfigurationException {
      if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.fSecureProcess = var2;
      } else {
         if (this.features == null) {
            this.features = new Hashtable();
         }

         this.features.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);

         try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
         } catch (SAXNotSupportedException var5) {
            this.features.remove(var1);
            throw new ParserConfigurationException(var5.getMessage());
         } catch (SAXNotRecognizedException var6) {
            this.features.remove(var1);
            throw new ParserConfigurationException(var6.getMessage());
         }
      }
   }
}
