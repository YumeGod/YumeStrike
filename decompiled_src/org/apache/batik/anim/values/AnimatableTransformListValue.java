package org.apache.batik.anim.values;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.svg.AbstractSVGTransform;
import org.apache.batik.dom.svg.SVGOMTransform;
import org.w3c.dom.svg.SVGMatrix;

public class AnimatableTransformListValue extends AnimatableValue {
   protected static SVGOMTransform IDENTITY_SKEWX = new SVGOMTransform();
   protected static SVGOMTransform IDENTITY_SKEWY = new SVGOMTransform();
   protected static SVGOMTransform IDENTITY_SCALE = new SVGOMTransform();
   protected static SVGOMTransform IDENTITY_ROTATE = new SVGOMTransform();
   protected static SVGOMTransform IDENTITY_TRANSLATE = new SVGOMTransform();
   protected Vector transforms;

   protected AnimatableTransformListValue(AnimationTarget var1) {
      super(var1);
   }

   public AnimatableTransformListValue(AnimationTarget var1, AbstractSVGTransform var2) {
      super(var1);
      this.transforms = new Vector();
      this.transforms.add(var2);
   }

   public AnimatableTransformListValue(AnimationTarget var1, List var2) {
      super(var1);
      this.transforms = new Vector(var2);
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatableTransformListValue var6 = (AnimatableTransformListValue)var2;
      AnimatableTransformListValue var7 = (AnimatableTransformListValue)var4;
      int var8 = var4 == null ? 0 : var7.transforms.size();
      int var9 = this.transforms.size() + var8 * var5;
      AnimatableTransformListValue var10;
      if (var1 == null) {
         var10 = new AnimatableTransformListValue(this.target);
         var10.transforms = new Vector(var9);
         var10.transforms.setSize(var9);
      } else {
         var10 = (AnimatableTransformListValue)var1;
         if (var10.transforms == null) {
            var10.transforms = new Vector(var9);
            var10.transforms.setSize(var9);
         } else if (var10.transforms.size() != var9) {
            var10.transforms.setSize(var9);
         }
      }

      int var11 = 0;

      int var12;
      for(var12 = 0; var12 < var5; ++var12) {
         for(int var13 = 0; var13 < var8; ++var11) {
            var10.transforms.setElementAt(var7.transforms.elementAt(var13), var11);
            ++var13;
         }
      }

      for(var12 = 0; var12 < this.transforms.size() - 1; ++var11) {
         var10.transforms.setElementAt(this.transforms.elementAt(var12), var11);
         ++var12;
      }

      AbstractSVGTransform var21;
      Object var22;
      if (var2 != null) {
         var21 = (AbstractSVGTransform)var6.transforms.lastElement();
         var22 = null;
         short var14;
         if (this.transforms.isEmpty()) {
            var14 = var21.getType();
            switch (var14) {
               case 2:
                  var22 = IDENTITY_TRANSLATE;
                  break;
               case 3:
                  var22 = IDENTITY_SCALE;
                  break;
               case 4:
                  var22 = IDENTITY_ROTATE;
                  break;
               case 5:
                  var22 = IDENTITY_SKEWX;
                  break;
               case 6:
                  var22 = IDENTITY_SKEWY;
            }
         } else {
            var22 = (AbstractSVGTransform)this.transforms.lastElement();
            var14 = ((AbstractSVGTransform)var22).getType();
         }

         if (var14 == var21.getType()) {
            Object var15;
            if (var10.transforms.isEmpty()) {
               var15 = new SVGOMTransform();
               var10.transforms.add(var15);
            } else {
               var15 = (AbstractSVGTransform)var10.transforms.elementAt(var11);
               if (var15 == null) {
                  var15 = new SVGOMTransform();
                  var10.transforms.setElementAt(var15, var11);
               }
            }

            float var18 = 0.0F;
            float var16;
            float var17;
            SVGMatrix var19;
            SVGMatrix var20;
            switch (var14) {
               case 2:
                  var19 = ((AbstractSVGTransform)var22).getMatrix();
                  var20 = var21.getMatrix();
                  var16 = var19.getE();
                  var17 = var19.getF();
                  var16 += var3 * (var20.getE() - var16);
                  var17 += var3 * (var20.getF() - var17);
                  ((AbstractSVGTransform)var15).setTranslate(var16, var17);
                  break;
               case 3:
                  var19 = ((AbstractSVGTransform)var22).getMatrix();
                  var20 = var21.getMatrix();
                  var16 = var19.getA();
                  var17 = var19.getD();
                  var16 += var3 * (var20.getA() - var16);
                  var17 += var3 * (var20.getD() - var17);
                  ((AbstractSVGTransform)var15).setScale(var16, var17);
                  break;
               case 4:
                  var16 = ((AbstractSVGTransform)var22).getX();
                  var17 = ((AbstractSVGTransform)var22).getY();
                  var16 += var3 * (var21.getX() - var16);
                  var17 += var3 * (var21.getY() - var17);
                  var18 = ((AbstractSVGTransform)var22).getAngle();
                  var18 += var3 * (var21.getAngle() - var18);
                  ((AbstractSVGTransform)var15).setRotate(var18, var16, var17);
                  break;
               case 5:
               case 6:
                  var18 = ((AbstractSVGTransform)var22).getAngle();
                  var18 += var3 * (var21.getAngle() - var18);
                  if (var14 == 5) {
                     ((AbstractSVGTransform)var15).setSkewX(var18);
                  } else if (var14 == 6) {
                     ((AbstractSVGTransform)var15).setSkewY(var18);
                  }
            }
         }
      } else {
         var21 = (AbstractSVGTransform)this.transforms.lastElement();
         var22 = (AbstractSVGTransform)var10.transforms.elementAt(var11);
         if (var22 == null) {
            var22 = new SVGOMTransform();
            var10.transforms.setElementAt(var22, var11);
         }

         ((AbstractSVGTransform)var22).assign(var21);
      }

      var10.hasChanged = true;
      return var10;
   }

