package org.apache.xerces.impl.xs.opti;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDSource;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class DefaultXMLDocumentHandler implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler {
   private XMLDocumentSource fDocumentSource;
   private XMLDTDSource fDTDSource;
   private XMLDTDContentModelSource fCMSource;

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
   }

   public void startPrefixMapping(String var1, String var2, Augmentations var3) throws XNIException {
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
   }

   public void endPrefixMapping(String var1, Augmentations var2) throws XNIException {
   }

   public void startCDATA(Augmentations var1) throws XNIException {
   }

   public void endCDATA(Augmentations var1) throws XNIException {
   }

   public void endDocument(Augmentations var1) throws XNIException {
   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
   }

   public void startAttlist(String var1, Augmentations var2) throws XNIException {
   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
   }

   public void endAttlist(Augmentations var1) throws XNIException {
   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
   }

   public void startConditional(short var1, Augmentations var2) throws XNIException {
   }

   public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void endConditional(Augmentations var1) throws XNIException {
   }

   public void endDTD(Augmentations var1) throws XNIException {
   }

   public void startContentModel(String var1, Augmentations var2) throws XNIException {
   }

   public void any(Augmentations var1) throws XNIException {
   }

   public void empty(Augmentations var1) throws XNIException {
   }

   public void startGroup(Augmentations var1) throws XNIException {
   }

   public void pcdata(Augmentations var1) throws XNIException {
   }

   public void element(String var1, Augmentations var2) throws XNIException {
   }

   public void separator(short var1, Augmentations var2) throws XNIException {
   }

   public void occurrence(short var1, Augmentations var2) throws XNIException {
   }

   public void endGroup(Augmentations var1) throws XNIException {
   }

   public void endContentModel(Augmentations var1) throws XNIException {
   }

   public void setDocumentSource(XMLDocumentSource var1) {
      this.fDocumentSource = var1;
   }

   public XMLDocumentSource getDocumentSource() {
      return this.fDocumentSource;
   }

   public void setDTDSource(XMLDTDSource var1) {
      this.fDTDSource = var1;
   }

   public XMLDTDSource getDTDSource() {
      return this.fDTDSource;
   }

   public void setDTDContentModelSource(XMLDTDContentModelSource var1) {
      this.fCMSource = var1;
   }

   public XMLDTDContentModelSource getDTDContentModelSource() {
      return this.fCMSource;
   }
}
