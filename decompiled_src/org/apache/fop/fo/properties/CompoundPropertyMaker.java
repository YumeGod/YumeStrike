package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.CompoundDatatype;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CompoundPropertyMaker extends PropertyMaker {
   private PropertyMaker[] subproperties = new PropertyMaker[11];
   private PropertyMaker shorthandMaker = null;

   public CompoundPropertyMaker(int propId) {
      super(propId);
   }

   public void useGeneric(PropertyMaker generic) {
      super.useGeneric(generic);
      if (generic instanceof CompoundPropertyMaker) {
         CompoundPropertyMaker compoundGeneric = (CompoundPropertyMaker)generic;

         for(int i = 0; i < 11; ++i) {
            PropertyMaker submaker = compoundGeneric.subproperties[i];
            if (submaker != null) {
               this.addSubpropMaker((PropertyMaker)submaker.clone());
            }
         }
      }

   }

   public void addSubpropMaker(PropertyMaker subproperty) {
      subproperty.propId &= -512;
      subproperty.propId |= this.propId;
      this.subproperties[this.getSubpropIndex(subproperty.getPropId())] = subproperty;
      if (this.shorthandMaker == null && subproperty.setByShorthand) {
         this.shorthandMaker = subproperty;
      }

   }

   public PropertyMaker getSubpropMaker(int subpropertyId) {
      return this.subproperties[this.getSubpropIndex(subpropertyId)];
   }

   private int getSubpropIndex(int subpropertyId) {
      return ((subpropertyId & -512) >> 9) - 1;
   }

   protected Property checkEnumValues(String value) {
      Property result = null;
      if (this.shorthandMaker != null) {
         result = this.shorthandMaker.checkEnumValues(value);
      }

      if (result == null) {
         result = super.checkEnumValues(value);
      }

      return result;
   }

   public Property get(int subpropertyId, PropertyList propertyList, boolean tryInherit, boolean tryDefault) throws PropertyException {
      Property p = super.get(subpropertyId, propertyList, tryInherit, tryDefault);
      if (subpropertyId != 0 && p != null) {
         p = this.getSubprop(p, subpropertyId);
      }

      return p;
   }

   protected Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      p = this.shorthandMaker.convertProperty(p, propertyList, fo);
      if (p != null) {
         Property prop = this.makeCompound(propertyList, fo);
         CompoundDatatype pval = (CompoundDatatype)prop.getObject();

         for(int i = 0; i < 11; ++i) {
            PropertyMaker submaker = this.subproperties[i];
            if (submaker != null && submaker.setByShorthand) {
               pval.setComponent(submaker.getPropId() & -512, p, false);
            }
         }

         return prop;
      } else {
         return null;
      }
   }

   public Property make(PropertyList propertyList) throws PropertyException {
      return this.defaultValue != null ? this.make(propertyList, this.defaultValue, propertyList.getParentFObj()) : this.makeCompound(propertyList, propertyList.getParentFObj());
   }

   public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
      Property p = super.make(propertyList, value, fo);
      p = this.convertProperty(p, propertyList, fo);
      return p;
   }

   public Property make(Property baseProperty, int subpropertyId, PropertyList propertyList, String value, FObj fo) throws PropertyException {
      if (baseProperty == null) {
         baseProperty = this.makeCompound(propertyList, fo);
      }

      PropertyMaker spMaker = this.getSubpropMaker(subpropertyId);
      if (spMaker != null) {
         Property p = spMaker.make(propertyList, value, fo);
         if (p != null) {
            return this.setSubprop(baseProperty, subpropertyId & -512, p);
         }
      }

      return baseProperty;
   }

   protected Property makeCompound(PropertyList propertyList, FObj parentFO) throws PropertyException {
      Property p = this.makeNewProperty();
      CompoundDatatype data = (CompoundDatatype)p.getObject();

      for(int i = 0; i < 11; ++i) {
         PropertyMaker subpropertyMaker = this.subproperties[i];
         if (subpropertyMaker != null) {
            Property subproperty = subpropertyMaker.make(propertyList);
            data.setComponent(subpropertyMaker.getPropId() & -512, subproperty, true);
         }
      }

      return p;
   }
}
