package org.apache.fop.fo.properties;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.datatypes.CompoundDatatype;
import org.apache.fop.datatypes.LengthBase;
import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.fo.FOPropertyMapping;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.expr.PropertyInfo;
import org.apache.fop.fo.expr.PropertyParser;

public class PropertyMaker implements Cloneable {
   private static Log log;
   protected int propId;
   private boolean inherited = true;
   private Map enums = null;
   private Map keywords = null;
   protected String defaultValue = null;
   protected boolean contextDep = false;
   protected boolean setByShorthand = false;
   private int percentBase = -1;
   private PropertyMaker[] shorthands = null;
   private ShorthandParser datatypeParser;
   protected Property defaultProperty;
   protected CorrespondingPropertyMaker corresponding;

   public int getPropId() {
      return this.propId;
   }

   public PropertyMaker(int propId) {
      this.propId = propId;
   }

   public void useGeneric(PropertyMaker generic) {
      this.contextDep = generic.contextDep;
      this.inherited = generic.inherited;
      this.defaultValue = generic.defaultValue;
      this.percentBase = generic.percentBase;
      if (generic.shorthands != null) {
         this.shorthands = new PropertyMaker[generic.shorthands.length];
         System.arraycopy(generic.shorthands, 0, this.shorthands, 0, this.shorthands.length);
      }

      if (generic.enums != null) {
         this.enums = new HashMap(generic.enums);
      }

      if (generic.keywords != null) {
         this.keywords = new HashMap(generic.keywords);
      }

   }

   public void setInherited(boolean inherited) {
      this.inherited = inherited;
   }

   public void addKeyword(String keyword, String value) {
      if (this.keywords == null) {
         this.keywords = new HashMap();
      }

      this.keywords.put(keyword, value);
   }

   public void addEnum(String constant, Property value) {
      if (this.enums == null) {
         this.enums = new HashMap();
      }

      this.enums.put(constant, value);
   }

   public void addSubpropMaker(PropertyMaker subproperty) {
      throw new RuntimeException("Unable to add subproperties " + this.getClass());
   }

   public PropertyMaker getSubpropMaker(int subpropertyId) {
      throw new RuntimeException("Unable to add subproperties");
   }

   public void addShorthand(PropertyMaker shorthand) {
      if (this.shorthands == null) {
         this.shorthands = new PropertyMaker[3];
      }

      for(int i = 0; i < this.shorthands.length; ++i) {
         if (this.shorthands[i] == null) {
            this.shorthands[i] = shorthand;
            break;
         }
      }

   }

   public void setDatatypeParser(ShorthandParser parser) {
      this.datatypeParser = parser;
   }

