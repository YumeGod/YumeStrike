package org.apache.batik.anim.timing;

public abstract class TimingSpecifier {
   protected TimedElement owner;
   protected boolean isBegin;

   protected TimingSpecifier(TimedElement var1, boolean var2) {
      this.owner = var1;
      this.isBegin = var2;
   }

   public TimedElement getOwner() {
      return this.owner;
   }

   public boolean isBegin() {
      return this.isBegin;
   }

   public void initialize() {
   }

   public void deinitialize() {
   }

   public abstract boolean isEventCondition();

   float newInterval(Interval var1) {
      return Float.POSITIVE_INFINITY;
   }

   float removeInterval(Interval var1) {
      return Float.POSITIVE_INFINITY;
   }

   float handleTimebaseUpdate(InstanceTime var1, float var2) {
      return Float.POSITIVE_INFINITY;
   }
}
