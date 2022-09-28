package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonAbsolutePosition {
   public int absolutePosition;
   public Length top;
   public Length right;
   public Length bottom;
   public Length left;

   public CommonAbsolutePosition(PropertyList pList) throws PropertyException {
      this.absolutePosition = pList.get(1).getEnum();
      this.top = pList.get(253).getLength();
      this.bottom = pList.get(57).getLength();
      this.left = pList.get(140).getLength();
      this.right = pList.get(211).getLength();
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("CommonAbsolutePosition{");
      sb.append(" absPos=");
      sb.append(this.absolutePosition);
      sb.append(" top=");
      sb.append(this.top);
      sb.append(" bottom=");
      sb.append(this.bottom);
      sb.append(" left=");
      sb.append(this.left);
      sb.append(" right=");
      sb.append(this.right);
      sb.append("}");
      return sb.toString();
   }
}
