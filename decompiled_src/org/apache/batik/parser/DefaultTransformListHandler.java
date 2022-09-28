package org.apache.batik.parser;

public class DefaultTransformListHandler implements TransformListHandler {
   public static final TransformListHandler INSTANCE = new DefaultTransformListHandler();

   protected DefaultTransformListHandler() {
   }

   public void startTransformList() throws ParseException {
   }

   public void matrix(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
   }

   public void rotate(float var1) throws ParseException {
   }

   public void rotate(float var1, float var2, float var3) throws ParseException {
   }

   public void translate(float var1) throws ParseException {
   }

   public void translate(float var1, float var2) throws ParseException {
   }

   public void scale(float var1) throws ParseException {
   }

   public void scale(float var1, float var2) throws ParseException {
   }

   public void skewX(float var1) throws ParseException {
   }

   public void skewY(float var1) throws ParseException {
   }

   public void endTransformList() throws ParseException {
   }
}
