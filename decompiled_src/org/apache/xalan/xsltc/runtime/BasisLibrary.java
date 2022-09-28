package org.apache.xalan.xsltc.runtime;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.AbsoluteIterator;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.MultiDOM;
import org.apache.xalan.xsltc.dom.SingletonIterator;
import org.apache.xalan.xsltc.dom.StepIterator;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XML11Char;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class BasisLibrary {
   private static final String EMPTYSTRING = "";
   private static final int DOUBLE_FRACTION_DIGITS = 340;
   private static final double lowerBounds = 0.001;
   private static final double upperBounds = 1.0E7;
   private static DecimalFormat defaultFormatter;
   private static String defaultPattern = "";
   private static FieldPosition _fieldPosition;
   private static char[] _characterArray;
   private static int prefixIndex;
   public static final String RUN_TIME_INTERNAL_ERR = "RUN_TIME_INTERNAL_ERR";
   public static final String RUN_TIME_COPY_ERR = "RUN_TIME_COPY_ERR";
   public static final String DATA_CONVERSION_ERR = "DATA_CONVERSION_ERR";
   public static final String EXTERNAL_FUNC_ERR = "EXTERNAL_FUNC_ERR";
   public static final String EQUALITY_EXPR_ERR = "EQUALITY_EXPR_ERR";
   public static final String INVALID_ARGUMENT_ERR = "INVALID_ARGUMENT_ERR";
   public static final String FORMAT_NUMBER_ERR = "FORMAT_NUMBER_ERR";
   public static final String ITERATOR_CLONE_ERR = "ITERATOR_CLONE_ERR";
   public static final String AXIS_SUPPORT_ERR = "AXIS_SUPPORT_ERR";
   public static final String TYPED_AXIS_SUPPORT_ERR = "TYPED_AXIS_SUPPORT_ERR";
   public static final String STRAY_ATTRIBUTE_ERR = "STRAY_ATTRIBUTE_ERR";
   public static final String STRAY_NAMESPACE_ERR = "STRAY_NAMESPACE_ERR";
   public static final String NAMESPACE_PREFIX_ERR = "NAMESPACE_PREFIX_ERR";
   public static final String DOM_ADAPTER_INIT_ERR = "DOM_ADAPTER_INIT_ERR";
   public static final String PARSER_DTD_SUPPORT_ERR = "PARSER_DTD_SUPPORT_ERR";
   public static final String NAMESPACES_SUPPORT_ERR = "NAMESPACES_SUPPORT_ERR";
   public static final String CANT_RESOLVE_RELATIVE_URI_ERR = "CANT_RESOLVE_RELATIVE_URI_ERR";
   public static final String UNSUPPORTED_XSL_ERR = "UNSUPPORTED_XSL_ERR";
   public static final String UNSUPPORTED_EXT_ERR = "UNSUPPORTED_EXT_ERR";
   public static final String UNKNOWN_TRANSLET_VERSION_ERR = "UNKNOWN_TRANSLET_VERSION_ERR";
   public static final String INVALID_QNAME_ERR = "INVALID_QNAME_ERR";
   public static final String INVALID_NCNAME_ERR = "INVALID_NCNAME_ERR";
   public static final String UNALLOWED_EXTENSION_FUNCTION_ERR = "UNALLOWED_EXTENSION_FUNCTION_ERR";
   public static final String UNALLOWED_EXTENSION_ELEMENT_ERR = "UNALLOWED_EXTENSION_ELEMENT_ERR";
   private static ResourceBundle m_bundle;
   public static final String ERROR_MESSAGES_KEY = "error-messages";
   // $FF: synthetic field
   static Class class$java$lang$String;

   public static int countF(DTMAxisIterator iterator) {
      return iterator.getLast();
   }

   /** @deprecated */
   public static int positionF(DTMAxisIterator iterator) {
      return iterator.isReverse() ? iterator.getLast() - iterator.getPosition() + 1 : iterator.getPosition();
   }

   public static double sumF(DTMAxisIterator iterator, DOM dom) {
      try {
         double result;
         int node;
         for(result = 0.0; (node = iterator.next()) != -1; result += Double.parseDouble(dom.getStringValueX(node))) {
         }

         return result;
      } catch (NumberFormatException var5) {
         return Double.NaN;
      }
   }

   public static String stringF(int node, DOM dom) {
      return dom.getStringValueX(node);
   }

   public static String stringF(Object obj, DOM dom) {
      if (obj instanceof DTMAxisIterator) {
         return dom.getStringValueX(((DTMAxisIterator)obj).reset().next());
      } else if (obj instanceof Node) {
         return dom.getStringValueX(((Node)obj).node);
      } else {
         return obj instanceof DOM ? ((DOM)obj).getStringValue() : obj.toString();
      }
   }

   public static String stringF(Object obj, int node, DOM dom) {
      if (obj instanceof DTMAxisIterator) {
         return dom.getStringValueX(((DTMAxisIterator)obj).reset().next());
      } else if (obj instanceof Node) {
         return dom.getStringValueX(((Node)obj).node);
      } else if (obj instanceof DOM) {
         return ((DOM)obj).getStringValue();
      } else if (obj instanceof Double) {
         Double d = (Double)obj;
         String result = d.toString();
         int length = result.length();
         return result.charAt(length - 2) == '.' && result.charAt(length - 1) == '0' ? result.substring(0, length - 2) : result;
      } else {
         return obj != null ? obj.toString() : stringF(node, dom);
      }
   }

   public static double numberF(int node, DOM dom) {
      return stringToReal(dom.getStringValueX(node));
   }

   public static double numberF(Object obj, DOM dom) {
      if (obj instanceof Double) {
         return (Double)obj;
      } else if (obj instanceof Integer) {
         return ((Integer)obj).doubleValue();
      } else if (obj instanceof Boolean) {
         return (Boolean)obj ? 1.0 : 0.0;
      } else if (obj instanceof String) {
         return stringToReal((String)obj);
      } else if (obj instanceof DTMAxisIterator) {
         DTMAxisIterator iter = (DTMAxisIterator)obj;
         return stringToReal(dom.getStringValueX(iter.reset().next()));
      } else if (obj instanceof Node) {
         return stringToReal(dom.getStringValueX(((Node)obj).node));
      } else if (obj instanceof DOM) {
         return stringToReal(((DOM)obj).getStringValue());
      } else {
         String className = obj.getClass().getName();
         runTimeError("INVALID_ARGUMENT_ERR", className, "number()");
         return 0.0;
      }
   }

   public static double roundF(double d) {
      return !(d < -0.5) && !(d > 0.0) ? (d == 0.0 ? d : (Double.isNaN(d) ? Double.NaN : -0.0)) : Math.floor(d + 0.5);
   }

   public static boolean booleanF(Object obj) {
      if (!(obj instanceof Double)) {
         if (obj instanceof Integer) {
            return ((Integer)obj).doubleValue() != 0.0;
         } else if (obj instanceof Boolean) {
            return (Boolean)obj;
         } else if (obj instanceof String) {
            return !((String)obj).equals("");
         } else if (obj instanceof DTMAxisIterator) {
            DTMAxisIterator iter = (DTMAxisIterator)obj;
            return iter.reset().next() != -1;
         } else if (obj instanceof Node) {
            return true;
         } else {
            String className;
            if (obj instanceof DOM) {
               className = ((DOM)obj).getStringValue();
               return !className.equals("");
            } else {
               className = obj.getClass().getName();
               runTimeError("INVALID_ARGUMENT_ERR", className, "boolean()");
               return false;
            }
         }
      } else {
         double temp = (Double)obj;
         return temp != 0.0 && !Double.isNaN(temp);
      }
   }

   public static String substringF(String value, double start) {
      try {
         int strlen = value.length();
         int istart = (int)Math.round(start) - 1;
         if (Double.isNaN(start)) {
            return "";
         } else if (istart > strlen) {
            return "";
         } else {
            if (istart < 1) {
               istart = 0;
            }

            return value.substring(istart);
         }
      } catch (IndexOutOfBoundsException var5) {
         runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"substring()");
         return null;
      }
   }

   public static String substringF(String value, double start, double length) {
      try {
         int strlen = value.length();
         int istart = (int)Math.round(start) - 1;
         int isum = istart + (int)Math.round(length);
         if (Double.isInfinite(length)) {
            isum = Integer.MAX_VALUE;
         }

         if (!Double.isNaN(start) && !Double.isNaN(length)) {
            if (Double.isInfinite(start)) {
               return "";
            } else if (istart > strlen) {
               return "";
            } else if (isum < 0) {
               return "";
            } else {
               if (istart < 0) {
                  istart = 0;
               }

               return isum > strlen ? value.substring(istart) : value.substring(istart, isum);
            }
         } else {
            return "";
         }
      } catch (IndexOutOfBoundsException var8) {
         runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"substring()");
         return null;
      }
   }

   public static String substring_afterF(String value, String substring) {
      int index = value.indexOf(substring);
      return index >= 0 ? value.substring(index + substring.length()) : "";
   }

   public static String substring_beforeF(String value, String substring) {
      int index = value.indexOf(substring);
      return index >= 0 ? value.substring(0, index) : "";
   }

   public static String translateF(String value, String from, String to) {
      int tol = to.length();
      int froml = from.length();
      int valuel = value.length();
      StringBuffer result = new StringBuffer();

      for(int i = 0; i < valuel; ++i) {
         char ch = value.charAt(i);

         int j;
         for(j = 0; j < froml; ++j) {
            if (ch == from.charAt(j)) {
               if (j < tol) {
                  result.append(to.charAt(j));
               }
               break;
            }
         }

         if (j == froml) {
            result.append(ch);
         }
      }

      return result.toString();
   }

   public static String normalize_spaceF(int node, DOM dom) {
      return normalize_spaceF(dom.getStringValueX(node));
   }

   public static String normalize_spaceF(String value) {
      int i = 0;
      int n = value.length();

      StringBuffer result;
      for(result = new StringBuffer(); i < n && isWhiteSpace(value.charAt(i)); ++i) {
      }

      while(true) {
         while(i >= n || isWhiteSpace(value.charAt(i))) {
            if (i == n) {
               return result.toString();
            }

            while(i < n && isWhiteSpace(value.charAt(i))) {
               ++i;
            }

            if (i < n) {
               result.append(' ');
            }
         }

         result.append(value.charAt(i++));
      }
   }

   public static String generate_idF(int node) {
      return node > 0 ? "N" + node : "";
   }

   public static String getLocalName(String value) {
      int idx = value.lastIndexOf(58);
      if (idx >= 0) {
         value = value.substring(idx + 1);
      }

      idx = value.lastIndexOf(64);
      if (idx >= 0) {
         value = value.substring(idx + 1);
      }

      return value;
   }

   public static void unresolved_externalF(String name) {
      runTimeError("EXTERNAL_FUNC_ERR", (Object)name);
   }

   public static void unallowed_extension_functionF(String name) {
      runTimeError("UNALLOWED_EXTENSION_FUNCTION_ERR", (Object)name);
   }

   public static void unallowed_extension_elementF(String name) {
      runTimeError("UNALLOWED_EXTENSION_ELEMENT_ERR", (Object)name);
   }

   public static void unsupported_ElementF(String qname, boolean isExtension) {
      if (isExtension) {
         runTimeError("UNSUPPORTED_EXT_ERR", (Object)qname);
      } else {
         runTimeError("UNSUPPORTED_XSL_ERR", (Object)qname);
      }

   }

   public static String namespace_uriF(DTMAxisIterator iter, DOM dom) {
      return namespace_uriF(iter.next(), dom);
   }

   public static String system_propertyF(String name) {
      if (name.equals("xsl:version")) {
         return "1.0";
      } else if (name.equals("xsl:vendor")) {
         return "Apache Software Foundation (Xalan XSLTC)";
      } else if (name.equals("xsl:vendor-url")) {
         return "http://xml.apache.org/xalan-j";
      } else {
         runTimeError("INVALID_ARGUMENT_ERR", name, "system-property()");
         return "";
      }
   }

   public static String namespace_uriF(int node, DOM dom) {
      String value = dom.getNodeName(node);
      int colon = value.lastIndexOf(58);
      return colon >= 0 ? value.substring(0, colon) : "";
   }

   public static String objectTypeF(Object obj) {
      if (obj instanceof String) {
         return "string";
      } else if (obj instanceof Boolean) {
         return "boolean";
      } else if (obj instanceof Number) {
         return "number";
      } else if (obj instanceof DOM) {
         return "RTF";
      } else {
         return obj instanceof DTMAxisIterator ? "node-set" : "unknown";
      }
   }

   public static DTMAxisIterator nodesetF(Object obj) {
      if (obj instanceof DOM) {
         DOM dom = (DOM)obj;
         return new SingletonIterator(dom.getDocument(), true);
      } else if (obj instanceof DTMAxisIterator) {
         return (DTMAxisIterator)obj;
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", "node-set", className);
         return null;
      }
   }

   private static boolean isWhiteSpace(char ch) {
      return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
   }

   private static boolean compareStrings(String lstring, String rstring, int op, DOM dom) {
      switch (op) {
         case 0:
            return lstring.equals(rstring);
         case 1:
            return !lstring.equals(rstring);
         case 2:
            return numberF(lstring, dom) > numberF(rstring, dom);
         case 3:
            return numberF(lstring, dom) < numberF(rstring, dom);
         case 4:
            return numberF(lstring, dom) >= numberF(rstring, dom);
         case 5:
            return numberF(lstring, dom) <= numberF(rstring, dom);
         default:
            runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"compare()");
            return false;
      }
   }

   public static boolean compare(DTMAxisIterator left, DTMAxisIterator right, int op, DOM dom) {
      left.reset();

      int lnode;
      label32:
      while((lnode = left.next()) != -1) {
         String lvalue = dom.getStringValueX(lnode);
         right.reset();

         int rnode;
         do {
            do {
               if ((rnode = right.next()) == -1) {
                  continue label32;
               }

               if (lnode != rnode) {
                  break;
               }

               if (op == 0) {
                  return true;
               }
            } while(op == 1);
         } while(!compareStrings(lvalue, dom.getStringValueX(rnode), op, dom));

         return true;
      }

      return false;
   }

   public static boolean compare(int node, DTMAxisIterator iterator, int op, DOM dom) {
      int rnode;
      String value;
      switch (op) {
         case 0:
            rnode = iterator.next();
            if (rnode != -1) {
               value = dom.getStringValueX(node);

               do {
                  if (node == rnode || value.equals(dom.getStringValueX(rnode))) {
                     return true;
                  }
               } while((rnode = iterator.next()) != -1);
            }
            break;
         case 1:
            rnode = iterator.next();
            if (rnode != -1) {
               value = dom.getStringValueX(node);

               do {
                  if (node != rnode && !value.equals(dom.getStringValueX(rnode))) {
                     return true;
                  }
               } while((rnode = iterator.next()) != -1);
            }
            break;
         case 2:
            do {
               if ((rnode = iterator.next()) == -1) {
                  return false;
               }
            } while(rnode >= node);

            return true;
         case 3:
            while((rnode = iterator.next()) != -1) {
               if (rnode > node) {
                  return true;
               }
            }
      }

      return false;
   }

   public static boolean compare(DTMAxisIterator left, double rnumber, int op, DOM dom) {
      int node;
      switch (op) {
         case 0:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(numberF(dom.getStringValueX(node), dom) != rnumber);

            return true;
         case 1:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(numberF(dom.getStringValueX(node), dom) == rnumber);

            return true;
         case 2:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(!(numberF(dom.getStringValueX(node), dom) > rnumber));

            return true;
         case 3:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(!(numberF(dom.getStringValueX(node), dom) < rnumber));

            return true;
         case 4:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(!(numberF(dom.getStringValueX(node), dom) >= rnumber));

            return true;
         case 5:
            do {
               if ((node = left.next()) == -1) {
                  return false;
               }
            } while(!(numberF(dom.getStringValueX(node), dom) <= rnumber));

            return true;
         default:
            runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"compare()");
            return false;
      }
   }

   public static boolean compare(DTMAxisIterator left, String rstring, int op, DOM dom) {
      int node;
      do {
         if ((node = left.next()) == -1) {
            return false;
         }
      } while(!compareStrings(dom.getStringValueX(node), rstring, op, dom));

      return true;
   }

   public static boolean compare(Object left, Object right, int op, DOM dom) {
      boolean result = false;
      boolean hasSimpleArgs = hasSimpleType(left) && hasSimpleType(right);
      if (op != 0 && op != 1) {
         if (left instanceof Node || right instanceof Node) {
            if (left instanceof Boolean) {
               right = new Boolean(booleanF(right));
               hasSimpleArgs = true;
            }

            if (right instanceof Boolean) {
               left = new Boolean(booleanF(left));
               hasSimpleArgs = true;
            }
         }

         if (hasSimpleArgs) {
            switch (op) {
               case 2:
                  return numberF(left, dom) > numberF(right, dom);
               case 3:
                  return numberF(left, dom) < numberF(right, dom);
               case 4:
                  return numberF(left, dom) >= numberF(right, dom);
               case 5:
                  return numberF(left, dom) <= numberF(right, dom);
               default:
                  runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"compare()");
            }
         }
      }

      if (hasSimpleArgs) {
         if (!(left instanceof Boolean) && !(right instanceof Boolean)) {
            if (!(left instanceof Double) && !(right instanceof Double) && !(left instanceof Integer) && !(right instanceof Integer)) {
               result = stringF(left, dom).equals(stringF(right, dom));
            } else {
               result = numberF(left, dom) == numberF(right, dom);
            }
         } else {
            result = booleanF(left) == booleanF(right);
         }

         if (op == 1) {
            result = !result;
         }
      } else {
         if (left instanceof Node) {
            left = new SingletonIterator(((Node)left).node);
         }

         if (right instanceof Node) {
            right = new SingletonIterator(((Node)right).node);
         }

         if (hasSimpleType(left) || left instanceof DOM && right instanceof DTMAxisIterator) {
            Object temp = right;
            right = left;
            left = temp;
            op = Operators.swapOp(op);
         }

         if (left instanceof DOM) {
            if (right instanceof Boolean) {
               result = (Boolean)right;
               return result == (op == 0);
            }

            String sleft = ((DOM)left).getStringValue();
            if (right instanceof Number) {
               result = ((Number)right).doubleValue() == stringToReal(sleft);
            } else if (right instanceof String) {
               result = sleft.equals((String)right);
            } else if (right instanceof DOM) {
               result = sleft.equals(((DOM)right).getStringValue());
            }

            if (op == 1) {
               result = !result;
            }

            return result;
         }

         DTMAxisIterator iter = ((DTMAxisIterator)left).reset();
         if (right instanceof DTMAxisIterator) {
            result = compare(iter, (DTMAxisIterator)right, op, dom);
         } else if (right instanceof String) {
            result = compare(iter, (String)right, op, dom);
         } else if (right instanceof Number) {
            double temp = ((Number)right).doubleValue();
            result = compare(iter, temp, op, dom);
         } else if (right instanceof Boolean) {
            boolean temp = (Boolean)right;
            result = iter.reset().next() != -1 == temp;
         } else if (right instanceof DOM) {
            result = compare(iter, ((DOM)right).getStringValue(), op, dom);
         } else {
            if (right == null) {
               return false;
            }

            String className = right.getClass().getName();
            runTimeError("INVALID_ARGUMENT_ERR", className, "compare()");
         }
      }

      return result;
   }

   public static boolean testLanguage(String testLang, DOM dom, int node) {
      String nodeLang = dom.getLanguage(node);
      if (nodeLang == null) {
         return false;
      } else {
         nodeLang = nodeLang.toLowerCase();
         testLang = testLang.toLowerCase();
         return testLang.length() == 2 ? nodeLang.startsWith(testLang) : nodeLang.equals(testLang);
      }
   }

   private static boolean hasSimpleType(Object obj) {
      return obj instanceof Boolean || obj instanceof Double || obj instanceof Integer || obj instanceof String || obj instanceof Node || obj instanceof DOM;
   }

   public static double stringToReal(String s) {
      try {
         return Double.valueOf(s);
      } catch (NumberFormatException var2) {
         return Double.NaN;
      }
   }

   public static int stringToInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException var2) {
         return -1;
      }
   }

   public static String realToString(double d) {
      double m = Math.abs(d);
      if (m >= 0.001 && m < 1.0E7) {
         String result = Double.toString(d);
         int length = result.length();
         return result.charAt(length - 2) == '.' && result.charAt(length - 1) == '0' ? result.substring(0, length - 2) : result;
      } else {
         return !Double.isNaN(d) && !Double.isInfinite(d) ? formatNumber(d, defaultPattern, defaultFormatter) : Double.toString(d);
      }
   }

   public static int realToInt(double d) {
      return (int)d;
   }

   public static String formatNumber(double number, String pattern, DecimalFormat formatter) {
      if (formatter == null) {
         formatter = defaultFormatter;
      }

      try {
         StringBuffer result = new StringBuffer();
         if (pattern != defaultPattern) {
            formatter.applyLocalizedPattern(pattern);
         }

         formatter.format(number, result, _fieldPosition);
         return result.toString();
      } catch (IllegalArgumentException var5) {
         runTimeError("FORMAT_NUMBER_ERR", Double.toString(number), pattern);
         return "";
      }
   }

   public static DTMAxisIterator referenceToNodeSet(Object obj) {
      if (obj instanceof Node) {
         return new SingletonIterator(((Node)obj).node);
      } else if (obj instanceof DTMAxisIterator) {
         return ((DTMAxisIterator)obj).cloneIterator();
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", className, "node-set");
         return null;
      }
   }

   public static NodeList referenceToNodeList(Object obj, DOM dom) {
      if (!(obj instanceof Node) && !(obj instanceof DTMAxisIterator)) {
         if (obj instanceof DOM) {
            dom = (DOM)obj;
            return dom.makeNodeList(0);
         } else {
            String className = obj.getClass().getName();
            runTimeError("DATA_CONVERSION_ERR", className, "org.w3c.dom.NodeList");
            return null;
         }
      } else {
         DTMAxisIterator iter = referenceToNodeSet(obj);
         return dom.makeNodeList(iter);
      }
   }

   public static org.w3c.dom.Node referenceToNode(Object obj, DOM dom) {
      DTMAxisIterator iter;
      if (!(obj instanceof Node) && !(obj instanceof DTMAxisIterator)) {
         if (obj instanceof DOM) {
            dom = (DOM)obj;
            iter = dom.getChildren(0);
            return dom.makeNode(iter);
         } else {
            String className = obj.getClass().getName();
            runTimeError("DATA_CONVERSION_ERR", className, "org.w3c.dom.Node");
            return null;
         }
      } else {
         iter = referenceToNodeSet(obj);
         return dom.makeNode(iter);
      }
   }

   public static long referenceToLong(Object obj) {
      if (obj instanceof Number) {
         return ((Number)obj).longValue();
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", className, Long.TYPE);
         return 0L;
      }
   }

   public static double referenceToDouble(Object obj) {
      if (obj instanceof Number) {
         return ((Number)obj).doubleValue();
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", className, Double.TYPE);
         return 0.0;
      }
   }

   public static boolean referenceToBoolean(Object obj) {
      if (obj instanceof Boolean) {
         return (Boolean)obj;
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", className, Boolean.TYPE);
         return false;
      }
   }

   public static String referenceToString(Object obj, DOM dom) {
      if (obj instanceof String) {
         return (String)obj;
      } else if (obj instanceof DTMAxisIterator) {
         return dom.getStringValueX(((DTMAxisIterator)obj).reset().next());
      } else if (obj instanceof Node) {
         return dom.getStringValueX(((Node)obj).node);
      } else if (obj instanceof DOM) {
         return ((DOM)obj).getStringValue();
      } else {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", className, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
         return null;
      }
   }

   public static DTMAxisIterator node2Iterator(final org.w3c.dom.Node node, Translet translet, DOM dom) {
      NodeList nodelist = new NodeList() {
         public int getLength() {
            return 1;
         }

         public org.w3c.dom.Node item(int index) {
            return index == 0 ? node : null;
         }
      };
      return nodeList2Iterator(nodelist, translet, dom);
   }

   private static void copyNodes(NodeList nodeList, Document doc, org.w3c.dom.Node parent) {
      int size = nodeList.getLength();

      for(int i = 0; i < size; ++i) {
         org.w3c.dom.Node curr = nodeList.item(i);
         int nodeType = curr.getNodeType();
         String value = null;

         try {
            value = curr.getNodeValue();
         } catch (DOMException var15) {
            runTimeError("RUN_TIME_INTERNAL_ERR", (Object)var15.getMessage());
            return;
         }

         String nodeName = curr.getNodeName();
         org.w3c.dom.Node newNode = null;
         switch (nodeType) {
            case 1:
               Element element = doc.createElementNS(curr.getNamespaceURI(), nodeName);
               if (curr.hasAttributes()) {
                  NamedNodeMap attributes = curr.getAttributes();

                  for(int k = 0; k < attributes.getLength(); ++k) {
                     org.w3c.dom.Node attr = attributes.item(k);
                     element.setAttributeNS(attr.getNamespaceURI(), attr.getNodeName(), attr.getNodeValue());
                  }
               }

               copyNodes(curr.getChildNodes(), doc, element);
               newNode = element;
               break;
            case 2:
               newNode = doc.createAttributeNS(curr.getNamespaceURI(), nodeName);
               break;
            case 3:
               newNode = doc.createTextNode(value);
               break;
            case 4:
               newNode = doc.createCDATASection(value);
               break;
            case 5:
               newNode = doc.createEntityReference(nodeName);
            case 6:
            case 10:
            case 12:
            default:
               break;
            case 7:
               newNode = doc.createProcessingInstruction(nodeName, value);
               break;
            case 8:
               newNode = doc.createComment(value);
               break;
            case 9:
               newNode = doc.createElementNS((String)null, "__document__");
               copyNodes(curr.getChildNodes(), doc, (org.w3c.dom.Node)newNode);
               break;
            case 11:
               newNode = doc.createDocumentFragment();
         }

         try {
            parent.appendChild((org.w3c.dom.Node)newNode);
         } catch (DOMException var14) {
            runTimeError("RUN_TIME_INTERNAL_ERR", (Object)var14.getMessage());
            return;
         }
      }

   }

   public static DTMAxisIterator nodeList2Iterator(NodeList nodeList, Translet translet, DOM dom) {
      Document doc = null;

      try {
         doc = ((AbstractTranslet)translet).newDocument("", "__top__");
      } catch (ParserConfigurationException var12) {
         runTimeError("RUN_TIME_INTERNAL_ERR", (Object)var12.getMessage());
         return null;
      }

      copyNodes(nodeList, doc, doc.getDocumentElement());
      if (dom instanceof MultiDOM) {
         MultiDOM multiDOM = (MultiDOM)dom;
         DTMDefaultBase dtm = (DTMDefaultBase)((DOMAdapter)multiDOM.getMain()).getDOMImpl();
         DTMManager dtmManager = dtm.getManager();
         DOM idom = (DOM)dtmManager.getDTM(new DOMSource(doc), false, (DTMWSFilter)null, true, false);
         DOMAdapter domAdapter = new DOMAdapter(idom, translet.getNamesArray(), translet.getUrisArray(), translet.getTypesArray(), translet.getNamespaceArray());
         multiDOM.addDOMAdapter(domAdapter);
         DTMAxisIterator iter1 = idom.getAxisIterator(3);
         DTMAxisIterator iter2 = idom.getAxisIterator(3);
         DTMAxisIterator iter = new AbsoluteIterator(new StepIterator(iter1, iter2));
         iter.setStartNode(0);
         return iter;
      } else {
         runTimeError("RUN_TIME_INTERNAL_ERR", (Object)"nodeList2Iterator()");
         return null;
      }
   }

   public static DOM referenceToResultTree(Object obj) {
      try {
         return (DOM)obj;
      } catch (IllegalArgumentException var3) {
         String className = obj.getClass().getName();
         runTimeError("DATA_CONVERSION_ERR", "reference", className);
         return null;
      }
   }

   public static DTMAxisIterator getSingleNode(DTMAxisIterator iterator) {
      int node = iterator.next();
      return new SingletonIterator(node);
   }

   public static void copy(Object obj, SerializationHandler handler, int node, DOM dom) {
      try {
         if (obj instanceof DTMAxisIterator) {
            DTMAxisIterator iter = (DTMAxisIterator)obj;
            dom.copy(iter.reset(), handler);
         } else if (obj instanceof Node) {
            dom.copy(((Node)obj).node, handler);
         } else if (obj instanceof DOM) {
            DOM newDom = (DOM)obj;
            newDom.copy(newDom.getDocument(), handler);
         } else {
            String string = obj.toString();
            int length = string.length();
            if (length > _characterArray.length) {
               _characterArray = new char[length];
            }

            string.getChars(0, length, _characterArray, 0);
            handler.characters(_characterArray, 0, length);
         }
      } catch (SAXException var6) {
         runTimeError("RUN_TIME_COPY_ERR");
      }

   }

   public static void checkAttribQName(String name) {
      int firstOccur = name.indexOf(":");
      int lastOccur = name.lastIndexOf(":");
      String localName = name.substring(lastOccur + 1);
      if (firstOccur > 0) {
         String newPrefix = name.substring(0, firstOccur);
         if (firstOccur != lastOccur) {
            String oriPrefix = name.substring(firstOccur + 1, lastOccur);
            if (!XML11Char.isXML11ValidNCName(oriPrefix)) {
               runTimeError("INVALID_QNAME_ERR", (Object)(oriPrefix + ":" + localName));
            }
         }

         if (!XML11Char.isXML11ValidNCName(newPrefix)) {
            runTimeError("INVALID_QNAME_ERR", (Object)(newPrefix + ":" + localName));
         }
      }

      if (!XML11Char.isXML11ValidNCName(localName) || localName.equals("xmlns")) {
         runTimeError("INVALID_QNAME_ERR", (Object)localName);
      }

   }

   public static void checkNCName(String name) {
      if (!XML11Char.isXML11ValidNCName(name)) {
         runTimeError("INVALID_NCNAME_ERR", (Object)name);
      }

   }

   public static void checkQName(String name) {
      if (!XML11Char.isXML11ValidQName(name)) {
         runTimeError("INVALID_QNAME_ERR", (Object)name);
      }

   }

   public static String startXslElement(String qname, String namespace, SerializationHandler handler, DOM dom, int node) {
      try {
         int index = qname.indexOf(58);
         String prefix;
         if (index > 0) {
            prefix = qname.substring(0, index);
            if (namespace == null || namespace.length() == 0) {
               try {
                  namespace = dom.lookupNamespace(node, prefix);
               } catch (RuntimeException var9) {
                  handler.flushPending();
                  NamespaceMappings nm = handler.getNamespaceMappings();
                  namespace = nm.lookupNamespace(prefix);
                  if (namespace == null) {
                     runTimeError("NAMESPACE_PREFIX_ERR", (Object)prefix);
                  }
               }
            }

            handler.startElement(namespace, qname.substring(index + 1), qname);
            handler.namespaceAfterStartElement(prefix, namespace);
         } else if (namespace != null && namespace.length() > 0) {
            prefix = generatePrefix();
            qname = prefix + ':' + qname;
            handler.startElement(namespace, qname, qname);
            handler.namespaceAfterStartElement(prefix, namespace);
         } else {
            handler.startElement((String)null, (String)null, qname);
         }

         return qname;
      } catch (SAXException var10) {
         throw new RuntimeException(var10.getMessage());
      }
   }

   public static String getPrefix(String qname) {
      int index = qname.indexOf(58);
      return index > 0 ? qname.substring(0, index) : null;
   }

   public static String generatePrefix() {
      return "ns" + prefixIndex++;
   }

   public static void runTimeError(String code) {
      throw new RuntimeException(m_bundle.getString(code));
   }

   public static void runTimeError(String code, Object[] args) {
      String message = MessageFormat.format(m_bundle.getString(code), args);
      throw new RuntimeException(message);
   }

   public static void runTimeError(String code, Object arg0) {
      runTimeError(code, new Object[]{arg0});
   }

   public static void runTimeError(String code, Object arg0, Object arg1) {
      runTimeError(code, new Object[]{arg0, arg1});
   }

   public static void consoleOutput(String msg) {
      System.out.println(msg);
   }

   public static String replace(String base, char ch, String str) {
      return base.indexOf(ch) < 0 ? base : replace(base, String.valueOf(ch), new String[]{str});
   }

   public static String replace(String base, String delim, String[] str) {
      int len = base.length();
      StringBuffer result = new StringBuffer();

      for(int i = 0; i < len; ++i) {
         char ch = base.charAt(i);
         int k = delim.indexOf(ch);
         if (k >= 0) {
            result.append(str[k]);
         } else {
            result.append(ch);
         }
      }

      return result.toString();
   }

   public static String mapQNameToJavaName(String base) {
      return replace(base, ".-:/{}?#%*", new String[]{"$dot$", "$dash$", "$colon$", "$slash$", "", "$colon$", "$ques$", "$hash$", "$per$", "$aster$"});
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
      NumberFormat f = NumberFormat.getInstance(Locale.getDefault());
      defaultFormatter = f instanceof DecimalFormat ? (DecimalFormat)f : new DecimalFormat();
      defaultFormatter.setMaximumFractionDigits(340);
      defaultFormatter.setMinimumFractionDigits(0);
      defaultFormatter.setMinimumIntegerDigits(1);
      defaultFormatter.setGroupingUsed(false);
      _fieldPosition = new FieldPosition(0);
      _characterArray = new char[32];
      prefixIndex = 0;
      String resource = "org.apache.xalan.xsltc.runtime.ErrorMessages";
      m_bundle = ResourceBundle.getBundle(resource);
   }
}
