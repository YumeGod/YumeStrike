package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public abstract class LengthProperty extends Property implements Length, Numeric {
   public int getDimension() {
      return 1;
   }

   public Numeric getNumeric() {
      return this;
   }

   public Length getLength() {
      return this;
   }

   public Object getObject() {
      return this;
   }

   public static class Maker extends PropertyMaker {
      public Maker(int propId) {
         super(propId);
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         if (p instanceof EnumProperty) {
            return new EnumLength(p);
         } else if (p instanceof LengthProperty) {
            return p;
         } else if (p instanceof NumberProperty) {
            float resolution = propertyList.getFObj().getUserAgent().getSourceResolution();
            return FixedLength.getInstance(p.getNumeric().getNumericValue(), "px", 72.0F / resolution);
         } else {
            Length val = p.getLength();
            return val != null ? (Property)val : this.convertPropertyDatatype(p, propertyList, fo);
         }
      }
   }
}