   public static AnimatableTransformListValue interpolate(AnimatableTransformListValue var0, AnimatableTransformListValue var1, AnimatableTransformListValue var2, AnimatableTransformListValue var3, AnimatableTransformListValue var4, float var5, float var6, AnimatableTransformListValue var7, int var8) {
      int var9 = var7 == null ? 0 : var7.transforms.size();
      int var10 = var9 * var8 + 1;
      if (var0 == null) {
         var0 = new AnimatableTransformListValue(var3.target);
         var0.transforms = new Vector(var10);
         var0.transforms.setSize(var10);
      } else if (var0.transforms == null) {
         var0.transforms = new Vector(var10);
         var0.transforms.setSize(var10);
      } else if (var0.transforms.size() != var10) {
         var0.transforms.setSize(var10);
      }

      int var11 = 0;

      for(int var12 = 0; var12 < var8; ++var12) {
         for(int var13 = 0; var13 < var9; ++var11) {
            var0.transforms.setElementAt(var7.transforms.elementAt(var13), var11);
            ++var13;
         }
      }

      AbstractSVGTransform var20 = (AbstractSVGTransform)var1.transforms.lastElement();
      AbstractSVGTransform var21 = (AbstractSVGTransform)var2.transforms.lastElement();
      Object var14 = (AbstractSVGTransform)var0.transforms.elementAt(var11);
      if (var14 == null) {
         var14 = new SVGOMTransform();
         var0.transforms.setElementAt(var14, var11);
      }

      short var15 = var20.getType();
      float var16;
      float var17;
      if (var15 == 3) {
         var16 = var20.getMatrix().getA();
         var17 = var21.getMatrix().getD();
      } else {
         var16 = var20.getMatrix().getE();
         var17 = var21.getMatrix().getF();
      }

      if (var3 != null) {
         AbstractSVGTransform var18 = (AbstractSVGTransform)var3.transforms.lastElement();
         AbstractSVGTransform var19 = (AbstractSVGTransform)var4.transforms.lastElement();
         if (var15 == 3) {
            var16 += var5 * (var18.getMatrix().getA() - var16);
            var17 += var6 * (var19.getMatrix().getD() - var17);
         } else {
            var16 += var5 * (var18.getMatrix().getE() - var16);
            var17 += var6 * (var19.getMatrix().getF() - var17);
         }
      }

      if (var15 == 3) {
         ((AbstractSVGTransform)var14).setScale(var16, var17);
      } else {
         ((AbstractSVGTransform)var14).setTranslate(var16, var17);
      }

      var0.hasChanged = true;
      return var0;
   }

