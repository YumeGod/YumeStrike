package org.apache.batik.ext.awt.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SegmentList {
   List segments = new LinkedList();

   public SegmentList() {
   }

   public SegmentList(Shape var1) {
      PathIterator var2 = var1.getPathIterator((AffineTransform)null);
      float[] var3 = new float[6];
      Point2D.Double var5 = null;

      for(Point2D.Double var6 = null; !var2.isDone(); var2.next()) {
         int var4 = var2.currentSegment(var3);
         Point2D.Double var7;
         Point2D.Double var8;
         switch (var4) {
            case 0:
               var6 = var5 = new Point2D.Double((double)var3[0], (double)var3[1]);
               break;
            case 1:
               var7 = new Point2D.Double((double)var3[0], (double)var3[1]);
               this.segments.add(new Linear(var5, var7));
               var5 = var7;
               break;
            case 2:
               var7 = new Point2D.Double((double)var3[0], (double)var3[1]);
               var8 = new Point2D.Double((double)var3[2], (double)var3[3]);
               this.segments.add(new Quadradic(var5, var7, var8));
               var5 = var8;
               break;
            case 3:
               var7 = new Point2D.Double((double)var3[0], (double)var3[1]);
               var8 = new Point2D.Double((double)var3[2], (double)var3[3]);
               Point2D.Double var9 = new Point2D.Double((double)var3[4], (double)var3[5]);
               this.segments.add(new Cubic(var5, var7, var8, var9));
               var5 = var9;
               break;
            case 4:
               this.segments.add(new Linear(var5, var6));
               var5 = var6;
         }
      }

   }

   public Rectangle2D getBounds2D() {
      Iterator var1 = this.iterator();
      if (!var1.hasNext()) {
         return null;
      } else {
         Rectangle2D var2 = (Rectangle2D)((Segment)var1.next()).getBounds2D().clone();

         while(var1.hasNext()) {
            Segment var3 = (Segment)var1.next();
            Rectangle2D var4 = var3.getBounds2D();
            Rectangle2D.union(var4, var2, var2);
         }

         return var2;
      }
   }

   public void add(Segment var1) {
      this.segments.add(var1);
   }

   public Iterator iterator() {
      return this.segments.iterator();
   }

   public int size() {
      return this.segments.size();
   }

   public SplitResults split(double var1) {
      Iterator var3 = this.segments.iterator();
      SegmentList var4 = new SegmentList();
      SegmentList var5 = new SegmentList();

      while(true) {
         while(var3.hasNext()) {
            Segment var6 = (Segment)var3.next();
            Segment.SplitResults var7 = var6.split(var1);
            if (var7 == null) {
               Rectangle2D var11 = var6.getBounds2D();
               if (var11.getY() > var1) {
                  var5.add(var6);
               } else if (var11.getY() == var1) {
                  if (var11.getHeight() != 0.0) {
                     var5.add(var6);
                  }
               } else {
                  var4.add(var6);
               }
            } else {
               Segment[] var8 = var7.getAbove();

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  var4.add(var8[var9]);
               }

               Segment[] var12 = var7.getBelow();

               for(int var10 = 0; var10 < var12.length; ++var10) {
                  var5.add(var12[var10]);
               }
            }
         }

         return new SplitResults(var4, var5);
      }
   }

   public static class SplitResults {
      final SegmentList above;
      final SegmentList below;

      public SplitResults(SegmentList var1, SegmentList var2) {
         if (var1 != null && var1.size() > 0) {
            this.above = var1;
         } else {
            this.above = null;
         }

         if (var2 != null && var2.size() > 0) {
            this.below = var2;
         } else {
            this.below = null;
         }

      }

      public SegmentList getAbove() {
         return this.above;
      }

      public SegmentList getBelow() {
         return this.below;
      }
   }
}
