package org.w3c.dom.ranges;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

public interface Range {
   short START_TO_START = 0;
   short START_TO_END = 1;
   short END_TO_END = 2;
   short END_TO_START = 3;

   Node getStartContainer() throws DOMException;

   int getStartOffset() throws DOMException;

   Node getEndContainer() throws DOMException;

   int getEndOffset() throws DOMException;

   boolean getCollapsed() throws DOMException;

   Node getCommonAncestorContainer() throws DOMException;

   void setStart(Node var1, int var2) throws RangeException, DOMException;

   void setEnd(Node var1, int var2) throws RangeException, DOMException;

   void setStartBefore(Node var1) throws RangeException, DOMException;

   void setStartAfter(Node var1) throws RangeException, DOMException;

   void setEndBefore(Node var1) throws RangeException, DOMException;

   void setEndAfter(Node var1) throws RangeException, DOMException;

   void collapse(boolean var1) throws DOMException;

   void selectNode(Node var1) throws RangeException, DOMException;

   void selectNodeContents(Node var1) throws RangeException, DOMException;

   short compareBoundaryPoints(short var1, Range var2) throws DOMException;

   void deleteContents() throws DOMException;

   DocumentFragment extractContents() throws DOMException;

   DocumentFragment cloneContents() throws DOMException;

   void insertNode(Node var1) throws DOMException, RangeException;

   void surroundContents(Node var1) throws DOMException, RangeException;

   Range cloneRange() throws DOMException;

   String toString() throws DOMException;

   void detach() throws DOMException;
}