   public static AnimatableTransformListValue interpolate(AnimatableTransformListValue var0, AnimatableTransformListValue var1, AnimatableTransformListValue var2, AnimatableTransformListValue var3, AnimatableTransformListValue var4, AnimatableTransformListValue var5, AnimatableTransformListValue var6, float var7, float var8, float var9, AnimatableTransformListValue var10, int var11) {
      int var12 = var10 == null ? 0 : var10.transforms.size();
      int var13 = var12 * var11 + 1;
      if (var0 == null) {
         var0 = new AnimatableTransformListValue(var4.target);
         var0.transforms = new Vector(var13);
         var0.transforms.setSize(var13);
      } else if (var0.transforms == null) {
         var0.transforms = new Vector(var13);
         var0.transforms.setSize(var13);
      } else if (var0.transforms.size() != var13) {
         var0.transforms.setSize(var13);
      }

      int var14 = 0;

      for(int var15 = 0; var15 < var11; ++var15) {
         for(int var16 = 0; var16 < var12; ++var14) {
            var0.transforms.setElementAt(var10.transforms.elementAt(var16), var14);
            ++var16;
         }
      }

      AbstractSVGTransform var25 = (AbstractSVGTransform)var1.transforms.lastElement();
      AbstractSVGTransform var26 = (AbstractSVGTransform)var2.transforms.lastElement();
      AbstractSVGTransform var17 = (AbstractSVGTransform)var3.transforms.lastElement();
      Object var18 = (AbstractSVGTransform)var0.transforms.elementAt(var14);
      if (var18 == null) {
         var18 = new SVGOMTransform();
         var0.transforms.setElementAt(var18, var14);
      }

      float var21 = var25.getAngle();
      float var19 = var26.getX();
      float var20 = var17.getY();
      if (var4 != null) {
         AbstractSVGTransform var22 = (AbstractSVGTransform)var4.transforms.lastElement();
         AbstractSVGTransform var23 = (AbstractSVGTransform)var5.transforms.lastElement();
         AbstractSVGTransform var24 = (AbstractSVGTransform)var6.transforms.lastElement();
         var21 += var7 * (var22.getAngle() - var21);
         var19 += var8 * (var23.getX() - var19);
         var20 += var9 * (var24.getY() - var20);
      }

      ((AbstractSVGTransform)var18).setRotate(var21, var19, var20);
      var0.hasChanged = true;
      return var0;
   }

   public Iterator getTransforms() {
      return this.transforms.iterator();
   }

   public boolean canPace() {
      return true;
   }

   public float distanceTo(AnimatableValue var1) {
      AnimatableTransformListValue var2 = (AnimatableTransformListValue)var1;
      if (!this.transforms.isEmpty() && !var2.transforms.isEmpty()) {
         AbstractSVGTransform var3 = (AbstractSVGTransform)this.transforms.lastElement();
         AbstractSVGTransform var4 = (AbstractSVGTransform)var2.transforms.lastElement();
         short var5 = var3.getType();
         if (var5 != var4.getType()) {
            return 0.0F;
         } else {
            SVGMatrix var6 = var3.getMatrix();
            SVGMatrix var7 = var4.getMatrix();
            switch (var5) {
               case 2:
                  return Math.abs(var6.getE() - var7.getE()) + Math.abs(var6.getF() - var7.getF());
               case 3:
                  return Math.abs(var6.getA() - var7.getA()) + Math.abs(var6.getD() - var7.getD());
               case 4:
               case 5:
               case 6:
                  return Math.abs(var3.getAngle() - var4.getAngle());
               default:
                  return 0.0F;
            }
         }
      } else {
         return 0.0F;
      }
   }

