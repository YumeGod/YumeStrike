package javax.xml.xpath;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XPathException extends Exception {
   private final Throwable cause;
   private static final long serialVersionUID = -1837080260374986980L;

   public XPathException(String var1) {
      super(var1);
      if (var1 == null) {
         throw new NullPointerException("message can't be null");
      } else {
         this.cause = null;
      }
   }

   public XPathException(Throwable var1) {
      super(var1 == null ? null : var1.toString());
      this.cause = var1;
      if (var1 == null) {
         throw new NullPointerException("cause can't be null");
      }
   }

   public Throwable getCause() {
      return this.cause;
   }

   public void printStackTrace(PrintStream var1) {
      if (this.getCause() != null) {
         this.getCause().printStackTrace(var1);
         var1.println("--------------- linked to ------------------");
      }

      super.printStackTrace(var1);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintWriter var1) {
      if (this.getCause() != null) {
         this.getCause().printStackTrace(var1);
         var1.println("--------------- linked to ------------------");
      }

      super.printStackTrace(var1);
   }
}
