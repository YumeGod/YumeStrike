package org.apache.batik.bridge;

import java.util.ArrayList;
import org.apache.batik.anim.AbstractAnimation;
import org.apache.batik.anim.MotionAnimation;
import org.apache.batik.anim.values.AnimatableMotionPointValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.svg.SVGAnimatedPathDataSupport;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.apache.batik.parser.AWTPathProducer;
import org.apache.batik.parser.AngleHandler;
import org.apache.batik.parser.AngleParser;
import org.apache.batik.parser.LengthArrayProducer;
import org.apache.batik.parser.LengthPairListParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGAnimateMotionElementBridge extends SVGAnimateElementBridge {
   public String getLocalName() {
      return "animateMotion";
   }

   public Bridge getInstance() {
      return new SVGAnimateMotionElementBridge();
   }

   protected AbstractAnimation createAnimation(AnimationTarget var1) {
      this.animationType = 2;
      this.attributeLocalName = "motion";
      AnimatableValue var2 = this.parseLengthPair("from");
      AnimatableValue var3 = this.parseLengthPair("to");
      AnimatableValue var4 = this.parseLengthPair("by");
      boolean var5 = false;
      boolean var6 = false;
      float var7 = 0.0F;
      short var8 = 0;
      String var9 = this.element.getAttributeNS((String)null, "rotate");
      if (var9.length() != 0) {
         if (var9.equals("auto")) {
            var5 = true;
         } else if (var9.equals("auto-reverse")) {
            var5 = true;
            var6 = true;
         } else {
            AngleParser var10 = new AngleParser();

            class Handler implements AngleHandler {
               float theAngle;
               short theUnit = 1;

               public void startAngle() throws ParseException {
               }

               public void angleValue(float var1) throws ParseException {
                  this.theAngle = var1;
               }

               public void deg() throws ParseException {
                  this.theUnit = 2;
               }

               public void grad() throws ParseException {
                  this.theUnit = 4;
               }

               public void rad() throws ParseException {
                  this.theUnit = 3;
               }

               public void endAngle() throws ParseException {
               }
            }

            Handler var11 = new Handler();
            var10.setAngleHandler(var11);

            try {
               var10.parse(var9);
            } catch (ParseException var13) {
               throw new BridgeException(this.ctx, this.element, var13, "attribute.malformed", new Object[]{"rotate", var9});
            }

            var7 = var11.theAngle;
            var8 = var11.theUnit;
         }
      }

      return new MotionAnimation(this.timedElement, this, this.parseCalcMode(), this.parseKeyTimes(), this.parseKeySplines(), this.parseAdditive(), this.parseAccumulate(), this.parseValues(), var2, var3, var4, this.parsePath(), this.parseKeyPoints(), var5, var6, var7, var8);
   }

   protected ExtendedGeneralPath parsePath() {
      String var2;
      for(Node var1 = this.element.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1.getNodeType() == 1 && "http://www.w3.org/2000/svg".equals(var1.getNamespaceURI()) && "mpath".equals(var1.getLocalName())) {
            var2 = XLinkSupport.getXLinkHref((Element)var1);
            Element var3 = this.ctx.getReferencedElement(this.element, var2);
            if ("http://www.w3.org/2000/svg".equals(var3.getNamespaceURI()) && "path".equals(var3.getLocalName())) {
               SVGOMPathElement var4 = (SVGOMPathElement)var3;
               AWTPathProducer var5 = new AWTPathProducer();
               SVGAnimatedPathDataSupport.handlePathSegList(var4.getPathSegList(), var5);
               return (ExtendedGeneralPath)var5.getShape();
            }

            throw new BridgeException(this.ctx, this.element, "uri.badTarget", new Object[]{var2});
         }
      }

      var2 = this.element.getAttributeNS((String)null, "path");
      if (var2.length() == 0) {
         return null;
      } else {
         try {
            AWTPathProducer var7 = new AWTPathProducer();
            PathParser var8 = new PathParser();
            var8.setPathHandler(var7);
            var8.parse(var2);
            return (ExtendedGeneralPath)var7.getShape();
         } catch (ParseException var6) {
            throw new BridgeException(this.ctx, this.element, var6, "attribute.malformed", new Object[]{"path", var2});
         }
      }
   }

   protected float[] parseKeyPoints() {
      String var1 = this.element.getAttributeNS((String)null, "keyPoints");
      int var2 = var1.length();
      if (var2 == 0) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(7);
         int var4 = 0;
         boolean var5 = false;

         label61:
         while(var4 < var2) {
            while(var1.charAt(var4) == ' ') {
               ++var4;
               if (var4 == var2) {
                  break label61;
               }
            }

            int var11 = var4++;
            if (var4 != var2) {
               for(char var7 = var1.charAt(var4); var7 != ' ' && var7 != ';' && var7 != ','; var7 = var1.charAt(var4)) {
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
               throw new BridgeException(this.ctx, this.element, var10, "attribute.malformed", new Object[]{"keyPoints", var1});
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

   protected int getDefaultCalcMode() {
      return 2;
   }

   protected AnimatableValue[] parseValues() {
      String var1 = this.element.getAttributeNS((String)null, "values");
      int var2 = var1.length();
      return var2 == 0 ? null : this.parseValues(var1);
   }

   protected AnimatableValue[] parseValues(String var1) {
      try {
         LengthPairListParser var2 = new LengthPairListParser();
         LengthArrayProducer var3 = new LengthArrayProducer();
         var2.setLengthListHandler(var3);
         var2.parse(var1);
         short[] var4 = var3.getLengthTypeArray();
         float[] var5 = var3.getLengthValueArray();
         AnimatableValue[] var6 = new AnimatableValue[var4.length / 2];

         for(int var7 = 0; var7 < var4.length; var7 += 2) {
            float var8 = this.animationTarget.svgToUserSpace(var5[var7], var4[var7], (short)1);
            float var9 = this.animationTarget.svgToUserSpace(var5[var7 + 1], var4[var7 + 1], (short)2);
            var6[var7 / 2] = new AnimatableMotionPointValue(this.animationTarget, var8, var9, 0.0F);
         }

         return var6;
      } catch (ParseException var10) {
         throw new BridgeException(this.ctx, this.element, var10, "attribute.malformed", new Object[]{"values", var1});
      }
   }

   protected AnimatableValue parseLengthPair(String var1) {
      String var2 = this.element.getAttributeNS((String)null, var1);
      return var2.length() == 0 ? null : this.parseValues(var2)[0];
   }

   public AnimatableValue getUnderlyingValue() {
      return new AnimatableMotionPointValue(this.animationTarget, 0.0F, 0.0F, 0.0F);
   }

   protected void initializeAnimation() {
      String var1 = XLinkSupport.getXLinkHref(this.element);
      Object var2;
      if (var1.length() == 0) {
         var2 = this.element.getParentNode();
      } else {
         var2 = this.ctx.getReferencedElement(this.element, var1);
         if (((Node)var2).getOwnerDocument() != this.element.getOwnerDocument()) {
            throw new BridgeException(this.ctx, this.element, "uri.badTarget", new Object[]{var1});
         }
      }

      this.animationTarget = null;
      if (var2 instanceof SVGOMElement) {
         this.targetElement = (SVGOMElement)var2;
         this.animationTarget = this.targetElement;
      }

      if (this.animationTarget == null) {
         throw new BridgeException(this.ctx, this.element, "uri.badTarget", new Object[]{var1});
      } else {
         this.timedElement = this.createTimedElement();
         this.animation = this.createAnimation(this.animationTarget);
         this.eng.addAnimation(this.animationTarget, (short)2, this.attributeNamespaceURI, this.attributeLocalName, this.animation);
      }
   }
}
