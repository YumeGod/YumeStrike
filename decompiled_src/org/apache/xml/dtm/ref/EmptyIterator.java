package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTMAxisIterator;

public final class EmptyIterator implements DTMAxisIterator {
   private static final EmptyIterator INSTANCE = new EmptyIterator();

   public static DTMAxisIterator getInstance() {
      return INSTANCE;
   }

   private EmptyIterator() {
   }

   public final int next() {
      return -1;
   }

   public final DTMAxisIterator reset() {
      return this;
   }

   public final int getLast() {
      return 0;
   }

   public final int getPosition() {
      return 1;
   }

   public final void setMark() {
   }

   public final void gotoMark() {
   }

   public final DTMAxisIterator setStartNode(int node) {
      return this;
   }

   public final int getStartNode() {
      return -1;
   }

   public final boolean isReverse() {
      return false;
   }

   public final DTMAxisIterator cloneIterator() {
      return this;
   }

   public final void setRestartable(boolean isRestartable) {
   }

   public final int getNodeByPosition(int position) {
      return -1;
   }
}
