package org.apache.batik.anim.values;

import org.apache.batik.dom.anim.AnimationTarget;

public class AnimatablePaintValue extends AnimatableColorValue {
   public static final int PAINT_NONE = 0;
   public static final int PAINT_CURRENT_COLOR = 1;
   public static final int PAINT_COLOR = 2;
   public static final int PAINT_URI = 3;
   public static final int PAINT_URI_NONE = 4;
   public static final int PAINT_URI_CURRENT_COLOR = 5;
   public static final int PAINT_URI_COLOR = 6;
   public static final int PAINT_INHERIT = 7;
   protected int paintType;
   protected String uri;

   protected AnimatablePaintValue(AnimationTarget var1) {
      super(var1);
   }

   protected AnimatablePaintValue(AnimationTarget var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
   }

   public static AnimatablePaintValue createNonePaintValue(AnimationTarget var0) {
      AnimatablePaintValue var1 = new AnimatablePaintValue(var0);
      var1.paintType = 0;
      return var1;
   }

   public static AnimatablePaintValue createCurrentColorPaintValue(AnimationTarget var0) {
      AnimatablePaintValue var1 = new AnimatablePaintValue(var0);
      var1.paintType = 1;
      return var1;
   }

   public static AnimatablePaintValue createColorPaintValue(AnimationTarget var0, float var1, float var2, float var3) {
      AnimatablePaintValue var4 = new AnimatablePaintValue(var0, var1, var2, var3);
      var4.paintType = 2;
      return var4;
   }

   public static AnimatablePaintValue createURIPaintValue(AnimationTarget var0, String var1) {
      AnimatablePaintValue var2 = new AnimatablePaintValue(var0);
      var2.uri = var1;
      var2.paintType = 3;
      return var2;
   }

   public static AnimatablePaintValue createURINonePaintValue(AnimationTarget var0, String var1) {
      AnimatablePaintValue var2 = new AnimatablePaintValue(var0);
      var2.uri = var1;
      var2.paintType = 4;
      return var2;
   }

   public static AnimatablePaintValue createURICurrentColorPaintValue(AnimationTarget var0, String var1) {
      AnimatablePaintValue var2 = new AnimatablePaintValue(var0);
      var2.uri = var1;
      var2.paintType = 5;
      return var2;
   }

   public static AnimatablePaintValue createURIColorPaintValue(AnimationTarget var0, String var1, float var2, float var3, float var4) {
      AnimatablePaintValue var5 = new AnimatablePaintValue(var0, var2, var3, var4);
      var5.uri = var1;
      var5.paintType = 6;
      return var5;
   }

   public static AnimatablePaintValue createInheritPaintValue(AnimationTarget var0) {
      AnimatablePaintValue var1 = new AnimatablePaintValue(var0);
      var1.paintType = 7;
      return var1;
   }

   public AnimatableValue interpolate(AnimatableValue var1, AnimatableValue var2, float var3, AnimatableValue var4, int var5) {
      AnimatablePaintValue var6;
      if (var1 == null) {
         var6 = new AnimatablePaintValue(this.target);
      } else {
         var6 = (AnimatablePaintValue)var1;
      }

      if (this.paintType == 2) {
         boolean var7 = true;
         AnimatablePaintValue var8;
         if (var2 != null) {
            var8 = (AnimatablePaintValue)var2;
            var7 = var8.paintType == 2;
         }

         if (var4 != null) {
            var8 = (AnimatablePaintValue)var4;
            var7 = var7 && var8.paintType == 2;
         }

         if (var7) {
            var6.paintType = 2;
            return super.interpolate(var6, var2, var3, var4, var5);
         }
      }

      float var9;
      float var10;
      float var11;
      int var13;
      String var14;
      if (var2 != null && (double)var3 >= 0.5) {
         AnimatablePaintValue var12 = (AnimatablePaintValue)var2;
         var13 = var12.paintType;
         var14 = var12.uri;
         var9 = var12.red;
         var10 = var12.green;
         var11 = var12.blue;
      } else {
         var13 = this.paintType;
         var14 = this.uri;
         var9 = this.red;
         var10 = this.green;
         var11 = this.blue;
      }

      if (var6.paintType != var13 || var6.uri == null || !var6.uri.equals(var14) || var6.red != var9 || var6.green != var10 || var6.blue != var11) {
         var6.paintType = var13;
         var6.uri = var14;
         var6.red = var9;
         var6.green = var10;
         var6.blue = var11;
         var6.hasChanged = true;
      }

      return var6;
   }

   public int getPaintType() {
      return this.paintType;
   }

   public String getURI() {
      return this.uri;
   }

   public boolean canPace() {
      return false;
   }

   public float distanceTo(AnimatableValue var1) {
      return 0.0F;
   }

   public AnimatableValue getZeroValue() {
      return createColorPaintValue(this.target, 0.0F, 0.0F, 0.0F);
   }

   public String getCssText() {
      switch (this.paintType) {
         case 0:
            return "none";
         case 1:
            return "currentColor";
         case 2:
            return super.getCssText();
         case 3:
            return "url(" + this.uri + ")";
         case 4:
            return "url(" + this.uri + ") none";
         case 5:
            return "url(" + this.uri + ") currentColor";
         case 6:
            return "url(" + this.uri + ") " + super.getCssText();
         default:
            return "inherit";
      }
   }
}
