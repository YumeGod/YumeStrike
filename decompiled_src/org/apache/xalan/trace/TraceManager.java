package org.apache.xalan.trace;

import java.lang.reflect.Method;
import java.util.TooManyListenersException;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class TraceManager {
   private TransformerImpl m_transformer;
   private Vector m_traceListeners = null;

   public TraceManager(TransformerImpl transformer) {
      this.m_transformer = transformer;
   }

   public void addTraceListener(TraceListener tl) throws TooManyListenersException {
      this.m_transformer.setDebug(true);
      if (null == this.m_traceListeners) {
         this.m_traceListeners = new Vector();
      }

      this.m_traceListeners.addElement(tl);
   }

   public void removeTraceListener(TraceListener tl) {
      if (null != this.m_traceListeners) {
         this.m_traceListeners.removeElement(tl);
         if (0 == this.m_traceListeners.size()) {
            this.m_traceListeners = null;
         }
      }

   }

   public void fireGenerateEvent(GenerateEvent te) {
      if (null != this.m_traceListeners) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            tl.generated(te);
         }
      }

   }

   public boolean hasTraceListeners() {
      return null != this.m_traceListeners;
   }

   public void fireTraceEvent(ElemTemplateElement styleNode) {
      if (this.hasTraceListeners()) {
         int sourceNode = this.m_transformer.getXPathContext().getCurrentNode();
         Node source = this.getDOMNodeFromDTM(sourceNode);
         this.fireTraceEvent(new TracerEvent(this.m_transformer, source, this.m_transformer.getMode(), styleNode));
      }

   }

   public void fireTraceEndEvent(ElemTemplateElement styleNode) {
      if (this.hasTraceListeners()) {
         int sourceNode = this.m_transformer.getXPathContext().getCurrentNode();
         Node source = this.getDOMNodeFromDTM(sourceNode);
         this.fireTraceEndEvent(new TracerEvent(this.m_transformer, source, this.m_transformer.getMode(), styleNode));
      }

   }

   public void fireTraceEndEvent(TracerEvent te) {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx2) {
               ((TraceListenerEx2)tl).traceEnd(te);
            }
         }
      }

   }

   public void fireTraceEvent(TracerEvent te) {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            tl.trace(te);
         }
      }

   }

   public void fireSelectedEvent(int sourceNode, ElemTemplateElement styleNode, String attributeName, XPath xpath, XObject selection) throws TransformerException {
      if (this.hasTraceListeners()) {
         Node source = this.getDOMNodeFromDTM(sourceNode);
         this.fireSelectedEvent(new SelectionEvent(this.m_transformer, source, styleNode, attributeName, xpath, selection));
      }

   }

   public void fireSelectedEndEvent(int sourceNode, ElemTemplateElement styleNode, String attributeName, XPath xpath, XObject selection) throws TransformerException {
      if (this.hasTraceListeners()) {
         Node source = this.getDOMNodeFromDTM(sourceNode);
         this.fireSelectedEndEvent(new EndSelectionEvent(this.m_transformer, source, styleNode, attributeName, xpath, selection));
      }

   }

   public void fireSelectedEndEvent(EndSelectionEvent se) throws TransformerException {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx) {
               ((TraceListenerEx)tl).selectEnd(se);
            }
         }
      }

   }

   public void fireSelectedEvent(SelectionEvent se) throws TransformerException {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            tl.selected(se);
         }
      }

   }

   public void fireExtensionEndEvent(Method method, Object instance, Object[] arguments) {
      ExtensionEvent ee = new ExtensionEvent(this.m_transformer, method, instance, arguments);
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx3) {
               ((TraceListenerEx3)tl).extensionEnd(ee);
            }
         }
      }

   }

   public void fireExtensionEvent(Method method, Object instance, Object[] arguments) {
      ExtensionEvent ee = new ExtensionEvent(this.m_transformer, method, instance, arguments);
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx3) {
               ((TraceListenerEx3)tl).extension(ee);
            }
         }
      }

   }

   public void fireExtensionEndEvent(ExtensionEvent ee) {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx3) {
               ((TraceListenerEx3)tl).extensionEnd(ee);
            }
         }
      }

   }

   public void fireExtensionEvent(ExtensionEvent ee) {
      if (this.hasTraceListeners()) {
         int nListeners = this.m_traceListeners.size();

         for(int i = 0; i < nListeners; ++i) {
            TraceListener tl = (TraceListener)this.m_traceListeners.elementAt(i);
            if (tl instanceof TraceListenerEx3) {
               ((TraceListenerEx3)tl).extension(ee);
            }
         }
      }

   }

   private Node getDOMNodeFromDTM(int sourceNode) {
      DTM dtm = this.m_transformer.getXPathContext().getDTM(sourceNode);
      Node source = dtm == null ? null : dtm.getNode(sourceNode);
      return source;
   }
}
