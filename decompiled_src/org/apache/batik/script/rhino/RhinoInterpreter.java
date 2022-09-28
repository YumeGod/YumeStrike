package org.apache.batik.script.rhino;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.batik.bridge.InterruptedBridgeException;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterException;
import org.apache.batik.script.Window;
import org.mozilla.javascript.ClassCache;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.SecurityController;
import org.mozilla.javascript.WrapFactory;
import org.mozilla.javascript.WrappedException;
import org.w3c.dom.events.EventTarget;

public class RhinoInterpreter implements Interpreter {
   protected static String[] TO_BE_IMPORTED = new String[]{"java.lang", "org.w3c.dom", "org.w3c.dom.css", "org.w3c.dom.events", "org.w3c.dom.smil", "org.w3c.dom.stylesheets", "org.w3c.dom.svg", "org.w3c.dom.views", "org.w3c.dom.xpath"};
   private static final int MAX_CACHED_SCRIPTS = 32;
   public static final String SOURCE_NAME_SVG = "<SVG>";
   public static final String BIND_NAME_WINDOW = "window";
   protected static List contexts = new LinkedList();
   protected Window window;
   protected ScriptableObject globalObject = null;
   protected LinkedList compiledScripts = new LinkedList();
   protected WrapFactory wrapFactory = new BatikWrapFactory(this);
   protected ClassShutter classShutter = new RhinoClassShutter();
   protected RhinoClassLoader rhinoClassLoader;
   protected SecurityController securityController = new BatikSecurityController();
   protected ContextFactory contextFactory = new Factory();
   protected Context defaultContext;
   // $FF: synthetic field
   static Class class$org$apache$batik$script$rhino$WindowWrapper;

   public RhinoInterpreter(URL var1) {
      try {
         this.rhinoClassLoader = new RhinoClassLoader(var1, this.getClass().getClassLoader());
      } catch (SecurityException var3) {
         this.rhinoClassLoader = null;
      }

      ContextAction var2 = new ContextAction() {
         public Object run(Context var1) {
            ScriptableObject var2 = var1.initStandardObjects((ScriptableObject)null, false);
            RhinoInterpreter.this.defineGlobalWrapperClass(var2);
            RhinoInterpreter.this.globalObject = RhinoInterpreter.this.createGlobalObject(var1);
            ClassCache var3 = ClassCache.get(RhinoInterpreter.this.globalObject);
            var3.setCachingEnabled(RhinoInterpreter.this.rhinoClassLoader != null);
            StringBuffer var4 = new StringBuffer("importPackage(Packages.");

            for(int var5 = 0; var5 < RhinoInterpreter.TO_BE_IMPORTED.length - 1; ++var5) {
               var4.append(RhinoInterpreter.TO_BE_IMPORTED[var5]);
               var4.append(");importPackage(Packages.");
            }

            var4.append(RhinoInterpreter.TO_BE_IMPORTED[RhinoInterpreter.TO_BE_IMPORTED.length - 1]);
            var4.append(')');
            var1.evaluateString(RhinoInterpreter.this.globalObject, var4.toString(), (String)null, 0, RhinoInterpreter.this.rhinoClassLoader);
            return null;
         }
      };
      this.contextFactory.call(var2);
   }

   public Window getWindow() {
      return this.window;
   }

   public ContextFactory getContextFactory() {
      return this.contextFactory;
   }

   protected void defineGlobalWrapperClass(Scriptable var1) {
      try {
         ScriptableObject.defineClass(var1, class$org$apache$batik$script$rhino$WindowWrapper == null ? (class$org$apache$batik$script$rhino$WindowWrapper = class$("org.apache.batik.script.rhino.WindowWrapper")) : class$org$apache$batik$script$rhino$WindowWrapper);
      } catch (Exception var3) {
      }

   }

   protected ScriptableObject createGlobalObject(Context var1) {
      return new WindowWrapper(var1);
   }

   public AccessControlContext getAccessControlContext() {
      return this.rhinoClassLoader.getAccessControlContext();
   }

   protected ScriptableObject getGlobalObject() {
      return this.globalObject;
   }

   public Object evaluate(Reader var1) throws IOException {
      return this.evaluate(var1, "<SVG>");
   }

   public Object evaluate(final Reader var1, final String var2) throws IOException {
      ContextAction var3 = new ContextAction() {
         public Object run(Context var1x) {
            try {
               return var1x.evaluateReader(RhinoInterpreter.this.globalObject, var1, var2, 1, RhinoInterpreter.this.rhinoClassLoader);
            } catch (IOException var3) {
               throw new WrappedException(var3);
            }
         }
      };

      try {
         return this.contextFactory.call(var3);
      } catch (JavaScriptException var7) {
         Object var11 = var7.getValue();
         Object var6 = var11 instanceof Exception ? (Exception)var11 : var7;
         throw new InterpreterException((Exception)var6, ((Exception)var6).getMessage(), -1, -1);
      } catch (WrappedException var8) {
         Throwable var5 = var8.getWrappedException();
         if (var5 instanceof Exception) {
            throw new InterpreterException((Exception)var5, var5.getMessage(), -1, -1);
         } else {
            throw new InterpreterException(var5.getMessage(), -1, -1);
         }
      } catch (InterruptedBridgeException var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw new InterpreterException(var10, var10.getMessage(), -1, -1);
      }
   }

