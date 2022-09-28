package org.apache.fop.render.intermediate;

import java.awt.Color;

public class IFState {
   private IFState parent;
   private String fontFamily;
   private int fontSize;
   private String fontStyle;
   private int fontWeight;
   private String fontVariant;
   private boolean fontChanged = true;
   private Color textColor;

   private IFState() {
   }

   private IFState(IFState parent) {
      this.parent = parent;
      this.fontFamily = parent.fontFamily;
      this.fontSize = parent.fontSize;
      this.fontStyle = parent.fontStyle;
      this.fontWeight = parent.fontWeight;
      this.fontVariant = parent.fontVariant;
      this.textColor = parent.textColor;
   }

   public static IFState create() {
      return new IFState();
   }

   public IFState push() {
      return new IFState(this);
   }

   public IFState pop() {
      return this.parent;
   }

   public boolean isFontChanged() {
      return this.fontChanged;
   }

   public void resetFontChanged() {
      this.fontChanged = false;
   }

   public String getFontFamily() {
      return this.fontFamily;
   }

   public void setFontFamily(String family) {
      if (!family.equals(this.fontFamily)) {
         this.fontChanged = true;
      }

      this.fontFamily = family;
   }

   public int getFontSize() {
      return this.fontSize;
   }

   public void setFontSize(int size) {
      if (size != this.fontSize) {
         this.fontChanged = true;
      }

      this.fontSize = size;
   }

   public String getFontStyle() {
      return this.fontStyle;
   }

   public void setFontStyle(String style) {
      if (!style.equals(this.fontStyle)) {
         this.fontChanged = true;
      }

      this.fontStyle = style;
   }

   public int getFontWeight() {
      return this.fontWeight;
   }

   public void setFontWeight(int weight) {
      if (weight != this.fontWeight) {
         this.fontChanged = true;
      }

      this.fontWeight = weight;
   }

   public String getFontVariant() {
      return this.fontVariant;
   }

   public void setFontVariant(String variant) {
      if (!variant.equals(this.fontVariant)) {
         this.fontChanged = true;
      }

      this.fontVariant = variant;
   }

   public Color getTextColor() {
      return this.textColor;
   }

   public void setTextColor(Color color) {
      if (!color.equals(this.textColor)) {
         this.fontChanged = true;
      }

      this.textColor = color;
   }
}
