package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.CompoundDatatype;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CondLengthProperty extends Property implements CompoundDatatype {
   private static final PropertyCache cache;
   private Property length;
   private EnumProperty conditionality;
   private boolean isCached = false;
   private int hash = -1;

   public void setComponent(int cmpId, Property cmpnValue, boolean bIsDefault) {
      if (this.isCached) {
         throw new IllegalStateException("CondLengthProperty.setComponent() called on a cached value!");
      } else {
         if (cmpId == 2048) {
            this.length = cmpnValue;
         } else if (cmpId == 1024) {
            this.conditionality = (EnumProperty)cmpnValue;
         }

      }
   }

   public Property getComponent(int cmpId) {
      if (cmpId == 2048) {
         return this.length;
      } else {
         return cmpId == 1024 ? this.conditionality : null;
      }
   }

   public Property getConditionality() {
      return this.conditionality;
   }

   public Property getLengthComponent() {
      return this.length;
   }

   public boolean isDiscard() {
      return this.conditionality.getEnum() == 32;
   }

   public int getLengthValue() {
      return this.length.getLength().getValue();
   }

   public int getLengthValue(PercentBaseContext context) {
      return this.length.getLength().getValue(context);
   }

   public String toString() {
      return "CondLength[" + this.length.getObject().toString() + ", " + (this.isDiscard() ? this.conditionality.toString().toLowerCase() : this.conditionality.toString()) + "]";
   }

   public CondLengthProperty getCondLength() {
      if (this.length.getLength().isAbsolute()) {
         CondLengthProperty clp = (CondLengthProperty)cache.fetch((Property)this);
         if (clp == this) {
            this.isCached = true;
         }

         return clp;
      } else {
         return this;
      }
   }

   public Length getLength() {
      return this.length.getLength();
   }

   public Object getObject() {
      return this;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof CondLengthProperty)) {
         return false;
      } else {
         CondLengthProperty clp = (CondLengthProperty)obj;
         return this.length == clp.length && this.conditionality == clp.conditionality;
      }
   }

   public int hashCode() {
      if (this.hash == -1) {
         int hash = 17;
         hash = 37 * hash + (this.length == null ? 0 : this.length.hashCode());
         hash = 37 * hash + (this.conditionality == null ? 0 : this.conditionality.hashCode());
         this.hash = hash;
      }

      return this.hash;
   }

   static {
      cache = new PropertyCache(CondLengthProperty.class);
   }

   public static class Maker extends CompoundPropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property makeNewProperty() {
         return new CondLengthProperty();
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         return p instanceof KeepProperty ? p : super.convertProperty(p, propertyList, fo);
      }
   }
}
