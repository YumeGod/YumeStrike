package org.apache.fop.afp.modca;

public abstract class AbstractDescriptor extends AbstractTripletStructuredObject {
   protected int width = 0;
   protected int height = 0;
   protected int widthRes = 0;
   protected int heightRes = 0;

   public AbstractDescriptor() {
   }

   public AbstractDescriptor(int width, int height, int widthRes, int heightRes) {
      this.width = width;
      this.height = height;
      this.widthRes = widthRes;
      this.heightRes = heightRes;
   }

   public String toString() {
      return "width=" + this.width + ", height=" + this.height + ", widthRes=" + this.widthRes + ", heightRes=" + this.heightRes;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }
}
