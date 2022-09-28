package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.LineArea;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.inline.InlineLevelLayoutManager;
import org.apache.fop.layoutmgr.inline.LineLayoutManager;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;

public class BlockLayoutManager extends BlockStackingLayoutManager implements ConditionalElementListener {
   private static Log log;
   private Block curBlockArea;
   protected ListIterator proxyLMiter = new ProxyLMiter();
   private int lead = 12000;
   private Length lineHeight;
   private int follow = 2000;
   private boolean discardBorderBefore;
   private boolean discardBorderAfter;
   private boolean discardPaddingBefore;
   private boolean discardPaddingAfter;
   private MinOptMax effSpaceBefore;
   private MinOptMax effSpaceAfter;

   public BlockLayoutManager(org.apache.fop.fo.flow.Block inBlock) {
      super(inBlock);
   }

   public void initialize() {
      super.initialize();
      FontInfo fi = this.getBlockFO().getFOEventHandler().getFontInfo();
      FontTriplet[] fontkeys = this.getBlockFO().getCommonFont().getFontState(fi);
      Font initFont = fi.getFontInstance(fontkeys[0], this.getBlockFO().getCommonFont().fontSize.getValue(this));
      this.lead = initFont.getAscender();
      this.follow = -initFont.getDescender();
      this.lineHeight = this.getBlockFO().getLineHeight().getOptimum(this).getLength();
      this.startIndent = this.getBlockFO().getCommonMarginBlock().startIndent.getValue(this);
      this.endIndent = this.getBlockFO().getCommonMarginBlock().endIndent.getValue(this);
      this.foSpaceBefore = (new SpaceVal(this.getBlockFO().getCommonMarginBlock().spaceBefore, this)).getSpace();
      this.foSpaceAfter = (new SpaceVal(this.getBlockFO().getCommonMarginBlock().spaceAfter, this)).getSpace();
      this.bpUnit = 0;
      if (this.bpUnit == 0) {
         this.adjustedSpaceBefore = this.getBlockFO().getCommonMarginBlock().spaceBefore.getSpace().getOptimum(this).getLength().getValue(this);
         this.adjustedSpaceAfter = this.getBlockFO().getCommonMarginBlock().spaceAfter.getSpace().getOptimum(this).getLength().getValue(this);
      } else {
         this.adjustedSpaceBefore = this.getBlockFO().getCommonMarginBlock().spaceBefore.getSpace().getMinimum(this).getLength().getValue(this);
         this.adjustedSpaceAfter = this.getBlockFO().getCommonMarginBlock().spaceAfter.getSpace().getMinimum(this).getLength().getValue(this);
      }

   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      return this.getNextKnuthElements(context, alignment, (Stack)null, (Position)null, (LayoutManager)null);
   }

   public List getNextKnuthElements(LayoutContext context, int alignment, Stack lmStack, Position restartPosition, LayoutManager restartAtLM) {
      this.resetSpaces();
      return lmStack == null ? super.getNextKnuthElements(context, alignment) : super.getNextKnuthElements(context, alignment, lmStack, restartPosition, restartAtLM);
   }

   private void resetSpaces() {
      this.discardBorderBefore = false;
      this.discardBorderAfter = false;
      this.discardPaddingBefore = false;
      this.discardPaddingAfter = false;
      this.effSpaceBefore = null;
      this.effSpaceAfter = null;
   }

   public boolean createNextChildLMs(int pos) {
      while(true) {
         if (this.proxyLMiter.hasNext()) {
            LayoutManager lm = (LayoutManager)this.proxyLMiter.next();
            if (lm instanceof InlineLevelLayoutManager) {
               LineLayoutManager lineLM = this.createLineManager(lm);
               this.addChildLM(lineLM);
            } else {
               this.addChildLM(lm);
            }

            if (pos >= this.childLMs.size()) {
               continue;
            }

            return true;
         }

         return false;
      }
   }

   private LineLayoutManager createLineManager(LayoutManager firstlm) {
      LineLayoutManager llm = new LineLayoutManager(this.getBlockFO(), this.lineHeight, this.lead, this.follow);
      List inlines = new ArrayList();
      inlines.add(firstlm);

      while(this.proxyLMiter.hasNext()) {
         LayoutManager lm = (LayoutManager)this.proxyLMiter.next();
         if (!(lm instanceof InlineLevelLayoutManager)) {
            this.proxyLMiter.previous();
            break;
         }

         inlines.add(lm);
      }

      llm.addChildLMs(inlines);
      return llm;
   }

