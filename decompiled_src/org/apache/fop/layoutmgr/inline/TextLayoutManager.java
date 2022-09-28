package org.apache.fop.layoutmgr.inline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.properties.StructurePointerPropertySet;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontSelector;
import org.apache.fop.layoutmgr.InlineKnuthSequence;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.KnuthSequence;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.text.linebreak.LineBreakStatus;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;
import org.apache.fop.util.CharUtilities;
import org.apache.fop.util.ListUtil;

public class TextLayoutManager extends LeafNodeLayoutManager {
   private static final int SOFT_HYPHEN_PENALTY = 1;
   private static final Log LOG;
   private final List areaInfos;
   private static final String BREAK_CHARS = "-/";
   private final FOText foText;
   private final MinOptMax[] letterAdjustArray;
   private Font spaceFont = null;
   private int nextStart = 0;
   private int spaceCharIPD;
   private MinOptMax wordSpaceIPD;
   private MinOptMax letterSpaceIPD;
   private int hyphIPD;
   private boolean hasChanged = false;
   private int returnedIndex = 0;
   private int thisStart = 0;
   private int tempStart = 0;
   private List changeList = new LinkedList();
   private AlignmentContext alignmentContext = null;
   private int lineStartBAP = 0;
   private int lineEndBAP = 0;
   private boolean keepTogether;
   private final Position auxiliaryPosition = new LeafPosition(this, -1);
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public TextLayoutManager(FOText node) {
      this.foText = node;
      this.letterAdjustArray = new MinOptMax[node.length() + 1];
      this.areaInfos = new ArrayList();
   }

   private KnuthPenalty makeZeroWidthPenalty(int penaltyValue) {
      return new KnuthPenalty(0, penaltyValue, false, this.auxiliaryPosition, true);
   }

   private KnuthBox makeAuxiliaryZeroWidthBox() {
      return new KnuthInlineBox(0, (AlignmentContext)null, this.notifyPos(new LeafPosition(this, -1)), true);
   }

   public void initialize() {
      this.foText.resetBuffer();
      this.spaceFont = FontSelector.selectFontForCharacterInText(' ', this.foText, this);
      this.spaceCharIPD = this.spaceFont.getCharWidth(' ');
      this.hyphIPD = this.foText.getCommonHyphenation().getHyphIPD(this.spaceFont);
      SpaceVal letterSpacing = SpaceVal.makeLetterSpacing(this.foText.getLetterSpacing());
      SpaceVal wordSpacing = SpaceVal.makeWordSpacing(this.foText.getWordSpacing(), letterSpacing, this.spaceFont);
      this.letterSpaceIPD = letterSpacing.getSpace();
      this.wordSpaceIPD = MinOptMax.getInstance(this.spaceCharIPD).plus(wordSpacing.getSpace());
      this.keepTogether = this.foText.getKeepTogether().getWithinLine().getEnum() == 7;
   }

   public void addAreas(PositionIterator posIter, LayoutContext context) {
      int wordSpaceCount = 0;
      int letterSpaceCount = 0;
      int firstAreaInfoIndex = -1;
      int lastAreaInfoIndex = 0;
      MinOptMax realWidth = MinOptMax.ZERO;
      AreaInfo lastAreaInfo = null;

      while(true) {
         LeafPosition tbpNext;
         do {
            do {
               if (!posIter.hasNext()) {
                  if (lastAreaInfo != null) {
                     this.addAreaInfoAreas(lastAreaInfo, wordSpaceCount, letterSpaceCount, firstAreaInfoIndex, lastAreaInfoIndex, realWidth, context);
                  }

                  return;
               }

               tbpNext = (LeafPosition)posIter.next();
            } while(tbpNext == null);
         } while(tbpNext.getLeafPos() == -1);

         AreaInfo areaInfo = (AreaInfo)this.areaInfos.get(tbpNext.getLeafPos());
         if (lastAreaInfo == null || areaInfo.font != lastAreaInfo.font) {
            if (lastAreaInfo != null) {
               this.addAreaInfoAreas(lastAreaInfo, wordSpaceCount, letterSpaceCount, firstAreaInfoIndex, lastAreaInfoIndex, realWidth, context);
            }

            firstAreaInfoIndex = tbpNext.getLeafPos();
            wordSpaceCount = 0;
            letterSpaceCount = 0;
            realWidth = MinOptMax.ZERO;
         }

         wordSpaceCount += areaInfo.wordSpaceCount;
         letterSpaceCount += areaInfo.letterSpaceCount;
         realWidth = realWidth.plus(areaInfo.areaIPD);
         lastAreaInfoIndex = tbpNext.getLeafPos();
         lastAreaInfo = areaInfo;
      }
   }

