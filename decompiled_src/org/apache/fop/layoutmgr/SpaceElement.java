package org.apache.fop.layoutmgr;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.traits.MinOptMax;

public class SpaceElement extends UnresolvedListElementWithLength {
   private int precedence;

   public SpaceElement(Position position, SpaceProperty space, RelSide side, boolean isFirst, boolean isLast, PercentBaseContext context) {
      super(position, space.getSpace().getLengthRange().toMinOptMax(context), side, space.isDiscard(), isFirst, isLast);
      int en = space.getSpace().getPrecedence().getEnum();
      if (en == 53) {
         this.precedence = Integer.MAX_VALUE;
      } else {
         this.precedence = space.getSpace().getPrecedence().getNumber().intValue();
      }

   }

   public boolean isForcing() {
      return this.precedence == Integer.MAX_VALUE;
   }

   public int getPrecedence() {
      return this.precedence;
   }

   public void notifyLayoutManager(MinOptMax effectiveLength) {
      LayoutManager lm = this.getOriginatingLayoutManager();
      if (lm instanceof ConditionalElementListener) {
         ((ConditionalElementListener)lm).notifySpace(this.getSide(), effectiveLength);
      } else {
         log.warn("Cannot notify LM. It does not implement ConditionalElementListener:" + lm.getClass().getName());
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Space[");
      sb.append(super.toString());
      sb.append(", precedence=");
      if (this.isForcing()) {
         sb.append("forcing");
      } else {
         sb.append(this.getPrecedence());
      }

      sb.append("]");
      return sb.toString();
   }
}
