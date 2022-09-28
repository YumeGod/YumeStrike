package org.apache.batik.script.rhino;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.script.ScriptEventWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

class EventTargetWrapper extends NativeJavaObject {
   protected static WeakHashMap mapOfListenerMap;
   public static final String ADD_NAME = "addEventListener";
   public static final String ADDNS_NAME = "addEventListenerNS";
   public static final String REMOVE_NAME = "removeEventListener";
   public static final String REMOVENS_NAME = "removeEventListenerNS";
   protected RhinoInterpreter interpreter;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$org$mozilla$javascript$Function;
   // $FF: synthetic field
   static Class class$org$mozilla$javascript$Scriptable;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   EventTargetWrapper(Scriptable var1, EventTarget var2, RhinoInterpreter var3) {
      super(var1, var2, (Class)null);
      this.interpreter = var3;
   }

   public Object get(String var1, Scriptable var2) {
      Object var3 = super.get(var1, var2);
      if (var1.equals("addEventListener")) {
         var3 = new FunctionAddProxy(this.interpreter, (Function)var3, this.initMap());
      } else if (var1.equals("removeEventListener")) {
         var3 = new FunctionRemoveProxy((Function)var3, this.initMap());
      } else if (var1.equals("addEventListenerNS")) {
         var3 = new FunctionAddNSProxy(this.interpreter, (Function)var3, this.initMap());
      } else if (var1.equals("removeEventListenerNS")) {
         var3 = new FunctionRemoveNSProxy((Function)var3, this.initMap());
      }

      return var3;
   }

