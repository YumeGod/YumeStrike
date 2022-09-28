package com.xmlmind.fo.properties;

import com.xmlmind.fo.properties.expression.Expression;
import com.xmlmind.fo.properties.expression.Function;
import java.text.NumberFormat;
import java.util.Locale;

public final class Value {
   public static final byte TYPE_KEYWORD = 1;
   public static final byte TYPE_INTEGER = 2;
   public static final byte TYPE_NUMBER = 3;
   public static final byte TYPE_LENGTH = 4;
   public static final byte TYPE_LENGTH_RANGE = 5;
   public static final byte TYPE_LENGTH_CONDITIONAL = 6;
   public static final byte TYPE_LENGTH_BP_IP_DIRECTION = 7;
   public static final byte TYPE_KEEP = 8;
   public static final byte TYPE_SPACE = 9;
   public static final byte TYPE_ANGLE = 10;
   public static final byte TYPE_TIME = 11;
   public static final byte TYPE_FREQUENCY = 12;
   public static final byte TYPE_PERCENTAGE = 13;
   public static final byte TYPE_CHARACTER = 14;
   public static final byte TYPE_STRING = 15;
   public static final byte TYPE_NAME = 16;
   public static final byte TYPE_ID = 17;
   public static final byte TYPE_IDREF = 18;
   public static final byte TYPE_COUNTRY = 19;
   public static final byte TYPE_LANGUAGE = 20;
   public static final byte TYPE_SCRIPT = 21;
   public static final byte TYPE_URI_SPECIFICATION = 22;
   public static final byte TYPE_FUNCTION = 23;
   public static final byte TYPE_COLOR = 24;
   public static final byte TYPE_SHADOW = 25;
   public static final byte TYPE_SHAPE = 26;
   public static final byte TYPE_LIST = 27;
   public static final byte TYPE_EXPRESSION = 28;
   public static final byte TYPE_PROPORTIONAL_COLUMN_WIDTH = 29;
   public static final byte TYPE_LABEL_FORMAT = 30;
   public static final Value INTEGER_ZERO = new Value((byte)2, 0);
   public static final Value KEYWORD_ALWAYS = new Value((byte)1, 8);
   public static final Value KEYWORD_AUTO = new Value((byte)1, 10);
   public static final Value KEYWORD_DISCARD = new Value((byte)1, 44);
   public static final Value KEYWORD_FALSE = new Value((byte)1, 61);
   public static final Value KEYWORD_FORCE = new Value((byte)1, 72);
   public static final Value KEYWORD_MEDIUM = new Value((byte)1, 118);
   public static final Value KEYWORD_NONE = new Value((byte)1, 125);
   public static final Value KEYWORD_NORMAL = new Value((byte)1, 127);
   public static final Value KEYWORD_RETAIN = new Value((byte)1, 163);
   public static final Value KEYWORD_STATIC = new Value((byte)1, 191);
   public static final Value KEYWORD_TRUE = new Value((byte)1, 209);
   public static final Value LENGTH_ZERO = new Value((byte)4, 4, 0.0);
   public static final Value NUMBER_ONE = new Value((byte)3, 1.0);
   public static final Value KEEP_AUTO = new Value((byte)8, (Object)null);
   public static final Value SPACE_ZERO;
   public byte type;
   public int intValue;
   public double doubleValue;
   public Object objectValue;

   public Value(byte var1, int var2) {
      this.type = var1;
      this.intValue = var2;
   }

   public Value(byte var1, double var2) {
      this.type = var1;
      this.doubleValue = var2;
   }

   public Value(byte var1, Object var2) {
      this.type = var1;
      this.objectValue = var2;
   }

   public Value(byte var1, int var2, double var3) {
      this.type = var1;
      this.intValue = var2;
      this.doubleValue = var3;
   }

   public Value(byte var1, int var2, Object var3) {
      this.type = var1;
      this.intValue = var2;
      this.objectValue = var3;
   }

   public static Value keyword(int var0) {
      return new Value((byte)1, var0);
   }

   public static Value number(double var0) {
      return new Value((byte)3, var0);
   }

   public static Value length(double var0, int var2) {
      return new Value((byte)4, var2, var0);
   }

   public static Value percentage(double var0) {
      return new Value((byte)13, var0);
   }

   public static Value color(Color var0) {
      return new Value((byte)24, var0);
   }

   public static Value name(String var0) {
      return new Value((byte)16, var0);
   }

   public static Value function(int var0, Value[] var1) {
      return new Value((byte)23, var0, var1);
   }

   public static Value expression(Expression var0) {
      return new Value((byte)28, var0);
   }

   public static Value proportionalColumnWidth(double var0) {
      return new Value((byte)29, var0);
   }

   public static Value labelFormat(LabelFormat var0) {
      return new Value((byte)30, var0);
   }

   public int keyword() {
      return this.intValue;
   }

   public int integer() {
      return this.intValue;
   }

   public double number() {
      return this.doubleValue;
   }

   public double length() {
      return this.doubleValue;
   }

   public int unit() {
      return this.intValue;
   }

   public Value[] lengthRange() {
      return (Value[])((Value[])this.objectValue);
   }

