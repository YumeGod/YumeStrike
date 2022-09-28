package org.apache.batik.apps.rasterizer;

public class SVGConverterException extends Exception {
   protected String errorCode;
   protected Object[] errorInfo;
   protected boolean isFatal;

   public SVGConverterException(String var1) {
      this(var1, (Object[])null, false);
   }

   public SVGConverterException(String var1, Object[] var2) {
      this(var1, var2, false);
   }

   public SVGConverterException(String var1, Object[] var2, boolean var3) {
      this.errorCode = var1;
      this.errorInfo = var2;
      this.isFatal = var3;
   }

   public SVGConverterException(String var1, boolean var2) {
      this(var1, (Object[])null, var2);
   }

   public boolean isFatal() {
      return this.isFatal;
   }

   public String getMessage() {
      return Messages.formatMessage(this.errorCode, this.errorInfo);
   }

   public String getErrorCode() {
      return this.errorCode;
   }
}
