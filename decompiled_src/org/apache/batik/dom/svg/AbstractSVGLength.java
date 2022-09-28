package org.apache.batik.dom.svg;

import org.apache.batik.parser.LengthParser;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.UnitProcessor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGLength;

public abstract class AbstractSVGLength implements SVGLength {
   public static final short HORIZONTAL_LENGTH = 2;
   public static final short VERTICAL_LENGTH = 1;
   public static final short OTHER_LENGTH = 0;
   protected short unitType;
   protected float value;
   protected short direction;
   protected UnitProcessor.Context context = new DefaultContext();
   protected static final String[] UNITS = new String[]{"", "", "%", "em", "ex", "px", "cm", "mm", "in", "pt", "pc"};

   protected abstract SVGOMElement getAssociatedElement();

   public AbstractSVGLength(short var1) {
      this.direction = var1;
      this.value = 0.0F;
      this.unitType = 1;
   }

   public short getUnitType() {
      this.revalidate();
      return this.unitType;
   }

   public float getValue() {
      this.revalidate();

      try {
         return UnitProcessor.svgToUserSpace(this.value, this.unitType, this.direction, this.context);
      } catch (IllegalArgumentException var2) {
         return 0.0F;
      }
   }

   public void setValue(float var1) throws DOMException {
      this.value = UnitProcessor.userSpaceToSVG(var1, this.unitType, this.direction, this.context);
      this.reset();
   }

   public float getValueInSpecifiedUnits() {
      this.revalidate();
      return this.value;
   }

   public void setValueInSpecifiedUnits(float var1) throws DOMException {
      this.revalidate();
      this.value = var1;
      this.reset();
   }

   public String getValueAsString() {
      this.revalidate();
      return this.unitType == 0 ? "" : Float.toString(this.value) + UNITS[this.unitType];
   }

   public void setValueAsString(String var1) throws DOMException {
      this.parse(var1);
      this.reset();
   }

   public void newValueSpecifiedUnits(short var1, float var2) {
      this.unitType = var1;
      this.value = var2;
      this.reset();
   }

   public void convertToSpecifiedUnits(short var1) {
      float var2 = this.getValue();
      this.unitType = var1;
      this.setValue(var2);
   }

   protected void reset() {
   }

   protected void revalidate() {
   }

   protected void parse(String var1) {
      try {
         LengthParser var2 = new LengthParser();
         UnitProcessor.UnitResolver var3 = new UnitProcessor.UnitResolver();
         var2.setLengthHandler(var3);
         var2.parse(var1);
         this.unitType = var3.unit;
         this.value = var3.value;
      } catch (ParseException var4) {
         this.unitType = 0;
         this.value = 0.0F;
      }

   }

   protected class DefaultContext implements UnitProcessor.Context {
      public Element getElement() {
         return AbstractSVGLength.this.getAssociatedElement();
      }

      public float getPixelUnitToMillimeter() {
         return AbstractSVGLength.this.getAssociatedElement().getSVGContext().getPixelUnitToMillimeter();
      }

      public float getPixelToMM() {
         return this.getPixelUnitToMillimeter();
      }

      public float getFontSize() {
         return AbstractSVGLength.this.getAssociatedElement().getSVGContext().getFontSize();
      }

      public float getXHeight() {
         return 0.5F;
      }

      public float getViewportWidth() {
         return AbstractSVGLength.this.getAssociatedElement().getSVGContext().getViewportWidth();
      }

      public float getViewportHeight() {
         return AbstractSVGLength.this.getAssociatedElement().getSVGContext().getViewportHeight();
      }
   }
}
