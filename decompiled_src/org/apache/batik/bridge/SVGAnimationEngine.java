package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.batik.anim.AnimationEngine;
import org.apache.batik.anim.AnimationException;
import org.apache.batik.anim.timing.TimedDocumentRoot;
import org.apache.batik.anim.timing.TimedElement;
import org.apache.batik.anim.values.AnimatableAngleOrIdentValue;
import org.apache.batik.anim.values.AnimatableAngleValue;
import org.apache.batik.anim.values.AnimatableBooleanValue;
import org.apache.batik.anim.values.AnimatableColorValue;
import org.apache.batik.anim.values.AnimatableIntegerValue;
import org.apache.batik.anim.values.AnimatableLengthListValue;
import org.apache.batik.anim.values.AnimatableLengthOrIdentValue;
import org.apache.batik.anim.values.AnimatableLengthValue;
import org.apache.batik.anim.values.AnimatableNumberListValue;
import org.apache.batik.anim.values.AnimatableNumberOrIdentValue;
import org.apache.batik.anim.values.AnimatableNumberOrPercentageValue;
import org.apache.batik.anim.values.AnimatableNumberValue;
import org.apache.batik.anim.values.AnimatablePaintValue;
import org.apache.batik.anim.values.AnimatablePathDataValue;
import org.apache.batik.anim.values.AnimatablePointListValue;
import org.apache.batik.anim.values.AnimatablePreserveAspectRatioValue;
import org.apache.batik.anim.values.AnimatableRectValue;
import org.apache.batik.anim.values.AnimatableStringValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.parser.DefaultLengthHandler;
import org.apache.batik.parser.DefaultPreserveAspectRatioHandler;
import org.apache.batik.parser.FloatArrayProducer;
import org.apache.batik.parser.LengthArrayProducer;
import org.apache.batik.parser.LengthHandler;
import org.apache.batik.parser.LengthListParser;
import org.apache.batik.parser.LengthParser;
import org.apache.batik.parser.NumberListParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathArrayProducer;
import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.PointsParser;
import org.apache.batik.parser.PreserveAspectRatioParser;
import org.apache.batik.util.RunnableQueue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.EventTarget;

public class SVGAnimationEngine extends AnimationEngine {
   protected BridgeContext ctx;
   protected CSSEngine cssEngine;
   protected boolean started;
   protected AnimationTickRunnable animationTickRunnable;
   protected UncomputedAnimatableStringValueFactory uncomputedAnimatableStringValueFactory = new UncomputedAnimatableStringValueFactory();
   protected AnimatableLengthOrIdentFactory animatableLengthOrIdentFactory = new AnimatableLengthOrIdentFactory();
   protected AnimatableNumberOrIdentFactory animatableNumberOrIdentFactory = new AnimatableNumberOrIdentFactory(false);
   protected Factory[] factories;
   protected boolean isSVG12;
   protected LinkedList initialBridges;
   protected StyleMap dummyStyleMap;
   protected AnimationThread animationThread;
   protected int animationLimitingMode;
   protected float animationLimitingAmount;
   protected static final Set animationEventNames11 = new HashSet();
   protected static final Set animationEventNames12 = new HashSet();

