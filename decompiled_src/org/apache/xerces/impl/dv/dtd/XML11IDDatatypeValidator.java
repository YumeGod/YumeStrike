package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XML11Char;

public class XML11IDDatatypeValidator extends IDDatatypeValidator {
   public void validate(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      if (var2.useNamespaces()) {
         if (!XML11Char.isXML11ValidNCName(var1)) {
            throw new InvalidDatatypeValueException("IDInvalidWithNamespaces", new Object[]{var1});
         }
      } else if (!XML11Char.isXML11ValidName(var1)) {
         throw new InvalidDatatypeValueException("IDInvalid", new Object[]{var1});
      }

      if (var2.isIdDeclared(var1)) {
         throw new InvalidDatatypeValueException("IDNotUnique", new Object[]{var1});
      } else {
         var2.addId(var1);
      }
   }
}
