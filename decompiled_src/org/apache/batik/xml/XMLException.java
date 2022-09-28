package org.apache.batik.xml;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XMLException extends RuntimeException {
   protected Exception exception;

   public XMLException(String var1) {
      super(var1);
      this.exception = null;
   }

   public XMLException(Exception var1) {
      this.exception = var1;
   }

   public XMLException(String var1, Exception var2) {
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

   public void printStackTrace() {
      if (this.exception == null) {
         super.printStackTrace();
      } else {
         synchronized(System.err) {
            System.err.println(this);
            super.printStackTrace();
         }
      }

   }

   public void printStackTrace(PrintStream var1) {
      if (this.exception == null) {
         super.printStackTrace(var1);
      } else {
         synchronized(var1) {
            var1.println(this);
            super.printStackTrace();
         }
      }

   }

   public void printStackTrace(PrintWriter var1) {
      if (this.exception == null) {
         super.printStackTrace(var1);
      } else {
         synchronized(var1) {
            var1.println(this);
            super.printStackTrace(var1);
         }
      }

   }
}
