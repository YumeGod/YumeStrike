package org.apache.fop.fo;

import java.awt.Color;
import java.nio.CharBuffer;
import java.util.NoSuchElementException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonHyphenation;
import org.apache.fop.fo.properties.CommonTextDecoration;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.SpaceProperty;
import org.xml.sax.Locator;

public class FOText extends FONode implements CharSequence {
   private CharBuffer charBuffer;
   private CommonFont commonFont;
   private CommonHyphenation commonHyphenation;
   private Color color;
   private KeepProperty keepTogether;
   private Property letterSpacing;
   private SpaceProperty lineHeight;
   private int whiteSpaceTreatment;
   private int whiteSpaceCollapse;
   private int textTransform;
   private Property wordSpacing;
   private int wrapOption;
   private Length baselineShift;
   private FOText prevFOTextThisBlock = null;
   private FOText nextFOTextThisBlock = null;
   private Block ancestorBlock = null;
   private CommonTextDecoration textDecoration;
   private static final int IS_WORD_CHAR_FALSE = 0;
   private static final int IS_WORD_CHAR_TRUE = 1;
   private static final int IS_WORD_CHAR_MAYBE = 2;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public FOText(FONode parent) {
      super(parent);
   }

   protected void characters(char[] data, int start, int length, PropertyList list, Locator locator) throws FOPException {
      if (this.charBuffer == null) {
         this.charBuffer = CharBuffer.allocate(length);
      } else {
         int newLength = this.charBuffer.limit() + length;
         CharBuffer newBuffer = CharBuffer.allocate(newLength);
         this.charBuffer.rewind();
         newBuffer.put(this.charBuffer);
         this.charBuffer = newBuffer;
      }

      this.charBuffer.put(data, start, length);
   }

   public char[] getCharArray() {
      if (this.charBuffer == null) {
         return null;
      } else if (this.charBuffer.hasArray()) {
         return this.charBuffer.array();
      } else {
         char[] ca = new char[this.charBuffer.limit()];
         this.charBuffer.rewind();
         this.charBuffer.get(ca);
         return ca;
      }
   }

   public FONode clone(FONode parent, boolean removeChildren) throws FOPException {
      FOText ft = (FOText)super.clone(parent, removeChildren);
      if (removeChildren && this.charBuffer != null) {
         ft.charBuffer = CharBuffer.allocate(this.charBuffer.limit());
         this.charBuffer.rewind();
         ft.charBuffer.put(this.charBuffer);
         ft.charBuffer.rewind();
      }

      ft.prevFOTextThisBlock = null;
      ft.nextFOTextThisBlock = null;
      ft.ancestorBlock = null;
      return ft;
   }

   public void bind(PropertyList pList) throws FOPException {
      this.commonFont = pList.getFontProps();
      this.commonHyphenation = pList.getHyphenationProps();
      this.color = pList.get(72).getColor(this.getUserAgent());
      this.keepTogether = pList.get(131).getKeep();
      this.lineHeight = pList.get(144).getSpace();
      this.letterSpacing = pList.get(141);
      this.whiteSpaceCollapse = pList.get(261).getEnum();
      this.whiteSpaceTreatment = pList.get(262).getEnum();
      this.textTransform = pList.get(252).getEnum();
      this.wordSpacing = pList.get(265);
      this.wrapOption = pList.get(266).getEnum();
      this.textDecoration = pList.getTextDecorationProps();
      this.baselineShift = pList.get(15).getLength();
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().characters(this.getCharArray(), 0, this.charBuffer.limit());
   }

   public void finalizeNode() {
      this.textTransform();
   }

