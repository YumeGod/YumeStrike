package org.xml.sax.helpers;

import org.xml.sax.Parser;

/** @deprecated */
public class ParserFactory {
   private ParserFactory() {
   }

   public static Parser makeParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException {
      SecuritySupport var0 = SecuritySupport.getInstance();
      String var1 = var0.getSystemProperty("org.xml.sax.parser");
      if (var1 == null) {
         throw new NullPointerException("No value for sax.parser property");
      } else {
         return makeParser(var1);
      }
   }

   public static Parser makeParser(String var0) throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {
      return (Parser)NewInstance.newInstance(NewInstance.getClassLoader(), var0);
   }
}
