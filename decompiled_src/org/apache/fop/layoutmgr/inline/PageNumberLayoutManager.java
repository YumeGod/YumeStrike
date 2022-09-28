package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;

public class PageNumberLayoutManager extends LeafNodeLayoutManager {
   private PageNumber fobj;
   private Font font;

   public PageNumberLayoutManager(PageNumber node) {
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
      TextArea text = new TextArea();
      String str = this.getCurrentPV().getPageNumberString();
      int width = this.getStringWidth(str);
      text.addWord(str, 0);
      text.setIPD(width);
      text.setBPD(this.font.getAscender() - this.font.getDescender());
      text.setBaselineOffset(this.font.getAscender());
      TraitSetter.addFontTraits(text, this.font);
      text.addTrait(Trait.COLOR, this.fobj.getColor());
      TraitSetter.addPtr(text, this.fobj.getPtr());
      TraitSetter.addTextDecoration(text, this.fobj.getTextDecoration());
      return text;
   }

   protected InlineArea getEffectiveArea() {
      TextArea baseArea = (TextArea)this.curArea;
      TextArea ta = new TextArea();
      TraitSetter.setProducerID(ta, this.fobj.getId());
      ta.setIPD(baseArea.getIPD());
      ta.setBPD(baseArea.getBPD());
      ta.setOffset(baseArea.getOffset());
      ta.setBaselineOffset(baseArea.getBaselineOffset());
      ta.addTrait(Trait.COLOR, this.fobj.getColor());
      ta.getTraits().putAll(baseArea.getTraits());
      this.updateContent(ta);
      return ta;
   }

   private void updateContent(TextArea area) {
      area.removeText();
      area.addWord(this.getCurrentPV().getPageNumberString(), 0);
      area.handleIPDVariation(this.getStringWidth(area.getText()) - area.getIPD());
      this.areaInfo.ipdArea = MinOptMax.getInstance(area.getIPD());
   }

   private int getStringWidth(String str) {
      int width = 0;

      for(int count = 0; count < str.length(); ++count) {
         width += this.font.getCharWidth(str.charAt(count));
      }

      return width;
   }
}
