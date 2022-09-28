package org.apache.fop.layoutmgr;

import org.apache.fop.traits.MinOptMax;

public interface ConditionalElementListener {
   void notifySpace(RelSide var1, MinOptMax var2);

   void notifyBorder(RelSide var1, MinOptMax var2);

   void notifyPadding(RelSide var1, MinOptMax var2);
}
