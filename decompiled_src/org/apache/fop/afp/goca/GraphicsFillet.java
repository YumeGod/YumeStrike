package org.apache.fop.afp.goca;

public final class GraphicsFillet extends AbstractGraphicsCoord {
   public GraphicsFillet(int[] coords, boolean relative) {
      super(coords, relative);
   }

   byte getOrderCode() {
      return (byte)(this.isRelative() ? -123 : -59);
   }
}
