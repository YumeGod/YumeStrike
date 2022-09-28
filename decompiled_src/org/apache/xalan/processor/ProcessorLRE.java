package org.apache.xalan.processor;

import java.util.Vector;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.XMLNSDecl;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.XPath;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class ProcessorLRE extends ProcessorTemplateElem {
   static final long serialVersionUID = -1490218021772101404L;

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      try {
         ElemTemplateElement p = handler.getElemTemplateElement();
         boolean excludeXSLDecl = false;
         boolean isLREAsStyleSheet = false;
         if (null == p) {
            XSLTElementProcessor lreProcessor = handler.popProcessor();
            XSLTElementProcessor stylesheetProcessor = handler.getProcessorFor("http://www.w3.org/1999/XSL/Transform", "stylesheet", "xsl:stylesheet");
            handler.pushProcessor(lreProcessor);

            Stylesheet stylesheet;
            try {
               stylesheet = this.getStylesheetRoot(handler);
            } catch (TransformerConfigurationException var23) {
               throw new TransformerException(var23);
            }

            SAXSourceLocator slocator = new SAXSourceLocator();
            Locator locator = handler.getLocator();
            if (null != locator) {
               slocator.setLineNumber(locator.getLineNumber());
               slocator.setColumnNumber(locator.getColumnNumber());
               slocator.setPublicId(locator.getPublicId());
               slocator.setSystemId(locator.getSystemId());
            }

            stylesheet.setLocaterInfo(slocator);
            stylesheet.setPrefixes(handler.getNamespaceSupport());
            handler.pushStylesheet(stylesheet);
            isLREAsStyleSheet = true;
            AttributesImpl stylesheetAttrs = new AttributesImpl();
            AttributesImpl lreAttrs = new AttributesImpl();
            int n = ((Attributes)attributes).getLength();

            for(int i = 0; i < n; ++i) {
               String attrLocalName = ((Attributes)attributes).getLocalName(i);
               String attrUri = ((Attributes)attributes).getURI(i);
               String value = ((Attributes)attributes).getValue(i);
               if (null != attrUri && attrUri.equals("http://www.w3.org/1999/XSL/Transform")) {
                  stylesheetAttrs.addAttribute((String)null, attrLocalName, attrLocalName, ((Attributes)attributes).getType(i), ((Attributes)attributes).getValue(i));
               } else if (!attrLocalName.startsWith("xmlns:") && !attrLocalName.equals("xmlns") || !value.equals("http://www.w3.org/1999/XSL/Transform")) {
                  lreAttrs.addAttribute(attrUri, attrLocalName, ((Attributes)attributes).getQName(i), ((Attributes)attributes).getType(i), ((Attributes)attributes).getValue(i));
               }
            }

            attributes = lreAttrs;

            try {
               stylesheetProcessor.setPropertiesFromAttributes(handler, "stylesheet", stylesheetAttrs, stylesheet);
            } catch (Exception var24) {
               if (stylesheet.getDeclaredPrefixes() != null && this.declaredXSLNS(stylesheet)) {
                  throw new SAXException(var24);
               }

               throw new SAXException(XSLMessages.createWarning("WG_OLD_XSLT_NS", (Object[])null));
            }

            handler.pushElemTemplateElement(stylesheet);
            ElemTemplate template = new ElemTemplate();
            if (slocator != null) {
               template.setLocaterInfo(slocator);
            }

            this.appendAndPush(handler, template);
            XPath rootMatch = new XPath("/", stylesheet, stylesheet, 1, handler.getStylesheetProcessor().getErrorListener());
            template.setMatch(rootMatch);
            stylesheet.setTemplate(template);
            p = handler.getElemTemplateElement();
            excludeXSLDecl = true;
         }

         XSLTElementDef def = this.getElemDef();
         Class classObject = def.getClassObject();
         boolean isExtension = false;
         boolean isComponentDecl = false;

         boolean isUnknownTopLevel;
         for(isUnknownTopLevel = false; null != p; p = p.getParentElem()) {
            if (p instanceof ElemLiteralResult) {
               ElemLiteralResult parentElem = (ElemLiteralResult)p;
               isExtension = parentElem.containsExtensionElementURI(uri);
            } else if (p instanceof Stylesheet) {
               Stylesheet parentElem = (Stylesheet)p;
               isExtension = parentElem.containsExtensionElementURI(uri);
               if (isExtension || null == uri || !uri.equals("http://xml.apache.org/xalan") && !uri.equals("http://xml.apache.org/xslt")) {
                  isUnknownTopLevel = true;
               } else {
                  isComponentDecl = true;
               }
            }

            if (isExtension) {
               break;
            }
         }

         ElemTemplateElement elem = null;

         try {
            if (isExtension) {
               elem = new ElemExtensionCall();
            } else if (isComponentDecl) {
               elem = (ElemTemplateElement)classObject.newInstance();
            } else if (isUnknownTopLevel) {
               elem = (ElemTemplateElement)classObject.newInstance();
            } else {
               elem = (ElemTemplateElement)classObject.newInstance();
            }

            ((ElemTemplateElement)elem).setDOMBackPointer(handler.getOriginatingNode());
            ((ElemTemplateElement)elem).setLocaterInfo(handler.getLocator());
            ((ElemTemplateElement)elem).setPrefixes(handler.getNamespaceSupport(), excludeXSLDecl);
            if (elem instanceof ElemLiteralResult) {
               ((ElemLiteralResult)elem).setNamespace(uri);
               ((ElemLiteralResult)elem).setLocalName(localName);
               ((ElemLiteralResult)elem).setRawName(rawName);
               ((ElemLiteralResult)elem).setIsLiteralResultAsStylesheet(isLREAsStyleSheet);
            }
         } catch (InstantiationException var21) {
            handler.error("ER_FAILED_CREATING_ELEMLITRSLT", (Object[])null, var21);
         } catch (IllegalAccessException var22) {
            handler.error("ER_FAILED_CREATING_ELEMLITRSLT", (Object[])null, var22);
         }

         this.setPropertiesFromAttributes(handler, rawName, (Attributes)attributes, (ElemTemplateElement)elem);
         if (!isExtension && elem instanceof ElemLiteralResult) {
            isExtension = ((ElemLiteralResult)elem).containsExtensionElementURI(uri);
            if (isExtension) {
               elem = new ElemExtensionCall();
               ((ElemTemplateElement)elem).setLocaterInfo(handler.getLocator());
               ((ElemTemplateElement)elem).setPrefixes(handler.getNamespaceSupport());
               ((ElemLiteralResult)elem).setNamespace(uri);
               ((ElemLiteralResult)elem).setLocalName(localName);
               ((ElemLiteralResult)elem).setRawName(rawName);
               this.setPropertiesFromAttributes(handler, rawName, (Attributes)attributes, (ElemTemplateElement)elem);
            }
         }

         this.appendAndPush(handler, (ElemTemplateElement)elem);
      } catch (TransformerException var25) {
         throw new SAXException(var25);
      }
   }

   protected Stylesheet getStylesheetRoot(StylesheetHandler handler) throws TransformerConfigurationException {
      StylesheetRoot stylesheet = new StylesheetRoot(handler.getSchema(), handler.getStylesheetProcessor().getErrorListener());
      if (handler.getStylesheetProcessor().isSecureProcessing()) {
         stylesheet.setSecureProcessing(true);
      }

      return stylesheet;
   }

   public void endElement(StylesheetHandler handler, String uri, String localName, String rawName) throws SAXException {
      ElemTemplateElement elem = handler.getElemTemplateElement();
      if (elem instanceof ElemLiteralResult && ((ElemLiteralResult)elem).getIsLiteralResultAsStylesheet()) {
         handler.popStylesheet();
      }

      super.endElement(handler, uri, localName, rawName);
   }

   private boolean declaredXSLNS(Stylesheet stylesheet) {
      Vector declaredPrefixes = stylesheet.getDeclaredPrefixes();
      int n = declaredPrefixes.size();

      for(int i = 0; i < n; ++i) {
         XMLNSDecl decl = (XMLNSDecl)declaredPrefixes.elementAt(i);
         if (decl.getURI().equals("http://www.w3.org/1999/XSL/Transform")) {
            return true;
         }
      }

      return false;
   }
}
