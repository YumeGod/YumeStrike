package org.w3c.dom.ls;

public class LSException extends RuntimeException {
   public short code;
   public static final short PARSE_ERR = 81;
   public static final short SERIALIZE_ERR = 82;

   public LSException(short var1, String var2) {
      super(var2);
      this.code = var1;
   }
}
