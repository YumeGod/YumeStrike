package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentScanner;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLDocumentFragmentScannerImpl extends XMLScanner implements XMLDocumentScanner, XMLComponent, XMLEntityHandler {
   protected static final int SCANNER_STATE_START_OF_MARKUP = 1;
   protected static final int SCANNER_STATE_COMMENT = 2;
   protected static final int SCANNER_STATE_PI = 3;
   protected static final int SCANNER_STATE_DOCTYPE = 4;
   protected static final int SCANNER_STATE_ROOT_ELEMENT = 6;
   protected static final int SCANNER_STATE_CONTENT = 7;
   protected static final int SCANNER_STATE_REFERENCE = 8;
   protected static final int SCANNER_STATE_END_OF_INPUT = 13;
   protected static final int SCANNER_STATE_TERMINATED = 14;
   protected static final int SCANNER_STATE_CDATA = 15;
   protected static final int SCANNER_STATE_TEXT_DECL = 16;
   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
   private static final String[] RECOGNIZED_FEATURES = new String[]{"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs"};
   private static final Boolean[] FEATURE_DEFAULTS;
   private static final String[] RECOGNIZED_PROPERTIES;
   private static final Object[] PROPERTY_DEFAULTS;
   private static final boolean DEBUG_SCANNER_STATE = false;
   private static final boolean DEBUG_DISPATCHER = false;
   protected static final boolean DEBUG_CONTENT_SCANNING = false;
   protected XMLDocumentHandler fDocumentHandler;
   protected int[] fEntityStack = new int[4];
   protected int fMarkupDepth;
   protected int fScannerState;
   protected boolean fInScanContent = false;
   protected boolean fHasExternalDTD;
   protected boolean fStandalone;
   protected ExternalSubsetResolver fExternalSubsetResolver;
   protected QName fCurrentElement;
   protected ElementStack fElementStack = new ElementStack();
   protected boolean fNotifyBuiltInRefs = false;
   protected Dispatcher fDispatcher;
   protected Dispatcher fContentDispatcher = this.createContentDispatcher();
   protected final QName fElementQName = new QName();
   protected final QName fAttributeQName = new QName();
   protected final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
   protected final XMLString fTempString = new XMLString();
   protected final XMLString fTempString2 = new XMLString();
   private final String[] fStrings = new String[3];
   private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
   private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
   private final QName fQName = new QName();
   private final char[] fSingleChar = new char[1];
   private final XMLEntityManager.ExternalEntity fExternalEntity = new XMLEntityManager.ExternalEntity();
   private boolean fSawSpace;
   private Augmentations fTempAugmentations = null;

   public void setInputSource(XMLInputSource var1) throws IOException {
      super.fEntityManager.setEntityHandler(this);
      super.fEntityManager.startEntity("$fragment$", var1, false, true);
   }

   public boolean scanDocument(boolean var1) throws IOException, XNIException {
      super.fEntityScanner = super.fEntityManager.getEntityScanner();
      super.fEntityManager.setEntityHandler(this);

      while(this.fDispatcher.dispatch(var1)) {
         if (!var1) {
            return true;
         }
      }

      return false;
   }

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      super.reset(var1);
      this.fAttributes.setNamespaces(super.fNamespaces);
      this.fMarkupDepth = 0;
      this.fCurrentElement = null;
      this.fElementStack.clear();
      this.fHasExternalDTD = false;
      this.fStandalone = false;
      this.fInScanContent = false;
      this.setScannerState(7);
      this.setDispatcher(this.fContentDispatcher);
      if (super.fParserSettings) {
         try {
            this.fNotifyBuiltInRefs = var1.getFeature("http://apache.org/xml/features/scanner/notify-builtin-refs");
         } catch (XMLConfigurationException var4) {
            this.fNotifyBuiltInRefs = false;
         }

         try {
            Object var2 = var1.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            this.fExternalSubsetResolver = var2 instanceof ExternalSubsetResolver ? (ExternalSubsetResolver)var2 : null;
         } catch (XMLConfigurationException var3) {
            this.fExternalSubsetResolver = null;
         }
      }

   }

   public String[] getRecognizedFeatures() {
      return (String[])RECOGNIZED_FEATURES.clone();
   }

   public void setFeature(String var1, boolean var2) throws XMLConfigurationException {
      super.setFeature(var1, var2);
      if (var1.startsWith("http://apache.org/xml/features/")) {
         int var3 = var1.length() - "http://apache.org/xml/features/".length();
         if (var3 == "scanner/notify-builtin-refs".length() && var1.endsWith("scanner/notify-builtin-refs")) {
            this.fNotifyBuiltInRefs = var2;
         }
      }

   }

   public String[] getRecognizedProperties() {
      return (String[])RECOGNIZED_PROPERTIES.clone();
   }

   public void setProperty(String var1, Object var2) throws XMLConfigurationException {
      super.setProperty(var1, var2);
      if (var1.startsWith("http://apache.org/xml/properties/")) {
         int var3 = var1.length() - "http://apache.org/xml/properties/".length();
         if (var3 == "internal/entity-manager".length() && var1.endsWith("internal/entity-manager")) {
            super.fEntityManager = (XMLEntityManager)var2;
            return;
         }

         if (var3 == "internal/entity-resolver".length() && var1.endsWith("internal/entity-resolver")) {
            this.fExternalSubsetResolver = var2 instanceof ExternalSubsetResolver ? (ExternalSubsetResolver)var2 : null;
            return;
         }
      }

   }

   public Boolean getFeatureDefault(String var1) {
      for(int var2 = 0; var2 < RECOGNIZED_FEATURES.length; ++var2) {
         if (RECOGNIZED_FEATURES[var2].equals(var1)) {
            return FEATURE_DEFAULTS[var2];
         }
      }

      return null;
   }

   public Object getPropertyDefault(String var1) {
      for(int var2 = 0; var2 < RECOGNIZED_PROPERTIES.length; ++var2) {
         if (RECOGNIZED_PROPERTIES[var2].equals(var1)) {
            return PROPERTY_DEFAULTS[var2];
         }
      }

      return null;
   }

   public void setDocumentHandler(XMLDocumentHandler var1) {
      this.fDocumentHandler = var1;
   }

   public XMLDocumentHandler getDocumentHandler() {
      return this.fDocumentHandler;
   }

   public void startEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException {
      if (super.fEntityDepth == this.fEntityStack.length) {
         int[] var5 = new int[this.fEntityStack.length * 2];
         System.arraycopy(this.fEntityStack, 0, var5, 0, this.fEntityStack.length);
         this.fEntityStack = var5;
      }

      this.fEntityStack[super.fEntityDepth] = this.fMarkupDepth;
      super.startEntity(var1, var2, var3, var4);
      if (this.fStandalone && super.fEntityManager.isEntityDeclInExternalSubset(var1)) {
         this.reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{var1});
      }

      if (this.fDocumentHandler != null && !super.fScanningAttribute && !var1.equals("[xml]")) {
         this.fDocumentHandler.startGeneralEntity(var1, var2, var3, var4);
      }

   }

   public void endEntity(String var1, Augmentations var2) throws XNIException {
      if (this.fInScanContent && this.fStringBuffer.length != 0 && this.fDocumentHandler != null) {
         this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
         this.fStringBuffer.length = 0;
      }

      super.endEntity(var1, var2);
      if (this.fMarkupDepth != this.fEntityStack[super.fEntityDepth]) {
         this.reportFatalError("MarkupEntityMismatch", (Object[])null);
      }

      if (this.fDocumentHandler != null && !super.fScanningAttribute && !var1.equals("[xml]")) {
         this.fDocumentHandler.endGeneralEntity(var1, var2);
      }

   }

   protected Dispatcher createContentDispatcher() {
      return new FragmentContentDispatcher();
   }

   protected void scanXMLDeclOrTextDecl(boolean var1) throws IOException, XNIException {
      super.scanXMLDeclOrTextDecl(var1, this.fStrings);
      --this.fMarkupDepth;
      String var2 = this.fStrings[0];
      String var3 = this.fStrings[1];
      String var4 = this.fStrings[2];
      this.fStandalone = var4 != null && var4.equals("yes");
      super.fEntityManager.setStandalone(this.fStandalone);
      super.fEntityScanner.setXMLVersion(var2);
      if (this.fDocumentHandler != null) {
         if (var1) {
            this.fDocumentHandler.textDecl(var2, var3, (Augmentations)null);
         } else {
            this.fDocumentHandler.xmlDecl(var2, var3, var4, (Augmentations)null);
         }
      }

      if (var3 != null && !super.fEntityScanner.fCurrentEntity.isEncodingExternallySpecified()) {
         super.fEntityScanner.setEncoding(var3);
      }

   }

   protected void scanPIData(String var1, XMLString var2) throws IOException, XNIException {
      super.scanPIData(var1, var2);
      --this.fMarkupDepth;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.processingInstruction(var1, var2, (Augmentations)null);
      }

   }

   protected void scanComment() throws IOException, XNIException {
      this.scanComment(this.fStringBuffer);
      --this.fMarkupDepth;
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.comment(this.fStringBuffer, (Augmentations)null);
      }

   }

   protected boolean scanStartElement() throws IOException, XNIException {
      String var1;
      if (super.fNamespaces) {
         super.fEntityScanner.scanQName(this.fElementQName);
      } else {
         var1 = super.fEntityScanner.scanName();
         this.fElementQName.setValues((String)null, var1, var1, (String)null);
      }

      var1 = this.fElementQName.rawname;
      this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
      boolean var2 = false;
      this.fAttributes.removeAllAttributes();

      while(true) {
         boolean var3 = super.fEntityScanner.skipSpaces();
         int var4 = super.fEntityScanner.peekChar();
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

         this.scanAttribute(this.fAttributes);
      }

      if (this.fDocumentHandler != null) {
         if (var2) {
            --this.fMarkupDepth;
            if (this.fMarkupDepth < this.fEntityStack[super.fEntityDepth - 1]) {
               this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
            }

            this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, (Augmentations)null);
            this.fElementStack.popElement(this.fElementQName);
         } else {
            this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, (Augmentations)null);
         }
      }

      return var2;
   }

   protected void scanStartElementName() throws IOException, XNIException {
      if (super.fNamespaces) {
         super.fEntityScanner.scanQName(this.fElementQName);
      } else {
         String var1 = super.fEntityScanner.scanName();
         this.fElementQName.setValues((String)null, var1, var1, (String)null);
      }

      this.fSawSpace = super.fEntityScanner.skipSpaces();
   }

   protected boolean scanStartElementAfterName() throws IOException, XNIException {
      String var1 = this.fElementQName.rawname;
      this.fCurrentElement = this.fElementStack.pushElement(this.fElementQName);
      boolean var2 = false;
      this.fAttributes.removeAllAttributes();

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

         this.scanAttribute(this.fAttributes);
         this.fSawSpace = super.fEntityScanner.skipSpaces();
      }

      if (this.fDocumentHandler != null) {
         if (var2) {
            --this.fMarkupDepth;
            if (this.fMarkupDepth < this.fEntityStack[super.fEntityDepth - 1]) {
               this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
            }

            this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, (Augmentations)null);
            this.fElementStack.popElement(this.fElementQName);
         } else {
            this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, (Augmentations)null);
         }
      }

      return var2;
   }

   protected void scanAttribute(XMLAttributes var1) throws IOException, XNIException {
      if (super.fNamespaces) {
         super.fEntityScanner.scanQName(this.fAttributeQName);
      } else {
         String var2 = super.fEntityScanner.scanName();
         this.fAttributeQName.setValues((String)null, var2, var2, (String)null);
      }

      super.fEntityScanner.skipSpaces();
      if (!super.fEntityScanner.skipChar(61)) {
         this.reportFatalError("EqRequiredInAttribute", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
      }

      super.fEntityScanner.skipSpaces();
      int var6 = var1.getLength();
      int var3 = var1.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, (String)null);
      if (var6 == var1.getLength()) {
         this.reportFatalError("AttributeNotUnique", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
      }

      boolean var4 = this.fHasExternalDTD && !this.fStandalone;
      boolean var5 = this.scanAttributeValue(this.fTempString, this.fTempString2, this.fAttributeQName.rawname, var4, this.fCurrentElement.rawname);
      var1.setValue(var3, this.fTempString.toString());
      if (!var5) {
         var1.setNonNormalizedValue(var3, this.fTempString2.toString());
      }

      var1.setSpecified(var3, true);
   }

   protected int scanContent() throws IOException, XNIException {
      Object var1 = this.fTempString;
      int var2 = super.fEntityScanner.scanContent((XMLString)var1);
      if (var2 == 13) {
         super.fEntityScanner.scanChar();
         this.fStringBuffer.clear();
         this.fStringBuffer.append(this.fTempString);
         this.fStringBuffer.append((char)var2);
         var1 = this.fStringBuffer;
         var2 = -1;
      }

      if (this.fDocumentHandler != null && ((XMLString)var1).length > 0) {
         this.fDocumentHandler.characters((XMLString)var1, (Augmentations)null);
      }

      if (var2 == 93 && this.fTempString.length == 0) {
         this.fStringBuffer.clear();
         this.fStringBuffer.append((char)super.fEntityScanner.scanChar());
         this.fInScanContent = true;
         if (super.fEntityScanner.skipChar(93)) {
            this.fStringBuffer.append(']');

            while(super.fEntityScanner.skipChar(93)) {
               this.fStringBuffer.append(']');
            }

            if (super.fEntityScanner.skipChar(62)) {
               this.reportFatalError("CDEndInContent", (Object[])null);
            }
         }

         if (this.fDocumentHandler != null && this.fStringBuffer.length != 0) {
            this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
         }

         this.fInScanContent = false;
         var2 = -1;
      }

      return var2;
   }

   protected boolean scanCDATASection(boolean var1) throws IOException, XNIException {
      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.startCDATA((Augmentations)null);
      }

      while(true) {
         while(true) {
            this.fStringBuffer.clear();
            int var2;
            if (!super.fEntityScanner.scanData("]]", this.fStringBuffer)) {
               if (this.fDocumentHandler != null && this.fStringBuffer.length > 0) {
                  this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
               }

               for(var2 = 0; super.fEntityScanner.skipChar(93); ++var2) {
               }

               if (this.fDocumentHandler != null && var2 > 0) {
                  this.fStringBuffer.clear();
                  int var3;
                  if (var2 > 2048) {
                     var3 = var2 / 2048;
                     int var4 = var2 % 2048;

                     for(int var5 = 0; var5 < 2048; ++var5) {
                        this.fStringBuffer.append(']');
                     }

                     for(int var6 = 0; var6 < var3; ++var6) {
                        this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
                     }

                     if (var4 != 0) {
                        this.fStringBuffer.length = var4;
                        this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
                     }
                  } else {
                     for(var3 = 0; var3 < var2; ++var3) {
                        this.fStringBuffer.append(']');
                     }

                     this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
                  }
               }

               if (super.fEntityScanner.skipChar(62)) {
                  --this.fMarkupDepth;
                  if (this.fDocumentHandler != null) {
                     this.fDocumentHandler.endCDATA((Augmentations)null);
                  }

                  return true;
               }

               if (this.fDocumentHandler != null) {
                  this.fStringBuffer.clear();
                  this.fStringBuffer.append("]]");
                  this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
               }
            } else {
               if (this.fDocumentHandler != null) {
                  this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
               }

               var2 = super.fEntityScanner.peekChar();
               if (var2 != -1 && this.isInvalidLiteral(var2)) {
                  if (XMLChar.isHighSurrogate(var2)) {
                     this.fStringBuffer.clear();
                     this.scanSurrogates(this.fStringBuffer);
                     if (this.fDocumentHandler != null) {
                        this.fDocumentHandler.characters(this.fStringBuffer, (Augmentations)null);
                     }
                  } else {
                     this.reportFatalError("InvalidCharInCDSect", new Object[]{Integer.toString(var2, 16)});
                     super.fEntityScanner.scanChar();
                  }
               }
            }
         }
      }
   }

   protected int scanEndElement() throws IOException, XNIException {
      this.fElementStack.popElement(this.fElementQName);
      if (!super.fEntityScanner.skipString(this.fElementQName.rawname)) {
         this.reportFatalError("ETagRequired", new Object[]{this.fElementQName.rawname});
      }

      super.fEntityScanner.skipSpaces();
      if (!super.fEntityScanner.skipChar(62)) {
         this.reportFatalError("ETagUnterminated", new Object[]{this.fElementQName.rawname});
      }

      --this.fMarkupDepth;
      --this.fMarkupDepth;
      if (this.fMarkupDepth < this.fEntityStack[super.fEntityDepth - 1]) {
         this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
      }

      if (this.fDocumentHandler != null) {
         this.fDocumentHandler.endElement(this.fElementQName, (Augmentations)null);
      }

      return this.fMarkupDepth;
   }

   protected void scanCharReference() throws IOException, XNIException {
      this.fStringBuffer2.clear();
      int var1 = this.scanCharReferenceValue(this.fStringBuffer2, (XMLStringBuffer)null);
      --this.fMarkupDepth;
      if (var1 != -1 && this.fDocumentHandler != null) {
         if (super.fNotifyCharRefs) {
            this.fDocumentHandler.startGeneralEntity(super.fCharRefLiteral, (XMLResourceIdentifier)null, (String)null, (Augmentations)null);
         }

         Augmentations var2 = null;
         if (super.fValidation && var1 <= 32) {
            if (this.fTempAugmentations != null) {
               this.fTempAugmentations.removeAllItems();
            } else {
               this.fTempAugmentations = new AugmentationsImpl();
            }

            var2 = this.fTempAugmentations;
            var2.putItem("CHAR_REF_PROBABLE_WS", Boolean.TRUE);
         }

         this.fDocumentHandler.characters(this.fStringBuffer2, var2);
         if (super.fNotifyCharRefs) {
            this.fDocumentHandler.endGeneralEntity(super.fCharRefLiteral, (Augmentations)null);
         }
      }

   }

   protected void scanEntityReference() throws IOException, XNIException {
      String var1 = super.fEntityScanner.scanName();
      if (var1 == null) {
         this.reportFatalError("NameRequiredInReference", (Object[])null);
      } else {
         if (!super.fEntityScanner.skipChar(59)) {
            this.reportFatalError("SemicolonRequiredInReference", new Object[]{var1});
         }

         --this.fMarkupDepth;
         if (var1 == XMLScanner.fAmpSymbol) {
            this.handleCharacter('&', XMLScanner.fAmpSymbol);
         } else if (var1 == XMLScanner.fLtSymbol) {
            this.handleCharacter('<', XMLScanner.fLtSymbol);
         } else if (var1 == XMLScanner.fGtSymbol) {
            this.handleCharacter('>', XMLScanner.fGtSymbol);
         } else if (var1 == XMLScanner.fQuotSymbol) {
            this.handleCharacter('"', XMLScanner.fQuotSymbol);
         } else if (var1 == XMLScanner.fAposSymbol) {
            this.handleCharacter('\'', XMLScanner.fAposSymbol);
         } else if (super.fEntityManager.isUnparsedEntity(var1)) {
            this.reportFatalError("ReferenceToUnparsedEntity", new Object[]{var1});
         } else {
            if (!super.fEntityManager.isDeclaredEntity(var1)) {
               if (this.fHasExternalDTD && !this.fStandalone) {
                  if (super.fValidation) {
                     super.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{var1}, (short)1);
                  }
               } else {
                  this.reportFatalError("EntityNotDeclared", new Object[]{var1});
               }
            }

            super.fEntityManager.startEntity(var1, false);
         }

      }
   }

   private void handleCharacter(char var1, String var2) throws XNIException {
      if (this.fDocumentHandler != null) {
         if (this.fNotifyBuiltInRefs) {
            this.fDocumentHandler.startGeneralEntity(var2, (XMLResourceIdentifier)null, (String)null, (Augmentations)null);
         }

         this.fSingleChar[0] = var1;
         this.fTempString.setValues(this.fSingleChar, 0, 1);
         this.fDocumentHandler.characters(this.fTempString, (Augmentations)null);
         if (this.fNotifyBuiltInRefs) {
            this.fDocumentHandler.endGeneralEntity(var2, (Augmentations)null);
         }
      }

   }

   protected int handleEndElement(QName var1, boolean var2) throws XNIException {
      --this.fMarkupDepth;
      if (this.fMarkupDepth < this.fEntityStack[super.fEntityDepth - 1]) {
         this.reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
      }

      QName var3 = this.fQName;
      this.fElementStack.popElement(var3);
      if (var1.rawname != var3.rawname) {
         this.reportFatalError("ETagRequired", new Object[]{var3.rawname});
      }

      if (super.fNamespaces) {
         var1.uri = var3.uri;
      }

      if (this.fDocumentHandler != null && !var2) {
         this.fDocumentHandler.endElement(var1, (Augmentations)null);
      }

      return this.fMarkupDepth;
   }

   protected final void setScannerState(int var1) {
      this.fScannerState = var1;
   }

   protected final void setDispatcher(Dispatcher var1) {
      this.fDispatcher = var1;
   }

   protected String getScannerStateName(int var1) {
      switch (var1) {
         case 1:
            return "SCANNER_STATE_START_OF_MARKUP";
         case 2:
            return "SCANNER_STATE_COMMENT";
         case 3:
            return "SCANNER_STATE_PI";
         case 4:
            return "SCANNER_STATE_DOCTYPE";
         case 5:
         case 9:
         case 10:
         case 11:
         case 12:
         default:
            return "??? (" + var1 + ')';
         case 6:
            return "SCANNER_STATE_ROOT_ELEMENT";
         case 7:
            return "SCANNER_STATE_CONTENT";
         case 8:
            return "SCANNER_STATE_REFERENCE";
         case 13:
            return "SCANNER_STATE_END_OF_INPUT";
         case 14:
            return "SCANNER_STATE_TERMINATED";
         case 15:
            return "SCANNER_STATE_CDATA";
         case 16:
            return "SCANNER_STATE_TEXT_DECL";
      }
   }

   public String getDispatcherName(Dispatcher var1) {
      return "null";
   }

   static {
      FEATURE_DEFAULTS = new Boolean[]{null, null, Boolean.FALSE, Boolean.FALSE};
      RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/entity-resolver"};
      PROPERTY_DEFAULTS = new Object[]{null, null, null, null};
   }

   protected class FragmentContentDispatcher implements Dispatcher {
      public boolean dispatch(boolean var1) throws IOException, XNIException {
         try {
            boolean var2;
            do {
               var2 = false;
               switch (XMLDocumentFragmentScannerImpl.this.fScannerState) {
                  case 1:
                     ++XMLDocumentFragmentScannerImpl.this.fMarkupDepth;
                     if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(47)) {
                        if (XMLDocumentFragmentScannerImpl.this.scanEndElement() == 0 && this.elementDepthIsZeroHook()) {
                           return true;
                        }

                        XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     } else if (XMLDocumentFragmentScannerImpl.this.isValidNameStartChar(XMLDocumentFragmentScannerImpl.super.fEntityScanner.peekChar())) {
                        XMLDocumentFragmentScannerImpl.this.scanStartElement();
                        XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     } else if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(33)) {
                        if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(45)) {
                           if (!XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(45)) {
                              XMLDocumentFragmentScannerImpl.this.reportFatalError("InvalidCommentStart", (Object[])null);
                           }

                           XMLDocumentFragmentScannerImpl.this.setScannerState(2);
                           var2 = true;
                        } else if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipString("[CDATA[")) {
                           XMLDocumentFragmentScannerImpl.this.setScannerState(15);
                           var2 = true;
                        } else if (!this.scanForDoctypeHook()) {
                           XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", (Object[])null);
                        }
                     } else if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(63)) {
                        XMLDocumentFragmentScannerImpl.this.setScannerState(3);
                        var2 = true;
                     } else if (XMLDocumentFragmentScannerImpl.this.isValidNameStartHighSurrogate(XMLDocumentFragmentScannerImpl.super.fEntityScanner.peekChar())) {
                        XMLDocumentFragmentScannerImpl.this.scanStartElement();
                        XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     } else {
                        XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", (Object[])null);
                        XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     }
                     break;
                  case 2:
                     XMLDocumentFragmentScannerImpl.this.scanComment();
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     break;
                  case 3:
                     XMLDocumentFragmentScannerImpl.this.scanPI();
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     break;
                  case 4:
                     XMLDocumentFragmentScannerImpl.this.reportFatalError("DoctypeIllegalInContent", (Object[])null);
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                  case 5:
                  case 9:
                  case 10:
                  case 11:
                  case 12:
                  case 13:
                  case 14:
                  default:
                     break;
                  case 6:
                     if (this.scanRootElementHook()) {
                        return true;
                     }

                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     break;
                  case 7:
                     if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(60)) {
                        XMLDocumentFragmentScannerImpl.this.setScannerState(1);
                        var2 = true;
                     } else if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(38)) {
                        XMLDocumentFragmentScannerImpl.this.setScannerState(8);
                        var2 = true;
                     } else {
                        while(true) {
                           int var8 = XMLDocumentFragmentScannerImpl.this.scanContent();
                           if (var8 == 60) {
                              XMLDocumentFragmentScannerImpl.super.fEntityScanner.scanChar();
                              XMLDocumentFragmentScannerImpl.this.setScannerState(1);
                              break;
                           }

                           if (var8 == 38) {
                              XMLDocumentFragmentScannerImpl.super.fEntityScanner.scanChar();
                              XMLDocumentFragmentScannerImpl.this.setScannerState(8);
                              break;
                           }

                           if (var8 != -1 && XMLDocumentFragmentScannerImpl.this.isInvalidLiteral(var8)) {
                              if (XMLChar.isHighSurrogate(var8)) {
                                 XMLDocumentFragmentScannerImpl.this.fStringBuffer.clear();
                                 if (XMLDocumentFragmentScannerImpl.this.scanSurrogates(XMLDocumentFragmentScannerImpl.this.fStringBuffer) && XMLDocumentFragmentScannerImpl.this.fDocumentHandler != null) {
                                    XMLDocumentFragmentScannerImpl.this.fDocumentHandler.characters(XMLDocumentFragmentScannerImpl.this.fStringBuffer, (Augmentations)null);
                                 }
                              } else {
                                 XMLDocumentFragmentScannerImpl.this.reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(var8, 16)});
                                 XMLDocumentFragmentScannerImpl.super.fEntityScanner.scanChar();
                              }
                           }

                           if (!var1) {
                              break;
                           }
                        }
                     }
                     break;
                  case 8:
                     ++XMLDocumentFragmentScannerImpl.this.fMarkupDepth;
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipChar(35)) {
                        XMLDocumentFragmentScannerImpl.this.scanCharReference();
                     } else {
                        XMLDocumentFragmentScannerImpl.this.scanEntityReference();
                     }
                     break;
                  case 15:
                     XMLDocumentFragmentScannerImpl.this.scanCDATASection(var1);
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
                     break;
                  case 16:
                     if (XMLDocumentFragmentScannerImpl.super.fEntityScanner.skipString("<?xml")) {
                        ++XMLDocumentFragmentScannerImpl.this.fMarkupDepth;
                        if (!XMLDocumentFragmentScannerImpl.this.isValidNameChar(XMLDocumentFragmentScannerImpl.super.fEntityScanner.peekChar())) {
                           XMLDocumentFragmentScannerImpl.this.scanXMLDeclOrTextDecl(true);
                        } else {
                           XMLDocumentFragmentScannerImpl.this.fStringBuffer.clear();
                           XMLDocumentFragmentScannerImpl.this.fStringBuffer.append("xml");
                           if (XMLDocumentFragmentScannerImpl.super.fNamespaces) {
                              while(XMLDocumentFragmentScannerImpl.this.isValidNCName(XMLDocumentFragmentScannerImpl.super.fEntityScanner.peekChar())) {
                                 XMLDocumentFragmentScannerImpl.this.fStringBuffer.append((char)XMLDocumentFragmentScannerImpl.super.fEntityScanner.scanChar());
                              }
                           } else {
                              while(XMLDocumentFragmentScannerImpl.this.isValidNameChar(XMLDocumentFragmentScannerImpl.super.fEntityScanner.peekChar())) {
                                 XMLDocumentFragmentScannerImpl.this.fStringBuffer.append((char)XMLDocumentFragmentScannerImpl.super.fEntityScanner.scanChar());
                              }
                           }

                           String var3 = XMLDocumentFragmentScannerImpl.super.fSymbolTable.addSymbol(XMLDocumentFragmentScannerImpl.this.fStringBuffer.ch, XMLDocumentFragmentScannerImpl.this.fStringBuffer.offset, XMLDocumentFragmentScannerImpl.this.fStringBuffer.length);
                           XMLDocumentFragmentScannerImpl.this.scanPIData(var3, XMLDocumentFragmentScannerImpl.this.fTempString);
                        }
                     }

                     XMLDocumentFragmentScannerImpl.super.fEntityManager.fCurrentEntity.mayReadChunks = true;
                     XMLDocumentFragmentScannerImpl.this.setScannerState(7);
               }
            } while(var1 || var2);

            return true;
         } catch (MalformedByteSequenceException var5) {
            XMLDocumentFragmentScannerImpl.super.fErrorReporter.reportError(var5.getDomain(), var5.getKey(), var5.getArguments(), (short)2);
            return false;
         } catch (CharConversionException var6) {
            XMLDocumentFragmentScannerImpl.this.reportFatalError("CharConversionFailure", (Object[])null);
            return false;
         } catch (EOFException var7) {
            this.endOfFileHook(var7);
            return false;
         }
      }

      protected boolean scanForDoctypeHook() throws IOException, XNIException {
         return false;
      }

      protected boolean elementDepthIsZeroHook() throws IOException, XNIException {
         return false;
      }

      protected boolean scanRootElementHook() throws IOException, XNIException {
         return false;
      }

      protected void endOfFileHook(EOFException var1) throws IOException, XNIException {
         if (XMLDocumentFragmentScannerImpl.this.fMarkupDepth != 0) {
            XMLDocumentFragmentScannerImpl.this.reportFatalError("PrematureEOF", (Object[])null);
         }

      }
   }

   protected interface Dispatcher {
      boolean dispatch(boolean var1) throws IOException, XNIException;
   }

   protected static class ElementStack {
      protected QName[] fElements = new QName[10];
      protected int fSize;

      public ElementStack() {
         for(int var1 = 0; var1 < this.fElements.length; ++var1) {
            this.fElements[var1] = new QName();
         }

      }

      public QName pushElement(QName var1) {
         if (this.fSize == this.fElements.length) {
            QName[] var2 = new QName[this.fElements.length * 2];
            System.arraycopy(this.fElements, 0, var2, 0, this.fSize);
            this.fElements = var2;

            for(int var3 = this.fSize; var3 < this.fElements.length; ++var3) {
               this.fElements[var3] = new QName();
            }
         }

         this.fElements[this.fSize].setValues(var1);
         return this.fElements[this.fSize++];
      }

      public void popElement(QName var1) {
         var1.setValues(this.fElements[--this.fSize]);
      }

      public void clear() {
         this.fSize = 0;
      }
   }
}
