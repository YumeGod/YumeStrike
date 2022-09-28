package org.apache.fop.layoutmgr.inline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.properties.CommonHyphenation;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.hyphenation.Hyphenation;
import org.apache.fop.hyphenation.Hyphenator;
import org.apache.fop.layoutmgr.Adjustment;
import org.apache.fop.layoutmgr.BlockLevelLayoutManager;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.BreakingAlgorithm;
import org.apache.fop.layoutmgr.ElementListObserver;
import org.apache.fop.layoutmgr.InlineKnuthSequence;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBlockBox;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.KnuthPossPosIter;
import org.apache.fop.layoutmgr.KnuthSequence;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.LeafPosition;
import org.apache.fop.layoutmgr.ListElement;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceSpecifier;
import org.apache.fop.traits.MinOptMax;

public class LineLayoutManager extends InlineStackingLayoutManager implements BlockLevelLayoutManager {
   private static Log log;
   private Block fobj;
   private boolean isFirstInBlock;
   private int textAlignment = 70;
   private int textAlignmentLast;
   private int effectiveAlignment;
   private Length textIndent;
   private Length lastLineEndIndent;
   private CommonHyphenation hyphenationProperties;
   private Numeric hyphenationLadderCount;
   private int wrapOption = 161;
   private int whiteSpaceTreament;
   private Length lineHeight;
   private int lead;
   private int follow;
   private AlignmentContext alignmentContext;
   private List knuthParagraphs;
   private LineLayoutPossibilities lineLayouts;
   private LineLayoutPossibilities[] lineLayoutsList;
   private int ipd = 0;
   private boolean hyphenationPerformed;
   public static final int DEFAULT_SPACE_WIDTH = 3336;
   private int constantLineHeight = 12000;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public void initialize() {
      this.textAlignment = this.fobj.getTextAlign();
      this.textAlignmentLast = this.fobj.getTextAlignLast();
      this.textIndent = this.fobj.getTextIndent();
      this.lastLineEndIndent = this.fobj.getLastLineEndIndent();
      this.hyphenationProperties = this.fobj.getCommonHyphenation();
      this.hyphenationLadderCount = this.fobj.getHyphenationLadderCount();
      this.wrapOption = this.fobj.getWrapOption();
      this.whiteSpaceTreament = this.fobj.getWhitespaceTreatment();
      this.effectiveAlignment = this.getEffectiveAlignment(this.textAlignment, this.textAlignmentLast);
      this.isFirstInBlock = this == this.getParent().getChildLMs().get(0);
   }

   private int getEffectiveAlignment(int alignment, int alignmentLast) {
      return this.textAlignment != 70 && this.textAlignmentLast == 70 ? 0 : this.textAlignment;
   }

   public LineLayoutManager(Block block, Length lh, int l, int f) {
      super(block);
      this.fobj = block;
      this.fobjIter = null;
      this.lineHeight = lh;
      this.lead = l;
      this.follow = f;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      FontInfo fi = this.fobj.getFOEventHandler().getFontInfo();
      FontTriplet[] fontkeys = this.fobj.getCommonFont().getFontState(fi);
      Font fs = fi.getFontInstance(fontkeys[0], this.fobj.getCommonFont().fontSize.getValue(this));
      this.alignmentContext = new AlignmentContext(fs, this.lineHeight.getValue(this), context.getWritingMode());
      context.setAlignmentContext(this.alignmentContext);
      this.ipd = context.getRefIPD();
      if (this.knuthParagraphs == null) {
         this.knuthParagraphs = new ArrayList();
         this.collectInlineKnuthElements(context);
      }

      if (this.knuthParagraphs.size() == 0) {
         this.setFinished(true);
         return null;
      } else {
         return this.createLineBreaks(context.getBPAlignment(), context);
      }
   }

   public List getNextKnuthElements(LayoutContext context, int alignment, LeafPosition restartPosition) {
      log.trace("Restarting line breaking from index " + restartPosition.getIndex());
      int parIndex = restartPosition.getLeafPos();
      Paragraph paragraph = (Paragraph)this.knuthParagraphs.get(parIndex);

      for(int i = 0; i <= restartPosition.getIndex(); ++i) {
         paragraph.remove(0);
      }

      Iterator iter = paragraph.iterator();

      while(iter.hasNext() && !((KnuthElement)iter.next()).isBox()) {
         iter.remove();
      }

      if (!iter.hasNext()) {
         this.knuthParagraphs.remove(parIndex);
      }

      if (this.knuthParagraphs.size() == 0) {
         this.setFinished(true);
         return null;
      } else {
         this.ipd = context.getRefIPD();
         return this.createLineBreaks(context.getBPAlignment(), context);
      }
   }