   public boolean willCreateArea() {
      if (this.whiteSpaceCollapse == 48 && this.charBuffer.limit() > 0) {
         return true;
      } else {
         this.charBuffer.rewind();

         char ch;
         do {
            if (!this.charBuffer.hasRemaining()) {
               return false;
            }

            ch = this.charBuffer.get();
         } while(ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t');

         this.charBuffer.rewind();
         return true;
      }
   }

   public CharIterator charIterator() {
      return new TextCharIterator();
   }

   protected void createBlockPointers(Block ancestorBlock) {
      this.ancestorBlock = ancestorBlock;
      if (ancestorBlock.lastFOTextProcessed != null) {
         if (ancestorBlock.lastFOTextProcessed.ancestorBlock == this.ancestorBlock) {
            this.prevFOTextThisBlock = ancestorBlock.lastFOTextProcessed;
            this.prevFOTextThisBlock.nextFOTextThisBlock = this;
         } else {
            this.prevFOTextThisBlock = null;
         }
      }

   }

   private void textTransform() {
      if (!this.getBuilderContext().inMarker() && this.textTransform != 95) {
         this.charBuffer.rewind();
         CharBuffer tmp = this.charBuffer.slice();
         int lim = this.charBuffer.limit();
         int pos = -1;

         while(true) {
            ++pos;
            if (pos >= lim) {
               return;
            }

            char c = this.charBuffer.get();
            switch (this.textTransform) {
               case 22:
                  if (this.isStartOfWord(pos)) {
                     tmp.put(Character.toTitleCase(c));
                  } else {
                     tmp.put(c);
                  }
                  break;
               case 78:
                  tmp.put(Character.toLowerCase(c));
                  break;
               case 155:
                  tmp.put(Character.toUpperCase(c));
                  break;
               default:
                  if (!$assertionsDisabled) {
                     throw new AssertionError();
                  }
            }
         }
      }
   }

   private boolean isStartOfWord(int var1) {
      // $FF: Couldn't be decompiled
   }

   private char getRelativeCharInBlock(int i, int offset) {
      int charIndex = i + offset;
      if (charIndex >= 0 && charIndex < this.length()) {
         return this.charAt(i + offset);
      } else if (offset > 0) {
         return '\u0000';
      } else {
         boolean foundChar = false;
         char charToReturn = 0;
         FOText nodeToTest = this;
         int remainingOffset = offset + i;

         while(!foundChar && nodeToTest.prevFOTextThisBlock != null) {
            nodeToTest = nodeToTest.prevFOTextThisBlock;
            int diff = nodeToTest.length() + remainingOffset - 1;
            if (diff >= 0) {
               charToReturn = nodeToTest.charAt(diff);
               foundChar = true;
            } else {
               remainingOffset += diff;
            }
         }

         return charToReturn;
      }
   }

   public FOText getPrevFOTextThisBlock() {
      return this.prevFOTextThisBlock;
   }

   public FOText getNextFOTextThisBlock() {
      return this.nextFOTextThisBlock;
   }

   public Block getAncestorBlock() {
      return this.ancestorBlock;
   }

   private static int isWordChar(char var0) {
      // $FF: Couldn't be decompiled
   }

   public CommonFont getCommonFont() {
      return this.commonFont;
   }

   public CommonHyphenation getCommonHyphenation() {
      return this.commonHyphenation;
   }

   public Color getColor() {
      return this.color;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public Property getLetterSpacing() {
      return this.letterSpacing;
   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public int getWhitespaceTreatment() {
      return this.whiteSpaceTreatment;
   }

   public Property getWordSpacing() {
      return this.wordSpacing;
   }

   public int getWrapOption() {
      return this.wrapOption;
   }

   public CommonTextDecoration getTextDecoration() {
      return this.textDecoration;
   }

   public Length getBaseLineShift() {
      return this.baselineShift;
   }

   public String toString() {
      return this.charBuffer == null ? "" : this.charBuffer.toString();
   }

   public String getLocalName() {
      return "#PCDATA";
   }

   public String getNormalNamespacePrefix() {
      return null;
   }

   protected String gatherContextInfo() {
      return this.locator != null ? super.gatherContextInfo() : this.toString();
   }

   public char charAt(int position) {
      return this.charBuffer.get(position);
   }

   public CharSequence subSequence(int start, int end) {
      return this.charBuffer.subSequence(start, end);
   }

   public int length() {
      return this.charBuffer.limit();
   }

   public void resetBuffer() {
      if (this.charBuffer != null) {
         this.charBuffer.rewind();
      }

   }

   static {
      $assertionsDisabled = !FOText.class.desiredAssertionStatus();
   }

   private class TextCharIterator extends CharIterator {
      int currentPosition;
      boolean canRemove;
      boolean canReplace;

      private TextCharIterator() {
         this.currentPosition = 0;
         this.canRemove = false;
         this.canReplace = false;
      }

      public boolean hasNext() {
         return this.currentPosition < FOText.this.charBuffer.limit();
      }

      public char nextChar() {
         if (this.currentPosition < FOText.this.charBuffer.limit()) {
            this.canRemove = true;
            this.canReplace = true;
            return FOText.this.charBuffer.get(this.currentPosition++);
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.canRemove) {
            FOText.this.charBuffer.position(this.currentPosition);
            CharBuffer tmp = FOText.this.charBuffer.slice();
            FOText.this.charBuffer.position(--this.currentPosition);
            if (tmp.hasRemaining()) {
               FOText.this.charBuffer.mark();
               FOText.this.charBuffer.put(tmp);
               FOText.this.charBuffer.reset();
            }

            FOText.this.charBuffer.limit(FOText.this.charBuffer.limit() - 1);
            this.canRemove = false;
         } else {
            throw new IllegalStateException();
         }
      }

      public void replaceChar(char c) {
         if (this.canReplace) {
            FOText.this.charBuffer.put(this.currentPosition - 1, c);
         } else {
            throw new IllegalStateException();
         }
      }

      // $FF: synthetic method
      TextCharIterator(Object x1) {
         this();
      }
   }
}
