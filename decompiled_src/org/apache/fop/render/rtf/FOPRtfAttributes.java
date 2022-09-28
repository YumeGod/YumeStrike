package org.apache.fop.render.rtf;

import java.awt.Color;
import org.apache.fop.datatypes.Length;
import org.apache.fop.render.DummyPercentBaseContext;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfColorTable;

public class FOPRtfAttributes extends RtfAttributes {
   public RtfAttributes setTwips(String name, Length value) {
      this.set(name, value.getValue() / 50);
      return this;
   }

   public RtfAttributes setTwips(String name, int value) {
      this.set(name, value / 50);
      return this;
   }

   public RtfAttributes setHalfPoints(String name, Length value) {
      this.set(name, value.getValue(DummyPercentBaseContext.getInstance()) / 500);
      return this;
   }

   public RtfAttributes set(String name, Color color) {
      int redComponent = color.getRed();
      int greenComponent = color.getGreen();
      int blueComponent = color.getBlue();
      this.set(name, RtfColorTable.getInstance().getColorNumber(redComponent, greenComponent, blueComponent));
      return this;
   }
}
