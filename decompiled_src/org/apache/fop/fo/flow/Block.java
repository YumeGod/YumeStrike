package org.apache.fop.fo.flow;

import java.awt.Color;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.CharIterator;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.NullCharIterator;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonHyphenation;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.CommonRelativePosition;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.fo.properties.StructurePointerPropertySet;
import org.xml.sax.Locator;

public class Block extends FObjMixed implements BreakPropertySet, StructurePointerPropertySet {
   private boolean blockOrInlineItemFound = false;
   private boolean initialPropertySetFound = false;
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonFont commonFont;
   private CommonHyphenation commonHyphenation;
   private CommonMarginBlock commonMarginBlock;
   private CommonRelativePosition commonRelativePosition;
   private int breakAfter;
   private int breakBefore;
   private Color color;
   private int hyphenationKeep;
   private Numeric hyphenationLadderCount;
   private int intrusionDisplace;
   private KeepProperty keepTogether;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private Length lastLineEndIndent;
   private int linefeedTreatment;
   private SpaceProperty lineHeight;
   private int lineHeightShiftAdjustment;
   private int lineStackingStrategy;
   private Numeric orphans;
   private String ptr;
   private int whiteSpaceTreatment;
   private int span;
   private int textAlign;
   private int textAlignLast;
   private Length textIndent;
   private int whiteSpaceCollapse;
   private Numeric widows;
   private int wrapOption;
   private int disableColumnBalancing;

   public Block(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonFont = pList.getFontProps();
      this.commonHyphenation = pList.getHyphenationProps();
      this.commonMarginBlock = pList.getMarginBlockProps();
      this.commonRelativePosition = pList.getRelativePositionProps();
      this.breakAfter = pList.get(58).getEnum();
      this.breakBefore = pList.get(59).getEnum();
      this.color = pList.get(72).getColor(this.getUserAgent());
      this.hyphenationKeep = pList.get(118).getEnum();
      this.hyphenationLadderCount = pList.get(119).getNumeric();
      this.intrusionDisplace = pList.get(130).getEnum();
      this.keepTogether = pList.get(131).getKeep();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      this.lastLineEndIndent = pList.get(135).getLength();
      this.linefeedTreatment = pList.get(143).getEnum();
      this.lineHeight = pList.get(144).getSpace();
      this.lineHeightShiftAdjustment = pList.get(145).getEnum();
      this.lineStackingStrategy = pList.get(146).getEnum();
      this.orphans = pList.get(168).getNumeric();
      this.ptr = pList.get(274).getString();
      this.whiteSpaceTreatment = pList.get(262).getEnum();
      this.span = pList.get(226).getEnum();
      this.textAlign = pList.get(245).getEnum();
      this.textAlignLast = pList.get(246).getEnum();
      this.textIndent = pList.get(250).getLength();
      this.whiteSpaceCollapse = pList.get(261).getEnum();
      this.widows = pList.get(263).getNumeric();
      this.wrapOption = pList.get(266).getEnum();
      this.disableColumnBalancing = pList.get(273).getEnum();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startBlock(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endBlock(this);
   }

   public CommonMarginBlock getCommonMarginBlock() {
      return this.commonMarginBlock;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public CommonFont getCommonFont() {
      return this.commonFont;
   }

   public CommonHyphenation getCommonHyphenation() {
      return this.commonHyphenation;
   }

   public int getBreakAfter() {
      return this.breakAfter;
   }

   public String getPtr() {
      return this.ptr;
   }

   public int getBreakBefore() {
      return this.breakBefore;
   }

   public Numeric getHyphenationLadderCount() {
      return this.hyphenationLadderCount;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public int getOrphans() {
      return this.orphans.getValue();
   }

   public int getWidows() {
      return this.widows.getValue();
   }

   public int getLineStackingStrategy() {
      return this.lineStackingStrategy;
   }

   public Color getColor() {
      return this.color;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public int getSpan() {
      return this.span;
   }

   public int getTextAlign() {
      return this.textAlign;
   }

   public int getTextAlignLast() {
      return this.textAlignLast;
   }

   public Length getTextIndent() {
      return this.textIndent;
   }

   public Length getLastLineEndIndent() {
      return this.lastLineEndIndent;
   }

   public int getWrapOption() {
      return this.wrapOption;
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if ("marker".equals(localName)) {
            if (this.blockOrInlineItemFound || this.initialPropertySetFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "initial-property-set? (#PCDATA|%inline;|%block;)");
            }
         } else if ("initial-property-set".equals(localName)) {
            if (this.initialPropertySetFound) {
               this.tooManyNodesError(loc, "fo:initial-property-set");
            } else if (this.blockOrInlineItemFound) {
               this.nodesOutOfOrderError(loc, "fo:initial-property-set", "(#PCDATA|%inline;|%block;)");
            } else {
               this.initialPropertySetFound = true;
            }
         } else if (this.isBlockOrInlineItem(nsURI, localName)) {
            this.blockOrInlineItemFound = true;
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public int getLinefeedTreatment() {
      return this.linefeedTreatment;
   }

   public int getWhitespaceTreatment() {
      return this.whiteSpaceTreatment;
   }

   public int getWhitespaceCollapse() {
      return this.whiteSpaceCollapse;
   }

   public CommonRelativePosition getCommonRelativePosition() {
      return this.commonRelativePosition;
   }

   public int getHyphenationKeep() {
      return this.hyphenationKeep;
   }

   public int getIntrusionDisplace() {
      return this.intrusionDisplace;
   }

   public int getLineHeightShiftAdjustment() {
      return this.lineHeightShiftAdjustment;
   }

   public int getDisableColumnBalancing() {
      return this.disableColumnBalancing;
   }

   public CharIterator charIterator() {
      return NullCharIterator.getInstance();
   }

   public String getLocalName() {
      return "block";
   }

   public int getNameId() {
      return 3;
   }
}
