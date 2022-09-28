package org.apache.batik.anim.timing;

public class IndefiniteTimingSpecifier extends TimingSpecifier {
   public IndefiniteTimingSpecifier(TimedElement var1, boolean var2) {
      super(var1, var2);
   }

   public String toString() {
      return "indefinite";
   }

   public void initialize() {
      if (!this.isBegin) {
         InstanceTime var1 = new InstanceTime(this, Float.POSITIVE_INFINITY, false);
         this.owner.addInstanceTime(var1, this.isBegin);
      }

   }

   public boolean isEventCondition() {
      return false;
   }
}
