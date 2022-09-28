package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.datatypes.XSQName;

public class QNameDV extends TypeValidator {
   private static final String EMPTY_STRING = "".intern();

   public short getAllowedFacets() {
      return 2079;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      int var5 = var1.indexOf(":");
      String var3;
      String var4;
      if (var5 > 0) {
         var3 = var2.getSymbol(var1.substring(0, var5));
         var4 = var1.substring(var5 + 1);
      } else {
         var3 = EMPTY_STRING;
         var4 = var1;
      }

      if (var3.length() > 0 && !XMLChar.isValidNCName(var3)) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "QName"});
      } else if (!XMLChar.isValidNCName(var4)) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "QName"});
      } else {
         String var6 = var2.getURI(var3);
         if (var3.length() > 0 && var6 == null) {
            throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[]{var1, var3});
         } else {
            return new XQName(var3, var2.getSymbol(var4), var2.getSymbol(var1), var6);
         }
      }
   }

   public int getDataLength(Object var1) {
      return ((XQName)var1).rawname.length();
   }

   private static final class XQName extends QName implements XSQName {
      public XQName(String var1, String var2, String var3, String var4) {
         this.setValues(var1, var2, var3, var4);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof QName)) {
            return false;
         } else {
            QName var2 = (QName)var1;
            return super.uri == var2.uri && super.localpart == var2.localpart;
         }
      }

      public synchronized String toString() {
         return super.rawname;
      }

      public javax.xml.namespace.QName getJAXPQName() {
         return new javax.xml.namespace.QName(super.uri, super.localpart, super.prefix);
      }

      public QName getXNIQName() {
         return this;
      }
   }
}
