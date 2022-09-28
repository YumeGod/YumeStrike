package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.CompoundDatatype;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public final class KeepProperty extends Property implements CompoundDatatype {
   private static final PropertyCache cache;
   private boolean isCachedValue = false;
   private Property withinLine;
   private Property withinColumn;
   private Property withinPage;

   public void setComponent(int cmpId, Property cmpnValue, boolean bIsDefault) {
      if (this.isCachedValue) {
         log.warn("KeepProperty.setComponent() called on cached value. Ignoring...");
      } else {
         if (cmpId == 5120) {
            this.setWithinLine(cmpnValue, bIsDefault);
         } else if (cmpId == 4608) {
            this.setWithinColumn(cmpnValue, bIsDefault);
         } else if (cmpId == 5632) {
            this.setWithinPage(cmpnValue, bIsDefault);
         }

      }
   }

   public Property getComponent(int cmpId) {
      if (cmpId == 5120) {
         return this.getWithinLine();
      } else if (cmpId == 4608) {
         return this.getWithinColumn();
      } else {
         return cmpId == 5632 ? this.getWithinPage() : null;
      }
   }

   public void setWithinLine(Property withinLine, boolean bIsDefault) {
      this.withinLine = withinLine;
   }

   protected void setWithinColumn(Property withinColumn, boolean bIsDefault) {
      this.withinColumn = withinColumn;
   }

   public void setWithinPage(Property withinPage, boolean bIsDefault) {
      this.withinPage = withinPage;
   }

   public Property getWithinLine() {
      return this.withinLine;
   }

   public Property getWithinColumn() {
      return this.withinColumn;
   }

   public Property getWithinPage() {
      return this.withinPage;
   }

   public String toString() {
      return "Keep[withinLine:" + this.getWithinLine().getObject() + ", withinColumn:" + this.getWithinColumn().getObject() + ", withinPage:" + this.getWithinPage().getObject() + "]";
   }

   public KeepProperty getKeep() {
      KeepProperty keep = (KeepProperty)cache.fetch((Property)this);
      keep.isCachedValue = true;
      return keep;
   }

   public Object getObject() {
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof KeepProperty)) {
         return false;
      } else {
         KeepProperty keep = (KeepProperty)o;
         return keep.withinColumn == this.withinColumn && keep.withinLine == this.withinLine && keep.withinPage == this.withinPage;
      }
   }

   public int hashCode() {
      int hash = 17;
      hash = 37 * hash + (this.withinColumn == null ? 0 : this.withinColumn.hashCode());
      hash = 37 * hash + (this.withinLine == null ? 0 : this.withinLine.hashCode());
      hash = 37 * hash + (this.withinPage == null ? 0 : this.withinPage.hashCode());
      return hash;
   }

   static {
      cache = new PropertyCache(KeepProperty.class);
   }

   public static class Maker extends CompoundPropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property makeNewProperty() {
         return new KeepProperty();
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         return p instanceof KeepProperty ? p : super.convertProperty(p, propertyList, fo);
      }
   }
}
