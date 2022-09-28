package org.apache.xalan.xsltc.dom;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class DocumentCache implements DOMCache {
   private int _size;
   private Hashtable _references;
   private String[] _URIs;
   private int _count;
   private int _current;
   private SAXParser _parser;
   private XMLReader _reader;
   private XSLTCDTMManager _dtmManager;
   private static final int REFRESH_INTERVAL = 1000;

   public DocumentCache(int size) throws SAXException {
      this(size, (XSLTCDTMManager)null);

      try {
         this._dtmManager = (XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance();
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   public DocumentCache(int size, XSLTCDTMManager dtmManager) throws SAXException {
      this._dtmManager = dtmManager;
      this._count = 0;
      this._current = 0;
      this._size = size;
      this._references = new Hashtable(this._size + 2);
      this._URIs = new String[this._size];

      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();

         try {
            factory.setFeature("http://xml.org/sax/features/namespaces", true);
         } catch (Exception var5) {
            factory.setNamespaceAware(true);
         }

         this._parser = factory.newSAXParser();
         this._reader = this._parser.getXMLReader();
      } catch (ParserConfigurationException var6) {
         BasisLibrary.runTimeError("NAMESPACES_SUPPORT_ERR");
      }

   }

   private final long getLastModified(String uri) {
      try {
         URL url = new URL(uri);
         URLConnection connection = url.openConnection();
         long timestamp = connection.getLastModified();
         if (timestamp == 0L && "file".equals(url.getProtocol())) {
            File localfile = new File(URLDecoder.decode(url.getFile()));
            timestamp = localfile.lastModified();
         }

         return timestamp;
      } catch (Exception var7) {
         return System.currentTimeMillis();
      }
   }

   private CachedDocument lookupDocument(String uri) {
      return (CachedDocument)this._references.get(uri);
   }

   private synchronized void insertDocument(String uri, CachedDocument doc) {
      if (this._count < this._size) {
         this._URIs[this._count++] = uri;
         this._current = 0;
      } else {
         this._references.remove(this._URIs[this._current]);
         this._URIs[this._current] = uri;
         if (++this._current >= this._size) {
            this._current = 0;
         }
      }

      this._references.put(uri, doc);
   }

   private synchronized void replaceDocument(String uri, CachedDocument doc) {
      CachedDocument old = (CachedDocument)this._references.get(uri);
      if (doc == null) {
         this.insertDocument(uri, doc);
      } else {
         this._references.put(uri, doc);
      }

   }

   public DOM retrieveDocument(String baseURI, String href, Translet trs) {
      String uri = href;
      if (baseURI != null && !baseURI.equals("")) {
         try {
            uri = SystemIDResolver.getAbsoluteURI(uri, baseURI);
         } catch (TransformerException var12) {
         }
      }

      CachedDocument doc;
      if ((doc = this.lookupDocument(uri)) == null) {
         doc = new CachedDocument(uri);
         if (doc == null) {
            return null;
         }

         doc.setLastModified(this.getLastModified(uri));
         this.insertDocument(uri, doc);
      } else {
         long now = System.currentTimeMillis();
         long chk = doc.getLastChecked();
         doc.setLastChecked(now);
         if (now > chk + 1000L) {
            doc.setLastChecked(now);
            long last = this.getLastModified(uri);
            if (last > doc.getLastModified()) {
               doc = new CachedDocument(uri);
               if (doc == null) {
                  return null;
               }

               doc.setLastModified(this.getLastModified(uri));
               this.replaceDocument(uri, doc);
            }
         }
      }

      DOM dom = doc.getDocument();
      if (dom == null) {
         return null;
      } else {
         doc.incAccessCount();
         AbstractTranslet translet = (AbstractTranslet)trs;
         translet.prepassDocument(dom);
         return doc.getDocument();
      }
   }

   public void getStatistics(PrintWriter out) {
      out.println("<h2>DOM cache statistics</h2><center><table border=\"2\"><tr><td><b>Document URI</b></td><td><center><b>Build time</b></center></td><td><center><b>Access count</b></center></td><td><center><b>Last accessed</b></center></td><td><center><b>Last modified</b></center></td></tr>");

      for(int i = 0; i < this._count; ++i) {
         CachedDocument doc = (CachedDocument)this._references.get(this._URIs[i]);
         out.print("<tr><td><a href=\"" + this._URIs[i] + "\">" + "<font size=-1>" + this._URIs[i] + "</font></a></td>");
         out.print("<td><center>" + doc.getLatency() + "ms</center></td>");
         out.print("<td><center>" + doc.getAccessCount() + "</center></td>");
         out.print("<td><center>" + new Date(doc.getLastReferenced()) + "</center></td>");
         out.print("<td><center>" + new Date(doc.getLastModified()) + "</center></td>");
         out.println("</tr>");
      }

      out.println("</table></center>");
   }

   public final class CachedDocument {
      private long _firstReferenced;
      private long _lastReferenced;
      private long _accessCount;
      private long _lastModified;
      private long _lastChecked;
      private long _buildTime;
      private DOMEnhancedForDTM _dom = null;

      public CachedDocument(String uri) {
         long stamp = System.currentTimeMillis();
         this._firstReferenced = stamp;
         this._lastReferenced = stamp;
         this._accessCount = 0L;
         this.loadDocument(uri);
         this._buildTime = System.currentTimeMillis() - stamp;
      }

      public void loadDocument(String uri) {
         try {
            long stamp = System.currentTimeMillis();
            this._dom = (DOMEnhancedForDTM)DocumentCache.this._dtmManager.getDTM(new SAXSource(DocumentCache.this._reader, new InputSource(uri)), false, (DTMWSFilter)null, true, false);
            this._dom.setDocumentURI(uri);
            long thisTime = System.currentTimeMillis() - stamp;
            if (this._buildTime > 0L) {
               this._buildTime = this._buildTime + thisTime >>> 1;
            } else {
               this._buildTime = thisTime;
            }
         } catch (Exception var6) {
            this._dom = null;
         }

      }

      public DOM getDocument() {
         return this._dom;
      }

      public long getFirstReferenced() {
         return this._firstReferenced;
      }

      public long getLastReferenced() {
         return this._lastReferenced;
      }

      public long getAccessCount() {
         return this._accessCount;
      }

      public void incAccessCount() {
         ++this._accessCount;
      }

      public long getLastModified() {
         return this._lastModified;
      }

      public void setLastModified(long t) {
         this._lastModified = t;
      }

      public long getLatency() {
         return this._buildTime;
      }

      public long getLastChecked() {
         return this._lastChecked;
      }

      public void setLastChecked(long t) {
         this._lastChecked = t;
      }

      public long getEstimatedSize() {
         return this._dom != null ? (long)(this._dom.getSize() << 5) : 0L;
      }
   }
}
