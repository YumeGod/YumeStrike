package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EmptySerializer implements SerializationHandler {
   protected static final String ERR = "EmptySerializer method not over-ridden";

   protected void couldThrowIOException() throws IOException {
   }

   protected void couldThrowSAXException() throws SAXException {
   }

   protected void couldThrowSAXException(char[] chars, int off, int len) throws SAXException {
   }

   protected void couldThrowSAXException(String elemQName) throws SAXException {
   }

   protected void couldThrowException() throws Exception {
   }

   void aMethodIsCalled() {
   }

   public ContentHandler asContentHandler() throws IOException {
      this.couldThrowIOException();
      return null;
   }

   public void setContentHandler(ContentHandler ch) {
      this.aMethodIsCalled();
   }

   public void close() {
      this.aMethodIsCalled();
   }

   public Properties getOutputFormat() {
      this.aMethodIsCalled();
      return null;
   }

   public OutputStream getOutputStream() {
      this.aMethodIsCalled();
      return null;
   }

   public Writer getWriter() {
      this.aMethodIsCalled();
      return null;
   }

   public boolean reset() {
      this.aMethodIsCalled();
      return false;
   }

   public void serialize(Node node) throws IOException {
      this.couldThrowIOException();
   }

   public void setCdataSectionElements(Vector URI_and_localNames) {
      this.aMethodIsCalled();
   }

   public boolean setEscaping(boolean escape) throws SAXException {
      this.couldThrowSAXException();
      return false;
   }

   public void setIndent(boolean indent) {
      this.aMethodIsCalled();
   }

   public void setIndentAmount(int spaces) {
      this.aMethodIsCalled();
   }

   public void setOutputFormat(Properties format) {
      this.aMethodIsCalled();
   }

   public void setOutputStream(OutputStream output) {
      this.aMethodIsCalled();
   }

   public void setVersion(String version) {
      this.aMethodIsCalled();
   }

   public void setWriter(Writer writer) {
      this.aMethodIsCalled();
   }

   public void setTransformer(Transformer transformer) {
      this.aMethodIsCalled();
   }

   public Transformer getTransformer() {
      this.aMethodIsCalled();
      return null;
   }

   public void flushPending() throws SAXException {
      this.couldThrowSAXException();
   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
      this.couldThrowSAXException();
   }

   public void addAttributes(Attributes atts) throws SAXException {
      this.couldThrowSAXException();
   }

   public void addAttribute(String name, String value) {
      this.aMethodIsCalled();
   }

   public void characters(String chars) throws SAXException {
      this.couldThrowSAXException();
   }

   public void endElement(String elemName) throws SAXException {
      this.couldThrowSAXException();
   }

   public void startDocument() throws SAXException {
      this.couldThrowSAXException();
   }

   public void startElement(String uri, String localName, String qName) throws SAXException {
      this.couldThrowSAXException(qName);
   }

   public void startElement(String qName) throws SAXException {
      this.couldThrowSAXException(qName);
   }

   public void namespaceAfterStartElement(String uri, String prefix) throws SAXException {
      this.couldThrowSAXException();
   }

   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
      this.couldThrowSAXException();
      return false;
   }

   public void entityReference(String entityName) throws SAXException {
      this.couldThrowSAXException();
   }

   public NamespaceMappings getNamespaceMappings() {
      this.aMethodIsCalled();
      return null;
   }

   public String getPrefix(String uri) {
      this.aMethodIsCalled();
      return null;
   }

   public String getNamespaceURI(String name, boolean isElement) {
      this.aMethodIsCalled();
      return null;
   }

   public String getNamespaceURIFromPrefix(String prefix) {
      this.aMethodIsCalled();
      return null;
   }

   public void setDocumentLocator(Locator arg0) {
      this.aMethodIsCalled();
   }

   public void endDocument() throws SAXException {
      this.couldThrowSAXException();
   }

   public void startPrefixMapping(String arg0, String arg1) throws SAXException {
      this.couldThrowSAXException();
   }

   public void endPrefixMapping(String arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
      this.couldThrowSAXException();
   }

   public void endElement(String arg0, String arg1, String arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
      this.couldThrowSAXException(arg0, arg1, arg2);
   }

   public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public void processingInstruction(String arg0, String arg1) throws SAXException {
      this.couldThrowSAXException();
   }

   public void skippedEntity(String arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void comment(String comment) throws SAXException {
      this.couldThrowSAXException();
   }

   public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public void endDTD() throws SAXException {
      this.couldThrowSAXException();
   }

   public void startEntity(String arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void endEntity(String arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void startCDATA() throws SAXException {
      this.couldThrowSAXException();
   }

   public void endCDATA() throws SAXException {
      this.couldThrowSAXException();
   }

   public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public String getDoctypePublic() {
      this.aMethodIsCalled();
      return null;
   }

   public String getDoctypeSystem() {
      this.aMethodIsCalled();
      return null;
   }

   public String getEncoding() {
      this.aMethodIsCalled();
      return null;
   }

   public boolean getIndent() {
      this.aMethodIsCalled();
      return false;
   }

   public int getIndentAmount() {
      this.aMethodIsCalled();
      return 0;
   }

   public String getMediaType() {
      this.aMethodIsCalled();
      return null;
   }

   public boolean getOmitXMLDeclaration() {
      this.aMethodIsCalled();
      return false;
   }

   public String getStandalone() {
      this.aMethodIsCalled();
      return null;
   }

   public String getVersion() {
      this.aMethodIsCalled();
      return null;
   }

   public void setCdataSectionElements(Hashtable h) throws Exception {
      this.couldThrowException();
   }

   public void setDoctype(String system, String pub) {
      this.aMethodIsCalled();
   }

   public void setDoctypePublic(String doctype) {
      this.aMethodIsCalled();
   }

   public void setDoctypeSystem(String doctype) {
      this.aMethodIsCalled();
   }

   public void setEncoding(String encoding) {
      this.aMethodIsCalled();
   }

   public void setMediaType(String mediatype) {
      this.aMethodIsCalled();
   }

   public void setOmitXMLDeclaration(boolean b) {
      this.aMethodIsCalled();
   }

   public void setStandalone(String standalone) {
      this.aMethodIsCalled();
   }

   public void elementDecl(String arg0, String arg1) throws SAXException {
      this.couldThrowSAXException();
   }

   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
      this.couldThrowSAXException();
   }

   public void internalEntityDecl(String arg0, String arg1) throws SAXException {
      this.couldThrowSAXException();
   }

   public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public void warning(SAXParseException arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void error(SAXParseException arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public void fatalError(SAXParseException arg0) throws SAXException {
      this.couldThrowSAXException();
   }

   public DOMSerializer asDOMSerializer() throws IOException {
      this.couldThrowIOException();
      return null;
   }

   public void setNamespaceMappings(NamespaceMappings mappings) {
      this.aMethodIsCalled();
   }

   public void setSourceLocator(SourceLocator locator) {
      this.aMethodIsCalled();
   }

   public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
      this.couldThrowSAXException();
   }

   public void characters(Node node) throws SAXException {
      this.couldThrowSAXException();
   }

   public void addXSLAttribute(String qName, String value, String uri) {
      this.aMethodIsCalled();
   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException {
      this.couldThrowSAXException();
   }

   public void notationDecl(String arg0, String arg1, String arg2) throws SAXException {
      this.couldThrowSAXException();
   }

   public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3) throws SAXException {
      this.couldThrowSAXException();
   }

   public void setDTDEntityExpansion(boolean expand) {
      this.aMethodIsCalled();
   }
}
