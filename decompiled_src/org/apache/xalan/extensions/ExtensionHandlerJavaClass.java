package org.apache.xalan.extensions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;

public class ExtensionHandlerJavaClass extends ExtensionHandlerJava {
   private Class m_classObj = null;
   private Object m_defaultInstance = null;
   // $FF: synthetic field
   static Class class$org$apache$xalan$extensions$XSLProcessorContext;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$ElemExtensionCall;
   // $FF: synthetic field
   static Class class$org$apache$xalan$extensions$ExpressionContext;

   public ExtensionHandlerJavaClass(String namespaceUri, String scriptLang, String className) {
      super(namespaceUri, scriptLang, className);

      try {
         this.m_classObj = ExtensionHandler.getClassForName(className);
      } catch (ClassNotFoundException var5) {
      }

   }

   public boolean isFunctionAvailable(String function) {
      Method[] methods = this.m_classObj.getMethods();
      int nMethods = methods.length;

      for(int i = 0; i < nMethods; ++i) {
         if (methods[i].getName().equals(function)) {
            return true;
         }
      }

      return false;
   }

   public boolean isElementAvailable(String element) {
      Method[] methods = this.m_classObj.getMethods();
      int nMethods = methods.length;

      for(int i = 0; i < nMethods; ++i) {
         if (methods[i].getName().equals(element)) {
            Class[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length == 2 && paramTypes[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? (class$org$apache$xalan$extensions$XSLProcessorContext = class$("org.apache.xalan.extensions.XSLProcessorContext")) : class$org$apache$xalan$extensions$XSLProcessorContext) && paramTypes[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? (class$org$apache$xalan$templates$ElemExtensionCall = class$("org.apache.xalan.templates.ElemExtensionCall")) : class$org$apache$xalan$templates$ElemExtensionCall)) {
               return true;
            }
         }
      }

      return false;
   }

