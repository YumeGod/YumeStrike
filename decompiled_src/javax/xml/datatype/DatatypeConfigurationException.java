package javax.xml.datatype;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class DatatypeConfigurationException extends Exception {
   private static final long serialVersionUID = -1699373159027047238L;
   private Throwable causeOnJDK13OrBelow;
   private transient boolean isJDK14OrAbove = false;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public DatatypeConfigurationException() {
   }

   public DatatypeConfigurationException(String var1) {
      super(var1);
   }

   public DatatypeConfigurationException(String var1, Throwable var2) {
      super(var1);
      this.initCauseByReflection(var2);
   }

   public DatatypeConfigurationException(Throwable var1) {
      super(var1 == null ? null : var1.toString());
      this.initCauseByReflection(var1);
   }

   public void printStackTrace() {
      if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
         this.printStackTrace0(new PrintWriter(System.err, true));
      } else {
         super.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream var1) {
      if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
         this.printStackTrace0(new PrintWriter(var1));
      } else {
         super.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      if (!this.isJDK14OrAbove && this.causeOnJDK13OrBelow != null) {
         this.printStackTrace0(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   private void printStackTrace0(PrintWriter var1) {
      this.causeOnJDK13OrBelow.printStackTrace(var1);
      var1.println("------------------------------------------");
      super.printStackTrace(var1);
   }

   private void initCauseByReflection(Throwable var1) {
      this.causeOnJDK13OrBelow = var1;

      try {
         Method var2 = this.getClass().getMethod("initCause", class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable);
         var2.invoke(this, var1);
         this.isJDK14OrAbove = true;
      } catch (Exception var3) {
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();

      try {
         Method var2 = this.getClass().getMethod("getCause");
         Throwable var3 = (Throwable)var2.invoke(this);
         if (this.causeOnJDK13OrBelow == null) {
            this.causeOnJDK13OrBelow = var3;
         } else if (var3 == null) {
            Method var4 = this.getClass().getMethod("initCause", class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable);
            var4.invoke(this, this.causeOnJDK13OrBelow);
         }

         this.isJDK14OrAbove = true;
      } catch (Exception var5) {
      }

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