   private void addAreaInfoAreas(AreaInfo areaInfo, int wordSpaceCount, int letterSpaceCount, int firstAreaInfoIndex, int lastAreaInfoIndex, MinOptMax realWidth, LayoutContext context) {
      int textLength = areaInfo.getCharLength();
      if (areaInfo.letterSpaceCount == textLength && !areaInfo.isHyphenated && context.isLastArea()) {
         realWidth = realWidth.minus(this.letterSpaceIPD);
         --letterSpaceCount;
      }

      for(int i = areaInfo.startIndex; i < areaInfo.breakIndex; ++i) {
         MinOptMax letterAdjustment = this.letterAdjustArray[i + 1];
         if (letterAdjustment != null && letterAdjustment.isElastic()) {
            ++letterSpaceCount;
         }
      }

      if (context.isLastArea() && areaInfo.isHyphenated) {
         realWidth = realWidth.plus(this.hyphIPD);
      }

      double ipdAdjust = context.getIPDAdjust();
      int difference;
      if (ipdAdjust > 0.0) {
         difference = (int)((double)realWidth.getStretch() * ipdAdjust);
      } else {
         difference = (int)((double)realWidth.getShrink() * ipdAdjust);
      }

      int letterSpaceDim = this.letterSpaceIPD.getOpt();
      if (ipdAdjust > 0.0) {
         letterSpaceDim += (int)((double)this.letterSpaceIPD.getStretch() * ipdAdjust);
      } else {
         letterSpaceDim += (int)((double)this.letterSpaceIPD.getShrink() * ipdAdjust);
      }

      int totalAdjust = (letterSpaceDim - this.letterSpaceIPD.getOpt()) * letterSpaceCount;
      int wordSpaceDim = this.wordSpaceIPD.getOpt();
      if (wordSpaceCount > 0) {
         wordSpaceDim += (difference - totalAdjust) / wordSpaceCount;
      }

      totalAdjust += (wordSpaceDim - this.wordSpaceIPD.getOpt()) * wordSpaceCount;
      if (totalAdjust != difference) {
         LOG.trace("TextLM.addAreas: error in word / letter space adjustment = " + (totalAdjust - difference));
         totalAdjust = difference;
      }

      TextArea textArea = (new TextAreaBuilder(realWidth, totalAdjust, context, firstAreaInfoIndex, lastAreaInfoIndex, context.isLastArea(), areaInfo.font)).build();
      textArea.setTextLetterSpaceAdjust(letterSpaceDim);
      textArea.setTextWordSpaceAdjust(wordSpaceDim - this.spaceCharIPD - 2 * textArea.getTextLetterSpaceAdjust());
      if (context.getIPDAdjust() != 0.0) {
         textArea.setSpaceDifference(this.wordSpaceIPD.getOpt() - this.spaceCharIPD - 2 * textArea.getTextLetterSpaceAdjust());
      }

      this.parentLayoutManager.addChildArea(textArea);
   }

   private String getPtr() {
      FObj fobj = this.parentLayoutManager.getFObj();
      return fobj instanceof StructurePointerPropertySet ? ((StructurePointerPropertySet)fobj).getPtr() : null;
   }

   private AreaInfo getAreaInfo(int index) {
      return (AreaInfo)this.areaInfos.get(index);
   }

   private void addToLetterAdjust(int index, int width) {
      if (this.letterAdjustArray[index] == null) {
         this.letterAdjustArray[index] = MinOptMax.getInstance(width);
      } else {
         this.letterAdjustArray[index] = this.letterAdjustArray[index].plus(width);
      }

   }

   private static boolean isSpace(char ch) {
      return ch == ' ' || CharUtilities.isNonBreakableSpace(ch) || CharUtilities.isFixedWidthSpace(ch);
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      this.lineStartBAP = context.getLineStartBorderAndPaddingWidth();
      this.lineEndBAP = context.getLineEndBorderAndPaddingWidth();
      this.alignmentContext = context.getAlignmentContext();
      List returnList = new LinkedList();
      KnuthSequence sequence = new InlineKnuthSequence();
      AreaInfo areaInfo = null;
      AreaInfo prevAreaInfo = null;
      returnList.add(sequence);
      LineBreakStatus lineBreakStatus = new LineBreakStatus();
      this.thisStart = this.nextStart;
      boolean inWord = false;
      boolean inWhitespace = false;

      char ch;
      for(ch = 0; this.nextStart < this.foText.length(); ++this.nextStart) {
         ch = this.foText.charAt(this.nextStart);
         boolean breakOpportunity = false;
         byte breakAction = this.keepTogether ? 4 : lineBreakStatus.nextChar(ch);
         switch (breakAction) {
            case 0:
            case 1:
            case 2:
               breakOpportunity = true;
            case 3:
            case 4:
            case 5:
               break;
            default:
               LOG.error("Unexpected breakAction: " + breakAction);
         }

         if (inWord) {
            if (breakOpportunity || isSpace(ch) || CharUtilities.isExplicitBreak(ch)) {
               prevAreaInfo = this.processWord(alignment, (KnuthSequence)sequence, prevAreaInfo, ch, breakOpportunity, true);
            }
         } else if (inWhitespace) {
            if (ch != ' ' || breakOpportunity) {
               prevAreaInfo = this.processWhitespace(alignment, (KnuthSequence)sequence, breakOpportunity);
            }
         } else {
            if (areaInfo != null) {
               prevAreaInfo = areaInfo;
               this.processLeftoverAreaInfo(alignment, (KnuthSequence)sequence, areaInfo, ch == ' ' || breakOpportunity);
               areaInfo = null;
            }

            if (breakAction == 5) {
               sequence = this.processLinebreak(returnList, (KnuthSequence)sequence);
            }
         }

         if ((ch != ' ' || this.foText.getWhitespaceTreatment() != 108) && ch != 160) {
            if (!CharUtilities.isFixedWidthSpace(ch) && !CharUtilities.isZeroWidthSpace(ch)) {
               if (CharUtilities.isExplicitBreak(ch)) {
                  this.thisStart = this.nextStart + 1;
               }
            } else {
               Font font = FontSelector.selectFontForCharacterInText(ch, this.foText, this);
               MinOptMax ipd = MinOptMax.getInstance(font.getCharWidth(ch));
               areaInfo = new AreaInfo(this.nextStart, this.nextStart + 1, 0, 0, ipd, false, true, breakOpportunity, font);
               this.thisStart = this.nextStart + 1;
            }
         } else {
            areaInfo = new AreaInfo(this.nextStart, this.nextStart + 1, 1, 0, this.wordSpaceIPD, false, true, breakOpportunity, this.spaceFont);
            this.thisStart = this.nextStart + 1;
         }

         inWord = !isSpace(ch) && !CharUtilities.isExplicitBreak(ch);
         inWhitespace = ch == ' ' && this.foText.getWhitespaceTreatment() != 108;
      }

      if (inWord) {
         this.processWord(alignment, (KnuthSequence)sequence, prevAreaInfo, ch, false, false);
      } else if (inWhitespace) {
         this.processWhitespace(alignment, (KnuthSequence)sequence, true);
      } else if (areaInfo != null) {
         this.processLeftoverAreaInfo(alignment, (KnuthSequence)sequence, areaInfo, ch == 8203);
      } else if (CharUtilities.isExplicitBreak(ch)) {
         this.processLinebreak(returnList, (KnuthSequence)sequence);
      }

      if (((List)ListUtil.getLast(returnList)).isEmpty()) {
         ListUtil.removeLast(returnList);
      }

      this.setFinished(true);
      if (returnList.isEmpty()) {
         return null;
      } else {
         return returnList;
      }
   }

