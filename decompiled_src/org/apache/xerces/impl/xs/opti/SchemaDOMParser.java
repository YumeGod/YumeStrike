package org.apache.xerces.impl.xs.opti;

import java.io.IOException;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Document;

public class SchemaDOMParser extends DefaultXMLDocumentHandler {
   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
   public static final String GENERATE_SYNTHETIC_ANNOTATION = "http://apache.org/xml/features/generate-synthetic-annotations";
   protected XMLLocator fLocator;
   protected NamespaceContext fNamespaceContext = null;
   SchemaDOM schemaDOM;
   XMLParserConfiguration config;
   private int fAnnotationDepth = -1;
   private int fInnerAnnotationDepth = -1;
   private int fDepth = -1;
   XMLErrorReporter fErrorReporter;
   private boolean fGenerateSyntheticAnnotation = false;
   private BooleanStack fHasNonSchemaAttributes = new BooleanStack();
   private BooleanStack fSawAnnotation = new BooleanStack();
   private XMLAttributes fEmptyAttr = new XMLAttributesImpl();

   public SchemaDOMParser(XMLParserConfiguration var1) {
      this.config = var1;
   }

   public void startDocument(XMLLocator var1, String var2, NamespaceContext var3, Augmentations var4) throws XNIException {
      this.fErrorReporter = (XMLErrorReporter)this.config.getProperty("http://apache.org/xml/properties/internal/error-reporter");
      this.fGenerateSyntheticAnnotation = this.config.getFeature("http://apache.org/xml/features/generate-synthetic-annotations");
      this.fHasNonSchemaAttributes.clear();
      this.fSawAnnotation.clear();
      this.schemaDOM = new SchemaDOM();
      this.fAnnotationDepth = -1;
      this.fInnerAnnotationDepth = -1;
      this.fDepth = -1;
      this.fLocator = var1;
      this.fNamespaceContext = var3;
      this.schemaDOM.setDocumentURI(var1.getExpandedSystemId());
   }

   public void endDocument(Augmentations var1) throws XNIException {
   }

