package org.apache.xerces.impl.dv.dtd;

import java.util.Hashtable;
import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.impl.dv.DatatypeValidator;

public class DTDDVFactoryImpl extends DTDDVFactory {
   static Hashtable fBuiltInTypes = new Hashtable();

   public DatatypeValidator getBuiltInDV(String var1) {
      return (DatatypeValidator)fBuiltInTypes.get(var1);
   }

   public Hashtable getBuiltInTypes() {
      return (Hashtable)fBuiltInTypes.clone();
   }

   static void createBuiltInTypes() {
      fBuiltInTypes.put("string", new StringDatatypeValidator());
      fBuiltInTypes.put("ID", new IDDatatypeValidator());
      IDREFDatatypeValidator var0 = new IDREFDatatypeValidator();
      fBuiltInTypes.put("IDREF", var0);
      fBuiltInTypes.put("IDREFS", new ListDatatypeValidator(var0));
      ENTITYDatatypeValidator var1 = new ENTITYDatatypeValidator();
      fBuiltInTypes.put("ENTITY", new ENTITYDatatypeValidator());
      fBuiltInTypes.put("ENTITIES", new ListDatatypeValidator(var1));
      fBuiltInTypes.put("NOTATION", new NOTATIONDatatypeValidator());
      NMTOKENDatatypeValidator var2 = new NMTOKENDatatypeValidator();
      fBuiltInTypes.put("NMTOKEN", var2);
      fBuiltInTypes.put("NMTOKENS", new ListDatatypeValidator(var2));
   }

   static {
      createBuiltInTypes();
   }
}
