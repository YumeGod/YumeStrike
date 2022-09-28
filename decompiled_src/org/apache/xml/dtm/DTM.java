package org.apache.xml.dtm;

import javax.xml.transform.SourceLocator;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public interface DTM {
   int NULL = -1;
   short ROOT_NODE = 0;
   short ELEMENT_NODE = 1;
   short ATTRIBUTE_NODE = 2;
   short TEXT_NODE = 3;
   short CDATA_SECTION_NODE = 4;
   short ENTITY_REFERENCE_NODE = 5;
   short ENTITY_NODE = 6;
   short PROCESSING_INSTRUCTION_NODE = 7;
   short COMMENT_NODE = 8;
   short DOCUMENT_NODE = 9;
   short DOCUMENT_TYPE_NODE = 10;
   short DOCUMENT_FRAGMENT_NODE = 11;
   short NOTATION_NODE = 12;
   short NAMESPACE_NODE = 13;
   short NTYPES = 14;

   void setFeature(String var1, boolean var2);

   void setProperty(String var1, Object var2);

   DTMAxisTraverser getAxisTraverser(int var1);

   DTMAxisIterator getAxisIterator(int var1);

   DTMAxisIterator getTypedAxisIterator(int var1, int var2);

   boolean hasChildNodes(int var1);

   int getFirstChild(int var1);

   int getLastChild(int var1);

   int getAttributeNode(int var1, String var2, String var3);

   int getFirstAttribute(int var1);

   int getFirstNamespaceNode(int var1, boolean var2);

   int getNextSibling(int var1);

   int getPreviousSibling(int var1);

   int getNextAttribute(int var1);

   int getNextNamespaceNode(int var1, int var2, boolean var3);

   int getParent(int var1);

   int getDocument();

   int getOwnerDocument(int var1);

   int getDocumentRoot(int var1);

   XMLString getStringValue(int var1);

   int getStringValueChunkCount(int var1);

   char[] getStringValueChunk(int var1, int var2, int[] var3);

   int getExpandedTypeID(int var1);

   int getExpandedTypeID(String var1, String var2, int var3);

   String getLocalNameFromExpandedNameID(int var1);

   String getNamespaceFromExpandedNameID(int var1);

   String getNodeName(int var1);

   String getNodeNameX(int var1);

   String getLocalName(int var1);

   String getPrefix(int var1);

   String getNamespaceURI(int var1);

   String getNodeValue(int var1);

   short getNodeType(int var1);

   short getLevel(int var1);

   boolean isSupported(String var1, String var2);

   String getDocumentBaseURI();

   void setDocumentBaseURI(String var1);

   String getDocumentSystemIdentifier(int var1);

   String getDocumentEncoding(int var1);

   String getDocumentStandalone(int var1);

   String getDocumentVersion(int var1);

   boolean getDocumentAllDeclarationsProcessed();

   String getDocumentTypeDeclarationSystemIdentifier();

   String getDocumentTypeDeclarationPublicIdentifier();

   int getElementById(String var1);

   String getUnparsedEntityURI(String var1);

   boolean supportsPreStripping();

   boolean isNodeAfter(int var1, int var2);

   boolean isCharacterElementContentWhitespace(int var1);

   boolean isDocumentAllDeclarationsProcessed(int var1);

   boolean isAttributeSpecified(int var1);

   void dispatchCharactersEvents(int var1, ContentHandler var2, boolean var3) throws SAXException;

   void dispatchToEvents(int var1, ContentHandler var2) throws SAXException;

   Node getNode(int var1);

   boolean needsTwoThreads();

   ContentHandler getContentHandler();

   LexicalHandler getLexicalHandler();

   EntityResolver getEntityResolver();

   DTDHandler getDTDHandler();

   ErrorHandler getErrorHandler();

   DeclHandler getDeclHandler();

   void appendChild(int var1, boolean var2, boolean var3);

   void appendTextChild(String var1);

   SourceLocator getSourceLocatorFor(int var1);

   void documentRegistration();

   void documentRelease();

   void migrateTo(DTMManager var1);
}
