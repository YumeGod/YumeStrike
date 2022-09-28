package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class CommonAccessibility {
   public String sourceDoc = null;
   public String role = null;

   public CommonAccessibility(PropertyList pList) throws PropertyException {
      this.sourceDoc = pList.get(221).getString();
      if ("none".equals(this.sourceDoc)) {
         this.sourceDoc = null;
      }

      this.role = pList.get(212).getString();
      if ("none".equals(this.role)) {
         this.role = null;
      }

   }
}