   public SVGAnimationEngine(Document var1, BridgeContext var2) {
      super(var1);
      this.factories = new Factory[]{null, new AnimatableIntegerValueFactory(), new AnimatableNumberValueFactory(), new AnimatableLengthValueFactory(), null, new AnimatableAngleValueFactory(), new AnimatableColorValueFactory(), new AnimatablePaintValueFactory(), null, null, this.uncomputedAnimatableStringValueFactory, null, null, new AnimatableNumberListValueFactory(), new AnimatableLengthListValueFactory(), this.uncomputedAnimatableStringValueFactory, this.uncomputedAnimatableStringValueFactory, this.animatableLengthOrIdentFactory, this.uncomputedAnimatableStringValueFactory, this.uncomputedAnimatableStringValueFactory, this.uncomputedAnimatableStringValueFactory, this.uncomputedAnimatableStringValueFactory, new AnimatablePathDataFactory(), this.uncomputedAnimatableStringValueFactory, null, this.animatableNumberOrIdentFactory, this.uncomputedAnimatableStringValueFactory, null, new AnimatableNumberOrIdentFactory(true), new AnimatableAngleOrIdentFactory(), null, new AnimatablePointListValueFactory(), new AnimatablePreserveAspectRatioValueFactory(), null, this.uncomputedAnimatableStringValueFactory, null, null, null, null, this.animatableLengthOrIdentFactory, this.animatableLengthOrIdentFactory, this.animatableLengthOrIdentFactory, this.animatableLengthOrIdentFactory, this.animatableLengthOrIdentFactory, this.animatableNumberOrIdentFactory, null, null, new AnimatableNumberOrPercentageValueFactory(), null, new AnimatableBooleanValueFactory(), new AnimatableRectValueFactory()};
      this.initialBridges = new LinkedList();
      this.ctx = var2;
      SVGOMDocument var3 = (SVGOMDocument)var1;
      this.cssEngine = var3.getCSSEngine();
      this.dummyStyleMap = new StyleMap(this.cssEngine.getNumberOfProperties());
      this.isSVG12 = var3.isSVG12();
   }

   public void dispose() {
      synchronized(this) {
         this.pause();
         super.dispose();
      }
   }

   public void addInitialBridge(SVGAnimationElementBridge var1) {
      if (this.initialBridges != null) {
         this.initialBridges.add(var1);
      }

   }

   public boolean hasStarted() {
      return this.started;
   }

   public AnimatableValue parseAnimatableValue(Element var1, AnimationTarget var2, String var3, String var4, boolean var5, String var6) {
      SVGOMElement var7 = (SVGOMElement)var2.getElement();
      int var8;
      if (var5) {
         var8 = var7.getPropertyType(var4);
      } else {
         var8 = var7.getAttributeType(var3, var4);
      }

      Factory var9 = this.factories[var8];
      if (var9 == null) {
         String var10 = var3 == null ? var4 : '{' + var3 + '}' + var4;
         throw new BridgeException(this.ctx, var1, "attribute.not.animatable", new Object[]{var2.getElement().getNodeName(), var10});
      } else {
         return this.factories[var8].createValue(var2, var3, var4, var5, var6);
      }
   }

   public AnimatableValue getUnderlyingCSSValue(Element var1, AnimationTarget var2, String var3) {
      ValueManager[] var4 = this.cssEngine.getValueManagers();
      int var5 = this.cssEngine.getPropertyIndex(var3);
      if (var5 != -1) {
         int var6 = var4[var5].getPropertyType();
         Factory var7 = this.factories[var6];
         if (var7 == null) {
            throw new BridgeException(this.ctx, var1, "attribute.not.animatable", new Object[]{var2.getElement().getNodeName(), var3});
         } else {
            SVGStylableElement var8 = (SVGStylableElement)var2.getElement();
            CSSStyleDeclaration var9 = var8.getOverrideStyle();
            String var10 = var9.getPropertyValue(var3);
            if (var10 != null) {
               var9.removeProperty(var3);
            }

            Value var11 = this.cssEngine.getComputedStyle(var8, (String)null, var5);
            if (var10 != null && !var10.equals("")) {
               var9.setProperty(var3, var10, (String)null);
            }

            return this.factories[var6].createValue(var2, var3, var11);
         }
      } else {
         return null;
      }
   }

   public void pause() {
      super.pause();
      UpdateManager var1 = this.ctx.getUpdateManager();
      if (var1 != null) {
         var1.getUpdateRunnableQueue().setIdleRunnable((RunnableQueue.IdleRunnable)null);
      }

   }

   public void unpause() {
      super.unpause();
      UpdateManager var1 = this.ctx.getUpdateManager();
      if (var1 != null) {
         var1.getUpdateRunnableQueue().setIdleRunnable(this.animationTickRunnable);
      }

   }

   public float getCurrentTime() {
      boolean var1 = this.pauseTime != 0L;
      this.unpause();
      float var2 = this.timedDocumentRoot.getCurrentTime();
      if (var1) {
         this.pause();
      }

      return var2;
   }

   public float setCurrentTime(float var1) {
      float var2 = super.setCurrentTime(var1);
      if (this.animationTickRunnable != null) {
         this.animationTickRunnable.resume();
      }

      return var2;
   }

