package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.xs.ShortList;

public interface ValueStore {
   void addValue(Field var1, Object var2, short var3, ShortList var4);

   void reportError(String var1, Object[] var2);
}
