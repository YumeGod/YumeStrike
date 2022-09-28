package org.xml.sax;

public class SAXException extends Exception {
   private Exception exception;
   static final long serialVersionUID = 583241635256073760L;

   public SAXException() {
      this.exception = null;
   }

   public SAXException(String var1) {
      super(var1);
      this.exception = null;
   }

   public SAXException(Exception var1) {
      this.exception = var1;
   }

   public SAXException(String var1, Exception var2) {
      super(var1);
      this.exception = var2;
   }

   public String getMessage() {
      String var1 = super.getMessage();
      return var1 == null && this.exception != null ? this.exception.getMessage() : var1;
   }

   public Exception getException() {
      return this.exception;
   }

   public String toString() {
      return this.exception != null ? this.exception.toString() : super.toString();
   }
}
