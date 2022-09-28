package org.apache.batik.anim.timing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import org.apache.batik.anim.AnimationException;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.parser.ClockHandler;
import org.apache.batik.parser.ClockParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.util.SMILConstants;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public abstract class TimedElement implements SMILConstants {
   public static final int FILL_REMOVE = 0;
   public static final int FILL_FREEZE = 1;
   public static final int RESTART_ALWAYS = 0;
   public static final int RESTART_WHEN_NOT_ACTIVE = 1;
   public static final int RESTART_NEVER = 2;
   public static final float INDEFINITE = Float.POSITIVE_INFINITY;
   public static final float UNRESOLVED = Float.NaN;
   protected TimedDocumentRoot root;
   protected TimeContainer parent;
   protected TimingSpecifier[] beginTimes = new TimingSpecifier[0];
   protected TimingSpecifier[] endTimes;
   protected float simpleDur;
   protected boolean durMedia;
   protected float repeatCount;
   protected float repeatDur;
   protected int currentRepeatIteration;
   protected float lastRepeatTime;
   protected int fillMode;
   protected int restartMode;
   protected float min;
   protected boolean minMedia;
   protected float max;
   protected boolean maxMedia;
   protected boolean isActive;
   protected boolean isFrozen;
   protected float lastSampleTime;
   protected float repeatDuration;
   protected List beginInstanceTimes = new ArrayList();
   protected List endInstanceTimes = new ArrayList();
   protected Interval currentInterval;
   protected float lastIntervalEnd;
   protected Interval previousInterval;
   protected LinkedList beginDependents = new LinkedList();
   protected LinkedList endDependents = new LinkedList();
   protected boolean shouldUpdateCurrentInterval = true;
   protected boolean hasParsed;
   protected Map handledEvents = new HashMap();
   protected boolean isSampling;
   protected boolean hasPropagated;
   protected static final String RESOURCES = "org.apache.batik.anim.resources.Messages";
   protected static LocalizableSupport localizableSupport;
   // $FF: synthetic field
   static Class class$org$apache$batik$anim$timing$TimedElement;

   public TimedElement() {
      this.endTimes = this.beginTimes;
      this.simpleDur = Float.NaN;
      this.repeatCount = Float.NaN;
      this.repeatDur = Float.NaN;
      this.lastRepeatTime = Float.NaN;
      this.max = Float.POSITIVE_INFINITY;
      this.lastSampleTime = Float.NaN;
      this.lastIntervalEnd = Float.NEGATIVE_INFINITY;
   }

   public TimedDocumentRoot getRoot() {
      return this.root;
   }

   public float getActiveTime() {
      return this.lastSampleTime;
   }

   public float getSimpleTime() {
      return this.lastSampleTime - this.lastRepeatTime;
   }

   protected float addInstanceTime(InstanceTime var1, boolean var2) {
      this.hasPropagated = true;
      List var3 = var2 ? this.beginInstanceTimes : this.endInstanceTimes;
      int var4 = Collections.binarySearch(var3, var1);
      if (var4 < 0) {
         var4 = -(var4 + 1);
      }

      var3.add(var4, var1);
      this.shouldUpdateCurrentInterval = true;
      float var5;
      if (this.root.isSampling() && !this.isSampling) {
         var5 = this.sampleAt(this.root.getCurrentTime(), this.root.isHyperlinking());
      } else {
         var5 = Float.POSITIVE_INFINITY;
      }

      this.hasPropagated = false;
      this.root.currentIntervalWillUpdate();
      return var5;
   }

   protected float removeInstanceTime(InstanceTime var1, boolean var2) {
      this.hasPropagated = true;
      List var3 = var2 ? this.beginInstanceTimes : this.endInstanceTimes;
      int var4 = Collections.binarySearch(var3, var1);

      int var5;
      for(var5 = var4; var5 >= 0; --var5) {
         InstanceTime var6 = (InstanceTime)var3.get(var5);
         if (var6 == var1) {
            var3.remove(var5);
            break;
         }

         if (var6.compareTo(var1) != 0) {
            break;
         }
      }

      var5 = var3.size();

      for(int var8 = var4 + 1; var8 < var5; ++var8) {
         InstanceTime var7 = (InstanceTime)var3.get(var8);
         if (var7 == var1) {
            var3.remove(var8);
            break;
         }

         if (var7.compareTo(var1) != 0) {
            break;
         }
      }

      this.shouldUpdateCurrentInterval = true;
      float var9;
      if (this.root.isSampling() && !this.isSampling) {
         var9 = this.sampleAt(this.root.getCurrentTime(), this.root.isHyperlinking());
      } else {
         var9 = Float.POSITIVE_INFINITY;
      }

      this.hasPropagated = false;
      this.root.currentIntervalWillUpdate();
      return var9;
   }

   protected float instanceTimeChanged(InstanceTime var1, boolean var2) {
      this.hasPropagated = true;
      this.shouldUpdateCurrentInterval = true;
      float var3;
      if (this.root.isSampling() && !this.isSampling) {
         var3 = this.sampleAt(this.root.getCurrentTime(), this.root.isHyperlinking());
      } else {
         var3 = Float.POSITIVE_INFINITY;
      }

      this.hasPropagated = false;
      return var3;
   }

   protected void addDependent(TimingSpecifier var1, boolean var2) {
      if (var2) {
         this.beginDependents.add(var1);
      } else {
         this.endDependents.add(var1);
      }

   }

   protected void removeDependent(TimingSpecifier var1, boolean var2) {
      if (var2) {
         this.beginDependents.remove(var1);
      } else {
         this.endDependents.remove(var1);
      }

   }

   public float getSimpleDur() {
      if (this.durMedia) {
         return this.getImplicitDur();
      } else if (isUnresolved(this.simpleDur)) {
         return isUnresolved(this.repeatCount) && isUnresolved(this.repeatDur) && this.endTimes.length > 0 ? Float.POSITIVE_INFINITY : this.getImplicitDur();
      } else {
         return this.simpleDur;
      }
   }

   public static boolean isUnresolved(float var0) {
      return Float.isNaN(var0);
   }

   public float getActiveDur(float var1, float var2) {
      float var3 = this.getSimpleDur();
      float var4;
      if (!isUnresolved(var2) && var3 == Float.POSITIVE_INFINITY) {
         var4 = this.minusTime(var2, var1);
         this.repeatDuration = this.minTime(this.max, this.maxTime(this.min, var4));
         return this.repeatDuration;
      } else {
         float var5;
         if (var3 == 0.0F) {
            var5 = 0.0F;
         } else if (isUnresolved(this.repeatDur) && isUnresolved(this.repeatCount)) {
            var5 = var3;
         } else {
            float var6 = isUnresolved(this.repeatCount) ? Float.POSITIVE_INFINITY : this.multiplyTime(var3, this.repeatCount);
            float var7 = isUnresolved(this.repeatDur) ? Float.POSITIVE_INFINITY : this.repeatDur;
            var5 = this.minTime(this.minTime(var6, var7), Float.POSITIVE_INFINITY);
         }

         if (!isUnresolved(var2) && var2 != Float.POSITIVE_INFINITY) {
            var4 = this.minTime(var5, this.minusTime(var2, var1));
         } else {
            var4 = var5;
         }

         this.repeatDuration = var5;
         return this.minTime(this.max, this.maxTime(this.min, var4));
      }
   }

   protected float minusTime(float var1, float var2) {
      if (!isUnresolved(var1) && !isUnresolved(var2)) {
         return var1 != Float.POSITIVE_INFINITY && var2 != Float.POSITIVE_INFINITY ? var1 - var2 : Float.POSITIVE_INFINITY;
      } else {
         return Float.NaN;
      }
   }

   protected float multiplyTime(float var1, float var2) {
      return !isUnresolved(var1) && var1 != Float.POSITIVE_INFINITY ? var1 * var2 : var1;
   }

   protected float minTime(float var1, float var2) {
      if (var1 != 0.0F && var2 != 0.0F) {
         if ((var1 == Float.POSITIVE_INFINITY || isUnresolved(var1)) && var2 != Float.POSITIVE_INFINITY && !isUnresolved(var2)) {
            return var2;
         } else if ((var2 == Float.POSITIVE_INFINITY || isUnresolved(var2)) && var1 != Float.POSITIVE_INFINITY && !isUnresolved(var1)) {
            return var1;
         } else if ((var1 != Float.POSITIVE_INFINITY || !isUnresolved(var2)) && (!isUnresolved(var1) || var2 != Float.POSITIVE_INFINITY)) {
            return var1 < var2 ? var1 : var2;
         } else {
            return Float.POSITIVE_INFINITY;
         }
      } else {
         return 0.0F;
      }
   }

   protected float maxTime(float var1, float var2) {
      if ((var1 == Float.POSITIVE_INFINITY || isUnresolved(var1)) && var2 != Float.POSITIVE_INFINITY && !isUnresolved(var2)) {
         return var1;
      } else if ((var2 == Float.POSITIVE_INFINITY || isUnresolved(var2)) && var1 != Float.POSITIVE_INFINITY && !isUnresolved(var1)) {
         return var2;
      } else if (var1 == Float.POSITIVE_INFINITY && isUnresolved(var2) || isUnresolved(var1) && var2 == Float.POSITIVE_INFINITY) {
         return Float.NaN;
      } else {
         return var1 > var2 ? var1 : var2;
      }
   }

   protected float getImplicitDur() {
      return Float.NaN;
   }

   protected float notifyNewInterval(Interval var1) {
      float var2 = Float.POSITIVE_INFINITY;
      Iterator var3 = this.beginDependents.iterator();

      TimingSpecifier var4;
      float var5;
      while(var3.hasNext()) {
         var4 = (TimingSpecifier)var3.next();
         var5 = var4.newInterval(var1);
         if (var5 < var2) {
            var2 = var5;
         }
      }

      var3 = this.endDependents.iterator();

      while(var3.hasNext()) {
         var4 = (TimingSpecifier)var3.next();
         var5 = var4.newInterval(var1);
         if (var5 < var2) {
            var2 = var5;
         }
      }

      return var2;
   }

   protected float notifyRemoveInterval(Interval var1) {
      float var2 = Float.POSITIVE_INFINITY;
      Iterator var3 = this.beginDependents.iterator();

      TimingSpecifier var4;
      float var5;
      while(var3.hasNext()) {
         var4 = (TimingSpecifier)var3.next();
         var5 = var4.removeInterval(var1);
         if (var5 < var2) {
            var2 = var5;
         }
      }

      var3 = this.endDependents.iterator();

      while(var3.hasNext()) {
         var4 = (TimingSpecifier)var3.next();
         var5 = var4.removeInterval(var1);
         if (var5 < var2) {
            var2 = var5;
         }
      }

      return var2;
   }

   protected float sampleAt(float var1, boolean var2) {
      this.isSampling = true;
      float var3 = var1;
      Iterator var4 = this.handledEvents.entrySet().iterator();

      label293:
      while(true) {
         Event var6;
         Set var7;
         Iterator var8;
         boolean var12;
         boolean var23;
         while(true) {
            boolean var9;
            boolean var10;
            if (!var4.hasNext()) {
               this.handledEvents.clear();
               if (this.currentInterval != null) {
                  float var15 = this.currentInterval.getBegin();
                  if (this.lastSampleTime < var15 && var1 >= var15) {
                     if (!this.isActive) {
                        this.toActive(var15);
                     }

                     this.isActive = true;
                     this.isFrozen = false;
                     this.lastRepeatTime = var15;
                     this.fireTimeEvent("beginEvent", this.currentInterval.getBegin(), 0);
                  }
               }

               boolean var16 = this.currentInterval != null && var1 >= this.currentInterval.getEnd();
               float var17;
               float var18;
               if (this.currentInterval != null) {
                  var17 = this.currentInterval.getBegin();
                  if (var1 >= var17) {
                     var18 = this.getSimpleDur();

                     while(var3 - this.lastRepeatTime >= var18 && this.lastRepeatTime + var18 < var17 + this.repeatDuration) {
                        this.lastRepeatTime += var18;
                        ++this.currentRepeatIteration;
                        this.fireTimeEvent(this.root.getRepeatEventName(), this.lastRepeatTime, this.currentRepeatIteration);
                     }
                  }
               }

               var17 = Float.POSITIVE_INFINITY;
               if (var2) {
                  this.shouldUpdateCurrentInterval = true;
               }

               float var20;
               while(this.shouldUpdateCurrentInterval || var16) {
                  if (var16) {
                     this.previousInterval = this.currentInterval;
                     this.isActive = false;
                     this.isFrozen = this.fillMode == 1;
                     this.toInactive(false, this.isFrozen);
                     this.fireTimeEvent("endEvent", this.currentInterval.getEnd(), 0);
                  }

                  boolean var19 = this.currentInterval == null && this.previousInterval == null;
                  if (this.currentInterval != null && var2) {
                     this.isActive = false;
                     this.isFrozen = false;
                     this.toInactive(false, false);
                     this.currentInterval = null;
                  }

                  float var26;
                  if (this.currentInterval != null && !var16) {
                     var20 = this.currentInterval.getBegin();
                     if (var20 > var3) {
                        var10 = true;
                        float var21;
                        if (this.previousInterval == null) {
                           var21 = Float.NEGATIVE_INFINITY;
                        } else {
                           var21 = this.previousInterval.getEnd();
                           var10 = var21 != this.previousInterval.getBegin();
                        }

                        Interval var27 = this.computeInterval(false, false, var21, var10);
                        float var28 = this.notifyRemoveInterval(this.currentInterval);
                        if (var28 < var17) {
                           var17 = var28;
                        }

                        if (var27 == null) {
                           this.currentInterval = null;
                        } else {
                           var28 = this.selectNewInterval(var3, var27);
                           if (var28 < var17) {
                              var17 = var28;
                           }
                        }
                     } else {
                        Interval var24 = this.computeInterval(false, true, var20, true);
                        float var25 = var24.getEnd();
                        if (this.currentInterval.getEnd() != var25) {
                           var26 = this.currentInterval.setEnd(var25, var24.getEndInstanceTime());
                           if (var26 < var17) {
                              var17 = var26;
                           }
                        }
                     }
                  } else if (!var19 && !var2 && this.restartMode == 2) {
                     this.currentInterval = null;
                  } else {
                     var9 = true;
                     if (!var19 && !var2) {
                        var20 = this.previousInterval.getEnd();
                        var9 = var20 != this.previousInterval.getBegin();
                     } else {
                        var20 = Float.NEGATIVE_INFINITY;
                     }

                     Interval var22 = this.computeInterval(var19, false, var20, var9);
                     if (var22 == null) {
                        this.currentInterval = null;
                     } else {
                        var26 = this.selectNewInterval(var3, var22);
                        if (var26 < var17) {
                           var17 = var26;
                        }
                     }
                  }

                  this.shouldUpdateCurrentInterval = false;
                  var2 = false;
                  var16 = this.currentInterval != null && var3 >= this.currentInterval.getEnd();
               }

               var18 = this.getSimpleDur();
               if (this.isActive && !this.isFrozen) {
                  if (var3 - this.currentInterval.getBegin() >= this.repeatDuration) {
                     this.isFrozen = this.fillMode == 1;
                     this.toInactive(true, this.isFrozen);
                  } else {
                     this.sampledAt(var3 - this.lastRepeatTime, var18, this.currentRepeatIteration);
                  }
               }

               if (this.isFrozen) {
                  if (this.isActive) {
                     var20 = this.currentInterval.getBegin() + this.repeatDuration - this.lastRepeatTime;
                     var9 = this.lastRepeatTime + var18 == this.currentInterval.getBegin() + this.repeatDuration;
                  } else {
                     var20 = this.previousInterval.getEnd() - this.lastRepeatTime;
                     var9 = this.lastRepeatTime + var18 == this.previousInterval.getEnd();
                  }

                  if (var9) {
                     this.sampledLastValue(this.currentRepeatIteration);
                  } else {
                     this.sampledAt(var20 % var18, var18, this.currentRepeatIteration);
                  }
               } else if (!this.isActive) {
               }

               this.isSampling = false;
               this.lastSampleTime = var3;
               if (this.currentInterval == null) {
                  return var17;
               }

               var20 = this.currentInterval.getBegin() - var3;
               if (var20 <= 0.0F) {
                  var20 = !this.isConstantAnimation() && !this.isFrozen ? 0.0F : this.currentInterval.getEnd() - var3;
               }

               if (var17 < var20) {
                  return var17;
               }

               return var20;
            }

            Map.Entry var5 = (Map.Entry)var4.next();
            var6 = (Event)var5.getKey();
            var7 = (Set)var5.getValue();
            var8 = var7.iterator();
            var9 = false;
            var10 = false;

            while(var8.hasNext() && (!var9 || !var10)) {
               EventLikeTimingSpecifier var11 = (EventLikeTimingSpecifier)var8.next();
               if (var11.isBegin()) {
                  var9 = true;
               } else {
                  var10 = true;
               }
            }

            if (var9 && var10) {
               var23 = !this.isActive || this.restartMode == 0;
               var12 = !var23;
               break;
            }

            if (var9 && (!this.isActive || this.restartMode == 0)) {
               var23 = true;
               var12 = false;
               break;
            }

            if (var10 && this.isActive) {
               var23 = false;
               var12 = true;
               break;
            }
         }

         var8 = var7.iterator();

         while(true) {
            EventLikeTimingSpecifier var13;
            boolean var14;
            do {
               if (!var8.hasNext()) {
                  continue label293;
               }

               var13 = (EventLikeTimingSpecifier)var8.next();
               var14 = var13.isBegin();
            } while((!var14 || !var23) && (var14 || !var12));

            var13.resolve(var6);
            this.shouldUpdateCurrentInterval = true;
         }
      }
   }

   protected boolean endHasEventConditions() {
      for(int var1 = 0; var1 < this.endTimes.length; ++var1) {
         if (this.endTimes[var1].isEventCondition()) {
            return true;
         }
      }

      return false;
   }

   protected float selectNewInterval(float var1, Interval var2) {
      this.currentInterval = var2;
      float var3 = this.notifyNewInterval(this.currentInterval);
      float var4 = this.currentInterval.getBegin();
      if (var1 >= var4) {
         this.lastRepeatTime = var4;
         if (var4 < 0.0F) {
            var4 = 0.0F;
         }

         this.toActive(var4);
         this.isActive = true;
         this.isFrozen = false;
         this.fireTimeEvent("beginEvent", var4, 0);
         float var5 = this.getSimpleDur();
         float var6 = this.currentInterval.getEnd();

         while(var1 - this.lastRepeatTime >= var5 && this.lastRepeatTime + var5 < var6) {
            this.lastRepeatTime += var5;
            ++this.currentRepeatIteration;
            this.fireTimeEvent(this.root.getRepeatEventName(), this.lastRepeatTime, this.currentRepeatIteration);
         }
      }

      return var3;
   }

   protected Interval computeInterval(boolean var1, boolean var2, float var3, boolean var4) {
      Iterator var5 = this.beginInstanceTimes.iterator();
      Iterator var6 = this.endInstanceTimes.iterator();
      float var7 = this.parent.getSimpleDur();
      InstanceTime var8 = var6.hasNext() ? (InstanceTime)var6.next() : null;
      boolean var9 = true;
      InstanceTime var10 = null;
      InstanceTime var11 = null;

      while(true) {
         float var12;
         if (var2) {
            var12 = var3;

            while(var5.hasNext()) {
               var11 = (InstanceTime)var5.next();
               if (var11.getTime() > var12) {
                  break;
               }
            }
         } else {
            while(true) {
               do {
                  if (!var5.hasNext()) {
                     return null;
                  }

                  var10 = (InstanceTime)var5.next();
                  var12 = var10.getTime();
               } while((!var4 || !(var12 >= var3)) && (var4 || !(var12 > var3)));

               if (!var5.hasNext()) {
                  break;
               }

               var11 = (InstanceTime)var5.next();
               if (var10.getTime() != var11.getTime()) {
                  break;
               }

               var11 = null;
            }
         }

         if (var12 >= var7) {
            return null;
         }

         float var13;
         float var14;
         if (this.endTimes.length == 0) {
            var13 = var12 + this.getActiveDur(var12, Float.POSITIVE_INFINITY);
         } else {
            if (this.endInstanceTimes.isEmpty()) {
               var13 = Float.NaN;
            } else {
               var13 = var8.getTime();
               if (var1 && !var9 && var13 == var12 || !var1 && this.currentInterval != null && var13 == this.currentInterval.getEnd() && (var4 && var3 >= var13 || !var4 && var3 > var13)) {
                  do {
                     if (!var6.hasNext()) {
                        if (!this.endHasEventConditions()) {
                           return null;
                        }

                        var13 = Float.NaN;
                        break;
                     }

                     var8 = (InstanceTime)var6.next();
                     var13 = var8.getTime();
                  } while(!(var13 > var12));
               }

               for(var9 = false; !(var13 >= var12); var13 = var8.getTime()) {
                  if (!var6.hasNext()) {
                     if (!this.endHasEventConditions()) {
                        return null;
                     }

                     var13 = Float.NaN;
                     break;
                  }

                  var8 = (InstanceTime)var6.next();
               }
            }

            var14 = this.getActiveDur(var12, var13);
            var13 = var12 + var14;
         }

         if (!var1 || var13 > 0.0F || var12 == 0.0F && var13 == 0.0F || isUnresolved(var13)) {
            if (this.restartMode == 0 && var11 != null) {
               var14 = var11.getTime();
               if (var14 < var13 || isUnresolved(var13)) {
                  var13 = var14;
                  var8 = var11;
               }
            }

            Interval var15 = new Interval(var12, var13, var10, var8);
            return var15;
         }

         if (var2) {
            return null;
         }

         var3 = var13;
      }
   }

   protected void reset(boolean var1) {
      Iterator var2 = this.beginInstanceTimes.iterator();

      while(true) {
         InstanceTime var3;
         do {
            do {
               if (!var2.hasNext()) {
                  var2 = this.endInstanceTimes.iterator();

                  while(var2.hasNext()) {
                     var3 = (InstanceTime)var2.next();
                     if (var3.getClearOnReset()) {
                        var2.remove();
                     }
                  }

                  if (this.isFrozen) {
                     this.removeFill();
                  }

                  this.currentRepeatIteration = 0;
                  this.lastRepeatTime = Float.NaN;
                  this.isActive = false;
                  this.isFrozen = false;
                  this.lastSampleTime = Float.NaN;
                  return;
               }

               var3 = (InstanceTime)var2.next();
            } while(!var3.getClearOnReset());
         } while(!var1 && this.currentInterval != null && this.currentInterval.getBeginInstanceTime() == var3);

         var2.remove();
      }
   }

   public void parseAttributes(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      if (!this.hasParsed) {
         this.parseBegin(var1);
         this.parseDur(var2);
         this.parseEnd(var3);
         this.parseMin(var4);
         this.parseMax(var5);
         if (this.min > this.max) {
            this.min = 0.0F;
            this.max = Float.POSITIVE_INFINITY;
         }

         this.parseRepeatCount(var6);
         this.parseRepeatDur(var7);
         this.parseFill(var8);
         this.parseRestart(var9);
         this.hasParsed = true;
      }

   }

   protected void parseBegin(String var1) {
      try {
         if (var1.length() == 0) {
            var1 = "0";
         }

         this.beginTimes = TimingSpecifierListProducer.parseTimingSpecifierList(this, true, var1, this.root.useSVG11AccessKeys, this.root.useSVG12AccessKeys);
      } catch (ParseException var3) {
         throw this.createException("attribute.malformed", new Object[]{null, "begin"});
      }
   }

   protected void parseDur(String var1) {
      if (var1.equals("media")) {
         this.durMedia = true;
         this.simpleDur = Float.NaN;
      } else {
         this.durMedia = false;
         if (var1.length() != 0 && !var1.equals("indefinite")) {
            try {
               this.simpleDur = this.parseClockValue(var1, false);
            } catch (ParseException var3) {
               throw this.createException("attribute.malformed", new Object[]{null, "dur"});
            }

            if (this.simpleDur < 0.0F) {
               this.simpleDur = Float.POSITIVE_INFINITY;
            }
         } else {
            this.simpleDur = Float.POSITIVE_INFINITY;
         }
      }

   }

   protected float parseClockValue(String var1, boolean var2) throws ParseException {
      ClockParser var3 = new ClockParser(var2);

      class Handler implements ClockHandler {
         protected float v = 0.0F;

         public void clockValue(float var1) {
            this.v = var1;
         }
      }

      Handler var4 = new Handler();
      var3.setClockHandler(var4);
      var3.parse(var1);
      return var4.v;
   }

   protected void parseEnd(String var1) {
      try {
         this.endTimes = TimingSpecifierListProducer.parseTimingSpecifierList(this, false, var1, this.root.useSVG11AccessKeys, this.root.useSVG12AccessKeys);
      } catch (ParseException var3) {
         throw this.createException("attribute.malformed", new Object[]{null, "end"});
      }
   }

   protected void parseMin(String var1) {
      if (var1.equals("media")) {
         this.min = 0.0F;
         this.minMedia = true;
      } else {
         this.minMedia = false;
         if (var1.length() == 0) {
            this.min = 0.0F;
         } else {
            try {
               this.min = this.parseClockValue(var1, false);
            } catch (ParseException var3) {
               this.min = 0.0F;
            }

            if (this.min < 0.0F) {
               this.min = 0.0F;
            }
         }
      }

   }

   protected void parseMax(String var1) {
      if (var1.equals("media")) {
         this.max = Float.POSITIVE_INFINITY;
         this.maxMedia = true;
      } else {
         this.maxMedia = false;
         if (var1.length() != 0 && !var1.equals("indefinite")) {
            try {
               this.max = this.parseClockValue(var1, false);
            } catch (ParseException var3) {
               this.max = Float.POSITIVE_INFINITY;
            }

            if (this.max < 0.0F) {
               this.max = 0.0F;
            }
         } else {
            this.max = Float.POSITIVE_INFINITY;
         }
      }

   }

   protected void parseRepeatCount(String var1) {
      if (var1.length() == 0) {
         this.repeatCount = Float.NaN;
      } else if (var1.equals("indefinite")) {
         this.repeatCount = Float.POSITIVE_INFINITY;
      } else {
         try {
            this.repeatCount = Float.parseFloat(var1);
            if (this.repeatCount > 0.0F) {
               return;
            }
         } catch (NumberFormatException var3) {
            throw this.createException("attribute.malformed", new Object[]{null, "repeatCount"});
         }
      }

   }

   protected void parseRepeatDur(String var1) {
      try {
         if (var1.length() == 0) {
            this.repeatDur = Float.NaN;
         } else if (var1.equals("indefinite")) {
            this.repeatDur = Float.POSITIVE_INFINITY;
         } else {
            this.repeatDur = this.parseClockValue(var1, false);
         }

      } catch (ParseException var3) {
         throw this.createException("attribute.malformed", new Object[]{null, "repeatDur"});
      }
   }

   protected void parseFill(String var1) {
      if (var1.length() != 0 && !var1.equals("remove")) {
         if (!var1.equals("freeze")) {
            throw this.createException("attribute.malformed", new Object[]{null, "fill"});
         }

         this.fillMode = 1;
      } else {
         this.fillMode = 0;
      }

   }

   protected void parseRestart(String var1) {
      if (var1.length() != 0 && !var1.equals("always")) {
         if (var1.equals("whenNotActive")) {
            this.restartMode = 1;
         } else {
            if (!var1.equals("never")) {
               throw this.createException("attribute.malformed", new Object[]{null, "restart"});
            }

            this.restartMode = 2;
         }
      } else {
         this.restartMode = 0;
      }

   }

   public void initialize() {
      int var1;
      for(var1 = 0; var1 < this.beginTimes.length; ++var1) {
         this.beginTimes[var1].initialize();
      }

      for(var1 = 0; var1 < this.endTimes.length; ++var1) {
         this.endTimes[var1].initialize();
      }

   }

   public void deinitialize() {
      int var1;
      for(var1 = 0; var1 < this.beginTimes.length; ++var1) {
         this.beginTimes[var1].deinitialize();
      }

      for(var1 = 0; var1 < this.endTimes.length; ++var1) {
         this.endTimes[var1].deinitialize();
      }

   }

   public void beginElement() {
      this.beginElement(0.0F);
   }

   public void beginElement(float var1) {
      float var2 = this.root.convertWallclockTime(Calendar.getInstance());
      InstanceTime var3 = new InstanceTime((TimingSpecifier)null, var2 + var1, true);
      this.addInstanceTime(var3, true);
   }

   public void endElement() {
      this.endElement(0.0F);
   }

   public void endElement(float var1) {
      float var2 = this.root.convertWallclockTime(Calendar.getInstance());
      InstanceTime var3 = new InstanceTime((TimingSpecifier)null, var2 + var1, true);
      this.addInstanceTime(var3, false);
   }

   public float getLastSampleTime() {
      return this.lastSampleTime;
   }

   public float getCurrentBeginTime() {
      float var1;
      return this.currentInterval != null && !((var1 = this.currentInterval.getBegin()) < this.lastSampleTime) ? var1 : Float.NaN;
   }

   public boolean canBegin() {
      return this.currentInterval == null || this.isActive && this.restartMode != 2;
   }

   public boolean canEnd() {
      return this.isActive;
   }

   public float getHyperlinkBeginTime() {
      if (this.isActive) {
         return this.currentInterval.getBegin();
      } else {
         return !this.beginInstanceTimes.isEmpty() ? ((InstanceTime)this.beginInstanceTimes.get(0)).getTime() : Float.NaN;
      }
   }

   protected void fireTimeEvent(String var1, float var2, int var3) {
      Calendar var4 = (Calendar)this.root.getDocumentBeginTime().clone();
      var4.add(14, (int)Math.round((double)var2 * 1000.0));
      this.fireTimeEvent(var1, var4, var3);
   }

   void eventOccurred(TimingSpecifier var1, Event var2) {
      HashSet var3 = (HashSet)this.handledEvents.get(var2);
      if (var3 == null) {
         var3 = new HashSet();
         this.handledEvents.put(var2, var3);
      }

      var3.add(var1);
      this.root.currentIntervalWillUpdate();
   }

   protected abstract void fireTimeEvent(String var1, Calendar var2, int var3);

   protected abstract void toActive(float var1);

   protected abstract void toInactive(boolean var1, boolean var2);

   protected abstract void removeFill();

   protected abstract void sampledAt(float var1, float var2, int var3);

   protected abstract void sampledLastValue(int var1);

   protected abstract TimedElement getTimedElementById(String var1);

   protected abstract EventTarget getEventTargetById(String var1);

   protected abstract EventTarget getRootEventTarget();

   public abstract Element getElement();

   protected abstract EventTarget getAnimationEventTarget();

   public abstract boolean isBefore(TimedElement var1);

   protected abstract boolean isConstantAnimation();

   public AnimationException createException(String var1, Object[] var2) {
      Element var3 = this.getElement();
      if (var3 != null) {
         var2[0] = var3.getNodeName();
      }

      return new AnimationException(this, var1, var2);
   }

   public static void setLocale(Locale var0) {
      localizableSupport.setLocale(var0);
   }

   public static Locale getLocale() {
      return localizableSupport.getLocale();
   }

   public static String formatMessage(String var0, Object[] var1) throws MissingResourceException {
      return localizableSupport.formatMessage(var0, var1);
   }

   public static String toString(float var0) {
      if (Float.isNaN(var0)) {
         return "UNRESOLVED";
      } else {
         return var0 == Float.POSITIVE_INFINITY ? "INDEFINITE" : Float.toString(var0);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      localizableSupport = new LocalizableSupport("org.apache.batik.anim.resources.Messages", (class$org$apache$batik$anim$timing$TimedElement == null ? (class$org$apache$batik$anim$timing$TimedElement = class$("org.apache.batik.anim.timing.TimedElement")) : class$org$apache$batik$anim$timing$TimedElement).getClassLoader());
   }
}