   protected TimedDocumentRoot createDocumentRoot() {
      return new AnimationRoot();
   }

   public void start(long var1) {
      if (!this.started) {
         this.started = true;

         try {
            try {
               Calendar var3 = Calendar.getInstance();
               var3.setTime(new Date(var1));
               this.timedDocumentRoot.resetDocument(var3);
               Object[] var4 = this.initialBridges.toArray();
               this.initialBridges = null;

               int var5;
               SVGAnimationElementBridge var6;
               for(var5 = 0; var5 < var4.length; ++var5) {
                  var6 = (SVGAnimationElementBridge)var4[var5];
                  var6.initializeAnimation();
               }

               for(var5 = 0; var5 < var4.length; ++var5) {
                  var6 = (SVGAnimationElementBridge)var4[var5];
                  var6.initializeTimedElement();
               }

               UpdateManager var9 = this.ctx.getUpdateManager();
               if (var9 != null) {
                  RunnableQueue var10 = var9.getUpdateRunnableQueue();
                  this.animationTickRunnable = new AnimationTickRunnable(var10, this);
                  var10.setIdleRunnable(this.animationTickRunnable);
               }
            } catch (AnimationException var7) {
               throw new BridgeException(this.ctx, var7.getElement().getElement(), var7.getMessage());
            }
         } catch (Exception var8) {
            if (this.ctx.getUserAgent() == null) {
               var8.printStackTrace();
            } else {
               this.ctx.getUserAgent().displayError(var8);
            }
         }

      }
   }

   public void setAnimationLimitingNone() {
      this.animationLimitingMode = 0;
   }

   public void setAnimationLimitingCPU(float var1) {
      this.animationLimitingMode = 1;
      this.animationLimitingAmount = var1;
   }

   public void setAnimationLimitingFPS(float var1) {
      this.animationLimitingMode = 2;
      this.animationLimitingAmount = var1;
   }

   static {
      String[] var0 = new String[]{"click", "mousedown", "mouseup", "mouseover", "mousemove", "mouseout", "beginEvent", "endEvent"};
      String[] var1 = new String[]{"DOMSubtreeModified", "DOMNodeInserted", "DOMNodeRemoved", "DOMNodeRemovedFromDocument", "DOMNodeInsertedIntoDocument", "DOMAttrModified", "DOMCharacterDataModified", "SVGLoad", "SVGUnload", "SVGAbort", "SVGError", "SVGResize", "SVGScroll", "repeatEvent"};
      String[] var2 = new String[]{"load", "resize", "scroll", "zoom"};

      int var3;
      for(var3 = 0; var3 < var0.length; ++var3) {
         animationEventNames11.add(var0[var3]);
         animationEventNames12.add(var0[var3]);
      }

      for(var3 = 0; var3 < var1.length; ++var3) {
         animationEventNames11.add(var1[var3]);
      }

      for(var3 = 0; var3 < var2.length; ++var3) {
         animationEventNames12.add(var2[var3]);
      }

   }

   protected class AnimatableStringValueFactory extends CSSValueFactory {
      protected AnimatableStringValueFactory() {
         super();
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableStringValue(var1, var3.getCssText());
      }
   }

   protected class AnimatablePaintValueFactory extends CSSValueFactory {
      protected AnimatablePaintValueFactory() {
         super();
      }

