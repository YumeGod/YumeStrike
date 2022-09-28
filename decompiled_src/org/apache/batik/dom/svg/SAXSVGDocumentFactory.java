package org.apache.batik.dom.svg;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.MissingResourceException;
import java.util.Properties;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg12.SVG12DOMImplementation;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.util.MimeTypeConstants;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SAXSVGDocumentFactory extends SAXDocumentFactory implements SVGDocumentFactory {
   public static final Object LOCK = new Object();
   public static final String KEY_PUBLIC_IDS = "publicIds";
   public static final String KEY_SKIPPABLE_PUBLIC_IDS = "skippablePublicIds";
   public static final String KEY_SKIP_DTD = "skipDTD";
   public static final String KEY_SYSTEM_ID = "systemId.";
   protected static final String DTDIDS = "org.apache.batik.dom.svg.resources.dtdids";
   protected static final String HTTP_CHARSET = "charset";
   protected static String dtdids;
   protected static String skippable_dtdids;
   protected static String skip_dtd;
   protected static Properties dtdProps;
   // $FF: synthetic field
   static Class class$org$apache$batik$dom$svg$SAXSVGDocumentFactory;

   public SAXSVGDocumentFactory(String var1) {
      super(SVGDOMImplementation.getDOMImplementation(), var1);
   }

   public SAXSVGDocumentFactory(String var1, boolean var2) {
      super(SVGDOMImplementation.getDOMImplementation(), var1, var2);
   }

   public SVGDocument createSVGDocument(String var1) throws IOException {
      return (SVGDocument)this.createDocument(var1);
   }

   public SVGDocument createSVGDocument(String var1, InputStream var2) throws IOException {
      return (SVGDocument)this.createDocument(var1, var2);
   }

   public SVGDocument createSVGDocument(String var1, Reader var2) throws IOException {
      return (SVGDocument)this.createDocument(var1, var2);
   }

   public Document createDocument(String var1) throws IOException {
      ParsedURL var2 = new ParsedURL(var1);
      InputStream var3 = var2.openStream(MimeTypeConstants.MIME_TYPES_SVG);
      InputSource var4 = new InputSource(var3);
      String var5 = var2.getContentType();
      int var6 = -1;
      if (var5 != null) {
         var5 = var5.toLowerCase();
         var6 = var5.indexOf("charset");
      }

      String var7 = null;
      if (var6 != -1) {
         int var8 = var6 + "charset".length();
         int var9 = var5.indexOf(61, var8);
         if (var9 != -1) {
            ++var9;
            int var10 = var5.indexOf(44, var9);
            int var11 = var5.indexOf(59, var9);
            if (var11 != -1 && (var11 < var10 || var10 == -1)) {
               var10 = var11;
            }

            if (var10 != -1) {
               var7 = var5.substring(var9, var10);
            } else {
               var7 = var5.substring(var9);
            }

            var7 = var7.trim();
            var4.setEncoding(var7);
         }
      }

      var4.setSystemId(var1);
      SVGOMDocument var12 = (SVGOMDocument)super.createDocument("http://www.w3.org/2000/svg", "svg", var1, var4);
      var12.setParsedURL(var2);
      var12.setDocumentInputEncoding(var7);
      var12.setXmlStandalone(this.isStandalone);
      var12.setXmlVersion(this.xmlVersion);
      return var12;
   }

   public Document createDocument(String var1, InputStream var2) throws IOException {
      InputSource var4 = new InputSource(var2);
      var4.setSystemId(var1);

      try {
         Document var3 = super.createDocument("http://www.w3.org/2000/svg", "svg", var1, var4);
         if (var1 != null) {
            ((SVGOMDocument)var3).setParsedURL(new ParsedURL(var1));
         }

         AbstractDocument var5 = (AbstractDocument)var3;
         var5.setDocumentURI(var1);
         var5.setXmlStandalone(this.isStandalone);
         var5.setXmlVersion(this.xmlVersion);
         return var3;
      } catch (MalformedURLException var6) {
         throw new IOException(var6.getMessage());
      }
   }

   public Document createDocument(String var1, Reader var2) throws IOException {
      InputSource var4 = new InputSource(var2);
      var4.setSystemId(var1);

      try {
         Document var3 = super.createDocument("http://www.w3.org/2000/svg", "svg", var1, var4);
         if (var1 != null) {
            ((SVGOMDocument)var3).setParsedURL(new ParsedURL(var1));
         }

         AbstractDocument var5 = (AbstractDocument)var3;
         var5.setDocumentURI(var1);
         var5.setXmlStandalone(this.isStandalone);
         var5.setXmlVersion(this.xmlVersion);
         return var3;
      } catch (MalformedURLException var6) {
         throw new IOException(var6.getMessage());
      }
   }

   public Document createDocument(String var1, String var2, String var3) throws IOException {
      if ("http://www.w3.org/2000/svg".equals(var1) && "svg".equals(var2)) {
         return this.createDocument(var3);
      } else {
         throw new RuntimeException("Bad root element");
      }
   }

   public Document createDocument(String var1, String var2, String var3, InputStream var4) throws IOException {
      if ("http://www.w3.org/2000/svg".equals(var1) && "svg".equals(var2)) {
         return this.createDocument(var3, var4);
      } else {
         throw new RuntimeException("Bad root element");
      }
   }

   public Document createDocument(String var1, String var2, String var3, Reader var4) throws IOException {
      if ("http://www.w3.org/2000/svg".equals(var1) && "svg".equals(var2)) {
         return this.createDocument(var3, var4);
      } else {
         throw new RuntimeException("Bad root element");
      }
   }

   public DOMImplementation getDOMImplementation(String var1) {
      if (var1 != null && var1.length() != 0 && !var1.equals("1.0") && !var1.equals("1.1")) {
         if (var1.equals("1.2")) {
            return SVG12DOMImplementation.getDOMImplementation();
         } else {
            throw new RuntimeException("Unsupport SVG version '" + var1 + "'");
         }
      } else {
         return SVGDOMImplementation.getDOMImplementation();
      }
   }

   public void startDocument() throws SAXException {
      super.startDocument();
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException {
      try {
         synchronized(LOCK) {
            if (dtdProps == null) {
               dtdProps = new Properties();

               try {
                  Class var4 = class$org$apache$batik$dom$svg$SAXSVGDocumentFactory == null ? (class$org$apache$batik$dom$svg$SAXSVGDocumentFactory = class$("org.apache.batik.dom.svg.SAXSVGDocumentFactory")) : class$org$apache$batik$dom$svg$SAXSVGDocumentFactory;
                  InputStream var5 = var4.getResourceAsStream("resources/dtdids.properties");
                  dtdProps.load(var5);
               } catch (IOException var7) {
                  throw new SAXException(var7);
               }
            }

            if (dtdids == null) {
               dtdids = dtdProps.getProperty("publicIds");
            }

            if (skippable_dtdids == null) {
               skippable_dtdids = dtdProps.getProperty("skippablePublicIds");
            }

            if (skip_dtd == null) {
               skip_dtd = dtdProps.getProperty("skipDTD");
            }
         }

         if (var1 == null) {
            return null;
         } else if (!this.isValidating && skippable_dtdids.indexOf(var1) != -1) {
            return new InputSource(new StringReader(skip_dtd));
         } else {
            if (dtdids.indexOf(var1) != -1) {
               String var3 = dtdProps.getProperty("systemId." + var1.replace(' ', '_'));
               if (var3 != null && !"".equals(var3)) {
                  return new InputSource(this.getClass().getResource(var3).toString());
               }
            }

            return null;
         }
      } catch (MissingResourceException var9) {
         throw new SAXException(var9);
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
