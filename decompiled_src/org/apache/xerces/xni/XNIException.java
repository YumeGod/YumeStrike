package org.apache.xerces.xni;

public class XNIException extends RuntimeException {
   static final long serialVersionUID = 9019819772686063775L;
   private Exception fException;

   public XNIException(String var1) {
      super(var1);
   }

   public XNIException(Exception var1) {
      super(var1.getMessage());
      this.fException = var1;
   }

   public XNIException(String var1, Exception var2) {
      super(var1);
      this.fException = var2;
   }

   public Exception getException() {
      return this.fException;
   }
}
