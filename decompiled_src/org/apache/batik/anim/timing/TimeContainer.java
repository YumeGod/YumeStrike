package org.apache.batik.anim.timing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class TimeContainer extends TimedElement {
   protected List children = new LinkedList();

   public void addChild(TimedElement var1) {
      if (var1 == this) {
         throw new IllegalArgumentException("recursive datastructure not allowed here!");
      } else {
         this.children.add(var1);
         var1.parent = this;
         this.setRoot(var1, this.root);
         this.root.fireElementAdded(var1);
         this.root.currentIntervalWillUpdate();
      }
   }

   protected void setRoot(TimedElement var1, TimedDocumentRoot var2) {
      var1.root = var2;
      if (var1 instanceof TimeContainer) {
         TimeContainer var3 = (TimeContainer)var1;
         Iterator var4 = var3.children.iterator();

         while(var4.hasNext()) {
            TimedElement var5 = (TimedElement)var4.next();
            this.setRoot(var5, var2);
         }
      }

   }

   public void removeChild(TimedElement var1) {
      this.children.remove(var1);
      var1.parent = null;
      this.setRoot(var1, (TimedDocumentRoot)null);
      this.root.fireElementRemoved(var1);
      this.root.currentIntervalWillUpdate();
   }

   public TimedElement[] getChildren() {
      return (TimedElement[])this.children.toArray(new TimedElement[0]);
   }

   protected float sampleAt(float var1, boolean var2) {
      super.sampleAt(var1, var2);
      return this.sampleChildren(var1, var2);
   }

   protected float sampleChildren(float var1, boolean var2) {
      float var3 = Float.POSITIVE_INFINITY;
      Iterator var4 = this.children.iterator();

      while(var4.hasNext()) {
         TimedElement var5 = (TimedElement)var4.next();
         float var6 = var5.sampleAt(var1, var2);
         if (var6 < var3) {
            var3 = var6;
         }
      }

      return var3;
   }

   protected void reset(boolean var1) {
      super.reset(var1);
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         TimedElement var3 = (TimedElement)var2.next();
         var3.reset(var1);
      }

   }

   protected boolean isConstantAnimation() {
      return false;
   }

   public abstract float getDefaultBegin(TimedElement var1);
}
