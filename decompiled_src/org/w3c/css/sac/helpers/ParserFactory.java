package org.w3c.css.sac.helpers;

import org.w3c.css.sac.Parser;

public class ParserFactory {
   public Parser makeParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException {
      String var1 = System.getProperty("org.w3c.css.sac.parser");
      if (var1 == null) {
         throw new NullPointerException("No value for sac.parser property");
      } else {
         return (Parser)Class.forName(var1).newInstance();
      }
   }
}
