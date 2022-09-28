package com.xmlmind.fo.properties;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.objects.TableCell;
import com.xmlmind.fo.objects.TableColumn;
import com.xmlmind.fo.properties.compound.BorderWidthConditional;
import com.xmlmind.fo.properties.compound.Keep;
import com.xmlmind.fo.properties.compound.LengthBpIpDirection;
import com.xmlmind.fo.properties.compound.LengthRange;
import com.xmlmind.fo.properties.compound.LineHeight;
import com.xmlmind.fo.properties.compound.PaddingConditional;
import com.xmlmind.fo.properties.compound.Space;
import com.xmlmind.fo.properties.expression.Expression;
import com.xmlmind.fo.properties.shorthand.Background;
import com.xmlmind.fo.properties.shorthand.BackgroundPosition;
import com.xmlmind.fo.properties.shorthand.Border;
import com.xmlmind.fo.properties.shorthand.BorderBottom;
import com.xmlmind.fo.properties.shorthand.BorderColor;
import com.xmlmind.fo.properties.shorthand.BorderLeft;
import com.xmlmind.fo.properties.shorthand.BorderRight;
import com.xmlmind.fo.properties.shorthand.BorderSpacing;
import com.xmlmind.fo.properties.shorthand.BorderStyle;
import com.xmlmind.fo.properties.shorthand.BorderTop;
import com.xmlmind.fo.properties.shorthand.BorderWidth;
import com.xmlmind.fo.properties.shorthand.Cue;
import com.xmlmind.fo.properties.shorthand.Font;
import com.xmlmind.fo.properties.shorthand.Margin;
import com.xmlmind.fo.properties.shorthand.Padding;
import com.xmlmind.fo.properties.shorthand.PageBreakAfter;
import com.xmlmind.fo.properties.shorthand.PageBreakBefore;
import com.xmlmind.fo.properties.shorthand.PageBreakInside;
import com.xmlmind.fo.properties.shorthand.Pause;
import com.xmlmind.fo.properties.shorthand.Position;
import com.xmlmind.fo.properties.shorthand.Size;
import com.xmlmind.fo.properties.shorthand.VerticalAlign;
import com.xmlmind.fo.properties.shorthand.WhiteSpace;
import com.xmlmind.fo.properties.shorthand.XmlLang;
import com.xmlmind.fo.util.StringUtil;
import com.xmlmind.fo.util.SystemUtil;
import com.xmlmind.fo.util.URLUtil;
import java.io.File;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Property {
   public static final int ABSOLUTE_POSITION = 0;
   public static final int ACTIVE_STATE = 1;
   public static final int ALIGNMENT_ADJUST = 2;
   public static final int ALIGNMENT_BASELINE = 3;
   public static final int AUTO_RESTORE = 4;
   public static final int AZIMUTH = 5;
   public static final int BACKGROUND = 6;
   public static final int BACKGROUND_ATTACHMENT = 7;
   public static final int BACKGROUND_COLOR = 8;
   public static final int BACKGROUND_IMAGE = 9;
   public static final int BACKGROUND_POSITION = 10;
   public static final int BACKGROUND_POSITION_HORIZONTAL = 11;
   public static final int BACKGROUND_POSITION_VERTICAL = 12;
   public static final int BACKGROUND_REPEAT = 13;
   public static final int BASELINE_SHIFT = 14;
   public static final int BLANK_OR_NOT_BLANK = 15;
   public static final int BLOCK_PROGRESSION_DIMENSION = 16;
   public static final int BLOCK_PROGRESSION_DIMENSION_MAXIMUM = 17;
   public static final int BLOCK_PROGRESSION_DIMENSION_MINIMUM = 18;
   public static final int BLOCK_PROGRESSION_DIMENSION_OPTIMUM = 19;
   public static final int BORDER = 20;
   public static final int BORDER_AFTER_COLOR = 21;
   public static final int BORDER_AFTER_PRECEDENCE = 22;
   public static final int BORDER_AFTER_STYLE = 23;
   public static final int BORDER_AFTER_WIDTH = 24;
   public static final int BORDER_AFTER_WIDTH_CONDITIONALITY = 25;
   public static final int BORDER_AFTER_WIDTH_LENGTH = 26;
   public static final int BORDER_BEFORE_COLOR = 27;
   public static final int BORDER_BEFORE_PRECEDENCE = 28;
   public static final int BORDER_BEFORE_STYLE = 29;
   public static final int BORDER_BEFORE_WIDTH = 30;
   public static final int BORDER_BEFORE_WIDTH_CONDITIONALITY = 31;
   public static final int BORDER_BEFORE_WIDTH_LENGTH = 32;
   public static final int BORDER_BOTTOM = 33;
   public static final int BORDER_BOTTOM_COLOR = 34;
   public static final int BORDER_BOTTOM_STYLE = 35;
   public static final int BORDER_BOTTOM_WIDTH = 36;
   public static final int BORDER_COLLAPSE = 37;
   public static final int BORDER_COLOR = 38;
   public static final int BORDER_END_COLOR = 39;
   public static final int BORDER_END_PRECEDENCE = 40;
   public static final int BORDER_END_STYLE = 41;
   public static final int BORDER_END_WIDTH = 42;
   public static final int BORDER_END_WIDTH_CONDITIONALITY = 43;
   public static final int BORDER_END_WIDTH_LENGTH = 44;
   public static final int BORDER_LEFT = 45;
   public static final int BORDER_LEFT_COLOR = 46;
   public static final int BORDER_LEFT_STYLE = 47;
   public static final int BORDER_LEFT_WIDTH = 48;
   public static final int BORDER_RIGHT = 49;
   public static final int BORDER_RIGHT_COLOR = 50;
   public static final int BORDER_RIGHT_STYLE = 51;
   public static final int BORDER_RIGHT_WIDTH = 52;
   public static final int BORDER_SEPARATION = 53;
   public static final int BORDER_SEPARATION_BLOCK_DIRECTION = 54;
   public static final int BORDER_SEPARATION_INLINE_DIRECTION = 55;
   public static final int BORDER_SPACING = 56;
   public static final int BORDER_START_COLOR = 57;
   public static final int BORDER_START_PRECEDENCE = 58;
   public static final int BORDER_START_STYLE = 59;
   public static final int BORDER_START_WIDTH = 60;
   public static final int BORDER_START_WIDTH_CONDITIONALITY = 61;
   public static final int BORDER_START_WIDTH_LENGTH = 62;
   public static final int BORDER_STYLE = 63;
   public static final int BORDER_TOP = 64;
   public static final int BORDER_TOP_COLOR = 65;
   public static final int BORDER_TOP_STYLE = 66;
   public static final int BORDER_TOP_WIDTH = 67;
   public static final int BORDER_WIDTH = 68;
   public static final int BOTTOM = 69;
   public static final int BREAK_AFTER = 70;
   public static final int BREAK_BEFORE = 71;
   public static final int CAPTION_SIDE = 72;
   public static final int CASE_NAME = 73;
   public static final int CASE_TITLE = 74;
   public static final int CHARACTER = 75;
   public static final int CLEAR = 76;
   public static final int CLIP = 77;
   public static final int COLOR = 78;
   public static final int COLOR_PROFILE_NAME = 79;
   public static final int COLUMN_COUNT = 80;
   public static final int COLUMN_GAP = 81;
   public static final int COLUMN_NUMBER = 82;
   public static final int COLUMN_WIDTH = 83;
   public static final int CONTENT_HEIGHT = 84;
   public static final int CONTENT_TYPE = 85;
   public static final int CONTENT_WIDTH = 86;
   public static final int COUNTRY = 87;
   public static final int CUE = 88;
   public static final int CUE_AFTER = 89;
   public static final int CUE_BEFORE = 90;
   public static final int DESTINATION_PLACEMENT_OFFSET = 91;
   public static final int DIRECTION = 92;
   public static final int DISPLAY_ALIGN = 93;
   public static final int DOMINANT_BASELINE = 94;
   public static final int ELEVATION = 95;
   public static final int EMPTY_CELLS = 96;
   public static final int END_INDENT = 97;
   public static final int ENDS_ROW = 98;
   public static final int EXTENT = 99;
   public static final int EXTERNAL_DESTINATION = 100;
   public static final int FLOAT = 101;
   public static final int FLOW_NAME = 102;
   public static final int FONT = 103;
   public static final int FONT_FAMILY = 104;
   public static final int FONT_SELECTION_STRATEGY = 105;
   public static final int FONT_SIZE = 106;
   public static final int FONT_SIZE_ADJUST = 107;
   public static final int FONT_STRETCH = 108;
   public static final int FONT_STYLE = 109;
   public static final int FONT_VARIANT = 110;
   public static final int FONT_WEIGHT = 111;
   public static final int FORCE_PAGE_COUNT = 112;
   public static final int FORMAT = 113;
   public static final int GLYPH_ORIENTATION_HORIZONTAL = 114;
   public static final int GLYPH_ORIENTATION_VERTICAL = 115;
   public static final int GROUPING_SEPARATOR = 116;
   public static final int GROUPING_SIZE = 117;
   public static final int HEIGHT = 118;
   public static final int HYPHENATE = 119;
   public static final int HYPHENATION_CHARACTER = 120;
   public static final int HYPHENATION_KEEP = 121;
   public static final int HYPHENATION_LADDER_COUNT = 122;
   public static final int HYPHENATION_PUSH_CHARACTER_COUNT = 123;
   public static final int HYPHENATION_REMAIN_CHARACTER_COUNT = 124;
   public static final int ID = 125;
   public static final int INDICATE_DESTINATION = 126;
   public static final int INITIAL_PAGE_NUMBER = 127;
   public static final int INLINE_PROGRESSION_DIMENSION = 128;
   public static final int INLINE_PROGRESSION_DIMENSION_MAXIMUM = 129;
   public static final int INLINE_PROGRESSION_DIMENSION_MINIMUM = 130;
   public static final int INLINE_PROGRESSION_DIMENSION_OPTIMUM = 131;
   public static final int INTERNAL_DESTINATION = 132;
   public static final int INTRUSION_DISPLACE = 133;
   public static final int KEEP_TOGETHER = 134;
   public static final int KEEP_TOGETHER_WITHIN_COLUMN = 135;
   public static final int KEEP_TOGETHER_WITHIN_LINE = 136;
   public static final int KEEP_TOGETHER_WITHIN_PAGE = 137;
   public static final int KEEP_WITH_NEXT = 138;
   public static final int KEEP_WITH_NEXT_WITHIN_COLUMN = 139;
   public static final int KEEP_WITH_NEXT_WITHIN_LINE = 140;
   public static final int KEEP_WITH_NEXT_WITHIN_PAGE = 141;
   public static final int KEEP_WITH_PREVIOUS = 142;
   public static final int KEEP_WITH_PREVIOUS_WITHIN_COLUMN = 143;
   public static final int KEEP_WITH_PREVIOUS_WITHIN_LINE = 144;
   public static final int KEEP_WITH_PREVIOUS_WITHIN_PAGE = 145;
   public static final int LANGUAGE = 146;
   public static final int LAST_LINE_END_INDENT = 147;
   public static final int LEADER_ALIGNMENT = 148;
   public static final int LEADER_LENGTH = 149;
   public static final int LEADER_LENGTH_MAXIMUM = 150;
   public static final int LEADER_LENGTH_MINIMUM = 151;
   public static final int LEADER_LENGTH_OPTIMUM = 152;
   public static final int LEADER_PATTERN = 153;
   public static final int LEADER_PATTERN_WIDTH = 154;
   public static final int LEFT = 155;
   public static final int LETTER_SPACING = 156;
   public static final int LETTER_SPACING_CONDITIONALITY = 157;
   public static final int LETTER_SPACING_MAXIMUM = 158;
   public static final int LETTER_SPACING_MINIMUM = 159;
   public static final int LETTER_SPACING_OPTIMUM = 160;
   public static final int LETTER_SPACING_PRECEDENCE = 161;
   public static final int LETTER_VALUE = 162;
   public static final int LINEFEED_TREATMENT = 163;
   public static final int LINE_HEIGHT = 164;
   public static final int LINE_HEIGHT_CONDITIONALITY = 165;
   public static final int LINE_HEIGHT_MAXIMUM = 166;
   public static final int LINE_HEIGHT_MINIMUM = 167;
   public static final int LINE_HEIGHT_OPTIMUM = 168;
   public static final int LINE_HEIGHT_PRECEDENCE = 169;
   public static final int LINE_HEIGHT_SHIFT_ADJUSTMENT = 170;
   public static final int LINE_STACKING_STRATEGY = 171;
   public static final int MARGIN = 172;
   public static final int MARGIN_BOTTOM = 173;
   public static final int MARGIN_LEFT = 174;
   public static final int MARGIN_RIGHT = 175;
   public static final int MARGIN_TOP = 176;
   public static final int MARKER_CLASS_NAME = 177;
   public static final int MASTER_NAME = 178;
   public static final int MASTER_REFERENCE = 179;
   public static final int MAX_HEIGHT = 180;
   public static final int MAXIMUM_REPEATS = 181;
   public static final int MAX_WIDTH = 182;
   public static final int MEDIA_USAGE = 183;
   public static final int MIN_HEIGHT = 184;
   public static final int MIN_WIDTH = 185;
   public static final int NUMBER_COLUMNS_REPEATED = 186;
   public static final int NUMBER_COLUMNS_SPANNED = 187;
   public static final int NUMBER_ROWS_SPANNED = 188;
   public static final int ODD_OR_EVEN = 189;
   public static final int ORPHANS = 190;
   public static final int OVERFLOW = 191;
   public static final int PADDING = 192;
   public static final int PADDING_AFTER = 193;
   public static final int PADDING_AFTER_CONDITIONALITY = 194;
   public static final int PADDING_AFTER_LENGTH = 195;
   public static final int PADDING_BEFORE = 196;
   public static final int PADDING_BEFORE_CONDITIONALITY = 197;
   public static final int PADDING_BEFORE_LENGTH = 198;
   public static final int PADDING_BOTTOM = 199;
   public static final int PADDING_END = 200;
   public static final int PADDING_END_CONDITIONALITY = 201;
   public static final int PADDING_END_LENGTH = 202;
   public static final int PADDING_LEFT = 203;
   public static final int PADDING_RIGHT = 204;
   public static final int PADDING_START = 205;
   public static final int PADDING_START_CONDITIONALITY = 206;
   public static final int PADDING_START_LENGTH = 207;
   public static final int PADDING_TOP = 208;
   public static final int PAGE_BREAK_AFTER = 209;
   public static final int PAGE_BREAK_BEFORE = 210;
   public static final int PAGE_BREAK_INSIDE = 211;
   public static final int PAGE_HEIGHT = 212;
   public static final int PAGE_POSITION = 213;
   public static final int PAGE_WIDTH = 214;
   public static final int PAUSE = 215;
   public static final int PAUSE_AFTER = 216;
   public static final int PAUSE_BEFORE = 217;
   public static final int PITCH = 218;
   public static final int PITCH_RANGE = 219;
   public static final int PLAY_DURING = 220;
   public static final int POSITION = 221;
   public static final int PRECEDENCE = 222;
   public static final int PROVISIONAL_DISTANCE_BETWEEN_STARTS = 223;
   public static final int PROVISIONAL_LABEL_SEPARATION = 224;
   public static final int REFERENCE_ORIENTATION = 225;
   public static final int REF_ID = 226;
   public static final int REGION_NAME = 227;
   public static final int RELATIVE_ALIGN = 228;
   public static final int RELATIVE_POSITION = 229;
   public static final int RENDERING_INTENT = 230;
   public static final int RETRIEVE_BOUNDARY = 231;
   public static final int RETRIEVE_CLASS_NAME = 232;
   public static final int RETRIEVE_POSITION = 233;
   public static final int RICHNESS = 234;
   public static final int RIGHT = 235;
   public static final int ROLE = 236;
   public static final int RULE_STYLE = 237;
   public static final int RULE_THICKNESS = 238;
   public static final int SCALING = 239;
   public static final int SCALING_METHOD = 240;
   public static final int SCORE_SPACES = 241;
   public static final int SCRIPT = 242;
   public static final int SHOW_DESTINATION = 243;
   public static final int SIZE = 244;
   public static final int SOURCE_DOCUMENT = 245;
   public static final int SPACE_AFTER = 246;
   public static final int SPACE_AFTER_CONDITIONALITY = 247;
   public static final int SPACE_AFTER_MAXIMUM = 248;
   public static final int SPACE_AFTER_MINIMUM = 249;
   public static final int SPACE_AFTER_OPTIMUM = 250;
   public static final int SPACE_AFTER_PRECEDENCE = 251;
   public static final int SPACE_BEFORE = 252;
   public static final int SPACE_BEFORE_CONDITIONALITY = 253;
   public static final int SPACE_BEFORE_MAXIMUM = 254;
   public static final int SPACE_BEFORE_MINIMUM = 255;
   public static final int SPACE_BEFORE_OPTIMUM = 256;
   public static final int SPACE_BEFORE_PRECEDENCE = 257;
   public static final int SPACE_END = 258;
   public static final int SPACE_END_CONDITIONALITY = 259;
   public static final int SPACE_END_MAXIMUM = 260;
   public static final int SPACE_END_MINIMUM = 261;
   public static final int SPACE_END_OPTIMUM = 262;
   public static final int SPACE_END_PRECEDENCE = 263;
   public static final int SPACE_START = 264;
   public static final int SPACE_START_CONDITIONALITY = 265;
   public static final int SPACE_START_MAXIMUM = 266;
   public static final int SPACE_START_MINIMUM = 267;
   public static final int SPACE_START_OPTIMUM = 268;
   public static final int SPACE_START_PRECEDENCE = 269;
   public static final int SPAN = 270;
   public static final int SPEAK = 271;
   public static final int SPEAK_HEADER = 272;
   public static final int SPEAK_NUMERAL = 273;
   public static final int SPEAK_PUNCTUATION = 274;
   public static final int SPEECH_RATE = 275;
   public static final int SRC = 276;
   public static final int START_INDENT = 277;
   public static final int STARTING_STATE = 278;
   public static final int STARTS_ROW = 279;
   public static final int STRESS = 280;
   public static final int SUPPRESS_AT_LINE_BREAK = 281;
   public static final int SWITCH_TO = 282;
   public static final int TABLE_LAYOUT = 283;
   public static final int TABLE_OMIT_FOOTER_AT_BREAK = 284;
   public static final int TABLE_OMIT_HEADER_AT_BREAK = 285;
   public static final int TARGET_PRESENTATION_CONTEXT = 286;
   public static final int TARGET_PROCESSING_CONTEXT = 287;
   public static final int TARGET_STYLESHEET = 288;
   public static final int TEXT_ALIGN = 289;
   public static final int TEXT_ALIGN_LAST = 290;
   public static final int TEXT_ALTITUDE = 291;
   public static final int TEXT_DECORATION = 292;
   public static final int TEXT_DEPTH = 293;
   public static final int TEXT_INDENT = 294;
   public static final int TEXT_SHADOW = 295;
   public static final int TEXT_TRANSFORM = 296;
   public static final int TOP = 297;
   public static final int TREAT_AS_WORD_SPACE = 298;
   public static final int UNICODE_BIDI = 299;
   public static final int VERTICAL_ALIGN = 300;
   public static final int VISIBILITY = 301;
   public static final int VOICE_FAMILY = 302;
   public static final int VOLUME = 303;
   public static final int WHITE_SPACE = 304;
   public static final int WHITE_SPACE_COLLAPSE = 305;
   public static final int WHITE_SPACE_TREATMENT = 306;
   public static final int WIDOWS = 307;
   public static final int WIDTH = 308;
   public static final int WORD_SPACING = 309;
   public static final int WORD_SPACING_CONDITIONALITY = 310;
   public static final int WORD_SPACING_MAXIMUM = 311;
   public static final int WORD_SPACING_MINIMUM = 312;
   public static final int WORD_SPACING_OPTIMUM = 313;
   public static final int WORD_SPACING_PRECEDENCE = 314;
   public static final int WRAP_OPTION = 315;
   public static final int WRITING_MODE = 316;
   public static final int XML_LANG = 317;
   public static final int Z_INDEX = 318;
   public static final int TAB_POSITION = 319;
   public static final int TAB_ALIGN = 320;
   public static final int LABEL_FORMAT = 321;
   public static final int OUTLINE_LEVEL = 322;
   public static final int PROPERTY_COUNT = 323;
   public static final int TYPE_SIMPLE = 0;
   public static final int TYPE_COMPOUND = 1;
   public static final int TYPE_SHORTHAND = 2;
   public static final int UNIT_CENTIMETER = 1;
   public static final int UNIT_MILLIMETER = 2;
   public static final int UNIT_INCH = 3;
   public static final int UNIT_POINT = 4;
   public static final int UNIT_PICA = 5;
   public static final int UNIT_PIXEL = 6;
   public static final int UNIT_EM = 7;
   public static final int UNIT_EX = 8;
   public static final int UNIT_DEGREE = 1;
   public static final int UNIT_GRAD = 2;
   public static final int UNIT_RADIAN = 3;
   public static final Property[] list;
   private static final Hashtable indexes;
   public int index;
   public String name;
   public int type;
   public boolean inherited;
   public byte[] valueTypes;
   public int[] keywords;
   public Value initialValue;

   public Property(int var1, String var2, int var3, boolean var4, byte[] var5, int[] var6, Value var7) {
      this.index = var1;
      this.name = var2;
      this.type = var3;
      this.inherited = var4;
      this.valueTypes = var5;
      this.keywords = var6;
      this.initialValue = var7;
   }

   public static void check() {
      if (list.length != 323) {
         throw new Error("size mismatch: 323 " + list.length);
      } else {
         for(int var0 = 0; var0 < list.length; ++var0) {
            if (list[var0].index != var0) {
               throw new Error("index mismatch: " + var0 + " " + list[var0].index);
            }
         }

      }
   }

   public static int index(String var0) {
      Integer var1 = (Integer)indexes.get(var0);
      return var1 != null ? var1 : -1;
   }

   public static Property property(String var0) {
      int var1 = index(var0);
      return var1 >= 0 ? list[var1] : null;
   }

   public static String name(int var0) {
      return list[var0].name;
   }

   public Value initialValue(Context var1) {
      return this.initialValue;
   }

   public Value evaluate(String var1) {
      return this.evaluate(var1, (Context)null);
   }

   public Value evaluate(String var1, Context var2) {
      Value var3 = null;

      for(int var4 = 0; var4 < this.valueTypes.length; ++var4) {
         switch (this.valueTypes[var4]) {
            case 1:
               var3 = this.keyword(var1);
               break;
            case 2:
               var3 = this.integer(var1);
               break;
            case 3:
               var3 = this.number(var1);
               break;
            case 4:
               var3 = this.length(var1);
               break;
            case 5:
               var3 = this.lengthRange(var1);
               break;
            case 6:
               var3 = this.lengthConditional(var1);
               break;
            case 7:
               var3 = this.lengthBpIpDirection(var1);
               break;
            case 8:
               var3 = this.keep(var1);
               break;
            case 9:
               var3 = this.space(var1);
               break;
            case 10:
               var3 = this.angle(var1);
               break;
            case 11:
               var3 = this.time(var1);
               break;
            case 12:
               var3 = this.frequency(var1);
               break;
            case 13:
               var3 = this.percentage(var1);
               break;
            case 14:
               var3 = this.character(var1);
               break;
            case 15:
               var3 = this.string(var1);
               break;
            case 16:
               var3 = this.name(var1);
               break;
            case 17:
               var3 = this.id(var1);
               break;
            case 18:
               var3 = this.idref(var1);
               break;
            case 19:
               var3 = this.country(var1);
               break;
            case 20:
               var3 = this.language(var1);
               break;
            case 21:
               var3 = this.script(var1);
               break;
            case 22:
               var3 = this.uriSpecification(var1);
            case 23:
            case 28:
            case 29:
            default:
               break;
            case 24:
               var3 = this.color(var1);
               break;
            case 25:
               var3 = this.shadow(var1);
               break;
            case 26:
               var3 = this.shape(var1);
               break;
            case 27:
               var3 = this.list(var1);
               break;
            case 30:
               var3 = this.labelFormat(var1);
         }

         if (var3 != null) {
            break;
         }
      }

      if (var3 == null) {
         var3 = this.expression(var1);
      }

      return var3;
   }

   protected Value keyword(String var1) {
      int var2 = Keyword.index(var1);
      if (var2 < 0) {
         return null;
      } else {
         for(int var3 = 0; var3 < this.keywords.length; ++var3) {
            if (var2 == this.keywords[var3]) {
               return new Value((byte)1, var2);
            }
         }

         return null;
      }
   }

   protected Value integer(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         return null;
      }

      return new Value((byte)2, var2);
   }

   protected Value number(String var1) {
      double var2;
      try {
         var2 = Double.valueOf(var1);
      } catch (NumberFormatException var5) {
         return null;
      }

      return new Value((byte)3, var2);
   }

   protected Value length(String var1) {
      byte var2;
      if (var1.endsWith("cm")) {
         var2 = 1;
      } else if (var1.endsWith("mm")) {
         var2 = 2;
      } else if (var1.endsWith("in")) {
         var2 = 3;
      } else if (var1.endsWith("pt")) {
         var2 = 4;
      } else if (var1.endsWith("pc")) {
         var2 = 5;
      } else if (var1.endsWith("px")) {
         var2 = 6;
      } else if (var1.endsWith("em")) {
         var2 = 7;
      } else {
         if (!var1.endsWith("ex")) {
            return null;
         }

         var2 = 8;
      }

      double var3;
      try {
         Double var5 = Double.valueOf(var1.substring(0, var1.length() - 2));
         var3 = var5;
      } catch (NumberFormatException var6) {
         return null;
      }

      return new Value((byte)4, var2, var3);
   }

   protected Value lengthRange(String var1) {
      return null;
   }

   protected Value lengthConditional(String var1) {
      return null;
   }

   protected Value lengthBpIpDirection(String var1) {
      return null;
   }

   protected Value keep(String var1) {
      return null;
   }

   protected Value space(String var1) {
      return null;
   }

   protected Value angle(String var1) {
      byte var2;
      if (var1.endsWith("deg")) {
         var2 = 1;
         var1 = var1.substring(0, var1.length() - 3);
      } else if (var1.endsWith("grad")) {
         var2 = 2;
         var1 = var1.substring(0, var1.length() - 4);
      } else {
         if (!var1.endsWith("rad")) {
            return null;
         }

         var2 = 3;
         var1 = var1.substring(0, var1.length() - 3);
      }

      double var3;
      try {
         var3 = Double.valueOf(var1);
      } catch (NumberFormatException var6) {
         return null;
      }

      return new Value((byte)10, toDegrees(var3, var2));
   }

   private static double toDegrees(double var0, int var2) {
      double var3;
      switch (var2) {
         case 1:
         default:
            var3 = var0;
            break;
         case 2:
            var3 = 0.9 * var0;
            break;
         case 3:
            var3 = 180.0 * var0 / Math.PI;
      }

      if (var3 < 0.0) {
         var3 += 360.0;
      }

      return var3;
   }

   protected Value time(String var1) {
      double var2;
      try {
         if (var1.endsWith("ms")) {
            var1 = var1.substring(0, var1.length() - 2);
            var2 = Double.valueOf(var1) / 1000.0;
         } else {
            if (!var1.endsWith("s")) {
               return null;
            }

            var1 = var1.substring(0, var1.length() - 1);
            var2 = Double.valueOf(var1);
         }
      } catch (NumberFormatException var5) {
         return null;
      }

      return new Value((byte)11, var2);
   }

   protected Value frequency(String var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         return null;
      }

      return new Value((byte)12, var2);
   }

   protected Value percentage(String var1) {
      if (!var1.endsWith("%")) {
         return null;
      } else {
         double var2;
         try {
            Double var4 = Double.valueOf(var1.substring(0, var1.length() - 1));
            var2 = var4;
         } catch (NumberFormatException var5) {
            return null;
         }

         return new Value((byte)13, var2);
      }
   }

   protected Value character(String var1) {
      return var1.length() != 1 ? null : new Value((byte)14, var1.charAt(0));
   }

   protected Value string(String var1) {
      return new Value((byte)15, var1);
   }

   protected Value name(String var1) {
      return new Value((byte)16, var1);
   }

   protected Value id(String var1) {
      return new Value((byte)17, var1);
   }

   protected Value idref(String var1) {
      return new Value((byte)18, var1);
   }

   protected Value country(String var1) {
      return new Value((byte)19, var1);
   }

   protected Value language(String var1) {
      return new Value((byte)20, var1);
   }

   protected Value script(String var1) {
      return new Value((byte)21, var1);
   }

   protected Value uriSpecification(String var1) {
      if (var1.startsWith("url(")) {
         if (!var1.endsWith(")")) {
            return null;
         }

         var1 = var1.substring(4, var1.length() - 1).trim();
      }

      if (var1.length() == 0) {
         return null;
      } else {
         byte var2;
         int var3;
         switch (var1.charAt(0)) {
            case '"':
               var2 = 1;
               var3 = var1.lastIndexOf(34);
               break;
            case '\'':
               var2 = 1;
               var3 = var1.lastIndexOf(39);
               break;
            default:
               var2 = 0;
               var3 = var1.length();
         }

         if (var3 <= var2) {
            return null;
         } else {
            var1 = var1.substring(var2, var3);
            var1 = checkPath(var1);
            return new Value((byte)22, var1);
         }
      }
   }

   private static final String checkPath(String var0) {
      if (!SystemUtil.IS_WINDOWS) {
         return var0;
      } else {
         int var1 = var0.length();
         if (var1 >= 3) {
            if (isASCIILetter(var0.charAt(0)) && var0.charAt(1) == ':' && var0.charAt(2) == '\\') {
               return URLUtil.fileToLocation(new File(var0));
            }

            if (var1 >= 4 && var0.charAt(0) == '\\' && var0.charAt(1) == '\\' && isASCIILetter(var0.charAt(2))) {
               int var2 = var0.indexOf(92, 3);
               if (var2 >= 3) {
                  boolean var3 = true;

                  for(int var4 = 3; var4 < var2; ++var4) {
                     char var5 = var0.charAt(var4);
                     if (!isASCIILetter(var5) && (var5 < '0' || var5 > '9') && var5 != '-' && var5 != '_') {
                        var3 = false;
                        break;
                     }
                  }

                  if (var3) {
                     return URLUtil.fileToLocation(new File(var0));
                  }
               }
            }
         }

         if (var0.indexOf(92) >= 0) {
            var0 = var0.replace('\\', '/');
         }

         if (var0.indexOf(32) >= 0) {
            var0 = StringUtil.replaceAll(var0, " ", "%20");
         }

         return var0;
      }
   }

   private static final boolean isASCIILetter(char var0) {
      return var0 >= 'A' && var0 <= 'Z' || var0 >= 'a' && var0 <= 'z';
   }

   protected Value color(String var1) {
      Color var2 = Color.parse(var1);
      return var2 != null ? new Value((byte)24, var2) : null;
   }

   protected Value labelFormat(String var1) {
      LabelFormat var2 = LabelFormat.parse(var1);
      return var2 != null ? new Value((byte)30, var2) : null;
   }

   protected Value shadow(String var1) {
      return null;
   }

   protected Value shape(String var1) {
      Value[] var2 = new Value[4];
      String var3 = "auto";
      if (!var1.startsWith("rect(")) {
         return null;
      } else {
         var1 = var1.substring(5, var1.length());
         StringTokenizer var4 = new StringTokenizer(var1, " ,)");

         for(int var5 = 0; var5 < var2.length; ++var5) {
            if (!var4.hasMoreTokens()) {
               return null;
            }

            var1 = var4.nextToken();
            if (var1.equals(var3)) {
               var2[var5] = new Value((byte)4, 0.0);
            } else {
               var2[var5] = this.length(var1);
               if (var2[var5] == null) {
                  return null;
               }
            }
         }

         return new Value((byte)26, var2);
      }
   }

   protected Value list(String var1) {
      return null;
   }

   protected Value expression(String var1) {
      return Expression.parse(var1);
   }

   public Value compute(Value var1, Context var2) {
      switch (var1.type) {
         case 1:
            if (var1.keyword() == 88) {
               Context var4 = var2.parent();
               if (var4.properties != null) {
                  var1 = this.inherit(var4.properties);
               }
            }
            break;
         case 4:
            if (var1.unit() != 4) {
               var1 = this.length(var1, var2);
            }
            break;
         case 28:
            Expression var3 = var1.expression();
            var1 = var3.evaluate(this, var2);
            if (var1 != null && !this.isValidType(var1.type)) {
               var1 = this.convert(var1, var2);
            }
      }

      return var1;
   }

   protected boolean isValidType(byte var1) {
      for(int var2 = 0; var2 < this.valueTypes.length; ++var2) {
         if (var1 == this.valueTypes[var2]) {
            return true;
         }
      }

      return false;
   }

   protected Value convert(Value var1, Context var2) {
      if (var1.type == 3 && this.isValidType((byte)4)) {
         double var3 = var1.number();
         if (var3 == 0.0) {
            return Value.length(0.0, 4);
         }

         if (var3 > 0.0) {
            var3 = var3 / (double)var2.screenResolution * 72.0;
            return Value.length(var3, 4);
         }
      }

      return null;
   }

   public Value inherit(PropertyValues var1) {
      return var1.values[this.index];
   }

   protected Value length(Value var1, Context var2) {
      double var3 = var1.length();
      switch (var1.unit()) {
         case 1:
            var3 = 72.0 * var3 / 2.54;
            break;
         case 2:
            var3 = 72.0 * var3 / 25.4;
            break;
         case 3:
            var3 = 72.0 * var3;
         case 4:
         default:
            break;
         case 5:
            var3 = 12.0 * var3;
            break;
         case 6:
            var3 = var3 / (double)var2.screenResolution * 72.0;
            break;
         case 7:
            var3 *= var2.fontSize();
            break;
         case 8:
            var3 = var3 * var2.fontSize() / 2.0;
      }

      return new Value((byte)4, 4, var3);
   }

   public Value inheritedPropertyValue(String var1, Context var2) {
      int var3 = this.index;
      Context var4 = var2.parent();
      Value var5 = null;
      if (var1 != null) {
         var3 = index(var1);
         if (var3 < 0) {
            return null;
         }
      }

      if (!list[var3].inherited) {
         return null;
      } else {
         if (var4.properties != null) {
            if (var3 == 164) {
               var5 = var4.lineHeight;
            } else {
               var5 = var4.properties.values[var3];
            }
         }

         return var5;
      }
   }

   public Value fromParent(String var1, Context var2) {
      int var3 = this.index;
      Context var4 = var2.parent();
      if (var1 != null) {
         var3 = index(var1);
         if (var3 < 0) {
            return null;
         }
      }

      if (list[var3].type == 2 && var3 != this.index) {
         return null;
      } else {
         Value var5;
         if (var4.properties != null) {
            if (var3 == 164) {
               var5 = var4.lineHeight;
            } else {
               var5 = var4.properties.values[var3];
            }
         } else {
            var5 = list[var3].initialValue;
         }

         return var5;
      }
   }

   public Value fromNearestSpecifiedValue(String var1, Context var2) {
      int var3 = this.index;
      if (var1 != null) {
         var3 = index(var1);
         if (var3 < 0) {
            return null;
         }
      }

      Value var4 = var2.nearestSpecifiedValue(var3);
      if (var4 == null) {
         var4 = list[var3].initialValue;
      }

      return var4;
   }

   public Value fromTableColumn(String var1, Context var2) {
      int var3 = this.index;
      TableCell var5 = var2.tableCell;
      if (var1 != null) {
         var3 = index(var1);
         if (var3 < 0) {
            return null;
         }
      }

      if (var5 == null) {
         return null;
      } else {
         Value var7 = list[var3].initialValue;
         Integer var4 = new Integer(var5.columnNumber);
         TableColumn var6 = (TableColumn)var2.tableColumns.get(var4);
         if (var6 != null && var6.columnNumber == var5.columnNumber) {
            if (var6.numberColumnsSpanned == var5.numberColumnsSpanned) {
               var7 = var6.properties[var3];
            } else if (var5.numberColumnsSpanned == 1) {
               var7 = var6.properties[var3];
            }
         }

         return var7;
      }
   }

   public Value bodyStart(Context var1) {
      Value var2 = null;
      Context var3 = var1.ancestor(19);
      if (var3 != null) {
         Value[] var4 = var3.properties.values;
         double var5 = var4[277].length();
         var5 += var4[223].length();
         var2 = new Value((byte)4, 4, var5);
      }

      return var2;
   }

   public Value labelEnd(Context var1) {
      Value var2 = null;
      Context var3 = var1.ancestor(19);
      double var4 = var1.translator.referenceWidth();
      if (var3 != null) {
         if (var4 > 0.0) {
            Value[] var6 = var3.properties.values;
            var4 -= var6[277].length();
            var4 -= var6[223].length();
            var4 += var6[224].length();
            var2 = new Value((byte)4, 4, var4);
         } else {
            var2 = Value.LENGTH_ZERO;
         }
      }

      return var2;
   }

   static {
      list = new Property[]{new Property(0, "absolute-position", 0, false, new byte[]{1}, new int[]{10, 1, 69, 88}, Value.KEYWORD_AUTO), new Property(1, "active-state", 0, false, new byte[]{1}, new int[]{108, 222, 3, 79, 70}, (Value)null), new Property(2, "alignment-adjust", 0, false, new byte[]{1, 13, 4}, new int[]{10, 15, 17, 199, 121, 34, 5, 198, 81, 7, 74, 116, 88}, Value.KEYWORD_AUTO), new Property(3, "alignment-baseline", 0, false, new byte[]{1}, new int[]{10, 15, 17, 199, 121, 34, 5, 198, 81, 7, 74, 116, 88}, Value.KEYWORD_AUTO), new Property(4, "auto-restore", 0, true, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Azimuth(5, "azimuth", 0, true, new byte[]{1, 27, 10}, new int[]{102, 62, 100, 32, 31, 33, 165, 63, 167, 18, 101, 166, 88}, new Value((byte)1, 31)), new Background(6, "background", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(7, "background-attachment", 0, false, new byte[]{1}, new int[]{174, 69, 88}, new Value((byte)1, 174)), new Property(8, "background-color", 0, false, new byte[]{1, 24}, new int[]{206, 88}, new Value((byte)1, 206)), new Property(9, "background-image", 0, false, new byte[]{1, 22}, new int[]{125, 88}, Value.KEYWORD_NONE), new BackgroundPosition(10, "background-position", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(11, "background-position-horizontal", 0, false, new byte[]{1, 13, 4}, new int[]{100, 31, 165, 88}, new Value((byte)13, 0.0)), new Property(12, "background-position-vertical", 0, false, new byte[]{1, 13, 4}, new int[]{204, 31, 27, 88}, new Value((byte)13, 0.0)), new Property(13, "background-repeat", 0, false, new byte[]{1}, new int[]{156, 157, 158, 136, 88}, new Value((byte)1, 156)), new BaselineShift(14, "baseline-shift", 0, false, new byte[]{1, 13, 4}, new int[]{15, 193, 194, 88}, new Value((byte)1, 15)), new Property(15, "blank-or-not-blank", 0, false, new byte[]{1}, new int[]{21, 128, 9, 88}, new Value((byte)1, 9)), new LengthRange(16, "block-progression-dimension", 1, false, new byte[]{1, 4, 13, 5}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(17, "block-progression-dimension.maximum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new Property(18, "block-progression-dimension.minimum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new Property(19, "block-progression-dimension.optimum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new Border(20, "border", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(21, "border-after-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(22, "border-after-precedence", 0, false, new byte[]{1, 2}, new int[]{72, 88}, (Value)null), new Property(23, "border-after-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new BorderWidthConditional(24, "border-after-width", 1, false, new byte[]{1, 4, 6}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new Property(25, "border-after-width.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_RETAIN), new Property(26, "border-after-width.length", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203}, Value.KEYWORD_MEDIUM), new Property(27, "border-before-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(28, "border-before-precedence", 0, false, new byte[]{1, 2}, new int[]{72, 88}, (Value)null), new Property(29, "border-before-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new BorderWidthConditional(30, "border-before-width", 1, false, new byte[]{1, 4, 6}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new Property(31, "border-before-width.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_RETAIN), new Property(32, "border-before-width.length", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203}, Value.KEYWORD_MEDIUM), new BorderBottom(33, "border-bottom", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(34, "border-bottom-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(35, "border-bottom-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new Property(36, "border-bottom-width", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new Property(37, "border-collapse", 0, true, new byte[]{1}, new int[]{37, 177, 88}, new Value((byte)1, 37)), new BorderColor(38, "border-color", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(39, "border-end-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(40, "border-end-precedence", 0, false, new byte[]{1, 2}, new int[]{72, 88}, (Value)null), new Property(41, "border-end-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new BorderWidthConditional(42, "border-end-width", 1, false, new byte[]{1, 4, 6}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new Property(43, "border-end-width.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(44, "border-end-width.length", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203}, Value.KEYWORD_MEDIUM), new BorderLeft(45, "border-left", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(46, "border-left-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(47, "border-left-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new Property(48, "border-left-width", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new BorderRight(49, "border-right", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(50, "border-right-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(51, "border-right-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new Property(52, "border-right-width", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new LengthBpIpDirection(53, "border-separation", 1, true, new byte[]{1, 4, 7}, new int[]{88}, new Value((byte)7, new Value[]{Value.LENGTH_ZERO, Value.LENGTH_ZERO})), new Property(54, "border-separation.block-progression-direction", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(55, "border-separation.inline-progression-direction", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new BorderSpacing(56, "border-spacing", 2, true, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(57, "border-start-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(58, "border-start-precedence", 0, false, new byte[]{1, 2}, new int[]{72, 88}, (Value)null), new Property(59, "border-start-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new BorderWidthConditional(60, "border-start-width", 1, false, new byte[]{1, 4, 6}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new Property(61, "border-start-width.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(62, "border-start-width.length", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203}, Value.KEYWORD_MEDIUM), new BorderStyle(63, "border-style", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new BorderTop(64, "border-top", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(65, "border-top-color", 0, false, new byte[]{1, 24}, new int[]{88}, (Value)null), new Property(66, "border-top-style", 0, false, new byte[]{1}, new int[]{125, 75, 49, 42, 187, 50, 73, 164, 89, 143, 88}, Value.KEYWORD_NONE), new Property(67, "border-top-width", 0, false, new byte[]{1, 4}, new int[]{202, 118, 203, 88}, Value.KEYWORD_MEDIUM), new BorderWidth(68, "border-width", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(69, "bottom", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(70, "break-after", 0, false, new byte[]{1}, new int[]{10, 38, 146, 57, 141, 88}, Value.KEYWORD_AUTO), new Property(71, "break-before", 0, false, new byte[]{1}, new int[]{10, 38, 146, 57, 141, 88}, Value.KEYWORD_AUTO), new Property(72, "caption-side", 0, true, new byte[]{1}, new int[]{16, 4, 190, 52, 204, 27, 100, 165, 88}, new Value((byte)1, 16)), new Property(73, "case-name", 0, false, new byte[]{16}, (int[])null, (Value)null), new Property(74, "case-title", 0, false, new byte[]{15}, (int[])null, (Value)null), new Property(75, "character", 0, false, new byte[]{14}, (int[])null, (Value)null), new Property(76, "clear", 0, false, new byte[]{1}, new int[]{190, 52, 100, 165, 26, 125, 88}, Value.KEYWORD_NONE), new Property(77, "clip", 0, false, new byte[]{1, 26}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(78, "color", 0, true, new byte[]{1, 24}, new int[]{88}, new Value((byte)24, Color.list[1])), new Property(79, "color-profile-name", 0, false, new byte[]{1, 16}, new int[]{88}, (Value)null), new Property(80, "column-count", 0, false, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 1.0)), new Property(81, "column-gap", 0, false, new byte[]{1, 4, 13}, new int[]{88}, new Value((byte)4, 4, 12.0)), new Property(82, "column-number", 0, false, new byte[]{3}, (int[])null, (Value)null), new Property(83, "column-width", 0, false, new byte[]{4, 13, 29}, (int[])null, (Value)null), new Property(84, "content-height", 0, false, new byte[]{1, 4, 13}, new int[]{10, 173, 246, 247, 88}, Value.KEYWORD_AUTO), new Property(85, "content-type", 0, false, new byte[]{1, 15}, new int[]{10}, Value.KEYWORD_AUTO), new Property(86, "content-width", 0, false, new byte[]{1, 4, 13}, new int[]{10, 173, 246, 247, 88}, Value.KEYWORD_AUTO), new Property(87, "country", 0, true, new byte[]{1, 19}, new int[]{125, 88}, Value.KEYWORD_NONE), new Cue(88, "cue", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(89, "cue-after", 0, false, new byte[]{1, 22}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(90, "cue-before", 0, false, new byte[]{1, 22}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(91, "destination-placement-offset", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(92, "direction", 0, true, new byte[]{1}, new int[]{115, 170, 88}, new Value((byte)1, 115)), new Property(93, "display-align", 0, true, new byte[]{1}, new int[]{10, 16, 31, 4, 88}, Value.KEYWORD_AUTO), new Property(94, "dominant-baseline", 0, false, new byte[]{1}, new int[]{10, 219, 131, 161, 81, 7, 74, 116, 88}, Value.KEYWORD_AUTO), new Property(95, "elevation", 0, true, new byte[]{1, 10}, new int[]{19, 103, 0, 78, 111, 88}, new Value((byte)1, 103)), new Property(96, "empty-cells", 0, true, new byte[]{1}, new int[]{178, 76, 88}, new Value((byte)1, 178)), new Property(97, "end-indent", 0, true, new byte[]{1, 4}, new int[]{88}, Value.LENGTH_ZERO), new Property(98, "ends-row", 0, false, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Property(99, "extent", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new ExternalDestination(100, "external-destination", 0, false, new byte[]{22}, (int[])null, new Value((byte)22, "")), new Property(101, "float", 0, false, new byte[]{1}, new int[]{16, 190, 52, 100, 165, 125, 88}, Value.KEYWORD_NONE), new Property(102, "flow-name", 0, false, new byte[]{16}, (int[])null, (Value)null), new Font(103, "font", 2, true, new byte[]{1, 27}, new int[]{30, 80, 119, 120, 185, 192, 88}, (Value)null), new FontFamily(104, "font-family", 0, true, new byte[]{1, 27}, new int[]{88}, new Value((byte)27, new Value[]{new Value((byte)15, "serif")})), new Property(105, "font-selection-strategy", 0, true, new byte[]{1}, new int[]{10, 35, 88}, Value.KEYWORD_AUTO), new FontSize(106, "font-size", 0, true, new byte[]{1, 4, 13}, new int[]{236, 243, 182, 118, 95, 239, 235, 96, 183, 88}, Value.KEYWORD_MEDIUM), new Property(107, "font-size-adjust", 0, true, new byte[]{1, 3}, new int[]{125, 88}, Value.KEYWORD_NONE), new FontStretch(108, "font-stretch", 0, true, new byte[]{1}, new int[]{127, 223, 123, 210, 59, 39, 175, 176, 58, 60, 211, 88}, Value.KEYWORD_NORMAL), new Property(109, "font-style", 0, true, new byte[]{1}, new int[]{127, 92, 139, 14, 88}, Value.KEYWORD_NORMAL), new Property(110, "font-variant", 0, true, new byte[]{1}, new int[]{127, 184, 88}, Value.KEYWORD_NORMAL), new FontWeight(111, "font-weight", 0, true, new byte[]{1, 2}, new int[]{127, 24, 25, 104, 88}, new Value((byte)2, 400)), new Property(112, "force-page-count", 0, false, new byte[]{1}, new int[]{10, 56, 140, 53, 54, 132, 88}, Value.KEYWORD_AUTO), new Property(113, "format", 0, false, new byte[]{15}, (int[])null, new Value((byte)15, "1")), new Property(114, "glyph-orientation-horizontal", 0, true, new byte[]{1, 10}, new int[]{88}, new Value((byte)10, 0.0)), new Property(115, "glyph-orientation-vertical", 0, true, new byte[]{1, 10}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(116, "grouping-separator", 0, false, new byte[]{14}, (int[])null, (Value)null), new Property(117, "grouping-size", 0, false, new byte[]{3}, (int[])null, (Value)null), new Property(118, "height", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(119, "hyphenate", 0, true, new byte[]{1}, new int[]{61, 209, 88}, Value.KEYWORD_FALSE), new Property(120, "hyphenation-character", 0, true, new byte[]{1, 14}, new int[]{88}, new Value((byte)14, 8208)), new Property(121, "hyphenation-keep", 0, true, new byte[]{1}, new int[]{10, 38, 146, 88}, Value.KEYWORD_AUTO), new Property(122, "hyphenation-ladder-count", 0, true, new byte[]{1, 3}, new int[]{133, 88}, new Value((byte)1, 133)), new Property(123, "hyphenation-push-character-count", 0, true, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 2.0)), new Property(124, "hyphenation-remain-character-count", 0, true, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 2.0)), new Property(125, "id", 0, false, new byte[]{17}, (int[])null, (Value)null), new Property(126, "indicate-destination", 0, false, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Property(127, "initial-page-number", 0, false, new byte[]{1, 3}, new int[]{10, 12, 11, 88}, Value.KEYWORD_AUTO), new LengthRange(128, "inline-progression-dimension", 1, false, new byte[]{1, 4, 13, 5}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(129, "inline-progression-dimension.maximum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new Property(130, "inline-progression-dimension.minimum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new Property(131, "inline-progression-dimension.optimum", 0, false, new byte[]{1, 4, 13}, new int[]{10}, Value.KEYWORD_AUTO), new InternalDestination(132, "internal-destination", 0, false, new byte[]{18}, (int[])null, new Value((byte)18, "")), new Property(133, "intrusion-displace", 0, true, new byte[]{1}, new int[]{10, 125, 105, 87, 23, 88}, Value.KEYWORD_AUTO), new Keep(134, "keep-together", 1, true, new byte[]{1, 2, 8}, new int[]{10, 8, 88}, Value.KEEP_AUTO), new Property(135, "keep-together.within-column", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(136, "keep-together.within-line", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(137, "keep-together.within-page", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Keep(138, "keep-with-next", 1, false, new byte[]{1, 2, 8}, new int[]{10, 8, 88}, Value.KEEP_AUTO), new Property(139, "keep-with-next.within-column", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(140, "keep-with-next.within-line", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(141, "keep-with-next.within-page", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Keep(142, "keep-with-previous", 1, false, new byte[]{1, 2, 8}, new int[]{10, 8, 88}, Value.KEEP_AUTO), new Property(143, "keep-with-previous.within-column", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(144, "keep-with-previous.within-line", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(145, "keep-with-previous.within-page", 0, false, new byte[]{1, 2}, new int[]{10, 8}, Value.KEYWORD_AUTO), new Property(146, "language", 0, true, new byte[]{1, 20}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(147, "last-line-end-indent", 0, true, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new Property(148, "leader-alignment", 0, true, new byte[]{1}, new int[]{125, 153, 146, 88}, Value.KEYWORD_NONE), new LengthRange(149, "leader-length", 1, true, new byte[]{1, 4, 13, 5}, new int[]{88}, new Value((byte)5, new Value[]{Value.LENGTH_ZERO, new Value((byte)4, 4, 12.0), new Value((byte)13, 100.0)})), new Property(150, "leader-length.maximum", 0, false, new byte[]{4, 13}, (int[])null, new Value((byte)13, 100.0)), new Property(151, "leader-length.minimum", 0, false, new byte[]{4, 13}, (int[])null, Value.LENGTH_ZERO), new Property(152, "leader-length.optimum", 0, false, new byte[]{4, 13}, (int[])null, new Value((byte)4, 4, 12.0)), new Property(153, "leader-pattern", 0, true, new byte[]{1}, new int[]{188, 171, 48, 216, 88}, new Value((byte)1, 188)), new Property(154, "leader-pattern-width", 0, true, new byte[]{1, 4}, new int[]{217, 88}, new Value((byte)1, 217)), new Property(155, "left", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Space(156, "letter-spacing", 1, true, new byte[]{1, 4, 9}, new int[]{127, 88}, Value.KEYWORD_NORMAL), new Property(157, "letter-spacing.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(158, "letter-spacing.maximum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(159, "letter-spacing.minimum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(160, "letter-spacing.optimum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(161, "letter-spacing.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.KEYWORD_FORCE), new Property(162, "letter-value", 0, false, new byte[]{1}, new int[]{10, 7, 205}, Value.KEYWORD_AUTO), new Property(163, "linefeed-treatment", 0, true, new byte[]{1}, new int[]{82, 152, 207, 208, 88}, new Value((byte)1, 207)), new LineHeight(164, "line-height", 1, true, new byte[]{1, 4, 3, 13, 9}, new int[]{127, 88}, Value.KEYWORD_NORMAL), new Property(165, "line-height.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_RETAIN), new Property(166, "line-height.maximum", 0, false, new byte[]{1, 4, 3, 13}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(167, "line-height.minimum", 0, false, new byte[]{1, 4, 3, 13}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(168, "line-height.optimum", 0, false, new byte[]{1, 4, 3, 13}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(169, "line-height.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.KEYWORD_FORCE), new Property(170, "line-height-shift-adjustment", 0, true, new byte[]{1}, new int[]{40, 45, 88}, new Value((byte)1, 40)), new Property(171, "line-stacking-strategy", 0, true, new byte[]{1}, new int[]{106, 71, 117, 88}, new Value((byte)1, 106)), new Margin(172, "margin", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(173, "margin-bottom", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.LENGTH_ZERO), new Property(174, "margin-left", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.LENGTH_ZERO), new Property(175, "margin-right", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.LENGTH_ZERO), new Property(176, "margin-top", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.LENGTH_ZERO), new Property(177, "marker-class-name", 0, false, new byte[]{16}, (int[])null, (Value)null), new Property(178, "master-name", 0, false, new byte[]{16}, (int[])null, (Value)null), new Property(179, "master-reference", 0, false, new byte[]{16}, (int[])null, (Value)null), new Property(180, "max-height", 0, false, new byte[]{1, 4, 13}, new int[]{125, 88}, Value.LENGTH_ZERO), new Property(181, "maximum-repeats", 0, false, new byte[]{1, 3}, new int[]{133, 88}, new Value((byte)1, 133)), new Property(182, "max-width", 0, false, new byte[]{1, 4, 13}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(183, "media-usage", 0, false, new byte[]{1}, new int[]{10, 148, 28, 212}, Value.KEYWORD_AUTO), new Property(184, "min-height", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new Property(185, "min-width", 0, false, new byte[]{1, 4, 13}, new int[]{88}, (Value)null), new Property(186, "number-columns-repeated", 0, false, new byte[]{3}, (int[])null, Value.NUMBER_ONE), new Property(187, "number-columns-spanned", 0, false, new byte[]{3}, (int[])null, Value.NUMBER_ONE), new Property(188, "number-rows-spanned", 0, false, new byte[]{3}, (int[])null, Value.NUMBER_ONE), new Property(189, "odd-or-even", 0, false, new byte[]{1}, new int[]{140, 56, 9, 88}, new Value((byte)1, 9)), new Property(190, "orphans", 0, true, new byte[]{1, 2}, new int[]{88}, new Value((byte)2, 2)), new Property(191, "overflow", 0, false, new byte[]{1}, new int[]{221, 75, 174, 55, 10, 88}, Value.KEYWORD_AUTO), new Padding(192, "padding", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new PaddingConditional(193, "padding-after", 1, false, new byte[]{1, 4, 13, 6}, new int[]{88}, Value.LENGTH_ZERO), new Property(194, "padding-after.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_RETAIN), new Property(195, "padding-after.length", 0, false, new byte[]{4, 13}, (int[])null, Value.LENGTH_ZERO), new PaddingConditional(196, "padding-before", 1, false, new byte[]{1, 4, 13, 6}, new int[]{88}, Value.LENGTH_ZERO), new Property(197, "padding-before.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_RETAIN), new Property(198, "padding-before.length", 0, false, new byte[]{4, 13}, (int[])null, Value.LENGTH_ZERO), new Property(199, "padding-bottom", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new PaddingConditional(200, "padding-end", 1, false, new byte[]{1, 4, 13, 6}, new int[]{88}, Value.LENGTH_ZERO), new Property(201, "padding-end.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(202, "padding-end.length", 0, false, new byte[]{4, 13}, (int[])null, Value.LENGTH_ZERO), new Property(203, "padding-left", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new Property(204, "padding-right", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new PaddingConditional(205, "padding-start", 1, false, new byte[]{1, 4, 13, 6}, new int[]{88}, Value.LENGTH_ZERO), new Property(206, "padding-start.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(207, "padding-start.length", 0, false, new byte[]{4, 13}, (int[])null, Value.LENGTH_ZERO), new Property(208, "padding-top", 0, false, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new PageBreakAfter(209, "page-break-after", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new PageBreakBefore(210, "page-break-before", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new PageBreakInside(211, "page-break-inside", 2, true, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(212, "page-height", 0, false, new byte[]{1, 4}, new int[]{10, 86, 88}, Value.KEYWORD_AUTO), new Property(213, "page-position", 0, false, new byte[]{1}, new int[]{66, 97, 162, 9, 88}, new Value((byte)1, 9)), new Property(214, "page-width", 0, false, new byte[]{1, 4}, new int[]{10, 86, 88}, Value.KEYWORD_AUTO), new Pause(215, "pause", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(216, "pause-after", 0, false, new byte[]{1, 11, 13}, new int[]{88}, new Value((byte)13, 100.0)), new Property(217, "pause-before", 0, false, new byte[]{1, 11, 13}, new int[]{88}, new Value((byte)13, 100.0)), new Property(218, "pitch", 0, true, new byte[]{1, 12}, new int[]{241, 110, 118, 77, 238, 88}, Value.KEYWORD_MEDIUM), new Property(219, "pitch-range", 0, true, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 50.0)), new PlayDuring(220, "play-during", 0, false, new byte[]{1, 27}, new int[]{10, 125, 88}, Value.KEYWORD_AUTO), new Position(221, "position", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(222, "precedence", 0, false, new byte[]{1}, new int[]{209, 61, 88}, Value.KEYWORD_FALSE), new Property(223, "provisional-distance-between-starts", 0, true, new byte[]{1, 4}, new int[]{88}, new Value((byte)4, 4, 24.0)), new Property(224, "provisional-label-separation", 0, true, new byte[]{1, 4}, new int[]{88}, new Value((byte)4, 4, 6.0)), new ReferenceOrientation(225, "reference-orientation", 0, true, new byte[]{1, 2}, new int[]{88}, new Value((byte)2, 0)), new Property(226, "ref-id", 0, false, new byte[]{1, 18}, new int[]{88}, (Value)null), new Property(227, "region-name", 0, false, new byte[]{1, 16}, new int[]{232, 234, 233, 231, 230, 226, 228}, (Value)null), new Property(228, "relative-align", 0, true, new byte[]{1}, new int[]{16, 15, 88}, new Value((byte)1, 16)), new Property(229, "relative-position", 0, false, new byte[]{1}, new int[]{191, 154, 88}, new Value((byte)1, 191)), new Property(230, "rendering-intent", 0, false, new byte[]{1}, new int[]{10, 149, 155, 172, 2, 88}, Value.KEYWORD_AUTO), new Property(231, "retrieve-boundary", 0, false, new byte[]{1}, new int[]{146, 147, 46}, new Value((byte)1, 147)), new Property(232, "retrieve-class-name", 0, false, new byte[]{16}, (int[])null, (Value)null), new Property(233, "retrieve-position", 0, false, new byte[]{1}, new int[]{68, 67, 99, 98}, new Value((byte)1, 68)), new Property(234, "richness", 0, true, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 50.0)), new Property(235, "right", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(236, "role", 0, false, new byte[]{1, 22, 15}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(237, "rule-style", 0, true, new byte[]{1}, new int[]{125, 49, 42, 187, 50, 73, 164, 88}, new Value((byte)1, 187)), new Property(238, "rule-thickness", 0, true, new byte[]{4}, (int[])null, new Value((byte)4, 4, 1.0)), new Property(239, "scaling", 0, false, new byte[]{1}, new int[]{214, 126, 88}, new Value((byte)1, 214)), new Property(240, "scaling-method", 0, false, new byte[]{1}, new int[]{10, 91, 160, 88}, Value.KEYWORD_AUTO), new Property(241, "score-spaces", 0, true, new byte[]{1}, new int[]{209, 61, 88}, Value.KEYWORD_TRUE), new Property(242, "script", 0, true, new byte[]{1, 21}, new int[]{125, 10, 88}, Value.KEYWORD_AUTO), new Property(243, "show-destination", 0, false, new byte[]{1}, new int[]{159, 124}, new Value((byte)1, 159)), new Size(244, "size", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new SourceDocument(245, "source-document", 0, false, new byte[]{1, 27}, new int[]{125, 88}, Value.KEYWORD_NONE), new Space(246, "space-after", 1, false, new byte[]{1, 4, 9}, new int[]{88}, Value.SPACE_ZERO), new Property(247, "space-after.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(248, "space-after.maximum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(249, "space-after.minimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(250, "space-after.optimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(251, "space-after.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.INTEGER_ZERO), new Space(252, "space-before", 1, false, new byte[]{1, 4, 9}, new int[]{88}, Value.SPACE_ZERO), new Property(253, "space-before.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(254, "space-before.maximum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(255, "space-before.minimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(256, "space-before.optimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(257, "space-before.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.INTEGER_ZERO), new Space(258, "space-end", 1, false, new byte[]{1, 4, 9}, new int[]{88}, Value.SPACE_ZERO), new Property(259, "space-end.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(260, "space-end.maximum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(261, "space-end.minimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(262, "space-end.optimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(263, "space-end.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.INTEGER_ZERO), new Space(264, "space-start", 1, false, new byte[]{1, 4, 9}, new int[]{88}, Value.SPACE_ZERO), new Property(265, "space-start.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(266, "space-start.maximum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(267, "space-start.minimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(268, "space-start.optimum", 0, false, new byte[]{4}, (int[])null, Value.LENGTH_ZERO), new Property(269, "space-start.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.INTEGER_ZERO), new Property(270, "span", 0, false, new byte[]{1}, new int[]{125, 6, 88}, Value.KEYWORD_NONE), new Property(271, "speak", 0, true, new byte[]{1}, new int[]{127, 125, 189, 88}, Value.KEYWORD_NORMAL), new Property(272, "speak-header", 0, true, new byte[]{1}, new int[]{142, 8, 88}, new Value((byte)1, 142)), new Property(273, "speak-numeral", 0, true, new byte[]{1}, new int[]{43, 41, 88}, new Value((byte)1, 41)), new Property(274, "speak-punctuation", 0, true, new byte[]{1}, new int[]{36, 125, 88}, Value.KEYWORD_NONE), new SpeechRate(275, "speech-rate", 0, true, new byte[]{1, 3}, new int[]{242, 180, 118, 64, 237, 65, 181, 88}, new Value((byte)3, 180.0)), new Property(276, "src", 0, false, new byte[]{1, 22}, new int[]{88}, (Value)null), new Property(277, "start-indent", 0, true, new byte[]{1, 4}, new int[]{88}, Value.LENGTH_ZERO), new Property(278, "starting-state", 0, false, new byte[]{1}, new int[]{178, 76}, new Value((byte)1, 178)), new Property(279, "starts-row", 0, false, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Property(280, "stress", 0, true, new byte[]{1, 3}, new int[]{88}, new Value((byte)3, 50.0)), new Property(281, "suppress-at-line-break", 0, false, new byte[]{1}, new int[]{10, 195, 163, 88}, Value.KEYWORD_AUTO), new SwitchTo(282, "switch-to", 0, false, new byte[]{1, 27}, new int[]{229, 227, 225}, new Value((byte)1, 225)), new Property(283, "table-layout", 0, false, new byte[]{1}, new int[]{10, 69, 88}, Value.KEYWORD_AUTO), new Property(284, "table-omit-footer-at-break", 0, false, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Property(285, "table-omit-header-at-break", 0, false, new byte[]{1}, new int[]{209, 61}, Value.KEYWORD_FALSE), new Property(286, "target-presentation-context", 0, false, new byte[]{1, 22}, new int[]{220}, new Value((byte)1, 220)), new Property(287, "target-processing-context", 0, false, new byte[]{1, 22}, new int[]{47}, new Value((byte)1, 47)), new Property(288, "target-stylesheet", 0, false, new byte[]{1, 22}, new int[]{218}, new Value((byte)1, 218)), new Property(289, "text-align", 0, true, new byte[]{1, 15}, new int[]{190, 31, 52, 93, 90, 144, 100, 165, 88}, new Value((byte)1, 190)), new Property(290, "text-align-last", 0, true, new byte[]{1}, new int[]{154, 190, 31, 52, 93, 90, 144, 100, 165, 88}, new Value((byte)1, 154)), new Property(291, "text-altitude", 0, false, new byte[]{1, 4}, new int[]{217, 88}, new Value((byte)1, 217)), new TextDecoration(292, "text-decoration", 0, false, new byte[]{1, 27}, new int[]{125, 213, 137, 145, 135, 107, 134, 22, 130, 88}, Value.KEYWORD_NONE), new Property(293, "text-depth", 0, false, new byte[]{1, 4}, new int[]{217, 88}, new Value((byte)1, 217)), new Property(294, "text-indent", 0, true, new byte[]{1, 4, 13}, new int[]{88}, Value.LENGTH_ZERO), new TextShadow(295, "text-shadow", 0, false, new byte[]{1, 27}, new int[]{125, 88}, Value.KEYWORD_NONE), new Property(296, "text-transform", 0, true, new byte[]{1}, new int[]{29, 215, 112, 125, 88}, Value.KEYWORD_NONE), new Property(297, "top", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(298, "treat-as-word-space", 0, false, new byte[]{1}, new int[]{10, 209, 61, 88}, Value.KEYWORD_AUTO), new Property(299, "unicode-bidi", 0, false, new byte[]{1}, new int[]{127, 51, 20, 88}, Value.KEYWORD_NORMAL), new VerticalAlign(300, "vertical-align", 2, false, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(301, "visibility", 0, false, new byte[]{1}, new int[]{221, 75, 37, 88}, new Value((byte)1, 221)), new VoiceFamily(302, "voice-family", 0, true, new byte[]{1, 27}, new int[]{88}, new Value((byte)27, new Value[]{new Value((byte)15, "male")})), new Volume(303, "volume", 0, true, new byte[]{1, 3, 13}, new int[]{179, 244, 186, 118, 109, 240, 88}, new Value((byte)3, 50.0)), new WhiteSpace(304, "white-space", 2, true, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(305, "white-space-collapse", 0, true, new byte[]{1}, new int[]{61, 209, 88}, Value.KEYWORD_TRUE), new Property(306, "white-space-treatment", 0, true, new byte[]{1}, new int[]{82, 152, 84, 83, 85, 88}, new Value((byte)1, 85)), new Property(307, "widows", 0, true, new byte[]{1, 2}, new int[]{88}, new Value((byte)2, 2)), new Property(308, "width", 0, false, new byte[]{1, 4, 13}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Space(309, "word-spacing", 1, true, new byte[]{1, 4, 9}, new int[]{127, 88}, Value.KEYWORD_NORMAL), new Property(310, "word-spacing.conditionality", 0, false, new byte[]{1}, new int[]{44, 163}, Value.KEYWORD_DISCARD), new Property(311, "word-spacing.maximum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(312, "word-spacing.minimum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(313, "word-spacing.optimum", 0, false, new byte[]{1, 4}, new int[]{127}, Value.KEYWORD_NORMAL), new Property(314, "word-spacing.precedence", 0, false, new byte[]{1, 2}, new int[]{72}, Value.KEYWORD_FORCE), new Property(315, "wrap-option", 0, true, new byte[]{1}, new int[]{138, 224, 88}, new Value((byte)1, 224)), new Property(316, "writing-mode", 0, true, new byte[]{1}, new int[]{114, 169, 197, 113, 168, 196, 88}, new Value((byte)1, 114)), new XmlLang(317, "xml:lang", 2, true, new byte[]{1, 27}, new int[]{88}, (Value)null), new Property(318, "z-index", 0, false, new byte[]{1, 2}, new int[]{10, 88}, Value.KEYWORD_AUTO), new Property(319, "tab-position", 0, false, new byte[]{4}, (int[])null, (Value)null), new Property(320, "tab-align", 0, false, new byte[]{1}, new int[]{100, 31, 165, 245}, (Value)null), new Property(321, "label-format", 0, false, new byte[]{30}, (int[])null, (Value)null), new Property(322, "outline-level", 0, false, new byte[]{2}, (int[])null, (Value)null)};
      indexes = new Hashtable();

      for(int var0 = 0; var0 < list.length; ++var0) {
         indexes.put(list[var0].name, new Integer(var0));
      }

   }
}