   public void setDefault(String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public void setDefault(String defaultValue, boolean contextDep) {
      this.defaultValue = defaultValue;
      this.contextDep = contextDep;
   }

   public void setPercentBase(int percentBase) {
      this.percentBase = percentBase;
   }

   public void setByShorthand(boolean setByShorthand) {
      this.setByShorthand = setByShorthand;
   }

   public void setCorresponding(CorrespondingPropertyMaker corresponding) {
      this.corresponding = corresponding;
   }

   public Property makeNewProperty() {
      return null;
   }

   public Property findProperty(PropertyList propertyList, boolean tryInherit) throws PropertyException {
      Property p = null;
      if (log.isTraceEnabled()) {
         log.trace("PropertyMaker.findProperty: " + FOPropertyMapping.getPropertyName(this.propId) + ", " + propertyList.getFObj().getName());
      }

      if (this.corresponding != null && this.corresponding.isCorrespondingForced(propertyList)) {
         p = this.corresponding.compute(propertyList);
      } else {
         p = propertyList.getExplicit(this.propId);
         if (p == null) {
            p = this.getShorthand(propertyList);
         }

         if (p == null) {
            p = this.compute(propertyList);
         }
      }

      if (p == null && tryInherit) {
         PropertyList parentPropertyList = propertyList.getParentPropertyList();
         if (parentPropertyList != null && this.isInherited()) {
            p = parentPropertyList.get(this.propId, true, false);
         }
      }

      return p;
   }

   public Property get(int subpropertyId, PropertyList propertyList, boolean tryInherit, boolean tryDefault) throws PropertyException {
      Property p = this.findProperty(propertyList, tryInherit);
      if (p == null && tryDefault) {
         p = this.make(propertyList);
      }

      return p;
   }

   public boolean isInherited() {
      return this.inherited;
   }

   public PercentBase getPercentBase(PropertyList pl) throws PropertyException {
      return this.percentBase == -1 ? null : new LengthBase(pl, this.percentBase);
   }

   public Property getSubprop(Property p, int subpropertyId) {
      CompoundDatatype val = (CompoundDatatype)p.getObject();
      return val.getComponent(subpropertyId);
   }

   protected Property setSubprop(Property baseProperty, int subpropertyId, Property subproperty) {
      CompoundDatatype val = (CompoundDatatype)baseProperty.getObject();
      val.setComponent(subpropertyId, subproperty, false);
      return baseProperty;
   }

   public Property make(PropertyList propertyList) throws PropertyException {
      if (this.defaultProperty != null) {
         if (log.isTraceEnabled()) {
            log.trace("PropertyMaker.make: reusing defaultProperty, " + FOPropertyMapping.getPropertyName(this.propId));
         }

         return this.defaultProperty;
      } else {
         if (log.isTraceEnabled()) {
            log.trace("PropertyMaker.make: making default property value, " + FOPropertyMapping.getPropertyName(this.propId) + ", " + propertyList.getFObj().getName());
         }

         Property p = this.make(propertyList, this.defaultValue, propertyList.getParentFObj());
         if (!this.contextDep) {
            this.defaultProperty = p;
         }

         return p;
      }
   }

   public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
      try {
         Property newProp = null;
         String pvalue = value;
         if ("inherit".equals(value)) {
            newProp = propertyList.getFromParent(this.propId & 511);
            if ((this.propId & -512) != 0) {
               newProp = this.getSubprop(newProp, this.propId & -512);
            }

            if (!this.isInherited() && log.isWarnEnabled()) {
               Property parentExplicit = propertyList.getParentPropertyList().getExplicit(this.getPropId());
               if (parentExplicit == null) {
                  log.warn(FOPropertyMapping.getPropertyName(this.getPropId()) + "=\"inherit\" on " + propertyList.getFObj().getName() + ", but no explicit value found on the parent FO.");
               }
            }
         } else {
            pvalue = this.checkValueKeywords(value.trim());
            newProp = this.checkEnumValues(pvalue);
         }

         if (newProp == null) {
            newProp = PropertyParser.parse(pvalue, new PropertyInfo(this, propertyList));
         }

         if (newProp != null) {
            newProp = this.convertProperty(newProp, propertyList, fo);
         }

         if (newProp == null) {
            throw new PropertyException("No conversion defined " + pvalue);
         } else {
            return newProp;
         }
      } catch (PropertyException var7) {
         var7.setLocator(fo.getLocator());
         var7.setPropertyName(this.getName());
         throw var7;
      }
   }

   public Property make(Property baseProperty, int subpropertyId, PropertyList propertyList, String value, FObj fo) throws PropertyException {
      return baseProperty;
   }

   public Property convertShorthandProperty(PropertyList propertyList, Property prop, FObj fo) throws PropertyException {
      Property pret = this.convertProperty(prop, propertyList, fo);
      if (pret == null) {
         String sval = prop.getNCname();
         if (sval != null) {
            pret = this.checkEnumValues(sval);
            if (pret == null) {
               String pvalue = this.checkValueKeywords(sval);
               if (!pvalue.equals(sval)) {
                  Property p = PropertyParser.parse(pvalue, new PropertyInfo(this, propertyList));
                  pret = this.convertProperty(p, propertyList, fo);
               }
            }
         }
      }

      if (pret != null) {
      }

      return pret;
   }

   protected Property checkEnumValues(String value) {
      if (this.enums != null) {
         Property p = (Property)this.enums.get(value);
         return p;
      } else {
         return null;
      }
   }

   protected String checkValueKeywords(String keyword) {
      if (this.keywords != null) {
         String value = (String)this.keywords.get(keyword);
         if (value != null) {
            return value;
         }
      }

      return keyword;
   }

   protected Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      return null;
   }

   protected Property convertPropertyDatatype(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      return null;
   }

   protected Property compute(PropertyList propertyList) throws PropertyException {
      return this.corresponding != null ? this.corresponding.compute(propertyList) : null;
   }

   public Property getShorthand(PropertyList propertyList) throws PropertyException {
      if (this.shorthands == null) {
         return null;
      } else {
         int n = this.shorthands.length;

         for(int i = 0; i < n && this.shorthands[i] != null; ++i) {
            PropertyMaker shorthand = this.shorthands[i];
            Property prop = propertyList.getExplicit(shorthand.propId);
            if (prop != null) {
               ShorthandParser parser = shorthand.datatypeParser;
               Property p = parser.getValueForProperty(this.getPropId(), prop, this, propertyList);
               if (p != null) {
                  return p;
               }
            }
         }

         return null;
      }
   }

   public String getName() {
      return FOPropertyMapping.getPropertyName(this.propId);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   static {
      log = LogFactory.getLog(PropertyMaker.class);
   }
}
