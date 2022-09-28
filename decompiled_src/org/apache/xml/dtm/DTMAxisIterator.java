package org.apache.xml.dtm;

public interface DTMAxisIterator extends Cloneable {
   int END = -1;

   int next();

   DTMAxisIterator reset();

   int getLast();

   int getPosition();

   void setMark();

   void gotoMark();

   DTMAxisIterator setStartNode(int var1);

   int getStartNode();

   boolean isReverse();

   DTMAxisIterator cloneIterator();

   void setRestartable(boolean var1);

   int getNodeByPosition(int var1);
}
