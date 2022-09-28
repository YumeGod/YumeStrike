package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XMLChar;

public class IDREFDatatypeValidator implements DatatypeValidator {
   public void validate(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      if (var2.useNamespaces()) {
         if (!XMLChar.isValidNCName(var1)) {
            throw new InvalidDatatypeValueException("IDREFInvalidWithNamespaces", new Object[]{var1});
         }
      } else if (!XMLChar.isValidName(var1)) {
         throw new InvalidDatatypeValueException("IDREFInvalid", new Object[]{var1});
      }

      var2.addIdRef(var1);
   }
}
