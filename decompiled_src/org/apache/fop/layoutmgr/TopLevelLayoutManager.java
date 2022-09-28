package org.apache.fop.layoutmgr;

import org.apache.fop.datatypes.Numeric;

public interface TopLevelLayoutManager {
   void activateLayout();

   void doForcePageCount(Numeric var1);

   void finishPageSequence();
}
