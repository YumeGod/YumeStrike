package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ElemExtensionCall extends ElemLiteralResult {
   static final long serialVersionUID = 3171339708500216920L;
   String m_extns;
   String m_lang;
   String m_srcURL;
   String m_scriptSrc;
   ElemExtensionDecl m_decl = null;

   public int getXSLToken() {
      return 79;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      this.m_extns = this.getNamespace();
      this.m_decl = this.getElemExtensionDecl(sroot, this.m_extns);
      if (this.m_decl == null) {
         sroot.getExtensionNamespacesManager().registerExtension(this.m_extns);
      }

   }

   private ElemExtensionDecl getElemExtensionDecl(StylesheetRoot stylesheet, String namespace) {
      ElemExtensionDecl decl = null;
      int n = stylesheet.getGlobalImportCount();

      for(int i = 0; i < n; ++i) {
         Stylesheet imported = stylesheet.getGlobalImport(i);

         for(ElemTemplateElement child = imported.getFirstChildElem(); child != null; child = child.getNextSiblingElem()) {
            if (85 == child.getXSLToken()) {
               decl = (ElemExtensionDecl)child;
               String prefix = decl.getPrefix();
               String declNamespace = child.getNamespaceForPrefix(prefix);
               if (namespace.equals(declNamespace)) {
                  return decl;
               }
            }
         }
      }

      return null;
   }

   private void executeFallbacks(TransformerImpl transformer) throws TransformerException {
      for(ElemTemplateElement child = super.m_firstChild; child != null; child = child.m_nextSibling) {
         if (child.getXSLToken() == 57) {
            try {
               transformer.pushElemTemplateElement(child);
               ((ElemFallback)child).executeFallback(transformer);
            } finally {
               transformer.popElemTemplateElement();
            }
         }
      }

   }

   private boolean hasFallbackChildren() {
      for(ElemTemplateElement child = super.m_firstChild; child != null; child = child.m_nextSibling) {
         if (child.getXSLToken() == 57) {
            return true;
         }
      }

      return false;
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.getStylesheet().isSecureProcessing()) {
         throw new TransformerException(XSLMessages.createMessage("ER_EXTENSION_ELEMENT_NOT_ALLOWED_IN_SECURE_PROCESSING", new Object[]{this.getRawName()}));
      } else {
         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
         }

         try {
            transformer.getResultTreeHandler().flushPending();
            ExtensionsTable etable = transformer.getExtensionsTable();
            ExtensionHandler nsh = etable.get(this.m_extns);
            if (null == nsh) {
               if (this.hasFallbackChildren()) {
                  this.executeFallbacks(transformer);
               } else {
                  TransformerException te = new TransformerException(XSLMessages.createMessage("ER_CALL_TO_EXT_FAILED", new Object[]{this.getNodeName()}));
                  transformer.getErrorListener().fatalError(te);
               }

               return;
            }

            try {
               nsh.processElement(this.getLocalName(), this, transformer, this.getStylesheet(), this);
            } catch (Exception var6) {
               if (this.hasFallbackChildren()) {
                  this.executeFallbacks(transformer);
               } else if (var6 instanceof TransformerException) {
                  TransformerException te = (TransformerException)var6;
                  if (null == te.getLocator()) {
                     te.setLocator(this);
                  }

                  transformer.getErrorListener().fatalError(te);
               } else if (var6 instanceof RuntimeException) {
                  transformer.getErrorListener().fatalError(new TransformerException(var6));
               } else {
                  transformer.getErrorListener().warning(new TransformerException(var6));
               }
            }
         } catch (TransformerException var7) {
            transformer.getErrorListener().fatalError(var7);
         } catch (SAXException var8) {
            throw new TransformerException(var8);
         }

         if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }
   }

   public String getAttribute(String rawName, Node sourceNode, TransformerImpl transformer) throws TransformerException {
      AVT avt = this.getLiteralResultAttribute(rawName);
      if (null != avt && avt.getRawName().equals(rawName)) {
         XPathContext xctxt = transformer.getXPathContext();
         return avt.evaluate(xctxt, xctxt.getDTMHandleFromNode(sourceNode), this);
      } else {
         return null;
      }
   }

   protected boolean accept(XSLTVisitor visitor) {
      return visitor.visitExtensionElement(this);
   }
}
