package org.apache.xalan.processor;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TemplatesHandler;
import org.apache.xalan.extensions.ExpressionVisitor;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.NamespaceSupport2;
import org.apache.xml.utils.NodeConsumer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xpath.XPath;
import org.apache.xpath.compiler.FunctionTable;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

public class StylesheetHandler extends DefaultHandler implements TemplatesHandler, PrefixResolver, NodeConsumer {
   private FunctionTable m_funcTable = new FunctionTable();
   private boolean m_optimize = true;
   private boolean m_incremental = false;
   private boolean m_source_location = false;
   private int m_stylesheetLevel = -1;
   private boolean m_parsingComplete = false;
   private Vector m_prefixMappings = new Vector();
   private boolean m_shouldProcess = true;
   private String m_fragmentIDString;
   private int m_elementID = 0;
   private int m_fragmentID = 0;
   private TransformerFactoryImpl m_stylesheetProcessor;
   public static final int STYPE_ROOT = 1;
   public static final int STYPE_INCLUDE = 2;
   public static final int STYPE_IMPORT = 3;
   private int m_stylesheetType = 1;
   private Stack m_stylesheets = new Stack();
   StylesheetRoot m_stylesheetRoot;
   Stylesheet m_lastPoppedStylesheet;
   private Stack m_processors = new Stack();
   private XSLTSchema m_schema = new XSLTSchema();
   private Stack m_elems = new Stack();
   private int m_docOrderCount = 0;
   Stack m_baseIdentifiers = new Stack();
   private Stack m_stylesheetLocatorStack = new Stack();
   private Stack m_importStack = new Stack();
   private boolean warnedAboutOldXSLTNamespace = false;
   Stack m_nsSupportStack = new Stack();
   private Node m_originatingNode;
   private BoolStack m_spacePreserveStack = new BoolStack();
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$FuncDocument;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$FuncFormatNumb;

   public StylesheetHandler(TransformerFactoryImpl processor) throws TransformerConfigurationException {
      Class func = class$org$apache$xalan$templates$FuncDocument == null ? (class$org$apache$xalan$templates$FuncDocument = class$("org.apache.xalan.templates.FuncDocument")) : class$org$apache$xalan$templates$FuncDocument;
      this.m_funcTable.installFunction("document", func);
      func = class$org$apache$xalan$templates$FuncFormatNumb == null ? (class$org$apache$xalan$templates$FuncFormatNumb = class$("org.apache.xalan.templates.FuncFormatNumb")) : class$org$apache$xalan$templates$FuncFormatNumb;
      this.m_funcTable.installFunction("format-number", func);
      this.m_optimize = (Boolean)processor.getAttribute("http://xml.apache.org/xalan/features/optimize");
      this.m_incremental = (Boolean)processor.getAttribute("http://xml.apache.org/xalan/features/incremental");
      this.m_source_location = (Boolean)processor.getAttribute("http://xml.apache.org/xalan/properties/source-location");
      this.init(processor);
   }

   void init(TransformerFactoryImpl processor) {
      this.m_stylesheetProcessor = processor;
      this.m_processors.push(this.m_schema.getElementProcessor());
      this.pushNewNamespaceSupport();
   }

   public XPath createXPath(String str, ElemTemplateElement owningTemplate) throws TransformerException {
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();
      XPath xpath = new XPath(str, owningTemplate, this, 0, handler, this.m_funcTable);
      xpath.callVisitors(xpath, new ExpressionVisitor(this.getStylesheetRoot()));
      return xpath;
   }

   XPath createMatchPatternXPath(String str, ElemTemplateElement owningTemplate) throws TransformerException {
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();
      XPath xpath = new XPath(str, owningTemplate, this, 1, handler, this.m_funcTable);
      xpath.callVisitors(xpath, new ExpressionVisitor(this.getStylesheetRoot()));
      return xpath;
   }

   public String getNamespaceForPrefix(String prefix) {
      return this.getNamespaceSupport().getURI(prefix);
   }

