package org.apache.xerces.impl.dv;

import java.util.Hashtable;

public abstract class DTDDVFactory {
   private static final String DEFAULT_FACTORY_CLASS = "org.apache.xerces.impl.dv.dtd.DTDDVFactoryImpl";

   public static final synchronized DTDDVFactory getInstance() throws DVFactoryException {
      return getInstance("org.apache.xerces.impl.dv.dtd.DTDDVFactoryImpl");
   }

   public static final synchronized DTDDVFactory getInstance(String var0) throws DVFactoryException {
      try {
         return (DTDDVFactory)ObjectFactory.newInstance(var0, ObjectFactory.findClassLoader(), true);
      } catch (ClassCastException var2) {
         throw new DVFactoryException("DTD factory class " + var0 + " does not extend from DTDDVFactory.");
      }
   }

   protected DTDDVFactory() {
   }

   public abstract DatatypeValidator getBuiltInDV(String var1);

   public abstract Hashtable getBuiltInTypes();
}
