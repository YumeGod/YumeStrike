package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class TableBorderPrecedence extends NumberProperty.Maker {
   private static Property num0 = NumberProperty.getInstance(0);
   private static Property num1 = NumberProperty.getInstance(1);
   private static Property num2 = NumberProperty.getInstance(2);
   private static Property num3 = NumberProperty.getInstance(3);
   private static Property num4 = NumberProperty.getInstance(4);
   private static Property num5 = NumberProperty.getInstance(5);
   private static Property num6 = NumberProperty.getInstance(6);

   public TableBorderPrecedence(int propId) {
      super(propId);
   }

   public Property make(PropertyList propertyList) throws PropertyException {
      FObj fo = propertyList.getFObj();
      switch (fo.getNameId()) {
         case 71:
            return num6;
         case 72:
         case 74:
         default:
            return null;
         case 73:
            return num2;
         case 75:
            return num5;
         case 76:
            return num4;
         case 77:
            return num0;
         case 78:
            return num1;
         case 79:
            return num3;
      }
   }
}