   public KeepProperty getKeepTogetherProperty() {
      return this.getBlockFO().getKeepTogether();
   }

   public KeepProperty getKeepWithPreviousProperty() {
      return this.getBlockFO().getKeepWithPrevious();
   }

   public KeepProperty getKeepWithNextProperty() {
      return this.getBlockFO().getKeepWithNext();
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      this.getParentArea((Area)null);
      if (layoutContext.getSpaceBefore() > 0) {
         this.addBlockSpacing(0.0, MinOptMax.getInstance(layoutContext.getSpaceBefore()));
      }

      LayoutManager lastLM = null;
      LayoutContext lc = new LayoutContext(0);
      lc.setSpaceAdjust(layoutContext.getSpaceAdjust());
      if (layoutContext.getSpaceAfter() > 0) {
         lc.setSpaceAfter(layoutContext.getSpaceAfter());
      }

      LinkedList positionList = new LinkedList();
      boolean spaceBefore = false;
      boolean spaceAfter = false;
      Position firstPos = null;
      Position lastPos = null;

      while(true) {
         while(parentIter.hasNext()) {
            Position pos = (Position)parentIter.next();
            if (pos.getIndex() >= 0) {
               if (firstPos == null) {
                  firstPos = pos;
               }

               lastPos = pos;
            }

            Position innerPosition = pos;
            if (pos instanceof NonLeafPosition) {
               innerPosition = pos.getPosition();
            }

            if (innerPosition == null) {
               if (positionList.size() == 0) {
                  spaceBefore = true;
               } else {
                  spaceAfter = true;
               }
            } else if (innerPosition.getLM() != this || innerPosition instanceof BlockStackingLayoutManager.MappingPosition) {
               positionList.add(innerPosition);
               lastLM = innerPosition.getLM();
            }
         }

         this.addId();
         this.addMarkersToPage(true, this.isFirst(firstPos), this.isLast(lastPos));
         Object childPosIter;
         if (this.bpUnit == 0) {
            childPosIter = new BlockStackingLayoutManager.StackingIter(positionList.listIterator());
         } else {
            LinkedList splitList = new LinkedList();
            int splitLength = 0;
            int iFirst = ((BlockStackingLayoutManager.MappingPosition)positionList.getFirst()).getFirstIndex();
            int iLast = ((BlockStackingLayoutManager.MappingPosition)positionList.getLast()).getLastIndex();
            ListIterator storedListIterator = this.storedList.listIterator(iFirst);

            while(storedListIterator.nextIndex() <= iLast) {
               KnuthElement element = (KnuthElement)storedListIterator.next();
               if (element.getLayoutManager() != this) {
                  splitList.add(element);
                  splitLength += element.getWidth();
                  lastLM = element.getLayoutManager();
               }
            }

            if (spaceBefore && spaceAfter) {
               this.foSpaceBefore = (new SpaceVal(this.getBlockFO().getCommonMarginBlock().spaceBefore, this)).getSpace();
               this.foSpaceAfter = (new SpaceVal(this.getBlockFO().getCommonMarginBlock().spaceAfter, this)).getSpace();
               this.adjustedSpaceBefore = (this.neededUnits(splitLength + this.foSpaceBefore.getMin() + this.foSpaceAfter.getMin()) * this.bpUnit - splitLength) / 2;
               this.adjustedSpaceAfter = this.neededUnits(splitLength + this.foSpaceBefore.getMin() + this.foSpaceAfter.getMin()) * this.bpUnit - splitLength - this.adjustedSpaceBefore;
            } else if (spaceBefore) {
               this.adjustedSpaceBefore = this.neededUnits(splitLength + this.foSpaceBefore.getMin()) * this.bpUnit - splitLength;
            } else {
               this.adjustedSpaceAfter = this.neededUnits(splitLength + this.foSpaceAfter.getMin()) * this.bpUnit - splitLength;
            }

            childPosIter = new KnuthPossPosIter(splitList, 0, splitList.size());
         }

         LayoutManager childLM;
         while((childLM = ((PositionIterator)childPosIter).getNextChildLM()) != null) {
            lc.setFlags(128, layoutContext.isLastArea() && childLM == lastLM);
            lc.setStackLimitBP(layoutContext.getStackLimitBP());
            childLM.addAreas((PositionIterator)childPosIter, lc);
         }

         this.addMarkersToPage(false, this.isFirst(firstPos), this.isLast(lastPos));
         TraitSetter.addPtr(this.curBlockArea, this.getBlockFO().getPtr());
         TraitSetter.addSpaceBeforeAfter(this.curBlockArea, layoutContext.getSpaceAdjust(), this.effSpaceBefore, this.effSpaceAfter);
         this.flush();
         this.curBlockArea = null;
         this.resetSpaces();
         this.checkEndOfLayout(lastPos);
         return;
      }
   }