   private KnuthSequence processLinebreak(List returnList, KnuthSequence sequence) {
      if (this.lineEndBAP != 0) {
         sequence.add(new KnuthGlue(this.lineEndBAP, 0, 0, this.auxiliaryPosition, true));
      }

      sequence.endSequence();
      KnuthSequence sequence = new InlineKnuthSequence();
      returnList.add(sequence);
      return sequence;
   }

   private void processLeftoverAreaInfo(int alignment, KnuthSequence sequence, AreaInfo areaInfo, boolean breakOpportunityAfter) {
      this.areaInfos.add(areaInfo);
      areaInfo.breakOppAfter = breakOpportunityAfter;
      this.addElementsForASpace(sequence, alignment, areaInfo, this.areaInfos.size() - 1);
   }

   private AreaInfo processWhitespace(int alignment, KnuthSequence sequence, boolean breakOpportunity) {
      if (!$assertionsDisabled && this.nextStart < this.thisStart) {
         throw new AssertionError();
      } else {
         AreaInfo areaInfo = new AreaInfo(this.thisStart, this.nextStart, this.nextStart - this.thisStart, 0, this.wordSpaceIPD.mult(this.nextStart - this.thisStart), false, true, breakOpportunity, this.spaceFont);
         this.areaInfos.add(areaInfo);
         this.addElementsForASpace(sequence, alignment, areaInfo, this.areaInfos.size() - 1);
         this.thisStart = this.nextStart;
         return areaInfo;
      }
   }

   private AreaInfo processWord(int alignment, KnuthSequence sequence, AreaInfo prevAreaInfo, char ch, boolean breakOpportunity, boolean checkEndsWithHyphen) {
      int lastIndex;
      for(lastIndex = this.nextStart; lastIndex > 0 && this.foText.charAt(lastIndex - 1) == 173; --lastIndex) {
      }

      boolean endsWithHyphen = checkEndsWithHyphen && this.foText.charAt(lastIndex) == 173;
      Font font = FontSelector.selectFontForCharactersInText(this.foText, this.thisStart, lastIndex, this.foText, this);
      int wordLength = lastIndex - this.thisStart;
      boolean kerning = font.hasKerning();
      MinOptMax wordIPD = MinOptMax.ZERO;

      int i;
      for(i = this.thisStart; i < lastIndex; ++i) {
         char currentChar = this.foText.charAt(i);
         int charWidth = font.getCharWidth(currentChar);
         wordIPD = wordIPD.plus(charWidth);
         if (kerning) {
            int kern = 0;
            char previousChar;
            if (i > this.thisStart) {
               previousChar = this.foText.charAt(i - 1);
               kern = font.getKernValue(previousChar, currentChar);
            } else if (prevAreaInfo != null && !prevAreaInfo.isSpace && prevAreaInfo.breakIndex > 0) {
               previousChar = this.foText.charAt(prevAreaInfo.breakIndex - 1);
               kern = font.getKernValue(previousChar, currentChar);
            }

            if (kern != 0) {
               this.addToLetterAdjust(i, kern);
               wordIPD = wordIPD.plus(kern);
            }
         }
      }

      if (kerning && breakOpportunity && !isSpace(ch) && lastIndex > 0 && endsWithHyphen) {
         i = font.getKernValue(this.foText.charAt(lastIndex - 1), ch);
         if (i != 0) {
            this.addToLetterAdjust(lastIndex, i);
         }
      }

      i = wordLength - 1;
      if (breakOpportunity && !isSpace(ch)) {
         ++i;
      }

      if (!$assertionsDisabled && i < 0) {
         throw new AssertionError();
      } else {
         wordIPD = wordIPD.plus(this.letterSpaceIPD.mult(i));
         AreaInfo areaInfo = new AreaInfo(this.thisStart, lastIndex, 0, i, wordIPD, endsWithHyphen, false, breakOpportunity, font);
         this.areaInfos.add(areaInfo);
         this.tempStart = this.nextStart;
         this.addElementsForAWordFragment(sequence, alignment, areaInfo, this.areaInfos.size() - 1);
         this.thisStart = this.nextStart;
         return areaInfo;
      }
   }

