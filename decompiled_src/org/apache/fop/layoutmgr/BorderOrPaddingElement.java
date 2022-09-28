package org.apache.fop.layoutmgr;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.CondLengthProperty;
import org.apache.fop.traits.MinOptMax;

public abstract class BorderOrPaddingElement extends UnresolvedListElementWithLength {
   public BorderOrPaddingElement(Position position, CondLengthProperty condLength, RelSide side, boolean isFirst, boolean isLast, PercentBaseContext context) {
      super(position, MinOptMax.getInstance(condLength.getLength().getValue(context)), side, condLength.isDiscard(), isFirst, isLast);
   }

   public abstract void notifyLayoutManager(MinOptMax var1);
}
