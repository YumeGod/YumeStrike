package org.apache.xerces.util;

import java.io.IOException;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class XMLCatalogResolver implements XMLEntityResolver, EntityResolver2, LSResourceResolver {
   private CatalogManager fResolverCatalogManager;
   private Catalog fCatalog;
   private String[] fCatalogsList;
   private boolean fCatalogsChanged;
   private boolean fPreferPublic;
   private boolean fUseLiteralSystemId;

   public XMLCatalogResolver() {
      this((String[])null, true);
   }

   public XMLCatalogResolver(String[] var1) {
      this(var1, true);
   }

   public XMLCatalogResolver(String[] var1, boolean var2) {
      this.fResolverCatalogManager = null;
      this.fCatalog = null;
      this.fCatalogsList = null;
      this.fCatalogsChanged = true;
      this.fPreferPublic = true;
      this.fUseLiteralSystemId = true;
      this.init(var1, var2);
   }

   public final synchronized String[] getCatalogList() {
      return this.fCatalogsList != null ? (String[])this.fCatalogsList.clone() : null;
   }

   public final synchronized void setCatalogList(String[] var1) {
      this.fCatalogsChanged = true;
      this.fCatalogsList = var1 != null ? (String[])var1.clone() : null;
   }

   public final synchronized void clear() {
      this.fCatalog = null;
   }

   public final boolean getPreferPublic() {
      return this.fPreferPublic;
   }

   public final void setPreferPublic(boolean var1) {
      this.fPreferPublic = var1;
      this.fResolverCatalogManager.setPreferPublic(var1);
   }

   public final boolean getUseLiteralSystemId() {
      return this.fUseLiteralSystemId;
   }

   public final void setUseLiteralSystemId(boolean var1) {
      this.fUseLiteralSystemId = var1;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      String var3 = null;
      if (var1 != null && var2 != null) {
         var3 = this.resolvePublic(var1, var2);
      } else if (var2 != null) {
         var3 = this.resolveSystem(var2);
      }

      if (var3 != null) {
         InputSource var4 = new InputSource(var3);
         var4.setPublicId(var1);
         return var4;
      } else {
         return null;
      }
   }

   public InputSource resolveEntity(String var1, String var2, String var3, String var4) throws SAXException, IOException {
      String var5 = null;
      if (!this.getUseLiteralSystemId() && var3 != null) {
         try {
            URI var6 = new URI(new URI(var3), var4);
            var4 = var6.toString();
         } catch (URI.MalformedURIException var7) {
         }
      }

      if (var2 != null && var4 != null) {
         var5 = this.resolvePublic(var2, var4);
      } else if (var4 != null) {
         var5 = this.resolveSystem(var4);
      }

      if (var5 != null) {
         InputSource var8 = new InputSource(var5);
         var8.setPublicId(var2);
         return var8;
      } else {
         return null;
      }
   }

   public InputSource getExternalSubset(String var1, String var2) throws SAXException, IOException {
      return null;
   }

   public LSInput resolveResource(String var1, String var2, String var3, String var4, String var5) {
      String var6 = null;

      try {
         if (var2 != null) {
            var6 = this.resolveURI(var2);
         }

         if (!this.getUseLiteralSystemId() && var5 != null) {
            try {
               URI var7 = new URI(new URI(var5), var4);
               var4 = var7.toString();
            } catch (URI.MalformedURIException var8) {
            }
         }

         if (var6 == null) {
            if (var3 != null && var4 != null) {
               var6 = this.resolvePublic(var3, var4);
            } else if (var4 != null) {
               var6 = this.resolveSystem(var4);
            }
         }
      } catch (IOException var9) {
      }

      return var6 != null ? new DOMInputImpl(var3, var6, var5) : null;
   }

   public XMLInputSource resolveEntity(XMLResourceIdentifier var1) throws XNIException, IOException {
      String var2 = this.resolveIdentifier(var1);
      return var2 != null ? new XMLInputSource(var1.getPublicId(), var2, var1.getBaseSystemId()) : null;
   }

   public String resolveIdentifier(XMLResourceIdentifier var1) throws IOException, XNIException {
      String var2 = null;
      String var3 = var1.getNamespace();
      if (var3 != null) {
         var2 = this.resolveURI(var3);
      }

      if (var2 == null) {
         String var4 = var1.getPublicId();
         String var5 = this.getUseLiteralSystemId() ? var1.getLiteralSystemId() : var1.getExpandedSystemId();
         if (var4 != null && var5 != null) {
            var2 = this.resolvePublic(var4, var5);
         } else if (var5 != null) {
            var2 = this.resolveSystem(var5);
         }
      }

      return var2;
   }

   public final synchronized String resolveSystem(String var1) throws IOException {
      if (this.fCatalogsChanged) {
         this.parseCatalogs();
         this.fCatalogsChanged = false;
      }

      return this.fCatalog != null ? this.fCatalog.resolveSystem(var1) : null;
   }

   public final synchronized String resolvePublic(String var1, String var2) throws IOException {
      if (this.fCatalogsChanged) {
         this.parseCatalogs();
         this.fCatalogsChanged = false;
      }

      return this.fCatalog != null ? this.fCatalog.resolvePublic(var1, var2) : null;
   }

   public final synchronized String resolveURI(String var1) throws IOException {
      if (this.fCatalogsChanged) {
         this.parseCatalogs();
         this.fCatalogsChanged = false;
      }

      return this.fCatalog != null ? this.fCatalog.resolveURI(var1) : null;
   }

   private void init(String[] var1, boolean var2) {
      this.fCatalogsList = var1 != null ? (String[])var1.clone() : null;
      this.fPreferPublic = var2;
      this.fResolverCatalogManager = new CatalogManager();
      this.fResolverCatalogManager.setAllowOasisXMLCatalogPI(false);
      this.fResolverCatalogManager.setCatalogClassName("org.apache.xml.resolver.Catalog");
      this.fResolverCatalogManager.setCatalogFiles("");
      this.fResolverCatalogManager.setIgnoreMissingProperties(true);
      this.fResolverCatalogManager.setPreferPublic(this.fPreferPublic);
      this.fResolverCatalogManager.setRelativeCatalogs(false);
      this.fResolverCatalogManager.setUseStaticCatalog(false);
      this.fResolverCatalogManager.setVerbosity(0);
   }

   private void parseCatalogs() throws IOException {
      if (this.fCatalogsList != null) {
         this.fCatalog = new Catalog(this.fResolverCatalogManager);
         this.attachReaderToCatalog(this.fCatalog);

         for(int var1 = 0; var1 < this.fCatalogsList.length; ++var1) {
            String var2 = this.fCatalogsList[var1];
            if (var2 != null && var2.length() > 0) {
               this.fCatalog.parseCatalog(var2);
            }
         }
      } else {
         this.fCatalog = null;
      }

   }

   private void attachReaderToCatalog(Catalog var1) {
      SAXParserFactoryImpl var2 = new SAXParserFactoryImpl();
      var2.setNamespaceAware(true);
      var2.setValidating(false);
      SAXCatalogReader var3 = new SAXCatalogReader(var2);
      var3.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "org.apache.xml.resolver.readers.OASISXMLCatalogReader");
      var1.addReader("application/xml", var3);
   }
}
