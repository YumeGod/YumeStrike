package org.apache.fop.afp.goca;

public class GraphicsSetCurrentPosition extends AbstractGraphicsCoord {
   public GraphicsSetCurrentPosition(int[] coords) {
      super(coords);
   }

   protected byte getOrderCode() {
      return 33;
   }
}
