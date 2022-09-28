package org.apache.xmlgraphics.xmp;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMPArray extends XMPComplexValue {
   private XMPArrayType type;
   private List values = new ArrayList();
   private List xmllang = new ArrayList();

   public XMPArray(XMPArrayType type) {
      this.type = type;
   }

   public XMPArrayType getType() {
      return this.type;
   }

   public Object getValue(int idx) {
      return this.values.get(idx);
   }

   public XMPStructure getStructure(int idx) {
      return (XMPStructure)this.values.get(idx);
   }

   public Object getSimpleValue() {
      if (this.values.size() == 1) {
         return this.getValue(0);
      } else {
         return this.values.size() > 1 ? this.getLangValue("x-default") : null;
      }
   }

   private String getParentLanguage(String lang) {
      if (lang == null) {
         return null;
      } else {
         int pos = lang.indexOf(45);
         if (pos > 0) {
            String parent = lang.substring(0, pos);
            return parent;
         } else {
            return null;
         }
      }
   }

   public String getLangValue(String lang) {
      String v = null;
      String valueForParentLanguage = null;
      int i = 0;

      for(int c = this.values.size(); i < c; ++i) {
         String l = (String)this.xmllang.get(i);
         if (l == null && lang == null || l != null && l.equals(lang)) {
            v = this.values.get(i).toString();
            break;
         }

         if (l != null && lang != null) {
            String parent = this.getParentLanguage(l);
            if (parent != null && parent.equals(lang)) {
               valueForParentLanguage = this.values.get(i).toString();
            }
         }
      }

      if (lang != null & v == null && valueForParentLanguage != null) {
         v = valueForParentLanguage;
      }

      if (lang == null && v == null) {
         v = this.getLangValue("x-default");
         if (v == null && this.values.size() > 0) {
            v = this.getValue(0).toString();
         }
      }

      return v;
   }

   public String removeLangValue(String lang) {
      if (lang == null || "".equals(lang)) {
         lang = "x-default";
      }

      int i = 0;

      for(int c = this.values.size(); i < c; ++i) {
         String l = (String)this.xmllang.get(i);
         if ("x-default".equals(lang) && l == null || lang.equals(l)) {
            String value = (String)this.values.remove(i);
            this.xmllang.remove(i);
            return value;
         }
      }

      return null;
   }

   public void add(Object value) {
      this.values.add(value);
      this.xmllang.add((Object)null);
   }

   public boolean remove(String value) {
      int idx = this.values.indexOf(value);
      if (idx >= 0) {
         this.values.remove(idx);
         this.xmllang.remove(idx);
         return true;
      } else {
         return false;
      }
   }

   public void add(String value, String lang) {
      this.values.add(value);
      this.xmllang.add(lang);
   }

   public int getSize() {
      return this.values.size();
   }

   public boolean isEmpty() {
      return this.getSize() == 0;
   }

   public Object[] toObjectArray() {
      Object[] res = new Object[this.getSize()];
      int i = 0;

      for(int c = res.length; i < c; ++i) {
         res[i] = this.getValue(i);
      }

      return res;
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      handler.startElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", this.type.getName(), "rdf:" + this.type.getName(), atts);
      int i = 0;

      for(int c = this.values.size(); i < c; ++i) {
         String lang = (String)this.xmllang.get(i);
         atts.clear();
         if (lang != null) {
            atts.addAttribute("http://www.w3.org/XML/1998/namespace", "lang", "xml:lang", "CDATA", lang);
         }

         handler.startElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "li", "rdf:li", atts);
         Object v = this.values.get(i);
         if (v instanceof XMPComplexValue) {
            ((XMPComplexValue)v).toSAX(handler);
         } else {
            String value = (String)this.values.get(i);
            char[] chars = value.toCharArray();
            handler.characters(chars, 0, chars.length);
         }

         handler.endElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "li", "rdf:li");
      }

      handler.endElement("http://www.w3.org/1999/02/22-rdf-syntax-ns#", this.type.getName(), "rdf:" + this.type.getName());
   }

   public String toString() {
      return "XMP array: " + this.type + ", " + this.getSize();
   }
}
