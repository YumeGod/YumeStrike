package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CorrespondingPropertyMaker {
   protected PropertyMaker baseMaker;
   protected int lr_tb;
   protected int rl_tb;
   protected int tb_rl;
   protected boolean useParent;
   private boolean relative;

   public CorrespondingPropertyMaker(PropertyMaker baseMaker) {
      this.baseMaker = baseMaker;
      baseMaker.setCorresponding(this);
   }

   public void setCorresponding(int lr_tb, int rl_tb, int tb_rl) {
      this.lr_tb = lr_tb;
      this.rl_tb = rl_tb;
      this.tb_rl = tb_rl;
   }

   public void setUseParent(boolean useParent) {
      this.useParent = useParent;
   }

   public void setRelative(boolean relative) {
      this.relative = relative;
   }

   public boolean isCorrespondingForced(PropertyList propertyList) {
      if (!this.relative) {
         return false;
      } else {
         PropertyList pList = this.getWMPropertyList(propertyList);
         if (pList != null) {
            int correspondingId = pList.getWritingMode(this.lr_tb, this.rl_tb, this.tb_rl);
            if (pList.getExplicit(correspondingId) != null) {
               return true;
            }
         }

         return false;
      }
   }

   public Property compute(PropertyList propertyList) throws PropertyException {
      PropertyList pList = this.getWMPropertyList(propertyList);
      if (pList == null) {
         return null;
      } else {
         int correspondingId = pList.getWritingMode(this.lr_tb, this.rl_tb, this.tb_rl);
         Property p = propertyList.getExplicitOrShorthand(correspondingId);
         if (p != null) {
            FObj parentFO = propertyList.getParentFObj();
            p = this.baseMaker.convertProperty(p, propertyList, parentFO);
         }

         return p;
      }
   }

   protected PropertyList getWMPropertyList(PropertyList pList) {
      return this.useParent ? pList.getParentPropertyList() : pList;
   }
}