   public Value[] lengthConditional() {
      return (Value[])((Value[])this.objectValue);
   }

   public Value[] lengthBpIpDirection() {
      return (Value[])((Value[])this.objectValue);
   }

   public Value[] keep() {
      return (Value[])((Value[])this.objectValue);
   }

   public Value[] space() {
      return (Value[])((Value[])this.objectValue);
   }

   public double angle() {
      return this.doubleValue;
   }

   public double time() {
      return this.doubleValue;
   }

   public int frequency() {
      return this.intValue;
   }

   public double percentage() {
      return this.doubleValue;
   }

   public char character() {
      return (char)this.intValue;
   }

   public String string() {
      return (String)this.objectValue;
   }

   public String name() {
      return (String)this.objectValue;
   }

   public String id() {
      return (String)this.objectValue;
   }

   public String idref() {
      return (String)this.objectValue;
   }

   public String country() {
      return (String)this.objectValue;
   }

   public String language() {
      return (String)this.objectValue;
   }

   public String script() {
      return (String)this.objectValue;
   }

   public String uriSpecification() {
      return (String)this.objectValue;
   }

   public int function() {
      return this.intValue;
   }

   public Value[] arguments() {
      return (Value[])((Value[])this.objectValue);
   }

   public Color color() {
      return (Color)this.objectValue;
   }

   public Shadow shadow() {
      return (Shadow)this.objectValue;
   }

   public Value[] shape() {
      return (Value[])((Value[])this.objectValue);
   }

   public Value[] list() {
      return (Value[])((Value[])this.objectValue);
   }

   public Expression expression() {
      return (Expression)this.objectValue;
   }

   public double proportionalColumnWidth() {
      return this.doubleValue;
   }

   public LabelFormat labelFormat() {
      return (LabelFormat)this.objectValue;
   }

   public String toString() {
      StringBuffer var1;
      Value[] var3;
      int var5;
      switch (this.type) {
         case 1:
            return Keyword.list[this.intValue].keyword;
         case 2:
         case 12:
            return String.valueOf(this.intValue);
         case 3:
            return format(this.doubleValue, 3);
         case 4:
            var1 = new StringBuffer();
            var1.append(format(this.doubleValue, 3));
            switch (this.intValue) {
               case 1:
                  var1.append("cm");
                  break;
               case 2:
                  var1.append("mm");
                  break;
               case 3:
                  var1.append("in");
                  break;
               case 4:
                  var1.append("pt");
                  break;
               case 5:
                  var1.append("pc");
                  break;
               case 6:
                  var1.append("px");
                  break;
               case 7:
                  var1.append("em");
                  break;
               case 8:
                  var1.append("ex");
            }

            return var1.toString();
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 26:
         case 27:
            var1 = new StringBuffer();
            var3 = (Value[])((Value[])this.objectValue);

            for(var5 = 0; var5 < var3.length; ++var5) {
               if (var5 > 0) {
                  var1.append(' ');
               }

               var1.append(var3[var5].toString());
            }

            return var1.toString();
         case 10:
            var1 = new StringBuffer();
            var1.append(format(this.doubleValue, 3));
            var1.append("deg");
            return var1.toString();
         case 11:
            var1 = new StringBuffer();
            var1.append(format(this.doubleValue, 3));
            var1.append("s");
            return var1.toString();
         case 13:
            var1 = new StringBuffer();
            var1.append(format(this.doubleValue, 3));
            var1.append("%");
            return var1.toString();
         case 14:
            return String.valueOf((char)this.intValue);
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
            String var2 = (String)this.objectValue;
            if (var2.indexOf(32) >= 0) {
               return "\"" + var2 + "\"";
            }

            return var2;
         case 23:
            var1 = new StringBuffer();
            var1.append(Function.name(this.intValue));
            var1.append('(');
            if (this.objectValue != null) {
               var3 = (Value[])((Value[])this.objectValue);
               var1.append(var3[0].toString());

               for(var5 = 1; var5 < var3.length; ++var5) {
                  var1.append(", " + var3[var5].toString());
               }
            }

            var1.append(')');
            return var1.toString();
         case 24:
            return this.color().toString();
         case 25:
            return this.shadow().toString();
         case 28:
            Expression var4 = (Expression)this.objectValue;
            return var4.toString();
         case 29:
         default:
            return "?";
         case 30:
            return this.labelFormat().toString();
      }
   }

   private static String format(double var0, int var2) {
      NumberFormat var3 = NumberFormat.getInstance(Locale.ENGLISH);
      var3.setMinimumFractionDigits(var2);
      var3.setMaximumFractionDigits(var2);
      return var3.format(var0);
   }

   static {
      KEEP_AUTO.objectValue = new Value[]{KEYWORD_AUTO, KEYWORD_AUTO, KEYWORD_AUTO};
      SPACE_ZERO = new Value((byte)9, (Object)null);
      SPACE_ZERO.objectValue = new Value[]{LENGTH_ZERO, LENGTH_ZERO, LENGTH_ZERO, KEYWORD_DISCARD, INTEGER_ZERO};
   }
}