   public String getNamespaceForPrefix(String prefix, Node context) {
      this.assertion(true, "can't process a context node in StylesheetHandler!");
      return null;
   }

   private boolean stackContains(Stack stack, String url) {
      int n = stack.size();
      boolean contains = false;

      for(int i = 0; i < n; ++i) {
         String url2 = (String)stack.elementAt(i);
         if (url2.equals(url)) {
            contains = true;
            break;
         }
      }

      return contains;
   }

   public Templates getTemplates() {
      return this.getStylesheetRoot();
   }

   public void setSystemId(String baseID) {
      this.pushBaseIndentifier(baseID);
   }

   public String getSystemId() {
      return this.getBaseIdentifier();
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
      return this.getCurrentProcessor().resolveEntity(this, publicId, systemId);
   }

   public void notationDecl(String name, String publicId, String systemId) {
      this.getCurrentProcessor().notationDecl(this, name, publicId, systemId);
   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) {
      this.getCurrentProcessor().unparsedEntityDecl(this, name, publicId, systemId, notationName);
   }

   XSLTElementProcessor getProcessorFor(String uri, String localName, String rawName) throws SAXException {
      XSLTElementProcessor currentProcessor = this.getCurrentProcessor();
      XSLTElementDef def = currentProcessor.getElemDef();
      XSLTElementProcessor elemProcessor = def.getProcessorFor(uri, localName);
      if (null == elemProcessor && !(currentProcessor instanceof ProcessorStylesheetDoc) && (null == this.getStylesheet() || Double.valueOf(this.getStylesheet().getVersion()) > 1.0 || !uri.equals("http://www.w3.org/1999/XSL/Transform") && currentProcessor instanceof ProcessorStylesheetElement || this.getElemVersion() > 1.0)) {
         elemProcessor = def.getProcessorForUnknown(uri, localName);
      }

      if (null == elemProcessor) {
         this.error(XSLMessages.createMessage("ER_NOT_ALLOWED_IN_POSITION", new Object[]{rawName}), (Exception)null);
      }

      return elemProcessor;
   }

   public void setDocumentLocator(Locator locator) {
      this.m_stylesheetLocatorStack.push(new SAXSourceLocator(locator));
   }

   public void startDocument() throws SAXException {
      ++this.m_stylesheetLevel;
      this.pushSpaceHandling(false);
   }

   public boolean isStylesheetParsingComplete() {
      return this.m_parsingComplete;
   }

