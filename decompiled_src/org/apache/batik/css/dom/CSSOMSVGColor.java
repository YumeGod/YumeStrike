package org.apache.batik.css.dom;

import java.util.ArrayList;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.svg.ICCColor;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;
import org.w3c.dom.svg.SVGColor;
import org.w3c.dom.svg.SVGICCColor;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGNumberList;

public class CSSOMSVGColor implements SVGColor, RGBColor, SVGICCColor, SVGNumberList {
   protected ValueProvider valueProvider;
   protected ModificationHandler handler;
   protected RedComponent redComponent;
   protected GreenComponent greenComponent;
   protected BlueComponent blueComponent;
   protected ArrayList iccColors;

   public CSSOMSVGColor(ValueProvider var1) {
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
         this.iccColors = null;
         this.handler.textChanged(var1);
      }
   }

   public short getCssValueType() {
      return 3;
   }

   public short getColorType() {
      Value var1 = this.valueProvider.getValue();
      short var2 = var1.getCssValueType();
      switch (var2) {
         case 1:
            short var3 = var1.getPrimitiveType();
            switch (var3) {
               case 21:
                  if (var1.getStringValue().equalsIgnoreCase("currentcolor")) {
                     return 3;
                  }

                  return 1;
               case 25:
                  return 1;
               default:
                  throw new IllegalStateException("Found unexpected PrimitiveType:" + var3);
            }
         case 2:
            return 2;
         default:
            throw new IllegalStateException("Found unexpected CssValueType:" + var2);
      }
   }

   public RGBColor getRGBColor() {
      return this;
   }

   public RGBColor getRgbColor() {
      return this;
   }

   public void setRGBColor(String var1) {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.rgbColorChanged(var1);
      }
   }

   public SVGICCColor getICCColor() {
      return this;
   }

   public SVGICCColor getIccColor() {
      return this;
   }

   public void setRGBColorICCColor(String var1, String var2) {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.iccColors = null;
         this.handler.rgbColorICCColorChanged(var1, var2);
      }
   }

   public void setColor(short var1, String var2, String var3) {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.iccColors = null;
         this.handler.colorChanged(var1, var2, var3);
      }
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

   public String getColorProfile() {
      if (this.getColorType() != 2) {
         throw new DOMException((short)12, "");
      } else {
         Value var1 = this.valueProvider.getValue();
         return ((ICCColor)var1.item(1)).getColorProfile();
      }
   }

   public void setColorProfile(String var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.handler.colorProfileChanged(var1);
      }
   }

   public SVGNumberList getColors() {
      return this;
   }

   public int getNumberOfItems() {
      if (this.getColorType() != 2) {
         throw new DOMException((short)12, "");
      } else {
         Value var1 = this.valueProvider.getValue();
         return ((ICCColor)var1.item(1)).getNumberOfColors();
      }
   }

   public void clear() throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         this.iccColors = null;
         this.handler.colorsCleared();
      }
   }

   public SVGNumber initialize(SVGNumber var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         float var2 = var1.getValue();
         this.iccColors = new ArrayList();
         ColorNumber var3 = new ColorNumber(var2);
         this.iccColors.add(var3);
         this.handler.colorsInitialized(var2);
         return var3;
      }
   }

   public SVGNumber getItem(int var1) throws DOMException {
      if (this.getColorType() != 2) {
         throw new DOMException((short)1, "");
      } else {
         int var2 = this.getNumberOfItems();
         if (var1 >= 0 && var1 < var2) {
            if (this.iccColors == null) {
               this.iccColors = new ArrayList(var2);

               for(int var3 = this.iccColors.size(); var3 < var2; ++var3) {
                  this.iccColors.add((Object)null);
               }
            }

            Value var6 = this.valueProvider.getValue().item(1);
            float var4 = ((ICCColor)var6).getColor(var1);
            ColorNumber var5 = new ColorNumber(var4);
            this.iccColors.set(var1, var5);
            return var5;
         } else {
            throw new DOMException((short)1, "");
         }
      }
   }

   public SVGNumber insertItemBefore(SVGNumber var1, int var2) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         int var3 = this.getNumberOfItems();
         if (var2 >= 0 && var2 <= var3) {
            if (this.iccColors == null) {
               this.iccColors = new ArrayList(var3);

               for(int var4 = this.iccColors.size(); var4 < var3; ++var4) {
                  this.iccColors.add((Object)null);
               }
            }

            float var6 = var1.getValue();
            ColorNumber var5 = new ColorNumber(var6);
            this.iccColors.add(var2, var5);
            this.handler.colorInsertedBefore(var6, var2);
            return var5;
         } else {
            throw new DOMException((short)1, "");
         }
      }
   }

   public SVGNumber replaceItem(SVGNumber var1, int var2) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         int var3 = this.getNumberOfItems();
         if (var2 >= 0 && var2 < var3) {
            if (this.iccColors == null) {
               this.iccColors = new ArrayList(var3);

               for(int var4 = this.iccColors.size(); var4 < var3; ++var4) {
                  this.iccColors.add((Object)null);
               }
            }

            float var6 = var1.getValue();
            ColorNumber var5 = new ColorNumber(var6);
            this.iccColors.set(var2, var5);
            this.handler.colorReplaced(var6, var2);
            return var5;
         } else {
            throw new DOMException((short)1, "");
         }
      }
   }

   public SVGNumber removeItem(int var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         int var2 = this.getNumberOfItems();
         if (var1 >= 0 && var1 < var2) {
            ColorNumber var3 = null;
            if (this.iccColors != null) {
               var3 = (ColorNumber)this.iccColors.get(var1);
            }

            if (var3 == null) {
               Value var4 = this.valueProvider.getValue().item(1);
               var3 = new ColorNumber(((ICCColor)var4).getColor(var1));
            }

            this.handler.colorRemoved(var1);
            return var3;
         } else {
            throw new DOMException((short)1, "");
         }
      }
   }

   public SVGNumber appendItem(SVGNumber var1) throws DOMException {
      if (this.handler == null) {
         throw new DOMException((short)7, "");
      } else {
         if (this.iccColors == null) {
            int var2 = this.getNumberOfItems();
            this.iccColors = new ArrayList(var2);

            for(int var3 = 0; var3 < var2; ++var3) {
               this.iccColors.add((Object)null);
            }
         }

         float var4 = var1.getValue();
         ColorNumber var5 = new ColorNumber(var4);
         this.iccColors.add(var5);
         this.handler.colorAppend(var4);
         return var5;
      }
   }

   protected class BlueComponent extends FloatComponent {
      protected BlueComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMSVGColor.this.valueProvider.getValue().getBlue();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.blueTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.blueFloatValueChanged(var1, var2);
         }
      }
   }

   protected class GreenComponent extends FloatComponent {
      protected GreenComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMSVGColor.this.valueProvider.getValue().getGreen();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.greenTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.greenFloatValueChanged(var1, var2);
         }
      }
   }

   protected class RedComponent extends FloatComponent {
      protected RedComponent() {
         super();
      }

      protected Value getValue() {
         return CSSOMSVGColor.this.valueProvider.getValue().getRed();
      }

      public void setCssText(String var1) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.redTextChanged(var1);
         }
      }

      public void setFloatValue(short var1, float var2) throws DOMException {
         if (CSSOMSVGColor.this.handler == null) {
            throw new DOMException((short)7, "");
         } else {
            this.getValue();
            CSSOMSVGColor.this.handler.redFloatValueChanged(var1, var2);
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
         return CSSOMSVGColor.this.valueProvider.getValue().getStringValue();
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

      public void redTextChanged(String var1) throws DOMException {
         StringBuffer var2 = new StringBuffer(40);
         Value var3 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var2.append("rgb(");
               var2.append(var1);
               var2.append(',');
               var2.append(var3.getGreen().getCssText());
               var2.append(',');
               var2.append(var3.getBlue().getCssText());
               var2.append(')');
               break;
            case 2:
               var2.append("rgb(");
               var2.append(var1);
               var2.append(',');
               var2.append(var3.item(0).getGreen().getCssText());
               var2.append(',');
               var2.append(var3.item(0).getBlue().getCssText());
               var2.append(')');
               var2.append(var3.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var2.toString());
      }

      public void redFloatValueChanged(short var1, float var2) throws DOMException {
         StringBuffer var3 = new StringBuffer(40);
         Value var4 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var3.append("rgb(");
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(',');
               var3.append(var4.getGreen().getCssText());
               var3.append(',');
               var3.append(var4.getBlue().getCssText());
               var3.append(')');
               break;
            case 2:
               var3.append("rgb(");
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(',');
               var3.append(var4.item(0).getGreen().getCssText());
               var3.append(',');
               var3.append(var4.item(0).getBlue().getCssText());
               var3.append(')');
               var3.append(var4.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3.toString());
      }

      public void greenTextChanged(String var1) throws DOMException {
         StringBuffer var2 = new StringBuffer(40);
         Value var3 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var2.append("rgb(");
               var2.append(var3.getRed().getCssText());
               var2.append(',');
               var2.append(var1);
               var2.append(',');
               var2.append(var3.getBlue().getCssText());
               var2.append(')');
               break;
            case 2:
               var2.append("rgb(");
               var2.append(var3.item(0).getRed().getCssText());
               var2.append(',');
               var2.append(var1);
               var2.append(',');
               var2.append(var3.item(0).getBlue().getCssText());
               var2.append(')');
               var2.append(var3.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var2.toString());
      }

      public void greenFloatValueChanged(short var1, float var2) throws DOMException {
         StringBuffer var3 = new StringBuffer(40);
         Value var4 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var3.append("rgb(");
               var3.append(var4.getRed().getCssText());
               var3.append(',');
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(',');
               var3.append(var4.getBlue().getCssText());
               var3.append(')');
               break;
            case 2:
               var3.append("rgb(");
               var3.append(var4.item(0).getRed().getCssText());
               var3.append(',');
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(',');
               var3.append(var4.item(0).getBlue().getCssText());
               var3.append(')');
               var3.append(var4.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3.toString());
      }

      public void blueTextChanged(String var1) throws DOMException {
         StringBuffer var2 = new StringBuffer(40);
         Value var3 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var2.append("rgb(");
               var2.append(var3.getRed().getCssText());
               var2.append(',');
               var2.append(var3.getGreen().getCssText());
               var2.append(',');
               var2.append(var1);
               var2.append(')');
               break;
            case 2:
               var2.append("rgb(");
               var2.append(var3.item(0).getRed().getCssText());
               var2.append(',');
               var2.append(var3.item(0).getGreen().getCssText());
               var2.append(',');
               var2.append(var1);
               var2.append(')');
               var2.append(var3.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var2.toString());
      }

      public void blueFloatValueChanged(short var1, float var2) throws DOMException {
         StringBuffer var3 = new StringBuffer(40);
         Value var4 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 1:
               var3.append("rgb(");
               var3.append(var4.getRed().getCssText());
               var3.append(',');
               var3.append(var4.getGreen().getCssText());
               var3.append(',');
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(')');
               break;
            case 2:
               var3.append("rgb(");
               var3.append(var4.item(0).getRed().getCssText());
               var3.append(',');
               var3.append(var4.item(0).getGreen().getCssText());
               var3.append(',');
               var3.append(FloatValue.getCssText(var1, var2));
               var3.append(')');
               var3.append(var4.item(1).getCssText());
               break;
            default:
               throw new DOMException((short)7, "");
         }

         this.textChanged(var3.toString());
      }

      public void rgbColorChanged(String var1) throws DOMException {
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               var1 = var1 + this.getValue().item(1).getCssText();
            case 1:
               this.textChanged(var1);
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void rgbColorICCColorChanged(String var1, String var2) throws DOMException {
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               this.textChanged(var1 + ' ' + var2);
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorChanged(short var1, String var2, String var3) throws DOMException {
         switch (var1) {
            case 1:
               this.textChanged(var2);
               break;
            case 2:
               this.textChanged(var2 + ' ' + var3);
               break;
            case 3:
               this.textChanged("currentcolor");
               break;
            default:
               throw new DOMException((short)9, "");
         }

      }

      public void colorProfileChanged(String var1) throws DOMException {
         Value var2 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var3 = new StringBuffer(var2.item(0).getCssText());
               var3.append(" icc-color(");
               var3.append(var1);
               ICCColor var4 = (ICCColor)var2.item(1);

               for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorsCleared() throws DOMException {
         Value var1 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var2 = new StringBuffer(var1.item(0).getCssText());
               var2.append(" icc-color(");
               ICCColor var3 = (ICCColor)var1.item(1);
               var2.append(var3.getColorProfile());
               var2.append(')');
               this.textChanged(var2.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorsInitialized(float var1) throws DOMException {
         Value var2 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var3 = new StringBuffer(var2.item(0).getCssText());
               var3.append(" icc-color(");
               ICCColor var4 = (ICCColor)var2.item(1);
               var3.append(var4.getColorProfile());
               var3.append(',');
               var3.append(var1);
               var3.append(')');
               this.textChanged(var3.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorInsertedBefore(float var1, int var2) throws DOMException {
         Value var3 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var4 = new StringBuffer(var3.item(0).getCssText());
               var4.append(" icc-color(");
               ICCColor var5 = (ICCColor)var3.item(1);
               var4.append(var5.getColorProfile());

               int var6;
               for(var6 = 0; var6 < var2; ++var6) {
                  var4.append(',');
                  var4.append(var5.getColor(var6));
               }

               var4.append(',');
               var4.append(var1);

               for(var6 = var2; var6 < var5.getLength(); ++var6) {
                  var4.append(',');
                  var4.append(var5.getColor(var6));
               }

               var4.append(')');
               this.textChanged(var4.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorReplaced(float var1, int var2) throws DOMException {
         Value var3 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var4 = new StringBuffer(var3.item(0).getCssText());
               var4.append(" icc-color(");
               ICCColor var5 = (ICCColor)var3.item(1);
               var4.append(var5.getColorProfile());

               int var6;
               for(var6 = 0; var6 < var2; ++var6) {
                  var4.append(',');
                  var4.append(var5.getColor(var6));
               }

               var4.append(',');
               var4.append(var1);

               for(var6 = var2 + 1; var6 < var5.getLength(); ++var6) {
                  var4.append(',');
                  var4.append(var5.getColor(var6));
               }

               var4.append(')');
               this.textChanged(var4.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorRemoved(int var1) throws DOMException {
         Value var2 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var3 = new StringBuffer(var2.item(0).getCssText());
               var3.append(" icc-color(");
               ICCColor var4 = (ICCColor)var2.item(1);
               var3.append(var4.getColorProfile());

               int var5;
               for(var5 = 0; var5 < var1; ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               for(var5 = var1 + 1; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(')');
               this.textChanged(var3.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }

      public void colorAppend(float var1) throws DOMException {
         Value var2 = this.getValue();
         switch (CSSOMSVGColor.this.getColorType()) {
            case 2:
               StringBuffer var3 = new StringBuffer(var2.item(0).getCssText());
               var3.append(" icc-color(");
               ICCColor var4 = (ICCColor)var2.item(1);
               var3.append(var4.getColorProfile());

               for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                  var3.append(',');
                  var3.append(var4.getColor(var5));
               }

               var3.append(',');
               var3.append(var1);
               var3.append(')');
               this.textChanged(var3.toString());
               return;
            default:
               throw new DOMException((short)7, "");
         }
      }
   }

   public interface ModificationHandler {
      void textChanged(String var1) throws DOMException;

      void redTextChanged(String var1) throws DOMException;

      void redFloatValueChanged(short var1, float var2) throws DOMException;

      void greenTextChanged(String var1) throws DOMException;

      void greenFloatValueChanged(short var1, float var2) throws DOMException;

      void blueTextChanged(String var1) throws DOMException;

      void blueFloatValueChanged(short var1, float var2) throws DOMException;

      void rgbColorChanged(String var1) throws DOMException;

      void rgbColorICCColorChanged(String var1, String var2) throws DOMException;

      void colorChanged(short var1, String var2, String var3) throws DOMException;

      void colorProfileChanged(String var1) throws DOMException;

      void colorsCleared() throws DOMException;

      void colorsInitialized(float var1) throws DOMException;

      void colorInsertedBefore(float var1, int var2) throws DOMException;

      void colorReplaced(float var1, int var2) throws DOMException;

      void colorRemoved(int var1) throws DOMException;

      void colorAppend(float var1) throws DOMException;
   }

   public interface ValueProvider {
      Value getValue();
   }

   protected class ColorNumber implements SVGNumber {
      protected float value;

      public ColorNumber(float var2) {
         this.value = var2;
      }

      public float getValue() {
         if (CSSOMSVGColor.this.iccColors == null) {
            return this.value;
         } else {
            int var1 = CSSOMSVGColor.this.iccColors.indexOf(this);
            if (var1 == -1) {
               return this.value;
            } else {
               Value var2 = CSSOMSVGColor.this.valueProvider.getValue().item(1);
               return ((ICCColor)var2).getColor(var1);
            }
         }
      }

      public void setValue(float var1) {
         this.value = var1;
         if (CSSOMSVGColor.this.iccColors != null) {
            int var2 = CSSOMSVGColor.this.iccColors.indexOf(this);
            if (var2 != -1) {
               if (CSSOMSVGColor.this.handler == null) {
                  throw new DOMException((short)7, "");
               } else {
                  CSSOMSVGColor.this.handler.colorReplaced(var1, var2);
               }
            }
         }
      }
   }
}
