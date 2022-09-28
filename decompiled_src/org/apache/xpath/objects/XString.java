package org.apache.xpath.objects;

import java.util.Locale;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class XString extends XObject implements XMLString {
   static final long serialVersionUID = 2020470518395094525L;
   public static final XString EMPTYSTRING = new XString("");

   protected XString(Object val) {
      super(val);
   }

   public XString(String val) {
      super(val);
   }

   public int getType() {
      return 3;
   }

   public String getTypeString() {
      return "#STRING";
   }

   public boolean hasString() {
      return true;
   }

   public double num() {
      return this.toDouble();
   }

   public double toDouble() {
      XMLString s = this.trim();
      double result = Double.NaN;

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c != '-' && c != '.' && (c < '0' || c > '9')) {
            return result;
         }
      }

      try {
         result = Double.parseDouble(s.toString());
      } catch (NumberFormatException var6) {
      }

      return result;
   }

   public boolean bool() {
      return this.str().length() > 0;
   }

   public XMLString xstr() {
      return this;
   }

   public String str() {
      return null != super.m_obj ? (String)super.m_obj : "";
   }

   public int rtf(XPathContext support) {
      DTM frag = support.createDocumentFragment();
      frag.appendTextChild(this.str());
      return frag.getDocument();
   }

   public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
      String str = this.str();
      ch.characters(str.toCharArray(), 0, str.length());
   }

   public void dispatchAsComment(LexicalHandler lh) throws SAXException {
      String str = this.str();
      lh.comment(str.toCharArray(), 0, str.length());
   }

   public int length() {
      return this.str().length();
   }

   public char charAt(int index) {
      return this.str().charAt(index);
   }

   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
      this.str().getChars(srcBegin, srcEnd, dst, dstBegin);
   }

   public boolean equals(XObject obj2) {
      int t = obj2.getType();

      try {
         if (4 == t) {
            return obj2.equals(this);
         }

         if (1 == t) {
            return obj2.bool() == this.bool();
         }

         if (2 == t) {
            return obj2.num() == this.num();
         }
      } catch (TransformerException var4) {
         throw new WrappedRuntimeException(var4);
      }

      return this.xstr().equals(obj2.xstr());
   }

   public boolean equals(XMLString obj2) {
      return !obj2.hasString() ? obj2.equals((XMLString)this) : this.str().equals(obj2.toString());
   }

   public boolean equals(Object obj2) {
      if (null == obj2) {
         return false;
      } else if (obj2 instanceof XNodeSet) {
         return obj2.equals(this);
      } else {
         return obj2 instanceof XNumber ? obj2.equals(this) : this.str().equals(obj2.toString());
      }
   }

   public boolean equalsIgnoreCase(String anotherString) {
      return this.str().equalsIgnoreCase(anotherString);
   }

   public int compareTo(XMLString xstr) {
      int len1 = this.length();
      int len2 = xstr.length();
      int n = Math.min(len1, len2);
      int i = 0;

      for(int j = 0; n-- != 0; ++j) {
         char c1 = this.charAt(i);
         char c2 = xstr.charAt(j);
         if (c1 != c2) {
            return c1 - c2;
         }

         ++i;
      }

      return len1 - len2;
   }

   public int compareToIgnoreCase(XMLString str) {
      throw new WrappedRuntimeException(new NoSuchMethodException("Java 1.2 method, not yet implemented"));
   }

   public boolean startsWith(String prefix, int toffset) {
      return this.str().startsWith(prefix, toffset);
   }

   public boolean startsWith(String prefix) {
      return this.startsWith((String)prefix, 0);
   }

   public boolean startsWith(XMLString prefix, int toffset) {
      int to = toffset;
      int tlim = this.length();
      int po = 0;
      int pc = prefix.length();
      if (toffset >= 0 && toffset <= tlim - pc) {
         while(true) {
            --pc;
            if (pc < 0) {
               return true;
            }

            if (this.charAt(to) != prefix.charAt(po)) {
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
      return this.startsWith((XMLString)prefix, 0);
   }

   public boolean endsWith(String suffix) {
      return this.str().endsWith(suffix);
   }

   public int hashCode() {
      return this.str().hashCode();
   }

   public int indexOf(int ch) {
      return this.str().indexOf(ch);
   }

   public int indexOf(int ch, int fromIndex) {
      return this.str().indexOf(ch, fromIndex);
   }

   public int lastIndexOf(int ch) {
      return this.str().lastIndexOf(ch);
   }

   public int lastIndexOf(int ch, int fromIndex) {
      return this.str().lastIndexOf(ch, fromIndex);
   }

   public int indexOf(String str) {
      return this.str().indexOf(str);
   }

   public int indexOf(XMLString str) {
      return this.str().indexOf(str.toString());
   }

   public int indexOf(String str, int fromIndex) {
      return this.str().indexOf(str, fromIndex);
   }

   public int lastIndexOf(String str) {
      return this.str().lastIndexOf(str);
   }

   public int lastIndexOf(String str, int fromIndex) {
      return this.str().lastIndexOf(str, fromIndex);
   }

   public XMLString substring(int beginIndex) {
      return new XString(this.str().substring(beginIndex));
   }

   public XMLString substring(int beginIndex, int endIndex) {
      return new XString(this.str().substring(beginIndex, endIndex));
   }

   public XMLString concat(String str) {
      return new XString(this.str().concat(str));
   }

   public XMLString toLowerCase(Locale locale) {
      return new XString(this.str().toLowerCase(locale));
   }

   public XMLString toLowerCase() {
      return new XString(this.str().toLowerCase());
   }

   public XMLString toUpperCase(Locale locale) {
      return new XString(this.str().toUpperCase(locale));
   }

   public XMLString toUpperCase() {
      return new XString(this.str().toUpperCase());
   }

   public XMLString trim() {
      return new XString(this.str().trim());
   }

   private static boolean isSpace(char ch) {
      return XMLCharacterRecognizer.isWhiteSpace(ch);
   }

   public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces) {
      int len = this.length();
      char[] buf = new char[len];
      this.getChars(0, len, buf, 0);
      boolean edit = false;

      int s;
      for(s = 0; s < len && !isSpace(buf[s]); ++s) {
      }

      int d = s;

      int c;
      for(boolean pres = false; s < len; ++s) {
         c = buf[s];
         if (isSpace((char)c)) {
            if (!pres) {
               if (32 != c) {
                  edit = true;
               }

               buf[d++] = ' ';
               if (doublePunctuationSpaces && s != 0) {
                  char prevChar = buf[s - 1];
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
      return (XMLString)(edit ? xsf.newstr(new String(buf, c, d - c)) : this);
   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      visitor.visitStringLiteral(owner, this);
   }
}
