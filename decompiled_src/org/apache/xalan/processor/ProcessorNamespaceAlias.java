package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.NamespaceAlias;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorNamespaceAlias extends XSLTElementProcessor {
   static final long serialVersionUID = -6309867839007018964L;

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      NamespaceAlias na = new NamespaceAlias(handler.nextUid());
      this.setPropertiesFromAttributes(handler, rawName, attributes, na);
      String prefix = na.getStylesheetPrefix();
      if (prefix.equals("#default")) {
         prefix = "";
         na.setStylesheetPrefix(prefix);
      }

      String stylesheetNS = handler.getNamespaceForPrefix(prefix);
      na.setStylesheetNamespace(stylesheetNS);
      prefix = na.getResultPrefix();
      String resultNS;
      if (prefix.equals("#default")) {
         prefix = "";
         na.setResultPrefix(prefix);
         resultNS = handler.getNamespaceForPrefix(prefix);
         if (null == resultNS) {
            handler.error("ER_INVALID_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX_FOR_DEFAULT", (Object[])null, (Exception)null);
         }
      } else {
         resultNS = handler.getNamespaceForPrefix(prefix);
         if (null == resultNS) {
            handler.error("ER_INVALID_SET_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX", new Object[]{prefix}, (Exception)null);
         }
      }

      na.setResultNamespace(resultNS);
      handler.getStylesheet().setNamespaceAlias(na);
      handler.getStylesheet().appendChild((ElemTemplateElement)na);
   }
}