   public void endDocument() throws SAXException {
      try {
         if (null != this.getStylesheetRoot()) {
            if (0 == this.m_stylesheetLevel) {
               this.getStylesheetRoot().recompose();
            }

            XSLTElementProcessor elemProcessor = this.getCurrentProcessor();
            if (null != elemProcessor) {
               elemProcessor.startNonText(this);
            }

            --this.m_stylesheetLevel;
            this.popSpaceHandling();
            this.m_parsingComplete = this.m_stylesheetLevel < 0;
         } else {
            throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEETROOT", (Object[])null));
         }
      } catch (TransformerException var2) {
         throw new SAXException(var2);
      }
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
      this.m_prefixMappings.addElement(prefix);
      this.m_prefixMappings.addElement(uri);
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   private void flushCharacters() throws SAXException {
      XSLTElementProcessor elemProcessor = this.getCurrentProcessor();
      if (null != elemProcessor) {
         elemProcessor.startNonText(this);
      }

   }

   public void startElement(String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      NamespaceSupport nssupport = this.getNamespaceSupport();
      nssupport.pushContext();
      int n = this.m_prefixMappings.size();

      for(int i = 0; i < n; ++i) {
         String prefix = (String)this.m_prefixMappings.elementAt(i++);
         String nsURI = (String)this.m_prefixMappings.elementAt(i);
         nssupport.declarePrefix(prefix, nsURI);
      }

      this.m_prefixMappings.removeAllElements();
      ++this.m_elementID;
      this.checkForFragmentID(attributes);
      if (this.m_shouldProcess) {
         this.flushCharacters();
         this.pushSpaceHandling(attributes);
         XSLTElementProcessor elemProcessor = this.getProcessorFor(uri, localName, rawName);
         if (null != elemProcessor) {
            this.pushProcessor(elemProcessor);
            elemProcessor.startElement(this, uri, localName, rawName, attributes);
         } else {
            this.m_shouldProcess = false;
            this.popSpaceHandling();
         }

      }
   }

   public void endElement(String uri, String localName, String rawName) throws SAXException {
      --this.m_elementID;
      if (this.m_shouldProcess) {
         if (this.m_elementID + 1 == this.m_fragmentID) {
            this.m_shouldProcess = false;
         }

         this.flushCharacters();
         this.popSpaceHandling();
         XSLTElementProcessor p = this.getCurrentProcessor();
         p.endElement(this, uri, localName, rawName);
         this.popProcessor();
         this.getNamespaceSupport().popContext();
      }
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (this.m_shouldProcess) {
         XSLTElementProcessor elemProcessor = this.getCurrentProcessor();
         XSLTElementDef def = elemProcessor.getElemDef();
         if (def.getType() != 2) {
            elemProcessor = def.getProcessorFor((String)null, "text()");
         }

         if (null == elemProcessor) {
            if (!XMLCharacterRecognizer.isWhiteSpace(ch, start, length)) {
               this.error(XSLMessages.createMessage("ER_NONWHITESPACE_NOT_ALLOWED_IN_POSITION", (Object[])null), (Exception)null);
            }
         } else {
            elemProcessor.characters(this, ch, start, length);
         }

      }
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      if (this.m_shouldProcess) {
         this.getCurrentProcessor().ignorableWhitespace(this, ch, start, length);
      }
   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (this.m_shouldProcess) {
         String prefix = "";
         String ns = "";
         String localName = target;
         int colon = target.indexOf(58);
         if (colon >= 0) {
            ns = this.getNamespaceForPrefix(target.substring(0, colon));
            localName = target.substring(colon + 1);
         }

         try {
            if ("xalan-doc-cache-off".equals(target) || "xalan:doc-cache-off".equals(target) || "doc-cache-off".equals(localName) && ns.equals("org.apache.xalan.xslt.extensions.Redirect")) {
               if (!(this.m_elems.peek() instanceof ElemForEach)) {
                  throw new TransformerException("xalan:doc-cache-off not allowed here!", this.getLocator());
               }

               ElemForEach elem = (ElemForEach)this.m_elems.peek();
               elem.m_doc_cache_off = true;
            }
         } catch (Exception var8) {
         }

         this.flushCharacters();
         this.getCurrentProcessor().processingInstruction(this, target, data);
      }
   }

   public void skippedEntity(String name) throws SAXException {
      if (this.m_shouldProcess) {
         this.getCurrentProcessor().skippedEntity(this, name);
      }
   }

   public void warn(String msg, Object[] args) throws SAXException {
      String formattedMsg = XSLMessages.createWarning(msg, args);
      SAXSourceLocator locator = this.getLocator();
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();

      try {
         if (null != handler) {
            handler.warning(new TransformerException(formattedMsg, locator));
         }

      } catch (TransformerException var7) {
         throw new SAXException(var7);
      }
   }

   private void assertion(boolean condition, String msg) throws RuntimeException {
      if (!condition) {
         throw new RuntimeException(msg);
      }
   }

   protected void error(String msg, Exception e) throws SAXException {
      SAXSourceLocator locator = this.getLocator();
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();
      TransformerException pe;
      if (!(e instanceof TransformerException)) {
         pe = null == e ? new TransformerException(msg, locator) : new TransformerException(msg, locator, e);
      } else {
         pe = (TransformerException)e;
      }

      if (null != handler) {
         try {
            handler.error(pe);
         } catch (TransformerException var7) {
            throw new SAXException(var7);
         }
      } else {
         throw new SAXException(pe);
      }
   }

   protected void error(String msg, Object[] args, Exception e) throws SAXException {
      String formattedMsg = XSLMessages.createMessage(msg, args);
      this.error(formattedMsg, e);
   }

   public void warning(SAXParseException e) throws SAXException {
      String formattedMsg = e.getMessage();
      SAXSourceLocator locator = this.getLocator();
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();

      try {
         handler.warning(new TransformerException(formattedMsg, locator));
      } catch (TransformerException var6) {
         throw new SAXException(var6);
      }
   }

   public void error(SAXParseException e) throws SAXException {
      String formattedMsg = e.getMessage();
      SAXSourceLocator locator = this.getLocator();
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();

      try {
         handler.error(new TransformerException(formattedMsg, locator));
      } catch (TransformerException var6) {
         throw new SAXException(var6);
      }
   }

   public void fatalError(SAXParseException e) throws SAXException {
      String formattedMsg = e.getMessage();
      SAXSourceLocator locator = this.getLocator();
      ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();

      try {
         handler.fatalError(new TransformerException(formattedMsg, locator));
      } catch (TransformerException var6) {
         throw new SAXException(var6);
      }
   }

   private void checkForFragmentID(Attributes attributes) {
      if (!this.m_shouldProcess && null != attributes && null != this.m_fragmentIDString) {
         int n = attributes.getLength();

         for(int i = 0; i < n; ++i) {
            String name = attributes.getQName(i);
            if (name.equals("id")) {
               String val = attributes.getValue(i);
               if (val.equalsIgnoreCase(this.m_fragmentIDString)) {
                  this.m_shouldProcess = true;
                  this.m_fragmentID = this.m_elementID;
               }
            }
         }
      }

   }

   public TransformerFactoryImpl getStylesheetProcessor() {
      return this.m_stylesheetProcessor;
   }

   int getStylesheetType() {
      return this.m_stylesheetType;
   }

   void setStylesheetType(int type) {
      this.m_stylesheetType = type;
   }

   Stylesheet getStylesheet() {
      return this.m_stylesheets.size() == 0 ? null : (Stylesheet)this.m_stylesheets.peek();
   }

   Stylesheet getLastPoppedStylesheet() {
      return this.m_lastPoppedStylesheet;
   }

   public StylesheetRoot getStylesheetRoot() {
      if (this.m_stylesheetRoot != null) {
         this.m_stylesheetRoot.setOptimizer(this.m_optimize);
         this.m_stylesheetRoot.setIncremental(this.m_incremental);
         this.m_stylesheetRoot.setSource_location(this.m_source_location);
      }

      return this.m_stylesheetRoot;
   }

   public void pushStylesheet(Stylesheet s) {
      if (this.m_stylesheets.size() == 0) {
         this.m_stylesheetRoot = (StylesheetRoot)s;
      }

      this.m_stylesheets.push(s);
   }

   Stylesheet popStylesheet() {
      if (!this.m_stylesheetLocatorStack.isEmpty()) {
         this.m_stylesheetLocatorStack.pop();
      }

      if (!this.m_stylesheets.isEmpty()) {
         this.m_lastPoppedStylesheet = (Stylesheet)this.m_stylesheets.pop();
      }

      return this.m_lastPoppedStylesheet;
   }

   XSLTElementProcessor getCurrentProcessor() {
      return (XSLTElementProcessor)this.m_processors.peek();
   }

   void pushProcessor(XSLTElementProcessor processor) {
      this.m_processors.push(processor);
   }

   XSLTElementProcessor popProcessor() {
      return (XSLTElementProcessor)this.m_processors.pop();
   }

   public XSLTSchema getSchema() {
      return this.m_schema;
   }

   ElemTemplateElement getElemTemplateElement() {
      try {
         return (ElemTemplateElement)this.m_elems.peek();
      } catch (EmptyStackException var2) {
         return null;
      }
   }

   int nextUid() {
      return this.m_docOrderCount++;
   }

   void pushElemTemplateElement(ElemTemplateElement elem) {
      if (elem.getUid() == -1) {
         elem.setUid(this.nextUid());
      }

      this.m_elems.push(elem);
   }

   ElemTemplateElement popElemTemplateElement() {
      return (ElemTemplateElement)this.m_elems.pop();
   }

   void pushBaseIndentifier(String baseID) {
      if (null != baseID) {
         int posOfHash = baseID.indexOf(35);
         if (posOfHash > -1) {
            this.m_fragmentIDString = baseID.substring(posOfHash + 1);
            this.m_shouldProcess = false;
         } else {
            this.m_shouldProcess = true;
         }
      } else {
         this.m_shouldProcess = true;
      }

      this.m_baseIdentifiers.push(baseID);
   }

   String popBaseIndentifier() {
      return (String)this.m_baseIdentifiers.pop();
   }

   public String getBaseIdentifier() {
      String base = (String)(this.m_baseIdentifiers.isEmpty() ? null : this.m_baseIdentifiers.peek());
      if (null == base) {
         SourceLocator locator = this.getLocator();
         base = null == locator ? "" : locator.getSystemId();
      }

      return base;
   }

   public SAXSourceLocator getLocator() {
      if (this.m_stylesheetLocatorStack.isEmpty()) {
         SAXSourceLocator locator = new SAXSourceLocator();
         locator.setSystemId(this.getStylesheetProcessor().getDOMsystemID());
         return locator;
      } else {
         return (SAXSourceLocator)this.m_stylesheetLocatorStack.peek();
      }
   }

   void pushImportURL(String hrefUrl) {
      this.m_importStack.push(hrefUrl);
   }

   boolean importStackContains(String hrefUrl) {
      return this.stackContains(this.m_importStack, hrefUrl);
   }

   String popImportURL() {
      return (String)this.m_importStack.pop();
   }

   void pushNewNamespaceSupport() {
      this.m_nsSupportStack.push(new NamespaceSupport2());
   }

   void popNamespaceSupport() {
      this.m_nsSupportStack.pop();
   }

   NamespaceSupport getNamespaceSupport() {
      return (NamespaceSupport)this.m_nsSupportStack.peek();
   }

   public void setOriginatingNode(Node n) {
      this.m_originatingNode = n;
   }

   public Node getOriginatingNode() {
      return this.m_originatingNode;
   }

   boolean isSpacePreserve() {
      return this.m_spacePreserveStack.peek();
   }

   void popSpaceHandling() {
      this.m_spacePreserveStack.pop();
   }

   void pushSpaceHandling(boolean b) throws SAXParseException {
      this.m_spacePreserveStack.push(b);
   }

   void pushSpaceHandling(Attributes attrs) throws SAXParseException {
      String value = attrs.getValue("xml:space");
      if (null == value) {
         this.m_spacePreserveStack.push(this.m_spacePreserveStack.peekOrFalse());
      } else if (value.equals("preserve")) {
         this.m_spacePreserveStack.push(true);
      } else if (value.equals("default")) {
         this.m_spacePreserveStack.push(false);
      } else {
         SAXSourceLocator locator = this.getLocator();
         ErrorListener handler = this.m_stylesheetProcessor.getErrorListener();

         try {
            handler.error(new TransformerException(XSLMessages.createMessage("ER_ILLEGAL_XMLSPACE_VALUE", (Object[])null), locator));
         } catch (TransformerException var6) {
            throw new SAXParseException(var6.getMessage(), locator, var6);
         }

         this.m_spacePreserveStack.push(this.m_spacePreserveStack.peek());
      }

   }

   private double getElemVersion() {
      ElemTemplateElement elem = this.getElemTemplateElement();

      double version;
      for(version = -1.0; (version == -1.0 || version == 1.0) && elem != null; elem = elem.getParentElem()) {
         try {
            version = Double.valueOf(elem.getXmlVersion());
         } catch (Exception var5) {
            version = -1.0;
         }
      }

      return version == -1.0 ? 1.0 : version;
   }

   public boolean handlesNullPrefixes() {
      return false;
   }

   public boolean getOptimize() {
      return this.m_optimize;
   }

   public boolean getIncremental() {
      return this.m_incremental;
   }

   public boolean getSource_location() {
      return this.m_source_location;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
