package org.apache.batik.gvt.flow;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.ext.awt.geom.Segment;
import org.apache.batik.ext.awt.geom.SegmentList;

public class FlowRegions {
   Shape flowShape;
   SegmentList sl;
   SegmentList.SplitResults sr;
   List validRanges;
   int currentRange;
   double currentY;
   double lineHeight;

   public FlowRegions(Shape var1) {
      this(var1, var1.getBounds2D().getY());
   }

   public FlowRegions(Shape var1, double var2) {
      this.flowShape = var1;
      this.sl = new SegmentList(var1);
      this.currentY = var2 - 1.0;
      this.gotoY(var2);
   }

   public double getCurrentY() {
      return this.currentY;
   }

   public double getLineHeight() {
      return this.lineHeight;
   }

   public boolean gotoY(double var1) {
      if (var1 < this.currentY) {
         throw new IllegalArgumentException("New Y can not be lower than old Y\nOld Y: " + this.currentY + " New Y: " + var1);
      } else if (var1 == this.currentY) {
         return false;
      } else {
         this.sr = this.sl.split(var1);
         this.sl = this.sr.getBelow();
         this.sr = null;
         this.currentY = var1;
         if (this.sl == null) {
            return true;
         } else {
            this.newLineHeight(this.lineHeight);
            return false;
         }
      }
   }

   public void newLineHeight(double var1) {
      this.lineHeight = var1;
      this.sr = this.sl.split(this.currentY + var1);
      if (this.sr.getAbove() != null) {
         this.sortRow(this.sr.getAbove());
      }

      this.currentRange = 0;
   }

   public int getNumRangeOnLine() {
      return this.validRanges == null ? 0 : this.validRanges.size();
   }

   public void resetRange() {
      this.currentRange = 0;
   }

   public double[] nextRange() {
      return this.currentRange >= this.validRanges.size() ? null : (double[])this.validRanges.get(this.currentRange++);
   }

   public void endLine() {
      this.sl = this.sr.getBelow();
      this.sr = null;
      this.currentY += this.lineHeight;
   }

   public boolean newLine() {
      return this.newLine(this.lineHeight);
   }

   public boolean newLine(double var1) {
      if (this.sr != null) {
         this.sl = this.sr.getBelow();
      }

      this.sr = null;
      if (this.sl == null) {
         return false;
      } else {
         this.currentY += this.lineHeight;
         this.newLineHeight(var1);
         return true;
      }
   }

   public boolean newLineAt(double var1, double var3) {
      if (this.sr != null) {
         this.sl = this.sr.getBelow();
      }

      this.sr = null;
      if (this.sl == null) {
         return false;
      } else {
         this.currentY = var1;
         this.newLineHeight(var3);
         return true;
      }
   }

   public boolean done() {
      return this.sl == null;
   }

   public void sortRow(SegmentList var1) {
      Transition[] var2 = new Transition[var1.size() * 2];
      Iterator var3 = var1.iterator();

      int var4;
      Segment var5;
      for(var4 = 0; var3.hasNext(); var2[var4++] = new Transition(var5.maxX(), false)) {
         var5 = (Segment)var3.next();
         var2[var4++] = new Transition(var5.minX(), true);
      }

      Arrays.sort(var2, FlowRegions.TransitionComp.COMP);
      this.validRanges = new ArrayList();
      int var13 = 1;
      double var6 = 0.0;

      for(var4 = 1; var4 < var2.length; ++var4) {
         Transition var8 = var2[var4];
         if (var8.up) {
            if (var13 == 0) {
               double var9 = (var6 + var8.loc) / 2.0;
               double var11 = this.currentY + this.lineHeight / 2.0;
               if (this.flowShape.contains(var9, var11)) {
                  this.validRanges.add(new double[]{var6, var8.loc});
               }
            }

            ++var13;
         } else {
            --var13;
            if (var13 == 0) {
               var6 = var8.loc;
            }
         }
      }

   }

   static class TransitionComp implements Comparator {
      public static Comparator COMP = new TransitionComp();

      public int compare(Object var1, Object var2) {
         Transition var3 = (Transition)var1;
         Transition var4 = (Transition)var2;
         if (var3.loc < var4.loc) {
            return -1;
         } else if (var3.loc > var4.loc) {
            return 1;
         } else if (var3.up) {
            return var4.up ? 0 : -1;
         } else {
            return var4.up ? 1 : 0;
         }
      }

      public boolean equals(Object var1) {
         return this == var1;
      }
   }

   static class Transition {
      public double loc;
      public boolean up;

      public Transition(double var1, boolean var3) {
         this.loc = var1;
         this.up = var3;
      }
   }
}
