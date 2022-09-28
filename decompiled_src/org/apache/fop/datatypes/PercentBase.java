package org.apache.fop.datatypes;

import org.apache.fop.fo.expr.PropertyException;

public interface PercentBase {
   int getDimension();

   double getBaseValue();

   int getBaseLength(PercentBaseContext var1) throws PropertyException;
}
