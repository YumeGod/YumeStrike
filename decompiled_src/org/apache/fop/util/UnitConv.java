package org.apache.fop.util;

import java.awt.geom.AffineTransform;

/** @deprecated */
public final class UnitConv {
   /** @deprecated */
   public static final float IN2MM = 25.4F;
   /** @deprecated */
   public static final float IN2CM = 2.54F;
   /** @deprecated */
   public static final int IN2PT = 72;

   /** @deprecated */
   public static double mm2pt(double mm) {
      return org.apache.xmlgraphics.util.UnitConv.mm2pt(mm);
   }

   /** @deprecated */
   public static double mm2mpt(double mm) {
      return org.apache.xmlgraphics.util.UnitConv.mm2mpt(mm);
   }

   /** @deprecated */
   public static double pt2mm(double pt) {
      return org.apache.xmlgraphics.util.UnitConv.pt2mm(pt);
   }

   /** @deprecated */
   public static double mm2in(double mm) {
      return org.apache.xmlgraphics.util.UnitConv.mm2in(mm);
   }

   /** @deprecated */
   public static double in2mm(double in) {
      return org.apache.xmlgraphics.util.UnitConv.in2mm(in);
   }

   /** @deprecated */
   public static double in2mpt(double in) {
      return org.apache.xmlgraphics.util.UnitConv.in2mpt(in);
   }

   /** @deprecated */
   public static double in2pt(double in) {
      return org.apache.xmlgraphics.util.UnitConv.in2pt(in);
   }

   /** @deprecated */
   public static double mpt2in(double mpt) {
      return org.apache.xmlgraphics.util.UnitConv.mpt2in(mpt);
   }

   /** @deprecated */
   public static double mm2px(double mm, int resolution) {
      return org.apache.xmlgraphics.util.UnitConv.mm2px(mm, resolution);
   }

   /** @deprecated */
   public static double mpt2px(double mpt, int resolution) {
      return org.apache.xmlgraphics.util.UnitConv.mpt2px(mpt, resolution);
   }

   /** @deprecated */
   public static AffineTransform mptToPt(AffineTransform at) {
      return org.apache.xmlgraphics.util.UnitConv.mptToPt(at);
   }

   /** @deprecated */
   public static AffineTransform ptToMpt(AffineTransform at) {
      return org.apache.xmlgraphics.util.UnitConv.ptToMpt(at);
   }
}
