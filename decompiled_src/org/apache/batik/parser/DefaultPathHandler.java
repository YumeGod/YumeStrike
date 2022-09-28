package org.apache.batik.parser;

public class DefaultPathHandler implements PathHandler {
   public static final PathHandler INSTANCE = new DefaultPathHandler();

   protected DefaultPathHandler() {
   }

   public void startPath() throws ParseException {
   }

   public void endPath() throws ParseException {
   }

   public void movetoRel(float var1, float var2) throws ParseException {
   }

   public void movetoAbs(float var1, float var2) throws ParseException {
   }

   public void closePath() throws ParseException {
   }

   public void linetoRel(float var1, float var2) throws ParseException {
   }

   public void linetoAbs(float var1, float var2) throws ParseException {
   }

   public void linetoHorizontalRel(float var1) throws ParseException {
   }

   public void linetoHorizontalAbs(float var1) throws ParseException {
   }

   public void linetoVerticalRel(float var1) throws ParseException {
   }

   public void linetoVerticalAbs(float var1) throws ParseException {
   }

   public void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
   }

   public void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
   }

   public void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException {
   }

   public void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException {
   }

   public void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException {
   }

   public void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException {
   }

   public void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException {
   }

   public void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException {
   }

   public void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
   }

   public void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException {
   }
}
