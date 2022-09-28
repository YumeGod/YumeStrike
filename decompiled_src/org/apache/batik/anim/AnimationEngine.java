package org.apache.batik.anim;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.anim.timing.TimedDocumentRoot;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.timing.TimegraphListener;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.anim.AnimationTargetListener;
import org.apache.batik.util.DoublyIndexedTable;
import org.w3c.dom.Document;

public abstract class AnimationEngine {
   public static final short ANIM_TYPE_XML = 0;
   public static final short ANIM_TYPE_CSS = 1;
   public static final short ANIM_TYPE_OTHER = 2;
   protected Document document;
   protected TimedDocumentRoot timedDocumentRoot;
   protected long pauseTime;
   protected HashMap targets = new HashMap();
   protected HashMap animations = new HashMap();
   protected Listener targetListener = new Listener();
   protected static final Map.Entry[] MAP_ENTRY_ARRAY = new Map.Entry[0];

   public AnimationEngine(Document var1) {
      this.document = var1;
      this.timedDocumentRoot = this.createDocumentRoot();
   }

   public void dispose() {
      Iterator var1 = this.targets.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         AnimationTarget var3 = (AnimationTarget)var2.getKey();
         TargetInfo var4 = (TargetInfo)var2.getValue();
         Iterator var5 = var4.xmlAnimations.iterator();

         String var7;
         while(var5.hasNext()) {
            DoublyIndexedTable.Entry var6 = (DoublyIndexedTable.Entry)var5.next();
            var7 = (String)var6.getKey1();
            String var8 = (String)var6.getKey2();
            Sandwich var9 = (Sandwich)var6.getValue();
            if (var9.listenerRegistered) {
               var3.removeTargetListener(var7, var8, false, this.targetListener);
            }
         }

         var5 = var4.cssAnimations.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var10 = (Map.Entry)var5.next();
            var7 = (String)var10.getKey();
            Sandwich var11 = (Sandwich)var10.getValue();
            if (var11.listenerRegistered) {
               var3.removeTargetListener((String)null, var7, true, this.targetListener);
            }
         }
      }

   }

   public void pause() {
      if (this.pauseTime == 0L) {
         this.pauseTime = System.currentTimeMillis();
      }

   }

   public void unpause() {
      if (this.pauseTime != 0L) {
         Calendar var1 = this.timedDocumentRoot.getDocumentBeginTime();
         int var2 = (int)(System.currentTimeMillis() - this.pauseTime);
         var1.add(14, var2);
         this.pauseTime = 0L;
      }

   }

   public boolean isPaused() {
      return this.pauseTime != 0L;
   }

   public float getCurrentTime() {
      return this.timedDocumentRoot.getCurrentTime();
   }

   public float setCurrentTime(float var1) {
      boolean var2 = this.pauseTime != 0L;
      this.unpause();
      Calendar var3 = this.timedDocumentRoot.getDocumentBeginTime();
      float var4 = this.timedDocumentRoot.convertEpochTime(System.currentTimeMillis());
      var3.add(14, (int)((var4 - var1) * 1000.0F));
      if (var2) {
         this.pause();
      }

      return this.tick(var1, true);
   }

   public void addAnimation(AnimationTarget var1, short var2, String var3, String var4, AbstractAnimation var5) {
      this.timedDocumentRoot.addChild(var5.getTimedElement());
      AnimationInfo var6 = this.getAnimationInfo(var5);
      var6.type = var2;
      var6.attributeNamespaceURI = var3;
      var6.attributeLocalName = var4;
      var6.target = var1;
      this.animations.put(var5, var6);
      Sandwich var7 = this.getSandwich(var1, var2, var3, var4);
      if (var7.animation == null) {
         var5.lowerAnimation = null;
         var5.higherAnimation = null;
      } else {
         var7.animation.higherAnimation = var5;
         var5.lowerAnimation = var7.animation;
         var5.higherAnimation = null;
      }

      var7.animation = var5;
      if (var5.lowerAnimation == null) {
         var7.lowestAnimation = var5;
      }

   }

   public void removeAnimation(AbstractAnimation var1) {
      this.timedDocumentRoot.removeChild(var1.getTimedElement());
      AbstractAnimation var2 = var1.higherAnimation;
      if (var2 != null) {
         var2.markDirty();
      }

      this.moveToBottom(var1);
      if (var1.higherAnimation != null) {
         var1.higherAnimation.lowerAnimation = null;
      }

      AnimationInfo var3 = this.getAnimationInfo(var1);
      Sandwich var4 = this.getSandwich(var3.target, var3.type, var3.attributeNamespaceURI, var3.attributeLocalName);
      if (var4.animation == var1) {
         var4.animation = null;
         var4.lowestAnimation = null;
         var4.shouldUpdate = true;
      }

   }

   protected Sandwich getSandwich(AnimationTarget var1, short var2, String var3, String var4) {
      TargetInfo var5 = this.getTargetInfo(var1);
      Sandwich var6;
      if (var2 == 0) {
         var6 = (Sandwich)var5.xmlAnimations.get(var3, var4);
         if (var6 == null) {
            var6 = new Sandwich();
            var5.xmlAnimations.put(var3, var4, var6);
         }
      } else if (var2 == 1) {
         var6 = (Sandwich)var5.cssAnimations.get(var4);
         if (var6 == null) {
            var6 = new Sandwich();
            var5.cssAnimations.put(var4, var6);
         }
      } else {
         var6 = (Sandwich)var5.otherAnimations.get(var4);
         if (var6 == null) {
            var6 = new Sandwich();
            var5.otherAnimations.put(var4, var6);
         }
      }

      return var6;
   }

   protected TargetInfo getTargetInfo(AnimationTarget var1) {
      TargetInfo var2 = (TargetInfo)this.targets.get(var1);
      if (var2 == null) {
         var2 = new TargetInfo();
         this.targets.put(var1, var2);
      }

      return var2;
   }

   protected AnimationInfo getAnimationInfo(AbstractAnimation var1) {
      AnimationInfo var2 = (AnimationInfo)this.animations.get(var1);
      if (var2 == null) {
         var2 = new AnimationInfo();
         this.animations.put(var1, var2);
      }

      return var2;
   }

   protected float tick(float var1, boolean var2) {
      float var3 = this.timedDocumentRoot.seekTo(var1, var2);
      Map.Entry[] var4 = (Map.Entry[])this.targets.entrySet().toArray(MAP_ENTRY_ARRAY);

      label117:
      for(int var5 = 0; var5 < var4.length; ++var5) {
         Map.Entry var6 = var4[var5];
         AnimationTarget var7 = (AnimationTarget)var6.getKey();
         TargetInfo var8 = (TargetInfo)var6.getValue();
         Iterator var9 = var8.xmlAnimations.iterator();

         while(true) {
            String var11;
            String var12;
            Sandwich var13;
            do {
               if (!var9.hasNext()) {
                  var9 = var8.cssAnimations.entrySet().iterator();

                  while(true) {
                     Sandwich var18;
                     AnimatableValue var19;
                     do {
                        Map.Entry var17;
                        if (!var9.hasNext()) {
                           var9 = var8.otherAnimations.entrySet().iterator();

                           while(true) {
                              do {
                                 if (!var9.hasNext()) {
                                    continue label117;
                                 }

                                 var17 = (Map.Entry)var9.next();
                                 var11 = (String)var17.getKey();
                                 var18 = (Sandwich)var17.getValue();
                              } while(!var18.shouldUpdate && !var18.animation.isDirty);

                              var19 = null;
                              AbstractAnimation var22 = var18.animation;
                              if (var22 != null) {
                                 var19 = var18.animation.getComposedValue();
                                 var22.isDirty = false;
                              }

                              var7.updateOtherValue(var11, var19);
                              var18.shouldUpdate = false;
                           }
                        }

                        var17 = (Map.Entry)var9.next();
                        var11 = (String)var17.getKey();
                        var18 = (Sandwich)var17.getValue();
                     } while(!var18.shouldUpdate && (var18.animation == null || !var18.animation.isDirty));

                     var19 = null;
                     boolean var20 = false;
                     AbstractAnimation var21 = var18.animation;
                     if (var21 != null) {
                        var19 = var21.getComposedValue();
                        var20 = var18.lowestAnimation.usesUnderlyingValue();
                        var21.isDirty = false;
                     }

                     if (var20 && !var18.listenerRegistered) {
                        var7.addTargetListener((String)null, var11, true, this.targetListener);
                        var18.listenerRegistered = true;
                     } else if (!var20 && var18.listenerRegistered) {
                        var7.removeTargetListener((String)null, var11, true, this.targetListener);
                        var18.listenerRegistered = false;
                     }

                     if (var20) {
                        var7.updatePropertyValue(var11, (AnimatableValue)null);
                     }

                     if (!var20 || var19 != null) {
                        var7.updatePropertyValue(var11, var19);
                     }

                     var18.shouldUpdate = false;
                  }
               }

               DoublyIndexedTable.Entry var10 = (DoublyIndexedTable.Entry)var9.next();
               var11 = (String)var10.getKey1();
               var12 = (String)var10.getKey2();
               var13 = (Sandwich)var10.getValue();
            } while(!var13.shouldUpdate && (var13.animation == null || !var13.animation.isDirty));

            AnimatableValue var14 = null;
            boolean var15 = false;
            AbstractAnimation var16 = var13.animation;
            if (var16 != null) {
               var14 = var16.getComposedValue();
               var15 = var13.lowestAnimation.usesUnderlyingValue();
               var16.isDirty = false;
            }

            if (var15 && !var13.listenerRegistered) {
               var7.addTargetListener(var11, var12, false, this.targetListener);
               var13.listenerRegistered = true;
            } else if (!var15 && var13.listenerRegistered) {
               var7.removeTargetListener(var11, var12, false, this.targetListener);
               var13.listenerRegistered = false;
            }

            var7.updateAttributeValue(var11, var12, var14);
            var13.shouldUpdate = false;
         }
      }

      return var3;
   }

   public void toActive(AbstractAnimation var1, float var2) {
      this.moveToTop(var1);
      var1.isActive = true;
      var1.beginTime = var2;
      var1.isFrozen = false;
      this.pushDown(var1);
      var1.markDirty();
   }

   protected void pushDown(AbstractAnimation var1) {
      TimedElement var2 = var1.getTimedElement();
      AbstractAnimation var3 = null;
      boolean var4 = false;

      while(var1.lowerAnimation != null && (var1.lowerAnimation.isActive || var1.lowerAnimation.isFrozen) && (var1.lowerAnimation.beginTime > var1.beginTime || var1.lowerAnimation.beginTime == var1.beginTime && var2.isBefore(var1.lowerAnimation.getTimedElement()))) {
         AbstractAnimation var5 = var1.higherAnimation;
         AbstractAnimation var6 = var1.lowerAnimation;
         AbstractAnimation var7 = var6.lowerAnimation;
         if (var5 != null) {
            var5.lowerAnimation = var6;
         }

         if (var7 != null) {
            var7.higherAnimation = var1;
         }

         var6.lowerAnimation = var1;
         var6.higherAnimation = var5;
         var1.lowerAnimation = var7;
         var1.higherAnimation = var6;
         if (!var4) {
            var3 = var6;
            var4 = true;
         }
      }

      if (var4) {
         AnimationInfo var8 = this.getAnimationInfo(var1);
         Sandwich var9 = this.getSandwich(var8.target, var8.type, var8.attributeNamespaceURI, var8.attributeLocalName);
         if (var9.animation == var1) {
            var9.animation = var3;
         }

         if (var1.lowerAnimation == null) {
            var9.lowestAnimation = var1;
         }
      }

   }

   public void toInactive(AbstractAnimation var1, boolean var2) {
      var1.isActive = false;
      var1.isFrozen = var2;
      var1.beginTime = Float.NEGATIVE_INFINITY;
      var1.markDirty();
      if (!var2) {
         var1.value = null;
         this.moveToBottom(var1);
      } else {
         this.pushDown(var1);
      }

   }

   public void removeFill(AbstractAnimation var1) {
      var1.isActive = false;
      var1.isFrozen = false;
      var1.value = null;
      var1.markDirty();
      this.moveToBottom(var1);
   }

   protected void moveToTop(AbstractAnimation var1) {
      AnimationInfo var2 = this.getAnimationInfo(var1);
      Sandwich var3 = this.getSandwich(var2.target, var2.type, var2.attributeNamespaceURI, var2.attributeLocalName);
      var3.shouldUpdate = true;
      if (var1.higherAnimation != null) {
         if (var1.lowerAnimation == null) {
            var3.lowestAnimation = var1.higherAnimation;
         } else {
            var1.lowerAnimation.higherAnimation = var1.higherAnimation;
         }

         var1.higherAnimation.lowerAnimation = var1.lowerAnimation;
         if (var3.animation != null) {
            var3.animation.higherAnimation = var1;
         }

         var1.lowerAnimation = var3.animation;
         var1.higherAnimation = null;
         var3.animation = var1;
      }
   }

   protected void moveToBottom(AbstractAnimation var1) {
      if (var1.lowerAnimation != null) {
         AnimationInfo var2 = this.getAnimationInfo(var1);
         Sandwich var3 = this.getSandwich(var2.target, var2.type, var2.attributeNamespaceURI, var2.attributeLocalName);
         AbstractAnimation var4 = var1.lowerAnimation;
         var4.markDirty();
         var1.lowerAnimation.higherAnimation = var1.higherAnimation;
         if (var1.higherAnimation != null) {
            var1.higherAnimation.lowerAnimation = var1.lowerAnimation;
         } else {
            var3.animation = var4;
            var3.shouldUpdate = true;
         }

         var3.lowestAnimation.lowerAnimation = var1;
         var1.higherAnimation = var3.lowestAnimation;
         var1.lowerAnimation = null;
         var3.lowestAnimation = var1;
         if (var3.animation.isDirty) {
            var3.shouldUpdate = true;
         }

      }
   }

   public void addTimegraphListener(TimegraphListener var1) {
      this.timedDocumentRoot.addTimegraphListener(var1);
   }

   public void removeTimegraphListener(TimegraphListener var1) {
      this.timedDocumentRoot.removeTimegraphListener(var1);
   }

   public void sampledAt(AbstractAnimation var1, float var2, float var3, int var4) {
      var1.sampledAt(var2, var3, var4);
   }

   public void sampledLastValue(AbstractAnimation var1, int var2) {
      var1.sampledLastValue(var2);
   }

   protected abstract TimedDocumentRoot createDocumentRoot();

   protected static class AnimationInfo {
      public AnimationTarget target;
      public short type;
      public String attributeNamespaceURI;
      public String attributeLocalName;
   }

   protected static class Sandwich {
      public AbstractAnimation animation;
      public AbstractAnimation lowestAnimation;
      public boolean shouldUpdate;
      public boolean listenerRegistered;
   }

   protected static class TargetInfo {
      public DoublyIndexedTable xmlAnimations = new DoublyIndexedTable();
      public HashMap cssAnimations = new HashMap();
      public HashMap otherAnimations = new HashMap();
   }

   protected class Listener implements AnimationTargetListener {
      public void baseValueChanged(AnimationTarget var1, String var2, String var3, boolean var4) {
         int var5 = var4 ? 1 : 0;
         Sandwich var6 = AnimationEngine.this.getSandwich(var1, (short)var5, var2, var3);
         var6.shouldUpdate = true;

         AbstractAnimation var7;
         for(var7 = var6.animation; var7.lowerAnimation != null; var7 = var7.lowerAnimation) {
         }

         var7.markDirty();
      }
   }
}
