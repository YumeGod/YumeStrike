package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public interface ShorthandParser {
   Property getValueForProperty(int var1, Property var2, PropertyMaker var3, PropertyList var4) throws PropertyException;
}
