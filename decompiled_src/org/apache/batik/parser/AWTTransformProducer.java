package org.apache.batik.parser;

import java.awt.geom.AffineTransform;
import java.io.Reader;

public class AWTTransformProducer implements TransformListHandler {
   protected AffineTransform affineTransform;

   public static AffineTransform createAffineTransform(Reader var0) throws ParseException {
      TransformListParser var1 = new TransformListParser();
      AWTTransformProducer var2 = new AWTTransformProducer();
      var1.setTransformListHandler(var2);
      var1.parse(var0);
      return var2.getAffineTransform();
   }

   public static AffineTransform createAffineTransform(String var0) throws ParseException {
      TransformListParser var1 = new TransformListParser();
      AWTTransformProducer var2 = new AWTTransformProducer();
      var1.setTransformListHandler(var2);
      var1.parse(var0);
      return var2.getAffineTransform();
   }

   public AffineTransform getAffineTransform() {
      return this.affineTransform;
   }

   public void startTransformList() throws ParseException {
      this.affineTransform = new AffineTransform();
   }

   public void matrix(float var1, float var2, float var3, float var4, float var5, float var6) throws ParseException {
      this.affineTransform.concatenate(new AffineTransform(var1, var2, var3, var4, var5, var6));
   }

   public void rotate(float var1) throws ParseException {
      this.affineTransform.concatenate(AffineTransform.getRotateInstance(Math.toRadians((double)var1)));
   }

   public void rotate(float var1, float var2, float var3) throws ParseException {
      AffineTransform var4 = AffineTransform.getRotateInstance(Math.toRadians((double)var1), (double)var2, (double)var3);
      this.affineTransform.concatenate(var4);
   }

   public void translate(float var1) throws ParseException {
      AffineTransform var2 = AffineTransform.getTranslateInstance((double)var1, 0.0);
      this.affineTransform.concatenate(var2);
   }

   public void translate(float var1, float var2) throws ParseException {
      AffineTransform var3 = AffineTransform.getTranslateInstance((double)var1, (double)var2);
      this.affineTransform.concatenate(var3);
   }

   public void scale(float var1) throws ParseException {
      this.affineTransform.concatenate(AffineTransform.getScaleInstance((double)var1, (double)var1));
   }

   public void scale(float var1, float var2) throws ParseException {
      this.affineTransform.concatenate(AffineTransform.getScaleInstance((double)var1, (double)var2));
   }

   public void skewX(float var1) throws ParseException {
      this.affineTransform.concatenate(AffineTransform.getShearInstance(Math.tan(Math.toRadians((double)var1)), 0.0));
   }

   public void skewY(float var1) throws ParseException {
      this.affineTransform.concatenate(AffineTransform.getShearInstance(0.0, Math.tan(Math.toRadians((double)var1))));
   }

   public void endTransformList() throws ParseException {
   }
}
