package org.apache.batik.anim.timing;

public interface TimegraphListener {
   void elementAdded(TimedElement var1);

   void elementRemoved(TimedElement var1);

   void elementActivated(TimedElement var1, float var2);

   void elementFilled(TimedElement var1, float var2);

   void elementDeactivated(TimedElement var1, float var2);

   void intervalCreated(TimedElement var1, Interval var2);

   void intervalRemoved(TimedElement var1, Interval var2);

   void intervalChanged(TimedElement var1, Interval var2);

   void intervalBegan(TimedElement var1, Interval var2);

   void elementRepeated(TimedElement var1, int var2, float var3);

   void elementInstanceTimesChanged(TimedElement var1, float var2);
}
