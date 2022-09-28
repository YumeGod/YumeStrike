package org.apache.fop.area;

public abstract class AbstractOffDocumentItem implements OffDocumentItem {
   public static final int IMMEDIATELY = 0;
   public static final int AFTER_PAGE = 1;
   public static final int END_OF_DOC = 2;
   public static final int START_OF_DOC = 2;
   protected int whenToProcess = 0;

   public int getWhenToProcess() {
      return this.whenToProcess;
   }

   public abstract String getName();
}
