package org.apache.fop.layoutmgr.inline;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import org.apache.fop.area.Area;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.flow.AbstractGraphics;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.TraitSetter;

public abstract class AbstractGraphicsLayoutManager extends LeafNodeLayoutManager {
   public AbstractGraphicsLayoutManager(AbstractGraphics node) {
      super(node);
   }

   private Viewport getInlineArea() {
      AbstractGraphics fobj = (AbstractGraphics)this.fobj;
      Dimension intrinsicSize = new Dimension(fobj.getIntrinsicWidth(), fobj.getIntrinsicHeight());
      ImageLayout imageLayout = new ImageLayout(fobj, this, intrinsicSize);
      Rectangle placement = imageLayout.getPlacement();
      CommonBorderPaddingBackground borderProps = fobj.getCommonBorderPaddingBackground();
      int beforeBPD = borderProps.getPadding(0, false, this);
      beforeBPD += borderProps.getBorderWidth(0, false);
      placement.y += beforeBPD;
      int startIPD = borderProps.getPadding(2, false, this);
      startIPD += borderProps.getBorderWidth(2, false);
      placement.x += startIPD;
      Area viewportArea = this.getChildArea();
      TraitSetter.setProducerID(viewportArea, fobj.getId());
      this.transferForeignAttributes(viewportArea);
      Viewport vp = new Viewport(viewportArea);
      TraitSetter.addPtr(vp, fobj.getPtr());
      TraitSetter.setProducerID(vp, fobj.getId());
      vp.setIPD(imageLayout.getViewportSize().width);
      vp.setBPD(imageLayout.getViewportSize().height);
      vp.setContentPosition(placement);
      vp.setClip(imageLayout.isClipped());
      vp.setOffset(0);
      TraitSetter.addBorders(vp, fobj.getCommonBorderPaddingBackground(), false, false, false, false, this);
      TraitSetter.addPadding(vp, fobj.getCommonBorderPaddingBackground(), false, false, false, false, this);
      TraitSetter.addBackground(vp, fobj.getCommonBorderPaddingBackground(), this);
      return vp;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      Viewport areaCurrent = this.getInlineArea();
      this.setCurrentArea(areaCurrent);
      return super.getNextKnuthElements(context, alignment);
   }

   protected AlignmentContext makeAlignmentContext(LayoutContext context) {
      AbstractGraphics fobj = (AbstractGraphics)this.fobj;
      return new AlignmentContext(this.get(context).getAllocBPD(), fobj.getAlignmentAdjust(), fobj.getAlignmentBaseline(), fobj.getBaselineShift(), fobj.getDominantBaseline(), context.getAlignmentContext());
   }

   protected abstract Area getChildArea();

   public int getBaseLength(int lengthBase, FObj fobj) {
      switch (lengthBase) {
         case 7:
            return ((AbstractGraphics)fobj).getIntrinsicWidth();
         case 8:
            return ((AbstractGraphics)fobj).getIntrinsicHeight();
         case 12:
            return this.get((LayoutContext)null).getBPD();
         default:
            return super.getBaseLength(lengthBase, fobj);
      }
   }
}
