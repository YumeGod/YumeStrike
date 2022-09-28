package org.apache.fop.fonts;

import org.apache.avalon.framework.ValuedEnum;

public class CIDFontType extends ValuedEnum {
   public static final CIDFontType CIDTYPE0 = new CIDFontType("CIDFontType0", 0);
   public static final CIDFontType CIDTYPE2 = new CIDFontType("CIDFontType2", 1);

   protected CIDFontType(String name, int value) {
      super(name, value);
   }

   public static CIDFontType byName(String name) {
      if (name.equalsIgnoreCase(CIDTYPE0.getName())) {
         return CIDTYPE0;
      } else if (name.equalsIgnoreCase(CIDTYPE2.getName())) {
         return CIDTYPE2;
      } else {
         throw new IllegalArgumentException("Invalid CID font type: " + name);
      }
   }

   public static CIDFontType byValue(int value) {
      if (value == CIDTYPE0.getValue()) {
         return CIDTYPE0;
      } else if (value == CIDTYPE2.getValue()) {
         return CIDTYPE2;
      } else {
         throw new IllegalArgumentException("Invalid CID font type: " + value);
      }
   }
}
