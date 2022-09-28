package com.xmlmind.fo.properties.expression;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;

public class Expression {
   public static final int OPERATOR_NONE = 0;
   public static final int OPERATOR_NEGATION = 1;
   public static final int OPERATOR_ADDITION = 2;
   public static final int OPERATOR_SUBTRACTION = 3;
   public static final int OPERATOR_MULTIPLICATION = 4;
   public static final int OPERATOR_DIVISION = 5;
   public static final int OPERATOR_REMAINDER = 6;
   public int operator = 0;
   public Value operand1;
   public Value operand2;

   public static Value parse(String var0) {
      try {
         Expression var1 = Parser.parse(var0);
         return Value.expression(var1);
      } catch (ParseException var2) {
         return null;
      }
   }

   public Value evaluate(Property var1, Context var2) {
      Value var5 = this.operand1;
      Value var6 = this.operand2;
      Value var7 = null;
      Expression var8;
      if (var5 != null) {
         switch (var5.type) {
            case 23:
               var5 = Function.evaluate(var5.function(), var5.arguments(), var1, var2);
               break;
            case 28:
               var8 = var5.expression();
               var5 = var8.evaluate(var1, var2);
               break;
            default:
               var5 = var1.compute(var5, var2);
         }
      }

      if (var6 != null) {
         switch (var6.type) {
            case 23:
               var6 = Function.evaluate(var6.function(), var6.arguments(), var1, var2);
               break;
            case 28:
               var8 = var6.expression();
               var6 = var8.evaluate(var1, var2);
               break;
            default:
               var6 = var1.compute(var6, var2);
         }
      }

      if (var5 == null) {
         return null;
      } else {
         double var3;
         switch (this.operator) {
            case 0:
               var7 = var5;
               break;
            case 1:
               switch (var5.type) {
                  case 3:
                     var7 = Value.number(-var5.number());
                     return var7;
                  case 4:
                     var7 = Value.length(-var5.length(), 4);
                     return var7;
                  default:
                     return var7;
               }
            case 2:
               if (var6 != null && var6.type == var5.type) {
                  switch (var5.type) {
                     case 3:
                        var3 = var5.number() + var6.number();
                        var7 = Value.number(var3);
                        break;
                     case 4:
                        var3 = var5.length() + var6.length();
                        var7 = Value.length(var3, 4);
                  }
               }
               break;
            case 3:
               if (var6 != null && var6.type == var5.type) {
                  switch (var5.type) {
                     case 3:
                        var3 = var5.number() - var6.number();
                        var7 = Value.number(var3);
                        break;
                     case 4:
                        var3 = var5.length() - var6.length();
                        var7 = Value.length(var3, 4);
                  }
               }
               break;
            case 4:
               if (var6 != null) {
                  switch (var5.type) {
                     case 3:
                        switch (var6.type) {
                           case 3:
                              var3 = var5.number() * var6.number();
                              var7 = Value.number(var3);
                              return var7;
                           case 4:
                              var3 = var5.number() * var6.length();
                              var7 = Value.length(var3, 4);
                              return var7;
                           default:
                              return var7;
                        }
                     case 4:
                        if (var6.type == 3) {
                           var3 = var5.length() * var6.number();
                           var7 = Value.length(var3, 4);
                        }
                  }
               }
               break;
            case 5:
               if (var6 != null && var6.type == 3) {
                  switch (var5.type) {
                     case 3:
                        var3 = var5.number() / var6.number();
                        var7 = Value.number(var3);
                        break;
                     case 4:
                        var3 = var5.length() / var6.number();
                        var7 = Value.length(var3, 4);
                  }
               }
               break;
            case 6:
               if (var6 != null && var6.type == 3) {
                  switch (var5.type) {
                     case 3:
                        var3 = var5.number() % var6.number();
                        var7 = Value.number(var3);
                        break;
                     case 4:
                        var3 = var5.length() % var6.number();
                        var7 = Value.length(var3, 4);
                  }
               }
         }

         return var7;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.operand1 == null) {
         return "?";
      } else {
         var1.append("( ");
         if (this.operator == 0) {
            var1.append(this.operand1.toString());
         } else if (this.operator == 1) {
            var1.append("- " + this.operand1.toString());
         } else {
            if (this.operand2 == null) {
               return "?";
            }

            var1.append(this.operand1.toString());
            switch (this.operator) {
               case 2:
                  var1.append(" + ");
                  break;
               case 3:
                  var1.append(" - ");
                  break;
               case 4:
                  var1.append(" * ");
                  break;
               case 5:
                  var1.append(" / ");
                  break;
               case 6:
                  var1.append(" % ");
                  break;
               default:
                  return "?";
            }

            var1.append(this.operand2.toString());
         }

         var1.append(" )");
         return var1.toString();
      }
   }

   public static void main(String[] var0) {
      Expression var2 = null;
      if (var0.length != 2) {
         String var5 = "com.xmlmind.fo.properties.expression.Expression";
         System.err.println("usage: java " + var5 + "<property> <expression>");
         System.exit(1);
      }

      Property var1 = Property.property(var0[0]);
      if (var1 == null) {
         System.out.println("Error: bad property name '" + var0[0] + "'");
         System.exit(2);
      }

      try {
         var2 = Parser.parse(var0[1]);
      } catch (ParseException var6) {
         System.out.println("Error: " + var6.toString());
         System.exit(2);
      }

      Context var3 = new Context();
      var3 = var3.startElement(2, new PropertyValues());
      var3.properties.set(106, Value.length(12.0, 4));
      var3.update();
      Value var4 = var2.evaluate(var1, var3);
      if (var4 == null) {
         System.out.println("Error: cannot evaluate '" + var0[1] + "'");
         System.exit(2);
      }

      System.out.println(var4.toString());
      System.exit(0);
   }
}
