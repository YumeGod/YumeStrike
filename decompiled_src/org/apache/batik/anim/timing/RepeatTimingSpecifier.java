package org.apache.batik.anim.timing;

import org.w3c.dom.events.Event;
import org.w3c.dom.smil.TimeEvent;

public class RepeatTimingSpecifier extends EventbaseTimingSpecifier {
   protected int repeatIteration;
   protected boolean repeatIterationSpecified;

   public RepeatTimingSpecifier(TimedElement var1, boolean var2, float var3, String var4) {
      super(var1, var2, var3, var4, var1.getRoot().getRepeatEventName());
   }

   public RepeatTimingSpecifier(TimedElement var1, boolean var2, float var3, String var4, int var5) {
      super(var1, var2, var3, var4, var1.getRoot().getRepeatEventName());
      this.repeatIteration = var5;
      this.repeatIterationSpecified = true;
   }

   public String toString() {
      return (this.eventbaseID == null ? "" : this.eventbaseID + ".") + "repeat" + (this.repeatIterationSpecified ? "(" + this.repeatIteration + ")" : "") + (this.offset != 0.0F ? super.toString() : "");
   }

   public void handleEvent(Event var1) {
      TimeEvent var2 = (TimeEvent)var1;
      if (!this.repeatIterationSpecified || var2.getDetail() == this.repeatIteration) {
         super.handleEvent(var1);
      }

   }
}
