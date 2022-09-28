package org.apache.fop.layoutmgr;

import org.apache.fop.fo.properties.KeepProperty;

public interface BlockLevelLayoutManager extends LayoutManager {
   int negotiateBPDAdjustment(int var1, KnuthElement var2);

   void discardSpace(KnuthGlue var1);

   Keep getKeepTogether();

   boolean mustKeepTogether();

   Keep getKeepWithPrevious();

   boolean mustKeepWithPrevious();

   Keep getKeepWithNext();

   boolean mustKeepWithNext();

   KeepProperty getKeepTogetherProperty();

   KeepProperty getKeepWithPreviousProperty();

   KeepProperty getKeepWithNextProperty();
}
