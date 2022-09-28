package org.apache.fop.datatypes;

import org.apache.fop.fo.Constants;
import org.apache.fop.fo.properties.Property;

public interface CompoundDatatype extends Constants {
   void setComponent(int var1, Property var2, boolean var3);

   Property getComponent(int var1);
}
