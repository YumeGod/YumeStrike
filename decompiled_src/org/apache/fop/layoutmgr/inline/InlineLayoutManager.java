package org.apache.fop.layoutmgr.inline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.InlineBlockParent;
import org.apache.fop.area.inline.InlineParent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.InlineLevel;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.pagination.Title;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonMarginInline;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.layoutmgr.BlockKnuthSequence;
import org.apache.fop.layoutmgr.BlockLevelLayoutManager;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.InlineKnuthSequence;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthSequence;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceSpecifier;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;
import org.apache.fop.util.ListUtil;

public class InlineLayoutManager extends InlineStackingLayoutManager {
   private static Log log;
   private CommonMarginInline inlineProps = null;
   private CommonBorderPaddingBackground borderProps = null;
   private boolean areaCreated = false;
   private LayoutManager lastChildLM = null;
   private Position auxiliaryPosition;
   private Font font;
   protected Length alignmentAdjust;
   protected int alignmentBaseline = 12;
   protected Length baselineShift;
   protected int dominantBaseline;
   protected SpaceProperty lineHeight;
   private AlignmentContext alignmentContext = null;

   public InlineLayoutManager(InlineLevel node) {
      super(node);
   }

   private Inline getInlineFO() {
      return (Inline)this.fobj;
   }

   public void initialize() {
      InlineLevel fobj = (InlineLevel)this.fobj;
      int padding = 0;
      FontInfo fi = fobj.getFOEventHandler().getFontInfo();
      CommonFont commonFont = fobj.getCommonFont();
      FontTriplet[] fontkeys = commonFont.getFontState(fi);
      this.font = fi.getFontInstance(fontkeys[0], commonFont.fontSize.getValue(this));
      this.lineHeight = fobj.getLineHeight();
      this.borderProps = fobj.getCommonBorderPaddingBackground();
      this.inlineProps = fobj.getCommonMarginInline();
      if (fobj instanceof Inline) {
         this.alignmentAdjust = ((Inline)fobj).getAlignmentAdjust();
         this.alignmentBaseline = ((Inline)fobj).getAlignmentBaseline();
         this.baselineShift = ((Inline)fobj).getBaselineShift();
         this.dominantBaseline = ((Inline)fobj).getDominantBaseline();
      } else if (fobj instanceof Leader) {
         this.alignmentAdjust = ((Leader)fobj).getAlignmentAdjust();
         this.alignmentBaseline = ((Leader)fobj).getAlignmentBaseline();
         this.baselineShift = ((Leader)fobj).getBaselineShift();
         this.dominantBaseline = ((Leader)fobj).getDominantBaseline();
      }

      if (this.borderProps != null) {
         padding = this.borderProps.getPadding(0, false, this);
         padding += this.borderProps.getBorderWidth(0, false);
         padding += this.borderProps.getPadding(1, false, this);
         padding += this.borderProps.getBorderWidth(1, false);
      }

      this.extraBPD = MinOptMax.getInstance(padding);
   }

   protected MinOptMax getExtraIPD(boolean isNotFirst, boolean isNotLast) {
      int borderAndPadding = 0;
      if (this.borderProps != null) {
         borderAndPadding = this.borderProps.getPadding(2, isNotFirst, this);
         borderAndPadding += this.borderProps.getBorderWidth(2, isNotFirst);
         borderAndPadding += this.borderProps.getPadding(3, isNotLast, this);
         borderAndPadding += this.borderProps.getBorderWidth(3, isNotLast);
      }

      return MinOptMax.getInstance(borderAndPadding);
   }

   protected boolean hasLeadingFence(boolean isNotFirst) {
      return this.borderProps != null && (this.borderProps.getPadding(2, isNotFirst, this) > 0 || this.borderProps.getBorderWidth(2, isNotFirst) > 0);
   }

   protected boolean hasTrailingFence(boolean isNotLast) {
      return this.borderProps != null && (this.borderProps.getPadding(3, isNotLast, this) > 0 || this.borderProps.getBorderWidth(3, isNotLast) > 0);
   }

