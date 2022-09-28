package org.apache.xml.serializer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class NamespaceMappings {
   private int count = 0;
   private Hashtable m_namespaces = new Hashtable();
   private Stack m_nodeStack = new Stack();
   private static final String EMPTYSTRING = "";
   private static final String XML_PREFIX = "xml";

   public NamespaceMappings() {
      this.initNamespaces();
   }

   private void initNamespaces() {
      Stack stack;
      this.m_namespaces.put("", stack = new Stack());
      stack.push(new MappingRecord("", "", 0));
      this.m_namespaces.put("xml", stack = new Stack());
      stack.push(new MappingRecord("xml", "http://www.w3.org/XML/1998/namespace", 0));
      this.m_nodeStack.push(new MappingRecord((String)null, (String)null, -1));
   }

   public String lookupNamespace(String prefix) {
      Stack stack = (Stack)this.m_namespaces.get(prefix);
      return stack != null && !stack.isEmpty() ? ((MappingRecord)stack.peek()).m_uri : null;
   }

   MappingRecord getMappingFromPrefix(String prefix) {
      Stack stack = (Stack)this.m_namespaces.get(prefix);
      return stack != null && !stack.isEmpty() ? (MappingRecord)stack.peek() : null;
   }

   public String lookupPrefix(String uri) {
      String foundPrefix = null;
      Enumeration prefixes = this.m_namespaces.keys();

      while(prefixes.hasMoreElements()) {
         String prefix = (String)prefixes.nextElement();
         String uri2 = this.lookupNamespace(prefix);
         if (uri2 != null && uri2.equals(uri)) {
            foundPrefix = prefix;
            break;
         }
      }

      return foundPrefix;
   }

   MappingRecord getMappingFromURI(String uri) {
      MappingRecord foundMap = null;
      Enumeration prefixes = this.m_namespaces.keys();

      while(prefixes.hasMoreElements()) {
         String prefix = (String)prefixes.nextElement();
         MappingRecord map2 = this.getMappingFromPrefix(prefix);
         if (map2 != null && map2.m_uri.equals(uri)) {
            foundMap = map2;
            break;
         }
      }

      return foundMap;
   }

   boolean popNamespace(String prefix) {
      if (prefix.startsWith("xml")) {
         return false;
      } else {
         Stack stack;
         if ((stack = (Stack)this.m_namespaces.get(prefix)) != null) {
            stack.pop();
            return true;
         } else {
            return false;
         }
      }
   }

   boolean pushNamespace(String prefix, String uri, int elemDepth) {
      if (prefix.startsWith("xml")) {
         return false;
      } else {
         Stack stack;
         if ((stack = (Stack)this.m_namespaces.get(prefix)) == null) {
            this.m_namespaces.put(prefix, stack = new Stack());
         }

         if (!stack.empty() && uri.equals(((MappingRecord)stack.peek()).m_uri)) {
            return false;
         } else {
            MappingRecord map = new MappingRecord(prefix, uri, elemDepth);
            stack.push(map);
            this.m_nodeStack.push(map);
            return true;
         }
      }
   }

   void popNamespaces(int elemDepth, ContentHandler saxHandler) {
      while(!this.m_nodeStack.isEmpty()) {
         MappingRecord map = (MappingRecord)this.m_nodeStack.peek();
         int depth = map.m_declarationDepth;
         if (depth < elemDepth) {
            return;
         }

         map = (MappingRecord)this.m_nodeStack.pop();
         String prefix = map.m_prefix;
         this.popNamespace(prefix);
         if (saxHandler != null) {
            try {
               saxHandler.endPrefixMapping(prefix);
            } catch (SAXException var7) {
            }
         }
      }

   }

   public String generateNextPrefix() {
      return "ns" + this.count++;
   }

   public Object clone() throws CloneNotSupportedException {
      NamespaceMappings clone = new NamespaceMappings();
      clone.m_nodeStack = (Stack)this.m_nodeStack.clone();
      clone.m_namespaces = (Hashtable)this.m_namespaces.clone();
      clone.count = this.count;
      return clone;
   }

   final void reset() {
      this.count = 0;
      this.m_namespaces.clear();
      this.m_nodeStack.clear();
      this.initNamespaces();
   }

   class MappingRecord {
      final String m_prefix;
      final String m_uri;
      final int m_declarationDepth;

      MappingRecord(String prefix, String uri, int depth) {
         this.m_prefix = prefix;
         this.m_uri = uri;
         this.m_declarationDepth = depth;
      }
   }
}
