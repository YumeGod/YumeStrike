package org.apache.fop.render.txt.border;

import org.apache.fop.render.txt.TXTState;

public class BorderManager {
   private AbstractBorderElement[][] borderInfo;
   private int width;
   private int height;
   private int startX;
   private int startY;
   private TXTState state;

   public BorderManager(int pageWidth, int pageHeight, TXTState state) {
      this.state = state;
      this.borderInfo = new AbstractBorderElement[pageHeight][pageWidth];
   }

   public void addBorderElement(int x, int y, int style, int type) {
      AbstractBorderElement be = null;
      if (style != 133 && style != 37) {
         if (style == 36) {
            be = new DottedBorderElement();
         } else {
            if (style != 31) {
               return;
            }

            be = new DashedBorderElement(type);
         }
      } else {
         be = new SolidAndDoubleBorderElement(style, type);
      }

      ((AbstractBorderElement)be).transformElement(this.state);
      if (this.borderInfo[y][x] != null) {
         this.borderInfo[y][x] = this.borderInfo[y][x].merge((AbstractBorderElement)be);
      } else {
         this.borderInfo[y][x] = (AbstractBorderElement)be;
      }

   }

   public Character getCharacter(int x, int y) {
      Character c = null;
      if (this.borderInfo[y][x] != null) {
         c = new Character(this.borderInfo[y][x].convert2Char());
      }

      return c;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getStartX() {
      return this.startX;
   }

   public void setStartX(int startX) {
      this.startX = startX;
   }

   public int getStartY() {
      return this.startY;
   }

   public void setStartY(int startY) {
      this.startY = startY;
   }
}
