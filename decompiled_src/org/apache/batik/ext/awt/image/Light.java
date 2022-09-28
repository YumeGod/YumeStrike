package org.apache.batik.ext.awt.image;

import java.awt.Color;

public interface Light {
   boolean isConstant();

   void getLight(double var1, double var3, double var5, double[] var7);

   double[][][] getLightMap(double var1, double var3, double var5, double var7, int var9, int var10, double[][][] var11);

   double[][] getLightRow(double var1, double var3, double var5, int var7, double[][] var8, double[][] var9);

   double[] getColor(boolean var1);

   void setColor(Color var1);
}
