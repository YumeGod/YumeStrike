package org.apache.fop.layoutmgr.inline;

import java.util.LinkedList;
import java.util.List;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.fo.flow.Character;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontSelector;
import org.apache.fop.layoutmgr.InlineKnuthSequence;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.KnuthSequence;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;
import org.apache.fop.util.CharUtilities;

public class CharacterLayoutManager extends LeafNodeLayoutManager {
   private MinOptMax letterSpaceIPD;
   private int hyphIPD;
   private Font font;
   private CommonBorderPaddingBackground borderProps = null;

   public CharacterLayoutManager(Character node) {
      super(node);
   }

   public void initialize() {
      Character fobj = (Character)this.fobj;
      this.font = FontSelector.selectFontForCharacter(fobj, this);
      SpaceVal ls = SpaceVal.makeLetterSpacing(fobj.getLetterSpacing());
      this.letterSpaceIPD = ls.getSpace();
      this.hyphIPD = fobj.getCommonHyphenation().getHyphIPD(this.font);
      this.borderProps = fobj.getCommonBorderPaddingBackground();
      this.setCommonBorderPaddingBackground(this.borderProps);
      TextArea chArea = this.getCharacterInlineArea(fobj);
      chArea.setBaselineOffset(this.font.getAscender());
      this.setCurrentArea(chArea);
   }

   private TextArea getCharacterInlineArea(Character node) {
      TextArea text = new TextArea();
      char ch = node.getCharacter();
      if (CharUtilities.isAnySpace(ch)) {
         if (!CharUtilities.isZeroWidthSpace(ch)) {
            text.addSpace(ch, 0, CharUtilities.isAdjustableSpace(ch));
         }
      } else {
         text.addWord(String.valueOf(ch), 0);
      }

      TraitSetter.setProducerID(text, node.getId());
      TraitSetter.addTextDecoration(text, node.getTextDecoration());
      TraitSetter.addPtr(text, node.getPtr());
      return text;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      this.curArea = this.get(context);
      KnuthSequence seq = new InlineKnuthSequence();
      if (this.curArea == null) {
         this.setFinished(true);
         return null;
      } else {
         Character fobj = (Character)this.fobj;
         MinOptMax ipd = MinOptMax.getInstance(this.font.getCharWidth(fobj.getCharacter()));
         this.curArea.setIPD(ipd.getOpt());
         this.curArea.setBPD(this.font.getAscender() - this.font.getDescender());
         TraitSetter.addFontTraits(this.curArea, this.font);
         this.curArea.addTrait(Trait.COLOR, fobj.getColor());
         this.alignmentContext = new AlignmentContext(this.font, this.font.getFontSize(), fobj.getAlignmentAdjust(), fobj.getAlignmentBaseline(), fobj.getBaselineShift(), fobj.getDominantBaseline(), context.getAlignmentContext());
         this.addKnuthElementsForBorderPaddingStart(seq);
         this.areaInfo = new LeafNodeLayoutManager.AreaInfo((short)0, ipd, false, this.alignmentContext);
         if (this.letterSpaceIPD.isStiff()) {
            seq.add(new KnuthInlineBox(this.areaInfo.ipdArea.getOpt(), this.areaInfo.alignmentContext, this.notifyPos(new LeafPosition(this, 0)), false));
         } else {
            seq.add(new KnuthInlineBox(this.areaInfo.ipdArea.getOpt(), this.areaInfo.alignmentContext, this.notifyPos(new LeafPosition(this, 0)), false));
            seq.add(new KnuthPenalty(0, 1000, false, new LeafPosition(this, -1), true));
            seq.add(new KnuthGlue(0, 0, 0, new LeafPosition(this, -1), true));
            seq.add(new KnuthInlineBox(0, (AlignmentContext)null, this.notifyPos(new LeafPosition(this, -1)), true));
         }

         this.addKnuthElementsForBorderPaddingEnd(seq);
         LinkedList returnList = new LinkedList();
         returnList.add(seq);
         this.setFinished(true);
         return returnList;
      }
   }

   public String getWordChars(Position pos) {
      return ((TextArea)this.curArea).getText();
   }

   public void hyphenate(Position pos, HyphContext hc) {
      if (hc.getNextHyphPoint() == 1) {
         this.areaInfo.bHyphenated = true;
         this.isSomethingChanged = true;
      }

      hc.updateOffset(1);
   }

   public boolean applyChanges(List oldList) {
      this.setFinished(false);
      return this.isSomethingChanged;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      if (this.isFinished()) {
         return null;
      } else {
         LinkedList returnList = new LinkedList();
         this.addKnuthElementsForBorderPaddingStart(returnList);
         if (!this.letterSpaceIPD.isStiff() && this.areaInfo.iLScount != 0) {
            returnList.add(new KnuthInlineBox(this.areaInfo.ipdArea.getOpt() - this.areaInfo.iLScount * this.letterSpaceIPD.getOpt(), this.areaInfo.alignmentContext, this.notifyPos(new LeafPosition(this, 0)), false));
            returnList.add(new KnuthPenalty(0, 1000, false, new LeafPosition(this, -1), true));
            returnList.add(new KnuthGlue(this.letterSpaceIPD.mult(this.areaInfo.iLScount), new LeafPosition(this, -1), true));
            returnList.add(new KnuthInlineBox(0, (AlignmentContext)null, this.notifyPos(new LeafPosition(this, -1)), true));
            if (this.areaInfo.bHyphenated) {
               returnList.add(new KnuthPenalty(this.hyphIPD, 50, true, new LeafPosition(this, -1), false));
            }
         } else {
            returnList.add(new KnuthInlineBox(this.areaInfo.ipdArea.getOpt(), this.areaInfo.alignmentContext, this.notifyPos(new LeafPosition(this, 0)), false));
            if (this.areaInfo.bHyphenated) {
               returnList.add(new KnuthPenalty(this.hyphIPD, 50, true, new LeafPosition(this, -1), false));
            }
         }

         this.addKnuthElementsForBorderPaddingEnd(returnList);
         this.setFinished(true);
         return returnList;
      }
   }
}
