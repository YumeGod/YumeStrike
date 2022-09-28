package org.apache.batik.anim.timing;

import org.w3c.dom.events.Event;

public abstract class EventLikeTimingSpecifier extends OffsetTimingSpecifier {
   public EventLikeTimingSpecifier(TimedElement var1, boolean var2, float var3) {
      super(var1, var2, var3);
   }

   public boolean isEventCondition() {
      return true;
   }

   public abstract void resolve(Event var1);
}
