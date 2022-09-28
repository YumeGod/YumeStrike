package org.apache.xalan.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVT;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringToIntTable;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.XML11Char;
import org.apache.xpath.XPath;
import org.xml.sax.SAXException;

public class XSLTAttributeDef {
   static final int FATAL = 0;
   static final int ERROR = 1;
   static final int WARNING = 2;
   static final int T_CDATA = 1;
   static final int T_URL = 2;
   static final int T_AVT = 3;
   static final int T_PATTERN = 4;
   static final int T_EXPR = 5;
   static final int T_CHAR = 6;
   static final int T_NUMBER = 7;
   static final int T_YESNO = 8;
   static final int T_QNAME = 9;
   static final int T_QNAMES = 10;
   static final int T_ENUM = 11;
   static final int T_SIMPLEPATTERNLIST = 12;
   static final int T_NMTOKEN = 13;
   static final int T_STRINGLIST = 14;
   static final int T_PREFIX_URLLIST = 15;
   static final int T_ENUM_OR_PQNAME = 16;
   static final int T_NCNAME = 17;
   static final int T_AVT_QNAME = 18;
   static final int T_QNAMES_RESOLVE_NULL = 19;
   static final int T_PREFIXLIST = 20;
   static final XSLTAttributeDef m_foreignAttr = new XSLTAttributeDef("*", "*", 1, false, false, 2);
   static final String S_FOREIGNATTR_SETTER = "setForeignAttr";
   private String m_namespace;
   private String m_name;
   private int m_type;
   private StringToIntTable m_enums;
   private String m_default;
   private boolean m_required;
   private boolean m_supportsAVT;
   int m_errorType = 2;
   String m_setterString = null;
   // $FF: synthetic field
   static Class class$org$apache$xpath$XPath;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Character;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;

   XSLTAttributeDef(String namespace, String name, int type, boolean required, boolean supportsAVT, int errorType) {
      this.m_namespace = namespace;
      this.m_name = name;
      this.m_type = type;
      this.m_required = required;
      this.m_supportsAVT = supportsAVT;
      this.m_errorType = errorType;
   }

   XSLTAttributeDef(String namespace, String name, int type, boolean supportsAVT, int errorType, String defaultVal) {
      this.m_namespace = namespace;
      this.m_name = name;
      this.m_type = type;
      this.m_required = false;
      this.m_supportsAVT = supportsAVT;
      this.m_errorType = errorType;
      this.m_default = defaultVal;
   }

   XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2) {
      this.m_namespace = namespace;
      this.m_name = name;
      this.m_type = prefixedQNameValAllowed ? 16 : 11;
      this.m_required = required;
      this.m_supportsAVT = supportsAVT;
      this.m_errorType = errorType;
      this.m_enums = new StringToIntTable(2);
      this.m_enums.put(k1, v1);
      this.m_enums.put(k2, v2);
   }

   XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2, String k3, int v3) {
      this.m_namespace = namespace;
      this.m_name = name;
      this.m_type = prefixedQNameValAllowed ? 16 : 11;
      this.m_required = required;
      this.m_supportsAVT = supportsAVT;
      this.m_errorType = errorType;
      this.m_enums = new StringToIntTable(3);
      this.m_enums.put(k1, v1);
      this.m_enums.put(k2, v2);
      this.m_enums.put(k3, v3);
   }

   XSLTAttributeDef(String namespace, String name, boolean required, boolean supportsAVT, boolean prefixedQNameValAllowed, int errorType, String k1, int v1, String k2, int v2, String k3, int v3, String k4, int v4) {
      this.m_namespace = namespace;
      this.m_name = name;
      this.m_type = prefixedQNameValAllowed ? 16 : 11;
      this.m_required = required;
      this.m_supportsAVT = supportsAVT;
      this.m_errorType = errorType;
      this.m_enums = new StringToIntTable(4);
      this.m_enums.put(k1, v1);
      this.m_enums.put(k2, v2);
      this.m_enums.put(k3, v3);
      this.m_enums.put(k4, v4);
   }

   String getNamespace() {
      return this.m_namespace;
   }

   String getName() {
      return this.m_name;
   }

   int getType() {
      return this.m_type;
   }

   private int getEnum(String key) {
      return this.m_enums.get(key);
   }

   private String[] getEnumNames() {
      return this.m_enums.keys();
   }

   String getDefault() {
      return this.m_default;
   }

   void setDefault(String def) {
      this.m_default = def;
   }

   boolean getRequired() {
      return this.m_required;
   }

   boolean getSupportsAVT() {
      return this.m_supportsAVT;
   }

   int getErrorType() {
      return this.m_errorType;
   }

   public String getSetterMethodName() {
      if (null == this.m_setterString) {
         if (m_foreignAttr == this) {
            return "setForeignAttr";
         }

         if (this.m_name.equals("*")) {
            this.m_setterString = "addLiteralResultAttribute";
            return this.m_setterString;
         }

         StringBuffer outBuf = new StringBuffer();
         outBuf.append("set");
         if (this.m_namespace != null && this.m_namespace.equals("http://www.w3.org/XML/1998/namespace")) {
            outBuf.append("Xml");
         }

         int n = this.m_name.length();

         for(int i = 0; i < n; ++i) {
            char c = this.m_name.charAt(i);
            if ('-' == c) {
               ++i;
               c = this.m_name.charAt(i);
               c = Character.toUpperCase(c);
            } else if (0 == i) {
               c = Character.toUpperCase(c);
            }

            outBuf.append(c);
         }

         this.m_setterString = outBuf.toString();
      }

      return this.m_setterString;
   }

   AVT processAVT(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      try {
         AVT avt = new AVT(handler, uri, name, rawName, value, owner);
         return avt;
      } catch (TransformerException var8) {
         throw new SAXException(var8);
      }
   }

   Object processCDATA(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         try {
            AVT avt = new AVT(handler, uri, name, rawName, value, owner);
            return avt;
         } catch (TransformerException var8) {
            throw new SAXException(var8);
         }
      } else {
         return value;
      }
   }

   Object processCHAR(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         try {
            AVT avt = new AVT(handler, uri, name, rawName, value, owner);
            if (avt.isSimple() && value.length() != 1) {
               this.handleError(handler, "INVALID_TCHAR", new Object[]{name, value}, (Exception)null);
               return null;
            } else {
               return avt;
            }
         } catch (TransformerException var8) {
            throw new SAXException(var8);
         }
      } else if (value.length() != 1) {
         this.handleError(handler, "INVALID_TCHAR", new Object[]{name, value}, (Exception)null);
         return null;
      } else {
         return new Character(value.charAt(0));
      }
   }

   Object processENUM(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      AVT avt = null;
      if (this.getSupportsAVT()) {
         try {
            avt = new AVT(handler, uri, name, rawName, value, owner);
            if (!avt.isSimple()) {
               return avt;
            }
         } catch (TransformerException var10) {
            throw new SAXException(var10);
         }
      }

      int retVal = this.getEnum(value);
      if (retVal == -10000) {
         StringBuffer enumNamesList = this.getListOfEnums();
         this.handleError(handler, "INVALID_ENUM", new Object[]{name, value, enumNamesList.toString()}, (Exception)null);
         return null;
      } else {
         return this.getSupportsAVT() ? avt : new Integer(retVal);
      }
   }

   Object processENUM_OR_PQNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      Object objToReturn = null;
      if (this.getSupportsAVT()) {
         try {
            AVT avt = new AVT(handler, uri, name, rawName, value, owner);
            if (!avt.isSimple()) {
               return avt;
            }

            objToReturn = avt;
         } catch (TransformerException var14) {
            throw new SAXException(var14);
         }
      }

      int key = this.getEnum(value);
      if (key != -10000) {
         if (objToReturn == null) {
            objToReturn = new Integer(key);
         }
      } else {
         StringBuffer enumNamesList;
         try {
            QName qname = new QName(value, handler, true);
            if (objToReturn == null) {
               objToReturn = qname;
            }

            if (qname.getPrefix() == null) {
               enumNamesList = this.getListOfEnums();
               enumNamesList.append(" <qname-but-not-ncname>");
               this.handleError(handler, "INVALID_ENUM", new Object[]{name, value, enumNamesList.toString()}, (Exception)null);
               return null;
            }
         } catch (IllegalArgumentException var12) {
            enumNamesList = this.getListOfEnums();
            enumNamesList.append(" <qname-but-not-ncname>");
            this.handleError(handler, "INVALID_ENUM", new Object[]{name, value, enumNamesList.toString()}, var12);
            return null;
         } catch (RuntimeException var13) {
            StringBuffer enumNamesList = this.getListOfEnums();
            enumNamesList.append(" <qname-but-not-ncname>");
            this.handleError(handler, "INVALID_ENUM", new Object[]{name, value, enumNamesList.toString()}, var13);
            return null;
         }
      }

      return objToReturn;
   }

   Object processEXPR(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      try {
         XPath expr = handler.createXPath(value, owner);
         return expr;
      } catch (TransformerException var9) {
         new SAXException(var9);
         throw new SAXException(var9);
      }
   }

   Object processNMTOKEN(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         try {
            AVT avt = new AVT(handler, uri, name, rawName, value, owner);
            if (avt.isSimple() && !XML11Char.isXML11ValidNmtoken(value)) {
               this.handleError(handler, "INVALID_NMTOKEN", new Object[]{name, value}, (Exception)null);
               return null;
            } else {
               return avt;
            }
         } catch (TransformerException var8) {
            throw new SAXException(var8);
         }
      } else if (!XML11Char.isXML11ValidNmtoken(value)) {
         this.handleError(handler, "INVALID_NMTOKEN", new Object[]{name, value}, (Exception)null);
         return null;
      } else {
         return value;
      }
   }

   Object processPATTERN(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      try {
         XPath pattern = handler.createMatchPatternXPath(value, owner);
         return pattern;
      } catch (TransformerException var8) {
         throw new SAXException(var8);
      }
   }

   Object processNUMBER(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         AVT avt = null;

         try {
            avt = new AVT(handler, uri, name, rawName, value, owner);
            if (avt.isSimple()) {
               Double val = Double.valueOf(value);
            }

            return avt;
         } catch (TransformerException var11) {
            throw new SAXException(var11);
         } catch (NumberFormatException var12) {
            this.handleError(handler, "INVALID_NUMBER", new Object[]{name, value}, var12);
            return null;
         }
      } else {
         try {
            return Double.valueOf(value);
         } catch (NumberFormatException var13) {
            this.handleError(handler, "INVALID_NUMBER", new Object[]{name, value}, var13);
            return null;
         }
      }
   }

   Object processQNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      try {
         QName qname = new QName(value, handler, true);
         return qname;
      } catch (IllegalArgumentException var9) {
         this.handleError(handler, "INVALID_QNAME", new Object[]{name, value}, var9);
         return null;
      } catch (RuntimeException var10) {
         this.handleError(handler, "INVALID_QNAME", new Object[]{name, value}, var10);
         return null;
      }
   }

   Object processAVT_QNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      AVT avt = null;

      try {
         avt = new AVT(handler, uri, name, rawName, value, owner);
         if (avt.isSimple()) {
            int indexOfNSSep = value.indexOf(58);
            String localName;
            if (indexOfNSSep >= 0) {
               localName = value.substring(0, indexOfNSSep);
               if (!XML11Char.isXML11ValidNCName(localName)) {
                  this.handleError(handler, "INVALID_QNAME", new Object[]{name, value}, (Exception)null);
                  return null;
               }
            }

            localName = indexOfNSSep < 0 ? value : value.substring(indexOfNSSep + 1);
            if (localName == null || localName.length() == 0 || !XML11Char.isXML11ValidNCName(localName)) {
               this.handleError(handler, "INVALID_QNAME", new Object[]{name, value}, (Exception)null);
               return null;
            }
         }

         return avt;
      } catch (TransformerException var10) {
         throw new SAXException(var10);
      }
   }

   Object processNCNAME(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         AVT avt = null;

         try {
            avt = new AVT(handler, uri, name, rawName, value, owner);
            if (avt.isSimple() && !XML11Char.isXML11ValidNCName(value)) {
               this.handleError(handler, "INVALID_NCNAME", new Object[]{name, value}, (Exception)null);
               return null;
            } else {
               return avt;
            }
         } catch (TransformerException var9) {
            throw new SAXException(var9);
         }
      } else if (!XML11Char.isXML11ValidNCName(value)) {
         this.handleError(handler, "INVALID_NCNAME", new Object[]{name, value}, (Exception)null);
         return null;
      } else {
         return value;
      }
   }

   Vector processQNAMES(StylesheetHandler handler, String uri, String name, String rawName, String value) throws SAXException {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nQNames = tokenizer.countTokens();
      Vector qnames = new Vector(nQNames);

      for(int i = 0; i < nQNames; ++i) {
         qnames.addElement(new QName(tokenizer.nextToken(), handler));
      }

      return qnames;
   }

   final Vector processQNAMESRNU(StylesheetHandler handler, String uri, String name, String rawName, String value) throws SAXException {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nQNames = tokenizer.countTokens();
      Vector qnames = new Vector(nQNames);
      String defaultURI = handler.getNamespaceForPrefix("");

      for(int i = 0; i < nQNames; ++i) {
         String tok = tokenizer.nextToken();
         if (tok.indexOf(58) == -1) {
            qnames.addElement(new QName(defaultURI, tok));
         } else {
            qnames.addElement(new QName(tok, handler));
         }
      }

      return qnames;
   }

   Vector processSIMPLEPATTERNLIST(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      try {
         StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
         int nPatterns = tokenizer.countTokens();
         Vector patterns = new Vector(nPatterns);

         for(int i = 0; i < nPatterns; ++i) {
            XPath pattern = handler.createMatchPatternXPath(tokenizer.nextToken(), owner);
            patterns.addElement(pattern);
         }

         return patterns;
      } catch (TransformerException var12) {
         throw new SAXException(var12);
      }
   }

   StringVector processSTRINGLIST(StylesheetHandler handler, String uri, String name, String rawName, String value) {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nStrings = tokenizer.countTokens();
      StringVector strings = new StringVector(nStrings);

      for(int i = 0; i < nStrings; ++i) {
         strings.addElement(tokenizer.nextToken());
      }

      return strings;
   }

   StringVector processPREFIX_URLLIST(StylesheetHandler handler, String uri, String name, String rawName, String value) throws SAXException {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nStrings = tokenizer.countTokens();
      StringVector strings = new StringVector(nStrings);

      for(int i = 0; i < nStrings; ++i) {
         String prefix = tokenizer.nextToken();
         String url = handler.getNamespaceForPrefix(prefix);
         if (url == null) {
            throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[]{prefix}));
         }

         strings.addElement(url);
      }

      return strings;
   }

   StringVector processPREFIX_LIST(StylesheetHandler handler, String uri, String name, String rawName, String value) throws SAXException {
      StringTokenizer tokenizer = new StringTokenizer(value, " \t\n\r\f");
      int nStrings = tokenizer.countTokens();
      StringVector strings = new StringVector(nStrings);

      for(int i = 0; i < nStrings; ++i) {
         String prefix = tokenizer.nextToken();
         String url = handler.getNamespaceForPrefix(prefix);
         if (!prefix.equals("#default") && url == null) {
            throw new SAXException(XSLMessages.createMessage("ER_CANT_RESOLVE_NSPREFIX", new Object[]{prefix}));
         }

         strings.addElement(prefix);
      }

      return strings;
   }

   Object processURL(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      if (this.getSupportsAVT()) {
         try {
            AVT avt = new AVT(handler, uri, name, rawName, value, owner);
            return avt;
         } catch (TransformerException var8) {
            throw new SAXException(var8);
         }
      } else {
         return value;
      }
   }

   private Boolean processYESNO(StylesheetHandler handler, String uri, String name, String rawName, String value) throws SAXException {
      if (!value.equals("yes") && !value.equals("no")) {
         this.handleError(handler, "INVALID_BOOLEAN", new Object[]{name, value}, (Exception)null);
         return null;
      } else {
         return new Boolean(value.equals("yes"));
      }
   }

   Object processValue(StylesheetHandler handler, String uri, String name, String rawName, String value, ElemTemplateElement owner) throws SAXException {
      int type = this.getType();
      Object processedValue = null;
      switch (type) {
         case 1:
            processedValue = this.processCDATA(handler, uri, name, rawName, value, owner);
            break;
         case 2:
            processedValue = this.processURL(handler, uri, name, rawName, value, owner);
            break;
         case 3:
            processedValue = this.processAVT(handler, uri, name, rawName, value, owner);
            break;
         case 4:
            processedValue = this.processPATTERN(handler, uri, name, rawName, value, owner);
            break;
         case 5:
            processedValue = this.processEXPR(handler, uri, name, rawName, value, owner);
            break;
         case 6:
            processedValue = this.processCHAR(handler, uri, name, rawName, value, owner);
            break;
         case 7:
            processedValue = this.processNUMBER(handler, uri, name, rawName, value, owner);
            break;
         case 8:
            processedValue = this.processYESNO(handler, uri, name, rawName, value);
            break;
         case 9:
            processedValue = this.processQNAME(handler, uri, name, rawName, value, owner);
            break;
         case 10:
            processedValue = this.processQNAMES(handler, uri, name, rawName, value);
            break;
         case 11:
            processedValue = this.processENUM(handler, uri, name, rawName, value, owner);
            break;
         case 12:
            processedValue = this.processSIMPLEPATTERNLIST(handler, uri, name, rawName, value, owner);
            break;
         case 13:
            processedValue = this.processNMTOKEN(handler, uri, name, rawName, value, owner);
            break;
         case 14:
            processedValue = this.processSTRINGLIST(handler, uri, name, rawName, value);
            break;
         case 15:
            processedValue = this.processPREFIX_URLLIST(handler, uri, name, rawName, value);
            break;
         case 16:
            processedValue = this.processENUM_OR_PQNAME(handler, uri, name, rawName, value, owner);
            break;
         case 17:
            processedValue = this.processNCNAME(handler, uri, name, rawName, value, owner);
            break;
         case 18:
            processedValue = this.processAVT_QNAME(handler, uri, name, rawName, value, owner);
            break;
         case 19:
            processedValue = this.processQNAMESRNU(handler, uri, name, rawName, value);
            break;
         case 20:
            processedValue = this.processPREFIX_LIST(handler, uri, name, rawName, value);
      }

      return processedValue;
   }

   void setDefAttrValue(StylesheetHandler handler, ElemTemplateElement elem) throws SAXException {
      this.setAttrValue(handler, this.getNamespace(), this.getName(), this.getName(), this.getDefault(), elem);
   }

   private Class getPrimativeClass(Object obj) {
      if (obj instanceof XPath) {
         return class$org$apache$xpath$XPath == null ? (class$org$apache$xpath$XPath = class$("org.apache.xpath.XPath")) : class$org$apache$xpath$XPath;
      } else {
         Class cl = obj.getClass();
         if (cl == (class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double)) {
            cl = Double.TYPE;
         }

         if (cl == (class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float)) {
            cl = Float.TYPE;
         } else if (cl == (class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean)) {
            cl = Boolean.TYPE;
         } else if (cl == (class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte)) {
            cl = Byte.TYPE;
         } else if (cl == (class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character)) {
            cl = Character.TYPE;
         } else if (cl == (class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short)) {
            cl = Short.TYPE;
         } else if (cl == (class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer)) {
            cl = Integer.TYPE;
         } else if (cl == (class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long)) {
            cl = Long.TYPE;
         }

         return cl;
      }
   }

   private StringBuffer getListOfEnums() {
      StringBuffer enumNamesList = new StringBuffer();
      String[] enumValues = this.getEnumNames();

      for(int i = 0; i < enumValues.length; ++i) {
         if (i > 0) {
            enumNamesList.append(' ');
         }

         enumNamesList.append(enumValues[i]);
      }

      return enumNamesList;
   }

   boolean setAttrValue(StylesheetHandler handler, String attrUri, String attrLocalName, String attrRawName, String attrValue, ElemTemplateElement elem) throws SAXException {
      if (!attrRawName.equals("xmlns") && !attrRawName.startsWith("xmlns:")) {
         String setterString = this.getSetterMethodName();
         if (null != setterString) {
            try {
               Method meth;
               Object[] args;
               Class[] argTypes;
               if (setterString.equals("setForeignAttr")) {
                  if (attrUri == null) {
                     attrUri = "";
                  }

                  Class sclass = attrUri.getClass();
                  argTypes = new Class[]{sclass, sclass, sclass, sclass};
                  meth = elem.getClass().getMethod(setterString, argTypes);
                  args = new Object[]{attrUri, attrLocalName, attrRawName, attrValue};
               } else {
                  Object value = this.processValue(handler, attrUri, attrLocalName, attrRawName, attrValue, elem);
                  if (null == value) {
                     return false;
                  }

                  argTypes = new Class[]{this.getPrimativeClass(value)};

                  try {
                     meth = elem.getClass().getMethod(setterString, argTypes);
                  } catch (NoSuchMethodException var14) {
                     Class cl = value.getClass();
                     argTypes[0] = cl;
                     meth = elem.getClass().getMethod(setterString, argTypes);
                  }

                  args = new Object[]{value};
               }

               meth.invoke(elem, args);
            } catch (NoSuchMethodException var15) {
               if (!setterString.equals("setForeignAttr")) {
                  handler.error("ER_FAILED_CALLING_METHOD", new Object[]{setterString}, var15);
                  return false;
               }
            } catch (IllegalAccessException var16) {
               handler.error("ER_FAILED_CALLING_METHOD", new Object[]{setterString}, var16);
               return false;
            } catch (InvocationTargetException var17) {
               this.handleError(handler, "WG_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{"name", this.getName()}, var17);
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   private void handleError(StylesheetHandler handler, String msg, Object[] args, Exception exc) throws SAXException {
      switch (this.getErrorType()) {
         case 0:
         case 1:
            handler.error(msg, args, exc);
            break;
         case 2:
            handler.warn(msg, args);
      }

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
