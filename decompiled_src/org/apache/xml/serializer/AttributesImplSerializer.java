package org.apache.xml.serializer;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public final class AttributesImplSerializer extends AttributesImpl {
   private final Hashtable m_indexFromQName = new Hashtable();
   private final StringBuffer m_buff = new StringBuffer();
   private static final int MAX = 12;
   private static final int MAXMinus1 = 11;

   public final int getIndex(String qname) {
      int index;
      if (super.getLength() < 12) {
         index = super.getIndex(qname);
         return index;
      } else {
         Integer i = (Integer)this.m_indexFromQName.get(qname);
         if (i == null) {
            index = -1;
         } else {
            index = i;
         }

         return index;
      }
   }

   public final void addAttribute(String uri, String local, String qname, String type, String val) {
      int index = super.getLength();
      super.addAttribute(uri, local, qname, type, val);
      if (index >= 11) {
         if (index == 11) {
            this.switchOverToHash(12);
         } else {
            Integer i = new Integer(index);
            this.m_indexFromQName.put(qname, i);
            this.m_buff.setLength(0);
            this.m_buff.append('{').append(uri).append('}').append(local);
            String key = this.m_buff.toString();
            this.m_indexFromQName.put(key, i);
         }

      }
   }

   private void switchOverToHash(int numAtts) {
      for(int index = 0; index < numAtts; ++index) {
         String qName = super.getQName(index);
         Integer i = new Integer(index);
         this.m_indexFromQName.put(qName, i);
         String uri = super.getURI(index);
         String local = super.getLocalName(index);
         this.m_buff.setLength(0);
         this.m_buff.append('{').append(uri).append('}').append(local);
         String key = this.m_buff.toString();
         this.m_indexFromQName.put(key, i);
      }

   }

   public final void clear() {
      int len = super.getLength();
      super.clear();
      if (12 <= len) {
         this.m_indexFromQName.clear();
      }

   }

   public final void setAttributes(Attributes atts) {
      super.setAttributes(atts);
      int numAtts = atts.getLength();
      if (12 <= numAtts) {
         this.switchOverToHash(numAtts);
      }

   }

   public final int getIndex(String uri, String localName) {
      int index;
      if (super.getLength() < 12) {
         index = super.getIndex(uri, localName);
         return index;
      } else {
         this.m_buff.setLength(0);
         this.m_buff.append('{').append(uri).append('}').append(localName);
         String key = this.m_buff.toString();
         Integer i = (Integer)this.m_indexFromQName.get(key);
         if (i == null) {
            index = -1;
         } else {
            index = i;
         }

         return index;
      }
   }
}