   public List addALetterSpaceTo(List oldList) {
      ListIterator oldListIterator = oldList.listIterator();
      KnuthElement knuthElement = (KnuthElement)oldListIterator.next();
      LeafPosition pos = (LeafPosition)((KnuthBox)knuthElement).getPosition();
      int index = pos.getLeafPos();
      if (index > -1) {
         AreaInfo areaInfo = this.getAreaInfo(index);
         areaInfo.letterSpaceCount++;
         areaInfo.addToAreaIPD(this.letterSpaceIPD);
         if ("-/".indexOf(this.foText.charAt(this.tempStart - 1)) >= 0) {
            oldListIterator = oldList.listIterator(oldList.size());
            oldListIterator.add(new KnuthPenalty(0, 50, true, this.auxiliaryPosition, false));
            oldListIterator.add(new KnuthGlue(this.letterSpaceIPD, this.auxiliaryPosition, false));
         } else if (this.letterSpaceIPD.isStiff()) {
            oldListIterator.set(new KnuthInlineBox(areaInfo.areaIPD.getOpt(), this.alignmentContext, pos, false));
         } else {
            oldListIterator.next();
            oldListIterator.next();
            oldListIterator.set(new KnuthGlue(this.letterSpaceIPD.mult(areaInfo.letterSpaceCount), this.auxiliaryPosition, true));
         }
      }

      return oldList;
   }

   public void removeWordSpace(List oldList) {
      ListIterator oldListIterator = oldList.listIterator();
      if (((KnuthElement)((LinkedList)oldList).getFirst()).isPenalty()) {
         oldListIterator.next();
      }

      if (oldList.size() > 2) {
         oldListIterator.next();
         oldListIterator.next();
      }

      KnuthElement knuthElement = (KnuthElement)oldListIterator.next();
      int leafValue = ((LeafPosition)knuthElement.getPosition()).getLeafPos();
      if (leafValue == this.areaInfos.size() - 1) {
         this.areaInfos.remove(leafValue);
      } else {
         LOG.error("trying to remove a non-trailing word space");
      }

   }

   public void hyphenate(Position pos, HyphContext hyphContext) {
      AreaInfo areaInfo = this.getAreaInfo(((LeafPosition)pos).getLeafPos());
      int startIndex = areaInfo.startIndex;
      boolean nothingChanged = true;

      int stopIndex;
      for(Font font = areaInfo.font; startIndex < areaInfo.breakIndex; startIndex = stopIndex) {
         MinOptMax newIPD = MinOptMax.ZERO;
         stopIndex = startIndex + hyphContext.getNextHyphPoint();
         boolean hyphenFollows;
         if (hyphContext.hasMoreHyphPoints() && stopIndex <= areaInfo.breakIndex) {
            hyphenFollows = true;
         } else {
            hyphenFollows = false;
            stopIndex = areaInfo.breakIndex;
         }

         hyphContext.updateOffset(stopIndex - startIndex);

         int letterSpaceCount;
         for(int i = startIndex; i < stopIndex; ++i) {
            letterSpaceCount = this.foText.charAt(i);
            newIPD = newIPD.plus(font.getCharWidth((char)letterSpaceCount));
            if (i < stopIndex) {
               MinOptMax letterAdjust = this.letterAdjustArray[i + 1];
               if (i == stopIndex - 1 && hyphenFollows) {
                  letterAdjust = null;
               }

               if (letterAdjust != null) {
                  newIPD = newIPD.plus(letterAdjust);
               }
            }
         }

         boolean isWordEnd = stopIndex == areaInfo.breakIndex && areaInfo.letterSpaceCount < areaInfo.getCharLength();
         letterSpaceCount = isWordEnd ? stopIndex - startIndex - 1 : stopIndex - startIndex;
         if (!$assertionsDisabled && letterSpaceCount < 0) {
            throw new AssertionError();
         }

         newIPD = newIPD.plus(this.letterSpaceIPD.mult(letterSpaceCount));
         if (!nothingChanged || stopIndex != areaInfo.breakIndex || hyphenFollows) {
            this.changeList.add(new PendingChange(new AreaInfo(startIndex, stopIndex, 0, letterSpaceCount, newIPD, hyphenFollows, false, false, font), ((LeafPosition)pos).getLeafPos()));
            nothingChanged = false;
         }
      }

      this.hasChanged |= !nothingChanged;
   }

