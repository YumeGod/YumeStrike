package org.apache.batik.bridge;

import java.util.ArrayList;
import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.SimpleAnimation;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;

public class SVGAnimateElementBridge extends SVGAnimationElementBridge {
   public String getLocalName() {
      return "animate";
   }

   public Bridge getInstance() {
      return new SVGAnimateElementBridge();
   }

   protected AbstractAnimation createAnimation(AnimationTarget var1) {
      AnimatableValue var2 = this.parseAnimatableValue("from");
      AnimatableValue var3 = this.parseAnimatableValue("to");
      AnimatableValue var4 = this.parseAnimatableValue("by");
      return new SimpleAnimation(this.timedElement, this, this.parseCalcMode(), this.parseKeyTimes(), this.parseKeySplines(), this.parseAdditive(), this.parseAccumulate(), this.parseValues(), var2, var3, var4);
   }

   protected int parseCalcMode() {
      if ((this.animationType != 1 || this.targetElement.isPropertyAdditive(this.attributeLocalName)) && (this.animationType != 0 || this.targetElement.isAttributeAdditive(this.attributeNamespaceURI, this.attributeLocalName))) {
         String var1 = this.element.getAttributeNS((String)null, "calcMode");
         if (var1.length() == 0) {
            return this.getDefaultCalcMode();
         } else if (var1.equals("linear")) {
            return 1;
         } else if (var1.equals("discrete")) {
            return 0;
         } else if (var1.equals("paced")) {
            return 2;
         } else if (var1.equals("spline")) {
            return 3;
         } else {
            throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"calcMode", var1});
         }
      } else {
         return 0;
      }
   }

   protected boolean parseAdditive() {
      String var1 = this.element.getAttributeNS((String)null, "additive");
      if (var1.length() != 0 && !var1.equals("replace")) {
         if (var1.equals("sum")) {
            return true;
         } else {
            throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"additive", var1});
         }
      } else {
         return false;
      }
   }

   protected boolean parseAccumulate() {
      String var1 = this.element.getAttributeNS((String)null, "accumulate");
      if (var1.length() != 0 && !var1.equals("none")) {
         if (var1.equals("sum")) {
            return true;
         } else {
            throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"accumulate", var1});
         }
      } else {
         return false;
      }
   }

   protected AnimatableValue[] parseValues() {
      boolean var1 = this.animationType == 1;
      String var2 = this.element.getAttributeNS((String)null, "values");
      int var3 = var2.length();
      if (var3 == 0) {
         return null;
      } else {
         ArrayList var4 = new ArrayList(7);
         int var5 = 0;
         boolean var6 = false;

         label48:
         while(var5 < var3) {
            while(var2.charAt(var5) == ' ') {
               ++var5;
               if (var5 == var3) {
                  break label48;
               }
            }

            int var10 = var5++;
            if (var5 != var3) {
               for(char var8 = var2.charAt(var5); var8 != ';'; var8 = var2.charAt(var5)) {
                  ++var5;
                  if (var5 == var3) {
                     break;
                  }
               }
            }

            int var7 = var5++;
            AnimatableValue var9 = this.eng.parseAnimatableValue(this.element, this.animationTarget, this.attributeNamespaceURI, this.attributeLocalName, var1, var2.substring(var10, var7));
            if (!this.checkValueType(var9)) {
               throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"values", var2});
            }

            var4.add(var9);
         }

         AnimatableValue[] var11 = new AnimatableValue[var4.size()];
         return (AnimatableValue[])var4.toArray(var11);
      }
   }

   protected float[] parseKeyTimes() {
      String var1 = this.element.getAttributeNS((String)null, "keyTimes");
      int var2 = var1.length();
      if (var2 == 0) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(7);
         int var4 = 0;
         boolean var5 = false;

         label58:
         while(var4 < var2) {
            while(var1.charAt(var4) == ' ') {
               ++var4;
               if (var4 == var2) {
                  break label58;
               }
            }

            int var11 = var4++;
            if (var4 != var2) {
               for(char var7 = var1.charAt(var4); var7 != ' ' && var7 != ';'; var7 = var1.charAt(var4)) {
                  ++var4;
                  if (var4 == var2) {
                     break;
                  }
               }
            }

            int var6 = var4++;

            try {
               float var8 = Float.parseFloat(var1.substring(var11, var6));
               var3.add(new Float(var8));
            } catch (NumberFormatException var10) {
               throw new BridgeException(this.ctx, this.element, var10, "attribute.malformed", new Object[]{"keyTimes", var1});
            }
         }

         var2 = var3.size();
         float[] var12 = new float[var2];

         for(int var9 = 0; var9 < var2; ++var9) {
            var12[var9] = (Float)var3.get(var9);
         }

         return var12;
      }
   }

   protected float[] parseKeySplines() {
      String var1 = this.element.getAttributeNS((String)null, "keySplines");
      int var2 = var1.length();
      if (var2 == 0) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(7);
         int var4 = 0;
         int var5 = 0;
         boolean var6 = false;

         label88:
         while(var5 < var2) {
            while(var1.charAt(var5) == ' ') {
               ++var5;
               if (var5 == var2) {
                  break label88;
               }
            }

            int var12 = var5++;
            int var7;
            if (var5 != var2) {
               char var8;
               for(var8 = var1.charAt(var5); var8 != ' ' && var8 != ',' && var8 != ';'; var8 = var1.charAt(var5)) {
                  ++var5;
                  if (var5 == var2) {
                     break;
                  }
               }

               var7 = var5++;
               if (var8 == ' ') {
                  while(var5 != var2) {
                     var8 = var1.charAt(var5++);
                     if (var8 != ' ') {
                        break;
                     }
                  }

                  if (var8 != ';' && var8 != ',') {
                     --var5;
                  }
               }

               if (var8 == ';') {
                  if (var4 != 3) {
                     throw new BridgeException(this.ctx, this.element, "attribute.malformed", new Object[]{"keySplines", var1});
                  }

                  var4 = 0;
               } else {
                  ++var4;
               }
            } else {
               var7 = var5++;
            }

            try {
               float var9 = Float.parseFloat(var1.substring(var12, var7));
               var3.add(new Float(var9));
            } catch (NumberFormatException var11) {
               throw new BridgeException(this.ctx, this.element, var11, "attribute.malformed", new Object[]{"keySplines", var1});
            }
         }

         var2 = var3.size();
         float[] var13 = new float[var2];

         for(int var10 = 0; var10 < var2; ++var10) {
            var13[var10] = (Float)var3.get(var10);
         }

         return var13;
      }
   }

   protected int getDefaultCalcMode() {
      return 1;
   }

   protected boolean canAnimateType(int var1) {
      return true;
   }
}
