package org.apache.xalan.xsltc.runtime;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public abstract class AbstractTranslet implements Translet {
   public String _version = "1.0";
   public String _method = null;
   public String _encoding = "UTF-8";
   public boolean _omitHeader = false;
   public String _standalone = null;
   public String _doctypePublic = null;
   public String _doctypeSystem = null;
   public boolean _indent = false;
   public String _mediaType = null;
   public Vector _cdata = null;
   public int _indentamount = -1;
   public static final int FIRST_TRANSLET_VERSION = 100;
   public static final int VER_SPLIT_NAMES_ARRAY = 101;
   public static final int CURRENT_TRANSLET_VERSION = 101;
   protected int transletVersion = 100;
   protected String[] namesArray;
   protected String[] urisArray;
   protected int[] typesArray;
   protected String[] namespaceArray;
   protected Templates _templates = null;
   protected boolean _hasIdCall = false;
   protected StringValueHandler stringValueHandler = new StringValueHandler();
   private static final String EMPTYSTRING = "";
   private static final String ID_INDEX_NAME = "##id";
   protected int pbase = 0;
   protected int pframe = 0;
   protected ArrayList paramsStack = new ArrayList();
   private MessageHandler _msgHandler = null;
   public Hashtable _formatSymbols = null;
   private Hashtable _keyIndexes = null;
   private KeyIndex _emptyKeyIndex = null;
   private int _indexSize = 0;
   private DOMCache _domCache = null;
   private Hashtable _auxClasses = null;
   protected DOMImplementation _domImplementation = null;

   public void printInternalState() {
      System.out.println("-------------------------------------");
      System.out.println("AbstractTranslet this = " + this);
      System.out.println("pbase = " + this.pbase);
      System.out.println("vframe = " + this.pframe);
      System.out.println("paramsStack.size() = " + this.paramsStack.size());
      System.out.println("namesArray.size = " + this.namesArray.length);
      System.out.println("namespaceArray.size = " + this.namespaceArray.length);
      System.out.println("");
      System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
   }

   public final DOMAdapter makeDOMAdapter(DOM dom) throws TransletException {
      return new DOMAdapter(dom, this.namesArray, this.urisArray, this.typesArray, this.namespaceArray);
   }

   public final void pushParamFrame() {
      this.paramsStack.add(this.pframe, new Integer(this.pbase));
      this.pbase = ++this.pframe;
   }

   public final void popParamFrame() {
      if (this.pbase > 0) {
         int oldpbase = (Integer)this.paramsStack.get(--this.pbase);

         for(int i = this.pframe - 1; i >= this.pbase; --i) {
            this.paramsStack.remove(i);
         }

         this.pframe = this.pbase;
         this.pbase = oldpbase;
      }

   }

   public final Object addParameter(String name, Object value) {
      name = BasisLibrary.mapQNameToJavaName(name);
      return this.addParameter(name, value, false);
   }

   public final Object addParameter(String name, Object value, boolean isDefault) {
      for(int i = this.pframe - 1; i >= this.pbase; --i) {
         Parameter param = (Parameter)this.paramsStack.get(i);
         if (param._name.equals(name)) {
            if (!param._isDefault && isDefault) {
               return param._value;
            }

            param._value = value;
            param._isDefault = isDefault;
            return value;
         }
      }

      this.paramsStack.add(this.pframe++, new Parameter(name, value, isDefault));
      return value;
   }

   public void clearParameters() {
      this.pbase = this.pframe = 0;
      this.paramsStack.clear();
   }

   public final Object getParameter(String name) {
      name = BasisLibrary.mapQNameToJavaName(name);

      for(int i = this.pframe - 1; i >= this.pbase; --i) {
         Parameter param = (Parameter)this.paramsStack.get(i);
         if (param._name.equals(name)) {
            return param._value;
         }
      }

      return null;
   }

   public final void setMessageHandler(MessageHandler handler) {
      this._msgHandler = handler;
   }

   public final void displayMessage(String msg) {
      if (this._msgHandler == null) {
         System.err.println(msg);
      } else {
         this._msgHandler.displayMessage(msg);
      }

   }

   public void addDecimalFormat(String name, DecimalFormatSymbols symbols) {
      if (this._formatSymbols == null) {
         this._formatSymbols = new Hashtable();
      }

      if (name == null) {
         name = "";
      }

      DecimalFormat df = new DecimalFormat();
      if (symbols != null) {
         df.setDecimalFormatSymbols(symbols);
      }

      this._formatSymbols.put(name, df);
   }

   public final DecimalFormat getDecimalFormat(String name) {
      if (this._formatSymbols != null) {
         if (name == null) {
            name = "";
         }

         DecimalFormat df = (DecimalFormat)this._formatSymbols.get(name);
         if (df == null) {
            df = (DecimalFormat)this._formatSymbols.get("");
         }

         return df;
      } else {
         return null;
      }
   }

   public final void prepassDocument(DOM document) {
      this.setIndexSize(document.getSize());
      this.buildIDIndex(document);
   }

   private final void buildIDIndex(DOM document) {
      if (document instanceof DOMEnhancedForDTM) {
         DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM)document;
         if (enhancedDOM.hasDOMSource()) {
            this.buildKeyIndex("##id", document);
            return;
         }

         Hashtable elementsByID = enhancedDOM.getElementsWithIDs();
         if (elementsByID == null) {
            return;
         }

         Enumeration idValues = elementsByID.keys();

         boolean hasIDValues;
         for(hasIDValues = false; idValues.hasMoreElements(); hasIDValues = true) {
            Object idValue = idValues.nextElement();
            int element = (Integer)elementsByID.get(idValue);
            this.buildKeyIndex("##id", element, idValue);
         }

         if (hasIDValues) {
            this.setKeyIndexDom("##id", document);
         }
      }

   }

   public final void postInitialization() {
      if (this.transletVersion < 101) {
         int arraySize = this.namesArray.length;
         String[] newURIsArray = new String[arraySize];
         String[] newNamesArray = new String[arraySize];
         int[] newTypesArray = new int[arraySize];

         for(int i = 0; i < arraySize; ++i) {
            String name = this.namesArray[i];
            int colonIndex = name.lastIndexOf(58);
            int lNameStartIdx = colonIndex + 1;
            if (colonIndex > -1) {
               newURIsArray[i] = name.substring(0, colonIndex);
            }

            if (name.charAt(lNameStartIdx) == '@') {
               ++lNameStartIdx;
               newTypesArray[i] = 2;
            } else if (name.charAt(lNameStartIdx) == '?') {
               ++lNameStartIdx;
               newTypesArray[i] = 13;
            } else {
               newTypesArray[i] = 1;
            }

            newNamesArray[i] = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
         }

         this.namesArray = newNamesArray;
         this.urisArray = newURIsArray;
         this.typesArray = newTypesArray;
      }

      if (this.transletVersion > 101) {
         BasisLibrary.runTimeError("UNKNOWN_TRANSLET_VERSION_ERR", (Object)this.getClass().getName());
      }

   }

   public void setIndexSize(int size) {
      if (size > this._indexSize) {
         this._indexSize = size;
      }

   }

   public KeyIndex createKeyIndex() {
      return new KeyIndex(this._indexSize);
   }

   public void buildKeyIndex(String name, int node, Object value) {
      if (this._keyIndexes == null) {
         this._keyIndexes = new Hashtable();
      }

      KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
      if (index == null) {
         this._keyIndexes.put(name, index = new KeyIndex(this._indexSize));
      }

      index.add(value, node);
   }

   public void buildKeyIndex(String name, DOM dom) {
      if (this._keyIndexes == null) {
         this._keyIndexes = new Hashtable();
      }

      KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
      if (index == null) {
         this._keyIndexes.put(name, index = new KeyIndex(this._indexSize));
      }

      index.setDom(dom);
   }

   public KeyIndex getKeyIndex(String name) {
      if (this._keyIndexes == null) {
         return this._emptyKeyIndex != null ? this._emptyKeyIndex : (this._emptyKeyIndex = new KeyIndex(1));
      } else {
         KeyIndex index = (KeyIndex)this._keyIndexes.get(name);
         if (index == null) {
            return this._emptyKeyIndex != null ? this._emptyKeyIndex : (this._emptyKeyIndex = new KeyIndex(1));
         } else {
            return index;
         }
      }
   }

   public void buildKeys(DOM document, DTMAxisIterator iterator, SerializationHandler handler, int root) throws TransletException {
   }

   public void setKeyIndexDom(String name, DOM document) {
      this.getKeyIndex(name).setDom(document);
   }

   public void setDOMCache(DOMCache cache) {
      this._domCache = cache;
   }

   public DOMCache getDOMCache() {
      return this._domCache;
   }

   public SerializationHandler openOutputHandler(String filename, boolean append) throws TransletException {
      try {
         TransletOutputHandlerFactory factory = TransletOutputHandlerFactory.newInstance();
         String dirStr = (new File(filename)).getParent();
         if (null != dirStr && dirStr.length() > 0) {
            File dir = new File(dirStr);
            dir.mkdirs();
         }

         factory.setEncoding(this._encoding);
         factory.setOutputMethod(this._method);
         factory.setWriter(new FileWriter(filename, append));
         factory.setOutputType(0);
         SerializationHandler handler = factory.getSerializationHandler();
         this.transferOutputSettings(handler);
         handler.startDocument();
         return handler;
      } catch (Exception var6) {
         throw new TransletException(var6);
      }
   }

   public SerializationHandler openOutputHandler(String filename) throws TransletException {
      return this.openOutputHandler(filename, false);
   }

   public void closeOutputHandler(SerializationHandler handler) {
      try {
         handler.endDocument();
         handler.close();
      } catch (Exception var3) {
      }

   }

   public abstract void transform(DOM var1, DTMAxisIterator var2, SerializationHandler var3) throws TransletException;

   public final void transform(DOM document, SerializationHandler handler) throws TransletException {
      try {
         this.transform(document, document.getIterator(), handler);
      } finally {
         this._keyIndexes = null;
      }

   }

   public final void characters(String string, SerializationHandler handler) throws TransletException {
      if (string != null) {
         try {
            handler.characters(string);
         } catch (Exception var4) {
            throw new TransletException(var4);
         }
      }

   }

   public void addCdataElement(String name) {
      if (this._cdata == null) {
         this._cdata = new Vector();
      }

      int lastColon = name.lastIndexOf(58);
      if (lastColon > 0) {
         String uri = name.substring(0, lastColon);
         String localName = name.substring(lastColon + 1);
         this._cdata.addElement(uri);
         this._cdata.addElement(localName);
      } else {
         this._cdata.addElement((Object)null);
         this._cdata.addElement(name);
      }

   }

   protected void transferOutputSettings(SerializationHandler handler) {
      if (this._method != null) {
         if (this._method.equals("xml")) {
            if (this._standalone != null) {
               handler.setStandalone(this._standalone);
            }

            if (this._omitHeader) {
               handler.setOmitXMLDeclaration(true);
            }

            handler.setCdataSectionElements(this._cdata);
            if (this._version != null) {
               handler.setVersion(this._version);
            }

            handler.setIndent(this._indent);
            handler.setIndentAmount(this._indentamount);
            if (this._doctypeSystem != null) {
               handler.setDoctype(this._doctypeSystem, this._doctypePublic);
            }
         } else if (this._method.equals("html")) {
            handler.setIndent(this._indent);
            handler.setDoctype(this._doctypeSystem, this._doctypePublic);
            if (this._mediaType != null) {
               handler.setMediaType(this._mediaType);
            }
         }
      } else {
         handler.setCdataSectionElements(this._cdata);
         if (this._version != null) {
            handler.setVersion(this._version);
         }

         if (this._standalone != null) {
            handler.setStandalone(this._standalone);
         }

         if (this._omitHeader) {
            handler.setOmitXMLDeclaration(true);
         }

         handler.setIndent(this._indent);
         handler.setDoctype(this._doctypeSystem, this._doctypePublic);
      }

   }

   public void addAuxiliaryClass(Class auxClass) {
      if (this._auxClasses == null) {
         this._auxClasses = new Hashtable();
      }

      this._auxClasses.put(auxClass.getName(), auxClass);
   }

   public void setAuxiliaryClasses(Hashtable auxClasses) {
      this._auxClasses = auxClasses;
   }

   public Class getAuxiliaryClass(String className) {
      return this._auxClasses == null ? null : (Class)this._auxClasses.get(className);
   }

   public String[] getNamesArray() {
      return this.namesArray;
   }

   public String[] getUrisArray() {
      return this.urisArray;
   }

   public int[] getTypesArray() {
      return this.typesArray;
   }

   public String[] getNamespaceArray() {
      return this.namespaceArray;
   }

   public boolean hasIdCall() {
      return this._hasIdCall;
   }

   public Templates getTemplates() {
      return this._templates;
   }

   public void setTemplates(Templates templates) {
      this._templates = templates;
   }

   public Document newDocument(String uri, String qname) throws ParserConfigurationException {
      if (this._domImplementation == null) {
         this._domImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
      }

      return this._domImplementation.createDocument(uri, qname, (DocumentType)null);
   }

   public abstract void transform(DOM var1, SerializationHandler[] var2) throws TransletException;
}
