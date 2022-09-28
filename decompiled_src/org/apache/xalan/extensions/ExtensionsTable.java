package org.apache.xalan.extensions;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.functions.FuncExtFunction;

public class ExtensionsTable {
   public Hashtable m_extensionFunctionNamespaces = new Hashtable();
   private StylesheetRoot m_sroot;

   public ExtensionsTable(StylesheetRoot sroot) throws TransformerException {
      this.m_sroot = sroot;
      Vector extensions = this.m_sroot.getExtensions();

      for(int i = 0; i < extensions.size(); ++i) {
         ExtensionNamespaceSupport extNamespaceSpt = (ExtensionNamespaceSupport)extensions.elementAt(i);
         ExtensionHandler extHandler = extNamespaceSpt.launch();
         if (extHandler != null) {
            this.addExtensionNamespace(extNamespaceSpt.getNamespace(), extHandler);
         }
      }

   }

   public ExtensionHandler get(String extns) {
      return (ExtensionHandler)this.m_extensionFunctionNamespaces.get(extns);
   }

   public void addExtensionNamespace(String uri, ExtensionHandler extNS) {
      this.m_extensionFunctionNamespaces.put(uri, extNS);
   }

   public boolean functionAvailable(String ns, String funcName) throws TransformerException {
      boolean isAvailable = false;
      if (null != ns) {
         ExtensionHandler extNS = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(ns);
         if (extNS != null) {
            isAvailable = extNS.isFunctionAvailable(funcName);
         }
      }

      return isAvailable;
   }

   public boolean elementAvailable(String ns, String elemName) throws TransformerException {
      boolean isAvailable = false;
      if (null != ns) {
         ExtensionHandler extNS = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(ns);
         if (extNS != null) {
            isAvailable = extNS.isElementAvailable(elemName);
         }
      }

      return isAvailable;
   }

   public Object extFunction(String ns, String funcName, Vector argVec, Object methodKey, ExpressionContext exprContext) throws TransformerException {
      Object result = null;
      if (null != ns) {
         ExtensionHandler extNS = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(ns);
         if (null == extNS) {
            throw new XPathProcessorException(XSLMessages.createMessage("ER_EXTENSION_FUNC_UNKNOWN", new Object[]{ns, funcName}));
         }

         try {
            result = extNS.callFunction(funcName, argVec, methodKey, exprContext);
         } catch (TransformerException var10) {
            throw var10;
         } catch (Exception var11) {
            throw new TransformerException(var11);
         }
      }

      return result;
   }

   public Object extFunction(FuncExtFunction extFunction, Vector argVec, ExpressionContext exprContext) throws TransformerException {
      Object result = null;
      String ns = extFunction.getNamespace();
      if (null != ns) {
         ExtensionHandler extNS = (ExtensionHandler)this.m_extensionFunctionNamespaces.get(ns);
         if (null == extNS) {
            throw new XPathProcessorException(XSLMessages.createMessage("ER_EXTENSION_FUNC_UNKNOWN", new Object[]{ns, extFunction.getFunctionName()}));
         }

         try {
            result = extNS.callFunction(extFunction, argVec, exprContext);
         } catch (TransformerException var9) {
            throw var9;
         } catch (Exception var10) {
            throw new TransformerException(var10);
         }
      }

      return result;
   }
}
