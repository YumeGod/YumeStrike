package org.apache.fop.area;

import java.util.List;

public interface Resolvable {
   boolean isResolved();

   String[] getIDRefs();

   void resolveIDRef(String var1, List var2);
}
