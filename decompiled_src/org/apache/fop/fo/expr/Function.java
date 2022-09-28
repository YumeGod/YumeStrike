package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.fo.properties.Property;

public interface Function {
   int nbArgs();

   PercentBase getPercentBase();

   Property eval(Property[] var1, PropertyInfo var2) throws PropertyException;

   boolean padArgsWithPropertyName();
}
