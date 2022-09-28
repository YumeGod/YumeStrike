package org.apache.batik.parser;

public interface PathHandler {
   void startPath() throws ParseException;

   void endPath() throws ParseException;

   void movetoRel(float var1, float var2) throws ParseException;

   void movetoAbs(float var1, float var2) throws ParseException;

   void closePath() throws ParseException;

   void linetoRel(float var1, float var2) throws ParseException;

   void linetoAbs(float var1, float var2) throws ParseException;

   void linetoHorizontalRel(float var1) throws ParseException;

   void linetoHorizontalAbs(float var1) throws ParseException;

   void linetoVerticalRel(float var1) throws ParseException;

   void linetoVerticalAbs(float var1) throws ParseException;

   void curvetoCubicRel(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException;

   void curvetoCubicAbs(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException;

   void curvetoCubicSmoothRel(float var1, float var2, float var3, float var4) throws ParseException;

   void curvetoCubicSmoothAbs(float var1, float var2, float var3, float var4) throws ParseException;

   void curvetoQuadraticRel(float var1, float var2, float var3, float var4) throws ParseException;

   void curvetoQuadraticAbs(float var1, float var2, float var3, float var4) throws ParseException;

   void curvetoQuadraticSmoothRel(float var1, float var2) throws ParseException;

   void curvetoQuadraticSmoothAbs(float var1, float var2) throws ParseException;

   void arcRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException;

   void arcAbs(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) throws ParseException;
}
