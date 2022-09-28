package com.xmlmind.fo.properties;

import java.util.Hashtable;

public final class Keyword {
   public static final int ABOVE = 0;
   public static final int ABSOLUTE = 1;
   public static final int ABSOLUTE_COLORIMETRIC = 2;
   public static final int ACTIVE = 3;
   public static final int AFTER = 4;
   public static final int AFTER_EDGE = 5;
   public static final int ALL = 6;
   public static final int ALPHABETIC = 7;
   public static final int ALWAYS = 8;
   public static final int ANY = 9;
   public static final int AUTO = 10;
   public static final int AUTO_EVEN = 11;
   public static final int AUTO_ODD = 12;
   public static final int AVOID = 13;
   public static final int BACKSLANT = 14;
   public static final int BASELINE = 15;
   public static final int BEFORE = 16;
   public static final int BEFORE_EDGE = 17;
   public static final int BEHIND = 18;
   public static final int BELOW = 19;
   public static final int BIDI_OVERRIDE = 20;
   public static final int BLANK = 21;
   public static final int BLINK = 22;
   public static final int BLOCK = 23;
   public static final int BOLD = 24;
   public static final int BOLDER = 25;
   public static final int BOTH = 26;
   public static final int BOTTOM = 27;
   public static final int BOUNDED_IN_ONE_DIMENSION = 28;
   public static final int CAPITALIZE = 29;
   public static final int CAPTION = 30;
   public static final int CENTER = 31;
   public static final int CENTER_LEFT = 32;
   public static final int CENTER_RIGHT = 33;
   public static final int CENTRAL = 34;
   public static final int CHARACTER_BY_CHARACTER = 35;
   public static final int CODE = 36;
   public static final int COLLAPSE = 37;
   public static final int COLUMN = 38;
   public static final int CONDENSED = 39;
   public static final int CONSIDER_SHIFTS = 40;
   public static final int CONTINUOUS = 41;
   public static final int DASHED = 42;
   public static final int DIGITS = 43;
   public static final int DISCARD = 44;
   public static final int DISREGARD_SHIFTS = 45;
   public static final int DOCUMENT = 46;
   public static final int DOCUMENT_ROOT = 47;
   public static final int DOTS = 48;
   public static final int DOTTED = 49;
   public static final int DOUBLE = 50;
   public static final int EMBED = 51;
   public static final int END = 52;
   public static final int END_ON_EVEN = 53;
   public static final int END_ON_ODD = 54;
   public static final int ERROR_IF_OVERFLOW = 55;
   public static final int EVEN = 56;
   public static final int EVEN_PAGE = 57;
   public static final int EXPANDED = 58;
   public static final int EXTRA_CONDENSED = 59;
   public static final int EXTRA_EXPANDED = 60;
   public static final int FALSE = 61;
   public static final int FAR_LEFT = 62;
   public static final int FAR_RIGHT = 63;
   public static final int FAST = 64;
   public static final int FASTER = 65;
   public static final int FIRST = 66;
   public static final int FIRST_INCLUDING_CARRYOVER = 67;
   public static final int FIRST_STARTING_WITHIN_PAGE = 68;
   public static final int FIXED = 69;
   public static final int FOCUS = 70;
   public static final int FONT_HEIGHT = 71;
   public static final int FORCE = 72;
   public static final int GROOVE = 73;
   public static final int HANGING = 74;
   public static final int HIDDEN = 75;
   public static final int HIDE = 76;
   public static final int HIGH = 77;
   public static final int HIGHER = 78;
   public static final int HOVER = 79;
   public static final int ICON = 80;
   public static final int IDEOGRAPHIC = 81;
   public static final int IGNORE = 82;
   public static final int IGNORE_IF_AFTER_LINEFEED = 83;
   public static final int IGNORE_IF_BEFORE_LINEFEED = 84;
   public static final int IGNORE_IF_SURROUNDING_LINEFEED = 85;
   public static final int INDEFINITE = 86;
   public static final int INDENT = 87;
   public static final int INHERIT = 88;
   public static final int INSET = 89;
   public static final int INSIDE = 90;
   public static final int INTEGER_PIXELS = 91;
   public static final int ITALIC = 92;
   public static final int JUSTIFY = 93;
   public static final int LANDSCAPE = 94;
   public static final int LARGE = 95;
   public static final int LARGER = 96;
   public static final int LAST = 97;
   public static final int LAST_ENDING_WITHIN_PAGE = 98;
   public static final int LAST_STARTING_WITHIN_PAGE = 99;
   public static final int LEFT = 100;
   public static final int LEFTWARDS = 101;
   public static final int LEFT_SIDE = 102;
   public static final int LEVEL = 103;
   public static final int LIGHTER = 104;
   public static final int LINE = 105;
   public static final int LINE_HEIGHT = 106;
   public static final int LINE_THROUGH = 107;
   public static final int LINK = 108;
   public static final int LOUD = 109;
   public static final int LOW = 110;
   public static final int LOWER = 111;
   public static final int LOWERCASE = 112;
   public static final int LR = 113;
   public static final int LR_TB = 114;
   public static final int LTR = 115;
   public static final int MATHEMATICAL = 116;
   public static final int MAX_HEIGHT = 117;
   public static final int MEDIUM = 118;
   public static final int MENU = 119;
   public static final int MESSAGE_BOX = 120;
   public static final int MIDDLE = 121;
   public static final int MIX = 122;
   public static final int NARROWER = 123;
   public static final int NEW = 124;
   public static final int NONE = 125;
   public static final int NON_UNIFORM = 126;
   public static final int NORMAL = 127;
   public static final int NOT_BLANK = 128;
   public static final int NOWRAP = 129;
   public static final int NO_BLINK = 130;
   public static final int NO_CHANGE = 131;
   public static final int NO_FORCE = 132;
   public static final int NO_LIMIT = 133;
   public static final int NO_LINE_THROUGH = 134;
   public static final int NO_OVERLINE = 135;
   public static final int NO_REPEAT = 136;
   public static final int NO_UNDERLINE = 137;
   public static final int NO_WRAP = 138;
   public static final int OBLIQUE = 139;
   public static final int ODD = 140;
   public static final int ODD_PAGE = 141;
   public static final int ONCE = 142;
   public static final int OUTSET = 143;
   public static final int OUTSIDE = 144;
   public static final int OVERLINE = 145;
   public static final int PAGE = 146;
   public static final int PAGE_SEQUENCE = 147;
   public static final int PAGINATE = 148;
   public static final int PERCEPTUAL = 149;
   public static final int PORTRAIT = 150;
   public static final int PRE = 151;
   public static final int PRESERVE = 152;
   public static final int REFERENCE_AREA = 153;
   public static final int RELATIVE = 154;
   public static final int RELATIVE_COLORIMETRIC = 155;
   public static final int REPEAT = 156;
   public static final int REPEAT_X = 157;
   public static final int REPEAT_Y = 158;
   public static final int REPLACE = 159;
   public static final int RESAMPLE_ANY_METHOD = 160;
   public static final int RESET_SIZE = 161;
   public static final int REST = 162;
   public static final int RETAIN = 163;
   public static final int RIDGE = 164;
   public static final int RIGHT = 165;
   public static final int RIGHTWARDS = 166;
   public static final int RIGHT_SIDE = 167;
   public static final int RL = 168;
   public static final int RL_TB = 169;
   public static final int RTL = 170;
   public static final int RULE = 171;
   public static final int SATURATION = 172;
   public static final int SCALE_TO_FIT = 173;
   public static final int SCROLL = 174;
   public static final int SEMI_CONDENSED = 175;
   public static final int SEMI_EXPANDED = 176;
   public static final int SEPARATE = 177;
   public static final int SHOW = 178;
   public static final int SILENT = 179;
   public static final int SLOW = 180;
   public static final int SLOWER = 181;
   public static final int SMALL = 182;
   public static final int SMALLER = 183;
   public static final int SMALL_CAPS = 184;
   public static final int SMALL_CAPTION = 185;
   public static final int SOFT = 186;
   public static final int SOLID = 187;
   public static final int SPACE = 188;
   public static final int SPELL_OUT = 189;
   public static final int START = 190;
   public static final int STATIC = 191;
   public static final int STATUS_BAR = 192;
   public static final int SUB = 193;
   public static final int SUPER = 194;
   public static final int SUPPRESS = 195;
   public static final int TB = 196;
   public static final int TB_RL = 197;
   public static final int TEXT_AFTER_EDGE = 198;
   public static final int TEXT_BEFORE_EDGE = 199;
   public static final int TEXT_BOTTOM = 200;
   public static final int TEXT_TOP = 201;
   public static final int THIN = 202;
   public static final int THICK = 203;
   public static final int TOP = 204;
   public static final int TRADITIONAL = 205;
   public static final int TRANSPARENT = 206;
   public static final int TREAT_AS_SPACE = 207;
   public static final int TREAT_AS_ZERO_WIDTH_SPACE = 208;
   public static final int TRUE = 209;
   public static final int ULTRA_CONDENSED = 210;
   public static final int ULTRA_EXPANDED = 211;
   public static final int UNBOUNDED = 212;
   public static final int UNDERLINE = 213;
   public static final int UNIFORM = 214;
   public static final int UPPERCASE = 215;
   public static final int USE_CONTENT = 216;
   public static final int USE_FONT_METRICS = 217;
   public static final int USE_NORMAL_STYLESHEET = 218;
   public static final int USE_SCRIPT = 219;
   public static final int USE_TARGET_PROCESSING_CONTEXT = 220;
   public static final int VISIBLE = 221;
   public static final int VISITED = 222;
   public static final int WIDER = 223;
   public static final int WRAP = 224;
   public static final int XSL_ANY = 225;
   public static final int XSL_BEFORE_FLOAT_SEPARATOR = 226;
   public static final int XSL_FOLLOWING = 227;
   public static final int XSL_FOOTNOTE_SEPARATOR = 228;
   public static final int XSL_PRECEDING = 229;
   public static final int XSL_REGION_AFTER = 230;
   public static final int XSL_REGION_BEFORE = 231;
   public static final int XSL_REGION_BODY = 232;
   public static final int XSL_REGION_END = 233;
   public static final int XSL_REGION_START = 234;
   public static final int XX_LARGE = 235;
   public static final int XX_SMALL = 236;
   public static final int X_FAST = 237;
   public static final int X_HIGH = 238;
   public static final int X_LARGE = 239;
   public static final int X_LOUD = 240;
   public static final int X_LOW = 241;
   public static final int X_SLOW = 242;
   public static final int X_SMALL = 243;
   public static final int X_SOFT = 244;
   public static final int DECIMAL = 245;
   public static final int SCALE_DOWN_TO_FIT = 246;
   public static final int SCALE_UP_TO_FIT = 247;
   public static final int KEYWORD_COUNT = 246;
   public static final Keyword[] list = new Keyword[]{new Keyword(0, "above"), new Keyword(1, "absolute"), new Keyword(2, "absolute-colorimetric"), new Keyword(3, "active"), new Keyword(4, "after"), new Keyword(5, "after-edge"), new Keyword(6, "all"), new Keyword(7, "alphabetic"), new Keyword(8, "always"), new Keyword(9, "any"), new Keyword(10, "auto"), new Keyword(11, "auto-even"), new Keyword(12, "auto-odd"), new Keyword(13, "avoid"), new Keyword(14, "backslant"), new Keyword(15, "baseline"), new Keyword(16, "before"), new Keyword(17, "before-edge"), new Keyword(18, "behind"), new Keyword(19, "below"), new Keyword(20, "bidi-override"), new Keyword(21, "blank"), new Keyword(22, "blink"), new Keyword(23, "block"), new Keyword(24, "bold"), new Keyword(25, "bolder"), new Keyword(26, "both"), new Keyword(27, "bottom"), new Keyword(28, "bounded-in-one-dimension"), new Keyword(29, "capitalize"), new Keyword(30, "caption"), new Keyword(31, "center"), new Keyword(32, "center-left"), new Keyword(33, "center-right"), new Keyword(34, "central"), new Keyword(35, "character-by-character"), new Keyword(36, "code"), new Keyword(37, "collapse"), new Keyword(38, "column"), new Keyword(39, "condensed"), new Keyword(40, "consider-shifts"), new Keyword(41, "continuous"), new Keyword(42, "dashed"), new Keyword(43, "digits"), new Keyword(44, "discard"), new Keyword(45, "disregard-shifts"), new Keyword(46, "document"), new Keyword(47, "document-root"), new Keyword(48, "dots"), new Keyword(49, "dotted"), new Keyword(50, "double"), new Keyword(51, "embed"), new Keyword(52, "end"), new Keyword(53, "end-on-even"), new Keyword(54, "end-on-odd"), new Keyword(55, "error-if-overflow"), new Keyword(56, "even"), new Keyword(57, "even-page"), new Keyword(58, "expanded"), new Keyword(59, "extra-condensed"), new Keyword(60, "extra-expanded"), new Keyword(61, "false"), new Keyword(62, "far-left"), new Keyword(63, "far-right"), new Keyword(64, "fast"), new Keyword(65, "faster"), new Keyword(66, "first"), new Keyword(67, "first-including-carryover"), new Keyword(68, "first-starting-within-page"), new Keyword(69, "fixed"), new Keyword(70, "focus"), new Keyword(71, "font-height"), new Keyword(72, "force"), new Keyword(73, "groove"), new Keyword(74, "hanging"), new Keyword(75, "hidden"), new Keyword(76, "hide"), new Keyword(77, "high"), new Keyword(78, "higher"), new Keyword(79, "hover"), new Keyword(80, "icon"), new Keyword(81, "ideographic"), new Keyword(82, "ignore"), new Keyword(83, "ignore-if-after-linefeed"), new Keyword(84, "ignore-if-before-linefeed"), new Keyword(85, "ignore-if-surrounding-linefeed"), new Keyword(86, "indefinite"), new Keyword(87, "indent"), new Keyword(88, "inherit"), new Keyword(89, "inset"), new Keyword(90, "inside"), new Keyword(91, "integer-pixels"), new Keyword(92, "italic"), new Keyword(93, "justify"), new Keyword(94, "landscape"), new Keyword(95, "large"), new Keyword(96, "larger"), new Keyword(97, "last"), new Keyword(98, "last-ending-within-page"), new Keyword(99, "last-starting-within-page"), new Keyword(100, "left"), new Keyword(101, "leftwards"), new Keyword(102, "left-side"), new Keyword(103, "level"), new Keyword(104, "lighter"), new Keyword(105, "line"), new Keyword(106, "line-height"), new Keyword(107, "line-through"), new Keyword(108, "link"), new Keyword(109, "loud"), new Keyword(110, "low"), new Keyword(111, "lower"), new Keyword(112, "lowercase"), new Keyword(113, "lr"), new Keyword(114, "lr-tb"), new Keyword(115, "ltr"), new Keyword(116, "mathematical"), new Keyword(117, "max-height"), new Keyword(118, "medium"), new Keyword(119, "menu"), new Keyword(120, "message-box"), new Keyword(121, "middle"), new Keyword(122, "mix"), new Keyword(123, "narrower"), new Keyword(124, "new"), new Keyword(125, "none"), new Keyword(126, "non-uniform"), new Keyword(127, "normal"), new Keyword(128, "not-blank"), new Keyword(129, "nowrap"), new Keyword(130, "no-blink"), new Keyword(131, "no-change"), new Keyword(132, "no-force"), new Keyword(133, "no-limit"), new Keyword(134, "no-line-through"), new Keyword(135, "no-overline"), new Keyword(136, "no-repeat"), new Keyword(137, "no-underline"), new Keyword(138, "no-wrap"), new Keyword(139, "oblique"), new Keyword(140, "odd"), new Keyword(141, "odd-page"), new Keyword(142, "once"), new Keyword(143, "outset"), new Keyword(144, "outside"), new Keyword(145, "overline"), new Keyword(146, "page"), new Keyword(147, "page-sequence"), new Keyword(148, "paginate"), new Keyword(149, "perceptual"), new Keyword(150, "portrait"), new Keyword(151, "pre"), new Keyword(152, "preserve"), new Keyword(153, "reference-area"), new Keyword(154, "relative"), new Keyword(155, "relative-colorimetric"), new Keyword(156, "repeat"), new Keyword(157, "repeat-x"), new Keyword(158, "repeat-y"), new Keyword(159, "replace"), new Keyword(160, "resample-any-method"), new Keyword(161, "reset-size"), new Keyword(162, "rest"), new Keyword(163, "retain"), new Keyword(164, "ridge"), new Keyword(165, "right"), new Keyword(166, "rightwards"), new Keyword(167, "right-side"), new Keyword(168, "rl"), new Keyword(169, "rl_tb"), new Keyword(170, "rtl"), new Keyword(171, "rule"), new Keyword(172, "saturation"), new Keyword(173, "scale-to-fit"), new Keyword(174, "scroll"), new Keyword(175, "semi-condensed"), new Keyword(176, "semi-expanded"), new Keyword(177, "separate"), new Keyword(178, "show"), new Keyword(179, "silent"), new Keyword(180, "slow"), new Keyword(181, "slower"), new Keyword(182, "small"), new Keyword(183, "smaller"), new Keyword(184, "small-caps"), new Keyword(185, "small-caption"), new Keyword(186, "soft"), new Keyword(187, "solid"), new Keyword(188, "space"), new Keyword(189, "spell-out"), new Keyword(190, "start"), new Keyword(191, "static"), new Keyword(192, "status-bar"), new Keyword(193, "sub"), new Keyword(194, "super"), new Keyword(195, "suppress"), new Keyword(196, "tb"), new Keyword(197, "tb-rl"), new Keyword(198, "text-after-edge"), new Keyword(199, "text-before-edge"), new Keyword(200, "text-bottom"), new Keyword(201, "text-top"), new Keyword(202, "thin"), new Keyword(203, "thick"), new Keyword(204, "top"), new Keyword(205, "traditional"), new Keyword(206, "transparent"), new Keyword(207, "treat-as-space"), new Keyword(208, "treat-as-zero-width-space"), new Keyword(209, "true"), new Keyword(210, "ultra-condensed"), new Keyword(211, "ultra-expanded"), new Keyword(212, "unbounded"), new Keyword(213, "underline"), new Keyword(214, "uniform"), new Keyword(215, "uppercase"), new Keyword(216, "use-content"), new Keyword(217, "use-font-metrics"), new Keyword(218, "use-normal-stylesheet"), new Keyword(219, "use-script"), new Keyword(220, "use-target-processing-context"), new Keyword(221, "visible"), new Keyword(222, "visited"), new Keyword(223, "wider"), new Keyword(224, "wrap"), new Keyword(225, "xsl-any"), new Keyword(226, "xsl-before-float-separator"), new Keyword(227, "xsl-following"), new Keyword(228, "xsl-footnote-separator"), new Keyword(229, "xsl-preceding"), new Keyword(230, "xsl-region-after"), new Keyword(231, "xsl-region-before"), new Keyword(232, "xsl-region-body"), new Keyword(233, "xsl-region-end"), new Keyword(234, "xsl-region-start"), new Keyword(235, "xx-large"), new Keyword(236, "xx-small"), new Keyword(237, "x-fast"), new Keyword(238, "x-high"), new Keyword(239, "x-large"), new Keyword(240, "x-loud"), new Keyword(241, "x-low"), new Keyword(242, "x-slow"), new Keyword(243, "x-small"), new Keyword(244, "x-soft"), new Keyword(245, "decimal"), new Keyword(246, "scale-down-to-fit"), new Keyword(247, "scale-up-to-fit")};
   private static final Hashtable indexes = new Hashtable();
   public int index;
   public String keyword;

   private Keyword(int var1, String var2) {
      this.index = var1;
      this.keyword = var2;
   }

   public static void check() {
      if (list.length != 246) {
         throw new Error("size mismatch: 246 " + list.length);
      } else {
         for(int var0 = 0; var0 < list.length; ++var0) {
            if (list[var0].index != var0) {
               throw new Error("index mismatch: " + var0 + " " + list[var0].index);
            }
         }

      }
   }

   public static int index(String var0) {
      Object var1 = indexes.get(var0);
      return var1 != null ? (Integer)var1 : -1;
   }

   public static String keyword(int var0) {
      return list[var0].keyword;
   }

   static {
      for(int var0 = 0; var0 < list.length; ++var0) {
         indexes.put(list[var0].keyword, new Integer(var0));
      }

   }
}