      protected AnimatablePaintValue createColorPaintValue(AnimationTarget var1, Color var2) {
         return AnimatablePaintValue.createColorPaintValue(var1, (float)var2.getRed() / 255.0F, (float)var2.getGreen() / 255.0F, (float)var2.getBlue() / 255.0F);
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         if (var3.getCssValueType() == 1) {
            switch (var3.getPrimitiveType()) {
               case 20:
                  return AnimatablePaintValue.createURIPaintValue(var1, var3.getStringValue());
               case 21:
                  return AnimatablePaintValue.createNonePaintValue(var1);
               case 25:
                  Paint var4 = PaintServer.convertPaint(var1.getElement(), (GraphicsNode)null, var3, 1.0F, SVGAnimationEngine.this.ctx);
                  return this.createColorPaintValue(var1, (Color)var4);
            }
         } else {
            Value var7 = var3.item(0);
            switch (var7.getPrimitiveType()) {
               case 20:
                  Value var8 = var3.item(1);
                  switch (var8.getPrimitiveType()) {
                     case 21:
                        return AnimatablePaintValue.createURINonePaintValue(var1, var7.getStringValue());
                     case 25:
                        Paint var6 = PaintServer.convertPaint(var1.getElement(), (GraphicsNode)null, var3.item(1), 1.0F, SVGAnimationEngine.this.ctx);
                        return this.createColorPaintValue(var1, (Color)var6);
                     default:
                        return null;
                  }
               case 25:
                  Paint var5 = PaintServer.convertPaint(var1.getElement(), (GraphicsNode)null, var3, 1.0F, SVGAnimationEngine.this.ctx);
                  return this.createColorPaintValue(var1, (Color)var5);
            }
         }

         return null;
      }
   }

   protected class AnimatableColorValueFactory extends CSSValueFactory {
      protected AnimatableColorValueFactory() {
         super();
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         Paint var4 = PaintServer.convertPaint(var1.getElement(), (GraphicsNode)null, var3, 1.0F, SVGAnimationEngine.this.ctx);
         if (var4 instanceof Color) {
            Color var5 = (Color)var4;
            return new AnimatableColorValue(var1, (float)var5.getRed() / 255.0F, (float)var5.getGreen() / 255.0F, (float)var5.getBlue() / 255.0F);
         } else {
            return null;
         }
      }
   }

   protected class AnimatableAngleOrIdentFactory extends CSSValueFactory {
      protected AnimatableAngleOrIdentFactory() {
         super();
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         if (var3 instanceof StringValue) {
            return new AnimatableAngleOrIdentValue(var1, var3.getStringValue());
         } else {
            FloatValue var4 = (FloatValue)var3;
            byte var5;
            switch (var4.getPrimitiveType()) {
               case 1:
               case 11:
                  var5 = 2;
                  break;
               case 12:
                  var5 = 3;
                  break;
               case 13:
                  var5 = 4;
                  break;
               default:
                  return null;
            }

            return new AnimatableAngleOrIdentValue(var1, var4.getFloatValue(), var5);
         }
      }
   }

   protected class AnimatableAngleValueFactory extends CSSValueFactory {
      protected AnimatableAngleValueFactory() {
         super();
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         FloatValue var4 = (FloatValue)var3;
         byte var5;
         switch (var4.getPrimitiveType()) {
            case 1:
            case 11:
               var5 = 2;
               break;
            case 12:
               var5 = 3;
               break;
            case 13:
               var5 = 4;
               break;
            default:
               return null;
         }

         return new AnimatableAngleValue(var1, var4.getFloatValue(), var5);
      }
   }

   protected class AnimatableNumberOrIdentFactory extends CSSValueFactory {
      protected boolean numericIdents;

      public AnimatableNumberOrIdentFactory(boolean var2) {
         super();
         this.numericIdents = var2;
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         if (var3 instanceof StringValue) {
            return new AnimatableNumberOrIdentValue(var1, var3.getStringValue());
         } else {
            FloatValue var4 = (FloatValue)var3;
            return new AnimatableNumberOrIdentValue(var1, var4.getFloatValue(), this.numericIdents);
         }
      }
   }

   protected class AnimatableLengthOrIdentFactory extends CSSValueFactory {
      protected AnimatableLengthOrIdentFactory() {
         super();
      }

