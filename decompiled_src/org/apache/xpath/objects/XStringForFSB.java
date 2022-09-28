package org.apache.xpath.objects;

import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.res.XPATHMessages;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XStringForFSB extends XString {
   static final long serialVersionUID = -1533039186550674548L;
   int m_start;
   int m_length;
   protected String m_strCache = null;
   protected int m_hash = 0;

   public XStringForFSB(FastStringBuffer val, int start, int length) {
      super((Object)val);
      this.m_start = start;
      this.m_length = length;
      if (null == val) {
         throw new IllegalArgumentException(XPATHMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", (Object[])null));
      }
   }

   private XStringForFSB(String val) {
      super(val);
      throw new IllegalArgumentException(XPATHMessages.createXPATHMessage("ER_FSB_CANNOT_TAKE_STRING", (Object[])null));
   }

   public FastStringBuffer fsb() {
      return (FastStringBuffer)super.m_obj;
   }

   public void appendToFsb(FastStringBuffer fsb) {
      fsb.append(this.str());
   }

   public boolean hasString() {
      return null != this.m_strCache;
   }

   public Object object() {
      return this.str();
   }

   public String str() {
      if (null == this.m_strCache) {
         this.m_strCache = this.fsb().getString(this.m_start, this.m_length);
      }

      return this.m_strCache;
   }

   public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
      this.fsb().sendSAXcharacters(ch, this.m_start, this.m_length);
   }

   public void dispatchAsComment(LexicalHandler lh) throws SAXException {
      this.fsb().sendSAXComment(lh, this.m_start, this.m_length);
   }

   public int length() {
      return this.m_length;
   }

   public char charAt(int index) {
      return this.fsb().charAt(this.m_start + index);
   }

   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
      int n = srcEnd - srcBegin;
      if (n > this.m_length) {
         n = this.m_length;
      }

      if (n > dst.length - dstBegin) {
         n = dst.length - dstBegin;
      }

      int end = srcBegin + this.m_start + n;
      int d = dstBegin;
      FastStringBuffer fsb = this.fsb();

      for(int i = srcBegin + this.m_start; i < end; ++i) {
         dst[d++] = fsb.charAt(i);
      }

   }

   public boolean equals(XMLString obj2) {
      if (this == obj2) {
         return true;
      } else {
         int n = this.m_length;
         if (n != obj2.length()) {
            return false;
         } else {
            FastStringBuffer fsb = this.fsb();
            int i = this.m_start;

            for(int j = 0; n-- != 0; ++j) {
               if (fsb.charAt(i) != obj2.charAt(j)) {
                  return false;
               }

               ++i;
            }

            return true;
         }
      }
   }

   public boolean equals(XObject obj2) {
      if (this == obj2) {
         return true;
      } else if (obj2.getType() == 2) {
         return obj2.equals(this);
      } else {
         String str = obj2.str();
         int n = this.m_length;
         if (n != str.length()) {
            return false;
         } else {
            FastStringBuffer fsb = this.fsb();
            int i = this.m_start;

            for(int j = 0; n-- != 0; ++j) {
               if (fsb.charAt(i) != str.charAt(j)) {
                  return false;
               }

               ++i;
            }

            return true;
         }
      }
   }

   public boolean equals(String anotherString) {
      int n = this.m_length;
      if (n != anotherString.length()) {
         return false;
      } else {
         FastStringBuffer fsb = this.fsb();
         int i = this.m_start;

         for(int j = 0; n-- != 0; ++j) {
            if (fsb.charAt(i) != anotherString.charAt(j)) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }

   public boolean equals(Object obj2) {
      if (null == obj2) {
         return false;
      } else if (obj2 instanceof XNumber) {
         return obj2.equals(this);
      } else if (obj2 instanceof XNodeSet) {
         return obj2.equals(this);
      } else {
         return obj2 instanceof XStringForFSB ? this.equals((XMLString)obj2) : this.equals(obj2.toString());
      }
   }

   public boolean equalsIgnoreCase(String anotherString) {
      return this.m_length == anotherString.length() ? this.str().equalsIgnoreCase(anotherString) : false;
   }

   public int compareTo(XMLString xstr) {
      int len1 = this.m_length;
      int len2 = xstr.length();
      int n = Math.min(len1, len2);
      FastStringBuffer fsb = this.fsb();
      int i = this.m_start;

      for(int j = 0; n-- != 0; ++j) {
         char c1 = fsb.charAt(i);
         char c2 = xstr.charAt(j);
         if (c1 != c2) {
            return c1 - c2;
         }

         ++i;
      }

      return len1 - len2;
   }

   public int compareToIgnoreCase(XMLString xstr) {
      int len1 = this.m_length;
      int len2 = xstr.length();
      int n = Math.min(len1, len2);
      FastStringBuffer fsb = this.fsb();
      int i = this.m_start;

      for(int j = 0; n-- != 0; ++j) {
         char c1 = Character.toLowerCase(fsb.charAt(i));
         char c2 = Character.toLowerCase(xstr.charAt(j));
         if (c1 != c2) {
            return c1 - c2;
         }

         ++i;
      }

      return len1 - len2;
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean startsWith(XMLString prefix, int toffset) {
      FastStringBuffer fsb = this.fsb();
      int to = this.m_start + toffset;
      int var10000 = this.m_start + this.m_length;
      int po = 0;
      int pc = prefix.length();
      if (toffset >= 0 && toffset <= this.m_length - pc) {
         while(true) {
            --pc;
            if (pc < 0) {
               return true;
            }

            if (fsb.charAt(to) != prefix.charAt(po)) {
               return false;
            }

            ++to;
            ++po;
         }
      } else {
         return false;
      }
   }

   public boolean startsWith(XMLString prefix) {
      return this.startsWith(prefix, 0);
   }

   public int indexOf(int ch) {
      return this.indexOf(ch, 0);
   }

   public int indexOf(int ch, int fromIndex) {
      int max = this.m_start + this.m_length;
      FastStringBuffer fsb = this.fsb();
      if (fromIndex < 0) {
         fromIndex = 0;
      } else if (fromIndex >= this.m_length) {
         return -1;
      }

      for(int i = this.m_start + fromIndex; i < max; ++i) {
         if (fsb.charAt(i) == ch) {
            return i - this.m_start;
         }
      }

      return -1;
   }

   public XMLString substring(int beginIndex) {
      int len = this.m_length - beginIndex;
      if (len <= 0) {
         return XString.EMPTYSTRING;
      } else {
         int start = this.m_start + beginIndex;
         return new XStringForFSB(this.fsb(), start, len);
      }
   }

   public XMLString substring(int beginIndex, int endIndex) {
      int len = endIndex - beginIndex;
      if (len > this.m_length) {
         len = this.m_length;
      }

      if (len <= 0) {
         return XString.EMPTYSTRING;
      } else {
         int start = this.m_start + beginIndex;
         return new XStringForFSB(this.fsb(), start, len);
      }
   }

   public XMLString concat(String str) {
      return new XString(this.str().concat(str));
   }

   public XMLString trim() {
      return this.fixWhiteSpace(true, true, false);
   }

   private static boolean isSpace(char ch) {
      return XMLCharacterRecognizer.isWhiteSpace(ch);
   }

   public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces) {
      int end = this.m_length + this.m_start;
      char[] buf = new char[this.m_length];
      FastStringBuffer fsb = this.fsb();
      boolean edit = false;
      int d = 0;
      boolean pres = false;

      int c;
      for(int s = this.m_start; s < end; ++s) {
         c = fsb.charAt(s);
         if (isSpace((char)c)) {
            if (!pres) {
               if (32 != c) {
                  edit = true;
               }

               buf[d++] = ' ';
               if (doublePunctuationSpaces && d != 0) {
                  char prevChar = buf[d - 1];
                  if (prevChar != '.' && prevChar != '!' && prevChar != '?') {
                     pres = true;
                  }
               } else {
                  pres = true;
               }
            } else {
               edit = true;
               pres = true;
            }
         } else {
            buf[d++] = (char)c;
            pres = false;
         }
      }

      if (trimTail && 1 <= d && ' ' == buf[d - 1]) {
         edit = true;
         --d;
      }

      c = 0;
      if (trimHead && 0 < d && ' ' == buf[0]) {
         edit = true;
         ++c;
      }

      XMLStringFactory xsf = XMLStringFactoryImpl.getFactory();
      return (XMLString)(edit ? xsf.newstr(buf, c, d - c) : this);
   }

   public double toDouble() {
      if (this.m_length == 0) {
         return Double.NaN;
      } else {
         String valueString = this.fsb().getString(this.m_start, this.m_length);

         int i;
         for(i = 0; i < this.m_length && XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i)); ++i) {
         }

         if (i == this.m_length) {
            return Double.NaN;
         } else {
            if (valueString.charAt(i) == '-') {
               ++i;
            }

            while(i < this.m_length) {
               char c = valueString.charAt(i);
               if (c != '.' && (c < '0' || c > '9')) {
                  break;
               }

               ++i;
            }

            while(i < this.m_length && XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i))) {
               ++i;
            }

            if (i != this.m_length) {
               return Double.NaN;
            } else {
               try {
                  return new Double(valueString);
               } catch (NumberFormatException var5) {
                  return Double.NaN;
               }
            }
         }
      }
   }
}