   public float distanceTo1(AnimatableValue var1) {
      AnimatableTransformListValue var2 = (AnimatableTransformListValue)var1;
      if (!this.transforms.isEmpty() && !var2.transforms.isEmpty()) {
         AbstractSVGTransform var3 = (AbstractSVGTransform)this.transforms.lastElement();
         AbstractSVGTransform var4 = (AbstractSVGTransform)var2.transforms.lastElement();
         short var5 = var3.getType();
         if (var5 != var4.getType()) {
            return 0.0F;
         } else {
            SVGMatrix var6 = var3.getMatrix();
            SVGMatrix var7 = var4.getMatrix();
            switch (var5) {
               case 2:
                  return Math.abs(var6.getE() - var7.getE());
               case 3:
                  return Math.abs(var6.getA() - var7.getA());
               case 4:
               case 5:
               case 6:
                  return Math.abs(var3.getAngle() - var4.getAngle());
               default:
                  return 0.0F;
            }
         }
      } else {
         return 0.0F;
      }
   }

   public float distanceTo2(AnimatableValue var1) {
      AnimatableTransformListValue var2 = (AnimatableTransformListValue)var1;
      if (!this.transforms.isEmpty() && !var2.transforms.isEmpty()) {
         AbstractSVGTransform var3 = (AbstractSVGTransform)this.transforms.lastElement();
         AbstractSVGTransform var4 = (AbstractSVGTransform)var2.transforms.lastElement();
         short var5 = var3.getType();
         if (var5 != var4.getType()) {
            return 0.0F;
         } else {
            SVGMatrix var6 = var3.getMatrix();
            SVGMatrix var7 = var4.getMatrix();
            switch (var5) {
               case 2:
                  return Math.abs(var6.getF() - var7.getF());
               case 3:
                  return Math.abs(var6.getD() - var7.getD());
               case 4:
                  return Math.abs(var3.getX() - var4.getX());
               default:
                  return 0.0F;
            }
         }
      } else {
         return 0.0F;
      }
   }

   public float distanceTo3(AnimatableValue var1) {
      AnimatableTransformListValue var2 = (AnimatableTransformListValue)var1;
      if (!this.transforms.isEmpty() && !var2.transforms.isEmpty()) {
         AbstractSVGTransform var3 = (AbstractSVGTransform)this.transforms.lastElement();
         AbstractSVGTransform var4 = (AbstractSVGTransform)var2.transforms.lastElement();
         short var5 = var3.getType();
         if (var5 != var4.getType()) {
            return 0.0F;
         } else {
            return var5 == 4 ? Math.abs(var3.getY() - var4.getY()) : 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   public AnimatableValue getZeroValue() {
      return new AnimatableTransformListValue(this.target, new Vector(5));
   }

   public String toStringRep() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.transforms.iterator();

      while(var2.hasNext()) {
         AbstractSVGTransform var3 = (AbstractSVGTransform)var2.next();
         if (var3 == null) {
            var1.append("null");
         } else {
            SVGMatrix var4 = var3.getMatrix();
            switch (var3.getType()) {
               case 2:
                  var1.append("translate(");
                  var1.append(var4.getE());
                  var1.append(',');
                  var1.append(var4.getF());
                  var1.append(')');
                  break;
               case 3:
                  var1.append("scale(");
                  var1.append(var4.getA());
                  var1.append(',');
                  var1.append(var4.getD());
                  var1.append(')');
                  break;
               case 4:
                  var1.append("rotate(");
                  var1.append(var3.getAngle());
                  var1.append(',');
                  var1.append(var3.getX());
                  var1.append(',');
                  var1.append(var3.getY());
                  var1.append(')');
                  break;
               case 5:
                  var1.append("skewX(");
                  var1.append(var3.getAngle());
                  var1.append(')');
                  break;
               case 6:
                  var1.append("skewY(");
                  var1.append(var3.getAngle());
                  var1.append(')');
            }
         }

         if (var2.hasNext()) {
            var1.append(' ');
         }
      }

      return var1.toString();
   }

   static {
      IDENTITY_SKEWX.setSkewX(0.0F);
      IDENTITY_SKEWY.setSkewY(0.0F);
      IDENTITY_SCALE.setScale(0.0F, 0.0F);
      IDENTITY_ROTATE.setRotate(0.0F, 0.0F, 0.0F);
      IDENTITY_TRANSLATE.setTranslate(0.0F, 0.0F);
   }
}
