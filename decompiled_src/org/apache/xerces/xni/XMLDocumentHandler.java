package org.apache.xerces.xni;

import org.apache.xerces.xni.parser.XMLDocumentSource;

public interface XMLDocumentHandler {
   void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException;

   void xmlDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException;

   void doctypeDecl(String var1, String var2, String var3, Augmentations var4) throws XNIException;

   void comment(XMLString var1, Augmentations var2) throws XNIException;

   void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException;

   void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException;

   void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException;

   void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

   void textDecl(String var1, String var2, Augmentations var3) throws XNIException;

   void endGeneralEntity(String var1, Augmentations var2) throws XNIException;

   void characters(XMLString var1, Augmentations var2) throws XNIException;

   void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException;

   void endElement(QName var1, Augmentations var2) throws XNIException;

   void startCDATA(Augmentations var1) throws XNIException;

   void endCDATA(Augmentations var1) throws XNIException;

   void endDocument(Augmentations var1) throws XNIException;

   void setDocumentSource(XMLDocumentSource var1);

   XMLDocumentSource getDocumentSource();
}
