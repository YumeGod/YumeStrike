package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XML11Char;

public class XML11IDREFDatatypeValidator extends IDREFDatatypeValidator {
   public void validate(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      if (var2.useNamespaces()) {
         if (!XML11Char.isXML11ValidNCName(var1)) {
            throw new InvalidDatatypeValueException("IDREFInvalidWithNamespaces", new Object[]{var1});
         }
      } else if (!XML11Char.isXML11ValidName(var1)) {
         throw new InvalidDatatypeValueException("IDREFInvalid", new Object[]{var1});
      }

      var2.addIdRef(var1);
   }
}
