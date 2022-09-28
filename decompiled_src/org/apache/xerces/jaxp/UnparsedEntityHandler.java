package org.apache.xerces.jaxp;

import java.util.HashMap;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDFilter;
import org.apache.xerces.xni.parser.XMLDTDSource;

final class UnparsedEntityHandler implements XMLDTDFilter, EntityState {
   private XMLDTDSource fDTDSource;
   private XMLDTDHandler fDTDHandler;
   private final ValidationManager fValidationManager;
   private HashMap fUnparsedEntities = null;

   UnparsedEntityHandler(ValidationManager var1) {
      this.fValidationManager = var1;
   }

   public void startDTD(XMLLocator var1, Augmentations var2) throws XNIException {
      this.fValidationManager.setEntityState(this);
      if (this.fUnparsedEntities != null && !this.fUnparsedEntities.isEmpty()) {
         this.fUnparsedEntities.clear();
      }

      if (this.fDTDHandler != null) {
         this.fDTDHandler.startDTD(var1, var2);
      }

   }

   public void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startParameterEntity(var1, var2, var3, var4);
      }

   }

   public void textDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.textDecl(var1, var2, var3);
      }

   }

   public void endParameterEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endParameterEntity(var1, var2);
      }

   }

   public void startExternalSubset(XMLResourceIdentifier var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startExternalSubset(var1, var2);
      }

   }

   public void endExternalSubset(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endExternalSubset(var1);
      }

   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.comment(var1, var2);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.processingInstruction(var1, var2, var3);
      }

   }

   public void elementDecl(String var1, String var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.elementDecl(var1, var2, var3);
      }

   }

   public void startAttlist(String var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startAttlist(var1, var2);
      }

   }

   public void attributeDecl(String var1, String var2, String var3, String[] var4, String var5, XMLString var6, XMLString var7, Augmentations var8) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.attributeDecl(var1, var2, var3, var4, var5, var6, var7, var8);
      }

   }

   public void endAttlist(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endAttlist(var1);
      }

   }

   public void internalEntityDecl(String var1, XMLString var2, XMLString var3, Augmentations var4) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.internalEntityDecl(var1, var2, var3, var4);
      }

   }

   public void externalEntityDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.externalEntityDecl(var1, var2, var3);
      }

   }

   public void unparsedEntityDecl(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (this.fUnparsedEntities == null) {
         this.fUnparsedEntities = new HashMap();
      }

      this.fUnparsedEntities.put(var1, var1);
      if (this.fDTDHandler != null) {
         this.fDTDHandler.unparsedEntityDecl(var1, var2, var3, var4);
      }

   }

   public void notationDecl(String var1, XMLResourceIdentifier var2, Augmentations var3) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.notationDecl(var1, var2, var3);
      }

   }

   public void startConditional(short var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.startConditional(var1, var2);
      }

   }

   public void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.ignoredCharacters(var1, var2);
      }

   }

   public void endConditional(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endConditional(var1);
      }

   }

   public void endDTD(Augmentations var1) throws XNIException {
      if (this.fDTDHandler != null) {
         this.fDTDHandler.endDTD(var1);
      }

   }

   public void setDTDSource(XMLDTDSource var1) {
      this.fDTDSource = var1;
   }

   public XMLDTDSource getDTDSource() {
      return this.fDTDSource;
   }

   public void setDTDHandler(XMLDTDHandler var1) {
      this.fDTDHandler = var1;
   }

   public XMLDTDHandler getDTDHandler() {
      return this.fDTDHandler;
   }

   public boolean isEntityDeclared(String var1) {
      return false;
   }

   public boolean isEntityUnparsed(String var1) {
      return this.fUnparsedEntities != null ? this.fUnparsedEntities.containsKey(var1) : false;
   }
}