   public void comment(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fAnnotationDepth > -1) {
         this.schemaDOM.comment(var1);
      }

   }

   public void processingInstruction(String var1, XMLString var2, Augmentations var3) throws XNIException {
      if (this.fAnnotationDepth > -1) {
         this.schemaDOM.processingInstruction(var1, var2.toString());
      }

   }

   public void characters(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fInnerAnnotationDepth == -1) {
         for(int var3 = var1.offset; var3 < var1.offset + var1.length; ++var3) {
            if (!XMLChar.isSpace(var1.ch[var3])) {
               String var4 = new String(var1.ch, var3, var1.length + var1.offset - var3);
               this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "s4s-elt-character", new Object[]{var4}, (short)1);
               break;
            }
         }
      } else {
         this.schemaDOM.characters(var1);
      }

   }

   public void startElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      ++this.fDepth;
      if (this.fAnnotationDepth == -1) {
         if (var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && var1.localpart == SchemaSymbols.ELT_ANNOTATION) {
            if (this.fGenerateSyntheticAnnotation) {
               if (this.fSawAnnotation.size() > 0) {
                  this.fSawAnnotation.pop();
               }

               this.fSawAnnotation.push(true);
            }

            this.fAnnotationDepth = this.fDepth;
            this.schemaDOM.startAnnotation(var1, var2, this.fNamespaceContext);
         } else if (var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && this.fGenerateSyntheticAnnotation) {
            this.fSawAnnotation.push(false);
            this.fHasNonSchemaAttributes.push(this.hasNonSchemaAttributes(var1, var2));
         }
      } else {
         if (this.fDepth != this.fAnnotationDepth + 1) {
            this.schemaDOM.startAnnotationElement(var1, var2);
            return;
         }

         this.fInnerAnnotationDepth = this.fDepth;
         this.schemaDOM.startAnnotationElement(var1, var2);
      }

      this.schemaDOM.startElement(var1, var2, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
   }

   public void emptyElement(QName var1, XMLAttributes var2, Augmentations var3) throws XNIException {
      if (this.fGenerateSyntheticAnnotation && this.fAnnotationDepth == -1 && var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && var1.localpart != SchemaSymbols.ELT_ANNOTATION && this.hasNonSchemaAttributes(var1, var2)) {
         this.schemaDOM.startElement(var1, var2, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
         var2.removeAllAttributes();
         String var4 = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
         QName var5 = new QName(var4, SchemaSymbols.ELT_ANNOTATION, var4 + (var4.length() == 0 ? "" : ":") + SchemaSymbols.ELT_ANNOTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
         this.schemaDOM.startAnnotation(var5, var2, this.fNamespaceContext);
         QName var6 = new QName(var4, SchemaSymbols.ELT_DOCUMENTATION, var4 + (var4.length() == 0 ? "" : ":") + SchemaSymbols.ELT_DOCUMENTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
         this.schemaDOM.startAnnotationElement(var6, var2);
         this.schemaDOM.characters(new XMLString("SYNTHETIC_ANNOTATION".toCharArray(), 0, 20));
         this.schemaDOM.endSyntheticAnnotationElement(var6, false);
         this.schemaDOM.endSyntheticAnnotationElement(var5, true);
         this.schemaDOM.endElement();
      } else {
         if (this.fAnnotationDepth == -1) {
            if (var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && var1.localpart == SchemaSymbols.ELT_ANNOTATION) {
               this.schemaDOM.startAnnotation(var1, var2, this.fNamespaceContext);
            }
         } else {
            this.schemaDOM.startAnnotationElement(var1, var2);
         }

         this.schemaDOM.emptyElement(var1, var2, this.fLocator.getLineNumber(), this.fLocator.getColumnNumber(), this.fLocator.getCharacterOffset());
         if (this.fAnnotationDepth == -1) {
            if (var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && var1.localpart == SchemaSymbols.ELT_ANNOTATION) {
               this.schemaDOM.endAnnotationElement(var1, true);
            }
         } else {
            this.schemaDOM.endAnnotationElement(var1, false);
         }

      }
   }

   public void endElement(QName var1, Augmentations var2) throws XNIException {
      if (this.fAnnotationDepth > -1) {
         if (this.fInnerAnnotationDepth == this.fDepth) {
            this.fInnerAnnotationDepth = -1;
            this.schemaDOM.endAnnotationElement(var1, false);
            this.schemaDOM.endElement();
         } else if (this.fAnnotationDepth == this.fDepth) {
            this.fAnnotationDepth = -1;
            this.schemaDOM.endAnnotationElement(var1, true);
            this.schemaDOM.endElement();
         } else {
            this.schemaDOM.endAnnotationElement(var1, false);
         }
      } else {
         if (var1.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && this.fGenerateSyntheticAnnotation) {
            boolean var3 = this.fHasNonSchemaAttributes.pop();
            boolean var4 = this.fSawAnnotation.pop();
            if (var3 && !var4) {
               String var5 = this.fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
               QName var6 = new QName(var5, SchemaSymbols.ELT_ANNOTATION, var5 + (var5.length() == 0 ? "" : ":") + SchemaSymbols.ELT_ANNOTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
               this.schemaDOM.startAnnotation(var6, this.fEmptyAttr, this.fNamespaceContext);
               QName var7 = new QName(var5, SchemaSymbols.ELT_DOCUMENTATION, var5 + (var5.length() == 0 ? "" : ":") + SchemaSymbols.ELT_DOCUMENTATION, SchemaSymbols.URI_SCHEMAFORSCHEMA);
               this.schemaDOM.startAnnotationElement(var7, this.fEmptyAttr);
               this.schemaDOM.characters(new XMLString("SYNTHETIC_ANNOTATION".toCharArray(), 0, 20));
               this.schemaDOM.endSyntheticAnnotationElement(var7, false);
               this.schemaDOM.endSyntheticAnnotationElement(var6, true);
            }
         }

         this.schemaDOM.endElement();
      }

      --this.fDepth;
   }

   private boolean hasNonSchemaAttributes(QName var1, XMLAttributes var2) {
      int var3 = var2.getLength();

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2.getURI(var4);
         if (var5 != null && var5 != SchemaSymbols.URI_SCHEMAFORSCHEMA && var5 != NamespaceContext.XMLNS_URI && (var5 != NamespaceContext.XML_URI || var2.getQName(var4) != SchemaSymbols.ATT_XML_LANG || var1.localpart != SchemaSymbols.ELT_SCHEMA)) {
            return true;
         }
      }

      return false;
   }

   public void ignorableWhitespace(XMLString var1, Augmentations var2) throws XNIException {
      if (this.fAnnotationDepth != -1) {
         this.schemaDOM.characters(var1);
      }

   }

   public void startCDATA(Augmentations var1) throws XNIException {
      if (this.fAnnotationDepth != -1) {
         this.schemaDOM.startAnnotationCDATA();
      }

   }

   public void endCDATA(Augmentations var1) throws XNIException {
      if (this.fAnnotationDepth != -1) {
         this.schemaDOM.endAnnotationCDATA();
      }

   }

   public Document getDocument() {
      return this.schemaDOM;
   }

   public void setFeature(String var1, boolean var2) {
      this.config.setFeature(var1, var2);
   }

   public boolean getFeature(String var1) {
      return this.config.getFeature(var1);
   }

   public void setProperty(String var1, Object var2) {
      this.config.setProperty(var1, var2);
   }

   public Object getProperty(String var1) {
      return this.config.getProperty(var1);
   }

   public void setEntityResolver(XMLEntityResolver var1) {
      this.config.setEntityResolver(var1);
   }

   public void parse(XMLInputSource var1) throws IOException {
      this.config.parse(var1);
   }

   public Document getDocument2() {
      return ((SchemaParsingConfig)this.config).getDocument();
   }

   public void reset() {
      ((SchemaParsingConfig)this.config).reset();
   }

   public void resetNodePool() {
      ((SchemaParsingConfig)this.config).resetNodePool();
   }

   private static final class BooleanStack {
      private int fDepth;
      private boolean[] fData;

      public BooleanStack() {
      }

      public int size() {
         return this.fDepth;
      }

      public void push(boolean var1) {
         this.ensureCapacity(this.fDepth + 1);
         this.fData[this.fDepth++] = var1;
      }

      public boolean pop() {
         return this.fData[--this.fDepth];
      }

      public void clear() {
         this.fDepth = 0;
      }

      private void ensureCapacity(int var1) {
         if (this.fData == null) {
            this.fData = new boolean[32];
         } else if (this.fData.length <= var1) {
            boolean[] var2 = new boolean[this.fData.length * 2];
            System.arraycopy(this.fData, 0, var2, 0, this.fData.length);
            this.fData = var2;
         }

      }
   }
}