   public boolean applyChanges(List oldList) {
      this.setFinished(false);
      if (!this.changeList.isEmpty()) {
         int areaInfosAdded = 0;
         int areaInfosRemoved = 0;
         int oldIndex = -1;

         int changeIndex;
         PendingChange currChange;
         for(ListIterator changeListIterator = this.changeList.listIterator(); changeListIterator.hasNext(); this.areaInfos.add(changeIndex, currChange.areaInfo)) {
            currChange = (PendingChange)changeListIterator.next();
            if (currChange.index == oldIndex) {
               ++areaInfosAdded;
               changeIndex = currChange.index + areaInfosAdded - areaInfosRemoved;
            } else {
               ++areaInfosRemoved;
               ++areaInfosAdded;
               oldIndex = currChange.index;
               changeIndex = currChange.index + areaInfosAdded - areaInfosRemoved;
               this.areaInfos.remove(changeIndex);
            }
         }

         this.changeList.clear();
      }

      this.returnedIndex = 0;
      return this.hasChanged;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      if (this.isFinished()) {
         return null;
      } else {
         LinkedList returnList;
         for(returnList = new LinkedList(); this.returnedIndex < this.areaInfos.size(); ++this.returnedIndex) {
            AreaInfo areaInfo = this.getAreaInfo(this.returnedIndex);
            if (areaInfo.wordSpaceCount == 0) {
               this.addElementsForAWordFragment(returnList, alignment, areaInfo, this.returnedIndex);
            } else {
               this.addElementsForASpace(returnList, alignment, areaInfo, this.returnedIndex);
            }
         }

         this.setFinished(true);
         return returnList;
      }
   }

   public String getWordChars(Position pos) {
      int leafValue = ((LeafPosition)pos).getLeafPos();
      if (leafValue == -1) {
         return "";
      } else {
         AreaInfo areaInfo = this.getAreaInfo(leafValue);
         StringBuffer buffer = new StringBuffer(areaInfo.getCharLength());

         for(int i = areaInfo.startIndex; i < areaInfo.breakIndex; ++i) {
            buffer.append(this.foText.charAt(i));
         }

         return buffer.toString();
      }
   }

   private void addElementsForASpace(List baseList, int alignment, AreaInfo areaInfo, int leafValue) {
      LeafPosition mainPosition = new LeafPosition(this, leafValue);
      if (!areaInfo.breakOppAfter) {
         if (alignment == 70) {
            baseList.add(this.makeAuxiliaryZeroWidthBox());
            baseList.add(this.makeZeroWidthPenalty(1000));
            baseList.add(new KnuthGlue(areaInfo.areaIPD, mainPosition, false));
         } else {
            baseList.add(new KnuthInlineBox(areaInfo.areaIPD.getOpt(), (AlignmentContext)null, mainPosition, true));
         }
      } else if (this.foText.charAt(areaInfo.startIndex) == ' ' && this.foText.getWhitespaceTreatment() != 108) {
         baseList.addAll(this.getElementsForBreakingSpace(alignment, areaInfo, mainPosition, areaInfo.areaIPD.getOpt(), this.auxiliaryPosition, 0, false));
      } else {
         baseList.addAll(this.getElementsForBreakingSpace(alignment, areaInfo, this.auxiliaryPosition, 0, mainPosition, areaInfo.areaIPD.getOpt(), true));
      }

   }

   private List getElementsForBreakingSpace(int alignment, AreaInfo areaInfo, Position pos2, int p2WidthOffset, Position pos3, int p3WidthOffset, boolean skipZeroCheck) {
      List elements = new ArrayList();
      switch (alignment) {
         case 23:
            elements.add(new KnuthGlue(this.lineEndBAP, 10008, 0, this.auxiliaryPosition, false));
            elements.add(this.makeZeroWidthPenalty(0));
            elements.add(new KnuthGlue(p2WidthOffset - (this.lineStartBAP + this.lineEndBAP), -20016, 0, pos2, false));
            elements.add(this.makeAuxiliaryZeroWidthBox());
            elements.add(this.makeZeroWidthPenalty(1000));
            elements.add(new KnuthGlue(this.lineStartBAP + p3WidthOffset, 10008, 0, pos3, false));
            break;
         case 39:
         case 135:
            if (!skipZeroCheck && this.lineStartBAP == 0 && this.lineEndBAP == 0) {
               elements.add(new KnuthGlue(0, 10008, 0, this.auxiliaryPosition, false));
               elements.add(this.makeZeroWidthPenalty(0));
               elements.add(new KnuthGlue(areaInfo.areaIPD.getOpt(), -10008, 0, pos2, false));
            } else {
               elements.add(new KnuthGlue(this.lineEndBAP, 10008, 0, this.auxiliaryPosition, false));
               elements.add(this.makeZeroWidthPenalty(0));
               elements.add(new KnuthGlue(p2WidthOffset - (this.lineStartBAP + this.lineEndBAP), -10008, 0, pos2, false));
               elements.add(this.makeAuxiliaryZeroWidthBox());
               elements.add(this.makeZeroWidthPenalty(1000));
               elements.add(new KnuthGlue(this.lineStartBAP + p3WidthOffset, 0, 0, pos3, false));
            }
            break;
         case 70:
            elements.addAll(this.getElementsForJustifiedText(areaInfo, pos2, p2WidthOffset, pos3, p3WidthOffset, skipZeroCheck, areaInfo.areaIPD.getShrink()));
            break;
         default:
            elements.addAll(this.getElementsForJustifiedText(areaInfo, pos2, p2WidthOffset, pos3, p3WidthOffset, skipZeroCheck, 0));
      }

      return elements;
   }

