package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.NumericOp;
import org.apache.fop.fo.expr.PropertyException;

public class IndentPropertyMaker extends CorrespondingPropertyMaker {
   private int[] paddingCorresponding = null;
   private int[] borderWidthCorresponding = null;

   public IndentPropertyMaker(PropertyMaker baseMaker) {
      super(baseMaker);
   }

   public void setPaddingCorresponding(int[] paddingCorresponding) {
      this.paddingCorresponding = paddingCorresponding;
   }

   public void setBorderWidthCorresponding(int[] borderWidthCorresponding) {
      this.borderWidthCorresponding = borderWidthCorresponding;
   }

   public Property compute(PropertyList propertyList) throws PropertyException {
      return propertyList.getFObj().getUserAgent().isBreakIndentInheritanceOnReferenceAreaBoundary() ? this.computeAlternativeRuleset(propertyList) : this.computeConforming(propertyList);
   }

   public Property computeConforming(PropertyList propertyList) throws PropertyException {
      PropertyList pList = this.getWMPropertyList(propertyList);
      if (pList == null) {
         return null;
      } else {
         Numeric padding = this.getCorresponding(this.paddingCorresponding, propertyList).getNumeric();
         Numeric border = this.getCorresponding(this.borderWidthCorresponding, propertyList).getNumeric();
         int marginProp = pList.getWritingMode(this.lr_tb, this.rl_tb, this.tb_rl);
         if (propertyList.getExplicitOrShorthand(marginProp) == null) {
            Property indent = propertyList.getExplicit(this.baseMaker.propId);
            return indent == null ? null : indent;
         } else {
            Numeric margin = propertyList.get(marginProp).getNumeric();
            Numeric v = FixedLength.ZERO_FIXED_LENGTH;
            if (!propertyList.getFObj().generatesReferenceAreas()) {
               v = NumericOp.addition((Numeric)v, propertyList.getInherited(this.baseMaker.propId).getNumeric());
            }

            Numeric v = NumericOp.addition((Numeric)v, margin);
            v = NumericOp.addition(v, padding);
            v = NumericOp.addition(v, border);
            return (Property)v;
         }
      }
   }

   private boolean isInherited(PropertyList pList) {
      if (!pList.getFObj().getUserAgent().isBreakIndentInheritanceOnReferenceAreaBoundary()) {
         return true;
      } else {
         FONode nd = pList.getFObj().getParent();
         return !(nd instanceof FObj) || !((FObj)nd).generatesReferenceAreas();
      }
   }

   public Property computeAlternativeRuleset(PropertyList propertyList) throws PropertyException {
      PropertyList pList = this.getWMPropertyList(propertyList);
      if (pList == null) {
         return null;
      } else {
         Numeric padding = this.getCorresponding(this.paddingCorresponding, propertyList).getNumeric();
         Numeric border = this.getCorresponding(this.borderWidthCorresponding, propertyList).getNumeric();
         int marginProp = pList.getWritingMode(this.lr_tb, this.rl_tb, this.tb_rl);
         boolean marginNearest = false;

         for(PropertyList pl = propertyList.getParentPropertyList(); pl != null && pl.getExplicit(this.baseMaker.propId) == null; pl = pl.getParentPropertyList()) {
            if (pl.getExplicitOrShorthand(marginProp) != null) {
               marginNearest = true;
               break;
            }
         }

         if (propertyList.getExplicitOrShorthand(marginProp) == null) {
            Property indent = propertyList.getExplicit(this.baseMaker.propId);
            if (indent == null) {
               return !this.isInherited(propertyList) && marginNearest ? FixedLength.ZERO_FIXED_LENGTH : null;
            } else {
               return indent;
            }
         } else {
            Numeric margin = propertyList.get(marginProp).getNumeric();
            Numeric v = FixedLength.ZERO_FIXED_LENGTH;
            if (this.isInherited(propertyList)) {
               v = NumericOp.addition((Numeric)v, propertyList.getInherited(this.baseMaker.propId).getNumeric());
            }

            Numeric v = NumericOp.addition((Numeric)v, margin);
            v = NumericOp.addition(v, padding);
            v = NumericOp.addition(v, border);
            return (Property)v;
         }
      }
   }

   private Property getCorresponding(int[] corresponding, PropertyList propertyList) throws PropertyException {
      PropertyList pList = this.getWMPropertyList(propertyList);
      if (pList != null) {
         int wmcorr = pList.getWritingMode(corresponding[0], corresponding[1], corresponding[2]);
         return propertyList.get(wmcorr);
      } else {
         return null;
      }
   }
}
