package javax.xml.transform;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransformerException extends Exception {
   private static final long serialVersionUID = 975798773772956428L;
   SourceLocator locator;
   Throwable containedException;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public SourceLocator getLocator() {
      return this.locator;
   }

   public void setLocator(SourceLocator var1) {
      this.locator = var1;
   }

   public Throwable getException() {
      return this.containedException;
   }

   public Throwable getCause() {
      return this.containedException == this ? null : this.containedException;
   }

   public synchronized Throwable initCause(Throwable var1) {
      if (this.containedException != null) {
         throw new IllegalStateException("Can't overwrite cause");
      } else if (var1 == this) {
         throw new IllegalArgumentException("Self-causation not permitted");
      } else {
         this.containedException = var1;
         return this;
      }
   }

   public TransformerException(String var1) {
      super(var1);
      this.containedException = null;
      this.locator = null;
   }

   public TransformerException(Throwable var1) {
      super(var1.toString());
      this.containedException = var1;
      this.locator = null;
   }

   public TransformerException(String var1, Throwable var2) {
      super(var1 != null && var1.length() != 0 ? var1 : var2.toString());
      this.containedException = var2;
      this.locator = null;
   }

   public TransformerException(String var1, SourceLocator var2) {
      super(var1);
      this.containedException = null;
      this.locator = var2;
   }

   public TransformerException(String var1, SourceLocator var2, Throwable var3) {
      super(var1);
      this.containedException = var3;
      this.locator = var2;
   }

   public String getMessageAndLocation() {
      StringBuffer var1 = new StringBuffer();
      String var2 = super.getMessage();
      if (null != var2) {
         var1.append(var2);
      }

      if (null != this.locator) {
         String var3 = this.locator.getSystemId();
         int var4 = this.locator.getLineNumber();
         int var5 = this.locator.getColumnNumber();
         if (null != var3) {
            var1.append("; SystemID: ");
            var1.append(var3);
         }

         if (0 != var4) {
            var1.append("; Line#: ");
            var1.append(var4);
         }

         if (0 != var5) {
            var1.append("; Column#: ");
            var1.append(var5);
         }
      }

      return var1.toString();
   }

   public String getLocationAsString() {
      if (null != this.locator) {
         StringBuffer var1 = new StringBuffer();
         String var2 = this.locator.getSystemId();
         int var3 = this.locator.getLineNumber();
         int var4 = this.locator.getColumnNumber();
         if (null != var2) {
            var1.append("; SystemID: ");
            var1.append(var2);
         }

         if (0 != var3) {
            var1.append("; Line#: ");
            var1.append(var3);
         }

         if (0 != var4) {
            var1.append("; Column#: ");
            var1.append(var4);
         }

         return var1.toString();
      } else {
         return null;
      }
   }

   public void printStackTrace() {
      this.printStackTrace(new PrintWriter(System.err, true));
   }

   public void printStackTrace(PrintStream var1) {
      this.printStackTrace(new PrintWriter(var1));
   }

   public void printStackTrace(PrintWriter var1) {
      if (var1 == null) {
         var1 = new PrintWriter(System.err, true);
      }

      try {
         String var2 = this.getLocationAsString();
         if (null != var2) {
            var1.println(var2);
         }

         super.printStackTrace(var1);
      } catch (Throwable var10) {
      }

      boolean var14 = false;

      try {
         (class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable).getMethod("getCause", (Class[])null);
         var14 = true;
      } catch (NoSuchMethodException var9) {
      }

      if (!var14) {
         Throwable var3 = this.getException();

         for(int var4 = 0; var4 < 10 && null != var3; ++var4) {
            var1.println("---------");

            try {
               if (var3 instanceof TransformerException) {
                  String var5 = ((TransformerException)var3).getLocationAsString();
                  if (null != var5) {
                     var1.println(var5);
                  }
               }

               var3.printStackTrace(var1);
            } catch (Throwable var8) {
               var1.println("Could not print stack trace...");
            }

            try {
               Method var15 = var3.getClass().getMethod("getException", (Class[])null);
               if (null != var15) {
                  Throwable var6 = var3;
                  var3 = (Throwable)var15.invoke(var3, (Object[])null);
                  if (var6 == var3) {
                     break;
                  }
               } else {
                  var3 = null;
               }
            } catch (InvocationTargetException var11) {
               var3 = null;
            } catch (IllegalAccessException var12) {
               var3 = null;
            } catch (NoSuchMethodException var13) {
               var3 = null;
            }
         }
      }

      var1.flush();
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
