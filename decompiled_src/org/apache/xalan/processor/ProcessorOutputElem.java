package org.apache.xalan.processor;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorOutputElem extends XSLTElementProcessor {
   static final long serialVersionUID = 3513742319582547590L;
   private OutputProperties m_outputProperties;

   public void setCdataSectionElements(Vector newValue) {
      this.m_outputProperties.setQNameProperties("cdata-section-elements", newValue);
   }

   public void setDoctypePublic(String newValue) {
      this.m_outputProperties.setProperty("doctype-public", newValue);
   }

   public void setDoctypeSystem(String newValue) {
      this.m_outputProperties.setProperty("doctype-system", newValue);
   }

   public void setEncoding(String newValue) {
      this.m_outputProperties.setProperty("encoding", newValue);
   }

   public void setIndent(boolean newValue) {
      this.m_outputProperties.setBooleanProperty("indent", newValue);
   }

   public void setMediaType(String newValue) {
      this.m_outputProperties.setProperty("media-type", newValue);
   }

   public void setMethod(QName newValue) {
      this.m_outputProperties.setQNameProperty("method", newValue);
   }

   public void setOmitXmlDeclaration(boolean newValue) {
      this.m_outputProperties.setBooleanProperty("omit-xml-declaration", newValue);
   }

   public void setStandalone(boolean newValue) {
      this.m_outputProperties.setBooleanProperty("standalone", newValue);
   }

   public void setVersion(String newValue) {
      this.m_outputProperties.setProperty("version", newValue);
   }

   public void setForeignAttr(String attrUri, String attrLocalName, String attrRawName, String attrValue) {
      QName key = new QName(attrUri, attrLocalName);
      this.m_outputProperties.setProperty(key, attrValue);
   }

   public void addLiteralResultAttribute(String attrUri, String attrLocalName, String attrRawName, String attrValue) {
      QName key = new QName(attrUri, attrLocalName);
      this.m_outputProperties.setProperty(key, attrValue);
   }

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      this.m_outputProperties = new OutputProperties();
      this.m_outputProperties.setDOMBackPointer(handler.getOriginatingNode());
      this.m_outputProperties.setLocaterInfo(handler.getLocator());
      this.m_outputProperties.setUid(handler.nextUid());
      this.setPropertiesFromAttributes(handler, rawName, attributes, this);
      String entitiesFileName = (String)this.m_outputProperties.getProperties().get("{http://xml.apache.org/xalan}entities");
      if (null != entitiesFileName) {
         try {
            String absURL = SystemIDResolver.getAbsoluteURI(entitiesFileName, handler.getBaseIdentifier());
            this.m_outputProperties.getProperties().put("{http://xml.apache.org/xalan}entities", absURL);
         } catch (TransformerException var8) {
            handler.error(var8.getMessage(), var8);
         }
      }

      handler.getStylesheet().setOutput(this.m_outputProperties);
      ElemTemplateElement parent = handler.getElemTemplateElement();
      parent.appendChild((ElemTemplateElement)this.m_outputProperties);
      this.m_outputProperties = null;
   }
}