   private void collectInlineKnuthElements(LayoutContext context) {
      LayoutContext inlineLC = new LayoutContext(context);
      boolean previousIsBox = false;
      StringBuffer trace = new StringBuffer("LineLM:");
      Paragraph lastPar = null;

      label108:
      while(true) {
         List inlineElements;
         do {
            do {
               InlineLevelLayoutManager curLM;
               if ((curLM = (InlineLevelLayoutManager)this.getChildLM()) == null) {
                  if (lastPar != null) {
                     lastPar.endParagraph();
                     ElementListObserver.observe(lastPar, "line", this.fobj.getId());
                     if (log.isTraceEnabled()) {
                        trace.append(" ]");
                     }
                  }

                  log.trace(trace);
                  return;
               }

               inlineElements = curLM.getNextKnuthElements(inlineLC, this.effectiveAlignment);
            } while(inlineElements == null);
         } while(inlineElements.size() == 0);

         if (lastPar != null) {
            KnuthSequence firstSeq = (KnuthSequence)inlineElements.get(0);
            if (!firstSeq.isInlineSequence()) {
               lastPar.endParagraph();
               ElementListObserver.observe(lastPar, "line", (String)null);
               lastPar = null;
               if (log.isTraceEnabled()) {
                  trace.append(" ]");
               }

               previousIsBox = false;
            }

            if (lastPar != null) {
               KnuthElement thisElement = (KnuthElement)firstSeq.get(0);
               if (thisElement.isBox() && !thisElement.isAuxiliary() && previousIsBox) {
                  lastPar.addALetterSpace();
               }
            }
         }

         ListIterator iter = inlineElements.listIterator();

         while(true) {
            while(true) {
               if (!iter.hasNext()) {
                  continue label108;
               }

               KnuthSequence sequence = (KnuthSequence)iter.next();
               if (sequence.isInlineSequence()) {
                  ListElement lastElement = sequence.getLast();
                  if (!$assertionsDisabled && lastElement == null) {
                     throw new AssertionError();
                  }

                  previousIsBox = lastElement.isBox() && !((KnuthElement)lastElement).isAuxiliary() && ((KnuthElement)lastElement).getWidth() != 0;
                  if (lastPar == null) {
                     lastPar = new Paragraph(this, this.textAlignment, this.textAlignmentLast, this.textIndent.getValue(this), this.lastLineEndIndent.getValue(this));
                     lastPar.startSequence();
                     if (log.isTraceEnabled()) {
                        trace.append(" [");
                     }
                  } else if (log.isTraceEnabled()) {
                     trace.append(" +");
                  }

                  lastPar.addAll(sequence);
                  if (log.isTraceEnabled()) {
                     trace.append(" I");
                  }

                  if (lastElement.isPenalty() && ((KnuthPenalty)lastElement).getPenalty() == -1000) {
                     lastPar.removeLast();
                     if (!lastPar.containsBox()) {
                        lastPar.add(new KnuthGlue(this.ipd, 0, this.ipd, (Position)null, true));
                     }

                     lastPar.endParagraph();
                     ElementListObserver.observe(lastPar, "line", (String)null);
                     lastPar = null;
                     if (log.isTraceEnabled()) {
                        trace.append(" ]");
                     }

                     previousIsBox = false;
                  }
               } else {
                  this.knuthParagraphs.add(sequence);
                  if (log.isTraceEnabled()) {
                     trace.append(" B");
                  }
               }
            }
         }
      }
   }

   private List createLineBreaks(int alignment, LayoutContext context) {
      Iterator paragraphsIterator = this.knuthParagraphs.iterator();
      this.lineLayoutsList = new LineLayoutPossibilities[this.knuthParagraphs.size()];

      for(int i = 0; paragraphsIterator.hasNext(); ++i) {
         KnuthSequence seq = (KnuthSequence)paragraphsIterator.next();
         LineLayoutPossibilities llPoss;
         if (!seq.isInlineSequence()) {
            llPoss = new LineLayoutPossibilities();
         } else {
            llPoss = this.findOptimalBreakingPoints(alignment, (Paragraph)seq);
         }

         this.lineLayoutsList[i] = llPoss;
      }

      this.setFinished(true);
      return this.postProcessLineBreaks(alignment, context);
   }

   private LineLayoutPossibilities findOptimalBreakingPoints(int alignment, Paragraph currPar) {
      this.lineLayouts = new LineLayoutPossibilities();
      double maxAdjustment = 1.0;
      int iBPcount = false;
      LineBreakingAlgorithm alg = new LineBreakingAlgorithm(alignment, this.textAlignment, this.textAlignmentLast, this.textIndent.getValue(this), currPar.lineFiller.getOpt(), this.lineHeight.getValue(this), this.lead, this.follow, this.knuthParagraphs.indexOf(currPar) == 0, this.hyphenationLadderCount.getEnum() == 89 ? 0 : this.hyphenationLadderCount.getValue(), this);
      if (this.hyphenationProperties.hyphenate.getEnum() == 149 && this.fobj.getWrapOption() != 93 && !this.hyphenationPerformed) {
         this.hyphenationPerformed = true;
         this.findHyphenationPoints(currPar);
      }

      byte allowedBreaks;
      if (this.wrapOption == 93) {
         allowedBreaks = 2;
      } else {
         allowedBreaks = 1;
      }

      alg.setConstantLineWidth(this.ipd);
      int iBPcount = alg.findBreakingPoints(currPar, maxAdjustment, false, allowedBreaks);
      if (iBPcount == 0 || alignment == 70) {
         if (iBPcount > 0) {
            alg.resetAlgorithm();
            this.lineLayouts.savePossibilities(false);
         } else {
            log.debug("No set of breaking points found with maxAdjustment = " + maxAdjustment);
         }

         log.debug("Hyphenation possible? " + (this.hyphenationProperties.hyphenate.getEnum() == 149));
         if (this.hyphenationProperties.hyphenate.getEnum() == 149 && allowedBreaks != 2) {
            allowedBreaks = 0;
         } else {
            maxAdjustment = 5.0;
         }

         if (alg.findBreakingPoints(currPar, maxAdjustment, false, allowedBreaks) == 0) {
            if (log.isDebugEnabled()) {
               log.debug("No set of breaking points found with maxAdjustment = " + maxAdjustment + (this.hyphenationProperties.hyphenate.getEnum() == 149 ? " and hyphenation" : ""));
            }

            maxAdjustment = 20.0;
            alg.findBreakingPoints(currPar, maxAdjustment, true, allowedBreaks);
         }

         this.lineLayouts.restorePossibilities();
      }

      return this.lineLayouts;
   }

