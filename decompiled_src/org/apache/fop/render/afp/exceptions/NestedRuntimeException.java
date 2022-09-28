package org.apache.fop.render.afp.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class NestedRuntimeException extends RuntimeException {
   private Throwable underlyingException;

   public NestedRuntimeException(String msg) {
      super(msg);
   }

   public NestedRuntimeException(String msg, Throwable t) {
      super(msg);
      this.underlyingException = t;
   }

   public Throwable getUnderlyingException() {
      return this.underlyingException;
   }

   public String getMessage() {
      return this.underlyingException == null ? super.getMessage() : super.getMessage() + "; nested exception is " + this.underlyingException.getClass().getName();
   }

   public void printStackTrace(PrintStream ps) {
      if (this.underlyingException == null) {
         super.printStackTrace(ps);
      } else {
         ps.println(this);
         this.underlyingException.printStackTrace(ps);
      }

   }

   public void printStackTrace(PrintWriter pw) {
      if (this.underlyingException == null) {
         super.printStackTrace(pw);
      } else {
         pw.println(this);
         this.underlyingException.printStackTrace(pw);
      }

   }
}
