package org.apache.xalan.xsltc;

public interface NodeIterator extends Cloneable {
   int END = -1;

   int next();

   NodeIterator reset();

   int getLast();

   int getPosition();

   void setMark();

   void gotoMark();

   NodeIterator setStartNode(int var1);

   boolean isReverse();

   NodeIterator cloneIterator();

   void setRestartable(boolean var1);
}
