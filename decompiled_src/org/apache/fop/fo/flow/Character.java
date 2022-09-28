package org.apache.fop.fo.flow;

import java.awt.Color;
import java.util.NoSuchElementException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.CharIterator;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonHyphenation;
import org.apache.fop.fo.properties.CommonTextDecoration;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.fo.properties.StructurePointerPropertySet;
import org.xml.sax.Locator;

public class Character extends FObj implements StructurePointerPropertySet {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonFont commonFont;
   private CommonHyphenation commonHyphenation;
   private Length alignmentAdjust;
   private int alignmentBaseline;
   private Length baselineShift;
   private char character;
   private Color color;
   private int dominantBaseline;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private Property letterSpacing;
   private SpaceProperty lineHeight;
   private CommonTextDecoration textDecoration;
   private Property wordSpacing;
   private String ptr;
   public static final int OK = 0;
   public static final int DOESNOT_FIT = 1;

   public Character(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonFont = pList.getFontProps();
      this.commonHyphenation = pList.getHyphenationProps();
      this.alignmentAdjust = pList.get(3).getLength();
      this.alignmentBaseline = pList.get(4).getEnum();
      this.baselineShift = pList.get(15).getLength();
      this.character = pList.get(69).getCharacter();
      this.color = pList.get(72).getColor(this.getUserAgent());
      this.dominantBaseline = pList.get(88).getEnum();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      this.letterSpacing = pList.get(141);
      this.lineHeight = pList.get(144).getSpace();
      this.textDecoration = pList.getTextDecorationProps();
      this.wordSpacing = pList.get(265);
      this.ptr = pList.get(274).getString();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().character(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public CharIterator charIterator() {
      return new FOCharIterator(this);
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

   public char getCharacter() {
      return this.character;
   }

   public Color getColor() {
      return this.color;
   }

   public Length getAlignmentAdjust() {
      return this.alignmentAdjust;
   }

   public int getAlignmentBaseline() {
      return this.alignmentBaseline;
   }

   public Length getBaselineShift() {
      return this.baselineShift;
   }

   public int getDominantBaseline() {
      return this.dominantBaseline;
   }

   public Property getLetterSpacing() {
      return this.letterSpacing;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public CommonTextDecoration getTextDecoration() {
      return this.textDecoration;
   }

   public Property getWordSpacing() {
      return this.wordSpacing;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public String getPtr() {
      return this.ptr;
   }

   public String getLocalName() {
      return "character";
   }

   public int getNameId() {
      return 10;
   }

   private class FOCharIterator extends CharIterator {
      private boolean bFirst = true;
      private Character foChar;

      FOCharIterator(Character foChar) {
         this.foChar = foChar;
      }

      public boolean hasNext() {
         return this.bFirst;
      }

      public char nextChar() {
         if (this.bFirst) {
            this.bFirst = false;
            return this.foChar.character;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         this.foChar.parent.removeChild(this.foChar);
      }

      public void replaceChar(char c) {
         this.foChar.character = c;
      }
   }
}
