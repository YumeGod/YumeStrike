package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonMarginInline {
   public Length marginTop;
   public Length marginBottom;
   public Length marginLeft;
   public Length marginRight;
   public SpaceProperty spaceStart;
   public SpaceProperty spaceEnd;

   public CommonMarginInline(PropertyList pList) throws PropertyException {
      this.marginTop = pList.get(151).getLength();
      this.marginBottom = pList.get(148).getLength();
      this.marginLeft = pList.get(149).getLength();
      this.marginRight = pList.get(150).getLength();
      this.spaceStart = pList.get(225).getSpace();
      this.spaceEnd = pList.get(224).getSpace();
   }
}
