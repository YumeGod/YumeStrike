package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.dtd.XMLDTDValidatorFilter;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XML11NSDocumentScannerImpl extends XML11DocumentScannerImpl {
   protected boolean fBindNamespaces;
   protected boolean fPerformValidation;
   private XMLDTDValidatorFilter fDTDValidator;
   private boolean fSawSpace;

   public void setDTDValidator(XMLDTDValidatorFilter var1) {
      this.fDTDValidator = var1;
   }

   protected boolean scanStartElement() throws IOException, XNIException {
      super.fEntityScanner.scanQName(super.fElementQName);
      String var1 = super.fElementQName.rawname;
      if (this.fBindNamespaces) {
         super.fNamespaceContext.pushContext();
         if (super.fScannerState == 6 && this.fPerformValidation) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{var1}, (short)1);
            if (super.fDoctypeName == null || !super.fDoctypeName.equals(var1)) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{super.fDoctypeName, var1}, (short)1);
            }
         }
      }

      super.fCurrentElement = super.fElementStack.pushElement(super.fElementQName);
      boolean var2 = false;
      super.fAttributes.removeAllAttributes();

      int var4;
      while(true) {
         boolean var3 = super.fEntityScanner.skipSpaces();
         var4 = super.fEntityScanner.peekChar();
         if (var4 == 62) {
            super.fEntityScanner.scanChar();
            break;
         }

         if (var4 == 47) {
            super.fEntityScanner.scanChar();
            if (!super.fEntityScanner.skipChar(62)) {
               this.reportFatalError("ElementUnterminated", new Object[]{var1});
            }

            var2 = true;
            break;
         }

         if ((!this.isValidNameStartChar(var4) || !var3) && (!this.isValidNameStartHighSurrogate(var4) || !var3)) {
            this.reportFatalError("ElementUnterminated", new Object[]{var1});
         }

         this.scanAttribute(super.fAttributes);
      }

      if (this.fBindNamespaces) {
         if (super.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{super.fElementQName.rawname}, (short)2);
         }

         String var8 = super.fElementQName.prefix != null ? super.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
         super.fElementQName.uri = super.fNamespaceContext.getURI(var8);
         super.fCurrentElement.uri = super.fElementQName.uri;
         if (super.fElementQName.prefix == null && super.fElementQName.uri != null) {
            super.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
            super.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
         }

         if (super.fElementQName.prefix != null && super.fElementQName.uri == null) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{super.fElementQName.prefix, super.fElementQName.rawname}, (short)2);
         }

         var4 = super.fAttributes.getLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            super.fAttributes.getName(var5, super.fAttributeQName);
            String var6 = super.fAttributeQName.prefix != null ? super.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String var7 = super.fNamespaceContext.getURI(var6);
            if ((super.fAttributeQName.uri == null || super.fAttributeQName.uri != var7) && var6 != XMLSymbols.EMPTY_STRING) {
               super.fAttributeQName.uri = var7;
               if (var7 == null) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{super.fElementQName.rawname, super.fAttributeQName.rawname, var6}, (short)2);
               }

               super.fAttributes.setURI(var5, var7);
            }
         }

         if (var4 > 1) {
            QName var9 = super.fAttributes.checkDuplicatesNS();
            if (var9 != null) {
               if (var9.uri != null) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{super.fElementQName.rawname, var9.localpart, var9.uri}, (short)2);
               } else {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[]{super.fElementQName.rawname, var9.rawname}, (short)2);
               }
            }
         }
      }

      if (super.fDocumentHandler != null) {
         if (var2) {
            --super.fMarkupDepth;
            if (super.fMarkupDepth < super.fEntityStack[super.fEntityDepth - 1]) {
               this.reportFatalError("ElementEntityMismatch", new Object[]{super.fCurrentElement.rawname});
            }

            super.fDocumentHandler.emptyElement(super.fElementQName, super.fAttributes, (Augmentations)null);
            if (this.fBindNamespaces) {
               super.fNamespaceContext.popContext();
            }

            super.fElementStack.popElement(super.fElementQName);
         } else {
            super.fDocumentHandler.startElement(super.fElementQName, super.fAttributes, (Augmentations)null);
         }
      }

      return var2;
   }

   protected void scanStartElementName() throws IOException, XNIException {
      super.fEntityScanner.scanQName(super.fElementQName);
      this.fSawSpace = super.fEntityScanner.skipSpaces();
   }

   protected boolean scanStartElementAfterName() throws IOException, XNIException {
      String var1 = super.fElementQName.rawname;
      if (this.fBindNamespaces) {
         super.fNamespaceContext.pushContext();
         if (super.fScannerState == 6 && this.fPerformValidation) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{var1}, (short)1);
            if (super.fDoctypeName == null || !super.fDoctypeName.equals(var1)) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{super.fDoctypeName, var1}, (short)1);
            }
         }
      }

      super.fCurrentElement = super.fElementStack.pushElement(super.fElementQName);
      boolean var2 = false;
      super.fAttributes.removeAllAttributes();

      while(true) {
         int var3 = super.fEntityScanner.peekChar();
         if (var3 == 62) {
            super.fEntityScanner.scanChar();
            break;
         }

         if (var3 == 47) {
            super.fEntityScanner.scanChar();
            if (!super.fEntityScanner.skipChar(62)) {
               this.reportFatalError("ElementUnterminated", new Object[]{var1});
            }

            var2 = true;
            break;
         }

         if ((!this.isValidNameStartChar(var3) || !this.fSawSpace) && (!this.isValidNameStartHighSurrogate(var3) || !this.fSawSpace)) {
            this.reportFatalError("ElementUnterminated", new Object[]{var1});
         }

         this.scanAttribute(super.fAttributes);
         this.fSawSpace = super.fEntityScanner.skipSpaces();
      }

      if (this.fBindNamespaces) {
         if (super.fElementQName.prefix == XMLSymbols.PREFIX_XMLNS) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{super.fElementQName.rawname}, (short)2);
         }

         String var8 = super.fElementQName.prefix != null ? super.fElementQName.prefix : XMLSymbols.EMPTY_STRING;
         super.fElementQName.uri = super.fNamespaceContext.getURI(var8);
         super.fCurrentElement.uri = super.fElementQName.uri;
         if (super.fElementQName.prefix == null && super.fElementQName.uri != null) {
            super.fElementQName.prefix = XMLSymbols.EMPTY_STRING;
            super.fCurrentElement.prefix = XMLSymbols.EMPTY_STRING;
         }

         if (super.fElementQName.prefix != null && super.fElementQName.uri == null) {
            super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{super.fElementQName.prefix, super.fElementQName.rawname}, (short)2);
         }

         int var4 = super.fAttributes.getLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            super.fAttributes.getName(var5, super.fAttributeQName);
            String var6 = super.fAttributeQName.prefix != null ? super.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String var7 = super.fNamespaceContext.getURI(var6);
            if ((super.fAttributeQName.uri == null || super.fAttributeQName.uri != var7) && var6 != XMLSymbols.EMPTY_STRING) {
               super.fAttributeQName.uri = var7;
               if (var7 == null) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{super.fElementQName.rawname, super.fAttributeQName.rawname, var6}, (short)2);
               }

               super.fAttributes.setURI(var5, var7);
            }
         }

         if (var4 > 1) {
            QName var9 = super.fAttributes.checkDuplicatesNS();
            if (var9 != null) {
               if (var9.uri != null) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{super.fElementQName.rawname, var9.localpart, var9.uri}, (short)2);
               } else {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNotUnique", new Object[]{super.fElementQName.rawname, var9.rawname}, (short)2);
               }
            }
         }
      }

      if (super.fDocumentHandler != null) {
         if (var2) {
            --super.fMarkupDepth;
            if (super.fMarkupDepth < super.fEntityStack[super.fEntityDepth - 1]) {
               this.reportFatalError("ElementEntityMismatch", new Object[]{super.fCurrentElement.rawname});
            }

            super.fDocumentHandler.emptyElement(super.fElementQName, super.fAttributes, (Augmentations)null);
            if (this.fBindNamespaces) {
               super.fNamespaceContext.popContext();
            }

            super.fElementStack.popElement(super.fElementQName);
         } else {
            super.fDocumentHandler.startElement(super.fElementQName, super.fAttributes, (Augmentations)null);
         }
      }

      return var2;
   }

   protected void scanAttribute(XMLAttributesImpl var1) throws IOException, XNIException {
      super.fEntityScanner.scanQName(super.fAttributeQName);
      super.fEntityScanner.skipSpaces();
      if (!super.fEntityScanner.skipChar(61)) {
         this.reportFatalError("EqRequiredInAttribute", new Object[]{super.fCurrentElement.rawname, super.fAttributeQName.rawname});
      }

      super.fEntityScanner.skipSpaces();
      int var2;
      if (this.fBindNamespaces) {
         var2 = var1.getLength();
         var1.addAttributeNS(super.fAttributeQName, XMLSymbols.fCDATASymbol, (String)null);
      } else {
         int var3 = var1.getLength();
         var2 = var1.addAttribute(super.fAttributeQName, XMLSymbols.fCDATASymbol, (String)null);
         if (var3 == var1.getLength()) {
            this.reportFatalError("AttributeNotUnique", new Object[]{super.fCurrentElement.rawname, super.fAttributeQName.rawname});
         }
      }

      boolean var9 = super.fHasExternalDTD && !super.fStandalone;
      boolean var4 = this.scanAttributeValue(super.fTempString, super.fTempString2, super.fAttributeQName.rawname, var9, super.fCurrentElement.rawname);
      String var5 = super.fTempString.toString();
      var1.setValue(var2, var5);
      if (!var4) {
         var1.setNonNormalizedValue(var2, super.fTempString2.toString());
      }

      var1.setSpecified(var2, true);
      if (this.fBindNamespaces) {
         String var6 = super.fAttributeQName.localpart;
         String var7 = super.fAttributeQName.prefix != null ? super.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
         if (var7 != XMLSymbols.PREFIX_XMLNS && (var7 != XMLSymbols.EMPTY_STRING || var6 != XMLSymbols.PREFIX_XMLNS)) {
            if (super.fAttributeQName.prefix != null) {
               var1.setURI(var2, super.fNamespaceContext.getURI(super.fAttributeQName.prefix));
            }
         } else {
            String var8 = super.fSymbolTable.addSymbol(var5);
            if (var7 == XMLSymbols.PREFIX_XMLNS && var6 == XMLSymbols.PREFIX_XMLNS) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{super.fAttributeQName}, (short)2);
            }

            if (var8 == NamespaceContext.XMLNS_URI) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{super.fAttributeQName}, (short)2);
            }

            if (var6 == XMLSymbols.PREFIX_XML) {
               if (var8 != NamespaceContext.XML_URI) {
                  super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{super.fAttributeQName}, (short)2);
               }
            } else if (var8 == NamespaceContext.XML_URI) {
               super.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{super.fAttributeQName}, (short)2);
            }

            var7 = var6 != XMLSymbols.PREFIX_XMLNS ? var6 : XMLSymbols.EMPTY_STRING;
            super.fNamespaceContext.declarePrefix(var7, var8.length() != 0 ? var8 : null);
            var1.setURI(var2, super.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS));
         }
      }

   }

   protected int scanEndElement() throws IOException, XNIException {
      super.fElementStack.popElement(super.fElementQName);
      if (!super.fEntityScanner.skipString(super.fElementQName.rawname)) {
         this.reportFatalError("ETagRequired", new Object[]{super.fElementQName.rawname});
      }

      super.fEntityScanner.skipSpaces();
      if (!super.fEntityScanner.skipChar(62)) {
         this.reportFatalError("ETagUnterminated", new Object[]{super.fElementQName.rawname});
      }

      --super.fMarkupDepth;
      --super.fMarkupDepth;
      if (super.fMarkupDepth < super.fEntityStack[super.fEntityDepth - 1]) {
         this.reportFatalError("ElementEntityMismatch", new Object[]{super.fCurrentElement.rawname});
      }

      if (super.fDocumentHandler != null) {
         super.fDocumentHandler.endElement(super.fElementQName, (Augmentations)null);
         if (this.fBindNamespaces) {
            super.fNamespaceContext.popContext();
         }
      }

      return super.fMarkupDepth;
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      super.reset(var1);
      this.fPerformValidation = false;
      this.fBindNamespaces = false;
   }

   protected XMLDocumentFragmentScannerImpl.Dispatcher createContentDispatcher() {
      return new NS11ContentDispatcher();
   }

   protected final class NS11ContentDispatcher extends XMLDocumentScannerImpl.ContentDispatcher {
      protected NS11ContentDispatcher() {
         super();
      }

      protected boolean scanRootElementHook() throws IOException, XNIException {
         if (XML11NSDocumentScannerImpl.super.fExternalSubsetResolver != null && !XML11NSDocumentScannerImpl.super.fSeenDoctypeDecl && !XML11NSDocumentScannerImpl.super.fDisallowDoctype && (XML11NSDocumentScannerImpl.super.fValidation || XML11NSDocumentScannerImpl.super.fLoadExternalDTD)) {
            XML11NSDocumentScannerImpl.this.scanStartElementName();
            this.resolveExternalSubsetAndRead();
            this.reconfigurePipeline();
            if (XML11NSDocumentScannerImpl.this.scanStartElementAfterName()) {
               XML11NSDocumentScannerImpl.this.setScannerState(12);
               XML11NSDocumentScannerImpl.this.setDispatcher(XML11NSDocumentScannerImpl.super.fTrailingMiscDispatcher);
               return true;
            }
         } else {
            this.reconfigurePipeline();
            if (XML11NSDocumentScannerImpl.this.scanStartElement()) {
               XML11NSDocumentScannerImpl.this.setScannerState(12);
               XML11NSDocumentScannerImpl.this.setDispatcher(XML11NSDocumentScannerImpl.super.fTrailingMiscDispatcher);
               return true;
            }
         }

         return false;
      }

      private void reconfigurePipeline() {
         if (XML11NSDocumentScannerImpl.this.fDTDValidator == null) {
            XML11NSDocumentScannerImpl.this.fBindNamespaces = true;
         } else if (!XML11NSDocumentScannerImpl.this.fDTDValidator.hasGrammar()) {
            XML11NSDocumentScannerImpl.this.fBindNamespaces = true;
            XML11NSDocumentScannerImpl.this.fPerformValidation = XML11NSDocumentScannerImpl.this.fDTDValidator.validate();
            XMLDocumentSource var1 = XML11NSDocumentScannerImpl.this.fDTDValidator.getDocumentSource();
            XMLDocumentHandler var2 = XML11NSDocumentScannerImpl.this.fDTDValidator.getDocumentHandler();
            var1.setDocumentHandler(var2);
            if (var2 != null) {
               var2.setDocumentSource(var1);
            }

            XML11NSDocumentScannerImpl.this.fDTDValidator.setDocumentSource((XMLDocumentSource)null);
            XML11NSDocumentScannerImpl.this.fDTDValidator.setDocumentHandler((XMLDocumentHandler)null);
         }

      }
   }
}
