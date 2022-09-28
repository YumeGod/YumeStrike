package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonRelativePosition {
   public int relativePosition;
   public Length top;
   public Length right;
   public Length bottom;
   public Length left;

   public CommonRelativePosition(PropertyList pList) throws PropertyException {
      this.relativePosition = pList.get(203).getEnum();
      this.top = pList.get(253).getLength();
      this.bottom = pList.get(57).getLength();
      this.left = pList.get(140).getLength();
      this.right = pList.get(211).getLength();
   }
}
