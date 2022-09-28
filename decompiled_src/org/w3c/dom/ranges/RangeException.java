package org.w3c.dom.ranges;

public class RangeException extends RuntimeException {
   public short code;
   public static final short BAD_BOUNDARYPOINTS_ERR = 1;
   public static final short INVALID_NODE_TYPE_ERR = 2;

   public RangeException(short var1, String var2) {
      super(var2);
      this.code = var1;
   }
}
