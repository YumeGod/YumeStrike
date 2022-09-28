package org.apache.fop.fo.expr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.LengthBase;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.fo.properties.ColorProperty;
import org.apache.fop.fo.properties.FixedLength;
import org.apache.fop.fo.properties.ListProperty;
import org.apache.fop.fo.properties.NumberProperty;
import org.apache.fop.fo.properties.PercentLength;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.StringProperty;

public final class PropertyParser extends PropertyTokenizer {
   private PropertyInfo propInfo;
   private static final String RELUNIT = "em";
   private static final HashMap FUNCTION_TABLE = new HashMap();

   public static Property parse(String expr, PropertyInfo propInfo) throws PropertyException {
      try {
         return (new PropertyParser(expr, propInfo)).parseProperty();
      } catch (PropertyException var3) {
         var3.setPropertyInfo(propInfo);
         throw var3;
      }
   }

   private PropertyParser(String propExpr, PropertyInfo pInfo) {
      super(propExpr);
      this.propInfo = pInfo;
   }

   private Property parseProperty() throws PropertyException {
      this.next();
      if (this.currentToken == 0) {
         return StringProperty.getInstance("");
      } else {
         ListProperty propList = null;

         while(true) {
            Property prop = this.parseAdditiveExpr();
            if (this.currentToken == 0) {
               if (propList != null) {
                  propList.addProperty(prop);
                  return propList;
               }

               return prop;
            }

            if (propList == null) {
               propList = new ListProperty(prop);
            } else {
               propList.addProperty(prop);
            }
         }
      }
   }

   private Property parseAdditiveExpr() throws PropertyException {
      Property prop = this.parseMultiplicativeExpr();

      while(true) {
         switch (this.currentToken) {
            case 8:
               this.next();
               prop = this.evalAddition(prop.getNumeric(), this.parseMultiplicativeExpr().getNumeric());
               break;
            case 9:
               this.next();
               prop = this.evalSubtraction(prop.getNumeric(), this.parseMultiplicativeExpr().getNumeric());
               break;
            default:
               return prop;
         }
      }
   }

   private Property parseMultiplicativeExpr() throws PropertyException {
      Property prop = this.parseUnaryExpr();

      while(true) {
         switch (this.currentToken) {
            case 2:
               this.next();
               prop = this.evalMultiply(prop.getNumeric(), this.parseUnaryExpr().getNumeric());
               break;
            case 10:
               this.next();
               prop = this.evalModulo(prop.getNumber(), this.parseUnaryExpr().getNumber());
               break;
            case 11:
               this.next();
               prop = this.evalDivide(prop.getNumeric(), this.parseUnaryExpr().getNumeric());
               break;
            default:
               return prop;
         }
      }
   }

   private Property parseUnaryExpr() throws PropertyException {
      if (this.currentToken == 9) {
         this.next();
         return this.evalNegate(this.parseUnaryExpr().getNumeric());
      } else {
         return this.parsePrimaryExpr();
      }
   }

   private void expectRpar() throws PropertyException {
      if (this.currentToken != 4) {
         throw new PropertyException("expected )");
      } else {
         this.next();
      }
   }

   private Property parsePrimaryExpr() throws PropertyException {
      if (this.currentToken == 13) {
         this.next();
      }

      Object var1;
      Property prop;
      switch (this.currentToken) {
         case 1:
            var1 = new NCnameProperty(this.currentTokenValue);
            break;
         case 2:
         case 4:
         case 6:
         case 8:
         case 9:
         case 10:
         case 11:
         case 13:
         default:
            throw new PropertyException("syntax error");
         case 3:
            this.next();
            prop = this.parseAdditiveExpr();
            this.expectRpar();
            return prop;
         case 5:
            var1 = StringProperty.getInstance(this.currentTokenValue);
            break;
         case 7:
            Function function = (Function)FUNCTION_TABLE.get(this.currentTokenValue);
            if (function == null) {
               throw new PropertyException("no such function: " + this.currentTokenValue);
            }

            this.next();
            this.propInfo.pushFunction(function);
            if (function.nbArgs() < 0) {
               prop = function.eval(this.parseVarArgs(function), this.propInfo);
            } else {
               prop = function.eval(this.parseArgs(function), this.propInfo);
            }

            this.propInfo.popFunction();
            return prop;
         case 12:
            int numLen = this.currentTokenValue.length() - this.currentUnitLength;
            String unitPart = this.currentTokenValue.substring(numLen);
            double numPart = Double.parseDouble(this.currentTokenValue.substring(0, numLen));
            if ("em".equals(unitPart)) {
               var1 = (Property)NumericOp.multiply(NumberProperty.getInstance(numPart), this.propInfo.currentFontSize());
            } else if ("px".equals(unitPart)) {
               float resolution = this.propInfo.getPropertyList().getFObj().getUserAgent().getSourceResolution();
               var1 = FixedLength.getInstance(numPart, unitPart, 72.0F / resolution);
            } else {
               var1 = FixedLength.getInstance(numPart, unitPart);
            }
            break;
         case 14:
            double pcval = Double.parseDouble(this.currentTokenValue.substring(0, this.currentTokenValue.length() - 1)) / 100.0;
            PercentBase pcBase = this.propInfo.getPercentBase();
            if (pcBase != null) {
               if (pcBase.getDimension() == 0) {
                  var1 = NumberProperty.getInstance(pcval * pcBase.getBaseValue());
               } else {
                  if (pcBase.getDimension() != 1) {
                     throw new PropertyException("Illegal percent dimension value");
                  }

                  if (pcBase instanceof LengthBase) {
                     if (pcval == 0.0) {
                        var1 = FixedLength.ZERO_FIXED_LENGTH;
                        break;
                     }

                     Length base = ((LengthBase)pcBase).getBaseLength();
                     if (base != null && base.isAbsolute()) {
                        var1 = FixedLength.getInstance(pcval * (double)base.getValue());
                        break;
                     }
                  }

                  var1 = new PercentLength(pcval, pcBase);
               }
            } else {
               var1 = NumberProperty.getInstance(pcval);
            }
            break;
         case 15:
            var1 = ColorProperty.getInstance(this.propInfo.getUserAgent(), this.currentTokenValue);
            break;
         case 16:
            var1 = NumberProperty.getInstance(new Double(this.currentTokenValue));
            break;
         case 17:
            var1 = NumberProperty.getInstance(new Integer(this.currentTokenValue));
      }

      this.next();
      return (Property)var1;
   }

