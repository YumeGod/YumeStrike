package org.apache.xerces.xni;

import org.apache.xerces.xni.parser.XMLDTDSource;

public interface XMLDTDHandler {
   short CONDITIONAL_INCLUDE = 0;
   short CONDITIONAL_IGNORE = 1;

   void startDTD(XMLLocator var1, Augmentations var2) throws XNIException;

   void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

   void textDecl(String var1, String var2, Augmentations var3) throws XNIException;

   void endParameterEntity(String var1, Augmentations var2) throws XNIException;

   void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException;

   void endExternalSubset(Augmentations var1) throws XNIException;

   void comment(XMLString var1, Augmentations var2) throws XNIException;

   void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException;

   void elementDecl(String var1, String var2, Augmentations var3) throws XNIException;

   void startAttlist(String var1, Augmentations var2) throws XNIException;

   void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException;

   void endAttlist(Augmentations var1) throws XNIException;

   void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException;

   void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException;

   void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

   void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException;

   void startConditional(short var1, Augmentations var2) throws XNIException;

   void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException;

   void endConditional(Augmentations var1) throws XNIException;

   void endDTD(Augmentations var1) throws XNIException;

   void setDTDSource(XMLDTDSource var1);

   XMLDTDSource getDTDSource();
}
