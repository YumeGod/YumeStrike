package org.apache.batik.anim.timing;

public class InstanceTime implements Comparable {
   protected float time;
   protected TimingSpecifier creator;
   protected boolean clearOnReset;

   public InstanceTime(TimingSpecifier var1, float var2, boolean var3) {
      this.creator = var1;
      this.time = var2;
      this.clearOnReset = var3;
   }

   public boolean getClearOnReset() {
      return this.clearOnReset;
   }

   public float getTime() {
      return this.time;
   }

   float dependentUpdate(float var1) {
      this.time = var1;
      return this.creator != null ? this.creator.handleTimebaseUpdate(this, this.time) : Float.POSITIVE_INFINITY;
   }

   public String toString() {
      return Float.toString(this.time);
   }

   public int compareTo(Object var1) {
      InstanceTime var2 = (InstanceTime)var1;
      if (this.time == var2.time) {
         return 0;
      } else {
         return this.time > var2.time ? 1 : -1;
      }
   }
}
