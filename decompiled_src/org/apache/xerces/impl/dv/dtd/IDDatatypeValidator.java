package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XMLChar;

public class IDDatatypeValidator implements DatatypeValidator {
   public void validate(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      if (var2.useNamespaces()) {
         if (!XMLChar.isValidNCName(var1)) {
            throw new InvalidDatatypeValueException("IDInvalidWithNamespaces", new Object[]{var1});
         }
      } else if (!XMLChar.isValidName(var1)) {
         throw new InvalidDatatypeValueException("IDInvalid", new Object[]{var1});
      }

      if (var2.isIdDeclared(var1)) {
         throw new InvalidDatatypeValueException("IDNotUnique", new Object[]{var1});
      } else {
         var2.addId(var1);
      }
   }
}
