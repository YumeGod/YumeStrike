package org.apache.fop.util.text;

import org.xml.sax.Locator;

public class LocatorFormatter implements AdvancedMessageFormat.ObjectFormatter {
   public void format(StringBuffer sb, Object obj) {
      Locator loc = (Locator)obj;
      sb.append(loc.getLineNumber()).append(":").append(loc.getColumnNumber());
   }

   public boolean supportsObject(Object obj) {
      return obj instanceof Locator;
   }
}
