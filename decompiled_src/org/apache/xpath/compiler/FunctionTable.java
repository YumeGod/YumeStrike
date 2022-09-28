package org.apache.xpath.compiler;

import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.apache.xpath.functions.Function;

public class FunctionTable {
   public static final int FUNC_CURRENT = 0;
   public static final int FUNC_LAST = 1;
   public static final int FUNC_POSITION = 2;
   public static final int FUNC_COUNT = 3;
   public static final int FUNC_ID = 4;
   public static final int FUNC_KEY = 5;
   public static final int FUNC_LOCAL_PART = 7;
   public static final int FUNC_NAMESPACE = 8;
   public static final int FUNC_QNAME = 9;
   public static final int FUNC_GENERATE_ID = 10;
   public static final int FUNC_NOT = 11;
   public static final int FUNC_TRUE = 12;
   public static final int FUNC_FALSE = 13;
   public static final int FUNC_BOOLEAN = 14;
   public static final int FUNC_NUMBER = 15;
   public static final int FUNC_FLOOR = 16;
   public static final int FUNC_CEILING = 17;
   public static final int FUNC_ROUND = 18;
   public static final int FUNC_SUM = 19;
   public static final int FUNC_STRING = 20;
   public static final int FUNC_STARTS_WITH = 21;
   public static final int FUNC_CONTAINS = 22;
   public static final int FUNC_SUBSTRING_BEFORE = 23;
   public static final int FUNC_SUBSTRING_AFTER = 24;
   public static final int FUNC_NORMALIZE_SPACE = 25;
   public static final int FUNC_TRANSLATE = 26;
   public static final int FUNC_CONCAT = 27;
   public static final int FUNC_SUBSTRING = 29;
   public static final int FUNC_STRING_LENGTH = 30;
   public static final int FUNC_SYSTEM_PROPERTY = 31;
   public static final int FUNC_LANG = 32;
   public static final int FUNC_EXT_FUNCTION_AVAILABLE = 33;
   public static final int FUNC_EXT_ELEM_AVAILABLE = 34;
   public static final int FUNC_UNPARSED_ENTITY_URI = 36;
   public static final int FUNC_DOCLOCATION = 35;
   private static Class[] m_functions = new Class[37];
   private static HashMap m_functionID = new HashMap();
   private Class[] m_functions_customer = new Class[30];
   private HashMap m_functionID_customer = new HashMap();
   private static final int NUM_BUILT_IN_FUNCS = 37;
   private static final int NUM_ALLOWABLE_ADDINS = 30;
   private int m_funcNextFreeIndex = 37;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncCurrent;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncLast;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncPosition;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncCount;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncId;
   // $FF: synthetic field
   static Class class$org$apache$xalan$templates$FuncKey;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncLocalPart;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncNamespace;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncQname;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncGenerateId;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncNot;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncTrue;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncFalse;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncBoolean;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncLang;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncNumber;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncFloor;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncCeiling;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncRound;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncSum;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncString;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncStartsWith;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncContains;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncSubstringBefore;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncSubstringAfter;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncNormalizeSpace;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncTranslate;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncConcat;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncSystemProperty;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncExtFunctionAvailable;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncExtElementAvailable;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncSubstring;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncStringLength;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncDoclocation;
   // $FF: synthetic field
   static Class class$org$apache$xpath$functions$FuncUnparsedEntityURI;

   String getFunctionName(int funcID) {
      return funcID < 37 ? m_functions[funcID].getName() : this.m_functions_customer[funcID - 37].getName();
   }

   Function getFunction(int which) throws TransformerException {
      try {
         return which < 37 ? (Function)m_functions[which].newInstance() : (Function)this.m_functions_customer[which - 37].newInstance();
      } catch (IllegalAccessException var4) {
         throw new TransformerException(var4.getMessage());
      } catch (InstantiationException var5) {
         throw new TransformerException(var5.getMessage());
      }
   }

