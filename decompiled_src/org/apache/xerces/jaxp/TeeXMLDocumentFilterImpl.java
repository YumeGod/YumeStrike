package org.apache.xerces.jaxp;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

class TeeXMLDocumentFilterImpl implements XMLDocumentFilter {
   private XMLDocumentHandler next;
   private XMLDocumentHandler side;
   private XMLDocumentSource source;

   public XMLDocumentHandler getSide() {
      return this.side;
   }

   public void setSide(XMLDocumentHandler var1) {
      this.side = var1;
   }

   public XMLDocumentSource getDocumentSource() {
      return this.source;
   }

   public void setDocumentSource(XMLDocumentSource var1) {
      this.source = var1;
   }

   public XMLDocumentHandler getDocumentHandler() {
      return this.next;
   }

   public void setDocumentHandler(XMLDocumentHandler var1) {
      this.next = var1;
   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      this.side.characters(var1, var2);
      this.next.characters(var1, var2);
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      this.side.comment(var1, var2);
      this.next.comment(var1, var2);
   }

   public void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.side.doctypeDecl(var1, var2, var3, var4);
      this.next.doctypeDecl(var1, var2, var3, var4);
   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.side.emptyElement(var1, var2, var3);
      this.next.emptyElement(var1, var2, var3);
   }

   public void endCDATA(Augmentations var1) throws XNIException {
      this.side.endCDATA(var1);
      this.next.endCDATA(var1);
   }

   public void endDocument(Augmentations var1) throws XNIException {
      this.side.endDocument(var1);
      this.next.endDocument(var1);
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      this.side.endElement(var1, var2);
      this.next.endElement(var1, var2);
   }

   public void endGeneralEntity(String var1, Augmentations var2) throws XNIException {
      this.side.endGeneralEntity(var1, var2);
      this.next.endGeneralEntity(var1, var2);
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      this.side.ignorableWhitespace(var1, var2);
      this.next.ignorableWhitespace(var1, var2);
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      this.side.processingInstruction(var1, var2, var3);
      this.next.processingInstruction(var1, var2, var3);
   }

   public void startCDATA(Augmentations var1) throws XNIException {
      this.side.startCDATA(var1);
      this.next.startCDATA(var1);
   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.side.startDocument(var1, var2, var3, var4);
      this.next.startDocument(var1, var2, var3, var4);
   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      this.side.startElement(var1, var2, var3);
      this.next.startElement(var1, var2, var3);
   }

   public void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      this.side.startGeneralEntity(var1, var2, var3, var4);
      this.next.startGeneralEntity(var1, var2, var3, var4);
   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      this.side.textDecl(var1, var2, var3);
      this.next.textDecl(var1, var2, var3);
   }

   public void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException {
      this.side.xmlDecl(var1, var2, var3, var4);
      this.next.xmlDecl(var1, var2, var3, var4);
   }
}
