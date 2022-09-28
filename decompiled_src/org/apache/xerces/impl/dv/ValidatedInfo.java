package org.apache.xerces.impl.dv;

import org.apache.xerces.xs.ShortList;

public class ValidatedInfo {
   public String normalizedValue;
   public Object actualValue;
   public short actualValueType;
   public XSSimpleType memberType;
   public XSSimpleType[] memberTypes;
   public ShortList itemValueTypes;

   public void reset() {
      this.normalizedValue = null;
      this.actualValue = null;
      this.memberType = null;
      this.memberTypes = null;
   }

   public String stringValue() {
      return this.actualValue == null ? this.normalizedValue : this.actualValue.toString();
   }
}
