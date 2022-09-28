package org.apache.xalan.xsltc.compiler;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java_cup.runtime.Symbol;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class Parser implements Constants, ContentHandler {
   private static final String XSL = "xsl";
   private static final String TRANSLET = "translet";
   private Locator _locator = null;
   private XSLTC _xsltc;
   private XPathParser _xpathParser;
   private Vector _errors;
   private Vector _warnings;
   private Hashtable _instructionClasses;
   private Hashtable _instructionAttrs;
   private Hashtable _qNames;
   private Hashtable _namespaces;
   private QName _useAttributeSets;
   private QName _excludeResultPrefixes;
   private QName _extensionElementPrefixes;
   private Hashtable _variableScope;
   private Stylesheet _currentStylesheet;
   private SymbolTable _symbolTable;
   private Output _output;
   private Template _template;
   private boolean _rootNamespaceDef;
   private SyntaxTreeNode _root;
   private String _target;
   private int _currentImportPrecedence;
   private String _PImedia = null;
   private String _PItitle = null;
   private String _PIcharset = null;
   private int _templateIndex = 0;
   private boolean versionIsOne = true;
   private Stack _parentStack = null;
   private Hashtable _prefixMapping = null;

   public Parser(XSLTC xsltc) {
      this._xsltc = xsltc;
   }

   public void init() {
      this._qNames = new Hashtable(512);
      this._namespaces = new Hashtable();
      this._instructionClasses = new Hashtable();
      this._instructionAttrs = new Hashtable();
      this._variableScope = new Hashtable();
      this._template = null;
      this._errors = new Vector();
      this._warnings = new Vector();
      this._symbolTable = new SymbolTable();
      this._xpathParser = new XPathParser(this);
      this._currentStylesheet = null;
      this._output = null;
      this._root = null;
      this._rootNamespaceDef = false;
      this._currentImportPrecedence = 1;
      this.initStdClasses();
      this.initInstructionAttrs();
      this.initExtClasses();
      this.initSymbolTable();
      this._useAttributeSets = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "use-attribute-sets");
      this._excludeResultPrefixes = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "exclude-result-prefixes");
      this._extensionElementPrefixes = this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", "extension-element-prefixes");
   }

   public void setOutput(Output output) {
      if (this._output != null) {
         if (this._output.getImportPrecedence() <= output.getImportPrecedence()) {
            String cdata = this._output.getCdata();
            output.mergeOutput(this._output);
            this._output.disable();
            this._output = output;
         } else {
            output.disable();
         }
      } else {
         this._output = output;
      }

   }

   public Output getOutput() {
      return this._output;
   }

   public Properties getOutputProperties() {
      return this.getTopLevelStylesheet().getOutputProperties();
   }

   public void addVariable(Variable var) {
      this.addVariableOrParam(var);
   }

   public void addParameter(Param param) {
      this.addVariableOrParam(param);
   }

   private void addVariableOrParam(VariableBase var) {
      Object existing = this._variableScope.get(var.getName());
      if (existing != null) {
         Stack stack;
         if (existing instanceof Stack) {
            stack = (Stack)existing;
            stack.push(var);
         } else if (existing instanceof VariableBase) {
            stack = new Stack();
            stack.push(existing);
            stack.push(var);
            this._variableScope.put(var.getName(), stack);
         }
      } else {
         this._variableScope.put(var.getName(), var);
      }

   }

   public void removeVariable(QName name) {
      Object existing = this._variableScope.get(name);
      if (existing instanceof Stack) {
         Stack stack = (Stack)existing;
         if (!stack.isEmpty()) {
            stack.pop();
         }

         if (!stack.isEmpty()) {
            return;
         }
      }

      this._variableScope.remove(name);
   }

   public VariableBase lookupVariable(QName name) {
      Object existing = this._variableScope.get(name);
      if (existing instanceof VariableBase) {
         return (VariableBase)existing;
      } else if (existing instanceof Stack) {
         Stack stack = (Stack)existing;
         return (VariableBase)stack.peek();
      } else {
         return null;
      }
   }

   public void setXSLTC(XSLTC xsltc) {
      this._xsltc = xsltc;
   }

   public XSLTC getXSLTC() {
      return this._xsltc;
   }

   public int getCurrentImportPrecedence() {
      return this._currentImportPrecedence;
   }

   public int getNextImportPrecedence() {
      return ++this._currentImportPrecedence;
   }

   public void setCurrentStylesheet(Stylesheet stylesheet) {
      this._currentStylesheet = stylesheet;
   }

   public Stylesheet getCurrentStylesheet() {
      return this._currentStylesheet;
   }

   public Stylesheet getTopLevelStylesheet() {
      return this._xsltc.getStylesheet();
   }

   public QName getQNameSafe(String stringRep) {
      int colon = stringRep.lastIndexOf(58);
      String prefix;
      if (colon != -1) {
         prefix = stringRep.substring(0, colon);
         String localname = stringRep.substring(colon + 1);
         String namespace = null;
         if (!prefix.equals("xmlns")) {
            namespace = this._symbolTable.lookupNamespace(prefix);
            if (namespace == null) {
               namespace = "";
            }
         }

         return this.getQName(namespace, prefix, localname);
      } else {
         prefix = stringRep.equals("xmlns") ? null : this._symbolTable.lookupNamespace("");
         return this.getQName(prefix, (String)null, stringRep);
      }
   }

   public QName getQName(String stringRep) {
      return this.getQName(stringRep, true, false);
   }

   public QName getQNameIgnoreDefaultNs(String stringRep) {
      return this.getQName(stringRep, true, true);
   }

   public QName getQName(String stringRep, boolean reportError) {
      return this.getQName(stringRep, reportError, false);
   }

   private QName getQName(String stringRep, boolean reportError, boolean ignoreDefaultNs) {
      int colon = stringRep.lastIndexOf(58);
      String prefix;
      if (colon != -1) {
         prefix = stringRep.substring(0, colon);
         String localname = stringRep.substring(colon + 1);
         String namespace = null;
         if (!prefix.equals("xmlns")) {
            namespace = this._symbolTable.lookupNamespace(prefix);
            if (namespace == null && reportError) {
               int line = this.getLineNumber();
               ErrorMsg err = new ErrorMsg("NAMESPACE_UNDEF_ERR", line, prefix);
               this.reportError(3, err);
            }
         }

         return this.getQName(namespace, prefix, localname);
      } else {
         if (stringRep.equals("xmlns")) {
            ignoreDefaultNs = true;
         }

         prefix = ignoreDefaultNs ? null : this._symbolTable.lookupNamespace("");
         return this.getQName(prefix, (String)null, stringRep);
      }
   }

   public QName getQName(String namespace, String prefix, String localname) {
      if (namespace != null && !namespace.equals("")) {
         Dictionary space = (Dictionary)this._namespaces.get(namespace);
         QName name;
         if (space == null) {
            name = new QName(namespace, prefix, localname);
            Hashtable space;
            this._namespaces.put(namespace, space = new Hashtable());
            space.put(localname, name);
            return name;
         } else {
            name = (QName)space.get(localname);
            if (name == null) {
               name = new QName(namespace, prefix, localname);
               space.put(localname, name);
            }

            return name;
         }
      } else {
         QName name = (QName)this._qNames.get(localname);
         if (name == null) {
            name = new QName((String)null, prefix, localname);
            this._qNames.put(localname, name);
         }

         return name;
      }
   }

   public QName getQName(String scope, String name) {
      return this.getQName(scope + name);
   }

   public QName getQName(QName scope, QName name) {
      return this.getQName(scope.toString() + name.toString());
   }

   public QName getUseAttributeSets() {
      return this._useAttributeSets;
   }

   public QName getExtensionElementPrefixes() {
      return this._extensionElementPrefixes;
   }

   public QName getExcludeResultPrefixes() {
      return this._excludeResultPrefixes;
   }

   public Stylesheet makeStylesheet(SyntaxTreeNode element) throws CompilerException {
      try {
         Stylesheet stylesheet;
         if (element instanceof Stylesheet) {
            stylesheet = (Stylesheet)element;
         } else {
            stylesheet = new Stylesheet();
            stylesheet.setSimplified();
            stylesheet.addElement(element);
            stylesheet.setAttributes((AttributeList)element.getAttributes());
            if (element.lookupNamespace("") == null) {
               element.addPrefixMapping("", "");
            }
         }

         stylesheet.setParser(this);
         return stylesheet;
      } catch (ClassCastException var4) {
         ErrorMsg err = new ErrorMsg("NOT_STYLESHEET_ERR", element);
         throw new CompilerException(err.toString());
      }
   }

   public void createAST(Stylesheet stylesheet) {
      try {
         if (stylesheet != null) {
            stylesheet.parseContents(this);
            int precedence = stylesheet.getImportPrecedence();
            Enumeration elements = stylesheet.elements();

            while(elements.hasMoreElements()) {
               Object child = elements.nextElement();
               if (child instanceof Text) {
                  int l = this.getLineNumber();
                  ErrorMsg err = new ErrorMsg("ILLEGAL_TEXT_NODE_ERR", l, (Object)null);
                  this.reportError(3, err);
               }
            }

            if (!this.errorsFound()) {
               stylesheet.typeCheck(this._symbolTable);
            }
         }
      } catch (TypeCheckError var7) {
         this.reportError(3, new ErrorMsg(var7));
      }

   }

   public SyntaxTreeNode parse(XMLReader reader, InputSource input) {
      try {
         reader.setContentHandler(this);
         reader.parse(input);
         return this.getStylesheet(this._root);
      } catch (IOException var7) {
         if (this._xsltc.debug()) {
            var7.printStackTrace();
         }

         this.reportError(3, new ErrorMsg(var7));
      } catch (SAXException var8) {
         Throwable ex = var8.getException();
         if (this._xsltc.debug()) {
            var8.printStackTrace();
            if (ex != null) {
               ex.printStackTrace();
            }
         }

         this.reportError(3, new ErrorMsg(var8));
      } catch (CompilerException var9) {
         if (this._xsltc.debug()) {
            var9.printStackTrace();
         }

         this.reportError(3, new ErrorMsg(var9));
      } catch (Exception var10) {
         if (this._xsltc.debug()) {
            var10.printStackTrace();
         }

         this.reportError(3, new ErrorMsg(var10));
      }

      return null;
   }

   public SyntaxTreeNode parse(InputSource input) {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         if (this._xsltc.isSecureProcessing()) {
            try {
               factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            } catch (SAXException var6) {
            }
         }

         try {
            factory.setFeature("http://xml.org/sax/features/namespaces", true);
         } catch (Exception var5) {
            factory.setNamespaceAware(true);
         }

         SAXParser parser = factory.newSAXParser();
         XMLReader reader = parser.getXMLReader();
         return this.parse(reader, input);
      } catch (ParserConfigurationException var7) {
         ErrorMsg err = new ErrorMsg("SAX_PARSER_CONFIG_ERR");
         this.reportError(3, err);
      } catch (SAXParseException var8) {
         this.reportError(3, new ErrorMsg(var8.getMessage(), var8.getLineNumber()));
      } catch (SAXException var9) {
         this.reportError(3, new ErrorMsg(var9.getMessage()));
      }

      return null;
   }

   public SyntaxTreeNode getDocumentRoot() {
      return this._root;
   }

   protected void setPIParameters(String media, String title, String charset) {
      this._PImedia = media;
      this._PItitle = title;
      this._PIcharset = charset;
   }

   private SyntaxTreeNode getStylesheet(SyntaxTreeNode root) throws CompilerException {
      if (this._target == null) {
         if (!this._rootNamespaceDef) {
            ErrorMsg msg = new ErrorMsg("MISSING_XSLT_URI_ERR");
            throw new CompilerException(msg.toString());
         } else {
            return root;
         }
      } else if (this._target.charAt(0) == '#') {
         SyntaxTreeNode element = this.findStylesheet(root, this._target.substring(1));
         if (element == null) {
            ErrorMsg msg = new ErrorMsg("MISSING_XSLT_TARGET_ERR", this._target, root);
            throw new CompilerException(msg.toString());
         } else {
            return element;
         }
      } else {
         return this.loadExternalStylesheet(this._target);
      }
   }

   private SyntaxTreeNode findStylesheet(SyntaxTreeNode root, String href) {
      if (root == null) {
         return null;
      } else {
         if (root instanceof Stylesheet) {
            String id = root.getAttribute("id");
            if (id.equals(href)) {
               return root;
            }
         }

         Vector children = root.getContents();
         if (children != null) {
            int count = children.size();

            for(int i = 0; i < count; ++i) {
               SyntaxTreeNode child = (SyntaxTreeNode)children.elementAt(i);
               SyntaxTreeNode node = this.findStylesheet(child, href);
               if (node != null) {
                  return node;
               }
            }
         }

         return null;
      }
   }

   private SyntaxTreeNode loadExternalStylesheet(String location) throws CompilerException {
      InputSource source;
      if ((new File(location)).exists()) {
         source = new InputSource("file:" + location);
      } else {
         source = new InputSource(location);
      }

      SyntaxTreeNode external = this.parse(source);
      return external;
   }

   private void initAttrTable(String elementName, String[] attrs) {
      this._instructionAttrs.put(this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", elementName), attrs);
   }

   private void initInstructionAttrs() {
      this.initAttrTable("template", new String[]{"match", "name", "priority", "mode"});
      this.initAttrTable("stylesheet", new String[]{"id", "version", "extension-element-prefixes", "exclude-result-prefixes"});
      this.initAttrTable("transform", new String[]{"id", "version", "extension-element-prefixes", "exclude-result-prefixes"});
      this.initAttrTable("text", new String[]{"disable-output-escaping"});
      this.initAttrTable("if", new String[]{"test"});
      this.initAttrTable("choose", new String[0]);
      this.initAttrTable("when", new String[]{"test"});
      this.initAttrTable("otherwise", new String[0]);
      this.initAttrTable("for-each", new String[]{"select"});
      this.initAttrTable("message", new String[]{"terminate"});
      this.initAttrTable("number", new String[]{"level", "count", "from", "value", "format", "lang", "letter-value", "grouping-separator", "grouping-size"});
      this.initAttrTable("comment", new String[0]);
      this.initAttrTable("copy", new String[]{"use-attribute-sets"});
      this.initAttrTable("copy-of", new String[]{"select"});
      this.initAttrTable("param", new String[]{"name", "select"});
      this.initAttrTable("with-param", new String[]{"name", "select"});
      this.initAttrTable("variable", new String[]{"name", "select"});
      this.initAttrTable("output", new String[]{"method", "version", "encoding", "omit-xml-declaration", "standalone", "doctype-public", "doctype-system", "cdata-section-elements", "indent", "media-type"});
      this.initAttrTable("sort", new String[]{"select", "order", "case-order", "lang", "data-type"});
      this.initAttrTable("key", new String[]{"name", "match", "use"});
      this.initAttrTable("fallback", new String[0]);
      this.initAttrTable("attribute", new String[]{"name", "namespace"});
      this.initAttrTable("attribute-set", new String[]{"name", "use-attribute-sets"});
      this.initAttrTable("value-of", new String[]{"select", "disable-output-escaping"});
      this.initAttrTable("element", new String[]{"name", "namespace", "use-attribute-sets"});
      this.initAttrTable("call-template", new String[]{"name"});
      this.initAttrTable("apply-templates", new String[]{"select", "mode"});
      this.initAttrTable("apply-imports", new String[0]);
      this.initAttrTable("decimal-format", new String[]{"name", "decimal-separator", "grouping-separator", "infinity", "minus-sign", "NaN", "percent", "per-mille", "zero-digit", "digit", "pattern-separator"});
      this.initAttrTable("import", new String[]{"href"});
      this.initAttrTable("include", new String[]{"href"});
      this.initAttrTable("strip-space", new String[]{"elements"});
      this.initAttrTable("preserve-space", new String[]{"elements"});
      this.initAttrTable("processing-instruction", new String[]{"name"});
      this.initAttrTable("namespace-alias", new String[]{"stylesheet-prefix", "result-prefix"});
   }

   private void initStdClasses() {
      this.initStdClass("template", "Template");
      this.initStdClass("stylesheet", "Stylesheet");
      this.initStdClass("transform", "Stylesheet");
      this.initStdClass("text", "Text");
      this.initStdClass("if", "If");
      this.initStdClass("choose", "Choose");
      this.initStdClass("when", "When");
      this.initStdClass("otherwise", "Otherwise");
      this.initStdClass("for-each", "ForEach");
      this.initStdClass("message", "Message");
      this.initStdClass("number", "Number");
      this.initStdClass("comment", "Comment");
      this.initStdClass("copy", "Copy");
      this.initStdClass("copy-of", "CopyOf");
      this.initStdClass("param", "Param");
      this.initStdClass("with-param", "WithParam");
      this.initStdClass("variable", "Variable");
      this.initStdClass("output", "Output");
      this.initStdClass("sort", "Sort");
      this.initStdClass("key", "Key");
      this.initStdClass("fallback", "Fallback");
      this.initStdClass("attribute", "XslAttribute");
      this.initStdClass("attribute-set", "AttributeSet");
      this.initStdClass("value-of", "ValueOf");
      this.initStdClass("element", "XslElement");
      this.initStdClass("call-template", "CallTemplate");
      this.initStdClass("apply-templates", "ApplyTemplates");
      this.initStdClass("apply-imports", "ApplyImports");
      this.initStdClass("decimal-format", "DecimalFormatting");
      this.initStdClass("import", "Import");
      this.initStdClass("include", "Include");
      this.initStdClass("strip-space", "Whitespace");
      this.initStdClass("preserve-space", "Whitespace");
      this.initStdClass("processing-instruction", "ProcessingInstruction");
      this.initStdClass("namespace-alias", "NamespaceAlias");
   }

   private void initStdClass(String elementName, String className) {
      this._instructionClasses.put(this.getQName("http://www.w3.org/1999/XSL/Transform", "xsl", elementName), "org.apache.xalan.xsltc.compiler." + className);
   }

   public boolean elementSupported(String namespace, String localName) {
      return this._instructionClasses.get(this.getQName(namespace, "xsl", localName)) != null;
   }

   public boolean functionSupported(String fname) {
      return this._symbolTable.lookupPrimop(fname) != null;
   }

   private void initExtClasses() {
      this.initExtClass("output", "TransletOutput");
      this.initExtClass("http://xml.apache.org/xalan/redirect", "write", "TransletOutput");
   }

   private void initExtClass(String elementName, String className) {
      this._instructionClasses.put(this.getQName("http://xml.apache.org/xalan/xsltc", "translet", elementName), "org.apache.xalan.xsltc.compiler." + className);
   }

   private void initExtClass(String namespace, String elementName, String className) {
      this._instructionClasses.put(this.getQName(namespace, "translet", elementName), "org.apache.xalan.xsltc.compiler." + className);
   }

   private void initSymbolTable() {
      MethodType I_V = new MethodType(Type.Int, Type.Void);
      new MethodType(Type.Int, Type.Real);
      MethodType I_S = new MethodType(Type.Int, Type.String);
      MethodType I_D = new MethodType(Type.Int, Type.NodeSet);
      new MethodType(Type.Real, Type.Int);
      MethodType R_V = new MethodType(Type.Real, Type.Void);
      MethodType R_R = new MethodType(Type.Real, Type.Real);
      MethodType R_D = new MethodType(Type.Real, Type.NodeSet);
      MethodType R_O = new MethodType(Type.Real, Type.Reference);
      MethodType I_I = new MethodType(Type.Int, Type.Int);
      MethodType D_O = new MethodType(Type.NodeSet, Type.Reference);
      MethodType D_V = new MethodType(Type.NodeSet, Type.Void);
      MethodType D_S = new MethodType(Type.NodeSet, Type.String);
      MethodType D_D = new MethodType(Type.NodeSet, Type.NodeSet);
      MethodType A_V = new MethodType(Type.Node, Type.Void);
      MethodType S_V = new MethodType(Type.String, Type.Void);
      MethodType S_S = new MethodType(Type.String, Type.String);
      MethodType S_A = new MethodType(Type.String, Type.Node);
      MethodType S_D = new MethodType(Type.String, Type.NodeSet);
      MethodType S_O = new MethodType(Type.String, Type.Reference);
      MethodType B_O = new MethodType(Type.Boolean, Type.Reference);
      MethodType B_V = new MethodType(Type.Boolean, Type.Void);
      MethodType B_B = new MethodType(Type.Boolean, Type.Boolean);
      MethodType B_S = new MethodType(Type.Boolean, Type.String);
      new MethodType(Type.NodeSet, Type.Object);
      MethodType R_RR = new MethodType(Type.Real, Type.Real, Type.Real);
      MethodType I_II = new MethodType(Type.Int, Type.Int, Type.Int);
      MethodType B_RR = new MethodType(Type.Boolean, Type.Real, Type.Real);
      MethodType B_II = new MethodType(Type.Boolean, Type.Int, Type.Int);
      MethodType S_SS = new MethodType(Type.String, Type.String, Type.String);
      MethodType S_DS = new MethodType(Type.String, Type.Real, Type.String);
      MethodType S_SR = new MethodType(Type.String, Type.String, Type.Real);
      MethodType O_SO = new MethodType(Type.Reference, Type.String, Type.Reference);
      MethodType D_SS = new MethodType(Type.NodeSet, Type.String, Type.String);
      MethodType D_SD = new MethodType(Type.NodeSet, Type.String, Type.NodeSet);
      MethodType B_BB = new MethodType(Type.Boolean, Type.Boolean, Type.Boolean);
      MethodType B_SS = new MethodType(Type.Boolean, Type.String, Type.String);
      new MethodType(Type.String, Type.String, Type.NodeSet);
      MethodType S_DSS = new MethodType(Type.String, Type.Real, Type.String, Type.String);
      MethodType S_SRR = new MethodType(Type.String, Type.String, Type.Real, Type.Real);
      MethodType S_SSS = new MethodType(Type.String, Type.String, Type.String, Type.String);
      this._symbolTable.addPrimop("current", A_V);
      this._symbolTable.addPrimop("last", I_V);
      this._symbolTable.addPrimop("position", I_V);
      this._symbolTable.addPrimop("true", B_V);
      this._symbolTable.addPrimop("false", B_V);
      this._symbolTable.addPrimop("not", B_B);
      this._symbolTable.addPrimop("name", S_V);
      this._symbolTable.addPrimop("name", S_A);
      this._symbolTable.addPrimop("generate-id", S_V);
      this._symbolTable.addPrimop("generate-id", S_A);
      this._symbolTable.addPrimop("ceiling", R_R);
      this._symbolTable.addPrimop("floor", R_R);
      this._symbolTable.addPrimop("round", R_R);
      this._symbolTable.addPrimop("contains", B_SS);
      this._symbolTable.addPrimop("number", R_O);
      this._symbolTable.addPrimop("number", R_V);
      this._symbolTable.addPrimop("boolean", B_O);
      this._symbolTable.addPrimop("string", S_O);
      this._symbolTable.addPrimop("string", S_V);
      this._symbolTable.addPrimop("translate", S_SSS);
      this._symbolTable.addPrimop("string-length", I_V);
      this._symbolTable.addPrimop("string-length", I_S);
      this._symbolTable.addPrimop("starts-with", B_SS);
      this._symbolTable.addPrimop("format-number", S_DS);
      this._symbolTable.addPrimop("format-number", S_DSS);
      this._symbolTable.addPrimop("unparsed-entity-uri", S_S);
      this._symbolTable.addPrimop("key", D_SS);
      this._symbolTable.addPrimop("key", D_SD);
      this._symbolTable.addPrimop("id", D_S);
      this._symbolTable.addPrimop("id", D_D);
      this._symbolTable.addPrimop("namespace-uri", S_V);
      this._symbolTable.addPrimop("function-available", B_S);
      this._symbolTable.addPrimop("element-available", B_S);
      this._symbolTable.addPrimop("document", D_S);
      this._symbolTable.addPrimop("document", D_V);
      this._symbolTable.addPrimop("count", I_D);
      this._symbolTable.addPrimop("sum", R_D);
      this._symbolTable.addPrimop("local-name", S_V);
      this._symbolTable.addPrimop("local-name", S_D);
      this._symbolTable.addPrimop("namespace-uri", S_V);
      this._symbolTable.addPrimop("namespace-uri", S_D);
      this._symbolTable.addPrimop("substring", S_SR);
      this._symbolTable.addPrimop("substring", S_SRR);
      this._symbolTable.addPrimop("substring-after", S_SS);
      this._symbolTable.addPrimop("substring-before", S_SS);
      this._symbolTable.addPrimop("normalize-space", S_V);
      this._symbolTable.addPrimop("normalize-space", S_S);
      this._symbolTable.addPrimop("system-property", S_S);
      this._symbolTable.addPrimop("nodeset", D_O);
      this._symbolTable.addPrimop("objectType", S_O);
      this._symbolTable.addPrimop("cast", O_SO);
      this._symbolTable.addPrimop("+", R_RR);
      this._symbolTable.addPrimop("-", R_RR);
      this._symbolTable.addPrimop("*", R_RR);
      this._symbolTable.addPrimop("/", R_RR);
      this._symbolTable.addPrimop("%", R_RR);
      this._symbolTable.addPrimop("+", I_II);
      this._symbolTable.addPrimop("-", I_II);
      this._symbolTable.addPrimop("*", I_II);
      this._symbolTable.addPrimop("<", B_RR);
      this._symbolTable.addPrimop("<=", B_RR);
      this._symbolTable.addPrimop(">", B_RR);
      this._symbolTable.addPrimop(">=", B_RR);
      this._symbolTable.addPrimop("<", B_II);
      this._symbolTable.addPrimop("<=", B_II);
      this._symbolTable.addPrimop(">", B_II);
      this._symbolTable.addPrimop(">=", B_II);
      this._symbolTable.addPrimop("<", B_BB);
      this._symbolTable.addPrimop("<=", B_BB);
      this._symbolTable.addPrimop(">", B_BB);
      this._symbolTable.addPrimop(">=", B_BB);
      this._symbolTable.addPrimop("or", B_BB);
      this._symbolTable.addPrimop("and", B_BB);
      this._symbolTable.addPrimop("u-", R_R);
      this._symbolTable.addPrimop("u-", I_I);
   }

   public SymbolTable getSymbolTable() {
      return this._symbolTable;
   }

   public Template getTemplate() {
      return this._template;
   }

   public void setTemplate(Template template) {
      this._template = template;
   }

   public int getTemplateIndex() {
      return this._templateIndex++;
   }

   public SyntaxTreeNode makeInstance(String uri, String prefix, String local, Attributes attributes) {
      SyntaxTreeNode node = null;
      QName qname = this.getQName(uri, prefix, local);
      String className = (String)this._instructionClasses.get(qname);
      ErrorMsg msg;
      ErrorMsg msg;
      if (className != null) {
         try {
            Class clazz = ObjectFactory.findProviderClass(className, ObjectFactory.findClassLoader(), true);
            node = (SyntaxTreeNode)clazz.newInstance();
            ((SyntaxTreeNode)node).setQName(qname);
            ((SyntaxTreeNode)node).setParser(this);
            if (this._locator != null) {
               ((SyntaxTreeNode)node).setLineNumber(this.getLineNumber());
            }

            if (node instanceof Stylesheet) {
               this._xsltc.setStylesheet((Stylesheet)node);
            }

            this.checkForSuperfluousAttributes((SyntaxTreeNode)node, attributes);
         } catch (ClassNotFoundException var11) {
            msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", (SyntaxTreeNode)node);
            this.reportError(3, msg);
         } catch (Exception var12) {
            msg = new ErrorMsg("INTERNAL_ERR", var12.getMessage(), (SyntaxTreeNode)node);
            this.reportError(2, msg);
         }
      } else {
         if (uri != null) {
            UnsupportedElement element;
            if (uri.equals("http://www.w3.org/1999/XSL/Transform")) {
               node = new UnsupportedElement(uri, prefix, local, false);
               element = (UnsupportedElement)node;
               msg = new ErrorMsg("UNSUPPORTED_XSL_ERR", this.getLineNumber(), local);
               element.setErrorMessage(msg);
               if (this.versionIsOne) {
                  this.reportError(1, msg);
               }
            } else if (uri.equals("http://xml.apache.org/xalan/xsltc")) {
               node = new UnsupportedElement(uri, prefix, local, true);
               element = (UnsupportedElement)node;
               msg = new ErrorMsg("UNSUPPORTED_EXT_ERR", this.getLineNumber(), local);
               element.setErrorMessage(msg);
            } else {
               Stylesheet sheet = this._xsltc.getStylesheet();
               if (sheet != null && sheet.isExtension(uri) && sheet != (SyntaxTreeNode)this._parentStack.peek()) {
                  node = new UnsupportedElement(uri, prefix, local, true);
                  UnsupportedElement elem = (UnsupportedElement)node;
                  msg = new ErrorMsg("UNSUPPORTED_EXT_ERR", this.getLineNumber(), prefix + ":" + local);
                  elem.setErrorMessage(msg);
               }
            }
         }

         if (node == null) {
            node = new LiteralElement();
            ((SyntaxTreeNode)node).setLineNumber(this.getLineNumber());
         }
      }

      if (node != null && node instanceof LiteralElement) {
         ((LiteralElement)node).setQName(qname);
      }

      return (SyntaxTreeNode)node;
   }

   private void checkForSuperfluousAttributes(SyntaxTreeNode node, Attributes attrs) {
      QName qname = node.getQName();
      boolean isStylesheet = node instanceof Stylesheet;
      String[] legal = (String[])this._instructionAttrs.get(qname);
      if (this.versionIsOne && legal != null) {
         int n = attrs.getLength();

         for(int i = 0; i < n; ++i) {
            String attrQName = attrs.getQName(i);
            if (isStylesheet && attrQName.equals("version")) {
               this.versionIsOne = attrs.getValue(i).equals("1.0");
            }

            if (!attrQName.startsWith("xml") && attrQName.indexOf(58) <= 0) {
               int j;
               for(j = 0; j < legal.length && !attrQName.equalsIgnoreCase(legal[j]); ++j) {
               }

               if (j == legal.length) {
                  ErrorMsg err = new ErrorMsg("ILLEGAL_ATTRIBUTE_ERR", attrQName, node);
                  err.setWarningError(true);
                  this.reportError(4, err);
               }
            }
         }
      }

   }

   public Expression parseExpression(SyntaxTreeNode parent, String exp) {
      return (Expression)this.parseTopLevel(parent, "<EXPRESSION>" + exp, (String)null);
   }

   public Expression parseExpression(SyntaxTreeNode parent, String attr, String def) {
      String exp = parent.getAttribute(attr);
      if (exp.length() == 0 && def != null) {
         exp = def;
      }

      return (Expression)this.parseTopLevel(parent, "<EXPRESSION>" + exp, exp);
   }

   public Pattern parsePattern(SyntaxTreeNode parent, String pattern) {
      return (Pattern)this.parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
   }

   public Pattern parsePattern(SyntaxTreeNode parent, String attr, String def) {
      String pattern = parent.getAttribute(attr);
      if (pattern.length() == 0 && def != null) {
         pattern = def;
      }

      return (Pattern)this.parseTopLevel(parent, "<PATTERN>" + pattern, pattern);
   }

   private SyntaxTreeNode parseTopLevel(SyntaxTreeNode parent, String text, String expression) {
      int line = this.getLineNumber();

      try {
         this._xpathParser.setScanner(new XPathLexer(new StringReader(text)));
         Symbol result = this._xpathParser.parse(expression, line);
         if (result != null) {
            SyntaxTreeNode node = (SyntaxTreeNode)result.value;
            if (node != null) {
               node.setParser(this);
               node.setParent(parent);
               node.setLineNumber(line);
               return node;
            }
         }

         this.reportError(3, new ErrorMsg("XPATH_PARSER_ERR", expression, parent));
      } catch (Exception var7) {
         if (this._xsltc.debug()) {
            var7.printStackTrace();
         }

         this.reportError(3, new ErrorMsg("XPATH_PARSER_ERR", expression, parent));
      }

      SyntaxTreeNode.Dummy.setParser(this);
      return SyntaxTreeNode.Dummy;
   }

   public boolean errorsFound() {
      return this._errors.size() > 0;
   }

   public void printErrors() {
      int size = this._errors.size();
      if (size > 0) {
         System.err.println(new ErrorMsg("COMPILER_ERROR_KEY"));

         for(int i = 0; i < size; ++i) {
            System.err.println("  " + this._errors.elementAt(i));
         }
      }

   }

   public void printWarnings() {
      int size = this._warnings.size();
      if (size > 0) {
         System.err.println(new ErrorMsg("COMPILER_WARNING_KEY"));

         for(int i = 0; i < size; ++i) {
            System.err.println("  " + this._warnings.elementAt(i));
         }
      }

   }

   public void reportError(int category, ErrorMsg error) {
      switch (category) {
         case 0:
            this._errors.addElement(error);
            break;
         case 1:
            this._errors.addElement(error);
            break;
         case 2:
            this._errors.addElement(error);
            break;
         case 3:
            this._errors.addElement(error);
            break;
         case 4:
            this._warnings.addElement(error);
      }

   }

   public Vector getErrors() {
      return this._errors;
   }

   public Vector getWarnings() {
      return this._warnings;
   }

   public void startDocument() {
      this._root = null;
      this._target = null;
      this._prefixMapping = null;
      this._parentStack = new Stack();
   }

   public void endDocument() {
   }

   public void startPrefixMapping(String prefix, String uri) {
      if (this._prefixMapping == null) {
         this._prefixMapping = new Hashtable();
      }

      this._prefixMapping.put(prefix, uri);
   }

   public void endPrefixMapping(String prefix) {
   }

   public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException {
      int col = qname.lastIndexOf(58);
      String prefix = col == -1 ? null : qname.substring(0, col);
      SyntaxTreeNode element = this.makeInstance(uri, prefix, localname, attributes);
      if (element == null) {
         ErrorMsg err = new ErrorMsg("ELEMENT_PARSE_ERR", prefix + ':' + localname);
         throw new SAXException(err.toString());
      } else {
         if (this._root == null) {
            if (this._prefixMapping != null && this._prefixMapping.containsValue("http://www.w3.org/1999/XSL/Transform")) {
               this._rootNamespaceDef = true;
            } else {
               this._rootNamespaceDef = false;
            }

            this._root = element;
         } else {
            SyntaxTreeNode parent = (SyntaxTreeNode)this._parentStack.peek();
            parent.addElement(element);
            element.setParent(parent);
         }

         element.setAttributes(new AttributeList(attributes));
         element.setPrefixMapping(this._prefixMapping);
         if (element instanceof Stylesheet) {
            this.getSymbolTable().setCurrentNode(element);
            ((Stylesheet)element).excludeExtensionPrefixes(this);
         }

         this._prefixMapping = null;
         this._parentStack.push(element);
      }
   }

   public void endElement(String uri, String localname, String qname) {
      this._parentStack.pop();
   }

   public void characters(char[] ch, int start, int length) {
      String string = new String(ch, start, length);
      SyntaxTreeNode parent = (SyntaxTreeNode)this._parentStack.peek();
      if (string.length() != 0) {
         if (parent instanceof Text) {
            ((Text)parent).setText(string);
         } else if (!(parent instanceof Stylesheet)) {
            SyntaxTreeNode bro = parent.lastChild();
            if (bro != null && bro instanceof Text) {
               Text text = (Text)bro;
               if (!text.isTextElement() && (length > 1 || ch[0] < 256)) {
                  text.setText(string);
                  return;
               }
            }

            parent.addElement(new Text(string));
         }
      }
   }

   private String getTokenValue(String token) {
      int start = token.indexOf(34);
      int stop = token.lastIndexOf(34);
      return token.substring(start + 1, stop);
   }

   public void processingInstruction(String name, String value) {
      if (this._target == null && name.equals("xml-stylesheet")) {
         String href = null;
         String media = null;
         String title = null;
         String charset = null;
         StringTokenizer tokens = new StringTokenizer(value);

         while(tokens.hasMoreElements()) {
            String token = (String)tokens.nextElement();
            if (token.startsWith("href")) {
               href = this.getTokenValue(token);
            } else if (token.startsWith("media")) {
               media = this.getTokenValue(token);
            } else if (token.startsWith("title")) {
               title = this.getTokenValue(token);
            } else if (token.startsWith("charset")) {
               charset = this.getTokenValue(token);
            }
         }

         if ((this._PImedia == null || this._PImedia.equals(media)) && (this._PItitle == null || this._PImedia.equals(title)) && (this._PIcharset == null || this._PImedia.equals(charset))) {
            this._target = href;
         }
      }

   }

   public void ignorableWhitespace(char[] ch, int start, int length) {
   }

   public void skippedEntity(String name) {
   }

   public void setDocumentLocator(Locator locator) {
      this._locator = locator;
   }

   private int getLineNumber() {
      int line = 0;
      if (this._locator != null) {
         line = this._locator.getLineNumber();
      }

      return line;
   }
}
