package org.apache.fop.render.intermediate;

import java.awt.geom.AffineTransform;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;
import org.apache.batik.parser.TransformListParser;

public class AffineTransformArrayParser implements TransformListHandler {
   private static final AffineTransform[] EMPTY_ARRAY = new AffineTransform[0];
   private List transforms;

   public static AffineTransform[] createAffineTransform(Reader r) throws ParseException {
      TransformListParser p = new TransformListParser();
      AffineTransformArrayParser th = new AffineTransformArrayParser();
      p.setTransformListHandler(th);
      p.parse(r);
      return th.getAffineTransforms();
   }

   public static AffineTransform[] createAffineTransform(String s) throws ParseException {
      if (s == null) {
         return EMPTY_ARRAY;
      } else {
         TransformListParser p = new TransformListParser();
         AffineTransformArrayParser th = new AffineTransformArrayParser();
         p.setTransformListHandler(th);
         p.parse(s);
         return th.getAffineTransforms();
      }
   }

   public AffineTransform[] getAffineTransforms() {
      if (this.transforms == null) {
         return null;
      } else {
         int count = this.transforms.size();
         return (AffineTransform[])this.transforms.toArray(new AffineTransform[count]);
      }
   }

   public void startTransformList() throws ParseException {
      this.transforms = new ArrayList();
   }

   public void matrix(float a, float b, float c, float d, float e, float f) throws ParseException {
      this.transforms.add(new AffineTransform(a, b, c, d, e, f));
   }

   public void rotate(float theta) throws ParseException {
      this.transforms.add(AffineTransform.getRotateInstance(Math.toRadians((double)theta)));
   }

   public void rotate(float theta, float cx, float cy) throws ParseException {
      AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians((double)theta), (double)cx, (double)cy);
      this.transforms.add(at);
   }

   public void translate(float tx) throws ParseException {
      AffineTransform at = AffineTransform.getTranslateInstance((double)tx, 0.0);
      this.transforms.add(at);
   }

   public void translate(float tx, float ty) throws ParseException {
      AffineTransform at = AffineTransform.getTranslateInstance((double)tx, (double)ty);
      this.transforms.add(at);
   }

   public void scale(float sx) throws ParseException {
      this.transforms.add(AffineTransform.getScaleInstance((double)sx, (double)sx));
   }

   public void scale(float sx, float sy) throws ParseException {
      this.transforms.add(AffineTransform.getScaleInstance((double)sx, (double)sy));
   }

   public void skewX(float skx) throws ParseException {
      this.transforms.add(AffineTransform.getShearInstance(Math.tan(Math.toRadians((double)skx)), 0.0));
   }

   public void skewY(float sky) throws ParseException {
      this.transforms.add(AffineTransform.getShearInstance(0.0, Math.tan(Math.toRadians((double)sky))));
   }

   public void endTransformList() throws ParseException {
   }
}
