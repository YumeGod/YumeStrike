package org.apache.xml.dtm;

public interface DTMIterator {
   short FILTER_ACCEPT = 1;
   short FILTER_REJECT = 2;
   short FILTER_SKIP = 3;

   DTM getDTM(int var1);

   DTMManager getDTMManager();

   int getRoot();

   void setRoot(int var1, Object var2);

   void reset();

   int getWhatToShow();

   boolean getExpandEntityReferences();

   int nextNode();

   int previousNode();

   void detach();

   void allowDetachToRelease(boolean var1);

   int getCurrentNode();

   boolean isFresh();

   void setShouldCacheNodes(boolean var1);

   boolean isMutable();

   int getCurrentPos();

   void runTo(int var1);

   void setCurrentPos(int var1);

   int item(int var1);

   void setItem(int var1, int var2);

   int getLength();

   DTMIterator cloneWithReset() throws CloneNotSupportedException;

   Object clone() throws CloneNotSupportedException;

   boolean isDocOrdered();

   int getAxis();
}