   private List getElementsForJustifiedText(AreaInfo areaInfo, Position pos2, int p2WidthOffset, Position pos3, int p3WidthOffset, boolean skipZeroCheck, int shrinkability) {
      int stretchability = areaInfo.areaIPD.getStretch();
      List elements = new ArrayList();
      if (!skipZeroCheck && this.lineStartBAP == 0 && this.lineEndBAP == 0) {
         elements.add(new KnuthGlue(areaInfo.areaIPD.getOpt(), stretchability, shrinkability, pos2, false));
      } else {
         elements.add(new KnuthGlue(this.lineEndBAP, 0, 0, this.auxiliaryPosition, false));
         elements.add(this.makeZeroWidthPenalty(0));
         elements.add(new KnuthGlue(p2WidthOffset - (this.lineStartBAP + this.lineEndBAP), stretchability, shrinkability, pos2, false));
         elements.add(this.makeAuxiliaryZeroWidthBox());
         elements.add(this.makeZeroWidthPenalty(1000));
         elements.add(new KnuthGlue(this.lineStartBAP + p3WidthOffset, 0, 0, pos3, false));
      }

      return elements;
   }

   private void addElementsForAWordFragment(List baseList, int alignment, AreaInfo areaInfo, int leafValue) {
      LeafPosition mainPosition = new LeafPosition(this, leafValue);
      boolean suppressibleLetterSpace = areaInfo.breakOppAfter && !areaInfo.isHyphenated;
      if (this.letterSpaceIPD.isStiff()) {
         baseList.add(new KnuthInlineBox(suppressibleLetterSpace ? areaInfo.areaIPD.getOpt() - this.letterSpaceIPD.getOpt() : areaInfo.areaIPD.getOpt(), this.alignmentContext, this.notifyPos(mainPosition), false));
      } else {
         int unsuppressibleLetterSpaces = suppressibleLetterSpace ? areaInfo.letterSpaceCount - 1 : areaInfo.letterSpaceCount;
         baseList.add(new KnuthInlineBox(areaInfo.areaIPD.getOpt() - areaInfo.letterSpaceCount * this.letterSpaceIPD.getOpt(), this.alignmentContext, this.notifyPos(mainPosition), false));
         baseList.add(this.makeZeroWidthPenalty(1000));
         baseList.add(new KnuthGlue(this.letterSpaceIPD.mult(unsuppressibleLetterSpaces), this.auxiliaryPosition, true));
         baseList.add(this.makeAuxiliaryZeroWidthBox());
      }

      if (areaInfo.isHyphenated) {
         MinOptMax widthIfNoBreakOccurs = null;
         if (areaInfo.breakIndex < this.foText.length()) {
            widthIfNoBreakOccurs = this.letterAdjustArray[areaInfo.breakIndex];
         }

         this.addElementsForAHyphen(baseList, alignment, this.hyphIPD, widthIfNoBreakOccurs, areaInfo.breakOppAfter && areaInfo.isHyphenated);
      } else if (suppressibleLetterSpace) {
         this.addElementsForAHyphen(baseList, alignment, 0, this.letterSpaceIPD, true);
      }

   }