   public Object evaluate(final String var1) {
      ContextAction var2 = new ContextAction() {
         public Object run(final Context var1x) {
            Script var2 = null;
            Entry var3 = null;
            Iterator var4 = RhinoInterpreter.this.compiledScripts.iterator();

            while(var4.hasNext()) {
               if ((var3 = (Entry)var4.next()).str.equals(var1)) {
                  var2 = var3.script;
                  var4.remove();
                  break;
               }
            }

            if (var2 == null) {
               PrivilegedAction var5 = new PrivilegedAction() {
                  public Object run() {
                     try {
                        return var1x.compileReader(new StringReader(var1), "<SVG>", 1, RhinoInterpreter.this.rhinoClassLoader);
                     } catch (IOException var2) {
                        throw new Error(var2.getMessage());
                     }
                  }
               };
               var2 = (Script)AccessController.doPrivileged(var5);
               if (RhinoInterpreter.this.compiledScripts.size() + 1 > 32) {
                  RhinoInterpreter.this.compiledScripts.removeFirst();
               }

               RhinoInterpreter.this.compiledScripts.addLast(new Entry(var1, var2));
            } else {
               RhinoInterpreter.this.compiledScripts.addLast(var3);
            }

            return var2.exec(var1x, RhinoInterpreter.this.globalObject);
         }
      };

      try {
         return this.contextFactory.call(var2);
      } catch (InterpreterException var6) {
         throw var6;
      } catch (JavaScriptException var7) {
         Object var10 = var7.getValue();
         Object var5 = var10 instanceof Exception ? (Exception)var10 : var7;
         throw new InterpreterException((Exception)var5, ((Exception)var5).getMessage(), -1, -1);
      } catch (WrappedException var8) {
         Throwable var4 = var8.getWrappedException();
         if (var4 instanceof Exception) {
            throw new InterpreterException((Exception)var4, var4.getMessage(), -1, -1);
         } else {
            throw new InterpreterException(var4.getMessage(), -1, -1);
         }
      } catch (RuntimeException var9) {
         throw new InterpreterException(var9, var9.getMessage(), -1, -1);
      }
   }

   public void dispose() {
      if (this.rhinoClassLoader != null) {
         ClassCache var1 = ClassCache.get(this.globalObject);
         var1.setCachingEnabled(false);
      }

   }

   public void bindObject(final String var1, final Object var2) {
      this.contextFactory.call(new ContextAction() {
         public Object run(Context var1x) {
            Object var2x = var2;
            if (var1.equals("window") && var2 instanceof Window) {
               ((WindowWrapper)RhinoInterpreter.this.globalObject).window = (Window)var2;
               RhinoInterpreter.this.window = (Window)var2;
               var2x = RhinoInterpreter.this.globalObject;
            }

            Scriptable var3 = Context.toObject(var2x, RhinoInterpreter.this.globalObject);
            RhinoInterpreter.this.globalObject.put(var1, RhinoInterpreter.this.globalObject, var3);
            return null;
         }
      });
   }

   void callHandler(final Function var1, final Object var2) {
      this.contextFactory.call(new ContextAction() {
         public Object run(Context var1x) {
            Scriptable var2x = Context.toObject(var2, RhinoInterpreter.this.globalObject);
            Object[] var3 = new Object[]{var2x};
            var1.call(var1x, RhinoInterpreter.this.globalObject, RhinoInterpreter.this.globalObject, var3);
            return null;
         }
      });
   }

   void callMethod(final ScriptableObject var1, final String var2, final ArgumentsBuilder var3) {
      this.contextFactory.call(new ContextAction() {
         public Object run(Context var1x) {
            ScriptableObject.callMethod(var1, var2, var3.buildArguments());
            return null;
         }
      });
   }

   void callHandler(final Function var1, final Object[] var2) {
      this.contextFactory.call(new ContextAction() {
         public Object run(Context var1x) {
            var1.call(var1x, RhinoInterpreter.this.globalObject, RhinoInterpreter.this.globalObject, var2);
            return null;
         }
      });
   }

   void callHandler(final Function var1, final ArgumentsBuilder var2) {
      this.contextFactory.call(new ContextAction() {
         public Object run(Context var1x) {
            Object[] var2x = var2.buildArguments();
            var1.call(var1x, var1.getParentScope(), RhinoInterpreter.this.globalObject, var2x);
            return null;
         }
      });
   }

   Object call(ContextAction var1) {
      return this.contextFactory.call(var1);
   }

   Scriptable buildEventTargetWrapper(EventTarget var1) {
      return new EventTargetWrapper(this.globalObject, var1, this);
   }

   public void setOut(Writer var1) {
   }

   public Locale getLocale() {
      return null;
   }

   public void setLocale(Locale var1) {
   }

   public String formatMessage(String var1, Object[] var2) {
      return null;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   protected class Factory extends ContextFactory {
      protected Context makeContext() {
         Context var1 = super.makeContext();
         var1.setWrapFactory(RhinoInterpreter.this.wrapFactory);
         var1.setSecurityController(RhinoInterpreter.this.securityController);
         var1.setClassShutter(RhinoInterpreter.this.classShutter);
         if (RhinoInterpreter.this.rhinoClassLoader == null) {
            var1.setOptimizationLevel(-1);
         }

         return var1;
      }
   }

   protected static class Entry {
      public String str;
      public Script script;

      public Entry(String var1, Script var2) {
         this.str = var1;
         this.script = var2;
      }
   }

   public interface ArgumentsBuilder {
      Object[] buildArguments();
   }
}
