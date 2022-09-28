package org.apache.batik.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.dom.util.DocumentDescriptor;
import org.apache.batik.util.CleanerThread;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class DocumentLoader {
   protected SVGDocumentFactory documentFactory;
   protected HashMap cacheMap = new HashMap();
   protected UserAgent userAgent;

   protected DocumentLoader() {
   }

   public DocumentLoader(UserAgent var1) {
      this.userAgent = var1;
      this.documentFactory = new SAXSVGDocumentFactory(var1.getXMLParserClassName(), true);
      this.documentFactory.setValidating(var1.isXMLParserValidating());
   }

   public Document checkCache(String var1) {
      int var2 = var1.lastIndexOf(47);
      if (var2 == -1) {
         var2 = 0;
      }

      var2 = var1.indexOf(35, var2);
      if (var2 != -1) {
         var1 = var1.substring(0, var2);
      }

      DocumentState var3;
      synchronized(this.cacheMap) {
         var3 = (DocumentState)this.cacheMap.get(var1);
      }

      return var3 != null ? var3.getDocument() : null;
   }

   public Document loadDocument(String var1) throws IOException {
      Document var2 = this.checkCache(var1);
      if (var2 != null) {
         return var2;
      } else {
         SVGDocument var3 = this.documentFactory.createSVGDocument(var1);
         DocumentDescriptor var4 = this.documentFactory.getDocumentDescriptor();
         DocumentState var5 = new DocumentState(var1, var3, var4);
         synchronized(this.cacheMap) {
            this.cacheMap.put(var1, var5);
         }

         return var5.getDocument();
      }
   }

   public Document loadDocument(String var1, InputStream var2) throws IOException {
      Document var3 = this.checkCache(var1);
      if (var3 != null) {
         return var3;
      } else {
         SVGDocument var4 = this.documentFactory.createSVGDocument(var1, var2);
         DocumentDescriptor var5 = this.documentFactory.getDocumentDescriptor();
         DocumentState var6 = new DocumentState(var1, var4, var5);
         synchronized(this.cacheMap) {
            this.cacheMap.put(var1, var6);
         }

         return var6.getDocument();
      }
   }

   public UserAgent getUserAgent() {
      return this.userAgent;
   }

   public void dispose() {
      synchronized(this.cacheMap) {
         this.cacheMap.clear();
      }
   }

   public int getLineNumber(Element var1) {
      String var2 = ((SVGDocument)var1.getOwnerDocument()).getURL();
      DocumentState var3;
      synchronized(this.cacheMap) {
         var3 = (DocumentState)this.cacheMap.get(var2);
      }

      return var3 == null ? -1 : var3.desc.getLocationLine(var1);
   }

   private class DocumentState extends CleanerThread.SoftReferenceCleared {
      private String uri;
      private DocumentDescriptor desc;

      public DocumentState(String var2, Document var3, DocumentDescriptor var4) {
         super(var3);
         this.uri = var2;
         this.desc = var4;
      }

      public void cleared() {
         synchronized(DocumentLoader.this.cacheMap) {
            DocumentLoader.this.cacheMap.remove(this.uri);
         }
      }

      public DocumentDescriptor getDocumentDescriptor() {
         return this.desc;
      }

      public String getURI() {
         return this.uri;
      }

      public Document getDocument() {
         return (Document)this.get();
      }
   }
}
