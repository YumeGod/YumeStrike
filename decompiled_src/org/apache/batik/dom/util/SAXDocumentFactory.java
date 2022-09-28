package org.apache.batik.dom.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.batik.util.HaltingThread;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXDocumentFactory extends DefaultHandler implements LexicalHandler, DocumentFactory {
   protected DOMImplementation implementation;
   protected String parserClassName;
   protected XMLReader parser;
   protected Document document;
   protected DocumentDescriptor documentDescriptor;
   protected boolean createDocumentDescriptor;
   protected Node currentNode;
   protected Locator locator;
   protected StringBuffer stringBuffer = new StringBuffer();
   protected boolean stringContent;
   protected boolean inDTD;
   protected boolean inCDATA;
   protected boolean inProlog;
   protected boolean isValidating;
   protected boolean isStandalone;
   protected String xmlVersion;
   protected HashTableStack namespaces;
   protected ErrorHandler errorHandler;
   protected List preInfo;
   static SAXParserFactory saxFactory = SAXParserFactory.newInstance();

   public SAXDocumentFactory(DOMImplementation var1, String var2) {
      this.implementation = var1;
      this.parserClassName = var2;
   }

   public SAXDocumentFactory(DOMImplementation var1, String var2, boolean var3) {
      this.implementation = var1;
      this.parserClassName = var2;
      this.createDocumentDescriptor = var3;
   }

   public Document createDocument(String var1, String var2, String var3) throws IOException {
      return this.createDocument(var1, var2, var3, new InputSource(var3));
   }

   public Document createDocument(String var1) throws IOException {
      return this.createDocument(new InputSource(var1));
   }

   public Document createDocument(String var1, String var2, String var3, InputStream var4) throws IOException {
      InputSource var5 = new InputSource(var4);
      var5.setSystemId(var3);
      return this.createDocument(var1, var2, var3, var5);
   }

   public Document createDocument(String var1, InputStream var2) throws IOException {
      InputSource var3 = new InputSource(var2);
      var3.setSystemId(var1);
      return this.createDocument(var3);
   }

   public Document createDocument(String var1, String var2, String var3, Reader var4) throws IOException {
      InputSource var5 = new InputSource(var4);
      var5.setSystemId(var3);
      return this.createDocument(var1, var2, var3, var5);
   }

   public Document createDocument(String var1, String var2, String var3, XMLReader var4) throws IOException {
      var4.setContentHandler(this);
      var4.setDTDHandler(this);
      var4.setEntityResolver(this);

      try {
         var4.parse(var3);
      } catch (SAXException var7) {
         Exception var6 = var7.getException();
         if (var6 != null && var6 instanceof InterruptedIOException) {
            throw (InterruptedIOException)var6;
         }

         throw new SAXIOException(var7);
      }

      this.currentNode = null;
      Document var5 = this.document;
      this.document = null;
      return var5;
   }

   public Document createDocument(String var1, Reader var2) throws IOException {
      InputSource var3 = new InputSource(var2);
      var3.setSystemId(var1);
      return this.createDocument(var3);
   }

   protected Document createDocument(String var1, String var2, String var3, InputSource var4) throws IOException {
      Document var5 = this.createDocument(var4);
      Element var6 = var5.getDocumentElement();
      String var7 = var2;
      String var8 = var1;
      if (var1 == null) {
         int var9 = var2.indexOf(58);
         String var10 = var9 != -1 && var9 != var2.length() - 1 ? var2.substring(0, var9) : "";
         var8 = this.namespaces.get(var10);
         if (var9 != -1 && var9 != var2.length() - 1) {
            var7 = var2.substring(var9 + 1);
         }
      }

      String var11 = var6.getNamespaceURI();
      if (var11 != var8 && (var11 == null || !var11.equals(var8))) {
         throw new IOException("Root element namespace does not match that requested:\nRequested: " + var8 + "\n" + "Found: " + var11);
      } else {
         if (var11 != null) {
            if (!var6.getLocalName().equals(var7)) {
               throw new IOException("Root element does not match that requested:\nRequested: " + var7 + "\n" + "Found: " + var6.getLocalName());
            }
         } else if (!var6.getNodeName().equals(var7)) {
            throw new IOException("Root element does not match that requested:\nRequested: " + var7 + "\n" + "Found: " + var6.getNodeName());
         }

         return var5;
      }
   }

   protected Document createDocument(InputSource var1) throws IOException {
      try {
         if (this.parserClassName != null) {
            this.parser = XMLReaderFactory.createXMLReader(this.parserClassName);
         } else {
            SAXParser var2;
            try {
               var2 = saxFactory.newSAXParser();
            } catch (ParserConfigurationException var4) {
               throw new IOException("Could not create SAXParser: " + var4.getMessage());
            }

            this.parser = var2.getXMLReader();
         }

         this.parser.setContentHandler(this);
         this.parser.setDTDHandler(this);
         this.parser.setEntityResolver(this);
         this.parser.setErrorHandler((ErrorHandler)(this.errorHandler == null ? this : this.errorHandler));
         this.parser.setFeature("http://xml.org/sax/features/namespaces", true);
         this.parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
         this.parser.setFeature("http://xml.org/sax/features/validation", this.isValidating);
         this.parser.setProperty("http://xml.org/sax/properties/lexical-handler", this);
         this.parser.parse(var1);
      } catch (SAXException var5) {
         Exception var3 = var5.getException();
         if (var3 != null && var3 instanceof InterruptedIOException) {
            throw (InterruptedIOException)var3;
         }

         throw new SAXIOException(var5);
      }

      this.currentNode = null;
      Document var6 = this.document;
      this.document = null;
      this.locator = null;
      this.parser = null;
      return var6;
   }

   public DocumentDescriptor getDocumentDescriptor() {
      return this.documentDescriptor;
   }

   public void setDocumentLocator(Locator var1) {
      this.locator = var1;
   }

   public void setValidating(boolean var1) {
      this.isValidating = var1;
   }

   public boolean isValidating() {
      return this.isValidating;
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public DOMImplementation getDOMImplementation(String var1) {
      return this.implementation;
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      throw var1;
   }

   public void error(SAXParseException var1) throws SAXException {
      throw var1;
   }

   public void warning(SAXParseException var1) throws SAXException {
   }

   public void startDocument() throws SAXException {
      this.preInfo = new LinkedList();
      this.namespaces = new HashTableStack();
      this.namespaces.put("xml", "http://www.w3.org/XML/1998/namespace");
      this.namespaces.put("xmlns", "http://www.w3.org/2000/xmlns/");
      this.namespaces.put("", (String)null);
      this.inDTD = false;
      this.inCDATA = false;
      this.inProlog = true;
      this.currentNode = null;
      this.document = null;
      this.isStandalone = false;
      this.xmlVersion = "1.0";
      this.stringBuffer.setLength(0);
      this.stringContent = false;
      if (this.createDocumentDescriptor) {
         this.documentDescriptor = new DocumentDescriptor();
      } else {
         this.documentDescriptor = null;
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (HaltingThread.hasBeenHalted()) {
         throw new SAXException(new InterruptedIOException());
      } else {
         if (this.inProlog) {
            this.inProlog = false;

            try {
               this.isStandalone = this.parser.getFeature("http://xml.org/sax/features/is-standalone");
            } catch (SAXNotRecognizedException var15) {
            }

            try {
               this.xmlVersion = (String)this.parser.getProperty("http://xml.org/sax/properties/document-xml-version");
            } catch (SAXNotRecognizedException var14) {
            }
         }

         int var5 = var4.getLength();
         this.namespaces.push();
         String var6 = null;

         String var10;
         for(int var7 = 0; var7 < var5; ++var7) {
            String var8 = var4.getQName(var7);
            int var9 = var8.length();
            if (var9 >= 5) {
               if (var8.equals("version")) {
                  var6 = var4.getValue(var7);
               } else if (var8.startsWith("xmlns")) {
                  if (var9 == 5) {
                     var10 = var4.getValue(var7);
                     if (var10.length() == 0) {
                        var10 = null;
                     }

                     this.namespaces.put("", var10);
                  } else if (var8.charAt(5) == ':') {
                     var10 = var4.getValue(var7);
                     if (var10.length() == 0) {
                        var10 = null;
                     }

                     this.namespaces.put(var8.substring(6), var10);
                  }
               }
            }
         }

         this.appendStringData();
         int var17 = var3.indexOf(58);
         String var18 = var17 != -1 && var17 != var3.length() - 1 ? var3.substring(0, var17) : "";
         var10 = this.namespaces.get(var18);
         Element var16;
         if (this.currentNode == null) {
            this.implementation = this.getDOMImplementation(var6);
            this.document = this.implementation.createDocument(var10, var3, (DocumentType)null);
            Iterator var11 = this.preInfo.iterator();
            this.currentNode = var16 = this.document.getDocumentElement();

            while(var11.hasNext()) {
               PreInfo var12 = (PreInfo)var11.next();
               Node var13 = var12.createNode(this.document);
               this.document.insertBefore(var13, var16);
            }

            this.preInfo = null;
         } else {
            var16 = this.document.createElementNS(var10, var3);
            this.currentNode.appendChild(var16);
            this.currentNode = var16;
         }

         if (this.createDocumentDescriptor && this.locator != null) {
            this.documentDescriptor.setLocation(var16, this.locator.getLineNumber(), this.locator.getColumnNumber());
         }

         for(int var19 = 0; var19 < var5; ++var19) {
            String var20 = var4.getQName(var19);
            if (var20.equals("xmlns")) {
               var16.setAttributeNS("http://www.w3.org/2000/xmlns/", var20, var4.getValue(var19));
            } else {
               var17 = var20.indexOf(58);
               var10 = var17 == -1 ? null : this.namespaces.get(var20.substring(0, var17));
               var16.setAttributeNS(var10, var20, var4.getValue(var19));
            }
         }

      }
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.appendStringData();
      if (this.currentNode != null) {
         this.currentNode = this.currentNode.getParentNode();
      }

      this.namespaces.pop();
   }

   public void appendStringData() {
      if (this.stringContent) {
         String var1 = this.stringBuffer.toString();
         this.stringBuffer.setLength(0);
         this.stringContent = false;
         if (this.currentNode == null) {
            if (this.inCDATA) {
               this.preInfo.add(new CDataInfo(var1));
            } else {
               this.preInfo.add(new TextInfo(var1));
            }
         } else {
            Object var2;
            if (this.inCDATA) {
               var2 = this.document.createCDATASection(var1);
            } else {
               var2 = this.document.createTextNode(var1);
            }

            this.currentNode.appendChild((Node)var2);
         }

      }
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.stringBuffer.append(var1, var2, var3);
      this.stringContent = true;
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      this.stringBuffer.append(var1, var2, var3);
      this.stringContent = true;
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (!this.inDTD) {
         this.appendStringData();
         if (this.currentNode == null) {
            this.preInfo.add(new ProcessingInstructionInfo(var1, var2));
         } else {
            this.currentNode.appendChild(this.document.createProcessingInstruction(var1, var2));
         }

      }
   }

   public void startDTD(String var1, String var2, String var3) throws SAXException {
      this.appendStringData();
      this.inDTD = true;
   }

   public void endDTD() throws SAXException {
      this.inDTD = false;
   }

   public void startEntity(String var1) throws SAXException {
   }

   public void endEntity(String var1) throws SAXException {
   }

   public void startCDATA() throws SAXException {
      this.appendStringData();
      this.inCDATA = true;
      this.stringContent = true;
   }

   public void endCDATA() throws SAXException {
      this.appendStringData();
      this.inCDATA = false;
   }

   public void comment(char[] var1, int var2, int var3) throws SAXException {
      if (!this.inDTD) {
         this.appendStringData();
         String var4 = new String(var1, var2, var3);
         if (this.currentNode == null) {
            this.preInfo.add(new CommentInfo(var4));
         } else {
            this.currentNode.appendChild(this.document.createComment(var4));
         }

      }
   }

   static class TextInfo implements PreInfo {
      public String text;

      public TextInfo(String var1) {
         this.text = var1;
      }

      public Node createNode(Document var1) {
         return var1.createTextNode(this.text);
      }
   }

   static class CDataInfo implements PreInfo {
      public String cdata;

      public CDataInfo(String var1) {
         this.cdata = var1;
      }

      public Node createNode(Document var1) {
         return var1.createCDATASection(this.cdata);
      }
   }

   static class CommentInfo implements PreInfo {
      public String comment;

      public CommentInfo(String var1) {
         this.comment = var1;
      }

      public Node createNode(Document var1) {
         return var1.createComment(this.comment);
      }
   }

   static class ProcessingInstructionInfo implements PreInfo {
      public String target;
      public String data;

      public ProcessingInstructionInfo(String var1, String var2) {
         this.target = var1;
         this.data = var2;
      }

      public Node createNode(Document var1) {
         return var1.createProcessingInstruction(this.target, this.data);
      }
   }

   protected interface PreInfo {
      Node createNode(Document var1);
   }
}
