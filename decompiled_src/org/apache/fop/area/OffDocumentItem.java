package org.apache.fop.area;

public interface OffDocumentItem {
   int IMMEDIATELY = 0;
   int AFTER_PAGE = 1;
   int END_OF_DOC = 2;

   int getWhenToProcess();

   String getName();
}
