package org.apache.fop.fo.expr;

import java.awt.Color;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.util.ColorUtil;

public class NCnameProperty extends Property {
   private final String ncName;

   public NCnameProperty(String ncName) {
      this.ncName = ncName;
   }

   public Color getColor(FOUserAgent foUserAgent) {
      try {
         return ColorUtil.parseColorString(foUserAgent, this.ncName);
      } catch (PropertyException var3) {
         return null;
      }
   }

   public String getString() {
      return this.ncName;
   }

   public Object getObject() {
      return this.ncName;
   }

   public String getNCname() {
      return this.ncName;
   }
}
