package org.w3c.dom.xpath;

public class XPathException extends RuntimeException {
   public short code;
   public static final short INVALID_EXPRESSION_ERR = 51;
   public static final short TYPE_ERR = 52;

   public XPathException(short var1, String var2) {
      super(var2);
      this.code = var1;
   }
}
