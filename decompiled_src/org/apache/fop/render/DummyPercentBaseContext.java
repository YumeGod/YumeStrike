package org.apache.fop.render;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;

public final class DummyPercentBaseContext implements PercentBaseContext {
   private static DummyPercentBaseContext singleton = new DummyPercentBaseContext();

   private DummyPercentBaseContext() {
   }

   public static DummyPercentBaseContext getInstance() {
      return singleton;
   }

   public int getBaseLength(int lengthBase, FObj fo) {
      return 0;
   }
}
