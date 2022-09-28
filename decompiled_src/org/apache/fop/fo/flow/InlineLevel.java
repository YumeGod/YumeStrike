package org.apache.fop.fo.flow;

import java.awt.Color;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonMarginInline;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.SpaceProperty;

public abstract class InlineLevel extends FObjMixed {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonMarginInline commonMarginInline;
   private CommonFont commonFont;
   private Color color;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private SpaceProperty lineHeight;

   protected InlineLevel(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonMarginInline = pList.getMarginInlineProps();
      this.commonFont = pList.getFontProps();
      this.color = pList.get(72).getColor(this.getUserAgent());
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      this.lineHeight = pList.get(144).getSpace();
   }

   public CommonMarginInline getCommonMarginInline() {
      return this.commonMarginInline;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public CommonFont getCommonFont() {
      return this.commonFont;
   }

   public Color getColor() {
      return this.color;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }
}
