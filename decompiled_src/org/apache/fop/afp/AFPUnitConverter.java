package org.apache.fop.afp;

import java.awt.geom.AffineTransform;

public class AFPUnitConverter {
   private final AFPPaintingState paintingState;

   public AFPUnitConverter(AFPPaintingState paintingState) {
      this.paintingState = paintingState;
   }

   public int[] mpts2units(float[] srcPts, float[] dstPts) {
      return this.transformPoints(srcPts, dstPts, true);
   }

   public int[] pts2units(float[] srcPts, float[] dstPts) {
      return this.transformPoints(srcPts, dstPts, false);
   }

   public int[] mpts2units(float[] srcPts) {
      return this.transformPoints(srcPts, (float[])null, true);
   }

   public int[] pts2units(float[] srcPts) {
      return this.transformPoints(srcPts, (float[])null, false);
   }

   public float pt2units(float pt) {
      return pt / (72.0F / (float)this.paintingState.getResolution());
   }

   public float mpt2units(float mpt) {
      return mpt / (72000.0F / (float)this.paintingState.getResolution());
   }

   private int[] transformPoints(float[] srcPts, float[] dstPts, boolean milli) {
      if (dstPts == null) {
         dstPts = new float[srcPts.length];
      }

      AffineTransform at = this.paintingState.getData().getTransform();
      at.transform(srcPts, 0, dstPts, 0, srcPts.length / 2);
      int[] coords = new int[srcPts.length];

      for(int i = 0; i < srcPts.length; ++i) {
         if (!milli) {
            dstPts[i] *= 1000.0F;
         }

         coords[i] = Math.round(dstPts[i]);
      }

      return coords;
   }
}
