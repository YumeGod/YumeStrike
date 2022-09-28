package org.apache.batik.parser;

public interface TransformListHandler {
   void startTransformList() throws ParseException;

   void matrix(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException;

   void rotate(float var1) throws ParseException;

   void rotate(float var1, float var2, float var3) throws ParseException;

   void translate(float var1) throws ParseException;

   void translate(float var1, float var2) throws ParseException;

   void scale(float var1) throws ParseException;

   void scale(float var1, float var2) throws ParseException;

   void skewX(float var1) throws ParseException;

   void skewY(float var1) throws ParseException;

   void endTransformList() throws ParseException;
}
