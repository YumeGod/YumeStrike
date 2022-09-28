package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Resolvable;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.UnresolvedPageNumber;
import org.apache.fop.fo.flow.AbstractPageNumberCitation;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.TraitSetter;

public abstract class AbstractPageNumberCitationLayoutManager extends LeafNodeLayoutManager {
   protected AbstractPageNumberCitation fobj;
   protected Font font;
   protected boolean resolved = false;

   public AbstractPageNumberCitationLayoutManager(AbstractPageNumberCitation node) {
      super(node);
      this.fobj = node;
   }

   public void initialize() {
      FontInfo fi = this.fobj.getFOEventHandler().getFontInfo();
      FontTriplet[] fontkeys = this.fobj.getCommonFont().getFontState(fi);
      this.font = fi.getFontInstance(fontkeys[0], this.fobj.getCommonFont().fontSize.getValue(this));
      this.setCommonBorderPaddingBackground(this.fobj.getCommonBorderPaddingBackground());
   }

   protected AlignmentContext makeAlignmentContext(LayoutContext context) {
      return new AlignmentContext(this.font, this.fobj.getLineHeight().getOptimum(this).getLength().getValue(this), this.fobj.getAlignmentAdjust(), this.fobj.getAlignmentBaseline(), this.fobj.getBaselineShift(), this.fobj.getDominantBaseline(), context.getAlignmentContext());
   }

   public InlineArea get(LayoutContext context) {
      this.curArea = this.getPageNumberCitationInlineArea();
      return this.curArea;
   }

   public void addAreas(PositionIterator posIter, LayoutContext context) {
      super.addAreas(posIter, context);
      if (!this.resolved) {
         this.getPSLM().addUnresolvedArea(this.fobj.getRefId(), (Resolvable)this.curArea);
      }

   }

   private InlineArea getPageNumberCitationInlineArea() {
      PageViewport page = this.getPSLM().getFirstPVWithID(this.fobj.getRefId());
      Object text;
      String str;
      int width;
      if (page != null) {
         str = page.getPageNumberString();
         text = new TextArea();
         width = this.getStringWidth(str);
         ((TextArea)text).addWord(str, 0);
         ((TextArea)text).setIPD(width);
         this.resolved = true;
      } else {
         this.resolved = false;
         text = new UnresolvedPageNumber(this.fobj.getRefId(), this.font);
         str = "MMM";
         width = this.getStringWidth(str);
         ((TextArea)text).setIPD(width);
      }

      this.updateTextAreaTraits((TextArea)text);
      return (InlineArea)text;
   }

   protected void updateTextAreaTraits(TextArea text) {
      TraitSetter.setProducerID(text, this.fobj.getId());
      text.setBPD(this.font.getAscender() - this.font.getDescender());
      text.setBaselineOffset(this.font.getAscender());
      TraitSetter.addFontTraits(text, this.font);
      text.addTrait(Trait.COLOR, this.fobj.getColor());
      TraitSetter.addPtr(text, this.fobj.getPtr());
      TraitSetter.addTextDecoration(text, this.fobj.getTextDecoration());
   }

   protected int getStringWidth(String str) {
      int width = 0;

      for(int count = 0; count < str.length(); ++count) {
         width += this.font.getCharWidth(str.charAt(count));
      }

      return width;
   }
}
