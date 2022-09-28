package org.apache.xerces.xni;

public interface XMLDocumentFragmentHandler {
   void startDocumentFragment(XMLLocator var1, NamespaceContext var2, Augmentations var3) throws XNIException;

   void startGeneralEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

   void textDecl(String var1, String var2, Augmentations var3) throws XNIException;

   void endGeneralEntity(String var1, Augmentations var2) throws XNIException;

   void comment(XMLString var1, Augmentations var2) throws XNIException;

   void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException;

   void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException;

   void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException;

   void characters(XMLString var1, Augmentations var2) throws XNIException;

   void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException;

   void endElement(QName var1, Augmentations var2) throws XNIException;

   void startCDATA(Augmentations var1) throws XNIException;

   void endCDATA(Augmentations var1) throws XNIException;

   void endDocumentFragment(Augmentations var1) throws XNIException;
}
