package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonMarginBlock {
   public Length marginTop;
   public Length marginBottom;
   public Length marginLeft;
   public Length marginRight;
   public SpaceProperty spaceBefore;
   public SpaceProperty spaceAfter;
   public Length startIndent;
   public Length endIndent;

   public CommonMarginBlock(PropertyList pList) throws PropertyException {
      this.marginTop = pList.get(151).getLength();
      this.marginBottom = pList.get(148).getLength();
      this.marginLeft = pList.get(149).getLength();
      this.marginRight = pList.get(150).getLength();
      this.spaceBefore = pList.get(223).getSpace();
      this.spaceAfter = pList.get(222).getSpace();
      this.startIndent = pList.get(233).getLength();
      this.endIndent = pList.get(91).getLength();
   }

   public String toString() {
      return "CommonMarginBlock:\nMargins (top, bottom, left, right): (" + this.marginTop + ", " + this.marginBottom + ", " + this.marginLeft + ", " + this.marginRight + ")\n" + "Space (before, after): (" + this.spaceBefore + ", " + this.spaceAfter + ")\n" + "Indents (start, end): (" + this.startIndent + ", " + this.endIndent + ")\n";
   }
}
