package org.apache.xerces.impl;

import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLVersionDetector {
   private static final char[] XML11_VERSION = new char[]{'1', '.', '1'};
   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
   protected static final String fVersionSymbol = "version".intern();
   protected static final String fXMLSymbol = "[xml]".intern();
   protected SymbolTable fSymbolTable;
   protected XMLErrorReporter fErrorReporter;
   protected XMLEntityManager fEntityManager;
   protected String fEncoding = null;
   private XMLString fVersionNum = new XMLString();
   private final char[] fExpectedVersionString = new char[]{'<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' '};

   public void reset(XMLComponentManager var1) throws XMLConfigurationException {
      this.fSymbolTable = (SymbolTable)var1.getProperty("http://apache.org/xml/properties/internal/symbol-table");
      this.fErrorReporter = (XMLErrorReporter)var1.getProperty("http://apache.org/xml/properties/internal/error-reporter");
      this.fEntityManager = (XMLEntityManager)var1.getProperty("http://apache.org/xml/properties/internal/entity-manager");

      for(int var2 = 14; var2 < this.fExpectedVersionString.length; ++var2) {
         this.fExpectedVersionString[var2] = ' ';
      }

   }

   public void startDocumentParsing(XMLEntityHandler var1, short var2) {
      if (var2 == 1) {
         this.fEntityManager.setScannerVersion((short)1);
      } else {
         this.fEntityManager.setScannerVersion((short)2);
      }

      this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
      this.fEntityManager.setEntityHandler(var1);
      var1.startEntity(fXMLSymbol, this.fEntityManager.getCurrentResourceIdentifier(), this.fEncoding, (Augmentations)null);
   }

   public short determineDocVersion(XMLInputSource var1) throws IOException {
      this.fEncoding = this.fEntityManager.setupCurrentEntity(fXMLSymbol, var1, false, true);
      this.fEntityManager.setScannerVersion((short)1);
      XMLEntityScanner var2 = this.fEntityManager.getEntityScanner();

      try {
         if (!var2.skipString("<?xml")) {
            return 1;
         } else if (!var2.skipDeclSpaces()) {
            this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 5);
            return 1;
         } else if (!var2.skipString("version")) {
            this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 6);
            return 1;
         } else {
            var2.skipDeclSpaces();
            if (var2.peekChar() != 61) {
               this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 13);
               return 1;
            } else {
               var2.scanChar();
               var2.skipDeclSpaces();
               int var3 = var2.scanChar();
               this.fExpectedVersionString[14] = (char)var3;

               for(int var4 = 0; var4 < XML11_VERSION.length; ++var4) {
                  this.fExpectedVersionString[15 + var4] = (char)var2.scanChar();
               }

               this.fExpectedVersionString[18] = (char)var2.scanChar();
               this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 19);

               int var5;
               for(var5 = 0; var5 < XML11_VERSION.length && this.fExpectedVersionString[15 + var5] == XML11_VERSION[var5]; ++var5) {
               }

               return (short)(var5 == XML11_VERSION.length ? 2 : 1);
            }
         }
      } catch (EOFException var6) {
         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "PrematureEOF", (Object[])null, (short)2);
         return 1;
      }
   }

   private void fixupCurrentEntity(XMLEntityManager var1, char[] var2, int var3) {
      XMLEntityManager.ScannedEntity var4 = var1.getCurrentEntity();
      if (var4.count - var4.position + var3 > var4.ch.length) {
         char[] var5 = var4.ch;
         var4.ch = new char[var3 + var4.count - var4.position + 1];
         System.arraycopy(var5, 0, var4.ch, 0, var5.length);
      }

      if (var4.position < var3) {
         System.arraycopy(var4.ch, var4.position, var4.ch, var3, var4.count - var4.position);
         var4.count += var3 - var4.position;
      } else {
         for(int var6 = var3; var6 < var4.position; ++var6) {
            var4.ch[var6] = ' ';
         }
      }

      System.arraycopy(var2, 0, var4.ch, 0, var3);
      var4.position = 0;
      var4.baseCharOffset = 0;
      var4.startPosition = 0;
      var4.columnNumber = var4.lineNumber = 1;
   }
}
