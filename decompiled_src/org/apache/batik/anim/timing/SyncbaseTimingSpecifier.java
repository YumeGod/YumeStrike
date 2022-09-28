package org.apache.batik.anim.timing;

import java.util.HashMap;

public class SyncbaseTimingSpecifier extends OffsetTimingSpecifier {
   protected String syncbaseID;
   protected TimedElement syncbaseElement;
   protected boolean syncBegin;
   protected HashMap instances = new HashMap();

   public SyncbaseTimingSpecifier(TimedElement var1, boolean var2, float var3, String var4, boolean var5) {
      super(var1, var2, var3);
      this.syncbaseID = var4;
      this.syncBegin = var5;
      this.syncbaseElement = var1.getTimedElementById(var4);
      this.syncbaseElement.addDependent(this, var5);
   }

   public String toString() {
      return this.syncbaseID + "." + (this.syncBegin ? "begin" : "end") + (this.offset != 0.0F ? super.toString() : "");
   }

   public void initialize() {
   }

   public boolean isEventCondition() {
      return false;
   }

   float newInterval(Interval var1) {
      if (this.owner.hasPropagated) {
         return Float.POSITIVE_INFINITY;
      } else {
         InstanceTime var2 = new InstanceTime(this, (this.syncBegin ? var1.getBegin() : var1.getEnd()) + this.offset, true);
         this.instances.put(var1, var2);
         var1.addDependent(var2, this.syncBegin);
         return this.owner.addInstanceTime(var2, this.isBegin);
      }
   }

   float removeInterval(Interval var1) {
      if (this.owner.hasPropagated) {
         return Float.POSITIVE_INFINITY;
      } else {
         InstanceTime var2 = (InstanceTime)this.instances.get(var1);
         var1.removeDependent(var2, this.syncBegin);
         return this.owner.removeInstanceTime(var2, this.isBegin);
      }
   }

   float handleTimebaseUpdate(InstanceTime var1, float var2) {
      return this.owner.hasPropagated ? Float.POSITIVE_INFINITY : this.owner.instanceTimeChanged(var1, this.isBegin);
   }
}
