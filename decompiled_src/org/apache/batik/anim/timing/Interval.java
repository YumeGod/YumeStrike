package org.apache.batik.anim.timing;

import java.util.Iterator;
import java.util.LinkedList;

public class Interval {
   protected float begin;
   protected float end;
   protected InstanceTime beginInstanceTime;
   protected InstanceTime endInstanceTime;
   protected LinkedList beginDependents = new LinkedList();
   protected LinkedList endDependents = new LinkedList();

   public Interval(float var1, float var2, InstanceTime var3, InstanceTime var4) {
      this.begin = var1;
      this.end = var2;
      this.beginInstanceTime = var3;
      this.endInstanceTime = var4;
   }

   public String toString() {
      return TimedElement.toString(this.begin) + ".." + TimedElement.toString(this.end);
   }

   public float getBegin() {
      return this.begin;
   }

   public float getEnd() {
      return this.end;
   }

   public InstanceTime getBeginInstanceTime() {
      return this.beginInstanceTime;
   }

   public InstanceTime getEndInstanceTime() {
      return this.endInstanceTime;
   }

   void addDependent(InstanceTime var1, boolean var2) {
      if (var2) {
         this.beginDependents.add(var1);
      } else {
         this.endDependents.add(var1);
      }

   }

   void removeDependent(InstanceTime var1, boolean var2) {
      if (var2) {
         this.beginDependents.remove(var1);
      } else {
         this.endDependents.remove(var1);
      }

   }

   float setBegin(float var1) {
      float var2 = Float.POSITIVE_INFINITY;
      this.begin = var1;
      Iterator var3 = this.beginDependents.iterator();

      while(var3.hasNext()) {
         InstanceTime var4 = (InstanceTime)var3.next();
         float var5 = var4.dependentUpdate(var1);
         if (var5 < var2) {
            var2 = var5;
         }
      }

      return var2;
   }

   float setEnd(float var1, InstanceTime var2) {
      float var3 = Float.POSITIVE_INFINITY;
      this.end = var1;
      this.endInstanceTime = var2;
      Iterator var4 = this.endDependents.iterator();

      while(var4.hasNext()) {
         InstanceTime var5 = (InstanceTime)var4.next();
         float var6 = var5.dependentUpdate(var1);
         if (var6 < var3) {
            var3 = var6;
         }
      }

      return var3;
   }
}
