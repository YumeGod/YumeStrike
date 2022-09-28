package org.apache.fop.afp.goca;

public class GraphicsSetArcParameters extends AbstractGraphicsCoord {
   public GraphicsSetArcParameters(int xmaj, int ymin, int xmin, int ymaj) {
      super(xmaj, ymin, xmin, ymaj);
   }

   protected byte getOrderCode() {
      return 34;
   }

   public String toString() {
      return this.getName() + "{xmaj=" + this.coords[0] + ",ymin=" + this.coords[1] + ",xmin=" + this.coords[2] + ",ymaj=" + this.coords[3] + "}";
   }
}
