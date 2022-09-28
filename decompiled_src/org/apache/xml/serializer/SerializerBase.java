package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.apache.xml.serializer.utils.Utils;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class SerializerBase implements SerializationHandler, SerializerConstants {
   protected boolean m_needToCallStartDocument = true;
   protected boolean m_cdataTagOpen = false;
   protected AttributesImplSerializer m_attributes = new AttributesImplSerializer();
   protected boolean m_inEntityRef = false;
   protected boolean m_inExternalDTD = false;
   private String m_doctypeSystem;
   private String m_doctypePublic;
   boolean m_needToOutputDocTypeDecl = true;
   private String m_encoding = null;
   private boolean m_shouldNotWriteXMLHeader = false;
   private String m_standalone;
   protected boolean m_standaloneWasSpecified = false;
   protected boolean m_doIndent = false;
   protected int m_indentAmount = 0;
   private String m_version = null;
   private String m_mediatype;
   private Transformer m_transformer;
   protected Vector m_cdataSectionElements = null;
   protected NamespaceMappings m_prefixMap;
   protected SerializerTrace m_tracer;
   protected SourceLocator m_sourceLocator;
   protected Writer m_writer = null;
   protected ElemContext m_elemContext = new ElemContext();
   protected char[] m_charsBuff = new char[60];
   protected char[] m_attrBuff = new char[30];

   protected void fireEndElem(String name) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(4, name, (Attributes)((Attributes)null));
      }

   }

   protected void fireCharEvent(char[] chars, int start, int length) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(5, chars, start, length);
      }

   }

   public void comment(String data) throws SAXException {
      int length = data.length();
      if (length > this.m_charsBuff.length) {
         this.m_charsBuff = new char[length * 2 + 1];
      }

      data.getChars(0, length, this.m_charsBuff, 0);
      this.comment(this.m_charsBuff, 0, length);
   }

   protected String patchName(String qname) {
      int lastColon = qname.lastIndexOf(58);
      if (lastColon > 0) {
         int firstColon = qname.indexOf(58);
         String prefix = qname.substring(0, firstColon);
         String localName = qname.substring(lastColon + 1);
         String uri = this.m_prefixMap.lookupNamespace(prefix);
         if (uri != null && uri.length() == 0) {
            return localName;
         }

         if (firstColon != lastColon) {
            return prefix + ':' + localName;
         }
      }

      return qname;
   }

   protected static String getLocalName(String qname) {
      int col = qname.lastIndexOf(58);
      return col > 0 ? qname.substring(col + 1) : qname;
   }

   public void setDocumentLocator(Locator locator) {
   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
      if (this.m_elemContext.m_startTagOpen) {
         this.addAttributeAlways(uri, localName, rawName, type, value, XSLAttribute);
      }

   }

   public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {
      int index;
      if (localName != null && uri != null && uri.length() != 0) {
         index = this.m_attributes.getIndex(uri, localName);
      } else {
         index = this.m_attributes.getIndex(rawName);
      }

      boolean was_added;
      if (index >= 0) {
         this.m_attributes.setValue(index, value);
         was_added = false;
      } else {
         this.m_attributes.addAttribute(uri, localName, rawName, type, value);
         was_added = true;
      }

      return was_added;
   }

   public void addAttribute(String name, String value) {
      if (this.m_elemContext.m_startTagOpen) {
         String patchedName = this.patchName(name);
         String localName = getLocalName(patchedName);
         String uri = this.getNamespaceURI(patchedName, false);
         this.addAttributeAlways(uri, localName, patchedName, "CDATA", value, false);
      }

   }

   public void addXSLAttribute(String name, String value, String uri) {
      if (this.m_elemContext.m_startTagOpen) {
         String patchedName = this.patchName(name);
         String localName = getLocalName(patchedName);
         this.addAttributeAlways(uri, localName, patchedName, "CDATA", value, true);
      }

   }

   public void addAttributes(Attributes atts) throws SAXException {
      int nAtts = atts.getLength();

      for(int i = 0; i < nAtts; ++i) {
         String uri = atts.getURI(i);
         if (null == uri) {
            uri = "";
         }

         this.addAttributeAlways(uri, atts.getLocalName(i), atts.getQName(i), atts.getType(i), atts.getValue(i), false);
      }

   }

   public ContentHandler asContentHandler() throws IOException {
      return this;
   }

   public void endEntity(String name) throws SAXException {
      if (name.equals("[dtd]")) {
         this.m_inExternalDTD = false;
      }

      this.m_inEntityRef = false;
      if (this.m_tracer != null) {
         this.fireEndEntity(name);
      }

   }

   public void close() {
   }

   protected void initCDATA() {
   }

   public String getEncoding() {
      return this.m_encoding;
   }

   public void setEncoding(String m_encoding) {
      this.m_encoding = m_encoding;
   }

   public void setOmitXMLDeclaration(boolean b) {
      this.m_shouldNotWriteXMLHeader = b;
   }

   public boolean getOmitXMLDeclaration() {
      return this.m_shouldNotWriteXMLHeader;
   }

   public String getDoctypePublic() {
      return this.m_doctypePublic;
   }

   public void setDoctypePublic(String doctypePublic) {
      this.m_doctypePublic = doctypePublic;
   }

   public String getDoctypeSystem() {
      return this.m_doctypeSystem;
   }

   public void setDoctypeSystem(String doctypeSystem) {
      this.m_doctypeSystem = doctypeSystem;
   }

   public void setDoctype(String doctypeSystem, String doctypePublic) {
      this.m_doctypeSystem = doctypeSystem;
      this.m_doctypePublic = doctypePublic;
   }

   public void setStandalone(String standalone) {
      if (standalone != null) {
         this.m_standaloneWasSpecified = true;
         this.setStandaloneInternal(standalone);
      }

   }

   protected void setStandaloneInternal(String standalone) {
      if ("yes".equals(standalone)) {
         this.m_standalone = "yes";
      } else {
         this.m_standalone = "no";
      }

   }

   public String getStandalone() {
      return this.m_standalone;
   }

   public boolean getIndent() {
      return this.m_doIndent;
   }

   public String getMediaType() {
      return this.m_mediatype;
   }

   public String getVersion() {
      return this.m_version;
   }

   public void setVersion(String version) {
      this.m_version = version;
   }

   public void setMediaType(String mediaType) {
      this.m_mediatype = mediaType;
   }

   public int getIndentAmount() {
      return this.m_indentAmount;
   }

   public void setIndentAmount(int m_indentAmount) {
      this.m_indentAmount = m_indentAmount;
   }

   public void setIndent(boolean doIndent) {
      this.m_doIndent = doIndent;
   }

   public void namespaceAfterStartElement(String uri, String prefix) throws SAXException {
   }

   public DOMSerializer asDOMSerializer() throws IOException {
      return this;
   }

   protected boolean isCdataSection() {
      boolean b = false;
      if (null != this.m_cdataSectionElements) {
         if (this.m_elemContext.m_elementLocalName == null) {
            this.m_elemContext.m_elementLocalName = getLocalName(this.m_elemContext.m_elementName);
         }

         if (this.m_elemContext.m_elementURI == null) {
            String prefix = getPrefixPart(this.m_elemContext.m_elementName);
            if (prefix != null) {
               this.m_elemContext.m_elementURI = this.m_prefixMap.lookupNamespace(prefix);
            }
         }

         if (null != this.m_elemContext.m_elementURI && this.m_elemContext.m_elementURI.length() == 0) {
            this.m_elemContext.m_elementURI = null;
         }

         int nElems = this.m_cdataSectionElements.size();

         for(int i = 0; i < nElems; i += 2) {
            String uri = (String)this.m_cdataSectionElements.elementAt(i);
            String loc = (String)this.m_cdataSectionElements.elementAt(i + 1);
            if (loc.equals(this.m_elemContext.m_elementLocalName) && subPartMatch(this.m_elemContext.m_elementURI, uri)) {
               b = true;
               break;
            }
         }
      }

      return b;
   }

   private static final boolean subPartMatch(String p, String t) {
      return p == t || null != p && p.equals(t);
   }

   protected static final String getPrefixPart(String qname) {
      int col = qname.indexOf(58);
      return col > 0 ? qname.substring(0, col) : null;
   }

   public NamespaceMappings getNamespaceMappings() {
      return this.m_prefixMap;
   }

   public String getPrefix(String namespaceURI) {
      String prefix = this.m_prefixMap.lookupPrefix(namespaceURI);
      return prefix;
   }

   public String getNamespaceURI(String qname, boolean isElement) {
      String uri = "";
      int col = qname.lastIndexOf(58);
      String prefix = col > 0 ? qname.substring(0, col) : "";
      if ((!"".equals(prefix) || isElement) && this.m_prefixMap != null) {
         uri = this.m_prefixMap.lookupNamespace(prefix);
         if (uri == null && !prefix.equals("xmlns")) {
            throw new RuntimeException(Utils.messages.createMessage("ER_NAMESPACE_PREFIX", new Object[]{qname.substring(0, col)}));
         }
      }

      return uri;
   }

   public String getNamespaceURIFromPrefix(String prefix) {
      String uri = null;
      if (this.m_prefixMap != null) {
         uri = this.m_prefixMap.lookupNamespace(prefix);
      }

      return uri;
   }

   public void entityReference(String name) throws SAXException {
      this.flushPending();
      this.startEntity(name);
      this.endEntity(name);
      if (this.m_tracer != null) {
         this.fireEntityReference(name);
      }

   }

   public void setTransformer(Transformer t) {
      this.m_transformer = t;
      if (this.m_transformer instanceof SerializerTrace && ((SerializerTrace)this.m_transformer).hasTraceListeners()) {
         this.m_tracer = (SerializerTrace)this.m_transformer;
      } else {
         this.m_tracer = null;
      }

   }

   public Transformer getTransformer() {
      return this.m_transformer;
   }

   public void characters(Node node) throws SAXException {
      this.flushPending();
      String data = node.getNodeValue();
      if (data != null) {
         int length = data.length();
         if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[length * 2 + 1];
         }

         data.getChars(0, length, this.m_charsBuff, 0);
         this.characters(this.m_charsBuff, 0, length);
      }

   }

   public void error(SAXParseException exc) throws SAXException {
   }

   public void fatalError(SAXParseException exc) throws SAXException {
      this.m_elemContext.m_startTagOpen = false;
   }

   public void warning(SAXParseException exc) throws SAXException {
   }

   protected void fireStartEntity(String name) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(9, name);
      }

   }

   private void flushMyWriter() {
      if (this.m_writer != null) {
         try {
            this.m_writer.flush();
         } catch (IOException var2) {
         }
      }

   }

   protected void fireCDATAEvent(char[] chars, int start, int length) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(10, chars, start, length);
      }

   }

   protected void fireCommentEvent(char[] chars, int start, int length) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(8, new String(chars, start, length));
      }

   }

   public void fireEndEntity(String name) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
      }

   }

   protected void fireStartDoc() throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(1);
      }

   }

   protected void fireEndDoc() throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(2);
      }

   }

   protected void fireStartElem(String elemName) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(3, elemName, (Attributes)this.m_attributes);
      }

   }

   protected void fireEscapingEvent(String name, String data) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(7, name, (String)data);
      }

   }

   protected void fireEntityReference(String name) throws SAXException {
      if (this.m_tracer != null) {
         this.flushMyWriter();
         this.m_tracer.fireGenerateEvent(9, name, (Attributes)((Attributes)null));
      }

   }

   public void startDocument() throws SAXException {
      this.startDocumentInternal();
      this.m_needToCallStartDocument = false;
   }

   protected void startDocumentInternal() throws SAXException {
      if (this.m_tracer != null) {
         this.fireStartDoc();
      }

   }

   public void setSourceLocator(SourceLocator locator) {
      this.m_sourceLocator = locator;
   }

   public void setNamespaceMappings(NamespaceMappings mappings) {
      this.m_prefixMap = mappings;
   }

   public boolean reset() {
      this.resetSerializerBase();
      return true;
   }

   private void resetSerializerBase() {
      this.m_attributes.clear();
      this.m_cdataSectionElements = null;
      this.m_elemContext = new ElemContext();
      this.m_doctypePublic = null;
      this.m_doctypeSystem = null;
      this.m_doIndent = false;
      this.m_encoding = null;
      this.m_indentAmount = 0;
      this.m_inEntityRef = false;
      this.m_inExternalDTD = false;
      this.m_mediatype = null;
      this.m_needToCallStartDocument = true;
      this.m_needToOutputDocTypeDecl = false;
      if (this.m_prefixMap != null) {
         this.m_prefixMap.reset();
      }

      this.m_shouldNotWriteXMLHeader = false;
      this.m_sourceLocator = null;
      this.m_standalone = null;
      this.m_standaloneWasSpecified = false;
      this.m_tracer = null;
      this.m_transformer = null;
      this.m_version = null;
   }

   final boolean inTemporaryOutputState() {
      return this.getEncoding() == null;
   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException {
      if (this.m_elemContext.m_startTagOpen) {
         this.addAttributeAlways(uri, localName, rawName, type, value, false);
      }

   }

   public void notationDecl(String arg0, String arg1, String arg2) throws SAXException {
   }

   public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3) throws SAXException {
   }

   public void setDTDEntityExpansion(boolean expand) {
   }

   public abstract void flushPending() throws SAXException;

   public abstract boolean setEscaping(boolean var1) throws SAXException;

   public abstract void serialize(Node var1) throws IOException;

   public abstract void setContentHandler(ContentHandler var1);

   public abstract void addUniqueAttribute(String var1, String var2, int var3) throws SAXException;

   public abstract boolean startPrefixMapping(String var1, String var2, boolean var3) throws SAXException;

   public abstract void startElement(String var1) throws SAXException;

   public abstract void startElement(String var1, String var2, String var3) throws SAXException;

   public abstract void endElement(String var1) throws SAXException;

   public abstract void characters(String var1) throws SAXException;

   public abstract void skippedEntity(String var1) throws SAXException;

   public abstract void processingInstruction(String var1, String var2) throws SAXException;

   public abstract void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException;

   public abstract void characters(char[] var1, int var2, int var3) throws SAXException;

   public abstract void endElement(String var1, String var2, String var3) throws SAXException;

   public abstract void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException;

   public abstract void endPrefixMapping(String var1) throws SAXException;

   public abstract void startPrefixMapping(String var1, String var2) throws SAXException;

   public abstract void endDocument() throws SAXException;

   public abstract void comment(char[] var1, int var2, int var3) throws SAXException;

   public abstract void endCDATA() throws SAXException;

   public abstract void startCDATA() throws SAXException;

   public abstract void startEntity(String var1) throws SAXException;

   public abstract void endDTD() throws SAXException;

   public abstract void startDTD(String var1, String var2, String var3) throws SAXException;

   public abstract void setCdataSectionElements(Vector var1);

   public abstract void externalEntityDecl(String var1, String var2, String var3) throws SAXException;

   public abstract void internalEntityDecl(String var1, String var2) throws SAXException;

   public abstract void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException;

   public abstract void elementDecl(String var1, String var2) throws SAXException;

   public abstract Properties getOutputFormat();

   public abstract void setOutputFormat(Properties var1);

   public abstract Writer getWriter();

   public abstract void setWriter(Writer var1);

   public abstract OutputStream getOutputStream();

   public abstract void setOutputStream(OutputStream var1);
}
