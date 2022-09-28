package org.apache.fop.layoutmgr;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.CondLengthProperty;
import org.apache.fop.traits.MinOptMax;

public class BorderElement extends BorderOrPaddingElement {
   public BorderElement(Position position, CondLengthProperty condLength, RelSide side, boolean isFirst, boolean isLast, PercentBaseContext context) {
      super(position, condLength, side, isFirst, isLast, context);
   }

   public void notifyLayoutManager(MinOptMax effectiveLength) {
      LayoutManager lm = this.getOriginatingLayoutManager();
      if (lm instanceof ConditionalElementListener) {
         ((ConditionalElementListener)lm).notifyBorder(this.getSide(), effectiveLength);
      } else {
         log.warn("Cannot notify LM. It does not implement ConditionalElementListener: " + lm.getClass().getName());
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Border[");
      sb.append(super.toString());
      sb.append("]");
      return sb.toString();
   }
}
