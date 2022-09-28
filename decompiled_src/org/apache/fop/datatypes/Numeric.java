package org.apache.fop.datatypes;

import org.apache.fop.fo.expr.PropertyException;

public interface Numeric {
   double getNumericValue() throws PropertyException;

   double getNumericValue(PercentBaseContext var1) throws PropertyException;

   int getDimension();

   boolean isAbsolute();

   int getValue();

   int getValue(PercentBaseContext var1);

   int getEnum();
}
