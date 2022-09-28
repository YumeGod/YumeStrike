package org.apache.fop.fo.expr;

import org.apache.fop.apps.FOPException;

public class PropertyException extends FOPException {
   private String propertyName;

   public PropertyException(String detail) {
      super(detail);
   }

   public PropertyException(Exception cause) {
      super(cause);
      if (cause instanceof PropertyException) {
         this.propertyName = ((PropertyException)cause).propertyName;
      }

   }

   public void setPropertyInfo(PropertyInfo propInfo) {
      this.setLocator(propInfo.getPropertyList().getFObj().getLocator());
      this.propertyName = propInfo.getPropertyMaker().getName();
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   public String getMessage() {
      return this.propertyName != null ? super.getMessage() + "; property:'" + this.propertyName + "'" : super.getMessage();
   }
}
