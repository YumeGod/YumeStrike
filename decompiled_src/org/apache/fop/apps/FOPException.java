package org.apache.fop.apps;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class FOPException extends SAXException {
   private static final String EXCEPTION_SEPARATOR = "\n---------\n";
   private String systemId;
   private int line;
   private int column;
   private String localizedMessage;

   public FOPException(String message) {
      super(message);
   }

   public FOPException(String message, String systemId, int line, int column) {
      super(message);
      this.systemId = systemId;
      this.line = line;
      this.column = column;
   }

   public FOPException(String message, Locator locator) {
      super(message);
      this.setLocator(locator);
   }

   public FOPException(Exception cause) {
      super(cause);
   }

   public FOPException(String message, Exception cause) {
      super(message, cause);
   }

   public void setLocator(Locator locator) {
      if (locator != null) {
         this.systemId = locator.getSystemId();
         this.line = locator.getLineNumber();
         this.column = locator.getColumnNumber();
      }

   }

   public void setLocation(String systemId, int line, int column) {
      this.systemId = systemId;
      this.line = line;
      this.column = column;
   }

   public boolean isLocationSet() {
      return this.line > 0;
   }

   public String getMessage() {
      return this.isLocationSet() ? this.systemId + ":" + this.line + ":" + this.column + ": " + super.getMessage() : super.getMessage();
   }

   protected Throwable getRootException() {
      Throwable result = this.getException();
      if (result instanceof SAXException) {
         result = ((SAXException)result).getException();
      }

      if (result instanceof InvocationTargetException) {
         result = ((InvocationTargetException)result).getTargetException();
      }

      return (Throwable)(result != this.getException() ? result : null);
   }

   public void printStackTrace() {
      synchronized(System.err) {
         super.printStackTrace();
         if (this.getException() != null) {
            System.err.println("\n---------\n");
            this.getException().printStackTrace();
         }

         if (this.getRootException() != null) {
            System.err.println("\n---------\n");
            this.getRootException().printStackTrace();
         }

      }
   }

   public void printStackTrace(PrintStream stream) {
      synchronized(stream) {
         super.printStackTrace(stream);
         if (this.getException() != null) {
            stream.println("\n---------\n");
            this.getException().printStackTrace(stream);
         }

         if (this.getRootException() != null) {
            stream.println("\n---------\n");
            this.getRootException().printStackTrace(stream);
         }

      }
   }

   public void printStackTrace(PrintWriter writer) {
      synchronized(writer) {
         super.printStackTrace(writer);
         if (this.getException() != null) {
            writer.println("\n---------\n");
            this.getException().printStackTrace(writer);
         }

         if (this.getRootException() != null) {
            writer.println("\n---------\n");
            this.getRootException().printStackTrace(writer);
         }

      }
   }

   public void setLocalizedMessage(String msg) {
      this.localizedMessage = msg;
   }

   public String getLocalizedMessage() {
      return this.localizedMessage != null ? this.localizedMessage : super.getLocalizedMessage();
   }
}
