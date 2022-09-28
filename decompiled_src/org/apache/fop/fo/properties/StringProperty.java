package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;

public final class StringProperty extends Property {
   private static final PropertyCache cache;
   public static final StringProperty EMPTY_STRING_PROPERTY;
   private final String str;

   private StringProperty(String str) {
      this.str = str;
   }

   public static StringProperty getInstance(String str) {
      return !"".equals(str) && str != null ? (StringProperty)cache.fetch((Property)(new StringProperty(str))) : EMPTY_STRING_PROPERTY;
   }

   public Object getObject() {
      return this.str;
   }

   public String getString() {
      return this.str;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StringProperty)) {
         return false;
      } else {
         StringProperty sp = (StringProperty)obj;
         return sp.str == this.str || sp.str.equals(this.str);
      }
   }

   public int hashCode() {
      return this.str.hashCode();
   }

   // $FF: synthetic method
   StringProperty(String x0, Object x1) {
      this(x0);
   }

   static {
      cache = new PropertyCache(StringProperty.class);
      EMPTY_STRING_PROPERTY = new StringProperty("");
   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property make(PropertyList propertyList, String value, FObj fo) {
         int vlen = value.length() - 1;
         if (vlen > 0) {
            char q1 = value.charAt(0);
            if (q1 == '"' || q1 == '\'') {
               if (value.charAt(vlen) == q1) {
                  return new StringProperty(value.substring(1, vlen));
               }

               Property.log.warn("String-valued property starts with quote but doesn't end with quote: " + value);
            }

            String str = this.checkValueKeywords(value);
            if (str != null) {
               value = str;
            }
         }

         return StringProperty.getInstance(value);
      }
   }
}
