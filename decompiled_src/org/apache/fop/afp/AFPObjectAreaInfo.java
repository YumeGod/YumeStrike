package org.apache.fop.afp;

public class AFPObjectAreaInfo {
   private int x;
   private int y;
   private int width;
   private int height;
   private int widthRes;
   private int heightRes;
   private int rotation = 0;

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public void setWidthRes(int widthRes) {
      this.widthRes = widthRes;
   }

   public void setHeightRes(int heightRes) {
      this.heightRes = heightRes;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidthRes() {
      return this.widthRes;
   }

   public int getHeightRes() {
      return this.heightRes;
   }

   public int getRotation() {
      return this.rotation;
   }

   public void setRotation(int rotation) {
      this.rotation = rotation;
   }

   public String toString() {
      return "x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ", widthRes=" + this.widthRes + ", heightRes=" + this.heightRes + ", rotation=" + this.rotation;
   }
}