   private void addElementsForAHyphen(List baseList, int alignment, int widthIfBreakOccurs, MinOptMax widthIfNoBreakOccurs, boolean unflagged) {
      if (widthIfNoBreakOccurs == null) {
         widthIfNoBreakOccurs = MinOptMax.ZERO;
      }

      switch (alignment) {
         case 23:
            baseList.add(this.makeZeroWidthPenalty(1000));
            baseList.add(new KnuthGlue(this.lineEndBAP, 10008, 0, this.auxiliaryPosition, true));
            baseList.add(new KnuthPenalty(this.hyphIPD, unflagged ? 1 : 50, !unflagged, this.auxiliaryPosition, false));
            baseList.add(new KnuthGlue(-(this.lineEndBAP + this.lineStartBAP), -20016, 0, this.auxiliaryPosition, false));
            baseList.add(this.makeAuxiliaryZeroWidthBox());
            baseList.add(this.makeZeroWidthPenalty(1000));
            baseList.add(new KnuthGlue(this.lineStartBAP, 10008, 0, this.auxiliaryPosition, true));
            break;
         case 39:
         case 135:
            if (this.lineStartBAP == 0 && this.lineEndBAP == 0) {
               baseList.add(this.makeZeroWidthPenalty(1000));
               baseList.add(new KnuthGlue(0, 10008, 0, this.auxiliaryPosition, false));
               baseList.add(new KnuthPenalty(widthIfBreakOccurs, unflagged ? 1 : 50, !unflagged, this.auxiliaryPosition, false));
               baseList.add(new KnuthGlue(widthIfNoBreakOccurs.getOpt(), -10008, 0, this.auxiliaryPosition, false));
            } else {
               baseList.add(this.makeZeroWidthPenalty(1000));
               baseList.add(new KnuthGlue(this.lineEndBAP, 10008, 0, this.auxiliaryPosition, false));
               baseList.add(new KnuthPenalty(widthIfBreakOccurs, unflagged ? 1 : 50, !unflagged, this.auxiliaryPosition, false));
               baseList.add(new KnuthGlue(widthIfNoBreakOccurs.getOpt() - (this.lineStartBAP + this.lineEndBAP), -10008, 0, this.auxiliaryPosition, false));
               baseList.add(this.makeAuxiliaryZeroWidthBox());
               baseList.add(this.makeZeroWidthPenalty(1000));
               baseList.add(new KnuthGlue(this.lineStartBAP, 0, 0, this.auxiliaryPosition, false));
            }
            break;
         default:
            if (this.lineStartBAP == 0 && this.lineEndBAP == 0) {
               baseList.add(new KnuthPenalty(widthIfBreakOccurs, unflagged ? 1 : 50, !unflagged, this.auxiliaryPosition, false));
               if (widthIfNoBreakOccurs.isNonZero()) {
                  baseList.add(new KnuthGlue(widthIfNoBreakOccurs, this.auxiliaryPosition, false));
               }
            } else {
               baseList.add(this.makeZeroWidthPenalty(1000));
               baseList.add(new KnuthGlue(this.lineEndBAP, 0, 0, this.auxiliaryPosition, false));
               baseList.add(new KnuthPenalty(widthIfBreakOccurs, unflagged ? 1 : 50, !unflagged, this.auxiliaryPosition, false));
               if (widthIfNoBreakOccurs.isNonZero()) {
                  baseList.add(new KnuthGlue(widthIfNoBreakOccurs.getOpt() - (this.lineStartBAP + this.lineEndBAP), widthIfNoBreakOccurs.getStretch(), widthIfNoBreakOccurs.getShrink(), this.auxiliaryPosition, false));
               } else {
                  baseList.add(new KnuthGlue(-(this.lineStartBAP + this.lineEndBAP), 0, 0, this.auxiliaryPosition, false));
               }

               baseList.add(this.makeAuxiliaryZeroWidthBox());
               baseList.add(this.makeZeroWidthPenalty(1000));
               baseList.add(new KnuthGlue(this.lineStartBAP, 0, 0, this.auxiliaryPosition, false));
            }
      }

   }

   static {
      $assertionsDisabled = !TextLayoutManager.class.desiredAssertionStatus();
      LOG = LogFactory.getLog(TextLayoutManager.class);
   }

   private final class TextAreaBuilder {
      private final MinOptMax width;
      private final int adjust;
      private final LayoutContext context;
      private final int firstIndex;
      private final int lastIndex;
      private final boolean isLastArea;
      private final Font font;
      private int blockProgressionDimension;
      private AreaInfo areaInfo;
      private StringBuffer wordChars;
      private int[] letterAdjust;
      private int letterAdjustIndex;
      private TextArea textArea;

      private TextAreaBuilder(MinOptMax width, int adjust, LayoutContext context, int firstIndex, int lastIndex, boolean isLastArea, Font font) {
         this.width = width;
         this.adjust = adjust;
         this.context = context;
         this.firstIndex = firstIndex;
         this.lastIndex = lastIndex;
         this.isLastArea = isLastArea;
         this.font = font;
      }

      private TextArea build() {
         this.createTextArea();
         this.setInlineProgressionDimension();
         this.calcBlockProgressionDimension();
         this.setBlockProgressionDimension();
         this.setBaselineOffset();
         this.setOffset();
         this.setText();
         TraitSetter.addFontTraits(this.textArea, this.font);
         this.textArea.addTrait(Trait.COLOR, TextLayoutManager.this.foText.getColor());
         TraitSetter.addPtr(this.textArea, TextLayoutManager.this.getPtr());
         TraitSetter.addTextDecoration(this.textArea, TextLayoutManager.this.foText.getTextDecoration());
         TraitSetter.addFontTraits(this.textArea, this.font);
         return this.textArea;
      }

      private void createTextArea() {
         if (this.context.getIPDAdjust() == 0.0) {
            this.textArea = new TextArea();
         } else {
            this.textArea = new TextArea(this.width.getStretch(), this.width.getShrink(), this.adjust);
         }

      }

      private void setInlineProgressionDimension() {
         this.textArea.setIPD(this.width.getOpt() + this.adjust);
      }

      private void calcBlockProgressionDimension() {
         this.blockProgressionDimension = this.font.getAscender() - this.font.getDescender();
      }

      private void setBlockProgressionDimension() {
         this.textArea.setBPD(this.blockProgressionDimension);
      }

      private void setBaselineOffset() {
         this.textArea.setBaselineOffset(this.font.getAscender());
      }

      private void setOffset() {
         if (this.blockProgressionDimension == TextLayoutManager.this.alignmentContext.getHeight()) {
            this.textArea.setOffset(0);
         } else {
            this.textArea.setOffset(TextLayoutManager.this.alignmentContext.getOffset());
         }

      }

      private void setText() {
         int wordStartIndex = -1;
         int wordCharLength = 0;

         for(int wordIndex = this.firstIndex; wordIndex <= this.lastIndex; ++wordIndex) {
            this.areaInfo = TextLayoutManager.this.getAreaInfo(wordIndex);
            if (this.areaInfo.isSpace) {
               this.addSpaces();
            } else {
               if (wordStartIndex == -1) {
                  wordStartIndex = wordIndex;
                  wordCharLength = 0;
               }

               wordCharLength += this.areaInfo.getCharLength();
               if (this.isWordEnd(wordIndex)) {
                  this.addWord(wordStartIndex, wordIndex, wordCharLength);
                  wordStartIndex = -1;
               }
            }
         }

      }

