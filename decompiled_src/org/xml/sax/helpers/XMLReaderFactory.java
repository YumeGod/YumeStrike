package org.xml.sax.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class XMLReaderFactory {
   private static final String property = "org.xml.sax.driver";
   private static final int DEFAULT_LINE_LENGTH = 80;
   // $FF: synthetic field
   static Class class$org$xml$sax$helpers$XMLReaderFactory;

   private XMLReaderFactory() {
   }

   public static XMLReader createXMLReader() throws SAXException {
      String var0 = null;
      SecuritySupport var1 = SecuritySupport.getInstance();
      ClassLoader var2 = NewInstance.getClassLoader();

      try {
         var0 = var1.getSystemProperty("org.xml.sax.driver");
      } catch (Exception var24) {
      }

      if (var0 == null) {
         String var3 = "META-INF/services/org.xml.sax.driver";
         InputStream var4 = null;
         ClassLoader var5 = var1.getContextClassLoader();
         if (var5 != null) {
            var4 = var1.getResourceAsStream(var5, var3);
            if (var4 == null) {
               var5 = (class$org$xml$sax$helpers$XMLReaderFactory == null ? (class$org$xml$sax$helpers$XMLReaderFactory = class$("org.xml.sax.helpers.XMLReaderFactory")) : class$org$xml$sax$helpers$XMLReaderFactory).getClassLoader();
               var4 = var1.getResourceAsStream(var5, var3);
            }
         } else {
            var5 = (class$org$xml$sax$helpers$XMLReaderFactory == null ? (class$org$xml$sax$helpers$XMLReaderFactory = class$("org.xml.sax.helpers.XMLReaderFactory")) : class$org$xml$sax$helpers$XMLReaderFactory).getClassLoader();
            var4 = var1.getResourceAsStream(var5, var3);
         }

         if (var4 != null) {
            BufferedReader var6;
            try {
               var6 = new BufferedReader(new InputStreamReader(var4, "UTF-8"), 80);
            } catch (UnsupportedEncodingException var23) {
               var6 = new BufferedReader(new InputStreamReader(var4), 80);
            }

            try {
               var0 = var6.readLine();
            } catch (Exception var21) {
            } finally {
               try {
                  var6.close();
               } catch (IOException var19) {
               }

            }
         }
      }

      if (var0 == null) {
         var0 = "org.apache.xerces.parsers.SAXParser";
      }

      if (var0 != null) {
         return loadClass(var2, var0);
      } else {
         try {
            return new ParserAdapter(ParserFactory.makeParser());
         } catch (Exception var20) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
         }
      }
   }

   public static XMLReader createXMLReader(String var0) throws SAXException {
      return loadClass(NewInstance.getClassLoader(), var0);
   }

   private static XMLReader loadClass(ClassLoader var0, String var1) throws SAXException {
      try {
         return (XMLReader)NewInstance.newInstance(var0, var1);
      } catch (ClassNotFoundException var6) {
         throw new SAXException("SAX2 driver class " + var1 + " not found", var6);
      } catch (IllegalAccessException var7) {
         throw new SAXException("SAX2 driver class " + var1 + " found but cannot be loaded", var7);
      } catch (InstantiationException var8) {
         throw new SAXException("SAX2 driver class " + var1 + " loaded but cannot be instantiated (no empty public constructor?)", var8);
      } catch (ClassCastException var9) {
         throw new SAXException("SAX2 driver class " + var1 + " does not implement XMLReader", var9);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
