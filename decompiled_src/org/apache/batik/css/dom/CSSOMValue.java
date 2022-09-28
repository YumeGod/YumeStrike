package org.apache.batik.css.dom;

import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

public class CSSOMValue implements CSSPrimitiveValue, CSSValueList, Counter, Rect, RGBColor {
   protected ValueProvider valueProvider;
   protected ModificationHandler handler;
   protected LeftComponent leftComponent;
   protected RightComponent rightComponent;
   protected BottomComponent bottomComponent;
   protected TopComponent topComponent;
   protected RedComponent redComponent;
   protected GreenComponent greenComponent;
   protected BlueComponent blueComponent;
   protected CSSValue[] items;

   public CSSOMValue(ValueProvider var1) {
      this.valueProvider = var1;
   }

   public void setModificationHandler(ModificationHandler var1) {
      this.handler = var1;
   }

   public String getCssText() {
      return this.valueProvider.getValue().getCssText();
   }

   public void setCssText(String var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.textChanged(var1);
      }
   }

   public short getCssValueType() {
      return this.valueProvider.getValue().getCssValueType();
   }

   public short getPrimitiveType() {
      return this.valueProvider.getValue().getPrimitiveType();
   }

   public void setFloatValue(short var1, float var2) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.floatValueChanged(var1, var2);
      }
   }

   public float getFloatValue(short var1) throws DOMException {
      return convertFloatValue(var1, this.valueProvider.getValue());
   }

   public static float convertFloatValue(short var0, Value var1) {
      switch (var0) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 18:
            if (var1.getPrimitiveType() == var0) {
               return var1.getFloatValue();
            }
         default:
            throw new DOMException((short)15, "");
         case 6:
            return toCentimeters(var1);
         case 7:
            return toMillimeters(var1);
         case 8:
            return toInches(var1);
         case 9:
            return toPoints(var1);
         case 10:
            return toPicas(var1);
         case 11:
            return toDegrees(var1);
         case 12:
            return toRadians(var1);
         case 13:
            return toGradians(var1);
         case 14:
            return toMilliseconds(var1);
         case 15:
            return toSeconds(var1);
         case 16:
            return toHertz(var1);
         case 17:
            return tokHertz(var1);
      }
   }

   protected static float toCentimeters(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 6:
            return var0.getFloatValue();
         case 7:
            return var0.getFloatValue() / 10.0F;
         case 8:
            return var0.getFloatValue() * 2.54F;
         case 9:
            return var0.getFloatValue() * 2.54F / 72.0F;
         case 10:
            return var0.getFloatValue() * 2.54F / 6.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toInches(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 6:
            return var0.getFloatValue() / 2.54F;
         case 7:
            return var0.getFloatValue() / 25.4F;
         case 8:
            return var0.getFloatValue();
         case 9:
            return var0.getFloatValue() / 72.0F;
         case 10:
            return var0.getFloatValue() / 6.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toMillimeters(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 6:
            return var0.getFloatValue() * 10.0F;
         case 7:
            return var0.getFloatValue();
         case 8:
            return var0.getFloatValue() * 25.4F;
         case 9:
            return var0.getFloatValue() * 25.4F / 72.0F;
         case 10:
            return var0.getFloatValue() * 25.4F / 6.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toPoints(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 6:
            return var0.getFloatValue() * 72.0F / 2.54F;
         case 7:
            return var0.getFloatValue() * 72.0F / 25.4F;
         case 8:
            return var0.getFloatValue() * 72.0F;
         case 9:
            return var0.getFloatValue();
         case 10:
            return var0.getFloatValue() * 12.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toPicas(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 6:
            return var0.getFloatValue() * 6.0F / 2.54F;
         case 7:
            return var0.getFloatValue() * 6.0F / 25.4F;
         case 8:
            return var0.getFloatValue() * 6.0F;
         case 9:
            return var0.getFloatValue() / 12.0F;
         case 10:
            return var0.getFloatValue();
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toDegrees(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 11:
            return var0.getFloatValue();
         case 12:
            return (float)Math.toDegrees((double)var0.getFloatValue());
         case 13:
            return var0.getFloatValue() * 9.0F / 5.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toRadians(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 11:
            return var0.getFloatValue() * 5.0F / 9.0F;
         case 12:
            return var0.getFloatValue();
         case 13:
            return (float)((double)(var0.getFloatValue() * 100.0F) / Math.PI);
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toGradians(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 11:
            return (float)((double)var0.getFloatValue() * Math.PI / 180.0);
         case 12:
            return (float)((double)var0.getFloatValue() * Math.PI / 100.0);
         case 13:
            return var0.getFloatValue();
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toMilliseconds(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 14:
            return var0.getFloatValue();
         case 15:
            return var0.getFloatValue() * 1000.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toSeconds(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 14:
            return var0.getFloatValue() / 1000.0F;
         case 15:
            return var0.getFloatValue();
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float toHertz(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 16:
            return var0.getFloatValue();
         case 17:
            return var0.getFloatValue() / 1000.0F;
         default:
            throw new DOMException((short)15, "");
      }
   }

   protected static float tokHertz(Value var0) {
      switch (var0.getPrimitiveType()) {
         case 16:
            return var0.getFloatValue() * 1000.0F;
         case 17:
            return var0.getFloatValue();
         default:
            throw new DOMException((short)15, "");
      }
   }

   public void setStringValue(short var1, String var2) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.stringValueChanged(var1, var2);
      }
   }

   public String getStringValue() throws DOMException {
      return this.valueProvider.getValue().getStringValue();
   }

   public Counter getCounterValue() throws DOMException {
      return this;
   }

   public Rect getRectValue() throws DOMException {
      return this;
   }

   public RGBColor getRGBColorValue() throws DOMException {
      return this;
   }

   public int getLength() {
      return this.valueProvider.getValue().getLength();
   }

   public CSSValue item(int var1) {
      int var2 = this.valueProvider.getValue().getLength();
      if (var1 >= 0 && var1 < var2) {
         if (this.items == null) {
            this.items = new CSSValue[this.valueProvider.getValue().getLength()];
         } else if (this.items.length < var2) {
            CSSValue[] var3 = new CSSValue[var2];
            System.arraycopy(this.items, 0, var3, 0, this.items.length);
            this.items = var3;
         }

         Object var4 = this.items[var1];
         if (var4 == null) {
            this.items[var1] = (CSSValue)(var4 = new ListComponent(var1));
         }

         return (CSSValue)var4;
      } else {
         return null;
      }
   }

   public String getIdentifier() {
      return this.valueProvider.getValue().getIdentifier();
   }

   public String getListStyle() {
      return this.valueProvider.getValue().getListStyle();
   }

   public String getSeparator() {
      return this.valueProvider.getValue().getSeparator();
   }

   public CSSPrimitiveValue getTop() {
      this.valueProvider.getValue().getTop();
      if (this.topComponent == null) {
         this.topComponent = new TopComponent();
      }

      return this.topComponent;
   }

   public CSSPrimitiveValue getRight() {
      this.valueProvider.getValue().getRight();
      if (this.rightComponent == null) {
         this.rightComponent = new RightComponent();
      }

      return this.rightComponent;
   }

   public CSSPrimitiveValue getBottom() {
      this.valueProvider.getValue().getBottom();
      if (this.bottomComponent == null) {
         this.bottomComponent = new BottomComponent();
      }

      return this.bottomComponent;
   }

   public CSSPrimitiveValue getLeft() {
      this.valueProvider.getValue().getLeft();
      if (this.leftComponent == null) {
         this.leftComponent = new LeftComponent();
      }

      return this.leftComponent;
   }

   public CSSPrimitiveValue getRed() {
      this.valueProvider.getValue().getRed();
      if (this.redComponent == null) {
         this.redComponent = new RedComponent();
      }

      return this.redComponent;
   }

   public CSSPrimitiveValue getGreen() {
      this.valueProvider.getValue().getGreen();
      if (this.greenComponent == null) {
         this.greenComponent = new GreenComponent();
      }

      return this.greenComponent;
   }

   public CSSPrimitiveValue getBlue() {
      this.valueProvider.getValue().getBlue();
      if (this.blueComponent == null) {
         this.blueComponent = new BlueComponent();
      }

      return this.blueComponent;
   }

   protected class ListComponent extends AbstractComponent {
      protected int index;

      public ListComponent(int var2) {
         super();
         this.index = var2;
      }

      protected Value getValue() {
         if (this.index >= CSSOMValue.this.valueProvider.getValue().getLength()) {
            throw new DOMException((short)7, "");
         } else {
            return CSSOMValue.this.valueProvider.getValue().item(this.index);
         }
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.listTextChanged(this.index, var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.listFloatValueChanged(this.index, var1, var2);
         }
      }

      public void setStringValue(short var1, String var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.listStringValueChanged(this.index, var1, var2);
         }
      }
   }

   protected class BlueComponent extends FloatComponent {
      protected BlueComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getBlue();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.blueTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.blueFloatValueChanged(var1, var2);
         }
      }
   }

   protected class GreenComponent extends FloatComponent {
      protected GreenComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getGreen();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.greenTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.greenFloatValueChanged(var1, var2);
         }
      }
   }

   protected class RedComponent extends FloatComponent {
      protected RedComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getRed();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.redTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.redFloatValueChanged(var1, var2);
         }
      }
   }

   protected class BottomComponent extends FloatComponent {
      protected BottomComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getBottom();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.bottomTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.bottomFloatValueChanged(var1, var2);
         }
      }
   }

   protected class RightComponent extends FloatComponent {
      protected RightComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getRight();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.rightTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.rightFloatValueChanged(var1, var2);
         }
      }
   }

   protected class TopComponent extends FloatComponent {
      protected TopComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getTop();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.topTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.topFloatValueChanged(var1, var2);
         }
      }
   }

   protected class LeftComponent extends FloatComponent {
      protected LeftComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMValue.this.valueProvider.getValue().getLeft();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.leftTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMValue.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMValue.this.handler.leftFloatValueChanged(var1, var2);
         }
      }
   }

   protected abstract class FloatComponent extends AbstractComponent {
      protected FloatComponent() {
         super();
      }

      public void setStringValue(short var1, String var2) throws DOMException {
         throw new DOMException((short)15, "");
      }
   }

   protected abstract class AbstractComponent implements CSSPrimitiveValue {
      protected abstract Value getValue();

      public String getCssText() {
         return this.getValue().getCssText();
      }

      public short getCssValueType() {
         return this.getValue().getCssValueType();
      }

      public short getPrimitiveType() {
         return this.getValue().getPrimitiveType();
      }

      public float getFloatValue(short var1) throws DOMException {
         return CSSOMValue.convertFloatValue(var1, this.getValue());
      }

      public String getStringValue() throws DOMException {
         return CSSOMValue.this.valueProvider.getValue().getStringValue();
      }

      public Counter getCounterValue() throws DOMException {
         throw new DOMException((short)15, "");
      }

      public Rect getRectValue() throws DOMException {
         throw new DOMException((short)15, "");
      }

      public RGBColor getRGBColorValue() throws DOMException {
         throw new DOMException((short)15, "");
      }

      public int getLength() {
         throw new DOMException((short)15, "");
      }

      public CSSValue item(int var1) {
         throw new DOMException((short)15, "");
      }
   }

   public abstract class AbstractModificationHandler implements ModificationHandler {
      protected abstract Value getValue();

      public void floatValueChanged(short var1, float var2) throws DOMException {
         this.textChanged(FloatValue.getCssText(var1, var2));
      }

      public void stringValueChanged(short var1, String var2) throws DOMException {
         this.textChanged(StringValue.getCssText(var1, var2));
      }

      public void leftTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rect(" + var2.getTop().getCssText() + ", " + var2.getRight().getCssText() + ", " + var2.getBottom().getCssText() + ", " + var1 + ')';
         this.textChanged(var1);
      }

      public void leftFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rect(" + var3.getTop().getCssText() + ", " + var3.getRight().getCssText() + ", " + var3.getBottom().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ')';
         this.textChanged(var4);
      }

      public void topTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rect(" + var1 + ", " + var2.getRight().getCssText() + ", " + var2.getBottom().getCssText() + ", " + var2.getLeft().getCssText() + ')';
         this.textChanged(var1);
      }

      public void topFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rect(" + FloatValue.getCssText(var1, var2) + ", " + var3.getRight().getCssText() + ", " + var3.getBottom().getCssText() + ", " + var3.getLeft().getCssText() + ')';
         this.textChanged(var4);
      }

      public void rightTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rect(" + var2.getTop().getCssText() + ", " + var1 + ", " + var2.getBottom().getCssText() + ", " + var2.getLeft().getCssText() + ')';
         this.textChanged(var1);
      }

      public void rightFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rect(" + var3.getTop().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + var3.getBottom().getCssText() + ", " + var3.getLeft().getCssText() + ')';
         this.textChanged(var4);
      }

      public void bottomTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rect(" + var2.getTop().getCssText() + ", " + var2.getRight().getCssText() + ", " + var1 + ", " + var2.getLeft().getCssText() + ')';
         this.textChanged(var1);
      }

      public void bottomFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rect(" + var3.getTop().getCssText() + ", " + var3.getRight().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + var3.getLeft().getCssText() + ')';
         this.textChanged(var4);
      }

      public void redTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rgb(" + var1 + ", " + var2.getGreen().getCssText() + ", " + var2.getBlue().getCssText() + ')';
         this.textChanged(var1);
      }

      public void redFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rgb(" + FloatValue.getCssText(var1, var2) + ", " + var3.getGreen().getCssText() + ", " + var3.getBlue().getCssText() + ')';
         this.textChanged(var4);
      }

      public void greenTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rgb(" + var2.getRed().getCssText() + ", " + var1 + ", " + var2.getBlue().getCssText() + ')';
         this.textChanged(var1);
      }

      public void greenFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rgb(" + var3.getRed().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ", " + var3.getBlue().getCssText() + ')';
         this.textChanged(var4);
      }

      public void blueTextChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         var1 = "rgb(" + var2.getRed().getCssText() + ", " + var2.getGreen().getCssText() + ", " + var1 + ')';
         this.textChanged(var1);
      }

      public void blueFloatValueChanged(short var1, float var2) throws DOMException {
         Value var3 = this.getValue();
         String var4 = "rgb(" + var3.getRed().getCssText() + ", " + var3.getGreen().getCssText() + ", " + FloatValue.getCssText(var1, var2) + ')';
         this.textChanged(var4);
      }

      public void listTextChanged(int var1, String var2) throws DOMException {
         ListValue var3 = (ListValue)this.getValue();
         int var4 = var3.getLength();
         StringBuffer var5 = new StringBuffer(var4 * 8);

         int var6;
         for(var6 = 0; var6 < var1; ++var6) {
            var5.append(var3.item(var6).getCssText());
            var5.append(var3.getSeparatorChar());
         }

         var5.append(var2);

         for(var6 = var1 + 1; var6 < var4; ++var6) {
            var5.append(var3.getSeparatorChar());
            var5.append(var3.item(var6).getCssText());
         }

         var2 = var5.toString();
         this.textChanged(var2);
      }

      public void listFloatValueChanged(int var1, short var2, float var3) throws DOMException {
         ListValue var4 = (ListValue)this.getValue();
         int var5 = var4.getLength();
         StringBuffer var6 = new StringBuffer(var5 * 8);

         int var7;
         for(var7 = 0; var7 < var1; ++var7) {
            var6.append(var4.item(var7).getCssText());
            var6.append(var4.getSeparatorChar());
         }

         var6.append(FloatValue.getCssText(var2, var3));

         for(var7 = var1 + 1; var7 < var5; ++var7) {
            var6.append(var4.getSeparatorChar());
            var6.append(var4.item(var7).getCssText());
         }

         this.textChanged(var6.toString());
      }

      public void listStringValueChanged(int var1, short var2, String var3) throws DOMException {
         ListValue var4 = (ListValue)this.getValue();
         int var5 = var4.getLength();
         StringBuffer var6 = new StringBuffer(var5 * 8);

         int var7;
         for(var7 = 0; var7 < var1; ++var7) {
            var6.append(var4.item(var7).getCssText());
            var6.append(var4.getSeparatorChar());
         }

         var6.append(StringValue.getCssText(var2, var3));

         for(var7 = var1 + 1; var7 < var5; ++var7) {
            var6.append(var4.getSeparatorChar());
            var6.append(var4.item(var7).getCssText());
         }

         this.textChanged(var6.toString());
      }
   }

   public interface ModificationHandler {
      void textChanged(String var1) throws DOMException;

      void floatValueChanged(short var1, float var2) throws DOMException;

      void stringValueChanged(short var1, String var2) throws DOMException;

      void leftTextChanged(String var1) throws DOMException;

      void leftFloatValueChanged(short var1, float var2) throws DOMException;

      void topTextChanged(String var1) throws DOMException;

      void topFloatValueChanged(short var1, float var2) throws DOMException;

      void rightTextChanged(String var1) throws DOMException;

      void rightFloatValueChanged(short var1, float var2) throws DOMException;

      void bottomTextChanged(String var1) throws DOMException;

      void bottomFloatValueChanged(short var1, float var2) throws DOMException;

      void redTextChanged(String var1) throws DOMException;

      void redFloatValueChanged(short var1, float var2) throws DOMException;

      void greenTextChanged(String var1) throws DOMException;

      void greenFloatValueChanged(short var1, float var2) throws DOMException;

      void blueTextChanged(String var1) throws DOMException;

      void blueFloatValueChanged(short var1, float var2) throws DOMException;

      void listTextChanged(int var1, String var2) throws DOMException;

      void listFloatValueChanged(int var1, short var2, float var3) throws DOMException;

      void listStringValueChanged(int var1, short var2, String var3) throws DOMException;
   }

   public interface ValueProvider {
      Value getValue();
   }
}
