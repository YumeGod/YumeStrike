package org.apache.batik.anim.timing;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.batik.util.DoublyIndexedSet;

public abstract class TimedDocumentRoot extends TimeContainer {
   protected Calendar documentBeginTime;
   protected boolean useSVG11AccessKeys;
   protected boolean useSVG12AccessKeys;
   protected DoublyIndexedSet propagationFlags = new DoublyIndexedSet();
   protected LinkedList listeners = new LinkedList();
   protected boolean isSampling;
   protected boolean isHyperlinking;

   public TimedDocumentRoot(boolean var1, boolean var2) {
      this.root = this;
      this.useSVG11AccessKeys = var1;
      this.useSVG12AccessKeys = var2;
   }

   protected float getImplicitDur() {
      return Float.POSITIVE_INFINITY;
   }

   public float getDefaultBegin(TimedElement var1) {
      return 0.0F;
   }

   public float getCurrentTime() {
      return this.lastSampleTime;
   }

   public boolean isSampling() {
      return this.isSampling;
   }

   public boolean isHyperlinking() {
      return this.isHyperlinking;
   }

   public float seekTo(float var1, boolean var2) {
      this.isSampling = true;
      this.lastSampleTime = var1;
      this.isHyperlinking = var2;
      this.propagationFlags.clear();
      float var3 = Float.POSITIVE_INFINITY;
      TimedElement[] var4 = this.getChildren();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         float var6 = var4[var5].sampleAt(var1, var2);
         if (var6 < var3) {
            var3 = var6;
         }
      }

      boolean var8;
      do {
         var8 = false;

         for(int var9 = 0; var9 < var4.length; ++var9) {
            if (var4[var9].shouldUpdateCurrentInterval) {
               var8 = true;
               float var7 = var4[var9].sampleAt(var1, var2);
               if (var7 < var3) {
                  var3 = var7;
               }
            }
         }
      } while(var8);

      this.isSampling = false;
      if (var2) {
         this.root.currentIntervalWillUpdate();
      }

      return var3;
   }

   public void resetDocument(Calendar var1) {
      if (var1 == null) {
         this.documentBeginTime = Calendar.getInstance();
      } else {
         this.documentBeginTime = var1;
      }

      this.reset(true);
   }

   public Calendar getDocumentBeginTime() {
      return this.documentBeginTime;
   }

   public float convertEpochTime(long var1) {
      long var3 = this.documentBeginTime.getTime().getTime();
      return (float)(var1 - var3) / 1000.0F;
   }

   public float convertWallclockTime(Calendar var1) {
      long var2 = this.documentBeginTime.getTime().getTime();
      long var4 = var1.getTime().getTime();
      return (float)(var4 - var2) / 1000.0F;
   }

   public void addTimegraphListener(TimegraphListener var1) {
      this.listeners.add(var1);
   }

   public void removeTimegraphListener(TimegraphListener var1) {
      this.listeners.remove(var1);
   }

   void fireElementAdded(TimedElement var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((TimegraphListener)var2.next()).elementAdded(var1);
      }

   }

   void fireElementRemoved(TimedElement var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((TimegraphListener)var2.next()).elementRemoved(var1);
      }

   }

   boolean shouldPropagate(Interval var1, TimingSpecifier var2, boolean var3) {
      InstanceTime var4 = var3 ? var1.getBeginInstanceTime() : var1.getEndInstanceTime();
      if (this.propagationFlags.contains(var4, var2)) {
         return false;
      } else {
         this.propagationFlags.add(var4, var2);
         return true;
      }
   }

   protected void currentIntervalWillUpdate() {
   }

   protected abstract String getEventNamespaceURI(String var1);

   protected abstract String getEventType(String var1);

   protected abstract String getRepeatEventName();
}
