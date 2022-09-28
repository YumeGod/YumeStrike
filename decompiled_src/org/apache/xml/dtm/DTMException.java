package org.apache.xml.dtm;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import org.apache.xml.res.XMLMessages;

public class DTMException extends RuntimeException {
   static final long serialVersionUID = -775576419181334734L;
   SourceLocator locator;
   Throwable containedException;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public SourceLocator getLocator() {
      return this.locator;
   }

   public void setLocator(SourceLocator location) {
      this.locator = location;
   }

   public Throwable getException() {
      return this.containedException;
   }

   public Throwable getCause() {
      return this.containedException == this ? null : this.containedException;
   }

   public synchronized Throwable initCause(Throwable cause) {
      if (this.containedException == null && cause != null) {
         throw new IllegalStateException(XMLMessages.createXMLMessage("ER_CANNOT_OVERWRITE_CAUSE", (Object[])null));
      } else if (cause == this) {
         throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_SELF_CAUSATION_NOT_PERMITTED", (Object[])null));
      } else {
         this.containedException = cause;
         return this;
      }
   }

   public DTMException(String message) {
      super(message);
      this.containedException = null;
      this.locator = null;
   }

   public DTMException(Throwable e) {
      super(e.getMessage());
      this.containedException = e;
      this.locator = null;
   }

   public DTMException(String message, Throwable e) {
      super(message != null && message.length() != 0 ? message : e.getMessage());
      this.containedException = e;
      this.locator = null;
   }

   public DTMException(String message, SourceLocator locator) {
      super(message);
      this.containedException = null;
      this.locator = locator;
   }

   public DTMException(String message, SourceLocator locator, Throwable e) {
      super(message);
      this.containedException = e;
      this.locator = locator;
   }

   public String getMessageAndLocation() {
      StringBuffer sbuffer = new StringBuffer();
      String message = super.getMessage();
      if (null != message) {
         sbuffer.append(message);
      }

      if (null != this.locator) {
         String systemID = this.locator.getSystemId();
         int line = this.locator.getLineNumber();
         int column = this.locator.getColumnNumber();
         if (null != systemID) {
            sbuffer.append("; SystemID: ");
            sbuffer.append(systemID);
         }

         if (0 != line) {
            sbuffer.append("; Line#: ");
            sbuffer.append(line);
         }

         if (0 != column) {
            sbuffer.append("; Column#: ");
            sbuffer.append(column);
         }
      }

      return sbuffer.toString();
   }

   public String getLocationAsString() {
      if (null != this.locator) {
         StringBuffer sbuffer = new StringBuffer();
         String systemID = this.locator.getSystemId();
         int line = this.locator.getLineNumber();
         int column = this.locator.getColumnNumber();
         if (null != systemID) {
            sbuffer.append("; SystemID: ");
            sbuffer.append(systemID);
         }

         if (0 != line) {
            sbuffer.append("; Line#: ");
            sbuffer.append(line);
         }

         if (0 != column) {
            sbuffer.append("; Column#: ");
            sbuffer.append(column);
         }

         return sbuffer.toString();
      } else {
         return null;
      }
   }

   public void printStackTrace() {
      this.printStackTrace(new PrintWriter(System.err, true));
   }

   public void printStackTrace(PrintStream s) {
      this.printStackTrace(new PrintWriter(s));
   }

   public void printStackTrace(PrintWriter s) {
      if (s == null) {
         s = new PrintWriter(System.err, true);
      }

      try {
         String locInfo = this.getLocationAsString();
         if (null != locInfo) {
            s.println(locInfo);
         }

         super.printStackTrace(s);
      } catch (Throwable var10) {
      }

      boolean isJdk14OrHigher = false;

      try {
         (class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable).getMethod("getCause", (Class[])null);
         isJdk14OrHigher = true;
      } catch (NoSuchMethodException var9) {
      }

      if (!isJdk14OrHigher) {
         Throwable exception = this.getException();

         for(int i = 0; i < 10 && null != exception; ++i) {
            s.println("---------");

            try {
               if (exception instanceof DTMException) {
                  String locInfo = ((DTMException)exception).getLocationAsString();
                  if (null != locInfo) {
                     s.println(locInfo);
                  }
               }

               exception.printStackTrace(s);
            } catch (Throwable var8) {
               s.println("Could not print stack trace...");
            }

            try {
               Method meth = exception.getClass().getMethod("getException", (Class[])null);
               if (null != meth) {
                  Throwable prev = exception;
                  exception = (Throwable)meth.invoke(exception, (Object[])null);
                  if (prev == exception) {
                     break;
                  }
               } else {
                  exception = null;
               }
            } catch (InvocationTargetException var11) {
               exception = null;
            } catch (IllegalAccessException var12) {
               exception = null;
            } catch (NoSuchMethodException var13) {
               exception = null;
            }
         }
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
