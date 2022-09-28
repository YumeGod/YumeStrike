package org.apache.xalan.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;

public class ExtensionHandlerGeneral extends ExtensionHandler {
   private String m_scriptSrc;
   private String m_scriptSrcURL;
   private Hashtable m_functions = new Hashtable();
   private Hashtable m_elements = new Hashtable();
   private Object m_engine;
   private Method m_engineCall = null;
   private static String BSF_MANAGER = ObjectFactory.lookUpFactoryClassName("org.apache.xalan.extensions.bsf.BSFManager", (String)null, (String)null);
   private static final String DEFAULT_BSF_MANAGER = "org.apache.bsf.BSFManager";
   private static final String propName = "org.apache.xalan.extensions.bsf.BSFManager";
   private static final Integer ZEROINT = new Integer(0);
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Object;
   // $FF: synthetic field
   static Class array$Ljava$lang$Object;

   public ExtensionHandlerGeneral(String namespaceUri, StringVector elemNames, StringVector funcNames, String scriptLang, String scriptSrcURL, String scriptSrc, String systemId) throws TransformerException {
      super(namespaceUri, scriptLang);
      Object manager;
      int n;
      int indexOfColon;
      String tok;
      if (elemNames != null) {
         manager = new Object();
         n = elemNames.size();

         for(indexOfColon = 0; indexOfColon < n; ++indexOfColon) {
            tok = elemNames.elementAt(indexOfColon);
            this.m_elements.put(tok, manager);
         }
      }

      if (funcNames != null) {
         manager = new Object();
         n = funcNames.size();

         for(indexOfColon = 0; indexOfColon < n; ++indexOfColon) {
            tok = funcNames.elementAt(indexOfColon);
            this.m_functions.put(tok, manager);
         }
      }

      this.m_scriptSrcURL = scriptSrcURL;
      this.m_scriptSrc = scriptSrc;
      if (this.m_scriptSrcURL != null) {
         manager = null;

         URL url;
         try {
            url = new URL(this.m_scriptSrcURL);
         } catch (MalformedURLException var17) {
            indexOfColon = this.m_scriptSrcURL.indexOf(58);
            int indexOfSlash = this.m_scriptSrcURL.indexOf(47);
            if (indexOfColon != -1 && indexOfSlash != -1 && indexOfColon < indexOfSlash) {
               manager = null;
               throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), var17);
            }

            try {
               url = new URL(new URL(SystemIDResolver.getAbsoluteURI(systemId)), this.m_scriptSrcURL);
            } catch (MalformedURLException var16) {
               throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), var16);
            }
         }

         if (url != null) {
            try {
               URLConnection uc = url.openConnection();
               InputStream is = uc.getInputStream();
               byte[] bArray = new byte[uc.getContentLength()];
               is.read(bArray);
               this.m_scriptSrc = new String(bArray);
            } catch (IOException var15) {
               throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), var15);
            }
         }
      }

      manager = null;

      try {
         manager = ObjectFactory.newInstance(BSF_MANAGER, ObjectFactory.findClassLoader(), true);
      } catch (ObjectFactory.ConfigurationError var14) {
         var14.printStackTrace();
      }

      if (manager == null) {
         throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_INIT_BSFMGR", (Object[])null));
      } else {
         try {
            Method loadScriptingEngine = manager.getClass().getMethod("loadScriptingEngine", class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            this.m_engine = loadScriptingEngine.invoke(manager, scriptLang);
            Method engineExec = this.m_engine.getClass().getMethod("exec", class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, Integer.TYPE, Integer.TYPE, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            engineExec.invoke(this.m_engine, "XalanScript", ZEROINT, ZEROINT, this.m_scriptSrc);
         } catch (Exception var13) {
            var13.printStackTrace();
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_CMPL_EXTENSN", (Object[])null), var13);
         }
      }
   }

   public boolean isFunctionAvailable(String function) {
      return this.m_functions.get(function) != null;
   }

   public boolean isElementAvailable(String element) {
      return this.m_elements.get(element) != null;
   }

   public Object callFunction(String funcName, Vector args, Object methodKey, ExpressionContext exprContext) throws TransformerException {
      try {
         Object[] argArray = new Object[args.size()];

         for(int i = 0; i < argArray.length; ++i) {
            Object o = args.elementAt(i);
            argArray[i] = o instanceof XObject ? ((XObject)o).object() : o;
            o = argArray[i];
            if (null != o && o instanceof DTMIterator) {
               argArray[i] = new DTMNodeList((DTMIterator)o);
            }
         }

         if (this.m_engineCall == null) {
            this.m_engineCall = this.m_engine.getClass().getMethod("call", class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
         }

         return this.m_engineCall.invoke(this.m_engine, null, funcName, argArray);
      } catch (Exception var8) {
         var8.printStackTrace();
         String msg = var8.getMessage();
         if (null != msg) {
            if (msg.startsWith("Stopping after fatal error:")) {
               msg = msg.substring("Stopping after fatal error:".length());
            }

            throw new TransformerException(var8);
         } else {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_CREATE_EXTENSN", new Object[]{funcName, var8}));
         }
      }
   }

   public Object callFunction(FuncExtFunction extFunction, Vector args, ExpressionContext exprContext) throws TransformerException {
      return this.callFunction(extFunction.getFunctionName(), args, extFunction.getMethodKey(), exprContext);
   }

   public void processElement(String localPart, ElemTemplateElement element, TransformerImpl transformer, Stylesheet stylesheetTree, Object methodKey) throws TransformerException, IOException {
      Object result = null;
      XSLProcessorContext xpc = new XSLProcessorContext(transformer, stylesheetTree);

      try {
         Vector argv = new Vector(2);
         argv.addElement(xpc);
         argv.addElement(element);
         result = this.callFunction(localPart, argv, methodKey, transformer.getXPathContext().getExpressionContext());
      } catch (XPathProcessorException var9) {
         throw new TransformerException(var9.getMessage(), var9);
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

   static {
      if (BSF_MANAGER == null) {
         BSF_MANAGER = "org.apache.bsf.BSFManager";
      }

   }
}
