package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class TransformerFactoryImpl extends SAXTransformerFactory implements SourceLoader, ErrorListener {
   public static final String TRANSLET_NAME = "translet-name";
   public static final String DESTINATION_DIRECTORY = "destination-directory";
   public static final String PACKAGE_NAME = "package-name";
   public static final String JAR_NAME = "jar-name";
   public static final String GENERATE_TRANSLET = "generate-translet";
   public static final String AUTO_TRANSLET = "auto-translet";
   public static final String USE_CLASSPATH = "use-classpath";
   public static final String DEBUG = "debug";
   public static final String ENABLE_INLINING = "enable-inlining";
   public static final String INDENT_NUMBER = "indent-number";
   private ErrorListener _errorListener = this;
   private URIResolver _uriResolver = null;
   protected static final String DEFAULT_TRANSLET_NAME = "GregorSamsa";
   private String _transletName = "GregorSamsa";
   private String _destinationDirectory = null;
   private String _packageName = null;
   private String _jarFileName = null;
   private Hashtable _piParams = null;
   private boolean _debug = false;
   private boolean _enableInlining = false;
   private boolean _generateTranslet = false;
   private boolean _autoTranslet = false;
   private boolean _useClasspath = false;
   private int _indentNumber = -1;
   private Class m_DTMManagerClass = XSLTCDTMManager.getDTMManagerClass();
   private boolean _isSecureProcessing = false;

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (listener == null) {
         ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "TransformerFactory");
         throw new IllegalArgumentException(err.toString());
      } else {
         this._errorListener = listener;
      }
   }

   public ErrorListener getErrorListener() {
      return this._errorListener;
   }

   public Object getAttribute(String name) throws IllegalArgumentException {
      if (name.equals("translet-name")) {
         return this._transletName;
      } else if (name.equals("generate-translet")) {
         return new Boolean(this._generateTranslet);
      } else if (name.equals("auto-translet")) {
         return new Boolean(this._autoTranslet);
      } else {
         ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
         throw new IllegalArgumentException(err.toString());
      }
   }

   public void setAttribute(String name, Object value) throws IllegalArgumentException {
      if (name.equals("translet-name") && value instanceof String) {
         this._transletName = (String)value;
      } else if (name.equals("destination-directory") && value instanceof String) {
         this._destinationDirectory = (String)value;
      } else if (name.equals("package-name") && value instanceof String) {
         this._packageName = (String)value;
      } else if (name.equals("jar-name") && value instanceof String) {
         this._jarFileName = (String)value;
      } else {
         if (name.equals("generate-translet")) {
            if (value instanceof Boolean) {
               this._generateTranslet = (Boolean)value;
               return;
            }

            if (value instanceof String) {
               this._generateTranslet = ((String)value).equalsIgnoreCase("true");
               return;
            }
         } else if (name.equals("auto-translet")) {
            if (value instanceof Boolean) {
               this._autoTranslet = (Boolean)value;
               return;
            }

            if (value instanceof String) {
               this._autoTranslet = ((String)value).equalsIgnoreCase("true");
               return;
            }
         } else if (name.equals("use-classpath")) {
            if (value instanceof Boolean) {
               this._useClasspath = (Boolean)value;
               return;
            }

            if (value instanceof String) {
               this._useClasspath = ((String)value).equalsIgnoreCase("true");
               return;
            }
         } else if (name.equals("debug")) {
            if (value instanceof Boolean) {
               this._debug = (Boolean)value;
               return;
            }

            if (value instanceof String) {
               this._debug = ((String)value).equalsIgnoreCase("true");
               return;
            }
         } else if (name.equals("enable-inlining")) {
            if (value instanceof Boolean) {
               this._enableInlining = (Boolean)value;
               return;
            }

            if (value instanceof String) {
               this._enableInlining = ((String)value).equalsIgnoreCase("true");
               return;
            }
         } else if (name.equals("indent-number")) {
            if (value instanceof String) {
               try {
                  this._indentNumber = Integer.parseInt((String)value);
                  return;
               } catch (NumberFormatException var4) {
               }
            } else if (value instanceof Integer) {
               this._indentNumber = (Integer)value;
               return;
            }
         }

         ErrorMsg err = new ErrorMsg("JAXP_INVALID_ATTR_ERR", name);
         throw new IllegalArgumentException(err.toString());
      }
   }

   public void setFeature(String name, boolean value) throws TransformerConfigurationException {
      ErrorMsg err;
      if (name == null) {
         err = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
         throw new NullPointerException(err.toString());
      } else if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
         this._isSecureProcessing = value;
      } else {
         err = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", name);
         throw new TransformerConfigurationException(err.toString());
      }
   }

   public boolean getFeature(String name) {
      String[] features = new String[]{"http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter"};
      if (name == null) {
         ErrorMsg err = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
         throw new NullPointerException(err.toString());
      } else {
         for(int i = 0; i < features.length; ++i) {
            if (name.equals(features[i])) {
               return true;
            }
         }

         return name.equals("http://javax.xml.XMLConstants/feature/secure-processing") ? this._isSecureProcessing : false;
      }
   }

   public URIResolver getURIResolver() {
      return this._uriResolver;
   }

   public void setURIResolver(URIResolver resolver) {
      this._uriResolver = resolver;
   }

   public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
      XMLReader reader = null;
      InputSource isource = null;
      StylesheetPIHandler _stylesheetPIHandler = new StylesheetPIHandler((String)null, media, title, charset);

      try {
         String baseId;
         if (source instanceof DOMSource) {
            DOMSource domsrc = (DOMSource)source;
            baseId = domsrc.getSystemId();
            Node node = domsrc.getNode();
            DOM2SAX dom2sax = new DOM2SAX(node);
            _stylesheetPIHandler.setBaseId(baseId);
            dom2sax.setContentHandler(_stylesheetPIHandler);
            dom2sax.parse();
         } else {
            isource = SAXSource.sourceToInputSource(source);
            baseId = isource.getSystemId();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            if (this._isSecureProcessing) {
               try {
                  factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
               } catch (SAXException var13) {
               }
            }

            SAXParser jaxpParser = factory.newSAXParser();
            reader = jaxpParser.getXMLReader();
            if (reader == null) {
               reader = XMLReaderFactory.createXMLReader();
            }

            _stylesheetPIHandler.setBaseId(baseId);
            reader.setContentHandler(_stylesheetPIHandler);
            reader.parse(isource);
         }

         if (this._uriResolver != null) {
            _stylesheetPIHandler.setURIResolver(this._uriResolver);
         }
      } catch (StopParseException var14) {
      } catch (ParserConfigurationException var15) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var15);
      } catch (SAXException var16) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var16);
      } catch (IOException var17) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var17);
      }

      return _stylesheetPIHandler.getAssociatedStylesheet();
   }

   public Transformer newTransformer() throws TransformerConfigurationException {
      TransformerImpl result = new TransformerImpl(new Properties(), this._indentNumber, this);
      if (this._uriResolver != null) {
         result.setURIResolver(this._uriResolver);
      }

      if (this._isSecureProcessing) {
         result.setSecureProcessing(true);
      }

      return result;
   }

   public Transformer newTransformer(Source source) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(source);
      Transformer transformer = templates.newTransformer();
      if (this._uriResolver != null) {
         transformer.setURIResolver(this._uriResolver);
      }

      return transformer;
   }

   private void passWarningsToListener(Vector messages) throws TransformerException {
      if (this._errorListener != null && messages != null) {
         int count = messages.size();

         for(int pos = 0; pos < count; ++pos) {
            ErrorMsg msg = (ErrorMsg)messages.elementAt(pos);
            if (msg.isWarningError()) {
               this._errorListener.error(new TransformerConfigurationException(msg.toString()));
            } else {
               this._errorListener.warning(new TransformerConfigurationException(msg.toString()));
            }
         }

      }
   }

   private void passErrorsToListener(Vector messages) {
      try {
         if (this._errorListener == null || messages == null) {
            return;
         }

         int count = messages.size();

         for(int pos = 0; pos < count; ++pos) {
            String message = messages.elementAt(pos).toString();
            this._errorListener.error(new TransformerException(message));
         }
      } catch (TransformerException var5) {
      }

   }

   public Templates newTemplates(Source source) throws TransformerConfigurationException {
      if (this._useClasspath) {
         String transletName = this.getTransletBaseName(source);
         if (this._packageName != null) {
            transletName = this._packageName + "." + transletName;
         }

         try {
            Class clazz = ObjectFactory.findProviderClass(transletName, ObjectFactory.findClassLoader(), true);
            this.resetTransientAttributes();
            return new TemplatesImpl(new Class[]{clazz}, transletName, (Properties)null, this._indentNumber, this);
         } catch (ClassNotFoundException var10) {
            ErrorMsg err = new ErrorMsg("CLASS_NOT_FOUND_ERR", transletName);
            throw new TransformerConfigurationException(err.toString());
         } catch (Exception var11) {
            ErrorMsg err = new ErrorMsg(new ErrorMsg("RUNTIME_ERROR_KEY") + var11.getMessage());
            throw new TransformerConfigurationException(err.toString());
         }
      } else {
         XSLTC xsltc;
         if (this._autoTranslet) {
            xsltc = null;
            String transletClassName = this.getTransletBaseName(source);
            if (this._packageName != null) {
               transletClassName = this._packageName + "." + transletClassName;
            }

            byte[][] bytecodes;
            if (this._jarFileName != null) {
               bytecodes = this.getBytecodesFromJar(source, transletClassName);
            } else {
               bytecodes = this.getBytecodesFromClasses(source, transletClassName);
            }

            if (bytecodes != null) {
               if (this._debug) {
                  if (this._jarFileName != null) {
                     System.err.println(new ErrorMsg("TRANSFORM_WITH_JAR_STR", transletClassName, this._jarFileName));
                  } else {
                     System.err.println(new ErrorMsg("TRANSFORM_WITH_TRANSLET_STR", transletClassName));
                  }
               }

               this.resetTransientAttributes();
               return new TemplatesImpl(bytecodes, transletClassName, (Properties)null, this._indentNumber, this);
            }
         }

         xsltc = new XSLTC();
         if (this._debug) {
            xsltc.setDebug(true);
         }

         if (this._enableInlining) {
            xsltc.setTemplateInlining(true);
         }

         if (this._isSecureProcessing) {
            xsltc.setSecureProcessing(true);
         }

         xsltc.init();
         if (this._uriResolver != null) {
            xsltc.setSourceLoader(this);
         }

         if (this._piParams != null && this._piParams.get(source) != null) {
            PIParamWrapper p = (PIParamWrapper)this._piParams.get(source);
            if (p != null) {
               xsltc.setPIParameters(p._media, p._title, p._charset);
            }
         }

         int outputType = 2;
         String transletName;
         if (this._generateTranslet || this._autoTranslet) {
            xsltc.setClassName(this.getTransletBaseName(source));
            if (this._destinationDirectory != null) {
               xsltc.setDestDirectory(this._destinationDirectory);
            } else {
               String xslName = this.getStylesheetFileName(source);
               if (xslName != null) {
                  File xslFile = new File(xslName);
                  transletName = xslFile.getParent();
                  if (transletName != null) {
                     xsltc.setDestDirectory(transletName);
                  }
               }
            }

            if (this._packageName != null) {
               xsltc.setPackageName(this._packageName);
            }

            if (this._jarFileName != null) {
               xsltc.setJarFileName(this._jarFileName);
               outputType = 5;
            } else {
               outputType = 4;
            }
         }

         InputSource input = Util.getInputSource(xsltc, source);
         byte[][] bytecodes = xsltc.compile((String)null, input, outputType);
         transletName = xsltc.getClassName();
         if ((this._generateTranslet || this._autoTranslet) && bytecodes != null && this._jarFileName != null) {
            try {
               xsltc.outputToJar();
            } catch (IOException var14) {
            }
         }

         this.resetTransientAttributes();
         if (this._errorListener != this) {
            try {
               this.passWarningsToListener(xsltc.getWarnings());
            } catch (TransformerException var13) {
               throw new TransformerConfigurationException(var13);
            }
         } else {
            xsltc.printWarnings();
         }

         if (bytecodes == null) {
            ErrorMsg err = new ErrorMsg("JAXP_COMPILE_ERR");
            TransformerConfigurationException exc = new TransformerConfigurationException(err.toString());
            if (this._errorListener != null) {
               this.passErrorsToListener(xsltc.getErrors());

               try {
                  this._errorListener.fatalError(exc);
               } catch (TransformerException var12) {
               }
            } else {
               xsltc.printErrors();
            }

            throw exc;
         } else {
            return new TemplatesImpl(bytecodes, transletName, xsltc.getOutputProperties(), this._indentNumber, this);
         }
      }
   }

   public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
      TemplatesHandlerImpl handler = new TemplatesHandlerImpl(this._indentNumber, this);
      if (this._uriResolver != null) {
         handler.setURIResolver(this._uriResolver);
      }

      return handler;
   }

   public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
      Transformer transformer = this.newTransformer();
      if (this._uriResolver != null) {
         transformer.setURIResolver(this._uriResolver);
      }

      return new TransformerHandlerImpl((TransformerImpl)transformer);
   }

   public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
      Transformer transformer = this.newTransformer(src);
      if (this._uriResolver != null) {
         transformer.setURIResolver(this._uriResolver);
      }

      return new TransformerHandlerImpl((TransformerImpl)transformer);
   }

   public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
      Transformer transformer = templates.newTransformer();
      TransformerImpl internal = (TransformerImpl)transformer;
      return new TransformerHandlerImpl(internal);
   }

   public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(src);
      return templates == null ? null : this.newXMLFilter(templates);
   }

   public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
      try {
         return new TrAXFilter(templates);
      } catch (TransformerConfigurationException var5) {
         TransformerConfigurationException e1 = var5;
         if (this._errorListener != null) {
            try {
               this._errorListener.fatalError(e1);
               return null;
            } catch (TransformerException var4) {
               new TransformerConfigurationException(var4);
            }
         }

         throw var5;
      }
   }

   public void error(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("ERROR_MSG", e.getMessageAndLocation()));
      }

      throw e;
   }

   public void fatalError(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("FATAL_ERR_MSG", e.getMessageAndLocation()));
      }

      throw e;
   }

   public void warning(TransformerException e) throws TransformerException {
      Throwable wrapped = e.getException();
      if (wrapped != null) {
         System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
      } else {
         System.err.println(new ErrorMsg("WARNING_MSG", e.getMessageAndLocation()));
      }

   }

   public InputSource loadSource(String href, String context, XSLTC xsltc) {
      try {
         if (this._uriResolver != null) {
            Source source = this._uriResolver.resolve(href, context);
            if (source != null) {
               return Util.getInputSource(xsltc, source);
            }
         }
      } catch (TransformerException var5) {
      }

      return null;
   }

   private void resetTransientAttributes() {
      this._transletName = "GregorSamsa";
      this._destinationDirectory = null;
      this._packageName = null;
      this._jarFileName = null;
   }

   private byte[][] getBytecodesFromClasses(Source source, String fullClassName) {
      if (fullClassName == null) {
         return null;
      } else {
         String xslFileName = this.getStylesheetFileName(source);
         File xslFile = null;
         if (xslFileName != null) {
            xslFile = new File(xslFileName);
         }

         int lastDotIndex = fullClassName.lastIndexOf(46);
         String transletName;
         if (lastDotIndex > 0) {
            transletName = fullClassName.substring(lastDotIndex + 1);
         } else {
            transletName = fullClassName;
         }

         String transletPath = fullClassName.replace('.', '/');
         if (this._destinationDirectory != null) {
            transletPath = this._destinationDirectory + "/" + transletPath + ".class";
         } else if (xslFile != null && xslFile.getParent() != null) {
            transletPath = xslFile.getParent() + "/" + transletPath + ".class";
         } else {
            transletPath = transletPath + ".class";
         }

         File transletFile = new File(transletPath);
         if (!transletFile.exists()) {
            return null;
         } else {
            if (xslFile != null && xslFile.exists()) {
               long xslTimestamp = xslFile.lastModified();
               long transletTimestamp = transletFile.lastModified();
               if (transletTimestamp < xslTimestamp) {
                  return null;
               }
            }

            Vector bytecodes = new Vector();
            int fileLength = (int)transletFile.length();
            if (fileLength <= 0) {
               return null;
            } else {
               FileInputStream input = null;

               try {
                  input = new FileInputStream(transletFile);
               } catch (FileNotFoundException var22) {
                  return null;
               }

               byte[] bytes = new byte[fileLength];

               try {
                  this.readFromInputStream(bytes, input, fileLength);
                  input.close();
               } catch (IOException var21) {
                  return null;
               }

               bytecodes.addElement(bytes);
               String transletParentDir = transletFile.getParent();
               if (transletParentDir == null) {
                  transletParentDir = System.getProperty("user.dir");
               }

               File transletParentFile = new File(transletParentDir);
               final String transletAuxPrefix = transletName + "$";
               File[] auxfiles = transletParentFile.listFiles(new FilenameFilter() {
                  public boolean accept(File dir, String name) {
                     return name.endsWith(".class") && name.startsWith(transletAuxPrefix);
                  }
               });

               for(int i = 0; i < auxfiles.length; ++i) {
                  File auxfile = auxfiles[i];
                  int auxlength = (int)auxfile.length();
                  if (auxlength > 0) {
                     FileInputStream auxinput = null;

                     try {
                        auxinput = new FileInputStream(auxfile);
                     } catch (FileNotFoundException var24) {
                        continue;
                     }

                     byte[] bytes = new byte[auxlength];

                     try {
                        this.readFromInputStream(bytes, auxinput, auxlength);
                        auxinput.close();
                     } catch (IOException var23) {
                        continue;
                     }

                     bytecodes.addElement(bytes);
                  }
               }

               int count = bytecodes.size();
               if (count <= 0) {
                  return null;
               } else {
                  byte[][] result = new byte[count][1];

                  for(int i = 0; i < count; ++i) {
                     result[i] = (byte[])bytecodes.elementAt(i);
                  }

                  return result;
               }
            }
         }
      }
   }

   private byte[][] getBytecodesFromJar(Source source, String fullClassName) {
      String xslFileName = this.getStylesheetFileName(source);
      File xslFile = null;
      if (xslFileName != null) {
         xslFile = new File(xslFileName);
      }

      String jarPath = null;
      if (this._destinationDirectory != null) {
         jarPath = this._destinationDirectory + "/" + this._jarFileName;
      } else if (xslFile != null && xslFile.getParent() != null) {
         jarPath = xslFile.getParent() + "/" + this._jarFileName;
      } else {
         jarPath = this._jarFileName;
      }

      File file = new File(jarPath);
      if (!file.exists()) {
         return null;
      } else {
         if (xslFile != null && xslFile.exists()) {
            long xslTimestamp = xslFile.lastModified();
            long transletTimestamp = file.lastModified();
            if (transletTimestamp < xslTimestamp) {
               return null;
            }
         }

         ZipFile jarFile = null;

         try {
            jarFile = new ZipFile(file);
         } catch (IOException var19) {
            return null;
         }

         String transletPath = fullClassName.replace('.', '/');
         String transletAuxPrefix = transletPath + "$";
         String transletFullName = transletPath + ".class";
         Vector bytecodes = new Vector();
         Enumeration entries = jarFile.entries();

         while(true) {
            ZipEntry entry;
            String entryName;
            do {
               do {
                  if (!entries.hasMoreElements()) {
                     int count = bytecodes.size();
                     if (count <= 0) {
                        return null;
                     }

                     byte[][] result = new byte[count][1];

                     for(int i = 0; i < count; ++i) {
                        result[i] = (byte[])bytecodes.elementAt(i);
                     }

                     return result;
                  }

                  entry = (ZipEntry)entries.nextElement();
                  entryName = entry.getName();
               } while(entry.getSize() <= 0L);
            } while(!entryName.equals(transletFullName) && (!entryName.endsWith(".class") || !entryName.startsWith(transletAuxPrefix)));

            try {
               InputStream input = jarFile.getInputStream(entry);
               int size = (int)entry.getSize();
               byte[] bytes = new byte[size];
               this.readFromInputStream(bytes, input, size);
               input.close();
               bytecodes.addElement(bytes);
            } catch (IOException var18) {
               return null;
            }
         }
      }
   }

   private void readFromInputStream(byte[] bytes, InputStream input, int size) throws IOException {
      int n = false;
      int offset = 0;

      int n;
      for(int length = size; length > 0 && (n = input.read(bytes, offset, length)) > 0; length -= n) {
         offset += n;
      }

   }

   private String getTransletBaseName(Source source) {
      String transletBaseName = null;
      if (!this._transletName.equals("GregorSamsa")) {
         return this._transletName;
      } else {
         String systemId = source.getSystemId();
         if (systemId != null) {
            String baseName = Util.baseName(systemId);
            if (baseName != null) {
               baseName = Util.noExtName(baseName);
               transletBaseName = Util.toJavaName(baseName);
            }
         }

         return transletBaseName != null ? transletBaseName : "GregorSamsa";
      }
   }

   private String getStylesheetFileName(Source source) {
      String systemId = source.getSystemId();
      if (systemId != null) {
         File file = new File(systemId);
         if (file.exists()) {
            return systemId;
         } else {
            URL url = null;

            try {
               url = new URL(systemId);
            } catch (MalformedURLException var6) {
               return null;
            }

            return "file".equals(url.getProtocol()) ? url.getFile() : null;
         }
      } else {
         return null;
      }
   }

   protected Class getDTMManagerClass() {
      return this.m_DTMManagerClass;
   }

   private static class PIParamWrapper {
      public String _media = null;
      public String _title = null;
      public String _charset = null;

      public PIParamWrapper(String media, String title, String charset) {
         this._media = media;
         this._title = title;
         this._charset = charset;
      }
   }
}
