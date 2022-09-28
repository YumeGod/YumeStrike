package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.utils.IntVector;
import org.w3c.dom.Node;

public class DTMAxisIterNodeList extends DTMNodeListBase {
   private DTM m_dtm;
   private DTMAxisIterator m_iter;
   private IntVector m_cachedNodes;
   private int m_last = -1;

   private DTMAxisIterNodeList() {
   }

   public DTMAxisIterNodeList(DTM dtm, DTMAxisIterator dtmAxisIterator) {
      if (dtmAxisIterator == null) {
         this.m_last = 0;
      } else {
         this.m_cachedNodes = new IntVector();
         this.m_dtm = dtm;
      }

      this.m_iter = dtmAxisIterator;
   }

   public DTMAxisIterator getDTMAxisIterator() {
      return this.m_iter;
   }

   public Node item(int index) {
      if (this.m_iter != null) {
         int count = this.m_cachedNodes.size();
         int node;
         if (count > index) {
            node = this.m_cachedNodes.elementAt(index);
            return this.m_dtm.getNode(node);
         }

         if (this.m_last == -1) {
            while((node = this.m_iter.next()) != -1 && count <= index) {
               this.m_cachedNodes.addElement(node);
               ++count;
            }

            if (node != -1) {
               return this.m_dtm.getNode(node);
            }

            this.m_last = count;
         }
      }

      return null;
   }

   public int getLength() {
      if (this.m_last == -1) {
         while(true) {
            int node;
            if ((node = this.m_iter.next()) == -1) {
               this.m_last = this.m_cachedNodes.size();
               break;
            }

            this.m_cachedNodes.addElement(node);
         }
      }

      return this.m_last;
   }
}
