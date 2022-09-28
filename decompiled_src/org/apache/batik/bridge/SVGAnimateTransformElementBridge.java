package org.apache.batik.bridge;

import java.util.ArrayList;
import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.TransformAnimation;
import org.apache.batik.anim.values.AnimatableTransformListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.svg.SVGOMTransform;

public class SVGAnimateTransformElementBridge extends SVGAnimateElementBridge {
   public String getLocalName() {
      return "animateTransform";
   }

   public Bridge getInstance() {
      return new SVGAnimateTransformElementBridge();
   }

   protected AbstractAnimation createAnimation(AnimationTarget var1) {
      short var2 = this.parseType();
      AnimatableValue var3 = null;
      AnimatableValue var4 = null;
      AnimatableValue var5 = null;
      if (this.element.hasAttributeNS((String)null, "from")) {
         var3 = this.parseValue(this.element.getAttributeNS((String)null, "from"), var2, var1);
      }

      if (this.element.hasAttributeNS((String)null, "to")) {
         var4 = this.parseValue(this.element.getAttributeNS((String)null, "to"), var2, var1);
      }

      if (this.element.hasAttributeNS((String)null, "by")) {
         var5 = this.parseValue(this.element.getAttributeNS((String)null, "by"), var2, var1);
      }

      return new TransformAnimation(this.timedElement, this, this.parseCalcMode(), this.parseKeyTimes(), this.parseKeySplines(), this.parseAdditive(), this.parseAccumulate(), this.parseValues(var2, var1), var3, var4, var5, var2);
   }

   protected short parseType() {
      String var1 = this.element.getAttributeNS((String)null, "type");
      if (var1.equals("translate")) {
         return 2;
      } else if (var1.equals("scale")) {
         return 3;
      } else if (var1.equals("rotate")) {
         return 4;
      } else if (var1.equals("skewX")) {
         return 5;
      } else if (var1.equals("skewY")) {
         return 6;
      } else {
         throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"type", var1});
      }
   }

   protected AnimatableValue parseValue(String var1, short var2, AnimationTarget var3) {
      float var5 = 0.0F;
      float var6 = 0.0F;
      int var7 = 0;
      char var8 = ',';

      int var9;
      for(var9 = var1.length(); var7 < var9; ++var7) {
         var8 = var1.charAt(var7);
         if (var8 == ' ' || var8 == ',') {
            break;
         }
      }

      float var4 = Float.parseFloat(var1.substring(0, var7));
      if (var7 < var9) {
         ++var7;
      }

      int var10 = 1;
      if (var7 < var9 && var8 == ' ') {
         while(var7 < var9) {
            var8 = var1.charAt(var7);
            if (var8 != ' ') {
               break;
            }

            ++var7;
         }

         if (var8 == ',') {
            ++var7;
         }
      }

      while(var7 < var9 && var1.charAt(var7) == ' ') {
         ++var7;
      }

      int var11 = var7;
      if (var7 < var9 && var2 != 5 && var2 != 6) {
         while(var7 < var9) {
            var8 = var1.charAt(var7);
            if (var8 == ' ' || var8 == ',') {
               break;
            }

            ++var7;
         }

         var5 = Float.parseFloat(var1.substring(var11, var7));
         if (var7 < var9) {
            ++var7;
         }

         ++var10;
         if (var7 < var9 && var8 == ' ') {
            while(var7 < var9) {
               var8 = var1.charAt(var7);
               if (var8 != ' ') {
                  break;
               }

               ++var7;
            }

            if (var8 == ',') {
               ++var7;
            }
         }

         while(var7 < var9 && var1.charAt(var7) == ' ') {
            ++var7;
         }

         var11 = var7;
         if (var7 < var9 && var2 == 4) {
            while(var7 < var9) {
               var8 = var1.charAt(var7);
               if (var8 == ',' || var8 == ' ') {
                  break;
               }

               ++var7;
            }

            var6 = Float.parseFloat(var1.substring(var11, var7));
            if (var7 < var9) {
               ++var7;
            }

            ++var10;

            while(var7 < var9 && var1.charAt(var7) == ' ') {
               ++var7;
            }
         }
      }

      if (var7 != var9) {
         return null;
      } else {
         SVGOMTransform var12 = new SVGOMTransform();
         switch (var2) {
            case 2:
               if (var10 == 2) {
                  var12.setTranslate(var4, var5);
               } else {
                  var12.setTranslate(var4, 0.0F);
               }
               break;
            case 3:
               if (var10 == 2) {
                  var12.setScale(var4, var5);
               } else {
                  var12.setScale(var4, var4);
               }
               break;
            case 4:
               if (var10 == 3) {
                  var12.setRotate(var4, var5, var6);
               } else {
                  var12.setRotate(var4, 0.0F, 0.0F);
               }
               break;
            case 5:
               var12.setSkewX(var4);
               break;
            case 6:
               var12.setSkewY(var4);
         }

         return new AnimatableTransformListValue(var3, var12);
      }
   }

   protected AnimatableValue[] parseValues(short var1, AnimationTarget var2) {
      String var3 = this.element.getAttributeNS((String)null, "values");
      int var4 = var3.length();
      if (var4 == 0) {
         return null;
      } else {
         ArrayList var5 = new ArrayList(7);
         int var6 = 0;
         boolean var7 = false;

         label43:
         while(var6 < var4) {
            while(var3.charAt(var6) == ' ') {
               ++var6;
               if (var6 == var4) {
                  break label43;
               }
            }

            int var12 = var6++;
            if (var6 < var4) {
               for(char var9 = var3.charAt(var6); var9 != ';'; var9 = var3.charAt(var6)) {
                  ++var6;
                  if (var6 == var4) {
                     break;
                  }
               }
            }

            int var8 = var6++;
            String var10 = var3.substring(var12, var8);
            AnimatableValue var11 = this.parseValue(var10, var1, var2);
            if (var11 == null) {
               throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"values", var3});
            }

            var5.add(var11);
         }

         AnimatableValue[] var13 = new AnimatableValue[var5.size()];
         return (AnimatableValue[])var5.toArray(var13);
      }
   }

   protected boolean canAnimateType(int var1) {
      return var1 == 9;
   }
}