   public Object callFunction(String funcName, Vector args, Object methodKey, ExpressionContext exprContext) throws TransformerException {
      Throwable targetException;
      try {
         TransformerImpl trans = exprContext != null ? (TransformerImpl)exprContext.getXPathContext().getOwnerObject() : null;
         Object[] methodArgs;
         Object[][] convertedArgs;
         Class[] paramTypes;
         int resolveType;
         if (funcName.equals("new")) {
            methodArgs = new Object[args.size()];
            convertedArgs = new Object[1][];

            for(resolveType = 0; resolveType < methodArgs.length; ++resolveType) {
               methodArgs[resolveType] = args.elementAt(resolveType);
            }

            Constructor c = null;
            if (methodKey != null) {
               c = (Constructor)this.getFromCache(methodKey, (Object)null, methodArgs);
            }

            if (c != null && !trans.getDebug()) {
               try {
                  paramTypes = c.getParameterTypes();
                  MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
                  return c.newInstance(convertedArgs[0]);
               } catch (InvocationTargetException var113) {
                  throw var113;
               } catch (Exception var114) {
               }
            }

            c = MethodResolver.getConstructor(this.m_classObj, methodArgs, convertedArgs, exprContext);
            if (methodKey != null) {
               this.putToCache(methodKey, (Object)null, methodArgs, c);
            }

            if (trans != null && trans.getDebug()) {
               trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, c, convertedArgs[0]));

               Object result;
               try {
                  result = c.newInstance(convertedArgs[0]);
               } catch (Exception var103) {
                  throw var103;
               } finally {
                  trans.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(trans, c, convertedArgs[0]));
               }

               return result;
            } else {
               return c.newInstance(convertedArgs[0]);
            }
         } else {
            targetException = null;
            methodArgs = new Object[args.size()];
            convertedArgs = new Object[1][];

            for(int i = 0; i < methodArgs.length; ++i) {
               methodArgs[i] = args.elementAt(i);
            }

            Method m = null;
            if (methodKey != null) {
               m = (Method)this.getFromCache(methodKey, (Object)null, methodArgs);
            }

            Object targetObject;
            if (m != null && !trans.getDebug()) {
               try {
                  paramTypes = m.getParameterTypes();
                  MethodResolver.convertParams(methodArgs, convertedArgs, paramTypes, exprContext);
                  if (Modifier.isStatic(m.getModifiers())) {
                     return m.invoke((Object)null, convertedArgs[0]);
                  }

                  int nTargetArgs = convertedArgs[0].length;
                  if ((class$org$apache$xalan$extensions$ExpressionContext == null ? (class$org$apache$xalan$extensions$ExpressionContext = class$("org.apache.xalan.extensions.ExpressionContext")) : class$org$apache$xalan$extensions$ExpressionContext).isAssignableFrom(paramTypes[0])) {
                     --nTargetArgs;
                  }

                  if (methodArgs.length <= nTargetArgs) {
                     return m.invoke(this.m_defaultInstance, convertedArgs[0]);
                  }

                  targetObject = methodArgs[0];
                  if (targetObject instanceof XObject) {
                     targetObject = ((XObject)targetObject).object();
                  }

                  return m.invoke(targetObject, convertedArgs[0]);
               } catch (InvocationTargetException var115) {
                  throw var115;
               } catch (Exception var116) {
               }
            }

            if (args.size() > 0) {
               targetObject = methodArgs[0];
               if (targetObject instanceof XObject) {
                  targetObject = ((XObject)targetObject).object();
               }

               if (this.m_classObj.isAssignableFrom(targetObject.getClass())) {
                  resolveType = 4;
               } else {
                  resolveType = 3;
               }
            } else {
               targetObject = null;
               resolveType = 3;
            }

            m = MethodResolver.getMethod(this.m_classObj, funcName, methodArgs, convertedArgs, exprContext, resolveType);
            if (methodKey != null) {
               this.putToCache(methodKey, (Object)null, methodArgs, m);
            }

            Object result;
            if (4 == resolveType) {
               if (trans != null && trans.getDebug()) {
                  trans.getTraceManager().fireExtensionEvent(m, targetObject, convertedArgs[0]);

                  try {
                     result = m.invoke(targetObject, convertedArgs[0]);
                  } catch (Exception var105) {
                     throw var105;
                  } finally {
                     trans.getTraceManager().fireExtensionEndEvent(m, targetObject, convertedArgs[0]);
                  }

                  return result;
               } else {
                  return m.invoke(targetObject, convertedArgs[0]);
               }
            } else if (Modifier.isStatic(m.getModifiers())) {
               if (trans != null && trans.getDebug()) {
                  trans.getTraceManager().fireExtensionEvent(m, (Object)null, convertedArgs[0]);

                  try {
                     result = m.invoke((Object)null, convertedArgs[0]);
                  } catch (Exception var107) {
                     throw var107;
                  } finally {
                     trans.getTraceManager().fireExtensionEndEvent(m, (Object)null, convertedArgs[0]);
                  }

                  return result;
               } else {
                  return m.invoke((Object)null, convertedArgs[0]);
               }
            } else {
               if (null == this.m_defaultInstance) {
                  if (trans != null && trans.getDebug()) {
                     trans.getTraceManager().fireExtensionEvent(new ExtensionEvent(trans, this.m_classObj));

                     try {
                        this.m_defaultInstance = this.m_classObj.newInstance();
                     } catch (Exception var111) {
                        throw var111;
                     } finally {
                        trans.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(trans, this.m_classObj));
                     }
                  } else {
                     this.m_defaultInstance = this.m_classObj.newInstance();
                  }
               }

               if (trans != null && trans.getDebug()) {
                  trans.getTraceManager().fireExtensionEvent(m, this.m_defaultInstance, convertedArgs[0]);

                  try {
                     result = m.invoke(this.m_defaultInstance, convertedArgs[0]);
                  } catch (Exception var109) {
                     throw var109;
                  } finally {
                     trans.getTraceManager().fireExtensionEndEvent(m, this.m_defaultInstance, convertedArgs[0]);
                  }

                  return result;
               } else {
                  return m.invoke(this.m_defaultInstance, convertedArgs[0]);
               }
            }
         }
      } catch (InvocationTargetException var117) {
         Throwable resultException = var117;
         targetException = var117.getTargetException();
         if (targetException instanceof TransformerException) {
            throw (TransformerException)targetException;
         } else {
            if (targetException != null) {
               resultException = targetException;
            }

            throw new TransformerException((Throwable)resultException);
         }
      } catch (Exception var118) {
         throw new TransformerException(var118);
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
            m = MethodResolver.getElementMethod(this.m_classObj, localPart);
            if (null == this.m_defaultInstance && !Modifier.isStatic(m.getModifiers())) {
               if (transformer.getDebug()) {
                  transformer.getTraceManager().fireExtensionEvent(new ExtensionEvent(transformer, this.m_classObj));

                  try {
                     this.m_defaultInstance = this.m_classObj.newInstance();
                  } catch (Exception var29) {
                     throw var29;
                  } finally {
                     transformer.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(transformer, this.m_classObj));
                  }
               } else {
                  this.m_defaultInstance = this.m_classObj.newInstance();
               }
            }
         } catch (Exception var33) {
            throw new TransformerException(var33.getMessage(), var33);
         }

         this.putToCache(methodKey, (Object)null, (Object[])null, m);
      }

      XSLProcessorContext xpc = new XSLProcessorContext(transformer, stylesheetTree);

      try {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireExtensionEvent(m, this.m_defaultInstance, new Object[]{xpc, element});

            try {
               result = m.invoke(this.m_defaultInstance, xpc, element);
            } catch (Exception var27) {
               throw var27;
            } finally {
               transformer.getTraceManager().fireExtensionEndEvent(m, this.m_defaultInstance, new Object[]{xpc, element});
            }
         } else {
            result = m.invoke(this.m_defaultInstance, xpc, element);
         }
      } catch (InvocationTargetException var31) {
         Throwable targetException = var31.getTargetException();
         if (targetException instanceof TransformerException) {
            throw (TransformerException)targetException;
         }

         if (targetException != null) {
            throw new TransformerException(targetException.getMessage(), targetException);
         }

         throw new TransformerException(var31.getMessage(), var31);
      } catch (Exception var32) {
         throw new TransformerException(var32.getMessage(), var32);
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
