package org.w3c.css.sac;

public interface LexicalUnit {
   short SAC_OPERATOR_COMMA = 0;
   short SAC_OPERATOR_PLUS = 1;
   short SAC_OPERATOR_MINUS = 2;
   short SAC_OPERATOR_MULTIPLY = 3;
   short SAC_OPERATOR_SLASH = 4;
   short SAC_OPERATOR_MOD = 5;
   short SAC_OPERATOR_EXP = 6;
   short SAC_OPERATOR_LT = 7;
   short SAC_OPERATOR_GT = 8;
   short SAC_OPERATOR_LE = 9;
   short SAC_OPERATOR_GE = 10;
   short SAC_OPERATOR_TILDE = 11;
   short SAC_INHERIT = 12;
   short SAC_INTEGER = 13;
   short SAC_REAL = 14;
   short SAC_EM = 15;
   short SAC_EX = 16;
   short SAC_PIXEL = 17;
   short SAC_INCH = 18;
   short SAC_CENTIMETER = 19;
   short SAC_MILLIMETER = 20;
   short SAC_POINT = 21;
   short SAC_PICA = 22;
   short SAC_PERCENTAGE = 23;
   short SAC_URI = 24;
   short SAC_COUNTER_FUNCTION = 25;
   short SAC_COUNTERS_FUNCTION = 26;
   short SAC_RGBCOLOR = 27;
   short SAC_DEGREE = 28;
   short SAC_GRADIAN = 29;
   short SAC_RADIAN = 30;
   short SAC_MILLISECOND = 31;
   short SAC_SECOND = 32;
   short SAC_HERTZ = 33;
   short SAC_KILOHERTZ = 34;
   short SAC_IDENT = 35;
   short SAC_STRING_VALUE = 36;
   short SAC_ATTR = 37;
   short SAC_RECT_FUNCTION = 38;
   short SAC_UNICODERANGE = 39;
   short SAC_SUB_EXPRESSION = 40;
   short SAC_FUNCTION = 41;
   short SAC_DIMENSION = 42;

   short getLexicalUnitType();

   LexicalUnit getNextLexicalUnit();

   LexicalUnit getPreviousLexicalUnit();

   int getIntegerValue();

   float getFloatValue();

   String getDimensionUnitText();

   String getFunctionName();

   LexicalUnit getParameters();

   String getStringValue();

   LexicalUnit getSubValues();
}
