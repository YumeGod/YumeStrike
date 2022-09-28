package org.w3c.dom.traversal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface NodeIterator {
   Node getRoot();

   int getWhatToShow();

   NodeFilter getFilter();

   boolean getExpandEntityReferences();

   Node nextNode() throws DOMException;

   Node previousNode() throws DOMException;

   void detach();
}
