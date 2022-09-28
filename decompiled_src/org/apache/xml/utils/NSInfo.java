package org.apache.xml.utils;

public class NSInfo {
   public String m_namespace;
   public boolean m_hasXMLNSAttrs;
   public boolean m_hasProcessedNS;
   public int m_ancestorHasXMLNSAttrs;
   public static final int ANCESTORXMLNSUNPROCESSED = 0;
   public static final int ANCESTORHASXMLNS = 1;
   public static final int ANCESTORNOXMLNS = 2;

   public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs) {
      this.m_hasProcessedNS = hasProcessedNS;
      this.m_hasXMLNSAttrs = hasXMLNSAttrs;
      this.m_namespace = null;
      this.m_ancestorHasXMLNSAttrs = 0;
   }

   public NSInfo(boolean hasProcessedNS, boolean hasXMLNSAttrs, int ancestorHasXMLNSAttrs) {
      this.m_hasProcessedNS = hasProcessedNS;
      this.m_hasXMLNSAttrs = hasXMLNSAttrs;
      this.m_ancestorHasXMLNSAttrs = ancestorHasXMLNSAttrs;
      this.m_namespace = null;
   }

   public NSInfo(String namespace, boolean hasXMLNSAttrs) {
      this.m_hasProcessedNS = true;
      this.m_hasXMLNSAttrs = hasXMLNSAttrs;
      this.m_namespace = namespace;
      this.m_ancestorHasXMLNSAttrs = 0;
   }
}