      protected AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3) {
         if (var3 instanceof StringValue) {
            return new AnimatableLengthOrIdentValue(var1, var3.getStringValue());
         } else {
            short var4 = var1.getPercentageInterpretation((String)null, var2, true);
            FloatValue var5 = (FloatValue)var3;
            return new AnimatableLengthOrIdentValue(var1, var5.getPrimitiveType(), var5.getFloatValue(), var4);
         }
      }
   }

   protected class UncomputedAnimatableStringValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         return new AnimatableStringValue(var1, var5);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableStringValue(var1, var3.getCssText());
      }
   }

   protected class AnimatablePathDataFactory implements Factory {
      protected PathParser parser = new PathParser();
      protected PathArrayProducer producer = new PathArrayProducer();

      public AnimatablePathDataFactory() {
         this.parser.setPathHandler(this.producer);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            this.parser.parse(var5);
            return new AnimatablePathDataValue(var1, this.producer.getPathCommands(), this.producer.getPathParameters());
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatablePointListValueFactory implements Factory {
      protected PointsParser parser = new PointsParser();
      protected FloatArrayProducer producer = new FloatArrayProducer();

      public AnimatablePointListValueFactory() {
         this.parser.setPointsHandler(this.producer);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            this.parser.parse(var5);
            return new AnimatablePointListValue(var1, this.producer.getFloatArray());
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatableRectValueFactory implements Factory {
      protected NumberListParser parser = new NumberListParser();
      protected FloatArrayProducer producer = new FloatArrayProducer();

      public AnimatableRectValueFactory() {
         this.parser.setNumberListHandler(this.producer);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            this.parser.parse(var5);
            float[] var6 = this.producer.getFloatArray();
            return var6.length != 4 ? null : new AnimatableRectValue(var1, var6[0], var6[1], var6[2], var6[3]);
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatableNumberListValueFactory implements Factory {
      protected NumberListParser parser = new NumberListParser();
      protected FloatArrayProducer producer = new FloatArrayProducer();

      public AnimatableNumberListValueFactory() {
         this.parser.setNumberListHandler(this.producer);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            this.parser.parse(var5);
            return new AnimatableNumberListValue(var1, this.producer.getFloatArray());
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatableLengthListValueFactory implements Factory {
      protected LengthListParser parser = new LengthListParser();
      protected LengthArrayProducer producer = new LengthArrayProducer();

      public AnimatableLengthListValueFactory() {
         this.parser.setLengthListHandler(this.producer);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            short var6 = var1.getPercentageInterpretation(var2, var3, var4);
            this.parser.parse(var5);
            return new AnimatableLengthListValue(var1, this.producer.getLengthTypeArray(), this.producer.getLengthValueArray(), var6);
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatableLengthValueFactory implements Factory {
      protected short type;
      protected float value;
      protected LengthParser parser = new LengthParser();
      protected LengthHandler handler = new DefaultLengthHandler() {
         public void startLength() throws ParseException {
            AnimatableLengthValueFactory.this.type = 1;
         }

         public void lengthValue(float var1) throws ParseException {
            AnimatableLengthValueFactory.this.value = var1;
         }

         public void em() throws ParseException {
            AnimatableLengthValueFactory.this.type = 3;
         }

         public void ex() throws ParseException {
            AnimatableLengthValueFactory.this.type = 4;
         }

         public void in() throws ParseException {
            AnimatableLengthValueFactory.this.type = 8;
         }

         public void cm() throws ParseException {
            AnimatableLengthValueFactory.this.type = 6;
         }

         public void mm() throws ParseException {
            AnimatableLengthValueFactory.this.type = 7;
         }

         public void pc() throws ParseException {
            AnimatableLengthValueFactory.this.type = 10;
         }

         public void pt() throws ParseException {
            AnimatableLengthValueFactory.this.type = 9;
         }

         public void px() throws ParseException {
            AnimatableLengthValueFactory.this.type = 5;
         }

         public void percentage() throws ParseException {
            AnimatableLengthValueFactory.this.type = 2;
         }

         public void endLength() throws ParseException {
         }
      };

      public AnimatableLengthValueFactory() {
         this.parser.setLengthHandler(this.handler);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         short var6 = var1.getPercentageInterpretation(var2, var3, var4);

         try {
            this.parser.parse(var5);
            return new AnimatableLengthValue(var1, this.type, this.value, var6);
         } catch (ParseException var8) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableIntegerValue(var1, Math.round(var3.getFloatValue()));
      }
   }

   protected class AnimatablePreserveAspectRatioValueFactory implements Factory {
      protected short align;
      protected short meetOrSlice;
      protected PreserveAspectRatioParser parser = new PreserveAspectRatioParser();
      protected DefaultPreserveAspectRatioHandler handler = new DefaultPreserveAspectRatioHandler() {
         public void startPreserveAspectRatio() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 0;
            AnimatablePreserveAspectRatioValueFactory.this.meetOrSlice = 0;
         }

         public void none() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 1;
         }

         public void xMaxYMax() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 10;
         }

         public void xMaxYMid() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 7;
         }

         public void xMaxYMin() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 4;
         }

         public void xMidYMax() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 9;
         }

         public void xMidYMid() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 6;
         }

         public void xMidYMin() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 3;
         }

         public void xMinYMax() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 8;
         }

         public void xMinYMid() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 5;
         }

         public void xMinYMin() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.align = 2;
         }

         public void meet() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.meetOrSlice = 1;
         }

         public void slice() throws ParseException {
            AnimatablePreserveAspectRatioValueFactory.this.meetOrSlice = 2;
         }
      };

      public AnimatablePreserveAspectRatioValueFactory() {
         this.parser.setPreserveAspectRatioHandler(this.handler);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         try {
            this.parser.parse(var5);
            return new AnimatablePreserveAspectRatioValue(var1, this.align, this.meetOrSlice);
         } catch (ParseException var7) {
            return null;
         }
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return null;
      }
   }

   protected class AnimatableNumberOrPercentageValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         float var6;
         boolean var7;
         if (var5.charAt(var5.length() - 1) == '%') {
            var6 = Float.parseFloat(var5.substring(0, var5.length() - 1));
            var7 = true;
         } else {
            var6 = Float.parseFloat(var5);
            var7 = false;
         }

         return new AnimatableNumberOrPercentageValue(var1, var6, var7);
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         switch (var3.getPrimitiveType()) {
            case 1:
               return new AnimatableNumberOrPercentageValue(var1, var3.getFloatValue());
            case 2:
               return new AnimatableNumberOrPercentageValue(var1, var3.getFloatValue(), true);
            default:
               return null;
         }
      }
   }

   protected class AnimatableNumberValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         return new AnimatableNumberValue(var1, Float.parseFloat(var5));
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableNumberValue(var1, var3.getFloatValue());
      }
   }

   protected class AnimatableIntegerValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         return new AnimatableIntegerValue(var1, Integer.parseInt(var5));
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableIntegerValue(var1, Math.round(var3.getFloatValue()));
      }
   }

   protected class AnimatableBooleanValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         return new AnimatableBooleanValue(var1, "true".equals(var5));
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         return new AnimatableBooleanValue(var1, "true".equals(var3.getCssText()));
      }
   }

   protected abstract class CSSValueFactory implements Factory {
      public AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5) {
         return this.createValue(var1, var3, this.createCSSValue(var1, var3, var5));
      }

      public AnimatableValue createValue(AnimationTarget var1, String var2, Value var3) {
         CSSStylableElement var4 = (CSSStylableElement)var1.getElement();
         var3 = this.computeValue(var4, var2, var3);
         return this.createAnimatableValue(var1, var2, var3);
      }

      protected abstract AnimatableValue createAnimatableValue(AnimationTarget var1, String var2, Value var3);

      protected Value createCSSValue(AnimationTarget var1, String var2, String var3) {
         CSSStylableElement var4 = (CSSStylableElement)var1.getElement();
         Value var5 = SVGAnimationEngine.this.cssEngine.parsePropertyValue(var4, var2, var3);
         return this.computeValue(var4, var2, var5);
      }

      protected Value computeValue(CSSStylableElement var1, String var2, Value var3) {
         ValueManager[] var4 = SVGAnimationEngine.this.cssEngine.getValueManagers();
         int var5 = SVGAnimationEngine.this.cssEngine.getPropertyIndex(var2);
         if (var5 != -1) {
            if (var3.getCssValueType() == 0) {
               var1 = CSSEngine.getParentCSSStylableElement(var1);
               if (var1 != null) {
                  return SVGAnimationEngine.this.cssEngine.getComputedStyle(var1, (String)null, var5);
               }

               return var4[var5].getDefaultValue();
            }

            var3 = var4[var5].computeValue(var1, (String)null, SVGAnimationEngine.this.cssEngine, var5, SVGAnimationEngine.this.dummyStyleMap, var3);
         }

         return var3;
      }
   }

   protected interface Factory {
      AnimatableValue createValue(AnimationTarget var1, String var2, String var3, boolean var4, String var5);

      AnimatableValue createValue(AnimationTarget var1, String var2, Value var3);
   }

   protected class AnimationThread extends Thread {
      protected Calendar time = Calendar.getInstance();
      protected RunnableQueue runnableQueue;
      protected Ticker ticker;

      protected AnimationThread() {
         this.runnableQueue = SVGAnimationEngine.this.ctx.getUpdateManager().getUpdateRunnableQueue();
         this.ticker = new Ticker();
      }

      public void run() {
         while(true) {
            this.time.setTime(new Date());
            this.ticker.t = SVGAnimationEngine.this.timedDocumentRoot.convertWallclockTime(this.time);

            try {
               this.runnableQueue.invokeAndWait(this.ticker);
            } catch (InterruptedException var2) {
               return;
            }
         }
      }

      protected class Ticker implements Runnable {
         protected float t;

         public void run() {
            SVGAnimationEngine.this.tick(this.t, false);
         }
      }
   }

   protected static class AnimationTickRunnable implements RunnableQueue.IdleRunnable {
      protected Calendar time = Calendar.getInstance();
      protected long waitTime;
      protected RunnableQueue q;
      private static final int NUM_TIMES = 8;
      protected long[] times = new long[8];
      protected long sumTime;
      protected int timeIndex;
      protected WeakReference engRef;
      protected static final int MAX_EXCEPTION_COUNT = 10;
      protected int exceptionCount;

      public AnimationTickRunnable(RunnableQueue var1, SVGAnimationEngine var2) {
         this.q = var1;
         this.engRef = new WeakReference(var2);
         Arrays.fill(this.times, 100L);
         this.sumTime = 800L;
      }

      public void resume() {
         this.waitTime = 0L;
         Object var1 = this.q.getIteratorLock();
         synchronized(var1) {
            var1.notify();
         }
      }

      public long getWaitTime() {
         return this.waitTime;
      }

      public void run() {
         SVGAnimationEngine var1 = this.getAnimationEngine();
         synchronized(var1) {
            int var3 = var1.animationLimitingMode;
            float var4 = var1.animationLimitingAmount;

            try {
               try {
                  long var5 = System.currentTimeMillis();
                  this.time.setTime(new Date(var5));
                  float var7 = var1.timedDocumentRoot.convertWallclockTime(this.time);
                  float var8 = var1.tick(var7, false);
                  long var9 = System.currentTimeMillis();
                  long var11 = var9 - var5;
                  if (var11 == 0L) {
                     var11 = 1L;
                  }

                  this.sumTime -= this.times[this.timeIndex];
                  this.sumTime += var11;
                  this.times[this.timeIndex] = var11;
                  this.timeIndex = (this.timeIndex + 1) % 8;
                  if (var8 == Float.POSITIVE_INFINITY) {
                     this.waitTime = Long.MAX_VALUE;
                  } else {
                     this.waitTime = var5 + (long)(var8 * 1000.0F) - 1000L;
                     if (this.waitTime < var9) {
                        this.waitTime = var9;
                     }

                     if (var3 != 0) {
                        float var13 = (float)this.sumTime / 8.0F;
                        float var14;
                        if (var3 == 1) {
                           var14 = var13 / var4 - var13;
                        } else {
                           var14 = 1000.0F / var4 - var13;
                        }

                        long var15 = var9 + (long)var14;
                        if (var15 > this.waitTime) {
                           this.waitTime = var15;
                        }
                     }
                  }
               } catch (AnimationException var19) {
                  throw new BridgeException(var1.ctx, var19.getElement().getElement(), var19.getMessage());
               }

               this.exceptionCount = 0;
            } catch (Exception var20) {
               if (++this.exceptionCount < 10) {
                  if (var1.ctx.getUserAgent() == null) {
                     var20.printStackTrace();
                  } else {
                     var1.ctx.getUserAgent().displayError(var20);
                  }
               }
            }

            if (var3 == 0) {
               try {
                  Thread.sleep(1L);
               } catch (InterruptedException var18) {
               }
            }

         }
      }

      protected SVGAnimationEngine getAnimationEngine() {
         return (SVGAnimationEngine)this.engRef.get();
      }
   }

   protected static class DebugAnimationTickRunnable extends AnimationTickRunnable {
      float t = 0.0F;

      public DebugAnimationTickRunnable(RunnableQueue var1, SVGAnimationEngine var2) {
         super(var1, var2);
         this.waitTime = Long.MAX_VALUE;
         (new Thread() {
            public void run() {
               BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
               System.out.println("Enter times.");

               while(true) {
                  String var2;
                  try {
                     var2 = var1.readLine();
                  } catch (IOException var4) {
                     var2 = null;
                  }

                  if (var2 == null) {
                     System.exit(0);
                  }

                  DebugAnimationTickRunnable.this.t = Float.parseFloat(var2);
                  DebugAnimationTickRunnable.this.resume();
               }
            }
         }).start();
      }

      public void resume() {
         this.waitTime = 0L;
         Object var1 = this.q.getIteratorLock();
         synchronized(var1) {
            var1.notify();
         }
      }

      public long getWaitTime() {
         long var1 = this.waitTime;
         this.waitTime = Long.MAX_VALUE;
         return var1;
      }

      public void run() {
         SVGAnimationEngine var1 = this.getAnimationEngine();
         synchronized(var1) {
            try {
               try {
                  var1.tick(this.t, false);
               } catch (AnimationException var5) {
                  throw new BridgeException(var1.ctx, var5.getElement().getElement(), var5.getMessage());
               }
            } catch (Exception var6) {
               if (var1.ctx.getUserAgent() == null) {
                  var6.printStackTrace();
               } else {
                  var1.ctx.getUserAgent().displayError(var6);
               }
            }

         }
      }
   }

   protected class AnimationRoot extends TimedDocumentRoot {
      public AnimationRoot() {
         super(!SVGAnimationEngine.this.isSVG12, SVGAnimationEngine.this.isSVG12);
      }

      protected String getEventNamespaceURI(String var1) {
         if (!SVGAnimationEngine.this.isSVG12) {
            return null;
         } else {
            return !var1.equals("focusin") && !var1.equals("focusout") && !var1.equals("activate") && !SVGAnimationEngine.animationEventNames12.contains(var1) ? null : "http://www.w3.org/2001/xml-events";
         }
      }

      protected String getEventType(String var1) {
         if (var1.equals("focusin")) {
            return "DOMFocusIn";
         } else if (var1.equals("focusout")) {
            return "DOMFocusOut";
         } else if (var1.equals("activate")) {
            return "DOMActivate";
         } else {
            if (SVGAnimationEngine.this.isSVG12) {
               if (SVGAnimationEngine.animationEventNames12.contains(var1)) {
                  return var1;
               }
            } else if (SVGAnimationEngine.animationEventNames11.contains(var1)) {
               return var1;
            }

            return null;
         }
      }

      protected String getRepeatEventName() {
         return "repeatEvent";
      }

      protected void fireTimeEvent(String var1, Calendar var2, int var3) {
         AnimationSupport.fireTimeEvent((EventTarget)SVGAnimationEngine.this.document, var1, var2, var3);
      }

      protected void toActive(float var1) {
      }

      protected void toInactive(boolean var1, boolean var2) {
      }

      protected void removeFill() {
      }

      protected void sampledAt(float var1, float var2, int var3) {
      }

      protected void sampledLastValue(int var1) {
      }

      protected TimedElement getTimedElementById(String var1) {
         return AnimationSupport.getTimedElementById(var1, SVGAnimationEngine.this.document);
      }

      protected EventTarget getEventTargetById(String var1) {
         return AnimationSupport.getEventTargetById(var1, SVGAnimationEngine.this.document);
      }

      protected EventTarget getAnimationEventTarget() {
         return null;
      }

      protected EventTarget getRootEventTarget() {
         return (EventTarget)SVGAnimationEngine.this.document;
      }

      public Element getElement() {
         return null;
      }

      public boolean isBefore(TimedElement var1) {
         return false;
      }

      protected void currentIntervalWillUpdate() {
         if (SVGAnimationEngine.this.animationTickRunnable != null) {
            SVGAnimationEngine.this.animationTickRunnable.resume();
         }

      }
   }
}