   protected SpaceProperty getSpaceStart() {
      return this.inlineProps != null ? this.inlineProps.spaceStart : null;
   }

   protected SpaceProperty getSpaceEnd() {
      return this.inlineProps != null ? this.inlineProps.spaceEnd : null;
   }

   protected InlineArea createArea(boolean hasInlineParent) {
      Object area;
      if (hasInlineParent) {
         area = new InlineParent();
         ((InlineArea)area).setOffset(0);
      } else {
         area = new InlineBlockParent();
      }

      if (this.fobj instanceof Inline) {
         TraitSetter.setProducerID((Area)area, this.getInlineFO().getId());
      }

      return (InlineArea)area;
   }

   protected void setTraits(boolean isNotFirst, boolean isNotLast) {
      if (this.borderProps != null) {
         TraitSetter.setBorderPaddingTraits(this.getCurrentArea(), this.borderProps, isNotFirst, isNotLast, this);
         TraitSetter.addBackground(this.getCurrentArea(), this.borderProps, this);
      }

   }

   public boolean mustKeepTogether() {
      return this.mustKeepTogether(this.getParent());
   }

   private boolean mustKeepTogether(LayoutManager lm) {
      if (lm instanceof BlockLevelLayoutManager) {
         return ((BlockLevelLayoutManager)lm).mustKeepTogether();
      } else {
         return lm instanceof InlineLayoutManager ? ((InlineLayoutManager)lm).mustKeepTogether() : this.mustKeepTogether(lm.getParent());
      }
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      List returnList = new LinkedList();
      KnuthSequence lastSequence = null;
      if (this.fobj instanceof Title) {
         this.alignmentContext = new AlignmentContext(this.font, this.lineHeight.getOptimum(this).getLength().getValue(this), context.getWritingMode());
      } else {
         this.alignmentContext = new AlignmentContext(this.font, this.lineHeight.getOptimum(this).getLength().getValue(this), this.alignmentAdjust, this.alignmentBaseline, this.baselineShift, this.dominantBaseline, context.getAlignmentContext());
      }

      this.childLC = new LayoutContext(context);
      this.childLC.setAlignmentContext(this.alignmentContext);
      if (context.startsNewArea() && this.getSpaceStart() != null) {
         context.getLeadingSpace().addSpace(new SpaceVal(this.getSpaceStart(), this));
      }

      StringBuffer trace = new StringBuffer("InlineLM:");
      boolean borderAdded = false;
      if (this.borderProps != null) {
         this.childLC.setLineStartBorderAndPaddingWidth(context.getLineStartBorderAndPaddingWidth() + this.borderProps.getPaddingStart(true, this) + this.borderProps.getBorderStartWidth(true));
         this.childLC.setLineEndBorderAndPaddingWidth(context.getLineEndBorderAndPaddingWidth() + this.borderProps.getPaddingEnd(true, this) + this.borderProps.getBorderEndWidth(true));
      }

      while(true) {
         LayoutManager curLM;
         List returnedList;
         do {
            do {
               if ((curLM = this.getChildLM()) == null) {
                  if (lastSequence != null) {
                     this.addKnuthElementsForBorderPaddingEnd(lastSequence);
                  }

                  this.setFinished(true);
                  log.trace(trace);
                  if (returnList.isEmpty() && (this.fobj.hasId() || this.fobj.hasMarkers())) {
                     InlineKnuthSequence emptySeq = new InlineKnuthSequence();
                     emptySeq.add(new KnuthInlineBox(0, this.alignmentContext, this.notifyPos(this.getAuxiliaryPosition()), true));
                     returnList.add(emptySeq);
                  }

                  return returnList.isEmpty() ? null : returnList;
               }

               if (!(curLM instanceof InlineLevelLayoutManager) && this.borderProps != null) {
                  this.childLC.setRefIPD(this.childLC.getRefIPD() - this.borderProps.getPaddingStart(this.lastChildLM != null, this) - this.borderProps.getBorderStartWidth(this.lastChildLM != null) - this.borderProps.getPaddingEnd(this.hasNextChildLM(), this) - this.borderProps.getBorderEndWidth(this.hasNextChildLM()));
               }

               returnedList = curLM.getNextKnuthElements(this.childLC, alignment);
               if (returnList.isEmpty() && this.childLC.isKeepWithPreviousPending()) {
                  this.childLC.clearKeepWithPreviousPending();
               }
            } while(returnedList == null);
         } while(returnedList.isEmpty());

         if (curLM instanceof InlineLevelLayoutManager) {
            context.clearKeepWithNextPending();
            ListIterator seqIter = returnedList.listIterator();

            while(seqIter.hasNext()) {
               KnuthSequence sequence = (KnuthSequence)seqIter.next();
               sequence.wrapPositions(this);
            }

            int insertionStartIndex = 0;
            if (lastSequence != null && lastSequence.appendSequenceOrClose((KnuthSequence)returnedList.get(0))) {
               insertionStartIndex = 1;
            }

            if (!borderAdded && !returnedList.isEmpty()) {
               this.addKnuthElementsForBorderPaddingStart((KnuthSequence)returnedList.get(0));
               borderAdded = true;
            }

            Iterator iter = returnedList.listIterator(insertionStartIndex);

            while(iter.hasNext()) {
               returnList.add(iter.next());
            }
         } else {
            BlockKnuthSequence sequence = new BlockKnuthSequence(returnedList);
            sequence.wrapPositions(this);
            boolean appended = false;
            if (lastSequence != null) {
               if (lastSequence.canAppendSequence(sequence)) {
                  BreakElement bk = new BreakElement(new Position(this), 0, context);
                  boolean keepTogether = this.mustKeepTogether() || context.isKeepWithNextPending() || this.childLC.isKeepWithPreviousPending();
                  appended = lastSequence.appendSequenceOrClose(sequence, keepTogether, bk);
               } else {
                  lastSequence.endSequence();
               }
            }

            if (!appended) {
               if (!borderAdded) {
                  this.addKnuthElementsForBorderPaddingStart(sequence);
                  borderAdded = true;
               }

               returnList.add(sequence);
            }

            context.updateKeepWithNextPending(this.childLC.getKeepWithNextPending());
            this.childLC.clearKeepsPending();
         }

         lastSequence = (KnuthSequence)ListUtil.getLast(returnList);
         this.lastChildLM = curLM;
      }
   }