   public Map initMap() {
      Object var1 = null;
      if (mapOfListenerMap == null) {
         mapOfListenerMap = new WeakHashMap(10);
      }

      if ((var1 = (Map)mapOfListenerMap.get(this.unwrap())) == null) {
         mapOfListenerMap.put(this.unwrap(), var1 = new WeakHashMap(2));
      }

      return (Map)var1;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static class FunctionRemoveNSProxy extends FunctionProxy {
      protected Map listenerMap;

      FunctionRemoveNSProxy(Function var1, Map var2) {
         super(var1);
         this.listenerMap = var2;
      }

      public Object call(Context var1, Scriptable var2, Scriptable var3, Object[] var4) {
         NativeJavaObject var5 = (NativeJavaObject)var3;
         SoftReference var6;
         EventListener var7;
         Class[] var8;
         int var9;
         AbstractNode var10;
         if (var4[2] instanceof Function) {
            var6 = (SoftReference)this.listenerMap.get(var4[2]);
            if (var6 == null) {
               return Undefined.instance;
            } else {
               var7 = (EventListener)var6.get();
               if (var7 == null) {
                  return Undefined.instance;
               } else {
                  var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Function == null ? (EventTargetWrapper.class$org$mozilla$javascript$Function = EventTargetWrapper.class$("org.mozilla.javascript.Function")) : EventTargetWrapper.class$org$mozilla$javascript$Function, Boolean.TYPE};

                  for(var9 = 0; var9 < var4.length; ++var9) {
                     var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
                  }

                  var10 = (AbstractNode)var5.unwrap();
                  var10.removeEventListenerNS((String)var4[0], (String)var4[1], var7, (Boolean)var4[3]);
                  return Undefined.instance;
               }
            }
         } else if (!(var4[2] instanceof NativeObject)) {
            return this.delegate.call(var1, var2, var3, var4);
         } else {
            var6 = (SoftReference)this.listenerMap.get(var4[2]);
            if (var6 == null) {
               return Undefined.instance;
            } else {
               var7 = (EventListener)var6.get();
               if (var7 == null) {
                  return Undefined.instance;
               } else {
                  var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Scriptable == null ? (EventTargetWrapper.class$org$mozilla$javascript$Scriptable = EventTargetWrapper.class$("org.mozilla.javascript.Scriptable")) : EventTargetWrapper.class$org$mozilla$javascript$Scriptable, Boolean.TYPE};

                  for(var9 = 0; var9 < var4.length; ++var9) {
                     var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
                  }

                  var10 = (AbstractNode)var5.unwrap();
                  var10.removeEventListenerNS((String)var4[0], (String)var4[1], var7, (Boolean)var4[3]);
                  return Undefined.instance;
               }
            }
         }
      }
   }

   static class FunctionAddNSProxy extends FunctionProxy {
      protected Map listenerMap;
      protected RhinoInterpreter interpreter;

      FunctionAddNSProxy(RhinoInterpreter var1, Function var2, Map var3) {
         super(var2);
         this.listenerMap = var3;
         this.interpreter = var1;
      }

      public Object call(Context var1, Scriptable var2, Scriptable var3, Object[] var4) {
         NativeJavaObject var5 = (NativeJavaObject)var3;
         Class[] var7;
         int var8;
         AbstractNode var10;
         if (var4[2] instanceof Function) {
            FunctionEventListener var9 = new FunctionEventListener((Function)var4[2], this.interpreter);
            this.listenerMap.put(var4[2], new SoftReference(var9));
            var7 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Function == null ? (EventTargetWrapper.class$org$mozilla$javascript$Function = EventTargetWrapper.class$("org.mozilla.javascript.Function")) : EventTargetWrapper.class$org$mozilla$javascript$Function, Boolean.TYPE, EventTargetWrapper.class$java$lang$Object == null ? (EventTargetWrapper.class$java$lang$Object = EventTargetWrapper.class$("java.lang.Object")) : EventTargetWrapper.class$java$lang$Object};

            for(var8 = 0; var8 < var4.length; ++var8) {
               var4[var8] = Context.jsToJava(var4[var8], var7[var8]);
            }

            var10 = (AbstractNode)var5.unwrap();
            var10.addEventListenerNS((String)var4[0], (String)var4[1], var9, (Boolean)var4[3], var4[4]);
            return Undefined.instance;
         } else if (!(var4[2] instanceof NativeObject)) {
            return this.delegate.call(var1, var2, var3, var4);
         } else {
            HandleEventListener var6 = new HandleEventListener((Scriptable)var4[2], this.interpreter);
            this.listenerMap.put(var4[2], new SoftReference(var6));
            var7 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Scriptable == null ? (EventTargetWrapper.class$org$mozilla$javascript$Scriptable = EventTargetWrapper.class$("org.mozilla.javascript.Scriptable")) : EventTargetWrapper.class$org$mozilla$javascript$Scriptable, Boolean.TYPE, EventTargetWrapper.class$java$lang$Object == null ? (EventTargetWrapper.class$java$lang$Object = EventTargetWrapper.class$("java.lang.Object")) : EventTargetWrapper.class$java$lang$Object};

            for(var8 = 0; var8 < var4.length; ++var8) {
               var4[var8] = Context.jsToJava(var4[var8], var7[var8]);
            }

            var10 = (AbstractNode)var5.unwrap();
            var10.addEventListenerNS((String)var4[0], (String)var4[1], var6, (Boolean)var4[3], var4[4]);
            return Undefined.instance;
         }
      }
   }

   static class FunctionRemoveProxy extends FunctionProxy {
      public Map listenerMap;

      FunctionRemoveProxy(Function var1, Map var2) {
         super(var1);
         this.listenerMap = var2;
      }

      public Object call(Context var1, Scriptable var2, Scriptable var3, Object[] var4) {
         NativeJavaObject var5 = (NativeJavaObject)var3;
         SoftReference var6;
         EventListener var7;
         Class[] var8;
         int var9;
         if (var4[1] instanceof Function) {
            var6 = (SoftReference)this.listenerMap.get(var4[1]);
            if (var6 == null) {
               return Undefined.instance;
            } else {
               var7 = (EventListener)var6.get();
               if (var7 == null) {
                  return Undefined.instance;
               } else {
                  var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Function == null ? (EventTargetWrapper.class$org$mozilla$javascript$Function = EventTargetWrapper.class$("org.mozilla.javascript.Function")) : EventTargetWrapper.class$org$mozilla$javascript$Function, Boolean.TYPE};

                  for(var9 = 0; var9 < var4.length; ++var9) {
                     var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
                  }

                  ((EventTarget)var5.unwrap()).removeEventListener((String)var4[0], var7, (Boolean)var4[2]);
                  return Undefined.instance;
               }
            }
         } else if (!(var4[1] instanceof NativeObject)) {
            return this.delegate.call(var1, var2, var3, var4);
         } else {
            var6 = (SoftReference)this.listenerMap.get(var4[1]);
            if (var6 == null) {
               return Undefined.instance;
            } else {
               var7 = (EventListener)var6.get();
               if (var7 == null) {
                  return Undefined.instance;
               } else {
                  var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Scriptable == null ? (EventTargetWrapper.class$org$mozilla$javascript$Scriptable = EventTargetWrapper.class$("org.mozilla.javascript.Scriptable")) : EventTargetWrapper.class$org$mozilla$javascript$Scriptable, Boolean.TYPE};

                  for(var9 = 0; var9 < var4.length; ++var9) {
                     var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
                  }

                  ((EventTarget)var5.unwrap()).removeEventListener((String)var4[0], var7, (Boolean)var4[2]);
                  return Undefined.instance;
               }
            }
         }
      }
   }

   static class FunctionAddProxy extends FunctionProxy {
      protected Map listenerMap;
      protected RhinoInterpreter interpreter;

      FunctionAddProxy(RhinoInterpreter var1, Function var2, Map var3) {
         super(var2);
         this.listenerMap = var3;
         this.interpreter = var1;
      }

      public Object call(Context var1, Scriptable var2, Scriptable var3, Object[] var4) {
         NativeJavaObject var5 = (NativeJavaObject)var3;
         Object var6;
         SoftReference var7;
         Class[] var8;
         int var9;
         if (var4[1] instanceof Function) {
            var6 = null;
            var7 = (SoftReference)this.listenerMap.get(var4[1]);
            if (var7 != null) {
               var6 = (EventListener)var7.get();
            }

            if (var6 == null) {
               var6 = new FunctionEventListener((Function)var4[1], this.interpreter);
               this.listenerMap.put(var4[1], new SoftReference(var6));
            }

            var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Function == null ? (EventTargetWrapper.class$org$mozilla$javascript$Function = EventTargetWrapper.class$("org.mozilla.javascript.Function")) : EventTargetWrapper.class$org$mozilla$javascript$Function, Boolean.TYPE};

            for(var9 = 0; var9 < var4.length; ++var9) {
               var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
            }

            ((EventTarget)var5.unwrap()).addEventListener((String)var4[0], (EventListener)var6, (Boolean)var4[2]);
            return Undefined.instance;
         } else if (!(var4[1] instanceof NativeObject)) {
            return this.delegate.call(var1, var2, var3, var4);
         } else {
            var6 = null;
            var7 = (SoftReference)this.listenerMap.get(var4[1]);
            if (var7 != null) {
               var6 = (EventListener)var7.get();
            }

            if (var6 == null) {
               var6 = new HandleEventListener((Scriptable)var4[1], this.interpreter);
               this.listenerMap.put(var4[1], new SoftReference(var6));
            }

            var8 = new Class[]{EventTargetWrapper.class$java$lang$String == null ? (EventTargetWrapper.class$java$lang$String = EventTargetWrapper.class$("java.lang.String")) : EventTargetWrapper.class$java$lang$String, EventTargetWrapper.class$org$mozilla$javascript$Scriptable == null ? (EventTargetWrapper.class$org$mozilla$javascript$Scriptable = EventTargetWrapper.class$("org.mozilla.javascript.Scriptable")) : EventTargetWrapper.class$org$mozilla$javascript$Scriptable, Boolean.TYPE};

            for(var9 = 0; var9 < var4.length; ++var9) {
               var4[var9] = Context.jsToJava(var4[var9], var8[var9]);
            }

            ((EventTarget)var5.unwrap()).addEventListener((String)var4[0], (EventListener)var6, (Boolean)var4[2]);
            return Undefined.instance;
         }
      }
   }

   abstract static class FunctionProxy implements Function {
      protected Function delegate;

      public FunctionProxy(Function var1) {
         this.delegate = var1;
      }

      public Scriptable construct(Context var1, Scriptable var2, Object[] var3) {
         return this.delegate.construct(var1, var2, var3);
      }

      public String getClassName() {
         return this.delegate.getClassName();
      }

      public Object get(String var1, Scriptable var2) {
         return this.delegate.get(var1, var2);
      }

      public Object get(int var1, Scriptable var2) {
         return this.delegate.get(var1, var2);
      }

      public boolean has(String var1, Scriptable var2) {
         return this.delegate.has(var1, var2);
      }

      public boolean has(int var1, Scriptable var2) {
         return this.delegate.has(var1, var2);
      }

      public void put(String var1, Scriptable var2, Object var3) {
         this.delegate.put(var1, var2, var3);
      }

      public void put(int var1, Scriptable var2, Object var3) {
         this.delegate.put(var1, var2, var3);
      }

      public void delete(String var1) {
         this.delegate.delete(var1);
      }

      public void delete(int var1) {
         this.delegate.delete(var1);
      }

      public Scriptable getPrototype() {
         return this.delegate.getPrototype();
      }

      public void setPrototype(Scriptable var1) {
         this.delegate.setPrototype(var1);
      }

      public Scriptable getParentScope() {
         return this.delegate.getParentScope();
      }

      public void setParentScope(Scriptable var1) {
         this.delegate.setParentScope(var1);
      }

      public Object[] getIds() {
         return this.delegate.getIds();
      }

      public Object getDefaultValue(Class var1) {
         return this.delegate.getDefaultValue(var1);
      }

      public boolean hasInstance(Scriptable var1) {
         return this.delegate.hasInstance(var1);
      }
   }

   static class HandleEventListener implements EventListener {
      public static final String HANDLE_EVENT = "handleEvent";
      public Scriptable scriptable;
      public Object[] array = new Object[1];
      public RhinoInterpreter interpreter;

      HandleEventListener(Scriptable var1, RhinoInterpreter var2) {
         this.scriptable = var1;
         this.interpreter = var2;
      }

      public void handleEvent(Event var1) {
         if (var1 instanceof ScriptEventWrapper) {
            this.array[0] = ((ScriptEventWrapper)var1).getEventObject();
         } else {
            this.array[0] = var1;
         }

         ContextAction var2 = new ContextAction() {
            public Object run(Context var1) {
               ScriptableObject.callMethod(HandleEventListener.this.scriptable, "handleEvent", HandleEventListener.this.array);
               return null;
            }
         };
         this.interpreter.call(var2);
      }
   }

   static class FunctionEventListener implements EventListener {
      protected Function function;
      protected RhinoInterpreter interpreter;

      FunctionEventListener(Function var1, RhinoInterpreter var2) {
         this.function = var1;
         this.interpreter = var2;
      }

      public void handleEvent(Event var1) {
         Object var2;
         if (var1 instanceof ScriptEventWrapper) {
            var2 = ((ScriptEventWrapper)var1).getEventObject();
         } else {
            var2 = var1;
         }

         this.interpreter.callHandler(this.function, var2);
      }
   }
}
