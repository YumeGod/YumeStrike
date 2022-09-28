package org.apache.batik.bridge;

import org.apache.batik.parser.ParseException;
import org.w3c.dom.Element;

public abstract class UnitProcessor extends org.apache.batik.parser.UnitProcessor {
   public static org.apache.batik.parser.UnitProcessor.Context createContext(BridgeContext var0, Element var1) {
      return new DefaultContext(var0, var1);
   }

   public static float svgHorizontalCoordinateToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToObjectBoundingBox(var0, var1, (short)2, var2);
   }

   public static float svgVerticalCoordinateToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToObjectBoundingBox(var0, var1, (short)1, var2);
   }

   public static float svgOtherCoordinateToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToObjectBoundingBox(var0, var1, (short)0, var2);
   }

   public static float svgHorizontalLengthToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToObjectBoundingBox(var0, var1, (short)2, var2);
   }

   public static float svgVerticalLengthToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToObjectBoundingBox(var0, var1, (short)1, var2);
   }

   public static float svgOtherLengthToObjectBoundingBox(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToObjectBoundingBox(var0, var1, (short)0, var2);
   }

   public static float svgLengthToObjectBoundingBox(String var0, String var1, short var2, org.apache.batik.parser.UnitProcessor.Context var3) {
      float var4 = svgToObjectBoundingBox(var0, var1, var2, var3);
      if (var4 < 0.0F) {
         throw new BridgeException(getBridgeContext(var3), var3.getElement(), "length.negative", new Object[]{var1, var0});
      } else {
         return var4;
      }
   }

   public static float svgToObjectBoundingBox(String var0, String var1, short var2, org.apache.batik.parser.UnitProcessor.Context var3) {
      try {
         return org.apache.batik.parser.UnitProcessor.svgToObjectBoundingBox(var0, var1, var2, var3);
      } catch (ParseException var5) {
         throw new BridgeException(getBridgeContext(var3), var3.getElement(), var5, "attribute.malformed", new Object[]{var1, var0, var5});
      }
   }

   public static float svgHorizontalLengthToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToUserSpace(var0, var1, (short)2, var2);
   }

   public static float svgVerticalLengthToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToUserSpace(var0, var1, (short)1, var2);
   }

   public static float svgOtherLengthToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgLengthToUserSpace(var0, var1, (short)0, var2);
   }

   public static float svgHorizontalCoordinateToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToUserSpace(var0, var1, (short)2, var2);
   }

   public static float svgVerticalCoordinateToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToUserSpace(var0, var1, (short)1, var2);
   }

   public static float svgOtherCoordinateToUserSpace(String var0, String var1, org.apache.batik.parser.UnitProcessor.Context var2) {
      return svgToUserSpace(var0, var1, (short)0, var2);
   }

   public static float svgLengthToUserSpace(String var0, String var1, short var2, org.apache.batik.parser.UnitProcessor.Context var3) {
      float var4 = svgToUserSpace(var0, var1, var2, var3);
      if (var4 < 0.0F) {
         throw new BridgeException(getBridgeContext(var3), var3.getElement(), "length.negative", new Object[]{var1, var0});
      } else {
         return var4;
      }
   }

   public static float svgToUserSpace(String var0, String var1, short var2, org.apache.batik.parser.UnitProcessor.Context var3) {
      try {
         return org.apache.batik.parser.UnitProcessor.svgToUserSpace(var0, var1, var2, var3);
      } catch (ParseException var5) {
         throw new BridgeException(getBridgeContext(var3), var3.getElement(), var5, "attribute.malformed", new Object[]{var1, var0, var5});
      }
   }

   protected static BridgeContext getBridgeContext(org.apache.batik.parser.UnitProcessor.Context var0) {
      return var0 instanceof DefaultContext ? ((DefaultContext)var0).ctx : null;
   }

   public static class DefaultContext implements org.apache.batik.parser.UnitProcessor.Context {
      protected Element e;
      protected BridgeContext ctx;

      public DefaultContext(BridgeContext var1, Element var2) {
         this.ctx = var1;
         this.e = var2;
      }

      public Element getElement() {
         return this.e;
      }

      public float getPixelUnitToMillimeter() {
         return this.ctx.getUserAgent().getPixelUnitToMillimeter();
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public float getFontSize() {
         return CSSUtilities.getComputedStyle(this.e, 22).getFloatValue();
      }

      public float getXHeight() {
         return 0.5F;
      }

      public float getViewportWidth() {
         return this.ctx.getViewport(this.e).getWidth();
      }

      public float getViewportHeight() {
         return this.ctx.getViewport(this.e).getHeight();
      }
   }
}
