package org.apache.xalan.extensions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;

public class ExtensionHandlerJavaPackage extends ExtensionHandlerJava {
   // $FF: synthetic field
   static Class class$org$apache$xalan$extensions$XSLProcessorContext;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExtensionCall;

   public ExtensionHandlerJavaPackage(String namespaceUri, String scriptLang, String className) {
      super(namespaceUri, scriptLang, className);
   }

   public boolean isFunctionAvailable(String function) {
      try {
         String fullName = super.m_className + function;
         int lastDot = fullName.lastIndexOf(".");
         if (lastDot >= 0) {
            Class myClass = ExtensionHandler.getClassForName(fullName.substring(0, lastDot));
            Method[] methods = myClass.getMethods();
            int nMethods = methods.length;
            function = fullName.substring(lastDot + 1);

            for(int i = 0; i < nMethods; ++i) {
               if (methods[i].getName().equals(function)) {
                  return true;
               }
            }
         }
      } catch (ClassNotFoundException var8) {
      }

      return false;
   }

   public boolean isElementAvailable(String element) {
      try {
         String fullName = super.m_className + element;
         int lastDot = fullName.lastIndexOf(".");
         if (lastDot >= 0) {
            Class myClass = ExtensionHandler.getClassForName(fullName.substring(0, lastDot));
            Method[] methods = myClass.getMethods();
            int nMethods = methods.length;
            element = fullName.substring(lastDot + 1);

            for(int i = 0; i < nMethods; ++i) {
               if (methods[i].getName().equals(element)) {
                  Class[] paramTypes = methods[i].getParameterTypes();
                  if (paramTypes.length == 2 && paramTypes[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? (class$org$apache$xalan$extensions$XSLProcessorContext = class$("org.apache.xalan.extensions.XSLProcessorContext")) : class$org$apache$xalan$extensions$XSLProcessorContext) && paramTypes[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? (class$org$apache$xalan$templates$ElemExtensionCall = class$("org.apache.xalan.templates.ElemExtensionCall")) : class$org$apache$xalan$templates$ElemExtensionCall)) {
                     return true;
                  }
               }
            }
         }
      } catch (ClassNotFoundException var9) {
      }

      return false;
   }