   public Area getParentArea(Area childArea) {
      if (this.curBlockArea == null) {
         this.curBlockArea = new Block();
         this.curBlockArea.setIPD(super.getContentAreaIPD());
         TraitSetter.addBreaks(this.curBlockArea, this.getBlockFO().getBreakBefore(), this.getBlockFO().getBreakAfter());
         this.parentLayoutManager.getParentArea(this.curBlockArea);
         TraitSetter.setProducerID(this.curBlockArea, this.getBlockFO().getId());
         TraitSetter.addBorders(this.curBlockArea, this.getBlockFO().getCommonBorderPaddingBackground(), this.discardBorderBefore, this.discardBorderAfter, false, false, this);
         TraitSetter.addPadding(this.curBlockArea, this.getBlockFO().getCommonBorderPaddingBackground(), this.discardPaddingBefore, this.discardPaddingAfter, false, false, this);
         TraitSetter.addMargins(this.curBlockArea, this.getBlockFO().getCommonBorderPaddingBackground(), this.startIndent, this.endIndent, this);
         this.setCurrentArea(this.curBlockArea);
      }

      return this.curBlockArea;
   }

   public void addChildArea(Area childArea) {
      if (this.curBlockArea != null) {
         if (childArea instanceof LineArea) {
            this.curBlockArea.addLineArea((LineArea)childArea);
         } else {
            this.curBlockArea.addBlock((Block)childArea);
         }
      }

   }

   protected void flush() {
      if (this.curBlockArea != null) {
         TraitSetter.addBackground(this.curBlockArea, this.getBlockFO().getCommonBorderPaddingBackground(), this);
         super.flush();
      }

   }

   protected org.apache.fop.fo.flow.Block getBlockFO() {
      return (org.apache.fop.fo.flow.Block)this.fobj;
   }

   public int getContentAreaIPD() {
      return this.curBlockArea != null ? this.curBlockArea.getIPD() : super.getContentAreaIPD();
   }

   public int getContentAreaBPD() {
      return this.curBlockArea != null ? this.curBlockArea.getBPD() : -1;
   }

   public boolean getGeneratesBlockArea() {
      return true;
   }

   public void notifySpace(RelSide side, MinOptMax effectiveLength) {
      if (RelSide.BEFORE == side) {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceBefore + "-> " + effectiveLength);
         }

         this.effSpaceBefore = effectiveLength;
      } else {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceAfter + "-> " + effectiveLength);
         }

         this.effSpaceAfter = effectiveLength;
      }

   }

   public void notifyBorder(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardBorderBefore = true;
         } else {
            this.discardBorderAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Border " + side + " -> " + effectiveLength);
      }

   }

   public void notifyPadding(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardPaddingBefore = true;
         } else {
            this.discardPaddingAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Padding " + side + " -> " + effectiveLength);
      }

   }

   public boolean isRestartable() {
      return true;
   }

   static {
      log = LogFactory.getLog(BlockLayoutManager.class);
   }

   protected class ProxyLMiter extends LMiter {
      public ProxyLMiter() {
         super(BlockLayoutManager.this);
         this.listLMs = new ArrayList(10);
      }

      public boolean hasNext() {
         return this.curPos < this.listLMs.size() || this.createNextChildLMs(this.curPos);
      }

      protected boolean createNextChildLMs(int pos) {
         List newLMs = BlockLayoutManager.this.createChildLMs(pos + 1 - this.listLMs.size());
         if (newLMs != null) {
            this.listLMs.addAll(newLMs);
         }

         return pos < this.listLMs.size();
      }
   }
}
