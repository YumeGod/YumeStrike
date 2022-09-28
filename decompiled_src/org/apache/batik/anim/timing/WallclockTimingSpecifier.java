package org.apache.batik.anim.timing;

import java.util.Calendar;

public class WallclockTimingSpecifier extends TimingSpecifier {
   protected Calendar time;
   protected InstanceTime instance;

   public WallclockTimingSpecifier(TimedElement var1, boolean var2, Calendar var3) {
      super(var1, var2);
      this.time = var3;
   }

   public String toString() {
      return "wallclock(" + this.time.toString() + ")";
   }

   public void initialize() {
      float var1 = this.owner.getRoot().convertWallclockTime(this.time);
      this.instance = new InstanceTime(this, var1, false);
      this.owner.addInstanceTime(this.instance, this.isBegin);
   }

   public boolean isEventCondition() {
      return false;
   }
}
