package org.apache.fop.fo.pagination;

public interface SubSequenceSpecifier {
   String getNextPageMasterName(boolean var1, boolean var2, boolean var3, boolean var4) throws PageProductionException;

   void reset();

   boolean goToPrevious();

   boolean hasPagePositionLast();

   boolean hasPagePositionOnly();
}
