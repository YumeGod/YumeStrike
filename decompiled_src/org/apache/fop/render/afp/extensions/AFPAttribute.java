package org.apache.fop.render.afp.extensions;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.StringProperty;

public class AFPAttribute extends StringProperty.Maker {
   private Property property = null;

   protected AFPAttribute(String name) {
      super(0);
   }

   public Property make(PropertyList propertyList) {
      if (this.property == null) {
         this.property = this.make(propertyList, "", propertyList.getParentFObj());
      }

      return this.property;
   }
}
