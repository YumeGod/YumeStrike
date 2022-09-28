package org.apache.batik.anim.timing;

public class OffsetTimingSpecifier extends TimingSpecifier {
   protected float offset;

   public OffsetTimingSpecifier(TimedElement var1, boolean var2, float var3) {
      super(var1, var2);
      this.offset = var3;
   }

   public String toString() {
      return (this.offset >= 0.0F ? "+" : "") + this.offset;
   }

   public void initialize() {
      InstanceTime var1 = new InstanceTime(this, this.offset, false);
      this.owner.addInstanceTime(var1, this.isBegin);
   }

   public boolean isEventCondition() {
      return false;
   }
}
