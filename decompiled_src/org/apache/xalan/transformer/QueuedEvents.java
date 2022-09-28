package org.apache.xalan.transformer;

import java.util.Vector;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.utils.MutableAttrListImpl;

public abstract class QueuedEvents {
   protected int m_eventCount = 0;
   public boolean m_docPending = false;
   protected boolean m_docEnded = false;
   public boolean m_elemIsPending = false;
   public boolean m_elemIsEnded = false;
   protected MutableAttrListImpl m_attributes = new MutableAttrListImpl();
   protected boolean m_nsDeclsHaveBeenAdded = false;
   protected String m_name;
   protected String m_url;
   protected String m_localName;
   protected Vector m_namespaces = null;
   private Serializer m_serializer;

   protected void reInitEvents() {
   }

   public void reset() {
      this.pushDocumentEvent();
      this.reInitEvents();
   }

   void pushDocumentEvent() {
      this.m_docPending = true;
      ++this.m_eventCount;
   }

   void popEvent() {
      this.m_elemIsPending = false;
      this.m_attributes.clear();
      this.m_nsDeclsHaveBeenAdded = false;
      this.m_name = null;
      this.m_url = null;
      this.m_localName = null;
      this.m_namespaces = null;
      --this.m_eventCount;
   }

   void setSerializer(Serializer s) {
      this.m_serializer = s;
   }

   Serializer getSerializer() {
      return this.m_serializer;
   }
}
