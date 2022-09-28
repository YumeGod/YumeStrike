package org.apache.xerces.jaxp;

import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class SAXParserFactoryImpl extends SAXParserFactory {
   private Hashtable features;
   private Schema grammar;
   private boolean isXIncludeAware;
   private boolean fSecureProcess = false;

   public SAXParser newSAXParser() throws ParserConfigurationException {
      try {
         SAXParserImpl var1 = new SAXParserImpl(this, this.features, this.fSecureProcess);
         return var1;
      } catch (SAXException var3) {
         throw new ParserConfigurationException(var3.getMessage());
      }
   }

   private SAXParserImpl newSAXParserImpl() throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      try {
         SAXParserImpl var1 = new SAXParserImpl(this, this.features);
         return var1;
      } catch (SAXNotSupportedException var5) {
         throw var5;
      } catch (SAXNotRecognizedException var6) {
         throw var6;
      } catch (SAXException var7) {
         throw new ParserConfigurationException(var7.getMessage());
      }
   }

   public void setFeature(String var1, boolean var2) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var1.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this.fSecureProcess = var2;
      } else {
         if (this.features == null) {
            this.features = new Hashtable();
         }

         this.features.put(var1, var2 ? Boolean.TRUE : Boolean.FALSE);

         try {
            this.newSAXParserImpl();
         } catch (SAXNotSupportedException var5) {
            this.features.remove(var1);
            throw var5;
         } catch (SAXNotRecognizedException var6) {
            this.features.remove(var1);
            throw var6;
         }
      }
   }

   public boolean getFeature(String var1) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var1.equals("http://javax.xml.XMLConstants/feature/secure-processing") ? this.fSecureProcess : this.newSAXParserImpl().getXMLReader().getFeature(var1);
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
}
