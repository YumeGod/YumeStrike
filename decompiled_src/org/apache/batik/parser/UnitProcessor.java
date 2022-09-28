package org.apache.batik.parser;

import org.w3c.dom.Element;

public abstract class UnitProcessor {
   public static final short HORIZONTAL_LENGTH = 2;
   public static final short VERTICAL_LENGTH = 1;
   public static final short OTHER_LENGTH = 0;
   static final double SQRT2 = Math.sqrt(2.0);

   protected UnitProcessor() {
   }

   public static float svgToObjectBoundingBox(String var0, String var1, short var2, Context var3) throws ParseException {
      LengthParser var4 = new LengthParser();
      UnitResolver var5 = new UnitResolver();
      var4.setLengthHandler(var5);
      var4.parse(var0);
      return svgToObjectBoundingBox(var5.value, var5.unit, var2, var3);
   }

   public static float svgToObjectBoundingBox(float var0, short var1, short var2, Context var3) {
      switch (var1) {
         case 1:
            return var0;
         case 2:
            return var0 / 100.0F;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
            return svgToUserSpace(var0, var1, var2, var3);
         default:
            throw new IllegalArgumentException("Length has unknown type");
      }
   }

   public static float svgToUserSpace(String var0, String var1, short var2, Context var3) throws ParseException {
      LengthParser var4 = new LengthParser();
      UnitResolver var5 = new UnitResolver();
      var4.setLengthHandler(var5);
      var4.parse(var0);
      return svgToUserSpace(var5.value, var5.unit, var2, var3);
   }

   public static float svgToUserSpace(float var0, short var1, short var2, Context var3) {
      switch (var1) {
         case 1:
         case 5:
            return var0;
         case 2:
            return percentagesToPixels(var0, var2, var3);
         case 3:
            return emsToPixels(var0, var2, var3);
         case 4:
            return exsToPixels(var0, var2, var3);
         case 6:
            return var0 * 10.0F / var3.getPixelUnitToMillimeter();
         case 7:
            return var0 / var3.getPixelUnitToMillimeter();
         case 8:
            return var0 * 25.4F / var3.getPixelUnitToMillimeter();
         case 9:
            return var0 * 25.4F / (72.0F * var3.getPixelUnitToMillimeter());
         case 10:
            return var0 * 25.4F / (6.0F * var3.getPixelUnitToMillimeter());
         default:
            throw new IllegalArgumentException("Length has unknown type");
      }
   }

   public static float userSpaceToSVG(float var0, short var1, short var2, Context var3) {
      switch (var1) {
         case 1:
         case 5:
            return var0;
         case 2:
            return pixelsToPercentages(var0, var2, var3);
         case 3:
            return pixelsToEms(var0, var2, var3);
         case 4:
            return pixelsToExs(var0, var2, var3);
         case 6:
            return var0 * var3.getPixelUnitToMillimeter() / 10.0F;
         case 7:
            return var0 * var3.getPixelUnitToMillimeter();
         case 8:
            return var0 * var3.getPixelUnitToMillimeter() / 25.4F;
         case 9:
            return var0 * 72.0F * var3.getPixelUnitToMillimeter() / 25.4F;
         case 10:
            return var0 * 6.0F * var3.getPixelUnitToMillimeter() / 25.4F;
         default:
            throw new IllegalArgumentException("Length has unknown type");
      }
   }

   protected static float percentagesToPixels(float var0, short var1, Context var2) {
      float var9;
      if (var1 == 2) {
         var9 = var2.getViewportWidth();
         return var9 * var0 / 100.0F;
      } else if (var1 == 1) {
         var9 = var2.getViewportHeight();
         return var9 * var0 / 100.0F;
      } else {
         double var3 = (double)var2.getViewportWidth();
         double var5 = (double)var2.getViewportHeight();
         double var7 = Math.sqrt(var3 * var3 + var5 * var5) / SQRT2;
         return (float)(var7 * (double)var0 / 100.0);
      }
   }

   protected static float pixelsToPercentages(float var0, short var1, Context var2) {
      float var9;
      if (var1 == 2) {
         var9 = var2.getViewportWidth();
         return var0 * 100.0F / var9;
      } else if (var1 == 1) {
         var9 = var2.getViewportHeight();
         return var0 * 100.0F / var9;
      } else {
         double var3 = (double)var2.getViewportWidth();
         double var5 = (double)var2.getViewportHeight();
         double var7 = Math.sqrt(var3 * var3 + var5 * var5) / SQRT2;
         return (float)((double)var0 * 100.0 / var7);
      }
   }

   protected static float pixelsToEms(float var0, short var1, Context var2) {
      return var0 / var2.getFontSize();
   }

   protected static float emsToPixels(float var0, short var1, Context var2) {
      return var0 * var2.getFontSize();
   }

   protected static float pixelsToExs(float var0, short var1, Context var2) {
      float var3 = var2.getXHeight();
      return var0 / var3 / var2.getFontSize();
   }

   protected static float exsToPixels(float var0, short var1, Context var2) {
      float var3 = var2.getXHeight();
      return var0 * var3 * var2.getFontSize();
   }

   public interface Context {
      Element getElement();

      float getPixelUnitToMillimeter();

      float getPixelToMM();

      float getFontSize();

      float getXHeight();

      float getViewportWidth();

      float getViewportHeight();
   }

   public static class UnitResolver implements LengthHandler {
      public float value;
      public short unit = 1;

      public void startLength() throws ParseException {
      }

      public void lengthValue(float var1) throws ParseException {
         this.value = var1;
      }

      public void em() throws ParseException {
         this.unit = 3;
      }

      public void ex() throws ParseException {
         this.unit = 4;
      }

      public void in() throws ParseException {
         this.unit = 8;
      }

      public void cm() throws ParseException {
         this.unit = 6;
      }

      public void mm() throws ParseException {
         this.unit = 7;
      }

      public void pc() throws ParseException {
         this.unit = 10;
      }

      public void pt() throws ParseException {
         this.unit = 9;
      }

      public void px() throws ParseException {
         this.unit = 5;
      }

      public void percentage() throws ParseException {
         this.unit = 2;
      }

      public void endLength() throws ParseException {
      }
   }
}