   Property[] parseArgs(Function function) throws PropertyException {
      int nbArgs = function.nbArgs();
      Property[] args = new Property[nbArgs];
      int i = 0;
      if (this.currentToken == 4) {
         this.next();
      } else {
         while(true) {
            Property prop = this.parseAdditiveExpr();
            if (i < nbArgs) {
               args[i++] = prop;
            }

            if (this.currentToken != 13) {
               this.expectRpar();
               break;
            }

            this.next();
         }
      }

      if (i == nbArgs - 1 && function.padArgsWithPropertyName()) {
         args[i++] = StringProperty.getInstance(this.propInfo.getPropertyMaker().getName());
      }

      if (nbArgs != i) {
         throw new PropertyException("Expected " + nbArgs + ", but got " + i + " args for function");
      } else {
         return args;
      }
   }

   Property[] parseVarArgs(Function function) throws PropertyException {
      int nbArgs = -function.nbArgs();
      List args = new LinkedList();
      if (this.currentToken == 4) {
         this.next();
      } else {
         while(true) {
            Property prop = this.parseAdditiveExpr();
            args.add(prop);
            if (this.currentToken != 13) {
               this.expectRpar();
               break;
            }

            this.next();
         }
      }

      if (nbArgs > args.size()) {
         throw new PropertyException("Expected at least " + nbArgs + ", but got " + args.size() + " args for function");
      } else {
         Property[] propArray = new Property[args.size()];
         args.toArray(propArray);
         return propArray;
      }
   }

   private Property evalAddition(Numeric op1, Numeric op2) throws PropertyException {
      if (op1 != null && op2 != null) {
         return (Property)NumericOp.addition(op1, op2);
      } else {
         throw new PropertyException("Non numeric operand in addition");
      }
   }

   private Property evalSubtraction(Numeric op1, Numeric op2) throws PropertyException {
      if (op1 != null && op2 != null) {
         return (Property)NumericOp.subtraction(op1, op2);
      } else {
         throw new PropertyException("Non numeric operand in subtraction");
      }
   }

   private Property evalNegate(Numeric op) throws PropertyException {
      if (op == null) {
         throw new PropertyException("Non numeric operand to unary minus");
      } else {
         return (Property)NumericOp.negate(op);
      }
   }

   private Property evalMultiply(Numeric op1, Numeric op2) throws PropertyException {
      if (op1 != null && op2 != null) {
         return (Property)NumericOp.multiply(op1, op2);
      } else {
         throw new PropertyException("Non numeric operand in multiplication");
      }
   }

   private Property evalDivide(Numeric op1, Numeric op2) throws PropertyException {
      if (op1 != null && op2 != null) {
         return (Property)NumericOp.divide(op1, op2);
      } else {
         throw new PropertyException("Non numeric operand in division");
      }
   }

   private Property evalModulo(Number op1, Number op2) throws PropertyException {
      if (op1 != null && op2 != null) {
         return NumberProperty.getInstance(op1.doubleValue() % op2.doubleValue());
      } else {
         throw new PropertyException("Non number operand to modulo");
      }
   }

   static {
      FUNCTION_TABLE.put("ceiling", new CeilingFunction());
      FUNCTION_TABLE.put("floor", new FloorFunction());
      FUNCTION_TABLE.put("round", new RoundFunction());
      FUNCTION_TABLE.put("min", new MinFunction());
      FUNCTION_TABLE.put("max", new MaxFunction());
      FUNCTION_TABLE.put("abs", new AbsFunction());
      FUNCTION_TABLE.put("rgb", new RGBColorFunction());
      FUNCTION_TABLE.put("system-color", new SystemColorFunction());
      FUNCTION_TABLE.put("from-table-column", new FromTableColumnFunction());
      FUNCTION_TABLE.put("inherited-property-value", new InheritedPropFunction());
      FUNCTION_TABLE.put("from-parent", new FromParentFunction());
      FUNCTION_TABLE.put("from-nearest-specified-value", new NearestSpecPropFunction());
      FUNCTION_TABLE.put("proportional-column-width", new PPColWidthFunction());
      FUNCTION_TABLE.put("label-end", new LabelEndFunction());
      FUNCTION_TABLE.put("body-start", new BodyStartFunction());
      FUNCTION_TABLE.put("rgb-icc", new ICCColorFunction());
      FUNCTION_TABLE.put("cmyk", new CMYKcolorFunction());
   }
}
