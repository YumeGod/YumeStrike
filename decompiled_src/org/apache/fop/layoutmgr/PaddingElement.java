package org.apache.fop.layoutmgr;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.CondLengthProperty;
import org.apache.fop.traits.MinOptMax;

public class PaddingElement extends BorderOrPaddingElement {
   public PaddingElement(Position position, CondLengthProperty condLength, RelSide side, boolean isFirst, boolean isLast, PercentBaseContext context) {
      super(position, condLength, side, isFirst, isLast, context);
   }

   public void notifyLayoutManager(MinOptMax effectiveLength) {
      LayoutManager lm = this.getOriginatingLayoutManager();
      if (lm instanceof ConditionalElementListener) {
         ((ConditionalElementListener)lm).notifyPadding(this.getSide(), effectiveLength);
      } else {
         log.warn("Cannot notify LM. It does not implement ConditionalElementListene: " + lm.getClass().getName());
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Padding[");
      sb.append(super.toString());
      sb.append("]");
      return sb.toString();
   }
}
