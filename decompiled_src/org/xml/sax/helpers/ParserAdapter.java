package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class ParserAdapter implements XMLReader, DocumentHandler {
   private static final String FEATURES = "http://xml.org/sax/features/";
   private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
   private static final String XMLNS_URIs = "http://xml.org/sax/features/xmlns-uris";
   private NamespaceSupport nsSupport;
   private AttributeListAdapter attAdapter;
   private boolean parsing = false;
   private String[] nameParts = new String[3];
   private Parser parser = null;
   private AttributesImpl atts = null;
   private boolean namespaces = true;
   private boolean prefixes = false;
   private boolean uris = false;
   Locator locator;
   EntityResolver entityResolver = null;
   DTDHandler dtdHandler = null;
   ContentHandler contentHandler = null;
   ErrorHandler errorHandler = null;

   public ParserAdapter() throws SAXException {
      String var1 = System.getProperty("org.xml.sax.parser");

      try {
         this.setup(ParserFactory.makeParser());
      } catch (ClassNotFoundException var7) {
         throw new SAXException("Cannot find SAX1 driver class " + var1, var7);
      } catch (IllegalAccessException var8) {
         throw new SAXException("SAX1 driver class " + var1 + " found but cannot be loaded", var8);
      } catch (InstantiationException var9) {
         throw new SAXException("SAX1 driver class " + var1 + " loaded but cannot be instantiated", var9);
      } catch (ClassCastException var10) {
         throw new SAXException("SAX1 driver class " + var1 + " does not implement org.xml.sax.Parser");
      } catch (NullPointerException var11) {
         throw new SAXException("System property org.xml.sax.parser not specified");
      }
   }

   public ParserAdapter(Parser var1) {
      this.setup(var1);
   }

   private void setup(Parser var1) {
      if (var1 == null) {
         throw new NullPointerException("Parser argument must not be null");
      } else {
         this.parser = var1;
         this.atts = new AttributesImpl();
         this.nsSupport = new NamespaceSupport();
         this.attAdapter = new AttributeListAdapter();
      }
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1.equals("http://xml.org/sax/features/namespaces")) {
         this.checkNotParsing("feature", var1);
         this.namespaces = var2;
         if (!this.namespaces && !this.prefixes) {
            this.prefixes = true;
         }
      } else if (var1.equals("http://xml.org/sax/features/namespace-prefixes")) {
         this.checkNotParsing("feature", var1);
         this.prefixes = var2;
         if (!this.prefixes && !this.namespaces) {
            this.namespaces = true;
         }
      } else {
         if (!var1.equals("http://xml.org/sax/features/xmlns-uris")) {
            throw new SAXNotRecognizedException("Feature: " + var1);
         }

         this.checkNotParsing("feature", var1);
         this.uris = var2;
      }

   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (var1.equals("http://xml.org/sax/features/namespaces")) {
         return this.namespaces;
      } else if (var1.equals("http://xml.org/sax/features/namespace-prefixes")) {
         return this.prefixes;
      } else if (var1.equals("http://xml.org/sax/features/xmlns-uris")) {
         return this.uris;
      } else {
         throw new SAXNotRecognizedException("Feature: " + var1);
      }
   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      throw new SAXNotRecognizedException("Property: " + var1);
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      throw new SAXNotRecognizedException("Property: " + var1);
   }

   public void setEntityResolver(EntityResolver var1) {
      this.entityResolver = var1;
   }

   public EntityResolver getEntityResolver() {
      return this.entityResolver;
   }

   public void setDTDHandler(DTDHandler var1) {
      this.dtdHandler = var1;
   }

   public DTDHandler getDTDHandler() {
      return this.dtdHandler;
   }

   public void setContentHandler(ContentHandler var1) {
      this.contentHandler = var1;
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void parse(String var1) throws IOException, SAXException {
      this.parse(new InputSource(var1));
   }

   public void parse(InputSource var1) throws IOException, SAXException {
      if (this.parsing) {
         throw new SAXException("Parser is already in use");
      } else {
         this.setupParser();
         this.parsing = true;

         try {
            this.parser.parse(var1);
         } finally {
            this.parsing = false;
         }

         this.parsing = false;
      }
   }

   public void setDocumentLocator(Locator var1) {
      this.locator = var1;
      if (this.contentHandler != null) {
         this.contentHandler.setDocumentLocator(var1);
      }

   }

   public void startDocument() throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.startDocument();
      }

   }

   public void endDocument() throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.endDocument();
      }

   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      Vector var3 = null;
      if (!this.namespaces) {
         if (this.contentHandler != null) {
            this.attAdapter.setAttributeList(var2);
            this.contentHandler.startElement("", "", var1.intern(), this.attAdapter);
         }

      } else {
         this.nsSupport.pushContext();
         int var4 = var2.getLength();

         String var7;
         String var9;
         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var2.getName(var5);
            if (var6.startsWith("xmlns")) {
               int var8 = var6.indexOf(58);
               if (var8 == -1 && var6.length() == 5) {
                  var7 = "";
               } else {
                  if (var8 != 5) {
                     continue;
                  }

                  var7 = var6.substring(var8 + 1);
               }

               var9 = var2.getValue(var5);
               if (!this.nsSupport.declarePrefix(var7, var9)) {
                  this.reportError("Illegal Namespace prefix: " + var7);
               } else if (this.contentHandler != null) {
                  this.contentHandler.startPrefixMapping(var7, var9);
               }
            }
         }

         this.atts.clear();

         for(int var13 = 0; var13 < var4; ++var13) {
            var7 = var2.getName(var13);
            String var14 = var2.getType(var13);
            var9 = var2.getValue(var13);
            if (var7.startsWith("xmlns")) {
               int var11 = var7.indexOf(58);
               String var10;
               if (var11 == -1 && var7.length() == 5) {
                  var10 = "";
               } else if (var11 != 5) {
                  var10 = null;
               } else {
                  var10 = var7.substring(6);
               }

               if (var10 != null) {
                  if (this.prefixes) {
                     if (this.uris) {
                        this.atts.addAttribute("http://www.w3.org/XML/1998/namespace", var10, var7.intern(), var14, var9);
                     } else {
                        this.atts.addAttribute("", "", var7.intern(), var14, var9);
                     }
                  }
                  continue;
               }
            }

            try {
               String[] var17 = this.processName(var7, true, true);
               this.atts.addAttribute(var17[0], var17[1], var17[2], var14, var9);
            } catch (SAXException var12) {
               if (var3 == null) {
                  var3 = new Vector();
               }

               var3.addElement(var12);
               this.atts.addAttribute("", var7, var7, var14, var9);
            }
         }

         if (var3 != null && this.errorHandler != null) {
            for(int var15 = 0; var15 < var3.size(); ++var15) {
               this.errorHandler.error((SAXParseException)var3.elementAt(var15));
            }
         }

         if (this.contentHandler != null) {
            String[] var16 = this.processName(var1, false, false);
            this.contentHandler.startElement(var16[0], var16[1], var16[2], this.atts);
         }

      }
   }

   public void endElement(String var1) throws SAXException {
      if (!this.namespaces) {
         if (this.contentHandler != null) {
            this.contentHandler.endElement("", "", var1.intern());
         }

      } else {
         String[] var2 = this.processName(var1, false, false);
         if (this.contentHandler != null) {
            this.contentHandler.endElement(var2[0], var2[1], var2[2]);
            Enumeration var3 = this.nsSupport.getDeclaredPrefixes();

            while(var3.hasMoreElements()) {
               String var4 = (String)var3.nextElement();
               this.contentHandler.endPrefixMapping(var4);
            }
         }

         this.nsSupport.popContext();
      }
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.characters(var1, var2, var3);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.ignorableWhitespace(var1, var2, var3);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.contentHandler != null) {
         this.contentHandler.processingInstruction(var1, var2);
      }

   }

   private void setupParser() {
      if (!this.prefixes && !this.namespaces) {
         throw new IllegalStateException();
      } else {
         this.nsSupport.reset();
         if (this.uris) {
            this.nsSupport.setNamespaceDeclUris(true);
         }

         if (this.entityResolver != null) {
            this.parser.setEntityResolver(this.entityResolver);
         }

         if (this.dtdHandler != null) {
            this.parser.setDTDHandler(this.dtdHandler);
         }

         if (this.errorHandler != null) {
            this.parser.setErrorHandler(this.errorHandler);
         }

         this.parser.setDocumentHandler(this);
         this.locator = null;
      }
   }

   private String[] processName(String var1, boolean var2, boolean var3) throws SAXException {
      String[] var4 = this.nsSupport.processName(var1, this.nameParts, var2);
      if (var4 == null) {
         if (var3) {
            throw this.makeException("Undeclared prefix: " + var1);
         }

         this.reportError("Undeclared prefix: " + var1);
         var4 = new String[3];
         var4[0] = var4[1] = "";
         var4[2] = var1.intern();
      }

      return var4;
   }

   void reportError(String var1) throws SAXException {
      if (this.errorHandler != null) {
         this.errorHandler.error(this.makeException(var1));
      }

   }

   private SAXParseException makeException(String var1) {
      return this.locator != null ? new SAXParseException(var1, this.locator) : new SAXParseException(var1, (String)null, (String)null, -1, -1);
   }

   private void checkNotParsing(String var1, String var2) throws SAXNotSupportedException {
      if (this.parsing) {
         throw new SAXNotSupportedException("Cannot change " + var1 + ' ' + var2 + " while parsing");
      }
   }

   final class AttributeListAdapter implements Attributes {
      private AttributeList qAtts;

      void setAttributeList(AttributeList var1) {
         this.qAtts = var1;
      }

      public int getLength() {
         return this.qAtts.getLength();
      }

      public String getURI(int var1) {
         return "";
      }

      public String getLocalName(int var1) {
         return "";
      }

      public String getQName(int var1) {
         return this.qAtts.getName(var1).intern();
      }

      public String getType(int var1) {
         return this.qAtts.getType(var1).intern();
      }

      public String getValue(int var1) {
         return this.qAtts.getValue(var1);
      }

      public int getIndex(String var1, String var2) {
         return -1;
      }

      public int getIndex(String var1) {
         int var2 = ParserAdapter.this.atts.getLength();

         for(int var3 = 0; var3 < var2; ++var3) {
            if (this.qAtts.getName(var3).equals(var1)) {
               return var3;
            }
         }

         return -1;
      }

      public String getType(String var1, String var2) {
         return null;
      }

      public String getType(String var1) {
         return this.qAtts.getType(var1).intern();
      }

      public String getValue(String var1, String var2) {
         return null;
      }

      public String getValue(String var1) {
         return this.qAtts.getValue(var1);
      }
   }
}
