package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class SpaceProperty extends LengthRangeProperty {
   private Property precedence;
   private Property conditionality;

   public void setComponent(int cmpId, Property cmpnValue, boolean bIsDefault) {
      if (cmpId == 4096) {
         this.setPrecedence(cmpnValue, bIsDefault);
      } else if (cmpId == 1024) {
         this.setConditionality(cmpnValue, bIsDefault);
      } else {
         super.setComponent(cmpId, cmpnValue, bIsDefault);
      }

   }

   public Property getComponent(int cmpId) {
      if (cmpId == 4096) {
         return this.getPrecedence();
      } else {
         return cmpId == 1024 ? this.getConditionality() : super.getComponent(cmpId);
      }
   }

   protected void setPrecedence(Property precedence, boolean bIsDefault) {
      this.precedence = precedence;
   }

   protected void setConditionality(Property conditionality, boolean bIsDefault) {
      this.conditionality = conditionality;
   }

   public Property getPrecedence() {
      return this.precedence;
   }

   public Property getConditionality() {
      return this.conditionality;
   }

   public boolean isDiscard() {
      return this.conditionality.getEnum() == 32;
   }

   public String toString() {
      return "Space[min:" + this.getMinimum((PercentBaseContext)null).getObject() + ", max:" + this.getMaximum((PercentBaseContext)null).getObject() + ", opt:" + this.getOptimum((PercentBaseContext)null).getObject() + ", precedence:" + this.precedence.getObject() + ", conditionality:" + this.conditionality.getObject() + "]";
   }

   public SpaceProperty getSpace() {
      return this;
   }

   public LengthRangeProperty getLengthRange() {
      return this;
   }

   public Object getObject() {
      return this;
   }

   public static class Maker extends CompoundPropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property makeNewProperty() {
         return new SpaceProperty();
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         return p instanceof SpaceProperty ? p : super.convertProperty(p, propertyList, fo);
      }
   }
}