   public void addAreas(PositionIterator parentIter, LayoutContext context) {
      this.addId();
      this.setChildContext(new LayoutContext(context));
      if (this.hasLeadingFence(this.areaCreated)) {
         this.getContext().setLeadingSpace(new SpaceSpecifier(false));
         this.getContext().setFlags(256, true);
      } else {
         this.getContext().setFlags(256, false);
      }

      if (this.getSpaceStart() != null) {
         context.getLeadingSpace().addSpace(new SpaceVal(this.getSpaceStart(), this));
      }

      List positionList = new LinkedList();
      LayoutManager lastLM = null;
      Position lastPos = null;

      while(parentIter.hasNext()) {
         NonLeafPosition pos = (NonLeafPosition)parentIter.next();
         if (pos != null && pos.getPosition() != null) {
            positionList.add(pos.getPosition());
            lastLM = pos.getPosition().getLM();
            lastPos = pos;
         }
      }

      this.addMarkersToPage(true, !this.areaCreated, lastPos == null || this.isLast(lastPos));
      InlineArea parent = this.createArea(lastLM == null || lastLM instanceof InlineLevelLayoutManager);
      parent.setBPD(this.alignmentContext.getHeight());
      if (parent instanceof InlineParent) {
         parent.setOffset(this.alignmentContext.getOffset());
      } else if (parent instanceof InlineBlockParent && this.borderProps != null) {
         parent.setOffset(this.borderProps.getPaddingBefore(false, this) + this.borderProps.getBorderBeforeWidth(false));
      }

      this.setCurrentArea(parent);
      InlineStackingLayoutManager.StackingIter childPosIter = new InlineStackingLayoutManager.StackingIter(positionList.listIterator());

      LayoutManager prevLM;
      LayoutManager childLM;
      for(prevLM = null; (childLM = childPosIter.getNextChildLM()) != null; prevLM = childLM) {
         this.getContext().setFlags(128, context.isLastArea() && childLM == lastLM);
         childLM.addAreas(childPosIter, this.getContext());
         this.getContext().setLeadingSpace(this.getContext().getTrailingSpace());
         this.getContext().setFlags(256, true);
      }

      boolean isLast = this.getContext().isLastArea() && prevLM == this.lastChildLM;
      if (this.hasTrailingFence(isLast)) {
         this.addSpace(this.getCurrentArea(), this.getContext().getTrailingSpace().resolve(false), this.getContext().getSpaceAdjust());
         context.setTrailingSpace(new SpaceSpecifier(false));
      } else {
         context.setTrailingSpace(this.getContext().getTrailingSpace());
      }

      if (context.getTrailingSpace() != null && this.getSpaceEnd() != null) {
         context.getTrailingSpace().addSpace(new SpaceVal(this.getSpaceEnd(), this));
      }

      this.setTraits(this.areaCreated, lastPos == null || !this.isLast(lastPos));
      this.parentLayoutManager.addChildArea(this.getCurrentArea());
      this.addMarkersToPage(false, !this.areaCreated, lastPos == null || this.isLast(lastPos));
      context.setFlags(128, isLast);
      this.areaCreated = true;
      this.checkEndOfLayout(lastPos);
   }