      private boolean isWordEnd(int areaInfoIndex) {
         return areaInfoIndex == this.lastIndex || TextLayoutManager.this.getAreaInfo(areaInfoIndex + 1).isSpace;
      }

      private void addWord(int startIndex, int endIndex, int charLength) {
         if (this.isHyphenated(endIndex)) {
            ++charLength;
         }

         this.initWord(charLength);

         for(int i = startIndex; i <= endIndex; ++i) {
            AreaInfo wordAreaInfo = TextLayoutManager.this.getAreaInfo(i);
            this.addWordChars(wordAreaInfo);
            this.addLetterAdjust(wordAreaInfo);
         }

         if (this.isHyphenated(endIndex)) {
            this.addHyphenationChar();
         }

         this.textArea.addWord(this.wordChars.toString(), 0, this.letterAdjust);
      }

      private void initWord(int charLength) {
         this.wordChars = new StringBuffer(charLength);
         this.letterAdjust = new int[charLength];
         this.letterAdjustIndex = 0;
      }

      private boolean isHyphenated(int endIndex) {
         return this.isLastArea && endIndex == this.lastIndex && this.areaInfo.isHyphenated;
      }

      private void addHyphenationChar() {
         this.wordChars.append(TextLayoutManager.this.foText.getCommonHyphenation().getHyphChar(this.font));
      }

      private void addWordChars(AreaInfo wordAreaInfo) {
         for(int i = wordAreaInfo.startIndex; i < wordAreaInfo.breakIndex; ++i) {
            this.wordChars.append(TextLayoutManager.this.foText.charAt(i));
         }

      }

      private void addLetterAdjust(AreaInfo wordAreaInfo) {
         int letterSpaceCount = wordAreaInfo.letterSpaceCount;

         for(int i = wordAreaInfo.startIndex; i < wordAreaInfo.breakIndex; ++i) {
            if (this.letterAdjustIndex > 0) {
               MinOptMax adj = TextLayoutManager.this.letterAdjustArray[i];
               this.letterAdjust[this.letterAdjustIndex] = adj == null ? 0 : adj.getOpt();
            }

            if (letterSpaceCount > 0) {
               int[] var10000 = this.letterAdjust;
               int var10001 = this.letterAdjustIndex;
               var10000[var10001] += this.textArea.getTextLetterSpaceAdjust();
               --letterSpaceCount;
            }

            ++this.letterAdjustIndex;
         }

      }

      private void addSpaces() {
         for(int i = this.areaInfo.startIndex; i < this.areaInfo.breakIndex; ++i) {
            char spaceChar = TextLayoutManager.this.foText.charAt(i);
            if (!CharUtilities.isZeroWidthSpace(spaceChar)) {
               this.textArea.addSpace(spaceChar, 0, CharUtilities.isAdjustableSpace(spaceChar));
            }
         }

      }

      // $FF: synthetic method
      TextAreaBuilder(MinOptMax x1, int x2, LayoutContext x3, int x4, int x5, boolean x6, Font x7, Object x8) {
         this(x1, x2, x3, x4, x5, x6, x7);
      }
   }

   private final class PendingChange {
      private final AreaInfo areaInfo;
      private final int index;

      private PendingChange(AreaInfo areaInfo, int index) {
         this.areaInfo = areaInfo;
         this.index = index;
      }

      // $FF: synthetic method
      PendingChange(AreaInfo x1, int x2, Object x3) {
         this(x1, x2);
      }
   }

   private class AreaInfo {
      private final int startIndex;
      private final int breakIndex;
      private final int wordSpaceCount;
      private int letterSpaceCount;
      private MinOptMax areaIPD;
      private final boolean isHyphenated;
      private final boolean isSpace;
      private boolean breakOppAfter;
      private final Font font;

      AreaInfo(int startIndex, int breakIndex, int wordSpaceCount, int letterSpaceCount, MinOptMax areaIPD, boolean isHyphenated, boolean isSpace, boolean breakOppAfter, Font font) {
         assert startIndex <= breakIndex;

         this.startIndex = startIndex;
         this.breakIndex = breakIndex;
         this.wordSpaceCount = wordSpaceCount;
         this.letterSpaceCount = letterSpaceCount;
         this.areaIPD = areaIPD;
         this.isHyphenated = isHyphenated;
         this.isSpace = isSpace;
         this.breakOppAfter = breakOppAfter;
         this.font = font;
      }

      private int getCharLength() {
         return this.breakIndex - this.startIndex;
      }

      private void addToAreaIPD(MinOptMax idp) {
         this.areaIPD = this.areaIPD.plus(idp);
      }

      public String toString() {
         return "AreaInfo[letterSpaceCount = " + this.letterSpaceCount + ", wordSpaceCount = " + this.wordSpaceCount + ", areaIPD = " + this.areaIPD + ", startIndex = " + this.startIndex + ", breakIndex = " + this.breakIndex + ", isHyphenated = " + this.isHyphenated + ", isSpace = " + this.isSpace + ", font = " + this.font + "]";
      }
   }
}
