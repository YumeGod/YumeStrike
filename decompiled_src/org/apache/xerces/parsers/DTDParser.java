package org.apache.xerces.parsers;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDTDSource;

public abstract class DTDParser extends XMLGrammarParser implements XMLDTDHandler, XMLDTDContentModelHandler {
   protected XMLDTDScanner fDTDScanner;

   public DTDParser(SymbolTable var1) {
      super(var1);
   }

   public DTDGrammar getDTDGrammar() {
      return null;
   }

   public void startEntity(String var1, String var2, String var3, String var4) throws XNIException {
   }

   public void textDecl(String var1, String var2) throws XNIException {
   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
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

   public void endConditional(Augmentations var1) throws XNIException {
   }

   public void endDTD(Augmentations var1) throws XNIException {
   }

   public void endEntity(String var1, Augmentations var2) throws XNIException {
   }

   public void startContentModel(String var1, short var2) throws XNIException {
   }

   public void mixedElement(String var1) throws XNIException {
   }

   public void childrenStartGroup() throws XNIException {
   }

   public void childrenElement(String var1) throws XNIException {
   }

   public void childrenSeparator(short var1) throws XNIException {
   }

   public void childrenOccurrence(short var1) throws XNIException {
   }

   public void childrenEndGroup() throws XNIException {
   }

   public void endContentModel() throws XNIException {
   }

   public abstract XMLDTDSource getDTDSource();

   public abstract void setDTDSource(XMLDTDSource var1);

   public abstract void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException;

   public abstract void endParameterEntity(String var1, Augmentations var2) throws XNIException;

   public abstract void textDecl(String var1, String var2, Augmentations var3) throws XNIException;

   public abstract void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

   public abstract XMLDTDContentModelSource getDTDContentModelSource();

   public abstract void setDTDContentModelSource(XMLDTDContentModelSource var1);

   public abstract void endContentModel(Augmentations var1) throws XNIException;

   public abstract void endGroup(Augmentations var1) throws XNIException;

   public abstract void occurrence(short var1, Augmentations var2) throws XNIException;

   public abstract void separator(short var1, Augmentations var2) throws XNIException;

   public abstract void element(String var1, Augmentations var2) throws XNIException;

   public abstract void pcdata(Augmentations var1) throws XNIException;

   public abstract void startGroup(Augmentations var1) throws XNIException;

   public abstract void empty(Augmentations var1) throws XNIException;

   public abstract void any(Augmentations var1) throws XNIException;

   public abstract void startContentModel(String var1, Augmentations var2) throws XNIException;
}
