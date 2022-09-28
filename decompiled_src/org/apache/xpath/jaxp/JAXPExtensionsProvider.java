package org.apache.xpath.jaxp;

import java.util.ArrayList;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;

public class JAXPExtensionsProvider implements ExtensionsProvider {
   private final XPathFunctionResolver resolver;
   private boolean extensionInvocationDisabled = false;

   public JAXPExtensionsProvider(XPathFunctionResolver resolver) {
      this.resolver = resolver;
      this.extensionInvocationDisabled = false;
   }

   public JAXPExtensionsProvider(XPathFunctionResolver resolver, boolean featureSecureProcessing) {
      this.resolver = resolver;
      this.extensionInvocationDisabled = featureSecureProcessing;
   }

   public boolean functionAvailable(String ns, String funcName) throws TransformerException {
      try {
         if (funcName == null) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"Function Name"});
            throw new NullPointerException(fmsg);
         } else {
            QName myQName = new QName(ns, funcName);
            XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, 0);
            return xpathFunction != null;
         }
      } catch (Exception var5) {
         return false;
      }
   }

   public boolean elementAvailable(String ns, String elemName) throws TransformerException {
      return false;
   }

   public Object extFunction(String ns, String funcName, Vector argVec, Object methodKey) throws TransformerException {
      try {
         if (funcName == null) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[]{"Function Name"});
            throw new NullPointerException(fmsg);
         } else {
            QName myQName = new QName(ns, funcName);
            if (this.extensionInvocationDisabled) {
               String fmsg = XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{myQName.toString()});
               throw new XPathFunctionException(fmsg);
            } else {
               int arity = argVec.size();
               XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
               ArrayList argList = new ArrayList(arity);

               for(int i = 0; i < arity; ++i) {
                  Object argument = argVec.elementAt(i);
                  if (argument instanceof XNodeSet) {
                     argList.add(i, ((XNodeSet)argument).nodelist());
                  } else if (argument instanceof XObject) {
                     Object passedArgument = ((XObject)argument).object();
                     argList.add(i, passedArgument);
                  } else {
                     argList.add(i, argument);
                  }
               }

               return xpathFunction.evaluate(argList);
            }
         }
      } catch (XPathFunctionException var12) {
         throw new WrappedRuntimeException(var12);
      } catch (Exception var13) {
         throw new TransformerException(var13);
      }
   }

   public Object extFunction(FuncExtFunction extFunction, Vector argVec) throws TransformerException {
      try {
         String namespace = extFunction.getNamespace();
         String functionName = extFunction.getFunctionName();
         int arity = extFunction.getArgCount();
         QName myQName = new QName(namespace, functionName);
         if (this.extensionInvocationDisabled) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{myQName.toString()});
            throw new XPathFunctionException(fmsg);
         } else {
            XPathFunction xpathFunction = this.resolver.resolveFunction(myQName, arity);
            ArrayList argList = new ArrayList(arity);

            for(int i = 0; i < arity; ++i) {
               Object argument = argVec.elementAt(i);
               if (argument instanceof XNodeSet) {
                  argList.add(i, ((XNodeSet)argument).nodelist());
               } else if (argument instanceof XObject) {
                  Object passedArgument = ((XObject)argument).object();
                  argList.add(i, passedArgument);
               } else {
                  argList.add(i, argument);
               }
            }

            return xpathFunction.evaluate(argList);
         }
      } catch (XPathFunctionException var12) {
         throw new WrappedRuntimeException(var12);
      } catch (Exception var13) {
         throw new TransformerException(var13);
      }
   }
}