   public void addChildArea(Area childArea) {
      Area parent = this.getCurrentArea();
      if (this.getContext().resolveLeadingSpace()) {
         this.addSpace(parent, this.getContext().getLeadingSpace().resolve(false), this.getContext().getSpaceAdjust());
      }

      parent.addChildArea(childArea);
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      List returnedList = new LinkedList();
      this.addKnuthElementsForBorderPaddingStart(returnedList);
      returnedList.addAll(super.getChangedKnuthElements(oldList, alignment));
      this.addKnuthElementsForBorderPaddingEnd(returnedList);
      return returnedList;
   }

   protected void addKnuthElementsForBorderPaddingStart(List returnList) {
      if (!(returnList instanceof BlockKnuthSequence)) {
         CommonBorderPaddingBackground borderAndPadding = ((InlineLevel)this.fobj).getCommonBorderPaddingBackground();
         if (borderAndPadding != null) {
            int ipStart = borderAndPadding.getBorderStartWidth(false) + borderAndPadding.getPaddingStart(false, this);
            if (ipStart > 0) {
               returnList.add(0, new KnuthBox(ipStart, this.getAuxiliaryPosition(), true));
            }
         }

      }
   }

   protected void addKnuthElementsForBorderPaddingEnd(List returnList) {
      if (!(returnList instanceof BlockKnuthSequence)) {
         CommonBorderPaddingBackground borderAndPadding = ((InlineLevel)this.fobj).getCommonBorderPaddingBackground();
         if (borderAndPadding != null) {
            int ipEnd = borderAndPadding.getBorderEndWidth(false) + borderAndPadding.getPaddingEnd(false, this);
            if (ipEnd > 0) {
               returnList.add(new KnuthBox(ipEnd, this.getAuxiliaryPosition(), true));
            }
         }

      }
   }

   protected Position getAuxiliaryPosition() {
      this.auxiliaryPosition = new NonLeafPosition(this, (Position)null);
      return this.auxiliaryPosition;
   }

   static {
      log = LogFactory.getLog(InlineLayoutManager.class);
   }
}
