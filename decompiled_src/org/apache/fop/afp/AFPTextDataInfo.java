package org.apache.fop.afp;

import java.awt.Color;

public class AFPTextDataInfo {
   private int fontReference;
   private int x;
   private int y;
   private Color color;
   private int variableSpaceCharacterIncrement;
   private int interCharacterAdjustment;
   private int rotation;
   private String textEncoding;
   private String textString;

   public int getFontReference() {
      return this.fontReference;
   }

   public void setFontReference(int fontReference) {
      this.fontReference = fontReference;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public int getVariableSpaceCharacterIncrement() {
      return this.variableSpaceCharacterIncrement;
   }

   public void setVariableSpaceCharacterIncrement(int variableSpaceCharacterIncrement) {
      this.variableSpaceCharacterIncrement = variableSpaceCharacterIncrement;
   }

   public int getInterCharacterAdjustment() {
      return this.interCharacterAdjustment;
   }

   public void setInterCharacterAdjustment(int interCharacterAdjustment) {
      this.interCharacterAdjustment = interCharacterAdjustment;
   }

   public void setRotation(int rotation) {
      this.rotation = rotation;
   }

   public int getRotation() {
      return this.rotation;
   }

   public void setEncoding(String textEncoding) {
      this.textEncoding = textEncoding;
   }

   public String getEncoding() {
      return this.textEncoding;
   }

   public void setString(String textString) {
      this.textString = textString;
   }

   public String getString() {
      return this.textString;
   }

   public String toString() {
      return "TextDataInfo{fontReference=" + this.fontReference + ", x=" + this.x + ", y=" + this.y + ", color=" + this.color + ", vsci=" + this.variableSpaceCharacterIncrement + ", ica=" + this.interCharacterAdjustment + ", orientation=" + this.rotation + ", textString=" + this.textString + ", textEncoding=" + this.textEncoding + "}";
   }
}
