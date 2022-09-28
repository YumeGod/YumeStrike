package org.apache.xalan.xsltc.runtime;

import org.apache.xml.serializer.EmptySerializer;
import org.xml.sax.SAXException;

public final class StringValueHandler extends EmptySerializer {
   private StringBuffer _buffer = new StringBuffer();
   private String _str = null;
   private static final String EMPTY_STR = "";
   private boolean m_escaping = false;
   private int _nestedLevel = 0;

   public void characters(char[] ch, int off, int len) throws SAXException {
      if (this._nestedLevel <= 0) {
         if (this._str != null) {
            this._buffer.append(this._str);
            this._str = null;
         }

         this._buffer.append(ch, off, len);
      }
   }

   public String getValue() {
      String result;
      if (this._buffer.length() != 0) {
         result = this._buffer.toString();
         this._buffer.setLength(0);
         return result;
      } else {
         result = this._str;
         this._str = null;
         return result != null ? result : "";
      }
   }

   public void characters(String characters) throws SAXException {
      if (this._nestedLevel <= 0) {
         if (this._str == null && this._buffer.length() == 0) {
            this._str = characters;
         } else {
            if (this._str != null) {
               this._buffer.append(this._str);
               this._str = null;
            }

            this._buffer.append(characters);
         }

      }
   }

   public void startElement(String qname) throws SAXException {
      ++this._nestedLevel;
   }

   public void endElement(String qname) throws SAXException {
      --this._nestedLevel;
   }

   public boolean setEscaping(boolean bool) {
      boolean oldEscaping = this.m_escaping;
      this.m_escaping = bool;
      return bool;
   }

   public String getValueOfPI() {
      String value = this.getValue();
      if (value.indexOf("?>") <= 0) {
         return value;
      } else {
         int n = value.length();
         StringBuffer valueOfPI = new StringBuffer();
         int i = 0;

         while(true) {
            while(i < n) {
               char ch = value.charAt(i++);
               if (ch == '?' && value.charAt(i) == '>') {
                  valueOfPI.append("? >");
                  ++i;
               } else {
                  valueOfPI.append(ch);
               }
            }

            return valueOfPI.toString();
         }
      }
   }
}
