package org.apache.xalan.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemForEach;
import org.apache.xalan.templates.ElemSort;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xalan.templates.XUnresolvedVariable;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.SerializerTrace;
import org.apache.xml.serializer.ToHTMLSAXHandler;
import org.apache.xml.serializer.ToSAXHandler;
import org.apache.xml.serializer.ToTextSAXHandler;
import org.apache.xml.serializer.ToTextStream;
import org.apache.xml.serializer.ToXMLSAXHandler;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.DOMBuilder;
import org.apache.xml.utils.DOMHelper;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectPool;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.ThreadControllerWrapper;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Arg;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class TransformerImpl extends Transformer implements Runnable, DTMWSFilter, ExtensionsProvider, SerializerTrace {
   private Boolean m_reentryGuard = new Boolean(true);
   private FileOutputStream m_outputStream = null;
   private boolean m_parserEventsOnMain = true;
   private Thread m_transformThread;
   private String m_urlOfSource = null;
   private Result m_outputTarget = null;
   private OutputProperties m_outputFormat;
   ContentHandler m_inputContentHandler;
   private ContentHandler m_outputContentHandler = null;
   DocumentBuilder m_docBuilder = null;
   private ObjectPool m_textResultHandlerObjectPool;
   private ObjectPool m_stringWriterObjectPool;
   private OutputProperties m_textformat;
   ObjectStack m_currentTemplateElements;
   Stack m_currentMatchTemplates;
   NodeVector m_currentMatchedNodes;
   private StylesheetRoot m_stylesheetRoot;
   private boolean m_quietConflictWarnings;
   private XPathContext m_xcontext;
   private StackGuard m_stackGuard;
   private SerializationHandler m_serializationHandler;
   private KeyManager m_keyManager;
   Stack m_attrSetStack;
   CountersTable m_countersTable;
   BoolStack m_currentTemplateRuleIsNull;
   ObjectStack m_currentFuncResult;
   private MsgMgr m_msgMgr;
   private boolean m_optimizer;
   private boolean m_incremental;
   private boolean m_source_location;
   private boolean m_debug;
   private ErrorListener m_errorHandler;
   private TraceManager m_traceManager;
   private Exception m_exceptionThrown;
   private Source m_xmlSource;
   private int m_doc;
   private boolean m_isTransformDone;
   private boolean m_hasBeenReset;
   private boolean m_shouldReset;
   private Stack m_modes;
   private ExtensionsTable m_extensionsTable;
   private boolean m_hasTransformThreadErrorCatcher;
   Vector m_userParams;
   // $FF: synthetic field
   static Class class$org$apache$xml$serializer$ToTextStream;
   // $FF: synthetic field
   static Class class$java$io$StringWriter;

   public void setShouldReset(boolean shouldReset) {
      this.m_shouldReset = shouldReset;
   }

   public TransformerImpl(StylesheetRoot stylesheet) {
      this.m_textResultHandlerObjectPool = new ObjectPool(class$org$apache$xml$serializer$ToTextStream == null ? (class$org$apache$xml$serializer$ToTextStream = class$("org.apache.xml.serializer.ToTextStream")) : class$org$apache$xml$serializer$ToTextStream);
      this.m_stringWriterObjectPool = new ObjectPool(class$java$io$StringWriter == null ? (class$java$io$StringWriter = class$("java.io.StringWriter")) : class$java$io$StringWriter);
      this.m_textformat = new OutputProperties("text");
      this.m_currentTemplateElements = new ObjectStack(4096);
      this.m_currentMatchTemplates = new Stack();
      this.m_currentMatchedNodes = new NodeVector();
      this.m_stylesheetRoot = null;
      this.m_quietConflictWarnings = true;
      this.m_keyManager = new KeyManager();
      this.m_attrSetStack = null;
      this.m_countersTable = null;
      this.m_currentTemplateRuleIsNull = new BoolStack();
      this.m_currentFuncResult = new ObjectStack();
      this.m_optimizer = true;
      this.m_incremental = false;
      this.m_source_location = false;
      this.m_debug = false;
      this.m_errorHandler = new DefaultErrorHandler(false);
      this.m_traceManager = new TraceManager(this);
      this.m_exceptionThrown = null;
      this.m_isTransformDone = false;
      this.m_hasBeenReset = false;
      this.m_shouldReset = true;
      this.m_modes = new Stack();
      this.m_extensionsTable = null;
      this.m_hasTransformThreadErrorCatcher = false;
      this.m_optimizer = stylesheet.getOptimizer();
      this.m_incremental = stylesheet.getIncremental();
      this.m_source_location = stylesheet.getSource_location();
      this.setStylesheet(stylesheet);
      XPathContext xPath = new XPathContext(this);
      xPath.setIncremental(this.m_incremental);
      xPath.getDTMManager().setIncremental(this.m_incremental);
      xPath.setSource_location(this.m_source_location);
      xPath.getDTMManager().setSource_location(this.m_source_location);
      if (stylesheet.isSecureProcessing()) {
         xPath.setSecureProcessing(true);
      }

      this.setXPathContext(xPath);
      this.getXPathContext().setNamespaceContext(stylesheet);
      this.m_stackGuard = new StackGuard(this);
   }

   public ExtensionsTable getExtensionsTable() {
      return this.m_extensionsTable;
   }

   void setExtensionsTable(StylesheetRoot sroot) throws TransformerException {
      try {
         if (sroot.getExtensions() != null) {
            this.m_extensionsTable = new ExtensionsTable(sroot);
         }
      } catch (TransformerException var3) {
         var3.printStackTrace();
      }

   }

   public boolean functionAvailable(String ns, String funcName) throws TransformerException {
      return this.getExtensionsTable().functionAvailable(ns, funcName);
   }

   public boolean elementAvailable(String ns, String elemName) throws TransformerException {
      return this.getExtensionsTable().elementAvailable(ns, elemName);
   }

   public Object extFunction(String ns, String funcName, Vector argVec, Object methodKey) throws TransformerException {
      return this.getExtensionsTable().extFunction(ns, funcName, argVec, methodKey, this.getXPathContext().getExpressionContext());
   }

   public Object extFunction(FuncExtFunction extFunction, Vector argVec) throws TransformerException {
      return this.getExtensionsTable().extFunction(extFunction, argVec, this.getXPathContext().getExpressionContext());
   }

   public void reset() {
      if (!this.m_hasBeenReset && this.m_shouldReset) {
         this.m_hasBeenReset = true;
         if (this.m_outputStream != null) {
            try {
               this.m_outputStream.close();
            } catch (IOException var2) {
            }
         }

         this.m_outputStream = null;
         this.m_countersTable = null;
         this.m_xcontext.reset();
         this.m_xcontext.getVarStack().reset();
         this.resetUserParameters();
         this.m_currentTemplateElements.removeAllElements();
         this.m_currentMatchTemplates.removeAllElements();
         this.m_currentMatchedNodes.removeAllElements();
         this.m_serializationHandler = null;
         this.m_outputTarget = null;
         this.m_keyManager = new KeyManager();
         this.m_attrSetStack = null;
         this.m_countersTable = null;
         this.m_currentTemplateRuleIsNull = new BoolStack();
         this.m_xmlSource = null;
         this.m_doc = -1;
         this.m_isTransformDone = false;
         this.m_transformThread = null;
         this.m_xcontext.getSourceTreeManager().reset();
      }

   }

   public boolean getProperty(String property) {
      return false;
   }

   public void setProperty(String property, Object value) {
   }

   public boolean isParserEventsOnMain() {
      return this.m_parserEventsOnMain;
   }

   public Thread getTransformThread() {
      return this.m_transformThread;
   }

   public void setTransformThread(Thread t) {
      this.m_transformThread = t;
   }

   public boolean hasTransformThreadErrorCatcher() {
      return this.m_hasTransformThreadErrorCatcher;
   }

   public void transform(Source source) throws TransformerException {
      this.transform(source, true);
   }

   public void transform(Source source, boolean shouldRelease) throws TransformerException {
      try {
         if (this.getXPathContext().getNamespaceContext() == null) {
            this.getXPathContext().setNamespaceContext(this.getStylesheet());
         }

         String base = ((Source)source).getSystemId();
         if (null == base) {
            base = this.m_stylesheetRoot.getBaseIdentifier();
         }

         if (null == base) {
            String currentDir = "";

            try {
               currentDir = System.getProperty("user.dir");
            } catch (SecurityException var27) {
            }

            if (currentDir.startsWith(File.separator)) {
               base = "file://" + currentDir;
            } else {
               base = "file:///" + currentDir;
            }

            base = base + File.separatorChar + source.getClass().getName();
         }

         this.setBaseURLOfSource(base);
         DTMManager mgr = this.m_xcontext.getDTMManager();
         if (source instanceof StreamSource && ((Source)source).getSystemId() == null && ((StreamSource)source).getInputStream() == null && ((StreamSource)source).getReader() == null || source instanceof SAXSource && ((SAXSource)source).getInputSource() == null && ((SAXSource)source).getXMLReader() == null || source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
            try {
               DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
               DocumentBuilder builder = builderF.newDocumentBuilder();
               String systemID = ((Source)source).getSystemId();
               source = new DOMSource(builder.newDocument());
               if (systemID != null) {
                  ((Source)source).setSystemId(systemID);
               }
            } catch (ParserConfigurationException var26) {
               this.fatalError(var26);
            }
         }

         DTM dtm = mgr.getDTM((Source)source, false, this, true, true);
         dtm.setDocumentBaseURI(base);
         boolean hardDelete = true;

         try {
            this.transformNode(dtm.getDocument());
         } finally {
            if (shouldRelease) {
               mgr.release(dtm, hardDelete);
            }

         }

         Exception e = this.getExceptionThrown();
         if (null != e) {
            if (e instanceof TransformerException) {
               throw (TransformerException)e;
            }

            if (!(e instanceof WrappedRuntimeException)) {
               throw new TransformerException(e);
            }

            this.fatalError(((WrappedRuntimeException)e).getException());
         } else if (null != this.m_serializationHandler) {
            this.m_serializationHandler.endDocument();
         }
      } catch (WrappedRuntimeException var29) {
         Exception throwable;
         for(throwable = var29.getException(); throwable instanceof WrappedRuntimeException; throwable = ((WrappedRuntimeException)throwable).getException()) {
         }

         this.fatalError(throwable);
      } catch (SAXParseException var30) {
         this.fatalError(var30);
      } catch (SAXException var31) {
         this.m_errorHandler.fatalError(new TransformerException(var31));
      } finally {
         this.m_hasTransformThreadErrorCatcher = false;
         this.reset();
      }

   }

   private void fatalError(Throwable throwable) throws TransformerException {
      if (throwable instanceof SAXParseException) {
         this.m_errorHandler.fatalError(new TransformerException(throwable.getMessage(), new SAXSourceLocator((SAXParseException)throwable)));
      } else {
         this.m_errorHandler.fatalError(new TransformerException(throwable));
      }

   }

   public String getBaseURLOfSource() {
      return this.m_urlOfSource;
   }

   public void setBaseURLOfSource(String base) {
      this.m_urlOfSource = base;
   }

   public Result getOutputTarget() {
      return this.m_outputTarget;
   }

   public void setOutputTarget(Result outputTarget) {
      this.m_outputTarget = outputTarget;
   }

   public String getOutputProperty(String qnameString) throws IllegalArgumentException {
      String value = null;
      OutputProperties props = this.getOutputFormat();
      value = props.getProperty(qnameString);
      if (null == value && !OutputProperties.isLegalPropertyKey(qnameString)) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{qnameString}));
      } else {
         return value;
      }
   }

   public String getOutputPropertyNoDefault(String qnameString) throws IllegalArgumentException {
      String value = null;
      OutputProperties props = this.getOutputFormat();
      value = (String)props.getProperties().get(qnameString);
      if (null == value && !OutputProperties.isLegalPropertyKey(qnameString)) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{qnameString}));
      } else {
         return value;
      }
   }

   public void setOutputProperty(String name, String value) throws IllegalArgumentException {
      Boolean var3 = this.m_reentryGuard;
      synchronized(var3) {
         if (null == this.m_outputFormat) {
            this.m_outputFormat = (OutputProperties)this.getStylesheet().getOutputComposed().clone();
         }

         if (!OutputProperties.isLegalPropertyKey(name)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{name}));
         } else {
            this.m_outputFormat.setProperty(name, value);
         }
      }
   }

   public void setOutputProperties(Properties oformat) throws IllegalArgumentException {
      Boolean var2 = this.m_reentryGuard;
      synchronized(var2) {
         if (null != oformat) {
            String method = (String)oformat.get("method");
            if (null != method) {
               this.m_outputFormat = new OutputProperties(method);
            } else if (this.m_outputFormat == null) {
               this.m_outputFormat = new OutputProperties();
            }

            this.m_outputFormat.copyFrom(oformat);
            this.m_outputFormat.copyFrom(this.m_stylesheetRoot.getOutputProperties());
         } else {
            this.m_outputFormat = null;
         }

      }
   }

   public Properties getOutputProperties() {
      return (Properties)this.getOutputFormat().getProperties().clone();
   }

   public SerializationHandler createSerializationHandler(Result outputTarget) throws TransformerException {
      SerializationHandler xoh = this.createSerializationHandler(outputTarget, this.getOutputFormat());
      return xoh;
   }

   public SerializationHandler createSerializationHandler(Result outputTarget, OutputProperties format) throws TransformerException {
      Node outputNode = null;
      Object xoh;
      String publicID;
      if (outputTarget instanceof DOMResult) {
         outputNode = ((DOMResult)outputTarget).getNode();
         Node nextSibling = ((DOMResult)outputTarget).getNextSibling();
         Document doc;
         short type;
         if (null != outputNode) {
            type = ((Node)outputNode).getNodeType();
            doc = 9 == type ? (Document)outputNode : ((Node)outputNode).getOwnerDocument();
         } else {
            boolean isSecureProcessing = this.m_stylesheetRoot.isSecureProcessing();
            doc = DOMHelper.createDocument(isSecureProcessing);
            outputNode = doc;
            type = doc.getNodeType();
            ((DOMResult)outputTarget).setNode(doc);
         }

         DOMBuilder handler = 11 == type ? new DOMBuilder(doc, (DocumentFragment)outputNode) : new DOMBuilder(doc, (Node)outputNode);
         if (nextSibling != null) {
            handler.setNextSibling(nextSibling);
         }

         publicID = format.getProperty("encoding");
         xoh = new ToXMLSAXHandler(handler, handler, publicID);
      } else {
         String fileURL;
         if (outputTarget instanceof SAXResult) {
            ContentHandler handler = ((SAXResult)outputTarget).getHandler();
            if (null == handler) {
               throw new IllegalArgumentException("handler can not be null for a SAXResult");
            }

            LexicalHandler lexHandler;
            if (handler instanceof LexicalHandler) {
               lexHandler = (LexicalHandler)handler;
            } else {
               lexHandler = null;
            }

            String encoding = format.getProperty("encoding");
            fileURL = format.getProperty("method");
            if ("html".equals(fileURL)) {
               xoh = new ToHTMLSAXHandler(handler, lexHandler, encoding);
            } else if ("text".equals(fileURL)) {
               xoh = new ToTextSAXHandler(handler, lexHandler, encoding);
            } else {
               ToXMLSAXHandler toXMLSAXHandler = new ToXMLSAXHandler(handler, lexHandler, encoding);
               toXMLSAXHandler.setShouldOutputNSAttr(false);
               xoh = toXMLSAXHandler;
            }

            publicID = format.getProperty("doctype-public");
            String systemID = format.getProperty("doctype-system");
            if (systemID != null) {
               ((SerializationHandler)xoh).setDoctypeSystem(systemID);
            }

            if (publicID != null) {
               ((SerializationHandler)xoh).setDoctypePublic(publicID);
            }

            if (handler instanceof TransformerClient) {
               XalanTransformState state = new XalanTransformState();
               ((TransformerClient)handler).setTransformState(state);
               ((ToSAXHandler)xoh).setTransformState(state);
            }
         } else {
            if (!(outputTarget instanceof StreamResult)) {
               throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_TRANSFORM_TO_RESULT_TYPE", new Object[]{outputTarget.getClass().getName()}));
            }

            StreamResult sresult = (StreamResult)outputTarget;
            String method = format.getProperty("method");

            try {
               SerializationHandler serializer = (SerializationHandler)SerializerFactory.getSerializer(format.getProperties());
               if (null != sresult.getWriter()) {
                  serializer.setWriter(sresult.getWriter());
               } else if (null != sresult.getOutputStream()) {
                  serializer.setOutputStream(sresult.getOutputStream());
               } else {
                  if (null == sresult.getSystemId()) {
                     throw new TransformerException(XSLMessages.createMessage("ER_NO_OUTPUT_SPECIFIED", (Object[])null));
                  }

                  fileURL = sresult.getSystemId();
                  if (fileURL.startsWith("file:///")) {
                     if (fileURL.substring(8).indexOf(":") > 0) {
                        fileURL = fileURL.substring(8);
                     } else {
                        fileURL = fileURL.substring(7);
                     }
                  } else if (fileURL.startsWith("file:/")) {
                     if (fileURL.substring(6).indexOf(":") > 0) {
                        fileURL = fileURL.substring(6);
                     } else {
                        fileURL = fileURL.substring(5);
                     }
                  }

                  this.m_outputStream = new FileOutputStream(fileURL);
                  serializer.setOutputStream(this.m_outputStream);
               }

               xoh = serializer;
            } catch (IOException var12) {
               throw new TransformerException(var12);
            }
         }
      }

      ((SerializationHandler)xoh).setTransformer(this);
      SourceLocator srcLocator = this.getStylesheet();
      ((SerializationHandler)xoh).setSourceLocator(srcLocator);
      return (SerializationHandler)xoh;
   }

   public void transform(Source xmlSource, Result outputTarget) throws TransformerException {
      this.transform(xmlSource, outputTarget, true);
   }

   public void transform(Source xmlSource, Result outputTarget, boolean shouldRelease) throws TransformerException {
      Boolean var4 = this.m_reentryGuard;
      synchronized(var4) {
         SerializationHandler xoh = this.createSerializationHandler(outputTarget);
         this.setSerializationHandler(xoh);
         this.m_outputTarget = outputTarget;
         this.transform(xmlSource, shouldRelease);
      }
   }

   public void transformNode(int node, Result outputTarget) throws TransformerException {
      SerializationHandler xoh = this.createSerializationHandler(outputTarget);
      this.setSerializationHandler(xoh);
      this.m_outputTarget = outputTarget;
      this.transformNode(node);
   }

   public void transformNode(int node) throws TransformerException {
      this.setExtensionsTable(this.getStylesheet());
      SerializationHandler var2 = this.m_serializationHandler;
      synchronized(var2) {
         this.m_hasBeenReset = false;
         XPathContext xctxt = this.getXPathContext();
         xctxt.getDTM(node);

         try {
            this.pushGlobalVars(node);
            StylesheetRoot stylesheet = this.getStylesheet();
            int n = stylesheet.getGlobalImportCount();

            for(int i = 0; i < n; ++i) {
               StylesheetComposed imported = stylesheet.getGlobalImport(i);
               int includedCount = imported.getIncludeCountComposed();

               for(int j = -1; j < includedCount; ++j) {
                  Stylesheet included = imported.getIncludeComposed(j);
                  included.runtimeInit(this);

                  for(ElemTemplateElement child = included.getFirstChildElem(); child != null; child = child.getNextSiblingElem()) {
                     child.runtimeInit(this);
                  }
               }
            }

            DTMIterator dtmIter = new SelfIteratorNoPredicate();
            dtmIter.setRoot(node, xctxt);
            xctxt.pushContextNodeList(dtmIter);

            try {
               this.applyTemplateToNode((ElemTemplateElement)null, (ElemTemplate)null, node);
            } finally {
               xctxt.popContextNodeList();
            }

            if (null != this.m_serializationHandler) {
               this.m_serializationHandler.endDocument();
            }
         } catch (Exception var30) {
            Exception se = var30;

            while(se instanceof WrappedRuntimeException) {
               Exception e = ((WrappedRuntimeException)se).getException();
               if (null != e) {
                  se = e;
               }
            }

            if (null != this.m_serializationHandler) {
               try {
                  if (se instanceof SAXParseException) {
                     this.m_serializationHandler.fatalError((SAXParseException)se);
                  } else if (se instanceof TransformerException) {
                     TransformerException te = (TransformerException)se;
                     SAXSourceLocator sl = new SAXSourceLocator(te.getLocator());
                     this.m_serializationHandler.fatalError(new SAXParseException(te.getMessage(), sl, te));
                  } else {
                     this.m_serializationHandler.fatalError(new SAXParseException(se.getMessage(), new SAXSourceLocator(), se));
                  }
               } catch (Exception var28) {
               }
            }

            if (se instanceof TransformerException) {
               this.m_errorHandler.fatalError((TransformerException)se);
            } else if (se instanceof SAXParseException) {
               this.m_errorHandler.fatalError(new TransformerException(se.getMessage(), new SAXSourceLocator((SAXParseException)se), se));
            } else {
               this.m_errorHandler.fatalError(new TransformerException(se));
            }
         } finally {
            this.reset();
         }

      }
   }

   public ContentHandler getInputContentHandler() {
      return this.getInputContentHandler(false);
   }

   public ContentHandler getInputContentHandler(boolean doDocFrag) {
      if (null == this.m_inputContentHandler) {
         this.m_inputContentHandler = new TransformerHandlerImpl(this, doDocFrag, this.m_urlOfSource);
      }

      return this.m_inputContentHandler;
   }

   public DeclHandler getInputDeclHandler() {
      return this.m_inputContentHandler instanceof DeclHandler ? (DeclHandler)this.m_inputContentHandler : null;
   }

   public LexicalHandler getInputLexicalHandler() {
      return this.m_inputContentHandler instanceof LexicalHandler ? (LexicalHandler)this.m_inputContentHandler : null;
   }

   public void setOutputFormat(OutputProperties oformat) {
      this.m_outputFormat = oformat;
   }

   public OutputProperties getOutputFormat() {
      OutputProperties format = null == this.m_outputFormat ? this.getStylesheet().getOutputComposed() : this.m_outputFormat;
      return format;
   }

   public void setParameter(String name, String namespace, Object value) {
      VariableStack varstack = this.getXPathContext().getVarStack();
      QName qname = new QName(namespace, name);
      XObject xobject = XObject.create(value, this.getXPathContext());
      StylesheetRoot sroot = this.m_stylesheetRoot;
      Vector vars = sroot.getVariablesAndParamsComposed();
      int i = vars.size();

      while(true) {
         --i;
         if (i < 0) {
            return;
         }

         ElemVariable variable = (ElemVariable)vars.elementAt(i);
         if (variable.getXSLToken() == 41 && variable.getName().equals(qname)) {
            varstack.setGlobalVariable(i, xobject);
         }
      }
   }

   public void setParameter(String name, Object value) {
      if (value == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_SET_PARAM_VALUE", new Object[]{name}));
      } else {
         StringTokenizer tokenizer = new StringTokenizer(name, "{}", false);

         try {
            String s1 = tokenizer.nextToken();
            String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
            if (null == this.m_userParams) {
               this.m_userParams = new Vector();
            }

            if (null == s2) {
               this.replaceOrPushUserParam(new QName(s1), XObject.create(value, this.getXPathContext()));
               this.setParameter(s1, (String)null, value);
            } else {
               this.replaceOrPushUserParam(new QName(s1, s2), XObject.create(value, this.getXPathContext()));
               this.setParameter(s2, s1, value);
            }
         } catch (NoSuchElementException var6) {
         }

      }
   }

   private void replaceOrPushUserParam(QName qname, XObject xval) {
      int n = this.m_userParams.size();

      for(int i = n - 1; i >= 0; --i) {
         Arg arg = (Arg)this.m_userParams.elementAt(i);
         if (arg.getQName().equals(qname)) {
            this.m_userParams.setElementAt(new Arg(qname, xval, true), i);
            return;
         }
      }

      this.m_userParams.addElement(new Arg(qname, xval, true));
   }

   public Object getParameter(String name) {
      try {
         QName qname = QName.getQNameFromString(name);
         if (null == this.m_userParams) {
            return null;
         } else {
            int n = this.m_userParams.size();

            for(int i = n - 1; i >= 0; --i) {
               Arg arg = (Arg)this.m_userParams.elementAt(i);
               if (arg.getQName().equals(qname)) {
                  return arg.getVal().object();
               }
            }

            return null;
         }
      } catch (NoSuchElementException var6) {
         return null;
      }
   }

   private void resetUserParameters() {
      try {
         if (null == this.m_userParams) {
            return;
         }

         int n = this.m_userParams.size();

         for(int i = n - 1; i >= 0; --i) {
            Arg arg = (Arg)this.m_userParams.elementAt(i);
            QName name = arg.getQName();
            String s1 = name.getNamespace();
            String s2 = name.getLocalPart();
            this.setParameter(s2, s1, arg.getVal().object());
         }
      } catch (NoSuchElementException var7) {
      }

   }

   public void setParameters(Properties params) {
      this.clearParameters();
      Enumeration names = params.propertyNames();

      while(names.hasMoreElements()) {
         String name = params.getProperty((String)names.nextElement());
         StringTokenizer tokenizer = new StringTokenizer(name, "{}", false);

         try {
            String s1 = tokenizer.nextToken();
            String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
            if (null == s2) {
               this.setParameter(s1, (String)null, params.getProperty(name));
            } else {
               this.setParameter(s2, s1, params.getProperty(name));
            }
         } catch (NoSuchElementException var7) {
         }
      }

   }

   public void clearParameters() {
      Boolean var1 = this.m_reentryGuard;
      synchronized(var1) {
         VariableStack varstack = new VariableStack();
         this.m_xcontext.setVarStack(varstack);
         this.m_userParams = null;
      }
   }

   protected void pushGlobalVars(int contextNode) throws TransformerException {
      XPathContext xctxt = this.m_xcontext;
      VariableStack vs = xctxt.getVarStack();
      StylesheetRoot sr = this.getStylesheet();
      Vector vars = sr.getVariablesAndParamsComposed();
      int i = vars.size();
      vs.link(i);

      while(true) {
         --i;
         if (i < 0) {
            return;
         }

         ElemVariable v = (ElemVariable)vars.elementAt(i);
         XObject xobj = new XUnresolvedVariable(v, contextNode, this, vs.getStackFrame(), 0, true);
         if (null == vs.elementAt(i)) {
            vs.setGlobalVariable(i, xobj);
         }
      }
   }

   public void setURIResolver(URIResolver resolver) {
      Boolean var2 = this.m_reentryGuard;
      synchronized(var2) {
         this.m_xcontext.getSourceTreeManager().setURIResolver(resolver);
      }
   }

   public URIResolver getURIResolver() {
      return this.m_xcontext.getSourceTreeManager().getURIResolver();
   }

   public void setContentHandler(ContentHandler handler) {
      if (handler == null) {
         throw new NullPointerException(XSLMessages.createMessage("ER_NULL_CONTENT_HANDLER", (Object[])null));
      } else {
         this.m_outputContentHandler = handler;
         if (null == this.m_serializationHandler) {
            ToXMLSAXHandler h = new ToXMLSAXHandler();
            h.setContentHandler(handler);
            h.setTransformer(this);
            this.m_serializationHandler = h;
         } else {
            this.m_serializationHandler.setContentHandler(handler);
         }

      }
   }

   public ContentHandler getContentHandler() {
      return this.m_outputContentHandler;
   }

   public int transformToRTF(ElemTemplateElement templateParent) throws TransformerException {
      DTM dtmFrag = this.m_xcontext.getRTFDTM();
      return this.transformToRTF(templateParent, dtmFrag);
   }

   public int transformToGlobalRTF(ElemTemplateElement templateParent) throws TransformerException {
      DTM dtmFrag = this.m_xcontext.getGlobalRTFDTM();
      return this.transformToRTF(templateParent, dtmFrag);
   }

   private int transformToRTF(ElemTemplateElement templateParent, DTM dtmFrag) throws TransformerException {
      XPathContext xctxt = this.m_xcontext;
      ContentHandler rtfHandler = dtmFrag.getContentHandler();
      SerializationHandler savedRTreeHandler = this.m_serializationHandler;
      ToSAXHandler h = new ToXMLSAXHandler();
      h.setContentHandler(rtfHandler);
      h.setTransformer(this);
      this.m_serializationHandler = h;
      SerializationHandler rth = this.m_serializationHandler;

      int resultFragment;
      try {
         rth.startDocument();
         rth.flushPending();

         try {
            this.executeChildTemplates(templateParent, true);
            rth.flushPending();
            resultFragment = dtmFrag.getDocument();
         } finally {
            rth.endDocument();
         }
      } catch (SAXException var20) {
         throw new TransformerException(var20);
      } finally {
         this.m_serializationHandler = savedRTreeHandler;
      }

      return resultFragment;
   }

   public ObjectPool getStringWriterPool() {
      return this.m_stringWriterObjectPool;
   }

   public String transformToString(ElemTemplateElement elem) throws TransformerException {
      ElemTemplateElement firstChild = elem.getFirstChildElem();
      if (null == firstChild) {
         return "";
      } else if (elem.hasTextLitOnly() && this.m_optimizer) {
         return ((ElemTextLiteral)firstChild).getNodeValue();
      } else {
         SerializationHandler savedRTreeHandler = this.m_serializationHandler;
         StringWriter sw = (StringWriter)this.m_stringWriterObjectPool.getInstance();
         this.m_serializationHandler = (ToTextStream)this.m_textResultHandlerObjectPool.getInstance();
         if (null == this.m_serializationHandler) {
            Serializer serializer = SerializerFactory.getSerializer(this.m_textformat.getProperties());
            this.m_serializationHandler = (SerializationHandler)serializer;
         }

         this.m_serializationHandler.setTransformer(this);
         this.m_serializationHandler.setWriter(sw);

         String result;
         try {
            this.executeChildTemplates(elem, true);
            this.m_serializationHandler.endDocument();
            result = sw.toString();
         } catch (SAXException var15) {
            throw new TransformerException(var15);
         } finally {
            sw.getBuffer().setLength(0);

            try {
               sw.close();
            } catch (Exception var14) {
            }

            this.m_stringWriterObjectPool.freeInstance(sw);
            this.m_serializationHandler.reset();
            this.m_textResultHandlerObjectPool.freeInstance(this.m_serializationHandler);
            this.m_serializationHandler = savedRTreeHandler;
         }

         return result;
      }
   }

   public boolean applyTemplateToNode(ElemTemplateElement xslInstruction, ElemTemplate template, int child) throws TransformerException {
      DTM dtm = this.m_xcontext.getDTM(child);
      short nodeType = dtm.getNodeType(child);
      boolean isDefaultTextRule = false;
      boolean isApplyImports = false;
      isApplyImports = xslInstruction == null ? false : xslInstruction.getXSLToken() == 72;
      if (null == template || isApplyImports) {
         int endImportLevel = 0;
         int maxImportLevel;
         if (isApplyImports) {
            maxImportLevel = template.getStylesheetComposed().getImportCountComposed() - 1;
            endImportLevel = template.getStylesheetComposed().getEndImportCountComposed();
         } else {
            maxImportLevel = -1;
         }

         if (isApplyImports && maxImportLevel == -1) {
            template = null;
         } else {
            XPathContext xctxt = this.m_xcontext;

            try {
               xctxt.pushNamespaceContext(xslInstruction);
               QName mode = this.getMode();
               if (isApplyImports) {
                  template = this.m_stylesheetRoot.getTemplateComposed(xctxt, child, mode, maxImportLevel, endImportLevel, this.m_quietConflictWarnings, dtm);
               } else {
                  template = this.m_stylesheetRoot.getTemplateComposed(xctxt, child, mode, this.m_quietConflictWarnings, dtm);
               }
            } finally {
               xctxt.popNamespaceContext();
            }
         }

         if (null == template) {
            switch (nodeType) {
               case 1:
               case 11:
                  template = this.m_stylesheetRoot.getDefaultRule();
                  break;
               case 2:
               case 3:
               case 4:
                  template = this.m_stylesheetRoot.getDefaultTextRule();
                  isDefaultTextRule = true;
                  break;
               case 5:
               case 6:
               case 7:
               case 8:
               case 10:
               default:
                  return false;
               case 9:
                  template = this.m_stylesheetRoot.getDefaultRootRule();
            }
         }
      }

      try {
         this.pushElemTemplateElement(template);
         this.m_xcontext.pushCurrentNode(child);
         this.pushPairCurrentMatched(template, child);
         if (!isApplyImports) {
            DTMIterator cnl = new NodeSetDTM(child, this.m_xcontext.getDTMManager());
            this.m_xcontext.pushContextNodeList(cnl);
         }

         if (isDefaultTextRule) {
            switch (nodeType) {
               case 2:
                  dtm.dispatchCharactersEvents(child, this.getResultTreeHandler(), false);
                  break;
               case 3:
               case 4:
                  ClonerToResultTree.cloneToResultTree(child, nodeType, dtm, this.getResultTreeHandler(), false);
            }
         } else {
            if (this.m_debug) {
               this.getTraceManager().fireTraceEvent((ElemTemplateElement)template);
            }

            this.m_xcontext.setSAXLocator(template);
            this.m_xcontext.getVarStack().link(template.m_frameSize);
            this.executeChildTemplates(template, true);
            if (this.m_debug) {
               this.getTraceManager().fireTraceEndEvent((ElemTemplateElement)template);
            }
         }
      } catch (SAXException var23) {
         throw new TransformerException(var23);
      } finally {
         if (!isDefaultTextRule) {
            this.m_xcontext.getVarStack().unlink();
         }

         this.m_xcontext.popCurrentNode();
         if (!isApplyImports) {
            this.m_xcontext.popContextNodeList();
         }

         this.popCurrentMatched();
         this.popElemTemplateElement();
      }

      return true;
   }

   public void executeChildTemplates(ElemTemplateElement elem, Node context, QName mode, ContentHandler handler) throws TransformerException {
      XPathContext xctxt = this.m_xcontext;

      try {
         if (null != mode) {
            this.pushMode(mode);
         }

         xctxt.pushCurrentNode(xctxt.getDTMHandleFromNode(context));
         this.executeChildTemplates(elem, handler);
      } finally {
         xctxt.popCurrentNode();
         if (null != mode) {
            this.popMode();
         }

      }

   }

   public void executeChildTemplates(ElemTemplateElement elem, boolean shouldAddAttrs) throws TransformerException {
      ElemTemplateElement t = elem.getFirstChildElem();
      if (null != t) {
         if (elem.hasTextLitOnly() && this.m_optimizer) {
            char[] chars = ((ElemTextLiteral)t).getChars();

            try {
               this.pushElemTemplateElement(t);
               this.m_serializationHandler.characters(chars, 0, chars.length);
            } catch (SAXException var18) {
               throw new TransformerException(var18);
            } finally {
               this.popElemTemplateElement();
            }

         } else {
            XPathContext xctxt = this.m_xcontext;
            xctxt.pushSAXLocatorNull();
            int currentTemplateElementsTop = this.m_currentTemplateElements.size();
            this.m_currentTemplateElements.push((Object)null);

            try {
               for(; t != null; t = t.getNextSiblingElem()) {
                  if (shouldAddAttrs || t.getXSLToken() != 48) {
                     xctxt.setSAXLocator(t);
                     this.m_currentTemplateElements.setElementAt(t, currentTemplateElementsTop);
                     t.execute(this);
                  }
               }
            } catch (RuntimeException var20) {
               TransformerException te = new TransformerException(var20);
               te.setLocator(t);
               throw te;
            } finally {
               this.m_currentTemplateElements.pop();
               xctxt.popSAXLocator();
            }

         }
      }
   }

   public void executeChildTemplates(ElemTemplateElement elem, ContentHandler handler) throws TransformerException {
      SerializationHandler xoh = this.getSerializationHandler();
      SerializationHandler savedHandler = xoh;

      try {
         xoh.flushPending();
         LexicalHandler lex = null;
         if (handler instanceof LexicalHandler) {
            lex = (LexicalHandler)handler;
         }

         this.m_serializationHandler = new ToXMLSAXHandler(handler, lex, savedHandler.getEncoding());
         this.m_serializationHandler.setTransformer(this);
         this.executeChildTemplates(elem, true);
      } catch (TransformerException var12) {
         throw var12;
      } catch (SAXException var13) {
         throw new TransformerException(var13);
      } finally {
         this.m_serializationHandler = xoh;
      }

   }

   public Vector processSortKeys(ElemForEach foreach, int sourceNodeContext) throws TransformerException {
      Vector keys = null;
      XPathContext xctxt = this.m_xcontext;
      int nElems = foreach.getSortElemCount();
      if (nElems > 0) {
         keys = new Vector();
      }

      for(int i = 0; i < nElems; ++i) {
         ElemSort sort = foreach.getSortElem(i);
         if (this.m_debug) {
            this.getTraceManager().fireTraceEvent((ElemTemplateElement)sort);
         }

         String langString = null != sort.getLang() ? sort.getLang().evaluate(xctxt, sourceNodeContext, foreach) : null;
         String dataTypeString = sort.getDataType().evaluate(xctxt, sourceNodeContext, foreach);
         if (dataTypeString.indexOf(":") >= 0) {
            System.out.println("TODO: Need to write the hooks for QNAME sort data type");
         } else if (!dataTypeString.equalsIgnoreCase("text") && !dataTypeString.equalsIgnoreCase("number")) {
            foreach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"data-type", dataTypeString});
         }

         boolean treatAsNumbers = null != dataTypeString && dataTypeString.equals("number");
         String orderString = sort.getOrder().evaluate(xctxt, sourceNodeContext, foreach);
         if (!orderString.equalsIgnoreCase("ascending") && !orderString.equalsIgnoreCase("descending")) {
            foreach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"order", orderString});
         }

         boolean descending = null != orderString && orderString.equals("descending");
         AVT caseOrder = sort.getCaseOrder();
         boolean caseOrderUpper;
         if (null != caseOrder) {
            String caseOrderString = caseOrder.evaluate(xctxt, sourceNodeContext, foreach);
            if (!caseOrderString.equalsIgnoreCase("upper-first") && !caseOrderString.equalsIgnoreCase("lower-first")) {
               foreach.error("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"case-order", caseOrderString});
            }

            caseOrderUpper = null != caseOrderString && caseOrderString.equals("upper-first");
         } else {
            caseOrderUpper = false;
         }

         keys.addElement(new NodeSortKey(this, sort.getSelect(), treatAsNumbers, descending, langString, caseOrderUpper, foreach));
         if (this.m_debug) {
            this.getTraceManager().fireTraceEndEvent((ElemTemplateElement)sort);
         }
      }

      return keys;
   }

   public Vector getElementCallstack() {
      Vector elems = new Vector();
      int nStackSize = this.m_currentTemplateElements.size();

      for(int i = 0; i < nStackSize; ++i) {
         ElemTemplateElement elem = (ElemTemplateElement)this.m_currentTemplateElements.elementAt(i);
         if (null != elem) {
            elems.addElement(elem);
         }
      }

      return elems;
   }

   public int getCurrentTemplateElementsCount() {
      return this.m_currentTemplateElements.size();
   }

   public ObjectStack getCurrentTemplateElements() {
      return this.m_currentTemplateElements;
   }

   public void pushElemTemplateElement(ElemTemplateElement elem) {
      this.m_currentTemplateElements.push(elem);
   }

   public void popElemTemplateElement() {
      this.m_currentTemplateElements.pop();
   }

   public void setCurrentElement(ElemTemplateElement e) {
      this.m_currentTemplateElements.setTop(e);
   }

   public ElemTemplateElement getCurrentElement() {
      return this.m_currentTemplateElements.size() > 0 ? (ElemTemplateElement)this.m_currentTemplateElements.peek() : null;
   }

   public int getCurrentNode() {
      return this.m_xcontext.getCurrentNode();
   }

   public Vector getTemplateCallstack() {
      Vector elems = new Vector();
      int nStackSize = this.m_currentTemplateElements.size();

      for(int i = 0; i < nStackSize; ++i) {
         ElemTemplateElement elem = (ElemTemplateElement)this.m_currentTemplateElements.elementAt(i);
         if (null != elem && elem.getXSLToken() != 19) {
            elems.addElement(elem);
         }
      }

      return elems;
   }

   public ElemTemplate getCurrentTemplate() {
      ElemTemplateElement elem;
      for(elem = this.getCurrentElement(); null != elem && elem.getXSLToken() != 19; elem = elem.getParentElem()) {
      }

      return (ElemTemplate)elem;
   }

   public void pushPairCurrentMatched(ElemTemplateElement template, int child) {
      this.m_currentMatchTemplates.push(template);
      this.m_currentMatchedNodes.push(child);
   }

   public void popCurrentMatched() {
      this.m_currentMatchTemplates.pop();
      this.m_currentMatchedNodes.pop();
   }

   public ElemTemplate getMatchedTemplate() {
      return (ElemTemplate)this.m_currentMatchTemplates.peek();
   }

   public int getMatchedNode() {
      return this.m_currentMatchedNodes.peepTail();
   }

   public DTMIterator getContextNodeList() {
      try {
         DTMIterator cnl = this.m_xcontext.getContextNodeList();
         return cnl == null ? null : cnl.cloneWithReset();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public Transformer getTransformer() {
      return this;
   }

   public void setStylesheet(StylesheetRoot stylesheetRoot) {
      this.m_stylesheetRoot = stylesheetRoot;
   }

   public final StylesheetRoot getStylesheet() {
      return this.m_stylesheetRoot;
   }

   public boolean getQuietConflictWarnings() {
      return this.m_quietConflictWarnings;
   }

   public void setQuietConflictWarnings(boolean b) {
      this.m_quietConflictWarnings = b;
   }

   public void setXPathContext(XPathContext xcontext) {
      this.m_xcontext = xcontext;
   }

   public final XPathContext getXPathContext() {
      return this.m_xcontext;
   }

   public StackGuard getStackGuard() {
      return this.m_stackGuard;
   }

   public int getRecursionLimit() {
      return this.m_stackGuard.getRecursionLimit();
   }

   public void setRecursionLimit(int limit) {
      this.m_stackGuard.setRecursionLimit(limit);
   }

   public SerializationHandler getResultTreeHandler() {
      return this.m_serializationHandler;
   }

   public SerializationHandler getSerializationHandler() {
      return this.m_serializationHandler;
   }

   public KeyManager getKeyManager() {
      return this.m_keyManager;
   }

   public boolean isRecursiveAttrSet(ElemAttributeSet attrSet) {
      if (null == this.m_attrSetStack) {
         this.m_attrSetStack = new Stack();
      }

      if (!this.m_attrSetStack.empty()) {
         int loc = this.m_attrSetStack.search(attrSet);
         if (loc > -1) {
            return true;
         }
      }

      return false;
   }

   public void pushElemAttributeSet(ElemAttributeSet attrSet) {
      this.m_attrSetStack.push(attrSet);
   }

   public void popElemAttributeSet() {
      this.m_attrSetStack.pop();
   }

   public CountersTable getCountersTable() {
      if (null == this.m_countersTable) {
         this.m_countersTable = new CountersTable();
      }

      return this.m_countersTable;
   }

   public boolean currentTemplateRuleIsNull() {
      return !this.m_currentTemplateRuleIsNull.isEmpty() && this.m_currentTemplateRuleIsNull.peek();
   }

   public void pushCurrentTemplateRuleIsNull(boolean b) {
      this.m_currentTemplateRuleIsNull.push(b);
   }

   public void popCurrentTemplateRuleIsNull() {
      this.m_currentTemplateRuleIsNull.pop();
   }

   public void pushCurrentFuncResult(Object val) {
      this.m_currentFuncResult.push(val);
   }

   public Object popCurrentFuncResult() {
      return this.m_currentFuncResult.pop();
   }

   public boolean currentFuncResultSeen() {
      return !this.m_currentFuncResult.empty() && this.m_currentFuncResult.peek() != null;
   }

   public MsgMgr getMsgMgr() {
      if (null == this.m_msgMgr) {
         this.m_msgMgr = new MsgMgr(this);
      }

      return this.m_msgMgr;
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      Boolean var2 = this.m_reentryGuard;
      synchronized(var2) {
         if (listener == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NULL_ERROR_HANDLER", (Object[])null));
         } else {
            this.m_errorHandler = listener;
         }
      }
   }

   public ErrorListener getErrorListener() {
      return this.m_errorHandler;
   }

   public TraceManager getTraceManager() {
      return this.m_traceManager;
   }

   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
      if ("http://xml.org/trax/features/sax/input".equals(name)) {
         return true;
      } else if ("http://xml.org/trax/features/dom/input".equals(name)) {
         return true;
      } else {
         throw new SAXNotRecognizedException(name);
      }
   }

   public QName getMode() {
      return this.m_modes.isEmpty() ? null : (QName)this.m_modes.peek();
   }

   public void pushMode(QName mode) {
      this.m_modes.push(mode);
   }

   public void popMode() {
      this.m_modes.pop();
   }

   public void runTransformThread(int priority) {
      Thread t = ThreadControllerWrapper.runThread(this, priority);
      this.setTransformThread(t);
   }

   public void runTransformThread() {
      ThreadControllerWrapper.runThread(this, -1);
   }

   public static void runTransformThread(Runnable runnable) {
      ThreadControllerWrapper.runThread(runnable, -1);
   }

   public void waitTransformThread() throws SAXException {
      Thread transformThread = this.getTransformThread();
      if (null != transformThread) {
         try {
            ThreadControllerWrapper.waitThread(transformThread, this);
            if (!this.hasTransformThreadErrorCatcher()) {
               Exception e = this.getExceptionThrown();
               if (null != e) {
                  e.printStackTrace();
                  throw new SAXException(e);
               }
            }

            this.setTransformThread((Thread)null);
         } catch (InterruptedException var3) {
         }
      }

   }

   public Exception getExceptionThrown() {
      return this.m_exceptionThrown;
   }

   public void setExceptionThrown(Exception e) {
      this.m_exceptionThrown = e;
   }

   public void setSourceTreeDocForThread(int doc) {
      this.m_doc = doc;
   }

   public void setXMLSource(Source source) {
      this.m_xmlSource = source;
   }

   public boolean isTransformDone() {
      synchronized(this) {
         boolean var2 = this.m_isTransformDone;
         return var2;
      }
   }

   public void setIsTransformDone(boolean done) {
      synchronized(this) {
         this.m_isTransformDone = done;
      }
   }

   void postExceptionFromThread(Exception e) {
      this.m_isTransformDone = true;
      this.m_exceptionThrown = e;
      synchronized(this) {
         this.notifyAll();
      }
   }

   public void run() {
      this.m_hasBeenReset = false;

      try {
         try {
            this.m_isTransformDone = false;
            this.transformNode(this.m_doc);
         } catch (Exception var7) {
            if (null == this.m_transformThread) {
               throw new RuntimeException(var7.getMessage());
            }

            this.postExceptionFromThread(var7);
         } finally {
            this.m_isTransformDone = true;
            if (this.m_inputContentHandler instanceof TransformerHandlerImpl) {
               ((TransformerHandlerImpl)this.m_inputContentHandler).clearCoRoutine();
            }

         }
      } catch (Exception var9) {
         if (null == this.m_transformThread) {
            throw new RuntimeException(var9.getMessage());
         }

         this.postExceptionFromThread(var9);
      }

   }

   /** @deprecated */
   public TransformSnapshot getSnapshot() {
      return new TransformSnapshotImpl(this);
   }

   /** @deprecated */
   public void executeFromSnapshot(TransformSnapshot ts) throws TransformerException {
      ElemTemplateElement template = this.getMatchedTemplate();
      int child = this.getMatchedNode();
      this.pushElemTemplateElement(template);
      this.m_xcontext.pushCurrentNode(child);
      this.executeChildTemplates(template, true);
   }

   /** @deprecated */
   public void resetToStylesheet(TransformSnapshot ts) {
      ((TransformSnapshotImpl)ts).apply(this);
   }

   public void stopTransformation() {
   }

   public short getShouldStripSpace(int elementHandle, DTM dtm) {
      try {
         WhiteSpaceInfo info = this.m_stylesheetRoot.getWhiteSpaceInfo(this.m_xcontext, elementHandle, dtm);
         if (null == info) {
            return 3;
         } else {
            return (short)(info.getShouldStripSpace() ? 2 : 1);
         }
      } catch (TransformerException var4) {
         return 3;
      }
   }

   public void init(ToXMLSAXHandler h, Transformer transformer, ContentHandler realHandler) {
      h.setTransformer(transformer);
      h.setContentHandler(realHandler);
   }

   public void setSerializationHandler(SerializationHandler xoh) {
      this.m_serializationHandler = xoh;
   }

   public void fireGenerateEvent(int eventType, char[] ch, int start, int length) {
      GenerateEvent ge = new GenerateEvent(this, eventType, ch, start, length);
      this.m_traceManager.fireGenerateEvent(ge);
   }

   public void fireGenerateEvent(int eventType, String name, Attributes atts) {
      GenerateEvent ge = new GenerateEvent(this, eventType, name, atts);
      this.m_traceManager.fireGenerateEvent(ge);
   }

   public void fireGenerateEvent(int eventType, String name, String data) {
      GenerateEvent ge = new GenerateEvent(this, eventType, name, data);
      this.m_traceManager.fireGenerateEvent(ge);
   }

   public void fireGenerateEvent(int eventType, String data) {
      GenerateEvent ge = new GenerateEvent(this, eventType, data);
      this.m_traceManager.fireGenerateEvent(ge);
   }

   public void fireGenerateEvent(int eventType) {
      GenerateEvent ge = new GenerateEvent(this, eventType);
      this.m_traceManager.fireGenerateEvent(ge);
   }

   public boolean hasTraceListeners() {
      return this.m_traceManager.hasTraceListeners();
   }

   public boolean getDebug() {
      return this.m_debug;
   }

   public void setDebug(boolean b) {
      this.m_debug = b;
   }

   public boolean getIncremental() {
      return this.m_incremental;
   }

   public boolean getOptimize() {
      return this.m_optimizer;
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