   public Object callFunction(String funcName, Vector args, Object methodKey, ExpressionContext exprContext) throws TransformerException {
      int lastDot = funcName.lastIndexOf(".");

      try {
         TransformerImpl trans = exprContext != null ? (TransformerImpl)exprContext.getXPathContext().getOwnerObject() : null;
         String className;
         Class classObj;
         Object[] methodArgs;
         Object[][] convertedArgs;
         Class[] paramTypes;
         Object result;
         int i;
         if (funcName.endsWith(".new")) {
            methodArgs = new Object[args.size()];
            convertedArgs = new Object[1][];

            for(i = 0; i < methodArgs.length; ++i) {
               methodArgs[i] = args.elementAt(i);
            }

            Constructor c = methodKey != null ? (Constructor)this.getFromCache(methodKey, (Object)null, methodArgs) : null;
            if (c != null) {
               try {
                  paramTypes = c.getParameterTypes();
                  MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
                  return c.newInstance(convertedArgs[0]);
               } catch (InvocationTargetException var80) {
                  throw var80;
               } catch (Exception var81) {
               }
            }

            className = super.m_className + funcName.substring(0, lastDot);

            try {
               classObj = ExtensionHandler.getClassForName(className);
            } catch (ClassNotFoundException var74) {
               throw new TransformerException(var74);
            }

            c = MethodResolver.getConstructor(classObj, methodArgs, convertedArgs, exprContext);
            if (methodKey != null) {
               this.putToCache(methodKey, (Object)null, methodArgs, c);
            }

            if (trans != null && trans.getDebug()) {
               trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, c, convertedArgs[0]));

               try {
                  result = c.newInstance(convertedArgs[0]);
               } catch (Exception var72) {
                  throw var72;
               } finally {
                  trans.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(trans, c, convertedArgs[0]));
               }

               return result;
            } else {
               return c.newInstance(convertedArgs[0]);
            }
         } else {
            Method m;
            if (-1 != lastDot) {
               methodArgs = new Object[args.size()];
               convertedArgs = new Object[1][];

               for(i = 0; i < methodArgs.length; ++i) {
                  methodArgs[i] = args.elementAt(i);
               }

               m = methodKey != null ? (Method)this.getFromCache(methodKey, (Object)null, methodArgs) : null;
               if (m != null && !trans.getDebug()) {
                  try {
                     paramTypes = m.getParameterTypes();
                     MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
                     return m.invoke((Object)null, convertedArgs[0]);
                  } catch (InvocationTargetException var82) {
                     throw var82;
                  } catch (Exception var83) {
                  }
               }

               className = super.m_className + funcName.substring(0, lastDot);
               String methodName = funcName.substring(lastDot + 1);

               try {
                  classObj = ExtensionHandler.getClassForName(className);
               } catch (ClassNotFoundException var77) {
                  throw new TransformerException(var77);
               }

               m = MethodResolver.getMethod(classObj, methodName, methodArgs, convertedArgs, exprContext, 1);
               if (methodKey != null) {
                  this.putToCache(methodKey, (Object)null, methodArgs, m);
               }

               if (trans != null && trans.getDebug()) {
                  trans.getTraceManager().fireExtensionEvent(m, (Object)null, convertedArgs[0]);

                  try {
                     result = m.invoke((Object)null, convertedArgs[0]);
                  } catch (Exception var75) {
                     throw var75;
                  } finally {
                     trans.getTraceManager().fireExtensionEndEvent(m, (Object)null, convertedArgs[0]);
                  }

                  return result;
               } else {
                  return m.invoke((Object)null, convertedArgs[0]);
               }
            } else if (args.size() < 1) {
               throw new TransformerException(XSLMessages.createMessage("ER_INSTANCE_MTHD_CALL_REQUIRES", new Object[]{funcName}));
            } else {
               Object targetObject = args.elementAt(0);
               if (targetObject instanceof XObject) {
                  targetObject = ((XObject)targetObject).object();
               }

               methodArgs = new Object[args.size() - 1];
               convertedArgs = new Object[1][];

               for(i = 0; i < methodArgs.length; ++i) {
                  methodArgs[i] = args.elementAt(i + 1);
               }

               m = methodKey != null ? (Method)this.getFromCache(methodKey, targetObject, methodArgs) : null;
               if (m != null) {
                  try {
                     paramTypes = m.getParameterTypes();
                     MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
                     return m.invoke(targetObject, convertedArgs[0]);
                  } catch (InvocationTargetException var84) {
                     throw var84;
                  } catch (Exception var85) {
                  }
               }

               classObj = targetObject.getClass();
               m = MethodResolver.getMethod(classObj, funcName, methodArgs, convertedArgs, exprContext, 2);
               if (methodKey != null) {
                  this.putToCache(methodKey, targetObject, methodArgs, m);
               }

               if (trans != null && trans.getDebug()) {
                  trans.getTraceManager().fireExtensionEvent(m, targetObject, convertedArgs[0]);

                  try {
                     result = m.invoke(targetObject, convertedArgs[0]);
                  } catch (Exception var78) {
                     throw var78;
                  } finally {
                     trans.getTraceManager().fireExtensionEndEvent(m, targetObject, convertedArgs[0]);
                  }

                  return result;
               } else {
                  return m.invoke(targetObject, convertedArgs[0]);
               }
            }
         }
      } catch (InvocationTargetException var86) {
         Throwable resultException = var86;
         Throwable targetException = var86.getTargetException();
         if (targetException instanceof TransformerException) {
            throw (TransformerException)targetException;
         } else {
            if (targetException != null) {
               resultException = targetException;
            }

            throw new TransformerException((Throwable)resultException);
         }
      } catch (Exception var87) {
         throw new TransformerException(var87);
      }
   }

   public Object callFunction(FuncExtFunction extFunction, Vector args, ExpressionContext exprContext) throws TransformerException {
      return this.callFunction(extFunction.getFunctionName(), args, extFunction.getMethodKey(), exprContext);
   }

   public void processElement(String localPart, ElemTemplateElement element, TransformerImpl transformer, Stylesheet stylesheetTree, Object methodKey) throws TransformerException, IOException {
      Object result = null;
      Method m = (Method)this.getFromCache(methodKey, (Object)null, (Object[])null);
      if (null == m) {
         try {
            String fullName = super.m_className + localPart;
            int lastDot = fullName.lastIndexOf(".");
            if (lastDot < 0) {
               throw new TransformerException(XSLMessages.createMessage("ER_INVALID_ELEMENT_NAME", new Object[]{fullName}));
            }

            Class classObj;
            try {
               classObj = ExtensionHandler.getClassForName(fullName.substring(0, lastDot));
            } catch (ClassNotFoundException var22) {
               throw new TransformerException(var22);
            }

            localPart = fullName.substring(lastDot + 1);
            m = MethodResolver.getElementMethod(classObj, localPart);
            if (!Modifier.isStatic(m.getModifiers())) {
               throw new TransformerException(XSLMessages.createMessage("ER_ELEMENT_NAME_METHOD_STATIC", new Object[]{fullName}));
            }
         } catch (Exception var25) {
            throw new TransformerException(var25);
         }

         this.putToCache(methodKey, (Object)null, (Object[])null, m);
      }

      XSLProcessorContext xpc = new XSLProcessorContext(transformer, stylesheetTree);

      try {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireExtensionEvent(m, (Object)null, new Object[]{xpc, element});

            try {
               result = m.invoke((Object)null, xpc, element);
            } catch (Exception var20) {
               throw var20;
            } finally {
               transformer.getTraceManager().fireExtensionEndEvent(m, (Object)null, new Object[]{xpc, element});
            }
         } else {
            result = m.invoke((Object)null, xpc, element);
         }
      } catch (InvocationTargetException var23) {
         Throwable resultException = var23;
         Throwable targetException = var23.getTargetException();
         if (targetException instanceof TransformerException) {
            throw (TransformerException)targetException;
         }

         if (targetException != null) {
            resultException = targetException;
         }

         throw new TransformerException((Throwable)resultException);
      } catch (Exception var24) {
         throw new TransformerException(var24);
      }

      if (result != null) {
         xpc.outputToResultTree(stylesheetTree, result);
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
