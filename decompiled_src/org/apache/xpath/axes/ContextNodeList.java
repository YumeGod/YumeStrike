package org.apache.xpath.axes;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public interface ContextNodeList {
   Node getCurrentNode();

   int getCurrentPos();

   void reset();

   void setShouldCacheNodes(boolean var1);

   void runTo(int var1);

   void setCurrentPos(int var1);

   int size();

   boolean isFresh();

   NodeIterator cloneWithReset() throws CloneNotSupportedException;

   Object clone() throws CloneNotSupportedException;

   int getLast();

   void setLast(int var1);
}
