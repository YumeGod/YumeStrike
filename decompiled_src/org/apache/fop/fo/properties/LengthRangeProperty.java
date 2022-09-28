package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.CompoundDatatype;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.traits.MinOptMax;

public class LengthRangeProperty extends Property implements CompoundDatatype {
   private Property minimum;
   private Property optimum;
   private Property maximum;
   private static final int MINSET = 1;
   private static final int OPTSET = 2;
   private static final int MAXSET = 4;
   private int bfSet = 0;
   private boolean consistent = false;

   public MinOptMax toMinOptMax(PercentBaseContext context) {
      int min = this.getMinimum(context).isAuto() ? 0 : this.getMinimum(context).getLength().getValue(context);
      int opt = this.getOptimum(context).isAuto() ? min : this.getOptimum(context).getLength().getValue(context);
      int max = this.getMaximum(context).isAuto() ? Integer.MAX_VALUE : this.getMaximum(context).getLength().getValue(context);
      return MinOptMax.getInstance(min, opt, max);
   }

   public void setComponent(int cmpId, Property cmpnValue, boolean bIsDefault) {
      if (cmpId == 3072) {
         this.setMinimum(cmpnValue, bIsDefault);
      } else if (cmpId == 3584) {
         this.setOptimum(cmpnValue, bIsDefault);
      } else if (cmpId == 2560) {
         this.setMaximum(cmpnValue, bIsDefault);
      }

   }

   public Property getComponent(int cmpId) {
      if (cmpId == 3072) {
         return this.getMinimum((PercentBaseContext)null);
      } else if (cmpId == 3584) {
         return this.getOptimum((PercentBaseContext)null);
      } else {
         return cmpId == 2560 ? this.getMaximum((PercentBaseContext)null) : null;
      }
   }

   protected void setMinimum(Property minimum, boolean bIsDefault) {
      this.minimum = minimum;
      if (!bIsDefault) {
         this.bfSet |= 1;
      }

      this.consistent = false;
   }

   protected void setMaximum(Property max, boolean bIsDefault) {
      this.maximum = max;
      if (!bIsDefault) {
         this.bfSet |= 4;
      }

      this.consistent = false;
   }

   protected void setOptimum(Property opt, boolean bIsDefault) {
      this.optimum = opt;
      if (!bIsDefault) {
         this.bfSet |= 2;
      }

      this.consistent = false;
   }

   private void checkConsistency(PercentBaseContext context) {
      if (!this.consistent) {
         if (context != null) {
            if (!this.minimum.isAuto() && !this.maximum.isAuto() && this.minimum.getLength().getValue(context) > this.maximum.getLength().getValue(context)) {
               if ((this.bfSet & 1) != 0) {
                  if ((this.bfSet & 4) != 0) {
                     log.error("forcing max to min in LengthRange");
                  }

                  this.maximum = this.minimum;
               } else {
                  this.minimum = this.maximum;
               }
            }

            if (!this.optimum.isAuto() && !this.maximum.isAuto() && this.optimum.getLength().getValue(context) > this.maximum.getLength().getValue(context)) {
               if ((this.bfSet & 2) != 0) {
                  if ((this.bfSet & 4) != 0) {
                     log.error("forcing opt to max in LengthRange");
                     this.optimum = this.maximum;
                  } else {
                     this.maximum = this.optimum;
                  }
               } else {
                  this.optimum = this.maximum;
               }
            } else if (!this.optimum.isAuto() && !this.minimum.isAuto() && this.optimum.getLength().getValue(context) < this.minimum.getLength().getValue(context)) {
               if ((this.bfSet & 1) != 0) {
                  if ((this.bfSet & 2) != 0) {
                     log.error("forcing opt to min in LengthRange");
                  }

                  this.optimum = this.minimum;
               } else {
                  this.minimum = this.optimum;
               }
            }

            this.consistent = true;
         }
      }
   }

   public Property getMinimum(PercentBaseContext context) {
      this.checkConsistency(context);
      return this.minimum;
   }

   public Property getMaximum(PercentBaseContext context) {
      this.checkConsistency(context);
      return this.maximum;
   }

   public Property getOptimum(PercentBaseContext context) {
      this.checkConsistency(context);
      return this.optimum;
   }

   public String toString() {
      return "LengthRange[min:" + this.getMinimum((PercentBaseContext)null).getObject() + ", max:" + this.getMaximum((PercentBaseContext)null).getObject() + ", opt:" + this.getOptimum((PercentBaseContext)null).getObject() + "]";
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
         return new LengthRangeProperty();
      }

      private boolean isNegativeLength(Length len) {
         return len instanceof PercentLength && ((PercentLength)len).getPercentage() < 0.0 || len.isAbsolute() && len.getValue() < 0;
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         if (p instanceof LengthRangeProperty) {
            return (Property)p;
         } else {
            if (this.propId == 17 || this.propId == 127) {
               Length len = ((Property)p).getLength();
               if (len != null && this.isNegativeLength(len)) {
                  Property.log.warn(FObj.decorateWithContextInfo("Replaced negative value (" + len + ") for " + this.getName() + " with 0mpt", fo));
                  p = FixedLength.ZERO_FIXED_LENGTH;
               }
            }

            return super.convertProperty((Property)p, propertyList, fo);
         }
      }

      protected Property setSubprop(Property baseProperty, int subpropertyId, Property subproperty) {
         CompoundDatatype val = (CompoundDatatype)baseProperty.getObject();
         if (this.propId == 17 || this.propId == 127) {
            Length len = subproperty.getLength();
            if (len != null && this.isNegativeLength(len)) {
               Property.log.warn("Replaced negative value (" + len + ") for " + this.getName() + " with 0mpt");
               val.setComponent(subpropertyId, FixedLength.ZERO_FIXED_LENGTH, false);
               return baseProperty;
            }
         }

         val.setComponent(subpropertyId, subproperty, false);
         return baseProperty;
      }
   }
}
