package org.apache.batik.script.rhino;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.batik.script.Window;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;

public class WindowWrapper extends ImporterTopLevel {
   private static final Object[] EMPTY_ARGUMENTS = new Object[0];
   protected RhinoInterpreter interpreter;
   protected Window window;
   // $FF: synthetic field
   static Class class$org$apache$batik$script$rhino$WindowWrapper;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class class$org$w3c$dom$Document;

   public WindowWrapper(Context var1) {
      super(var1);
      String[] var2 = new String[]{"setInterval", "setTimeout", "clearInterval", "clearTimeout", "parseXML", "getURL", "postURL", "alert", "confirm", "prompt"};
      this.defineFunctionProperties(var2, class$org$apache$batik$script$rhino$WindowWrapper == null ? (class$org$apache$batik$script$rhino$WindowWrapper = class$("org.apache.batik.script.rhino.WindowWrapper")) : class$org$apache$batik$script$rhino$WindowWrapper, 2);
   }

   public String getClassName() {
      return "Window";
   }

   public String toString() {
      return "[object Window]";
   }

   public static Object setInterval(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 < 2) {
         throw Context.reportRuntimeError("invalid argument count");
      } else {
         long var7 = (Long)Context.jsToJava(var2[1], Long.TYPE);
         if (var2[0] instanceof Function) {
            RhinoInterpreter var11 = (RhinoInterpreter)var6.getInterpreter();
            FunctionWrapper var10 = new FunctionWrapper(var11, (Function)var2[0], EMPTY_ARGUMENTS);
            return Context.toObject(var6.setInterval((Runnable)var10, var7), var1);
         } else {
            String var9 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return Context.toObject(var6.setInterval(var9, var7), var1);
         }
      }
   }

   public static Object setTimeout(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 < 2) {
         throw Context.reportRuntimeError("invalid argument count");
      } else {
         long var7 = (Long)Context.jsToJava(var2[1], Long.TYPE);
         if (var2[0] instanceof Function) {
            RhinoInterpreter var11 = (RhinoInterpreter)var6.getInterpreter();
            FunctionWrapper var10 = new FunctionWrapper(var11, (Function)var2[0], EMPTY_ARGUMENTS);
            return Context.toObject(var6.setTimeout((Runnable)var10, var7), var1);
         } else {
            String var9 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            return Context.toObject(var6.setTimeout(var9, var7), var1);
         }
      }
   }

   public static void clearInterval(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 >= 1) {
         var6.clearInterval(Context.jsToJava(var2[0], class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object));
      }

   }

   public static void clearTimeout(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 >= 1) {
         var6.clearTimeout(Context.jsToJava(var2[0], class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object));
      }

   }

   public static Object parseXML(Context var0, Scriptable var1, final Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      final Window var6 = var5.window;
      if (var4 < 2) {
         throw Context.reportRuntimeError("invalid argument count");
      } else {
         AccessControlContext var7 = ((RhinoInterpreter)var6.getInterpreter()).getAccessControlContext();
         Object var8 = AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return var6.parseXML((String)Context.jsToJava(var2[0], WindowWrapper.class$java$lang$String == null ? (WindowWrapper.class$java$lang$String = WindowWrapper.class$("java.lang.String")) : WindowWrapper.class$java$lang$String), (Document)Context.jsToJava(var2[1], WindowWrapper.class$org$w3c$dom$Document == null ? (WindowWrapper.class$org$w3c$dom$Document = WindowWrapper.class$("org.w3c.dom.Document")) : WindowWrapper.class$org$w3c$dom$Document));
            }
         }, var7);
         return Context.toObject(var8, var1);
      }
   }

   public static void getURL(Context var0, Scriptable var1, final Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      final Window var6 = var5.window;
      if (var4 < 2) {
         throw Context.reportRuntimeError("invalid argument count");
      } else {
         RhinoInterpreter var7 = (RhinoInterpreter)var6.getInterpreter();
         final String var8 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         final Object var9 = null;
         if (var2[1] instanceof Function) {
            var9 = new GetURLFunctionWrapper(var7, (Function)var2[1], var5);
         } else {
            var9 = new GetURLObjectWrapper(var7, (NativeObject)var2[1], var5);
         }

         AccessControlContext var11 = ((RhinoInterpreter)var6.getInterpreter()).getAccessControlContext();
         if (var4 == 2) {
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  var6.getURL(var8, (Window.URLResponseHandler)var9);
                  return null;
               }
            }, var11);
         } else {
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  var6.getURL(var8, (Window.URLResponseHandler)var9, (String)Context.jsToJava(var2[2], WindowWrapper.class$java$lang$String == null ? (WindowWrapper.class$java$lang$String = WindowWrapper.class$("java.lang.String")) : WindowWrapper.class$java$lang$String));
                  return null;
               }
            }, var11);
         }

      }
   }

   public static void postURL(Context var0, Scriptable var1, final Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      final Window var6 = var5.window;
      if (var4 < 3) {
         throw Context.reportRuntimeError("invalid argument count");
      } else {
         RhinoInterpreter var7 = (RhinoInterpreter)var6.getInterpreter();
         final String var8 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         final String var9 = (String)Context.jsToJava(var2[1], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         final Object var10 = null;
         if (var2[2] instanceof Function) {
            var10 = new GetURLFunctionWrapper(var7, (Function)var2[2], var5);
         } else {
            var10 = new GetURLObjectWrapper(var7, (NativeObject)var2[2], var5);
         }

         AccessControlContext var12 = var7.getAccessControlContext();
         switch (var4) {
            case 3:
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     var6.postURL(var8, var9, (Window.URLResponseHandler)var10);
                     return null;
                  }
               }, var12);
               break;
            case 4:
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     var6.postURL(var8, var9, (Window.URLResponseHandler)var10, (String)Context.jsToJava(var2[3], WindowWrapper.class$java$lang$String == null ? (WindowWrapper.class$java$lang$String = WindowWrapper.class$("java.lang.String")) : WindowWrapper.class$java$lang$String));
                     return null;
                  }
               }, var12);
               break;
            default:
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     var6.postURL(var8, var9, (Window.URLResponseHandler)var10, (String)Context.jsToJava(var2[3], WindowWrapper.class$java$lang$String == null ? (WindowWrapper.class$java$lang$String = WindowWrapper.class$("java.lang.String")) : WindowWrapper.class$java$lang$String), (String)Context.jsToJava(var2[4], WindowWrapper.class$java$lang$String == null ? (WindowWrapper.class$java$lang$String = WindowWrapper.class$("java.lang.String")) : WindowWrapper.class$java$lang$String));
                     return null;
                  }
               }, var12);
         }

      }
   }

   public static void alert(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 >= 1) {
         String var7 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         var6.alert(var7);
      }

   }

   public static Object confirm(Context var0, Scriptable var1, Object[] var2, Function var3) {
      int var4 = var2.length;
      WindowWrapper var5 = (WindowWrapper)var1;
      Window var6 = var5.window;
      if (var4 >= 1) {
         String var7 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         return var6.confirm(var7) ? Context.toObject(Boolean.TRUE, var1) : Context.toObject(Boolean.FALSE, var1);
      } else {
         return Context.toObject(Boolean.FALSE, var1);
      }
   }

   public static Object prompt(Context var0, Scriptable var1, Object[] var2, Function var3) {
      WindowWrapper var4 = (WindowWrapper)var1;
      Window var5 = var4.window;
      String var6;
      String var7;
      switch (var2.length) {
         case 0:
            var6 = "";
            break;
         case 1:
            var7 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6 = var5.prompt(var7);
            break;
         default:
            var7 = (String)Context.jsToJava(var2[0], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            String var8 = (String)Context.jsToJava(var2[1], class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            var6 = var5.prompt(var7, var8);
      }

      return var6 == null ? null : Context.toObject(var6, var1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static class GetURLDoneArgBuilder implements RhinoInterpreter.ArgumentsBuilder {
      boolean success;
      String mime;
      String content;
      WindowWrapper windowWrapper;

      public GetURLDoneArgBuilder(boolean var1, String var2, String var3, WindowWrapper var4) {
         this.success = var1;
         this.mime = var2;
         this.content = var3;
         this.windowWrapper = var4;
      }

      public Object[] buildArguments() {
         NativeObject var1 = new NativeObject();
         var1.put("success", var1, this.success ? Boolean.TRUE : Boolean.FALSE);
         if (this.mime != null) {
            var1.put("contentType", var1, Context.toObject(this.mime, this.windowWrapper));
         }

         if (this.content != null) {
            var1.put("content", var1, Context.toObject(this.content, this.windowWrapper));
         }

         return new Object[]{var1};
      }
   }

   private static class GetURLObjectWrapper implements Window.URLResponseHandler {
      private RhinoInterpreter interpreter;
      private ScriptableObject object;
      private WindowWrapper windowWrapper;
      private static final String COMPLETE = "operationComplete";

      public GetURLObjectWrapper(RhinoInterpreter var1, ScriptableObject var2, WindowWrapper var3) {
         this.interpreter = var1;
         this.object = var2;
         this.windowWrapper = var3;
      }

      public void getURLDone(boolean var1, String var2, String var3) {
         this.interpreter.callMethod(this.object, "operationComplete", new GetURLDoneArgBuilder(var1, var2, var3, this.windowWrapper));
      }
   }

   protected static class GetURLFunctionWrapper implements Window.URLResponseHandler {
      protected RhinoInterpreter interpreter;
      protected Function function;
      protected WindowWrapper windowWrapper;

      public GetURLFunctionWrapper(RhinoInterpreter var1, Function var2, WindowWrapper var3) {
         this.interpreter = var1;
         this.function = var2;
         this.windowWrapper = var3;
      }

      public void getURLDone(boolean var1, String var2, String var3) {
         this.interpreter.callHandler(this.function, (RhinoInterpreter.ArgumentsBuilder)(new GetURLDoneArgBuilder(var1, var2, var3, this.windowWrapper)));
      }
   }

   protected static class FunctionWrapper implements Runnable {
      protected RhinoInterpreter interpreter;
      protected Function function;
      protected Object[] arguments;

      public FunctionWrapper(RhinoInterpreter var1, Function var2, Object[] var3) {
         this.interpreter = var1;
         this.function = var2;
         this.arguments = var3;
      }

      public void run() {
         this.interpreter.callHandler(this.function, this.arguments);
      }
   }
}
