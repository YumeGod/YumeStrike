package org.apache.xalan.xsltc.trax;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.xml.sax.SAXException;

public final class XSLTCSource implements Source {
   private String _systemId = null;
   private Source _source = null;
   private ThreadLocal _dom = new ThreadLocal();

   public XSLTCSource(String systemId) {
      this._systemId = systemId;
   }

   public XSLTCSource(Source source) {
      this._source = source;
   }

   public void setSystemId(String systemId) {
      this._systemId = systemId;
      if (this._source != null) {
         this._source.setSystemId(systemId);
      }

   }

   public String getSystemId() {
      return this._source != null ? this._source.getSystemId() : this._systemId;
   }

   protected DOM getDOM(XSLTCDTMManager dtmManager, AbstractTranslet translet) throws SAXException {
      SAXImpl idom = (SAXImpl)this._dom.get();
      if (idom != null) {
         if (dtmManager != null) {
            idom.migrateTo(dtmManager);
         }
      } else {
         Source source = this._source;
         if (source == null) {
            if (this._systemId == null || this._systemId.length() <= 0) {
               ErrorMsg err = new ErrorMsg("XSLTC_SOURCE_ERR");
               throw new SAXException(err.toString());
            }

            source = new StreamSource(this._systemId);
         }

         DOMWSFilter wsfilter = null;
         if (translet != null && translet instanceof StripFilter) {
            wsfilter = new DOMWSFilter(translet);
         }

         boolean hasIdCall = translet != null ? translet.hasIdCall() : false;
         if (dtmManager == null) {
            dtmManager = XSLTCDTMManager.newInstance();
         }

         idom = (SAXImpl)dtmManager.getDTM((Source)source, true, wsfilter, false, false, hasIdCall);
         String systemId = this.getSystemId();
         if (systemId != null) {
            idom.setDocumentURI(systemId);
         }

         this._dom.set(idom);
      }

      return idom;
   }
}
