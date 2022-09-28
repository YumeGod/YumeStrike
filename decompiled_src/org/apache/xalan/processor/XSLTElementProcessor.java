package org.apache.xalan.processor;

import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.IntStack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XSLTElementProcessor extends ElemTemplateElement {
   static final long serialVersionUID = 5597421564955304421L;
   private IntStack m_savedLastOrder;
   private XSLTElementDef m_elemDef;

   XSLTElementProcessor() {
   }

   XSLTElementDef getElemDef() {
      return this.m_elemDef;
   }

   void setElemDef(XSLTElementDef def) {
      this.m_elemDef = def;
   }

   public InputSource resolveEntity(StylesheetHandler handler, String publicId, String systemId) throws SAXException {
      return null;
   }

   public void notationDecl(StylesheetHandler handler, String name, String publicId, String systemId) {
   }

   public void unparsedEntityDecl(StylesheetHandler handler, String name, String publicId, String systemId, String notationName) {
   }

   public void startNonText(StylesheetHandler handler) throws SAXException {
   }

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      if (this.m_savedLastOrder == null) {
         this.m_savedLastOrder = new IntStack();
      }

      this.m_savedLastOrder.push(this.getElemDef().getLastOrder());
      this.getElemDef().setLastOrder(-1);
   }

   public void endElement(StylesheetHandler handler, String uri, String localName, String rawName) throws SAXException {
      if (this.m_savedLastOrder != null && !this.m_savedLastOrder.empty()) {
         this.getElemDef().setLastOrder(this.m_savedLastOrder.pop());
      }

      if (!this.getElemDef().getRequiredFound()) {
         handler.error("ER_REQUIRED_ELEM_NOT_FOUND", new Object[]{this.getElemDef().getRequiredElem()}, (Exception)null);
      }

   }

   public void characters(StylesheetHandler handler, char[] ch, int start, int length) throws SAXException {
      handler.error("ER_CHARS_NOT_ALLOWED", (Object[])null, (Exception)null);
   }

   public void ignorableWhitespace(StylesheetHandler handler, char[] ch, int start, int length) throws SAXException {
   }

   public void processingInstruction(StylesheetHandler handler, String target, String data) throws SAXException {
   }

   public void skippedEntity(StylesheetHandler handler, String name) throws SAXException {
   }

   void setPropertiesFromAttributes(StylesheetHandler handler, String rawName, Attributes attributes, ElemTemplateElement target) throws SAXException {
      this.setPropertiesFromAttributes(handler, rawName, attributes, target, true);
   }

   Attributes setPropertiesFromAttributes(StylesheetHandler handler, String rawName, Attributes attributes, ElemTemplateElement target, boolean throwError) throws SAXException {
      XSLTElementDef def = this.getElemDef();
      AttributesImpl undefines = null;
      boolean isCompatibleMode = null != handler.getStylesheet() && handler.getStylesheet().getCompatibleMode() || !throwError;
      if (isCompatibleMode) {
         undefines = new AttributesImpl();
      }

      Vector processedDefs = new Vector();
      Vector errorDefs = new Vector();
      int nAttrs = attributes.getLength();

      for(int i = 0; i < nAttrs; ++i) {
         String attrUri = attributes.getURI(i);
         if (null != attrUri && attrUri.length() == 0 && (attributes.getQName(i).startsWith("xmlns:") || attributes.getQName(i).equals("xmlns"))) {
            attrUri = "http://www.w3.org/XML/1998/namespace";
         }

         String attrLocalName = attributes.getLocalName(i);
         XSLTAttributeDef attrDef = def.getAttributeDef(attrUri, attrLocalName);
         if (null == attrDef) {
            if (!isCompatibleMode) {
               handler.error("ER_ATTR_NOT_ALLOWED", new Object[]{attributes.getQName(i), rawName}, (Exception)null);
            } else {
               undefines.addAttribute(attrUri, attrLocalName, attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
            }
         } else {
            boolean success = attrDef.setAttrValue(handler, attrUri, attrLocalName, attributes.getQName(i), attributes.getValue(i), target);
            if (success) {
               processedDefs.addElement(attrDef);
            } else {
               errorDefs.addElement(attrDef);
            }
         }
      }

      XSLTAttributeDef[] attrDefs = def.getAttributes();
      int nAttrDefs = attrDefs.length;

      for(int i = 0; i < nAttrDefs; ++i) {
         XSLTAttributeDef attrDef = attrDefs[i];
         String defVal = attrDef.getDefault();
         if (null != defVal && !processedDefs.contains(attrDef)) {
            attrDef.setDefAttrValue(handler, target);
         }

         if (attrDef.getRequired() && !processedDefs.contains(attrDef) && !errorDefs.contains(attrDef)) {
            handler.error(XSLMessages.createMessage("ER_REQUIRES_ATTRIB", new Object[]{rawName, attrDef.getName()}), (Exception)null);
         }
      }

      return undefines;
   }
}
