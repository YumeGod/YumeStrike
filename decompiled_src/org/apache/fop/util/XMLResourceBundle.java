package org.apache.fop.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.WeakHashMap;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLResourceBundle extends ResourceBundle {
   private Properties resources = new Properties();
   private Locale locale;
   private static SAXTransformerFactory tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   private static final ResourceBundle MISSING = new MissingBundle();
   private static final ResourceBundle MISSINGBASE = new MissingBundle();
   private static Map cache = new WeakHashMap();

   public XMLResourceBundle(InputStream in) throws IOException {
      try {
         Transformer transformer = tFactory.newTransformer();
         StreamSource src = new StreamSource(in);
         SAXResult res = new SAXResult(new CatalogueHandler());
         transformer.transform(src, res);
      } catch (TransformerException var5) {
         throw new IOException("Error while parsing XML resource bundle: " + var5.getMessage());
      }
   }

   public static ResourceBundle getXMLBundle(String baseName, ClassLoader loader) throws MissingResourceException {
      return getXMLBundle(baseName, Locale.getDefault(), loader);
   }

   public static ResourceBundle getXMLBundle(String baseName, Locale locale, ClassLoader loader) throws MissingResourceException {
      if (loader == null) {
         throw new NullPointerException("loader must not be null");
      } else if (baseName == null) {
         throw new NullPointerException("baseName must not be null");
      } else {
         ResourceBundle bundle;
         if (!locale.equals(Locale.getDefault())) {
            bundle = handleGetXMLBundle(baseName, "_" + locale, false, loader);
            if (bundle != null) {
               return bundle;
            }
         }

         bundle = handleGetXMLBundle(baseName, "_" + Locale.getDefault(), true, loader);
         if (bundle != null) {
            return bundle;
         } else {
            throw new MissingResourceException(baseName + " (" + locale + ")", baseName + '_' + locale, (String)null);
         }
      }
   }

   private static ResourceBundle handleGetXMLBundle(String base, String locale, boolean loadBase, final ClassLoader loader) {
      XMLResourceBundle bundle = null;
      String bundleName = base + locale;
      Object cacheKey = loader != null ? loader : "null";
      Hashtable loaderCache;
      synchronized(cache) {
         loaderCache = (Hashtable)cache.get(cacheKey);
         if (loaderCache == null) {
            loaderCache = new Hashtable();
            cache.put(cacheKey, loaderCache);
         }
      }

      ResourceBundle result = (ResourceBundle)loaderCache.get(bundleName);
      final String extension;
      if (result != null) {
         if (result == MISSINGBASE) {
            return null;
         } else if (result == MISSING) {
            if (!loadBase) {
               return null;
            } else {
               extension = strip(locale);
               return extension == null ? null : handleGetXMLBundle(base, extension, loadBase, loader);
            }
         } else {
            return result;
         }
      } else {
         extension = bundleName.replace('.', '/') + ".xml";
         InputStream stream = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return loader == null ? ClassLoader.getSystemResourceAsStream(extension) : loader.getResourceAsStream(extension);
            }
         });
         if (stream != null) {
            try {
               try {
                  bundle = new XMLResourceBundle(stream);
               } finally {
                  stream.close();
               }

               bundle.setLocale(locale);
            } catch (IOException var17) {
               throw new MissingResourceException(var17.getMessage(), base, (String)null);
            }
         }

         String extension = strip(locale);
         ResourceBundle fallback;
         if (bundle != null) {
            if (extension != null) {
               fallback = handleGetXMLBundle(base, extension, true, loader);
               if (fallback != null) {
                  bundle.setParent(fallback);
               }
            }

            loaderCache.put(bundleName, bundle);
            return bundle;
         } else {
            if (extension != null) {
               fallback = handleGetXMLBundle(base, extension, loadBase, loader);
               if (fallback != null) {
                  loaderCache.put(bundleName, fallback);
                  return fallback;
               }
            }

            loaderCache.put(bundleName, loadBase ? MISSINGBASE : MISSING);
            return null;
         }
      }
   }

   private void setLocale(String name) {
      String language = "";
      String country = "";
      String variant = "";
      if (name.length() > 1) {
         int nextIndex = name.indexOf(95, 1);
         if (nextIndex == -1) {
            nextIndex = name.length();
         }

         language = name.substring(1, nextIndex);
         if (nextIndex + 1 < name.length()) {
            int index = nextIndex;
            nextIndex = name.indexOf(95, nextIndex + 1);
            if (nextIndex == -1) {
               nextIndex = name.length();
            }

            country = name.substring(index + 1, nextIndex);
            if (nextIndex + 1 < name.length()) {
               variant = name.substring(nextIndex + 1, name.length());
            }
         }
      }

      this.locale = new Locale(language, country, variant);
   }

   private static String strip(String name) {
      int index = name.lastIndexOf(95);
      return index != -1 ? name.substring(0, index) : null;
   }

   private Enumeration getLocalKeys() {
      return this.resources.propertyNames();
   }

   public Locale getLocale() {
      return this.locale;
   }

   public Enumeration getKeys() {
      return this.parent == null ? this.getLocalKeys() : new Enumeration() {
         private Enumeration local = XMLResourceBundle.this.getLocalKeys();
         private Enumeration pEnum;
         private Object nextElement;

         {
            this.pEnum = XMLResourceBundle.this.parent.getKeys();
         }

         private boolean findNext() {
            if (this.nextElement != null) {
               return true;
            } else {
               Object next;
               do {
                  if (!this.pEnum.hasMoreElements()) {
                     return false;
                  }

                  next = this.pEnum.nextElement();
               } while(XMLResourceBundle.this.resources.containsKey(next));

               this.nextElement = next;
               return true;
            }
         }

         public boolean hasMoreElements() {
            return this.local.hasMoreElements() ? true : this.findNext();
         }

         public Object nextElement() {
            if (this.local.hasMoreElements()) {
               return this.local.nextElement();
            } else if (this.findNext()) {
               Object result = this.nextElement;
               this.nextElement = null;
               return result;
            } else {
               return this.pEnum.nextElement();
            }
         }
      };
   }

   protected Object handleGetObject(String key) {
      if (key == null) {
         throw new NullPointerException("key must not be null");
      } else {
         return this.resources.get(key);
      }
   }

   public String toString() {
      return "XMLResourceBundle: " + this.getLocale();
   }

   private class CatalogueHandler extends DefaultHandler {
      private static final String CATALOGUE = "catalogue";
      private static final String MESSAGE = "message";
      private StringBuffer valueBuffer;
      private Stack elementStack;
      private String currentKey;

      private CatalogueHandler() {
         this.valueBuffer = new StringBuffer();
         this.elementStack = new Stack();
         this.currentKey = null;
      }

      private boolean isOwnNamespace(String uri) {
         return "".equals(uri);
      }

      private org.apache.xmlgraphics.util.QName getParentElementName() {
         return (org.apache.xmlgraphics.util.QName)this.elementStack.peek();
      }

      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
         super.startElement(uri, localName, qName, atts);
         org.apache.xmlgraphics.util.QName elementName = new org.apache.xmlgraphics.util.QName(uri, qName);
         if (this.isOwnNamespace(uri) && !"catalogue".equals(localName)) {
            if (!"message".equals(localName)) {
               throw new SAXException("Invalid element name: " + elementName);
            }

            if (!"catalogue".equals(this.getParentElementName().getLocalName())) {
               throw new SAXException("message must be a child of catalogue");
            }

            this.currentKey = atts.getValue("key");
         }

         this.valueBuffer.setLength(0);
         this.elementStack.push(elementName);
      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         super.endElement(uri, localName, qName);
         this.elementStack.pop();
         if (this.isOwnNamespace(uri) && !"catalogue".equals(localName) && "message".equals(localName)) {
            if (this.currentKey == null) {
               throw new SAXException("current key is null (attribute 'key' might be mistyped)");
            }

            XMLResourceBundle.this.resources.put(this.currentKey, this.valueBuffer.toString());
            this.currentKey = null;
         }

         this.valueBuffer.setLength(0);
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         super.characters(ch, start, length);
         this.valueBuffer.append(ch, start, length);
      }

      // $FF: synthetic method
      CatalogueHandler(Object x1) {
         this();
      }
   }

   static class MissingBundle extends ResourceBundle {
      public Enumeration getKeys() {
         return null;
      }

      public Object handleGetObject(String name) {
         return null;
      }
   }
}