   private List postProcessLineBreaks(int alignment, LayoutContext context) {
      List returnList = new LinkedList();
      int endIndex = -1;

      for(int p = 0; p < this.knuthParagraphs.size(); ++p) {
         if (p > 0) {
            Keep keep = this.getKeepTogether();
            returnList.add(new BreakElement(new Position(this), keep.getPenalty(), keep.getContext(), context));
         }

         LineLayoutPossibilities llPoss = this.lineLayoutsList[p];
         KnuthSequence seq = (KnuthSequence)this.knuthParagraphs.get(p);
         if (!seq.isInlineSequence()) {
            List targetList = new LinkedList();

            ListElement tempElement;
            for(ListIterator listIter = seq.listIterator(); listIter.hasNext(); targetList.add(tempElement)) {
               tempElement = (ListElement)listIter.next();
               if (tempElement.getLayoutManager() != this) {
                  tempElement.setPosition(this.notifyPos(new NonLeafPosition(this, tempElement.getPosition())));
               }
            }

            returnList.addAll(targetList);
         } else if (seq.isInlineSequence() && alignment == 70) {
            Position returnPosition = new LeafPosition(this, p);
            this.createElements(returnList, llPoss, returnPosition);
         } else {
            int startIndex = 0;

            for(int i = 0; i < llPoss.getChosenLineCount(); ++i) {
               if (returnList.size() > 0 && i > 0 && i >= this.fobj.getOrphans() && i <= llPoss.getChosenLineCount() - this.fobj.getWidows()) {
                  Keep keep = this.getKeepTogether();
                  returnList.add(new BreakElement(new LeafPosition(this, p, endIndex), keep.getPenalty(), keep.getContext(), context));
               }

               endIndex = ((LineBreakPosition)llPoss.getChosenPosition(i)).getLeafPos();
               List footnoteList = new LinkedList();
               ListIterator elementIterator = seq.listIterator(startIndex);

               while(true) {
                  while(elementIterator.nextIndex() <= endIndex) {
                     KnuthElement element = (KnuthElement)elementIterator.next();
                     if (element instanceof KnuthInlineBox && ((KnuthInlineBox)element).isAnchor()) {
                        footnoteList.add(((KnuthInlineBox)element).getFootnoteBodyLM());
                     } else if (element instanceof KnuthBlockBox) {
                        footnoteList.addAll(((KnuthBlockBox)element).getFootnoteBodyLMs());
                     }
                  }

                  startIndex = endIndex + 1;
                  LineBreakPosition lbp = (LineBreakPosition)llPoss.getChosenPosition(i);
                  returnList.add(new KnuthBlockBox(lbp.lineHeight + lbp.spaceBefore + lbp.spaceAfter, footnoteList, lbp, false));
                  break;
               }
            }
         }
      }

      return returnList;
   }

   private void createElements(List list, LineLayoutPossibilities llPoss, Position elementPosition) {
      int innerLines = 0;
      int optionalLines = 0;
      int conditionalOptionalLines = 0;
      int eliminableLines = 0;
      int conditionalEliminableLines = 0;
      int firstLines = this.fobj.getOrphans();
      int lastLines = this.fobj.getWidows();
      List breaker = new LinkedList();
      if (this.fobj.getOrphans() + this.fobj.getWidows() <= llPoss.getMinLineCount()) {
         innerLines = llPoss.getMinLineCount() - (this.fobj.getOrphans() + this.fobj.getWidows());
         optionalLines = llPoss.getMaxLineCount() - llPoss.getOptLineCount();
         eliminableLines = llPoss.getOptLineCount() - llPoss.getMinLineCount();
      } else if (this.fobj.getOrphans() + this.fobj.getWidows() <= llPoss.getOptLineCount()) {
         optionalLines = llPoss.getMaxLineCount() - llPoss.getOptLineCount();
         eliminableLines = llPoss.getOptLineCount() - (this.fobj.getOrphans() + this.fobj.getWidows());
         conditionalEliminableLines = this.fobj.getOrphans() + this.fobj.getWidows() - llPoss.getMinLineCount();
      } else if (this.fobj.getOrphans() + this.fobj.getWidows() <= llPoss.getMaxLineCount()) {
         optionalLines = llPoss.getMaxLineCount() - (this.fobj.getOrphans() + this.fobj.getWidows());
         conditionalOptionalLines = this.fobj.getOrphans() + this.fobj.getWidows() - llPoss.getOptLineCount();
         conditionalEliminableLines = llPoss.getOptLineCount() - llPoss.getMinLineCount();
         firstLines -= conditionalOptionalLines;
      } else {
         conditionalOptionalLines = llPoss.getMaxLineCount() - llPoss.getOptLineCount();
         conditionalEliminableLines = llPoss.getOptLineCount() - llPoss.getMinLineCount();
         firstLines = llPoss.getOptLineCount();
         lastLines = 0;
      }

      if (lastLines == 0 || conditionalOptionalLines <= 0 && conditionalEliminableLines <= 0) {
         if (lastLines != 0) {
            breaker.add(new KnuthPenalty(0, 0, false, elementPosition, false));
         }
      } else {
         breaker.add(new KnuthPenalty(0, 1000, false, elementPosition, false));
         breaker.add(new KnuthGlue(0, -conditionalOptionalLines * this.constantLineHeight, -conditionalEliminableLines * this.constantLineHeight, Adjustment.LINE_NUMBER_ADJUSTMENT, elementPosition, false));
         breaker.add(new KnuthPenalty(conditionalOptionalLines * this.constantLineHeight, 0, false, elementPosition, false));
         breaker.add(new KnuthGlue(0, conditionalOptionalLines * this.constantLineHeight, conditionalEliminableLines * this.constantLineHeight, Adjustment.LINE_NUMBER_ADJUSTMENT, elementPosition, false));
      }

      list.add(new KnuthBox(firstLines * this.constantLineHeight, elementPosition, lastLines == 0 && conditionalOptionalLines == 0 && conditionalEliminableLines == 0));
      if (conditionalOptionalLines > 0 || conditionalEliminableLines > 0) {
         list.add(new KnuthPenalty(0, 1000, false, elementPosition, false));
         list.add(new KnuthGlue(0, conditionalOptionalLines * this.constantLineHeight, conditionalEliminableLines * this.constantLineHeight, Adjustment.LINE_NUMBER_ADJUSTMENT, elementPosition, false));
         list.add(new KnuthBox(0, elementPosition, lastLines == 0));
      }

      int i;
      for(i = 0; i < optionalLines; ++i) {
         list.addAll(breaker);
         list.add(new KnuthBox(0, elementPosition, false));
         list.add(new KnuthPenalty(0, 1000, false, elementPosition, false));
         list.add(new KnuthGlue(0, this.constantLineHeight, 0, Adjustment.LINE_NUMBER_ADJUSTMENT, elementPosition, false));
         list.add(new KnuthBox(0, elementPosition, false));
      }

      for(i = 0; i < eliminableLines; ++i) {
         list.addAll(breaker);
         list.add(new KnuthBox(this.constantLineHeight, elementPosition, false));
         list.add(new KnuthPenalty(0, 1000, false, elementPosition, false));
         list.add(new KnuthGlue(0, 0, this.constantLineHeight, Adjustment.LINE_NUMBER_ADJUSTMENT, elementPosition, false));
         list.add(new KnuthBox(0, elementPosition, false));
      }

      for(i = 0; i < innerLines; ++i) {
         list.addAll(breaker);
         list.add(new KnuthBox(this.constantLineHeight, elementPosition, false));
      }

      if (lastLines > 0) {
         list.addAll(breaker);
         list.add(new KnuthBox(lastLines * this.constantLineHeight, elementPosition, true));
      }

   }

