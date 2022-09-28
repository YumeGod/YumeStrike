package org.apache.xerces.impl.xpath.regex;

public class ParseException extends RuntimeException {
   static final long serialVersionUID = -7012400318097691370L;
   int location;

   public ParseException(String var1, int var2) {
      super(var1);
      this.location = var2;
   }

   public int getLocation() {
      return this.location;
   }
}
