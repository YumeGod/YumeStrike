package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTMDOMException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.WrappedRuntimeException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class DTMNodeIterator implements NodeIterator {
   private DTMIterator dtm_iter;
   private boolean valid = true;

   public DTMNodeIterator(DTMIterator dtmIterator) {
      try {
         this.dtm_iter = (DTMIterator)dtmIterator.clone();
      } catch (CloneNotSupportedException var3) {
         throw new WrappedRuntimeException(var3);
      }
   }

   public DTMIterator getDTMIterator() {
      return this.dtm_iter;
   }

   public void detach() {
      this.valid = false;
   }

   public boolean getExpandEntityReferences() {
      return false;
   }

   public NodeFilter getFilter() {
      throw new DTMDOMException((short)9);
   }

   public Node getRoot() {
      int handle = this.dtm_iter.getRoot();
      return this.dtm_iter.getDTM(handle).getNode(handle);
   }

   public int getWhatToShow() {
      return this.dtm_iter.getWhatToShow();
   }

   public Node nextNode() throws DOMException {
      if (!this.valid) {
         throw new DTMDOMException((short)11);
      } else {
         int handle = this.dtm_iter.nextNode();
         return handle == -1 ? null : this.dtm_iter.getDTM(handle).getNode(handle);
      }
   }

   public Node previousNode() {
      if (!this.valid) {
         throw new DTMDOMException((short)11);
      } else {
         int handle = this.dtm_iter.previousNode();
         return handle == -1 ? null : this.dtm_iter.getDTM(handle).getNode(handle);
      }
   }
}
