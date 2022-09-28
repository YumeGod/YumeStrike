package org.apache.xerces.impl.dv.dtd;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xerces.impl.dv.DatatypeValidator;

public class XML11DTDDVFactoryImpl extends DTDDVFactoryImpl {
   static Hashtable fXML11BuiltInTypes = new Hashtable();

   public DatatypeValidator getBuiltInDV(String var1) {
      return fXML11BuiltInTypes.get(var1) != null ? (DatatypeValidator)fXML11BuiltInTypes.get(var1) : (DatatypeValidator)DTDDVFactoryImpl.fBuiltInTypes.get(var1);
   }

   public Hashtable getBuiltInTypes() {
      Hashtable var1 = (Hashtable)DTDDVFactoryImpl.fBuiltInTypes.clone();
      Enumeration var2 = fXML11BuiltInTypes.keys();

      while(var2.hasMoreElements()) {
         Object var3 = var2.nextElement();
         var1.put(var3, fXML11BuiltInTypes.get(var3));
      }

      return var1;
   }

   static {
      fXML11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
      XML11IDREFDatatypeValidator var0 = new XML11IDREFDatatypeValidator();
      fXML11BuiltInTypes.put("XML11IDREF", var0);
      fXML11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator(var0));
      XML11NMTOKENDatatypeValidator var1 = new XML11NMTOKENDatatypeValidator();
      fXML11BuiltInTypes.put("XML11NMTOKEN", var1);
      fXML11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator(var1));
   }
}
