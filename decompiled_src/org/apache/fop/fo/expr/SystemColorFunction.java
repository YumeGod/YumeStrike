package org.apache.fop.fo.expr;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.properties.ColorProperty;
import org.apache.fop.fo.properties.Property;

class SystemColorFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      FOUserAgent ua = pInfo == null ? null : (pInfo.getFO() == null ? null : pInfo.getFO().getUserAgent());
      return ColorProperty.getInstance(ua, "system-color(" + args[0] + ")");
   }
}