   public boolean mustKeepTogether() {
      return ((BlockLevelLayoutManager)this.getParent()).mustKeepTogether();
   }

   public KeepProperty getKeepTogetherProperty() {
      return ((BlockLevelLayoutManager)this.getParent()).getKeepTogetherProperty();
   }

   public KeepProperty getKeepWithPreviousProperty() {
      return ((BlockLevelLayoutManager)this.getParent()).getKeepWithPreviousProperty();
   }

   public KeepProperty getKeepWithNextProperty() {
      return ((BlockLevelLayoutManager)this.getParent()).getKeepWithNextProperty();
   }

   public Keep getKeepTogether() {
      return ((BlockLevelLayoutManager)this.getParent()).getKeepTogether();
   }

   public boolean mustKeepWithPrevious() {
      return !this.getKeepWithPrevious().isAuto();
   }

   public boolean mustKeepWithNext() {
      return !this.getKeepWithNext().isAuto();
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }

   public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
      LeafPosition pos = (LeafPosition)lastElement.getPosition();
      int lineNumberDifference = (int)Math.round((double)adj / (double)this.constantLineHeight + (adj > 0 ? -0.4 : 0.4));
      LineLayoutPossibilities llPoss = this.lineLayoutsList[pos.getLeafPos()];
      lineNumberDifference = llPoss.applyLineCountAdjustment(lineNumberDifference);
      return lineNumberDifference * this.constantLineHeight;
   }

   public void discardSpace(KnuthGlue spaceGlue) {
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      List returnList = new LinkedList();

      for(int p = 0; p < this.knuthParagraphs.size(); ++p) {
         LineLayoutPossibilities llPoss = this.lineLayoutsList[p];

         for(int i = 0; i < llPoss.getChosenLineCount(); ++i) {
            if (!((BlockLevelLayoutManager)this.parentLayoutManager).mustKeepTogether() && i >= this.fobj.getOrphans() && i <= llPoss.getChosenLineCount() - this.fobj.getWidows()) {
               returnList.add(new KnuthPenalty(0, 0, false, new Position(this), false));
            }

            LineBreakPosition lbp = (LineBreakPosition)llPoss.getChosenPosition(i);
            MinOptMax contentIPD;
            if (alignment == 70) {
               contentIPD = MinOptMax.getInstance(lbp.lineWidth - lbp.difference - lbp.availableShrink, lbp.lineWidth - lbp.difference, lbp.lineWidth - lbp.difference + lbp.availableStretch);
            } else if (alignment == 23) {
               contentIPD = MinOptMax.getInstance(lbp.lineWidth - 2 * lbp.startIndent);
            } else if (alignment == 39) {
               contentIPD = MinOptMax.getInstance(lbp.lineWidth - lbp.startIndent);
            } else {
               contentIPD = MinOptMax.getInstance(lbp.lineWidth - lbp.difference + lbp.startIndent);
            }

            returnList.add(new KnuthBlockBox(lbp.lineHeight, contentIPD, lbp.ipdAdjust != 0.0 ? lbp.lineWidth - lbp.difference : 0, lbp, false));
         }
      }

      return returnList;
   }

   private void findHyphenationPoints(Paragraph currPar) {
      ListIterator currParIterator = currPar.listIterator(currPar.ignoreAtStart);
      List updateList = new LinkedList();
      InlineLevelLayoutManager currLM = null;

      while(currParIterator.hasNext()) {
         KnuthElement firstElement = (KnuthElement)currParIterator.next();
         if (firstElement.getLayoutManager() != currLM) {
            currLM = (InlineLevelLayoutManager)firstElement.getLayoutManager();
            if (currLM == null) {
               break;
            }

            updateList.add(new Update(currLM, currParIterator.previousIndex()));
         } else if (currLM == null) {
            break;
         }

         if (firstElement.isBox() && !firstElement.isAuxiliary()) {
            int boxCount = 1;
            int auxCount = 0;
            StringBuffer sbChars = new StringBuffer();
            sbChars.append(currLM.getWordChars(firstElement.getPosition()));

            while(currParIterator.hasNext()) {
               KnuthElement nextElement = (KnuthElement)currParIterator.next();
               if (nextElement.isBox() && !nextElement.isAuxiliary()) {
                  if (currLM != nextElement.getLayoutManager()) {
                     currLM = (InlineLevelLayoutManager)nextElement.getLayoutManager();
                     updateList.add(new Update(currLM, currParIterator.previousIndex()));
                  }

                  ++boxCount;
                  sbChars.append(currLM.getWordChars(nextElement.getPosition()));
               } else {
                  if (!nextElement.isAuxiliary()) {
                     currParIterator.previous();
                     break;
                  }

                  if (currLM != nextElement.getLayoutManager()) {
                     currLM = (InlineLevelLayoutManager)nextElement.getLayoutManager();
                     updateList.add(new Update(currLM, currParIterator.previousIndex()));
                  }

                  ++auxCount;
               }
            }

            if (log.isTraceEnabled()) {
               log.trace(" Word to hyphenate: " + sbChars.toString());
            }

            HyphContext hc = this.getHyphenContext(sbChars);
            if (hc != null) {
               KnuthElement element = null;

               int i;
               for(i = 0; i < boxCount + auxCount; ++i) {
                  currParIterator.previous();
               }

               for(i = 0; i < boxCount + auxCount; ++i) {
                  element = (KnuthElement)currParIterator.next();
                  if (element.isBox() && !element.isAuxiliary()) {
                     ((InlineLevelLayoutManager)element.getLayoutManager()).hyphenate(element.getPosition(), hc);
                  }
               }
            }
         }
      }

      this.processUpdates(currPar, updateList);
   }

   private void processUpdates(Paragraph par, List updateList) {
      ListIterator updateListIterator = updateList.listIterator();
      int elementsAdded = 0;

      while(updateListIterator.hasNext()) {
         Update currUpdate = (Update)updateListIterator.next();
         int fromIndex = currUpdate.firstIndex;
         int toIndex;
         if (updateListIterator.hasNext()) {
            Update nextUpdate = (Update)updateListIterator.next();
            toIndex = nextUpdate.firstIndex;
            updateListIterator.previous();
         } else {
            toIndex = par.size() - par.ignoreAtEnd - elementsAdded;
         }

         if (currUpdate.inlineLM.applyChanges(par.subList(fromIndex + elementsAdded, toIndex + elementsAdded))) {
            List newElements = currUpdate.inlineLM.getChangedKnuthElements(par.subList(fromIndex + elementsAdded, toIndex + elementsAdded), this.effectiveAlignment);
            par.subList(fromIndex + elementsAdded, toIndex + elementsAdded).clear();
            par.addAll(fromIndex + elementsAdded, newElements);
            elementsAdded += newElements.size() - (toIndex - fromIndex);
         }
      }

      updateList.clear();
   }

   protected boolean hasLeadingFence(boolean isNotFirst) {
      return true;
   }

   protected boolean hasTrailingFence(boolean isNotLast) {
      return true;
   }

   private HyphContext getHyphenContext(StringBuffer sbChars) {
      Hyphenation hyph = Hyphenator.hyphenate(this.hyphenationProperties.language.getString(), this.hyphenationProperties.country.getString(), this.getFObj().getUserAgent().getFactory().getHyphenationTreeResolver(), sbChars.toString(), this.hyphenationProperties.hyphenationRemainCharacterCount.getValue(), this.hyphenationProperties.hyphenationPushCharacterCount.getValue());
      return hyph != null ? new HyphContext(hyph.getHyphenationPoints()) : null;
   }

   public void addAreas(PositionIterator parentIter, LayoutContext context) {
      while(parentIter.hasNext()) {
         Position pos = (Position)parentIter.next();
         boolean isLastPosition = !parentIter.hasNext();
         if (pos instanceof LineBreakPosition) {
            this.addInlineArea(context, (LineBreakPosition)pos, isLastPosition);
         } else if (pos instanceof NonLeafPosition && pos.generatesAreas()) {
            this.addBlockArea(context, pos, isLastPosition);
         }
      }

      this.setCurrentArea((Area)null);
   }

   private void addInlineArea(LayoutContext context, LineBreakPosition lbp, boolean isLastPosition) {
      LayoutManager lastLM = null;
      KnuthSequence seq = (KnuthSequence)this.knuthParagraphs.get(lbp.parIndex);
      int startElementIndex = lbp.startIndex;
      int endElementIndex = lbp.getLeafPos();
      LineArea lineArea = new LineArea(lbp.getLeafPos() < seq.size() - 1 ? this.textAlignment : this.textAlignmentLast, lbp.difference, lbp.availableStretch, lbp.availableShrink);
      if (lbp.startIndent != 0) {
         lineArea.addTrait(Trait.START_INDENT, new Integer(lbp.startIndent));
      }

      lineArea.setBPD(lbp.lineHeight);
      lineArea.setIPD(lbp.lineWidth);
      lineArea.addTrait(Trait.SPACE_BEFORE, new Integer(lbp.spaceBefore));
      lineArea.addTrait(Trait.SPACE_AFTER, new Integer(lbp.spaceAfter));
      this.alignmentContext.resizeLine(lbp.lineHeight, lbp.baseline);
      if (seq instanceof Paragraph) {
         Paragraph currPar = (Paragraph)seq;
         startElementIndex += startElementIndex == 0 ? currPar.ignoreAtStart : 0;
         if (endElementIndex == currPar.size() - 1) {
            endElementIndex -= currPar.ignoreAtEnd;
            lineArea.setIPD(lineArea.getIPD() - this.lastLineEndIndent.getValue(this));
         }
      }

      ListIterator seqIterator;
      if (this.whiteSpaceTreament == 63 || this.whiteSpaceTreament == 60 || this.whiteSpaceTreament == 62) {
         seqIterator = seq.listIterator(endElementIndex);
         KnuthElement lastElement = (KnuthElement)seqIterator.next();
         lastLM = lastElement.getLayoutManager();
         if (lastElement.isGlue()) {
            --endElementIndex;
            seqIterator.previous();
            if (seqIterator.hasPrevious()) {
               lastLM = ((KnuthElement)seqIterator.previous()).getLayoutManager();
            }
         }
      }

      if (this.whiteSpaceTreament == 63 || this.whiteSpaceTreament == 60 || this.whiteSpaceTreament == 61) {
         for(seqIterator = seq.listIterator(startElementIndex); seqIterator.hasNext() && !((KnuthElement)seqIterator.next()).isBox(); ++startElementIndex) {
         }
      }

      PositionIterator inlinePosIter = new KnuthPossPosIter(seq, startElementIndex, endElementIndex + 1);
      LayoutContext lc = new LayoutContext(0);
      lc.setAlignmentContext(this.alignmentContext);
      lc.setSpaceAdjust(lbp.dAdjust);
      lc.setIPDAdjust(lbp.ipdAdjust);
      lc.setLeadingSpace(new SpaceSpecifier(true));
      lc.setTrailingSpace(new SpaceSpecifier(false));
      lc.setFlags(256, true);
      this.setCurrentArea(lineArea);
      this.setChildContext(lc);

      LayoutManager childLM;
      while((childLM = inlinePosIter.getNextChildLM()) != null) {
         lc.setFlags(128, childLM == lastLM);
         childLM.addAreas(inlinePosIter, lc);
         lc.setLeadingSpace(lc.getTrailingSpace());
         lc.setTrailingSpace(new SpaceSpecifier(false));
      }

      if (context.getSpaceAfter() > 0 && (!context.isLastArea() || !isLastPosition)) {
         lineArea.setBPD(lineArea.getBPD() + context.getSpaceAfter());
      }

      lineArea.finalise();
      this.parentLayoutManager.addChildArea(lineArea);
   }

   private void addBlockArea(LayoutContext context, Position pos, boolean isLastPosition) {
      List positionList = new ArrayList(1);
      Position innerPosition = pos.getPosition();
      positionList.add(innerPosition);
      LayoutManager lastLM = null;
      if (isLastPosition) {
         lastLM = innerPosition.getLM();
      }

      LineArea lineArea = new LineArea();
      this.setCurrentArea(lineArea);
      LayoutContext lc = new LayoutContext(0);
      lc.setAlignmentContext(this.alignmentContext);
      this.setChildContext(lc);
      PositionIterator childPosIter = new InlineStackingLayoutManager.StackingIter(positionList.listIterator());
      LayoutContext blocklc = new LayoutContext(0);
      blocklc.setLeadingSpace(new SpaceSpecifier(true));
      blocklc.setTrailingSpace(new SpaceSpecifier(false));
      blocklc.setFlags(256, true);

      LayoutManager childLM;
      while((childLM = childPosIter.getNextChildLM()) != null) {
         blocklc.setFlags(128, context.isLastArea() && childLM == lastLM);
         blocklc.setStackLimitBP(context.getStackLimitBP());
         childLM.addAreas(childPosIter, blocklc);
         blocklc.setLeadingSpace(blocklc.getTrailingSpace());
         blocklc.setTrailingSpace(new SpaceSpecifier(false));
      }

      lineArea.updateExtentsFromChildren();
      this.parentLayoutManager.addChildArea(lineArea);
   }

   public void addChildArea(Area childArea) {
      if (childArea instanceof InlineArea) {
         Area parent = this.getCurrentArea();
         if (this.getContext().resolveLeadingSpace()) {
            this.addSpace(parent, this.getContext().getLeadingSpace().resolve(false), this.getContext().getSpaceAdjust());
         }

         parent.addChildArea(childArea);
      }

   }

   public boolean getGeneratesBlockArea() {
      return true;
   }

   public boolean getGeneratesLineArea() {
      return true;
   }

   public boolean isRestartable() {
      return true;
   }

   static {
      $assertionsDisabled = !LineLayoutManager.class.desiredAssertionStatus();
      log = LogFactory.getLog(LineLayoutManager.class);
   }

   private class LineBreakingAlgorithm extends BreakingAlgorithm {
      private LineLayoutManager thisLLM;
      private int pageAlignment;
      private int activePossibility;
      private int addedPositions;
      private int textIndent;
      private int lineHeight;
      private int lead;
      private int follow;
      private static final double MAX_DEMERITS = 1.0E7;

      public LineBreakingAlgorithm(int pageAlign, int textAlign, int textAlignLast, int indent, int fillerWidth, int lh, int ld, int fl, boolean first, int maxFlagCount, LineLayoutManager llm) {
         super(textAlign, textAlignLast, first, false, maxFlagCount);
         this.pageAlignment = pageAlign;
         this.textIndent = indent;
         this.lineHeight = lh;
         this.lead = ld;
         this.follow = fl;
         this.thisLLM = llm;
         this.activePossibility = -1;
      }

      public void updateData1(int lineCount, double demerits) {
         LineLayoutManager.this.lineLayouts.addPossibility(lineCount, demerits);
         if (log.isTraceEnabled()) {
            log.trace("Layout possibility in " + lineCount + " lines; break at position:");
         }

      }

      public void updateData2(BreakingAlgorithm.KnuthNode bestActiveNode, KnuthSequence par, int total) {
         int indent = 0;
         int difference = bestActiveNode.difference;
         int textAlign = bestActiveNode.line < total ? this.alignment : this.alignmentLast;
         indent += textAlign == 23 ? difference / 2 : (textAlign == 39 ? difference : 0);
         indent += bestActiveNode.line == 1 && this.indentFirstPart && LineLayoutManager.this.isFirstInBlock ? this.textIndent : 0;
         double ratio = textAlign != 70 && (difference >= 0 || -difference > bestActiveNode.availableShrink) ? 0.0 : bestActiveNode.adjustRatio;
         if (this.activePossibility == -1) {
            this.activePossibility = 0;
            this.addedPositions = 0;
         }

         if (this.addedPositions == LineLayoutManager.this.lineLayouts.getLineCount(this.activePossibility)) {
            ++this.activePossibility;
            this.addedPositions = 0;
         }

         if (log.isWarnEnabled()) {
            int lack = difference + bestActiveNode.availableShrink;
            if (lack < 0) {
               InlineLevelEventProducer eventProducer = InlineLevelEventProducer.Provider.get(LineLayoutManager.this.getFObj().getUserAgent().getEventBroadcaster());
               eventProducer.lineOverflows(this, bestActiveNode.line, -lack, LineLayoutManager.this.getFObj().getLocator());
            }
         }

         LineLayoutManager.this.lineLayouts.addBreakPosition(this.makeLineBreakPosition(par, bestActiveNode.line > 1 ? bestActiveNode.previous.position + 1 : 0, bestActiveNode.position, bestActiveNode.availableShrink - (this.addedPositions > 0 ? 0 : ((Paragraph)par).lineFiller.getShrink()), bestActiveNode.availableStretch, difference, ratio, indent), this.activePossibility);
         ++this.addedPositions;
      }

      public void resetAlgorithm() {
         this.activePossibility = -1;
      }

      private LineBreakPosition makeLineBreakPosition(KnuthSequence par, int firstElementIndex, int lastElementIndex, int availableShrink, int availableStretch, int difference, double ratio, int indent) {
         int spaceBefore = (this.lineHeight - this.lead - this.follow) / 2;
         int spaceAfter = this.lineHeight - this.lead - this.follow - spaceBefore;
         int lineLead = this.lead;
         int lineFollow = this.follow;
         boolean bZeroHeightLine = difference == LineLayoutManager.this.ipd;
         if (LineLayoutManager.this.fobj.getLineStackingStrategy() != 52) {
            ListIterator inlineIterator = par.listIterator(firstElementIndex);
            AlignmentContext lastAC = null;
            int maxIgnoredHeight = 0;

            for(int j = firstElementIndex; j <= lastElementIndex; ++j) {
               KnuthElement element = (KnuthElement)inlineIterator.next();
               if (element instanceof KnuthInlineBox) {
                  AlignmentContext ac = ((KnuthInlineBox)element).getAlignmentContext();
                  if (ac != null && lastAC != ac) {
                     if (!ac.usesInitialBaselineTable() || ac.getAlignmentBaselineIdentifier() != 14 && ac.getAlignmentBaselineIdentifier() != 4) {
                        if (LineLayoutManager.this.fobj.getLineHeightShiftAdjustment() == 30 || ac.getBaselineShiftValue() == 0) {
                           int alignmentOffset = ac.getTotalAlignmentBaselineOffset();
                           if (alignmentOffset + ac.getAltitude() > lineLead) {
                              lineLead = alignmentOffset + ac.getAltitude();
                           }

                           if (ac.getDepth() - alignmentOffset > lineFollow) {
                              lineFollow = ac.getDepth() - alignmentOffset;
                           }
                        }
                     } else if (ac.getHeight() > maxIgnoredHeight) {
                        maxIgnoredHeight = ac.getHeight();
                     }

                     lastAC = ac;
                  }

                  if (bZeroHeightLine && (!element.isAuxiliary() || ac != null && ac.getHeight() > 0)) {
                     bZeroHeightLine = false;
                  }
               }
            }

            if (lineFollow < maxIgnoredHeight - lineLead) {
               lineFollow = maxIgnoredHeight - lineLead;
            }
         }

         LineLayoutManager.this.constantLineHeight = lineLead + lineFollow;
         return bZeroHeightLine ? new LineBreakPosition(this.thisLLM, LineLayoutManager.this.knuthParagraphs.indexOf(par), firstElementIndex, lastElementIndex, availableShrink, availableStretch, difference, ratio, 0.0, indent, 0, LineLayoutManager.this.ipd, 0, 0, 0) : new LineBreakPosition(this.thisLLM, LineLayoutManager.this.knuthParagraphs.indexOf(par), firstElementIndex, lastElementIndex, availableShrink, availableStretch, difference, ratio, 0.0, indent, lineLead + lineFollow, LineLayoutManager.this.ipd, spaceBefore, spaceAfter, lineLead);
      }

      protected int filterActiveNodes() {
         BreakingAlgorithm.KnuthNode bestActiveNode = null;
         int i;
         BreakingAlgorithm.KnuthNode node;
         if (this.pageAlignment == 70) {
            for(i = this.startLine; i < this.endLine; ++i) {
               for(node = this.getNode(i); node != null; node = node.next) {
                  bestActiveNode = this.compareNodes(bestActiveNode, node);
               }
            }

            for(i = this.startLine; i < this.endLine; ++i) {
               for(node = this.getNode(i); node != null; node = node.next) {
                  if (node.line != bestActiveNode.line && node.totalDemerits > 1.0E7) {
                     this.removeNode(i, node);
                  }
               }
            }
         } else {
            for(i = this.startLine; i < this.endLine; ++i) {
               for(node = this.getNode(i); node != null; node = node.next) {
                  bestActiveNode = this.compareNodes(bestActiveNode, node);
                  if (node != bestActiveNode) {
                     this.removeNode(i, node);
                  }
               }
            }
         }

         return bestActiveNode.line;
      }
   }

   private class Paragraph extends InlineKnuthSequence {
      private int ignoreAtStart = 0;
      private int ignoreAtEnd = 0;
      private MinOptMax lineFiller;
      private int textAlignment;
      private int textAlignmentLast;
      private int textIndent;
      private int lastLineEndIndent;
      private LineLayoutManager layoutManager;

      Paragraph(LineLayoutManager llm, int alignment, int alignmentLast, int indent, int endIndent) {
         this.layoutManager = llm;
         this.textAlignment = alignment;
         this.textAlignmentLast = alignmentLast;
         this.textIndent = indent;
         this.lastLineEndIndent = endIndent;
      }

      public void startSequence() {
         if (this.textAlignment == 23) {
            this.lineFiller = MinOptMax.getInstance(this.lastLineEndIndent);
         } else {
            this.lineFiller = MinOptMax.getInstance(this.lastLineEndIndent, this.lastLineEndIndent, this.layoutManager.ipd);
         }

         if (this.textAlignment == 23 && this.textAlignmentLast != 70) {
            this.add(new KnuthGlue(0, 10008, 0, (Position)null, false));
            ++this.ignoreAtStart;
         }

         if (LineLayoutManager.this.isFirstInBlock && LineLayoutManager.this.knuthParagraphs.size() == 0 && this.textIndent != 0) {
            this.add(new KnuthInlineBox(this.textIndent, (AlignmentContext)null, (Position)null, false));
            ++this.ignoreAtStart;
         }

      }

      public void endParagraph() {
         KnuthSequence finishedPar = this.endSequence();
         if (finishedPar != null) {
            LineLayoutManager.this.knuthParagraphs.add(finishedPar);
         }

      }

      public KnuthSequence endSequence() {
         if (this.size() <= this.ignoreAtStart) {
            this.clear();
            return null;
         } else {
            if (this.textAlignment == 23 && this.textAlignmentLast != 70) {
               this.add(new KnuthGlue(0, 10008, 0, (Position)null, false));
               this.add(new KnuthPenalty(this.lineFiller.getOpt(), -1000, false, (Position)null, false));
               this.ignoreAtEnd = 2;
            } else if (this.textAlignmentLast != 70) {
               this.add(new KnuthPenalty(0, 1000, false, (Position)null, false));
               this.add(new KnuthGlue(0, this.lineFiller.getStretch(), this.lineFiller.getShrink(), (Position)null, false));
               this.add(new KnuthPenalty(this.lineFiller.getOpt(), -1000, false, (Position)null, false));
               this.ignoreAtEnd = 3;
            } else {
               this.add(new KnuthPenalty(this.lineFiller.getOpt(), -1000, false, (Position)null, false));
               this.ignoreAtEnd = 1;
            }

            return this;
         }
      }

      public boolean containsBox() {
         for(int i = 0; i < this.size(); ++i) {
            KnuthElement el = (KnuthElement)this.get(i);
            if (el.isBox()) {
               return true;
            }
         }

         return false;
      }
   }

   private class Update {
      private InlineLevelLayoutManager inlineLM;
      private int firstIndex;

      public Update(InlineLevelLayoutManager lm, int index) {
         this.inlineLM = lm;
         this.firstIndex = index;
      }
   }

   private static class LineBreakPosition extends LeafPosition {
      private int parIndex;
      private int startIndex;
      private int availableShrink;
      private int availableStretch;
      private int difference;
      private double dAdjust;
      private double ipdAdjust;
      private int startIndent;
      private int lineHeight;
      private int lineWidth;
      private int spaceBefore;
      private int spaceAfter;
      private int baseline;

      LineBreakPosition(LayoutManager lm, int index, int startIndex, int breakIndex, int shrink, int stretch, int diff, double ipdA, double adjust, int ind, int lh, int lw, int sb, int sa, int bl) {
         super(lm, breakIndex);
         this.availableShrink = shrink;
         this.availableStretch = stretch;
         this.difference = diff;
         this.parIndex = index;
         this.startIndex = startIndex;
         this.ipdAdjust = ipdA;
         this.dAdjust = adjust;
         this.startIndent = ind;
         this.lineHeight = lh;
         this.lineWidth = lw;
         this.spaceBefore = sb;
         this.spaceAfter = sa;
         this.baseline = bl;
      }
   }
}
