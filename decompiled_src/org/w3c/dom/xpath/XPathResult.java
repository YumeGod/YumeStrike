package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface XPathResult {
   short ANY_TYPE = 0;
   short NUMBER_TYPE = 1;
   short STRING_TYPE = 2;
   short BOOLEAN_TYPE = 3;
   short UNORDERED_NODE_ITERATOR_TYPE = 4;
   short ORDERED_NODE_ITERATOR_TYPE = 5;
   short UNORDERED_NODE_SNAPSHOT_TYPE = 6;
   short ORDERED_NODE_SNAPSHOT_TYPE = 7;
   short ANY_UNORDERED_NODE_TYPE = 8;
   short FIRST_ORDERED_NODE_TYPE = 9;

   short getResultType();

   double getNumberValue() throws XPathException;

   String getStringValue() throws XPathException;

   boolean getBooleanValue() throws XPathException;

   Node getSingleNodeValue() throws XPathException;

   boolean getInvalidIteratorState();

   int getSnapshotLength() throws XPathException;

   Node iterateNext() throws XPathException, DOMException;

   Node snapshotItem(int var1) throws XPathException;
}
