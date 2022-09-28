package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public final class EnumProperty extends Property {
   private static final PropertyCache cache;
   private final int value;
   private final String text;

   private EnumProperty(int explicitValue, String text) {
      this.value = explicitValue;
      this.text = text;
   }

   public static EnumProperty getInstance(int explicitValue, String text) {
      return (EnumProperty)cache.fetch((Property)(new EnumProperty(explicitValue, text)));
   }

   public int getEnum() {
      return this.value;
   }

   public Object getObject() {
      return this.text;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof EnumProperty)) {
         return false;
      } else {
         EnumProperty ep = (EnumProperty)obj;
         return ep.value == this.value && (ep.text == this.text || ep.text != null && ep.text.equals(this.text));
      }
   }

   public int hashCode() {
      return this.value + this.text.hashCode();
   }

   static {
      cache = new PropertyCache(EnumProperty.class);
   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property checkEnumValues(String value) {
         return super.checkEnumValues(value);
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         return p instanceof EnumProperty ? p : super.convertProperty(p, propertyList, fo);
      }
   }
}
