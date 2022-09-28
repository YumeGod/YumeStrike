package org.apache.xalan.extensions;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.apache.xalan.res.XSLMessages;

public class ExtensionNamespaceContext implements NamespaceContext {
   public static final String EXSLT_PREFIX = "exslt";
   public static final String EXSLT_URI = "http://exslt.org/common";
   public static final String EXSLT_MATH_PREFIX = "math";
   public static final String EXSLT_MATH_URI = "http://exslt.org/math";
   public static final String EXSLT_SET_PREFIX = "set";
   public static final String EXSLT_SET_URI = "http://exslt.org/sets";
   public static final String EXSLT_STRING_PREFIX = "str";
   public static final String EXSLT_STRING_URI = "http://exslt.org/strings";
   public static final String EXSLT_DATETIME_PREFIX = "datetime";
   public static final String EXSLT_DATETIME_URI = "http://exslt.org/dates-and-times";
   public static final String EXSLT_DYNAMIC_PREFIX = "dyn";
   public static final String EXSLT_DYNAMIC_URI = "http://exslt.org/dynamic";
   public static final String JAVA_EXT_PREFIX = "java";
   public static final String JAVA_EXT_URI = "http://xml.apache.org/xalan/java";

   public String getNamespaceURI(String prefix) {
      if (prefix == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_PREFIX", (Object[])null));
      } else if (prefix.equals("")) {
         return "";
      } else if (prefix.equals("xml")) {
         return "http://www.w3.org/XML/1998/namespace";
      } else if (prefix.equals("xmlns")) {
         return "http://www.w3.org/2000/xmlns/";
      } else if (prefix.equals("exslt")) {
         return "http://exslt.org/common";
      } else if (prefix.equals("math")) {
         return "http://exslt.org/math";
      } else if (prefix.equals("set")) {
         return "http://exslt.org/sets";
      } else if (prefix.equals("str")) {
         return "http://exslt.org/strings";
      } else if (prefix.equals("datetime")) {
         return "http://exslt.org/dates-and-times";
      } else if (prefix.equals("dyn")) {
         return "http://exslt.org/dynamic";
      } else {
         return prefix.equals("java") ? "http://xml.apache.org/xalan/java" : "";
      }
   }

   public String getPrefix(String namespace) {
      if (namespace == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_NAMESPACE", (Object[])null));
      } else if (namespace.equals("http://www.w3.org/XML/1998/namespace")) {
         return "xml";
      } else if (namespace.equals("http://www.w3.org/2000/xmlns/")) {
         return "xmlns";
      } else if (namespace.equals("http://exslt.org/common")) {
         return "exslt";
      } else if (namespace.equals("http://exslt.org/math")) {
         return "math";
      } else if (namespace.equals("http://exslt.org/sets")) {
         return "set";
      } else if (namespace.equals("http://exslt.org/strings")) {
         return "str";
      } else if (namespace.equals("http://exslt.org/dates-and-times")) {
         return "datetime";
      } else if (namespace.equals("http://exslt.org/dynamic")) {
         return "dyn";
      } else {
         return namespace.equals("http://xml.apache.org/xalan/java") ? "java" : null;
      }
   }

   public Iterator getPrefixes(String namespace) {
      final String result = this.getPrefix(namespace);
      return new Iterator() {
         private boolean isFirstIteration = result != null;

         public boolean hasNext() {
            return this.isFirstIteration;
         }

         public Object next() {
            if (this.isFirstIteration) {
               this.isFirstIteration = false;
               return result;
            } else {
               return null;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
