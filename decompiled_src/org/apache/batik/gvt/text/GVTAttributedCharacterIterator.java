package org.apache.batik.gvt.text;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import java.util.Set;

public interface GVTAttributedCharacterIterator extends AttributedCharacterIterator {
   void setString(String var1);

   void setString(AttributedString var1);

   void setAttributeArray(TextAttribute var1, Object[] var2, int var3, int var4);

   Set getAllAttributeKeys();

   Object getAttribute(AttributedCharacterIterator.Attribute var1);

   Map getAttributes();

   int getRunLimit();

   int getRunLimit(AttributedCharacterIterator.Attribute var1);

   int getRunLimit(Set var1);

   int getRunStart();

   int getRunStart(AttributedCharacterIterator.Attribute var1);

   int getRunStart(Set var1);

   Object clone();

   char current();

   char first();

   int getBeginIndex();

   int getEndIndex();

   int getIndex();

   char last();

   char next();

   char previous();

   char setIndex(int var1);

   public interface AttributeFilter {
      AttributedCharacterIterator mutateAttributes(AttributedCharacterIterator var1);
   }

   public static class TextAttribute extends AttributedCharacterIterator.Attribute {
      public static final TextAttribute FLOW_PARAGRAPH = new TextAttribute("FLOW_PARAGRAPH");
      public static final TextAttribute FLOW_EMPTY_PARAGRAPH = new TextAttribute("FLOW_EMPTY_PARAGRAPH");
      public static final TextAttribute FLOW_LINE_BREAK = new TextAttribute("FLOW_LINE_BREAK");
      public static final TextAttribute FLOW_REGIONS = new TextAttribute("FLOW_REGIONS");
      public static final TextAttribute LINE_HEIGHT = new TextAttribute("LINE_HEIGHT");
      public static final TextAttribute PREFORMATTED = new TextAttribute("PREFORMATTED");
      public static final TextAttribute TEXT_COMPOUND_DELIMITER = new TextAttribute("TEXT_COMPOUND_DELIMITER");
      public static final TextAttribute TEXT_COMPOUND_ID = new TextAttribute("TEXT_COMPOUND_ID");
      public static final TextAttribute ANCHOR_TYPE = new TextAttribute("ANCHOR_TYPE");
      public static final TextAttribute EXPLICIT_LAYOUT = new TextAttribute("EXPLICIT_LAYOUT");
      public static final TextAttribute X = new TextAttribute("X");
      public static final TextAttribute Y = new TextAttribute("Y");
      public static final TextAttribute DX = new TextAttribute("DX");
      public static final TextAttribute DY = new TextAttribute("DY");
      public static final TextAttribute ROTATION = new TextAttribute("ROTATION");
      public static final TextAttribute PAINT_INFO = new TextAttribute("PAINT_INFO");
      public static final TextAttribute BBOX_WIDTH = new TextAttribute("BBOX_WIDTH");
      public static final TextAttribute LENGTH_ADJUST = new TextAttribute("LENGTH_ADJUST");
      public static final TextAttribute CUSTOM_SPACING = new TextAttribute("CUSTOM_SPACING");
      public static final TextAttribute KERNING = new TextAttribute("KERNING");
      public static final TextAttribute LETTER_SPACING = new TextAttribute("LETTER_SPACING");
      public static final TextAttribute WORD_SPACING = new TextAttribute("WORD_SPACING");
      public static final TextAttribute TEXTPATH = new TextAttribute("TEXTPATH");
      public static final TextAttribute FONT_VARIANT = new TextAttribute("FONT_VARIANT");
      public static final TextAttribute BASELINE_SHIFT = new TextAttribute("BASELINE_SHIFT");
      public static final TextAttribute WRITING_MODE = new TextAttribute("WRITING_MODE");
      public static final TextAttribute VERTICAL_ORIENTATION = new TextAttribute("VERTICAL_ORIENTATION");
      public static final TextAttribute VERTICAL_ORIENTATION_ANGLE = new TextAttribute("VERTICAL_ORIENTATION_ANGLE");
      public static final TextAttribute HORIZONTAL_ORIENTATION_ANGLE = new TextAttribute("HORIZONTAL_ORIENTATION_ANGLE");
      public static final TextAttribute GVT_FONT_FAMILIES = new TextAttribute("GVT_FONT_FAMILIES");
      public static final TextAttribute GVT_FONTS = new TextAttribute("GVT_FONTS");
      public static final TextAttribute GVT_FONT = new TextAttribute("GVT_FONT");
      public static final TextAttribute ALT_GLYPH_HANDLER = new TextAttribute("ALT_GLYPH_HANDLER");
      public static final TextAttribute BIDI_LEVEL = new TextAttribute("BIDI_LEVEL");
      public static final TextAttribute CHAR_INDEX = new TextAttribute("CHAR_INDEX");
      public static final TextAttribute ARABIC_FORM = new TextAttribute("ARABIC_FORM");
      public static final Integer WRITING_MODE_LTR = new Integer(1);
      public static final Integer WRITING_MODE_RTL = new Integer(2);
      public static final Integer WRITING_MODE_TTB = new Integer(3);
      public static final Integer ORIENTATION_ANGLE = new Integer(1);
      public static final Integer ORIENTATION_AUTO = new Integer(2);
      public static final Integer SMALL_CAPS = new Integer(16);
      public static final Integer UNDERLINE_ON;
      public static final Boolean OVERLINE_ON;
      public static final Boolean STRIKETHROUGH_ON;
      public static final Integer ADJUST_SPACING;
      public static final Integer ADJUST_ALL;
      public static final Integer ARABIC_NONE;
      public static final Integer ARABIC_ISOLATED;
      public static final Integer ARABIC_TERMINAL;
      public static final Integer ARABIC_INITIAL;
      public static final Integer ARABIC_MEDIAL;

      public TextAttribute(String var1) {
         super(var1);
      }

      static {
         UNDERLINE_ON = java.awt.font.TextAttribute.UNDERLINE_ON;
         OVERLINE_ON = Boolean.TRUE;
         STRIKETHROUGH_ON = java.awt.font.TextAttribute.STRIKETHROUGH_ON;
         ADJUST_SPACING = new Integer(0);
         ADJUST_ALL = new Integer(1);
         ARABIC_NONE = new Integer(0);
         ARABIC_ISOLATED = new Integer(1);
         ARABIC_TERMINAL = new Integer(2);
         ARABIC_INITIAL = new Integer(3);
         ARABIC_MEDIAL = new Integer(4);
      }
   }
}
