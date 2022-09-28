package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.w3c.dom.Node;

public class DTMChildIterNodeList extends DTMNodeListBase {
   private int m_firstChild;
   private DTM m_parentDTM;

   private DTMChildIterNodeList() {
   }

   public DTMChildIterNodeList(DTM parentDTM, int parentHandle) {
      this.m_parentDTM = parentDTM;
      this.m_firstChild = parentDTM.getFirstChild(parentHandle);
   }

   public Node item(int index) {
      int handle = this.m_firstChild;

      while(true) {
         --index;
         if (index < 0 || handle == -1) {
            return handle == -1 ? null : this.m_parentDTM.getNode(handle);
         }

         handle = this.m_parentDTM.getNextSibling(handle);
      }
   }

   public int getLength() {
      int count = 0;

      for(int handle = this.m_firstChild; handle != -1; handle = this.m_parentDTM.getNextSibling(handle)) {
         ++count;
      }

      return count;
   }
}
