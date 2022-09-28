package org.apache.xalan.xsltc.dom;

import java.util.Vector;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class NodeCounter {
   public static final int END = -1;
   protected int _node = -1;
   protected int _nodeType = -1;
   protected double _value = -2.147483648E9;
   public final DOM _document;
   public final DTMAxisIterator _iterator;
   public final Translet _translet;
   protected String _format;
   protected String _lang;
   protected String _letterValue;
   protected String _groupSep;
   protected int _groupSize;
   private boolean _separFirst = true;
   private boolean _separLast = false;
   private Vector _separToks = new Vector();
   private Vector _formatToks = new Vector();
   private int _nSepars = 0;
   private int _nFormats = 0;
   private static final String[] Thousands = new String[]{"", "m", "mm", "mmm"};
   private static final String[] Hundreds = new String[]{"", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm"};
   private static final String[] Tens = new String[]{"", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc"};
   private static final String[] Ones = new String[]{"", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix"};
   private StringBuffer _tempBuffer = new StringBuffer();

   protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      this._translet = translet;
      this._document = document;
      this._iterator = iterator;
   }

   public abstract NodeCounter setStartNode(int var1);

   public NodeCounter setValue(double value) {
      this._value = value;
      return this;
   }

   protected void setFormatting(String format, String lang, String letterValue, String groupSep, String groupSize) {
      this._lang = lang;
      this._groupSep = groupSep;
      this._letterValue = letterValue;

      try {
         this._groupSize = Integer.parseInt(groupSize);
      } catch (NumberFormatException var7) {
         this._groupSize = 0;
      }

      this.setTokens(format);
   }

   private final void setTokens(String format) {
      if (this._format == null || !format.equals(this._format)) {
         this._format = format;
         int length = this._format.length();
         boolean isFirst = true;
         this._separFirst = true;
         this._separLast = false;
         this._nSepars = 0;
         this._nFormats = 0;
         this._separToks.clear();
         this._formatToks.clear();
         int j = false;
         int i = 0;

         while(i < length) {
            char c = format.charAt(i);

            int j;
            for(j = i; Character.isLetterOrDigit(c); c = format.charAt(i)) {
               ++i;
               if (i == length) {
                  break;
               }
            }

            if (i > j) {
               if (isFirst) {
                  this._separToks.addElement(".");
                  isFirst = this._separFirst = false;
               }

               this._formatToks.addElement(format.substring(j, i));
            }

            if (i == length) {
               break;
            }

            c = format.charAt(i);

            for(j = i; !Character.isLetterOrDigit(c); isFirst = false) {
               ++i;
               if (i == length) {
                  break;
               }

               c = format.charAt(i);
            }

            if (i > j) {
               this._separToks.addElement(format.substring(j, i));
            }
         }

         this._nSepars = this._separToks.size();
         this._nFormats = this._formatToks.size();
         if (this._nSepars > this._nFormats) {
            this._separLast = true;
         }

         if (this._separFirst) {
            --this._nSepars;
         }

         if (this._separLast) {
            --this._nSepars;
         }

         if (this._nSepars == 0) {
            this._separToks.insertElementAt(".", 1);
            ++this._nSepars;
         }

         if (this._separFirst) {
            ++this._nSepars;
         }

      }
   }

   public NodeCounter setDefaultFormatting() {
      this.setFormatting("1", "en", "alphabetic", (String)null, (String)null);
      return this;
   }

   public abstract String getCounter();

   public String getCounter(String format, String lang, String letterValue, String groupSep, String groupSize) {
      this.setFormatting(format, lang, letterValue, groupSep, groupSize);
      return this.getCounter();
   }

   public boolean matchesCount(int node) {
      return this._nodeType == this._document.getExpandedTypeID(node);
   }

   public boolean matchesFrom(int node) {
      return false;
   }

   protected String formatNumbers(int value) {
      return this.formatNumbers(new int[]{value});
   }

   protected String formatNumbers(int[] values) {
      int nValues = values.length;
      int length = this._format.length();
      boolean isEmpty = true;

      for(int i = 0; i < nValues; ++i) {
         if (values[i] != Integer.MIN_VALUE) {
            isEmpty = false;
         }
      }

      if (isEmpty) {
         return "";
      } else {
         boolean isFirst = true;
         int t = 0;
         int n = 0;
         int s = 1;
         this._tempBuffer.setLength(0);
         StringBuffer buffer = this._tempBuffer;
         if (this._separFirst) {
            buffer.append((String)this._separToks.elementAt(0));
         }

         for(; n < nValues; ++n) {
            int value = values[n];
            if (value != Integer.MIN_VALUE) {
               if (!isFirst) {
                  buffer.append((String)this._separToks.elementAt(s++));
               }

               this.formatValue(value, (String)this._formatToks.elementAt(t++), buffer);
               if (t == this._nFormats) {
                  --t;
               }

               if (s >= this._nSepars) {
                  --s;
               }

               isFirst = false;
            }
         }

         if (this._separLast) {
            buffer.append((String)this._separToks.lastElement());
         }

         return buffer.toString();
      }
   }

   private void formatValue(int value, String format, StringBuffer buffer) {
      char c = format.charAt(0);
      if (Character.isDigit(c)) {
         char zero = (char)(c - Character.getNumericValue(c));
         StringBuffer temp = buffer;
         if (this._groupSize > 0) {
            temp = new StringBuffer();
         }

         String s = "";

         for(int n = value; n > 0; n /= 10) {
            s = (char)(zero + n % 10) + s;
         }

         for(int i = 0; i < format.length() - s.length(); ++i) {
            temp.append(zero);
         }

         temp.append(s);
         if (this._groupSize > 0) {
            for(int i = 0; i < temp.length(); ++i) {
               if (i != 0 && (temp.length() - i) % this._groupSize == 0) {
                  buffer.append(this._groupSep);
               }

               buffer.append(temp.charAt(i));
            }
         }
      } else if (c == 'i' && !this._letterValue.equals("alphabetic")) {
         buffer.append(this.romanValue(value));
      } else if (c == 'I' && !this._letterValue.equals("alphabetic")) {
         buffer.append(this.romanValue(value).toUpperCase());
      } else {
         int max = c;
         if (c >= 945 && c <= 969) {
            max = 969;
         } else {
            while(Character.isLetterOrDigit((char)(max + 1))) {
               ++max;
            }
         }

         buffer.append(this.alphaValue(value, c, max));
      }

   }

   private String alphaValue(int value, int min, int max) {
      if (value <= 0) {
         return "" + value;
      } else {
         int range = max - min + 1;
         char last = (char)((value - 1) % range + min);
         return value > range ? this.alphaValue((value - 1) / range, min, max) + last : "" + last;
      }
   }

   private String romanValue(int n) {
      return n > 0 && n <= 4000 ? Thousands[n / 1000] + Hundreds[n / 100 % 10] + Tens[n / 10 % 10] + Ones[n % 10] : "" + n;
   }
}
