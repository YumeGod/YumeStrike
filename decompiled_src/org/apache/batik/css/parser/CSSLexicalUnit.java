package org.apache.batik.css.parser;

import org.w3c.css.sac.LexicalUnit;

public abstract class CSSLexicalUnit implements LexicalUnit {
   public static final String UNIT_TEXT_CENTIMETER = "cm";
   public static final String UNIT_TEXT_DEGREE = "deg";
   public static final String UNIT_TEXT_EM = "em";
   public static final String UNIT_TEXT_EX = "ex";
   public static final String UNIT_TEXT_GRADIAN = "grad";
   public static final String UNIT_TEXT_HERTZ = "Hz";
   public static final String UNIT_TEXT_INCH = "in";
   public static final String UNIT_TEXT_KILOHERTZ = "kHz";
   public static final String UNIT_TEXT_MILLIMETER = "mm";
   public static final String UNIT_TEXT_MILLISECOND = "ms";
   public static final String UNIT_TEXT_PERCENTAGE = "%";
   public static final String UNIT_TEXT_PICA = "pc";
   public static final String UNIT_TEXT_PIXEL = "px";
   public static final String UNIT_TEXT_POINT = "pt";
   public static final String UNIT_TEXT_RADIAN = "rad";
   public static final String UNIT_TEXT_REAL = "";
   public static final String UNIT_TEXT_SECOND = "s";
   public static final String TEXT_RGBCOLOR = "rgb";
   public static final String TEXT_RECT_FUNCTION = "rect";
   public static final String TEXT_COUNTER_FUNCTION = "counter";
   public static final String TEXT_COUNTERS_FUNCTION = "counters";
   protected short lexicalUnitType;
   protected LexicalUnit nextLexicalUnit;
   protected LexicalUnit previousLexicalUnit;

   protected CSSLexicalUnit(short var1, LexicalUnit var2) {
      this.lexicalUnitType = var1;
      this.previousLexicalUnit = var2;
      if (var2 != null) {
         ((CSSLexicalUnit)var2).nextLexicalUnit = this;
      }

   }

   public short getLexicalUnitType() {
      return this.lexicalUnitType;
   }

   public LexicalUnit getNextLexicalUnit() {
      return this.nextLexicalUnit;
   }

   public void setNextLexicalUnit(LexicalUnit var1) {
      this.nextLexicalUnit = var1;
   }

   public LexicalUnit getPreviousLexicalUnit() {
      return this.previousLexicalUnit;
   }

   public void setPreviousLexicalUnit(LexicalUnit var1) {
      this.previousLexicalUnit = var1;
   }

   public int getIntegerValue() {
      throw new IllegalStateException();
   }

   public float getFloatValue() {
      throw new IllegalStateException();
   }

   public String getDimensionUnitText() {
      switch (this.lexicalUnitType) {
         case 14:
            return "";
         case 15:
            return "em";
         case 16:
            return "ex";
         case 17:
            return "px";
         case 18:
            return "in";
         case 19:
            return "cm";
         case 20:
            return "mm";
         case 21:
            return "pt";
         case 22:
            return "pc";
         case 23:
            return "%";
         case 24:
         case 25:
         case 26:
         case 27:
         default:
            throw new IllegalStateException("No Unit Text for type: " + this.lexicalUnitType);
         case 28:
            return "deg";
         case 29:
            return "grad";
         case 30:
            return "rad";
         case 31:
            return "ms";
         case 32:
            return "s";
         case 33:
            return "Hz";
         case 34:
            return "kHz";
      }
   }

   public String getFunctionName() {
      throw new IllegalStateException();
   }

   public LexicalUnit getParameters() {
      throw new IllegalStateException();
   }

   public String getStringValue() {
      throw new IllegalStateException();
   }

   public LexicalUnit getSubValues() {
      throw new IllegalStateException();
   }

   public static CSSLexicalUnit createSimple(short var0, LexicalUnit var1) {
      return new SimpleLexicalUnit(var0, var1);
   }

   public static CSSLexicalUnit createInteger(int var0, LexicalUnit var1) {
      return new IntegerLexicalUnit(var0, var1);
   }

   public static CSSLexicalUnit createFloat(short var0, float var1, LexicalUnit var2) {
      return new FloatLexicalUnit(var0, var1, var2);
   }

   public static CSSLexicalUnit createDimension(float var0, String var1, LexicalUnit var2) {
      return new DimensionLexicalUnit(var0, var1, var2);
   }

   public static CSSLexicalUnit createFunction(String var0, LexicalUnit var1, LexicalUnit var2) {
      return new FunctionLexicalUnit(var0, var1, var2);
   }

   public static CSSLexicalUnit createPredefinedFunction(short var0, LexicalUnit var1, LexicalUnit var2) {
      return new PredefinedFunctionLexicalUnit(var0, var1, var2);
   }

   public static CSSLexicalUnit createString(short var0, String var1, LexicalUnit var2) {
      return new StringLexicalUnit(var0, var1, var2);
   }

   protected static class StringLexicalUnit extends CSSLexicalUnit {
      protected String value;

      public StringLexicalUnit(short var1, String var2, LexicalUnit var3) {
         super(var1, var3);
         this.value = var2;
      }

      public String getStringValue() {
         return this.value;
      }
   }

   protected static class PredefinedFunctionLexicalUnit extends CSSLexicalUnit {
      protected LexicalUnit parameters;

      public PredefinedFunctionLexicalUnit(short var1, LexicalUnit var2, LexicalUnit var3) {
         super(var1, var3);
         this.parameters = var2;
      }

      public String getFunctionName() {
         switch (this.lexicalUnitType) {
            case 25:
               return "counter";
            case 26:
               return "counters";
            case 27:
               return "rgb";
            case 38:
               return "rect";
            default:
               return super.getFunctionName();
         }
      }

      public LexicalUnit getParameters() {
         return this.parameters;
      }
   }

   protected static class FunctionLexicalUnit extends CSSLexicalUnit {
      protected String name;
      protected LexicalUnit parameters;

      public FunctionLexicalUnit(String var1, LexicalUnit var2, LexicalUnit var3) {
         super((short)41, var3);
         this.name = var1;
         this.parameters = var2;
      }

      public String getFunctionName() {
         return this.name;
      }

      public LexicalUnit getParameters() {
         return this.parameters;
      }
   }

   protected static class DimensionLexicalUnit extends CSSLexicalUnit {
      protected float value;
      protected String dimension;

      public DimensionLexicalUnit(float var1, String var2, LexicalUnit var3) {
         super((short)42, var3);
         this.value = var1;
         this.dimension = var2;
      }

      public float getFloatValue() {
         return this.value;
      }

      public String getDimensionUnitText() {
         return this.dimension;
      }
   }

   protected static class FloatLexicalUnit extends CSSLexicalUnit {
      protected float value;

      public FloatLexicalUnit(short var1, float var2, LexicalUnit var3) {
         super(var1, var3);
         this.value = var2;
      }

      public float getFloatValue() {
         return this.value;
      }
   }

   protected static class IntegerLexicalUnit extends CSSLexicalUnit {
      protected int value;

      public IntegerLexicalUnit(int var1, LexicalUnit var2) {
         super((short)13, var2);
         this.value = var1;
      }

      public int getIntegerValue() {
         return this.value;
      }
   }

   protected static class SimpleLexicalUnit extends CSSLexicalUnit {
      public SimpleLexicalUnit(short var1, LexicalUnit var2) {
         super(var1, var2);
      }
   }
}