   Object getFunctionID(String key) {
      Object id = this.m_functionID_customer.get(key);
      if (null == id) {
         id = m_functionID.get(key);
      }

      return id;
   }

   public int installFunction(String name, Class func) {
      Object funcIndexObj = this.getFunctionID(name);
      int funcIndex;
      if (null != funcIndexObj) {
         funcIndex = (Integer)funcIndexObj;
         if (funcIndex < 37) {
            funcIndex = this.m_funcNextFreeIndex++;
            this.m_functionID_customer.put(name, new Integer(funcIndex));
         }

         this.m_functions_customer[funcIndex - 37] = func;
      } else {
         funcIndex = this.m_funcNextFreeIndex++;
         this.m_functions_customer[funcIndex - 37] = func;
         this.m_functionID_customer.put(name, new Integer(funcIndex));
      }

      return funcIndex;
   }

   public boolean functionAvailable(String methName) {
      Object tblEntry = m_functionID.get(methName);
      if (null != tblEntry) {
         return true;
      } else {
         tblEntry = this.m_functionID_customer.get(methName);
         return null != tblEntry;
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

   static {
      m_functions[0] = class$org$apache$xpath$functions$FuncCurrent == null ? (class$org$apache$xpath$functions$FuncCurrent = class$("org.apache.xpath.functions.FuncCurrent")) : class$org$apache$xpath$functions$FuncCurrent;
      m_functions[1] = class$org$apache$xpath$functions$FuncLast == null ? (class$org$apache$xpath$functions$FuncLast = class$("org.apache.xpath.functions.FuncLast")) : class$org$apache$xpath$functions$FuncLast;
      m_functions[2] = class$org$apache$xpath$functions$FuncPosition == null ? (class$org$apache$xpath$functions$FuncPosition = class$("org.apache.xpath.functions.FuncPosition")) : class$org$apache$xpath$functions$FuncPosition;
      m_functions[3] = class$org$apache$xpath$functions$FuncCount == null ? (class$org$apache$xpath$functions$FuncCount = class$("org.apache.xpath.functions.FuncCount")) : class$org$apache$xpath$functions$FuncCount;
      m_functions[4] = class$org$apache$xpath$functions$FuncId == null ? (class$org$apache$xpath$functions$FuncId = class$("org.apache.xpath.functions.FuncId")) : class$org$apache$xpath$functions$FuncId;
      m_functions[5] = class$org$apache$xalan$templates$FuncKey == null ? (class$org$apache$xalan$templates$FuncKey = class$("org.apache.xalan.templates.FuncKey")) : class$org$apache$xalan$templates$FuncKey;
      m_functions[7] = class$org$apache$xpath$functions$FuncLocalPart == null ? (class$org$apache$xpath$functions$FuncLocalPart = class$("org.apache.xpath.functions.FuncLocalPart")) : class$org$apache$xpath$functions$FuncLocalPart;
      m_functions[8] = class$org$apache$xpath$functions$FuncNamespace == null ? (class$org$apache$xpath$functions$FuncNamespace = class$("org.apache.xpath.functions.FuncNamespace")) : class$org$apache$xpath$functions$FuncNamespace;
      m_functions[9] = class$org$apache$xpath$functions$FuncQname == null ? (class$org$apache$xpath$functions$FuncQname = class$("org.apache.xpath.functions.FuncQname")) : class$org$apache$xpath$functions$FuncQname;
      m_functions[10] = class$org$apache$xpath$functions$FuncGenerateId == null ? (class$org$apache$xpath$functions$FuncGenerateId = class$("org.apache.xpath.functions.FuncGenerateId")) : class$org$apache$xpath$functions$FuncGenerateId;
      m_functions[11] = class$org$apache$xpath$functions$FuncNot == null ? (class$org$apache$xpath$functions$FuncNot = class$("org.apache.xpath.functions.FuncNot")) : class$org$apache$xpath$functions$FuncNot;
      m_functions[12] = class$org$apache$xpath$functions$FuncTrue == null ? (class$org$apache$xpath$functions$FuncTrue = class$("org.apache.xpath.functions.FuncTrue")) : class$org$apache$xpath$functions$FuncTrue;
      m_functions[13] = class$org$apache$xpath$functions$FuncFalse == null ? (class$org$apache$xpath$functions$FuncFalse = class$("org.apache.xpath.functions.FuncFalse")) : class$org$apache$xpath$functions$FuncFalse;
      m_functions[14] = class$org$apache$xpath$functions$FuncBoolean == null ? (class$org$apache$xpath$functions$FuncBoolean = class$("org.apache.xpath.functions.FuncBoolean")) : class$org$apache$xpath$functions$FuncBoolean;
      m_functions[32] = class$org$apache$xpath$functions$FuncLang == null ? (class$org$apache$xpath$functions$FuncLang = class$("org.apache.xpath.functions.FuncLang")) : class$org$apache$xpath$functions$FuncLang;
      m_functions[15] = class$org$apache$xpath$functions$FuncNumber == null ? (class$org$apache$xpath$functions$FuncNumber = class$("org.apache.xpath.functions.FuncNumber")) : class$org$apache$xpath$functions$FuncNumber;
      m_functions[16] = class$org$apache$xpath$functions$FuncFloor == null ? (class$org$apache$xpath$functions$FuncFloor = class$("org.apache.xpath.functions.FuncFloor")) : class$org$apache$xpath$functions$FuncFloor;
      m_functions[17] = class$org$apache$xpath$functions$FuncCeiling == null ? (class$org$apache$xpath$functions$FuncCeiling = class$("org.apache.xpath.functions.FuncCeiling")) : class$org$apache$xpath$functions$FuncCeiling;
      m_functions[18] = class$org$apache$xpath$functions$FuncRound == null ? (class$org$apache$xpath$functions$FuncRound = class$("org.apache.xpath.functions.FuncRound")) : class$org$apache$xpath$functions$FuncRound;
      m_functions[19] = class$org$apache$xpath$functions$FuncSum == null ? (class$org$apache$xpath$functions$FuncSum = class$("org.apache.xpath.functions.FuncSum")) : class$org$apache$xpath$functions$FuncSum;
      m_functions[20] = class$org$apache$xpath$functions$FuncString == null ? (class$org$apache$xpath$functions$FuncString = class$("org.apache.xpath.functions.FuncString")) : class$org$apache$xpath$functions$FuncString;
      m_functions[21] = class$org$apache$xpath$functions$FuncStartsWith == null ? (class$org$apache$xpath$functions$FuncStartsWith = class$("org.apache.xpath.functions.FuncStartsWith")) : class$org$apache$xpath$functions$FuncStartsWith;
      m_functions[22] = class$org$apache$xpath$functions$FuncContains == null ? (class$org$apache$xpath$functions$FuncContains = class$("org.apache.xpath.functions.FuncContains")) : class$org$apache$xpath$functions$FuncContains;
      m_functions[23] = class$org$apache$xpath$functions$FuncSubstringBefore == null ? (class$org$apache$xpath$functions$FuncSubstringBefore = class$("org.apache.xpath.functions.FuncSubstringBefore")) : class$org$apache$xpath$functions$FuncSubstringBefore;
      m_functions[24] = class$org$apache$xpath$functions$FuncSubstringAfter == null ? (class$org$apache$xpath$functions$FuncSubstringAfter = class$("org.apache.xpath.functions.FuncSubstringAfter")) : class$org$apache$xpath$functions$FuncSubstringAfter;
      m_functions[25] = class$org$apache$xpath$functions$FuncNormalizeSpace == null ? (class$org$apache$xpath$functions$FuncNormalizeSpace = class$("org.apache.xpath.functions.FuncNormalizeSpace")) : class$org$apache$xpath$functions$FuncNormalizeSpace;
      m_functions[26] = class$org$apache$xpath$functions$FuncTranslate == null ? (class$org$apache$xpath$functions$FuncTranslate = class$("org.apache.xpath.functions.FuncTranslate")) : class$org$apache$xpath$functions$FuncTranslate;
      m_functions[27] = class$org$apache$xpath$functions$FuncConcat == null ? (class$org$apache$xpath$functions$FuncConcat = class$("org.apache.xpath.functions.FuncConcat")) : class$org$apache$xpath$functions$FuncConcat;
      m_functions[31] = class$org$apache$xpath$functions$FuncSystemProperty == null ? (class$org$apache$xpath$functions$FuncSystemProperty = class$("org.apache.xpath.functions.FuncSystemProperty")) : class$org$apache$xpath$functions$FuncSystemProperty;
      m_functions[33] = class$org$apache$xpath$functions$FuncExtFunctionAvailable == null ? (class$org$apache$xpath$functions$FuncExtFunctionAvailable = class$("org.apache.xpath.functions.FuncExtFunctionAvailable")) : class$org$apache$xpath$functions$FuncExtFunctionAvailable;
      m_functions[34] = class$org$apache$xpath$functions$FuncExtElementAvailable == null ? (class$org$apache$xpath$functions$FuncExtElementAvailable = class$("org.apache.xpath.functions.FuncExtElementAvailable")) : class$org$apache$xpath$functions$FuncExtElementAvailable;
      m_functions[29] = class$org$apache$xpath$functions$FuncSubstring == null ? (class$org$apache$xpath$functions$FuncSubstring = class$("org.apache.xpath.functions.FuncSubstring")) : class$org$apache$xpath$functions$FuncSubstring;
      m_functions[30] = class$org$apache$xpath$functions$FuncStringLength == null ? (class$org$apache$xpath$functions$FuncStringLength = class$("org.apache.xpath.functions.FuncStringLength")) : class$org$apache$xpath$functions$FuncStringLength;
      m_functions[35] = class$org$apache$xpath$functions$FuncDoclocation == null ? (class$org$apache$xpath$functions$FuncDoclocation = class$("org.apache.xpath.functions.FuncDoclocation")) : class$org$apache$xpath$functions$FuncDoclocation;
      m_functions[36] = class$org$apache$xpath$functions$FuncUnparsedEntityURI == null ? (class$org$apache$xpath$functions$FuncUnparsedEntityURI = class$("org.apache.xpath.functions.FuncUnparsedEntityURI")) : class$org$apache$xpath$functions$FuncUnparsedEntityURI;
      m_functionID.put("current", new Integer(0));
      m_functionID.put("last", new Integer(1));
      m_functionID.put("position", new Integer(2));
      m_functionID.put("count", new Integer(3));
      m_functionID.put("id", new Integer(4));
      m_functionID.put("key", new Integer(5));
      m_functionID.put("local-name", new Integer(7));
      m_functionID.put("namespace-uri", new Integer(8));
      m_functionID.put("name", new Integer(9));
      m_functionID.put("generate-id", new Integer(10));
      m_functionID.put("not", new Integer(11));
      m_functionID.put("true", new Integer(12));
      m_functionID.put("false", new Integer(13));
      m_functionID.put("boolean", new Integer(14));
      m_functionID.put("lang", new Integer(32));
      m_functionID.put("number", new Integer(15));
      m_functionID.put("floor", new Integer(16));
      m_functionID.put("ceiling", new Integer(17));
      m_functionID.put("round", new Integer(18));
      m_functionID.put("sum", new Integer(19));
      m_functionID.put("string", new Integer(20));
      m_functionID.put("starts-with", new Integer(21));
      m_functionID.put("contains", new Integer(22));
      m_functionID.put("substring-before", new Integer(23));
      m_functionID.put("substring-after", new Integer(24));
      m_functionID.put("normalize-space", new Integer(25));
      m_functionID.put("translate", new Integer(26));
      m_functionID.put("concat", new Integer(27));
      m_functionID.put("system-property", new Integer(31));
      m_functionID.put("function-available", new Integer(33));
      m_functionID.put("element-available", new Integer(34));
      m_functionID.put("substring", new Integer(29));
      m_functionID.put("string-length", new Integer(30));
      m_functionID.put("unparsed-entity-uri", new Integer(36));
      m_functionID.put("document-location", new Integer(35));
   }
}
