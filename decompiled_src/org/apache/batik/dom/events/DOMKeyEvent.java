package org.apache.batik.dom.events;

import org.w3c.dom.views.AbstractView;

public class DOMKeyEvent extends DOMUIEvent {
   public static final int CHAR_UNDEFINED = 65535;
   public static final int DOM_VK_0 = 48;
   public static final int DOM_VK_1 = 49;
   public static final int DOM_VK_2 = 50;
   public static final int DOM_VK_3 = 51;
   public static final int DOM_VK_4 = 52;
   public static final int DOM_VK_5 = 53;
   public static final int DOM_VK_6 = 54;
   public static final int DOM_VK_7 = 55;
   public static final int DOM_VK_8 = 56;
   public static final int DOM_VK_9 = 57;
   public static final int DOM_VK_A = 65;
   public static final int DOM_VK_ACCEPT = 30;
   public static final int DOM_VK_ADD = 107;
   public static final int DOM_VK_AGAIN = 65481;
   public static final int DOM_VK_ALL_CANDIDATES = 256;
   public static final int DOM_VK_ALPHANUMERIC = 240;
   public static final int DOM_VK_ALT = 18;
   public static final int DOM_VK_ALT_GRAPH = 65406;
   public static final int DOM_VK_AMPERSAND = 150;
   public static final int DOM_VK_ASTERISK = 151;
   public static final int DOM_VK_AT = 512;
   public static final int DOM_VK_B = 66;
   public static final int DOM_VK_BACK_QUOTE = 192;
   public static final int DOM_VK_BACK_SLASH = 92;
   public static final int DOM_VK_BACK_SPACE = 8;
   public static final int DOM_VK_BRACELEFT = 161;
   public static final int DOM_VK_BRACERIGHT = 162;
   public static final int DOM_VK_C = 67;
   public static final int DOM_VK_CANCEL = 3;
   public static final int DOM_VK_CAPS_LOCK = 20;
   public static final int DOM_VK_CIRCUMFLEX = 514;
   public static final int DOM_VK_CLEAR = 12;
   public static final int DOM_VK_CLOSE_BRACKET = 93;
   public static final int DOM_VK_CODE_INPUT = 258;
   public static final int DOM_VK_COLON = 513;
   public static final int DOM_VK_COMMA = 44;
   public static final int DOM_VK_COMPOSE = 65312;
   public static final int DOM_VK_CONTROL = 17;
   public static final int DOM_VK_CONVERT = 28;
   public static final int DOM_VK_COPY = 65485;
   public static final int DOM_VK_CUT = 65489;
   public static final int DOM_VK_D = 68;
   public static final int DOM_VK_DEAD_ABOVEDOT = 134;
   public static final int DOM_VK_DEAD_ABOVERING = 136;
   public static final int DOM_VK_DEAD_ACUTE = 129;
   public static final int DOM_VK_DEAD_BREVE = 133;
   public static final int DOM_VK_DEAD_CARON = 138;
   public static final int DOM_VK_DEAD_CEDILLA = 139;
   public static final int DOM_VK_DEAD_CIRCUMFLEX = 130;
   public static final int DOM_VK_DEAD_DIAERESIS = 135;
   public static final int DOM_VK_DEAD_DOUBLEACUTE = 137;
   public static final int DOM_VK_DEAD_GRAVE = 128;
   public static final int DOM_VK_DEAD_IOTA = 141;
   public static final int DOM_VK_DEAD_MACRON = 132;
   public static final int DOM_VK_DEAD_OGONEK = 140;
   public static final int DOM_VK_DEAD_SEMIVOICED_SOUND = 143;
   public static final int DOM_VK_DEAD_TILDE = 131;
   public static final int DOM_VK_DEAD_VOICED_SOUND = 142;
   public static final int DOM_VK_DECIMAL = 110;
   public static final int DOM_VK_DELETE = 127;
   public static final int DOM_VK_DIVIDE = 111;
   public static final int DOM_VK_DOLLAR = 515;
   public static final int DOM_VK_DOWN = 40;
   public static final int DOM_VK_E = 69;
   public static final int DOM_VK_END = 35;
   public static final int DOM_VK_ENTER = 13;
   public static final int DOM_VK_EQUALS = 61;
   public static final int DOM_VK_ESCAPE = 27;
   public static final int DOM_VK_EURO_SIGN = 516;
   public static final int DOM_VK_EXCLAMATION_MARK = 517;
   public static final int DOM_VK_F = 70;
   public static final int DOM_VK_F1 = 112;
   public static final int DOM_VK_F10 = 121;
   public static final int DOM_VK_F11 = 122;
   public static final int DOM_VK_F12 = 123;
   public static final int DOM_VK_F13 = 61440;
   public static final int DOM_VK_F14 = 61441;
   public static final int DOM_VK_F15 = 61442;
   public static final int DOM_VK_F16 = 61443;
   public static final int DOM_VK_F17 = 61444;
   public static final int DOM_VK_F18 = 61445;
   public static final int DOM_VK_F19 = 61446;
   public static final int DOM_VK_F2 = 113;
   public static final int DOM_VK_F20 = 61447;
   public static final int DOM_VK_F21 = 61448;
   public static final int DOM_VK_F22 = 61449;
   public static final int DOM_VK_F23 = 61450;
   public static final int DOM_VK_F24 = 61451;
   public static final int DOM_VK_F3 = 114;
   public static final int DOM_VK_F4 = 115;
   public static final int DOM_VK_F5 = 116;
   public static final int DOM_VK_F6 = 117;
   public static final int DOM_VK_F7 = 118;
   public static final int DOM_VK_F8 = 119;
   public static final int DOM_VK_F9 = 120;
   public static final int DOM_VK_FINAL = 24;
   public static final int DOM_VK_FIND = 65488;
   public static final int DOM_VK_FULL_WIDTH = 243;
   public static final int DOM_VK_G = 71;
   public static final int DOM_VK_GREATER = 160;
   public static final int DOM_VK_H = 72;
   public static final int DOM_VK_HALF_WIDTH = 244;
   public static final int DOM_VK_HELP = 156;
   public static final int DOM_VK_HIRAGANA = 242;
   public static final int DOM_VK_HOME = 36;
   public static final int DOM_VK_I = 73;
   public static final int DOM_VK_INSERT = 155;
   public static final int DOM_VK_INVERTED_EXCLAMATION_MARK = 518;
   public static final int DOM_VK_J = 74;
   public static final int DOM_VK_JAPANESE_HIRAGANA = 260;
   public static final int DOM_VK_JAPANESE_KATAKANA = 259;
   public static final int DOM_VK_JAPANESE_ROMAN = 261;
   public static final int DOM_VK_K = 75;
   public static final int DOM_VK_KANA = 21;
   public static final int DOM_VK_KANJI = 25;
   public static final int DOM_VK_KATAKANA = 241;
   public static final int DOM_VK_KP_DOWN = 225;
   public static final int DOM_VK_KP_LEFT = 226;
   public static final int DOM_VK_KP_RIGHT = 227;
   public static final int DOM_VK_KP_UP = 224;
   public static final int DOM_VK_L = 76;
   public static final int DOM_VK_LEFT = 37;
   public static final int DOM_VK_LEFT_PARENTHESIS = 519;
   public static final int DOM_VK_LESS = 153;
   public static final int DOM_VK_M = 77;
   public static final int DOM_VK_META = 157;
   public static final int DOM_VK_MINUS = 45;
   public static final int DOM_VK_MODECHANGE = 31;
   public static final int DOM_VK_MULTIPLY = 106;
   public static final int DOM_VK_N = 78;
   public static final int DOM_VK_NONCONVERT = 29;
   public static final int DOM_VK_NUM_LOCK = 144;
   public static final int DOM_VK_NUMBER_SIGN = 520;
   public static final int DOM_VK_NUMPAD0 = 96;
   public static final int DOM_VK_NUMPAD1 = 97;
   public static final int DOM_VK_NUMPAD2 = 98;
   public static final int DOM_VK_NUMPAD3 = 99;
   public static final int DOM_VK_NUMPAD4 = 100;
   public static final int DOM_VK_NUMPAD5 = 101;
   public static final int DOM_VK_NUMPAD6 = 102;
   public static final int DOM_VK_NUMPAD7 = 103;
   public static final int DOM_VK_NUMPAD8 = 104;
   public static final int DOM_VK_NUMPAD9 = 105;
   public static final int DOM_VK_O = 79;
   public static final int DOM_VK_OPEN_BRACKET = 91;
   public static final int DOM_VK_P = 80;
   public static final int DOM_VK_PAGE_DOWN = 34;
   public static final int DOM_VK_PAGE_UP = 33;
   public static final int DOM_VK_PASTE = 65487;
   public static final int DOM_VK_PAUSE = 19;
   public static final int DOM_VK_PERIOD = 46;
   public static final int DOM_VK_PLUS = 521;
   public static final int DOM_VK_PREVIOUS_CANDIDATE = 257;
   public static final int DOM_VK_PRINTSCREEN = 154;
   public static final int DOM_VK_PROPS = 65482;
   public static final int DOM_VK_Q = 81;
   public static final int DOM_VK_QUOTE = 222;
   public static final int DOM_VK_QUOTEDBL = 152;
   public static final int DOM_VK_R = 82;
   public static final int DOM_VK_RIGHT = 39;
   public static final int DOM_VK_RIGHT_PARENTHESIS = 522;
   public static final int DOM_VK_ROMAN_CHARACTERS = 245;
   public static final int DOM_VK_S = 83;
   public static final int DOM_VK_SCROLL_LOCK = 145;
   public static final int DOM_VK_SEMICOLON = 59;
   public static final int DOM_VK_SEPARATER = 108;
   public static final int DOM_VK_SHIFT = 16;
   public static final int DOM_VK_SLASH = 47;
   public static final int DOM_VK_SPACE = 32;
   public static final int DOM_VK_STOP = 65480;
   public static final int DOM_VK_SUBTRACT = 109;
   public static final int DOM_VK_T = 84;
   public static final int DOM_VK_TAB = 9;
   public static final int DOM_VK_U = 85;
   public static final int DOM_VK_UNDEFINED = 0;
   public static final int DOM_VK_UNDERSCORE = 523;
   public static final int DOM_VK_UNDO = 65483;
   public static final int DOM_VK_UP = 38;
   public static final int DOM_VK_V = 86;
   public static final int DOM_VK_W = 87;
   public static final int DOM_VK_X = 88;
   public static final int DOM_VK_Y = 89;
   public static final int DOM_VK_Z = 90;
   protected boolean ctrlKey;
   protected boolean altKey;
   protected boolean shiftKey;
   protected boolean metaKey;
   protected int keyCode;
   protected int charCode;

   public boolean getCtrlKey() {
      return this.ctrlKey;
   }

   public boolean getShiftKey() {
      return this.shiftKey;
   }

   public boolean getAltKey() {
      return this.altKey;
   }

   public boolean getMetaKey() {
      return this.metaKey;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public int getCharCode() {
      return this.charCode;
   }

   public void initKeyEvent(String var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, int var8, int var9, AbstractView var10) {
      this.initUIEvent(var1, var2, var3, var10, 0);
      this.ctrlKey = var4;
      this.altKey = var5;
      this.shiftKey = var6;
      this.metaKey = var7;
      this.keyCode = var8;
      this.charCode = var9;
   }
}
